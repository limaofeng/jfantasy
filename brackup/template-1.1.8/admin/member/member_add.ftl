<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function(){
        //三级联动
        var province = $(".province").select({'value':'code','text':'name'},function(data){
            city.load(data?Fantasy.config.list('area-gov',data.code):null);
        });
        var city = $(".city").select({'value':'code','text':'name'},function(data){
            district.load(data?Fantasy.config.list('area-gov',data.code):null);
        });
        var district = $(".district").select({'value':'code','text':'name'});
        province.load(Fantasy.config.list('area-gov'));
        //三级联动结束
        $("#saveForm").ajaxForm(function(data){
            $('#pager').pager().reload();
            top.$.msgbox({
                msg : "保存成功",
                icon : "success"
            });
            $(".back-page",$("#saveForm")).backpage();
        });
    });

</script>
<form id="saveForm" action="${request.contextPath}/admin/member/save.do" method="post">
	<@s.hidden name="details.grade" value="0"/>
	<table class="formTable mb3">
        <tbody>
		<tr>
			<td class="opt-title">
				<span>新增会员</span>
				<ul class="Tabtable" tabs="{selectedClass:'Tabcurr',event:'click'}">
		    		<li><a href="#" tab="#account">账号信息</a></li>
				    <li><a href="#" tab="#basic">基本信息</a></li>
				</ul>
				<a href="###" class="back-page" style="float:right;padding-right:50px;">返回会员列表>></a>
				<div style="clear:both;BORDER-BOTTOM:1PX SOLID #6483A4;"></div>
				<table id="account" class="formTable mb3">
			        <tbody>
			        <tr>
			            <td class="formItem_title w100">用户账号:</td>
			            <td class="formItem_content">
			                <@s.textfield name="username"/><@s.fielderror fieldName="username" />
			            </td>
			            <td class="formItem_title w100">登陆密码 :</td>
			            <td class="formItem_content">
			                <@s.password name="password"/><@s.fielderror fieldName="password" />
			            </td>
			        </tr>
			        <tr>
			            <td class="formItem_title ">用户显示昵称:</td>
			            <td class="formItem_content"><@s.textfield name="nickName"/></td>
			            <td class="formItem_title ">是否启用:</td>
			            <td class="formItem_content">
			                <@s.radio name="enabled" list=r"#{true:'是',false:'否' }" value="true" cssStyle="width: 30px;"></@s.radio>
			            </td>
			        </tr>
			        </tbody>
			    </table>
			    <table id="basic" class="formTable mb3">
			        <tbody>
			        <tr>
			            <td class="formItem_title w100">姓名:</td>
			            <td class="formItem_content"><@s.textfield name="details.name"/></td>
			            <td class="formItem_title w100">性别:</td>
			            <td class="formItem_content">
			                <!-- values 是枚举默认的方法 -->
			                <@s.radio name="details.sex" list="@com.fantasy.security.bean.enums.Sex@values()" cssStyle="width:30px;" listValue="value" value="'male'"/>
			            </td>
			        </tr>
			        <tr>
			            <td class="formItem_title ">生日:</td>
			            <td class="formItem_content">
			                <@s.textfield date="{dateFmt:'yyyy-MM-dd'}" name="details.birthday"  cssClass="ui_input_text"/>
			            </td>
			            <td class="formItem_title ">移动电话:</td>
			            <td class="formItem_content"><@s.textfield name="details.mobile" cssClass="mask-mobile"/></td>
			        </tr>
			        <tr>
			            <td class="formItem_title ">固定电话:</td>
			            <td class="formItem_content"><@s.textfield name="details.tel" cssClass="mask-phone"/></td>
			            <td class="formItem_title ">E-mail:</td>
			            <td class="formItem_content">
			                <@s.textfield name="details.email" />
			                <@s.checkbox name="details.mailValid" value="false" fieldValue="true" label="邮箱验证"/>
			                <@s.fielderror fieldName="details.email" />
			            </td>
			        </tr>
			        <tr>
			            <td class="formItem_title ">是否为vip会员:</td>
			            <td class="formItem_content">
			                <@s.radio name="details.vip" list=r"#{true:'是',false:'否' }" value="false" cssStyle="width: 30px;"></@s.radio>
			            </td>
			            <td class="formItem_title ">积分:</td>
			            <td class="formItem_content"><@s.textfield name="details.score" /></td>
			        </tr>
			        <tr>
			            <td class="formItem_title ">描述信息:</td>
			            <td class="formItem_content" colspan="3"><@s.textarea cssStyle="width:85%" name="details.description"></@s.textarea></td>
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