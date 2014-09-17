<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
</@override>
<@override name="container">
<table class="formTable mb3">
    <caption>查看FTP站点<a href="###" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
    <tbody>
    <tr>
        <td class="formItem_title w100">名称：</td>
        <td class="formItem_content"><@s.property value="ftp.name"/>
			<span style="padding-left:15px;" toggle="closest('td')">
				<a href="${request.contextPath}/admin/system/file/files.do?fileManagerId=<@s.property value="ftp.fileManagerId"/>" target="after:closest('.ajax-load-div')">查看文件信息</a>
			</span>
        </td>
        <td class="formItem_title w100">编码：</td>
        <td class="formItem_content"><@s.property value="ftp.controlEncoding"/></td>
    </tr>
    <tr>
        <td class="formItem_title w100">地址：</td>
        <td class="formItem_content"><@s.property value="ftp.hostname"/></td>
        <td class="formItem_title w100">端口：</td>
        <td class="formItem_content"><@s.property value="ftp.port"/></td>
    </tr>
    <tr>
        <td class="formItem_title ">用户名：</td>
        <td class="formItem_content"><@s.property value="ftp.username"/></td>
        <td class="formItem_title ">密码：</td>
        <td class="formItem_content"><@s.property value="ftp.password"/></td>
    </tr>
    <tr>
        <td class="formItem_title">路径：</td>
        <td class="formItem_content" colspan="3"><@s.property value="ftp.defaultDir"/></td>
    </tr>
    <tr>
        <td class="formItem_title">描述</td>
        <td class="formItem_content" colspan="3">
            <@s.property value="ftp.description"/>
        </td>
    </tr>
    </tbody>
</table>
</@override>
<@extends name="../base.ftl"/>