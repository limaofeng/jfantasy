(function($) {
	var timeout,$msgbox = function(options) {
		var defaults = {
			title : '系统提示!',
			icon : 'clear',
			time : '2500',
            type:'warning',
            effect:'md-effect-1'
		};
		var settings = jQuery.extend(defaults, options);
        if(!!timeout){
            clearTimeout(timeout);
        }
		$('#ts_Msgbox').remove();
        var box = $('<div class="md-modal" id="ts_Msgbox"><div class="md-content infobox small-box drop-shadow">' +
            '<div class="large btn info-icon">' +
            '<i class="glyph-icon"></i>' +
            '</div>' +
            '<h4 class="infobox-title">'+settings.title+'</h4>' +
            '<p>'+settings.msg+'</p>' +
            '</div></div>').appendTo($("body"));
        switch(settings.type){
            case 'warning':
                $('.glyph-icon',$('.info-icon',$('.md-content',box.addClass(settings.effect)).addClass('warning-bg')).addClass('bg-yellow')).addClass('icon-warning-sign');
                break;
            case 'success':
                $('.glyph-icon',$('.info-icon',$('.md-content',box.addClass(settings.effect)).addClass('success-bg')).addClass('bg-green')).addClass('icon-ok-sign');
                break;
        }
        timeout = setTimeout(function(){
            box.addClass('md-show');
            timeout = setTimeout(function() {
                $('#ts_Msgbox').removeClass('md-show');
                if (settings.callback) {
                    settings.callback();
                }
            }, settings.time);
        },100);
	};
	$.msgbox = function(options) {
		return new $msgbox(options);
	};
	return $.msgbox;
})(jQuery);