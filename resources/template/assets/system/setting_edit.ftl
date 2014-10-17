<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function () {
        $("#saveForm").ajaxForm({
            success: function (data) {
                $('#pager').pager().reload();
                top.$.msgbox({
                    msg: "<@s.text name="system.setting.save.success"/>",
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
                <span class="float-left"><@s.text name="system.setting.edit.title"/></span>
                <a href="javascript:void(0)" class="float-right icon-separator btn toggle-button" title="Toggle Box">
                    <i class="glyph-icon icon-toggle icon-chevron-up"></i>
                </a>
            </h3>
            <div class="content-box-wrapper">
            <@s.form id="saveForm" namespace="/system/website/setting" action="save" method="post" cssClass="center-margin">
                <@s.hidden name="id" value="%{setting.id}"/>
                <@s.hidden name="key" value="%{setting.key}"/>
                <@s.hidden name="website.id" value="%{setting.website.id}"/>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="system.setting.add.key"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textfield name="key" value="%{setting.key}" disabled="true"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="system.setting.add.value"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.if test="setting.key=='cms'">
                                        <@s.select list="@com.fantasy.cms.service.CmsService@articleCategoryList()"  name="value" listKey="code" listValue="name" value="%{setting.value}" cssClass="chosen-select"/>
                                    </@s.if>
                                        <@s.elseif test="setting.key=='good'">
                                    <@s.select list="@com.fantasy.mall.goods.service.GoodsService@goodsCategoryList()"  cssClass="chosen-select" name="value" listKey="sign" listValue="name" value="%{setting.value}"/>
                                </@s.elseif>
                                        <@s.else>
                                    <@s.textfield name="value" value="%{setting.value}" cssStyle="width: 885px;"/>
                                </@s.else>

                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="system.setting.description"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textarea name="description" cssStyle="width: 885px;height: 150px;"  value="%{setting.description}"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div style="float: left;padding-right: 50px;">
                                <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 menu-save4"  onclick="$('#saveForm').submit();return false;" title="<@s.text name="system.setting.button.save"/>"  >
                                         <span class="button-content">
                                                 <i class="glyph-icon icon-save float-left"></i>
                                             <@s.text name="system.setting.button.save"/>
                                          </span>
                                </a>
                            </div>
                            <div style="float: left;">
                                <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "  title="<@s.text name="system.setting.button.return"/>" >
                                         <span class="button-content">
                                              <i class="glyph-icon icon-reply"></i>
                                             <@s.text name="system.setting.button.return"/>
                                        </span>


                                </a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="system.setting.add.name"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                    <@s.textfield name="name"  value="%{setting.name}"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
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
