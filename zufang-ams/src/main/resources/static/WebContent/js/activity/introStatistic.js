$(function(){
	
    //Grid
    $('#list').datagrid({
        title : '转介绍数据管理',
        fit : true,
        fitColumns : true,
        rownumbers : true,
        pagination : true,
        singleSelect : true,
        striped:true,
        toolbar : '#tb',
        columns :[[
            {
                title : '全部',
                field : 'mobile',
                width : 150,
                align : 'center'
            }, {
                title : '拉新人数',
                field : 'inviteNum',
                width : 150,
                align : 'center'
            },
            {
                title : '现金',
                field : 'bankAmt',
                width : 150,
                align : 'center'
            },
            {
                title : '额度',
                field : 'creditAmt',
                width : 120,
                align : 'center'
            },
            {
                title : '返现金额',
                field : 'rebateAmt',
                width : 120,
                align : 'center'
            }

        ]],
        loader : function(param, success, error) {
            $.ajax({
                url : ctx + '/activity/introduce/statistic/list',
                data : param,
                type : "get",
                dataType : "json",
                success : function(data) {
                    $.validateResponse(data, function() {
                        debugger;
                    	$("#manSum").html(data.allCount);
                    	$("#cashSum").html(data.bankAmtSum);
                    	$("#amountSum").html(data.creditAmtSum);
                    	$("#returnCashSum").html(data.rebateAmtSum);
                        success(data);
                        awardbindrel(-1);
                        awarddetai(-1);
                    });
                }
            })
        }
    });


    //查询
    $(".search-btn").click(function(){
        var startCreateDate=$("#createDate1").datebox('getValue');
        var endCreateDate=$("#createDate2").datebox('getValue');
        var telephone=$("#telephone").textbox('getValue');
        if(startCreateDate!=null && startCreateDate!=''&&endCreateDate!=null && endCreateDate!=''){
    		if(startCreateDate>endCreateDate){
    			$.messager.alert("<span style='color: black;'>提示</span>","活动时间：开始时间应早于结束时间！",'info');
    			$('#createDate1').datebox('setValue','');
    			$('#createDate2').datebox('setValue','');
    			return;
    		}
    	}
        var params={};
        params['startCreateDate'] = startCreateDate;
        params['endCreateDate'] = endCreateDate;
        params['mobile'] = telephone;
        $('#list').datagrid('load',params);
    });

    // 重置
    $("#reset").click(function(){
        $("#createDate1").textbox('setValue','');
        $("#createDate2").textbox('setValue','');
        $("#telephone").textbox('setValue','');

        var params={};
        $('#list').datagrid('load',params);
    });
    
    //查询已放款和预计放款
    $("#awarddetail").combobox({
    	onBeforeLoad:function(){
    		$('#awarddetail').combobox('setValue',-1);
    		awarddetai(-1);
    	},
    	onChange:function(){
    		var days = $("#awarddetail").combobox('getValue');
    		awarddetai(days);
    	}
    });
    
    function awarddetai(days){
    	$.ajax({
            url : ctx + '/activity/introduce/sumAmountGroupByType',
            data : {"days":days},
            type : "POST",
            dataType : "json",
            success : function(data) {
                $("#awarddetail").combobox("setValue",days);
                $("#loadAmount").html(data.loadAmount);
                $("#expectLoadAmount").html(data.expectLoadAmount);
            }
        });
    }
    //查询拉取人数
    $("#awardbindrel").combobox({
    	onBeforeLoad:function(){
    		$('#awardbindrel').combobox('setValue',-1);
    		awardbindrel(-1);
    	},
    	onChange:function(){
    		var days = $("#awardbindrel").combobox('getValue');
    		awardbindrel(days);
    	}
    });
    
    function awardbindrel(days){
    	$.ajax({
            url : ctx + '/activity/introduce/inviterUserCountByTime',
            data : {"days":days},
            type : "POST",
            dataType : "json",
            success : function(data) {
                $("#awardbindrel").combobox("setValue",days);
            	$("#personSum").html(data);
            }
        });
    }
});
