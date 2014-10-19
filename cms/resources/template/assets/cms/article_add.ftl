<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function () {
        var imageUploader = $('#imageUploader').upload({data:{'dir':'brand_logo'},theme:'image',size:'160x160'});
        $("#saveForm").ajaxForm({
            beforeSerialize : function(zhis, options){
                var _data = {};
                var _images = imageUploader.getData();
                if(_images.length > 0){
                    _data['logoImageStore'] = _images[0].fileManagerId + ':' + _images[0].absolutePath;
                }
                options.data = _data;
            },
            success: function (data) {
                $('#pager').pager().reload();
                top.$.msgbox({
                    msg: "保存成功",
                    type: "success"
                });
                $(".back-page", $("#saveForm")).backpage();
            }
        });
    });
</script>
<div class="tst_pan">

    <!--
    <div class="infobox warning-bg mrg10B">
        <p><i class="glyph-icon icon-exclamation mrg10R"></i>To view the available grid system options &amp; configurations you can visit the <a title="Fides Admin Grid System documentation" target="_blank" href="grid.html">Fides Admin Grid System documentation</a> page.</p>
    </div>
    -->

    <div class="ie_jf_left">

        <div class="form-row">
            <div class="form-label col-md-2">
                <label for="">
                    文章标题：
                </label>
            </div>
            <div class="form-input col-md-10">
                <input type="text" id="" name="" placeholder="文章标题">
            </div>
        </div>

        <div class="form-row">
            <div class="form-label col-md-2">
                <label for="">
                    摘要：
                </label>
            </div>
            <div class="form-input col-md-10">
                <div class="row">
                    <div class="col-md-6">
                        <input type="text" id="" name="" placeholder="文本">
                    </div>
                    <div class="col-md-6">
                        <input type="text" id="" name="" placeholder="文本">
                    </div>
                </div>
            </div>
        </div>

        <div class="form-row">
            <div class="form-label col-md-2">
                <label for="">
                    地址：
                </label>
            </div>
            <div class="form-input col-md-10">
                <div class="row">
                    <div class="col-md-2">
                        <select name="" id="">
                            <option>省</option>
                            <option>上海</option>
                            <option>北京</option>
                            <option>广州</option>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <select name="" id="">
                            <option>市</option>
                            <option>上海市</option>
                            <option>北京市</option>
                            <option>广州市</option>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <select name="" id="">
                            <option>区</option>
                            <option>开发区</option>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <input type="text" id="" name="" placeholder="街道">
                    </div>
                </div>
            </div>
        </div>


        <div class="form-row">
            <div class="form-label col-md-2">
                <label for="">
                    地址：
                </label>
            </div>
            <div class="form-input col-md-10">
                <div class="row">
                    <div class="col-md-4">
                        <input type="text" id="" name="" placeholder="文本">
                    </div>
                    <div class="col-md-4">
                        <input type="text" id="" name="" placeholder="文本">
                    </div>
                    <div class="col-md-4">
                        <input type="text" id="" name="" placeholder="文本">
                    </div>
                </div>
            </div>
        </div>

        <div class="form-row">
            <div class="form-label col-md-2">
                <label for="">
                    是否推荐：
                </label>
            </div>
            <div class="form-checkbox-radio col-md-10">
                <input type="radio" id="" name="">
                <label for="">是</label>

                <input type="radio" checked="" id="" name="">
                <label for="">否</label>

            </div>
        </div>

        <div class="form-row">
            <div class="form-label col-md-2">
                <label for="">
                    填写分类：
                </label>
            </div>
            <div class="form-checkbox-radio col-md-10">
                <input type="checkbox" id="" name="">
                <label for="">服装</label>

                <input type="checkbox" checked="" id="" name="">
                <label for="">背包</label>

            </div>
        </div>

    </div>

    <div class="ie_jf_right">
        <div style="text-align: center;">
            <div>
                <div style="float: left;padding-right: 50px;">
                    <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 menu-save4"  onclick="$('#saveForm').submit();return false;" title="保存"  >
                        <span class="glyph-icon icon-separator">
                             <i class="glyph-icon icon-save"></i>
                        </span>
                         <span class="button-content">
                         <@s.text name="operation.save"/>
                         </span>
                    </a>
                </div>
                <div style="float: left;">
                    <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "  title="返回" >
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
    </div>

    <div class="example-box" style="display: none;">
    <@s.form id="saveForm" namespace="/cms/article" action="article_save" method="post" cssClass="center-margin">
        <@s.hidden name="category.code" value="%{category.code}"/>
        <a href="javascript:;" class="btn small hover-black float-right back-page" title="" style="margin-top: -30px;margin-right: 30px">
            <i class="glyph-icon icon-reply"></i>
        </a>
        <div class="col-md-6 pad10T">
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        <@s.text name="article.title"/>：
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.textfield name="title" id="title"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        <@s.text name="article.summary"/>：
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.textfield name="summary" id="summary"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        <@s.text name="article.keywords"/>：
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.textfield name="keywords" />
                    </div>
                </div>
            </div>

            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        <@s.text name="article.releaseDate"/>：
                    </label>
                </div>
                <div class="form-input col-md-10 ">
                    <div class="append-left">
                        <input type="text"  name="releaseDate" class="datepicker" data-date-format="yyyy-mm-dd"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        <@s.text name="article.author"/>：
                    </label>
                </div>
                <div class="form-input col-md-10 ">
                    <div class="append-left">
                        <@s.textfield name="author"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        <@s.text name="article.issue"/>：
                    </label>
                </div>
                <div class="form-checkbox-radio col-md-10 ">
                    <div class="append-left">
                        <@s.radio list=r"#{true:'是',false:'否'}"  name="issue" cssStyle="width:20px;" value="true"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-6 form-vertical">
            <div class="form-row">
                <div class="form-label col-md-2"></div>
                <div class="form-input col-md-10" id="imageUploader"></div>
            </div>
        </div>
        <div class="form-row">
            <div class="form-label col-md-2">
                <label for="">
                </label>
            </div>
            <div class="form-input col-md-10">
                <@s.textarea cssClass="ckeditor" name="content" cssStyle="width:900px;height:360px;" />
            </div>
        </div>
        <div class="form-row" style="text-align: center;">
            <div>
                <div style="float: left;padding-right: 50px;">
                    <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 menu-save4"  onclick="$('#saveForm').submit();return false;" title="保存"  >
                        <span class="glyph-icon icon-separator">
                             <i class="glyph-icon icon-save"></i>
                        </span>
                         <span class="button-content">
                              <@s.text name="operation.save"/>
                         </span>
                    </a>
                </div>
                <div style="float: left;">
                    <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "  title="返回" >
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
