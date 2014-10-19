/**
 * Sweet (Simple WEb front-End Template)
 * A lightweight javascript template with high performance
 *
 * This file is sweet's jquery plugin with some useful method.
 * You can use sweet alone by include sweet.js file.
 *
 * Copyright 2010, Mark One
 * http://code.google.com/p/sweet-template/
 *
 * Licensed under the MIT license
 */
(function($) {
    var subForPrefix = "__sub_foreach_",
        subIndexPrefix = "__index_",
        subTempIndexPrefix = "__index_tmp_",
        subTempVariablePrefix = "__var_tmp_",
        subLenPrefix = "__len_",
        printPrefix = "__buf__.push(";

    /**
     * Constructor of sweet template, no need to use 'new' keyword:
     *
     * <pre>
     * var tpl = Sweet([some template string...]);
     * </pre>
     * @param tplStr {string} Template string, could be any format with sweet delimiters (default to <[...]>)
     */
    var sweet = function( tplStr ) {

        // Prevent new operator
        if (!this.applyData)
            return new sweet(tplStr);

        var re = new RegExp("(.*?)" + sweet.startDelimiter + "(.*?)" + sweet.endDelimiter, "g"),
            foreachRe = /foreach[\s\xa0]*\([\s\xa0]*(\S+?)[\s\xa0]*(?:as[\s\xa0]*(\S+?)){0,1}?[\s\xa0]*\)[\s\xa0]*\{/g,
            tmpStr,
            i, l,      // loop variable
            subExprs = [],
            replaced = [];
        tmpStr = tplStr.replace(re, function(m, text, expr) {
            expr = trim(expr);
            if (text != "") {
                text = text.replace(/'/g, "\\'");
                replaced.push(printPrefix + '\'' + text + '\'');
                replaced.push(");");
                // deal with ?: expression
                if (expr.charAt(0) == ":") {
                    replaced[replaced.length - 1] = ")";
                }
            }
            if (expr != "") {
                if (expr.charAt(0) == "=") {
                    expr = printPrefix + expr.substr(1) + ');';
                } else {
                    if (!/[;\?\{\}:]/.test(expr.charAt(expr.length - 1)))
                        expr = expr + ";";
                }
                replaced.push(expr);
            }

            return "";
        });
        
        if (tmpStr) {
            replaced.push(printPrefix + '\'' + tmpStr.replace(/'/g, "\\'") + '\'' + ");");
        }

        // Join into a string, and deal with sub expression
        replaced = replaced.join('')
                .replace(foreachRe, function(m, varName, definedVarName) {
            var subExpr = {
                    type: "foreach",
                    varName: varName,
                    definedVarName: definedVarName || false
                },
                id = subExprs.push(subExpr) - 1,
                header = subForPrefix + id + "_{";
            subExpr.id = id;
            return header;
        });

        // replace sub expression
        for (i = 0, l = subExprs.length; i < l; i++) {
            replaced = replaceSubExpr(replaced, subExprs[i]);
        }

        replaced = ["var __buf__=[],$index=null;$util.print=function(str){__buf__.push(str);};try{with($data){",
            replaced, "}}catch(err){if(typeof console!='undefined' && typeof console.error == 'function'){console.error(err);}} return __buf__.join('');"].join('');

        this.compiled = new Function("$data", "$util", replaced);
    };

    /**
     * Public
     * Apply a json type data to tempalte
     * @param data {json}
     * @param scope {object} In your template's code, 'this' keywords will refer to the scope you assgined,
     *      default to window object
     * @return {string} replaced template string
     */
    sweet.prototype.applyData = function(data, scope) {
        var util = {};
        if (sweet.util) {
            var _util = sweet.util;
            for (var key in _util) {
                util[key] = _util[key];
            }
        }
        return this.compiled.call(scope || window, data, util);
    }

    /**
     * Private
     * Replaces sub expression with executable code
     * @param str {string} expression string
     * @param subExpr {object} sub expression object
     */
    function replaceSubExpr(str, subExpr) {
        var id = subExpr.id,
            varName = subExpr.varName,
            definedVarName = subExpr.definedVarName,
            indexName = subIndexPrefix + id,
            tmpIndexName = subTempIndexPrefix + id,
            tmpVarName = subTempVariablePrefix + id,
            lenName = subLenPrefix + id,
            indexVarName = [varName, "[", indexName, "]"].join(''),
            subRe = new RegExp(subForPrefix + id + "_{", "g"),
            braceRe = new RegExp("\{|\}", "g"),
            m, mbrace,
            subStr,
            index,
            lastIndex,
            unclosed = 0, prefix, suffix;
        if (definedVarName) {
            prefix = ["var ", tmpIndexName, "=$index;if(typeof ", definedVarName," !='undefined')var ",
                tmpVarName, "=", definedVarName,";else var ", definedVarName, "=null;for(var ",indexName,"=0,", lenName, "=", varName,
                ".length;",indexName, "<", lenName, ";", indexName, "++){$index=",indexName,
                ";", definedVarName, "=", indexVarName, ";with(",definedVarName, "){"].join('');
            suffix = ["}}$index=", tmpIndexName, ";if(typeof ", tmpVarName, "!='undefined')",
                definedVarName, "=", tmpVarName, ";"].join('');
        } else {
            prefix = ["var ", tmpIndexName, "=$index;for(var ",indexName,"=0,", lenName, "=", varName,
                ".length;",indexName, "<", lenName, ";", indexName, "++){$index=",indexName,
                ";with(",indexVarName, "){"].join('');
            suffix = "}}$index=" + tmpIndexName + ";";
        }
        m = subRe.exec(str);
        if (m) {
            index = m.index;
            lastIndex = subRe.lastIndex;
            subStr = str.substr(lastIndex);
            while ((mbrace = braceRe.exec(subStr))) {
                if (mbrace == "{") {
                    unclosed++;
                } else {
                    if (unclosed > 0)
                        unclosed--;
                    else {
                        subStr = subStr.substring(0, mbrace.index) + suffix + subStr.substr(braceRe.lastIndex);
                        break;
                    }
                }
            }
            str = str.substring(0, index) + prefix + subStr;
        }
        return str;
    }

    /**
     * Util function
     * Trims white spaces to the left and right of a string.
     * @param {string} str The string to trim.
     * @return {string} A trimmed copy of {@code str}.
     */
    function trim(str) {
        return str.replace(/^[\s\xa0]+|[\s\xa0]+$/g, '');
    }

    /**
     *
     * @param {string} delimitersPattern The delimiters pattern, format: start...end.
     *      For example: if you want to set <% and %> as delimiters, you should set '<%...%>' as pattern
     */
    sweet.setDelimiters = function(delimitersPattern) {
        var d = delimitersPattern.replace(/[\{\[\}\]\(\)\|]/g, function(m) {
            return "\\" + m;
        }).split("...");
        sweet.startDelimiter = d[0];
        sweet.endDelimiter = d[1];
    }

    // Default delimiters
    sweet.startDelimiter = "<\\[";
    sweet.endDelimiter = "\\]>";

    /**
     * You can use extendUtil method to add your own util methods to global util object,
     * such as string tools, version info, etc. Remember, all the change
     * you apply to global util object will effect all of the sweet templates
     * you has defined.
     */
    sweet.util = {
        trim: trim
    };
    sweet.extendUtil = function(utilExtends) {
        if (utilExtends) {
            var util = sweet.util;
            for (var key in utilExtends) {
                var func = utilExtends[key];
                if (func) util[key] = func;
            }
        }
    }

    /**
     * Attach to jqeury constructor
     */
    $.Sweet = sweet;

    /**
     * Test a string whether it is a valid sweet template
     * @param str
     * @return {boolean}
     */
    sweet.isSweet = function(str) {
        return new RegExp(sweet.startDelimiter + ".*?" + sweet.endDelimiter).test(str);
    };

    /**
     * Util function, unescape a html string.
     * Since jquery's .html() method will escape string, we must do this before using innerHTML
     * as template
     * @param str
     */
    $.unescapeHtml = function(str) {
    	//decodeURIComponent(str)
        return str.replace(/&([^;]+);/g, function(s, entity) {
            switch (entity) {
                case 'amp':
                    return '&';
                case 'lt':
                    return '<';
                case 'gt':
                    return '>';
                case 'quot':
                    return '"';
                default:
                    if (entity.charAt(0) == '#') {
                        var n = Number('0' + entity.substr(1));
                        if (!isNaN(n)) {
                            return String.fromCharCode(n);
                        }
                    }
                // For invalid entities we just return the entity
                return s;
            }
        });
    }

    $.fn.extend({

        /**
         * Add a sweet template object to dom element
         * @param tpl {string or $.Sweet or null} Template string or template object, if tpl is null,
         *      Sweet will use element's innerHTML as template string
         * @param options {object} optinal, fields:
         *      name:       the string name of sweet template, default to 'default'
         */
        template: function(tpl, options) {
            options = options || {};
            var sw, name = options.name || "default",
                cache = this.data("sweet");

            // Don't make element's html as template string if it has had a template with pointed name
            if (!tpl && cache && cache[name])
                return this;

            tpl = tpl || $.unescapeHtml(this.html());
            if (typeof tpl == "string") {
            	tpl = sweet.isSweet(tpl) ? tpl : $(tpl)[0].innerHTML;
                tpl = tpl.replace(/[\r\n\t]/g, '');
                sw = new sweet(tpl);
            } else if (typeof tpl.applyData == "function" && typeof tpl.compiled == "function") {
                sw = tpl;
            } else {
                throw Error("Template must be a string or an instance of $.Sweet or null.");
            }
            if (!cache) {
                cache = {};
                this.data("sweet", cache);
            }
            cache[name] = sw;
            return this;
        },

        /**
         * Remove a sweet template object from element
         * @param name {string} null to remove all templates
         */
        removeTemplate: function(name) {
            if (name) {
                var cache = this.data("sweet");
                if (cache) {
                    delete cache[name];
                }
            } else {
                this.data("sweet", {});
            }
            return this;
        },

        /**
         * Create dom elements from stored sweet template with data,
         * if data is an array, new elements will created and append to current element,
         * or new elements will replace current element's html. Target element will be emptied.
         * @param data {object} can be any valid json object and array
         * @param options {object} optional, fields:
         *      scope:          the runtime scope of inline template code
         *      notIterate:     if true, an array type data will not be iterated, it will be applyed as
         *                      a common object, default to false
         */
        applyData: function(data, options) {
            return this.empty().appendData(data, options);
        },

        /**
         * Similar with applyData, but not empty target element
         * @param data
         * @param options
         */
        appendData: function(data, options) {
            options = options || {};
            var tpl = this.data("sweet")[options.name || "default"];
            if (tpl) {
                var scope = options.scope, dom,
                    notIterate = options.notIterate || false,
                    rendering = options.rendering,
                    rendered =options.rendered;
                function applyData(context) {
                    if (typeof rendering == "function" && rendering.call(this, context) === false) {
                        return;
                    }
                    dom = $(tpl.applyData(context.data, scope));
                    this.append(dom);
                    if (typeof rendered == "function")
                        rendered.call(this, dom, context);
                }
                if (!notIterate && data.constructor == Array && data.length) {
                    for (var i = 0, l = data.length; i < l; i++) {
                        applyData.call(this, {data: data[i], index: i});
                    }
                } else {
                    applyData.call(this, {data: data});
                }
            }
            return this;
        },

        /**
         * Create elements with stored sweet and data, if no stored sweet template found,
         * create a template with source element's innerHTML
         * @param data
         * @param options {object} optional, fields:
         *      name:       name of stored template to use
         *      scope:      the runtime scope of inline template code    
         */
        createFromData: function(data, options) {
            options = options || {};
            var cache = this.data("sweet"),
                name = options.name || "default", tpl;
            if (!cache || !(name in cache)) {
                return this.template(null, {name: name})
                        .createFromData(data, options);
            }
            tpl = cache[name];
            return $(tpl.applyData(data, options.scope));
        }
    })
})(jQuery);