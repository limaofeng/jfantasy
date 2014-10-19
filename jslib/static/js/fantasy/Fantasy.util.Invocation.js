/**
 * 函数代理时的回调
 * @param {Object} fun		源函数
 * @param {Object} scope	域
 */
Fantasy.util.Invocation = Fantasy.util.jClass({

    jClass: 'Fantasy.util.Invocation',
    
    initialize: function(fun,scope){
        var zthis = this;
        var argNames = Fantasy.argumentNames(fun);
        var propertyNames = argNames.clone();
        propertyNames.push('args');
        propertyNames.push('function');
        propertyNames.push('scope');
        var property = new Fantasy.data.Property(new Array(this, propertyNames));
		this.setScope(scope);
		this.setFunction(fun);
		property.on('setArgs', function(){
			var args = arguments[0];
			argNames.each(function(index){
            	zthis['set' + this.upperCaseFirst()].apply(zthis, [args[index]]);
        	});
        }, this);
        property.on('getArgs', function(){
            argNames.each(function(index){
                property['args'][index] = property[this];
            });
        }, this);
    },
    
    invoke: function(){
        return this.getFunction().apply(this.getScope(), this.getArgs());
    }
    
});
