<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function () {
        var notExitList = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(notExitList)" escapeHtml="false"/>;
        if(notExitList==null||notExitList==''){
            $('#rl_2').attr("disabled","disabled");
        }
        var _selectItem = $('#selectItem').select3({value:'key',text:function(data){return data['name']+'('+data['key']+')';}},function(data){
            if(!data)return;
            $('#name').hide().find('input').val(data.name);
            switch (data.key){
                case 'cms':
                    $('#defvalue,#goodsvalue').hide().disabled(true);
                    $('#cmsvalue').show().disabled(false);
                    break;
                case 'good':
                    $('#defvalue,#cmsvalue').hide().disabled(true);
                    $('#goodsvalue').show().disabled(false);
                    break;
                default:
                    $('#defvalue').show().disabled(false);
                    $('#goodsvalue,#cmsvalue').hide().disabled(true);
            }
        });
        $(".custom-radio[name='rl$tt']").change(function(){
            if($(this).val()=='1'){
                $('#key').hide().disabled(true);
                $('#code').show().disabled(false).find('input').val('');
                $('#name').show().disabled(false).find('input').val('');
                $('#defvalue').show().disabled(false).next().hide().disabled(true);
                $('#goodsvalue').hide().disabled(true);
            }else{
                _selectItem.load(notExitList);
                $('#key').show().disabled(false);
                $('#code').hide().disabled(true);
            }
        }).filter(':checked').change();

        $("#saveForm").ajaxForm({
            success: function (data) {
                $('#pager').pager().reload();
                top.$.msgbox({
                    msg: " <@s.text name="system.setting.save.success"/>",
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
                <span class="float-left"> <@s.text name="system.setting.add.title"/></span>
                <a href="javascript:void(0)" class="float-right icon-separator btn toggle-button" title="Toggle Box">
                    <i class="glyph-icon icon-toggle icon-chevron-up"></i>
                </a>
            </h3>
            <div class="content-box-wrapper">
            <@s.form id="saveForm" namespace="/system/website/setting" action="save" method="post" cssClass="center-margin">
                <@s.hidden name="website.id" value="%{#parameters['websiteId']}"/>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="system.setting.add.select"/>：
                                </label>
                            </div>
                            <div class="form-checkbox-radio col-md-9">
                                <div class="append-left">
                                    <input id="rl_1" class="custom-radio" checked="checked" name="rl$tt" type="radio" value="1"/>
                                    <label for="rl_1"> <@s.text name="system.setting.add.select.default"/></label>
                                    <input id="rl_2" class="custom-radio" name="rl$tt" type="radio" value="2" />
                                    <label for="rl_2"> <@s.text name="system.setting.add.select.set"/></label>
                                </div>
                            </div>
                        </div>
                        <div class="form-row" id="code">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="system.setting.add.key"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textfield name="key"/><@s.fielderror fieldName="key"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row" id="key" style="display: none;">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="system.setting.add.key"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <select name="key" id="selectItem" class="chosen-select" disabled="true"></select>
                                </div>
                            </div>
                        </div>
                        <div class="form-row" id="defvalue">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="system.setting.add.value"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textfield name="value" cssStyle="width: 885px;"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row" id="cmsvalue">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="system.setting.add.value"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.select list="@com.fantasy.cms.service.CmsService@articleCategoryList()"  cssClass="chosen-select" name="value" listKey="code" listValue="name"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row" id="goodsvalue">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="system.setting.add.value"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.select list="@com.fantasy.mall.goods.service.GoodsService@goodsCategoryList()"  cssClass="chosen-select" name="value" listKey="sign" listValue="name"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="system.setting.description"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textarea name="description" cssStyle="width: 885px;height: 150px;" />
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div style="float: left;padding-right: 50px;">
                                <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 menu-save4"  onclick="$('#saveForm').submit();return false;" title="<@s.text name="system.setting.button.save"/>"  >
                                         <span class="button-content">
                                                 <i class="glyph-icon icon-save float-left"></i>
                                             <@s.text name="system.setting.button.save"/>
                                          </span>
                                </a>
                            </div>
                            <div style="float: left;">
                                <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "  title="<@s.text name="system.setting.button.return"/>" >
                                         <span class="button-content">
                                              <i class="glyph-icon icon-reply"></i>
                                             <@s.text name="system.setting.button.return"/>
                                        </span>


                                </a>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                </div>
                            </div>
                        </div>
                        <div class="form-row" id="name">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="system.setting.add.name"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                    <@s.textfield name="name"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
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
