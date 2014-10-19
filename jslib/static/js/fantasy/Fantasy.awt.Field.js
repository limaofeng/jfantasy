/**
 * Fantasy.awt.View.Field 对象
 * 		构建此对象需要一个mapping对象。
 * 		mapping 对象的结构
 * 				{
 * 				name:'名称',
 * 				mapping:'映射名称',
 * 				fullName:'全名',
 * 				defaultValue:'默认值',
 * 				type:'标签类型',
 * 				dataType:'数据类型'
 * 				html:'html'
 * 				}
 * @param {Object} mapping
 */
Fantasy.util.jClass({

    jClass: 'Fantasy.awt.Field',
    
    initialize: function(target,dataField){
        this.dataField = dataField;
		this.type = dataField.type;
        this.target = target;
    },
    
    /**
     * 为项设置数据
     * @param {json} data
     */
    setValue: function(data){
        switch (this.type) {
            case 'label':
                this.target.html(this.format(data, 'in'));
                break;
            case 'a':
                this.target.html(this.format(data, 'in'));
                break;
            case 'input':
                ((this.target.attr('type') == 'radio' || this.target.attr('type') == 'checkbox') ? $.fn.vals : $.fn.val).apply(this.target,[this.format(data, 'in')]);
                break;
            case 'select':
                this.target.val(this.format(data, 'in'));
                break;
            case 'img':
            	this.target.attr('src',request.getContextPath() + this.format(data, 'in'));
            	break;
            case 'text':
            	var v = this.format(data, 'in');
            	if(this.target.text()!=v)
            		this.target.text(v);
            	break;
        }
    },
    
    /**
     * 获取项对应的数据
     */
    getValue: function(){
        switch (this.type) {
            case 'label':
                return this.format(this.target.html(), 'out');
            case 'a':
                return this.format(this.target.html(), 'out');
            case 'input':
                return this.format(this.target.val(), 'out');
            case 'select':
                return this.format(this.target.val(), 'out');
            case 'img':
            	return this.format(this.target.attr('src'), 'out');
            case 'text':
            	return this.format(this.target.val(), 'out');
        }
    },
    
    /**
     * 值转换
     * @param {Object} data
     * @param {Object} type	标示符 in|out
     */
    format: function(data, type){
        var _d = data;
		switch(type){
			case 'in':
				return this.dataField.setValue(data);
			break;
			case 'out':
				return this.dataField.getValue(data);
			break;
		}
    },
    
    /**
     * 获取 属性的名称
     */
    getName: function(){
        return this.dataField.name;
    },
    
    /**
     * 获取 属性的映射名称
     */
    getMapping: function(){
        return this.dataField.mapping;
    }
    
});
