//============================================================
//[描    述]  Fantasy.awt.View		主要功能：动态生成列表
//============================================================
Fantasy.util.jClass(Fantasy.util.Observable, (function () {

    return {

        jClass: 'Fantasy.awt.View',

        /**
         * 构造方法
         * @param {Object} $super    父类方法
         * @param {Object} options    初始设置
         */
        initialize: function ($super, target, options) {
            $super();
            this.addEvents('add', 'remove', 'dataFilter', 'beforeinsert', 'insert', 'sort');

            if (target.length == 0) {
                throw new Error('target 参数为NULL 或者未匹配到DOM元素 : ' + target);
            }

            this.target = target;

            Fantasy.copy(this, Fantasy.merge(options || {}, {
                url: null,
                postData: {},
                jsonRoot: function (data) {
                    return data;
                },
                data: [],//初始化数据
                elements: new Fantasy.util.Collection(),//Fantasy.awt.Element的集合
                templates: (function (zhis, options) {//提取模板
                    zhis.templates = new Fantasy.util.Map();
                    if (options.template) {
                        zhis.addTemplate('default', options.template, options.fieldClass, options.tplJClass);
                    } else if (options.templates) {
                        options.templates.each(function () {
                            zhis.addTemplate(this.name, this.template, this.fieldClass, this.tplJClass);
                        });
                        options.templates = null;
                    }
                    return zhis.templates;
                })(this, options),//模板集合
                templateDefault: 'default',//默认模板类
                state: 'none'//当前状态(TODO带修改)
            }));
            //采用事件机制处理添加,删除元素
            //将单元添加到 target 里
            this.elements.on('add', (function (zhis) {
                return function (element, index) {
                    if (index < (this.size() - 1)) {//从中间插入元素
                        var next = this.get(index + 1).target;
                        if (next && next.length != 0) {
                            element.target.insertBefore(next);
                        }
                        this.each(function (_index) { //重置序列号
                            if (_index > index) {
                                this.setIndex(_index);
                                this.refresh();
                            }
                        });
                    } else {//末尾追加元素
                        var prev = this.size() > 1 ? this.get(index - 1).target : zhis.prev;
                        if (prev && prev.length != 0) {
                            element.target.insertAfter(prev);
                        } else {
                            zhis.parent.prepend(element.target);
                        }
                    }
                    element.setIndex(index);
                    element.refresh();
                };
            }(this)), this);
            //将单元从到 target 里移除掉
            this.elements.on('remove', function (element, index) {
                this.each(function (_index) {
                    if (_index > (index - 1)) {
                        this.setIndex(_index);
                        this.refresh();//this.update();
                    }
                });
            }, this);

            if (this.url) {
                this.reload();
            }
        },

        setState: function (state) {
            this.state = state;
        },

        setTemplateDefault: function (tname) {
            var _template = this.getTemplate(this.templateDefault = tname);
            this.elements.each(function () {
                this.setTemplate(_template);
            });
            return this;
        },

        getData: function () {
            var data = [];
            this.elements.each(function () {
                data.push(this.getData());
            });
            return data;
        },

        /**
         * 添加一个新的模板到 Fantasy.util.View 中
         * @param {String} name
         * @param {jQuery} template
         * @param {String} fieldClass
         */
        addTemplate: function (name, template, fieldClass, tplJClass) {
            if (!name || name == '') {
                throw new Error('name 参数为NULL : ' + name);
            }
            if (!template || template.length == 0) {
                throw new Error('template 参数为NULL 或者未匹配到DOM元素 : ' + template);
            }
            if (this.templates.containsKey(name)) {
                throw new Error('template  name = ' + name + ',的重复定义!');
            }
            if (name == 'default') {
                this.prev = template.prev();
                this.parent = template.parent() || this.target;
            }
            var jclass = Fantasy.defaultValue(tplJClass, Fantasy.awt.Template);
            this.templates.put(name, new jclass(template, {fieldClass: fieldClass}));
        },

        /**
         * 通过JSON数据初始化容器
         * @param {json} json    初始数据
         * @param {Function} callback    数据初始化完成后的回调
         */
        setJSON: function (json, callback) {
            this.clear();
            this.update(json);
            callback ? callback(json) : null;
            return this;
        },

        /**
         * 通过一个url地址初始化容器
         * @param {String} url    远程数据地址
         * @param {String} postData 请求参数
         * @param {String} jsonRoot     数据
         */
        setJSONUrl: function (url, postData, jsonRoot) {
            Fantasy.copy(this, Fantasy.merge({
                url: url,
                postData: postData,
                jsonRoot: jsonRoot
            }, {
                url: this.url,
                postData: this.postData,
                jsonRoot: this.jsonRoot
            }));
            return this;
        },

        setPostData: function (postData) {
            Fantasy.copy(this.postData, postData || {});
        },

        reload: function (postData, callback) {
            if (Fantasy.isFunction(postData)) {
                callback = postData;
                postData = {};
            }
            this.setPostData(postData);
            var zhis = this;
            jQuery.getJSON(this.url, this.postData, function (data) {
                zhis.setJSON(zhis.jsonRoot ? ((typeof zhis.jsonRoot === 'string') ? data[zhis.jsonRoot] : zhis.jsonRoot.apply(zhis, [data])) : data);
                callback ? callback(data) : null;
            });
            return this;
        },

        /**
         * 添加单元
         * @param index 添加到的位置，不写默认添加到末尾
         * @param data 添加的数据
         * @param templateName 模板类型
         * @returns {Element}
         */
        add: function (index, data, templateName) {
            if (jQuery.isPlainObject(index)) {
                templateName = data;
                data = index;
                index = this.elements.size();
            }
            this.setState('add');
            try {
                return this.insert(index, data, templateName);
            } finally {
                this.setState('none');
            }
        },

        /**
         *   添加
         * @param index
         * @param data
         * @param templateName
         * @returns {Element}
         */
        insert: function (index, data, templateName) {
            if (typeof index == 'number') {
                index = index > this.elements.size() ? this.elements.size() : index;
            } else if (typeof index == 'object') {
                templateName = data;
                data = index;
                index = this.elements.size();
            }
            if (typeof data == 'undefined') {
                throw new Error("insert 参数 data 为 NULL");
            }
            //this.fireEvent('dataFilter', this.view, [data,oldData]);
            var element = new Fantasy.awt.Element(this.getTemplate(templateName ? templateName : this.templateDefault), data);
            this.fireEvent('beforeinsert', element, [data, data.index]);
            this.elements.add(index, element);
            this.fireEvent('add', element, [element.getData(), element]);
            this.fireEvent('insert', this, [data, data.index, element]);
            return element;
        },

        /**
         * 更新数据
         * @param {number} index    如果 index 为数组对象 更新从下标为0开始的数据
         * @param {json} data        如果 data 为数组 则从下标为index开始更新
         */
        update: function (index, data) {
            this.setState('update');
            var zhis = this;
            if (index == null || (typeof index == 'number' && index >= this.elements.size()))
                return;
            if (typeof index == 'number') {
                if (Object.prototype.toString.call(data) === '[object Array]') {
                    data.each(function (i) {
                        zhis.elements.get((index + i)).update(this);
                    });
                } else {
                    this.elements.get(index).update(data);
                }
            } else if (Object.prototype.toString.call(index) === '[object Array]') {
                data = index;
                if (data.length > this.elements.size()) {
                    data.each(function (i) {
                        if (zhis.elements.get(i)) {
                            zhis.elements.get(i).update(this);
                        } else {
                            zhis.insert(this);
                        }
                    });
                } else {
                    var removeElements = [];
                    this.elements.each(function (i) {
                        if (data[i]) {
                            this.refresh(data[i]);
                        } else {
                            removeElements.push(i);
                        }
                    });
                    removeElements.each(function (i) {
                        zhis.remove(this - i);
                    });
                }
            }
            this.setState('none');
        },

        /**
         * 获取索引对应的 Fantasy.awt.View.Element 对象
         * @param {Object} index
         */
        get: function (index) {
            var element = this.elements.get(index);
            if (!element)
                throw new Error('Index = ' + index + '对应的 Fantasy.awt.View.Element 不存在!');
            return element;
        },

        size: function () {
            return this.elements.size();
        },

        /**
         * 通过数据查询行对象
         * @param {String}  name
         * @param {Object}  value
         */
        find: function (name, value) {
            return this.elements.each(function () {
                if (Fantasy.getValue(this.getData(), name) == value)
                    return this;
            });
        },

        each: function () {
            return this.elements.each.apply(this.elements, arguments);
        },

        /**
         * 获取key对应的 模板,如果不提供参数获取默认模板
         * @param {String} name
         */
        getTemplate: function (name) {
            name = name ? name : this.templateDefault;
            return typeof this.templates == 'undefined' ? null : this.templates.get(name);
        },

        /**
         * 改变行的模板
         * @param index 下标
         * @param name 模板名称
         * @param data 需要更新的数据(选填)
         * @returns {*}
         */
        setTemplate: function (index, name, data) {
            var element = this.get(index);
            element.setTemplate(this.getTemplate(name), data);
            this.fireEvent('add', element, [element.getData(), element]);
            return element;
        },

        /**
         * 清空容器
         */
        clear: function () {
            this.setState('clear');
            for (var i = this.elements.size() - 1; i > -1; i--) {
                this.elements.remove(i).target.remove();
            }
            this.setState('none');
        },

        /**
         * 移除子项 根据index
         */
        remove: function (index) {
            this.setState('remove');
            var delObj = this.elements.remove(index);
            if (typeof delObj !== 'undefined') {
                var val = this.fireEvent('remove', delObj, delObj.getData());
                if (val != false) {
                    delObj.target.remove();
                }
            }
            this.setState('none');
            return delObj;
        },

        /**
         * 排序方法
         * @param fieldName 字段名称
         * @param desc 排序方向
         */
        sort: function (fieldName, desc) {
            var dataFields = this.getTemplate().dataFields;
            var dataField = dataFields.get(fieldName);
            if (this.url) {
                if (this.fireEvent('sort', this, [compare, fieldName, desc, dataField]) != false) {
                    this.reload(jQuery.isFunction(this.postData) ? postData.apply(this, {order: desc, orderBy: fieldName}) : Fantasy.merge(this.postData, {order: desc, orderBy: fieldName}));
                }
            } else {
                var compare = (function () {
                    return function (to) {
                        if (desc) {
                            return dataField ? dataField.sortType(this[fieldName]) > dataField.sortType(to[fieldName]) : this[fieldName] > to[fieldName];
                        } else {
                            return  dataField ? dataField.sortType(this[fieldName]) < dataField.sortType(to[fieldName]) : this[fieldName] < to[fieldName];
                        }
                    };
                })();
                if (this.fireEvent('sort', this, [compare, fieldName, desc, dataField]) != false) {
                    this.setJSON(Fantasy.sort(this.getData(), compare));
                }
            }
        },

        refresh: function () {
            this.elements.sort(function (a, b) {
                return a.target.index() > b.target.index() ? 1 : -1;
            });
            this.each(function (_index) {
                this.setIndex(_index);
                this.refresh();
            });
        }

    };
})());