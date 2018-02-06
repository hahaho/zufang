/**
 * GOODS-category
 */
$(function () {
    $("#editCategoryDetail").window('close');
    $("#addCategoryDetail").window('close');
    $("#revlenceGoodsAttrWindow").window('close');

    var categoryId;
    var categoryName;
    var categoryLevel;
    var parentId;
    var rowIndex;
    var ifClickAndLevel;
    var clickPid;
    var picUrl;

    $('#westAttrDataGrid')
        .datagrid(
            {
                striped: true,
                fitColumns: true,
                rownumbers: true,
                singleSelect: true,
                pagination: false,
                columns: [[
                    {
                        width: '100',
                        title: '类目名称',
                        field: 'categoryName',
                        align: 'center',
                    },
                    {
                        width: '50',
                        title: '级别',
                        field: 'level',
                        align: 'center',
                        hidden: 'hidden',
                    },
                    {
                        width: '50',
                        title: '排序序号',
                        field: 'sortOrder',
                        align: 'center',
                        hidden: 'hidden',
                    },
                    {
                        width: '50',
                        title: '类目 id',
                        field: 'categoryId',
                        align: 'center',
                        hidden: 'hidden',
                    },
                    {
                        width: '200',
                        title: '操作',
                        field: 'opt',
                        align: 'center',
                        formatter: function (value, row, index) {
                            var content = "";
                            // 编辑
                            content += "<a href='javascript:void(0);' class='easyui-linkedbutton'";
                            content += " onmouseover='$.optEdit(\""
                                + encodeURI(JSON.stringify(row))
                                + "\",\"" + index
                                + "\");'>操作</a>&nbsp;";
                            return content;
                        }
                    }]],
                onClickRow: function (index, row) {
                    firstClickRowFunction(index, row)
                },
                loader: function (param, success, error) {
                    $.ajax({
                        url: ctx + '/categoryinfo/category/list',
                        data: param,
                        type: "get",
                        dataType: "json",
                        success: function (data) {
                            $.validateResponse(data, function () {
                                success(data.rows);
                            });
                        }
                    })
                }
            });
    // 操作菜单显示
    $.optEdit = function (row, index) {
        var response = JSON.parse(decodeURI(row));
        showCategroyName(response.level);
        categoryId = response.categoryId;
        categoryLevel = response.level;
        categoryName = response.categoryName;
        isFirstOne = response.isFirstOne;
        isLastOne = response.isLastOne;
        parentId = response.parentId;
        rowIndex = index;
        picUrl = response.pictureUrl;
        $('#optMenu').menu('enableItem', '#moveCategory');
        $('#optMenu').menu('enableItem', '#downCategory');

        if (isFirstOne) {
            $('#optMenu').menu('disableItem', '#moveCategory');
        }
        if (isLastOne) {
            $('#optMenu').menu('disableItem', '#downCategory');
        }
        var evt = window.event || arguments.callee.caller.arguments[0]; //获取event对象
        if(categoryLevel != '1'){
            $(".reverlanceGoodsAttrClass").css("display","none");
        }else{
            $(".reverlanceGoodsAttrClass").show();
        }
        $('#optMenu').menu('show', {
            left: evt.pageX,
            top: evt.pageY
        });
    }

    $('#optMenu').menu({
        onClick: function (item) {
            if (item.text == '编辑') {
                $("#editShowCategoryPicId").css("display", "none");
                $("#editShowCategoryPicId").attr("src", '');
                $("#editCategoryLevel").val(categoryLevel);
                $("#editCategoryFilePic").val('');
                $("#editCategoryDetail").window('open');
                if (categoryLevel == 3 || categoryLevel == 1) {
                    if ($.trim(picUrl) != '') {
                        $("#editShowCategoryPicId").css("display", "block");
                        if (categoryLevel == 3) {
                            $("#editShowCategoryPicId").removeAttr("width", '');
                            $("#editShowCategoryPicId").removeAttr("height", '');
                        }
                        loadPic("editShowCategoryPicId", picUrl);
                    }
                }

                $("#editCategoryName").textbox('setValue',
                    categoryName);
            } else if(item.text == '关联商品属性'){
                $("#revlenceGoodsAttrWindow").window('open');
                $('#revlenceGoodsAttrDiv').datagrid({
                    title: '关联商品属性',
                    fit: true,
                    idField: 'id',
                    treeField: 'text',
                    fitColumns: false,
                    rownumbers: true,
                    singleSelect: true,
                    striped: true,
                    queryParams: {
                        'categoryId': categoryId
                    },
                    columns: [[{
                        title: '商品属性名称',
                        field: 'name',
                        width: 100,
                        align: 'center'
                    }, {
                            title: '操作',
                            field: 'opt',
                            witch: 150,
                            align: 'center',
                            formatter: function (value, row, index) {
                                if (row.flag) {
                                    var checkboxflag = '<div class="border-circle" style="cursor:pointer"><span class="switch-circle"  style="left:54px" data-sku="' + encodeURI(JSON.stringify(row)) + '"></span><span class="relation-text" style="left:14px;">已关联</span></div>';
                                } else {
                                    var checkboxflag = '<div class="border-circle" style="cursor:pointer"><span class="switch-circle" style="left:0px"  data-sku="' + encodeURI(JSON.stringify(row)) + '"></span><span class="relation-text" style="left:27PX;">未关联</span></div>';
                                }
                                return checkboxflag;
                            }
                        }]],
                    loader : function(param, success, error){
                        $.ajax({
                            url : ctx + "/application/goods/goodsAttr/allGoodsAttr",
                            data : param,
                            type : "GET",
                            dataType : "json",
                            success : function(resp) {
                                $.validateResponse(resp, function () {
                                    success(resp.data);
                                });

                                $('.datagrid-cell').on('click','.border-circle',function(){
                                    if($(".border-circle").hasClass('disabled')){
                                        return;
                                    }
                                    $(".border-circle").addClass('disabled');
                                    var $that = $(this).find('.switch-circle');

                                    var rowData = JSON.parse(decodeURI($that.attr('data-sku')));
                                    var param = {
                                        "categoryId1":categoryId,
                                        "goodsAttrId":rowData.id
                                    };

                                    if($that.css('left')=='0px') {
                                        $.ajax({
                                            url : ctx + '/categoryinfo/category/revlenceGoodsAttr',
                                            data : param,
                                            type : "post",
                                            dateType:"json",
                                            success : function(data) {
                                                debugger;
                                                ifLogout(data);
                                                if(data.status=="1"){
                                                    debugger;
                                                    $that.animate({left:"54px"},50)
                                                    $that.parent().find('.relation-text').css('left','10px');
                                                    $that.parent().find('.relation-text').html('已关联');
                                                    // $.messager.alert("提示",data.msg,'info');
                                                    $(".border-circle").removeClass('disabled');
                                                }else{
                                                    $.messager.alert("错误",data.msg,'error');
                                                    $(".border-circle").removeClass('disabled');
                                                }
                                            }
                                        });
                                    } else if($that.css('left')=='54px'){
                                        // $.messager.confirm('确认','您确认想要取消关联吗？',function(r){
                                        //     if (r){
                                                $.ajax({
                                                    url : ctx + '/categoryinfo/category/disRevlenceGoodsAttr',
                                                    data : param,
                                                    type : "post",
                                                    dateType:"json",
                                                    success : function(data) {
                                                        debugger;
                                                        ifLogout(data);
                                                        $(".border-circle").removeClass("disabled");
                                                        if(data.status=="1"){
                                                            $that.animate({left:"0px"},50)
                                                            $that.parent().find('.relation-text').css('left','27px');
                                                            $that.parent().find('.relation-text').html('未关联');
                                                            // $.messager.alert("提示",data.msg,'info');
                                                            $(".border-circle").removeClass('disabled');
                                                        }else{
                                                            $.messager.alert("错误",data.msg,'error');
                                                            $(".border-circle").removeClass('disabled');
                                                        }
                                                    }
                                                });

                                            // }
                                        // });
                                    }
                                })
                            }
                        })
                    }
                })

            }else if (item.text == '删除') {
                $.messager.confirm('删除确认', '确定要删除么?删除后不可恢复', function (r) {
                    if (!r) {
                        return;
                    }
                    var params = {
                        "id": categoryId
                    };
                    $.ajax({
                        type: "POST",
                        url: ctx
                        + '/categoryinfo/category/delete',
                        data: "id="
                        + categoryId,
                        dateType: "json",
                        success: function (data) {
                            ifLogout(data);
                            if (data.status == "1") {
                                $.messager.alert("提示", data.msg, 'info');
                                // 关闭弹出窗
                                // var
                                // params={'categoryId':categoryId};
                                if (categoryLevel == '1') {
                                    $(
                                        '#westAttrDataGrid')
                                        .datagrid(
                                            'load',
                                            {
                                                'categoryId': categoryId
                                            });
                                } else if (categoryLevel == '2') {
                                    $(
                                        '#centerAttrDataGrid')
                                        .datagrid(
                                            'load',
                                            {
                                                'parentId': parentId
                                            });
                                } else if (categoryLevel == '3') {
                                    $(
                                        '#eastAttrDataGrid')
                                        .datagrid(
                                            'load',
                                            {
                                                'parentId': parentId
                                            });
                                }
                            } else {
                                $.messager
                                    .alert(
                                        "错误",
                                        data.msg,
                                        'error');
                            }
                        }
                    });
                });
            } else if (item.text == '上移') {
                if (categoryLevel == '1') {
                    mysort(rowIndex, 'up', 'westAttrDataGrid',
                        categoryLevel, categoryId, parentId);
                } else if (categoryLevel == '2') {
                    mysort(rowIndex, 'up',
                        'centerAttrDataGrid',
                        categoryLevel, categoryId, parentId);
                } else if (categoryLevel == '3') {
                    mysort(rowIndex, 'up', 'eastAttrDataGrid',
                        categoryLevel, categoryId, parentId);
                }
            } else if (item.text == '下移') {
                if (categoryLevel == '1') {
                    mysort(rowIndex, 'down',
                        'westAttrDataGrid', categoryLevel,
                        categoryId, parentId);
                } else if (categoryLevel == '2') {
                    mysort(rowIndex, 'down',
                        'centerAttrDataGrid',
                        categoryLevel, categoryId, parentId);
                } else if (categoryLevel == '3') {
                    mysort(rowIndex, 'down',
                        'eastAttrDataGrid', categoryLevel,
                        categoryId, parentId);
                }
            }
        }
    });

    // 确认编辑
    $("#editConfirmGoodCategory").click(function () {
        var categoryName = $("#editCategoryName").textbox('getValue');

        if (categoryName == null || categoryName == '') {
            $.messager.alert("提示", "请输入类目名称", "info");
            return;
        }
        if (categoryLevel == 1) {
            reg = /^[\u2E80-\u9FFF]+$/;
            if (!regExp_pattern(categoryName, reg)) {
                $.messager.alert("提示", "类目名称格式不正确，只能输入汉字,请重新输入", "info");
                return;
            }
            if (categoryName.length > 2) {
                $.messager.alert("提示", "类目名称不能超过2个汉字", "info");
                return;
            }
        } else if (categoryLevel == 2) {
            reg = /^[\u4E00-\u9FA5]+$/;
            var leng = getLenString(categoryName);
            if (!regExp_pattern(categoryName, reg)) {
                $.messager.alert("提示", "类目名称格式不正确，只能输入汉字,请重新输入", "info");
                return;
            }
            if (leng > 8) {
                $.messager.alert("提示", "字数超过最大长度，请重新输入。", "info");
                return;
            }
        } else if (categoryLevel == 3) {
            if (categoryName.length > 20) {
                $.messager.alert("提示", "字数长度不能大于20", "info");
                return;
            }
        }

        var leng = getLenString(categoryName);
        if (leng > 15) {
            $.messager.alert("提示", "字数超过最大长度，请重新输入。", "info");
            return;
        }

        var params = {
            'categoryId': categoryId,
            'categoryName': categoryName,
            'level': categoryLevel,
            'pictureUrl': picUrl
        };
        $.ajax({
            type: "POST",
            url: ctx + '/categoryinfo/category/edit',
            data: JSON.stringify(params),
            contentType: 'application/json',
            dateType: "json",
            success: function (data) {
                ifLogout(data);
                if (data.status == "1") {
                    $.messager.alert("提示", data.msg, 'info');
                    // 关闭弹出窗
                    $('#editCategoryDetail').window('close');
                    // var params={'categoryId':categoryId};
                    if (categoryLevel == '1') {
                        $('#westAttrDataGrid').datagrid('load', {
                            'categoryId': categoryId
                        });
                    } else if (categoryLevel == '2') {
                        $('#centerAttrDataGrid').datagrid('load', {
                            'parentId': parentId
                        });
                    } else if (categoryLevel == '3') {
                        $('#eastAttrDataGrid').datagrid('load', {
                            'parentId': parentId
                        });
                    }
                } else {
                    $.messager.alert("错误", data.msg, 'error');
                }
            }
        });
    });
    // 取消编辑
    $("#editDisGoodCategory").click(function () {
        $("#editCategoryDetail").window('close');
    });
    // 刷新分类
    $("#flashCategory").click(function () {
        $.ajax({
            url: ctx + '/categoryinfo/category/refresh',
            data: {},
            type: "POST",
            contentType: 'application/json',
            dataType: "json",
            success: function (data) {
                $.messager.alert("提示", data.msg, 'info');
            }
        });
    });
    // 添加一级类目
    $("#addFirstCategory").click(function () {
        picUrl == '';
        showCategroyName('1');
        $("#addCategoryName").textbox("clear");
        $("#addCategoryDetail").window('open');
        categoryLevel = 1;
        clickPid = null;
        $("#addCategoryFilePic").val('');
        loadPic("addShowCategoryPicId", null);
    });
    // 添加二级类目
    $("#addSecondCategory").click(function () {
        if (ifClickAndLevel != 1) {
            $.messager.alert("提示", "请先选中一个一级类目", "info");
            return;
        }

        showCategroyName('2');
        $("#addCategoryName").textbox("clear");
        $("#addCategoryDetail").window('open');
        categoryLevel = 2;
    });
    // 添加三级类目
    $("#addThirdCategory").click(function () {
        picUrl = '';
        if (ifClickAndLevel != 2) {
            $.messager.alert("提示", "请先选中一个二级类目", "info");
            return;
        }
        showCategroyName('3');
        $("#addCategoryName").textbox("clear");
        $("#addCategoryDetail").window('open');
        categoryLevel = 3;
        $("#addCategoryFilePic").val('');
        loadPic("addShowCategoryPicId", null);
    });
    // 取消添加
    $("#addDisGoodCategory").click(function () {
        $("#addCategoryDetail").window('close');
    });

    // 确认添加
    $("#addConfirmGoodCategory").click(function () {
        var reg;
        var categoryName = $("#addCategoryName").textbox('getValue');
        var addCategoryFilePic = $('#addCategoryFilePic').val();
        if (categoryName == null || categoryName == '') {
            $.messager.alert("提示", "请输入类目名称", "info");
            return;
        }
        if (categoryLevel == 1) {
            if (null == addCategoryFilePic || ('') == addCategoryFilePic) {
                $.messager.alert("提示", "请先上传文件！", "info");
                return;
            }
        }
        if (categoryLevel == 1) {
            reg = /^[\u2E80-\u9FFF]+$/;
            if (!regExp_pattern(categoryName, reg)) {
                $.messager.alert("提示", "类目名称格式不正确，只能输入汉字,请重新输入", "info");
                return;
            }
            if (categoryName.length > 2) {
                $.messager.alert("提示", "类目名称不能超过2个汉字", "info");
                return;
            }
        } else if (categoryLevel == 2) {
            reg = /^[\u4E00-\u9FA5]+$/;
            var leng = getLenString(categoryName);
            if (!regExp_pattern(categoryName, reg)) {
                $.messager.alert("提示", "类目名称格式不正确，只能输入汉字,请重新输入", "info");
                return;
            }
            if (leng > 8) {
                $.messager.alert("提示", "字数超过最大长度，请重新输入。", "info");
                return;
            }
        } else if (categoryLevel == 3) {
            if (categoryName.length > 20) {
                $.messager.alert("提示", "字数长度不能大于20", "info");
                return;
            }
        }
        if ((categoryLevel == 1) && (picUrl == null || picUrl == '')) {
            $.messager.alert("提示", "请先上传图片。", "info");
            return;
        }

        var params = {
            "categoryName": categoryName,
            "level": categoryLevel,
            "parentId": clickPid,
            "pictureUrl": picUrl
        };
        $.ajax({
            type: "POST",
            url: ctx + '/categoryinfo/category/add',
            data: JSON.stringify(params),
            contentType: 'application/json',
            dateType: "json",
            success: function (data) {
                debugger;
                ifLogout(data);
                if (data.status == "1") {
                    $.messager.alert("提示", data.msg, 'info');
                    // 关闭弹出窗
                    $('#addCategoryDetail').window('close');
                    if (categoryLevel == '1') {
                        $('#westAttrDataGrid').datagrid('load', {
                            'categoryId': categoryId
                        });
                    } else if (categoryLevel == '2') {
                        $('#centerAttrDataGrid').datagrid('load', {
                            'parentId': clickPid
                        });
                    } else if (categoryLevel == '3') {
                        $('#eastAttrDataGrid').datagrid('load', {
                            'parentId': clickPid
                        });
                    }
                } else {
                    $.messager.alert("错误", data.msg, 'error');
                }
            }
        });
    });

    // 添加上传图标
    $("#addUpLogoBtn").click(function () {
        var addCategoryFilePic = $('#addCategoryFilePic').val();
        if (null == addCategoryFilePic || ("") == addCategoryFilePic) {
            $.messager.alert("提示", "请选择文件！", "info");
            return;
        }

        // 提交from
        var thisform = $("#addCategoryFilePicForm");
        thisform.form("submit", {
            url: ctx + '/categoryinfo/category/addpic',
            success: function (data) {
                var response = JSON.parse(data);
                ifLogout(response);
                if (response.status == "1") {
                    $.messager.alert("提示", response.msg, 'info');
                    picUrl = response.data;
                    loadPic("addShowCategoryPicId", picUrl);
                } else {
                    $.messager.alert("错误", response.msg, 'error');
                }
            }
        });
    });
    // 编辑上传图标
    $("#editUpLogoBtn").click(function () {
        var editCategoryFilePic = $('#editCategoryFilePic').val();
        if (null == editCategoryFilePic || ("") == editCategoryFilePic) {
            $.messager.alert("提示", "请选择文件！", "info");
            return;
        }

        // 提交form
        var thisform = $("#editCategoryFilePicForm");
        thisform.form("submit", {
            url: ctx + '/categoryinfo/category/addpic',
            success: function (data) {
                debugger;
                var response = JSON.parse(data);
                ifLogout(response);
                if (response.status == "1") {
                    $.messager.alert("提示", response.msg, 'info');
                    picUrl = response.data;
                    if ($.trim(picUrl) != '') {
                        $("#editShowCategoryPicId").css("display", "block");
                        loadPic("editShowCategoryPicId", picUrl);
                    }
                } else {
                    $.messager.alert("错误", response.msg, 'error');
                }
            }
        });
    });

    /** 单击一级类目执行方法* */
    function firstClickRowFunction(indexFirst, rowFirst) {
        var id = rowFirst.categoryId;
        clickPid = rowFirst.categoryId;
        ifClickAndLevel = rowFirst.level;
        // $('#eastAttrDataGrid').datagrid({
        // url : ctx + '/categoryinfo/category/list',
        // rownumbers : false,
        // queryParams:{
        // 'parentId':'null',
        // }
        // })
        debugger;
        $('#eastAttrDataGrid').datagrid({
            rownumbers: false,
            striped: false,
            loader: function (param, success, error) {
                $.ajax({
                    url: ctx + '/categoryinfo/category/list',
                    data: {
                        'parentId': -2,
                    },
                    type: "get",
                    dataType: "json",
                    success: function (data) {
                        $.validateResponse(data, function () {
                            success(data.rows);
                        });
                    }
                })
            }
        });
        $('#centerAttrDataGrid')
            .datagrid(
                {
                    striped: true,
                    fitColumns: true,
                    rownumbers: true,
                    singleSelect: true,
                    queryParams: {
                        'parentId': id,
                    },
                    columns: [[
                        {
                            width: '100',
                            title: '类目名称',
                            field: 'categoryName',
                            align: 'center',
                        },
                        {
                            width: '50',
                            title: '级别',
                            field: 'level',
                            align: 'center',
                            hidden: 'hidden',
                        },
                        {
                            width: '50',
                            title: '排序序号',
                            field: 'sortOrder',
                            align: 'center',
                            hidden: 'hidden',
                        },
                        {
                            width: '50',
                            title: '类目 id',
                            field: 'categoryId',
                            align: 'center',
                            hidden: 'hidden',
                        },
                        {
                            width: '50',
                            title: '父id',
                            field: 'parentId',
                            align: 'center',
                            hidden: 'hidden',
                        },
                        {
                            width: '200',
                            title: '操作',
                            field: 'opt',
                            align: 'center',
                            formatter: function (value, row, index) {
                                var content = "";
                                // 编辑
                                content += "<a href='javascript:void(0);' class='easyui-linkedbutton'";
                                content += " onmouseover='$.optEdit(\""
                                    + encodeURI(JSON
                                        .stringify(row))
                                    + "\",\""
                                    + index
                                    + "\");'>操作</a>&nbsp;";
                                return content;
                            }
                        }]],
                    onClickRow: function (index, row) { // easyui封装好的事件（被单机行的索引，被单击行的值）
                        secondClickRowFunction(index, row);
                    },
                    loader: function (param, success, error) {
                        $.ajax({
                            url: ctx + '/categoryinfo/category/list',
                            data: param,
                            type: "get",
                            dataType: "json",
                            success: function (data) {
                                $.validateResponse(data, function () {
                                    success(data.rows);
                                });
                            }
                        })
                    }
                });
    }

    /** 单击二级类目执行方法* */
    function secondClickRowFunction(indexSecond, rowSecond) {
        var id = rowSecond.categoryId;
        clickPid = rowSecond.categoryId;
        ifClickAndLevel = rowSecond.level;
        $('#eastAttrDataGrid')
            .datagrid(
                {
                    striped: true,
                    fitColumns: true,
                    rownumbers: true,
                    singleSelect: true,
                    queryParams: {
                        'parentId': id,
                    },
                    sortOrder: 'asc',
                    columns: [[
                        {
                            width: '100',
                            title: '类目名称',
                            field: 'categoryName',
                            align: 'center',
                        },
                        {
                            width: '100',
                            title: '级别',
                            field: 'level',
                            align: 'center',
                            hidden: 'hidden',
                        },
                        {
                            width: '50',
                            title: '排序序号',
                            field: 'sortOrder',
                            align: 'center',
                            hidden: 'hidden',
                            sortable: true
                        },
                        {
                            width: '50',
                            title: '类目 id',
                            field: 'categoryId',
                            align: 'center',
                            hidden: 'hidden',
                        },
                        {
                            width: '50',
                            title: '父id',
                            field: 'parentId',
                            align: 'center',
                            hidden: 'hidden',
                        },
                        {
                            width: '50',
                            title: '图片url',
                            field: 'pictureUrl',
                            align: 'center',
                            hidden: 'hidden',
                        },
                        {
                            width: '200',
                            title: '操作',
                            field: 'opt',
                            align: 'center',
                            formatter: function (value, row, index) {
                                var content = "";
                                // 编辑
                                content += "<a href='javascript:void(0);' class='easyui-linkedbutton'";
                                content += " onmouseover='$.optEdit(\""
                                    + encodeURI(JSON
                                        .stringify(row))
                                    + "\",\""
                                    + index
                                    + "\");'>操作</a>&nbsp;";
                                return content;
                            }
                        }]],
                    loader: function (param, success, error) {
                        $.ajax({
                            url: ctx + '/categoryinfo/category/list',
                            data: param,
                            type: "get",
                            dataType: "json",
                            success: function (data) {
                                $.validateResponse(data, function () {
                                    success(data.rows);
                                });
                            }
                        })
                    }
                });
    }

});

