<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<!DOCTYPE html>
<html>
<head>

    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <title>Fides Admin</title>
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>

    <!-- Favicons -->

    <link rel="apple-touch-icon-precomposed" sizes="144x144"
          href="assets/images/icons/apple-touch-icon-144-precomposed.png"/>
    <link rel="apple-touch-icon-precomposed" sizes="114x114"
          href="assets/images/icons/apple-touch-icon-114-precomposed.png"/>
    <link rel="apple-touch-icon-precomposed" sizes="72x72"
          href="assets/images/icons/apple-touch-icon-72-precomposed.png"/>
    <link rel="apple-touch-icon-precomposed" href="assets/images/icons/apple-touch-icon-57-precomposed.png"/>
    <link rel="shortcut icon" href="assets/images/icons/favicon.png"/>

    <!--[if lt IE 9]>
    <script src="assets/js/minified/core/html5shiv.min.js"></script>
    <script src="assets/js/minified/core/respond.min.js"></script>
    <![endif]-->

    <!-- Fides Admin CSS Core -->

    <link rel="stylesheet" type="text/css" href="assets/css/minified/aui-production.min.css"/>

    <!-- Theme UI -->

    <link id="layout-theme" rel="stylesheet" type="text/css"
          href="assets/themes/minified/fides/color-schemes/dark-blue.min.css"/>

    <!-- Fides Admin Responsive -->

    <link rel="stylesheet" type="text/css" href="assets/themes/minified/fides/common.min.css"/>
    <link rel="stylesheet" type="text/css" href="assets/themes/minified/fides/responsive.min.css"/>

    <!-- Fides Admin JS -->

    <script type="text/javascript" src="assets/js/minified/aui-production.js"></script>
    <script type="text/javascript" src="${request.contextPath}/assets/js/common.js"></script>
    <script>
        jQuery(window).load(
                function () {

                    var wait_loading = window.setTimeout(function () {
                                $('#loading').slideUp('fast');
                                jQuery('body').css('overflow', 'auto');
                            }, 1000
                    );

                });
        function add_loader(){

        };
        function remove_loader(){

        };
    </script>
    <script type="text/javascript">
        $(function () {
            $('input[name=username]').change(function () {
                Fantasy.Cookie.put('admin_login_username', $(this).val());
            }).val(Fantasy.Cookie.get('admin_login_username'));
            $('input[name=captcha]').keypress(function (event) {
                if (event.keyCode == '13') {
                    $('#loginform').submit();
                    return false;
                }
            });
            $('#refreshCaptcha').click(function () {
                $(this).hide().attr('src', '${request.contextPath}/jcaptcha.jpg' + '?' + new Date().getTime()).fadeIn();
            });


            $('#resetpwd').click(function(){
                if($('#email').val()==''){
                    $.msgbox({
                        msg: "请填写邮箱",
                        type: "warning"
                    });
                    return  false;
                }
                $.post('<@s.url namespace="/findpwd" action="retrievePassword"/>',{email:$('#email').val()},function(data){
                    alert(data.massige);
                });
            });
        });
    </script>

    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body style="overflow: hidden;">


<div id="loading" class="ui-front loader ui-widget-overlay bg-white opacity-100">
    <img src="assets/images/loader-dark.gif" alt=""/>
</div>

<div id="login-page" class="mrg25B">


