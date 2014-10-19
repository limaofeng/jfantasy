//============================================================
//[描    述] static		字符串处理帮助类
//============================================================
Fantasy.util.StringUtil = new (Fantasy.util.jClass({

    jClass: 'Fantasy.util.StringUtil',
    
    initialize: function(){
    
    },
    
    /**
     * 名称：formatHtml 属于:StringUtil 描述：需要格式化的字符串 source 根据 source 串中 {属性名} 从
     * object 对象中查找其对应的值
     *
     * @param source
     * @param object
     * @return
     */
    formatHtml: function(source, object){
        if (jQuery.browser.msie) {
            return source.replace(/(\value={+)?value={(\S+)}(?!\}+)?/g, function(){
                if (arguments[1]) {
                    return arguments[0];
                }
                if (!Fantasy.isNull(arguments[2])) {
                    return arguments[0].replace("value=", "value=\"") + "\"";
                }
            });
        }
        if (!Fantasy.isNull(object)) {
            return source.replace(/(\{+)?{(\S+)}(?!\}+)?/g, function(){
                if (arguments[1]) {
                    return arguments[0];
                }
                if (!Fantasy.isNull(arguments[2])) {
                    try {
                        var value = eval("object." + arguments[2]);
                    } 
                    catch (e) {
                        value = "";
                    }
                    return Fantasy.isNull(value) ? "" : value;
                }
            });
        }
        else {
            return source.replace(/(\{+)?{(\S+)}(?!\}+)?/g, function(){
                if (arguments[1]) {
                    return arguments[0];
                }
                if (!Fantasy.isNull(arguments[2])) {
                    return "";
                }
            });
        }
    },
    
    /**
     * 字符在字符串中出现的次数
     */
    occurTimes: function(source, a){
        return '';
    },
    
    /**
     * 名称：isNull 属于:Fantasy.util.StringUtil 描述：判断字符串是否为空
     *
     * @param source
     * @return
     */
    isNull: function(source){
        return typeof source == "undefined" || source == null || source == "";
    },
    
    /**
     * 名称：nullValue 属于:Fantasy.util.StringUtil 描述：返回字符串如果为null 同时 defaultValue 不为 Null
     * 返回defaultValue 否则返回""
     *
     * @param source
     * @param defaultValue
     * @return
     */
    nullValue: function(source, defaultValue){
        if (!source && !defaultValue) {
            return '';
        }
        return this.isNull(source) ? defaultValue.toString() : source.toString();
    },
    
    /**
     * 检查一个引用值是否为空，若是则转换到缺省值。
     * @param {Mixed} value 要检查的引用值
     * @param {String} defaultValue 默认赋予的值（默认为""）
     * @return {String}
     */
    defaultValue : function(value, defaultValue){
        return value !== undefined && value !== '' && value !== null ? value : (defaultValue ? defaultValue : '');
    },
    
    /**
     * 返回数字 如果 source 为NUll 则返回0 len 为数字保留的小数位
     */
    numberValue: function(source, len){
    },
    
    /**
     * 首字母大写
     */
    upperCaseFirst: function(source){
        return "";
    },
    
    /**
     * 字符串中是否含有中文
     */
    includeChinese: function(source){
        return false;
    },
    
    /**
     * 名称：replaceAll 属于:String 描述：左边补零以满足长度要求
     *
     * @param source
     *            源字符串
     * @param resultLength
     *            最终长度
     * @return
     */
    addZeroLeft: function(source, resultLength){
        if (source == null) 
            return "";
        var result = this.nullValue(source);
        if (result.length < resultLength) {
            var i = result.length;
            while (i++ < resultLength) {
                result = "0" + result;
                if (result.length == resultLength) 
                    break;
            }
        }
        return result;
    },
    
    /**
     * 获取相同字符的个数；
     */
    getSameCharCount: function(str1, str2){
        return count;
    },
    
    filterHtml: function(input, length){
        var str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "");
        str = str.replaceAll("[(/>)<]", "");
        return str;
    },
    
    /**
     * 判断字符串是否与指定字符串开始的
     */
    startsWith: function(source, prefix){
        return source.startsWith(prefix);
    },
    
    /**
     * 判断字符串是否与指定字符串结束的
     */
    endsWith: function(source, prefix){
        return "";
    },
    
	length : function(source) {
    	return source.replaceAll("[^\\x00-\\xff]", "rr").length();
	},

	/**
	 * 计算图片比例的帮助类
	 * @param confine 实际比例
	 * @param orig 建议比例
	 * @returns
	 */
	measure : function(confine,orig){
		var confineRatio = confine.split('x');
		var origRatio = orig.split('x');
		var newWidth = 0, realWidth = Fantasy.toInt(origRatio[0]), width = Fantasy.toInt(confineRatio[0]);
		var newHeight = 0, realHeight = Fantasy.toInt(origRatio[1]), heigth = Fantasy.toInt(confineRatio[1]);
		if ((realWidth >= width) && (width > 0)) {
			newWidth = width;
			newHeight = realHeight * newWidth / realWidth;
		}
		if ((realHeight >= heigth) && (heigth > 0)) {
			newHeight = heigth;
			newWidth = realWidth * newHeight / realHeight;
		}
		return Fantasy.toInt(newWidth) + "x" + Fantasy.toInt(newHeight);
	} 
    
}))();

/**
 * {@link Fantasy.util.StringUtil#addZeroLeft}的简写方式
 * @member Fantasy addZeroLeft
 * @method */
Fantasy.addZeroLeft = Fantasy.util.StringUtil.addZeroLeft;

Fantasy.defaultValue = Fantasy.util.StringUtil.nullValue;

//Fantasy.defaultValue = Fantasy.util.StringUtil.defaultValue;

Fantasy.length = Fantasy.util.StringUtil.length;

Fantasy.measure = Fantasy.util.StringUtil.measure;
