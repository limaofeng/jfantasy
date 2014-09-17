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
        $page$.on('destroy',function(){
            $(window).unbind('resize',_resize);
        });
        var $advsearch = $('.propertyFilter').advsearch({
            filters : [{
                name : 'S_sessionId',
                text : '会话ID',
                type : 'input',
                matchType :['EQ','LIKE','LT','GT']
            },{
                name : 'S_value',
                text : '验证码',
                type : 'input',
                matchType :['EQ','LIKE','GT']
            }]
        });
        //列表初始化
        var pager=<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(pager)" escapeHtml="false"/>;
        var $grid = $('#view').dataGrid($('#searchFormPanel'),$('.batch'));
        $grid.data('grid').view().on('add',function(data){

        });
        $grid.setJSON(pager);
    });
</script>
<div id="searchFormPanel" class="button-panel pad5A">
<@s.form id="searchForm" namespace="/system/captchaConfig" action="captcha_log_search" method="post">
    <@s.hidden name="EQS_captchaConfig.id" value="%{#parameters['EQS_captchaConfig.id']}"/>
    <a title="返回" class="btn medium primary-bg back-page" href="javascript:void(0);">
        <span class="button-content">
             <i class="glyph-icon icon-reply"></i>
            返回
        </span>
    </a>
    <div class="propertyFilter">
    </div>
    <div class="form-search">
        <input type="text" name="LIKES_value" title="" data-placement="bottom" class="input tooltip-button ac_input" placeholder="Search..." autocomplete="off" style="display: inline-block; width: 200px;">
        <i class="glyph-icon icon-search"></i>
    </div>
</@s.form>
</div>
<div class="grid-panel" style="padding-left:10px;padding-right:10px;" id="captcha">
    <table id="view" class="table table-hover mrg5B text-center">
        <thead>
        <tr>
            <th>会话ID</th>
            <th>CODE值</th>
            <th>重试的次数</th>
            <th>创建时间</th>
            <th>最好一次更新时间</th>
        </tr>
        </thead>
        <tbody>
        <tr class="template" name="default">
            <td>{sessionId}</td>
            <td>{value}</td>
            <td>{retry}</td>
            <td>{createTime}</td>
            <td>{modifyTime}</td>
        </tr>
        </tbody>
    </table>
</div>
<div class="divider mrg0T"></div>