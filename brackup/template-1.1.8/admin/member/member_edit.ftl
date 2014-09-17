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
        //联动开始
        var province = $(".province").select({'value':'code','text':'name'},function(data){
            city.load(data?Fantasy.config.list('area-gov',data.code):null);
        });
        var city = $(".city").select({'value':'code','text':'name'},function(data){
            district.load(data?Fantasy.config.list('area-gov',data.code):null);
        });
        var district = $(".district").select({'value':'code','text':'name'});
        province.load(Fantasy.config.list('area-gov'));
        //联动结束

        $('#mailvalid').change(function(){
            if($(this).is(":checked")){
                $('[name="details.mailValid"]').val(true);
            }else{
                $('[name="details.mailValid"]').val(false);
            }
        });
    });
</script>
<form id="saveForm" action="${request.contextPath}/admin/member/save.do" method="post">
    <@s.hidden name="id" value="%{member.id}"/>
    <@s.hidden name="memberId" value="%{member.details.memberId}"/>
    <@s.hidden name="username" value="%{member.username}"/>
    <table class="formTable mb3">
        <tbody>
		<tr>
		<td class="opt-title">
			<span>编辑会员</span>
			<ul class="Tabtable" tabs="{selectedClass:'Tabcurr',event:'click'}">
	    		<li><a href="#" tab="#account">账号信息</a></li>
			    <li><a href="#" tab="#basic">基本信息</a></li>
			</ul>
			<a href="###" class="back-page" style="float:right;padding-right:50px;">返回会员列表>></a>
			<div style="clear:both;BORDER-BOTTOM:1PX SOLID #6483A4;"></div>
			<table id="basic" class="formTable mb3">
		        <tbody>
		        <tr>
		            <td class="formItem_title w100">姓名:</td>
		            <td class="formItem_content"><@s.textfield name="details.name" value="%{member.details.name}"/></td>
		            <td class="formItem_title w100">性别:</td>
		            <td class="formItem_content">
		                <!-- values 是枚举默认的方法 -->
		                <@s.radio name="details.sex" list="@com.fantasy.security.bean.enums.Sex@values()" cssStyle="width:30px;" listValue="value" value="%{member.details.sex}"/>
		            </td>
		        </tr>
		        <tr>
		            <td class="formItem_title ">生日:</td>
		            <td class="formItem_content">
		                <@s.textfield date="{dateFmt:'yyyy-MM-dd'}" name="details.birthday" cssClass="ui_input_text">
		                    <@s.param name="value">
		                        <@s.date name="member.details.birthday" format="yyyy-MM-dd"/>
		                    </@s.param>
		                </@s.textfield>
		            </td>
		            <td class="formItem_title ">移动电话:</td>
		            <td class="formItem_content"><@s.textfield name="details.mobile"  value="%{member.details.mobile}" cssClass="mask-mobile"/></td>
		        </tr>
		        <tr>
		            <td class="formItem_title ">固定电话:</td>
		            <td class="formItem_content"><@s.textfield name="details.tel" value="%{member.details.tel}" cssClass="mask-phone"/></td>
		            <td class="formItem_title ">E-mail:</td>
		            <td class="formItem_content">
		                <@s.textfield name="details.email" value="%{member.details.email}" />
		                <@s.checkbox name="details.mailValid" value="%{member.details.mailValid}" fieldValue="true" label="邮箱验证"/>
		                <@s.fielderror fieldName="details.email" />
		            </td>
		        </tr>
		        <tr>
		            <td class="formItem_title ">是否为vip会员:</td>
		            <td class="formItem_content">
		                <@s.radio name="details.vip" list=r"#{true:'是',false:'否' }" value="%{member.details.vip}" cssStyle="width: 30px;"></@s.radio>
		            </td>
		            <td class="formItem_title ">积分:</td>
		            <td class="formItem_content"><@s.textfield name="details.score" value="%{member.details.score}" /></td>
		        </tr>
		        <tr>
		            <td class="formItem_title ">描述信息:</td>
		            <td class="formItem_content" colspan="3"><@s.textarea cssStyle="width:400px;" name="details.description" value="%{member.details.description}"/></td>
		        </tr>
		        </tbody>
		    </table>
			<table id="account" class="formTable mb3">
       			 <tbody>
       			 	<tr>
			            <td class="formItem_title w100">用户账号:</td>
			            <td class="formItem_content">
			                <@s.textfield name="username" value="%{member.username}" disabled="true"/>
			            </td>
			            <td class="formItem_title w100">登陆密码 :</td>
			            <td class="formItem_content">
			                <input id="password" type="password" name="password" value="******" /><@s.fielderror fieldName="password" />
			            </td>
			        </tr>
			        <tr>
			            <td class="formItem_title">用户显示昵称:</td>
			            <td class="formItem_content"><@s.textfield name="nickName" value="%{member.nickName}"/></td>
			            <td class="formItem_title ">是否启用:</td>
			            <td class="formItem_content">
			                <@s.radio name="enabled" list=r"#{true:'是',false:'否' }" value="%{member.enabled}" cssStyle="width: 30px;"></@s.radio>
			            </td>
			        </tr>
       			 </tbody>
       		</table>
			</td>
		</tr>
        </tbody>
    </table>
    <table class="formTable mb3">
        <tbody>
        <tr>
            <td class="formItem_title w100"></td>
            <td class="formItem_content">
                <a class="ui_button" href="javascript:void(0);" onclick="$('#saveForm').submit();return false;">保存</a>
            </td>
        </tr>
        </tbody>
    </table>
</form>