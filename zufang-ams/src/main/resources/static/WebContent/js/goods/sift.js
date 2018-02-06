/**
 * RBAC - Users
 */
$(function() {
	$('#siftGoodsInfoUpload').window('close');
	$('#siftGoodsInfoEdit').window('close');
	$('#showSiftGoodImg').window('close');
    // Grid 列表
    $('#tablelist').datagrid({
        title : '商品精选列表',
        fit : true,
        //fitColumns : true,
        rownumbers : true,
        pagination : true,
        singleSelect : true,
        striped:true,
        toolbar : '#tb',
        columns : [[{
                    title : 'ID',
                    hidden : true,
                    field : '商品编号',
                    width : 50,
                    align : 'center',
                }, {
        			title : '商户',
        			field : 'merchantName',
        			width : 80,
        			align : 'center'
        		}, {
                    title : '商品名称',
                    field : 'goodsName',
                    width : 80,
                    align : 'center'
                },{
                    title : '商品编号',
                    field : 'goodsCode',
                    width : 80,
                    align : 'center'
                },{
                    title : 'skuid',
                    field : 'externalId',
                    width : 80,
                    align : 'center',
                    formatter : function(value, row, index) {
                        return value == '' ? "--" : value;
                    }
                },{
       		 		title : '三级类目名称',  
       		 		field : 'categoryName3', 
       		 	    width : 80,  
       		 		align : 'center'
       		 	}, {
                	title : '商品型号',
                	field : 'goodsModel',
                	width : 80,
                	align : 'center'
                }, {
                    title : '商品小标题',
                    field : 'goodsTitle',
                    width : 80,
                    align : 'center'
                }, {
                    title : '商品类型',
                    field : 'goodsType',
                    width : 80,
                    align : 'center',
                    formatter : function(value, row, index) {
                        return value == '1' ? "正常" : "精选";
                    }
                }, {
                    title : '规格类型',
                    field : 'goodsSkuType',
                    width : 80,
                    align : 'center',
                },{
                    title : '生产日期',
                    field : 'proDate',
                    width : 110,
                    align : 'center',
                	formatter:function(value,row,index){
                		if(value!=null){
                			return new Date(value).Format("yyyy-MM-dd");
                		}
                    }
                },{
                    title : '保质期',
                    field : 'keepDate',
                    width : 80,
                    align : 'center',
                    formatter:function(value,row,index){
                    	if(null!=value){
                    		return value+"个月";
                    	}
                    }
                },{
                    title : '生产厂家',
                    field : 'supNo',
                    width : 80,
                    align : 'center'
                },{
                    title : '精选排序',
                    field : 'siftSort',
                    width : 80,
                    align : 'center',
                    formatter:function(value,row,index){
                        if("0" == value){
                            return "";
                        }
                        return value;
                    }
                },{
                    title : '状态',
                    field : 'status',
                    width : 80,
                    align : 'center',
                    formatter : function(value, row, index) {
                    	if(value == 'G00'){
                    		return  '待上架';
                    	}
                    	if(value == 'G01'){
                    		return  '待审核';
                    	}
                    	if(value == 'G02'){
                    		return  '已上架';
                    	}
                    	if(value == 'G03'){
                    		return  '已下架';
                    	}
                    }
                }, {
                    title : '上架时间',
                    field : 'listTime',
                    width : 120,
                    align : 'center',
                    formatter:function(value,row,index){
                    	return new Date(value).Format("yyyy-MM-dd hh:mm:ss");
                    }
	            }, {
	                title : '下架时间',
	                field : 'delistTime',
	                width : 120,
	                align : 'center',
	            	formatter:function(value,row,index){
                        if(value != null && value != ""){
                            return new Date(value).Format("yyyy-MM-dd hh:mm:ss");
                        }
	                }
                }, {
                    title : '操作',
                    field : 'opt',
                    width : 150,
                    align : 'center',
                    formatter : function(value, row, index) {
                        var content = "";
                        if(row.goodsType=='1'){
                        	content += "<a href='javascript:void(0);' class='easyui-linkedbutton' onclick='$.updateGoodsSift(\"" + row.id + "\",\"" + row.goodsType + "\");'>设置为精选</a>&nbsp;";
                        }else if(row.goodsType=='2'){
                        	content += "<a href='javascript:void(0);' class='easyui-linkedbutton' onclick='$.editGoodsSift(\"" + row.id + "\",\"" + row.siftSort + "\",\"" + row.goodsSiftUrl + "\");'>编辑</a>&nbsp;";
                        	content += "<a href='javascript:void(0);' class='easyui-linkedbutton' onclick='$.updateGoodsSift(\"" + row.id + "\",\"" + row.goodsType + "\");'>恢复为正常</a>&nbsp;";
                        }
                        if(row.goodsSiftUrl){
                        	content += "<a href='javascript:void(0);' class='easyui-linkedbutton' onclick=\"$.show('" + row.goodsSiftUrl+ "');\">查看图片</a>&nbsp;&nbsp;";//encodeURI(JSON.stringify(row.bannerImgUrl) )
                        }
                        return content;
                    }
                }]],

        loader : function(param, success, error) {
            $.ajax({
                url : ctx + '/application/goods/sift/pagelist',
                data : param,
                type : "post",
                dataType : "json",
                success : function(data) {
                	//console.log(data);
                    $.validateResponse(data, function() {
                        success(data);
                    });
                }
            })
        }
    });
    $("#goodsCategoryCombo").combotree({
        loader : function(param, success, error) {
            $.ajax({
                url : ctx + "/application/goods/management/categoryList",
                data : param,
                type : "get",
                dataType : "json",
                success : function(resp) {
                    $.validateResponse(resp, function() {
                        success(resp.data);
                    });
                }
            })
        }
    });
    $("#goodsCategoryCombo").combotree('setValue', '请选择');

    // 查询列表
    $(".search-btn").click(function() {
        var params = {};
        var goodsType=$("#goodsTypeId").combobox('getValue');
        params['goodsName'] = $("#goodsName").textbox('getValue');
        params['goodsType'] = goodsType;
        params['goodsCode'] = $("#goodsCode").textbox('getValue');
        params['merchantName'] = $("#merchantName").textbox('getValue');
        var goodsCategoryCombo=$("#goodsCategoryCombo").combotree('getValue');
        if("请选择"==goodsCategoryCombo){
            goodsCategoryCombo="";
        }
        params['goodsCategoryCombo']=goodsCategoryCombo;
        $('#tablelist').datagrid('load', params);
    });
    
    //刷新
	$("#flush").click(function(){
		//debugger;
		$("#goodsName").textbox('setValue','');
		$("#goodsCode").textbox('setValue','');
		$("#goodsTypeId").combobox('setValue','');
        $("#merchantName").textbox('setValue','');
        $("#goodsCategoryCombo").combotree('setValue','');
        $("#goodsCategoryCombo").combotree('setValue', '请选择');
		var params = {};
		$('#tablelist').datagrid('load', params);
	});
	//全局变量 商品id
    var goodsId = null;
    //全局变量商品类型
	var goodsType = null;
	//全局变量商品精选排序
	var siftGoodsSort = null;
    // 设置商品为精选弹窗   or 恢复为正常
    $.updateGoodsSift = function(goodsId2,goodsType2) {
    	$("#siftGoodsFile").val("");//点击设置商品为精选时置空文件，让用户重新选择
//    	$("#siftGoodsSortInput").val('');
    	$("#siftGoodsSortInput").textbox('setValue',"");//点击设置商品排序为精选时置空，让用户重新选择
    	goodsId = goodsId2;
    	goodsType = goodsType2;
    	if(goodsType=='1'){
    		$('#siftGoodsInfoUpload').window({
    			shadow : true,
    			minimizable : false,
    			maximizable : false,
    			collapsible : false,
    			modal : true
    		});
			//打开编辑弹出框
			$('#siftGoodsInfoUpload').window('open');
    	}else if(goodsType=='2'){
    		updataGoodsType();
    	}
    }; 
    //查看图片
	$.show=function(goodsSiftUrl){
	   $("#siftGoodImg").attr("src","");
	   $("#siftGoodImg").attr("src", ctx + "/fileView/query?picUrl=" + goodsSiftUrl);
	   $("#showSiftGoodImg").window('open');
	}
    //修改商品类型(正常--1，精选--2)
    function updataGoodsType(){
    	var address = ctx + '/application/goods/sift/update';
        if(goodsType=='1'){
        	goodsType='2';
        }else if(goodsType=='2'){
        	goodsType='1'
        }
        $.getJSON(address, {
            "goodsId" : goodsId,
            "goodsType":goodsType,
            "siftGoodsSort":siftGoodsSort
        }, function(resp) {
            $.validateResponse(resp, function() {
                $(".search-btn").click();
            });
            if(resp.status=='1'){
            	$('#siftGoodsInfoUpload').window('close');
            }
        });
    }
    //确认  
	$("#agreeEdit").click(function() {	
		var siftGoodsFile=$('#siftGoodsFile').val();//结果:siftGoodsFile = "C:\fakepath\Capture001.png"
		var siftGoodsSortInput=$("#siftGoodsSortInput").textbox("getValue");
		var r = /^\+?[1-9][0-9]*$/;//正整数
	    var falg = r.test(siftGoodsSortInput);
	    if(!falg){
	    	$.messager.alert("提示", "精选商品排序字段请用正整数维护！", "info");
			return;
	    }
	    if (null == siftGoodsFile || ("") == siftGoodsFile) {
			$.messager.alert("提示", "请选择文件！", "info");
			return;
		}
		$("#siftGoodsId").val(goodsId);//赋值商品id
		siftGoodsSort = siftGoodsSortInput;//赋值精选商品排序字段
//		var pos = "." + siftGoodsFile.replace(/.+\./, "");//结果:pos = ".png"
//		if(pos!=".png" && pos!=".jpg" ){
//			$.messager.alert("提示", '请选择.png或.jpg类型文件！', "info");
//			return;
//		}
		//提交form
		var thisForm = $("#siftGoodsForm");
		thisForm.form("submit",{
			url : ctx + '/application/goods/sift/upSiftFile',
			success : function(data) {
				var res = JSON.parse(data);//字符串解析成json对象
				$.messager.alert("提示", res.msg, "info");
				if(res.status == '1'){
					updataGoodsType();
				} 
			}
		});
	});
	//取消
	$("#cancelEdit").click(function() {
		$('#siftGoodsInfoUpload').window('close');
	});
	// 编辑精选商品
	$.editGoodsSift= function(id,siftSort,goodsSiftUrl) {
		goodsId = id;
		$("#siftGoodsSortInputEdit").textbox('setValue',siftSort);
		$("#siftGoodsFileEdit").val("");//点击设置商品为精选时置空文件，让用户重新选择
		$("#siftGoods").attr("src",ctx + "/fileView/queryCompress?picUrl=" + goodsSiftUrl);//回显精选图片
    	$('#siftGoodsInfoEdit').window({
			shadow : true,
			minimizable : false,
			maximizable : false,
			collapsible : false,
			modal : true
		});
		$('#siftGoodsInfoEdit').window('open');
	};
	//确认  
	$("#agreeUpdate").click(function() {	
		var siftGoodsFile=$('#siftGoodsFileEdit').val();
		var siftGoodsSortInput=$("#siftGoodsSortInputEdit").textbox("getValue");
		var r = /^\+?[1-9][0-9]*$/;//正整数
	    var falg = r.test(siftGoodsSortInput);
	    if(!falg){
	    	$.messager.alert("提示", "精选商品排序字段请用正整数维护！", "info");
			return;
	    }
	    $("#siftGoodsIdEdit").val(goodsId);//赋值商品id
	    siftGoodsSort = siftGoodsSortInput;//赋值精选商品排序字段
	    if (null == siftGoodsFile || ("") == siftGoodsFile) {//只修改了精选排序
	    	
		}else{//修改排序和图片
			
		}
	    var thisForm = $("#siftGoodsFormEdit");
		thisForm.form("submit",{
			url : ctx + '/application/goods/sift/upSiftFileEdit',
			success : function(data) {
				var res = JSON.parse(data);//字符串解析成json对象
				if(res.status == '1'){
					$.messager.alert("提示", res.msg, "info");
					$(".search-btn").click();
					$('#siftGoodsInfoEdit').window('close');
				} 
			}
		});
	});
	//取消
	$("#cancelUpdate").click(function() {
		$('#siftGoodsInfoEdit').window('close');
	});
});