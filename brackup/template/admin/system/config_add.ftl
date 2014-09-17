<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<link rel="stylesheet" href="${request.contextPath}/static/css/pop.css"/>
<script type="text/javascript">
    $(function(){
        var win = $.dialog.open.origin;
        //表单异步提交
        $("#saveForm").ajaxForm(function(data){
            if(!data.success){
                return;
            }
            win.$('#pager').pager().reload();
            top.$.msgbox({
                msg : "保存成功",
                icon : "success"
            });
            Fantasy.config.reload();
            setTimeout("$.dialog.close();",10);
        });
        //选择上级分类对话框
        var configTreeBox = $('#parentSelect').selectionBox({
            event : 'mouseover',
            title : $('#parentSelect').find('.ico-expand-arrow').html(),
            content : $('#configTreeBox'),
            top : 0,
            left : 0
        });
        //上级配置项树
        var configTree = $('#configTree').zTree({
            data:{
                key : {
                    name: "name"
                },
                keep: {
                    leaf: true
                },
                simpleData: {
                    enable: true,
                    idKey: "code",
                    pIdKey: "parentCode"
                }
            },
            edit: false,
            callback:{
                onClick : function (e, treeId, treeNode) {
                    $('#parentName').val(treeNode.name);
                    $('[name="parent.code"]').val(treeNode.code);
                    $('[name="parent.type"]').val(treeNode.type);
                    configTreeBox.hide();
                    return false;
                }
            }
        },[]);
        //改变类型时重新加载配置项树
        $('#parent_type').change(function(){
            $('#parentName').val('');
            $('[name="parent.code"]').val('');
            configTree.setJSON(Fantasy.config.tree($(this).val()));
        }).change();
        $('#parentName').click(function(){
            $('#parentSelect').mouseover();
        });
        $('#type').val(win.$('#EQS_type').val()).change();
        //自动生成编码
        $('#autoCode').click(function(){
            $.post($(this).attr('href'),(function(zhis){
                return function(data){
                    zhis.val(data.guid);
                }
        })($(this).parent().prevAll('#code')));
            return false;
        });
    });
</script>
</@override>
<@override name="container">
<form id="saveForm" action="${request.contextPath}/admin/system/config/save.do" method="post">
    <table class="formTable">
        <tbody>
        <tr>
            <td class="formItem_title" style="width: 125px;">配置项类别 ：</td>
            <td class="formItem_content">
                <@s.select name="type" list="@com.fantasy.system.service.ConfigService@types()" listKey="code" listValue="name" cssStyle="width:207px"/>
            </td>
            <td class="formItem_content" style="width: 3px;"></td>
        </tr>
        <tr>
            <td class="formItem_title w100">配置项编码 ：</td>
            <td class="formItem_content w">
                <@s.textfield name="code" cssClass="w200" />
                <@s.fielderror fieldName="code" />
                <span style="position:relative;left:8px;"><a id="autoCode" href="${request.contextPath}/common/guid.do">自动编码</a></span>
            </td>
            <td class="formItem_content" style="width: 120px;"></td>
        </tr>
        <tr>
            <td class="formItem_title">配置项名称 ：</td>
            <td class="formItem_content">
                <@s.textfield name="name" cssClass="w200" />
                <@s.fielderror fieldName="name" />
            </td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title" style="width: 125px;">上级配置项类别 ：</td>
            <td class="formItem_content">
                <@s.select name="parent.type" list="@com.fantasy.system.service.ConfigService@types()" listKey="code" listValue="name" cssStyle="width:207px" headerKey="" headerValue="--请选择--"/>
            </td>
            <td class="formItem_content" style="width: 3px;"></td>
        </tr>
        <tr>
            <td class="formItem_title">上级配置项 ：</td>
            <td class="formItem_content">
                <input id="parentName" type="text" readonly="readonly" style="width:200px;"/>
                <span id="parentSelect" style="position:relative;left:8px;"><a href="javascript:void(0)" class="ico-expand-arrow">上级配置</a></span>
                <@s.hidden name="parent.code" value="config.parent.code"/>
            </td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title" style="word-spacing:20px;">排序 ：</td>
            <td class="formItem_content"><input name="sort" type="text" value="" class="w200" ></td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title ">配置描述 ：</td>
            <td class="formItem_content"><textarea name="description" style="height: 150px;" class="w" rows="" cols=""></textarea> </td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title"></td>
            <td class="formItem_content">
                <a class="ui_button" href="###" onclick="$('#saveForm').submit();return false;">保存</a>
                <a class="ui_button close" href="javascript:$.dialog.close();" style="margin-left: 5px;">取消</a>
            </td>
            <td class="formItem_content"></td>
        </tr>
        </tbody>
    </table>
</form>
<div id="configTreeBox" style="height:187px;padding:3px;display:none;overflow:auto;">
    <ul id="configTree" class="ztree"></ul>
</div>
</@override>
<@extends name="../dialog.ftl"/>