<div class="theme-customizer">
    <a href="javascript:;" class="change-theme-btn" title="Change theme">
        <i class="glyph-icon icon-cog"></i>
    </a>

    <div class="theme-wrapper">

        <div class="popover-title">Boxed layout options</div>
        <div class="pad10A clearfix">
            <a class="fluid-layout-btn hidden bg-blue-alt medium btn" href="javascript:;" title=""><span
                    class="button-content">Full width layout</span></a>
            <a class="boxed-layout-btn bg-blue-alt medium btn" href="javascript:;" title=""><span
                    class="button-content">Boxed layout</span></a>
        </div>
        <div class="popover-title">Boxed backgrounds</div>
        <div class="pad10A clearfix">
            <a href="javascript:;" class="choose-bg" boxed-bg="#000" style="background: #000;" title=""></a>
            <a href="javascript:;" class="choose-bg" boxed-bg="#333" style="background: #333;" title=""></a>
            <a href="javascript:;" class="choose-bg" boxed-bg="#666" style="background: #666;" title=""></a>
            <a href="javascript:;" class="choose-bg" boxed-bg="#888" style="background: #888;" title=""></a>
            <a href="javascript:;" class="choose-bg" boxed-bg="#383d43" style="background: #383d43;" title=""></a>
            <a href="javascript:;" class="choose-bg" boxed-bg="#fafafa"
               style="background: #fafafa; border: #ccc solid 1px;" title=""></a>
            <a href="javascript:;" class="choose-bg" boxed-bg="#fff" style="background: #fff; border: #eee solid 1px;"
               title=""></a>
        </div>
        <div class="popover-title">Color schemes</div>
        <div class="pad10A clearfix change-layout-theme">
            <p class="font-gray-dark font-size-11 pad0B">More color schemes will be available soon!</p>

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

<div id="page-header" class="clearfix">
<div id="header-logo">
    <a href="javascript:;" class="tooltip-button" data-placement="bottom" title="Close sidebar" id="close-sidebar">
        <i class="glyph-icon icon-caret-left"></i>
    </a>
    <a href="javascript:;" class="tooltip-button hidden" data-placement="bottom" title="Open sidebar"
       id="rm-close-sidebar">
        <i class="glyph-icon icon-caret-right"></i>
    </a>
    <a href="javascript:;" class="tooltip-button hidden" title="Navigation Menu" id="responsive-open-menu">
        <i class="glyph-icon icon-align-justify"></i>
    </a>
    Fides Admin <i class="opacity-80">1.0</i>
</div>
<div class="hide" id="black-modal-60" title="Modal window example">
    <div class="pad20A">

        <div class="infobox notice-bg">
            <div class="bg-azure large btn info-icon">
                <i class="glyph-icon icon-bullhorn"></i>
            </div>
            <h4 class="infobox-title">Modal windows</h4>

            <p>Thanks to the solid modular Fides Admin arhitecture, modal windows customizations are very flexible and
                easy to apply.</p>
        </div>

        <h4 class="heading-1 mrg20T clearfix">
            <div class="heading-content" style="width: auto;">
                Icons
                <small>
                    All icons across the Fides Admin Framework use FontAwesome icons.
                </small>
            </div>
            <div class="clear"></div>
            <div class="divider"></div>
        </h4>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-compass"
           href="../icon/compass"><i class="glyph-icon icon-compass"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-collapse"
           href="../icon/collapse"><i class="glyph-icon icon-collapse"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-collapse-top"
           href="../icon/collapse-top"><i class="glyph-icon icon-collapse-top"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-expand"
           href="../icon/expand"><i class="glyph-icon icon-expand"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-eur" href="../icon/eur"><i
                class="glyph-icon icon-eur"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-euro" href="../icon/eur"><i
                class="glyph-icon icon-euro"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-gbp" href="javascript:;"><i
                class="glyph-icon icon-gbp"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-usd" href="javascript:;"><i
                class="glyph-icon icon-usd"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-dollar"
           href="javascript:;"><i class="glyph-icon icon-dollar"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-inr" href="javascript:;"><i
                class="glyph-icon icon-inr"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-rupee" href="javascript:;"><i
                class="glyph-icon icon-rupee"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-jpy" href="javascript:;"><i
                class="glyph-icon icon-jpy"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-yen" href="javascript:;"><i
                class="glyph-icon icon-yen"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-cny" href="javascript:;"><i
                class="glyph-icon icon-cny"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-renminbi"
           href="javascript:;"><i class="glyph-icon icon-renminbi"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-krw" href="javascript:;"><i
                class="glyph-icon icon-krw"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-won" href="javascript:;"><i
                class="glyph-icon icon-won"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-btc" href="javascript:;"><i
                class="glyph-icon icon-btc"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-bitcoin"
           href="javascript:;"><i class="glyph-icon icon-bitcoin"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-file"
           href="javascript:;"><i class="glyph-icon icon-file"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-file-text"
           href="javascript:;"><i class="glyph-icon icon-file-text"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-sort-by-alphabet"
           href="javascript:;"><i class="glyph-icon icon-sort-by-alphabet"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-sort-by-alphabet-al"
           href="javascript:;"><i class="glyph-icon icon-sort-by-alphabet-alt"></i>t</a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-sort-by-attributes"
           href="javascript:;"><i class="glyph-icon icon-sort-by-attributes"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-sort-by-attribu"
           href="javascript:;"><i class="glyph-icon icon-sort-by-attributes-alt"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-sort-by-order"
           href="javascript:;"><i class="glyph-icon icon-sort-by-order"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-sort-by-order-alt"
           href="javascript:;"><i class="glyph-icon icon-sort-by-order-alt"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-thumbs-up"
           href="javascript:;"><i class="glyph-icon icon-thumbs-up"></i></a>

        <a class="btn medium radius-all-4 mrg5A ui-state-default tooltip-button" title="icon-thumbs-down"
           href="javascript:;"><i class="glyph-icon icon-thumbs-down"></i></a>

    </div>
