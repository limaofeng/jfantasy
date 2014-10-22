<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function(){
        var imageUploader = $('#imageUploader').upload({data:{'dir':'brand_logo'},theme:'image',size:'160x160'});
        $("#saveForm").ajaxForm({
            beforeSerialize : function(zhis, options){
                var _data = {};
                options.data = _data;
                var _images = imageUploader.getData();
                if(_images.length > 0){
                    _data['details.avatarStore'] = _images[0].fileManagerId + ':' + _images[0].absolutePath;
                }
            },
            success :function(data){
                $('#pager').pager().reload();
                $.msgbox({
                    msg : "<@s.text name="member.save.success"/>",
                    type : "success"
                });
                $page$.backpage();
            }
        });

    });
</script>

<div class="pad10L pad10R">
<div class="example-box">
<@s.form id="saveForm" namespace="/member" action="save" method="post" cssClass="center-margin">
<div class="tabs">
<ul >
    <li>
        <a title="<@s.text name="member.aev.account"/>" href="#normal-tabs-1">
            <@s.text name="member.aev.account"/>
        </a>
    </li>
    <li>
        <a title="<@s.text name="member.aev.basic"/>" href="#normal-tabs-2">
            <@s.text name="member.aev.basic"/>
        </a>
    </li>
</ul>
<a href="javascript:;" class="btn small hover-black float-right back-page" title="" style="margin-top: -30px;margin-right: 30px">
    <i class="glyph-icon icon-reply"></i>
</a>
<div id="normal-tabs-1">
    <div class="row">
        <div class="col-md-6">
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        <@s.text name="member.aev.username"/>
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-left">
                        <@s.textfield name="username" id="username"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        <@s.text name="member.aev.nickName"/>
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-left">
                        <@s.textfield name="nickName" id="nickName"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-6">
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        <@s.text name="member.aev.password"/>
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-left">
                        <@s.password name="password" />
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        <@s.text name="member.aev.enabled"/>
                    </label>
                </div>
                <div class="form-checkbox-radio col-md-9">
                    <div class="append-right">
                        <@s.radio list=r"#{true:'是',false:'否'}"  name="enabled" cssStyle="width:20px;" value="true"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="normal-tabs-2">
    <div class="row">
        <div class="col-md-6">
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        <@s.text name="member.aev.name"/>
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-left">
                        <@s.textfield name="details.name" id="details.name"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        <@s.text name="member.aev.sex"/>
                    </label>
                </div>
                <div class="form-checkbox-radio col-md-9">
                    <div class="append-left">
                        <@s.radio list=r"#{'male':'男','female':'女'}"  name="details.sex" cssStyle="width:20px;" value="male"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        <@s.text name="member.aev.vip"/>
                    </label>
                </div>
                <div class="form-checkbox-radio col-md-9">
                    <div class="append-left">
                        <@s.radio list=r"#{true:'是',false:'否'}"  name="details.vip" cssStyle="width:20px;" value="false"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        <@s.text name="member.aev.birthday"/>
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-left">
                        <input type="text" class="datepicker" data-date-format="yyyy-mm-dd" id="details.birthday" name="details.birthday" >
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        <@s.text name="member.aev.tel"/>
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-left">
                        <@s.textfield name="details.tel" id="details.tel"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        <@s.text name="member.aev.score"/>
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-left">
                        <@s.textfield name="details.score" />
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        <@s.text name="member.aev.description"/>
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-left">
                        <@s.textarea name="details.description" id="details.description" cssStyle="width:880px;height: 160px;"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-6">
            <div class="form-row" style="padding-left: 60px;padding-bottom:31px;">
                <div class="form-label col-md-2"></div>
                <div class="form-input col-md-10" id="imageUploader"></div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        <@s.text name="member.aev.mobile"/>
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-right">
                        <@s.textfield name="details.mobile"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        <@s.text name="member.aev.email"/>
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-right">
                        <@s.textfield name="details.email" />
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>
</div>
<div class="form-row" style="text-align: center;">
    <div>
        <div style="float: left;padding-right: 50px;padding-left: 27px;">
            <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 menu-save4"  onclick="$('#saveForm').submit();return false;" title="<@s.text name="member.button.save"/>"  >
                            <span class="glyph-icon icon-separator">
                                 <i class="glyph-icon icon-save"></i>
                            </span>
                             <span class="button-content">
                                 <@s.text name="member.button.save"/>
                             </span>
            </a>
        </div>
        <div style="float: left;">
            <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "  title="<@s.text name="member.button.return"/>" >
                            <span class="glyph-icon icon-separator">
                                  <i class="glyph-icon icon-reply"></i>
                            </span>
                             <span class="button-content">
                                 <@s.text name="member.button.return"/>
                             </span>
            </a>
        </div>
    </div>
</div>
</@s.form>


</div>
</div>
