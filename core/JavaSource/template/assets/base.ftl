<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@s.set name="adminUser" value="#session['SPRING_SECURITY_CONTEXT'].authentication.principal"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title><@s.property value="@com.fantasy.system.util.SettingUtil@getValue('title')"/></title>
    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <!-- Favicons -->
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="${request.contextPath}/assets/images/icons/apple-touch-icon-144-precomposed.png" />
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="${request.contextPath}/assets/images/icons/apple-touch-icon-114-precomposed.png" />
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="${request.contextPath}/assets/images/icons/apple-touch-icon-72-precomposed.png" />
    <link rel="apple-touch-icon-precomposed" href="${request.contextPath}/assets/images/icons/apple-touch-icon-57-precomposed.png" />
    <link rel="shortcut icon" href="${request.contextPath}/assets/images/icons/favicon.png" />
    <script type="text/javascript">window.contextPath = '${request.contextPath}';</script>
    <!--[if lt IE 9]>
    <script src="${request.contextPath}/assets/js/minified/core/html5shiv.min.js"></script>
    <script src="${request.contextPath}/assets/js/minified/core/respond.min.js"></script>
    <![endif]-->
    <!-- Fides Admin JS -->
    <script type="text/javascript" src="${request.contextPath}/assets/js/minified/aui-production.js"></script>
    <script type="text/javascript" src="${request.contextPath}/assets/js/common.js"></script>
    <!-- Fides Admin CSS Core -->
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/assets/css/minified/aui-production.min.css" />
    <!-- Theme UI -->
    <link id="layout-theme" rel="stylesheet" type="text/css" href="${request.contextPath}/assets/themes/minified/fides/color-schemes/dark-blue.min.css" />
    <!-- Fides Admin Responsive -->
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/assets/themes/minified/fides/common.min.css" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/assets/themes/minified/fides/responsive.min.css" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/assets/css/component.css" />
    <@block name="head"></@block>
    <style type="text/css">
        .inline-input input[type="text"], .inline-input input[type="password"], input.inline-input[type="text"], input.inline-input[type="password"] {
            background: none repeat scroll 0 0 #fff;
            border-color: #dee3ea;
            border-radius: 0;
            border-style: none none solid;
            border-width: 0 0 1px;
            box-shadow: none;
        }
    </style>
    <script>
        jQuery(window).load(function(){
            var wait_loading = window.setTimeout( function(){
                $('#loading').slideUp('fast');
                jQuery('body').css('overflow','auto');
            },1);
        });
        var add_loader = function(){
            var d = '<div id="loader-overlay" class="ui-front hide loader ui-widget-overlay ui-state-default opacity-60"><img src="${request.contextPath}/assets/images/loader-dark.gif" alt="" /></div>';
            $("#loader-overlay").remove();
            $("body").append(d);
            $("#loader-overlay").fadeIn("fast");
        };
        var remove_loader = function(){
            $("#loader-overlay").fadeOut("fast")
        };
        $(function(){
            setInterval(function(){
                $(window).resize();
            },1000);
        });
    </script>
</head>
<body style="overflow: hidden;">


<div id="loading" class="ui-front loader ui-widget-overlay bg-white opacity-100">
    <img src="${request.contextPath}/assets/images/loader-dark.gif" alt="" />
</div>