</div>

<div class="hide" id="white-modal-80" title="Dialog with tabs">
    <div class="tabs pad15A remove-border opacity-80">
        <ul class="opacity-80">
            <li><a href="#example-tabs-1">First</a></li>
            <li><a href="#example-tabs-2">Second</a></li>
            <li><a href="#example-tabs-3">Third</a></li>
        </ul>
        <div id="example-tabs-1">
            <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et
                dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip
                ex ea commodo consequat.
            </p>

            <p>Nam dui erat, auctor a, dignissim quis, sollicitudin eu, felis. Pellentesque nisi urna, interdum eget,
                sagittis et, consequat vestibulum, lacus. Mauris porttitor ullamcorper augue.
            </p>
        </div>
        <div id="example-tabs-2">
            <p>Phasellus mattis tincidunt nibh. Cras orci urna, blandit id, pretium vel, aliquet ornare, felis. Maecenas
                scelerisque sem non nisl. Fusce sed lorem in enim dictum bibendum.
            </p>

            <p>Nam dui erat, auctor a, dignissim quis, sollicitudin eu, felis. Pellentesque nisi urna, interdum eget,
                sagittis et, consequat vestibulum, lacus. Mauris porttitor ullamcorper augue.
            </p>
        </div>
        <div id="example-tabs-3">
            <p>Nam dui erat, auctor a, dignissim quis, sollicitudin eu, felis. Pellentesque nisi urna, interdum eget,
                sagittis et, consequat vestibulum, lacus. Mauris porttitor ullamcorper augue.
            </p>

            <p>Nam dui erat, auctor a, dignissim quis, sollicitudin eu, felis. Pellentesque nisi urna, interdum eget,
                sagittis et, consequat vestibulum, lacus. Mauris porttitor ullamcorper augue.
            </p>
        </div>
    </div>
    <div class="pad10A">
        <div class="infobox success-bg radius-all-4">
            <p>Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam
                rem aperiam, eaque</p>
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
        <a href="javascript:;" class="medium btn bg-blue-alt float-right mrg10R tooltip-button" data-placement="left"
           title="Remove comment">
            <i class="glyph-icon icon-plus"></i>
        </a>

    </div>
</div>
<div class="user-profile dropdown">
    <a href="javascript:;" title="" class="user-ico clearfix" data-toggle="dropdown">
        <img width="36" src="assets/images/gravatar.jpg" alt=""/>
        <span>Horia Simon</span>
        <i class="glyph-icon icon-chevron-down"></i>
    </a>
    <ul class="dropdown-menu float-right">
        <li>
            <a href="javascript:;" title="">
                <i class="glyph-icon icon-user mrg5R"></i>
                Account Details
            </a>
        </li>
        <li>
            <a href="javascript:;" title="">
                <i class="glyph-icon icon-cog mrg5R"></i>
                Edit Profile
            </a>
        </li>
        <li>
            <a href="javascript:;" title="">
                <i class="glyph-icon icon-flag mrg5R"></i>
                Notifications
            </a>
        </li>
        <li>
            <a href="javascript:;" title="">
                <i class="glyph-icon icon-signout font-size-13 mrg5R"></i>
                <span class="font-bold">Logout</span>
            </a>
        </li>
        <li class="divider"></li>
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

    </ul>
