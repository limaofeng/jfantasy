<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function(){
    <@s.if test="%{category == null}">
        <@s.set name="brands" value="@com.fantasy.mall.goods.service.BrandService@listBrands()"/>
    </@s.if>
    <@s.else>
        <@s.set name="brands" value="category.brands"/>
    </@s.else>
        $("#brand_view").view().setJSON(<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(@com.fantasy.framework.util.common.ObjectUtil@sort(#brands,category.brandCustomSortIds,'id'))" escapeHtml="false" default="[]"/>);
        //商品参数维护
        var list = $("#goodsParameter").list($('#goodsParameterForm'),<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(category.goodsParameters)" escapeHtml="false" default="[]"/>);
        //ajax保存操作
        $("#saveForm").ajaxForm({
            beforeSerialize : function(zhis, options){
                var _data = {};
                list.getData().each(function(_i){
                    _data['goodsParameters['+_i+'].name'] = this.name;
                    _data['goodsParameters['+_i+'].format'] = this.format;
                    _data['goodsParameters['+_i+'].remark'] = this.remark;
                });
                options.data = _data;
            },
            success:function(data) {
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

<div class="pad10L pad10R">
    <div class="example-box">
<@s.form id="saveForm" namespace="/mall/goods/category" action="save" method="post" cssClass="center-margin">
    <@s.hidden name="id" value="%{category.id}" />
    <@s.hidden name="parent.id" value="%{category.parent.id}"/>
    <@s.hidden name="sign" value="%{category.sign}"/>
<div class="tabs">
<ul>
    <li>
        <a title=" <@s.text name="mall.goods.category.ae.basic"/>" href="#normal-tabs-1">
            <@s.text name="mall.goods.category.ae.basic"/>
        </a>
    </li>
    <li>
        <a title=" <@s.text name="mall.goods.category.ae.parameter"/>" href="#normal-tabs-2">
            <@s.text name="mall.goods.category.ae.parameter"/>
        </a>
    </li>
    <li>
        <a title=" <@s.text name="mall.goods.category.ae.search"/>" href="#normal-tabs-5">
            <@s.text name="mall.goods.category.ae.search"/>
        </a>
    </li>
</ul>
    <a href="javascript:;" class="btn small hover-black float-right back-page" title="" style="margin-top: -30px;margin-right: 30px">
        <i class="glyph-icon icon-reply"></i>
    </a>
<div id="normal-tabs-1">
    <div class="row">
        <div class="col-md-6">
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        <@s.text name="mall.goods.category.ae.code"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.textfield name="sign"  value="%{category.sign}" disabled="true"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        <@s.text name="mall.goods.category.ae.name"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.textfield name="name"  value="%{category.name}" />
                    </div>
                </div>
            </div>
        </div>
    </div>
    <#include "include/brand.ftl">
</div>
<div id="normal-tabs-2">
    <#include "include/goods_parameter.ftl">
</div>
<div id="normal-tabs-5">
    <div class="example-box">
        <div class="example-code center-margin">
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        <@s.text name="mall.goods.category.ae.metaKeywords"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <@s.textfield name="metaKeywords" cssClass="tagsInput" placeholder="添加关键词" value="%{category.metaKeywords}"/>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        <@s.text name="mall.goods.category.ae.metaDescription"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <@s.textarea name="metaDescription"  value="%{category.metaDescription}"/>
                </div>
            </div>
        </div>

    </div>
</div>
<div class="form-row" style="text-align: center;">
    <div>
        <div style="float: left;padding-right: 50px;padding-left: 27px;">
            <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 menu-save4"  onclick="$('#saveForm').submit();return false;" title="<@s.text name="mall.goods.save"/>"  >
                            <span class="glyph-icon icon-separator">
                                 <i class="glyph-icon icon-save"></i>
                            </span>
                             <span class="button-content">
                                 <@s.text name="mall.goods.save"/>
                             </span>
            </a>
        </div>
        <div style="float: left;">
            <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "  title="<@s.text name="mall.goods.back"/>" >
                            <span class="glyph-icon icon-separator">
                                  <i class="glyph-icon icon-reply"></i>
                            </span>
                             <span class="button-content">
                                 <@s.text name="mall.goods.back"/>
                             </span>
            </a>
        </div>
    </div>
</div>
</@s.form>
</div>
</div>
