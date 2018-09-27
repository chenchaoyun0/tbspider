<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<script src="${pageContext.request.contextPath}/js/jquery-3.2.1.min.js"></script>
<script
 src="${pageContext.request.contextPath}/js/echarts.common.min.js"></script>
</head>
<body>
 <div id="pie" style="width: 600px; height: 500px;"></div>
 <script>
		//初始化echarts
		var pieCharts = echarts.init(document.getElementById("pie"));
		//设置属性
		pieCharts.setOption({
			title : {
				text : '帖子数',
				subtext : '有回复的帖子与没回复的帖子比例',
				x : 'center'
			},
			tooltip : {
				trigger : 'item',
				formatter : "{a} <br/>{b} : {c} ({d}%)"
			},
			legend : {
				orient : 'vertical',
				x : 'left',
				data : []
			},
			toolbox : {
				show : true,
				feature : {
					mark : {
						show : true
					},
					dataView : {
						show : true,
						readOnly : false
					},
					magicType : {
						show : true,
						type : [ 'pie', 'funnel' ],
						option : {
							funnel : {
								x : '25%',
								width : '50%',
								funnelAlign : 'left',
								max : 1548
							}
						}
					},
					restore : {
						show : true
					},
					saveAsImage : {
						show : true
					}
				}
			},
			calculable : true,
			series : [ {
				name : '帖子数',
				type : 'pie',
				radius : '70%',
				center : [ '50%', '60%' ],
				data : []
			} ]
		});
		//显示一段动画
		pieCharts.showLoading();
		//异步请求数据
		$.ajax({
			type : "post",
			async : true,
			url : '${pageContext.request.contextPath}/getPostHasRepAndNo',
			data : [],
			dataType : "json",
			success : function(result) {
				pieCharts.hideLoading();//隐藏加载动画
				pieCharts.setOption({
					title : {
						text : '帖子数',
						subtext : '有回复的帖子与没回复的帖子比例',
						x : 'center'
					},
					tooltip : {
						trigger : 'item',
						formatter : "{a} <br/>{b} : {c} ({d}%)"
					},
					legend : {
						orient : 'vertical',
						x : 'left',
						data : []
					},
					toolbox : {
						show : true,
						feature : {
							mark : {
								show : true
							},
							dataView : {
								show : true,
								readOnly : false
							},
							magicType : {
								show : true,
								type : [ 'pie', 'funnel' ],
								option : {
									funnel : {
										x : '25%',
										width : '50%',
										funnelAlign : 'left',
										max : 1548
									}
								}
							},
							restore : {
								show : true
							},
							saveAsImage : {
								show : true
							}
						}
					},
					calculable : true,
					series : [ {
						name : '帖子数',
						type : 'pie',
						radius : '70%',
						center : [ '50%', '60%' ],
						data : result
					} ]
				});
			}
		})
	</script>
</body>
</html>