<div id="page-wrapper" class="demo-example">
<#--
<div class="theme-customizer">
    <a href="javascript:;" class="change-theme-btn" title="Change theme">
        <i class="glyph-icon icon-cog"></i>
    </a>
    <div class="theme-wrapper">

        <div class="popover-title">布局模式</div>
        <div class="pad10A clearfix">
            <a class="fluid-layout-btn hidden bg-blue-alt medium btn" href="javascript:;" title=""><span class="button-content">全屏模式</span></a>
            <a class="boxed-layout-btn bg-blue-alt medium btn" href="javascript:;" title=""><span class="button-content">窄屏模式</span></a>
        </div>
        <div class="popover-title">页面背景</div>
        <div class="pad10A clearfix">
            <a href="javascript:;" class="choose-bg" boxed-bg="#000" style="background: #000;" title=""></a>
            <a href="javascript:;" class="choose-bg" boxed-bg="#333" style="background: #333;" title=""></a>
            <a href="javascript:;" class="choose-bg" boxed-bg="#666" style="background: #666;" title=""></a>
            <a href="javascript:;" class="choose-bg" boxed-bg="#888" style="background: #888;" title=""></a>
            <a href="javascript:;" class="choose-bg" boxed-bg="#383d43" style="background: #383d43;" title=""></a>
            <a href="javascript:;" class="choose-bg" boxed-bg="#fafafa" style="background: #fafafa; border: #ccc solid 1px;" title=""></a>
            <a href="javascript:;" class="choose-bg" boxed-bg="#fff" style="background: #fff; border: #eee solid 1px;" title=""></a>
        </div>
        <div class="popover-title">颜色方案</div>
        <div class="pad10A clearfix change-layout-theme">
            <p class="font-gray-dark font-size-11 pad0B">更多的配色方案将很快实现!</p>
            <div class="divider mrg10T mrg10B"></div>
            <a href="javascript:;" class="choose-theme" layout-theme="dark-blue" title="">
                <span style="background: #2381E9;"></span>
            </a>
            <a href="javascript:;" class="choose-theme opacity-30 mrg15R" layout-theme="white-blue" title="">
                <span style="background: #2381E9;"></span>
            </a>
            <a href="javascript:;" class="choose-theme" layout-theme="dark-green" title="D">
                <span style="background: #78CE12;"></span>
            </a>
            <a href="javascript:;" class="choose-theme opacity-30 mrg15R" layout-theme="white-green" title="D">
                <span style="background: #78CE12;"></span>
            </a>
            <a href="javascript:;" class="choose-theme" layout-theme="dark-orange" title="">
                <span style="background: #FF6041;"></span>
            </a>
            <a href="javascript:;" class="choose-theme opacity-30 mrg15R" layout-theme="white-orange" title="">
                <span style="background: #FF6041;"></span>
            </a>
        </div>

    </div>
</div>
-->
<div id="page-header" class="clearfix">
<div id="header-logo">
    <a href="javascript:;" class="tooltip-button" data-placement="bottom" title="关闭左侧栏" id="close-sidebar">
        <i class="glyph-icon icon-caret-left"></i>
    </a>
    <a href="javascript:;" class="tooltip-button hidden" data-placement="bottom" title="打开左侧栏" id="rm-close-sidebar">
        <i class="glyph-icon icon-caret-right"></i>
    </a>
    <a href="javascript:;" class="tooltip-button hidden" title="Navigation Menu" id="responsive-open-menu">
        <i class="glyph-icon icon-align-justify"></i>
    </a>
    <@s.property value="@com.fantasy.system.util.SettingUtil@getValue('system','FIDES ADMIN')"/> <i class="opacity-80"><@s.property value="@com.fantasy.system.util.SettingUtil@getValue('systemVersion','1.2')"/></i>
