<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="pageTitle">
粉丝维护
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
                name : 'S_code',
                text : 'openId',
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
            this.target.find('img').attr("src",data.headimgurl);
            if(data.state=="1"){
                $('.edit',this.target).hide();
            }
        });
        $grid.setJSON(pager);
        $("#refresh").click(function(){
            $.get("${request.contextPath}/weixin/refresh.do",{"appid":"${appid}"},function(){
                $('#pager').pager().reload();
            });
        })
    });
</script>
</@override>
<@override name="pageContent">
<div id="searchFormPanel" class="button-panel pad5A">
    <@s.form id="searchForm" namespace="/weixin" action="user_search" method="post">
        <a title="刷新粉丝" id="refresh" class="btn medium primary-bg" href="jaavscript:;">
            <span class="button-content">
                <i class="glyph-icon icon-refresh float:left"></i>
                刷新粉丝
            </span>
        </a>
        <div class="propertyFilter"></div>
        <div class="form-search">
            <input type="text" name="LIKES_name_OR_sn" title="" data-placement="bottom" class="input tooltip-button ac_input" placeholder="Search..." autocomplete="off" style="display: inline-block; width: 200px;">
            <i class="glyph-icon icon-search"></i>
        </div>
    </@s.form>
</div>
<div class="grid-panel">
    <table id="view" class="table table-hover mrg5B text-center">
        <thead>
        <tr>
            <th style="width:230px;">openId</th>
            <th style="width:270px;">昵称</th>
            <th>性别</th>
            <th>是否关注</th>
            <th class="text-center">关注时间</th>
        </tr>
        </thead>
        <tbody>
        <tr class="template" name="default">
            <td>{openid}</td>
            <td class="font-bold">
                <div class="large btn info-icon float-left mrg5R" style="height:45px;">
                    <img data-src="holder.js/38x38/simple" class="img-small view-field"/>
                </div>
                <p><span class="label bg-purple mrg5R">{nickname} </span></p>
            </td>
            <td>{sex:dict({'0':'未知','1':'男','2':'女'})}</td>
            <td>{subscribe:dict({'0':'未关注','1':'已关注'})}</td>
            <td>{time}</td>
        </tr>
        </tbody>
    </table>
</div>
<div class="divider mrg0T"></div>
</@override>
<@extends name="../wrapper.ftl"/>