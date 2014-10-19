<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>

<@s.set var="version" value="@com.fantasy.attr.service.AttributeVersionService@version(#parameters['vId'][0])"/>
<script type="text/javascript">
    $(function(){
        //表单  search
        var $advsearch = $('.propertyFilter').advsearch({
            filters : [{
                name : 'S_code',
                text : '编码',
                type : 'input',
                matchType :['EQ','LIKE']
            },{
                name : 'S_name',
                text : '名称',
                type : 'input',
                matchType :['EQ','LIKE']
            }]
        });
        //已经添加的版本属性
        var attrs=<@s.property default="[]" value="@com.fantasy.framework.util.jackson.JSON@serialize(#version.attributes)" escapeHtml="false"/>
        //列表初始化
        var pager=<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(pager)" escapeHtml="false"/>;
        var $grid = $('#view').dataGrid($('#searchFormPanel'),$('.batch'));
        $grid.data('grid').view().on('add',function(data){
           //版本属性添加
            this.target.find('.attr-save').click(function(e){
               var flag=true;
               for(var i=0;i<classAttrs.length;i++){
                    if(data.code==classAttrs[i]){
                        flag=false;
                        break;

                    }
               }
               if(flag) {
                   $.post($(this).attr("href"), function () {
                       $("#searchForm").submit();
                       top.$.msgbox({
                           msg: "添加成功!",
                           icon: "success"
                       });
                   });
               }else{
                   top.$.msgbox({
                       msg: "属性名称重复!",
                       icon: "warning"
                   });
               }
               return stopDefault(e);
           });

        });
        $grid.setJSON(pager);
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
                        <@s.property value="#version.number" />
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
                        <@s.property  value="#version.className" />
                    </div>
                </div>
            </div>
        </div>
     </div>
    <@s.form id="searchForm" namespace="/attr" action="search" method="post">
        <#-- 非临时属性  -->
        <@s.hidden name="EQB_notTemporary" value="true"/>
        <@s.hidden name="vId" value="%{#version.id}"/>
        <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "  title="返回" >
            <span class="glyph-icon icon-separator">
                  <i class="glyph-icon icon-reply"></i>
            </span>
             <span class="button-content">
                    返回
             </span>
        </a>
        <div class="propertyFilter">
        </div>
        <div class="form-search">
            <input type="text" name="LIKES_code" title="" data-placement="bottom" class="input tooltip-button ac_input" placeholder="Search..." autocomplete="off" style="display: inline-block; width: 200px;">
            <i class="glyph-icon icon-search"></i>
        </div>
    </@s.form>
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
            <td><input class="id custom-checkbox" type="checkbox" value="{id}"/></td>
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
                            <a title="<@s.text name= '详情' />" class="view" href="<@s.url namespace="/attr" action="view?id={id}"/>" target="after:closest('.ajax-load-div')" >
                                <i class="glyph-icon icon-external-link-sign mrg5R"></i>
                                详情
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li class="version--attr-add">
                            <a title="版本添加" href="<@s.url namespace="/attr" action="version_attrSave?vid=%{#version.id}&id={id}"/>" class="font-red attr-save">
                                <i class="glyph-icon icon-plus mrg5R"></i>
                                版本添加
                            </a>
                        </li>
                    </ul>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<div class="divider mrg0T"></div>