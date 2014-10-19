<%--
  Created by IntelliJ IDEA.
  User: lmf
  Date: 14-9-23
  Time: 下午2:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title> 动态属性自动封装 </title>
</head>
<body>
<form action="${requestScope.contextPath}/test/testSave.do" method="post">
    <label>
        <input type="text" name="version.id" value="1"/>
    </label>
    <br/>
    <label>
        <input type="text" name="test" value="limaofeng"/>
    </label>
    <br/>
    <button type="submit">提交表单</button>
</form>
</body>
</html>