</div>
<#include "about.ftl"/>
<#--
<div class="hide" id="white-modal-80" title="Dialog with tabs">
    <div class="tabs pad15A remove-border opacity-80">
        <ul class="opacity-80">
            <li><a href="#example-tabs-1">First</a></li>
            <li><a href="#example-tabs-2">Second</a></li>
            <li><a href="#example-tabs-3">Third</a></li>
        </ul>
        <div id="example-tabs-1">
            <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
            </p>
            <p>Nam dui erat, auctor a, dignissim quis, sollicitudin eu, felis. Pellentesque nisi urna, interdum eget, sagittis et, consequat vestibulum, lacus. Mauris porttitor ullamcorper augue.
            </p>
        </div>
        <div id="example-tabs-2">
            <p>Phasellus mattis tincidunt nibh. Cras orci urna, blandit id, pretium vel, aliquet ornare, felis. Maecenas scelerisque sem non nisl. Fusce sed lorem in enim dictum bibendum.
            </p>
            <p>Nam dui erat, auctor a, dignissim quis, sollicitudin eu, felis. Pellentesque nisi urna, interdum eget, sagittis et, consequat vestibulum, lacus. Mauris porttitor ullamcorper augue.
            </p>
        </div>
        <div id="example-tabs-3">
            <p>Nam dui erat, auctor a, dignissim quis, sollicitudin eu, felis. Pellentesque nisi urna, interdum eget, sagittis et, consequat vestibulum, lacus. Mauris porttitor ullamcorper augue.
            </p>
            <p>Nam dui erat, auctor a, dignissim quis, sollicitudin eu, felis. Pellentesque nisi urna, interdum eget, sagittis et, consequat vestibulum, lacus. Mauris porttitor ullamcorper augue.
            </p>
        </div>
    </div>
    <div class="pad10A">
        <div class="infobox success-bg radius-all-4">
            <p>Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque</p>
        </div>
    </div>
    <div class="ui-dialog-buttonpane clearfix">

        <a href="dropdown_menus.html" class="btn medium float-left bg-azure">
            <span class="button-content text-transform-upr font-size-11">Dropdown menus</span>
        </a>
        <div class="button-group float-right">
            <a href="buttons.html" class="btn medium bg-black" title="View more buttons examples">
                <i class="glyph-icon icon-star"></i>
            </a>
            <a href="buttons.html" class="btn medium bg-black" title="View more buttons examples">
                <i class="glyph-icon icon-random"></i>
            </a>
            <a href="buttons.html" class="btn medium bg-black" title="View more buttons examples">
                <i class="glyph-icon icon-map-marker"></i>
            </a>
        </div>
        <a href="javascript:;" class="medium btn bg-blue-alt float-right mrg10R tooltip-button" data-placement="left" title="Remove comment">
            <i class="glyph-icon icon-plus"></i>
        </a>

    </div>
</div>
-->
<div class="user-profile dropdown">
    <a href="javascript:;" title="" class="user-ico clearfix" data-toggle="dropdown">
        <@s.img src="%{#adminUser.user.details.avatar.absolutePath}" ratio="36x36"/>
        <span><@s.property value="#adminUser.user.nickName"/></span>
        <i class="glyph-icon icon-chevron-down"></i>
    </a>
    <ul class="dropdown-menu float-right">
        <li>
            <a href="javascript:;" title="">
                <i class="glyph-icon icon-user mrg5R"></i>
                个人资料
            </a>
        </li>
        <li>
            <a href="javascript:;" title="">
                <i class="glyph-icon icon-cog mrg5R"></i>
                设置
            </a>
        </li>
        <li>
            <a href="javascript:;" title="">
                <i class="glyph-icon icon-flag mrg5R"></i>
                通知中心
            </a>
        </li>
        <li class="divider"></li>
        <li>
            <@s.a href="${request.getContextPath() + '/logout'}">
                <i class="glyph-icon icon-signout font-size-13 mrg5R"></i>
                <span class="font-bold">退出</span>
            </@s.a>
        </li>
        <#--
        <li class="dropdown-submenu float-left">
            <a href="javascript:;" data-toggle="dropdown" title="">
                Dropdown menu
            </a>
            <ul class="dropdown-menu">
                <li>
                    <a href="javascript:;" title="">
                        Submenu 1
                    </a>
                </li>
                <li>
                    <a href="javascript:;" title="">
                        Submenu 2
                    </a>
                </li>
                <li class="dropdown-submenu">
                    <a href="javascript:;" title="">
                        Submenu 3
                    </a>
                    <ul class="dropdown-menu">
                        <li>
                            <a href="javascript:;" title="">
                                Submenu 2-1
                            </a>
                        </li>
                        <li>
                            <a href="javascript:;" title="">
                                Submenu 2-2
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>
        </li>
        <li>
            <a href="javascript:;" title="">
                Another menu item
            </a>
        </li>
        -->
    </ul>
