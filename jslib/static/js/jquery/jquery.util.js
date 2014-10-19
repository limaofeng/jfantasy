jQuery.extend({

    /**
     * 名称：getEvent        属于:jQuery
     * 描述：获取事件源
     * @return
     */
    getEvent: function () {
        if (document.all) {
            try {
                return $(window.event.srcElement);
            }
            catch (e) {
                return null;
            }
        }
        func = jQuery.getEvent.caller;
        var i = 100;
        while (func != null && i-- > 0) {
            var event = func.arguments ? func.arguments[0] : null;
            if (event) {
                if ((event.constructor == Event || event.constructor == MouseEvent) || (typeof(event) == "object" && event.preventDefault && event.stopPropagation)) {
                    return $(event.target);
                }
            }
            func = func.caller;
        }
        return null;
    },

    uploadBox: function (setting, uploadType) {
        var setting = Fantasy.merge(setting || {}, {
            url: request.getContextPath() + '/attach/upload/box.do?uploadType=' + uploadType,
            title: "文件上传组件",
            lock: true,
            fixed: true,
            padding: "10px 20px",
            opacity: 0.87
        });
        this.dialog(setting);
    },

    /** 设置屏锁 */
    lock: function () {
        div = document.createElement('div'),
            $div = $(div),
            $div.css({
                zIndex: 1000,
                position: 'absolute',
                left: 0,
                top: 0,
                width: $(window).width() + 'px',
                height: $(document).height() + 'px',
                overflow: 'hidden'
            }).addClass('d-mask');
        /*
         $div.bind('click', function () {
         that._reset();
         }).bind('dblclick', function () {
         that._click('cancel');
         });
         */
        top.document.body.appendChild(div);
        return $div;
    },

    /** 解开屏锁 */
    unlock: function () {
        $('.d-mask').remove();
    },

    scrollToTop: function (e) {
        e = !e ? "a#totop" : e;
        $(e).hide().removeAttr("href");
        if ($(window).scrollTop() != "0") {
            $(e).fadeIn("slow");
        }
        var scrollDiv = $(e);
        $(window).scroll(function () {
            if ($(window).scrollTop() == "0") {
                $(scrollDiv).fadeOut("slow");
            } else {
                $(scrollDiv).fadeIn("slow");
            }
        });
        $(e).click(function () {
            $("html, body").animate({
                scrollTop: 0
            }, "slow");
        });
    }

});

