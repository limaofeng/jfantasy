<table id="goodsParameter" class="table table-hover mrg5B text-center">
<@s.if test="category.goodsParameters.size > 0">
    <caption>商品自定义参数</caption>
</@s.if>
    <thead>
    <tr>
        <th style="width:30px;"><input id="allChecked" type="checkbox" checkAll=".select_box"></th>
        <th class="sort" orderBy="name">名称</th>
        <th>规格</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <tr class="template" name="default">
        <td><input type="checkbox" class="select_box" value="{id}"/><input name="customGoodsParameterValues[#index].id" mapping="id" class="view-field" type="hidden"/></td>
        <td>{name}<input name="customGoodsParameterValues[#index].name" mapping="name" class="view-field" type="hidden"/></td>
        <td>{value}<input name="customGoodsParameterValues[#index].value" mapping="value" class="view-field" type="hidden"/></td>
        <td>
            <a title="" data-placement="top" class="btn small bg-blue up" href="javascript:;" data-original-title="Remove">
                <i class="glyph-icon icon-circle-arrow-up"></i>
            </a>
            <a title="" data-placement="top" class="btn small bg-green down" href="javascript:;" data-original-title="Remove">
                <i class="glyph-icon icon-circle-arrow-down"></i>
            </a>
            <a title="" data-placement="top" class="btn small bg-red remove" href="javascript:;" data-original-title="Remove">
                <i class="glyph-icon icon-remove-sign"></i>
            </a>
        </td>
    </tr>
    <tr class="empty"><td class="norecord" colspan="4">还没有自定义参数,点击<a href="javascript:void(0);" class="formNew" style="font-weight:bold;">添加商品参数</a></td></tr>
    </tbody>
</table>
<div class="row">
    <div class="col-md-6">
        <div class="form-row">
            <div class="form-label col-md-3">
                <label for="">
                    参数名称：
                </label>
            </div>
            <div class="form-input col-md-9">
                <div class="append-left">
                    <input name="name" type="text" class="ui_input_text w250"/>
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
                    <a href="javascript:void(0);" id="goodsParameterFormNew" class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button formNew">
                                            <span class="glyph-icon icon-separator">
                                                 <i class="glyph-icon icon-plus"></i>
                                            </span>
                                            <span class="button-content">
                                                     添加新的商品参数
                                             </span>
                    </a>
                    <a href="javascript:void(0);" id="goodsParameterFormEdit" class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button formEdit">
                                            <span class="glyph-icon icon-separator">
                                                 <i class="glyph-icon icon-edit mrg5R"></i>
                                            </span>
                                            <span class="button-content">
                                                     编辑
                                             </span>
                    </a>


                </div>
            </div>
        </div>

    </div>

    <div class="col-md-6">
        <div class="form-row">
            <div class="form-label col-md-3">
                <label for="">
                    参数规格：
                </label>
            </div>
            <div class="form-input col-md-9">
                <div class="append-right">
                    <input name="value" type="text" class="ui_input_text w250"/>
                </div>
            </div>
        </div>
        <div class="form-row">
            <div class="form-label col-md-3">
                <label for="">
                </label>
            </div>
            <div class="form-input col-md-9">
                <div class="append-right">
                    <a href="javascript:void(0);" id="goodsParameterFormSave" class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button formSave">
                                            <span class="glyph-icon icon-separator">
                                                 <i class="glyph-icon icon-save"></i>
                                            </span>
                                            <span class="button-content">
                                                     保存商品参数
                                             </span>
                    </a>
                    <a href="javascript:void(0);" id="goodsParameterFormReset" class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button formReset">
                                            <span class="glyph-icon icon-separator">
                                                 <i class="glyph-icon icon-reply"></i>
                                            </span>
                                            <span class="button-content">
                                                     重置
                                             </span>
                    </a>
                </div>
            </div>
        </div>

    </div>


</div>