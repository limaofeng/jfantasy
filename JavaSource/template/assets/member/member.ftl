<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="pageTitle">
    <@s.text name="member.title"/>
<small>
    <@s.text name="member.description"/>
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
        $page$.on('destroy',function(){
            $(window).unbind('resize',_resize);
        });
        var $advsearch = $('.propertyFilter').advsearch({
            filters : [{
                name : 'S_code',
                text : '编码',
                type : 'input',
                matchType :['EQ','LIKE','LT','GT']
            },{
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
        });
        $grid.setJSON(pager);
        var deleteMethod = $('.batchDelete').batchExecute($("#allChecked"),$grid.data('grid').pager(),'id','是否确认删除[{username}]用户？',function(){
            $.msgbox({
                msg : "<@s.text name="member.delete.success"/>",
                type : "success"
            });
        });
    });
</script>
</@override>
<@override name="pageContent">
<div id="searchFormPanel" class="button-panel pad5A">
    <@s.form id="searchForm" namespace="/member" action="search" method="post">
        <a title="<@s.text name="member.button.add"/>" class="btn medium primary-bg dd-add" href="<@s.url namespace="/member" action="add"/>" target="after:closest('#page-content')">
            <span class="button-content">
                <i class="glyph-icon icon-plus float:left"></i>
                <@s.text name="member.button.add"/>
            </span>
        </a>
        <div class="propertyFilter">
        </div>
        <div class="form-search">
            <input type="text" name="LIKES_username_OR_nickName" title="" data-placement="bottom" class="input tooltip-button ac_input" placeholder="Search..." autocomplete="off" style="display: inline-block; width: 200px;">
            <i class="glyph-icon icon-search"></i>
        </div>
    </@s.form>
</div>
<div class="batch">
    <a title="<@s.text name="member.button.batchdelete"/>" class="btn small primary-bg batchDelete" href="<@s.url namespace="/cms/banner" action="delete"/>">
        <span class="button-content">
            <i class="glyph-icon icon-trash float-left"></i>
            <@s.text name="member.button.batchdelete"/>
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
            <th class="sort" orderBy="username"><@s.text name="member.list.username"/></th>
            <th class="sort" orderBy="nickName"><@s.text name="member.list.nickName"/></th>
            <th><@s.text name="member.list.vip"/></th>
            <th><@s.text name="member.list.enabled"/></th>
            <th class="text-center"><@s.text name="member.list.actions"/></th>
        </tr>
        </thead>
        <tbody>
        <tr class="template" name="default">
            <td><input class="id custom-checkbox" type="checkbox" value="{id}"/></td>
            <td class="font-bold">{username} </td>
            <td>{nickName}</td>
            <td>{details.vip:dict({'true':'是','false':'否'})}</td>
            <td>{enabled:dict({'true':'是','false':'否'})}</td>
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
                            <a title="<@s.text name="member.list.actions.view"/>" class="view" href="<@s.url namespace="/member" action="view?id={id}"/>" target="after:closest('#page-content')">
                                <i class="glyph-icon icon-external-link-sign mrg5R"></i>
                                <@s.text name="member.list.actions.view"/>
                            </a>
                        </li>
                        <li>
                            <a title="<@s.text name="member.list.actions.edit"/>" class="edit" href="<@s.url namespace="/member" action="edit?id={id}"/>" target="after:closest('#page-content')">
                                <i class="glyph-icon icon-edit mrg5R"></i>
                                <@s.text name="member.list.actions.edit"/>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="<@s.url namespace="/member" action="delete?id={id}"/>" class="font-red delete" title="<@s.text name="member.list.actions.delete"/>">
                                <i class="glyph-icon icon-remove mrg5R"></i>
                                <@s.text name="member.list.actions.delete"/>
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