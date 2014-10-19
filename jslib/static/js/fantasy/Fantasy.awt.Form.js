//===================================================================================
// [描    述]  Fantasy.awt.Form
// 主要功能：提供一个可以切换显示与编辑的panel，与Fatnasy.awt.View不同的是，他永远只有一条数据
//===================================================================================
Fantasy.util.jClass(Fantasy.awt.View, (function () {

    return {

        jClass: 'Fantasy.awt.Form',

        /**
         * 构造方法
         * @param {Object} $super    父类方法
         * @param {Object} options    初始设置
         */
        initialize: function ($super, target, options) {
            $super(target,options);
            this.addEvents('change');
            var _this = this;
            this.on('add',function(data){
                _this.fireEvent('change',_this,[data,this.getTemplateName(),this]);
            })
        },

        update: function($super,json,templateName){
            this.clear();
            if(!!templateName){
                this.setTemplateDefault(templateName);
            }
            $super([json]);
        },

        setTemplate: function($super,index, name, data){
            if(arguments.length == 2 && Fantasy.isString(index)){
                $super(0, index);
            }else{
                $super(index, name, data);
            }
        },

        getData: function($super){
            return $super()[0];
        },

        getElement : function(){
            return this.get(0);
        }

    }

})());