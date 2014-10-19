<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<div class="pad10L pad10R">
    <div class="example-box">
    <div class="tabs">
        <ul >
            <li>
                <a title="属性转换器" href="#normal-tabs-1">
                    属性转换器
                </a>
            </li>
        </ul>
        <a href="javascript:;" class="btn small hover-black float-right back-page" title="" style="margin-top: -30px;margin-right: 30px">
            <i class="glyph-icon icon-reply"></i>
        </a>
        <div id="normal-tabs-1">
                <div class="row issue-date">
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    名称：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.property  value="converter.name" />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    转换器：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                    <@s.property value="converter.typeConverter" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-label  col-md-2">
                        <label class="label-description" for="">
                            描述:
                        </label>
                    </div>
                    <div class="form-input col-md-10">
                        <@s.property value="converter.description"/>
                    </div>
                </div>
        </div>
    </div>
    </div>
</div>