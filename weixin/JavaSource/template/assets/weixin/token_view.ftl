<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<div class="pad10L pad10R">
<div class="example-box">
<div class="tabs">
    <ul >
        <li>
            <a title="配置信息" href="#normal-tabs-1">
                配置信息
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
                            AppId：
                        </label>
                    </div>
                    <div class="form-input col-md-9">
                        <div class="append-left">
                        <@s.property value="at.appid" escapeHtml="false"/>
                        </div>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-label col-md-3">
                        <label for="">
                            TokenNae：
                        </label>
                    </div>
                    <div class="form-input col-md-9">
                        <div class="append-left">
                            <@s.property value="%{at.tokenName}"/>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-6">
                <div class="form-row">
                    <div class="form-label col-md-3">
                        <label for="">
                            AppSecret：
                        </label>
                    </div>
                    <div class="form-input col-md-9">
                        <div class="append-left">
                            <@s.property value="%{at.appsecret}"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-6">
                <div class="form-row">
                    <div class="form-label col-md-3">
                        <label for="">
                            当前token：
                        </label>
                    </div>
                    <div class="form-input col-md-9">
                        <div class="append-left">
                            <@s.property value="%{at.token}"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-6">
                <div class="form-row">
                    <div class="form-label col-md-3">
                        <label for="">
                            获取时间：
                        </label>
                    </div>
                    <div class="form-input col-md-9">
                        <div class="append-left">
                            <@s.date name="at.modifyTime" format="yyyy-MM-dd hh:mm:ss" />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<div class="form-row" style="text-align: center;">
    <div>
        <div style="float: left;">
            <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "  title="返回" >
                            <span class="glyph-icon icon-separator">
                                  <i class="glyph-icon icon-reply"></i>
                            </span>
                             <span class="button-content">
                                    返回
                             </span>
            </a>
        </div>
    </div>
</div>


</div>
</div>
