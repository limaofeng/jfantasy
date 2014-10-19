/***********************************************************\
 *			Fantasy											*
 *[描    述] JS 框架											*
 *			 部分内容来自于网络								*
 \***********************************************************/
Fantasy = (function(){
    return {
    
        version: '1.0',
        
        /**
         * 定义命名空间
         * @param {Object} namespace
         */
        namespace: function(namespace){
            var namespaceArray = namespace.split('.');
            for (var i = 0; i < namespaceArray.length; i++) {
                namespace = i == 0 ? namespaceArray[i] : namespace += ("." + namespaceArray[i]);
                if (eval('typeof ' + namespace + ' == \"undefined\"')) {
                    if (i == 0) {
                        eval('var ' + namespace + ' = {};');
                    }
                    else {
                        eval(namespace + ' = {};');
                    }
                }
            }
        },
        
        /**
         * 为 Class 扩展方法与属性
         * @param {Object} jClass
         * @param {Object} prototypes 扩展方式
         * @param {Object} defaults	静态方法
         */
        apply: function(jClass, prototypes, defaults){
            if (jClass && prototypes && typeof prototypes == 'object') {
                //noinspection JSDuplicatedDeclaration
                for (var fName in prototypes) {
                    if(prototypes.hasOwnProperty(fName)){
                        jClass.prototype[fName] = prototypes[fName];
                    }
                }
                if (Object.prototype.toString != prototypes['toString']) {//TODO IE在json对象中的toString不能遍历的问题
                    jClass.prototype['toString'] = prototypes['toString'];
                }
            }
            if (defaults) {
                //noinspection JSDuplicatedDeclaration
                for (var fName in defaults) {
                    if(defaults.hasOwnProperty(fName)){
                         jClass[fName] = defaults[fName];
                    }
                }
                if (Object.prototype.toString != defaults['toString']) {//TODO IE在json对象中的toString不能遍历的问题
                    jClass['toString'] = defaults['toString'];
                }
            }
        },

        merge : function () {
        	if(arguments.length == 0){
        		throw new Error("Fantasy.merge 参数不正确!");
        	}
        	var plainObject = arguments[0];
            for(var i=1;i<arguments.length;i++){
        		if(arguments[i] == null)
        			continue;
        		for(var f in arguments[i]){
                    if(!arguments[i].hasOwnProperty(f)){
                        continue;
                    }
        			if(jQuery.isPlainObject(arguments[i][f])){
        				if(!jQuery.isPlainObject(plainObject[f])){
        					plainObject[f] = {};
        				}
						Fantasy.merge.apply(this,[plainObject[f],arguments[i][f]]);
        			}else{
        				if(plainObject[f] != null)
        					continue;
        				plainObject[f] = arguments[i][f];
        			}
        		}
        	}
        	return plainObject;
		},

		urlFilter : (function(){
			var constant = {};
			constant['contextPath'] = request.getContextPath();
			return function (url){
				if(!Fantasy.isString(url))
					return url;
				url = url.replace(/\%\{([^\}]+)\}/g,function($0,$1) {
			        return constant[$1];
			    });
				return url;
			};
		})(),

        get$Object : function(expr, z, $context){
            return (/[)]$/.test(expr) ? eval('(z.' + expr + ')') : $(expr, $context));
        }

    };

})();
