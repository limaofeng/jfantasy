//============================================================
//[描    述]  Proxy	主要功能：为方法添加代理
//============================================================
/**
 * 为js函数提供代理
 * @param {Object} fun		需要代理的函数
 * @param {Object} proxyFun	代理方法
 * @param {Object} scope	执行函数代理时的域
 */
Fantasy.util.jClass({
	
	jClass : 'Fantasy.util.Proxy',
	
	initialize : function(fun,proxyFun,scope) {
		var invocation = new Fantasy.util.Invocation(fun,scope);
        return function(){
			invocation.setArgs.apply(invocation,[arguments]);
			return proxyFun.call(scope,invocation);
		};
	}
	
});
/**
 * {@link Fantasy#proxy}的简写方式
 * @member Fantasy proxy
 * @method */
Fantasy.proxy = Fantasy.util.Proxy;