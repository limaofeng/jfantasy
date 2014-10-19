<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>

<div class="pad10L pad10R">
    <div class="example-box">
        <div class="tabs">
            <ul >
                <li>
                    <a title="公告信息" href="#normal-tabs-1">
                        公告详情
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
                            标题:
                        </label>
                    </div>
                    <div class="form-input col-md-10">
                        <@s.property value="notice.title" />
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-label  col-md-2">
                        <label class="label-description" for="">
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
                            是否发布:
                        </label>
                    </div>
                    <div class="form-checkbox-radio col-md-10">
                        <@s.if test="notice.issue">
                            是
                        </@s.if>
                        <@s.else>
                            否
                        </@s.else>
                    </div>
                </div>

                <div class="row issue-date">
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    预发布时间：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.date name="notice.startDate" format="yyyy-MM-dd"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    预结束时间：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                    <@s.date name="notice.endDate" format="yyyy-MM-dd"/>
                                </div>
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
