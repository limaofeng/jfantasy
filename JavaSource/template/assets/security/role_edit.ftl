<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function () {
        $("#saveForm").ajaxForm({
            success: function (data) {
                $('#pager').pager().reload();
                top.$.msgbox({
                    msg: "<@s.text name="security.role.save.success"/>",
                    type: "success"
                });
                $('.back-page').backpage();
            }
        });
    });
</script>
<div class="example-box" style="padding-left:10px;padding-right:10px;">
    <div class="example-code">
        <div class="content-box box-toggle button-toggle">
            <h3 class="content-box-header primary-bg">
                <span class="float-left"><@s.text name="security.role.edit.title"/></span>
                <a href="javascript:void(0)" class="float-right icon-separator btn toggle-button" title="Toggle Box">
                    <i class="glyph-icon icon-toggle icon-chevron-up"></i>
                </a>
            </h3>
            <div class="content-box-wrapper">
            <@s.form id="saveForm" namespace="/security/role" action="save" method="post" cssClass="center-margin">
                <@s.hidden name="code" value="%{role.code}"/>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="security.role.aev.name"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textfield name="name" id="name" value="%{role.name}"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="security.role.aev.enabled"/>
                                </label>
                            </div>
                            <div class="form-checkbox-radio col-md-9">
                                <div class="append-left">
                                    <@s.radio list=r"#{true:'启用',false:'禁用'}"  name="enabled" cssStyle="width:20px;"  value="%{role.enabled}"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="security.role.aev.description"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textarea name="description" cssStyle="width: 890px; height: 118px;" value="%{role.description}"/>
                                </div>

                            </div>
                        </div>
                        <div class="form-row">
                            <div style="float: left;padding-right: 50px;">
                                <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 menu-save4"  onclick="$('#saveForm').submit();return false;" title=" <@s.text name="security.role.button.save"/>"  >
                                         <span class="button-content">
                                                 <i class="glyph-icon icon-save float-left"></i>
                                             <@s.text name="security.role.button.save"/>
                                          </span>
                                </a>
                            </div>
                            <div style="float: left;">
                                <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "  title=" <@s.text name="security.role.button.return"/>" >
                                         <span class="button-content">
                                              <i class="glyph-icon icon-reply"></i>
                                             <@s.text name="security.role.button.return"/>
                                        </span>


                                </a>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="security.role.aev.code"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                    <@s.textfield name="code" value="%{role.code}" disabled="true"/>
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
