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

        $(".ask").click(function(){
            var id = this.id;
            if($("#"+id).html()=="追问"){
                $("#con"+id).show();
                $("#btn"+id).show();
                $("#"+id).html("收起")
            }else{
                $("#con"+id).hide();
                $("#btn"+id).hide();
                $("#"+id).html("追问")
            }

        })
        $(".ask1").click(function(){
            var id = this.id;
            if($("#"+id).html()=="追答"){
                $("#addcon"+id).show();
                $("#addbtn"+id).show();
                $("#"+id).html("收起");
            }
            else{
                $("#addcon"+id).hide();
                $("#addbtn"+id).hide();
                $("#"+id).html("追答");
            }
        })
        $(".ask2").click(function(){
            var id = this.id;
            if($("#"+id).html()=="追问"){
                $("#zwcon"+id).show();
                $("#zwbtn"+id).show();
                $("#"+id).html("收起");
            }
            else{
                $("#zwcon"+id).hide();
                $("#zwbtn"+id).hide();
                $("#"+id).html("追问");
            }
        })
        $("#pl").click(function(){
          if($("#pl").html()=="点击此处评论"){
                $("#mem").show();
                $("#con").show();
                $("#savebtn").show();
                $("#pl").html("点击此处收起")
            }else{
              $("#mem").hide();
              $("#con").hide();
              $("#savebtn").hide();
              $("#pl").html("点击此处评论")
          }
        });


        $(".fabu").click(function(){
            var id = this.id.replace("save","");
            var content = $("#t"+id).val();
            $.post('${request.contextPath}/answeradditional/save.do',{id:id,content:content},function(){
                $page$.backpage();
                $.msgbox({
                    msg : "保存成功",
                    type : "success",
                });
            });
        })


        $(".addfabu").click(function(){
                var id = this.id.replace("addsave","");
                var content = $("#addt"+id).val();
                $.post('${request.contextPath}/answeradditional/zhuida.do',{id:id,content:content},function(){
                    $page$.backpage();
                    $.msgbox({
                        msg : "保存成功",
                        type : "success",
                    });
                });
        });


        $(".zwfabu").click(function(){
            var id = this.id.replace("zwsave","");
            var content = $("#zwt"+id).val();
            $.post('${request.contextPath}/answeradditional/save.do',{id:id,content:content},function(){
                $page$.backpage();
                $.msgbox({
                    msg : "保存成功",
                    type : "success",
                });
            });
        });
    });
