<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<div class="example-box" style="padding-left:10px;padding-right:10px;">
    <div class="example-code">
        <div class="content-box box-toggle button-toggle">
            <h3 class="content-box-header primary-bg">
                <span class="float-left"><@s.text name="mall.delivery.shipping.view.title"/></span>
                <a href="javascript:void(0)" class="float-right icon-separator btn toggle-button" title="Toggle Box">
                    <i class="glyph-icon icon-toggle icon-chevron-up"></i>
                </a>
            </h3>
            <div class="content-box-wrapper">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                <@s.text name="mall.delivery.shipping.view.sn"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.property value="shipping.sn"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                <@s.text name="mall.delivery.shipping.view.deliveryCorpName"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.property value="shipping.deliveryCorpName"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                <@s.text name="mall.delivery.shipping.view.deliveryFee"/>
                                   </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                   <@s.property value="shipping.deliveryFee"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                <@s.text name="mall.delivery.shipping.view.shippingName"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.property value="shipping.shipName"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                <@s.text name="mall.delivery.shipping.view.shippingZipCode"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                <@s.property value="shipping.shipZipCode"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                <@s.text name="mall.delivery.shipping.view.shippingPhone"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                <@s.property value="shipping.shipPhone"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                <@s.text name="mall.delivery.shipping.view.memo"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                <@s.textarea cssStyle="width: 885px;height: 150px;" value="%{shipping.memo}" disabled="true"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div style="float: left;">
                                <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "  title="<@s.text name="mall.delivery.shipping.button.return"/>" >
                                         <span class="button-content">
                                              <i class="glyph-icon icon-reply"></i>
                                         <@s.text name="mall.delivery.shipping.button.return"/>
                                        </span>
                                </a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                <@s.text name="mall.delivery.shipping.view.deliveryTypeName"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                <@s.property value="shipping.deliveryTypeName"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                <@s.text name="mall.delivery.shipping.view.deliverySn"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                      <@s.property value="shipping.deliverySn"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                <@s.text name="mall.delivery.shipping.view.order.sn"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                <@s.property value="shipping.order.sn"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                <@s.text name="mall.delivery.shipping.view.shippingAddress"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                <@s.property value="shipping.shipAddress"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                <@s.text name="mall.delivery.shipping.view.shippingMobile"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                    <@s.property value="shipping.shipMobile"/>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>

            </div>

        </div>
    </div>
</div>
