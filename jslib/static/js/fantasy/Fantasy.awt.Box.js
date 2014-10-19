Fantasy.util.jClass(Fantasy.util.Observable,{
	
	jClass : 'Fantasy.awt.Box',
	
	/**
	 * widget:要显示的浮动层
	 * trigger:触发点
	 * positionAdjust:偏移坐标
	 * timeOut:鼠标移开超时消失时间
	 */
	initialize : function($super,options){
		$super(['show','hide']);
		var property = new Fantasy.data.Property(this, []);
		
		Fantasy.copy(this, Fantasy.merge(options || {}, {
			widget : null,
			trigger : null,
			timeOut : 300,
			positionAdjust : [ 0, 0 ],
			event : 'click'
		}));
		var _SF = (function(zhis){
			return function(event){
				zhis.show($(this));
				event.preventDefault();
			};
		})(this);
		this.trigger.bind(this.event,_SF);
		
		if(options.model!='simple'){
			this.widget.css({position:'absolute'}).appendTo($('body'));//将DIV设为绝对位置。并移动至body下
		}
	    
	    if(this.timeOut){
	    	(function(zhis){
	    		var _TOF = null;
	    		var _OVERF = function(){
					_TOF?clearTimeout(_TOF):null;
				};
				var _OUTF = function(){
					_TOF = setTimeout(function() {
						zhis.hide();
	    			},zhis.timeOut);
				};
				zhis.trigger.bind('mouseover',_OVERF);
				zhis.trigger.bind('mouseout',_OUTF);
    			zhis.widget.bind('mouseover',_OVERF);
    			zhis.widget.bind('mouseout',_OUTF);
	    	})(this);
	    }
	},
	
	show : function(){
		var t = $.getEvent() ? $.getEvent() : this.trigger ;
    	var of = t.offset();
    	
    	var pos_t = jQuery.isFunction(this.positionAdjust[0]) ? this.positionAdjust[0].apply(this,[this.widget,t]) : this.positionAdjust[0];
    	var pos_l = jQuery.isFunction(this.positionAdjust[1]) ? this.positionAdjust[1].apply(this,[this.widget,t]) : this.positionAdjust[1];
    	var pa = {top:pos_t,left:pos_l};
    	if(this.model!='simple'){
    		this.widget.css({top:of.top+pa.top,left:of.left+pa.left}).show();
    	}else{
    		this.widget.show();
    	}
    	this.fireEvent('show',this,[this.widget,this.trigger]);
	},

	hide : function(){
		this.widget.hide();
		this.fireEvent('hide',this,[this.widget]);
	},
	
	remove : function(){
		this.widget.remove();
	}
	
});