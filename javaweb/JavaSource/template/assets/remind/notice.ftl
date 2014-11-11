<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="pageTitle">
提醒列表
<small>
    remind
</small>
</@override>
<@override name="head">
<script language="javascript" type="text/javascript" src="${request.contextPath}/js/jquery.scroll.js" charset="utf-8"></script>
<script language="javascript" type="text/javascript">

</script>
<script type="text/javascript">

$(function() {
    $(window).resize(function () {
        $('.grid-panel').each(function(){
            $(this).css('minHeight', $(window).height() - ( $(this).offset().top + 15));
            $(this).triggerHandler('resize');
        });
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
    var deleteMethod = $('.batchDelete').batchExecute($("#allChecked"),$grid.data('grid').pager(),'id','是否确认删除？',function(){
        $.msgbox({
            msg : "删除成功!",
            type : "success"
        });
    });
    var modelPager=<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(models)" escapeHtml="false"/>;
    $("#modelDiv").ajaxScroll({
        advanceSize:50,
        searchOption:{
            title:"消息模版",searchName:"LIKES_name",searchText:"消息模版名称",filter:["name"]
        },
        data:{title:"name",img:{path:"avatar"}},
        url:"${request.contextPath}/notice/model/search.do",
        pager:modelPager,
        optionBtn:[
            {name:"新增",href:"${request.contextPath}/notice/model/add.do",target:"after:closest('#page-content')"},
            {name:"修改",href:"${request.contextPath}/notice/model/edit.do?id=$[code]",target:"after:closest('#page-content')"},
            {name:"删除",href:"${request.contextPath}/notice/model/delete.do",click:function(){
                $.post($(this).data("href"),{ids:[$(this).data("d").code]},function(){
                    $.msgbox({
                        msg : "删除成功",
                        type : "success"
                    });
                })
            }}
        ]
    });
});
</script>
<style>
    .chat-box .popover{width:auto;}
    .chat-box .popover.left {
        float: right;
    }
    .notifications-box li{
height:46px;line-height: 46px;
    }
    .chat-box .popover.left .arrow:after {
        border-left-color: #fff;
    }
    .chat-box .popover.right .arrow:after {
        border-right-color: #fff;
    }
    .current_user{
        zoom:1.3;
        background: #2381E9;
    }
    .current_user p span{
        color: #797979;
        background: #e9ecf1;
    }
</style>
</@override>
<@override name="pageContent">
<div class="row" id="messagePage">
    <div class="col-md-3" id="modelDiv">

    </div>
    <div class="col-md-9" id="noticeContent">
        <div id="searchFormPanel" class="button-panel pad5A">
            <@s.form id="searchForm" namespace="/notice" action="search" method="post">
                <div class="propertyFilter">
                </div>
                <div class="form-search">
                    <input type="text" name="LIKES_title" title="" data-placement="bottom" class="input tooltip-button ac_input" placeholder="Search..." autocomplete="off" style="display: inline-block; width: 200px;">
                    <i class="glyph-icon icon-search"></i>
                </div>
            </@s.form>
        </div>
        <div class="batch">
            <a title="批量删除" class="btn small primary-bg batchDelete" href="<@s.url namespace="/notice" action="delete"/>">
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
                    <th style="width:300px;">内容</th>
                    <th>创建时间</th>
                    <th>所用模版</th>
                    <th class="text-center">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr class="template" name="default">
                    <td><input class="id custom-checkbox" type="checkbox" value="{id}"/></td>
                    <td class="font-bold">{content} </td>
                    <td>{createTime:date('yyyy-MM-dd HH:mm:ss')}</td>
                    <td>
                        <div class="large btn info-icon float-left mrg5R">
                            <img data-src="holder.js/38x38/simple" class="img-small view-field" name="model.avatar"/>
                        </div>
                        <p style="margin-top: 10px;"><span class="label bg-purple mrg5R">{model.name}</span> </p>
                    </td>
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
                                    <a title="详情" class="view" href="<@s.url namespace="/notice" action="view?id={id}"/>" target="after:closest('#page-content')" >
                                        <i class="glyph-icon icon-external-link-sign mrg5R"></i>
                                        详情
                                    </a>
                                </li>
                                <li class="divider"></li>
                                <li>
                                    <a title="删除" href="<@s.url namespace="/notice" action="delete?ids={id}"/>" class="font-red delete">
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
    </div>

</div>
</@override>
<@extends name="../wrapper.ftl"/>