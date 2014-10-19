<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
$(function(){
    window.list = $('#bannerParameter').list($('#bannerParameterForm'));
    list.form.data('form').on('change',function(data,templateName,ele){
        if(templateName == 'edit'){
            var bannerUploader = $('#bannerUploader',this.target).upload({data:{'dir':'goodsImages'},theme:'image',size:'300x150'},!!data.bannerImage?[data.bannerImage]:[]);
            bannerUploader.on('uploadComplete',function(){
                var _data = bannerUploader.getData()[0];
                var data = ele.getData();
                data.bannerImage = _data;
                ele.setData(data);
            });
        }else{
            if(!!data.bannerImage) {
                $('.bitemimg').attr('src', data.bannerImage.absolutePath);
            }
        }
    });
    $("#saveForm").ajaxForm({
        beforeSerialize : function(zhis, options){
            var _data = list.getData();
            _data.each(function(){
                this.bannerImageStore = this.bannerImage.fileManagerId + ':' + this.bannerImage.absolutePath;
            });
            options.data = {'bannerItems':_data};
        },
        success :function(data){
            $('#pager').pager().reload();
            $.msgbox({
                msg : "保存成功",
                type : "success"
            });
            $(".back-page").backpage();
        }
	});
});
</script>

<div class="pad10L pad10R">
    <div class="example-box">
        <@s.form id="saveForm" namespace="/cms/banner" action="save" method="post" cssClass="center-margin">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="banner.key"/>:
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textfield name="key" id="key"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="banner.name"/>:
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textfield name="name" id="name"/>
                                </div>
                            </div>
                        </div>
                     </div>
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="banner.size"/>:
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textfield name="size" id="size" value="530x244"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="banner.description"/>:
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textarea name="description" id="description" cssStyle="width:940px;height: 160px;"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

               <#-- <div class="form-label">
                    <label>添加广告图：</label>
                </div>
            <div id="goodsImageUploader"></div>-->
            <table id="view" class="formTable">
                <caption>广告项列表<a href="javascript:void(0)" class="add-item" style="padding-left:50px;">  <@s.text name="banner.bannerItem.add"/></a></caption>
                <tr class="template" name="update" >
                    <td>
                        <input name="bannerItems[#index].id" class="view-field" type="hidden" mapping="id" />
                        <input name="bannerItems[#index].bannerImageStore" class="view-field"  type="hidden" mapping="bannerImageStore"/>
                        <table class="formTable">
                            <tr>
                                <td class="formItem_title w100"><@s.text name="banner.bannerItem.title"/>:</td>
                                <td class="formItem_content">
                                    <span><input name="bannerItems[#index].title" class="view-field w250" type="text" mapping="title"/></span>
                                    <div style="float:right;padding-right:20px;">
                                        <a href="javascript:void(0)" class="cancel" style="padding-left:5px;">取消</a>/
                                        <span class="delete-div"><a class="delete" href="javascript:void(0)">删除</a>/</span>
                                        <a href="javascript:void(0)" class="save" template="update" style="float:right;padding-right:5px;">保存</a>
                                    </div>
                                </td>
                                <td class="formItem_content" rowspan="3">
                                    <div id="uploader"></div>
                                </td>
                            </tr>
                            <tr>
                                <td class="formItem_title w100"><@s.text name="banner.bannerItem.url"/>:</td>
                                <td class="formItem_content"><input name="bannerItems[#index].url" class="view-field w250" type="text" mapping="url"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr class="template" name="default" >
                    <td>
                        <input name="bannerItems[#index].id" class="view-field" type="hidden" mapping="id" />
                        <input name="bannerItems[#index].title" class="view-field" type="hidden" mapping="title" />
                        <input name="bannerItems[#index].bannerImageStore" class="view-field" type="hidden" mapping="bannerImageStore" />
                        <input name="bannerItems[#index].summary" class="view-field" type="hidden" mapping="summary"/>
                        <input name="bannerItems[#index].url" class="view-field w250" type="hidden" mapping="url"/>
                        <table class="formTable">
                            <tr>
                                <td class="formItem_title w100"><@s.text name="banner.bannerItem.title"/>:</td>
                                <td class="formItem_content">
                                    <span>{title}</span>
                                    <div style="float:right;padding-right:20px;">
                                        <a class="delete" href="javascript:void(0)">删除</a>/
                                        <a href="javascript:void(0)" class="switch only" template='update' style="float:right;padding-right:5px;">编辑</a>
                                    </div>
                                </td>
                                <td class="formItem_content" rowspan="3">
                                    <div id="uploader"></div>
                                </td>
                            </tr>
                            <tr>
                                <td class="formItem_title w100"><@s.text name="banner.bannerItem.url"/>:</td>
                                <td class="formItem_content">{url}</td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr class="empty"><td class="norecord" colspan="4">暂无数据</td></tr>
            </table>

        <#include "bannerParameter.ftl">
        <div class="form-row" style="text-align: center;">
            <div>
                <div style="float: left;padding-right: 50px;padding-left: 27px;">
                    <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 menu-save4"  onclick="$('#saveForm').submit();return false;" title="保存"  >
                            <span class="glyph-icon icon-separator">
                                 <i class="glyph-icon icon-save"></i>
                            </span>
                             <span class="button-content">
                                 <@s.text name="banner.bannerItem.save"/>
                             </span>
                    </a>
                </div>
                <div style="float: left;">
                    <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "  title="返回" >
                            <span class="glyph-icon icon-separator">
                                  <i class="glyph-icon icon-reply"></i>
                            </span>
                             <span class="button-content">
                                 <@s.text name="banner.bannerItem.return"/>
                             </span>
                    </a>
                </div>
            </div>
        </div>
        </@s.form>
    </div>
</div>