</script>
<div class="example-box" style="padding-left:10px;padding-right:10px;">
    <div class="example-code">
        <div class="content-box box-toggle">
            <h3 class="content-box-header primary-bg">
                <span class="float-left">问题详情</span>
                <a href="javascript:;" class=" float-right back-page" style="margin-right: 30px;color: white;"><<<点击此处返回问题列表</a>
             <#--   <a href="javascript:;" class="btn small hover-black float-right back-page" title="返回" >
                <i class="glyph-icon icon-reply"></i>-->
            </a>

            </h3>
            <div class="content-box-wrapper">
            <@s.form id="saveForm" namespace="/answer" action="save" method="post" cssClass="center-margin">
                <@s.hidden  name="question.id" value="%{question.id}"/>
                <div class="row">
                    <div class="col-md-12">
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
                                        悬赏金额：
                                    </label>
                                </div>
                                <div class="form-input col-md-11">
                                    <div class="append-left">
                                        <@s.textfield name="offerMoney"  value="%{question.offerMoney}" disabled="true"/>
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
                                        <@s.radio list=r"#{true:'是',false:'否'}"  name="issue"  value="%{question.issue}" disabled="true"/>
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
                        <@s.if test="memberList.size()>0"> <div> <a href="javascript:void(0);" style="padding-right: 20px;color: blue;" id="pl">点击此处评论</a></div> </@s.if>
                            <@s.if test="memberList.size()>0">
                                <div class="form-row" id="mem" style="display: none;">
                                    <div class="form-label col-md-1">
                                        <label for="">
                                            回答人：
                                        </label>
                                    </div>
                                    <div class="form-input col-md-11">
                                        <div class="append-left">
                                            <@s.select name="member.id"  list="%{memberList}" listKey="id" listValue="username" cssClass="chosen-select view-field" data_placeholder="请选择回答人"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row" id="con" style="display: none;">
                                    <div class="form-label col-md-1">
                                        <label for="">
                                            回答内容：
                                        </label>
                                    </div>
                                    <div class="form-input col-md-11">
                                        <div class="append-left">
                                            <@s.textarea  name="content" cssStyle="height:160px;" />
                                        </div>
                                    </div>
                                </div>
                                <@s.if test="memberList.size()>0">
                                <div class="form-row" id="savebtn" style="display: none;padding-left: 150px">
                                    <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 menu-save4"  onclick="$('#saveForm').submit();return false;" title="保存">
                                             <span class="button-content">
                                                     <i class="glyph-icon icon-save float-left"></i>
                                                 保存
                                              </span>
                                    </a>
                                </div>

                                    <div style="float: left;; display: none;" id="savebtn">

                                    </div>
                                </@s.if>
                            </@s.if>



                            <@s.if test="question.answers.size()>0"><li class="divider"></li></@s.if>
                            <@s.iterator value="question.answers" var="answer" status="st">
                                <div class="form-row">
                                    <div class="form-label col-md-1">
                                        <label for="">
                                            <@s.property value="#answer.member.username"/>：
                                        </label>
                                    </div>
                                    <div class="form-input col-md-11">
                                        <div class="append-left">
                                            <span class="form-label"> <label for=""><@s.property value="#answer.content"/> </label></span>
                                            <div style="float: right;">
                                                <@s.if test="#answer.additionals.size()==0">
                                                <a href="javascript:void(0);" style="padding-right: 20px;color: blue;" id="<@s.property value="#answer.id"/>" class="ask">追问</a>
                                                </@s.if>
                                                <div style="width: 100px;float: right;">
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
                                </div>
                                <div class="form-row" id="con<@s.property value="#answer.id"/>" style="display: none;">
                                    <div class="form-input col-md-11">
                                        <div class="append-left">
                                            <textarea name="text" cssStyle="height:35px;" id="t<@s.property value="#answer.id"/>"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row" id="btn<@s.property value="#answer.id"/>" style="display: none;padding-left: 40px;">
                                    <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 menu-save4 fabu" id="save<@s.property value="#answer.id"/>">
                                             <span class="button-content">
                                                     <i class="glyph-icon icon-save float-left"></i>
                                                 发布
                                              </span>
                                    </a>
                                </div>
                                <@s.iterator value="#answer.additionals" var="additional"  status="sta">
                                    <div class="form-row" style="padding-left:140px;">
                                        <div class="form-label col-md-1">
                                            <label for="" style="color: #006400;">
                                               <@s.if test="#sta.isOdd()">追问：</@s.if><@s.else>追答：</@s.else>
                                            </label>
                                        </div>
                                        <div class="form-input col-md-11">
                                            <div class="append-left" >
                                                <span class="form-label">
                                                    <label for="" style="color: #006400"><@s.property value="#additional.content"/>
                                                        <@s.if test="#sta.isLast()">
                                                            <@s.if test="#additional.member.id==question.member.id"><a href="javascript:void(0);" style="padding-left: 50px;color: blue;" id="<@s.property value="#answer.id"/>" class="ask1">追答</a></@s.if>
                                                            <@s.else>
                                                                <a href="javascript:void(0);" style="padding-left: 50px;color: blue;" id="<@s.property value="#answer.id"/>" class="ask2">追问</a>
                                                            </@s.else>
                                                        </@s.if>
                                                     </label>
                                                </span>

                                            </div>
                                        </div>
                                    </div>
                                </@s.iterator>
                                <div class="form-row" id="addcon<@s.property value="#answer.id"/>" style="display: none;">
                                    <div class="form-input col-md-11">
                                        <div class="append-left">
                                            <textarea name="text" cssStyle="height:35px;" id="addt<@s.property value="#answer.id"/>"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row" id="addbtn<@s.property value="#answer.id"/>" style="display: none;padding-left: 40px;">
                                    <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 menu-save4 addfabu" id="addsave<@s.property value="#answer.id"/>">
                                             <span class="button-content">
                                                     <i class="glyph-icon icon-save float-left"></i>
                                                 发布
                                              </span>
                                    </a>
                                </div>
                                <div class="form-row" id="zwcon<@s.property value="#answer.id"/>" style="display: none;">
                                    <div class="form-input col-md-11">
                                        <div class="append-left">
                                            <textarea name="text" cssStyle="height:35px;" id="zwt<@s.property value="#answer.id"/>"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row" id="zwbtn<@s.property value="#answer.id"/>" style="display: none;padding-left: 40px;">
                                    <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 menu-save4 zwfabu" id="zwsave<@s.property value="#answer.id"/>">
                                             <span class="button-content">
                                                     <i class="glyph-icon icon-save float-left"></i>
                                                 发布
                                              </span>
                                    </a>
                                </div>
                                <li class="divider"></li>
                            </@s.iterator>
                    </div>
                </div>
            </@s.form>
            </div>
        </div>
    </div>
</div>
