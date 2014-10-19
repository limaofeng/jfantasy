<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="pageTitle">
微信配置维护
</@override>
<@override name="head">
<script type="text/javascript">
    $(function(){
        //当浏览器窗口发生变化时,自动调整布局的js代码
        $(window).resize(function () {
            var _$gridPanel = $('.grid-panel');
            if(!!_$gridPanel.length){
                _$gridPanel.css('minHeight', $(window).height() - (_$gridPanel.offset().top + 15));
                _$gridPanel.triggerHandler('resize');
            }
        });
        var $advsearch = $('.propertyFilter').advsearch({
            filters : [{
                name : 'S_appid',
                text : 'appid',
                type : 'input',
                matchType :['EQ','LIKE','LT','GT']
            },{
                name : 'S_appsecret',
                text : 'appsecret',
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
        var deleteMethod = $('.batchDelete').batchExecute($("#allChecked"),$grid.data('grid').pager(),'id','是否确认删除[{appid}]用户？',function(){
            $.msgbox({
                msg : "删除成功!",
                type : "success"
            });
        });
    });
</script>
</@override>
<@override name="pageContent">
<div id="searchFormPanel" class="button-panel pad5A">
    <@s.form id="searchForm" namespace="/weixin/token" action="search" method="post">
        <a title="添加" class="btn medium primary-bg dd-add" href="<@s.url namespace="/weixin/token" action="add"/>" ajax="{type:'html',target:'closest(\'#page-content\')'}">
            <span class="button-content">
                <i class="glyph-icon icon-plus float:left"></i>
                添加
            </span>
        </a>
        <div class="propertyFilter"></div>
        <div class="form-search">
            <input type="text" name="LIKES_appid_OR_appsecret" title="" data-placement="bottom" class="input tooltip-button ac_input" placeholder="Search..." autocomplete="off" style="display: inline-block; width: 200px;">
            <i class="glyph-icon icon-search"></i>
        </div>
    </@s.form>
</div>
<div class="batch">
    <a title="批量删除" class="btn small primary-bg batchDelete" href="<@s.url namespace="/weixin/token" action="delete"/>">
        <span class="button-content">
            <i class="glyph-icon icon-trash float-left"></i>
           批量删除
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
                <th>appid</th>
                <th>appsecret</th>
                <th>tokenName</th>
                <th>获取时间</th>
                <th>操作</th>
            </tr>
            </thead>
        <tbody>
            <tr class="template" name="default">
                <td><input class="id custom-checkbox" type="checkbox" value="{id}"/></td>
                <td>{appid}</td>
                <td>{appsecret}</td>
                <td>{tokenName}</td>
                <td>{modifyTime:date('yyyy-MM-dd HH:mm:ss')}</td>
                <td>
                    <div class="dropdown actions">
                        <a href="javascript:;" title="" class="btn medium bg-blue" data-toggle="dropdown">
                        <span class="button-content">
                            <i class="glyph-icon font-size-11 icon-cog"></i>
                            <i class="glyph-icon font-size-11 icon-chevron-down"></i>
                        </span>
                        </a>
                        <ul class="dropdown-menu float-right">
                            <li>
                                <a title="详情" class="view" href="<@s.url namespace="/weixin/token" action="view?id={id}"/>" ajax="{type:'html',target:'closest(\'#page-content\')'}">
                                    <i class="glyph-icon icon-external-link-sign mrg5R"></i>
                                    详情
                                </a>
                            </li>
                            <li>
                                <a title="编辑" class="edit" href="<@s.url namespace="/weixin/token" action="edit?id={id}"/>" ajax="{type:'html',target:'closest(\'#page-content\')'}">
                                    <i class="glyph-icon icon-edit mrg5R"></i>
                                    编辑
                                </a>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <a href="<@s.url namespace="/weixin/token" action="delete?id={id}"/>" class="font-red delete" title="删除">
                                    <i class="glyph-icon icon-remove mrg5R"></i>
                                    删除
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