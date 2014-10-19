
Fantasy.util.jClass(Fantasy.awt.View,{
	
	jClass : 'Fantasy.awt.Grid',
	
	initialize : function($super,options){
		if(!Fantasy.awt.Grid.Static){
			Fantasy.awt.Grid.Static = true;
			Fantasy.awt.Grid.FieldAttr = ['name','mapping','dataType','defaultValue','format'];
		}
		if(!options.target.hasClass('gridTable')){
			options.target = options.target.find('.gridTable');
		}
		if(!options.template){
			options.template = $('<tr class="fairy-awt-grid-tr"></tr>');
			var firstTr = $(options.target.find('tr')[0]);
			this.columns = firstTr.find('th');
			$.each(this.columns,function(seq){
				var thColumn = $(this);
				var gridTableResizeable = $('<span class="gridTable-resizeable"></span>');
				$(this).prepend(gridTableResizeable);
				gridTableResizeable.draggable({ axis: 'x',start:function(){
					
				},
				drag: function() {
				},
				stop: function() {
					var thwidth = Fantasy.util.MathUtil.toInt(thColumn.css('width').replace('px',''));
					var left = Fantasy.util.MathUtil.toInt($(this).css('left').replace('px',''));
					thColumn.css('width',thwidth+left);
					var thnextwidth = Fantasy.util.MathUtil.toInt(thColumn.next().css('width').replace('px',''));
					if(thColumn.next().next().html())
						thColumn.next().css('width',thnextwidth-left);
					$(this).css('left',0);
				}
				});
				var column = $('<td></td>');
				$(this).find('.grid-field').each(function(){
					var attr = new Object();
					var $gridField = $(this);
					Fantasy.awt.Grid.FieldAttr.each(function(){
						attr[this] = $gridField.attr(this.toString());
					});
					type = $gridField.attr('type');
					var field = null;
					if(type == 'span' || type == 'label'){
						field = $('<'+type+'></'+type+'>').attr(attr);
					}else if(type == 'checkbox'){
						field = $('<input type="checkbox" class="checkbox"/>').attr(attr);
					}
					field.removeClass('grid-field');
					field.addClass('view-field');
					column.append(field);
				});
				options.template.append(column);
			});
		}
		this.options = options;
		$super(options);
		this.addEvents('rowdblclick','rowclick','ajaxbeforeload');
		this.options.url = this.options.target.attr('url');		
		this.options.root = this.options.target.attr('root');
		this.options.mask = this.options.target.attr('mask');
		this.options.paging = this.options.target.attr('paging');
		var zthis = this;
		//this.options.url = Fantasy.util.StringUtil.startsWith(this.options.url,request.getContextPath()) ? this.options.url : (request.getContextPath() + this.options.url);
		/*
		this.httpProxy = new Fantasy.data.HttpProxy({url:this.options.url});
		this.httpProxy.on('beforeload',function(params){
			if(zthis.options.mask)
				zthis.options.view.parent().mask(zthis.options.mask);
			zthis.fireEvent('ajaxbeforeload',zthis,params);
		});
		this.httpProxy.on('load',function(json){
			if(zthis.options.mask)
				zthis.options.view.parent().unmask();
		});
		*/
		this.on('beforeload',function(data){
			if(data){
				return;
			}
			zthis.add({seq:1,name:'222',namespace:'/sdfsdf',classPath:'sasdasd.',method:'asd',postfix:'dhtm',notes:'ss'});
			/*
			var fun_ajaxback = function(json){
				zthis.load(eval('json.' + zthis.options.root));
				if(zthis.options.paging){
					var pager = $('<div></div>');
					pager.paging(eval('json.' + zthis.options.paging),function(index){
						var params = new Object();
						params[zthis.options.paging + '.pageIndex'] = index;
						//zthis.httpProxy.load(params,fun_ajaxback);
					});
					if(!zthis.pager){
						zthis.append.after($('<tr></tr>').append($('<td colspan="'+zthis.columns.length+'"></td>').append(pager)));
					}else{
						zthis.pager.replaceWith(pager);
					}
					zthis.pager = pager;
				}
			}
			*/
			//zthis.httpProxy.load(zthis.options.params,fun_ajaxback);
			return false;
		});
		if(!this.has('add',Fantasy.awt.Grid.methods.add)){
			this.on('add',Fantasy.awt.Grid.methods.add,[zthis]);
		}
	}
});
Fantasy.awt.Grid.create = function(config){
	var gridHtml = $('<table class="gridTable"><thead><tr></tr></thead></table>');
	gridHtml.attr({'url':config.url,'root':config.root,'paging':config.paging,'mask':config.mask});
	config.columns.each(function(){
		var column = $('<th>'+Fantasy.util.StringUtil.nullValue(this.title)+'</th>');
		column.css({'width':this.width});		
		var tr = gridHtml.find('tr');
		this.fields.each(function(){
			var field = $('<span class="grid-field"/>');
			field.attr({name:this.name,mapping:this.mapping,type:this.type,defaultValue:this.defaultValue,dataType:this.dataType});
			column.append(field);
		});
		tr.append(column);
	});
	return gridHtml;
}

Fantasy.awt.Grid.methods = (function(){
	return {
		add : function(zthis,data){
			if(this.get('seq')!=null)
				this.get('seq').getTarget().html(this.getSeq()+1);			
			var row = this;
			this.getTarget().bind('dblclick',function(){
				zthis.fireEvent('rowdblclick',row);
			});
			this.getTarget().bind('click',function(){
				zthis.fireEvent('rowclick',row);
			});
		}
	}
})();