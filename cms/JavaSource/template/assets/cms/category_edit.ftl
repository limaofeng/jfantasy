<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function () {
        var list = $('#attrParameter').list($('#attrParameterForm'));
        list.view.setJSON(<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(category.articleVersion.attributes)" escapeHtml="false" default="[]"/>);
        $(".custom-radio[name='rl$tt']").change(function(){
            if($(this).val()=='1'){
                $('#class1').show();
            }else{
                $('#class1,#tabs-2').hide();
            }
        }).filter(':checked').change();

        $('#class1').click(function(){
            $('#tabs-2').show();
        });

        $("#saveForm").ajaxForm({
            beforeSerialize : function(zhis, options){
                var _data = {};
                list.getData().each(function(_i){
                    _data['articleVersion.attributes['+_i+'].id'] = this.id;
                    _data['articleVersion.attributes['+_i+'].code'] = this.code;
                    _data['articleVersion.attributes['+_i+'].name'] = this.name;
                    _data['articleVersion.attributes['+_i+'].attributeType.id'] = this["attributeType.id"];
                    _data['articleVersion.attributes['+_i+'].nonNull'] = this.nonNull;

                });
                options.data= _data;

            },
            success:function (data) {
                data.isParent = true;
                var node = categoryTree.getNodeByParam("code", data.code);
                categoryTree.updateNode(Fantasy.copy(node, categoryFilter(data)), false);
                $page$.backpage();
                $('#' + node.tId + '_span').click();
                $.msgbox({
                    msg: "保存成功",
                    type: "success"
                });
            }
        });
    });
</script>


<div class="example-box" style="padding-left:10px;padding-right:10px;">
    <div class="example-code">
        <div class="content-box box-toggle button-toggle">
            <h3 class="content-box-header primary-bg">
                <span class="float-left">文章分类编辑</span>
                <a href="javascript:void(0)" class="float-right icon-separator btn toggle-button" title="Toggle Box">
                    <i class="glyph-icon icon-toggle icon-chevron-up"></i>
                </a>
            </h3>
            <div class="content-box-wrapper">
            <@s.form id="saveForm" namespace="/cms/article" action="category_save" method="post" cssClass="center-margin">
                <@s.hidden name="parent.code" value="%{category.parent.code}"/>
                <@s.hidden name="code" value="%{category.code}"/>
                <@s.hidden name="articleVersion.id" value="%{category.articleVersion.id}"/>
            <div class="tabs">
                <ul>
                    <li>
                        <a title="基本信息" href="#tabs-1">
                            基本信息
                        </a>
                    </li>
                    <li id="class1">
                        <a title="动态属性字段添加" href="#tabs-2">
                            动态属性字段添加
                        </a>
                    </li>
                 </ul>
                <div id="tabs-1">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-row">
                                <div class="form-label col-md-2">
                                    <label for="">
                                        <@s.text name="category.code"/>：
                                    </label>
                                </div>
                                <div class="form-input col-md-10">
                                    <div class="append-left">
                                        <@s.textfield name="code" disabled="true"  value="%{category.code}"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-row">
                                <div class="form-label col-md-2">
                                    <label for="">
                                        <@s.text name="category.name"/>：
                                    </label>
                                </div>
                                <div class="form-input col-md-10">
                                    <div class="append-left">
                                        <@s.textfield name="name" value="%{category.name}"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-row">
                                <div class="form-label col-md-2">
                                    <label for="">
                                        <@s.text name="category.egname"/>:
                                    </label>
                                </div>
                                <div class="form-input col-md-10">
                                    <div class="append-left">
                                        <@s.textfield name="egname"  value="%{category.egname}"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-row">
                                <div class="form-label col-md-2">
                                    <label for="">
                                        是否启用文章数据版本：
                                    </label>
                                </div>
                                <div class="form-checkbox-radio col-md-10">
                                    <div class="append-left">
                                        <input id="rl_1" class="custom-radio" checked="checked" name="rl$tt" type="radio" value="1"/>
                                        <label for="rl_1">是</label>
                                        <input id="rl_2" class="custom-radio" name="rl$tt" type="radio" value="2" />
                                        <label for="rl_2">否</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <#--<div class="col-md-6" id="class1">
                            <div class="form-row">
                                <div class="form-label col-md-2">
                                    <label for="">
                                        版本对应的类：
                                    </label>
                                </div>
                                <div class="form-input col-md-10">
                                    <div class="append-left">
                                        <@s.textfield name="articleVersion.className"  value="com.fantasy.cms.bean.Article" readonly="true"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6" id="class2">
                            <div class="form-row">
                                <div class="form-label col-md-2">
                                    <label for="">
                                    </label>
                                </div>
                                <div class="form-input col-md-10">
                                    <div class="append-left">
                                    </div>
                                </div>
                            </div>
                        </div>-->
                        <div class="col-md-6">
                            <div class="form-row">
                                <div class="form-label col-md-2">
                                    <label for="">
                                        <@s.text name="category.description"/>:
                                    </label>
                                </div>
                                <div class="form-input col-md-10">
                                    <div class="append-left">
                                        <@s.textarea name="description" cssStyle="width:900px;height: 160px;"  value="%{category.description}"/>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>

                <div id="tabs-2">
                    <#include "attrParameter.ftl">
                </div>
            </div>
                <div class="col-md-6">
                    <div class="form-row">
                        <div class="form-label col-md-2">
                            <label for="">
                            </label>
                        </div>
                        <div class="form-input col-md-10">
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
            </@s.form>

            </div>

        </div>
    </div>
</div>