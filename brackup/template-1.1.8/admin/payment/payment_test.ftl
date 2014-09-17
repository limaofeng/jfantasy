<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>在线支付</title>
<meta name="Author" content="Fantasy Team" />
<meta name="Copyright" content="Fantasy" />
<link rel="icon" href="favicon.ico" type="image/x-icon" />
</head>
<body>
	<form action="${paymentUrl}" method="post">
        <#list parameterMap?keys as key>
            <#if StringUtil.isNotBlank(parameterMap.get(key))>
            ${key}:<input type="text" name="${key}" value="${parameterMap.get(key)}"><br/>
            </#if>
        </#list>
        <input type="submit" value="提交">
	</form>
</body>
</html>