<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<link rel="stylesheet" href="${request.contextPath}/static/css/pop.css"/>
<script type="text/javascript">
    $(function(){
        var win = $.dialog.open.origin;
        //表单异步提交
        $("#saveForm").ajaxForm(function(data){
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
                    if(configTree.getParentNodes(treeNode).each(function(){
                        if(this.code != $('#code').val()){
                            top.$.msgbox({
                                msg:"不能将上级配置项与当前配置项设置为同一个",
                                icon:"warning"
                            });
                            return false;
                        }
                    })==false){
                        return stopDefault(e);
                    };
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
        });
        configTree.setJSON(Fantasy.config.tree($('#parent_type').val()));
        $('#parentName').click(function(){
            $('#parentSelect').mouseover();
        });
    });
</script>
</@override>
<@override name="container">
<form id="saveForm" action="${request.contextPath}/admin/system/config/update.do" method="post">
    <table class="formTable">
        <tbody>
        <tr>
            <td class="formItem_title" style="width: 125px;">配置项类别 ：</td>
            <td class="formItem_content">
                <@s.select name="type" list="@com.fantasy.system.service.ConfigService@types()" listKey="code" listValue="name" cssStyle="width:207px" value="%{config.type}"/>
            </td>
            <td class="formItem_content" style="width: 3px;"></td>
        </tr>
        <tr>
            <td class="formItem_title w100">配置项编码 ：</td>
            <td class="formItem_content w">
                <@s.hidden name="code" value="%{config.code}"/>
                <@s.textfield name="code" cssClass="w200" value="%{config.code}" disabled="true"/>
            </td>
            <td class="formItem_content" style="width: 120px;"></td>
        </tr>
        <tr>
            <td class="formItem_title">配置项名称 ：</td>
            <td class="formItem_content"><@s.textfield name="name" cssClass="w200" value="%{config.name}"/></td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title" style="width: 125px;">上级配置项类别 ：</td>
            <td class="formItem_content">
                <@s.select name="parent.type" list="@com.fantasy.system.service.ConfigService@types()" listKey="code" headerKey="" headerValue="--请选择--" listValue="name" cssStyle="width:207px" value="%{config.parent.type}"/>
            </td>
            <td class="formItem_content" style="width: 3px;"></td>
        </tr>
        <tr>
            <td class="formItem_title">上级配置项 ：</td>
            <td class="formItem_content">
                <input id="parentName" name="parentName" type="text" readonly="readonly" style="width:200px;" value="<@s.property value="config.parent.name"/>"/>
                <span id="parentSelect" style="position:relative;left:8px;"><a href="javascript:void(0)" class="ico-expand-arrow">上级配置</a></span>
                <@s.hidden name="parent.code" value="%{config.parent.code}"/>
            </td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title" style="word-spacing:20px;">排序 ：</td>
            <td class="formItem_content"><@s.textfield name="sort" cssClass="w200" value="%{config.sort}"/></td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title ">配置描述 ：</td>
            <td class="formItem_content"><@s.textarea name="description" cssStyle="height: 150px;" cssClass="w" rows="" cols="" value="%{config.description}"/></td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title"></td>
            <td class="formItem_content">
                <a class="ui_button" href="javascript:void(0);" onclick="$('#saveForm').submit();return false;">保存</a>
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