<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function () {
        $("#saveForm").ajaxForm({
            success:function (data) {
                data.isParent = true;
                var node = categoryTree.getNodeByParam("id", data.id);
                categoryTree.updateNode(Fantasy.copy(node, categoryFilter(data)), false);
                $(".back-page").backpage();
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
                <span class="float-left">问题分类编辑</span>
                <a href="javascript:void(0)" class="float-right icon-separator btn toggle-button" title="Toggle Box">
                    <i class="glyph-icon icon-toggle icon-chevron-up"></i>
                </a>
            </h3>
            <div class="content-box-wrapper">
            <@s.form id="saveForm" namespace="/question/category" action="save" method="post" cssClass="center-margin">
                <@s.hidden name="id" value="%{category.id}" />
                <@s.hidden name="parent.id" value="%{category.parent.id}"/>
                <@s.hidden name="sign" value="%{category.sign}"/>
                <div class="row">
                <div class="col-md-6">
                    <div class="form-row">
                        <div class="form-label col-md-2">
                            <label for="">
                               分类编码：
                            </label>
                        </div>
                        <div class="form-input col-md-10">
                            <div class="append-left">
                                <@s.textfield name="sign" value="%{category.sign}" disabled="true"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-row">
                        <div class="form-label col-md-2">
                            <label for="">
                               分类名称：
                            </label>
                        </div>
                        <div class="form-input col-md-10">
                            <div class="append-left">
                                <@s.textfield name="name"  value="%{category.name}"/>
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
            </div>
            </@s.form>

            </div>

        </div>
    </div>
</div>

