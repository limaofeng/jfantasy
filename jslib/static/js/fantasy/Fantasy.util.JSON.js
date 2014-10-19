/**
 *  JSON 数据转换类
 *  		参考 Ext.util.JSON 修改 decode() 方法。
 */
Fantasy.util.JSON = new (Fantasy.util.jClass((function(){

    var useHasOwn = {}.hasOwnProperty ? true : false;
    
    var pad = function(n){
        return n < 10 ? "0" + n : n;
    };
    
    var m = {
        "\b": '\\b',
        "\t": '\\t',
        "\n": '\\n',
        "\f": '\\f',
        "\r": '\\r',
        '"': '\\"',
        "\\": '\\\\'
    };
    
    var encodeString = function(s){
        if (/["\\\x00-\x1f]/.test(s)) {
            return '"' +
            s.replace(/([\x00-\x1f\\"])/g, function(a, b){
                var c = m[b];
                if (c) {
                    return c;
                }
                c = b.charCodeAt();
                return "\\u00" +
                Math.floor(c / 16).toString(16) +
                (c % 16).toString(16);
            }) +
            '"';
        }
        return '"' + s + '"';
    };
    
    var encodeArray = function(o){
        var a = ["["], b, i, l = o.length, v;
        for (i = 0; i < l; i += 1) {
            v = o[i];
            switch (typeof v) {
                case "undefined":
                case "function":
                case "unknown":
                    break;
                default:
                    if (b) {
                        a.push(',');
                    }
                    a.push(v === null ? "null" : Fantasy.util.JSON.encode(v));
                    b = true;
            }
        }
        a.push("]");
        return a.join("");
    };
    
    var encodeDate = function(o){
        return '"'+o.toString()+'"';
    };
    
    return {
    
        jClass: 'Fantasy.util.JSON',
		
        initialize: function(){        
        },
        
        /**
         * 对一个对象，数组，或是其它值编码。
         * @param {Mixed} o 要编码的变量
         * @return {String} JSON字符串
         */
        encode: function(o){
            if (typeof o == "undefined" || o === null) {
                return "null";
            }else if (o instanceof Array) {
				return encodeArray(o);
			}else if (o instanceof Date) {
				return encodeDate(o);
			}else if (typeof o == "string") {
				return encodeString(o);
			}else if (typeof o == "number") {
				return isFinite(o) ? String(o) : "null";
			}else if (typeof o == "boolean") {
				return String(o);
    		}else if (o instanceof jQuery){
				return '[object jQuery]';
			}else {
				var a = ["{"],b,i,v;
				for (i in o) {
					if (!useHasOwn || o.hasOwnProperty(i)) {
						v = o[i];
						switch (typeof v) {
							case "undefined":
							case "function":
							case "unknown":
							break;
						default:
							if (b) {
								a.push(',');
							}
							a.push(this.encode(i), ":",v === null ? "null" : this.encode(v));
							b = true;
						}
					}
				}
				a.push("}");
				return a.join("");
			}
        },
        
        /**
         * 将JSON字符串解码（解析）成为对象。如果JSON是无效的，该函数抛出一个“语法错误”。
         * @param {String} json JSON字符串
         * @return {Object} 对象
         */
        decode: function(json){
			var javaDate = /(\"(Mon|Tue|Wed|Thu|Fri|Sat|Sun) (Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) [0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2} (GMT|UT|UTC|CST) [0-9]{4}\")/g;
			var jsDate = /(\"(Mon|Tue|Wed|Thu|Fri|Sat|Sun) (Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) [0-9]{2} [0-9]{4} [0-9]{2}:[0-9]{2}:[0-9]{2} (GMT|UT|UTC|CST)[+][0-9]{4}\")/g;
			var jsonData = /(\"[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}\")/g;
			if(javaDate.test(json)){
				json = json.replace(javaDate,'Date.parse($1)');
			}else if(jsDate.test(json)){
				json = json.replace(jsDate,'Date.parse($1)');
			}else if(jsonData.test(json)){
				json = json.replace(jsonData,'Date.parse($1,"yyyy-MM-ddThh:mm:ss")');
			}
           return (new Function('return ' + json))();//eval('(' + json + ')');
        }
    };
    
})()))();
/**
 * {@link Fantasy.util.JSON#encode}的简写方式
 * @member Fantasy encode
 * @method */
Fantasy.encode = Fantasy.util.JSON.encode;
/**
 * {@link Fantasy.util.JSON#decode}的简写方式
 * @member Fantasy decode
 * @method */
Fantasy.decode = Fantasy.util.JSON.decode;
