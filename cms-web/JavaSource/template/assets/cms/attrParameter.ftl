<table id="attrParameter" class="table table-hover mrg5B text-center">
    <thead>
    <tr>
        <th class="pad15L" style="width:20px;"><input id="allChecked" type="checkbox" checkAll=".select_box" class="bg-white custom-checkbox"></th>
        <th>属性编码</th>
        <th>属性名称</th>
        <th>属性类型</th>
        <th>是否非空</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <tr class="template" name="default">
        <td><input type="checkbox" class="select_box custom-checkbox"></td>
        <td><span name="code" class="view-field"></span></td>
        <td><span name="name" class="view-field"></span></td>
        <td><span name="attributeType.id" class="view-field"></span></td>
        <td><span name="nonNull" class="view-field"></span></td>
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
    </tbody>
</table>
<div id="attrParameterForm" class="row">
    <div class="template" name="default">
        <div class="col-md-6">
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        属性编码
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                    <input type="hidden" name="id" class="view-field" disabled="disabled"/>
                    <input name="code" type="text" class="view-field" disabled="disabled"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        属性名称：
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <input name="name" type="text" class="view-field" disabled="disabled"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        属性类型：
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                    <@s.select name="attributeType.id" cssClass="chosen-select view-field" list="@com.fantasy.attr.service.AttributeTypeService@allAttributeType()" listKey="id" listValue="name" data_placeholder="请选择属性类型" disabled="true"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        非空：
                    </label>
                </div>
                <div class="form-checkbox-radio col-md-10">
                    <div class="append-left">
                    <@s.radio name="nonNull" value="false" list=r"#{true:'是',false:'否'}" disabled="true" cssClass="view-field"/>
                    </div>
                </div>
            </div>
         </div>
        <div class="form-row">
            <div class="form-label col-md-1">
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
                                新增
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

    <div class="template" name="edit">
        <div class="col-md-6">
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        属性编码
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <input type="hidden" name="id" class="view-field"/>
                        <input name="code" type="text" class="view-field"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        属性名称：
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <input name="name" type="text" class="view-field"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        属性类型：
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                    <@s.select name="attributeType.id" cssClass="chosen-select view-field" list="@com.fantasy.attr.service.AttributeTypeService@allAttributeType()" listKey="id" listValue="name" data_placeholder="请选择属性类型"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        非空：
                    </label>
                </div>
                <div class="form-checkbox-radio col-md-10">
                    <div class="append-left">
                    <@s.radio name="nonNull" value="false" list=r"#{true:'是',false:'否'}" cssClass="view-field"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-row">
            <div class="form-label col-md-1">
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
                           保存
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
</div>