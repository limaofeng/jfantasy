jQuery.extend({

	thumbnail : function($img, options, width, height) {
		var ratio = Math.max(0, options.ratio) || Math.min(1, Math.max(0, options.maxWidth) / width || 1, Math.max(0, options.maxHeight) / height || 1);
		//设置预览尺寸
		$img.width(Math.round(width * ratio) + "px");
		$img.height(Math.round(height * ratio) + "px");
	},
	preview : function(file, $img, options) {
		if($.browser.msie) {//如果为IE
			var $file = file, $preload = $('#preload_div');
			var preload = $preload[0];
			if(!preload) {
				$preload = $('<div id="preload_div"></div>');
				preload = $preload.attr('style',"position: absolute; filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='image'); WIDTH: 1px; HEIGHT: 1px; VISIBILITY: hidden; TOP: -9999px; LEFT: -9999px;")[0];
				//隐藏并设置滤镜
				$('body').prepend($preload);
				//插入body
			}
			if($.browser.msie && $.browser.version < 8) {
				//this.img.attr('src',this.file.val());
			} else {
				$img.attr('src', $.browser.msie && ($.browser.version == 6 || $.browser.version == 7) ? "mhtml:" + document.scripts[document.scripts.length - 1].getAttribute("src", 4) + "!blankImage" : "data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==");
			}
			$file.select();
			var data = (function() {
				try {
					return document.selection.createRange().text.replace(/[)'"%]/g, function(s) {
						return escape(escape(s));
					});
				} finally {
					document.selection.empty();
				}
			})();
			try {
				preload.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = data;
			} catch(e) {
				this._error("filter error");
				return;
			}
			$img[0].style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='scale',src=\"" + data + "\")";
			$.thumbnail($img, options, preload.offsetWidth, preload.offsetHeight);
		} else {
			var _tempImage = new Image();
			_tempImage.onload = function() {
				$.thumbnail($img, options, _tempImage.width, _tempImage.height);
				//console.log(_tempImage.width + '-' + _tempImage.height);
			};
			var reader = new FileReader();
			reader.onload = function(e) {
				$img.attr('src', e.target.result);
				_tempImage.src = e.target.result;
			};
			reader.readAsDataURL(file);
			//_tempImage.src = (window.webkitURL?window.webkitURL:window.URL).createObjectURL(file);
			//(window.webkitURL?window.webkitURL:window.URL).revokeObjectURL(file);
		}
	}
});
jQuery.fn.extend({
	upload : (function(){
		$(document).on({
			dragleave : function(e) {//拖离
				e.preventDefault();
			},
			drop : function(e) {//拖后放
				e.preventDefault();
			},
			dragenter : function(e) {//拖进
				e.preventDefault();
			},
			dragover : function(e) {//拖来拖去
				e.preventDefault();
			}
		});
		var ajaxUplaodFile = function(url, fileSize, fileName, fileType, fileData,events,callback) {
			var xhr = new XMLHttpRequest();
			xhr.open('post', url, true);
//			* load事件：传输成功完成。
//			* abort事件：传输被用户取消。
//			* error事件：传输中出现错误。
//			* loadstart事件：传输开始。
//			* loadEnd事件：传输结束，但是不知道成功还是失败。
//			* progress : 上传进度
			for(var e in events){
				xhr.upload.addEventListener(e, events[e], false);
			}
			xhr.onreadystatechange = function(){
				if (xhr.readyState == 4 && xhr.status == 200) {
					var data = Fantasy.decode(xhr.responseText);
					delete data.id;
					delete data.description;
					delete data.folder;
					delete data.createTime;
					delete data.modifyTime;
					delete data.creator;
					delete data.modifier;
					callback(data);
			    }
			};
			xhr.send(fileData);
		};
		var uploadFile = function(row,view,settings){
			var data = row.data;
			var load = function(data){
				row.target.find('.plupload_file_status').html("100%");
			};
			ajaxUplaodFile(settings.url, data.size, data.fileName, data.type, data.fd,{'progress':function(e) {
				if(e.lengthComputable) {
					var percentage = Math.round((e.loaded * 100) / e.total);
					row.target.find('.plupload_file_status').html(percentage+"%");
				}
			},'load':load},function(data){
				row.setData(data);
				row.refresh();
			});
		};
		return function(settings,json){
			settings = Fantasy.copy({
				url: request.getContextPath()+'/file/upload.do',
				view : {
					add : function(){
					}
				}
			},settings);
			var uploadView = $(this).view().on('add',settings.view.add);//.on('remove',settings.view.remove)
			$(this).find('.upload_remove').click(function(){
				var rmids = [];
				uploadView.each(function(){
					if(this.target.find('.index').is(":checked")){
						rmids.push(this.getIndex());
					}
				});
				rmids.reverse().each(function(){
					uploadView.remove(this);
				});
			});
			var box = $(this).find('#dropbox');
			var zhis = $(this);
			zhis.find('.plupload_filelist').height($(this).height()-80);
			var $uploadAdd = $(this).find('.upload_add');
			var _interval = setInterval(function(){
				if($uploadAdd.index() == -1){
					clearInterval(_interval);
					return;
				}
				if($uploadAdd.is(':visible')){
					$uploadAdd.next().css({width:$uploadAdd.outerWidth()});
					clearInterval(_interval);
				};
			}, 500);
			$uploadAdd.click(function(e){
				//fileUploadInput.click();
				//stopDefault(e);
			}).after('<input type="file" name="attach" class="fileUploadInput" multiple="multiple" accept="image/jpeg,image/gif,image/png,application/zip" style="display: block;height:'+$uploadAdd.outerHeight()+'px;left: 0px;opacity: 0;filter:alpha(opacity=0);position: relative;margin-top: -'+($uploadAdd.outerHeight()+4)+'px;width: '+$uploadAdd.outerWidth()+'px;z-index: 26;">');
			$(this).find('.plupload_start').click(function(){
				uploadFile(uploadView.get(0),uploadView);
			});
			var fileUploadInput = $(this).find('.fileUploadInput');
			var eventChangeFun = function(e) {
				if($.browser.msie) {
					var file = this;
					var tfs = file.value.split('\\');
					var fileName = tfs[tfs.length-1];
					var fileEl = uploadView.find('fileName',fileName);
					if(!!fileEl)
						return;
					var row = uploadView.add({fileName:fileName,size:'nan',type:file.type});
						if($("#preview").length != 0){//TODO 待调整
						$.preview(this, $("<img />").insertBefore($("#preview")), {
							ratio : null,
							maxWidth : 200,
							maxHeight : 200
						});
					}
					var clone = fileUploadInput.clone().insertAfter(fileUploadInput);
					var temp = $('<form action="'+settings.url+'" method="post" enctype="multipart/form-data"></form>').appendTo($('body')).append(fileUploadInput.remove()).ajaxForm(function(data){
						data = Fantasy.decode(data);
						delete data.id;
						delete data.description;
						delete data.folder;
						delete data.createTime;
						delete data.modifyTime;
						delete data.creator;
						delete data.modifier;
						uploadView.setTemplate(row.getIndex(),'default',data);
						row.target.find('.plupload_file_status').html("100%");
						row.setData(data);
						row.refresh();
						bindEvent.apply((fileUploadInput=clone).get(0));
						temp.remove();
					}).submit();
				} else {
					for(var i = 0, len = this.files.length; i < len; i++) {
						var file = this.files[i];
						var fileEl = uploadView.find('fileName',file.name);
						if(fileEl && fileEl.data.size == file.size)
							continue;
						var fd = new FormData();
						fd.append('attach', file);
						fd.append('dir', 'goodsImages');
						var row = uploadView.add({fileName:file.name,size:file.size,type:file.type,state:'',fd:fd});
						uploadFile(row,uploadView,settings);
						if($("#preview").length != 0){//TODO 待调整
							$.preview(file, $("<img />").insertBefore($("#preview")), {
								ratio : null,
								maxWidth : 200,
								maxHeight : 200
							});
						}
					}
				}
			};
			var bindEvent = function(){
				if ($(this).closest('form').length > 0) {
					$(this).closest('form').submit((function(fileInpt) {
						return function(e) {
							if(e.result == false){
								return;
							}
							$(this).find('input[type=file]').remove();
						};
					})(this));
				}
				$(this).bind('change',eventChangeFun);
			};
			bindEvent.apply(fileUploadInput.get(0));
			$(this).on({
				dragenter : function(e) {
					box.css('borderColor', 'gray');
				},
				dragleave : function(e) {
					box.css('backgroundColor', 'transparent');
				},
				dragenter : function(e) {
					box.css('backgroundColor', 'white');
					e.stopPropagation();
					e.preventDefault();
				},
				dragover : function(e) {
					e.stopPropagation();
					e.preventDefault();
				},
				drop : function(e) {
					e.stopPropagation();
					e.preventDefault();
		
					var files = e.originalEvent.dataTransfer.files;
					for(var i = 0; i < files.length; i++) {
						var file = files[i];
						var fileEl = uploadView.find('fileName',file.name);
						if(fileEl && fileEl.data.size == file.size)
							continue;
						var fd = new FormData();
						fd.append('attach', file);
						//上传的目录key
						fd.append('dir', 'default');
						var row = uploadView.add({fileName:file.name,size:file.size,type:file.type,state:'',fd:fd});
						uploadFile(row,uploadView,settings);
						if(file.type.match(/image*/g)) {
							if($("#preview").length > 0){
								$.preview(file, $("<img />").insertBefore($("#preview")), {
									ratio : null,
									maxWidth : 200,
									maxHeight : 200
								});
							}
						}
					}
				}
			});
			if(json){
				uploadView.setJSON(json);
			}
			return {view:uploadView};
		};
	})()
	
});