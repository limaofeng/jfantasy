Fantasy.util.jClass(Fantasy.util.Collection,{
	
	jClass : 'Fantasy.util.Map',
	
	initialize : function($super,values){
		$super(values?new Array(values):null);
	},
	
	add : function(){
		throw new Error('add 方法为 Fantasy.util.Collection 父类方法,以屏蔽!请使用put方法');
	},
	
	/**
	 * 名称：put 属于:Collection 描述：向Collection中增加元素（key, value)
	 * 
	 * @param _key
	 * @param _value
	 * @return
	 */
	put : function(_key, _value) {
		if(this.containsKey(_key)){
			this.elements.each(function(){
				if (this.key == _key) {
					this.value = _value;
				}
			});
		}else{
			this.elements.push({
				key : _key,
				value : _value
			});
		}
	},
	
	/**
	 * 名称：remove 属于:Collection 描述：删除指定KEY的元素，成功返回True，失败返回False
	 * 
	 * @param _key
	 * @return
	 */
	remove : function(_key) {
		var elements = this.elements;
		return this.elements.each(function(i){
			if (this.key == _key) {
				elements.splice(i, 1);
				return true;
			}
		});
	},
	
	/**
	 * 名称：get 属于:Collection 描述：获取指定KEY的元素值VALUE，失败返回NULL
	 * 
	 * @param _key
	 * @return
	 */
	get : function(_key) {
		return this.elements.each(function(){
			if (this.key == _key) {
				return this.value;
			}
		});
	},
	
	/**
	 * 名称：element 属于:Collection
	 * 描述：获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL
	 * 
	 * @param _index
	 * @return
	 */
	element : function(_index) {
		if (_index < 0 || _index >= this.elements.length) {
			return null;
		}
		return this.elements[_index];
	},
	
	/**
	 * 名称：containsKey 属于:Collection 描述：判断Collection中是否含有指定KEY的元素
	 * 
	 * @param _key
	 * @return
	 */
	containsKey : function(_key) {
		return this.elements.each(function(){
			if (this.key == _key) {
				return true;
			}
		});
	},
	
	/**
	 * 名称：containsValue 属于:Collection 描述：判断Collection中是否含有指定VALUE的元素
	 * @param _value
	 * @return
	 */
	containsValue : function(_value) {
		return this.elements.each(function(){
			if (this.value == _value) {
				return true;
			}
		});
	},
	
	/**
	 * 名称：values 属于:Collection 描述：获取Collection中所有VALUE的数组（ARRAY）
	 * 
	 * @return
	 */
	values : function() {
		var arr = new Array();
		for (var i = 0; i < this.elements.length; i++) {
			arr.push(this.elements[i].value);
		}
		return arr;
	},
	
	/**
	 * 名称：keys 属于:Collection 描述：获取Collection中所有KEY的数组（ARRAY）
	 * 
	 * @return
	 */
	keys : function() {
		var arr = new Array();
		for (var i = 0; i < this.elements.length; i++) {
			arr.push(this.elements[i].key);
		}
		return arr;
	}
});