/**
 * @class Fantasy.data.SortTypes
 * @singleton
 * 定义一些缺省的比较函数，供排序时使用。	
 * 		参考 Ext.data.SortTypes
 */
Fantasy.data.SortTypes = {
    /**
     * 默认的排序即什么也不做。
     * Default sort that does nothing
     * @param {Mixed} s 将被转换的值将被转换的值。The value being converted
     * @return {Mixed} 用来比较的值所比较的值。The comparison value
     */
    none : function(s){
        return s;
    },
    
    /**
     * 用来去掉标签的规则表达式。
     * The regular expression used to strip tags
     * @type {RegExp}
     * @property stripTagsRE
     */
    stripTagsRE : /<\/?[^>]+>/gi,
    
    /**
     * 去掉所有html标签来基于纯文本排序。
     * Strips all HTML tags to sort on text only
     * @param {Mixed} s 将被转换的值将被转换的值。The value being converted
     * @return {String} 用来比较的值所比较的值。The comparison value
     */
    asText : function(s){
        return String(s).replace(this.stripTagsRE, "");
    },
    
    /**
     * 去掉所有html标签来基于无大小写区别的文本的排序。
     * Strips all HTML tags to sort on text only - Case insensitive
     * @param {Mixed} s 将被转换的值。The value being converted
     * @return {String} 所比较的值所比较的值。The comparison value
     */
    asUCText : function(s){
        return String(s).toUpperCase().replace(this.stripTagsRE, "");
    },
    
    /**
     * 无分大小写的字符串。
     * Case insensitive string
     * @param {Mixed} s 将被转换的值。The value being converted
     * @return {String} 所比较的值。The comparison value
     */
    asUCString : function(s) {
    	return String(s).toUpperCase();
    },
    
    /**
     * 对日期排序。
     * Date sorting
     * @param {Mixed} s 将被转换的值。The value being converted
     * @return {Number} 所比较的值。The comparison value
     */
    asDate : function(s) {
        if(!s){
            return 0;
        }
        if(Fantasy.isDate(s)){
            return s.getTime();
        }
    	return Date.parse(String(s));
    },
    
    /**
     * 浮点数的排序。
     * Float sorting
     * @param {Mixed} s 将被转换的值。The value being converted
     * @return {Float} 所比较的值。The comparison value
     */
    asFloat : function(s) {
    	var val = parseFloat(String(s).replace(/,/g, ""));
        if(isNaN(val)) val = 0;
    	return val;
    },
    
    /**
     * 整数的排序。
     * Integer sorting
     * @param {Mixed} s 将被转换的值。The value being converted
     * @return {Number} 所比较的值。The comparison value
     */
    asInt : function(s) {
        var val = parseInt(String(s).replace(/,/g, ""));
        if(isNaN(val)) val = 0;
    	return val;
    }
};