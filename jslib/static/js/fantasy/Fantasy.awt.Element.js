Fantasy.util.jClass(Fantasy.util.Map,{
    
    jClass: 'Fantasy.awt.Element',
    
    initialize: function($super,template,data){
    	$super();
    	this.addEvents('refresh');
    	this.data = data;
    	this.setTemplate(template,data);
    	//this.view = view;

    },
    
    setState:function(state){
    	this.state = state;
    },
    
    getIndex :function(){
        return this.index;
    },
    
    setIndex:function(index){
        this.index = index;
    },
    
    /**
     * 设置元素项的模板
     * @param {Fantasy.util.Template} template
     * @param data {boolean|json} 如果为boolean表示是否返回原来的数据,如果为json就以json数据载人当前
     */
    setTemplate: function(template, data){
    	var zhis=this,oldData = this.getData();
		if(Fantasy.isBoolean(data) || typeof data == 'undefined'){
			data = data ? oldData : this.data ;
		}
		data = data == null ? {} : data ;
		this.template = template;
		var index = this.getIndex();
        data['index'] = index;//索引
        this.clear();//清除原来的子项
        var $template = template.applyTemplate(data,function(fields,refreshIndex){
            zhis.on("refresh",function(){
                refreshIndex(zhis.getIndex());//刷新元素中Field的name属性
            });
            refreshIndex(index);
            $.each(fields,function(i,field){
                zhis.put(field.getName(), field);
            });
        });
        if (this.target) {
            this.target.replaceWith($template);//TODO jquery 1.4 replaceWith .remove() 方法与jquery 1.3.2 不一致所以这样修改
        }
        this.target = $template;
        if (!!data)
            this.setData(data);
    },
    
    /**
     * 更新数据
     * @param {json} data
     */
    update : function(data){
		if(typeof data == 'undefined'){
			throw new Error("update 参数为 NULL");
		}
		this.setData(data);
        this.refresh();
    },
	
	/**
	 * 该方法不会重建html,只是通知该行的位置可能发生了改变
	 */
	refresh : function(){
		this.setState('refresh');
		this.fireEvent('refresh',this,[this.getData()]);
		this.setState('none');
	},
    
    /**
     * 设置数据
     * @param data
     */
    setData: function(data){
		if(typeof data == 'undefined'){
			throw new Error("update 参数为 NULL");
		}
		this.data = data;
        this.each(function(){
            var field = this.value;
			field.setValue(data);
        });
    },
	
	getValue:function(name){
		var f = this.get(name);
		return f ? f.getValue() : this.data[name];
	},
	
	setValue:function(name,value){
		this.data[name] =  value;
		var f = this.get(name);
		if(f)
			f.setValue(value);
	},
    
    /**
     * 获取数据
     */
    getData: function(){
        if (!this.target)
            return null;
        var retVal = Fantasy.clone(this.data);
        this.each(function(){
        	var key = !this.value.getMapping()?this.value.getName():this.value.getMapping();
            retVal[key] = this.value.getValue();
        });
        return retVal;
    },
    
    /**
     * 移除当前对象
     */
    remove: function(view){
        view.remove(this.getIndex());
//        this.target.detach();
    },
    
    getTemplateName: function(){
    	return this.target.attr("name");
    }
});
