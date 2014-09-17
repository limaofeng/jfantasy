<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<div class="pad10L pad10R">
<div class="example-box">
<div class="tabs">
    <ul >
        <li>
            <a title=" <@s.text name="member.aev.account"/>" href="#normal-tabs-1">
            <@s.text name="member.aev.account"/>
            </a>
        </li>
        <li>
            <a title=" <@s.text name="member.aev.basic"/>" href="#normal-tabs-2">
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
                            <@s.property value="%{member.username}"/>
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
                            <@s.property value="%{member.nickName}"/>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-6">
                <div class="form-row">
                    <div class="form-label col-md-3">
                        <label for="">
                        </label>
                    </div>
                    <div class="form-input col-md-9">
                        <div class="append-left">
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
                            <@s.radio list=r"#{true:'是',false:'否'}"  name="enabled" cssStyle="width:20px;" value="%{member.enabled}" disabled="true"/>
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
                            <@s.property value="%{member.details.name}"/>
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
                        <@s.radio list=r"#{'male':'男','female':'女'}"  name="details.sex" cssStyle="width:20px;" value="%{member.details.sex}" disabled="true"/>
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
                        <@s.radio list=r"#{true:'是',false:'否'}"  name="details.vip" cssStyle="width:20px;" value="%{member.details.vip}" disabled="true"/>
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
                            <@s.date name="%{member.details.birthday}" format="yyyy-MM-dd"/>
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
                            <@s.property value="%{member.details.tel}"/>
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
                        <div class="append-right">
                        <@s.property value="%{member.details.score}"/>
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
                        <@s.textarea name="details.description" id="details.description" cssStyle="width:880px;height: 160px;" value="%{member.details.description}" readonly="true"/>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-6">
                <div class="form-row" style="padding-left: 60px;padding-bottom:31px;">
                    <div class="form-label col-md-2"></div>
                    <div class="form-input col-md-10">
                    <@s.img src="%{member.details.avatar.absolutePath}" ratio="160x160" cssClass="img-thumbnail"/>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-label col-md-3">
                        <label for="">
                        <@s.text name="member.aev.mobile"/>
                        </label>
                    </div>
                    <div class="form-input col-md-9">
                        <div class="append-right">
                            <@s.property value="%{member.details.mobile}"/>
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
                            <@s.property value="%{member.details.email}"/>
                        </div>
                    </div>
                </div>


            </div>

            <div class="col-md-6">

            </div>
        </div>
    </div>
</div>
</div>
</div>
