<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="pageTitle">
    修改密码
<small>
</small>
</@override>
<@override name="head">
<script type="text/javascript">
    $(function () {
        $("#saveForm").ajaxForm({
            success: function (data) {
                $.msgbox({
                    msg: "密码修改成功,将在下次登录时生效!",
                    type: "success"
                });
                $(".pd").val("");
            }
        });


        $(".clean").click(function(){
            $(".pd").val("");
            return false;
        })


    });
</script>
</@override>
<@override name="pageContent">
<div class="example-box">
    <div class="example-code">
        <div class="content-box box-toggle button-toggle">
            <h3 class="content-box-header primary-bg">
                <span class="float-left">修改密码</span>
                <a href="javascript:void(0)" class="float-right icon-separator btn toggle-button" title="Toggle Box">
                    <i class="glyph-icon icon-toggle icon-chevron-up"></i>
                </a>
            </h3>
            <div class="content-box-wrapper">
                <@s.form id="saveForm" namespace="/security/user" action="resetpwd" method="post" cssClass="center-margin">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-row">
                                <div class="form-label col-md-3">
                                    <label for="">
                                        旧密码：
                                    </label>
                                </div>
                                <div class="form-input col-md-9">
                                    <div class="append-left">
                                        <input type="password" name="oldPwd" class="pd">
                                    </div>
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="form-label col-md-3">
                                    <label for="">
                                        新密码：
                                    </label>
                                </div>
                                <div class="form-input col-md-9">
                                    <div class="append-left">
                                        <input type="password" name="newPwd" class="pd">
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
                                            <div style="float: left;padding-right: 50px;">
                                                <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 menu-save4"  onclick="$('#saveForm').submit();return false;" title=" <@s.text name="security.user.center.button.save"/>"  >
                                                    <span class="button-content">
                                                     <i class="glyph-icon icon-save float-left"></i>
                                                 修改
                                                    </span>
                                                </a>
                                            </div>
                                            <div style="float: left;">
                                            <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 clean" title=" 清空"  >
                                                <span class="button-content">
                                                 <i class="glyph-icon icon-refresh float-left"></i>
                                                     清空
                                                 </span>
                                            </a>
                                            </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-row">

                            </div>
                        </div>
                    </div>
                </@s.form>

            </div>

        </div>
    </div>
</div>
</@override>
<@extends name="../wrapper.ftl"/>