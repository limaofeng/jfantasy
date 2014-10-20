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
                $page$.backpage();
            }
        });
    });
</script>

<div class="pad10L pad10R">
<div class="example-box">
<@s.form id="saveForm" namespace="/admin/notice" action="save" method="post" cssClass="center-margin">
<div class="tabs">
    <ul >
        <li>
            <a title="公告信息" href="#normal-tabs-1">
                公告
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
                        标题:
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <@s.textarea name="title" id="title"/>
                </div>
            </div>

            <div class="form-row">
                <div class="form-label  col-md-2">
                    <label class="label-description" for="">
                        内容:
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <@s.textarea name="content" id="content"/>
                </div>
            </div>

            <div class="form-row">
                <div class="form-label  col-md-2">
                    <label class="label-description" for="">
                        是否发布:
                    </label>
                </div>
                <div class="form-checkbox-radio col-md-10">
                    <@s.radio name="issue" id="issue" value="true" list=r"#{true:'是',false:'否'}" />
                </div>
            </div>

            <div class="row issue-date">
                <div class="col-md-6">
                    <div class="form-row">
                        <div class="form-label col-md-3">
                            <label for="">
                                预发布时间：
                            </label>
                        </div>
                        <div class="form-input col-md-9">
                            <div class="append-left">
                                <input id="startDate" class="datepicker" type="text" name="startDate" data-date-format="yyyy-mm-dd" />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-row">
                        <div class="form-label col-md-3">
                            <label for="">
                                预结束时间：
                            </label>
                        </div>
                        <div class="form-input col-md-9">
                            <div class="append-right">
                                <input id="endDate" class="datepicker" type="text" name="endDate" data-date-format="yyyy-mm-dd">
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
