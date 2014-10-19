//============================================================
//[描    述]  Observable类	主要功能：一个抽象基类,为事件机制的管理提供一个公共接口。
//============================================================
Fantasy.util.jClass({

    jClass: 'Fantasy.util.Observable',

    /**
     * 构造方法,在创建实例的时候调用
     */
    initialize: function ($super, events) {
        $super();
        if (events && Fantasy.isArray(events)) {
            this.events = events;
        } else {
            this.events = [];
        }
    },

    getListeners: function () {
        return this.listeners = this.listeners ? this.listeners : new Fantasy.util.Map();
    },

    /**
     * 添加处理事件
     */
    addEvents: function () {
        if (arguments) {
            for (var i = 0, length = arguments.length; i < length; i++) {
                this.events.push(arguments[i]);
            }
        }
    },

    /**
     * 获取可用的事件列表
     * @returns {events|*}
     */
    getEvents: function () {
        return this.events;
    },

    /**
     * 移除事件
     *
     * @param eventName 事件名称
     */
    removeEvent: function (eventName) {
        this.getListeners().remove(eventName);
    },

    /**
     * 判断事件对应的执行方法是否存在
     *
     * @param eventName 事件名称
     * @param fun 绑定方法
     * @returns {boolean}
     */
    has: function (eventName, fun) {
        return this.hasListener(eventName, fun);
    },


    /**
     * 判断事件对应的执行方法是否存在
     *
     * @param eventName 事件名称
     * @param fun 绑定方法
     * @returns {boolean}
     */
    hasListener: function (eventName, fun) {
        var retVal = false;
        if (this.getListeners().containsKey(eventName)) {
            var _listener = this.getListeners().get(eventName);
            _listener.each(function () {
                if (this.method == fun)
                    retVal = true;
            });
        }
        return retVal;
    },

    /**
     * 触发事件
     * @param eventName 事件名称
     * @param scope 触发域
     * @param params 触发参数
     * @returns {*}
     */
    fireEvent: function (eventName, scope, params) {
        var vals = [];
        if (this.getListeners().containsKey(eventName)) {
            var _removeListener = [];
            try {
                var _listener = this.getListeners().get(eventName);
                return _listener.each(function (i) {
                    var val = this.method.apply(!!scope ? scope : this.scope, [].concat(params));//TODO 如果注册事件时提供了scope则取原先的scope
                    if (this.one) {
                        _removeListener.push(this.method);
                    }
                    if (!Fantasy.isNull(val)) {
                        vals.push(val);
                    }
                    if (i == (_listener.size() - 1) && vals.length > 0)//TODO 事件队列里的其他事件如何得知 该事件已被终止
                        return vals[0];
                });
            } finally {
                _removeListener.each(function () {
                    _listener.removeListener(eventName, this);
                });
            }
        }
    },

    /**
     * 为事件添加执行方法
     * @param eventName 事件名称
     * @param fun 绑定方法
     * @param params 绑定参数
     * @param scope 执行域
     * @returns {*}
     */
    on: function (eventName, fun, params, scope) {
        this.addListener(eventName, fun, params, scope);
        return this;
    },

    /**
     * one() 方法为被选元素附加一个或多个事件处理程序，并规定当事件发生时运行的函数。
     * 当使用 one() 方法时，每个元素只能运行一次事件处理器函数。
     * @param eventName 事件名称
     * @param fun 绑定方法
     * @param params 绑定参数
     * @param scope 执行域
     * @returns {*}
     */
    one: function (eventName, fun, params, scope) {
        this.addListener(eventName, fun, params, scope, true);
        return this;
    },

    /**
     * 为事件添加执行方法
     */
    addListener: function (eventName, fun, params, scope, one) {
        if (!Fantasy.isArray(params)) {//params 不是数组类型的话,表示该参数代表scope
            scope = params;
            params = null;
        }
        scope = !scope ? this : scope;
        if (this.events.indexOf(eventName) > -1) {
            if (!this.getListeners().containsKey(eventName)) {
                this.getListeners().put(eventName, new Fantasy.util.Collection());
            }
            var _listener = this.getListeners().get(eventName);
            _listener.add({method: fun, params: params, scope: scope, one: one});
        } else {
            throw new Error('Event:' + eventName + ',不存在!');
        }
    },

    /**
     * 移除事件的执行方法
     */
    un: function (eventName, fun) {
        return this.removeListener(eventName, fun);
    },

    /**
     * 移除事件的执行方法
     */
    removeListener: function (eventName, fun) {
        var funIndexs = [];
        if (this.getListeners().containsKey(eventName)) {
            var eventFunctions = this.getListeners().get(eventName);
            eventFunctions.each(function (index) {
                if ((this.method == fun) || !fun) {
                    funIndexs.push(index);
                }
            });
            funIndexs.each(function () {
                eventFunctions.remove(this);
            });
        }
        return this;
    }

});