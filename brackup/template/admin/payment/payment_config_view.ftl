<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<table class="formTable mb3">
	<caption>支付方式<a href="###" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
	<tbody>
		<tr>
			<td class="formItem_title w100">名称:</td>
			<td class="formItem_content"><@s.property value="paymentConfig.name"/></td>
			<td class="formItem_title w100">支付配置类型:</td>
			<td class="formItem_content">
				<@s.property value="@com.fantasy.system.service.ConfigService@get('paymentType',paymentConfig.paymentConfigType).name" />
			</td>
		</tr>
		<tr>
			<td class="formItem_title ">支付手续费类型:</td>
			<td class="formItem_content">
				<@s.property value="@com.fantasy.system.service.ConfigService@get('paymentFeeType',paymentConfig.paymentFeeType).name" />
			</td>
			<td class="formItem_title ">支付费用:</td>
			<td class="formItem_content">
				<@s.text name="global.format.money"><@s.param value="paymentConfig.paymentFee"/></@s.text>
			</td>
		</tr>
		<tr>
			<td class="formItem_title ">排序:</td>
			<td class="formItem_content" colspan="3">
				<@s.property value="paymentConfig.sort"/>
			</td>
		</tr>
		<tr>
			<td class="formItem_title ">介绍:</td>
			<td class="formItem_content" colspan="3">
				<@s.property value="paymentConfig.description" />
			</td>
		</tr>
	</tbody>
</table>