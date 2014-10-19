/**
 * 模拟一种无侵入的日志管理方式
 * @param {Object} args
 */
Fantasy.util.jClass((function(){
    /**
     * 用于输出方法的参数
     * @param {Object} args
     */
    function encodeArguments(args){
        var str = [];
        for (var i = 0, length = args.length; i < length; i++) {
            str = str.concat(Fantasy.encode(args[i])).concat(i < length - 1 ? ',' : '');
        }
        return str;
    }
    return {
        jClass: 'Fantasy.util.Logger',
        
        initialize: function(jClass){
            this.target = jClass;
        },
        
        getTemplate: function(){
            if (!this.template) {
                this.template = new Fantasy.util.Template({
                    html: '{time} {type} [{fileName}](line:{lineNumber}) Class {className}.{functionName}({args}) {msg}',
                    compiled: false
                });
            }
            return this.template;
        },
        
        setFormat: function(format){
            this.format = format;
        },
        
        isDebugEnabled: function(){
            return jQuery.browser.mozilla && Fantasy.util.Logger.isCategory('DEBUG', new Error('').stack.split('\n')[3].split('@')[1].replace(new RegExp('\S+'+request.getContextPath())).split(':')[0]);
        },
        
        debug: function(msg){
            if (this.isDebugEnabled()) {
                var data = new Object();
                data.time = new Date().format('hh:mm:ss ms');
                data.type = this.getClass().getFunctionName(arguments.callee).toUpperCase();
                data.className = this.target.toString();
                data.functionName = this.target.getFunctionName(Fantasy.util.Logger.prototype.debug.caller);
                var info = new Error('').stack.split('\n')[2].split('@')[1].replace(new RegExp('\S+'+request.getContextPath()));
                data.fileName = info.split(':')[0];
                data.lineNumber = parseInt(info.split(':')[1]);
                data.msg = msg;
                data.args = encodeArguments(Fantasy.util.Logger.prototype.debug.caller.arguments);
                console.log(this.getTemplate().applyTemplate(data));
            }
        },
        
        error: function(e){
            if (e instanceof Error) {
                /*
                 console.log(new Error('dddddddddd').message);
                 console.log(new Error('dddddddddd').fileName);
                 console.log(new Error('dddddddddd').lineNumber);
                 console.log(new Error('dddddddddd').name);
                 var stack = new Error('dddddddddd').stack;
                 */
                var data = new Object();
                data.debugName = this.getClass().getFunctionName(arguments.callee);
                data.className = this.target.toString();
                data.functionName = this.target.getFunctionName(Fantasy.util.Logger.prototype.debug.caller);
                console.log(data.debugName + '|' + data.className + '|' + data.functionName + '\t' + e.message + '|' + e.lineNumber);
            }
        },
        
        log: function(){
            var zthis = this;
            for (var i = 0, len = arguments.length; i < len; i++) {
                var f = arguments[i];
                this.jClass.prototype[f] = Fantasy.proxy(this.jClass.prototype[f], function(i){
                    zthis.template.setHtml(zthis.format.before);
                    console.log(zthis.template.applyTemplate({
                        'time': new Date().format('yyyy-MM-dd hh:mm:ss ms'),
                        'jClass': zthis.jClass.toString(),
                        'method': f,
                        'args': encodeArguments(i.getArgs())
                    }));
                    var obj = i.invoke();
                    zthis.template.setHtml(zthis.format.fulfill);
                    console.log(zthis.template.applyTemplate({
                        'time': new Date().format('yyyy-MM-dd hh:mm:ss ms'),
                        'jClass': zthis.jClass.toString(),
                        'method': f,
                        'ret': Fantasy.encode(obj)
                    }));
                    return obj;
                }, this.jClass.prototype);
            }
        }
    };
})());

Fantasy.apply(Fantasy.util.Logger, {}, (function(){
    var library = null;
    var categorys = null;
    
    var getCategorys = function(){
        if (!categorys) {
            categorys = new Fantasy.util.Map();
            categorys.put('DEBUG', new Array());
            categorys.put('ERROR', new Array());
        }
        return categorys;
    }
    
    var getLibrary = function(){
        if (!library) {
            library = new Fantasy.util.Map();
        }
        return library;
    }
    
    
    return {
    
        addCategory: function(type, category){
            if (getCategorys().containsKey(type)) {
                getCategorys().get(type).push(new RegExp(category));
            }
        },
        
        isCategory: function(type, path){
            if (getCategorys().containsKey(type)) {
                return getCategorys().get(type).each(function(){
                    return this.test(path) ? true : undefined;
                });
            }
        },
        
        getLogger: function(jClass){
            if (getLibrary().containsKey(jClass)) {
                return getLibrary().get(jClass);
            }
            var logger = new Fantasy.util.Logger(jClass);
            getLibrary().put(jClass, logger);
            return logger;
        }
    };
})());
Fantasy.Logger = Fantasy.util.Logger;
