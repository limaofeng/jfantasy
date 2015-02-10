<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<div class="example-box" style="padding-left:10px;padding-right:10px;">
    <div class="example-code">
        <div class="content-box box-toggle">
            <h3 class="content-box-header primary-bg">
                <span class="float-left">属性详情</span>
                <a href="javascript:;" class="btn small hover-black float-right back-page" title="返回" style="margin-top: 10px;margin-right: 30px">
                    <i class="glyph-icon icon-reply"></i>
                </a>
            </h3>
        <div class="content-box-wrapper">
            <div class="row">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    编码：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textfield  id="code" name="code"  value="%{attribute.code}" disabled="true" />
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
                                <div class="append-left">
                                    <@s.select cssClass="chosen-select"  value="%{attribute.attributeType.id}"  list="@com.fantasy.attr.service.AttributeTypeService@allAttributeType()" name="attributeType.id" listKey="id" listValue="name" data_placeholder="请选择属性类型" disabled="true"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    描述：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textarea name="description" cssStyle="height: 150px;width:888px;"  value="%{attribute.description}" disabled="true"/>
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
                                <div class="append-right">
                                    <@s.textfield  id="name" name="name"  value="%{attribute.name}" disabled="true"/>
                                </div>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    非空：
                                </label>
                            </div>
                            <div class="form-checkbox-radio col-md-9">
                                <div class="append-right">
                                    <@s.radio name="nonNull" value="%{attribute.nonNull}" list=r"#{true:'是',false:'否'}" disabled="true"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        </div>
    </div>
</div>
</div>
