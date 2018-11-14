package com.megvii.dzh.spider.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

/**
 *
 */
@Slf4j
public class LogUtil {

  private static final String miwen = "*******";
  private static final String pwd1 = "pwd";
  private static final String pwd2 = "password";


  public static String jsonFormatter(String uglyJSONString){
    String result=null;
    try {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      JsonParser jp = new JsonParser();
      JsonElement je = jp.parse(uglyJSONString);
      result = gson.toJson(je);
    }catch (Exception e){
      log.error("jsonFormatter error {}",e);
    }
    return result;
  }

  /**
   * 在日志中过滤敏感信息
   *
   * @Description
   */
  public static String formatLog(Object obj) {
    String jsonStr = JSONObject.toJSONString(obj);
    JSONObject objCopy = JSONObject.parseObject(jsonStr);
    if (obj == null) {
      return "object is null";
    }
    List<Field> fields = getFieldListByClass(obj.getClass());
    for (Field field : fields) {
      // 抑制Java对其的检查
      field.setAccessible(true);
      hideAn(objCopy, field, obj);//敏感信息隐藏

      boolean removeImg = hasAnnotation(field);
      try {
        Object object = field.get(obj);
        if (!removeImg && (object instanceof List || object instanceof Map || object instanceof Set)) {
          handlerType(object, objCopy, field.getName());
        } else if (removeImg) {
          // 有注解
          hideImg(objCopy, field, obj);//照片信息隐藏
        }

      } catch (IllegalArgumentException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return JSONObject.toJSONString(objCopy);
  }

  private static boolean hasAnnotation(Field field) {
    boolean b = field.getAnnotation(HideImg.class) != null;
    return b;
  }

  /**
   * 敏感信息隐藏
   *
   * @Description
   */
  public static void hideAn(JSONObject objCopy, Field field, Object obj) {
    HideAnn hideAn = field.getAnnotation(HideAnn.class);
    if (!ObjectUtils.isEmpty(hideAn)) {
      if (field.getType() == String.class) {
        objCopy.put(field.getName(), miwen);
      }
    } else {
      if (field.getName().toLowerCase().contains(pwd1) || field.getName().toLowerCase().contains(pwd2)) {
        objCopy.put(field.getName(), miwen);
      }
    }
  }

  /**
   * 敏感信息隐藏
   *
   * @Description
   */
  public static void hideImg(JSONObject objCopy, Field field, Object obj) {
    try {
      Object imgObj = field.get(obj);
      if (imgObj instanceof String) {
        int length = ((String) imgObj).length();
        if (length > 20) {
          objCopy.put(field.getName(), ((String) imgObj).substring(0, 10) + miwen
              + ((String) imgObj).substring(length - 11, length - 1));
        }
      } else if (imgObj instanceof List) {
        objCopy.put(field.getName(), "照片数量" + ((List) imgObj).size());
      } else if (imgObj instanceof Map) {
        objCopy.put(field.getName(), "Map数量" + ((Map) imgObj).size());
      } else if (imgObj != null) {

        List<Field> fieldList = getFieldListByClass(imgObj.getClass());
        for (Field field2 : fieldList) {
          if (hasAnnotation(field2)) {
            field2.setAccessible(true);
            String name = field.getName();
            if (!objCopy.containsKey(name)) {
              continue;
            }
            JSONObject objCopy_sun = objCopy.getJSONObject(name);
            hideImg(objCopy_sun, field2, imgObj);
          }
        }
      }
    } catch (IllegalArgumentException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static List<Field> getFieldListByClass(Class<? extends Object> clazz) {
    List<Field> fieldList = new ArrayList<>();
    while (clazz != null) {// 当父类为null的时候说明到达了最上层的父类(Object类).
      fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
      clazz = clazz.getSuperclass(); // 得到父类,然后赋给自己
    }
    return fieldList;
  }

  public static boolean isBasicType(Object obj) {
    if (obj instanceof String || obj instanceof Boolean || obj instanceof Number || obj instanceof Character) {
      return true;
    } else {
      return false;
    }
  }

  private static void handlerType(Object obj, JSONObject objCopy, String fieldName) {
    if (obj instanceof List) {
      JSONArray array = new JSONArray();
      List objList = (List) obj;
      for (Object item : objList) {
        if (isBasicType(item)) {
          objCopy.put(fieldName, obj);
          return;
        }
        String jsonString = JSONObject.toJSONString(item);
        JSONObject jsonObj = JSONObject.parseObject(jsonString);
        handlerType(item, jsonObj, fieldName);
        array.add(jsonObj);
      }
      objCopy.put(fieldName, array);
    } else if (obj instanceof Map) {
      Map map = (Map) obj;
      String jsonString = JSONObject.toJSONString(obj);
      JSONObject jsonObj = JSONObject.parseObject(jsonString);
      for (Entry<Object, Object> objectEntry : (Set<Entry<Object, Object>>) (map.entrySet())) {
        Object key = objectEntry.getKey();
        Object item = objectEntry.getValue();
        handlerType(item, jsonObj, key.toString());
      }
      objCopy.put(fieldName, jsonObj);
    } else if (obj instanceof Set) {
      Set objSet = (Set) obj;
      JSONArray array = new JSONArray();
      for (Object item : objSet) {
        if (isBasicType(item)) {
          objCopy.put(fieldName, obj);
          return;
        }
        String jsonString = JSONObject.toJSONString(item);
        JSONObject jsonObj = JSONObject.parseObject(jsonString);
        handlerType(item, jsonObj, fieldName);
        array.add(jsonObj);
      }
      objCopy.put(fieldName, array);
    } else {
      // 基本类型
      if (isBasicType(obj)) {
        return;
      } else {
        //
        for (Field field2 : getFieldListByClass(obj.getClass())) {
          if (hasAnnotation(field2)) {
            field2.setAccessible(true);
            hideImg(objCopy, field2, obj);
          }
        }
      }
    }
  }

  public static String formatAsJSON(String content) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    JsonParser jsonPar = new JsonParser();
    JsonElement jsonEl = jsonPar.parse(content);
    String prettyJson = gson.toJson(jsonEl);
    return prettyJson;
  }
}
