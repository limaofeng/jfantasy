<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>

<div class="pad10L pad10R">
    <div class="example-box">
        <div class="tabs">
            <ul >
                <li>
                    <a title="提醒信息" href="#normal-tabs-1">
                        提醒信息
                    </a>
                </li>
            </ul>
            <a href="javascript:;" class="btn small hover-black float-right back-page" title="" style="margin-top: -30px;margin-right: 30px">
                <i class="glyph-icon icon-reply"></i>
            </a>
            <div id="normal-tabs-1">
                <div class="form-row">
                    <div class="form-label  col-md-2">
                        <label for="">
                            内容:
                        </label>
                    </div>
                    <div class="form-input col-md-10">
                        <@s.property value="notice.content" />
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-label  col-md-2">
                        <label class="label-description" for="">
                            跳转连接:
                        </label>
                    </div>
                    <div class="form-input col-md-10">
                        <@s.property value="notice.url" />
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-label  col-md-2">
                        <label class="label-description" for="">
                            使用模版:
                        </label>
                    </div>
                    <div class="form-input col-md-10">
                    <@s.property value="notice.model.name" />
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-label  col-md-2">
                        <label class="label-description" for="">
                            创建时间:
                        </label>
                    </div>
                    <div class="form-input col-md-10">
                    <@s.date name="%{notice.createTime}" format="yyyy-MM-dd hh:mm:ss"/>
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
