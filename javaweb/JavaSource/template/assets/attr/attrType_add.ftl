<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function(){
        $("#saveForm").ajaxForm({
            success :function(data){
                $('#pager').pager().reload();
                $.msgbox({
                    msg : "保存成功",
                    type : "success"
                });
                $page$.backpage();
            }
        });
    });
</script>

<div class="example-box" style="padding-left:10px;padding-right:10px;">
    <div class="example-code">
        <div class="content-box box-toggle button-toggle">
            <h3 class="content-box-header primary-bg">
                <span class="float-left">属性类型添加</span>
                <a href="javascript:void(0)" class="float-right icon-separator btn toggle-button" title="Toggle Box">
                    <i class="glyph-icon icon-toggle icon-chevron-up"></i>
                </a>
            </h3>
            <div class="content-box-wrapper">
            <@s.form id="saveForm" namespace="/attr" action="type_save" method="post" cssClass="center-margin">
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
                                        <@s.textfield name="name" id="name"/>
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
                                        <@s.select name="converter.id" list="@com.fantasy.attr.service.ConverterService@findAll()"  listKey="id" listValue="name" cssClass="chosen-select" data_placeholder="请选择类型对应的转换器"/>
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
                                        <@s.textarea name="description" cssStyle="height: 150px;width:888px;"/>
                                    </div>
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="form-label col-md-3">
                                    <label for="">
                                    </label>
                                </div>
                                <div class="form-input col-md-9">
                                    <div class="append-left">
                                        <div style="float:left;">
                                            <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 menu-save4"  onclick="$('#saveForm').submit();return false;" title="保存" >
                                     <span class="button-content">
                                             <i class="glyph-icon icon-save float-left"></i>
                                         保存
                                      </span>
                                            </a>
                                        </div>
                                        <div style="padding-left: 30px;float: left;">
                                            <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "  title="返回" >
                                     <span class="button-content">
                                          <i class="glyph-icon icon-reply"></i>
                                        返回
                                    </span>
                                            </a>
                                        </div>
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
                                        <@s.textfield id="dataType" name="dataType" />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    </div>
                </div>
            </@s.form>
            </div>
        </div>
    </div>
</div>