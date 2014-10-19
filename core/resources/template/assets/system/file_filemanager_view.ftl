<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<script  type="text/javascript">
    $(function(){
        var $type=$('#type').html();
        if($type=="ftp"){
            $('#ftp').show().find('select').removeAttr("disabled");
        }else if($type=="jdbc"){
            $('#jdbc').show().find('select').removeAttr("disabled");
        }else if($type=="local"){
            $('#local').show().find('textarea').removeAttr("disabled");
        }
    });
</script>
</@override>
<@override name="container">
<table class="formTable mb3">
    <caption>基本信息<a href="###" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
    <tbody>
    <tr>
        <td class="formItem_title w100">编码：</td>
        <td class="formItem_content "><@s.property value="fm.id"/> </td>
        <td class="formItem_title w100">名称：</td>
        <td class="formItem_content ">
            <@s.property value="fm.name" />
					<span style="padding-left:15px;" toggle="closest('td')">
						<a href="${request.contextPath}/admin/system/file/files.do?fileManagerId=<@s.property value="fm.id"/>" target="after:closest('.ajax-load-div')">查看文件信息</a>
					</span>
        </td>
    </tr>
    <tr>
        <td class="formItem_title w100">类型：</td>
        <td class="formItem_content">
            <span id="type"><@s.property value="fm.type.value" /></span>
        </td>
        <td class="formItem_title w100">是否虚拟目录：</td>
        <td class="formItem_content">
            <@s.property value="%{fm.virtual?'是':'否'}" />
        </td>
    </tr>
    <tr id="ftp" style="display:none;">
        <td class="formItem_title w100">ftp配置属性：</td>
        <td class="formItem_content" colspan="3">
            <@s.property value="@com.fantasy.common.service.FtpConfigService@getFtp(fm.ftpConfig.id).name"/>
        </td>
    </tr>
    <tr id="local" style="display: none;">
        <td class="formItem_title w100">Local配置属性：</td>
        <td class="formItem_content" colspan="3">
            <@s.property value="fm.localDefaultDir" />
        </td>
    </tr>
    <tr id="jdbc" style="display:none;">
        <td class="formItem_title w100">jdbc配置属性：</td>
        <td class="formItem_content" colspan="3">
            <@s.property value="@com.fantasy.common.service.JdbcConfigService@getJdbc(fm.jdbcConfig.id).name"/>
        </td>
    </tr>
    <tr>
        <td class="formItem_title w100" >描述：</td>
        <td class="formItem_content" colspan="3">
            <@s.property value="fm.description" />
        </td>
    </tr>
    </tbody>
</table>
</@override>
<@extends name="../base.ftl"/>