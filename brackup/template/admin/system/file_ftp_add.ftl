<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<script type="text/javascript">
    $(function(){
        $("#saveForm").ajaxForm(function(data){
            $('#view').reload();
            top.$.msgbox({
                msg : "保存成功",
                icon : "success"
            });
            $(".back-page",$("#saveForm")).backpage();
        });
    });
</script>
</@override>
<@override name="container">
<form id="saveForm" action="${request.contextPath}/admin/system/file/ftp_save.do" method="post">
    <table class="formTable mb3">
        <caption>新增FTP站点<a href="###" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
        <tbody>
        <tr>
            <td class="formItem_title w100">名称：</td>
            <td class="formItem_content"><@s.textfield name="name" cssClass="w250" /></td>
            <td class="formItem_title w100">编码：</td>
            <td class="formItem_content"><@s.select name="controlEncoding" list=r"#{'UTF-8','GBK'}" listValue="key"/></td>
        </tr>
        <tr>
            <td class="formItem_title w100">地址：</td>
            <td class="formItem_content"><@s.textfield name="hostname" cssClass="w250" /></td>
            <td class="formItem_title w100">端口：</td>
            <td class="formItem_content"><@s.textfield name="port" value="21"/></td>
        </tr>
        <tr>
            <td class="formItem_title ">用户名：</td>
            <td class="formItem_content"><@s.textfield name="username" /></td>
            <td class="formItem_title ">密码：</td>
            <td class="formItem_content"><@s.password name="password" /></td>
        </tr>
        <tr>
            <td class="formItem_title">路径：</td>
            <td class="formItem_content" colspan="3"><@s.textfield name="defaultDir" cssStyle="width:98%;" value="/"/></td>
        </tr>
        <tr>
            <td class="formItem_title">描述</td>
            <td class="formItem_content" colspan="3">
                <@s.textarea name="description" cssClass="KindEditor" cssStyle="width:98%;height:100px;"/>
            </td>
        </tr>
        <tr>
            <td class="formItem_title"></td>
            <td class="formItem_content" colspan="3"><a href="javascript:void(0);" onclick="$('#saveForm').submit();return false;" class="ui_button">保存</a></td>
        </tr>
        </tbody>
    </table>
</form>
</@override>
<@extends name="../base.ftl"/>