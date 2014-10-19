<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>

<div class="pad10L pad10R">
    <div class="example-box">
    <div class="tabs">
        <ul >
            <li>
                <a title="属性" href="#normal-tabs-1">
                    属性
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
                                    编码：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.property value="attribute.code" />
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    属性类型：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                    <@s.property value="attribute.attributeType.name" />
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    非空：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                    <@s.if test="attribute.nonNull==true">
                                        是
                                    </@s.if>
                                    <@s.else>
                                        否
                                    </@s.else>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    名称：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.property value="attribute.name" />
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    是否非临时：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.if test="attribute.notTemporary==true">
                                        是
                                    </@s.if>
                                    <@s.else>
                                        否
                                    </@s.else>
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
                        <@s.property value="attribute.description"/>
                    </div>
                </div>
        </div>
    </div>
    </div>
</div>