jQuery.fn.extend({

    /**
     * 扩展 Jquery 的Val 方法 使其能识别 checkbox 和 radio 如果 不是按 name 分组的话 建议通过 Class 分组
     * 例如： $(".groupclass").val("1"); $(".groupclass").val();
     */
    vals: (function (val) {
        return function (value) {
            var _type = $(this).attr("type") || "text";
            var vals = [];
            if (value === undefined) {
                if (this.length == 1){
                    vals.push(val.apply(this, arguments));
                }else if ($(this).is('input') && _type == "radio") {
                    $(this).filter(":checked").each(function () {
                        if (this.checked) {
                            vals.push(this.value);
                        }
                    });
                }else if ($(this).is('input') && _type == "checkbox") {
                    $(this).filter(":checked").each(function () {
                        if (this.checked) {
                            vals.push(this.value);
                        }
                    });
                }else{
                    $.each(this, function () {
                        vals.push($(this).val());
                    });
                }
                return vals;
            } else {
                if ($(this).is('input') && _type == "radio") {
                    $(this).each(function () {
                        if (this.value == value) {
                            this.checked = true;
                        }
                    });
                    return $(this);
                } else if ($(this).is('input') && _type == "checkbox") {
                    $(this).checkedNo();
                    $(this).each(function () {
                        var checkbox = this;
                        if (Fantasy.isArray(value)) {
                            jQuery.each(value, function () {
                                if (checkbox.value == this) {
                                    checkbox.checked = true;
                                }
                            });
                        } else {
                            if (this.value == value) {
                                this.checked = true;
                            }
                        }
                    });
                    return $(this);
                } else {
                    var v = arguments[0];
                    if ($(this).attr('date') && !!v) {
                        try {
                            var format = arguments[1];
                            if (Fantasy.isDate(v)) {
                                v = v.format($(this).data('date_settings').dateFmt)
                            } else {
                                 if(!format){
                                 format = $(this).data('date_settings').dateFmt;
                                 }
                                 v = Date.parse(v.toString(),format==null?'yyyy-MM-dd hh:mm:ss':format).format($(this).data('date_settings').dateFmt);
                            }
                        } catch (e) {
                        }
                    }
                    arguments[0] = v;
                    return val.apply(this, arguments);
                }
            }
        };
    })(jQuery.fn.val),

    /**
     * 复选框 全选操作
     */
    checkedAll: function () {
        $(this).prop("checked", true).change();
        return this;
    },

    /**
     * 复选框 取消选择
     */
    checkedNo: function () {
        $(this).prop("checked",false).change();
        return this;
    },

    /**
     * 复选框 反选操作
     */
    checkedRev: function () {
        $.each(this, function () {
            var _checked = !this.checked;
            $(this).prop('checked',_checked);
            // this.change();
        });
        return this;
    },

    /**
     * 判断按钮是否选中,如果有参数的话  判断传入值是否被选中
     */
    isCheck: function (value) {
        var _isCheck = false;
        var len = 0;
        if ($(this).attr("type") === "checkbox") {
            if (value == null) {
                _isCheck = $(this).val() != null;
            } else {
                var checkValues = $(this).val();
                if (checkValues != null && checkValues.length == value.length)
                    jQuery.each(checkValues, function () {
                        var checkValue = this;
                        jQuery.each(value, function () {
                            if (checkValue.toString() == this.toString()) {
                                len++;
                                return;
                            }
                        });
                    });
                _isCheck = len == value.length;
            }
            if (value != null)
                _isCheck = (len == value.length && $(this).val().length == value.length);
        } else if ($(this).attr("type") === "radio") {
            if (value == null) {
                _isCheck = $(this).val() != null;
            } else {
                _isCheck = $(this).val().toString() == value.toString();
            }
        }
        return _isCheck;
    },

    left: function () {
        return parseInt(this.offset().left);
    },

    top: function () {
        return parseInt(this.offset().top);
    },
    /*
     TODO 应该没有重写的必要了
     animate : (function(animate) {
     return function() {
     var funIndex = Fantasy.isFunction(arguments[3]) ? 3 : 2;
     if (arguments.length <= funIndex) {
     arguments.length = (funIndex + 1);
     }
     arguments[funIndex] = (function(callback) {
     return function() {
     if (Fantasy.isFunction(callback)) {
     callback.call(this, arguments);
     }
     if ($(this).css('opacity') == '1') {
     $(this).css('filter', '');
     $(this).attr('style', Fantasy.nullValue($(this).attr('style')).replace(/(filter|FILTER)(\S+)[;]?/g, ''));
     }
     };
     })(arguments[funIndex]);
     return animate.apply(this, arguments);
     };
     })(jQuery.fn.animate),
     */
    serialize: (function (serialize) {
        return function () {
            if (!$(this).is("form")) {
                var $temp = $('<form></form>').append($(this).clone());
                $(this).find('textarea').each(function () {
                    $temp.find('[name="' + $(this).attr('name') + '"]').val($(this).val());
                });
                $(this).find('select').each(function () {
                    $temp.find('[name="' + $(this).attr('name') + '"]').val($(this).val());
                });
                return $temp.serialize();
            } else {
                return serialize.apply(this, arguments);
            }
        }
    })(jQuery.fn.serialize),

    disabled: function (b) {
        b = arguments.length == 0 ? true : b;
        $(this).find('input,textarea,select').each(function () {
            this.disabled = b;
            if($(this).is('select') && $(this).hasClass('chosen-select')){
                $(this).change();
            }
        });
        $(this).data('disabled', b);
        return this;
    },

    resetForm: (function (resetForm) {
        return function () {
            if (arguments.length == 0) {
                if (!$(this).is("form")) {
                    $(this).find('input,textarea,select').each(function () {
                        $(this).val(this.defaultValue).change();
                    });
                    return this;
                } else {
                    return resetForm.apply(this, arguments);
                }
            } else {
                var json = arguments[0];
                for (var p in json) {
                    var $el = $(this).find('[name="' + p + '"]');
                    if (!!$el.length) {
                        $el.val(json[p]).change();
                        $el.get(0).defaultValue = json[p];
                    }
                }
                return this;
            }
        };
    })(jQuery.fn.resetForm),

    remove: (function (remove) {
        return function () {
            try {
                $(this).each(function () {
                    $(this).triggerHandler('remove');
                });
            } catch (e) {
                //TODO 不抛出异常(暂时只支持ajaxLoadDiv的remove事件)
            }
            return remove.apply(this, arguments);
        };
    })(jQuery.fn.remove)

});

