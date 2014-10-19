Fantasy.util.jClass((function () {

    /**
     *根据模板返回元素的 mappings
     * @param $template 模板对象
     * @param fieldClass 元素的class查找方式
     * @returns {Fantasy.util.Map}
     */
    var getDataFields = function ($template, fieldClass) {
        var dataFields = new Fantasy.util.Map();
        $.each($template.find('[name].' + fieldClass), function () {
            var dataField = getDataField($(this));
            if (dataFields.containsKey(dataField.name)) {
                if($(this).is('[type=radio]') || $(this).is('[type=checkbox]')){
                    return;
                }else{
                    throw new Error('view-field  name = ' + dataField.name + ',的重复定义!');
                }
            }
            dataFields.put(dataField.name, dataField);
        });
        return dataFields;
    };

    /**
     * 根据jquery的对象获取元素的定义
     * @param $element
     * @returns {*}
     */
    var getDataField = function ($element) {
        var fullName = $element.attr('name');
        var name = fullName.replace('[#index]', '');
        var mapping = $element.attr('mapping');
        var defaultValue = $element.attr('defaultValue');
        var type = getType($element);
        var dataType = $element.attr('dataType');
        var dateFormat = $element.attr('format');
        return Fantasy.copy(new Fantasy.data.Field({
            mapping: Fantasy.defaultValue(mapping, name),
            defaultValue: Fantasy.util.Format.undef(defaultValue),
            dataType: dateFormat ? 'date' : dataType,
            dateFormat: dateFormat
        }), {
            name: name,// 名称
            fullName: fullName,
            type: Fantasy.util.Format.defaultValue(type, 'label')
        });
    };

    var types = [
        {
            key: 'a',
            value: 'a'
        },
        {
            key: 'img',
            value: 'img'
        },
        {
            key: 'input',
            value: 'input'
        },
        {
            key: 'label',
            value: 'p,span,label,li'
        },
        {
            key: 'select',
            value: 'select'
        },
        {
            key: 'text',
            value: 'textarea'
        }
    ];

    /**
     * 获取 view-field 的标签类型
     * @param _$element
     * @returns {*}
     */
    var getType = function (_$element) {
        return types.each(function () {
            if (_$element.is(this.value)) {
                return this.key;
            }
        });
    };

    return {

        jClass: "Fantasy.awt.Template",

        initialize: function ($temple, options) {
            if (!$temple || $temple.length == 0) {
                throw new Error('template 参数为NULL 或者未匹配到DOM元素');
            }
            Fantasy.copy(this, Fantasy.merge(options || {}, {
                fieldClass: "view-field",
                template: $temple.remove()
            }));
            this.dataFields = getDataFields(this.template, this.fieldClass);
        },

        applyTemplate: function (data, fieldCallback) {
            var $template = this.template.clone().html(Fantasy.getTemplate(this.template.html()).applyTemplate(data));
            var renameCallbacks = $.Callbacks() , fileds = [];
            this.dataFields.each(function () {//标签值转换操作
                var dataField = this.value, $el = $template.find('[name="' + dataField.fullName + '"]');
                if ($el.length == 0) {
                    throw new Error("[name=" + dataField.fullName + "]匹配元素失败!");
                }
                fileds.push(new Fantasy.awt.Field($el, dataField));
            });
            $template.find("[id*=#index],[name*=#index]").each(function () {
                var _$el = $(this), name = this.name, id = this.id;
                if ($(this).is("[id*=#index]")) {
                    renameCallbacks.add(function (index) {
                        _$el.attr('id', id.replaceAll('#index', index));
                    });
                }
                if ($(this).is("[name*=#index]")) {
                    renameCallbacks.add(function (index) {
                        _$el.attr('name', name.replaceAll('#index', index));
                    });
                }
            });
            if (!!fieldCallback) {
                fieldCallback.apply(this, [fileds, function (index) {
                    renameCallbacks.fire(index);
                }]);
            }
            return $template;
        }

    };

})());
