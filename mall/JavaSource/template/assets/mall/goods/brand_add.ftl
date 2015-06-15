<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function () {
        window.categoryFilter = function (data) {
            if (!data.children) {
                delete data.children;
            }
            if (!data.parentId) {
                data.open = true;
            }
            data.isParent = true;
            return data;
        };
        window.categorys = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(categorys)" escapeHtml="false"/>;
        categorys.each(function () {
            categoryFilter(this);
        });
        window.categoryTree = $('#categoryTree').zTree({
            data: {
                key: {
                    name: "name"
                },
                simpleData: {
                    enable: true,
                    idKey: "id",
                    pIdKey: "parentId"
                }
            }, edit: {
                enable: false,
                showAddBtn: false,
                showRemoveBtn: false,
                showRenameBtn: false
            },
            check: {
                enable: true
            },
            callback: {
                onClick: function (e, treeId, treeNode) {
                    return false;
                }
            }}, categorys);
        var imageUploader = $('#imageUploader').upload({data:{'dir':'brand_logo'},theme:'image',size:'160x160'});
        $("#saveForm").ajaxForm({
            beforeSerialize : function(zhis, options){
                var _data = {};
                categoryTree.getCheckedNodes(true).each(function(_i){
                    _data['goodsCategories['+_i+'].id'] = this.id;
                });
                var _images = imageUploader.getData();
                if(_images.length > 0){
                    _data['logoImage'] = _images[0].fileManagerId + ':' + _images[0].absolutePath;
                }
                options.data = _data;
            },
            success: function (data) {
                $('#pager').pager().reload();
                top.$.msgbox({
                    msg: "<@s.text name="mall.brand.save.success"/>",
                    type: "success"
                });
                $page$.backpage();
            }
        });
    });
</script>
<div class="pad10L pad10R">
    <div class="infobox warning-bg mrg10B">
        <p><i class="glyph-icon icon-exclamation mrg10R"></i>To view the available grid system options &amp; configurations you can visit the <a title="Fides Admin Grid System documentation" target="_blank" href="grid.html">Fides Admin Grid System documentation</a> page.</p>
    </div>
<div class="example-box">
<@s.form id="saveForm" namespace="/mall/goods/brand" action="save" method="post" cssClass="center-margin">
        <div class="col-md-6 pad10T">
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        <@s.text name="mall.brand.aev.name"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.textfield name="name" id="name"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        <@s.text name="mall.brand.aev.engname"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.textfield name="engname" id="engname"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        <@s.text name="mall.brand.aev.url"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.textfield name="url" id="url"/>
                    </div>
                </div>
            </div>
            <div class="divider"></div>
            <div class="form-row form-vertical">
                <div class="form-label col-md-2"> <@s.text name="mall.brand.aev.brandcategory"/></div>
                <div class="form-input col-md-10"><ul id="categoryTree" class="ztree" style="min-height:73px;"></ul></div>
            </div>
            <div class="divider"></div>
        </div>
        <div class="col-md-6 form-vertical">
            <div class="form-row">
                <div class="form-label col-md-2"></div>
                <div class="form-input col-md-10" id="imageUploader"></div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        <@s.text name="mall.brand.aev.introduction"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <@s.textarea name="introduction" id="introduction" cssClass="small-textarea" cssStyle="height: 90px;"/>
                </div>
            </div>
        </div>
        <div class="form-row">
            <div class="form-input col-md-10 col-md-offset-2">
                <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4" onclick="$('#saveForm').submit();return false;" title=" <@s.text name="mall.brand.button.save"/>">
                <span class="glyph-icon icon-separator">
                     <i class="glyph-icon icon-save"></i>
                </span>
                 <span class="button-content">
                     <@s.text name="mall.brand.button.save"/>
                 </span>
                </a>
                <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch back-page" title=" <@s.text name="mall.brand.button.return"/>">
                <span class="glyph-icon icon-separator">
                      <i class="glyph-icon icon-reply"></i>
                </span>
                 <span class="button-content">
                     <@s.text name="mall.brand.button.return"/>
                 </span>
                </a>
            </div>
        </div>

</@s.form>
</div>
</div>
