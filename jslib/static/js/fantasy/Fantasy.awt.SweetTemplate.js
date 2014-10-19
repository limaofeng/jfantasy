Fantasy.util.jClass(Fantasy.awt.Template,{
	
	jClass : "Fantasy.awt.SweetTemplate",

	initialize : function($super,$temple, options) {
		$super($temple,options);
	},
	
	applyTemplate : function(data){
		//alert(Fantasy.encode(data));
		//alert(this.template.template().applyData(data).html());
		return this.template.clone().html(this.template.template().applyData(data).html());
	}

});