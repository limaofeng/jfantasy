/**
 * 用于对象生成属性的get与set方法.并能通过defaultValues参数初始化数据
 * @param {Object} $super
 * @param {Object} propertys
 * @param {Object} defaultValues
 */
Fantasy.util.jClass(Fantasy.util.Observable, {
    jClass: 'Fantasy.data.Property',
    
    initialize: function($super, fantasyObj, propertys){
        $super();
        this.fantasyObj = fantasyObj;
        this.addPropertys(propertys);//生成set与get方法
    },
    
    addPropertys: function(propertys){
        var zthis = this;		
        propertys.each(function(){//生成set与get方法
            zthis.addProperty(this);
        });
    },
    
    addProperty: function(name, value){
        var zthis = this;
        var jClass = this.fantasyObj;
        
        var property = name.toString();
        var propertyName = property.upperCaseFirst();
        var setPropertyName = 'set' + propertyName;
        var getPropertyName = 'get' + propertyName;
        zthis.addEvents(setPropertyName);//为set及get方法注册事件
        zthis.addEvents(getPropertyName);
        /***********************这一块可能比较乱*************************/
        jClass[setPropertyName] = jClass[setPropertyName] ? (function(name, fn){
            return function(){
                var that = this;
                if (Fantasy.argumentNames(fn)[0] === '$private') {
                    $private = zthis[name] = function(val){
                        zthis[property] = val;
                        zthis.fireEvent(setPropertyName, that, arguments);
                    };
                    return fn.apply(this, Array.prototype.concat.apply($private, arguments));
                }
                else {
                    return fn.apply(this, arguments);
                }
            };
        })(setPropertyName, jClass[setPropertyName]) : (function(){
            return function(val){
                zthis[property] = val;
                zthis.fireEvent(setPropertyName, this, zthis[property]);
            };
        })();
        jClass[getPropertyName] = jClass[getPropertyName] ? (function(name, fn){
            return function(){
                if (Fantasy.argumentNames(fn)[0] === '$private') {
                    var that = this;
                    $private = zthis[name] = function(){
                        zthis.fireEvent(getPropertyName, that, Array.prototype.concat.apply(new Array(zthis[property]), arguments));
                        return zthis[property];
                    };
                    return fn.apply(this, Array.prototype.concat.apply($private, arguments));
                }
                else {
                    return fn.apply(this, arguments);
                }
            };
        })(getPropertyName, jClass[getPropertyName]) : (function(){
            return function(){
                zthis.fireEvent(getPropertyName, this, Array.prototype.concat.apply(new Array(zthis[property]), Array.prototype.concat.apply(new Array(zthis[property]), arguments)));
                return zthis[property];
            };
        })();
        /************************************************/
        
        if (typeof value != 'undefined') {
            this.setPropertyValue(new Object()[name] = value);
        }
    },
    
    setPropertyValue: function(values, defaultValues){
        if (defaultValues && typeof defaultValues === 'object') {
            this.setPropertyValue(defaultValues);
        }
        if (values && typeof values === 'object') {
            var jClass = this.fantasyObj;
            for (var property in values) {
                var propertyName = property.toString().upperCaseFirst();
                var setPropertyName = 'set' + propertyName;
                var fn = jClass[setPropertyName];
                if (fn && typeof fn === 'function' && typeof values[property] != 'undefined') 
                    fn.apply(this.fantasyObj, [values[property]]);
            }
        }
    }
});