</div>
<div class="dropdown dash-menu">
    <a href="javascript:;" data-toggle="dropdown" data-placement="left"
       class="medium btn primary-bg float-right popover-button-header hidden-mobile tooltip-button"
       title="Example menu">
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
<div class="top-icon-bar">
<div class="dropdown">

    <a data-toggle="dropdown" href="javascript:;" title="">
        <span class="badge badge-absolute bg-blue">8</span>
        <i class="glyph-icon icon-lightbulb"></i>
    </a>

    <div class="dropdown-menu">

        <div class="small-box">
            <div class="popover-title">Boxed layout options</div>
            <div class="pad10A clearfix">
                <a class="fluid-layout-btn hidden bg-blue-alt medium btn" href="javascript:;" title=""><span
                        class="button-content">Full width layout</span></a>
                <a class="boxed-layout-btn bg-blue-alt medium btn" href="javascript:;" title=""><span
                        class="button-content">Boxed layout</span></a>
            </div>
            <div class="popover-title">Boxed backgrounds</div>
            <div class="pad10A clearfix">
                <a href="javascript:;" class="choose-bg" boxed-bg="#000" style="background: #000;" title=""></a>
                <a href="javascript:;" class="choose-bg" boxed-bg="#333" style="background: #333;" title=""></a>
                <a href="javascript:;" class="choose-bg" boxed-bg="#666" style="background: #666;" title=""></a>
                <a href="javascript:;" class="choose-bg" boxed-bg="#888" style="background: #888;" title=""></a>
                <a href="javascript:;" class="choose-bg" boxed-bg="#383d43" style="background: #383d43;" title=""></a>
                <a href="javascript:;" class="choose-bg" boxed-bg="#fafafa"
                   style="background: #fafafa; border: #ccc solid 1px;" title=""></a>
                <a href="javascript:;" class="choose-bg" boxed-bg="#fff"
                   style="background: #fff; border: #eee solid 1px;" title=""></a>
            </div>
            <div class="popover-title">Color schemes</div>
            <div class="pad10A clearfix change-layout-theme">
                <p class="font-gray-dark font-size-11 pad0B">More color schemes will be available soon!</p>

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

            <div class="pad10A button-pane button-pane-alt text-center">
                <a href="javascript:;" class="btn medium bg-black">
                    <span class="button-content text-transform-upr font-bold font-size-11">View all</span>
                </a>
            </div>
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
                        <img width="32" src="assets/images/gravatar.jpg" alt=""/>
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
                        <img width="32" src="assets/images/gravatar.jpg" alt=""/>
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
                        <img width="32" src="assets/images/gravatar.jpg" alt=""/>
                    </div>
                    <div class="messages-content">
                        <div class="messages-title">
                            <a href="javascript:;" class="font-orange" title="Message title">Another received
                                message</a>

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
                        <img width="32" src="assets/images/gravatar.jpg" alt=""/>
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
                        <img width="32" src="assets/images/gravatar.jpg" alt=""/>
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
                        <img width="32" src="assets/images/gravatar.jpg" alt=""/>
                    </div>
                    <div class="messages-content">
                        <div class="messages-title">
                            <a href="javascript:;" class="font-orange" title="Message title">Another received
                                message</a>

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
            <a href="javascript:;" class="small btn bg-red float-right mrg10R tooltip-button" data-placement="left"
               title="Remove comment">
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
                    <input type="text" placeholder="Search notifications..." class="radius-all-100" name="" id=""/>
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
</div>

</div>
<!-- #page-header -->

</div>
<img src="assets/images/login-bg.png" class="login-img" alt=""/>

