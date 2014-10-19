Fantasy.apply(String, {

	/**
	 * 字符串替换方法,再不传递第二个参数时,IE会默认为 undefined
	 * @param {Object} regText
	 * @param {Object} replaceText
	 */
	replace : (function(replace) {
		return function(regText, replaceText) {
			if( typeof replaceText == "undefined" || replaceText == null)
				replaceText = '';
			return replace.apply(this, [regText, replaceText]);
		};
	})(String.prototype.replace),

	/**
	 * 名称：replaceAll		属于:String
	 * 描述：字符串替换方法。替换所有
	 * @param regText
	 * @param replaceText
	 * @return
	 */
	replaceAll : function(raRegExp, replaceText) {
		if( typeof raRegExp == "undefined" || raRegExp == null)
			return;
		if(!( raRegExp instanceof RegExp))
			raRegExp = new RegExp(raRegExp, "g");
		return this.replace(raRegExp, replaceText);
	},
	/**
	 * 名称：trim		属于:String
	 * 描述：去除字符串首尾的空格
	 * @return
	 */
	trim : function() {
		return jQuery.trim(this);
	},
	/**
	 * 名称：startsWith		属于:String
	 * 描述：判断字符串是否以指定字符串开头
	 * @param prefix
	 * @return String
	 */
	startsWith : function(prefix) {
		prefix = prefix.replace('/', '\/');
		var pattern = new RegExp('^' + prefix);
		return pattern.test(this);
	},
	/**
	 * 首字母大写
	 */
	upperCaseFirst : function() {
		return this.replace(/\b\w+\b/g, function(word) {
			return word.substring(0, 1).toUpperCase() + word.substring(1);
		});
	},
	/**
	 * 名称：endsWith		属于:String
	 * 描述：判断是否以指定字符串结尾
	 * @param prefix
	 * @return
	 */
	endsWith : function(prefix) {
		var pattern = new RegExp(prefix + '$');
		return pattern.test(this);
	},
	/**
	 * 名称：equals		属于:String
	 * 描述：判断字符串的值是否相等
	 * @param text
	 * @return
	 */
	equals : function(text) {
		if(text == null)
			return false;
		return this == text.toString();
	},
	/**
	 * 字符串反转
	 */
	reverse : function() {
		return this.split('').reverse().join("");
	},
	/**
	 * 在字符串中的每个字符上运行一个函数
	 * @param {Object} fun	如果fun函数有返回值的,fil函数为null,each方法返回该值
	 * @param {Object} fil	如果fun函数有返回值的,fil函数将会触发,如果fil 也有返回值,该值为each方法的返回值.
	 */
	each : function(fun, fil) {
		for(var i = 0, length = this.length; i < length; i++) {
			var retVal = fun.call(this[i], i, this);
			if(!fil) {
				if(!( typeof retVal == "undefined" || retVal == null)) {
					return retVal;
				}
			} else {
				retVal = fil.call(this[i], i, this, retVal);
				if(!( typeof retVal == "undefined" || retVal == null)) {
					return retVal;
				}
			}
		}
	},
	
	clone : function() {
		return this.toString();
	},
	
	/**
	 * 名称：addZeroLeft 
	 * 属于:String 
	 * 描述：左边补零以满足长度要求
	 * @param resultLength
	 *            最终长度
	 * @return
	 */
	addZeroLeft : function(resultLength) {
		var result = this.clone();
		if(result.length < resultLength) {
			var i = result.length;
			while(i++ < resultLength) {
				result = "0" + result;
				if(result.length == resultLength)
					break;
			}
		}
		return result;
	},
	
	/**
	 * 返回一个字符串，该字符串中的第一个字母转化为大写字母，剩余的为小写。
	 */
	capitalize : function(){
		return Fantasy.util.Format.capitalize(this);
	},
	
	/**
	 * 转换为Unicode代码
	 */
	unicode : function(){
		return escape(this).toLocaleLowerCase().replace(/%u/gi, '\\u');
	},
	
	/**
	 * Unicode代码转换为汉字
	 */
	decodeUnicode : function(){
		return unescape(this.replace(/\\u/gi, '%u'));
	}
	
}, {
	
	isNull : function(object) {
		return Fantasy.util.StringUtil.isNull(object);
	},
	
	escape : function(text){
		return Fantasy.htmlEncode(text);
	},
	
	unescape : function(text){
		return Fantasy.htmlDecode(text);
	}
	
});