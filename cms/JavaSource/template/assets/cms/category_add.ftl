<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function () {
        $("#saveForm").ajaxForm({
            success:function (data) {
                data.isParent = true;
                categoryTree.addNodes(categoryTree.getNodeByParam("code", data.parentCode), data);
                var node = categoryTree.getNodeByParam("code", data.code);
                $page$.backpage();
                $('#' + node.tId + '_span').click();
                $.msgbox({
                    msg: "保存成功",
                    type: "success"
                });
            }
        });
    });
</script>

<div class="example-box" style="padding-left:10px;padding-right:10px;">
    <div class="example-code">
        <div class="content-box box-toggle button-toggle">
            <h3 class="content-box-header primary-bg">
                <span class="float-left">文章分类添加</span>
                <a href="javascript:void(0)" class="float-right icon-separator btn toggle-button" title="Toggle Box">
                    <i class="glyph-icon icon-toggle icon-chevron-up"></i>
                </a>
            </h3>
            <div class="content-box-wrapper">
            <@s.form id="saveForm" namespace="/cms/article" action="category_save" method="post" cssClass="center-margin">
                <@s.hidden name="parent.code" value="%{category.code}"/>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-2">
                                <label for="">
                                    <@s.text name="category.code"/>：
                                </label>
                            </div>
                            <div class="form-input col-md-10">
                                <div class="append-left">
                                    <@s.textfield name="code" /><@s.fielderror fieldName="code"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-2">
                                <label for="">
                                    <@s.text name="category.name"/>：
                                </label>
                            </div>
                            <div class="form-input col-md-10">
                                <div class="append-left">
                                    <@s.textfield name="name"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-2">
                                <label for="">
                                    <@s.text name="category.egname"/>:
                                </label>
                            </div>
                            <div class="form-input col-md-10">
                                <div class="append-left">
                                    <@s.textfield name="egname"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-2">
                                <label for="">
                                </label>
                            </div>
                            <div class="form-input col-md-10">
                                <div class="append-left">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-2">
                                <label for="">
                                    <@s.text name="category.description"/>:
                                </label>
                            </div>
                            <div class="form-input col-md-10">
                                <div class="append-left">
                                    <@s.textarea name="description" cssStyle="width:998px;height: 160px;"/>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
                <div class="col-md-6">
                    <div class="form-row">
                        <div class="form-label col-md-2">
                            <label for="">
                            </label>
                        </div>
                        <div class="form-input col-md-10">
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
            </@s.form>

            </div>

        </div>
    </div>
</div>
