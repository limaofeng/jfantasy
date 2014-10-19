//============================================================
//[描    述] static		Object帮助类
//============================================================
Fantasy.util.ObjectUtil = new (Fantasy.util.jClass((function(){

    var javaDate = /(\"(Mon|Tue|Wed|Thu|Fri|Sat|Sun) (Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) [0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2} (GMT|UT|UTC|CST) [0-9]{4}\")/;
    var jsDate = /(\"(Mon|Tue|Wed|Thu|Fri|Sat|Sun) (Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) [0-9]{2} [0-9]{4} [0-9]{2}:[0-9]{2}:[0-9]{2} (GMT|UT|UTC|CST)[+][0-9]{4}\")/;
    
    return {
        jClass: 'Fantasy.util.ObjectUtil',
        
        initialize: function(){        
        },
        
        /**
         * 浏览器判断
         */
        Browser: {
            isMsie: jQuery.browser.msie,
            isSafari: jQuery.browser.safari,
            isMozilla: jQuery.browser.mozilla,
            isOpera: jQuery.browser.opera,
            version: parseInt(jQuery.browser.version)
        },
        
        /**
         * 判断 Object 是不是数组
         */
        isArray: function(object){
            return Object.prototype.toString.call(object) === '[object Array]';
        },
        
        /**
         * 判断 Object 是不是数组
         */
        isString: function(object){
            return Object.prototype.toString.call(object) === '[object String]';// instanceof
        },
        
        /**
         *
         * @param {Object} object
         */
        isBoolean: function(object){
            return Object.prototype.toString.call(object) === '[object Boolean]';
        },
        
        /**
         * 判断对象是否是函数
         */
        isFunction: function(object){
            return jQuery.isFunction(object);
        },
        
        /**
         * 判断是否是有效的时间格式
         */
        isDate: function(str){
            return str instanceof Date| javaDate.test(str) || jsDate.test(str);
        },
        
		/**
         * 判断是否是数字
         * @param {Object} s
         */
        isNumber: function(s){
            var patrn = /^[]{0,1}[0-9]{0,}[.]{0,1}[0-9]{0,}$/;
            return patrn.exec(s);

        },
        
        isWindow: function(object){
            return Object.prototype.toString.call(object) === '[object Window]';
        },
		
		argumentNames : function (fn){
        	var names = fn.toString().match(/^[\s\(]*function[^(]*\(([^\)]*)\)/)[1].replace(/\s+/g, '').split(',');
        	return names.length == 1 && !names[0] ? [] : names;
    	},
		
        /**
         *
         * @param {Object} obj
         */
        clone: function(obj){
            var objClone;
            if (obj.constructor == Object || $.isPlainObject(obj)) {
                objClone = new obj.constructor();
            } else {
                objClone = new obj.constructor(obj.valueOf());
            }
            
            for (var key in obj) {
                if (objClone[key] != obj[key]) {
                	if(Fantasy.isWindow(obj[key])){
                		objClone[key] = obj[key];
                	}else if(obj[key] instanceof jQuery){
                		objClone[key] = obj[key].clone(true);
                	}else if (typeof(obj[key]) == 'object' && obj[key].clone != null) {
                        objClone[key] = obj[key].clone();
                    } else {
                        objClone[key] = obj[key];
                    }
                }
            }
            objClone.toString = obj.toString;
            objClone.valueOf = obj.valueOf;
            return objClone;
        },
        
        /**
         * 对象是否为空对象
         */
        isNull: function(object){
            return typeof object == "undefined" || object == null || this.isString(object) && Fantasy.util.StringUtil.isNull(object);
        },
		        
        /**
         *
         * @param {Object} object
         */
        nullValue: function(object){
            return this.isNull(object) ? new Object("") : object;
        },
        
        /**
         *
         * @param {Object} source
         */
        toNumber: function(source){
            return this.isNumber(source) ? Number(source) : 0;
        },
        
        isFantasy: function(source,jClass){
        	if(jClass){
        		jClass.toString().equals(source.toString());
        	}else{
        		return new RegExp('^(\\[object Fantasy\\.)[A-Za-z0-9.]+]$').test(source.toString());
        	}
        },
        
        toArray: function(source){
        	return this.isArray(source)?source:[source];
        },
        
        /**
         * 从请求参数中获取配置的参数：
         * @param url
         * @return
         */
        parseQuery: function(url){
        	var query = url.indexOf('?') > -1 ? url.replace(/^[^\?]{0,}\??/, '') : url;
            var Params = {};
            if (!query) {
                return Params;
            }// return empty object
            var Pairs = query.split(/[;&]/);
            for (var i = 0; i < Pairs.length; i++) {
                var KeyVal = Pairs[i].split('=');
                if (!KeyVal || KeyVal.length != 2) {
                    continue;
                }
                var key = decodeURIComponent(KeyVal[0]);//decodeURIComponent
                var val = decodeURIComponent(KeyVal[1]);//unescape
                if(Params[key]){
                	if(!Fantasy.isArray(Params[key])){
                		Params[key] = [Params[key]];
                	}
                	Params[key].push(val);
                }else{
                	Params[key] = val;
                }
            }
            return Params;
        },
        
        /**
         * Fantasy.util.Collection 和  Array 的排序方法
         * @param {Array|Collection} source
         * @param {function} equals
         */
        sort: function(source, equals){
            if (source instanceof Array) {
                for (var i = 0; i < source.length; i++) {
                    var isBreak = true;
                    for (var j = 0; j < source.length - i - 1; j++) {
                        if (equals.apply(source[j], [source[j + 1]])) {
                            var temp = source[j];
                            source[j] = source[j + 1];
                            source[j + 1] = temp;
                            isBreak = false;
                        }
                    }
                    if (isBreak) {
                        break;
                    }
                }
            }
            else 
                if (source == '[object Fantasy.util.Collection]') {
                    for (var i = 0; i < source.size(); i++) {
                        for (var j = 0; j < source.size() - i - 1; j++) {
                            if (equals.apply(source.get(j), [source.get(j + 1)])) {
                                var temp = source.get(j);
                                source.set(j, source.get(j + 1));
                                source.set(j + 1, temp);
                            }
                        }
                    }
                }
            return source;
        },
        
        /**
         *
         * @param {Object} source
         * @param {Object} query
         */
        search: function(source, query){
            if ((source instanceof Array) || ((source instanceof jQuery) && source.length > 0)) {
                var retVal = new Array();
                source.each(function(){
                    if (query.apply(this, [])) {
                        retVal.push(this);
                    }
                });
                return retVal;
            }
            else 
                if (source instanceof Fantasy.util.Collection) {
                    var retVal = new Fantasy.util.Collection();
                    source.each(function(){
                        if (query.apply(this, [])) {
                            retVal.add(this);
                        }
                    });
                    return retVal;
                }
        },
        
        /**
         *
         * @param {Object} dest
         * @param {Object} source
         * @return {Object}
         */
        copy: function(dest,orig,excludeProperties){
            for (var p in orig) {
            	if(Fantasy.toArray(excludeProperties).indexOf(p)<0){
                	dest[p] = orig[p];
                }
            }
            return dest;
        },
		
		getValue : function(json,fieldName){
			if(json.hasOwnProperty(fieldName)){
				return json[fieldName];
			}else{
				var newName = fieldName.substr(0,fieldName.indexOf('.'));
				if(json.hasOwnProperty(newName)){
					return arguments.callee(json[newName],fieldName.substr(fieldName.indexOf('.')+1));
				}else{
					return null;
				}				
			}
		},
		
		each : function(o,f){
			for(var i=0;i<o.length;i++){
				f.apply(o[i]);
			}
		},
		
		indexOf : function(array,fName,fValue){
			var retv = array.each(function(i,v){
				if(this[fName] == fValue){
					return i;
				}
			});
			return retv != null ? retv : -1;
		}
		
    };
    
})()))();
/**
 * {@link Fantasy.util.ObjectUtil#isArray}的简写方式
 * @member Fantasy isArray
 * @method */
Fantasy.isArray = Fantasy.util.ObjectUtil.isArray;
/**
 * {@link Fantasy.util.ObjectUtil#isString}的简写方式
 * @member Fantasy isString
 * @method */
Fantasy.isString = Fantasy.util.ObjectUtil.isString;
/**
 * {@link Fantasy.util.ObjectUtil#isBoolean}的简写方式
 * @member Fantasy isBoolean
 * @method */
Fantasy.isBoolean = Fantasy.util.ObjectUtil.isBoolean;
/**
 * {@link Fantasy.util.ObjectUtil#isFunction}的简写方式
 * @member Fantasy isFunction
 * @method */
Fantasy.isFunction = Fantasy.util.ObjectUtil.isFunction;
/**
 * {@link Fantasy.util.ObjectUtil#isDate}的简写方式
 * @member Fantasy isDate
 * @method */
Fantasy.isDate = Fantasy.util.ObjectUtil.isDate;
/**
 * {@link Fantasy.util.ObjectUtil#isNumber}的简写方式
 * @member Fantasy isNumber
 * @method */
Fantasy.isNumber = Fantasy.util.ObjectUtil.isNumber;
/**
 * {@link Fantasy.util.ObjectUtil#isFantasy}的简写方式
 * @member Fantasy isFantasy
 * @method */
Fantasy.isFantasy = Fantasy.util.ObjectUtil.isFantasy;

Fantasy.isWindow = Fantasy.util.ObjectUtil.isWindow;

/**
 * {@link Fantasy.util.ObjectUtil#argumentNames}的简写方式
 * @member Fantasy argumentNames
 * @method */
Fantasy.argumentNames = Fantasy.util.ObjectUtil.argumentNames;

Fantasy.isNull = Fantasy.util.ObjectUtil.isNull;

Fantasy.nullValue = Fantasy.util.ObjectUtil.nullValue;

Fantasy.clone = Fantasy.util.ObjectUtil.clone;

Fantasy.Browser = Fantasy.util.ObjectUtil.Browser;

Fantasy.getValue = Fantasy.util.ObjectUtil.getValue;

Fantasy.sort = Fantasy.util.ObjectUtil.sort;

Fantasy.parseQuery = Fantasy.util.ObjectUtil.parseQuery;

Fantasy.search = Fantasy.util.ObjectUtil.search;

Fantasy.toArray = Fantasy.util.ObjectUtil.toArray;

Fantasy.toNumber = Fantasy.util.ObjectUtil.toNumber;

Fantasy.copy = Fantasy.util.ObjectUtil.copy;

Fantasy.each = Fantasy.util.ObjectUtil.each;

Fantasy.indexOf = Fantasy.util.ObjectUtil.indexOf;