<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function(){
        $("#saveForm").ajaxForm({
            success :function(data){
                $('#page').pager().reload();
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
    <@s.form id="saveForm" namespace="/attr" action="version_save" method="post" cssClass="center-margin">
    <div class="tabs">
        <ul >
            <li>
                <a title="属性类型" href="#normal-tabs-1">
                    属性类型
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
                                    版本号：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textfield  id="number" name="number" />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    类名：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                    <@s.textfield  id="className" name="className" />
                                </div>
                            </div>
                        </div>
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