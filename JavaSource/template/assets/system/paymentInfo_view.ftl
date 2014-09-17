<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<div class="example-box" style="padding-left:10px;padding-right:10px;">
    <div class="example-code">
        <div class="content-box box-toggle button-toggle">
            <h3 class="content-box-header primary-bg">
                <span class="float-left"><@s.text name="payment.info.view.title"/></span>
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
                                <@s.text name="payment.info.view.sn"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.property value="payment.sn"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                <@s.text name="payment.info.view.paymentConfigName"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.property value="payment.paymentConfigName"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                <@s.text name="payment.info.view.totalAmount"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                   <@s.property value="payment.totalAmount"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                <@s.text name="payment.info.view.paymentStatus"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.select list=r"#{'ready':'准备','timeout':'超时','invalid':'作废','success':'成功','failure':'失败'}" value="payment.paymentStatus" disabled="true"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                <@s.text name="payment.info.view.memo"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                <@s.textarea cssStyle="width: 885px;height: 150px;" value="%{payment.memo}" disabled="true"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div style="float: left;">
                                <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "  title="<@s.text name="payment.info.button.return"/>" >
                                         <span class="button-content">
                                              <i class="glyph-icon icon-reply"></i>
                                         <@s.text name="payment.info.button.return"/>
                                        </span>
                                </a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                <@s.text name="payment.info.view.paymentType"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                    <@s.if test="payment.paymentType.toString()=='offline'">
                                        <@s.text name="payment.info.view.paymentType.xx"/>
                                    </@s.if>
                                    <@s.else>
                                        <@s.text name="payment.info.view.paymentType.zx"/>
                                    </@s.else>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                <@s.text name="payment.info.view.payer"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                      <@s.property value="payment.payer"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                <@s.text name="payment.info.view.paymentFee"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                <@s.property value="payment.paymentFee"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                <@s.text name="payment.info.view.createTime"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                    <@s.date name="payment.createTime" format="yyyy-MM-dd HH:mm:ss"/>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>

            </div>

        </div>
    </div>
</div>
