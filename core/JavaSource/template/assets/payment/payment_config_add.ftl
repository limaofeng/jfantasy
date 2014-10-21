<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function () {
        $("#saveForm_paymentConfigType").change(function(){
            if($(this).val() == 'online'){
                $('.online').show().find('input,select').removeAttr('disabled').change();
            }else{
                $('.online').hide().find('input,select').attr('disabled','disabled');
            }
        }).change();
        $('#saveForm_paymentFeeType').change(function(){
            $('.paymentFee').html($(this).val()=='scale'?'支付费用比例:':'固定支付费用:');
        }).change();
        $('[name=paymentProductId]').select3({value:'id',text:'name'},function(data){
            $('.bargainorIdName').html(data.bargainorIdName+':');
            $('.bargainorKeyName').html(data.bargainorKeyName+':');
            if(data.id.startsWith('alipay')){
                $('.sellerEmail').css('visibility','visible');
            }else{
                $('.sellerEmail').css('visibility','hidden').find('input').val('');
            }
        }).load(<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(@com.fantasy.payment.service.PaymentConfiguration@paymentProducts())" default="[]" escapeHtml="false"/>);
        $("#saveForm").ajaxForm(function () {
            $('#pager').pager().reload();
            $.msgbox({
                msg: "<@s.text name="payment.config.save.success"/>",
                type: "success"
            });
            $page$.backpage();
        });
    });
</script>
<div class="example-box" style="padding-left:10px;padding-right:10px;">
    <div class="example-code">
        <div class="content-box box-toggle button-toggle">
            <h3 class="content-box-header primary-bg">
                <span class="float-left"><@s.text name="payment.config.add.title"/></span>
                <a href="javascript:void(0)" class="float-right icon-separator btn toggle-button" title="Toggle Box">
                    <i class="glyph-icon icon-toggle icon-chevron-up"></i>
                </a>
            </h3>
            <div class="content-box-wrapper">
            <@s.form id="saveForm" namespace="/system/payconfig" action="save" method="post" cssClass="center-margin">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="payment.config.aev.name"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textfield name="name"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row online">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="payment.config.aev.paymentProductId"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <select name="paymentProductId" class="chosen-select"></select>
                                </div>
                            </div>
                        </div>
                        <div class="form-row online">
                            <div class="form-label col-md-3">
                                <label class="bargainorIdName">
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textfield name="bargainorId"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="payment.config.aev.paymentFeeType"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.select cssClass="chosen-select" name="paymentFeeType" list=r"#{'fixed':'固定费用','scale':'按比例收费'}" listKey="key" listValue="value"></@s.select>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="payment.config.aev.description"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textarea name="description" cssStyle="width: 880px; height: 118px;"/>
                                </div>

                            </div>
                        </div>
                        <div class="form-row">
                            <div style="float: left;padding-right: 50px;">
                                <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 menu-save4"  onclick="$('#saveForm').submit();return false;" title="<@s.text name="payment.config.button.save"/>"  >
                                         <span class="button-content">
                                                 <i class="glyph-icon icon-save float-left"></i>
                                             <@s.text name="payment.config.button.save"/>
                                          </span>
                                </a>
                            </div>
                            <div style="float: left;">
                                <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "  title="<@s.text name="payment.config.button.return"/>" >
                                         <span class="button-content">
                                              <i class="glyph-icon icon-reply"></i>
                                             <@s.text name="payment.config.button.return"/>
                                        </span>


                                </a>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="payment.config.aev.paymentConfigType"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                    <@s.select name="paymentConfigType" list=r"#{'offline':'线下支付','online':'在线支付'}" listKey="key" listValue="value" cssClass="chosen-select"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row online">
                            <div class="form-label col-md-3">
                                <label class="sellerEmail">
                                    <@s.text name="payment.config.aev.sellerEmail"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right sellerEmail">
                                    <@s.textfield name="sellerEmail"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row online">
                            <div class="form-label col-md-3">
                                <label class="bargainorKeyName"></label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                    <@s.textfield name="bargainorKey"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label class="paymentFee"></label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                    <@s.textfield name="paymentFee" value="0.00"/>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </@s.form>

            </div>

        </div>
    </div>
</div>
