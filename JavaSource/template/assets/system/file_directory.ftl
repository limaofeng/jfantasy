<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="pageTitle">
<@s.text name="file.directory.title"/>
<small>
    <@s.text name="file.directory.description"/>
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
                deleteMethod([data.key]);
                return stopDefault(e);
            });
        });
        $grid.setJSON(pager);
        var deleteMethod = $('.batchDelete').batchExecute($("#allChecked"),$grid.data('grid').pager(),'key','是否确认删除[{key}]目录？',function(){
            $.msgbox({
                msg : "<@s.text name="file.directory.delete.success"/>",
                type : "success"
            });
        });
    });
</script>
</@override>
<@override name="pageContent">
<div id="searchFormPanel" class="button-panel pad5A">
    <@s.form id="searchForm" namespace="/system/filepath" action="search" method="post">
        <a title="<@s.text name="file.directory.button.add"/>" class="btn medium primary-bg dd-add" href="<@s.url namespace="/system/filepath" action="add"/>" target="after:closest('#page-content')">
            <span class="button-content">
                <i class="glyph-icon icon-plus float:left"></i>
                <@s.text name="file.directory.button.add"/>
            </span>
        </a>
        <div class="propertyFilter">
        </div>
        <div class="form-search">
            <input type="text" name="LIKES_name_OR_key" title="" data-placement="bottom" class="input tooltip-button ac_input" placeholder="Search..." autocomplete="off" style="display: inline-block; width: 200px;">
            <i class="glyph-icon icon-search"></i>
        </div>
    </@s.form>
</div>
<div class="batch">
    <a title="<@s.text name="file.directory.button.batchdelete"/>" class="btn small primary-bg batchDelete" href="<@s.url namespace="/system/filepath" action="delete"/>">
        <span class="button-content">
            <i class="glyph-icon icon-trash float-left"></i>
            <@s.text name="file.directory.button.batchdelete"/>
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
            <th class="sort" orderBy="key"><@s.text name="file.directory.list.key"/></th>
            <th class="sort" orderBy="name"><@s.text name="file.directory.list.name"/></th>
            <th><@s.text name="file.directory.list.dirPath"/></th>
            <th><@s.text name="file.directory.list.fileManager.name"/></th>
            <th class="text-center"><@s.text name="file.directory.list.actions"/></th>
        </tr>
        </thead>
        <tbody>
        <tr class="template" name="default">
            <td><input class="id custom-checkbox" type="checkbox" value="{key}"/></td>
            <td class="font-bold">{key} </td>
            <td>{name}</td>
            <td>{dirPath}</td>
            <td>{fileManager.name}</td>
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
                            <a title="<@s.text name="file.directory.list.actions.edit"/>" class="edit" href="<@s.url namespace="/system/filepath" action="edit?id={key}"/>" target="after:closest('#page-content')">
                                <i class="glyph-icon icon-edit mrg5R"></i>
                                <@s.text name="file.directory.list.actions.edit"/>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="<@s.url namespace="/system/filepath" action="delete?key={key}"/>" class="font-red delete" title="<@s.text name="file.directory.list.actions.delete"/>">
                                <i class="glyph-icon icon-remove mrg5R"></i>
                                <@s.text name="file.directory.list.actions.delete"/>
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