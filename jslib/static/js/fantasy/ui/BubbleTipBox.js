var _BubbleTipBoxIndex = 0;

jQuery.fn.extend({
	
	bubble:function(tip,isUpdate){
		if(isUpdate)this.unbubble();
		var _bubble = $.Dataset.get("_BUBBLE_"+$(this).attr("id"));
		if(_bubble == null){
			$.Dataset.put("_BUBBLE_"+$(this).attr("id"),new BubbleTipBox($(this),tip));
		}
		return $.Dataset.get("_BUBBLE_"+$(this).attr("id"));
	},
	
	mouseTip:function(){
		var _bubble = $.Dataset.get("_BUBBLE_"+$(this).attr("id"));
		if(_bubble != null){
		    var trigger = $('#'+$(this).attr("id"));
		    var info = $('#'+_bubble.dpop.attr("id"));
		    $([trigger.get(0), info.get(0)]).mouseover(function(){trigger.showTip();});
		    $([trigger.get(0), info.get(0)]).mouseout(function(){trigger.hideTip();});
		}
	},
	
	showTip:function(){
		var _bubble = $.Dataset.get("_BUBBLE_"+$(this).attr("id"));
		if(_bubble != null)
			_bubble.show();
	},
	
	hideTip:function(){
		var _bubble = $.Dataset.get("_BUBBLE_"+$(this).attr("id"));
		if(_bubble != null)
			_bubble.hide();
	},
	
	unbubble:function(){
		$.Dataset.remove("_BUBBLE_"+$(this).attr("id"));
	}

});

function BubbleTipBox(trigger, content) {
	_BubbleTipBoxIndex++;
	var _BubbleTipBoxDiv = "<table id=\"_BubbleTipBoxDiv_" + _BubbleTipBoxIndex
			+ "\" class=\"popup\"><tbody><tr><td id=\"topleft\" class=\"corner\"></td>"
			+ "<td class=\"top\"></td><td id=\"topright\" class=\"corner\"></td></tr><tr>"
			+ "<td class=\"left\"></td><td class=\"popup-contents\"><table class=\"popup-contents\"><tbody><tr>"
			+ "<td id=\"_BubbleTip_CONTENT_"+_BubbleTipBoxIndex+"\"></td></tr></tbody></table></td><td class=\"right\"></td></tr><tr>"
			+ "<td class=\"corner\" id=\"bottomleft\"></td>"
			+ "<td class=\"bottom\"><div></div></td>"
			+ "<td id=\"bottomright\" class=\"corner\"></td></tr></tbody></table>";
	$("body").append(_BubbleTipBoxDiv);
	$("#_BubbleTip_CONTENT_"+_BubbleTipBoxIndex).append(content);
	this.content = content;
	this.distance = 10;
	this.time = 250;
	this.hideDelay = 500;
	this.hideDelayTimer = null;
	this.beingShown = false;
	this.shown = false;
	this.trigger = trigger;
	this.dpop = $("#_BubbleTipBoxDiv_" + _BubbleTipBoxIndex).css('opacity', 0);
	BubbleTipBox.prototype.showTip = function(){
		var thisObject = this;
		if (this.hideDelayTimer)
			clearTimeout(this.hideDelayTimer);
		if (this.beingShown || this.shown) {
			return;
		} else {
			this.beingShown = true;
			var _triggerTop = this.trigger.offset().top;
			var _triggerLeft = this.trigger.offset().left;
			var _triggerHeight = this.trigger.height();
			var _triggerWidth = this.trigger.width();
			var _dpopHeight = this.dpop.height();
			var _dpopWidth = this.dpop.width();
			this.dpop.css( {
				top : _triggerTop - 100 + (131 - _dpopHeight),
				left : _triggerLeft - (_dpopWidth / 2 - (_triggerWidth * 0.2)),
				display : 'block'
			}).animate( {
				top : '-=' + this.distance + 'px',
				opacity : 1
			}, this.time, 'swing', function() {
				thisObject.beingShown = false;
				thisObject.shown = true;
			});
		}
		return false;
	}
	BubbleTipBox.prototype.hideTip = function(){
		var thisObject = this;
		if (this.hideDelayTimer)
			clearTimeout(this.hideDelayTimer);
		this.hideDelayTimer = setTimeout(function() {
			thisObject.hideDelayTimer = null;
			thisObject.dpop.animate( {
				top : '-=' + thisObject.distance + 'px',
				opacity : 0
			}, thisObject.time, 'swing', function() {
				thisObject.shown = false;
				thisObject.dpop.css('display', 'none');
			});

		}, thisObject.hideDelay);
		return false;
	}
	BubbleTipBox.prototype.show = function() {
		this.showTip();
		trigger[0].focus();
	}
	BubbleTipBox.prototype.hide = function() {
		this.hideTip();
	}
}