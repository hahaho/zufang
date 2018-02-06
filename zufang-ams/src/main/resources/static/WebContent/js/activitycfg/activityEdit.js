$(function () {
    $("#addGoodsToGroup").window('close');

    $('#activityGroupList').datagrid({
        fit: false,
        rownumbers: true,
        pagination: true,
        singleSelect: false, //允许选择多行
        selectOnCheck: true,
        checkOnSelect: false,
        striped: true,
        fitColumns: true,
        columns: [[
            {
                title: '分组名称',
                field: 'groupName',
                width: 250,
                align: 'center'
            }, {
                title: '分组下商品数量',
                field: 'goodsSum',
                width: 250,
                align: 'center'
            }, {
                title: '排序',
                field: 'orderSort',
                width: 220,
                align: 'center',
                formatter: function (value, row, index) {
                    if (null == value || "null" == value)
                        value = "";
                    var msg = value + "";
                    return "<div  title='" + value + "'>" + value + "</div>";
                }
            }, {
                title: '操作',
                field: 'opt',
                width: 220,
                align: 'center',
                formatter: function (value, row, index) {
                    var content = "";
                    content += "<a href='javascript:void(0);' class='easyui-linkedbutton' onclick=\"$.editGroups('"
                        + row.groupName + "','"+ row.goodsSum + "','" + row.orderSort + "','" + row.id + "');\">编辑</a>&nbsp;&nbsp;";
                    content += "<a href='javascript:void(0);' class='easyui-linkedbutton' onclick=\"$.deleteGroups('" + this + "','" + row.id + "');\">删除</a>&nbsp;&nbsp;";
                    return content;
                }
            }]],
        queryParams: {"activityId": $("#addGoodsToGroupActivityId").val()},
        loader: function (param, success, error) {
            $.ajax({
                url: ctx + '/group/manager/list',
                data: param,
                type: "post",
                dataType: "json",
                success: function (data) {
                    console.log(data);
                    $.validateResponse(data, function () {
                        success(data);
                    });
                }
            })
        }
    });

    $('#importFileList').datagrid({
        title: '商品池',
        fit: true,
        fitColumns: false,
        rownumbers: true,
        pagination: true,
        singleSelect: false, //允许选择多行  
        selectOnCheck: true,
        checkOnSelect: false,
        height:500,
        striped: true,
        toolbar: '#tb',
        rowStyler: function (rowIndex, rowData) {
            if (rowData.colFalgt == '1') {
                return 'background-color:#6293BB;';
            }
        },
        columns: [[
            {//复选框
            	field: 'ck', 
            	checkbox: true, 
            	width: '30'
            },{
                title: '商品名称',
                field: 'goodsName',
                width: 100,
                align: 'center',
                formatter: function (value, row, index) {
                    if (null == value || "null" == value)
                        value = "";
                    var msg = value + "";
                    return "<div  title='" + value + "'>" + value + "</div>";
                }
            },{
            	title: '商品编码',
                field: 'goodsCode',
                width: 80,
                align: 'center'
            },{
            	title : '商品规格',
                field : 'skuAttr',
                width : 80,
                align : 'center',
            },{
            	title: 'skuid',
                field: 'skuId',
                width: 80,
                align: 'center'
            },{
                title: '商品状态',
                field: 'goodsStatus',
                width: 80,
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == 'G00') {
                        return "待上架";
                    } else if (value == 'G01') {
                        return "待审核";
                    } else if (value == 'G02') {
                        return "已上架";
                    } else if (value == 'G03') {
                        return "已下架";
                    } else if (value == 'G04') {
                        return "待审核";
                    }
                }
            },{
                title: '商品类目',
                field: 'goodsCategory',
                width: 80,
                align: 'center'
            },{
                title: '成本价',
                field: 'goodsCostPrice',
                width: 80,
                align: 'center'
            },{
                title: '售价',
                field: 'goodsPrice',
                width: 80,
                align: 'center'
            },{
                title: '市场价',
                field: 'marketPrice',
                width: 80,
                align: 'center'
            },{
                title: '活动价',
                field: 'activityPrice',
                width: 80,
                align: 'center'
            },{
                title: '分组',
                field: 'groupName',
                width: 80,
                align: 'center'
            },{
                title: '上传标志',
                field: 'detailDesc',
                width: 80,
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == '1') {
                        return "成功";
                    } else {
                        return "失败";
                    }
                }
            },{
                title: '操作',
                field: 'opt',
                width: 100,
                align: 'center',
                formatter: function (value, row, index) {
                    var content = "";
                    if (row.detailDesc == '1' && row.status != 'S') {
                        content += "<a href='javascript:void(0);' class='easyui-linkedbutton' onclick=\"$.editGoodsAndActivity('"
                            + row.goodsId + "','" + row.skuId+"','" +row.activityId + "');\">添加至</a>&nbsp;&nbsp;";
                    }
                    return content;
                }
            }]],
        queryParams: {"activityId": $("#addGoodsToGroupActivityId").val()},
        loader: function (param, success, error) {
            $.ajax({
                url: ctx + '/application/activity/list',
                data: param,
                type: "post",
                dataType: "json",
                success: function (data) {
                    console.log(data);
                    $.validateResponse(data, function () {
                        success(data);
                    });
                }
            })
        }
    });
    $("#createGroup").click(function () {
        $("#groupNameAdd").textbox('clear');
        $("#sordGroupAdd").textbox('clear');
        $("#addGroupDiv").dialog({
            modal: true,
            title: "<span style='color: black'>创建分组</span>",
            resizable: true,
            width: 400,
            buttons: [{
                text: "确定",
                handler: function () {
                    $.messager.confirm("<span style='color: black'>确认对话框</span>", "你确定要提交吗？", function(r){
                        if (r){
                            var activityId = $("#addGoodsToGroupActivityId").val();
                            var groupNameAdd = $("#groupNameAdd").textbox('getValue');
                            var sordGroupAdd = $("#sordGroupAdd").textbox('getValue');
                            if(groupNameAdd.length == 0){
                                $.messager.alert("<span style='color: black;'>提示</span>","分组名称不能为空!","info");
                                return;
                            }
                            if(sordGroupAdd.length == 0){
                                $.messager.alert("<span style='color: black;'>提示</span>","分组排序不能为空!","info");
                                return;
                            }
                            $.ajax({
                                type: "POST",
                                url: ctx + '/group/manager/add/save',
                                data: {"groupName": groupNameAdd, "orderSort": sordGroupAdd, "activityId": activityId},
                                success: function (data) {
                                    ifLogout(data);
                                    if (data.status == 1) {
                                        $("#addGroupDiv").dialog("close");
                                        $.messager.alert("<span style='color: black'>提示</span>", data.msg,'info');
                                        $('#activityGroupList').datagrid("load", {"activityId": activityId});
                                    } else {
                                        $.messager.alert("<span style='color: black'>提示</span>", data.msg,'info');
                                    }
                                }
                            });
                        }
                    });
                }
            }, {
                text: "取消",
                handler: function () {
                    $("#addGroupDiv").dialog("close");
                }
            }]
        });
    });

    //加载商品类目信息
    goodsCategoryComboFun();
    // 查询列表
    $("#searchGoods").click(function () {
        var params = {};
        params['goodsCode'] = $("#goodsCode").textbox('getValue');
        params['skuId'] = $("#skuId").textbox('getValue');
        var goodsCategoryCombo = $("#goodsCategoryCombo").combotree('getValue');
        if ("请选择" == goodsCategoryCombo) {
            goodsCategoryCombo = "";
        }
        params['goodsCategory'] = goodsCategoryCombo;
        params['activityId'] = $("#addGoodsToGroupActivityId").val();
        $('#importFileList').datagrid('load', params);
    });
    // 刷新列表
    $("#resetGoods").click(function () {
        $("#goodsCode").textbox('setValue', '');
        $("#skuId").textbox('setValue', '');
        $("#goodsCategoryCombo").combotree('setValue', '');
        $("#goodsCategoryCombo").combotree('setValue', '请选择');
        $('#importFileList').datagrid('load', {"activityId":$("#addGoodsToGroupActivityId").val()});
    });

    //导入
    var lock = true;
    $("#import").click(function () {
    	if(!lock){
    		return false;
    	}
    	lock = false;
    	var actiId = $("#addGoodsToGroupActivityId").val();
        $("#activityId").val(actiId);
        var form = $("#ExcelFileForm");
        var file = $("#Excelfile").val();
        if (file == null || file == '') {
        	lock = true;
            $.messager.alert("<font color='black'>提示</font>", "请选择要上传的Excel文件", "info");
            return;
        }
        form.form("submit", {
            url: ctx + '/application/activity/importFile',
            success: function (response) {
                var data = JSON.parse(response);
                if (data.status == '1') {
                    $.messager.alert("<font color='black'>提示</font>", data.msg, "info");
                    $('#Excelfile').val('');
                    var params = {};
                    lock = true;
                    $('#importFileList').datagrid('load', {"activityId":actiId});
                } else {
                	lock = true;
                    $.messager.alert("<font color='black'>提示</font>", data.msg, "info");
                }
            }
        });
    });
    
    //单个商品添加至
    $.editGoodsAndActivity = function (goodsId,skuId, activityId) {
        /**加载该活动的分组**/
        var params = {};
        params['activityId'] = activityId;
        $('#groupName').combobox({
            url: ctx + '/application/activity/loalgroupIds',
            queryParams: params,
            onLoadSuccess: function (object) {
                var l = object.length;
                if (l > 0) {
                	//$('#addGoodsToGroup').window("resize",{top:$(document).scrollTop() + ($(window).height()-250) * 0.5});//居中显示
                	$('#addGoodsToGroup').window('open')
                	$("#addGoodsToGroupActivityId").val(activityId);
                    $("#addGoodsToGroupGoodsId").val(goodsId);
                    $("#addGoodsToGroupSkuId").val(skuId);
                } else {
                    alert("请先创建分组！");
                }
            },
            valueField: 'id',
            textField: 'text'
        });
    };
    //按取消键 关闭弹框
    $("#addGoodsToGroupOppo").click(function () {
        $("#addGoodsToGroup").window('close');
    });
    //确定关联分组
    $("#addGoodsToGroupAgree").click(function () {
        $("#addGoodsToGroup").window('close');
        var params = {};
        var activityId = $("#addGoodsToGroupActivityId").val();
        var goodsId = $("#addGoodsToGroupGoodsId").val();
        var skuId = $("#addGoodsToGroupSkuId").val();
        var groupNameId = $("#groupName").textbox('getValue');
        if (null == groupNameId || groupNameId == "") {
            alert("请选择分组！");
            return;
        }
        params['activityId'] = activityId;
        params['goodsId'] = goodsId;
        params['skuId'] = skuId;
        params['groupNameId'] = groupNameId;
        $.ajax({
            type: "POST",
            url: ctx + '/application/activity/addOneGoods',
            data: params,
            success: function (data) {
                if (data.status == '1') {
                    alert(data.msg);
                } else {
                	alert(data.msg);
                }
                var params = {};
                params['activityId'] = activityId;
                $('#importFileList').datagrid('load', params);
                $('#activityGroupList').datagrid('load', {"activityId":activityId});
            }
        });
    });
    // 批量商品添加至
    $("#addGoods").click(function () {
        debugger;
        var selRow = $('#importFileList').datagrid('getChecked');
        if (selRow.length == 0) {
            $.messager.alert("<span style='color:#000;'>提示</span>", "至少要勾选一件商品！", "info");
            return;
        } else {
			var activityId= $("#addGoodsToGroupActivityId").val();
            var goodsIdsString = selRow[0].goodsId;
            var skuIdsString=selRow[0].skuId;
            for (var i = 1; i < selRow.length; i++) {
                goodsIdsString = goodsIdsString + ',' + selRow[i].goodsId;
                skuIdsString=skuIdsString+','+selRow[i].skuId;
            }
            /**加载该活动的分组**/
            var params = {};
            params['activityId'] = activityId;
            $('#groupName').combobox({
                url: ctx + '/application/activity/loalgroupIds',
                queryParams: params,
                onLoadSuccess: function (object) {
                    var l = object.length;
                    if (l > 0) {
                        var $win;
                        $win = $('#addGoodsToGroup').window({
                            title:"<font color='black'>添加至分组</font>",
                            width: 300,
                            height: 150,
                            top:180,
                            left:320,
                            shadow: true,
                            modal:true,
                            closed:true,
                            minimizable:false,
                            maximizable:false,
                            collapsible:false
                        });
                        $win.window('open');
                        $("#addGoodsToGroupActivityId").val(activityId);
                        $("#addGoodsToGroupGoodsId").val(goodsIdsString);
                        $("#addGoodsToGroupSkuId").val(skuIdsString);
                    } else {
                        alert("请先创建分组！");
                    }
                },
                valueField: 'id',
                textField: 'text'
            });
        }
    });

    $.editGroups = function (groupName,goodsSum, orderSort, id) {
        //首先清空input 和 div内容
        $("#groupNameEdit").textbox("setValue", groupName);
        $("#groupIdEdit").val(id);
        $("#sordGroupEdit").textbox("setValue", orderSort);
        $("#goodsList").empty();

        //此时应该填充分组编辑页面的商品列表
        $('#goodsList').datagrid({
            title: '编辑商品',
            fit: true,
            fitColumns: true,
            rownumbers: true,
            pagination: true,
            singleSelect: true,
            striped: true,
            nowrap: false,
            rowStyler: function (rowIndex, rowData) {
                if (rowData.colFalgt == '1') {
                    return 'background-color:#6293BB;';
                }
            },
            columns: [[
            {
                title: '商品编号',
                field: 'goodsCode',
                width: 150,
                align: 'center'
            }, {
                title: 'skuid',
                field: 'skuId',
                width: 150,
                align: 'center'
            }, {
                title: '商品名称',
                field: 'goodsName',
                width: 120,
                align: 'center',
                formatter: function (value, row, index) {
                    if (null == value || "null" == value)
                        value = "";
                    var msg = value + "";
                    return "<div  title='" + value + "'>" + value + "</div>";
                }
            },
                {
                    title: '商品状态',
                    field: 'goodsStatus',
                    width: 120,
                    align: 'center',
                    formatter: function (value, row, index) {
                        if (value == 'G00') {
                            return "待上架";
                        } else if (value == 'G01') {
                            return "待审核";
                        } else if (value == 'G02') {
                            return "已上架";
                        } else if (value == 'G03') {
                            return "已下架";
                        } else if (value == 'G04') {
                            return "待审核";
                        }
                    }
                }, {
                    title: '商品类目（三级）',
                    field: 'goodsCategory',
                    width: 120,
                    align: 'center'
                }, {
                    title: '成本价',
                    field: 'goodsCostPrice',
                    width: 120,
                    align: 'center'
                }, {
                    title: '售价',
                    field: 'goodsPrice',
                    width: 120,
                    align: 'center'
                }, {
                    title: '市场价',
                    field: 'marketPrice',
                    width: 120,
                    align: 'center'
                }, {
                    title: '活动价',
                    field: 'activityPrice',
                    width: 120,
                    align: 'center'
                }, {
                    title: '分组',
                    field: 'groupName',
                    width: 120,
                    align: 'center'
                }, {
                    title: '操作',
                    field: 'opt',
                    width: 120,
                    align: 'center',
                    formatter: function (value, row, index) {
                        var content = "";
                        content += "<a href='javascript:void(0);' class='easyui-linkedbutton'";
                        content += " onclick='$.optEdit(\"" + encodeURI(JSON.stringify(row)) + "\",\"" + index + "\");'>操作</a>&nbsp;";
                        return content;
                    }
                }]],
                queryParams:{"groupId": id},
                onLoadSuccess:function(data){  
                    if(data.total == 0){  
                    	var body = $(this).data().datagrid.dc.body2;
                        body.find('table tbody').append('<tr><td colspan="11" style="height: 35px; text-align: center;border:0px solid;"><h1>请先向该分组添加商品!</h1></td></tr>'); 
                    }
              }, 
            loader: function (param, success, error) {
                $.ajax({
                    url: ctx + '/application/activity/list',
                    data: param,
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        console.log(data);
                        $.validateResponse(data, function () {
                            success(data);
                        });
                    }
                })
            }
        });

        $("#editGroupDiv").dialog({
            modal: true,
            title: "<span style='color: black'>编辑</span>",
            resizable: false,
            width: 800,
            top:30,
            buttons: [{
                text: "确定",
                handler: function () {
                    var id = $("#groupIdEdit").val();
                    var groupName = $("#groupNameEdit").textbox('getValue');
                    var orderSort = $("#sordGroupEdit").textbox('getValue');
                    var activityId = $("#addGoodsToGroupActivityId").val();
                    if(groupName.length == 0){
                    	$.messager.alert("<span style='color: black;'>提示</span>","分组名称不能为空!","info");
                    	return;
                    }
                    if(orderSort.length == 0){
                    	$.messager.alert("<span style='color: black;'>提示</span>","分组排序不能为空!","info");
                    	return;
                    }
                    $.ajax({
                        type: "POST",
                        url: ctx + '/group/manager/edit/save',
                        data: {"groupName": groupName, "orderSort": orderSort, "id": id,"activityId":activityId},
                        success: function (data) {
                            ifLogout(data);
                            if (data.status == 1) {
                                $("#editGroupDiv").dialog("close");
                                $.messager.alert('提示', data.msg, 'success');
                                $('#activityGroupList').datagrid("load", {"activityId": activityId});
                            } else {
                                $.messager.alert('提示', data.msg, 'error');
                            }
                        }
                    });

                }
            }, {
                text: "取消",
                handler: function () {
                    $("#editGroupDiv").dialog("close");
                }
            }]
        });
       // var evt = window.event || arguments.callee.caller.arguments[0]; //获取event对象
       // $('#editGroupDiv').window("resize",{top:evt.pageY * 0.5});//居中显示
       // $("#editGroupDiv").window("open");
    }

    var groupGoodsId,//分组id
    groupGoodsIndex;//对应商品索引
    // 操作菜单显示
    $.optEdit = function (row, index) {
        var response = JSON.parse(decodeURI(row));
        groupGoodsId = response.id;
        groupGoodsIndex = index;
        isFirstOne = response.isFirstOne;
        isLastOne = response.isLastOne;
        $('#optMenu').menu('enableItem', '#moveCategory');
        $('#optMenu').menu('enableItem', '#downCategory');

        if (isFirstOne) {
            $('#optMenu').menu('disableItem', '#moveCategory');
        }
        if (isLastOne) {
            $('#optMenu').menu('disableItem', '#downCategory');
        }
        var evt = window.event || arguments.callee.caller.arguments[0]; //获取event对象
        $('#optMenu').menu('show', {
            left: evt.pageX,
            top: evt.pageY
        });
    }

    $('#optMenu').menu({
        onClick: function (item) {
            if (item.text == '删除') {
                $.messager.confirm('<font color="black">删除确认</font>', '确定要删除么?', function (r) {
                    if (!r) {
                        return;
                    }
                    var params = {
                        "id": groupGoodsId
                    };
                    $.ajax({
                        type: "POST",
                        url: ctx + '/application/activity/removeGoods',
                        data: params,
                        dateType: "json",
                        success: function (data) {
                            debugger;
                            ifLogout(data);
                            if (data.status == "1") {
                                $.messager.alert("提示", data.msg, 'info');
                                //刷新分组编辑页面的商品列表
                                $('#goodsList').datagrid('load',{"groupId": data.data.id});
                                $('#activityGroupList').datagrid('load', {"activityId":data.data.activityId});
                                //刷新活动编辑页面下的商品列表
                                $('#importFileList').datagrid('load', {"activityId":data.data.activityId});
                            } else {
                                $.messager.alert("错误",data.msg,'error');
                            }
                        }
                    });
                });
            } else if (item.text == '上移') {
                debugger;
                if (groupGoodsIndex != 0) {
                    var toup = $('#goodsList').datagrid('getData').rows[groupGoodsIndex];//当前
                    var todown = $('#goodsList').datagrid('getData').rows[new Number(groupGoodsIndex) - 1];
                    var params = {
                            "subjectId" : toup.id,
                            "passiveId" : todown.id
                    };
                    $.ajax({
                        type : "POST",
                        url : ctx + '/application/activity/edit/sort/save',
                        data : params,
                        dateType : "json",
                        success : function(data) {
                            debugger;
                            ifLogout(data);
                            if (data.status == "1") {
                                $.messager.alert("提示", data.msg, 'info');
                                // 关闭弹出窗
                                $('#goodsList').datagrid('load',{"groupId": data.data.groupId});
                            } else {
                                $.messager.alert("错误", data.msg, 'error');
                            }
                        }
                    });
                }

            } else if (item.text == '下移') {
                debugger;
                var rows = $('#goodsList').datagrid('getRows').length;
                if (groupGoodsIndex != rows - 1) {
                    var toup = $('#goodsList').datagrid('getData').rows[groupGoodsIndex];//当前
                    var todown = $('#goodsList').datagrid('getData').rows[new Number(groupGoodsIndex) + 1];
                    var params = {
                        "subjectId" : toup.id,
                        "passiveId" : todown.id
                    };
                    $.ajax({
                        type : "POST",
                        url : ctx + '/application/activity/edit/sort/save',
                        data : params,
                        dateType : "json",
                        success : function(data) {
                            debugger;
                            ifLogout(data);
                            if (data.status == "1") {
                                $.messager.alert("提示", data.msg, 'info');
                                // 关闭弹出窗
                                $('#goodsList').datagrid('load',{"groupId": data.data.groupId});
                            } else {
                                $.messager.alert("错误", data.msg, 'error');
                            }
                        }
                    });
                }
            }
        }
    });

    $.deleteGroups = function ($this, id) {
        $.messager.confirm('<font color="black">确认</font>', '您确认想要删除当前分组吗？', function (r) {
            if (r) {
                var activityId = $("#addGoodsToGroupActivityId").val();
                $.ajax({
                    url: ctx + '/group/manager/delete',
                    data: {"id": id},
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        alert(data.msg);
                        $('#activityGroupList').datagrid("load", {"activityId": activityId});
                    }
                })
            }
        });
    }


    $("#okButton").click(function () {
        window.location.href = ctx + "/activity/cfg/activity";
    });
});

//判断是否超时
function ifLogout(data) {
    if (data.message == 'timeout' && data.result == false) {
        $.messager.alert("操作提示", "登录超时, 请重新登录", "info");
        window.top.location = ctx + "/logout";
        return false;
    }
}