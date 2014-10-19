<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@s.set var="attributes" value="@com.fantasy.framework.util.common.ObjectUtil@sort(version.attributes,version.attributeSorts,'id')" />
<#-- class 原有属性及版本属性 -->
<@s.set var="classAttrs" value="@com.fantasy.attr.service.AttributeVersionService@classAllAttrs(version.id,version.className)" />
<script type="text/javascript">

    $(function(){
        //class 原有属性及版本属性
        window.classAttrs=<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(#classAttrs)" default="[]" escapeHtml="false"/>;

        //版本属性
        var attrs=<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(#attributes)" default="[]" escapeHtml="false"/>;
        //添加数据
        var $versionAtrr = $("#view").view().on("add",function(data) {
            this.target.find('.delete').click(function (e) {
                if(confirm("确定要删除版本属性吗？")) {
                    $.post($(this).attr("href"), function (data) {
                        top.$.msgbox({
                            msg:"移除成功!",
                            icon:"success"
                        });
                    });
                }
                return stopDefault(e);
            });
            //上调
            $upOrDown = this.target.find('.up').click(function (e) {
                if(confirm("确定要上调版本属性吗？")) {
                    var attrIds="";
                    $(".attr-checkbox").each(function(x){
                        if(x==0){
                            attrIds=$(this).val();
                        }else{
                            attrIds= attrIds+","+$(this).val();
                        }
                    });
                    $.post($(this).attr("href"),{id:<@s.property value="version.id"/>,attrIds:attrIds}, function (data) {
                        top.$.msgbox({
                            msg:"上调成功!",
                            icon:"success"
                        });
                    });
                }
                return stopDefault(e);
            });
            //下调
            this.target.find('.down').click(function(){
                if(confirm("确定要下调版本属性吗？")) {
                    var attrIds="";
                    $(".attr-checkbox").each(function(x){
                        if(x==0){
                            attrIds=$(this).val();
                        }else{
                            attrIds= attrIds+","+$(this).val();
                        }
                    });
                    $.post($(this).attr("href"),{id:<@s.property value="version.id"/>,attrIds:attrIds}, function (data) {
                        top.$.msgbox({
                            msg:"下调成功!",
                            icon:"success"
                        });
                    });
                }
                return stopDefault(e);
            });


        }).setJSON(attrs);
    });
</script>
<div id="searchFormPanel" class="button-panel pad5A">
    <#-- 版本号 -->
    <div class="row">
        <div class="col-md-6">
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        版本号：
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-left">
                    <@s.property value="version.number" />
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        类名：
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-right">
                    <@s.property  value="version.className" />
                    </div>
                </div>
            </div>
        </div>
    </div>

    <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "  title="返回" >
        <span class="glyph-icon icon-separator">
              <i class="glyph-icon icon-reply"></i>
        </span>
         <span class="button-content">
                返回
         </span>
    </a>
    <a title="添加非临时属性" class="btn medium primary-bg dd-add" href="<@s.url namespace="/attr" action="version_attrAdd?vId=%{version.id}&EQB_notTemporary=true"/>" target="after:closest('.ajax-load-div')">
        <span class="button-content">
            <i class="glyph-icon icon-plus float:left"></i>
            添加非临时属性
        </span>
    </a>
    <a title="添加临时属性" class="btn medium primary-bg dd-add" href="<@s.url namespace="/attr" action="add?vId=%{version.id}"/>" target="after:closest('.ajax-load-div')">
        <span class="button-content">
            <i class="glyph-icon icon-plus float:left"></i>
            添加临时属性
        </span>
    </a>
    <div class="propertyFilter"> </div>
        <#--
        <div class="form-search">
            <input type="text" name="LIKES_title" title="" data-placement="bottom" class="input tooltip-button ac_input" placeholder="Search..." autocomplete="off" style="display: inline-block; width: 200px;">
            <i class="glyph-icon icon-search"></i>
        </div>
        -->
</div>

<div class="batch"></div>
<div class="grid-panel">
    <table id="view" class="table table-hover mrg5B text-center">
        <thead>
        <tr>
            <th class="pad15L" style="width:20px;">
                <input id="allChecked" class="custom-checkbox bg-white" checkAll=".id" type="checkbox" <#--checktip="{message:'您选中了{num}条记录',tip:'#config_check_info'}"--> />
            </th>
            <th>编码</th>
            <th>名称</th>
            <th>类型</th>
            <th>非空</th>
            <th>是否非临时</th>
            <th class="text-center">操作</th>
        </tr>
        </thead>
        <tbody>
        <tr class="template" name="default">
            <td><input class="id attr-checkbox" type="checkbox" value="{id}"/></td>
            <td class="font-bold">{code} </td>
            <td>{name}</td>
            <td>{attributeType.name}</td>
            <td>{nonNull:dict({'true':'是','false':'否'})}</td>
            <td>{notTemporary:dict({'true':'是','false':'否'})}</td>
            <td class="pad0T pad0B text-center">
                <div class="dropdown actions">
                    <a href="javascript:;" title="" class="btn medium bg-blue" data-toggle="dropdown">
                        <span class="button-content">
                            <i class="glyph-icon font-size-11 icon-cog"></i>
                            <i class="glyph-icon font-size-11 icon-chevron-down"></i>
                        </span>
                    </a>
                    <ul class="dropdown-menu float-right">
                        <li>
                            <a title="<@s.text name= '详情' />" class="view" href="<@s.url namespace="/attr" action="view?id={id}"/>" >
                                <i class="glyph-icon icon-external-link-sign mrg5R"></i>
                                详情
                            </a>
                        </li>
                        <li>
                            <a title="<@s.text name= '上调' />" class="up" href="<@s.url namespace="/attr" action="version_upOrDown"/>" >
                                <i class="glyph-icon icon-external-link-sign mrg5R"></i>
                                上调
                            </a>
                        </li>
                        <li>
                            <a title="<@s.text name= '下调' />" class="down" href="<@s.url namespace="/attr" action="version_upOrDown"/>" target="after:closest('.ajax-load-div')" >
                                <i class="glyph-icon icon-external-link-sign mrg5R"></i>
                                下调
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a title="<@s.text name= '移除属性' />" href="<@s.url namespace="/attr" action="version_attrDel?vid=%{version.id}&id={id}"/>" class="font-red delete">
                                <i class="glyph-icon icon-remove mrg5R"></i>
                                移除属性
                            </a>
                        </li>
                    </ul>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>