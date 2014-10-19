jQuery.fn.extend({

    selectionBox: (function () {
        var templates = {};
        templates['simple'] = $.Sweet('<div class="pop-select" style="width:283px;">' +
            '<div class="pop-content">' +
            '<div class="pop-select-title-right">' +
            '	<a href="javascript:void(0)" class="ico-expand-arrow pop-select-title-link"><[=title]></a>' +
            '</div>' +
            '</div>' +
            '</div>');


        templates['group'] = $.Sweet('<div class="pop-select" id="select-role" style="width:365px;display: block;">' +
            '<div class="pop-content" style="padding:10px;">' +
            '<div class="select-title"><[=title]>： <a href="javascript:void(0)" class="pop-select-close">取消</a></div>' +
            '</div>' +
            '</div>');

        return function (settings) {
            settings = Fantasy.copy({top: 0, left: 0}, settings);
            var selectionBox = $(this).data('_selectionBox');
            if (!selectionBox) {
                settings = Fantasy.merge(settings, {model: 'simple'});
                var $box = $(templates[settings.model].applyData(settings)).appendTo('body');
                //添加内容
                if (settings.content)
                    $box.find('.pop-content').prepend(settings.content.show());
                selectionBox = $box.box($(this), {event: settings.event, positionAdjust: [settings.top, settings.left - $box.width() + $(this).width()]});
                $(this).data('_selectionBox', selectionBox).on('remove',function(){
                    selectionBox.remove();
                });
            }
            return selectionBox;
        };

    })()

});
/*
 <div id="authorizationBox">
 <div style="overflow:auto;"  class="c-group-all viewTarget">
 <a href="#" class="selected"><span>{name}</span></a>
 <a href="#" class="selected"><span>{name}</span></a>
 <a href="#" class="selected"><span>{name}</span></a>
 </div>
 <div class="c-group-select-action ft-right">
 <a href="#" class="fn-left new-group-tag"></a><input id="saveTest" type="button" class="btn-normal-s" value="保存" style="padding-bottom:1.5px;padding-top:1.5px;"/>
 <a id="cannel_choose" class="cannel_choose" href="javascript:void(0)">[清空选择]</a>
 </div>
 </div>
 */