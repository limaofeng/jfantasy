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
                $('#pager').pager().reload();
                $.msgbox({
                    msg : "保存成功",
                    type : "success"
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
                <span class="float-left">属性添加</span>
                <a href="javascript:void(0)" class="float-right icon-separator btn toggle-button" title="Toggle Box">
                    <i class="glyph-icon icon-toggle icon-chevron-up"></i>
                </a>
            </h3>
        <div class="content-box-wrapper">
        <@s.form id="saveForm" namespace="/attr" action="save" method="post" cssClass="center-margin">
            <@s.if test="@com.fantasy.framework.util.common.StringUtil@isNull(#vId)">
                <@s.hidden name="notTemporary" value="true" />
            </@s.if>
            <@s.else>
                <@s.hidden name="notTemporary" value="false" />
                <input type="hidden" name="vId" value="<@s.property value='#vId'/>"/>
            </@s.else>
            <div class="row">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    编码：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
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
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.select cssClass="chosen-select" list="@com.fantasy.attr.service.AttributeTypeService@allAttributeType()" name="attributeType.id" listKey="id" listValue="name" data_placeholder="请选择属性类型"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    描述：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textarea name="description" cssStyle="height: 150px;width:888px;"/>
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
                            <div class="form-checkbox-radio col-md-9">
                                <div class="append-right">
                                    <@s.radio name="nonNull" value="false" list=r"#{true:'是',false:'否'}" />
                                </div>
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