Fantasy.util.jClass(Fantasy.util.Observable, {

	jClass : 'Fantasy.util.Cache',

	initialize : function($super,id) {
		this.cacheMap = new Fantasy.util.Map();
	},

	putObject : function(key,value){
		this.cacheMap.put(key,value);
	},
	
	getObject : function(key){
		if(this.cacheMap.containsKey(key)){
			return this.cacheMap.get(key);
		}
		return null;
	}

});