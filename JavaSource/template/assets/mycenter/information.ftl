<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="pageTitle">
    <@s.text name="security.user.center.title"/>
<small>
    <@s.text name="security.user.center.description"/>
</small>
</@override>
<@override name="head">
<script type="text/javascript">
    $(function () {
        var imageUploader = $('#imageUploader').upload({data:{'dir':'brand_logo'},theme:'image',size:'160x160'},<@s.property value="user.details.avatarStore" default="[]" escapeHtml="false"/>);
        $("#saveForm").ajaxForm({
            beforeSerialize : function(zhis, options){
                var _data = {};
                options.data = _data;
                var _images = imageUploader.getData();
                if(_images.length > 0){
                    _data['details.avatarStore'] = _images[0].fileManagerId + ':' + _images[0].absolutePath;
                }
            },
            success: function (data) {
                top.$.msgbox({
                    msg: " <@s.text name="security.user.center.save.success"/>",
                    type: "success"
                });
            }
        });
    });
</script>
</@override>
<@override name="pageContent">
<div class="example-box">
    <div class="example-code">
        <div class="content-box box-toggle button-toggle">
            <h3 class="content-box-header primary-bg">
                <span class="float-left"> <@s.text name="security.user.center.info.title"/></span>
                <a href="javascript:void(0)" class="float-right icon-separator btn toggle-button" title="Toggle Box">
                    <i class="glyph-icon icon-toggle icon-chevron-up"></i>
                </a>
            </h3>
            <div class="content-box-wrapper">
        <@s.form id="saveForm" namespace="/mycenter" action="information_save" method="post" cssClass="center-margin">
        <@s.hidden name="id" value="%{user.id}"/>
        <@s.hidden name="password" value="%{user.password}"/>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="security.user.center.username"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textfield name="username" id="username" value="%{user.username}" disabled="true"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="security.user.center.nickName"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textfield name="nickName" value="%{user.nickName}"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="security.user.center.name"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textfield name="details.name" id="details.name" value="%{user.details.name}"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="security.user.center.birthday"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <input type="text" class="datepicker" data-date-format="yyyy-mm-dd" id="details.birthday" value="<@s.date name="%{user.details.birthday}" format="yyyy-MM-dd"/>" name="details.birthday" >
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="security.user.center.tel"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textfield name="details.tel" id="details.tel" value="%{user.details.tel}"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="security.user.center.email"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textfield name="details.email" value="%{user.details.email}"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div style="float: left;padding-right: 50px;">
                                <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 menu-save4"  onclick="$('#saveForm').submit();return false;" title=" <@s.text name="security.user.center.button.save"/>"  >
                                         <span class="button-content">
                                                 <i class="glyph-icon icon-save float-left"></i>
                                             <@s.text name="security.user.center.button.save"/>
                                          </span>
                                </a>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-2"></div>
                            <div class="form-input col-md-10" id="imageUploader" style="padding-top:8px;padding-left:50px;">
                                <div class="append-right">
                                </div>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="security.user.center.mobile"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                    <@s.textfield name="details.mobile" value="%{user.details.mobile}"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="security.user.center.sex"/>
                                </label>
                            </div>
                            <div class="form-checkbox-radio col-md-9">
                                <div class="append-right">
                                    <@s.radio list=r"#{'male':'男','female':'女'}"  name="details.sex" cssClass="custom-radio" value="%{user.details.sex}"/>
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
</@override>
<@extends name="../wrapper.ftl"/>