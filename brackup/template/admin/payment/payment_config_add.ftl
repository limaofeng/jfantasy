<style type="text/css">
.paymentForm input {
    width: 200px;
}
.paymentForm select {
    width: 215px;
}
</style>
<script type="text/javascript">
$(function () {
    $("#saveForm_paymentConfigType").change(function(){
        if($(this).val() == 'online'){
            $('.online').show().find('input,select').removeAttr('disabled');
        }else{
            $('.online').hide().find('input,select').attr('disabled','disabled');
        }
    }).change();
    $('#saveForm_paymentFeeType').change(function(){
        $('.paymentFee').html($(this).val()=='scale'?'支付费用比例:':'固定支付费用:');
    }).change();
    $('#saveForm_paymentProductId').select({value:'id',text:'name'},function(data){
        $('.bargainorIdName').html(data.bargainorIdName+':');
        $('.bargainorKeyName').html(data.bargainorKeyName+':');
        if(data.id.startsWith('alipay')){
            $('.sellerEmail').show();
        }else{
            $('.sellerEmail').hide().find('input').val('');
        }
    }).load(<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(@com.fantasy.payment.service.PaymentConfiguration@paymentProducts())" default="[]" escapeHtml="false"/>);
    $("#saveForm").ajaxForm(function () {
        $('#pager').pager().reload();
        top.$.msgbox({
            msg: "保存成功",
            icon: "success"
        });
        $(".back-page", $("#saveForm")).backpage();
    });
});
</script>
<@s.form id="saveForm" action="save" namespace="/admin/payment/payconfig" method="post">
    <table class="formTable mb3 paymentForm">
        <caption>支付方式<a href="#" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
        <tbody>
        <tr>
            <td class="formItem_title w100">名称:</td>
            <td class="formItem_content"><@s.textfield name="name"/></td>
            <td class="formItem_title w100">支付配置类型:</td>
            <td class="formItem_content">
                <@s.select name="paymentConfigType" list=r"#{'offline':'线下支付','online':'在线支付'}" listKey="key" listValue="value" cssStyle="width: 135px;"/>
            </td>
        </tr>
        <tr class="online">
            <td class="formItem_title ">支付产品:</td>
            <td class="formItem_content">
                <@s.select name="paymentProductId" list="" listKey="id" listValue="name"/>
            </td>
            <td class="formItem_title"><div class="sellerEmail">支付宝账号:</div></td>
            <td class="formItem_content"><div class="sellerEmail"><@s.textfield name="sellerEmail"/></div></td>
        </tr>
        <tr class="online">
            <td class="formItem_title bargainorIdName"></td>
            <td class="formItem_content">
                <@s.textfield name="bargainorId"/>
            </td>
            <td class="formItem_title bargainorKeyName"></td>
            <td class="formItem_content">
                <@s.textfield name="bargainorKey"/>
            </td>
        </tr>
        <tr>
            <td class="formItem_title ">支付手续费类型:</td>
            <td class="formItem_content">
            	<@s.select name="paymentFeeType" list=r"#{'fixed':'固定费用','scale':'按比例收费'}" listKey="key" listValue="value" cssStyle="width: 135px;"></@s.select>
            </td>
            <td class="formItem_title paymentFee"></td>
            <td class="formItem_content">
                <@s.textfield name="paymentFee" value="0.00"/>
            </td>
        </tr>
        <tr>
            <td class="formItem_title ">介绍:</td>
            <td class="formItem_content" colspan="3">
                <@s.textarea name="description" cssStyle="width:98%;"/>
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
</@s.form>