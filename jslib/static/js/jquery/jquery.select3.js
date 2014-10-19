jQuery.fn.extend({

    // 接收Url、func(该方法会绑定当前select元素的onchange事件)
    select3: function (url, mapping, fun) {
        if($(this).data('_select3')){
            return this;
        }
        $(this).data('_select3',true);
        if (arguments.length < 3 && jQuery.isPlainObject(url)) {
            fun = mapping;
            mapping = url;
            url = null;
        }
        // fun不为空，绑定onchange事件
        if (fun) {
            $(this).bind('change', function () {
                fun.apply($(this), [ $(this).find('option:selected').data('json') ]);
                if (!!$(this).data('chosen')) {
                    $(this).data('chosen').results_build();
                }
            });
        }
        var zhis = $(this);
        var show = function () {
            if(!!zhis.data('chosen')){
                zhis.data('chosen').container.show()
            }else if (!!zhis.data('select2')) {
                zhis.data('select2').container.show();
            } else {
                zhis.show();
            }
        };
        var hide = function () {
            if(!!zhis.data('chosen')){
                zhis.data('chosen').container.hide()
            }else if (!!zhis.data('select2')) {
                zhis.data('select2').container.hide()
            } else {
                zhis.hide();
            }
        };
        var getMappingValue = function(data,key){
            if(typeof mapping[key] == 'string'){
                return data[mapping[key]];
            }else{
                return mapping[key].apply(this,[data]);
            }
        };
        Fantasy.apply(this, {}, {
            /**
             * 方法会触发select的change事件
             */
            load: (function () {
                var loadData = function (data) {
                    if (jQuery.isArray(data) && data.length != 0) {// 如果请求结果为数组，且条数不为0时，将异步请求的结果
                        // 添加到select元素
                        if (!zhis.data('enable')) {
                            hide();
                        }
                        jQuery.each(data, function () {
                            var option = $('<option value="' + getMappingValue(this,'value') + '">' + getMappingValue(this,'text') + '</option>');
                            zhis.append(option);
                            if (!zhis.data('enable')) {
                                show();
                            }
                            option.data('json', this);
                        });
                    } else {
                        if (!zhis.data('enable')) {
                            hide();
                        }
                    }
                    if (zhis.attr('data-defval')) {
                        zhis.val(zhis.attr('data-defval'));
                        zhis.removeAttr('defval');
                    }
                    zhis.change();
                };
                return function (param) {
                    zhis.find('option[value!=""]').remove();
                    if (url) {// 如果URl存在，异步请求数据
                        if (!param) {// 参数不为空，执行异步请求，如果为空，触发自身的onchange事件
                            if (!zhis.data('enable')) {
                                hide();
                            }
                            zhis.change();// 触发自身的onchange事件
                            return;
                        }
                        jQuery.post(url, param, function (data) {
                            loadData(data);
                        });
                    } else {
                        if (jQuery.isArray(param)) {
                            loadData(param);
                        }
                    }
                    return this;
                };
            })()
        });
        return this;
    }

});