Fantasy.util.jClass(Fantasy.util.Observable, {

    jClass: 'Fantasy.awt.Upload',

    /**
     * 上传功能初始化
     * @param $super 父类引用
     * @param options 初始参数
     */
    initialize: function ($super, options) {
        /**
         * 事件说明:part
         *  load事件：传输成功完成。
         *  abort事件：传输被用户取消。
         *  error事件：传输中出现错误。
         *  loadstart事件：传输开始。
         *  loadEnd事件：传输结束，但是不知道成功还是失败。
         *  progress : 上传进度
         */
        $super(['uploadStart','uploadProgress', 'uploadAbort', 'uploadComplete']);
        var zhis = this;
        options = this.options = Fantasy.copy({
            url: request.getContextPath() + '/file/upload.do',
            theme: 'images',
            data: {'dir': 'default'}
        }, options);
        var theme = this.theme = Fantasy.awt.Upload.getTheme(options.theme);
        //初始化主题设置
        options = this.options = theme.defaultSettings(options, this);
        //添加风格代码
        var $html = this.$html = theme.appendTo(options.target, options);
        var fileView = this.fileView = $html.find('.file-list').view().on('add', function (data) {
            theme.callback.onAdd.apply(zhis, [fileView, this, data]);
            if (!!data.fileManagerId && !!data.absolutePath) {
                zhis.fireEvent('upload', zhis, [this, data, data.fileManagerId + ':' + data.absolutePath]);
            }
            this.on('refresh', function () {
                var data = this.getData();
                if (!!data.fileManagerId && !!data.absolutePath) {
                    zhis.fireEvent('upload', zhis, [this, data, data.fileManagerId + ':' + data.absolutePath]);
                }
            });
        }).on('remove', function () {
            //如果有ajax请求，应该终止
            var xhr = this.getValue('xhr');
            if (!!xhr) {
                xhr.abort();
            }
            $fileAddInput.val('');
        });
        //将附件从页面删除
        $html.find('.file-remove').click(function () {
            var rmids = [];
            fileView.each(function () {
                if (this.target.find('.index').is(":checked")) {
                    rmids.push(this.getIndex());
                }
            });
            rmids.reverse().each(function () {
                fileView.remove(this);
            });
        });
        //添加图片上传按钮功能
        var $fileAdd = $html.find('.file-add');
        var _interval = setInterval(function () {
            if ($fileAdd.index() == -1) {
                clearInterval(_interval);
                return;
            }
            if ($fileAdd.is(':visible')) {
                $fileAdd.next().css({width: $fileAdd.outerWidth()});
                clearInterval(_interval);
            }
        }, 500);
        var $fileAddInput = $('<input type="file" name="attach" class="file-add-input" multiple="multiple" accept="image/jpeg,image/gif,image/png,application/zip" style="display: block;height:' + $fileAdd.outerHeight() + 'px;left: 0px;opacity: 0;filter:alpha(opacity=0);position: relative;margin-top: -' + ($fileAdd.outerHeight() + 4) + 'px;width: ' + $fileAdd.outerWidth() + 'px;z-index: 26;">').insertAfter($fileAdd);
        //批量上传图片效果
        /*
         $html.find('.plupload_start').click(function(){
         uploadFile(uploadView.get(0),uploadView);
         });*/
        this.addFileInput($fileAddInput);
        theme.afterInit(this);
    },

    addFileInput: function ($fileInput) {
        var zhis = this;
        //fileinput change 事件
        $fileInput.bind('change', function () {
            if ($.browser.msie) {
                alert('上传功能,暂不支持IE浏览器');
                return;
            }
            if ($(this).hasClass('file-add-input')) {
                zhis.addFile($fileInput);
            } else {
                zhis.updateFile($fileInput, zhis.fileView.get($fileInput.data('index')));
            }
        });
    },

    getHtml: function () {
        return this.$html;
    },

    updateFile: function ($file, row) {
        var files = $file[0].files;
        for (var i = 0, len = files.length; i < len; i++) {
            var file = files[i];
            //var fileEl = this.fileView.find('fileName', file.name);
            if (row && row.data.size == file.size)
                continue;
            var fd = new FormData();
            fd.append('attach', file);
            //添加临时文件
            this.ajaxUplaodFile(row, fd);
            /*
             if($("#preview").length != 0){//TODO 待调整
             $.preview(file, $("<img />").insertBefore($("#preview")), {
             ratio : null,
             maxWidth : 200,
             maxHeight : 200
             });
             }*/
        }
    },

    addFile: function ($file) {
        var files = $file[0].files,queue = [];
        for (var i = 0, len = files.length; i < len; i++) {
            var file = files[i];
            var row = !!this._row ? this._row : this.fileView.add({fileName: file.name, size: file.size, type: file.type, unfinished: true , fileData: file});
            queue.push((function(file,_row,_zhis){
                return function(){
                    var xhr = _zhis.ajaxUplaodFile(file, _zhis.options.data,{
                        loadstart: function(){
                            _zhis.uploadStart(_row);
                        },
                        success: function (data) {
                            _zhis.uploadComplete(_row, data);
                            if(!!queue.length) {
                                queue.pop().call();
                            }
                        },
                        progress: function (loaded, total) {
                            var percentage = Math.round((loaded * 100) / total);
                            _zhis.uploadProgress(_row, percentage, total, loaded);
                        }
                    });
                    _row.setValue('xhr', xhr);
                };
            })(file,row,this));
            if(!!this._row){
                delete this._row;
                break;
            }
        }
        queue.reverse().pop().call();
    },

    _setRow : function(row){
        this._row = row;
    },

    /**
     * 上传文件方法
     */
    ajaxUplaodFile: function (file, postData, events) {
        var partSize = 1024 * 1024 * 50;//按每 50M 分割文件
        if (file.size <= partSize) {
            var xhr = new XMLHttpRequest();
            xhr.open('post', this.options.url, true);
            var callbacks = {};
            $.each(['success', 'loadstart', 'progress', 'abort', 'load', 'loadEnd', 'error', 'timeout'], function (i, value) {
                callbacks[value] = $.Callbacks();
            });
            $.each(events, function (key, value) {
                callbacks[key].add(value);
            });
            xhr.onreadystatechange = function () {
                if (xhr.readyState == 4 && xhr.status == 200) {
                    var data = Fantasy.decode(xhr.responseText);
                    delete data.id;
                    delete data.description;
                    delete data.folder;
                    delete data.createTime;
                    delete data.modifyTime;
                    delete data.creator;
                    delete data.modifier;
                    //上传成功回调
                    callbacks['success'].fire(data);
                }
            };
            //loadstart 事件：传输开始。
            xhr.upload.addEventListener('loadstart', function () {
                callbacks['loadstart'].fire();
            }, false);
            //progress 事件: 上传进度
            xhr.upload.addEventListener('progress', function (e) {
                /** @namespace e.lengthComputable */
                if (e.lengthComputable) {
                    var loaded = e.loaded, total = e.total;
                    if (loaded > total) {
                        loaded = loaded - total;
                    }
                    callbacks['progress'].fire(loaded, total);
                }
            }, false);
            //abort事件：传输被用户取消。
            xhr.upload.addEventListener('abort', function () {
                callbacks['abort'].fire(arguments);
            }, false);
            // load事件：传输成功完成。
            xhr.upload.addEventListener('load', function () {
            }, false);
            //loadEnd事件：传输结束，但是不知道成功还是失败。
            xhr.upload.addEventListener('loadEnd', function () {
            }, false);
            //error事件：传输中出现错误。
            xhr.upload.addEventListener('error', function () {
            }, false);
            //timeout事件: 在作者指定的时间段已经结束时触发。
            xhr.upload.addEventListener('timeout', function () {
            }, false);
            var fd = new FormData();
            fd.append('attach', file);
            $.each(postData, function (key, value) {
                fd.append(key, value);
            });
            xhr.send(fd);
            return xhr;
        } else {
            //分割文件
            var _xhr, _abort, zhis = this, fileParts = [], entireFileHash , loaded = 0, partLength = parseInt(file.size / partSize) + (file.size % partSize == 0 ? 0 : 1 ) , total = file.size;
            for (var i = 0, index = 1; i < file.size; index++, i += partSize) {
                var _part = {};
                _part.file = new Blob([file.slice(i, Fantasy.getMin(file.size, i + partSize))], {type: 'application/octet-stream',name:file.name + '.part' + ('' + index).addZeroLeft(2) ,lastModified: new Date()});
                _part.index = index;
                _part.total = partLength;
                _part.dir = 'part';
                _part.entireFileName = file.name;
                _part.entireFileDir = !postData.entireFileDir ? postData.dir : postData.entireFileDir;
                fileParts.push(_part);
            }
            //分段提交时，通过分段信息获取，postData
            var getPostData = function (part) {
                delete part.file;
                return Fantasy.copy(postData, part);
            };
            //ajax upload events
            var _events = {
                success: function (data) {
                    if (!!fileParts.length) {
                        loaded = (partLength - fileParts.length) * partSize;
                        var part = fileParts.pop();
                        _xhr = zhis.ajaxUplaodFile(part.file, getPostData(part), _events);
                        if (_abort)_xhr.abort();
                    } else {
                        if (events.hasOwnProperty('success')) {
                            events.success(data);
                        }
                    }
                },
                progress: function (partLoaded) {
                    if (events.hasOwnProperty('progress')) {
                        events.progress(loaded + partLoaded, total);
                    }
                },
                abort: function () {
                    fileParts.clear();
                }
            };
            //获取文件取样的hash值
            new hashMe(file, function (hash) {
                if (!entireFileHash) {
                    fileParts.each(function (i,v) {
                        v.entireFileHash = entireFileHash = hash;
                    });
                    //检查原来的上传信息
                    $.ajax({
                        url: request.getContextPath() + '/file/upload/pass.do',
                        data: {hash: hash},
                        dataType: 'json',
                        type: 'POST',
                        progress: 'none',
                        success: function (data) {
                            if (data.hasOwnProperty('fileDetail')) {//如果上传过此文件，直接忽略本次上传
                                if (events.hasOwnProperty('success')) {
                                    events.success(data["fileDetail"]);
                                }
                            } else {//忽略掉已上传的部分，并上传余下的内容
                                var hashLength = fileParts.length;
                                fileParts.each(function (i, v) {
                                    new hashMe(v.file, function (hash) {
                                        if (!v.partFileHash) {
                                            v.partFileHash = hash;
                                            if (!(--hashLength)) {//上传文件的hash值，计算完成
                                                data.parts.each(function(){//删除已上传的部分
                                                    var _partFileHash = this.partFileHash;
                                                    fileParts.remove(function(i,v){
                                                        if(v.partFileHash == _partFileHash){
                                                            return i;
                                                        }
                                                    });
                                                });
                                                loaded = (partLength - fileParts.length) * partSize;
                                                _events.progress(0);//设置进度
                                                fileParts.reverse();//反转数组 使用 pop 方法获取数据
                                                startUpload();//开始上传
                                            }
                                        }
                                    });
                                });
                            }
                        }
                    });
                }
            });
            var startUpload = function () {
                var part = fileParts.pop();
                _xhr = zhis.ajaxUplaodFile(part.file, getPostData(part), _events);
                if (_abort)_xhr.abort();
            };
            return {
                abort: function () {
                    fileParts.clear();
                    _abort = true;
                    if (!!_xhr) {
                        _xhr.abort();
                    }
                }
            };
        }
        /*
         //TODO IE 上传文件方法
         var clone = $file.clone().insertAfter($file);
         var temp = $('<form action="' + settings.url + '" method="post" enctype="multipart/form-data"></form>').appendTo($('body')).append($file.remove()).ajaxForm(function (data) {
         data = Fantasy.decode(data);
         delete data.id;
         delete data.description;
         delete data.folder;
         delete data.createTime;
         delete data.modifyTime;
         delete data.creator;
         delete data.modifier;
         uploadView.setTemplate(row.getIndex(), 'default', data);
         row.target.find('.plupload_file_status').html("100%");
         row.setData(data);
         row.refresh();
         zhis.addFile(clone);
         temp.remove();
         }).submit();
         */
    },

    getSimpleData: function () {
        var files = [];
        this.fileView.each(function () {
            var data = this.getData();
            data.sort = this.getIndex();
            files.push(data.fileManagerId + ":" + data.absolutePath);
        });
        return files.toString();
    },

    setJSON: function (json) {
        if (!!json) {
            this.fileView.setJSON(json);
        }
        return this;
    },

    getData: function () {
        return this.fileView.getData(function (data) {
            return !data.hasOwnProperty('unfinished');
        });
    },

    disenable: function () {
        var $finput = this.$html.find('[type=file]').hide();
        if ($finput.hasClass('file-add-input')) {
            var $img = this.$html.find('img');
            Holder.run({images: $img.attr('data-src', $img.data('src').replace(/\/upload$/g, '')).get(0)});
        }
    },

    uploadStart: function(row){
        this.fireEvent('uploadStart', row , row.getData());
    },

    uploadProgress: function (row, percentage, total, loaded) {
        this.fireEvent('uploadProgress', row, [percentage, total, loaded]);
    },

    uploadComplete: function (row, data) {
        row.update(data);
        this.fireEvent('uploadComplete', row, [data]);
    },

    uploadAbort: function () {
        this.fireEvent('uploadAbort');
    }

});
Fantasy.apply(Fantasy.awt.Upload, {}, (function () {

    var themes = {};

    return {

        /**
         * 添加主题
         */
        addTheme: function (key, theme) {
            themes[key] = Fantasy.copy({
                defaultSettings: function (options) {
                    return options;
                },
                appendTo: function (target, options) {
                    $($.Sweet(this.html).applyData(options)).appendTo(target).initialize();
                    return target;
                },
                afterInit: function (upload) {
                },
                callback: {
                    onAdd: function (view, row, data) {
                    },
                    beforeUpload: function () {
                    },
                    upload: function () {
                    }
                }
            }, theme);
        },

        getTheme: function (key) {
            return themes[key];
        }

    }

})());