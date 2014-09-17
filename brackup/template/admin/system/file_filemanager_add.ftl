<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function(){
        $("#saveForm").ajaxForm(function(data){
            $('#pager').pager().reload();
            top.$.msgbox({
                msg : "保存成功",
                icon : "success"
            });
            $(".back-page",$("#saveForm")).backpage();
        });

        $('[name="type"]').change(function(){
            $('#ftp').hide().find('select').attr("disabled","disabled");
            $('#jdbc').hide().find('select').attr("disabled","disabled");
            $('#local').hide().find('textarea').attr("disabled","disabled");
            if($(this).val()=="ftp"){
                $('#ftp').show().find('select').removeAttr("disabled");
            };
            if($(this).val()=="jdbc"){
                $('#jdbc').show().find('select').removeAttr("disabled");
            };
            if($(this).val()=="local"){
                $('#local').show().find('textarea').removeAttr("disabled");
            };
        });
        
        $('.saveForm').click(function(){
        	$('#saveForm').submit();
        	return false;
        });
    });
</script>
<form id="saveForm" action="${request.contextPath}/admin/system/file/save.do" method="post">
    <@s.hidden name="fm" value="fm.id"/>
    <table class="formTable mb3">
        <caption>基本信息<a href="###" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
        <tbody>
        <tr>
            <td class="formItem_title w100">编码：</td>
            <td class="formItem_content "><@s.textfield name="id"/><@s.fielderror fieldName="id"/></td>
            <td class="formItem_title w100">名称：</td>
            <td class="formItem_content "><@s.textfield name="name" /></td>
        </tr>
        <tr>
            <td class="formItem_title w100">类型：</td>
            <td class="formItem_content">
                <@s.radio name="type" value="'local'" list="@com.fantasy.file.bean.enums.FileManagerType@values()" cssStyle="width:30px;" listValue="value" />
            </td>
            <td class="formItem_title w100"> </td>
            <td class="formItem_content"></td>
          <#--
            <td class="formItem_title w100">是否虚拟目录：</td>
            <td class="formItem_content">
                <@s.radio name="virtual" list=r"#{true:'是',false:'否'}" value="false" cssStyle="width:30px;" />
            </td>
            -->
        </tr>
        <tr id="ftp" style="display:none;">
            <td class="formItem_title w100">ftp配置属性：</td>
            <td class="formItem_content" colspan="3">
                <@s.select name="ftpConfig.id" list="@com.fantasy.common.service.FtpConfigService@findftps()" listKey="id" listValue="name" cssStyle="width:170px;"  />
            </td>
        </tr>
        <tr id="local">
            <td class="formItem_title w100">Local配置属性：</td>
            <td class="formItem_content" colspan="3">
                <@s.textarea name="localDefaultDir" cssStyle="width:98%;height:20px;"/>
            </td>
        </tr>
        <tr id="jdbc" style="display:none;">
            <td class="formItem_title w100">jdbc配置属性：</td>
            <td class="formItem_content" colspan="3">
                <@s.select name="jdbcConfig.id" list="@com.fantasy.common.service.JdbcConfigService@jdbcConfigs()" listKey="id" listValue="driver" cssStyle="width:170px;" />
            </td>
        </tr>
        <tr>
            <td class="formItem_title w100" >描述：</td>
            <td class="formItem_content" colspan="3">
                <@s.textarea name="description" cssStyle="width:98%;height:100px;"/>
            </td>
        </tr>
        </tbody>
    </table>
</form>
<table class="formTable mb3">
    <tr>
        <td class="formItem_title w100"></td>
        <td class="formItem_content"><a href="javascript:void(0);" class="ui_button saveForm">保存</a></td>
    </tr>
</table>