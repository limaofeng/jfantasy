jQuery.extend({

    dialog:{
        confirm:function(msg,okFun){
            var $content = $('<div class="hide" id="white-modal-80" title="消息"><div class="pad10A"><div class="infobox bg-white radius-all-4"><div class="primary-bg large btn info-icon"><i class="glyph-icon icon-question"></i></div></div></div><div class="ui-dialog-buttonpane clearfix"><div class="float-right"><a href="javascript:;" class="btn medium primary-bg mrg10R ok"><span class="button-content">确认</span></a><a href="javascript:;" class="btn medium bg-gray font-gray-dark mrg10R cancel"><span class="button-content">取消</span></a></div></div></div>');
            $('.info-icon',$content).after(msg);
            $content.find('.cancel').click(function(e){
                $content.dialog("close");
                return e.stopPropagation();
            });
            $content.find('.ok').click(function(e){
                try {
                    okFun.call();
                }finally{
                    $content.dialog("close");
                }
                return e.stopPropagation();
            });
            $($content).dialog({modal: !0, minWidth: 250, minHeight: 150, dialogClass: "modal-dialog", show: "fadeIn"});
            $(".ui-widget-overlay").addClass("bg-white opacity-80");
        }
    }

});
$(function () {
    //---------------------------------------------------------------------------------------------
//	if ($("a#totop").length){ jQuery.scrollToTop("a#totop"); }
    //---------------------------------------------------------------------------------------------对话框全局配置
//	Fantasy.copy(artDialog.defaults,{lock:true});
    //---------------------------------------------------------------------------------------------ajax加载进度
    /*
     Fantasy.Ajax.on('send',function(event, xhr, s){
     if (!(s.data && (Fantasy.isString(s.data)?Fantasy.parseQuery(s.data):s.data)['proFlag'] == 'false')) {
     s.progress = top.jQuery.progress().start();
     }
     });
     Fantasy.Ajax.on('complete',function(event, xhr, s){
     if (!(s.data && (Fantasy.isString(s.data)?Fantasy.parseQuery(s.data):s.data)['proFlag'] == 'false')) {
     if(s.progress){
     s.progress.stop();
     }
     }
     });
     */
    //---------------------------------------------------------------------------------------------翻页标签配置
    Fantasy.apply(Fantasy.awt.Pager, {}, {
        getArray: function (totalPage, currentPage, pageNumber) {
            var arraylist = [];
            // 循环产生页码 产生数量最多为11个 前1个 4 《当前页》4 后一个
            for (var i = 1, size = 1; i <= totalPage && size <= (pageNumber * 2 + 1); i++) {
                if ((currentPage > totalPage - (pageNumber + 1) && i + (pageNumber * 2 + 1) > totalPage)// 当前页在前5位
                    || (i > currentPage - (pageNumber + 1) && i < currentPage)) {
                    arraylist.push(i);
                    size++;
                } else if (i == currentPage) {// 添加当前页
                    arraylist.push(i);
                    size++;
                } else if (i > currentPage && i < currentPage + (pageNumber + 1) && size < (pageNumber * 2 + 1)) {// 当前页在后5位
                    arraylist.push(i);
                    size++;
                } else if (size > (pageNumber + 1) && size <= (pageNumber * 2 + 1)) {
                    arraylist.push(i);
                    size++;
                }
            }
            return arraylist;
        },
        template: $.Sweet('<a class="first ui-corner-tl ui-corner-bl fg-button ui-button ui-state-default <[if(currentPage == 1){]>ui-state-disabled<[}]>" href="?page=1" tabindex="0"><i class="icon-caret-left"></i></a>' +
            '<a href="?page=<[=currentPage<=1?1:currentPage-1]>" class="previous fg-button ui-button ui-state-default <[if(currentPage == 1){]>ui-state-disabled<[}]>" tabindex="0"><i class="icon-angle-left"></i></a>' +
            '<[var array = Fantasy.awt.Pager.getArray(totalPage, currentPage, 4);' +
            'foreach( array as item){]>' +
            '<[if(currentPage == item){]>' +
            '<span><a class="fg-button ui-button ui-state-default ui-state-disabled" href="?page=<[=item]>" tabindex="0"><[=item]></a>' +
            '<[}else{]>'+
            '<a class="fg-button ui-button ui-state-default" tabindex="0" href="?page=<[=item]>"><[=item]></a>' +
            '<[}}]>' +
            '<a href="?page=<[=currentPage==totalPage?totalPage:currentPage+1]>" class="next fg-button ui-button ui-state-default <[if(currentPage == totalPage){]>ui-state-disabled<[}]>" tabindex="0"><i class="icon-angle-right"></i></a>' +
            '<a href="?page=<[=totalPage]>" class="last ui-corner-tr ui-corner-br fg-button ui-button ui-state-default <[if(currentPage == totalPage){]>ui-state-disabled<[}]>" tabindex="0"><i class="icon-caret-right"></i></a>')
    });
    //---------------------------------------------------------------------------------------------初始化方法配置
    var initializes = [];
    var _initialize = function () {
        var context = this instanceof jQuery ? this : $(this);

        var contexts = !arguments.callee.contexts ? arguments.callee.contexts = [] : arguments.callee.contexts;
        contexts.insert(0, context);
        //TODO contexts = [context]
        initializes.each(function (index) {
            this.apply(context, [context]);
        });
    };
    var get$ = function (expr, z, $context) {//支持jquery表达式
        //console.log($(expr, $context));
        return (/[)]$/.test(expr) ? eval('(z.' + expr + ')') : $(expr, $context));/*.filter(function(){
            var ajaxLoadDiv = $(this).closest('.ajax-load-div');
            return !(ajaxLoadDiv.length != 0 && ajaxLoadDiv[0] != $context[0]);
        });*/
    };
    /*
     initializes.push((function() {
     SyntaxHighlighter.config.clipboardSwf = request.getContextPath() + '/static/js/common/syntax/clipboard.swf';
     var execute = false;
     return function() {
     if (execute) {
     return execute = true;
     }
     $(".source", this).each(function() {
     if ($(this).data('_source'))
     return;
     $(this).data('_source', true);
     $(this).after($('<div class="code"><pre class="brush:' + ($(this).is("script") ? 'js' : 'xml') + ';">' + $(this).html().replace(/</g, '&lt;').replace(/>/g, '&gt;') + '</pre></div>'));
     });
     SyntaxHighlighter.all();
     };
     })());*/
    initializes.push(function () {
        $('.number', this).each(function (index, zhis) {
            if ($(zhis).data('_number')){
                return;
            }
            var format = $(zhis).attr('format');
            var setVal = function(val){
                val = val.replace(/[^\d.]/g,'');
                $(zhis).val(!!format?Fantasy.number(val,format):val);
            };
            $(zhis).data('_number', true).keyup(function(){
                setVal(this.value);
            }).bind("afterpaste",function(){
                setVal(this.value);
            });
        });
    });
    //---------------------------------------------------------------------------------------------加载日期控件
    initializes.push(function () {
        $('[date]', this).each(function (index, zhis) {
            if ($(zhis).data('_date'))
                return;
            $(zhis).data('_date', true);
            $(zhis).addClass('Wdate');
            //$(zhis).attr('readonly', true);
            var jsonStr = $(zhis).attr('date').replace(/\$\([^(^)]+\).val\(\)/g, function () {
                return '"#F{' + arguments[0] + '}"';//'new Function("return " + "' +  + ';")';
            });
            var settings = Fantasy.merge(eval('(' + jsonStr + ')') || {}, {
                maxDate: '2099-10-01',
                minDate: '1900-01-01',
                dateFmt: 'yyyy-MM-dd',
                skin: 'ext'
            });
            if (/ipad/.test(jQuery.userAgent)) {
                var fmt = settings.dateFmt.split(' ')[0];
                var timeWheels = settings.dateFmt.split(' ').length > 1 ? settings.dateFmt.split(' ')[1] : null;
                var _setting = {
                    dateFormat: fmt.replaceAll('MM', 'mm'),
                    theme: 'ios',
                    mode: 'scroller',
                    endYear: new Date().getFullYear() + 100
                };
                _setting.preset = timeWheels ? 'datetime' : 'date';
                _setting.dateOrder = fmt;
                if (timeWheels) {
                    _setting.timeWheels = timeWheels.replaceAll('mm', 'ii');// 'HHii';
                    _setting.timeFormat = timeWheels.replaceAll('mm', 'ii');
                }
                $(zhis).scroller(_setting);
                $(zhis).click(function () {
                    $(zhis).scroller('show');
                });
            } else {
                $(zhis).data('date_settings', settings).click(function () {
                    WdatePicker(settings);
                });
            }
        });
    });
    //---------------------------------------------------------------------------------------------jquery.select3.js扩展操作
    initializes.push(function () {
        if($.browser.msie){
            return;
        }
        $('select[href]', this).each(function (index, zhis) {
            if ($(zhis).data('_select'))
                return;
            $(zhis).data('_select', true);
            var url = $(zhis).attr("href");
            var select = $(zhis).select(eval('(' + $(this).attr('mapping') + ')'));
            $.post(url, function (data) {
                select.load(data);
            });
        });
    });
    //---------------------------------------------------------------------------------------------uniform 效果
    initializes.push(function () {
        $('input[type="checkbox"].custom-checkbox,input[type="radio"].custom-radio,.custom-select', this).filter(function(){
            return !$(this).data('_uniform');
        }).data('_uniform',true).each(function () {
            if ($(this).closest('.checker > span').length > 0) {
                $(this).unwrap().unwrap().next('i').remove();
            }
            var $checker = $(this).uniform().closest('.checker');
            var $radio = $(this).uniform().closest('.radio');
            $("span",$checker).append('<i class="glyph-icon icon-ok"></i>').addClass("ui-state-default btn radius-all-4");
            $("span",$radio).append('<i class="glyph-icon icon-circle"></i>').addClass("ui-state-default btn radius-all-100");
            if ($(this).is('[type=checkbox]')) {
                var bgColorClass = $(this).prop('class').split(' ').each(function(){
                    if(this.startsWith('bg-')){
                        return this;
                    }
                });
                if(!!bgColorClass){
                    $("span",$checker).addClass(bgColorClass.toString());
                }
                $(this).change(function () {
                    if ($(this).is(":checked")) {
                        $(this).closest('.checker > span').addClass('checked');
                    } else {
                        $(this).closest('.checker > span').removeClass('checked');
                    }
                });
            }
        });
    });
    //---------------------------------------------------------------------------------------------select2插件
    /*
    initializes.push(function () {
        if($.browser.msie){
            return;
        }
        $('select.chosen-select', this).filter(function(){
            return !$(this).data('_chosen_select');
        }).data('_chosen_select',true).each(function () {
            if ($(this).data('_select2'))
                return;
            var settings = {};
            if ($(this).width() != 0) {
                settings.width = $(this).width() + 'px';
            }
            $(this).data('_select2', true).select2(settings).on('remove', function () {
                $(this).off('remove').data('select2').destroy();
            });
        });
    });
    */
    //---------------------------------------------------------------------------------------------chosen插件
    initializes.push(function () {
        $('select.chosen-select', this).filter(function(){
            return !$(this).data('_chosen_select');
        }).data('_chosen_select',true).each(function () {
            var $container = $(this).chosen({no_results_text:'没有结果符合'}).on('remove', function () {
                $(this).off('remove').data('chosen').destroy();
            }).bind('change',function(){
                $(this).data('chosen').search_field_disabled();
            }).data('chosen').container;
            $(".chosen-search",$container).append('<i class="glyph-icon icon-search"></i>');
            $(".chosen-single div",$container).html('<i class="glyph-icon icon-caret-down"></i>');
        });
    });
    //---------------------------------------------------------------------------------------------popover插件
    initializes.push(function () {
        $(".popover-button", this).filter(function(){
            return !$(this).data('_popover');
        }).data('_popover',true).popover({container: "body", html: !0, animation: !0, content: function () {
            var a = $(this).attr("data-id");
            return $(a).html();
        }}).click(function (a) {
            a.preventDefault();
        }).on('shown.bs.popover',function(){
            $(this).data('bs.popover').$tip.initialize();
        }).on('remove', function () {
            var popover = $(this).off('remove').data('bs.popover');
            popover.tip().remove();
            popover.destroy();
        });
        $(".popover-button-default", this).filter(function () {
            return !$(this).data('_popover');
        }).data('_popover', true).popover({container: "body", html: !0, animation: !0}).click(function (a) {
            a.preventDefault();
        }).on('remove', function () {
            var popover = $(this).off('remove').data('bs.popover');
            popover.tip().remove();
            popover.destroy();
        });
    });
    //---------------------------------------------------------------------------------------------niceScroll插件
    initializes.push(function () {
        $(".scrollable-content",this).filter(function(){
            return !$(this).data('_niceScroll');
        }).data('_niceScroll',true).niceScroll({cursorborder: "transparent solid 2px", cursorwidth: "4", cursorcolor: "#363636", cursoropacitymax: "0.4", cursorborderradius: "2px"})
        $(".dataTables_scrollBody",this).filter(function(){
            return !$(this).data('_niceScroll');
        }).data('_niceScroll',true).niceScroll({cursorborder: "transparent solid 2px", cursorwidth: "4", cursorcolor: "#363636", cursoropacitymax: "0.4", cursorborderradius: "2px"})
        $(".scrollable-content",this).filter(function(){
            return !$(this).data('_niceScroll');
        }).data('_niceScroll',true).niceScroll({cursorborder: "transparent solid 2px", cursorwidth: "4", cursorcolor: "#363636", cursoropacitymax: "0.4", cursorborderradius: "2px"})

    });
    //---------------------------------------------------------------------------------------------select2_tags插件
    /*
    initializes.push(function () {
        $('input.tags', this).filter(function(){
            return !$(this).data('_select2_tags');
        }).data('_select2_tags',true).each(function () {
            var settings = {tags: []};
            if (!!$(this).attr('tags')) {
                settings.tags = $(this).attr('tags').split(',')
            }
            $(this).select2(settings);
        });
    });
    */
    //---------------------------------------------------------------------------------------------tagsInput插件
    initializes.push(function () {
        $('input.tagsInput', this).filter(function(){
            return !$(this).data('_tagsInput');
        }).data('_tagsInput',true).each(function(){
            $(this).tagsInput({
                'width': '100%',
                'height': 'auto',
                'defaultText': $(this).attr('placeholder'),
                'onAddTag': function (text) {
                },
                'onRemoveTag': function (text) {
                }
            });
        });
    });
    //---------------------------------------------------------------------------------------------sortable插件
    initializes.push(function () {
        $(".sortable-elements",this).filter(function(){
            return !$(this).data('_sortable');
        }).sortable();
        $(".column-sort",this).filter(function(){
            return !$(this).data('_sortable');
        }).sortable({connectWith: ".column-sort"});
    });
    // ---------------------------------------------------------------------------------------------colorpicker插件
    initializes.push(function () {
        if(!$.fn.hasOwnProperty('colorpicker')){
            return;
        }
        $('.colorpicker', this).filter(function () {
            return !$(this).data('_colorpicker');
        }).data('_colorpicker', true).colorpicker();
    });
    //---------------------------------------------------------------------------------------------datepicker插件
    initializes.push(function () {
        $('.datepicker', this).filter(function () {
            return !$(this).data('_datepicker');
        }).data('_datepicker', true).datepicker({isInput:true});
    });
    //---------------------------------------------------------------------------------------------tooltip插件
    initializes.push(function () {
        $('.tooltip-button,.tip,.tip-left,.tip-right,.tip-top,.tip-bottom', this).filter(function(){
            return !$(this).data('_tooltip');
        }).data('_tooltip',true).each(function () {
            var settings = {};
            if($(this).hasClass('tip-left')){
                settings['placement'] = 'left';
            }else if($(this).hasClass('tip-right')){
                settings['placement'] = 'right';
            }else if($(this).hasClass('tip-top')){
                settings['placement'] = 'top';
            }else if($(this).hasClass('tip-bottom')){
                settings['placement'] = 'bottom';
            }else if($(this).hasClass('tooltip-button')){
                settings['container'] = 'body';
            }
            $(this).tooltip(settings);
        });
    });
    //---------------------------------------------------------------------------------------------mask
    initializes.push(function () {
        if($.browser.msie){
            return;
        }
        $(".mask-mobile", this).each(function () {
            if ($(this).data('_mask'))
                return;
            $(this).data('_mask', true).mask("999-9999-9999");
        });
        $(".mask-phone", this).each(function () {
            if ($(this).data('_mask'))
                return;
            $(this).data('_mask', true).mask("(999) 999-9999");//{completed:function(){alert("Callback action after complete");}}
        });
        $(".mask-phoneExt", this).each(function () {
            if ($(this).data('_mask'))
                return;
            $(this).data('_mask', true).mask("(999) 999-9999? x99999");
        });
        $(".mask-phoneInt", this).each(function () {
            if ($(this).data('_mask'))
                return;
            $(this).data('_mask', true).mask("+40 999 999 999");
        });
        $(".mask-date", this).each(function () {
            if ($(this).data('_mask'))
                return;
            $(this).data('_mask', true).mask("99/99/9999");
        });
        $(".mask-ssn", this).each(function () {
            if ($(this).data('_mask'))
                return;
            $(this).data('_mask', true).mask("999-99-9999");
        });
        $(".mask-productKey", this).each(function () {
            if ($(this).data('_mask'))
                return;
            $(this).data('_mask', true).mask("a*-999-a999", { placeholder: "*" });
        });
        $(".mask-eyeScript", this).each(function () {
            if ($(this).data('_mask'))
                return;
            $(this).data('_mask', true).mask("~9.99 ~9.99 999");
        });
        $(".mask-percent", this).each(function () {
            if ($(this).data('_mask'))
                return;
            $(this).data('_mask', true).mask("99%");
        });
        $(".mask-custom", this).each(function () {
            if ($(this).data('_mask'))
                return;
            $(this).data('_mask', true).mask($(this).data('mask'));
        });
    });
    //---------------------------------------------------------------------------------------------checkbox全选效果
    initializes.push(function ($context) {
        $('[checkAll]', this).filter(function(){
            return !$(this).data('_checkAll');
        }).data('_checkAll', true).each(function (index, zhis) {//TODO [checkAll!=""]匹配规则有点问题
            //如果为body触发。选择最顶层的div
            if ($($context).is('body')) {
                var $temp = $(zhis).parents('body>div');
                if ($temp.length != 0) {
                    $context = $($temp[0]);
                }
            }
            $(zhis).attr('checked', false);
            var getItems = (function (expr) {
                return function () {
                    return get$(expr, $(zhis), $context);//支持jquery表达式
                };
            })($(zhis).attr('checkAll'));
            getItems().closest('tr').removeClass('row_checked');//选中行的样式
            var tipSetting = Fantasy.decode($(this).attr('checktip'));
            if (!!tipSetting) {
                var setTip = function (title) {//显示选中提示
                    $(tipSetting.tip, $context).html(title);
                };
                $(zhis).on('tip', function (target, items, length) {
                    if (length > 0) {
                        setTip(Fantasy.getTemplate(tipSetting.message).applyTemplate({num: length}));
                    } else {
                        setTip("");
                    }
                });
            }
            $(zhis).change(function () {
                setTimeout(function () {//延时处理,防止initializes绑定的事件在该方法之前触发
                    $(zhis).triggerHandler('tip', [getItems(), getItems().filter(":checked").length]);
                }, 10);
            });
            $(zhis).bind('change', function () {
                if ($(this).data('ignore')) {
                    $(this).removeData('ignore');
                    return;
                }
                if (this.checked) {
                    getItems().filter((function(_trigger){
                        return function(){
                            return this != _trigger;
                        };
                    })($(this).data('_trigger'))).data('_trigger',this).checkedAll().closest('tr').addClass('row_checked');
                } else {
                    getItems().filter((function(_trigger){
                        return function(){
                            return this != _trigger;
                        };
                    })($(this).data('_trigger'))).data('_trigger',this).checkedNo().closest('tr').removeClass('row_checked');
                }
                $(this).removeData('_trigger');
            });
            //定时器定时判断是否全选
            if(!!window.checkId){
                window.checkId = 0;
            }
            var checkId = 'check-' + (window.checkId++), items;
            var interval = setInterval(function () {
                if($(zhis).is(':hidden')){
                    return;
                }
                (items = getItems()).each(function () {
                    if ($(this).data(checkId))
                        return;
                    $(this).data(checkId, true);
                    $(this).change(function () {
                        $(zhis).filter((function(_trigger){
                            return function(){
                                return this != _trigger;
                            };
                        })($(this).data('_trigger'))).data('_trigger',this).data('ignore',!(items.filter(':checked').length == items.length || items.filter(':checked').length == 0)).prop('checked', items.filter(':checked').length == items.length).change();
                        $(this).removeData('_trigger');
                    }).change();
                });
            }, 1000);
            $context.on('remove', function () {
                clearInterval(interval);
            });
        });
    });
    //---------------------------------------------------------------------------------------------编辑器
    initializes.push(function () {
        $('.ckeditor', this).each(function () {
            if ($(this).data('_ckeditor'))
                return;
            $(this).data('_ckeditor', true).ckeditor({
                /*fullPage: true,*/
                allowedContent: true,
                skin:'bootstrapck',
                toolbar :
                    [
                        ['Styles', 'Format'],
                        ['Bold', 'Italic', '-', 'NumberedList', 'BulletedList', '-', 'Link', '-', 'Source']
                    ]
            });
        });
    });
    //---------------------------------------------------------------------------------------------dialog
    initializes.push(function () {
        $(".basic-dialog").filter(function(){
            return !$(this).data('_dialog');
        }).data('_dialog',true).click(function () {
            $("#basic-dialog").dialog({resizable: !0, minWidth: 400, minHeight: 350, modal: !1, closeOnEscape: !0, buttons: {OK: function () {
                $(this).dialog("close");
            }}, position: "center"});
        });
        $(".white-modal-60").filter(function(){
            return !$(this).data('_dialog');
        }).data('_dialog',true).click(function () {
            $("#white-modal-60").dialog({modal: !0, minWidth: 500, minHeight: 200, dialogClass: "", show: "fadeIn"}), $(".ui-widget-overlay").addClass("bg-white opacity-60")
        });
        $(".white-modal-80").filter(function(){
            return !$(this).data('_dialog');
        }).data('_dialog',true).click(function () {
            $("#white-modal-80").dialog({modal: !0, minWidth: 600, minHeight: 300, dialogClass: "modal-dialog", show: "fadeIn"}), $(".ui-widget-overlay").addClass("bg-white opacity-80")
        });
        $(".black-modal-60").filter(function(){
            return !$(this).data('_dialog');
        }).data('_dialog',true).click(function () {
            $("#black-modal-60").dialog({modal: !0, minWidth: 500, minHeight: 200, dialogClass: "modal-dialog", show: "fadeIn"}), $(".ui-widget-overlay").addClass("bg-black opacity-60")
        });
        $(".black-modal-80").filter(function(){
            return !$(this).data('_dialog');
        }).data('_dialog',true).click(function () {
            $("#black-modal-80").dialog({modal: !0, minWidth: 500, minHeight: 200, dialogClass: "no-shadow", show: "fadeIn"}), $(".ui-widget-overlay").addClass("bg-black opacity-80")
        });
        $(".red-modal-60").filter(function(){
            return !$(this).data('_dialog');
        }).data('_dialog',true).click(function () {
            $("#red-modal-60").dialog({modal: !0, minWidth: 500, minHeight: 200, dialogClass: "modal-dialog", show: "fadeIn"}), $(".ui-widget-overlay").addClass("bg-red opacity-60")
        });
        $(".dialog-tabs").filter(function(){
            return !$(this).data('_dialog');
        }).data('_dialog',true).click(function () {
            $("#dialog-tabs").dialog({modal: !1, minWidth: 650, minHeight: 200, dialogClass: "modal-dialog", show: "fadeIn"}), $(".ui-widget-overlay").addClass("bg-white opacity-60")
        });
    });
    //---------------------------------------------------------------------------------------------编辑器
    initializes.push(function () {
        $('[toggle]', this).each(function () {
            if ($(this).data('_toggle'))
                return;
            $(this).data('_toggle', true);
            var toggle = $(this).attr('toggle'), t = $(this);
            toggle = /[)]$/.test(toggle) ? eval('(t.' + toggle + ')') : $(toggle);
            toggle.hover(function (e) {
                t.show();
                return stopDefault(e);
            }, function (e) {
                t.css('display', 'none');//TODO t.hide() 方法会出现 message "Component is not available" 异常
                return stopDefault(e);
            });
            t.hide();
        });
    });
    initializes.push(function () {
        $('a[target],form[target]', this).filter(function(){
            return !$(this).data('_target') && ['_blank','_self','_parent','_top'].indexOf($(this).attr('target')) == -1;
        }).data('_target',true).each(function () {
            $(this).target();
        });
    });
    //---------------------------------------------------------------------------------------------显示隐藏切换
    /*
    var groups = {};
    initializes.push(function () {
        $('[target]', this).each(function () {
            if ($(this).data('target'))return;
            $(this).data('target', true);
            var groupname = $(this).attr('group');
            var t = $(this);
            if ($(this).is('option')) {
                groupname = 'select-' + $(this).parent().attr('name');
                t = t.parent();
                if (t.data('Target-chenge'))
                    return;
            }
            (function (t, w, g) {
                //t 触发源
                var titles = t.attr('title') ? t.attr('title').split('|') : [];
                t.attr('title', t.html());
                //w 目标元素
                if (w == '_blank' || w == '_self' || w == '_parent' || w == '_top') {
                    return;
                }
                if (!/[)]$/.test(w) && $('iframe[name=' + w + ']').length > 0) {
                    return;
                }
                //支持jquery表达式
                w = /[)]$/.test(w) ? eval('(t.' + w + ')') : $(w);
                //显示隐藏
                var _F = function (hidden) {
                    (hidden ? t.removeClass : t.addClass).apply(t, ['checked']);
                    (hidden ? w.slideUp : w.slideDown).apply(w, [500, function () {
                        //启用、禁用表单
                        if (w.find('[target]').length == 0) {
                            w.find('input,select,textarea').attr('disabled', w.is(':hidden'));
                        }
                        //切换显示
                        if (titles && titles.length == 2) {
                            t.html(titles[w.is(':hidden') ? 0 : 1]);
                        }
                    }]);
                };
                //分组
                var group = null;
                if (g) {
                    group = groups[g] ? groups[g] : groups[g] = [];
                    group.push(function (soh) {
                        _F.apply(this, [soh]);
                    });
                }
                if (t.is('select')) {//select 处理
                    t.bind('change', function () {
                        $(this).children().each(function () {
                            $($(this).attr('target')).hide();
                        });
                        $(t.children('[value=' + $(this).val() + ']').attr('target')).show();
                    });
                    t.data('Target-chenge', true);
                } else {
                    t.bind('click', function (event) {
                        if ($(this).is('a')) {
                            stopDefault(event);
                        }
                        if (group) {
                            group.each(function () {
                                this.apply(window, [true]);
                            });
                        }
                        _F.apply(this, [!w.is(':hidden')]);
                    });
                    _F.apply(this, [w.is(':hidden')]);
                }

            })(t, $(this).attr('target'), groupname);
        });
    });*/
    //---------------------------------------------------------------------------------------------图片加载时显示缩略图
    $('img[lowsrc]', this).each(function () {
        if ($(this).data('_lowsrc'))
            return;
        var $img = $(this).data('_lowsrc', true);
        var img = new Image();
        img.src = $img.attr('src');
        var min = $img.attr('lowsrc');
        min = min == '' ? img.src : min;
        var re = /_((\d+)[_](\d+))\./;
        //----start 该代码块需后台支持
        if (img.src.endsWith(min)) {//地址相同
            if (re.test(img.src)) {//地址带尺寸
                var arr = re.exec(min);
                min = img.src.replace(re, '_' + Fantasy.toInt(Fantasy.toInt(arr[2]) / 3) + 'x' + Fantasy.toInt(Fantasy.toInt(arr[3]) / 3) + '.');
            } else {//不带尺寸
                min = img.src.replace(/((\.jpg)|(\.png))/, '_' + Fantasy.toInt(Fantasy.toInt($img.width()) / 3) + 'x' + Fantasy.toInt(Fantasy.toInt($img.height()) / 3) + '$2');
            }
        }
        //----end
        $img.removeAttr('lowsrc').attr('src', min);
        img.onload = function () {
            $img.attr('src', this.src);
        };
    });
    //---------------------------------------------------------------------------------------------输入框提示
    initializes.push(function () {
        if (!$.browser.msie)
            return;
        $('[placeholder]', this).each(function () {
            if ($(this).data('_placeholder'))
                return;
            $(this).data('_placeholder', true).placeholder();
        });
    });
    //---------------------------------------------------------------------------------------------选项卡插件
    initializes.push(function ($context) {
        $(".tabs", $context).filter(function(){
            return !$(this).data('_tabs');
        }).data('_tabs',true).tabs({});
        $(".tabs-hover", $context).filter(function(){
            return !$(this).data('_tabs');
        }).data('_tabs',true).tabs({event: "mouseover"});
        /*$('[tabs]', this).each(function () {
            if ($(this).data('tabs'))return;
            (function (zhis) {
                zhis.data('tabs', true);
                var settings = Fantasy.merge(eval('(' + zhis.attr('tabs') + ')') || {}, {
                    selectedClass: 'selected',
                    show: 'show',
                    event: 'click',
                    defaultClass: 'default'
                });
                //获取选项卡对应的元素
                var getTab = function ($tab) {
                    return get$($tab.attr('tab'), $tab, $context);
                };
                //隐藏选项卡并移除选中样式
                var un = function () {
                    zhis.find('[tab]').removeClass(settings.selectedClass);
                    zhis.find('[tab]').each(function () {
                        getTab($(this)).hide().find('input,select');
                        //.attr('disabled',true);
                    });
                };
                //时间触发方法
                var event = function (event) {
                    if ($(this).hasClass(settings.selectedClass) && getTab($(this)).is(':visible'))return;
                    un.apply($context);
                    $(this).addClass(settings.selectedClass);
                    eval('$.fn.' + settings.show).apply(getTab($(this))).find('input,select');
                    //.attr('disabled',false);
                    if ($(this).is('a')) {
                        try {
                            event.preventDefault();
                        } catch (e) {
                        }
                    }
                };
                //绑定事件
                zhis.find('[tab]').bind(settings.event, event);
                un.apply($context);
                //触发默认选中项
                event.apply(zhis.find('.' + settings.selectedClass).length == 0 ? zhis.find('[tab]').first()[0] : zhis.find('.' + settings.selectedClass)[0], [
                    {preventDefault: function () {
                    }}
                ]);
                //单独判断mouseover事件
                if (settings.event == 'mouseover' && settings.defaultClass && settings.container) {
                    settings.container.hover(function () {
                    },function () {
                        zhis.find('.' + settings.defaultClass).mouseover();
                    }).mouseout();
                }
            })($(this));
        });*/
    });
    //--------------------------------------------------------------------------------------------下载中文问题解决
    initializes.push(function () {
        if ($('#hideIframe').length == 0) {
            $('<iframe name="hideIframe" id="hideIframe" style="display:none"></iframe>').appendTo('body');
        }
        var $downloadForm = $('#downloadForm');
        if ($downloadForm.length == 0) {
            $downloadForm = $('<form id="downloadForm" action="" target="hideIframe" method="post"></form>').appendTo('body');
        }
        $('a.download', this).each(function (index, zhis) {
            if ($(zhis).data('_download'))
                return;
            $(zhis).data('_download', true);
            $(zhis).click(function (e) {
                var url = $(this).attr('href');
                var params = Fantasy.parseQuery(url);
                url = url.substr(0, url.indexOf('?') > -1 ? url.indexOf('?') : url.length);
                $downloadForm.attr('action', url).children().remove();
                $.each(params,function(key,value){
                    $('<input type="hidden" name="' + key + '"/>').val(value).appendTo($downloadForm);
                });
                $downloadForm.submit();
                return stopDefault(e);
            });
        });
    });
    //---------------------------------------------------------------------------------------------添加主题
    Holder.add_theme("upload", { background: "#EEEEEE", foreground: "gray", size: 12,font: "Alex Brush",text:"点击上传文件" });
    initializes.push(function () {
        $('.img-rounded,.img-circle,.img-thumbnail,img[data-src^="holder.js"]', this).filter(function(){
            return !$(this).data('_Holder');
        }).data('_Holder',true).each(function (index, zhis) {
            if(!!$(zhis).attr('src')){
                var image = new Image();
                image.onerror = function () {
                    Holder.run({images: $(zhis).removeAttr('src').get(0)});
                };
                image.src = $(zhis).attr('src');
            }else{
                Holder.run({images:$(zhis).get(0)});
            }
        });
    });

    _initialize.apply(document.body);
    //---------------------------------------------------------------------------------------------将初始化方法绑定到  jQuery.fn.initialize
    jQuery.fn.extend({

        initialize: function () {
            if ($(this).children().length == 0) {
                _initialize.apply($(this).parent());
            } else {
                _initialize.apply($(this));
            }
            return this;
        },

        //---------------------------------------------------------------------------------------------批量删除方法封装
        /**
         * 名称：batchExecute
         * 描述：批量操作的辅助方法
         * @param checkBox 全选复选框的jquery对象
         * @param pager 绑定的数据来源    [view/pager]
         * @param idName 额外参数        [提交的主键字段]
         * @param title 操作前的提示范本
         * @param callback 操作完成后的回调
         * @return
         */
        batchExecute: function (checkBox, pager, idName, title, callback) {
            var getItems = (function (ele, expr) {
                return function () {
                    return get$(expr, checkBox, ele.length != 0 ? ele : $('body'));
                };
            })(checkBox.closest('.ajax-load-div'), checkBox.attr('checkAll'));
            $(this).click(function () {
                var ids = getItems().vals();
                deleteMethod(ids);
                return false;
            });
            var arr = /\{(\S+)\}/g.exec(title);
            var url = $(this).has('a') ? $(this).attr('href') : $(this).data('href');
            var titleName = !arr ? '' : arr[1];
            var getTitle = function () {
                new Error('不支持的调用,第2个参数必须为Pager或者View');
            };
            var reLoad = function () {
                new Error('不支持的调用,第2个参数必须为Pager或者View');
            };
            var callValue = function(values,p){
                var tempVal = values;
                p.split(".").each(function(){
                    if(tempVal == undefined){
                        tempVal = {};
                    }
                    tempVal = tempVal[this.toString()];
                });
                return  tempVal == undefined ? '' : tempVal ;
            };
            if (pager.toString() == '[object Fantasy.awt.Pager]') {
                getTitle = function (ids) {
                    var names = [];
                    ids.each(function () {
                        var row = pager.view.find(idName, this.toString());
                        names.push(callValue(row.data,titleName));
                    });
                    return title.replace(/\{(\S+)\}/g, Fantasy.ellipsis(names.toString(), 40, '...'));
                };
                reLoad = function () {
                    pager.reload();
                };
            } else if (pager.toString() == '[object Fantasy.awt.View]') {
                getTitle = function (ids) {
                    var names = [];
                    ids.each(function () {
                        var row = pager.find(idName, this.toString());
                        names.push(row.data[titleName]);
                    });
                    return title.replace(/\{(\S+)\}/g, names);
                };
                reLoad = function () {
                    pager.reload();
                };
            }
            var deleteMethod = function (ids) {
                if (ids) {
                    jQuery.dialog.confirm(getTitle(ids), function () {
                        var params = {};
                        params[idName + 's'] = ids;
                        $.post(url, params, function (data) {
                            reLoad();
                            callback.apply(this, [params, data]);
                        });
                    });
                } else {
                    top.$.msgbox({
                        msg: "您没有选中记录!",
                        icon: "warning"
                    });
                }
            };
            return deleteMethod;
        },

        list : function($savForm,json){
            var $list = $(this);
            var listView = $list.view().on('add',function(data,row){
                row.target.click(function(e){
                    if(!$(e.target).is('a')) {
                        var _data = row.getData();
                        _data.listIndex = row.getIndex();
                        _form.update(_data, 'default');
                    }
                });
            }).on('remove',function(){
                if($savForm.data('_index') == this.getIndex()){
                    $savForm.removeData('_index').disabled().resetForm();
                }
            });
            if(!!json){
                listView.setJSON(json);
            }
            var _form = $savForm.form();
            _form.on('change',function(data,template,f){
                if(template == 'default'){
                    if(data.isNew){
                        $('.formEdit', f.target).hide();
                    }
                    $('.formEdit', f.target).click(function(e){
                        _form.update(_form.getData(),'edit');
                        return stopDefault(e);
                    });
                    $('.formNew', f.target).show().click(function(e){
                        _form.update({isNew:true},'edit');
                        return stopDefault(e);
                    });
                }else if(template == 'edit'){
                    $('.formSave', f.target).click(function(e){
                        var data = f.getData();
                        delete data.isNew;
                        if(data.listIndex!=null){
                            listView.update(data.listIndex,data);
                            $('input[type=checkbox]',listView.target).prop('checked',false).change();
                            $('input[type=checkbox]',listView.get(data.listIndex).target).click();
                        }else{
                            $('input[type=checkbox]',listView.target).prop('checked',false).change();
                            $('input[type=checkbox]',listView.add(data).target).click();
                        }
                        return stopDefault(e);
                    });
                }
            });
            _form.update({isNew:true},'default');
            return {
                view : listView,
                form : $savForm,
                isEdit : function(){
                    return $savForm.data('_index') >= 0;
                },
                getData : function(){
                    return listView.getData();
                }
            };
        }

    });
    //全局ajax异常拦截
    Fantasy.Ajax.on('error', function (xhr, status, err, errorHeader, s) {
        if (status == 200 && err == 'struts2 action Error') {
            var errorMsg = '';
            if (!!s.form && s.form.length > -1) {
                s.form.find('[id$=Tip]').removeClass('onError').html("");
            }
            if (errorHeader.errorMessages.length != 0) {
                errorMsg = errorHeader.errorMessages[0];
                if (errorMsg.indexOf('ajax validator error!') == -1) {//验证错误信息不提示
                    top.$.msgbox({
                        msg: errorMsg.replace('<br/>', ''),
                        icon: "error"
                    });
                }
            } else {
                $.each(errorHeader.fieldErrors,function(fieldName,value){
                    $('#' + fieldName.replaceAll('[.]', '_') + 'Tip').addClass('onError').html(value[0]);
                });
            }
        }
    });
    //全局ajax登陆跳转
    Fantasy.Ajax.on('203', function () {
        $.dialog.close();
        $.dialog.alert('您还没有登录,或者登陆已经超时<br/>点击确定后将为您转到登录页!', function () {
            top.window.location.href = request.getContextPath() + "/admin/login.do";
        });
    });
    //添加异步操作时的进度条显示
    Fantasy.Ajax.on('send', function (event, xhr, s) {
        var block = $(s.form).parents('[class^=block]');
        if (s.dataType == 'script')return;
        if(s.progress != 'none'){
            add_loader(s.block = block);
        }
    });
    //异步操作完成时移除进度条
    Fantasy.Ajax.on('complete', function (event, xhr, s) {
        if (s.dataType == 'script')return;
        if(s.progress != 'none') {
            remove_loader(s.block);
            $(window).resize();
        }
    });
    //IE下皮肤加载路径错误问题 formValidator
    //fv_scriptSrc = request.getContextPath() + '/static/js/common/formvalidator/formValidator-4.1.1.js';
});
//-autocompleter 全局配置 	现在只有用户使用到。使用在这里配置
/*
 $.Autocompleter.defaults.resultsClass = "fuzzy_search";
 $.Autocompleter.defaults.formatItem = function(row, i, max){
 return row.details.name;
 };
 $.Autocompleter.defaults.formatMatch = function(row, i, max){
 return row.id + " " + row.details.name;
 };
 $.Autocompleter.defaults.formatResult = function(row){
 return row.name;
 };
 $.Autocompleter.defaults.parse = function(data){
 return $.map(data, function (row) {
 return {data:row, value:row.details.name,result:row.details.name,id:row.id};
 });
 };*/
