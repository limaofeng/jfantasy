jQuery.include('js/fantasy/style/but.css');

Fantasy.awt.RightMenu = Fantasy.util.jClass(Fantasy.util.Observable,{

	jClass : 'Fantasy.awt.RightMenu',

	initialize : function($super,options){
		this.source = $(Fantasy.awt.RightMenu.template['default']);
		
		//this.menu
		
	},
	
	addItem : function(){		
		
	},
	
	removeItem : function(){
		
	},
	
	getSource : function(){
		return this.source;
	},
	
	setItems : function(items){
		var zthis = this;
		zthis.source.show();
		zthis.source.children('ul').html('');
		items.each(function(){
			zthis.source.children('ul').append(zthis.createItem(this));
		});
	},
	
	createItem : function(itemData){
		if(Fantasy.awt.RightMenu.separator == itemData){
			return $('<li class="vakata-separator vakata-separator-after"></li>');
		}
		var zthis = this;		
		var li = $('<li class=""><ins>&nbsp;</ins><a rel="create" href="#">'+itemData['text']+'</a></li>');
		if(itemData['items'] && itemData['items'].length > 0){
			li.children('a').prepend('<span style="float: right;">?</span>');
			var ul = $('<ul style="display:none;"></ul>');			
			itemData['items'].each(function(){
				ul.append(zthis.createItem(this));
			});
			li.append(ul);
			li.hover(function(){
				ul.show();
			},function(){
				ul.hide();
			});
		}
		if(itemData['callback']){
			li.click(itemData['callback']);
		}
		return li;
	}
	
});
Fantasy.awt.RightMenu.separator = 'XXXX';
Fantasy.awt.RightMenu.template =  new Object();
Fantasy.awt.RightMenu.template['default'] =	'<div id="vakata-contextmenu" style="display:none;width: 180px;" class="jstree-default-context">'+
											'<ul></ul>'+
											'</div>';
$(function(){
	window.rightMenu = new Fantasy.awt.RightMenu();
	$('body').append(rightMenu.getSource());
});

jQuery.fn.extend({
	
	rightMenu : function(itemData){
		$(this).mousedown(function(e){
			if (e.button != 0) {
				rightMenu.setItems(itemData);
				rightMenu.getSource().css({
					left: e.clientX,
					top: e.clientY
				});
				//document.oncontextmenu = false;
			};
		});
	}
		
});