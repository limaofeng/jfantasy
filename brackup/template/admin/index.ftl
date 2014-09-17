<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@s.set name="user" value="#session['SPRING_SECURITY_CONTEXT'].authentication.principal.user"/>
<!doctype html >
<html>
<head>
<link href="${request.contextPath}<@s.property value="@com.fantasy.system.util.SettingUtil@getValue('icon')"/>" type="image/x-icon" rel="shortcut icon" />
<title><@s.property value="@com.fantasy.system.util.SettingUtil@getValue('title')"/></title>
<meta http-equiv="X-UA-Compatible" content="IE=8" >
<link href="${request.contextPath}/static/css/admin-base-1.0.css" rel="stylesheet" />
<link href="${request.contextPath}/static/css/admin-commonui.css" rel="stylesheet" />
<link rel="stylesheet" href="${request.contextPath}/static/css/admin-home.css" type="text/css" />
<script type="text/javascript">window.contextPath = '${request.contextPath}';</script>
<script type="text/javascript" src="${request.contextPath}/static/js/jquery.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/js/common.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/js/admin-home.js"></script>
<script>
var Progress = function() {
};
Progress.prototype.start = function(){
    if($('.kn_itle_top','body').length != 0){
        return;
    }
    //设置焦点防止多次提交
    //	try{
    //		var _4focus=document.getElementById("ispLog4focus");
    //		if(_4focus!=null)_4focus.focus();
    //	}catch(e){}
    var w = 258;
    var speeds = [
        [[20,120],[50,500],[70,1000],[80,100],[95,10000]],
        [[10,100],[40,200],[65,800],[70,100],[95,10000]],
        [[10,800],[60,500],[70,200],[80,100],[95,10000]],
        [[50,110],[60,500],[70,200],[80,100],[95,10000]]
    ];//预加载的进度
    this.progress = $(Progress.TEMPLATE);
    this.lock = $.lock().after(this.progress);
    //lock.css('top',document.documentElement.scrollTop);
    //$('body').addClass('masked');
    $(window).scroll(function(e){
        e = e || window.event;
        if (e && e.preventDefault) {
            e.preventDefault();
            e.stopPropagation();
        } else {
            e.returnvalue = false;
            return false;
        }
    });
    var height = window['innerHeight'] || document.documentElement.clientHeight;
    this.progress.css({position: 'fixed',left:(this.lock.width()/2)-(this.progress.width()/2),top:(height/2)-(this.progress.height()/2)});
    this.progress.find('.progress1_red').width(0);
    var i = 0;
    var bs = speeds[Fantasy.random(4)];
    var prFun = (function(progress){
        return function(){
            progress.find('.progress1_red').animate({width:(w/100)*bs[i][0]},bs[i][1],function(){
                if(++i < bs.length)
                    prFun();
            });
        };
    })(this.progress);
    prFun();
    return this;
};
Progress.prototype.stop = function(){
    this.progress.find('.progress1_red').stop();
    this.progress.find('.progress1_red').animate({width:258},100,(function(zhis){
        return function(){
            zhis.lock.remove();
            zhis.progress.remove();
            $(window).unbind('scroll');
        };
    })(this));
};

Progress.TEMPLATE = '<div class="kn_itle_top" style="width:460px;z-index:5000">'
        + '<div class="progress1">'
        + '<span class="fl"><img src="'+request.getContextPath()+'/static/images/progress_left.png"></span>'
        + '<span class="fl progress1_red"></span>'
        + '<span class="fl"><img src="'+request.getContextPath()+'/static/images/progress_dot.png"></span>'
        + '<span class="fr"><img src="'+request.getContextPath()+'/static/images/progress_left.png"></span>'
        + '</div>'
        + '<p class="progress_p">请稍等，正在为您读取进度中...</p>'
        + '</div>';
var progress = new Progress();
function add_loader(){
    progress.start();
}
function remove_loader(){
    progress.stop();
}
if(window != top.window){
    top.window.location.href=window.location.href;
}
var menu = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(#session['SPRING_SECURITY_CONTEXT'].authentication.principal.menus)" escapeHtml="false"/>;
menu.each(function(i){
	if(i == 0){
		this.close = false;
        window.defaultShowMenuId = this.id;
	}
});

</script>
</head>
<body>
<div id="north">
    <div id="logo"></div>
    <div class="cb"></div>
    <div id="north_right">&nbsp;&nbsp;当前用户：${user.username} &nbsp;&nbsp;  [ <a href="${request.contextPath}/admin/logout">退出</a> ]</div>
    <!-- 导航 -->
    <ul id="nav_top">
        <li class="template"><a href="javascript:void(0);" >{text}</a></li>
    </ul>
    <!-- 导航 end -->
</div>
<div id="north-seperate">
    <div class="icon-h icon-top">图标</div>
</div>
<div id="main">
    <div id="east">
        <div class="east-menu template">
            <h2><a href="javascript:void(0);">{text}</a></h2>
            <ul class="children-menu bcw pt5 pb5">
                <li class="bcw children-template"><a href="javascript:void(0);">[text]</a></li>
            </ul>
        </div>
    </div>
</div>
<div id="east-seperate">
    <div class="icon-v icon-left">图标</div>
</div>
<div id="west">
    <div id="menubar">
        <a href="javascript:void(0);" id="full_screen_btn" title="全屏">全屏</a>
        <a href="javascript:;" id="btn_tabs_prev" title="后退"></a>
        <a href="javascript:;" id="btn_tabs_next" title="前进"></a>
        <div id="menubar_tabs" class="menubar_tabs">
            <h3 class="template"><a class="" href="javascript:void(0);" title="{text}" >{text}<span class='b_gray' title='关闭'></span></a></h3>
        </div>
    </div>
    <div id="westIframe" style="height: 100%;">
        <iframe class="mainIframe template" frameborder="0"></iframe>
    </div>
</div>
</body>
</html>