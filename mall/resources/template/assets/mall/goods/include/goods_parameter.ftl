<table id="goodsParameter" class="table table-hover mrg5B text-center">
    <thead>
    <tr>
        <th class="pad15L" style="width:20px;"><input id="allChecked" type="checkbox" checkAll=".select_box" class="bg-white custom-checkbox"></th>
        <th class="sort" orderBy="name">名称</th>
        <th class="sort" orderBy="format">格式</th>
        <th>说明</th>
        <th style="width:120px;">操作</th>
    </tr>
    </thead>
    <tbody>
    <tr class="template" name="default">
        <td><input type="checkbox" class="select_box custom-checkbox"></td>
        <td><span name="name" class="view-field"></span></td>
        <td><span name="format" class="view-field"></span></td>
        <td><span name="remark" class="view-field"></span></td>
        <td>
            <a title="" data-placement="top" class="btn small hover-blue up" href="javascript:;">
                <i class="glyph-icon icon-circle-arrow-up"></i>
            </a>
            <a title="" data-placement="top" class="btn small hover-green down" href="javascript:;">
                <i class="glyph-icon icon-circle-arrow-down"></i>
            </a>
            <a title="" data-placement="top" class="btn small hover-red remove" href="javascript:;">
                <i class="glyph-icon icon-remove"></i>
            </a>
        </td>
    </tr>
    <tr class="empty">
        <td class="norecord" colspan="5">还没有自定义参数,点击<a href="javascript:;" style="font-weight:bold;">添加商品参数</a></td>
    </tr>
    </tbody>
</table>
<div id="goodsParameterForm" class="row">
    <div class="col-md-12 template" name="default">
        <div class="form-row">
            <div class="form-label col-md-2">
                <label for="">
                    参数名称：
                </label>
            </div>
            <div class="form-input col-md-10">
                <div class="append-left">
                    <input name="name" type="text" class="view-field" disabled="disabled"/>
                </div>
            </div>
        </div>
        <div class="form-row">
            <div class="form-label col-md-2">
                <label for="">
                    参数规格：
                </label>
            </div>
            <div class="form-input col-md-10">
                <div class="append-left">
                    <input name="format" type="text" class="view-field" disabled="disabled"/>
                </div>
            </div>
        </div>
        <div class="form-row">
            <div class="form-label col-md-2">
                <label for="">
                    说明：
                </label>
            </div>
            <div class="form-input col-md-10">
                <div class="append-left">
                    <textarea name="remark" class="view-field" rows="" cols="" disabled="disabled"></textarea>
                </div>
            </div>

        </div>

        <div class="form-row">
            <div class="form-label col-md-2">
                <label for="">
                </label>
            </div>
            <div class="form-input col-md-10">
                <div class="append-left">
                    <a href="javascript:;" class="btn medium bg-blue radius-all-4 mrg5A ui-state-default formNew">
                            <span class="glyph-icon icon-separator">
                                 <i class="glyph-icon icon-plus"></i>
                            </span>
                            <span class="button-content">
                                     添加新的商品参数
                             </span>
                    </a>
                    <a href="javascript:;" class="btn medium bg-blue radius-all-4 mrg5A ui-state-default formEdit">
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

    <div class="col-md-12 template" name="edit">
        <div class="form-row">
            <div class="form-label col-md-2">
                <label for="">
                    参数名称：
                </label>
            </div>
            <div class="form-input col-md-10">
                <div class="append-left">
                    <input name="name" type="text" class="view-field"/>
                </div>
            </div>
        </div>
        <div class="form-row">
            <div class="form-label col-md-2">
                <label for="">
                    参数规格：
                </label>
            </div>
            <div class="form-input col-md-10">
                <div class="append-left">
                    <input name="format" type="text" class="view-field"/>
                </div>
            </div>
        </div>
        <div class="form-row">
            <div class="form-label col-md-2">
                <label for="">
                    说明：
                </label>
            </div>
            <div class="form-input col-md-10">
                <div class="append-left">
                    <textarea name="remark" class="view-field" rows="" cols=""></textarea>
                </div>
            </div>
        </div>
        <div class="form-row">
            <div class="form-label col-md-2">
                <label for="">
                </label>
            </div>
            <div class="form-input col-md-10">
                <div class="append-left">
                    <a href="javascript:;" class="btn medium bg-blue radius-all-4 mrg5A ui-state-default formSave">
                            <span class="glyph-icon icon-separator">
                                 <i class="glyph-icon icon-save"></i>
                            </span>
                            <span class="button-content">
                                     保存商品参数
                             </span>
                    </a>
                    <a href="javascript:;" class="btn medium bg-blue radius-all-4 mrg5A ui-state-default ">
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

<#--
<div class="col-md-6">
    <div class="form-row">
        <div class="form-label col-md-2">
            <label for="">
                参数格式：
            </label>
        </div>
        <div class="form-input col-md-10">
            <div class="append-right">
                <input name="format" type="text" class="ui_input_text w250"/>
            </div>
        </div>
    </div>
</div>
-->

</div>