</div>
<#--
<div class="dropdown dash-menu">
    <a href="javascript:;" data-toggle="dropdown" data-placement="left" class="medium btn primary-bg float-right popover-button-header hidden-mobile tooltip-button" title="快捷菜单">
        <i class="glyph-icon icon-th"></i>
    </a>
    <div class="dropdown-menu float-right">
        <div class="small-box">
            <div class="pad10A dashboard-buttons clearfix">
                <p class="font-gray-dark font-size-11 pad0B">This menu type can be used in pages, not just popovers.</p>
                <div class="divider mrg10T mrg10B"></div>
                <a href="javascript:;" class="btn vertical-button hover-blue-alt" title="">
                                    <span class="glyph-icon icon-separator-vertical pad0A medium">
                                        <i class="glyph-icon icon-dashboard opacity-80 font-size-20"></i>
                                    </span>
                    <span class="button-content">Dashboard</span>
                </a>
                <a href="javascript:;" class="btn vertical-button hover-green" title="">
                                    <span class="glyph-icon icon-separator-vertical pad0A medium">
                                        <i class="glyph-icon icon-tags opacity-80 font-size-20"></i>
                                    </span>
                    <span class="button-content">Widgets</span>
                </a>
                <a href="javascript:;" class="btn vertical-button hover-orange" title="">
                                    <span class="glyph-icon icon-separator-vertical pad0A medium">
                                        <i class="glyph-icon icon-reorder opacity-80 font-size-20"></i>
                                    </span>
                    <span class="button-content">Tables</span>
                </a>
                <a href="javascript:;" class="btn vertical-button hover-orange" title="">
                                    <span class="glyph-icon icon-separator-vertical pad0A medium">
                                        <i class="glyph-icon icon-bar-chart opacity-80 font-size-20"></i>
                                    </span>
                    <span class="button-content">Charts</span>
                </a>
                <a href="javascript:;" class="btn vertical-button hover-purple" title="">
                                    <span class="glyph-icon icon-separator-vertical pad0A medium">
                                        <i class="glyph-icon icon-laptop opacity-80 font-size-20"></i>
                                    </span>
                    <span class="button-content">Buttons</span>
                </a>
                <a href="javascript:;" class="btn vertical-button hover-azure" title="">
                                    <span class="glyph-icon icon-separator-vertical pad0A medium">
                                        <i class="glyph-icon icon-code opacity-80 font-size-20"></i>
                                    </span>
                    <span class="button-content">Panels</span>
                </a>
            </div>

            <div class="bg-gray text-transform-upr font-size-12 font-bold font-gray-dark pad10A">Dashboard menu</div>
            <div class="pad10A dashboard-buttons clearfix">
                <a href="javascript:;" class="btn vertical-button remove-border bg-blue" title="">
                                    <span class="glyph-icon icon-separator-vertical pad0A medium">
                                        <i class="glyph-icon icon-dashboard opacity-80 font-size-20"></i>
                                    </span>
                    <span class="button-content">Dashboard</span>
                </a>
                <a href="javascript:;" class="btn vertical-button remove-border bg-red" title="">
                                    <span class="glyph-icon icon-separator-vertical pad0A medium">
                                        <i class="glyph-icon icon-tags opacity-80 font-size-20"></i>
                                    </span>
                    <span class="button-content">Widgets</span>
                </a>
                <a href="javascript:;" class="btn vertical-button remove-border bg-purple" title="">
                                    <span class="glyph-icon icon-separator-vertical pad0A medium">
                                        <i class="glyph-icon icon-reorder opacity-80 font-size-20"></i>
                                    </span>
                    <span class="button-content">Tables</span>
                </a>
                <a href="javascript:;" class="btn vertical-button remove-border bg-azure" title="">
                                    <span class="glyph-icon icon-separator-vertical pad0A medium">
                                        <i class="glyph-icon icon-bar-chart opacity-80 font-size-20"></i>
                                    </span>
                    <span class="button-content">Charts</span>
                </a>
                <a href="javascript:;" class="btn vertical-button remove-border bg-yellow" title="">
                                    <span class="glyph-icon icon-separator-vertical pad0A medium">
                                        <i class="glyph-icon icon-laptop opacity-80 font-size-20"></i>
                                    </span>
                    <span class="button-content">Buttons</span>
                </a>
                <a href="javascript:;" class="btn vertical-button remove-border bg-orange" title="">
                                    <span class="glyph-icon icon-separator-vertical pad0A medium">
                                        <i class="glyph-icon icon-code opacity-80 font-size-20"></i>
                                    </span>
                    <span class="button-content">Panels</span>
                </a>
            </div>
        </div>
    </div>
</div>
 -->