$(document).ready(function () {
    $(function () {
        $(".animation-toggle").click(function () {
            var a = $(this).attr("data-animation"), b = $(this).attr("data-animation-target");
            $(b).addClass("animated"), $(b).addClass(a), $(b).removeClass("hide"), window.setTimeout(function () {
                $(b).removeClass("animated"), $(b).removeClass(a), $(b).addClass("hide")
            }, 1300)
        })
    })
})
$(document).ready(function () {
    $(".loader-show").click(function () {
        var a = $(this).attr("data-theme");
        b = $(this).attr("data-opacity");
        c = $(this).attr("data-style");
        d = '<div id="loader-overlay" class="ui-front hide loader ui-widget-overlay ' + a + " opacity-" + b + '"><img src="assets/images/loader-' + c + '.gif" alt="" /></div>';
        $("#loader-overlay").remove();
        $("body").append(d);
        $("#loader-overlay").fadeIn("fast");
        window.setTimeout(function () {
            $("#loader-overlay").fadeOut("fast")
        }, 1500)
    });
    $(".refresh-button").click(function (a) {
        $(".glyph-icon", this).addClass("icon-spin display-block");
        a.preventDefault();
        var b = $(this).parent().parent();
        c = $(this).attr("data-theme");
        d = $(this).attr("data-opacity");
        e = $(this).attr("data-style");
        f = '<div id="refresh-overlay" class="ui-front hide loader ui-widget-overlay ' + c + " opacity-" + d + '"><img src="assets/images/loader-' + e + '.gif" alt="" /></div>';
        $("#refresh-overlay").remove();
        $(b).append(f);
        $("#refresh-overlay").fadeIn("fast");
        window.setTimeout(function () {
            $(".glyph-icon").removeClass("icon-spin display-block");
            $("#refresh-overlay").fadeOut("fast");
        }, 1e3)
    })
});
$(document).on("ready", function () {
    $(".progressbar").each(function () {
        var a = $(this), b = $(this).attr("data-value");
        progress(b, a)
    })
});
$(function () {
    $("#progress-dropdown").hover(function () {
        $(".progressbar").each(function () {
            var a = $(this), b = $(this).attr("data-value");
            progress(b, a)
        })
    })
});
$(document).ready(function () {
    $(".toggle-switch").click(function (a) {
        a.preventDefault();
        var b = $(this).attr("switch-parent"), c = $(this).attr("switch-target");
        $(b).slideToggle(), $(c).slideToggle()
    });
    $(".button-toggle").click(function (a) {
        a.preventDefault(), $(this).next(".toggle-content").slideToggle()
    });
    $(".button-toggle").hover(function () {
        $(".content-box-header a.btn", this).fadeIn("fast")
    }, function () {
        $(".content-box-header a.btn", this).fadeOut("normal")
    });
    $(".box-toggle .content-box-header .toggle-button").click(function (a) {
        a.preventDefault();
        $(".icon-toggle", this).toggleClass("icon-chevron-down").toggleClass("icon-chevron-up");
        $(this).parents(".content-box:first").hasClass("content-box-closed") ? $(this).parents(".content-box:first").removeClass("content-box-closed").find(".content-box-wrapper").slideDown("fast") : $(this).parents(".content-box:first").addClass("content-box-closed").find(".content-box-wrapper").slideUp("fast");
    });
    $(".remove-button").click(function (a) {
        a.preventDefault();
        var b = $(this).attr("data-animation"), c = $(this).parents(".content-box:first");
        $(c).addClass("animated"), $(c).addClass(b), window.setTimeout(function () {
            $(c).slideUp()
        }, 500), window.setTimeout(function () {
            $(c).removeClass(b).fadeIn()
        }, 2500)
    });
    $(function () {
        $(".infobox-close").click(function (a) {
            a.preventDefault(), $(this).parent().fadeOut()
        })
    });
});
$(function () {
    $(".sparkbar").sparkline("html", {type: "bar", disableHiddenCheck: !1, width: "36px", height: "36px"})
});
$(function () {
    $(".bar-sparkline-btn").sparkline([
        [3, 5],
        [4, 7],
        [2, 5],
        [3, 5],
        [4, 7],
        [4, 7],
        [5, 7],
        [2, 7],
        [3, 5]
    ], {type: "bar", height: "53px", barWidth: "5px", barSpacing: "2px"})
});
$(function () {
    $(".bar-sparkline-btn-2").sparkline([
        [3, 5],
        [4, 7],
        [2, 5],
        [3, 5],
        [4, 7],
        [4, 7],
        [5, 7],
        [2, 7],
        [3, 5]
    ], {type: "bar", height: "40px", barWidth: "3px", barSpacing: "2px"})
}), $(function () {
    $(".bar-sparkline").sparkline([
        [4, 8],
        [2, 7],
        [2, 6],
        [2, 7],
        [3, 5],
        [2, 7],
        [2, 6],
        [2, 7],
        [3, 5],
        [4, 7],
        [2, 5],
        [3, 5],
        [4, 7],
        [4, 7],
        [5, 7],
        [4, 8],
        [2, 7],
        [2, 6],
        [2, 7],
        [3, 5]
    ], {type: "bar", height: "35px", barWidth: "5px", barSpacing: "2px"})
});
$(function () {
    $(".bar-sparkline-2").sparkline("html", {type: "bar", barColor: "black", height: "35px", barWidth: "5px", barSpacing: "2px"})
});
$(function () {
    $(".tristate-sparkline").sparkline("html", {type: "tristate", barColor: "black", height: "35px", barWidth: "5px", barSpacing: "2px"})
});
$(function () {
    $(".discrete-sparkline").sparkline("html", {type: "discrete", barColor: "black", height: "45px", barSpacing: "4px"})
});
$(function () {
    $(".pie-sparkline").sparkline("html", {type: "pie", barColor: "black", height: "45px", width: "45px"})
});
$(function () {
    $(".pie-sparkline-alt").sparkline("html", {type: "pie", width: "100", height: "100", sliceColors: ["#EFEFEF", "#5BCCF6", "#FA7753"], borderWidth: 0})
});
$(function () {
    var a = [10, 8, 5, 7, 4, 4, 1];
    $(".dynamic-sparkline").sparkline(a, {height: "35px", width: "135px"})
});
$(function () {
    var a = [10, 8, 5, 7, 4, 4, 1];
    $(".dynamic-sparkline-5").sparkline(a, {height: "57px", width: "100px"})
});
$(function () {
    $(".tristate-sparkline-2").sparkline("html", {type: "tristate", posBarColor: "#ec6a00", negBarColor: "#ffc98a", zeroBarColor: "#000000", height: "35px", barWidth: "5px", barSpacing: "2px"})
});
$(function () {
    $(".infobox-sparkline").sparkline([
        [3, 5],
        [4, 7],
        [2, 5],
        [3, 5],
        [4, 7],
        [4, 7],
        [5, 7],
        [2, 7],
        [3, 5]
    ], {type: "bar", height: "53", barWidth: 5, barSpacing: 2, zeroAxis: !1, barColor: "#ccc", negBarColor: "#ddd", zeroColor: "#ccc", stackedBarColor: ["#871010", "#ffebeb"]})
});
$(function () {
    $(".infobox-sparkline-2").sparkline([
        [3, 5],
        [4, 7],
        [2, 5],
        [3, 5],
        [4, 7],
        [4, 7],
        [5, 7],
        [2, 7],
        [3, 5]
    ], {type: "bar", height: "53", barWidth: 5, barSpacing: 2, zeroAxis: !1, barColor: "#ccc", negBarColor: "#ddd", zeroColor: "#ccc", stackedBarColor: ["#000000", "#cccccc"]})
});
$(function () {
    $(".infobox-sparkline-pie").sparkline([1.5, 2.5, 2], {type: "pie", width: "57", height: "57", sliceColors: ["#0d4f26", "#00712b", "#2eee76"], offset: 0, borderWidth: 0, borderColor: "#000000"})
});
$(function () {
    $(".infobox-sparkline-tri").sparkline([1, 1, 0, 1, -1, -1, 1, -1, 0, 0, 2, 1], {type: "tristate", height: "53", posBarColor: "#1bb1fc", negBarColor: "#3d57ed", zeroBarColor: "#000000", barWidth: 5})
});
$(function () {
    $(".sprk-1").sparkline("html", {type: "line", width: "50%", height: "65", lineColor: "#b2b2b2", fillColor: "#ffffff", lineWidth: 1, spotColor: "#0065ff", minSpotColor: "#0065ff", maxSpotColor: "#0065ff", spotRadius: 4})
});
$(function () {
    $(".sparkline-big").sparkline("html", {type: "line", width: "85%", height: "80", highlightLineColor: "#ffffff", lineColor: "#ffffff", fillColor: "transparent", lineWidth: 1, spotColor: "#ffffff", minSpotColor: "#ffffff", maxSpotColor: "#ffffff", highlightSpotColor: "#000000", spotRadius: 4})
});
$(function () {
    $(".sparkline-bar-big").sparkline("html", {type: "bar", width: "85%", height: "90", barWidth: 6, barSpacing: 2, zeroAxis: !1, barColor: "#ffffff", negBarColor: "#ffffff"})
});
$(function () {
    $(".sparkline-bar-big-color").sparkline("html", {type: "bar", height: "90", width: "85%", barWidth: 6, barSpacing: 2, zeroAxis: !1, barColor: "#9CD159", negBarColor: "#9CD159"})
});
$(function () {
    $(".sparkline-bar-big-color-2").sparkline([405, 450, 302, 405, 230, 311, 405, 342, 579, 405, 450, 302, 183, 579, 180, 311, 405, 342, 579, 405, 450, 302, 405, 230, 311, 405, 342, 579, 405, 450, 302, 405, 342, 432, 405, 450, 302, 183, 579, 180, 311, 405, 342, 579, 183, 579, 180, 311, 405, 342, 579, 405, 450, 302, 405, 230, 311, 405, 342, 579, 405, 450, 302, 405, 342, 432, 405, 450, 302, 183, 579, 180, 311, 405, 342, 579, 240, 180, 311, 450, 302, 370, 210], {type: "bar", height: "88", width: "85%", barWidth: 6, barSpacing: 2, zeroAxis: !1, barColor: "#9CD159", negBarColor: "#9CD159"})
});
var initPieChart = function () {
    $(".chart").easyPieChart({barColor: function (a) {
        return a /= 100, "rgb(" + Math.round(254 * (1 - a)) + ", " + Math.round(255 * a) + ", 0)"
    }, animate: 1e3, scaleColor: "#ccc", lineWidth: 3, size: 100, lineCap: "cap", onStep: function (a) {
        this.$el.find("span").text(~~a)
    }}), $(".chart-alt").easyPieChart({barColor: function (a) {
        return a /= 100, "rgb(" + Math.round(255 * (1 - a)) + ", " + Math.round(255 * a) + ", 0)"
    }, trackColor: "#333", scaleColor: !1, lineCap: "butt", rotate: -90, lineWidth: 20, animate: 1500, onStep: function (a) {
        this.$el.find("span").text(~~a)
    }}), $(".chart-alt-1").easyPieChart({barColor: function (a) {
        return a /= 100, "rgb(" + Math.round(255 * (1 - a)) + ", " + Math.round(255 * a) + ", 0)"
    }, trackColor: "#e1ecf1", scaleColor: "#c4d7e0", lineCap: "cap", rotate: -90, lineWidth: 10, size: 80, animate: 2500, onStep: function (a) {
        this.$el.find("span").text(~~a)
    }}), $(".chart-alt-2").easyPieChart({barColor: function (a) {
        return a /= 100, "rgb(" + Math.round(255 * (1 - a)) + ", " + Math.round(255 * a) + ", 0)"
    }, trackColor: "#fff", scaleColor: !1, lineCap: "butt", rotate: -90, lineWidth: 4, size: 50, animate: 1500, onStep: function (a) {
        this.$el.find("span").text(~~a)
    }}), $(".updateEasyPieChart").on("click", function (a) {
        a.preventDefault(), $(".chart, .chart-alt, .chart-alt-1, .chart-alt-2").each(function () {
            $(this).data("easyPieChart").update(Math.round(100 * Math.random()))
        })
    })
};
$(document).ready(function () {
    initPieChart()
});
$(window).resize(function () {
    layoutFormatter()
});
$(document).on("ready", function () {
    if ($("body").hasClass("boxed-layout")) {
        var a = $(".boxed-layout #page-sidebar").offset().top;
        $(window).scroll(function () {
            var b = $(window).scrollTop();
            b > a ? $(".boxed-layout #page-sidebar").css({position: "fixed", top: 10}) : $(".boxed-layout #page-sidebar").css("position", "static")
        })
    }
});
$(document).ready(function () {
    layoutFormatter();
    $(function () {
        $("#responsive-open-menu").click(function (e) {
            console.log(e);
            $("#sidebar-menu").toggle()
        })
    });
    $(function () {
        $("#sidebar-menu li").click(function (e) {
            if($(e.target).is('a') && $(e.target).prop('href')!='javascript:;'){
                _active($(e.target).prop('href'));
                return;
            }
            if($(this).is(".active")){
                $(this).removeClass("active");
                $("ul", this).slideUp();
            } else {
                $("#sidebar-menu li ul").slideUp();
                $("ul", this).slideDown();
                $("#sidebar-menu li").removeClass("active");
                $(this).addClass("active");

            }
        });
    });
    $(function () {
        var a = window.location;
        window._active = function(a){
            $('#sidebar-menu li').removeClass('current-page').removeClass('active');
            $('#sidebar-menu a[href="' + a + '"]').parent("li").addClass("current-page"), $("#sidebar-menu a").filter(function () {
                return this.href == a;
            }).parent("li").addClass("current-page").parent("ul").slideDown().parent().addClass("active");
            $("#sidebar-menu li ul").filter(function(){
                return $(this).is(':visible') && !$(this).parent().is('.active');
            }).slideUp();
        };
        _active(a);
    });
    $(function () {
        $(".boxed-layout-btn").click(function () {
            $.cookie("boxedLayout", "on"), $("body").addClass("boxed-layout"), $(".boxed-layout-btn").addClass("hidden"), $(".fluid-layout-btn").removeClass("hidden")
        });
        $(".fluid-layout-btn").click(function () {
            $.cookie("boxedLayout", "off"), $("body").removeClass("boxed-layout"), $(".fluid-layout-btn").addClass("hidden"), $(".boxed-layout-btn").removeClass("hidden"), $("#page-sidebar").css("position", "fixed")
        });
        var a = $.cookie("boxedLayout");
        "on" == a && ($("body").addClass("boxed-layout"), $(".boxed-layout-btn").addClass("hidden"), $(".fluid-layout-btn").removeClass("hidden"))
    });
    $(function () {
        $("#close-sidebar").click(function () {
            $("#page-content-wrapper").animate({marginLeft: 0}, 300), $("body").addClass("close-sidebar"), $.cookie("closesidebar", "close"), $(this).addClass("hidden"), $("#rm-close-sidebar").removeClass("hidden")
        }), $("#rm-close-sidebar").click(function () {
            $("#page-content-wrapper").animate({marginLeft: 220}, 300), $("body").removeClass("close-sidebar"), $.cookie("closesidebar", "rm-close"), $(this).addClass("hidden"), $("#close-sidebar").removeClass("hidden")
        });
        var a = $.cookie("closesidebar");
        "close" == a && ($("#close-sidebar").addClass("hidden"), $("#rm-close-sidebar").removeClass("hidden"), $("body").addClass("close-sidebar"))
    })
});
$(document).ready(function () {
    $(".choose-bg").click(function () {
        var a = $(this).attr("boxed-bg");
        $("body").css("background", a);
        $.cookie("set-boxed-bg", a);
    });
    $(".change-layout-theme a").click(function () {
        var a = $(this).attr("layout-theme");
        $("#loading").slideDown({complete: function () {
            "" != a && ($("#layout-theme").attr("href", request.getContextPath() + "/assets/themes/minified/fides/color-schemes/" + a + ".min.css"), $.cookie("set-layout-theme", a))
        }});
        $("#loading").delay(1500).slideUp();
    });
    themefromCookie();
    bgFromCookie();
});
$(function () {
    $(".change-theme-btn").click(function () {
        $(".theme-customizer").animate({right: "0"})
    }), $(".theme-customizer .theme-wrapper").click(function () {
        $(this).parent().animate({right: "-350"})
    })
});
$(document).ready(function () {
    $(function () {
        $("#sidebar-search input").focus(function () {
            $(this).stop().animate({width: 200}, "slow")
        }).blur(function () {
            $(this).stop().animate({width: 100}, "slow")
        })
    });
    $(function () {
        $("#form-wizard").smartWizard({transitionEffect: "slide"})
    });
    $(function () {
        $(".jcrop-basic").Jcrop()
    });
    $(function () {
        $(".textarea-autosize").autosize()
    });
    $(function () {
        var a = [];
        a[""] = '<i class="glyph-icon icon-cog mrg5R"></i>This alert needs your attention, but it\'s not super important.', a.alert = '<i class="glyph-icon icon-cog mrg5R"></i>Best check yo self, you\'re not looking too good.', a.error = '<i class="glyph-icon icon-cog mrg5R"></i>Change a few things up and try submitting again.', a.success = '<i class="glyph-icon icon-cog mrg5R"></i>You successfully read this important alert message.', a.information = '<i class="glyph-icon icon-cog mrg5R"></i>This alert needs your attention, but it\'s not super important.', a.notification = '<i class="glyph-icon icon-cog mrg5R"></i>This alert needs your attention, but it\'s not super important.', a.warning = '<i class="glyph-icon icon-cog mrg5R"></i>Best check yo self, you\'re not looking too good.',
            $(".noty").click(function () {
            var b = $(this);
            return noty({text: a[b.data("type")], type: b.data("type"), dismissQueue: !0, theme: "agileUI", layout: b.data("layout")});
        })
    });
    $(function () {
        $(".colorpicker-position-bottom-left").minicolors({animationSpeed: 100, change: null, changeDelay: 0, control: "wheel", defaultValue: "", hide: null, hideSpeed: 100, inline: !1, letterCase: "lowercase", opacity: !1, position: "bottom left", show: null, showSpeed: 100, textfield: !0, theme: "default"})
    });
    $(function () {
        $(".colorpicker-position-bottom-right").minicolors({animationSpeed: 100, change: null, changeDelay: 0, control: "hue", defaultValue: "", hide: null, hideSpeed: 100, inline: !1, letterCase: "lowercase", opacity: !1, position: "bottom right", show: null, showSpeed: 100, textfield: !0, theme: "default"})
    });
    $(function () {
        $(".colorpicker-position-top-left").minicolors({animationSpeed: 100, change: null, changeDelay: 0, control: "saturation", defaultValue: "", hide: null, hideSpeed: 100, inline: !1, letterCase: "lowercase", opacity: !0, position: "top left", show: null, showSpeed: 100, textfield: !0, theme: "default"})
    });
    $(function () {
        $(".colorpicker-position-top-right").minicolors({animationSpeed: 100, change: null, changeDelay: 0, control: "brightness", defaultValue: "", hide: null, hideSpeed: 100, inline: !1, letterCase: "lowercase", opacity: !0, position: "top right", show: null, showSpeed: 100, textfield: !0, theme: "default"})
    });
    $(function () {
        $(".colorpicker-inline").minicolors({animationSpeed: 100, change: null, changeDelay: 0, control: "hue", defaultValue: "", hide: null, hideSpeed: 100, inline: !0, letterCase: "lowercase", opacity: !0, position: "bottom right", show: null, showSpeed: 100, textfield: !0, theme: "default"})
    });
    $(function () {
        $(".growl-top-left").click(function () {
            $.jGrowl("Top left jGrowl notification with shadow and <b>.bg-black</b> background", {sticky: !1, position: "top-left", theme: "bg-black drop-shadow-alt"})
        });
        $(".growl-top-right").click(function () {
            $.jGrowl("Top right jGrowl notification with <b>.bg-azure</b> background", {sticky: !1, position: "top-right", theme: "bg-azure btn text-left"})
        });
        $(".growl-bottom-left").click(function () {
            $.jGrowl("Bottom left jGrowl notification with <b>.bg-red</b> background", {sticky: !1, position: "bottom-right", theme: "bg-red btn text-left"})
        });
        $(".growl-bottom-right").click(function () {
            $.jGrowl("Bottom right jGrowl notification with <b>.bg-blue-alt</b> background", {sticky: !1, position: "bottom-left", theme: "bg-blue-alt btn text-left"})
        });
        $(".growl-error").click(function () {
            $.jGrowl("This is just a growl example using our custom color helpers for styling.", {sticky: !1, position: "top-right", theme: "bg-red"})
        });
        $(".growl-basic").click(function () {
            $.jGrowl("This is just a growl example using our custom color helpers for styling.", {sticky: !1, position: "bottom-right", theme: "primary-bg"})
        });
        $(".growl-basic-secondary").click(function () {
            $.jGrowl("This is just a growl example using our custom color helpers for styling.", {sticky: !1, position: "bottom-right", theme: "ui-state-default"})
        });
        $(".growl-success").click(function () {
            $.jGrowl("This is just a growl example using our custom color helpers for styling.", {sticky: !1, position: "top-right", theme: "bg-green"})
        });
        $(".growl-warning").click(function () {
            $.jGrowl("This is just a growl example using our custom color helpers for styling.", {sticky: !1, position: "bottom-right", theme: "bg-orange"})
        });
        $(".growl-info").click(function () {
            $.jGrowl("This is just a growl example using our custom color helpers for styling.", {sticky: !1, position: "top-right", theme: "bg-gray"})
        });
        $(".growl-notice").click(function () {
            $.jGrowl("This is just a growl example using our custom color helpers for styling.", {sticky: !1, position: "top-right", theme: "bg-black"})
        });
    });
    $(function () {
        $("#slider").slider({})
    });
    $(function () {
        $("#horizontal-slider").slider({value: 40, orientation: "horizontal", range: "min", animate: !0})
    });
    $(function () {
        $("#slider-range-vertical").slider({orientation: "vertical", range: !0, values: [17, 67], slide: function (a, b) {
            $("#amount-vertical-range").val("$" + b.values[0] + " - $" + b.values[1])
        }}), $("#amount-vertical-range").val("$" + $("#slider-range-vertical").slider("values", 0) + " - $" + $("#slider-range-vertical").slider("values", 1))
    });
    $(function () {
        $("#slider-range").slider({range: !0, min: 0, max: 500, values: [75, 300], slide: function (a, b) {
            $("#amount").val("$" + b.values[0] + " - $" + b.values[1])
        }}), $("#amount").val("$" + $("#slider-range").slider("values", 0) + " - $" + $("#slider-range").slider("values", 1))
    });
    $(function () {
        $("#slider-vertical").slider({orientation: "vertical", range: "min", min: 0, max: 100, value: 60, slide: function (a, b) {
            $("#amount3").val(b.value)
        }}), $("#amount3").val($("#slider-vertical").slider("value"))
    });
    $(function () {
        $("#master").slider({value: 60, orientation: "horizontal", range: "min", animate: !0}), $("#eq > span").each(function () {
            var a = parseInt($(this).text(), 10);
            $(this).empty().slider({value: a, range: "min", animate: !0, orientation: "vertical"})
        })
    });
    $(function () {
        $(".sparkbar").sparkline("html", {type: "bar", disableHiddenCheck: !1, width: "36px", height: "36px"})
    });
    $(function () {
        $(".accordion").accordion({heightStyle: "content"})
    });
    $(function () {
        $("#accordion-hover").accordion({event: "mouseover", heightStyle: "auto"})
    });
    $(function () {
        $("#accordion-with-tabs").accordion({active: 1, heightStyle: "content"})
    });
    $(function () {
        $(".slider-demo").slider({})
    });
    $(function () {
        $(".fromDate").datepicker({defaultDate: "+1w", changeMonth: !0, numberOfMonths: 3, onClose: function (a) {
            $(".toDate").datepicker("option", "minDate", a)
        }});
        $(".toDate").datepicker({defaultDate: "+1w", changeMonth: !0, numberOfMonths: 3, onClose: function (a) {
            $(".fromDate").datepicker("option", "maxDate", a)
        }})
    });
    $(function () {
        $("#datepicker2").datepicker({numberOfMonths: 3, showButtonPanel: !0})
    });
    $(function () {
        /*$("#example1").dataTable({sScrollY: 300, bJQueryUI: !0, sPaginationType: "full_numbers"});
        $(".dataTable .ui-icon-carat-2-n").addClass("icon-sort-up");
        $(".dataTable .ui-icon-carat-2-s").addClass("icon-sort-down");
        $(".dataTable .ui-icon-carat-2-n-s").addClass("icon-sort");
        $(".dataTables_paginate a.first").html('<i class="icon-caret-left"></i>');
        $(".dataTables_paginate a.previous").html('<i class="icon-angle-left"></i>');
        $(".dataTables_paginate a.last").html('<i class="icon-caret-right"></i>');
        $(".dataTables_paginate a.next").html('<i class="icon-angle-right"></i>');*/
    });
    $(function () {
        var a = ["ActionScript", "AppleScript", "Asp", "BASIC", "C", "C++", "Clojure", "COBOL", "ColdFusion", "Erlang", "Fortran", "Groovy", "Haskell", "Java", "JavaScript", "Lisp", "Perl", "PHP", "Python", "Ruby", "Scala", "Scheme"];
        $(".autocomplete-input").autocomplete({source: a})
    });
    $(function () {

    });
    $(function () {
        $(".spinner-input").spinner()
    });
    $(function () {

    });
    $(function () {
            $(".selector").append('<i class="glyph-icon icon-caret-down"></i>');
        /*
            $(".checker span").append('<i class="glyph-icon icon-ok"></i>').addClass("ui-state-default btn radius-all-4");
            $(".radio span").append('<i class="glyph-icon icon-circle"></i>').addClass("ui-state-default btn radius-all-100");*/
    });
    $(function () {
        $(".growl-button").click(function () {
            $.jGrowl("A message with a header", {header: "Important", sticky: !1, theme: "bg-black"})
        })
    });
    $(function () {
        $(".timepicker").timepicker()
    });
    $(window).bind("popstate", function() {
        console.log(arguments);
        alert(location.href);
    });

    // custom uiDropdown element, example can be seen in user-list.html on the 'Filter users' button
    var uiDropdown = new function() {
        var self;
        self = this;
        this.hideDialog = function($el) {
            return $el.find(".dialog").hide().removeClass("is-visible");
        };
        this.showDialog = function($el) {
            return $el.find(".dialog").show().addClass("is-visible");
        };
        return this.initialize = function() {
            $("html").click(function() {
                $(".ui-dropdown .head").removeClass("active");
                return self.hideDialog($(".ui-dropdown"));
            });
            $(".ui-dropdown .body").click(function(e) {
                return e.stopPropagation();
            });
            return $(".ui-dropdown").each(function(index, el) {
                return $(el).click(function(e) {
                    e.stopPropagation();
                    $(el).find(".head").toggleClass("active");
                    if ($(el).find(".head").hasClass("active")) {
                        return self.showDialog($(el));
                    } else {
                        return self.hideDialog($(el));
                    }
                });
            });
        };
    };

    // instantiate new uiDropdown from above to build the plugins
    new uiDropdown();

});
