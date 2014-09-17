<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<script type="text/javascript">
$(function(){
	//列表初始化
	var page = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(pager)" escapeHtml="false"/>;
	$('#pager').pager($("#searchForm"),page.pageSize,$('#config_view').view().on('add',function(data){
		this.target.find('.delete').click(function(){
			deleteMethod([data.id]);
			return false;
		});
	})).setJSON(page);
	//批量删除
	var deleteMethod = $('.batchDelete').batchExecute($("#allCheckBox"),$('#pager').pager(),'id','确认删除[{name}]？',function(){
		top.$.msgbox({
			msg : "删除成功!",
			icon : "success"
		});
	});
    $('#searchForm .search').click(function(e){
        $('#searchForm').submit();
        return stopDefault(e);
    });
});
</script>
</@override>
<@override name="container">
<@s.form id="searchForm" namespace="/admin/payment/payconfig" action="search" method="post">
<table class="formTable mb3">
	<caption>查询条件</caption>
	<tbody>
		<tr>
			<td class="formItem_title w100">名称:</td>
			<td class="formItem_content"><@s.textfield name="LIKES_name" cssClass="w100" /></td>
			<td class="formItem_title w100">付款类型:</td>
			<td class="formItem_content">
				<@s.select name="EQE_paymentConfigType" headerKey="" headerValue="--全部--" list=r"#{'offline':'线下支付','online':'在线支付'}" listKey="key" listValue="value" cssStyle="width: 135px;"></@s.select>
			</td>
			<td class="formItem_title w100">支付手续费类型:</td>
			<td class="formItem_content">
				<@s.select name="EQE_paymentFeeType" headerKey="" headerValue="--全部--" list=r"#{'fixed':'固定费用','scale':'按比例收费'}" listKey="key" listValue="value" cssStyle="width: 135px;"></@s.select>
			</td>
		</tr>
	</tbody>
</table>
</@s.form>
<a class="ui_button search">查询</a>
<a href="<@s.url namespace="/admin/payment/payconfig" action="add"/>" class="ui_button add" ajax="{target:'closest(\'#container\')'}">添加</a>
<@s.a cssClass="ui_button batchDelete" namespace="/admin/payment/payconfig" action="delete">批量删除</@s.a>
<span id="check_info"></span>
<table id="config_view" class="formTable mb3 listTable">
	<caption>查询结果列表</caption>
	<thead>
		<tr>
			<th style="width:30px;"><input id="allCheckBox" checkAll=".checkBoxAll" type="checkbox" checktip="{message:'您选中了{num}条记录',tip:'#check_info'}" /></th>
			<th style="width:250px;">支付方式名称</th>
			<th>支付类型</th>
			<th>支付手续费类型</th>
			<th>支付费用</th>
            <th>操作</th>
		</tr>
	</thead>
	<tbody>
		<tr align="center" class="template" name="default" >
			<td><input class="checkBoxAll" type="checkbox" value="{id}"/></td>
			<td>
				<a style="float:left;padding-left: 20px;" class="view" href="<@s.url namespace="/admin/payment/payconfig" action="view"/>?id={id}" ajax="{target:'closest(\'#container\')'}">{name}</a>
			</td>
			<td>{paymentConfigType:dict({'offline':'线下支付','online':'在线支付'})}</td>
			<td>{paymentFeeType:dict({'fixed':'固定费用','scale':'按比例收费'})}</td>
			<td>{paymentFee:number('0.00')}</td>
            <td>
                <a class="delete" href="###">删除</a>/
                <a class="edit" href="<@s.url namespace="/admin/payment/payconfig" action="edit"/>?id={id}" ajax="{target:'closest(\'#container\')'}">编辑</a>/
                <a target="_blank" href="<@s.url namespace="/admin/payment/payconfig" action="test?amount=0.01"/>&paymentConfigId={id}">测试支付</a>
            </td>
		</tr>
		<tr class="empty"><td class="norecord" colspan="6">暂无数据</td></tr>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="6">
			<div id="pager" class="paging digg"></div>
			</td>
		</tr>
	</tfoot>
</table>
</@override>
<@extends name="../base.ftl"/>