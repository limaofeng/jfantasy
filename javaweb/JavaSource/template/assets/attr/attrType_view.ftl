<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<div class="pad10L pad10R">
    <div class="example-box">
    <div class="tabs">
        <ul >
            <li>
                <a title="属性类型" href="#normal-tabs-1">
                    属性类型
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
                                    <@s.property  value="attributeType.name" />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    类型：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                    <@s.property value="attributeType.dataType" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-label  col-md-2">
                        <label class="label-description" for="">
                            类型对应的转换器:
                        </label>
                    </div>
                    <div class="form-checkbox-radio col-md-10">
                        <@s.property value="attributeType.converter.name" />
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-label  col-md-2">
                        <label class="label-description" for="">
                            描述:
                        </label>
                    </div>
                    <div class="form-input col-md-10">
                        <@s.property value="attributeType.description"/>
                    </div>
                </div>
        </div>
    </div>
    </div>
</div>