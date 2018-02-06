$(function(){
	$("#addBannerInfor").window('close');
	$("#showBannerPoto").window('close');

	//Grid
	$('#bannerList').datagrid({
		title : 'banner信息',
		fit : true,
		fitColumns : true,
        rownumbers : true,
        pagination : true,
        singleSelect : true,
        striped:true,
        toolbar : '#tb',
        columns :[[{
	        	title : 'BannerID',
				field : 'id',
				width : 50,
				align : 'center'
			},{
				title : '类型',
				field : 'bannerType',
				width : 100,
				align : 'center'
			},{
				title : '排序',
				field : 'bannerOrder',
				width : 100,
				align : 'center'
			},{
				title : '操作',
				field : 'opt',
				width : 100,
				align : 'center',
				formatter : function(value, row, index) {
					// 授权标示
	            	var grantedAuthority=$('#grantedAuthority').val();
					var content = "";
					if(grantedAuthority=='permission'){
					    content += "<a href='javascript:void(0);' class='easyui-linkedbutton' onclick=\"$.deleteBanner("
							+ row.id+ ");\">删除</a>&nbsp;&nbsp;";
					}
					content += "<a href='javascript:void(0);' class='easyui-linkedbutton' onclick=\"$.show('"
							+ row.bannerImgUrl+ "');\">查看图片</a>&nbsp;&nbsp;";
					content += "<a href='javascript:void(0);' class='easyui-linkedbutton' onclick=\"$.showActivity('"
						+ row.activityUrl+ "');\">预览</a>&nbsp;&nbsp;";
					content += "<a href='javascript:void(0);' class='easyui-linkedbutton' onclick=\"$.editBanner('"
						+ row.id+ "');\">编辑</a>&nbsp;&nbsp;";
				 return content;
			}
			}]],
        loader : function(param, success, error) {
            $.ajax({
                url : ctx + '/application/banner/management/query',
                data : param,
                type : "post",
                dataType : "json",
                success : function(data) {
            	   $.validateResponse(data, function() {
            		  // debugger;
                       success(data);
                   });
                }
            })
        }
	});


	$("#bannerType").combobox({
		onSelect:function(){
			if($("#bannerType").combobox('getValue') == "index"){
				$("#fondSpan").empty();
				$("#fondSpan").append("<font color='red'>支持格式：.png或.jpg;宽：750px,高：300px;大小：≤500kb</font>");
			}else{
				$("#fondSpan").empty();
				$("#fondSpan").append("<font color='red'>支持格式：.png或.jpg;宽：750px,高：300px;大小：≤500kb</font>");
			}
		}
	});

	//添加  banner信息
	$("#add").click(function(){
		$('#addBannerInfor').window({
            minimizable:false,
            maximizable:false,
            collapsible:false,
            modal:true,
            top:$(document).scrollTop() + ($(window).height()-250) * 0.5
		});
		$("#addBannerInfor").window('open');

		$("#bannerName").textbox('setValue','');
		$("#bannerType").combobox('setValue','');
		$("#bannerOrder").numberbox('setValue','');
		$("#bannerFile").val('');
		$("#activityUrl").textbox('setValue','');
	});

	//确认   添加  banner信息
	$("#agreeAdd").click(function(){
		// debugger;
		var bannerType=$("#bannerType").combobox('getValue');
		if(null == bannerType || bannerType==""){
			$.messager.alert("<span style='color: black;'>提示</span>","类型不能为空！","info");
			return;
		}
		var bannerOrder=$("#bannerOrder").numberbox('getValue');
		if(null == bannerOrder || bannerOrder==""){
			$.messager.alert("<span style='color: black;'>提示</span>","排序不能为空！","info");
			return;
		}
		var activityName=$("#activityName").numberbox('getText');
		var activityUrl=$("#activityUrl").numberbox('getValue');
		if(null == activityUrl || activityUrl==""){
			$.messager.alert("<span style='color: black;'>提示</span>",activityName+"不能为空！","info");
			return;
		}

		var bannerFile= $("#bannerFile").val();
		if(bannerFile=='' || null==bannerFile){
			$.messager.alert("<span style='color: black;'>提示</span>","请选择上传图片！","info");
			return;
		}

		//提交from
		var theForm = $("#addBannerFile");
		theForm.form("submit",{
			url : ctx + '/application/banner/management/addBannerFile',
			success : function(data) {
				debugger;
				var flag1 = data.indexOf('登录系统');
				var flag2 = data.indexOf('</div');
				if (flag1 != -1 && flag2 != -1) {
					$.messager.alert("操作提示", "登录超时, 请重新登录", "info");
					window.top.location = ctx + "/logout";
					return;
	            }
				var respon=JSON.parse(data);
				if(respon.status=="1"){
					$.messager.alert("<span style='color: black;'>提示</span>",respon.msg,"info");
					 $('#addBannerInfor').window('close');
				}else{
					$.messager.alert("<span style='color: black;'>警告</span>",respon.msg,"warning");
				}
				var param = {};
            	param['bannerType'] = $('#bannerType2').combobox('getValue');
		        $('#bannerList').datagrid('load', param);
			}
		});
	});

	//取消   添加  banner信息
	$("#cancelAdd").click(function(){
		$("#addBannerInfor").window('close');
	});
	//删除
	$.deleteBanner=function(id){
		$.messager.confirm("<span style='color: black;'>操作提示</span>", "您确定删除该条banner图吗？", function (r){
			if(r){
				var params={};
				params['id']=id;
				$.ajax({
					url : ctx + '/application/banner/management/delete',
					data : params,
					type : "post",
					dataType : "json",
					success : function(data) {
						if (data.result == false && data.message == 'timeout')
					    {
//		            		$.messager.alert("提示", "超时，请重新登录", "info");
//		            		parent.location.reload();//刷新整个页面
		            		$.messager.alert("操作提示", "登录超时, 请重新登录", "info");
		    				window.top.location = ctx + "/logout";
		    				return false;
					    }
						if(data.status=="1"){
							$.messager.alert("<span style='color: black;'>提示</span>",data.msg,"info");
							var params={};
							params['bannerType']=null;
							$('#bannerList').datagrid('load',params);
						}else{
							$.messager.alert("<span style='color: black;'>提示</span>",data.msg,"info");
						}
					}
				});
			}
		})
	};

	//查看图片
	$.show = function(bannerImgUrl) {
		$("#showPicture").attr("src","");
		$("#showPicture").attr("src",ctx + "/fileView/query?picUrl=" + bannerImgUrl);
		$("#showBannerPoto").window('open');
	}
	//查看活动
	$.showActivity = function(activityUrl) {
		var index = activityUrl.indexOf("?");
		var end=activityUrl.length;
		var acUrl=activityUrl.slice(index+1,end);
		if(acUrl.indexOf("id=") != -1){
			var id = acUrl.slice(acUrl.indexOf("=") +1,acUrl.indexOf("&"));
			var source = '';
			if(acUrl.indexOf("notJd") == -1){
				source = "wz";
			}
			//跳转到商品详情页
			$.previewProduct(id,source);
		}else{
			var subtitle = "活动预览-";
			var parentTabs = parent.$('#tabs');
			var address = "";
			if(acUrl.indexOf('http://') == -1){
				address="http://" + acUrl.slice(acUrl.indexOf("=") + 1,acUrl.length);

			}else{
				address=acUrl.slice(acUrl.indexOf("=") + 1,acUrl.length);
			}

			if (parentTabs.tabs('exists', subtitle)) {
				parentTabs.tabs('select', subtitle);
				return;
			}

			parentTabs.tabs('add', {
				title : subtitle,
				content : function() {
					var array = new Array();
					array.push('<iframe name="mainFrame" ');
					array.push('scrolling="auto" ');
					array.push('frameborder="0" ');
					array.push('src="' + address + '" ');
					array.push(' style="width:100%;height:100%;" ');
					array.push(' ></iframe>');
					return array.join('');
				},
				closable : true
			});



		}
	}

	//编辑
	$.editBanner = function(bannerId){
		$.ajax({
			url : ctx + '/application/banner/management/getById?id='+ bannerId,
			type : "get",
			dataType : "json",
			success : function(resp) {
				console.debug(resp)
				var data = resp.data;
				$('#addBannerInfor').window({
					minimizable:false,
					maximizable:false,
					collapsible:false,
					modal:true,
					top:$(document).scrollTop() + ($(window).height()-250) * 0.5
				});
				$("#addBannerInfor").window('open');
				$("#bannerType").combobox('setValue',data.bannerType);
				$("#bannerOrder").numberbox('setValue',data.bannerOrder);
				$('#activityUrl').textbox('setValue',data.attrVal);
				$('#activityName').combobox('setValue',data.attr);

				$('#bannerId').val(data.id);

			}
		});

	}

	$.previewProduct = function(id,source) {
		var subtitle = "商品预览-" + id;
		var parentTabs = parent.$('#tabs');
		var destAddress="";
		if("wz"==source){
			destAddress = ctx + "/application/goods/management/loadAllBannerPicJD?id=" + id+"&view=list";
		}else{
			destAddress = ctx + "/application/goods/management/loadAllBannerPic?id=" + id+"&view=list";
		}

		if (parentTabs.tabs('exists', subtitle)) {
			parentTabs.tabs('select', subtitle);
			return;
		}

		parentTabs.tabs('add', {
			title : subtitle,
			content : function() {
				var array = new Array();
				array.push('<iframe name="mainFrame" ');
				array.push('scrolling="auto" ');
				array.push('frameborder="0" ');
				array.push('src="' + destAddress + '" ');
				array.push(' style="width:100%;height:100%;" ');
				array.push(' ></iframe>');
				return array.join('');
			},
			closable : true
		});
	};

	// 重置
	$("#reset").click(function(){
		$('#bannerType2').combobox('setValue','');
		var params={};
		$('#bannerList').datagrid('load',params);
	});
	//查询
	$(".search-btn").click(function(){
		var params={};
		params['bannerType']=$('#bannerType2').combobox('getValue');
		$('#bannerList').datagrid('load',params);
	});

});
