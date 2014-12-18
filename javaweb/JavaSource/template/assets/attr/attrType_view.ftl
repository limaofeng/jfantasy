<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<div class="example-box" style="padding-left:10px;padding-right:10px;">
    <div class="example-code">
        <div class="content-box box-toggle">
            <h3 class="content-box-header primary-bg">
                <span class="float-left">属性类型详情</span>
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
                                    名称：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textfield name="name" id="name" value="%{attributeType.name}" disabled="true"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    类型对应的转换器：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.select name="converter.id" value="%{attributeType.converter.id}" list="@com.fantasy.attr.service.ConverterService@findAll()"  listKey="id" listValue="name" cssClass="chosen-select" data_placeholder="请选择类型对应的转换器"  disabled="true"/>
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
                                    <@s.textarea name="description" cssStyle="height: 150px;width:888px;" value="%{attributeType.description}"  disabled="true"/>
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
                                    <@s.textfield id="dataType" name="dataType" value="%{attributeType.dataType}"  disabled="true"/>
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