jQueryUtil = (function () {
    return {
        pageX: function (ele) {
            return ele.offsetParent ? (ele.offsetLeft + jQueryUtil.pageX(ele.offsetParent)) : ele.offsetLeft;
        },

        pageY: function (ele) {
            return ele.offsetParent ? (ele.offsetTop + jQueryUtil.pageY(ele.offsetParent)) : ele.offsetTop;
        }
    };
})();

jQuery.fn.extend({

    box: function (trigger, options) {
        var box = this.data('box');
        if (!box) {
            box = new Fantasy.awt.Box(Fantasy.merge(options || {}, {
                widget: $(this),
                trigger: trigger,
                positionAdjust: [ 0, 0 ]
            }));
            this.data('box', box);
        }
        return box;
    },

    tree: function (options) {
        var tree = this.data('tree');
        if (!tree) {
            tree = new Fantasy.awt.Tree($(this), Fantasy.merge(options || {}, {
                node: $(this).find('.node')
            }));
            this.data('tree', tree);
            if (tree.$node.find('button[class^=ico_]').length > 0) {
                tree.on('handleList', function (tree, parent, node) {
                    this.$html.find('button[class^=ico_]:eq(0)').attr('class', this.view.target.is(':hidden') ? 'ico_close' : 'ico_open');
                });
            }
            if (options.click) {
                tree.on('insert', function (tree, parent, node) {
                    this.$html.children('a').click(function () {
                        options.click.apply(tree, [ node, parent ]);
                    });
                });
            }
        }
        return tree;
    },

    zTree: (function (_zTree) {
        return function (settings, zNodes) {
            var zTree = this.data('zTree');
            if (!zTree) {
                settings = Fantasy.merge(settings || {}, {
                    level: 100,
                    async: {
                        enable: settings.url ? true : false,
                        url: settings.url,
                        dataFilter: settings.dataFilter,
                        autoParam: [ "id" ]
                    },
                    edit: {
                        enable: settings.edit == true,
                        showAddBtn: true
                    }
                });
                settings = Fantasy.merge(settings || {}, {
                    view: {
                        dblClickExpand: false,// 双击展开父节点的功能
                        selectedMulti: false,
                        addHoverDom: settings.edit.enable ? (function () {
                            return function (treeId, treeNode) {
                                var $node = $("#" + treeNode.tId + "_span");
                                if (treeNode.edit == false) {
                                    $node.parent().find('.edit').remove();
                                }
                                if (treeNode.remove == false) {
                                    $node.parent().find('.remove').remove();
                                }
                                $node.parent().find('.edit').click(function (e) {
                                    return stopDefault(e);
                                });
                                $node.parent().find('.remove').click(function (e) {
                                    return stopDefault(e);
                                });
                                if ((treeNode.add != false && !!settings.edit.showAddBtn) && treeNode.level < settings.level) {
                                    if (treeNode.editNameFlag || $("#addBtn_" + treeNode.id).length > 0)
                                        return;
                                    $("<button type='button' class='add' id='addBtn_" + treeNode.id + "' title='add node' onfocus='this.blur();'></button>").appendTo($node).bind("click", function () {
                                        if (settings.callback.add) {
                                            settings.callback.add.apply(zTree, [ treeId, treeNode ]);
                                        }
                                        return stopDefault(e);
                                    });
                                }
                            };
                        })() : null,
                        removeHoverDom: settings.edit.enable ? (function () {
                            return function (treeId, treeNode) {
                                $("#addBtn_" + treeNode.id).unbind().remove();
                            };
                        })() : null
                    },
                    edit: {
                        drag: {
                            isCopy: false,
                            isMove: false
                        },
                        enable: false,
                        editNameSelectAll: true
                    },
                    async: {
                        enable: settings.async && settings.async.url ? true : false,// 异步处理
                        otherParam: {},
                        contentType: "application/json"// 提交参数方式，这里 JSON// 格式，默认form格式
                    },
                    callback: {// 回调函数，在这里可做一些回调处理
                        onClick: function () {
                        },
                        onAsyncSuccess: function (event, treeId, treeNode, msg) {
                        }
                    },
                    data: {
                        keep: {
                            leaf: false,
                            parent: true
                        },
                        key: {
                            checked: "checked",
                            children: "children",
                            name: "name",
                            title: "title"
                        },
                        simpleData: {
                            enable: true,
                            idKey: "id",
                            pId: "pId",
                            rootPId: null
                        }
                    }
                });
                if (!settings.async.enable) {
                    settings.callback.beforeAsync = function () {
                        return false;
                    };
                    var call = (function (dataFilter) {
                        return function (zNodes) {
                            return dataFilter.apply(window, [ zNodes, call ]);
                        };
                    })(settings.dataFilter);
                    zNodes = settings.dataFilter ? settings.dataFilter.apply(window, [ zNodes, call ]) : zNodes;
                }
                var zhis = $(this);
                var createTree = function (zNodes) {
                    var zTree = Fantasy.merge(_zTree.init(zhis, settings, zNodes), {

                        getParentNodes: function (node) {
                            var sNodes = [], tp = node.getParentNode();
                            while (!!tp) {
                                sNodes.push(tp);
                                tp = tp.getParentNode();
                            }
                            return sNodes;
                        },

                        setJSON: function (zNodes) {
                            if (!zNodes)return;
                            zTree = createTree(zNodes);
                            /*
                             while(zTree.getNodes().length > 0){
                             zTree.removeNode(zTree.getNodes()[0], false);
                             }
                             zNodes.each(function() {
                             zTree.addNodes(null, this);
                             });*/
                        }
                    });
                    zhis.data('zTree', zTree);
                    return zTree;
                };
                var zTree = createTree(zNodes);
            }
            return zTree;
        };

    })($.fn.zTree),

    /**
     * 翻页插件
     * @param first 可以为三种类型的参数<br/>
     *          $(form) 表单jquery对象
     *        url     请求数据的地址
     *        json    json数组
     *     注意：方法只会初始化一次反复调用返回第一次初始化的pager对象
     * @param pageSize
     * @param view
     * @returns {___pager0}
     */
    pager: function (first, pageSize, view, options) {
        var pager = this.data('pager');
        if (!pager) {
            $(this).addClass('pages');
            var settings = arguments.length == 4 ? options : {};
            var json = null;
            var $form = null;
            if (first instanceof jQuery) {
                if (!first.is('form')) {
                    throw new Error('$(el).pager 的第一个参数如果为 jquery对象必须是 form 元素');
                }
                settings.url = first.attr('action');
                $form = first;
            } else if (Fantasy.isString(first)) {
                settings.url = first;
            } else {
                json = first;
            }
            settings.pageSize = pageSize;
            pager = new Fantasy.awt.Pager($(this), settings);
            //添加额外的方法
            Fantasy.apply(pager, {}, {
                /**
                 * 绑定查询的表单
                 * @param $form 表单jQuery对象
                 * @param data 表单初始数据
                 * @returns {this} 返回当前pager对象
                 */
                setSearchForm: function ($form, data) {
                    //if(!!pager.form){} TODO 是否对上一一次绑定的表单做注销操作
                    pager.form = $form;
                    $form.ajaxForm({data: {'pager.pageSize': function () {
                        return pager.options.pageSize;
                    }}, success: function (data) {
                        pager.setPostData(Fantasy.parseQuery($form.serialize()));
                        pager.setJSON(data);
                        if (!pager.orderBy || data.orderBy == '') {
                            view.target.find('.sort').removeClass('desc').removeClass('asc');
                        }
                    }});
                    $form.find('input').keypress(function (e) {
                        if (e.keyCode == '13') {
                            $form.submit();
                            return stopDefault(e);
                        }
                    });
                    if (!!data) {
                        $form.resetForm(data);//重置表单
                    } else {
                        data = Fantasy.parseQuery($form.serialize());
                    }
                    pager.setUrl($form.attr('action'));
                    pager.setPostData(data);//绑定翻页时的参数
                    return this;
                },
                /**
                 * 绑定view
                 * @param view
                 */
                setView: function (view) {
                    pager.view = view;
                    if (!pager['view_change_fun']) {//移除之前绑定 view 的事件
                        pager.un('change', pager['view_change_fun']);
                    }
                    //绑定新的view事件
                    pager['view_change_fun'] = function (data) {
                        pager.view.setJSON(data);
                    };
                    pager.on('change', pager['view_change_fun']);
                }
            });
            if (!!$form) {//绑定查询表单
                pager.setSearchForm($form);
            }
            if (!!view) {//绑定view
                pager.setView(view);
            }
            //排序事件
            pager.view.on('sort', function (compare, fieldName, desc) {
                if (pager.items) {
                    pager.setJSON(Fantasy.sort(pager.items, compare));
                    $(window).resize();
                } else {
                    pager.reload(Fantasy.copy(pager.getPostData(), {
                        'pager.order': desc ? 'asc' : 'desc',
                        'pager.orderBy': fieldName
                    }));
                    $(window).resize();
                }
                return false;
            });
            this.data('pager', pager);
            if (!!json) {
                pager.setJSON(json);
                $(window).resize();
            }
        }
        return pager;
    },

    /**
     * Fantasy.awt.View 的jQuery插件调用方式
     */
    view: function (controlOptions) {
        var view = this.data('view');
        if (!view) {
            var settings = {
                templates: [],
                templateClass: 'template'
            };
            settings = Fantasy.merge(controlOptions || {}, settings);
            $(this).find('.' + settings.templateClass).each(function () {
                settings.templates.push({
                    name: Fantasy.defaultValue($(this).attr('name'), 'default'),
                    template: $(this),
                    fieldClass: Fantasy.defaultValue($(this).attr('fieldClass'), 'view-field'),
                    tplJClass: {'default': Fantasy.awt.Template, 'sweet': Fantasy.awt.SweetTemplate}[Fantasy.defaultValue($(this).attr('tplType'), 'default')]
                });
            });
            view = new Fantasy.awt.View($(this), settings);
            var empty = function (b) {
                var empty = view.target.next('.empty');
                if (empty.length == 0) {
                    empty = view.target.find('.empty');
                }
                if (empty.length != 0) {
                    if (b != null) {
                        if (b) {
                            empty.show();
                        } else {
                            empty.hide();
                        }
                    } else {
                        if (view.size() == 0) {
                            empty.show();
                        } else {
                            empty.hide();
                        }
                    }
                }
            };
            view.on('add', function (data) {
                var zhis = this;
                if (this.target.is('tr')) {
                    var _row = this.target.initialize();
                    this.target.click(function (e) {
                        if (e.target == _row.find('[type="checkbox"]').get(0) || !!$(e.target).closest('.actions').length) {//触发源为checkbox 或者为 链接
                            return;
                        }
                        //如果当前行不是选中的同时选中行大于2时，触发checkbox
                        if (!(view.target.find('.template').find("[type=\"checkbox\"]:checked").length > 1 && _row.find('[type="checkbox"]:eq(0)').is(":checked"))) {
                            _row.find('[type="checkbox"]:eq(0)').click();
                        }
                        view.each(function () {//取消非事件源行的选中效果
                            if (this.target != _row) {
                                this.target.find('[type="checkbox"]:eq(0)').prop("checked",false).change();
                            }
                        });
                    });
                }
                this.target.find('.switch').click(function (e) {
                    if ($(this).hasClass('only')) {
                        view.elements.each(function () {
                            if (this.getTemplateName() != 'default') {
                                view.setTemplate(this.getIndex(), 'default', false);
                            }
                        });
                    }
                    view.setTemplate(zhis.getIndex(), $(this).attr('template'), !$(this).hasClass('back'));
                    return stopDefault(e);
                });
                this.target.find('.remove').click(function (e) {
                    if($(this).hasClass('disabled')){
                        return e.stopPropagation();
                    }
                    $.dialog.confirm(Fantasy.defaultValue($(this).data('placeholder'),'是否确认删除该行?'), function () {
                        view.remove(zhis.getIndex());
                    });
                    return e.stopPropagation();
                });
                this.target.find('.showData').click(function (e) {
                    alert(Fantasy.encode(view.get(zhis.getIndex()).getData()));
                    return stopDefault(e);
                });
                this.target.find('.bindData').click(function (e) {
                    this.bindData = view.get(zhis.getIndex()).getData();
                    return stopDefault(e);
                });
                if(this.getIndex() == 0){
                    $('.up',view.target).removeClass('disabled');
                    $('.up',this.target).addClass('disabled');
                }
                if(this.getIndex() == (view.size()-1)){
                    $('.down',view.target).removeClass('disabled');
                    $('.down',this.target).addClass('disabled');
                }
                this.target.find('.up').click(function (e) {
                    var _index = zhis.getIndex();
                    if (_index == 0) {
                        $.msgbox({
                            msg: "已经是第一个了",
                            icon: "warning"
                        });
                    } else {
                        var checked = zhis.target.find('[type="checkbox"]:eq(0)').is(":checked");
                        view.add(_index - 1, zhis.getData());
                        view.get(_index - 1).target.find('[type="checkbox"]:eq(0)').attr("checked", checked).prop('checked', checked).change();
                        view.remove(zhis.getIndex());
                        $('.up',view.target).removeClass('disabled');
                        $('.up:first',view.target).addClass('disabled');
                        $('.down',view.target).removeClass('disabled');
                        $('.down:last',view.target).addClass('disabled');
                    }
                    return stopDefault(e);
                });
                this.target.find('.down').click(function (e) {
                    var _index = zhis.getIndex();
                    if (_index == (view.size() - 1)) {
                        $.msgbox({
                            msg: "已经是到末尾了",
                            icon: "warning"
                        });
                    } else {
                        var checked = zhis.target.find('[type="checkbox"]:eq(0)').is(":checked");
                        view.add(_index + 2, zhis.getData());
                        view.get(_index + 2).target.find('[type="checkbox"]:eq(0)').attr("checked", checked).prop('checked', checked).change();
                        view.remove(zhis.getIndex());
                        $('.up',view.target).removeClass('disabled');
                        $('.up:first',view.target).addClass('disabled');
                        $('.down',view.target).removeClass('disabled');
                        $('.down:last',view.target).addClass('disabled');
                    }
                    return stopDefault(e);
                });
            });
            view.on('add', function (data) {
                //window.initializes.apply(this.target);
                empty(false);
            });
            view.on('remove', function (data) {
                empty();
            });
            $(this).find('.add').click(function () {
                view.add({});
            });
            var zths = $(this);
            $(this).find('.sort').click(function () {
                var order = $(this).hasClass('asc');
                zths.find('.sort').removeClass('asc').removeClass('desc');
                view.sort($(this).attr('orderBy'), !order);
                $(this).addClass(order ? 'desc' : 'asc');
            });
            view.setJSON = (function (setjson) {
                return function (json) {
                    var retval = setjson.apply(view, arguments);
                    empty(!json || json.length == 0);
                    if (!!json && json.length > 0) {
                        view.target.initialize();
                        $(window).resize();
                    }
                    return retval;
                };
            })(view.setJSON);
            this.data('view', view);
        }
        return view;
    },

    form: function(controlOptions,data){
        var form = this.data('form');
        if (!form) {
            var settings = {
                templates: [],
                templateClass: 'template'
            };
            settings = Fantasy.merge(controlOptions || {}, settings);
            $('.' + settings.templateClass,this).each(function () {
                settings.templates.push({
                    name: Fantasy.defaultValue($(this).attr('name'), 'default'),
                    template: $(this),
                    fieldClass: Fantasy.defaultValue($(this).attr('fieldClass'), 'view-field'),
                    tplJClass: {'default': Fantasy.awt.Template, 'sweet': Fantasy.awt.SweetTemplate}[Fantasy.defaultValue($(this).attr('tplType'), 'default')]
                });
            });
            form = new Fantasy.awt.Form($(this), settings, data);
            form.on('add',function(data){
                var zhis = this;
                this.target.initialize();
                this.target.find('.switch').click(function (e) {
                    form.setTemplate(zhis.getIndex(), $(this).attr('template'), data);
                    return stopDefault(e);
                });
            });
            form.setJSON([data||{}]);
            this.data('form', form);
        }
        return form;
    },

    drag: function (options) {
        if (!this.data('drag')) {
            this.data('drag', new Fantasy.util.Drag($(this), options));
        }
        return this.data('drag');
    },

    comboBox: function () {
        var comboBox = this.data('comboBox');
        if (!comboBox) {
            //Fantasy.awt.ComboBox.effect = false;
            comboBox = new Fantasy.awt.ComboBox($(this), {
                size: 10
            });
            this.data('comboBox', comboBox);
        }
        return comboBox;
    },

    /**
     * 扩展方法用于验证图形码
     */
    validationImageCode: function (event) {
        alert($(this).val());
        event.preventDefault();
    },

    _chart: function (settings) {
        var colors = Highcharts.getOptions().colors;
        if (settings.data) {
            settings.series = [
                {
                    data: settings.data
                }
            ];
        }
        settings.series.each(function (i) {
            Fantasy.merge(this, {
                name: 'default',
                type: 'column'
            });

        });
        if (this.length > 1) {
            chart = new Array();
        }
        $.each(this, function () {
            var _c = new Highcharts.Chart({
                title: {
                    text: settings.title
                },
                chart: {
                    renderTo: this,
                    plotShadow: false
                },
                yAxis: {
                    //minorTickLength: 10,
                    //tickLength: 8,
                    //tickInterval: 20,
                    tickPixelInterval: 30,
                    alternateGridColor: '#cccccc',
                    title: {
                        text: ''
                    }
                },
                xAxis: {
                    categories: settings.categories,
                    title: {
                        text: ''
                    }
                },
                plotOptions: {
                    column: {
                        dataLabels: {
                            enabled: true,
                            color: colors[0],
                            style: {
                                fontWeight: 'bold'
                            },
                            formatter: settings.formatter
                        }
                    },
                    pie: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: true,
                            color: '#000000',
                            connectorColor: '#000000',
                            formatter: function () {
                                return '<b>' + this.point.name + '</b>: ' + this.percentage + ' %';
                            }
                        }
                    }
                },
                legend: {
                    y: 10,
                    floating: true,
                    borderWidth: 0
                },
                series: settings.series
            });
            if (this.length > 1)
                chart.push(_c);
            else
                chart = _c;
        });
        this.data('chart', chart);
    },

    /**
     * 整合柱状图、饼图
     */
    chart: function (settings) {
        var chart = this.data('chart');
        if (!chart) {
            if (settings.url) {
                (function (zhis) {
                    $.getJSON(settings.url, function (json) {
                        json.series.each(function () {
                            this.data.each(function () {
                                if (!this.x)
                                    delete this.x;
                            });
                        });
                        settings = Fantasy.merge(json, settings);
                        jQuery.fn._chart.apply(zhis, [ settings ]);
                    });
                })(this);
            } else {
                jQuery.fn._chart.apply(this, [ settings ]);
            }
        } else {
            if (settings && settings.url) {
                (function (zhis) {
                    $.getJSON(settings.url, function (json) {
                        if (json.series) {
                            json.series.each(function () {
                                this.data.each(function () {
                                    if (!this.x)
                                        delete this.x;
                                });
                            });
                        }
                        settings = Fantasy.merge(json, settings);
                        jQuery.fn._chart.apply(zhis, [ settings ]);
                    });
                })(this);
            }
        }
        return chart;
    },

    swfupload: function (settings) {
        var swfupload = this.data('swfupload');
        if (!swfupload) {
            settings = Fantasy.merge(settings || {}, {
                flash_url: request.getContextPath() + "/static/js/common/swfupload/swfupload.swf",
                upload_url: settings.uploadUrl,
                /*post_params: {"PHPSESSID" : "${pageContext.session.id}"},*/
                file_size_limit: "100 MB",
                file_types: settings.fileTypes,//"*.*",
                file_types_description: "All Files",
                file_upload_limit: 100,
                file_queue_limit: 0,
                file_post_name: this.attr('name'),//settings.fileName,//加了这个，在webwork的action中要用
                custom_settings: {
                    progressTarget: "fsUploadProgress"
                    //cancelButtonId : "btnCancel"
                },
                debug: false,
                // Button settings
                button_image_url: request.getContextPath() + "/static/js/common/swfupload/SWFUpload.png",//images/TestImageNoText_65x29.png
                button_width: "67",
                button_height: "17",
                button_placeholder_id: "spanButtonPlaceHolder",
                //button_text: '<img src="${pageContext.request.contextPath}/css/base/img/icons/tjfj.gif" alt="" />',//<span class="theFont">浏览</span>
                //button_text_style: ".theFont { font-size: 16; }",
                //button_text_left_padding: 12,
                //button_text_top_padding: 3,

                // The event handler functions are defined in handlers.js
                file_queued_handler: fileQueued,
                file_queue_error_handler: fileQueueError,
                file_dialog_complete_handler: fileDialogComplete,
                upload_start_handler: uploadStart,
                upload_progress_handler: uploadProgress,
                upload_error_handler: uploadError,
                upload_success_handler: uploadSuccess,
                upload_complete_handler: uploadComplete,
                queue_complete_handler: queueComplete, // Queue plugin event
                files: []
            });
            $('#fsUploadProgress').view().on('add', function () {
                this.target.find('.download').bind('click', function () {
                    $.dialog({
                        title: '提示信息',
                        content: '下载功能未实现',
                        fixed: true
                    });
                });
            }).on('remove', function (data) {
                this.target.find('.progressBarStatus').html('等待删除服务器中的文件...').show();
                this.target.find('.progressContainer').attr('class', 'progressContainer red');
                setTimeout((function (zhis) {
                    return function () {
                        zhis.target.slideUp(500, function () {
                            $(this).remove();
                        });
                    };
                })(this), 1000);
                return false;
            }).setJSON(settings.files);
            swfupload = new SWFUpload(settings);
            this.data('swfupload', swfupload);
        }
        return swfupload;
    },
    /*
     autocomplete : function(settings) {
     var autocomplete = this.data('autocomplete');
     if (!autocomplete) {
     autocomplete = new Fantasy.awt.AutoComplete($(this), Fantasy.merge(settings || {}, {}));
     this.data('autocomplete', autocomplete);
     }
     return autocomplete;
     },*/

    // 输入框默认提示
    placeholder: function (emptyClass) {
        emptyClass = emptyClass ? emptyClass : 'empty';
        var txt = $(this).attr('placeholder');
        try {
            $(this).removeAttr('placeholder');
        } catch (e) {
        }
        // 设置默认文字
        $(this).val($.trim($(this).val()) == '' || $.trim($(this).val()) == txt ? (function (zhis) {
            $(zhis).addClass(emptyClass);
            return txt;
        })(this) : $(this).removeClass(emptyClass).val()).bind({
            focus: function () {
                if ($(this).hasClass(emptyClass)) {
                    $(this).val('');
                    $(this).removeClass(emptyClass);
                }
            },
            blur: function () {
                if ($.trim($(this).val()) == '' && !$(this).hasClass(emptyClass)) {
                    $(this).val(txt);
                    $(this).addClass(emptyClass);
                }
            }
        });
        //绑定表达提交页面
        if ($(this).closest('form').length > 0) {
            $(this).closest('form').submit((function (zhis) {
                return function () {
                    if ($(zhis).hasClass(emptyClass))
                        $(zhis).val('');
                };
            })(this));
        } else {
            $(this).val = (function (funVal) {
                return function () {
                    if (arguments.length == 0) {
                        return $(this).hasClass(emptyClass) ? '' : funVal.apply(this, arguments);
                    } else {
                        return funVal.apply(this, arguments);
                    }
                };
            })($(this).val);
        }
    },

    selectionRange: function (start, end) {
        if ($(this).length == 0) {
            throw new Error('selectionRange 选择多个元素!');
        }
        var txtFocus = $(this).get(0);
        if (arguments.length == 0) {
            start = 0;
            end = $(this).val().length;
        } else if (arguments.length == 1) {
            end = start;
        }
        if ($.browser.msie) {
            var range = txtFocus.createTextRange();
            range.moveStart("character", start);
            range.moveEnd("character", end);
            //range.move("character", txtFocus.value.length);
            range.select();
        } else {
            //obj.setSelectionRange(startPosition, endPosition);
            txtFocus.setSelectionRange(start, end);
            txtFocus.focus();
        }
        return $(this);
    },

    selectionStart: function () {
        if ($(this).length != 1) {
            throw new Error('selectionStart 选择多个元素!');
        }
        var txtFocus = $(this).get(0);
        if ($.browser.msie) {
            var s = document.selection.createRange();
            s.setEndPoint("StartToStart", txtFocus.createTextRange());
            return s.text.length;
        } else {
            return txtFocus.selectionStart;
        }
    },

    selectionEnd: function () {
        if ($(this).length != 1) {
            throw new Error('selectionEnd 选择多个元素!');
        }
        var txtFocus = $(this).get(0);
        if ($.browser.msie) {
            var s = document.selection.createRange();
            s.setEndPoint("StartToEnd", txtFocus.createTextRange());
            return s.text.length;
        } else {
            return txtFocus.selectionEnd;
        }
    },

    switchStyle: ((function () {
        var getBodys = function () {
            var bodys = [$('body')], w = window;
            while (w.parent && w != top.window) {
                if (w.parent.$) {
                    bodys.push(w.parent.$('body'));
                }
                w = w.parent;
            }
            return bodys;
        }, bodys = null;
        return function (event, cssClass) {
            var zhis = $(this);
            if ('mousedown' == event) {
                if (!bodys) {
                    bodys = getBodys();
                }
                $(this).bind('mousedown', function (e) {
                    var zhis = $(this).addClass(cssClass);
                    window.removeClassDown = function (e) {
                        zhis.removeClass(cssClass);
                        zhis.unbind('mouseup', window.removeClassDown);
                        bodys.each(function () {
                            this.unbind('mouseup', window.removeClassDown);
                        });
                        return stopDefault(e);
                    };
                    zhis.bind('mouseup', window.removeClassDown);
                    bodys.each(function () {
                        this.bind('mouseup', window.removeClassDown);
                    });
                    return stopDefault(e);
                });
            }
            return $(this);
        };
    })())

});
