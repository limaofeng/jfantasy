<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<div class="example-box" style="padding-left:10px;padding-right:10px;">
    <div class="example-code">
        <div class="content-box box-toggle button-toggle">
            <h3 class="content-box-header primary-bg">
                <span class="float-left">问题详情</span>
                <a href="javascript:void(0)" class="float-right icon-separator btn toggle-button" title="Toggle Box">
                    <i class="glyph-icon icon-toggle icon-chevron-up"></i>
                </a>
            </h3>
            <div class="content-box-wrapper">
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
                                    <@s.textfield name="title" value="%{question.title}" disabled="true"/>
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
                                    <@s.textarea cssClass="ckeditor" name="content" cssStyle="width:900px;height:360px;" value="%{question.content}" disabled="true"/>
                                </div>
                            </div>
                        </div>


                        <@s.if test="question.answers.size()>0"><li class="divider"></li></@s.if>
                        <@s.iterator value="question.answers" var="answer" status="st">
                            <div class="form-row">
                                <div class="form-label col-md-1">
                                    <label for="">
                                        会员回答：
                                    </label>
                                </div>
                                <div class="form-input col-md-11">
                                    <div class="append-left">
                                       <span class="form-label"> <label for=""><@s.property value="#answer.member.realName"/>：<@s.property value="#answer.content"/> </label></span>
                                        <div style="float: right;">
                                            <i class="glyph-icon icon-thumbs-up"></i>
                                                <@s.if test="#answer.praise==null">
                                                    (<@s.property value="0"/>)
                                                </@s.if>
                                                <@s.else> (<@s.property value="#answer.praise"/>)</@s.else>
                                            <i class="glyph-icon icon-thumbs-down"></i>
                                            <@s.if test="#answer.unpraise==null">
                                                (<@s.property value="0"/>)
                                            </@s.if>
                                            <@s.else> (<@s.property value="#answer.unpraise"/>)</@s.else>
                                        </div>
                                    </div>
                                </div>
                            </div>
                             <@s.iterator value="#answer.additionals" var="additional">
                            <div class="form-row" style="padding-left:180px;">
                                <div class="form-label col-md-1">
                                    <label for="">
                                        <@s.property value="#additional.member.realName"/>：
                                    </label>
                                </div>
                                <div class="form-input col-md-11">
                                    <div class="append-left">
                                       <span class="form-label"><label for=""><@s.property value="#additional.content"/> </label></span>
                                    </div>
                                </div>
                            </div>
                             </@s.iterator>
                            <li class="divider"></li>
                        </@s.iterator>
                        <div class="form-row">
                          <#--  <div style="float: left;padding-right: 20px;">
                                <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "  title="查看回答" target="after:closest('.ajax-load-div')">
                                         <span class="button-content">
                                              <i class="glyph-icon icon-reply"></i>
                                            查看回答
                                        </span>
                                </a>
                            </div>-->
                            <div style="float: left;">
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
    </div>
</div>