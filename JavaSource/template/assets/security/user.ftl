<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="pageTitle">
    <@s.text name="security.user.title"/>
    <small>
        <@s.text name="security.user.description"/>
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
        var $grid = $('#view').dataGrid($('#searchFormPanel'),$('#batchBtns'));
        $grid.data('grid').view().on('add',function(data){
            this.target.find('.delete').click(function(e){
                deleteMethod([data.id]);
                return stopDefault(e);
            });
        });
        $grid.setJSON(pager);
        var deleteMethod = $('.batchDelete').batchExecute($("#allChecked"),$grid.data('grid').pager(),'id','<@s.text name="security.user.delete.alert"><@s.param name="0" value="'{nickName}'"/></@s.text>',function(){
            $.msgbox({
                msg : "<@s.text name="security.user.delete.success"/>",
                type : "success"
            });
        });

    });
</script>
</@override>
<@override name="pageContent">
    <div id="searchFormPanel" class="button-panel pad5A">
        <@s.form id="searchForm" namespace="/security/user" action="search" method="post">
            <a title="<@s.text name="security.user.button.add"/>" class="btn medium primary-bg" href="<@s.url namespace="/security/user" action="add"/>" target="after:closest('#page-content')">
                <span class="button-content">
                   <i class="glyph-icon icon-plus float-left"></i>
                    <@s.text name="security.user.button.add"/>
                </span>
            </a>
            <div class="propertyFilter">
            </div>
            <div class="form-search">
                <input type="text" name="LIKES_name_OR_code" title="" data-placement="bottom" class="input tooltip-button ac_input" placeholder="Search..." autocomplete="off" style="display: inline-block; width: 200px;">
                <i class="glyph-icon icon-search"></i>
            </div>
        </@s.form>
    </div>
    <div id="batchBtns">
        <a title="<@s.text name="security.user.button.batchDelete"/>" class="btn small primary-bg batchDelete" href="<@s.url namespace="/security/user" action="delete"/>">
            <span class="button-content">
                <i class="glyph-icon icon-trash float-left"></i>
                <@s.text name="security.user.button.batchDelete"/>
            </span>
        </a>
    </div>
<div class="grid-panel">
    <table id="view" class="custom-table table table-hover mrg5B text-center">
        <thead>
        <tr>
            <th class="pad15L" style="width:20px;">
                <input id="allChecked" class="custom-checkbox bg-white" checkAll=".id" type="checkbox"/>
            </th>
            <th class="sort" orderBy="username"><@s.text name="security.user.list.username"/></th>
            <th class="sort" orderBy="nickName"><@s.text name="security.user.list.nickName"/></th>
            <th class="sort" orderBy="details.email"><@s.text name="security.user.list.email"/></th>
            <th class="sort" orderBy="lastLoginTime"><@s.text name="security.user.list.lastLoginTime"/></th>
            <th><@s.text name="security.user.list.role"/></th>
            <th class="center"><@s.text name="security.user.list.actions"/></th>
        </tr>
        </thead>
        <tbody>
        <tr class="template" name="default">
            <td><input class="id custom-checkbox" type="checkbox" value="{id}"/></td>
            <td>{username}</td>
            <td>{nickName}</td>
            <td>{details.email}</td>
            <td>{lastLoginTime:format('yyyy-MM-dd HH:mm:ss')}</td>
            <td title="{roles}">{roles:ellipsis(30,'...')}</td>
            <td class="pad0T pad0B center">
                <div class="dropdown actions">
                    <a href="javascript:;" title="" class="btn medium bg-blue" data-toggle="dropdown">
                        <span class="button-content">
                            <i class="glyph-icon font-size-11 icon-cog"></i>
                            <i class="glyph-icon font-size-11 icon-chevron-down"></i>
                        </span>
                    </a>
                    <ul class="dropdown-menu float-right">
                        <li>
                            <a href="<@s.url namespace="/security/user" action="edit?id={id}"/>" class="edit" title="<@s.text name="security.user.list.actions.edit"/>" target="after:closest('#page-content')">
                                <i class="glyph-icon icon-edit mrg5R"></i>
                                <@s.text name="security.user.list.actions.edit"/>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="<@s.url namespace="/security/user" action="delete?id={id}"/>" class="font-red delete" title="<@s.text name="security.user.list.actions.delete"/>">
                                <i class="glyph-icon icon-remove mrg5R"></i>
                                <@s.text name="security.user.list.actions.delete"/>
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