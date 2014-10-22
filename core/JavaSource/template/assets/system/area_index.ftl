<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="pageTitle">
    <@s.text name="common.area.title"/>
<small>
    <@s.text name="common.area.description"/>
</small>
</@override>
<@override name="head">
<script type="text/javascript">
    $(function(){
        //当浏览器窗口发生变化时,自动调整布局的js代码
        var _$gridPanel = $('.grid-panel');
        var _resize = function () {
            _$gridPanel.css('minHeight', $(window).height() - (_$gridPanel.offset().top + 15));
            _$gridPanel.triggerHandler('resize');
        };
        $(window).resize(_resize);
        $page$.one('destroy',function(){
            $(window).unbind('resize',_resize);
        });
        var $advsearch = $('.propertyFilter').advsearch({
            filters : [{
                name : 'S_name',
                text : '名称',
                type : 'input',
                matchType :['EQ','LIKE','GT']
            }]
        });
        //列表初始化
        var pager=<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(pager)" escapeHtml="false"/>;
        var $grid = $('#view').dataGrid($('#searchFormPanel'),$('.batch'));
        $grid.data('grid').view().on('add',function(data){
            this.target.find('.delete').click(function(e){
                deleteMethod([data.id]);
                return stopDefault(e);
            });
            this.target.find(".font-bold").click(function(){
                //复制元素
                $("#modelFolder").show();
                var e=$("#modelFolder").clone(true);
                $("#modelFolder").hide();
                e.removeAttr("id");
                e.find(".button-content").text(data.name);
                e.click(function(){
                    if(a_index==($("#areaFolder a").size()-1)){return;}
                    $("#parentAreaId").attr("name","EQS_parent.id").val(data.id);
                    $("#searchForm").submit();
                    $("#areaFolder a:gt("+a_index+")").remove();
                });
                $("#areaFolder").append(e);
                var a_index=$("#areaFolder a").size()-1;
                $("#parentAreaId").attr("name","EQS_parent.id").val(data.id);
                $("#searchForm").submit();
            });
        });
        $grid.setJSON(pager);
        var deleteMethod = $('.batchDelete').batchExecute($("#allChecked"),$grid.data('grid').pager(),'id','是否确认删除[{username}]地区？',function(){
            $.msgbox({
                msg : "<@s.text name="common.area.delete.success"/>",
                type : "success"
            });
        });

        $("#rootFolder").click(function(){
            $("#parentAreaId").attr("name","NULL_parent").val("");
            $("#searchForm").submit();
            $("#areaFolder a:not(:first)").remove();
        });
        $(".dd-add").bind('click', function (e) {
            $(".dd-add").attr("href",$(".dd-add").attr("href")+"?parentId="+$("#parentAreaId").val())
        }).target('after:#page-content');
    });
</script>
<style>
    .bg-twitter{margin-right:10px;}
</style>
</@override>
<@override name="pageContent">
<div id="searchFormPanel" class="button-panel pad5A">
    <@s.form id="searchForm" namespace="/system/area" action="search" method="post">
        <input type="hidden" name="NULL_parent" id="parentAreaId"/>
        <a title="<@s.text name="common.area.button.add"/>" class="btn medium primary-bg dd-add" href="<@s.url namespace="/system/area" action="add"/>">
            <span class="button-content">
                <i class="glyph-icon icon-plus float:left"></i>
                <@s.text name="common.area.button.add"/>
            </span>
        </a>

        <div class="propertyFilter">
        </div>
        <div class="form-search">
            <input type="text" name="LIKES_name_OR_id" title="" data-placement="bottom" class="input tooltip-button ac_input" placeholder="Search..." autocomplete="off" style="display: inline-block; width: 200px;">
            <i class="glyph-icon icon-search"></i>
        </div>
        <div style="margin-top:30px;" id="areaFolder">
            <a href="javascript:;" class="btn small bg-twitter" id="rootFolder">
                <span class="glyph-icon icon-separator">
                    <i class="glyph-icon icon-home"></i>
                </span>
                <span class="button-content">
                    <@s.text name="common.area.a.text"/>
                </span>
            </a>
        </div>
        <a href="javascript:;" class="btn small bg-twitter" style="display:none;" id="modelFolder">
                <span class="glyph-icon icon-separator">
                    <i class="glyph-icon icon-chevron-right"></i>
                </span>
                <span class="button-content">

                </span>
        </a>
    </@s.form>
</div>
<div class="batch">
    <a title="<@s.text name="common.area.button.batchdelete"/>" class="btn small primary-bg batchDelete" href="<@s.url namespace="/system/area" action="delete"/>">
        <span class="button-content">
            <i class="glyph-icon icon-trash float-left"></i>
            <@s.text name="common.area.button.batchdelete"/>
        </span>
    </a>
</div>
<div class="grid-panel">
    <table id="view" class="table table-hover mrg5B text-center">
        <thead>
        <tr>
            <th class="pad15L" style="width:20px;">
                <input id="allChecked" class="custom-checkbox bg-white" checkAll=".id" type="checkbox" <#--checktip="{message:'您选中了{num}条记录',tip:'#config_check_info'}"--> />
            </th>
            <th class="sort" orderBy="id"><@s.text name="common.area.list.name"/></th>
            <th class="sort" orderBy="displayName"><@s.text name="common.area.list.displayName"/></th>
            <th><@s.text name="common.area.list.path"/></th>
            <th class="sort" orderBy="layer"><@s.text name="common.area.list.layer"/></th>
            <th class="text-center"><@s.text name="common.area.list.actions"/></th>
        </tr>
        </thead>
        <tbody>
        <tr class="template" name="default">
            <td><input class="id custom-checkbox" type="checkbox" value="{id}"/></td>
            <td class="font-bold"  style="cursor:pointer;">{name} </td>
            <td>{displayName}</td>
            <td>{path}</td>
            <td>{layer}</td>
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
                            <a title="<@s.text name="common.area.list.actions.edit"/>" class="edit" href="<@s.url namespace="/system/area" action="edit?id={id}"/>" target="after:closest('#page-content')">
                                <i class="glyph-icon icon-edit mrg5R"></i>
                                <@s.text name="common.area.list.actions.edit"/>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="<@s.url namespace="/system/area" action="delete?id={id}"/>" class="font-red delete" title="<@s.text name="common.area.list.actions.delete"/>">
                                <i class="glyph-icon icon-remove mrg5R"></i>
                                <@s.text name="common.area.list.actions.delete"/>
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
</@override>
<@extends name="../wrapper.ftl"/>