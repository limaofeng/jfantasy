Fantasy.util.jClass({

    jClass: 'Fantasy.util.Object',
    
    initialize: function(){
        this.backup = {};
    },
    
    set: function(attrName, value){
        this.options[attrName] = value;
        if (this.listeners) 
            this.fireEvent('option', this, [attrName, value]);
    },
    
    setPropertyValue: function(values, defaultValues){
        if (defaultValues && typeof defaultValues === 'object') {
            this.setPropertyValue(defaultValues);
        }
        if (values && typeof values === 'object') {
            for (var property in values) {
                var propertyName = property.toString().upperCaseFirst();
                var setPropertyName = 'set' + propertyName;
                var fn = this[setPropertyName];
                if (fn && typeof fn === 'function' && typeof values[property] != 'undefined') 
                    fn.apply(this, [values[property]]);
				else
					this[property] = values[property];
            }
        }
    },
	
    get: function(attrName){
    	return this.options[attrName];
    },
    
	/**
	 * 默认的 toString 方法
	 */
    toString: function(){
        return this.getClass().toString();
    },
    
    /**
     * 获取对象的Class信息
     */
    getClass: function(){
        if (!this.jClass) {
			this.jClass = Fantasy.util.Class.forName(this.constructor);
        }
        return this.jClass;
    },
    
    clone: function(){
  		throw new Error('使用 clone 方法,必须先在'+this.toString()+'中重写该方法!');  
    }
    
});
