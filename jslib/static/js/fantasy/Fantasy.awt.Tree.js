//jQuery.include('static/js/fantasy/style/tree.css');


Fantasy.util.jClass(Fantasy.util.Observable, (function(){
	/**
	 * {String} pathSeparator 分割节点Id的标识符（默认为"/"）。
	 */
	var pathSeparator = '/';
	
    return {
		
        jClass: 'Fantasy.awt.Tree',
        
        initialize: function($super,target,options){			
			$super();
            this.addEvents('handleList','insert');			
			this.$node = options.node.clone();			
            this.root = new Fantasy.awt.Node(null,{
				tree: target,
				node: options.node
			},this);
            this.itemsName = options.itemsName ? options.itemsName : 'items';
            this.root.isRoot = true;
			this.root.$html = target;	
        },
        
		setJSON:function(json){
			var zthis = this;
			this.root.setJSON(json);
			this.root.cascade(function(){
				if(!this.isLeaf()){
					this.$html.find('button[class^=ico_]:eq(0)').attr('class', this.$html.children('.tree').is(':hidden') ? 'ico_close' : 'ico_open');
				}
			});
			this.root.$html.find('button[class^=switch_bottom_]').each(function(){
				if(!$(this).nextAll('ul').is(':hidden')){
					$(this).nextAll('ul').removeClass('line');
				}				
			});
		},
		
		setJSONUrl:function(url,data){
			var zthis = this;
			jQuery.getJSON(url,data,function(json){
				zthis.setJSON(json);
			});
		}
        
    };
    
})());
