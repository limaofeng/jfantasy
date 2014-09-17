<style type="text/css">
.paymentForm input{
    width: 200px;
}
.paymentForm select{
    width: 215px;
}
</style>
<script type="text/javascript">
    $(function () {
        $('#saveForm_paymentFeeType').change(function(){
            $('.paymentFee').html($(this).val()=='scale'?'支付费用比例:':'固定支付费用:');
        }).change();
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
<@s.hidden name="id" value="%{paymentConfig.id}"/>
<table class="formTable mb3 paymentForm">
    <caption>支付方式<a href="#" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
    <tbody>
    <tr>
        <td class="formItem_title w100">名称:</td>
        <td class="formItem_content"><@s.textfield name="name" value="%{paymentConfig.name}"/></td>
        <td class="formItem_title w100">支付配置类型:</td>
        <td class="formItem_content">
            <@s.select name="paymentConfigType" value="%{paymentConfig.paymentConfigType}" list=r"#{'offline':'线下支付','online':'在线支付'}" listKey="key" listValue="value" disabled="true"/>
        </td>
    </tr>
    <@s.if test="paymentConfig.paymentConfigType.toString() == 'online'">
    <tr>
        <td class="formItem_title ">支付产品:</td>
        <td class="formItem_content">
            <@s.select name="paymentProductId" list="@com.fantasy.payment.service.PaymentConfiguration@paymentProducts()" listKey="id" listValue="name" value="%{paymentConfig.paymentProductId}" disabled="true"/>
        </td>
        <td class="formItem_title"><div>支付宝账号:</div></td>
        <td class="formItem_content"><div><@s.textfield name="sellerEmail" value="%{paymentConfig.sellerEmail}"/></div></td>
    </tr>
    <tr>
        <td class="formItem_title "><@s.property value="@com.fantasy.payment.service.PaymentConfiguration@paymentProduct(paymentConfig.paymentProductId).bargainorIdName"/>:</td>
        <td class="formItem_content">
            <@s.textfield name="bargainorId" value="%{paymentConfig.bargainorId}"/>
        </td>
        <td class="formItem_title "><@s.property value="@com.fantasy.payment.service.PaymentConfiguration@paymentProduct(paymentConfig.paymentProductId).bargainorKeyName"/>:</td>
        <td class="formItem_content">
            <@s.textfield name="bargainorKey" value="%{paymentConfig.bargainorKey}"/>
        </td>
    </tr>
    </@s.if>
    <tr>
        <td class="formItem_title ">支付手续费类型:</td>
        <td class="formItem_content">
            <@s.select name="paymentFeeType" value="%{paymentConfig.paymentFeeType}" list=r"#{'fixed':'固定费用','scale':'按比例收费'}" listKey="key" listValue="value"/>
        </td>
        <td class="formItem_title paymentFee"></td>
        <td class="formItem_content">
            <@s.textfield name="paymentFee">
                <@s.param name="value">
                    <@s.text name="global.format.money"><@s.param value="%{paymentConfig.paymentFee}"/></@s.text>
                </@s.param>
            </@s.textfield>
        </td>
    </tr>
    <tr>
        <td class="formItem_title ">介绍:</td>
        <td class="formItem_content" colspan="3">
            <@s.textarea name="description" cssStyle="width:98%;" value="%{paymentConfig.description}"/>
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