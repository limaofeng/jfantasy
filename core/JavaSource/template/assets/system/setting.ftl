<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
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
                deleteMethod([data.id]);
                return stopDefault(e);
            });
        });
        $grid.setJSON(pager);
        var deleteMethod = $('.batchDelete').batchExecute($("#allChecked"),$grid.data('grid').pager(),'id','是否确认删除[{name}]属性?',function(){
            $.msgbox({
                msg : "<@s.text name="system.setting.delete.success"/>",
                type : "success"
            });
        });
    });
</script>
<div id="searchFormPanel" class="button-panel pad5A">
    <@s.form id="searchForm" namespace="/system/website/setting" action="search" method="post">
        <@s.hidden name="EQL_website.id" value="%{#parameters['EQL_website.id']}"/>
        <a title="<@s.text name="system.setting.button.add"/>" class="btn medium primary-bg dd-add" href="${request.contextPath}/system/website/setting/add.do?websiteId=<@s.property value="#parameters['EQL_website.id']"/>" target="after:closest('.ajax-load-div')">
            <span class="button-content">
                <i class="glyph-icon icon-plus float:left"></i>
                <@s.text name="system.setting.button.add"/>
            </span>
        </a>
        <a title="<@s.text name="system.setting.button.return"/>" class="btn medium primary-bg back-page" href="javascript:void(0);">
            <span class="button-content">
                 <i class="glyph-icon icon-reply"></i>
                <@s.text name="system.setting.button.return"/>
            </span>
        </a>
        <div class="propertyFilter">
        </div>
        <div class="form-search">
            <input type="text" name="LIKES_name" title="" data-placement="bottom" class="input tooltip-button ac_input" placeholder="Search..." autocomplete="off" style="display: inline-block; width: 200px;">
            <i class="glyph-icon icon-search"></i>

        </div>

    </@s.form>
</div>
<div class="batch">
    <a title="<@s.text name="system.setting.button.batchdelete"/>" class="btn small primary-bg batchDelete" href="<@s.url namespace="/system/website/setting" action="delete"/>">
        <span class="button-content">
            <i class="glyph-icon icon-trash float-left"></i>
        <@s.text name="system.setting.button.batchdelete"/>
        </span>
    </a>
</div>
<div class="grid-panel" style="padding-left:10px;padding-right:10px;">
    <table id="view" class="table table-hover mrg5B text-center">
        <thead>
        <tr>
            <th class="pad15L" style="width:20px;">
                <input id="allChecked" class="custom-checkbox bg-white" checkAll=".id" type="checkbox" <#--checktip="{message:'您选中了{num}条记录',tip:'#config_check_info'}"--> />
            </th>
            <th><@s.text name="system.setting.name"/></th>
            <th><@s.text name="system.setting.key"/></th>
            <th><@s.text name="system.setting.value"/></th>
            <th class="text-center"><@s.text name="system.setting.actions"/></th>
        </tr>
        </thead>
        <tbody>
        <tr class="template" name="default">
            <td><input class="id custom-checkbox" type="checkbox" value="{id}"/></td>
            <td class="font-bold">{name} </td>
            <td>{key}</td>
            <td>{value}</td>
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
                            <a title="<@s.text name="system.setting.actions.edit"/>" class="edit" href="<@s.url namespace="/system/website/setting" action="edit?id={id}"/>" target="after:closest('.ajax-load-div')">
                                <i class="glyph-icon icon-edit mrg5R"></i>
                            <@s.text name="system.setting.actions.edit"/>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="<@s.url namespace="/system/website/setting" action="delete?ids={id}"/>" class="font-red delete" title=" <@s.text name="system.setting.actions.delete"/>">
                                <i class="glyph-icon icon-remove mrg5R"></i>
                            <@s.text name="system.setting.actions.delete"/>
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