<div class="top-icon-bar">
<div class="dropdown">

    <a data-toggle="dropdown" href="javascript:;" title="">
        <#--
        <span class="badge badge-absolute bg-blue">8</span>
        -->
        <i class="glyph-icon icon-lightbulb"></i>
    </a>
    <div class="dropdown-menu">

        <div class="small-box">
            <div class="popover-title">布局模式</div>
            <div class="pad10A clearfix">
                <a class="fluid-layout-btn hidden bg-blue-alt medium btn" href="javascript:;" title=""><span class="button-content">全屏模式</span></a>
                <a class="boxed-layout-btn bg-blue-alt medium btn" href="javascript:;" title=""><span class="button-content">窄屏模式</span></a>
            </div>
            <div class="popover-title">页面背景</div>
            <div class="pad10A clearfix">
                <!--
                <a href="javascript:;" class="choose-bg" boxed-bg="#000" style="background: #000;" title=""></a>
                <a href="javascript:;" class="choose-bg" boxed-bg="#333" style="background: #333;" title=""></a>
                <a href="javascript:;" class="choose-bg" boxed-bg="#666" style="background: #666;" title=""></a>
                -->
                <a href="javascript:;" class="choose-bg" boxed-bg="#888" style="background: #888;" title=""></a>
                <!--
                <a href="javascript:;" class="choose-bg" boxed-bg="#383d43" style="background: #383d43;" title=""></a>
                -->
                <a href="javascript:;" class="choose-bg" boxed-bg="#fafafa" style="background: #fafafa; border: #ccc solid 1px;" title=""></a>
                <a href="javascript:;" class="choose-bg" boxed-bg="#fff" style="background: #fff; border: #eee solid 1px;" title=""></a>
            </div>
            <div class="popover-title">颜色方案</div>
            <div class="pad10A clearfix change-layout-theme">
                <p class="font-gray-dark font-size-11 pad0B">更多配色方案正在进行中...</p>
                <div class="divider mrg10T mrg10B"></div>
                <a href="javascript:;" class="choose-theme" layout-theme="dark-blue" title="">
                    <span style="background: #2381E9;"></span>
                </a>
                <a href="javascript:;" class="choose-theme opacity-30" layout-theme="white-blue" title="">
                    <span style="background: #2381E9;"></span>
                </a>
            <#--
            <a href="javascript:;" class="choose-theme" layout-theme="dark-green" title="D">
                <span style="background: #78CE12;"></span>
            </a>
            <a href="javascript:;" class="choose-theme opacity-30 mrg15R" layout-theme="white-green" title="D">
                <span style="background: #78CE12;"></span>
            </a>
            <a href="javascript:;" class="choose-theme" layout-theme="dark-orange" title="">
                <span style="background: #FF6041;"></span>
            </a>
            <a href="javascript:;" class="choose-theme opacity-30 mrg15R" layout-theme="white-orange" title="">
                <span style="background: #FF6041;"></span>
            </a>
            -->
                <a href="javascript:;" class="choose-theme" layout-theme="agile-default" title="">
                    <span style="background: #37485D;"></span>
                </a>
            </div>
            <#--
            <div class="pad10A button-pane button-pane-alt text-center">
                <a href="javascript:;" class="btn medium bg-black">
                    <span class="button-content text-transform-upr font-bold font-size-11">View all</span>
                </a>
            </div>
            -->
        </div>

    </div>
