<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function(){
        $('.saveFormBut').click(function(e){
            $("#saveForm").submit();
            return stopDefault(e);
        });
        $("#saveForm").ajaxForm(function(data){
            $('#pager').pager().reload();
            top.$.msgbox({
                msg : "保存成功",
                icon : "success"
            });
            $(".back-page",$("#saveForm")).backpage();


        });

    $('#'+$('[name="fm.type"]').val()).show();
        $('[name="fm.type"]').change(function(){
            $('#ftp,#jdbc,#local').hide();
            if($(this).val()=="ftp"){
                $('#ftp').show();
            };
            if($(this).val()=="jdbc"){
                $('#jdbc').show();
            };
            if($(this).val()=="local"){
                $('#local').show();
            };

        });


    });
</script>
<@s.form id="saveForm" namespace="/admin/system/file" action="save">
    <@s.hidden name="fm.id" />
    <table class="formTable mb3">
        <caption>基本信息<a href="#" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
        <tbody>
        <tr>
            <td class="formItem_title w100">名称：</td>
            <td class="formItem_content"><@s.textfield name="fm.name" /></td>
            <td class="formItem_title w100"></td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title w100">类型：</td>
            <td class="formItem_content">
                <@s.radio name="fm.type" list="@com.fantasy.file.bean.enums.FileManagerType@values()"  cssStyle="width:30px;" listValue="value" />
            </td>
            <td class="formItem_title w100"></td>
            <td class="formItem_content"></td>
          <#--
            <td class="formItem_title w100">是否虚拟目录：</td>
            <td class="formItem_content">
                <@s.radio name="fm.virtual" list=r"#{true:'是',false:'否'}" cssStyle="width:30px;" />
            </td>
            -->
        </tr>
        <tr id="ftp" style="display:none;">
            <td class="formItem_title w100">ftp配置属性：</td>
            <td class="formItem_content">
                <@s.select name="fm.ftpConfig.id" list="@com.fantasy.common.service.FtpConfigService@findftps()" listKey="id" listValue="name" cssStyle="width:170px;"  />
            </td>
            <td class="formItem_title w100"></td>
            <td class="formItem_content"></td>
        </tr>
        <tr id="local" style="display: none;">
            <td class="formItem_title w100">Local配置属性：</td>
            <td class="formItem_content">
                <@s.textarea name="fm.localDefaultDir" cssStyle="width:98%;height:20px;"/>
            </td>
            <td class="formItem_title w100"></td>
            <td class="formItem_content"></td>
        </tr>
        <tr id="jdbc" style="display:none;">
            <td class="formItem_title w100">jdbc配置属性：</td>
            <td class="formItem_content" colspan="3">
                <@s.select name="fm.jdbcConfig.id" list="@com.fantasy.common.service.JdbcConfigService@jdbcConfigs()" listKey="id" listValue="driver" cssStyle="width:170px;" />
            </td>
        </tr>
        <tr>
            <td class="formItem_title w100" >描述：</td>
            <td class="formItem_content" colspan="3">
                <@s.textarea name="fm.description" cssStyle="width:98%;height:100px;"/>
            </td>
        </tr>
        </tbody>
    </table>
</@s.form>
<table class="formTable mb3">
    <tr>
        <td class="formItem_title w100"></td>
        <td class="formItem_content"><a href="javascript:void(0);" class="saveFormBut ui_button">保存</a></td>
    </tr>
</table>