<div class="ui-widget-overlay bg-black opacity-60"></div>
<div class="pad20A mrg25T">
    <div class="row mrg25T">
        <form id="loginform" class="col-md-4 center-margin form-vertical mrg25T" action="${request.contextPath}/login<@s.if test="#session['SPRING_SECURITY_SAVED_REQUEST']==null">?url=/index.do</@s.if>" method="post">
            <div class="ui-dialog modal-dialog mrg25T" id="login-form" style="position: relative !important;">
                <div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">
                    <span class="ui-dialog-title">用户登陆 </span>
                </div>
                <div class="pad20A pad0B ui-dialog-content ui-widget-content">
                    <@s.if test="#session['SPRING_SECURITY_LAST_EXCEPTION'].message!=''">
                        <div class="infobox clearfix infobox-close-wrapper error-bg mrg20B">
                            <a href="#" title="Close Message" class="glyph-icon infobox-close icon-remove"></a>
                            <p><@s.property value="#session['SPRING_SECURITY_LAST_EXCEPTION'].message" /></p>
                        </div>
                    </@s.if>
                    <div class="form-row">
                        <div class="form-label col-md-2">
                            <label for="">
                                用户名:
                            </label>
                        </div>
                        <div class="form-input col-md-10">
                            <div class="form-input-icon">
                                <i class="glyph-icon icon-envelope-alt ui-state-default"></i>
                                <input tabindex="1" name="username" type="text" placeholder="Email address"
                                       autocomplete="off"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-label col-md-2">
                            <label for="">
                                密 码:
                            </label>
                        </div>
                        <div class="form-input col-md-10">
                            <div class="form-input-icon">
                                <i class="glyph-icon icon-unlock-alt ui-state-default"></i>
                                <input tabindex="2" placeholder="Password" type="password" name="password"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-checkbox-radio col-md-6">
                            <input type="checkbox" class="custom-checkbox" name="rememberme" id="remember-password"/>
                            <label for="remember-password" class="pad5L">记住我?</label>
                        </div>
                        <div class="form-checkbox-radio text-right col-md-6">
                            <a href="#" class="toggle-switch" switch-target="#login-forgot" switch-parent="#login-form"
                               title="Recover password">忘记密码?</a>
                        </div>
                    </div>
                </div>
                <div class="ui-dialog-buttonpane text-center">
                    <button type="submit"
                            class="btn large primary-bg text-transform-upr font-bold font-size-11 radius-all-4"
                            id="demo-form-valid" title="Validate!">
                                <span class="button-content">
                                    登陆
                                </span>
                    </button>
                </div>
            </div>
            <div class="ui-dialog mrg15T hide" id="login-forgot" style="position: relative !important;">
                <div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">
                    <span class="ui-dialog-title">找回密码</span>
                </div>
                <div class="pad20A ui-dialog-content ui-widget-content">
                    <div class="form-row">
                        <div class="form-label col-md-2">
                            <label for="">
                                电子邮箱:
                            </label>
                        </div>
                        <div class="form-input col-md-10">
                            <div class="form-input-icon">
                                <i class="glyph-icon icon-envelope-alt ui-state-default"></i>
                                <input placeholder="Email address" type="text" name="email" id="email"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="ui-dialog-buttonpane text-center">
                    <a href="javascript:void(0);"  class="btn large primary-bg" id="resetpwd">
                            <span class="button-content">
                                找回密码
                            </span>
                    </a>
                    <a href="javascript:;" switch-target="#login-form" switch-parent="#login-forgot" class="btn large transparent no-shadow toggle-switch font-bold font-size-11 radius-all-4" id="demo-form-valid" onclick="javascript:$(&apos;#demo-form&apos;).parsley( &apos;validate&apos; );" title="Validate!">
                            <span class="button-content">
                                取消
                            </span>
                    </a>
                </div>
            </div>

        </form>

    </div>

</div>


<div id="page-footer-wrapper" class="login-footer">
    <div id="page-footer">
        Copyright &copy; 2013 - Fides Admin
    </div>
</div>
<!-- #page-footer-wrapper -->

</body>
</html>
