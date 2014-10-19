//============================================================
//[描    述]		实现的Collection类
//============================================================
Fantasy.util.jClass(Fantasy.util.Observable,{
    
    jClass : 'Fantasy.util.Collection',
    
	initialize : function($super,array){
		$super(['add','set','clear','remove']);
		if(typeof array == 'number'){
			this.elements = new Array(array);
		}else if(Object.prototype.toString.call(array) === '[object Array]'){
			this.elements = array;
		}else{
			this.elements = [];
		}		
	},
	
	size : function() {
		return this.elements.length;
	},
	
	/**
	 * 名称：isEmpty 属于:Collection 描述：判断Collection是否为空
	 * 
	 * @return
	 */
	isEmpty : function() {
		return (this.elements.length < 1);
	},
	
	/**
	 * 名称：clear 属于:Collection 描述：删除Collection所有元素
	 * 
	 * @return
	 */
	clear : function() {
		this.fireEvent('clear',this,[this.elements]);
		this.elements = [];
	},
	
	/**
	 * 名称：add 属于:Collection 描述：向Collection中增加元素
	 * @param _index
	 * @param _value
	 * @return
	 */
	add : function(_index,_value) {
		if(arguments.length == 1){
			_value = _index;
			_index = this.size();
		}else{
			_index = _index > this.size() ? this.size() : _index; 
		}
		this.elements.insert(_index,_value);
		this.fireEvent('add',this,[_value,_index]);
	},
	
	set : function(_index,_value) {
		this.elements[_index] = _value;
	},
	
	/**
	 * 名称：remove 属于:Collection 描述：删除指定KEY的元素，成功返回True，失败返回False
	 * 
	 * @param _index
	 * @return
	 */
	remove : function(_index) {
		if (_index < 0 || _index >= this.elements.length) {
			return;
		}
		var delObj = this.elements.splice(_index,1);
		this.fireEvent('remove',this,[delObj[0],_index]);
		return delObj[0];
	},
	
	addAll : function(){
		
	},
	
	/**
	 * 名称：get 属于:Collection 描述：获取指定KEY的元素值VALUE，失败返回NULL
	 * 
	 * @param _index
	 * @return
	 */
	get : function(_index) {
		if (_index < 0 || _index >= this.elements.length) {
			return null;
		}
		return this.elements[_index];
	},
	
	indexOf : function(object){
		return this.elements.each(function(index){
			if(this == object){		
				return index;
			}
		});
	},
	
	getFirst : function(){
		return this.get(0);
	},
	
	each : function(fun){
		return this.elements.each(fun,function(index,elements,funRet){
			if(!(typeof funRet == "undefined" || funRet == null)){
				return funRet;
			}
		});
	},
	
	pop : function(){
		return this.remove(this.size()-1);
	},
	
	peek : function(){
		return this.get(this.size()-1);
	},
	
	push : function(_value){
		this.add(_value);
	},
	
	/**
	 * 返回对应数组
	 */
	toArray : function(){
		return this.elements;
	},

    sort: function(){
        return Array.prototype.sort.apply(this.elements,arguments);
    }
	
});

Fantasy.List = Fantasy.util.Collection;