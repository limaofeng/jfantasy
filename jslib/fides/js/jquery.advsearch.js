jQuery.fn.extend({

    reset:(function(_reset){
        return function(){
            if($(this).data('advsearch')){
                 $(this).triggerHandler('reset');
            }else{
                _reset.apply(this,arguments);
            }
        }
    })(jQuery.fn.reset),

    advsearch: (function () {

        var _templateHtml = '<a class="btn medium primary-bg" href="javascript:;" data-toggle="dropdown">' +
            '<i class="glyph-icon icon-chevron-down font-size-11 float-right mrg10R"></i>' +
            '<span class="button-content">高级搜索</span>' +
            '</a>' +
            '<div class="dropdown-menu float-right">' +
            '<div class="pad5A medium-box body">' +
            '<h6 class="pad10L">' +
            '过滤条件:' +
            '</h6>' +
            '<div class="form-input col-md-12 mrg5B template" name="default">' +
            '<div class="row">' +
            '<input class="propertyFilterInput" type="hidden"/>' +
            '<div class="col-md-3">' +
            '<select data-placeholder="选择筛选字段..." class="chosen-select filterName" style="display: none;"></select>' +
            '</div>' +
            '<div class="col-md-3">' +
            '<select data-placeholder="选择筛选类型..." class="chosen-select matchType" style="display: none;"></select>' +
            '</div>' +
            '<div class="col-md-6 value-div" style="padding-right:30px;">' +
            '' +
            '<a title="" style="position:absolute;float:right;top: 3px;" data-placement="top" class="btn mrg5L small bg-red radius-all-100 remove disabled" href="javascript:;" data-original-title="Remove">' +
            '<i class="glyph-icon icon-remove"></i>' +
            '</a></div>' +
            '</div>' +
            '</div>' +
            '<div>' +
            '<a title="添加过滤条件" class="btn add-filter small primary-bg radius-all-100" href="javascript:;">' +
            '<span class="button-content">' +
            '添加条件' +
            '</span>' +
            '</a>' +
            '<a title="立即搜索" class="btn search-filter small primary-bg radius-all-100 mrg5R" href="javascript:;">' +
            '<span class="button-content">' +
            '立即搜索' +
            '</span>' +
            '</a>' +
            '</div>' +
            '</div>' +
            '</div>';

        var matchTypes = [
            {value: 'EQ', name: '等于'},
            {value: 'LIKE', name: '模糊查询'},
            {value: 'LT', name: '小于'},
            {value: 'GT', name: '大于'},
            {value: 'LE', name: '小于等于'},
            {value: 'GE', name: '大于等于'},
            {value: 'IN', name: '包含'},
            {value: 'NOTIN', name: '不包含'},
            {value: 'NE', name: '不等于'},
            {value: 'NULL', name: '为空'},
            {value: 'NOTNULL', name: '不为空'}
        ];
        var getMatchTypes = function (matchType) {
            var array = [];
            matchType.each(function () {
                var _matchType = matchTypes.each((function (mt) {
                    return function () {
                        if (this.value == mt) {
                            return this;
                        }
                    }
                })(this));
                if (!!_matchType) {
                    array.push(_matchType);
                }
            });
            return array;
        };

        return function (settings) {
            var view = $(".dropdown-menu .body", $(this).append(_templateHtml).addClass('dropdown')).click(function (e) {
                return e.stopPropagation();
            }).view().on('add', function () {
                var _zhis = this;
                var $propertyFilterInput = $('.propertyFilterInput', this.target), $matchType = $('.matchType', this.target).select3({value: 'value', text: 'name'}, function (data) {
                    if (!data)return;
                    $propertyFilterInput.prop('name', $matchType.val() + $filterName.val());
                }), $filterName = $('.filterName', this.target).select3({value: 'name', text: 'text'}, function (data) {
                    if (!data)return;
                    $matchType.load(getMatchTypes(data.matchType));
                    $('.filter-value',_zhis.target).remove();
                    switch(data.type){
                        case 'input':
                            $('.value-div',_zhis.target).prepend('<input placeholder="输入筛选条件..." type="text" name="" class="filter-value">');
                            break;
                        case 'select':
                            var $filterValue = $('<select data-placeholder="选择筛选条件..." class="chosen-select filter-value" style="display: none;width:100px;"></select>');
                            $('.value-div',_zhis.target).prepend($filterValue);
                            $filterValue.select3({value: 'value', text: 'text'}).load(data.values);
                            _zhis.target.initialize();
                            break;
                    }
                });
                $filterName.load(settings.filters);
                $(this.target).initialize();
                if (view.size() > 1) {
                    $('.remove', view.target).removeClass('disabled');
                }
            }).on('remove', function () {
                if (view.size() <= 1) {
                    $('.remove', view.target).addClass('disabled');
                }
                setTimeout(function () {
                    $(this.target).closest('form').submit();
                }, 100);
            });
            view.setJSON([
                {}
            ]);
            $('.add-filter', $(this)).click(function (e) {
                view.add({});
                return e.stopPropagation();
            });
            $('.search-filter', $(this)).click(function (e) {
                view.each(function () {
                    $('.propertyFilterInput', this.target).val($('.filter-value', this.target).val());
                });
                $(this).closest('form').submit();
                $(this).closest('.open').removeClass('open');
                return e.stopPropagation();
            });

            $(this).on('reset',function(){
                view.setJSON([
                    {}
                ]);
            });

            return $(this).data('advsearch',{});
        }

    })()

});