</div>
<div class="dropdown">

    <a data-toggle="dropdown" href="javascript:;" title="">
        <span class="badge badge-absolute bg-orange">4</span>
        <i class="glyph-icon icon-envelope-alt"></i>
    </a>
    <div class="dropdown-menu">

        <div class="scrollable-content medium-box scrollable-small">

            <ul class="no-border messages-box">
                <li>
                    <div class="messages-img">
                        <img width="32" src="${request.contextPath}/assets/images/gravatar.jpg" alt="" />
                    </div>
                    <div class="messages-content">
                        <div class="messages-title">
                            <i class="glyph-icon icon-warning-sign font-red"></i>
                            <a href="javascript:;" title="Message title">Important message</a>
                            <div class="messages-time">
                                3 hr ago
                                <span class="glyph-icon icon-time"></span>
                            </div>
                        </div>
                        <div class="messages-text">
                            This message must be read immediately because of it's high importance...
                        </div>
                    </div>
                </li>
                <li>
                    <div class="messages-img">
                        <img width="32" src="${request.contextPath}/assets/images/gravatar.jpg" alt="" />
                    </div>
                    <div class="messages-content">
                        <div class="messages-title">
                            <i class="glyph-icon icon-tag font-blue"></i>
                            <a href="javascript:;" title="Message title">Some random email</a>
                            <div class="messages-time">
                                3 hr ago
                                <span class="glyph-icon icon-time"></span>
                            </div>
                        </div>
                        <div class="messages-text">
                            This message must be read immediately because of it's high importance...
                        </div>
                    </div>
                </li>
                <li>
                    <div class="messages-img">
                        <img width="32" src="${request.contextPath}/assets/images/gravatar.jpg" alt="" />
                    </div>
                    <div class="messages-content">
                        <div class="messages-title">
                            <a href="javascript:;" class="font-orange" title="Message title">Another received message</a>
                            <div class="messages-time">
                                3 hr ago
                                <span class="glyph-icon icon-time"></span>
                            </div>
                        </div>
                        <div class="messages-text">
                            This message must be read immediately because of it's high importance...
                        </div>
                    </div>
                </li>
                <li>
                    <div class="messages-img">
                        <img width="32" src="${request.contextPath}/assets/images/gravatar.jpg" alt="" />
                    </div>
                    <div class="messages-content">
                        <div class="messages-title">
                            <i class="glyph-icon icon-warning-sign font-red"></i>
                            <a href="javascript:;" title="Message title">Important message</a>
                            <div class="messages-time">
                                3 hr ago
                                <span class="glyph-icon icon-time"></span>
                            </div>
                        </div>
                        <div class="messages-text">
                            This message must be read immediately because of it's high importance...
                        </div>
                    </div>
                </li>
                <li>
                    <div class="messages-img">
                        <img width="32" src="${request.contextPath}/assets/images/gravatar.jpg" alt="" />
                    </div>
                    <div class="messages-content">
                        <div class="messages-title">
                            <i class="glyph-icon icon-tag font-blue"></i>
                            <a href="javascript:;" title="Message title">Some random email</a>
                            <div class="messages-time">
                                3 hr ago
                                <span class="glyph-icon icon-time"></span>
                            </div>
                        </div>
                        <div class="messages-text">
                            This message must be read immediately because of it's high importance...
                        </div>
                    </div>
                </li>
                <li>
                    <div class="messages-img">
                        <img width="32" src="${request.contextPath}/assets/images/gravatar.jpg" alt="" />
                    </div>
                    <div class="messages-content">
                        <div class="messages-title">
                            <a href="javascript:;" class="font-orange" title="Message title">Another received message</a>
                            <div class="messages-time">
                                3 hr ago
                                <span class="glyph-icon icon-time"></span>
                            </div>
                        </div>
                        <div class="messages-text">
                            This message must be read immediately because of it's high importance...
                        </div>
                    </div>
                </li>
            </ul>

        </div>
        <div class="pad10A button-pane button-pane-alt">
            <a href="messaging.html" class="btn small float-left bg-white">
                <span class="button-content text-transform-upr font-size-11">All messages</span>
            </a>
            <div class="button-group float-right">
                <a href="javascript:;" class="btn small primary-bg">
                    <i class="glyph-icon icon-star"></i>
                </a>
                <a href="javascript:;" class="btn small primary-bg">
                    <i class="glyph-icon icon-random"></i>
                </a>
                <a href="javascript:;" class="btn small primary-bg">
                    <i class="glyph-icon icon-map-marker"></i>
                </a>
            </div>
            <a href="javascript:;" class="small btn bg-red float-right mrg10R tooltip-button" data-placement="left" title="Remove comment">
                <i class="glyph-icon icon-remove"></i>
            </a>
        </div>

    </div>
