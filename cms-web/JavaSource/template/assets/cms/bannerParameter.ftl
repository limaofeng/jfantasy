<table id="bannerParameter" class="table table-hover mrg5B text-center">
    <thead>
    <tr>
        <th class="pad15L" style="width:20px;"><input id="allChecked" type="checkbox" checkAll=".select_box" class="bg-white custom-checkbox"></th>
        <th class="sort" orderBy="title">标题</th>
        <th class="sort" orderBy="summary">摘要</th>
        <th>url</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <tr class="template" name="default">
        <td><input type="checkbox" class="select_box custom-checkbox"></td>
        <td><span name="title" class="view-field"></span></td>
        <td><span name="summary" class="view-field"></span></td>
        <td><span name="url" class="view-field"></span></td>
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
    <tr class="empty">
        <td class="norecord" colspan="4">还没有广告位,点击添加新的广告位开始添加</a></td>
    </tr>
    </tbody>
</table>
<div id="bannerParameterForm" class="row">
    <div class="template" name="default">
        <div class="col-md-6">
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        标题：
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-left">
                        <input name="title" type="text" class="view-field" disabled="disabled"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        摘要：
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-left">
                        <input name="summary" type="text" class="view-field" disabled="disabled"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        url：
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-left">
                        <input name="url" type="text" class="view-field" disabled="disabled"/>
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
                                         添加新的广告位
                                 </span>
                        </a>
                        <a href="javascript:;" class="btn medium bg-blue radius-all-4 mrg5A ui-state-default formEdit">
                                <span class="glyph-icon icon-separator">
                                     <i class="glyph-icon icon-edit mrg5R"></i>
                                </span>
                                <span class="button-content">
                                         编辑广告位
                                 </span>
                        </a>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="form-row">
                <div class="form-input col-md-9">
                    <div class="append-right">
                        <img class="bitemimg" data-src="holder.js/300x150"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="template" name="edit">
    <div class="col-md-6 " >
        <div class="form-row">
            <div class="form-label col-md-3">
                <label for="">
                    标题：
                </label>
            </div>
            <div class="form-input col-md-9">
                <div class="append-left">
                    <input name="title" type="text" class="view-field"/>
                </div>
            </div>
        </div>
        <div class="form-row">
            <div class="form-label col-md-3">
                <label for="">
                    摘要：
                </label>
            </div>
            <div class="form-input col-md-9">
                <div class="append-left">
                    <input name="summary" type="text" class="view-field"/>
                </div>
            </div>
        </div>
        <div class="form-row">
            <div class="form-label col-md-3">
                <label for="">
                    url：
                </label>
            </div>
            <div class="form-input col-md-9">
                <div class="append-left">
                    <input name="url" type="text" class="view-field"/>
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
                                     保存广告位
                             </span>
                    </a>
                    <a href="javascript:;" class="btn medium bg-blue radius-all-4 mrg5A ui-state-default ">
                            <span class="glyph-icon icon-separator">
                                 <i class="glyph-icon icon-reply"></i>
                            </span>
                            <span class="button-content">
                                     重置广告位
                             </span>
                    </a>
                </div>
            </div>
        </div>

    </div>
    <div class="col-md-6">
        <div class="form-row">
            <div class="form-input col-md-9">
                <div class="append-right">
                    <div id="bannerUploader"></div>
                </div>
            </div>
        </div>
    </div>
    </div>
</div>