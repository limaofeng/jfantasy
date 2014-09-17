<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<!doctype html >
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="author" content="Tocersoft" />
    <meta name="keywords" content="-后台管理系统"/>
    <meta name="description" content="-后台管理系统"/>
    <meta name="robots" content="all" />
    <title><@s.property value="@com.fantasy.system.util.SettingUtil@getValue('wel_tle')"/></title>
    <link rel="stylesheet" href="${request.contextPath}/static/css/admin-welcome.css" />
    <script type="text/javascript" src="${request.contextPath}/static/js/jquery.js"></script>
    <!--
		<script type="text/javascript" src="${request.contextPath}/static/admin/js/home.js"></script>
		 -->
</head>
<body>
<div class="welcome">
    <p class="tit"><@s.property value="@com.fantasy.system.util.SettingUtil@getValue('wel_tle')"/></p>
    <p class="desc"><@s.property value="@com.fantasy.system.util.SettingUtil@getValue('wel_des')"/></p>
    <p class="version"><@s.property value="@com.fantasy.system.util.SettingUtil@getValue('wel_ver')"/></p>
    <p class="copyright"><@s.property value="@com.fantasy.system.util.SettingUtil@getValue('wel_copyr')"/></p>
</div>
</body>
</html>