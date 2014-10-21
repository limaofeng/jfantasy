<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="pageTitle">
    <@s.text name="mall.brand.title"/>
<small>
    <@s.text name="mall.brand.description"/>
</small>
</@override>
<@override name="head">
<script type="text/javascript">
    $(function(){
        //当浏览器窗口发生变化时,自动调整布局的js代码
        var _$gridPanel = $('.grid-panel'),_resize = function () {
            _$gridPanel.css('minHeight', $(window).height() - (_$gridPanel.offset().top + 15));
            _$gridPanel.triggerHandler('resize');
        };
        $(window).resize(_resize);
        $page$.one('destroy',function(){
            $(window).unbind('resize',_resize);
        });
        var $advsearch = $('.propertyFilter').advsearch({
            filters : [{
                name : 'S_engnamee',
                text : '英文名称',
                type : 'input',
                matchType :['EQ','LIKE']
            },{
                name : 'S_name',
                text : '品牌名称',
                type : 'input',
                matchType :['EQ','LIKE']
            }]
        });
        /**
         ,{
                name : 'S_name',
                text : '测试select',
                type : 'select',
                matchType :['EQ','LIKE'],
                values: [
                    {text: '选项1', value: '1'},
                    {text: '选项2', value: '2'},
                    {text: '选项3', value: '3'}
                ]
            }]
         }
         */
        //列表初始化
        var pager=<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(pager)" escapeHtml="false"/>;
        var $grid = $('#view').dataGrid($('#searchFormPanel'),$('.batch'));
        $grid.data('grid').view().on('add',function(data){
            this.target.find('.delete').click(function(e){
                deleteMethod([data.id]);
                return stopDefault(e);
            });
        });
        $grid.setJSON(pager);
        var deleteMethod = $('.batchDelete').batchExecute($("#allChecked"),$grid.data('grid').pager(),'id','<@s.text name="mall.brand.delete.alert"><@s.param name="0" value="'{name}'"/></@s.text>',function(){
            $.msgbox({
                msg : "<@s.text name="mall.brand.delete.success"/>!",
                type : "success"
            });
        });
    });
</script>
</@override>
<@override name="pageContent">
<div id="searchFormPanel" class="button-panel pad5A">
    <@s.form id="searchForm" namespace="/mall/goods/brand" action="search" method="post">
        <a title="<@s.text name="mall.brand.button.add"/>" class="btn medium primary-bg dd-add" href="<@s.url namespace="/mall/goods/brand" action="add"/>" target="after:closest('#page-content')">
            <span class="button-content">
                <i class="glyph-icon icon-plus float-left"></i>
                <@s.text name="mall.brand.button.add"/>
            </span>
        </a>
        <div class="propertyFilter">
        </div>
        <div class="form-search">
            <input type="text" name="LIKES_name_OR_engname" title="" data-placement="bottom" class="input tooltip-button ac_input" placeholder="Search..." autocomplete="off" style="display: inline-block; width: 200px;">
            <i class="glyph-icon icon-search"></i>
        </div>
    </@s.form>
</div>
<div class="batch">
    <a title="<@s.text name="mall.brand.button.batchDelete"/>" class="btn small primary-bg batchDelete" href="<@s.url namespace="/mall/goods/brand" action="delete"/>">
        <span class="button-content">
            <i class="glyph-icon icon-trash float-left"></i>
            <@s.text name="mall.brand.button.batchDelete"/>
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
            <th class="sort" orderBy="name"><@s.text name="mall.brand.list.name"/></th>
            <th class="sort" orderBy="engname"><@s.text name="mall.brand.list.engname"/></th>
            <th><@s.text name="mall.brand.list.url"/></th>
            <th class="text-center"><@s.text name="mall.brand.list.actions"/></th>
        </tr>
        </thead>
        <tbody>
        <tr class="template" name="default">
            <td><input class="id custom-checkbox" type="checkbox" value="{id}"/></td>
            <td class="font-bold">{name} </td>
            <td>{engname}</td>
            <td>{url}</td>
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
                            <a href="<@s.url namespace="/mall/goods/brand" action="view?id={id}"/>" class="view" title="<@s.text name="mall.brand.list.actions.details"/>" target="after:closest('#page-content')">
                                <i class="glyph-icon icon-external-link-sign mrg5R"></i>
                                <@s.text name="mall.brand.list.actions.details"/>
                            </a>
                        </li>
                        <li>
                            <a title="<@s.text name="mall.brand.list.actions.edit"/>" class="edit" href="<@s.url namespace="/mall/goods/brand" action="edit?id={id}"/>" target="after:closest('#page-content')">
                                <i class="glyph-icon icon-edit mrg5R"></i>
                                <@s.text name="mall.brand.list.actions.edit"/>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="<@s.url namespace="/mall/goods/brand" action="delete?id={id}"/>" class="font-red delete" title="<@s.text name="mall.brand.list.actions.delete"/>">
                                <i class="glyph-icon icon-remove mrg5R"></i>
                                <@s.text name="mall.brand.list.actions.delete"/>
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
<@extends name="../../wrapper.ftl"/>