<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
$(function(){
	var goodsImageUploader = $('#goodsImageUploader').upload({data:{'dir':'goods_image'},theme:'images',size:'160x160'});
    var list = $('#goodsParameter').list($('#goodsParameterForm'));
    $("#saveForm").ajaxForm({
        beforeSerialize : function(zhis, options){
           /* var _data = {};
            list.getData().each(function(_i){
                _data['customGoodsParameterValues['+_i+'].id'] = this.id;
                _data['customGoodsParameterValues['+_i+'].name'] = this.name;
                _data['customGoodsParameterValues['+_i+'].value'] = this.value;
            });
            options.data ={ _data, 'goodsImage':goodsImageUploader.getSimpleData()};
            console.log(goodsImageUploader.getData());*/
            options.data = {'customGoodsParameterValues':list.getData(),'goodsImages':goodsImageUploader.getSimpleData()};
        },
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

<div class="pad10L pad10R">
    <div class="example-box">
        <@s.form id="saveForm" namespace="/mall/goods" action="save" method="post" cssClass="center-margin">
            <@s.hidden  name="category.id" value="%{category.id}" />
            <@s.hidden  name="weight" value="0" />
        <div class="tabs">
            <ul >
                <li>
                    <a title="<@s.text name='mall.goods.base' />" href="#normal-tabs-1">
                        <@s.text name='mall.goods.base' />
                    </a>
                </li>
                <li>
                    <a title="<@s.text name='mall.goods.detail' />" href="#normal-tabs-2">
                        <@s.text name='mall.goods.detail' />
                    </a>
                </li>
                <li>
                    <a title="<@s.text name='mall.goods.paras' />" href="#normal-tabs-3">
                        <@s.text name='mall.goods.paras' />
                    </a>
                </li>
                <li>
                    <a title="<@s.text name='mall.goods.goodsImgs' />" href="#normal-tabs-4">
                        <@s.text name='mall.goods.goodsImgs' />
                    </a>
                </li>
                <li>
                    <a title="<@s.text name='mall.goods.searchGood' />" href="#normal-tabs-5">
                        <@s.text name='mall.goods.searchGood' />
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
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name='mall.goods.title' />：
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
                                    <@s.text name='mall.goods.marketPrice' />：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textfield name="marketPrice" id="marketPrice"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name='mall.goods.price' />：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textfield name="price" id="price"/>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-6">

                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name='mall.goods.english' />：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                    <@s.textfield name="engname" />
                                </div>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name='mall.goods.brand' />：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                 <@s.select name="brand.id"  list="%{category.brands}" cssClass="chosen-select" listKey="id" listValue="name + ' - (' + engname + ')'" />
                                </div>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name='mall.goods.cost' />：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                    <@s.textfield name="cost" id="cost"/>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
            <div id="normal-tabs-2">
                <@s.textarea cssClass="ckeditor" name="introduction" cssStyle="width:900px;height:360px;" />
            </div>
            <div id="normal-tabs-3">
                <@s.if test="category.goodsParameters.size > 0">
                    <div class="row">

                            <@s.iterator value="category.goodsParameters" var="goodsParameter" status="st">
                                <@s.if test="#st.odd">
                                <div class="col-md-6">
                                    <div class="form-row">
                                        <div class="form-label col-md-2">
                                            <label for="">
                                                <@s.hidden value="%{#goodsParameter.id}">
                                                    <@s.param name="name">
                                                        goodsParameterValues[<@s.property value="#st.index"/>].id
                                                    </@s.param>
                                                </@s.hidden>
                                                <@s.property value="#goodsParameter.name"/>
                                            </label>
                                        </div>
                                        <div class="form-input col-md-10">
                                            <div class="append-left">
                                                <@s.textfield name="goodsParameterValues[%{#st.index}].value" value="%{@com.fantasy.framework.util.common.ObjectUtil@find(goods.goodsParameterValues,'id',#goodsParameter.id).value}" cssClass="w250" placeholder="%{#goodsParameter.format}"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                </@s.if>
                                <@s.else>
                                <div class="col-md-6">
                                    <div class="form-row">
                                        <div class="form-label col-md-2">
                                            <label for="">
                                                <@s.hidden value="%{#goodsParameter.id}">
                                                    <@s.param name="name">
                                                        goodsParameterValues[<@s.property value="#st.index"/>].id
                                                    </@s.param>
                                                </@s.hidden>
                                                <@s.property value="#goodsParameter.name"/>
                                            </label>
                                        </div>
                                        <div class="form-input col-md-10">
                                            <div class="append-left">
                                                <@s.textfield name="goodsParameterValues[%{#st.index}].value" value="%{@com.fantasy.framework.util.common.ObjectUtil@find(goods.goodsParameterValues,'id',#goodsParameter.id).value}" cssClass="w250" placeholder="%{#goodsParameter.format}"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                </@s.else>
                            </@s.iterator>
                     </div>
                </@s.if>
                <#include "include/goods_customGoodsParameter.ftl">
            </div>
            <div id="normal-tabs-4">
                <#include "include/goods_images.ftl"/>
            </div>
            <div id="normal-tabs-5">
                <div class="example-box">
                    <div class="example-code center-margin">
                            <div class="form-row">
                                <div class="form-label col-md-2">
                                    <label for="">
                                        <@s.text name='mall.goods.metaKeywords' />：
                                    </label>
                                </div>
                                <div class="form-input col-md-10">
                                    <@s.textfield name="metaKeywords" cssClass="tags" cssStyle="width:98%;"/>
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="form-label col-md-2">
                                    <label for="">
                                        <@s.text name='mall.goods.metaDescription' />：
                                    </label>
                                </div>
                                <div class="form-input col-md-10">
                                    <@s.textarea name="metaDescription"  cssStyle="width:98%;"/>
                                </div>
                            </div>
                    </div>

                </div>
            </div>
        </div>
        <div class="form-row" style="text-align: center;">
            <div>
                <div style="float: left;padding-right: 50px;padding-left: 27px;">
                    <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 menu-save4"  onclick="$('#saveForm').submit();return false;" title="<@s.text name='mall.goods.save' />"  >
                        <span class="glyph-icon icon-separator">
                             <i class="glyph-icon icon-save"></i>
                        </span>
                         <span class="button-content">
                             <@s.text name='mall.goods.save' />
                         </span>
                    </a>
                </div>
                <div style="float: left;">
                    <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "  title="<@s.text name='mall.goods.back' />" >
                        <span class="glyph-icon icon-separator">
                              <i class="glyph-icon icon-reply"></i>
                        </span>
                         <span class="button-content">
                             <@s.text name='mall.goods.back' />
                         </span>
                    </a>
                </div>
            </div>
        </div>
        </@s.form>


    </div>
</div>
