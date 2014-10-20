<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@s.set var="vId" value="#parameters['vId'][0]" />
<script type="text/javascript">
    $(function(){

        var vId="<@s.property value='#vId'/>";
        $("#saveForm").submit(function(){
            //添加版本临时属性
            if(vId!=""){
                var flag=true;
                for(var i=0;i<classAttrs.length;i++){
                    if($("#code").val()==classAttrs[i]){
                        flag=false;
                        break;
                    }
                }
                if(!flag){
                    top.$.msgbox({
                        msg: "属性名称重复!",
                        icon: "warning"
                    });
                    return false;
                }

            }

        });

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
    <@s.form id="saveForm" namespace="/attr" action="save" method="post" cssClass="center-margin">
     <@s.if test="@com.fantasy.framework.util.common.StringUtil@isNull(#vId)">
         <@s.hidden name="notTemporary" value="true" />
     </@s.if>
     <@s.else>
         <@s.hidden name="notTemporary" value="false" />
         <input type="hidden" name="vId" value="<@s.property value='#vId'/>"/>
     </@s.else>
    <div class="tabs">
        <ul >
            <li>
                <a title="属性" href="#normal-tabs-1">
                    属性
                </a>
            </li>
        </ul>
        <a href="javascript:;" class="btn small hover-black float-right back-page" title="" style="margin-top: -30px;margin-right: 30px">
            <i class="glyph-icon icon-reply"></i>
        </a>
        <div id="normal-tabs-1">
                <div class="row issue-date">
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    编码：
                                </label>
                            </div>
                            <div class="form-input col-md-8 col-md-offset-1">
                                <div class="append-left">
                                    <@s.textfield  id="code" name="code" />
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    属性类型：
                                </label>
                            </div>
                            <div class="form-input col-md-8 col-md-offset-1">
                                <div class="append-left">
                                    <@s.select cssClass="chosen-select" list="@com.fantasy.attr.service.AttributeTypeService@allAttributeType()" name="attributeType.id" listKey="id" listValue="name" />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    名称：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                    <@s.textfield  id="name" name="name" />
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    非空：
                                </label>
                            </div>
                            <div class="form-checkbox-radio col-md-8 col-md-offset-1">
                                <div class="append-right">
                                    <@s.radio name="nonNull" value="false" list=r"#{true:'是',false:'否'}" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-label  col-md-2">
                        <label class="label-description" for="">
                            描述:
                        </label>
                    </div>
                    <div class="form-input col-md-10">
                        <@s.textarea name="description" id="description"/>
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