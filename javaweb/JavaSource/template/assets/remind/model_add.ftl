<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function(){
        var imageUploader = $('#imageUploader').upload({data:{'dir':'brand_logo'},theme:'image',size:'160x160'});
        $("#saveForm").ajaxForm({
            beforeSerialize : function(zhis, options){
                var _data = {};
                options.data = _data;
                var _images = imageUploader.getData();
                if(_images.length > 0){
                    _data['modelImageStore'] = _images[0].fileManagerId + ':' + _images[0].absolutePath;
                }
            },
            success :function(data){
                $('#page').pager().reload();
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
<@s.form id="saveForm" namespace="/notice/model" action="save" method="post" cssClass="center-margin">
<div class="tabs">
    <ul >
        <li>
            <a title="模版详情" href="#normal-tabs-1">
                模版详情
            </a>
        </li>
    </ul>
    <a href="javascript:;" class="btn small hover-black float-right back-page" title="" style="margin-top: -30px;margin-right: 30px">
        <i class="glyph-icon icon-reply"></i>
    </a>
    <div id="normal-tabs-1">
        <div class="form-row">
            <div class="form-label  col-md-2">
                <label for="">
                    模版图片:
                </label>
            </div>
            <div class="form-input col-md-10">
                <div id="imageUploader"></div>
            </div>
        </div>
        <div class="form-row">
            <div class="form-label  col-md-2">
                <label for="">
                    编码:
                </label>
            </div>
            <div class="form-input col-md-10">
                <@s.textfield name="code" id="code"/>
            </div>
        </div>
        <div class="form-row">
            <div class="form-label  col-md-2">
                <label for="">
                    模版名称:
                </label>
            </div>
            <div class="form-input col-md-10">
                <@s.textfield name="name" id="name"/>
            </div>
        </div>
        <div class="form-row">
            <div class="form-label  col-md-2">
                <label for="">
                    跳转连接模版:
                </label>
            </div>
            <div class="form-input col-md-10">
                <@s.textfield name="url" id="url"/>
            </div>
        </div>
        <div class="form-row">
            <div class="form-label  col-md-2">
                <label class="label-description" for="">
                    内容模版:
                </label>
            </div>
            <div class="form-input col-md-10">
                <@s.textfield name="content" id="content"/>
            </div>
        </div>
    </div>
</div>
<div class="form-row" style="text-align: center;">
    <div>
        <div style="float: left;padding-right: 50px;padding-left: 27px;">
            <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 menu-save4"  onclick="$('#saveForm').submit();return false;" title="保存"  >
                            <span class="glyph-icon icon-separator">
                                 <i class="glyph-icon icon-save"></i>
                            </span>
                             <span class="button-content">
                                    保存
                             </span>
            </a>
        </div>
        <div style="float: left;">
            <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "  title="返回" >
                            <span class="glyph-icon icon-separator">
                                  <i class="glyph-icon icon-reply"></i>
                            </span>
                             <span class="button-content">
                                    返回
                             </span>
            </a>
        </div>
    </div>
</div>
</@s.form>


</div>
</div>