</div>
<div class="dropdown">

    <a data-toggle="dropdown" href="javascript:;" title="">
        <span class="badge badge-absolute bg-green">9</span>
        <i class="glyph-icon icon-bell"></i>
    </a>
    <div class="dropdown-menu">

        <div class="popover-title display-block clearfix form-row pad10A">
            <div class="form-input">
                <div class="form-input-icon">
                    <i class="glyph-icon icon-search transparent"></i>
                    <input type="text" placeholder="Search notifications..." class="radius-all-100" name="" id="" />
                </div>
            </div>
        </div>
        <div class="scrollable-content medium-box scrollable-small">

            <ul class="no-border notifications-box">
                <li>
                    <span class="btn bg-purple icon-notification glyph-icon icon-user"></span>
                    <span class="notification-text">This is an error notification</span>
                    <div class="notification-time">
                        a few seconds ago
                        <span class="glyph-icon icon-time"></span>
                    </div>
                </li>
                <li>
                    <span class="btn bg-orange icon-notification glyph-icon icon-user"></span>
                    <span class="notification-text">This is a warning notification</span>
                    <div class="notification-time">
                        <b>15</b> minutes ago
                        <span class="glyph-icon icon-time"></span>
                    </div>
                </li>
                <li>
                    <span class="bg-green btn icon-notification glyph-icon icon-user"></span>
                    <span class="notification-text font-green font-bold">A success message example.</span>
                    <div class="notification-time">
                        <b>2 hours</b> ago
                        <span class="glyph-icon icon-time"></span>
                    </div>
                </li>
                <li>
                    <span class="btn bg-purple icon-notification glyph-icon icon-user"></span>
                    <span class="notification-text">This is an error notification</span>
                    <div class="notification-time">
                        a few seconds ago
                        <span class="glyph-icon icon-time"></span>
                    </div>
                </li>
                <li>
                    <span class="btn bg-orange icon-notification glyph-icon icon-user"></span>
                    <span class="notification-text">This is a warning notification</span>
                    <div class="notification-time">
                        <b>15</b> minutes ago
                        <span class="glyph-icon icon-time"></span>
                    </div>
                </li>
                <li>
                    <span class="bg-blue btn icon-notification glyph-icon icon-user"></span>
                    <span class="notification-text font-blue">Alternate notification styling.</span>
                    <div class="notification-time">
                        <b>2 hours</b> ago
                        <span class="glyph-icon icon-time"></span>
                    </div>
                </li>
            </ul>

        </div>
        <div class="pad10A button-pane button-pane-alt text-center">
            <a href="notifications.html" class="btn medium primary-bg">
                <span class="button-content">View all notifications</span>
            </a>
        </div>

    </div>
</div>
<#--
<div class="dropdown">

    <a data-toggle="dropdown" href="javascript:;" title="">
        <span class="badge badge-absolute bg-red">2</span>
        <i class="glyph-icon icon-tasks"></i>
    </a>
    <div class="dropdown-menu" id="progress-dropdown">

        <div class="scrollable-content small-box scrollable-small">

            <ul class="no-border progress-box">
                <li>
                    <div class="progress-title">
                        Finishing uploading files
                        <b>23%</b>
                    </div>
                    <div class="progressbar-small progressbar" data-value="23">
                        <div class="progressbar-value bg-blue">
                            <div class="progressbar-overlay"></div>
                        </div>
                    </div>
                </li>
                <li>
                    <div class="progress-title">
                        Roadmap progress
                        <b>91%</b>
                    </div>
                    <div class="progressbar-small progressbar" data-value="91">
                        <div class="progressbar-value primary-bg">
                            <div class="progressbar-overlay"></div>
                        </div>
                    </div>
                </li>
                <li>
                    <div class="progress-title">
                        Images upload
                        <b>58%</b>
                    </div>
                    <div class="progressbar-small progressbar" data-value="58">
                        <div class="progressbar-value bg-blue-alt"></div>
                    </div>
                </li>
                <li>
                    <div class="progress-title">
                        WordPress migration
                        <b>74%</b>
                    </div>
                    <div class="progressbar-small progressbar" data-value="74">
                        <div class="progressbar-value bg-purple"></div>
                    </div>
                </li>
                <li>
                    <div class="progress-title">
                        Agile development procedures
                        <b>91%</b>
                    </div>
                    <div class="progressbar-small progressbar" data-value="91">
                        <div class="progressbar-value primary-bg">
                            <div class="progressbar-overlay"></div>
                        </div>
                    </div>
                </li>
                <li>
                    <div class="progress-title">
                        Systems integration
                        <b>58%</b>
                    </div>
                    <div class="progressbar-small progressbar" data-value="58">
                        <div class="progressbar-value bg-blue-alt"></div>
                    </div>
                </li>
                <li>
                    <div class="progress-title">
                        Code optimizations
                        <b>97%</b>
                    </div>
                    <div class="progressbar-small progressbar" data-value="97">
                        <div class="progressbar-value bg-yellow"></div>
                    </div>
                </li>
            </ul>

        </div>
        <div class="pad10A button-pane button-pane-alt text-center">
            <a href="notifications.html" class="btn medium font-normal bg-green">
                <span class="button-content">View all</span>
            </a>
        </div>

    </div>
