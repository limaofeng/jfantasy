jQuery.fn.extend({

    setJSON: function () {
		$(this).triggerHandler('setJSON', arguments);
        return this;
    },

    dataGrid: (function () {

        var _templateHtml = '<div class="grid_wrapper">' +
            '<div class="grid_search"></div>' +
            '<div class="grid_scroll">' +
            '<div class="grid_scrollHead ui-state-default" style="overflow: hidden; position: relative; border: 0px none; width: 100%;">' +
            '<div class="grid_scrollHeadInner">' +
            '<table class="header table dataTable" style="margin-left: 0px;"><thead></thead></table>' +
            '<table class="optheader table dataTable" style="margin-left: 0px;display: none;"><thead></thead></table>' +
            '</div>' +
            '</div>' +
            ' <div class="grid_scrollBody scrollable-content" style="overflow: auto; width: 100%;">' +
            ' <table class="grid_view table table-hover dataTable" style="margin-left: 0px; width: 100%;">' +
            '      <thead>' +
            '          <tr style="height: 0px;"></tr>' +
            '       </thead>' +
            '       <tfoot>' +
            '           <tr style="height: 0px;"></tr>' +
            '       </tfoot>' +
            '       <tbody>' +
            '       </tbody>' +
            '   </table>' +
            '   </div>' +
            '<div class="grid_scrollFoot ui-state-default" style="overflow: hidden; border: 0px none; width: 100%;">' +
            '    <div class="grid_scrollFootInner" style="padding-right: 15px;">' +
            '       <table class="table dataTable" style="margin-left: 0px;">' +
            '           <tfoot>' +
            '               <tr>' +
            '               </tr>' +
            '           </tfoot>' +
            '       </table>' +
            '   </div>' +
            '</div>' +
            '</div>' +
            '<div class="grid_pager fg-toolbar ui-toolbar ui-widget-header ui-corner-bl ui-corner-br ui-helper-clearfix">' +
            '<div class="grid_info pad10L ui-toolbar">显示 <label class="start-number">1</label>到<label class="end-number">1</label> 共<label class="total-number">1</label>条</div>' +
            '<div id="pager" class="paging grid_paginate ui-toolbar"></div>' +
            '</div>' +
            '</div>';

        return function ($search, $batch,settings) {
            settings = Fantasy.copy({},settings);
            var _$dataGrid = $(_templateHtml);
            $(this).replaceWith(_$dataGrid);
            var cols = [];
            _$dataGrid.find('.grid_scrollHeadInner table.header thead').replaceWith($(this).find('thead'));
            _$dataGrid.find('.grid_search').append($search);
            var _$header = _$dataGrid.find('.grid_scrollHeadInner table.header');
            var _$ths = $('th', _$dataGrid.find('.grid_scrollHeadInner table.header thead')).addClass('ui-state-default').each(function () {
                var text = $(this).html();
                cols.push({width: $(this).width(), text: text, $: $(this)});
                if ($(this).hasClass('sort')) {
                    $(this).removeClass('sort').html($('<div class="grid_sort_wrapper">' + text + '<span class="sort"></span></div>')).find('.sort').attr('orderBy', $(this).attr('orderBy'));
                    $('.grid_sort_wrapper', $(this)).click(function (e) {
                        if (!$(e.target).hasClass('sort')) {
                            $(this).find('.sort').triggerHandler('click');
                        }
                    });
                }
            });
            var _$optheader = _$dataGrid.find('.grid_scrollHeadInner table.optheader');
            var _$check = _$ths.filter(function () {
                return $('[checkall]', $(this)).length > 0;
            }).clone();
            if (_$check.length > 0) {
                _$check.find('[checkall]').removeAttr('checkall').addClass('mcheack');
                $('thead', _$optheader).append(_$check).append('<th class="batch" colspan="' + (_$ths.length - 1) + '"><div class="row"></div></th>').find('th.batch div').append('<div class="col-md-2 tip" style="width:120px">已选中1个文件/文件夹</div>').append($batch);
                $('[checkall]', _$ths).on('tip', function (target, items, checkLength) {
                    if (checkLength > 0) {
                        _$optheader.show().find('.tip').html('已选中' + checkLength + '条数据');
                        _$header.hide();
                    } else {
                        _$optheader.hide();
                        _$header.show();
                    }
                })
            } else {
                _$dataGrid.find('.grid_scrollHeadInner table.optheader').remove();
            }
            _$dataGrid.find('.grid_scrollBody tbody').replaceWith($(this).find('tbody'));
            cols.each(function () {
                $('.grid_scrollBody table thead tr,.grid_scrollBody table tfoot tr', _$dataGrid).append(this.$.clone().attr('style', 'width: ' + this.width + 'px; padding-top: 0px; padding-bottom: 0px; border-top-width: 0px; border-bottom-width: 0px; height: 0px;').html(''));
                var _$mirror = this.$.clone().css({'width': this.width + 'px'});
                if (!!_$mirror.find('[checkall]').length) {
                    var _$mcheack = _$mirror.find('[checkall]').removeAttr('checkall');
                    var _$source = this.$.find('[checkall]').change(function () {
                        $([_$mcheack.get(0), _$check.find('.mcheack').get(0)]).filter((function (_trigger) {
                            return function () {
                                return this != _trigger;
                            };
                        })($(this).data('_trigger'))).data('_trigger', this).prop('checked', $(this).prop('checked')).change();
                        $(this).removeData('_trigger');
                    });
                    $([_$mcheack.get(0), _$check.find('.mcheack').get(0)]).change(function () {
                        _$source.filter((function (_trigger) {
                            return function () {
                                return this != _trigger;
                            };
                        })($(this).data('_trigger'))).data('_trigger', this).prop('checked', $(this).prop('checked')).change();
                        $(this).removeData('_trigger');
                    });
                }
                _$mirror.find('.sort').removeClass('sort');
                $('.grid_scrollFootInner table tfoot tr', _$dataGrid).append(_$mirror);
            });
            var _pager = $('#pager', _$dataGrid).pager($('#searchForm', _$dataGrid), 15, $('.grid_scroll', _$dataGrid).view().on('add', function (data) {
                $('.delete', this.target).click(function () {
                    return false;
                });
                this.target.addClass(this.getIndex() % 2 == 0 ? 'odd' : 'even');
                if(this.getIndex() + 1 >   _clas.pager().pageSize/2){
                    $('.dropdown',this.target).removeClass('dropdown').addClass('dropup');
                }
            })).on('change',function(){
                $('[checkall]', _$ths).triggerHandler('tip',[null,[],0]);
                $('.start-number',_$dataGrid).html(Math.max(this.first+1,1));
                $('.end-number',_$dataGrid).html(Math.min(Math.max(this.first,0) + this.pageSize,this.totalCount));
                $('.total-number',_$dataGrid).html(this.totalCount);
            });
            var _clas = {
                setJSON: function (data) {
                    _pager.setJSON(data);
                    return this;
                },
                view: function () {
                    return _pager.view;
                },
                pager: function () {
                    return _pager;
                }
            };
            if(settings.hasOwnProperty('height')){
                if(Fantasy.isString(settings['height'])){
                    $('.grid_scrollBody', _$dataGrid).css('height',settings['height']);
                }else if(Fantasy.isFunction(settings['height'])){
                    $('.grid_scrollBody', _$dataGrid).css('height',settings['height']());
                }
            }else {
                var _dh = _$dataGrid.height() - $('.grid_scrollBody', _$dataGrid).height() + 30;
                _$dataGrid.closest('.grid-panel').on('resize', function () {
                    $('.grid_scrollBody', _$dataGrid).css('height', $(this).height() - _dh);
                }).triggerHandler('resize');
            }
            return _$dataGrid.on('setJSON', function (trigger, data) {
                _clas.setJSON(data);
            }).data('grid', _clas);
        }

    })()

});