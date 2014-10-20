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

        $('[name="EQS_sn"]').autocomplete(request.getContextPath() + '/admin/dms/order/order_list_search.do', {
            minChars: 0,
            width: 199,
            max: 0,
            resultsClass: 'fuzzy_search',
            extraParams:{
                'LIKES_sn':function(){
                    return $('[name="EQS_sn"]').val();
                }
            },
            formatItem: function (row) {
                return row.sn;
            },
            formatMatch: function (row) {
                return row.sn;
            },
            formatResult: function (row) {
                return row.name;
            }, parse: function (data) {
                return $.map(data.pageItems, function (row) {
                    return {data: row, value: row.sn, result: row.sn, id: row.id}
                });
            }
        }).result(function (event, data, formatted) {
            /*
            $.each(data,function(key,value){
                list.form.find('.' + key).each(function(){
                    if($(this).is('input')){
                        $(this).val(value);
                    }else{
                        $(this).html(value);
                    }
                });
            });
            $('.productQuantity').val('1');
            */
        });
    });
</script>
<div class="pad10L pad10R">
    <div class="example-box">
    <@s.form id="saveForm" namespace="/attr" action="type_save" method="post" cssClass="center-margin">
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
                <div class="row issue-date">
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    名称：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-left">
                                    <@s.textfield  id="name" name="name" />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    类型：
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div class="append-right">
                                    <@s.textfield id="dataType" name="dataType" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-label  col-md-2">
                        <label class="label-description" for="">
                            类型对应的转换器:
                        </label>
                    </div>
                    <div class="form-checkbox-radio col-md-10">
                        <@s.select  name="converter.id"  list="@com.fantasy.attr.service.ConverterService@findAll()"  cssStyle="width:260px;" listKey="id" listValue="name"/>
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