<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="pageTitle">
    <@s.text name="schedule.title"/>
<small>
    <@s.text name="schedule.description"/>
</small>
</@override>
<@override name="head">
<script type="text/javascript">
    /*
    $(function () {
        $('#pager').pager($('#searchForm'), 15, $('#view').view().on('add', function (data) {
            $('.delete', this.target).click(function () {
                return false;
            });
            $('.edit', this.target).click(function () {
                $('#roleDetails').show().prevAll().hide();
                return false;
            });
        })).setJSON(jobs);
    });*/
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
        var jobs=<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(jobInfos)" escapeHtml="false"/>;
        var pager={"order":"asc","orderBy":null,"pageSize":15,"orderBySetted":false,"orders":[],"totalCount":0,"pageItems":jobs,"totalPage":1,"currentPage":1};
        var $grid = $('#view').dataGrid($('#searchFormPanel'),$('.batch'));

        $grid.data('grid').view().on('add',function(data){

            var trigger = data.triggerInfos[0];
            this.target.find('.preFire').text(trigger.preFire);

            this.target.find('.nextFire').text(trigger.nextFire);

            this.target.find('.delete').click(function(e){
                deleteMethod([data.id]);
                return stopDefault(e);
            });
        });

        $grid.setJSON(pager);

        var deleteMethod = $('.batchDelete').batchExecute($("#allChecked"),$grid.data('grid').pager(),'id',' <@s.text name="schedule.delete.alert"/>',function(){
            $.msgbox({
                msg : " <@s.text name="schedule.delete.success"/>",
                type : "success"
            });
        });
    });
</script>
</@override>
<@override name="pageContent">
<div id="searchFormPanel" class="button-panel pad5A">
    <@s.form id="searchForm" namespace="/system/schedule" action="search" method="post">
        <a title="<@s.text name="schedule.button.add"/>" class="btn medium primary-bg dd-add" href="<@s.url namespace="/system/schedule" action="add"/>" target="after:closest('#page-content')">
            <span class="button-content">
                <i class="glyph-icon icon-plus float:left"></i>
                <@s.text name="schedule.button.add"/>
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
    <a title="<@s.text name="schedule.button.batchdelete"/>" class="btn small primary-bg batchDelete" href="<@s.url namespace="/system/schedule" action="delete"/>">
        <span class="button-content">
            <i class="glyph-icon icon-trash float-left"></i>
            <@s.text name="schedule.button.batchdelete"/>
        </span>
    </a>
</div>
<div class="grid-panel">
    <table id="view" class="table table-hover mrg5B text-center">
        <thead>
        <tr>
            <th class="pad15L" style="width:20px;">
                <input id="allChecked" class="custom-checkbox bg-white" checkAll=".id" type="checkbox" />
            </th>
            <th><@s.text name="schedule.list.name"/></th>
            <th><@s.text name="schedule.list.class"/></th>
            <th><@s.text name="schedule.list.preFire"/></th>
            <th><@s.text name="schedule.list.nextFire"/></th>
            <th width="10%"><@s.text name="schedule.list.parameter"/></th>
            <th width="10%"><@s.text name="schedule.list.running"/></th>
            <th class="text-center" width="10%"><@s.text name="schedule.list.actions"/></th>
        </tr>
        </thead>
        <tbody>
        <tr class="template" name="default">
            <td><input class="id custom-checkbox" type="checkbox" value="{id}"/></td>
            <td class="font-bold">{key.group}.{key.name} </td>
            <td>{jobClass}</td>
            <td class="preFire"></td>
            <td class="nextFire"></td>
            <td></td>
            <td>{running}</td>
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
                            <a title="<@s.text name="schedule.list.actions.run"/>" class="edit" href="<@s.url namespace="/system/schedule" action="resumeJob?jobKey={id}"/>" target="after:closest('#page-content')">
                                <i class="glyph-icon icon-edit mrg5R"></i>
                                <@s.text name="schedule.list.actions.run"/>
                            </a>
                        </li>
                        <li>
                            <a title="<@s.text name="schedule.list.actions.suspend"/>" class="edit" href="<@s.url namespace="/system/schedule" action="pauseJob?jobKey={id}"/>" >
                                <i class="glyph-icon icon-edit mrg5R"></i>
                                <@s.text name="schedule.list.actions.suspend"/>
                            </a>
                        </li>
                        <li>
                            <a title="<@s.text name="schedule.list.actions.edit"/>" class="edit" href="<@s.url namespace="/system/schedule" action="edit?queryGroup={group}&queryJobName={name}&jobKey={id}"/>" target="after:closest('#page-content')">
                                <i class="glyph-icon icon-edit mrg5R"></i>
                                <@s.text name="schedule.list.actions.edit"/>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="###" class="font-red delete" title="<@s.text name="schedule.list.actions.delete"/>">
                                <i class="glyph-icon icon-remove mrg5R"></i>
                                <@s.text name="schedule.list.actions.delete"/>
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