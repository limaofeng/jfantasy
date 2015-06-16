<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function () {
        $("#saveForm").ajaxForm({
            success: function (data) {
                $('#pager').pager().reload();
                top.$.msgbox({
                    msg: "保存成功",
                    type: "success"
                });
                $page$.backpage();
            }
        });
    });
</script>
<div class="example-box" style="padding-left:10px;padding-right:10px;">
    <div class="example-code">
        <div class="content-box box-toggle button-toggle">
            <h3 class="content-box-header primary-bg">
                <span class="float-left">问题添加</span>
                <a href="javascript:void(0)" class="float-right icon-separator btn toggle-button" title="Toggle Box">
                    <i class="glyph-icon icon-toggle icon-chevron-up"></i>
                </a>
            </h3>
            <div class="content-box-wrapper">
            <@s.form id="saveForm" namespace="/question" action="save" method="post" cssClass="center-margin">
                <@s.hidden name="status" value="news"/>
                <@s.hidden  name="category.id" value="%{category.id}" />
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-row">
                            <div class="form-label col-md-1">
                                <label for="">
                                    问题标题：
                                </label>
                            </div>
                            <div class="form-input col-md-11">
                                <div class="append-left">
                                    <@s.textfield name="title"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-1">
                                <label for="">
                                    提问者：
                                </label>
                            </div>
                            <div class="form-input col-md-11">
                                <div class="append-left">
                                    <@s.select name="member.id"  list="@com.fantasy.member.service.MemberService@members()" listKey="id" listValue="username" cssClass="chosen-select view-field" data_placeholder="请选择提问者"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-1">
                                <label for="">
                                    悬赏金额：
                                </label>
                            </div>
                            <div class="form-input col-md-11">
                                <div class="append-left">
                                    <@s.textfield name="offerMoney"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-1">
                                <label for="">
                                    热门问题：
                                </label>
                            </div>
                            <div class="form-input col-md-11">
                                <div class="append-left form-checkbox-radio">
                                    <@s.radio list=r"#{true:'是',false:'否'}"  name="issue"  value="false"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-1">
                                <label for="">
                                   正文：
                                </label>
                            </div>
                            <div class="form-input col-md-11">
                                <div class="append-left">
                                    <@s.textarea cssClass="ckeditor" name="content" cssStyle="width:900px;height:360px;" />
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-1">
                                <label for="">
                                </label>
                            </div>
                            <div class="form-input col-md-11">
                                <div class="append-left">
                                    <div style="float:left;">
                                    <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 menu-save4"  onclick="$('#saveForm').submit();return false;" title="保存" >
                                         <span class="button-content">
                                                 <i class="glyph-icon icon-save float-left"></i>
                                             保存
                                          </span>
                                    </a>
                                    </div>
                                    <div style="padding-left: 30px;float: left;">
                                    <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "  title="返回" >
                                         <span class="button-content">
                                              <i class="glyph-icon icon-reply"></i>
                                            返回
                                        </span>
                                    </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </@s.form>
            </div>
        </div>
    </div>
</div>
