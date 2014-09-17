<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@s.set value="${response.setStatus(203)}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <link href="${request.contextPath}<@s.property value="@com.fantasy.system.util.SettingUtil@getValue('icon')"/>" type="image/x-icon" rel="shortcut icon" />
    <title><@s.property value="@com.fantasy.system.util.SettingUtil@getValue('title')"/></title>
    <link rel="stylesheet" href="${request.contextPath}/static/css/admin-login.css" type="text/css" />
    <link rel="stylesheet" href="${request.contextPath}/static/css/admin-base-1.0.css" type="text/css" />
    <link rel="stylesheet" href="${request.contextPath}/static/css/admin-login.css" type="text/css" />
    <script type="text/javascript">window.contextPath = '${request.contextPath}';</script>
    <script type="text/javascript" src="${request.contextPath}/static/js/jquery.js"></script>
    <script type="text/javascript" src="${request.contextPath}/static/js/common.js"></script>
    <script type="text/javascript" src="${request.contextPath}/static/js/common/formvalidator/formValidator-4.1.3.js"></script>
    <script type="text/javascript" src="${request.contextPath}/static/js/common/formvalidator/formValidatorRegex.js"></script>
    <script type="text/javascript">
        if(top.window.location.href!=window.location.href){
            top.window.location.href=window.location.href;
        }
        function add_loader(){
		}
		function remove_loader(){
		}
        $(function() {
            $('input[name=username]').change(function() {
                Fantasy.Cookie.put('admin_login_username',$(this).val());
            }).val(Fantasy.Cookie.get('admin_login_username'));
            $('input[name=captcha]').keypress(function(event){
                if(event.keyCode == '13'){
                	$('#loginForm').submit();
					return false;
                }
            });
            $('#refreshCaptcha').click(function() {
                $(this).hide().attr('src', '${request.contextPath}/jcaptcha.jpg' + '?' + new Date().getTime()).fadeIn();
            });
            $('#playSoundCaptcha').click(function(e){
			   	$('#hideIframe').attr('src','${request.contextPath}/jcaptcha.mpeg' + '?' + new Date().getTime())
			   	return stopDefault(e);
            });
            $('.login_link').click(function(){
            	$('#loginForm').submit();
				return false;
            });
            <#-- 
            //添加验证
	        $.formValidator.initConfig({theme:"adminlogin",errorFocus:false,formID:"loginForm",submitOnce:true,onError:function(error){
	            alert(error);
	        }});
	        $("#username").formValidator({onShow:"请输入您的用户名",onCorrect:"用户名已输入"}).regexValidator({regExp:"notempty",dataType:"enum",onError:"请输入您的用户名"}).regexValidator({regExp:"username",dataType:"enum",onError:"用户名不能包含特殊字符"});
            $("#password").formValidator({onShow:"请输入您的密码",onCorrect:"密码已输入"}).regexValidator({regExp:"notempty",dataType:"enum",onError:"请输入您的密码"});
	        $('#captcha').formValidator({onShow:"请输入验证码",onCorrect:"验证码已输入"}).regexValidator({regExp:"notempty",dataType:"enum",onError:"请输入验证码"}).ajaxValidator({
	            dataType : "json",
	            async : true,
	            data : {
	                'validatorType':'captcha',
	                'fieldName':'captcha'
	            },
	            type : 'POST',
	            url : "${request.contextPath}/common/ajaxValidator.do",
	            success : function(data){
	                if(data.success){
	                    return true;
	                }
	                return "验证码不正确";
	            },
	            error: function(jqXHR, textStatus, errorThrown){
	            	console.log(errorThrown);
	            },
	            onError : "验证码不正确",
	            onWait : "校验中，请稍候...",
	            beforeSend : function(){
	                $('#loginForm').hide();
	                return true;
	            },
	            complete : function(){
	                $('#loginForm').show();
	            }
        	});
        	-->
        });
    </script>
</head>
<body>
<div class="login-logo"><img alt="" src="${request.contextPath}/static/images/admin/login/logo_login.png"/></div>
<div class="login-main">
    <div class="login-middle-content">
        <div class="lmc-tologin">
            <form id="loginForm" action="${request.contextPath}/admin/login" method="post">
                <input type="hidden" name="rememberme" value="true"/>
                <div class="login-item mt5"><span class="user-login">用户登录</span> <!-- <a href="javascript:void(0);" class="ml130">帮助中心</a> --></div>
                <div class="form-item mt5"><span class="item-titles">用户名：</span><@s.textfield name="username" tabindex="1" cssClass="login-input w200 fl" />
                    <div class="cb"><span id="usernameTip"></span></div>
                </div>
                <div class="form-item"><span class="item-titles">密&nbsp;&nbsp;&nbsp;码：</span><@s.password name="password"  tabindex="2" cssClass="login-input w200 fl"/>
                    <div class="cb"><span  id="passwordTip"></span></div>
                </div>
                <div class="form-item"><span class="item-titles">验证码：</span> <input id="captcha" type="text" name="captcha" class="login-input w100 fl mr5" autocomplete="off" />
                	<#--
                	<a href="#" id="playSoundCaptcha"><img src="image/wheelchair.jpg" height="18px" width="18px" alt="play"></a>
                	-->
                	<img id="refreshCaptcha" src="${request.contextPath}/jcaptcha.jpg" align="middle" width="90" height="30" style="cursor: pointer;" class="vm fl" />
                    <div class="cb"><span class="captcha-span"></span></div>
                    <div id="captchaTip" class="clearBoth login_input_font"></div>
                </div>
                <div class="form-item"><span class="item-titles"></span> <a href="javascript:;" class="login_link">登 录</a>
                    <@s.if test="#session['SPRING_SECURITY_LAST_EXCEPTION'].message!=''">
                    <span class="status-red fl" style="padding:7px 7px 7px 10px;"><@s.property value="#session['SPRING_SECURITY_LAST_EXCEPTION'].message" /></span>
                    </@s.if>
                    <div class="cb"></div>
                </div>
            </form>
        </div>
    </div>
</div>
<div class="login-footer">
    <p class="mt10"><@s.property value="@com.fantasy.system.util.SettingUtil@getValue('title')"/>@<@s.property value="@com.fantasy.system.util.SettingUtil@getValue('copyright')"/></p>
    <p class="mt10 mb10"><font color="#747474">本网站之所有信息及作品，未经书面授权不得转载</font></p>
</div>
</body>
</html>