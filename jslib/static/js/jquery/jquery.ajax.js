jQuery.fn.extend({

    reload: function (f) {
        if (!!f) {
            return this.on('reload', f);
        } else {
            return this.triggerHandler('reload');
        }
    },

    backpage: function (f) {
        if (!!f) {
            return this.on('backpage', f);
        } else {
            return this.triggerHandler('backpage');
        }
    },

    ajax: (function () {
        window._$temp = [];
        var _handlers = {
            handlers: {'html': function (target, data, realTarget) {
                var settings = this;
                var ndiv = jQuery.fn.html.apply(target, [data]);
                if (!!realTarget) {
                    target = realTarget;
                    $('.back-page', jQuery.fn.html.apply(ndiv, [data])).bind('click',function () {
                        $(this).backpage();
                        return false;
                    }).backpage(function () {
                            ndiv.find('select').remove();
                            ndiv.remove();
                            target.show();
                            stack.pop();
                            _$temp.pop();
                            $(window).resize();
                            var page = target.find(".back-page:eq(0)");
                            return page.length != 0 ? page : {reload: function () {
                                if (settings.reload) {
                                    settings.reload(settings);
                                }
                            }};
                        }).reload(function () {
                            //if(!settings.reload || settings.reload(settings) != false){}
                            var _load = stack.pop();
                            _load.ajaxFun.apply(_load.target, [_handlers.getHandler('html', {loadType: 'html'}), ndiv]);
                        });
                }
                _$temp.clear();
                _$temp.push(ndiv);
                return ndiv;
            }, 'otherload': function (target, data) {
                var settings = this;
                var ndiv = $('<div class="ajax-load-div"></div>');
                jQuery.fn[this.otherSettings.loadType].apply(target, [ndiv]);
                $('.back-page', jQuery.fn.html.apply(ndiv, [data])).bind('click',function () {
                    $(this).backpage();
                    return false;
                }).backpage(function () {
                        ndiv.find('select').remove();
                        ndiv.remove();
                        target.show();
                        stack.pop();
                        _$temp.pop();
                        $(window).resize();
                        var page = target.find(".back-page:eq(0)");
                        return page.length != 0 ? page : {reload: function () {
                            if (settings.reload) {
                                settings.reload(settings);
                            }
                        }};
                    }).reload(function () {
                        //if(!settings.reload || settings.reload(settings) != false){
                        var _load = stack.pop();
                        _load.ajaxFun.apply(_load.target, [_handlers.getHandler('html', {loadType: 'html'}), ndiv]);
                        //}
                    });
                target.hide();
                _$temp.push(ndiv);
                return ndiv;
            }, 'dialog': function () {

            }, 'template': function () {

            }, 'view': function () {

            }, 'pager': function () {

            }, 'val': function (target, data) {
                target.val(data[this.mapping]);
                return target;
            }},
            getHandler: function (type, otherSettings) {
                if ('html' == type) {
                    if ('html' == otherSettings.loadType) {
                        return this.handlers['html'];
                    } else {
                        return this.handlers['otherload'];
                    }
                } else if ('view' == type) {
                    return this.handlers['view'];
                } else if ('pager' == type) {
                    return this.handlers['pager'];
                } else if ('val' == type) {
                    return this.handlers['val'];
                }
            }
        };
        var stack = new Fantasy.List();
        var ajax = function (options) {
            if (this.length == 0)
                return;
            if (this.length != 1) {
                throw new Error("jQuery.fn.ajax 只支持一次选择一个元素.");
            }
            if (!($(this).is("form") || $(this).is("a"))) {
                throw new Error("jQuery.fn.ajax 只支持 form 与 a 标签");
            }
            var settings = Fantasy.merge(options || {}, jQuery.fn.ajax.settings);// Object.clone()
            settings = Fantasy.merge(settings, function () {
                with (settings) {
                    switch (type) {
                        case 'dialog':
                            return {};
                        case 'html':
                            if (!settings.target) {
                                throw new Error("type = html 时，target属性不能为null");
                            }
                            return {ajaxSettings: {dataType: 'html'}, otherSettings: {loadType: 'after'}};
                        case 'val':
                            if (!settings.target) {
                                throw new Error("type = val 时，target属性不能为null");
                            }
                        return {};
                    }
                }
            }());

            var oldUrl = '';
            var initUrl = function () {
                oldUrl = settings.url = $(this).is("form") ? $(this).attr('action') : $(this).attr('href');
                if (settings.type != 'dialog') {
                    settings.ajaxSettings.postData = Fantasy.parseQuery(settings.url.substring(settings.url.indexOf('?') + 1));
                    settings.url = Fantasy.urlFilter(settings.url.indexOf('?') > 0 ? settings.url.substring(0, settings.url.indexOf('?')) : settings.url);
                }
            };
            initUrl.apply(this);
            var ajaxLoadDiv = function(data,settings,reloadHandler,reloadTarget){
                var _target = /[)]$/.test(settings.target) ? eval('($(this).' + settings.target + ')') : $(settings.target);
                //查找返回结果集中的js代码块
                var jstxts = [];
                if (!$.isPlainObject(data)) {
                    data = data.replace(/[\f\r\v]/g, '').replace(/(\<script((.|\n)*?)\<\/script\>)/gi, function($0) {
                        var jstxt = $0.replace(/<script[^>]*>|<\/script>|<SCRIPT[^>]*>|<\/SCRIPT>/g, '');
                        if(!!jstxt){
                            jstxts.push(jstxt);
                            return "";
                        }
                        return $0;
                    });
                }
                var $ajaxLoadDivTemp, handler = !reloadHandler ? _handlers.getHandler(settings.type, settings.otherSettings) : reloadHandler;
                if (!reloadTarget) {
                    settings.callback.apply(settings, [settings.type, $ajaxLoadDivTemp = handler.apply(settings, [_target, data])]);
                } else {
                    settings.callback.apply(settings, [settings.type, $ajaxLoadDivTemp = handler.apply(settings, [reloadTarget, data, _target])]);
                }
                //执行js
                jstxts.each(function () {
                    var jstxt = this.toString();
                    var $ = (function(_$){
                        return Fantasy.copy(function(){
                            if(arguments.length == 1 && typeof arguments[0] == 'string'){
                                if(_$(arguments[0],$ajaxLoadDivTemp).length != 0){
                                    return _$.apply(this,[arguments[0],$ajaxLoadDivTemp]);
                                }else{
                                    for(var i=_$temp.length-1;i>=0;i--){
                                        if (_$(arguments[0],_$temp[i]).length != 0) {
                                            return _$.apply(this,[arguments[0],_$temp[i]]);
                                        }
                                    }
                                }
                                return _$.apply(this,arguments);
                            }else{
                                return _$.apply(this,arguments);
                            }
                        },_$);
                    })(jQuery);
                    eval(jstxt);
                });
                stack.push({'target': this, 'ajaxFun': ajaxFun});
                history.pushState(null, document.title, settings.url);
            };
            var ajaxFun = function (reloadHandler, reloadTarget) {
                if (oldUrl != ($(this).is("form") ? $(this).attr('action') : $(this).attr('href'))) {
                    initUrl.apply(this);
                }
                var zhis = this;
                if (settings.type == 'dialog') {
                    jQuery.dialog.open(settings.url, settings.otherSettings);
                } else {
                    jQuery.ajax(Fantasy.copy(settings.ajaxSettings, {url: settings.url, data: settings.ajaxSettings.postData, success: function (data) {
                        ajaxLoadDiv.apply(zhis,[data,settings,reloadHandler,reloadTarget]);
                    }}));
                }
            };
            if ($(this).is("form")) {
                $(this).ajaxForm(function(data){
                    ajaxLoadDiv.apply(arguments[3],[data,options]);
                });
            } else {
                $(this).bind(settings.event, function (event) {
                    try {
                        if (event.result != false) {
                            ajaxFun.apply(this);
                        }
                    } finally {
                        stopDefault(event);
                    }
                });
            }
            return this;
        };
        ajax.settings = {type: 'html', target: null, event: 'click', callback: function () {
        }, ajaxSettings: {type: 'post'}, otherSettings: {}};
        return ajax;
    })()

});