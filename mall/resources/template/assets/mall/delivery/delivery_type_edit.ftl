<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function () {
        $("#saveForm").ajaxForm({
            success: function (data) {
                $('#pager').pager().reload();
                top.$.msgbox({
                    msg: "<@s.text name="mall.delivery.type.save.success"/>",
                    type: "success"
                });
                $page$.backpage();
            }
        });
    });
</script>
<div class="example-box" style="padding-left:10px;padding-right:10px;">
    <div class="example-code">
        <div class="content-box box-toggle button-toggle">
            <h3 class="content-box-header primary-bg">
                <span class="float-left"><@s.text name="mall.delivery.type.edit.title"/></span>
                <a href="javascript:void(0)" class="float-right icon-separator btn toggle-button" title="Toggle Box">
                    <i class="glyph-icon icon-toggle icon-chevron-up"></i>
                </a>
            </h3>
            <div class="content-box-wrapper">
            <@s.form id="saveForm" namespace="/system/deliverytype" action="save" method="post" cssClass="center-margin">
                <@s.hidden name="id" value="%{deliveryType.id}"/>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="mall.delivery.type.aev.name"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textfield name="name" value="%{deliveryType.name}"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="mall.delivery.type.aev.firstweight"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textfield name="firstWeight" value="%{deliveryType.firstWeight}"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="mall.delivery.type.aev.firstweightprice"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                  <@s.textfield name="firstWeightPrice" value="%{deliveryType.firstWeightPrice}"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="mall.delivery.type.aev.deliverycorp.name"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.select name="defaultDeliveryCorp.id"  list="@com.fantasy.mall.delivery.service.DeliveryService@deliveryCorps()"  listKey="id" listValue="name" cssClass="chosen-select" value="%{deliveryType.defaultDeliveryCorp.id}"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="mall.delivery.type.aev.description"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textarea name="description" cssStyle="width: 880px; height: 118px;" value="%{deliveryType.description}"/>
                                </div>

                            </div>
                        </div>
                        <div class="form-row">
                            <div style="float: left;padding-right: 50px;">
                                <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 menu-save4"  onclick="$('#saveForm').submit();return false;" title=" <@s.text name="mall.delivery.type.button.save"/>"  >
                                         <span class="button-content">
                                                 <i class="glyph-icon icon-save float-left"></i>
                                             <@s.text name="mall.delivery.type.button.save"/>
                                          </span>
                                </a>
                            </div>
                            <div style="float: left;">
                                <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "  title=" <@s.text name="mall.delivery.type.button.return"/>" >
                                         <span class="button-content">
                                              <i class="glyph-icon icon-reply"></i>
                                             <@s.text name="mall.delivery.type.button.return"/>
                                        </span>
                                </a>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="mall.delivery.type.aev.method"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                    <@s.select  name="method" list=r"#{'deliveryAgainstPayment':'先付款后发货','cashOnDelivery':'货到付款'}" cssClass="chosen-select" value="%{deliveryType.method}"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="mall.delivery.type.aev.continueWeight"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                    <@s.textfield name="continueWeight" value="%{deliveryType.continueWeight}"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="mall.delivery.type.aev.continueWeightPrice"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                    <@s.textfield name="continueWeightPrice" value="%{deliveryType.continueWeightPrice}"/>
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
