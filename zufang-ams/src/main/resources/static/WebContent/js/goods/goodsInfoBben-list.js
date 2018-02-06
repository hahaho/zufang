$(function() {
	$("#goodsStockWindow").window('close');
	
	//初始化
    $('#tablelist').datagrid({
        title : '商品列表',
        fit : true,
        //fitColumns : true,
        rownumbers : true,
        pagination : true,
        singleSelect: false, //允许选择多行  
        selectOnCheck: true,   
        checkOnSelect: false,
        striped:true,
        toolbar : '#tb',
        rowStyler:function(rowIndex,rowData){
        	if(rowData.colFalgt=='1'){
        		return 'background-color:#6293BB;';
        	}
        },
        columns : [[ { field: 'ck', checkbox: true, width: '30' },  //复选框 
                {
		            title : 'ID',
		            hidden:true,
		            field : '商品编号',
		            width : 50,
		            align : 'center'
                },{
		            title : '商户名称',
		            field : 'merchantName',
		            width : 90,
		            align : 'center'
                },{
                    title : '商品编号',
                    field : 'goodsCode',
                    width : 90,
                    align : 'center'
                },
                {
                    title : 'skuid',
                    field : 'externalId',
                    width : 90,
                    align : 'center'
                },{
                    title : '商品名称',
                    field : 'goodsName',
                    width : 90,
                    align : 'center'
                },{  
       		 		title : '三级类目名称',  
       		 		field : 'categoryName3', 
       		 	    width : 90,  
       		 		align : 'center'
       		 	},{
                    title : '商品型号',
                    field : 'goodsModel',
                    width : 80,
                    align : 'center'
                },{
                    title : '商品小标题',
                    field : 'goodsTitle',
                    width : 90,
                    align : 'center'
                },{
                    title : '商品类型',
                    field : 'goodsType',
                    width : 80,
                    align : 'center',
                    formatter : function(value, row, index) {
                        return value == '1' ? "正常" : "精选";
                    }
                },{
                    title : '规格类型',
                    field : 'goodsSkuType',
                    width : 80,
                    align : 'center'
                },{
                    title : '商品生产日期',
                    field : 'proDate',
                    width : 120,
                    align : 'center',
                    formatter:function(value,row,index){
                    	if(null!=value && ""!=value){
                    		return new Date(value).Format("yyyy-MM-dd");
                    	}
                    }
                },{
                    title : '保质期',
                    field : 'keepDate',
                    width : 80,
                    align : 'center',
                    formatter:function(value,row,index){
                    	if(null!=value && ""!=value){
                    		return value+"个月";
                    	}
                    }
                },{
                    title : '生产厂家',
                    field : 'supNo',
                    width : 90,
                    align : 'center'
                },{
                    title : '排序',
                    field : 'sordNo',
                    width : 60,
                    align : 'center'
                },{
                    title : '状态',
                    field : 'statusDesc',
                    width : 80,
                    align : 'center',
                },{
                    title : '商品上架时间',
                    field : 'listTime',
                    width : 140,
                    align : 'center',
                    formatter:function(value,row,index){
                    	if(value!=null){
                    		return new Date(value).Format("yyyy-MM-dd hh:mm:ss");
                    	}
                    }
                }, {
                    title : '商品下架时间',
                    field : 'delistTime',
                    width : 140,
                    align : 'center',
                    formatter:function(value,row,index){
                    	if(value!=null){
                    		return new Date(value).Format("yyyy-MM-dd hh:mm:ss");
                    	}
                    }
                },{
                    title : '创建人',
                    field : 'createUser',
                    width : 80,
                    align : 'center'
                },{
                    title : '修改人',
                    field : 'updateUser',
                    width : 80,
                    align : 'center'
                },{
                    title : '创建时间',
                    field : 'createDate',
                    width : 140,
                    align : 'center',
                    formatter:function(value,row,index){
                    	if(value!=null){
                    		return new Date(value).Format("yyyy-MM-dd hh:mm:ss");
                    	}
                    }
                },{
                    title : '修改时间',
                    field : 'updateDate',
                    width : 140,
                    align : 'center',
                    formatter:function(value,row,index){
                    	if(value!=null){
                    		return new Date(value).Format("yyyy-MM-dd hh:mm:ss");
                    	}
                    }
                },{
                	title : '商品来源标识',
                	field : 'source',
                	width : 140,
                	align : 'center',
                },{
                    title : '操作',
                    field : 'opt',
                    width : 100,
                    align : 'center',
                    formatter : function(value, row, index) {
                        debugger;
                    	// 授权标示
                    	 var grantedAuthority=$('#grantedAuthority').val();
                    	 var content = "";
                    	
                    	 content +="<a href='javascript:void(0);' class='easyui-linkedbutton chectDetailWL' data-id='"+row.id+"' data-source='"+row.source+"'\">查看详情</a>&nbsp;&nbsp;";
                    	 if(grantedAuthority=='permission'){
                    	    content +="<a href='javascript:void(0);' class='easyui-linkedbutton' onclick=\"$.checkOne("
                             + row.id + ");\">审核</a>&nbsp;&nbsp;";
                    	 }
                    	 
                    	 return content;
                    }
                }]],
        loader : function(param, success, error) {
        	 param['goodsStatus']='G04';
        	 param['isAll']='T';
            $.ajax({
                url : ctx + '/application/goods/management/pagelist',
                data : param,
                type : "post",
                dataType : "json",
                success : function(data) {
                    $.validateResponse(data, function() {
                        success(data);
                    });
                }
            })
        }
    });
    
    $(document).on("click",".chectDetailWL",function(){
		$("#goodsStockWindow").window('open');
		$("#goodsStockList").datagrid({
	        height:220,
	        width:"100%",
	        url : ctx + '/application/goods/stockinfo/pagelist?goodsId='+$(this).attr("data-id"),
	        idField:'id',
	        pagination : true,
			rownumbers : true,
	        columns: [[
	                {field: 'goodsSkuAttr', title: '库存规格', width: 80,align : 'center'},
	                {field: 'goodsCostPrice', title: '成本价格', width: 80,align : 'center',
//		                formatter:function(value,row,index){
//		                	// 授权标示
//		                 	var goodCostpriceIf=$('#goodCostpriceIf').val();
//		                 	if(goodCostpriceIf=='permission'){
//		                		return value;
//		                	} 
//		                		return "未授权";
//		                }
	                },
	                {field: 'marketPrice', title: '市场价格', width: 80,align : 'center'},
	                {field: 'goodsPrice', title: '售价', width: 80,align : 'center'},
                    {field: 'priceCostRate', title: '保本率', width: 80,
                        formatter:function(value, row, index){
                            if(value != null && value != '') {
                                return value+"%";
                            }
                        },
                        align : 'center'},
	                {field: 'stockTotalAmt', title: '商品总量', width: 80,align : 'center'},
	                {field: 'stockCurrAmt', title: '当前库存量', width: 80,align : 'center'}	                
	            ]] 
	    });
	 
    })
    
    // 查询列表
    $(".search-btn").click(function() {
        var params = {};
        params['merchantName'] = $("#merchantName").textbox('getValue');
        params['merchantType'] = $("#merchantType").combobox('getValue');
        params['goodsName'] = $("#goodsNames").textbox('getValue');
        params['goodsType'] = $("#goodsTypes").textbox('getValue');
        params['status']='G01';
        params['isAll']='T';//默认
        params['goodsCode'] = $("#goodsCode").textbox('getValue');
        var goodsCategoryCombo=$("#goodsCategoryCombo").combotree('getValue');
        if("请选择"==goodsCategoryCombo){
            goodsCategoryCombo="";
        }
        params['goodsCategoryCombo']=goodsCategoryCombo;

        $('#tablelist').datagrid('load', params);
    });

    goodsCategoryComboFun();

    // 批量复核
    $(".checkAll").click(function() {
    	var selRow = $('#tablelist').datagrid('getChecked');
		if(selRow.length==0){  
			 $.messager.alert("提示", "至少勾选一条数据！","info");  
			 return ;  
		}else{
			var ids=[];  
			for (var i = 0; i < selRow.length; i++) {
				var id=selRow[i].id;     
				ids.push(id); 
		      }
			checkGoods(ids,"商品批量保本率审核");

		}
    });
    
	$.checkOne = function(id) {
		debugger;
		var arr = new Array();
		arr[0]=id;
    	checkGoods(arr,"商品单个保本率审核");
    }
	 $("#reviewOpinion").switchbutton({
	        onText : '保本率审核驳回',
	        offText : '保本率审核通过'

    });
	 
	// 刷新
	$ ("#flush").click (function ()
	{
		$("#merchantName").textbox('setValue','');
    	$("#merchantType").combobox('setValue','');
		$ ("#goodsNames").textbox ('setValue', '');
		$ ("#goodsTypes").combobox ('setValue', '');
        $("#goodsCode").textbox('setValue','');
        $("#goodsCategoryCombo").combotree('setValue', '请选择');
		var params = {};
		$ ('#tablelist').datagrid ('load', params);
	});
	
	 //复核
	function checkGoods(ids,msgss){
		  var handlesubmit = function() {
			    debugger;
		        var reject = $("#reviewOpinion").switchbutton("options").checked;
		        $.ajax({
					type : "POST",
					url : ctx + "/application/goods/management/bBencheckview",
					data : {
			                "ids" : JSON.stringify(ids),
			                "flag" : reject ? "reject" : "pass"
			            },
					success : function(data) {
		            	$("#reviewGoods").dialog("close");
	                    $(".search-btn").click();
					}
				});
		    }
		  
		   $("#reviewGoods").dialog({
            modal : true,
            title : msgss,
            width : 500,
            height : 250,
            buttons : [{
                        text : "确定",
                        handler : function() {
                        	var reject = $("#reviewOpinion").switchbutton("options").checked;
                        	var flagts=reject ? "reject" : "pass";
                        	handlesubmit();
                        }
                    }, {
                        text : "关闭",
                        handler : function() {
                            $("#reviewGoods").dialog("close");
                        }
                    }]
	        });
		   
		   //清缓存
		   $ ("#reviewGoodsForm #message").textbox ('setValue', '');
		   $("#reviewOpinion").switchbutton("uncheck");
	}
	//预览商品
	$.previewProduct = function(id,goodsId) {
		debugger;
        var subtitle = "商品预览-" + id;
        var parentTabs = parent.$('#tabs');
        var destAddress = ctx + "/application/goods/management/loadAllBannerPic?id=" + id+"&view=check";
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
    
});