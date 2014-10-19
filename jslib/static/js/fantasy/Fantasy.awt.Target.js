Fantasy.util.jClass(Fantasy.util.Observable, {

    jClass: 'Fantasy.awt.Target',

    /**
     * a 与 form 标签的 target 属性扩展
     * @param $super
     * @param options
     */
    initialize: function ($super, options) {
        $super(['loadStart', 'load', 'loadComplete', 'destroy']);
        this.options = options;
        Fantasy.awt.Target.getTheme(this.options.theme).newInstance.apply(this);
    },

    load: function (url) {
        var _this = this;
        var ajaxSettings = {
            url: url,
            type: 'post',
            success: function (data) {
                //查找返回结果集中的js代码块
                var jstxts = [];
                if (!$.isPlainObject(data)) {
                    data = data.replace(/[\f\r\v]/g, '').replace(/(\<script((.|\n)*?)\<\/script\>)/gi, function ($0) {
                        var jstxt = $0.replace(/<script[^>]*>|<\/script>|<SCRIPT[^>]*>|<\/SCRIPT>/g, '');
                        if (!!jstxt) {
                            jstxts.push(jstxt);
                            return "";
                        }
                        return $0;
                    });
                }
                var $ajaxLoadDivTemp = _this.parse(data), $page$ = _this;
                _this.fireEvent('load', _this, [$ajaxLoadDivTemp, ajaxSettings, jstxts]);
                //执行js
                jstxts.each(function () {
                    var jstxt = this.toString();
                    var $ = (function (_$) {
                        return Fantasy.copy(function () {
                            if (arguments.length == 1 && typeof arguments[0] == 'string') {
                                for(var i=$.targets().length-1;i>=0;i--){
                                    if (_$(arguments[0], $.targets()[i]).length != 0) {
                                        return _$.apply(this, [arguments[0], $.targets()[i]]);
                                    }
                                }
                                console.log(arguments);
                                return _$.apply(this, arguments);
                            } else {
                                return _$.apply(this, arguments);
                            }
                        }, _$);
                    })(jQuery);
                    eval(jstxt);
                });
                _this.fireEvent('loadComplete', _this, [$ajaxLoadDivTemp]);
            }
        };
        _this.fireEvent('loadStart', this, [ajaxSettings]);
        jQuery.ajax(ajaxSettings);
    },

    parse: function (data) {
        var _destroy = (function (_this) {
            return function () {
                try {
                    _this.destroy();
                } finally {
                    $(this).unbind('_destroy', _destroy);
                }
            }
        })(this);
        return Fantasy.awt.Target.getTheme(this.options.theme).parse.apply(this, [data]).on('_destroy', _destroy);
    },

    getElement: function () {
        return this.element = Fantasy.get$Object(this.options.selector, this.options.target, $('body'));
    },

    destroy: function () {
        this.fireEvent('destroy', this, []);
    }

});
Fantasy.apply(Fantasy.awt.Target, {}, (function () {

    var themes = {};

    return {

        addTheme: function (key, theme) {
            themes[key] = Fantasy.copy({
                newInstance: function () {
                }
            }, theme);
        },

        getTheme: function (key) {
            return themes[key]
        }

    }

})());