</div>
-->
</div>

</div><!-- #page-header -->

<div id="page-sidebar" class="scrollable-content">

<div id="sidebar-menu">
<ul>
    <@s.iterator value="#adminUser.menus" var="menu">
    <@s.if test="#menu.type.toString() == 'url'">
        <@s.set name="menuUrl" value="@com.fantasy.framework.util.regexp.RegexpUtil@replaceFirst(#menu.value,'%\\\\{contextPath\\\\}',@org.apache.struts2.ServletActionContext@getRequest().getContextPath())"/>
        <@s.set name="menuUrl" value="#menuUrl == '' ? 'javascript:;' : #menuUrl "/>
    </@s.if>
    <@s.else>
        <@s.set name="menuUrl" value="'javascript:;'"/>
    </@s.else>
    <li>
        <a title="<@s.property value="#menu.name" />" href="<@s.property value="#menuUrl" />" <@s.if test=" 'javascript:;' != #menuUrl "> target="top:#page-content-wrapper"</@s.if>>
            <i class="glyph-icon <@s.property value="#menu.icon" />"></i>
            <@s.property value="#menu.name" />
        </a>
        <@s.if test="#menu.children.size() > 0 ">
            <ul>
                <@s.iterator value="#menu.children" var="cmenu">
                    <@s.set name="cmenuUrl" value="@com.fantasy.framework.util.regexp.RegexpUtil@replaceFirst(#cmenu.value,'%\\\\{contextPath\\\\}',@org.apache.struts2.ServletActionContext@getRequest().getContextPath())"/>
                    <@s.set name="cmenuUrl" value="#cmenuUrl == '' ? 'javascript:;' : #cmenuUrl "/>
                    <li>
                        <a title="<@s.property value="#cmenu.name" />" href="<@s.property value="#cmenuUrl" />" target="top:#page-content-wrapper">
                            <i class="glyph-icon <@s.property value="#cmenu.icon" />"></i>
                            <@s.property value="#cmenu.name" />
                        </a>
                    </li>
                </@s.iterator>
            </ul>
        </@s.if>
    </li>
    </@s.iterator>
<li>
    <a href="javascript:;" title="Help &amp; Support">
        <i class="glyph-icon icon-book"></i>
        帮助与技术支持
    </a>
    <ul>
        <li>
            <a href="docs.html" title="Getting started guide">
                <i class="glyph-icon icon-chevron-right"></i>
                Getting started guide
            </a>
        </li>
        <li>
            <a href="http://agileui.com/support-forums" title="Support forums">
                <i class="glyph-icon icon-chevron-right"></i>
                Support forums
            </a>
        </li>

    </ul>
</li>
</ul>
<div class="divider mrg5T mobile-hidden"></div>
<div class="text-center mobile-hidden">
    <div class="button-group display-inline">
        <a href="javascript:;" class="btn medium bg-green tooltip-button" data-placement="top" title="Messages">
            <i class="glyph-icon icon-flag"></i>
        </a>
        <a href="javascript:;" class="btn medium bg-green tooltip-button" data-placement="top" title="Mailbox">
            <i class="glyph-icon icon-inbox"></i>
        </a>
        <a href="javascript:;" class="btn medium bg-green tooltip-button" data-placement="top" title="Content">
            <i class="glyph-icon icon-hdd"></i>
        </a>
    </div>

</div>
</div>

</div>
<!-- #page-sidebar -->
<div id="page-content-wrapper">
<@block name="pageContentWrapper"></@block>
</div>
<!-- #page-main -->
</div><!-- #page-wrapper -->

</body>
</html>