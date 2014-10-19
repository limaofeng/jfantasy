<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function () {
        $("#saveForm").ajaxForm({
            success:function (data) {
                data.isParent = true;
                var node = categoryTree.getNodeByParam("code", data.code);
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

<div class="pad10L pad10R">
<div class="example-box">
<@s.form id="saveForm" namespace="/cms/article" action="category_save" method="post" cssClass="center-margin">
    <@s.hidden name="parent.code" value="%{category.parent.code}"/>
    <@s.hidden name="code" value="%{category.code}"/>
         <div class="row">
            <div class="col-md-6">
                <div class="form-row">
                    <div class="form-label col-md-2">
                        <label for="">
                            <@s.text name="category.code"/>:
                        </label>
                    </div>
                    <div class="form-input col-md-10">
                        <div class="append-left">
                            <@s.textfield name="code" disabled="true"  value="%{category.code}"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-row">
                    <div class="form-label col-md-2">
                        <label for="">
                            <@s.text name="category.name"/>:
                        </label>
                    </div>
                    <div class="form-input col-md-10">
                        <div class="append-left">
                            <@s.textfield name="name" value="%{category.name}"/>
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
                             <@s.textfield name="egname"  value="%{category.egname}"/>
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
                             <@s.textarea name="description" cssStyle="width:998px;height: 160px;"  value="%{category.description}"/>
                         </div>
                     </div>
                 </div>
             </div>
         </div>
    <div class="form-row">
        <div>
            <div style="float: left;padding-right: 50px;padding-left: 27px;">
                <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 menu-save4"
                   onclick="$('#saveForm').submit();return false;" title="保存">
                                <span class="glyph-icon icon-separator">
                                     <i class="glyph-icon icon-save"></i>
                                </span>
                                 <span class="button-content">
                                     <@s.text name="operation.save"/>
                                 </span>
                </a>
            </div>
            <div style="float: left;">
                <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "
                   title="返回">
                                <span class="glyph-icon icon-separator">
                                      <i class="glyph-icon icon-reply"></i>
                                </span>
                                 <span class="button-content">
                                     <@s.text name="operation.back"/>
                                 </span>
                </a>
            </div>
        </div>
    </div>
</@s.form>
</div>
</div>
