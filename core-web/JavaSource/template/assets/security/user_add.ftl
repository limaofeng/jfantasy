<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function () {
       var imageUploader = $('#imageUploader').upload({data:{'dir':'brand_logo'},theme:'image',size:'160x160'});
        $("#saveForm").ajaxForm({
            beforeSerialize : function(zhis, options){
                var _data = {};
                var _images = imageUploader.getData();
                if(_images.length > 0){
                    _data['details.avatarStore'] = _images[0].fileManagerId + ':' + _images[0].absolutePath;
                }
                options.data = _data;
            },
            success: function (data) {
                top.$.msgbox({
                    msg: "<@s.text name="security.user.save.success"/>",
                    type: "success"
                });
                $page$.backpage();
            }
        });
    });
</script>
<div class="pad10L pad10R">
    <div class="infobox warning-bg mrg10B">
        <p><i class="glyph-icon icon-exclamation mrg10R"></i>To view the available grid system options &amp; configurations you can visit the <a title="Fides Admin Grid System documentation" target="_blank" href="grid.html">Fides Admin Grid System documentation</a> page.</p>
    </div>
<div class="example-box">
<@s.form id="saveForm" namespace="/security/user" action="save" method="post" cssClass="center-margin">
        <div class="col-md-6 pad10T">
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        <@s.text name="security.user.ae.username"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.textfield name="username" id="username" autocomplete="off"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        <@s.text name="security.user.ae.password"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <input type="password" name="password"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        <@s.text name="security.user.ae.nickName"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.textfield name="nickName" />
                    </div>
                </div>
            </div>
        <div class="form-row">
            <div class="form-label col-md-2">
                <label for="">
                    <@s.text name="security.user.ae.email"/>
                </label>
            </div>
            <div class="form-input col-md-10">
                <div class="append-left">
                    <@s.textfield name="details.email" />
                </div>
            </div>
        </div>
        <div class="form-row">
            <div class="form-label col-md-2">
                <label for="">
                    <@s.text name="security.user.ae.roles"/>
                </label>
            </div>
            <div class="form-input col-md-10">
                <div class="append-left  form-checkbox-radio">
                    <@s.checkboxlist name="roles.code" list="@com.fantasy.security.service.RoleService@list()" listKey="code" listValue="name" cssClass="custom-checkbox" value="SYSTEM"/>
                </div>
            </div>
        </div>
        <div class="form-row">
            <div class="form-label col-md-2">
                <label for="">
                    <@s.text name="security.user.ae.website"/>
                </label>
            </div>
            <div class="form-input col-md-10">
                <div class="append-left">
                    <@s.select cssClass="chosen-select" name="website.id" list="@com.fantasy.system.service.WebsiteService@websiteList()" listKey="id" listValue="name" />
                </div>
            </div>
        </div>
            <div class="form-row" style="text-align: center;">
                <div>
                    <div style="float: left;padding-right: 50px;">
                        <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 menu-save4"  onclick="$('#saveForm').submit();return false;" title="<@s.text name="security.user.button.save"/>"  >
                            <span class="glyph-icon icon-separator">
                                 <i class="glyph-icon icon-save"></i>
                            </span>
                             <span class="button-content">
                                 <@s.text name="security.user.button.save"/>
                             </span>
                        </a>
                    </div>
                    <div style="float: left;">
                        <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "  title="<@s.text name="security.user.button.return"/>" >
                            <span class="glyph-icon icon-separator">
                                  <i class="glyph-icon icon-reply"></i>
                            </span>
                             <span class="button-content">
                                 <@s.text name="security.user.button.return"/>
                             </span>
                        </a>
                    </div>
                </div>
            </div>

    </div>


    <div class="col-md-6 form-vertical">
        <div class="form-row">
            <div class="form-label col-md-2"></div>
            <div class="form-input col-md-10" id="imageUploader"></div>
        </div>
        <div class="form-row">
            <div class="form-label col-md-2">
                <label for="">
                    <@s.text name="security.user.ae.description"/>
                </label>
            </div>
            <div class="form-input col-md-10">
                <@s.textarea name="details.description" cssClass="small-textarea" cssStyle="height: 90px;"/>
            </div>
        </div>
    </div>

</@s.form>
</div>
</div>