// 判断是否超时
function ifLogout(data) {
    if (data.message == 'timeout' && data.result == false) {
        $.messager.alert("操作提示", "登录超时, 请重新登录", "info");
        window.top.location = ctx + "/logout";
        return false;
    }
}
// 显示类目名称方法
function showCategroyName(level) {
    $("#addCategoryLevel").val(level);
    if (level == '1') {
        $(".oneCategory").css("display", "inline");
        $(".twoCategory").css("display", "none");
        $(".threeCategory").css("display", "none");
        $(".threeCategoryAndOneCategoryShow").css("display", "block");
    } else if (level == '2') {
        $(".oneCategory").css("display", "none");
        $(".twoCategory").css("display", "inline");
        $(".threeCategory").css("display", "none");
        $(".threeCategoryAndOneCategoryShow").css("display", "none");
    } else if (level == '3') {
        $(".oneCategory").css("display", "none");
        $(".twoCategory").css("display", "none");
        $(".threeCategory").css("display", "inline");
        $(".threeCategoryAndOneCategoryShow").css("display", "block");
    }
}
// 上移下移方法
function mysort(index, type, gridname, categoryLevel, categoryId, parentId) {
    if ("up" == type) {
        debugger;
        if (index != 0) {
            var toup = $('#' + gridname).datagrid('getData').rows[index];
            var todown = $('#' + gridname).datagrid('getData').rows[new Number(
                index) - 1];
            var categoryIdNew = toup.categoryId;
            var sortOrderNew = todown.sortOrder;

            var categoryIdOld = todown.categoryId;
            var sortOrderOld = toup.sortOrder;
            var params = {
                "categoryIdNew": categoryIdNew,
                "sortOrderNew": sortOrderNew,
                "categoryIdOld": categoryIdOld,
                "sortOrderOld": sortOrderOld,
            };

            $.ajax({
                type: "POST",
                url: ctx + '/categoryinfo/category/updateSort',
                data: JSON.stringify(params),
                contentType: 'application/json',
                dateType: "json",
                success: function (data) {
                    debugger;
                    ifLogout(data);
                    if (data.status == "1") {
                        debugger;
                        $.messager.alert("提示", data.msg, 'info');
                        // 关闭弹出窗
                        $('#editCategoryDetail').window('close');
                        // var params={'categoryId':categoryId};
                        if (categoryLevel == '1') {
                            $('#westAttrDataGrid').datagrid('load', {
                                'categoryId': categoryId
                            });
                        } else if (categoryLevel == '2') {
                            $('#centerAttrDataGrid').datagrid('load', {
                                'parentId': parentId
                            });
                        } else if (categoryLevel == '3') {
                            $('#eastAttrDataGrid').datagrid('load', {
                                'parentId': parentId
                            });
                        }
                    } else {
                        $.messager.alert("错误", data.msg, 'error');
                    }
                }
            });
        }
    } else if ("down" == type) {
        debugger;
        var rows = $('#' + gridname).datagrid('getRows').length;
        if (index != rows - 1) {
            var todown = $('#' + gridname).datagrid('getData').rows[index];
            var toup = $('#' + gridname).datagrid('getData').rows[new Number(
                index) + 1];
            var categoryIdNew = toup.categoryId;
            var sortOrderNew = todown.sortOrder;

            var categoryIdOld = todown.categoryId;
            var sortOrderOld = toup.sortOrder;

            var params = {
                "categoryIdNew": categoryIdNew,
                "sortOrderNew": sortOrderNew,
                "categoryIdOld": categoryIdOld,
                "sortOrderOld": sortOrderOld,
            };

            $.ajax({
                type: "POST",
                url: ctx + '/categoryinfo/category/updateSort',
                data: JSON.stringify(params),
                contentType: 'application/json',
                dateType: "json",
                success: function (data) {
                    debugger;
                    ifLogout(data);
                    if (data.status == "1") {
                        $.messager.alert("提示", data.msg, 'info');
                        // 关闭弹出窗
                        $('#editCategoryDetail').window('close');
                        // var params={'categoryId':categoryId};
                        if (categoryLevel == '1') {
                            $('#westAttrDataGrid').datagrid('load', {
                                'categoryId': categoryId
                            });
                        } else if (categoryLevel == '2') {
                            $('#centerAttrDataGrid').datagrid('load', {
                                'parentId': parentId
                            });
                        } else if (categoryLevel == '3') {
                            $('#eastAttrDataGrid').datagrid('load', {
                                'parentId': parentId
                            });
                        }
                    } else {
                        $.messager.alert("错误", data.msg, 'error');
                    }
                }
            });
        }
    }
}
/** 回显图标* */
function loadPic(id, pictureUrl) {
    if (pictureUrl != null) {
        debugger;
        $("#" + id).attr("src", "");
        $("#" + id).attr("src", ctx + "/fileView/query?picUrl=" + pictureUrl);
    } else {
        $("#" + id).attr("src", '');
    }
}
/** 正则校验* */
function regExp_pattern(str, pattern) {
    return pattern.test(str);
}
