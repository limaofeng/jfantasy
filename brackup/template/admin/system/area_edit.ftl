<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<script type="text/javascript">
    $(function(){
        $("#saveForm").ajaxForm(function(data){
            $('#pager').pager().reload();
            top.$.msgbox({
                msg : "保存成功",
                icon : "success"
            });
            $(".back-page",$("#saveForm")).backpage();
            Fantasy.area.reload();
        });
        Fantasy.area.selects($('#rootArea'),'<s:property value="area.path"/>'.replaceAll('.[^,]+$'),$('#parent_id'),'<select style="width:200px;"><option value="">--请选择查询的地区--</option></select>');
    });
</script>
</@override>
<@override name="container">
<form id="saveForm" action="${request.contextPath}/admin/system/area_save.do" method="post">
    <@s.hidden name="id" value="area.id"/>
    <@s.hidden name="parent.id"/>
    <table class="formTable mb3">
        <caption>基本信息<a href="###" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
        <tbody>
        <tr>
            <td class="formItem_title w100">上级：</td>
            <td class="formItem_content">
                <@s.select id="rootArea" headerKey="" headerValue="--添加到当前地区--" list="@com.fantasy.common.service.AreaService@list('')" listKey="id" listValue="name" cssStyle="width:200px;" value="''"/>
            </td>
        </tr>
        <tr>
            <td class="formItem_title w100">编号：</td>
            <td class="formItem_content"><@s.textfield name="id" value="area.id" cssClass="w250" disabled="true"/></td>
        </tr>
        <tr>
            <td class="formItem_title w100">名称：</td>
            <td class="formItem_content"><@s.textfield name="name" value="area.name" cssClass="w250"/></td>
        </tr>
        <tr>
            <td class="formItem_title">排序</td>
            <td class="formItem_content"><@s.textfield name="sort" value="area.sort"  /></td>
        </tr>
        <tr>
            <td class="formItem_title"></td>
            <td class="formItem_content"><a href="javascript:void(0);" onclick="$('#saveForm').submit();return false;" class="ui_button">保存</a></td>
        </tr>
        </tbody>
    </table>
</form>
</@override>
<@extends name="../base.ftl"/>