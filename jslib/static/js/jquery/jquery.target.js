jQuery.extend({

    target: function (key, theme) {
        Fantasy.awt.Target.addTheme(key, theme);
    },

    targets: (function (_targets) {
        return function () {
            return _targets;
        }
    })([])

});
jQuery.fn.extend({

    target: function (exp) {
        if (!$(this).data('target')) {
            var attrTarget = Fantasy.defaultValue($(this).attr('target'), exp).split(':');
            var _target = new Fantasy.awt.Target({theme: attrTarget[0], target: $(this), selector: attrTarget[1]});
            $(this).click(function (e) {
                _target.load($(this).attr('href'));
                stopDefault(e);
            });
            $(this).data('target', _target);
        }
        return $(this).data('target');
    }

});
$.target('top', {

    newInstance: function () {
        this.on('load', function (html, ajaxSettings) {
            history.pushState(null, document.title, ajaxSettings.url);
            $.targets().clear();
            $.targets().push(html);
            this.one('destroy',function(){
                $.targets().remove(html);
            });
        });
        this.on('loadComplete', function ($html) {
            $html.initialize();
        });
    },

    parse: function (data) {
        var _element = this.getElement();
        _element.triggerHandler('_destroy');
        return _element.html(data);
    }

});
$.target('html', {

    newInstance: function () {
        this.on('load', function (html) {
            $.targets().push(html);
            this.one('destroy',function(){
                $.targets().remove(html);
            });
        });
        this.on('loadComplete', function ($html) {
            $html.initialize();
        });
    },

    parse: function (data) {
        var _element = this.getElement();
        _element.triggerHandler('_destroy');
        return _element.html(data);
    }

});
$.target('after', {

    newInstance: function () {
        this.on('load', function (html) {
            $.targets().push(html);
            this.one('destroy',function(){
                $.targets().remove(html);
            });
        });
        this.on('loadComplete', function ($html) {
            $html.initialize();
            $('.back-page', $html).click(function (zhis) {
                return function () {
                    zhis.destroy();
                    zhis.getElement().show().next().remove();
                    $(window).resize();
                }
            }(this));
        });
    },

    parse: function (data) {
        var $tmp = $('<div class="ajax-load-div"></div>').html(data);
        this.getElement().hide().after($tmp);
        return $tmp;
    }

});
if (!window.$page$) {
    window.$page$ = {
        on: function (eventName, callback) {
            $('#page-content-wrapper').on('_' + eventName, callback);
        },
        one: function (eventName, callback) {
            $('#page-content-wrapper').one('_' + eventName, callback);
        }
    };
}