Fantasy.namespace("Fantasy.util");
//============================================================
//[描    述] static	存储所有被创建的类
//============================================================
Fantasy.util.Library = [];
//============================================================
//[描    述] static	用于创建JS类
//============================================================
Fantasy.util.jClass = function (baseClass, prop) {

    //没有父类情况,默认 Fantasy.util.Object 为父类
    if (typeof(baseClass) === 'object') {
        prop = baseClass;
        Fantasy.util.Object != prop ? baseClass = Fantasy.util.Object : baseClass = null;
    }

    if (!prop.hasOwnProperty('jClass')) {
        throw new Error('利用Fantasy.util.jClass 创建对象,必须拥有jClass属性!');
        return;
    }
    else if (!(typeof(prop.jClass) === 'string')) {
        throw new Error('jClass属性必须为 String 类型,jClass{' + prop.jClass + ':' + typeof(prop.jClass) + '}');
        return;
    }

    //Class类的原型
    function Class() {
        if (baseClass) {
            this.baseprototype = baseClass.prototype;
        }
        if (typeof this.initialize === 'undefined') {
            var callee = arguments.callee.prototype.initialize.apply(this, arguments);
            for (p in arguments.callee.prototype) {
                if (!callee.hasOwnProperty(p)) {
                    callee[p] = arguments.callee.prototype[p];
                }
            }
            return callee;
        }
        else {
            this.initialize.apply(this, arguments);
        }
    }

    // 如果此类需要从其它类扩展
    if (baseClass) {
        var middleClass = function () {
        };
        middleClass.prototype = baseClass.prototype;
        Class.prototype = new middleClass();
        Class.prototype.constructor = Class;
    }

    // 覆盖父类的同名函数
    for (var name in prop) {
        if (prop.hasOwnProperty(name)) {
            //这块比较难理解,简单点说， 如果对象的属性是方法。且第一个参数是$super 的时候，就为该方法加一个代理(这里用一个函数返回函数),作用是为了传递父类同方法对象。
            if (baseClass && typeof(prop[name]) === 'function' && argumentNames(prop[name])[0] === '$super') {
                Class.prototype[name] = (function (name, fn) {
                    return function () {
                        var that = this;
                        $super = function () {
                            return baseClass.prototype[name].apply(that, arguments);
                        };
                        return fn.apply(this, Array.prototype.concat[arguments.length == 1 ? 'call' : 'apply']($super, arguments));
                    };
                })(name, prop[name]);
            }
            else {
                Class.prototype[name] = prop[name];
            }
        }
    }
    if (Object.prototype.toString != prop['toString']) {//TODO IE在json对象中的toString不能遍历的问题
        Class.prototype['toString'] = prop['toString'];
    }

    //获取函数的参数名
    function argumentNames(fn) {
        try {
            var names = fn.toString().match(/^[\s\(]*function[^(]*\(([^\)]*)\)/)[1].replace(/\s+/g, '').split(',');
            return names.length == 1 && !names[0] ? [] : names;
        } catch (e) {
            return [];
        }
    }

    Fantasy.namespace(prop.jClass);//检查命名空间
    eval(prop.jClass + '=Class;');
    Fantasy.util.Library.push({
        key: prop.jClass,
        value: Class
    });
    delete Class.prototype.jClass;

    Fantasy.apply(Class, {}, {

        toString: function () {
            return Class.getClass().toString();
        },

        getClass: function () {//生成获取类[Fantasy.util.Class]对象的静态方法
            return Fantasy.util.Class.forName(Class);
        }
    });

    return Class;
};
