<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>个人中心-修改密码</title>
    <script type="text/javascript">
        $(function(){
            var win = $.dialog.open.origin;
            $("#resetPwdForm").ajaxForm(function(){
                top.$.msgbox({
                    msg : "密码修改成功,将在下次登录时生效!",
                    icon : "success"
                });
                setTimeout("$.dialog.close();",10);
            });
            $('#resetPwdForm').resetForm();
            $(window).resize();
        });

    </script>
</head>
</@override>
<@override name="container">
<form id="resetPwdForm" action="${request.contextPath}/admin/security/user_resetpwd.do" method="post">
    <table class="formTable">
        <caption>修改密码</caption>
        <tbody>
        <tr>
            <td class="formItem_title w100">旧密码 :</td>
            <td class="formItem_content"><input name="oldPwd" type="password" value="" class="ui_input_text w200" /></td>
            <td class="formItem_content w100"></td>
        </tr>
        <tr>
            <td class="formItem_title">新密码 :</td>
            <td class="formItem_content"><input name="newPwd" type="password" value="" class="ui_input_text w200"></td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title"></td>
            <td class="formItem_content">
                <a class="ui_button" href="javascript:void(0);" onclick="$('#resetPwdForm').submit();return false;">修改</a>
                <a class="ui_button close" href="javascript:$('#resetPwdForm').resetForm()" style="margin-left: 5px;">清空</a></td>
            <td class="formItem_content"></td>
        </tr>
        </tbody>
    </table>
</form>
</@override>
<@extends name="../base.ftl"/>