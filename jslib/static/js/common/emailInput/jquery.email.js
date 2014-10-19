jQuery.fn.extend({
	
	emailInput : (function(){
		var template = '<div class="email-view" style="width: 90%;margin: 5px 0;">'+
		'<div class="sc-view ck-token-view sc-static-layout">'+
				'<textarea class="hidden-text"></textarea>'+
				'<span class="sc-view ck-token-item focus template" name="default"><textarea class="view-field" name="name" autocorrect="off" spellcheck="false" wrap="off" rows="1"></textarea></span>'+
				'<span class="sc-view ck-token-item template" name="display"><span class="left"></span><span class="middle"></span><span class="right"></span><span style="max-width: 864px;" class="token-span"><label class="token-value">{name:htmlEncode()}</label></span></span>'+
			'</div>'+
			'<div style="right: 3px; width: 25px; top: 0px; height: 28px;" class="contact-picker sc-view" title="通讯录" >'+
				'<label><img class="icon contacts" src="data:image/gif;base64,R0lGODlhAQABAJAAAP///wAAACH5BAUQAAAALAAAAAABAAEAAAICBAEAOw=="></label>'+
			'</div>'+
		'</div>';
		//$.get(request.getContextPath()+'/static/js/common/emailInput/emailInput.html',function(html){
		//	template = html;
		//});
		var lazy = function(callback){
			setTimeout(function(){
				if(template == null){
					lazy(callback);
				}else{
					callback();
				}
			},100);
		};
		var emailInput = function(){
			//如果模板未下载，延时执行方法
			var zhis = this;
			var args = arguments;
			if(template == null){
				lazy(function(){
					emailInput.apply(zhis, args);
				});
				return;
			}
			//添加辅助div
			if($('#help_text_size').length == 0){
				$("body").append("<div id=\"help_text_size\" style=\"position:absolute;left:-1000px\"></div>");//计算输入文字宽度的辅助类
			}
			var eview = $(template);
			var $email = $(this).before(eview);
			var eimput = eview.find('.ck-token-view');
			eview.css('width',$(this).css('width'));
			eimput.css('min-height',$(this).css('height'));
			var picker = eview.find('.contact-picker').click(function(){
				var dialogSettings = {'title':'通讯录',init: function () {
			    	var win = this.iframe.contentWindow;
			    	win.book.linkmans.each(function(){
			    		if(!!this.email && !!emailView.find('email',this.email)){
			    			this.checked = true;
			    		}
			    	});
			    	win.contactlist.on('add',function(){
			    		var citem = this;
			    		this.target.find('.checkbox').click(function(e){
			    			var linkman = citem.getData();
			    			win.changeEmail(linkman,$(this).hasClass('checked'));
			    		});
			    	});
			    	win.changeEmail = function(linkman,move){
			    		var re = new RegExp("(\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+)","i");
		    			if(move){
		    				if(!(!re.exec(linkman.email)?emailView.find('name',linkman.name + '<' + linkman.email +'>'):emailView.find('email',linkman.email))){
		    					emailView.add({name:linkman.name + '<' + linkman.email +'>'},'display');
		    				}
		    			}else{
		    				var eitem = re.exec(linkman.email)?emailView.find('email',linkman.email):emailView.find('name',linkman.name + '<' + linkman.email + '>');
		    				if(eitem){
		    					emailView.remove(eitem.getIndex());
		    					if(eitem.getIndex() < emailView.size()){
		    						resize(emailView.get(eitem.getIndex()));
		    					}
		    				}
		    			}
			    	};
			    	win.groupslist.setJSON(win.book.groups);
			    	win.groupslist.insert(0,{name:'所有联系人',state:'forever'}).target.click();
			    	win.initScrollBar();
			    },cancelVal:'关闭通讯录',cancel:function(){
			    	
			    }/*,okVal:'添加选中联系人',ok:function(win){
					if(win.contactlist.size() == 0)
						return;
					var index = emailView.size();
					running = true;
					win.contactlist.each(function(){
						var email = '';
						var data = this.getData();
						if(this.getTemplateName() == 'default'){
							email = data.name + '<' + data.email +'>';
						}else{
							email = data.name;
						}
						emailView.insert(index, {name:email},'display');
					});
					running = false;
					resize(emailView.get(index));
				}*/};
				if($.browser.msie){
					dialogSettings.width = 650;
					dialogSettings.height = 475;
				}
				$.dialog.open(request.getContextPath()+'/admin/contacts/mybook.do',dialogSettings);
			}).switchStyle('mousedown','down');
			eview.hover(function(e){
				picker.show();
				return stopDefault(e);
			},function(e){
				picker.hide();
				return stopDefault(e);
			});
			picker.hide();
			//hideTimeout = setTimeout(function(){picker.hide();},50);
			var moveLeft = function(item){//在元素上按左方向键时的动作
				var tn = item.getTemplateName();
				if(tn == 'default'){
					if(item.getData().name != ''){
						display(item);
						emailView.insert(item.getIndex(),{}).target.find('textarea').focus();
					}else{
						if(item.getIndex() == 0){
							return;
						}
						var pitem = emailView.get(item.getIndex()-1);
						pitem.target.addClass("focus");
						emailView.remove(item.getIndex());
						lazyFocus($('.hidden-text',eimput).data('index',pitem.getIndex()).focus());
					}
				}else{
					lazyFocus(emailView.insert(item.getIndex(),{}).target.find('textarea').focus());
				}
			};
			var moveRight = function(item){////在元素上按右方向键时的动作
				var tn = item.getTemplateName();
				if(tn == 'default'){
					if(item.getData().name != ''){
						display(item);
						lazyFocus(emailView.insert(item.getIndex()+1,{}).target.find('textarea').focus());
					}else{
						if(item.getIndex() == emailView.size() -1 )
							return;
						var nitem = emailView.get(item.getIndex()+1);
						nitem.target.addClass("focus");
						emailView.remove(item.getIndex());
						lazyFocus($('.hidden-text',eimput).data('index',nitem.getIndex()).focus());
					}
				}else{
					lazyFocus(emailView.insert(item.getIndex()+1,{}).target.find('textarea').focus());
				}
			};
			var getLeft = function(item){//获取元素的left坐标
				var left = 0,row=1,dnum = 0;
				emailView.each(function(index){
					if(this.getIndex() <=  item.getIndex()){
						if((left + getWidth(this)) > eimput.width() - 10){
							left = 0;
							row++;
							dnum = 0;
						}
						var data = this.getData();
						data.row = row;
						this.setData(data);
						var _w = this.getIndex() ==  item.getIndex() ? 0 : this.target.width();
						if(this.getTemplateName() == 'default' && this.getData().name!=''){
							left += _w;//输入模式如果没有内容left取0
							dnum ++ ;
						}else if(this.getTemplateName() == 'display'){
							left += (_w + (dnum == 0 ? 5 : 9));//如果为行的第一段内容间隔5以后的间隔9
							dnum ++ ;
						}
						if(this.getIndex() == item.getIndex() && item.getTemplateName() == 'default'){
							left += dnum == 0 ? 0 : 3;
						}
					}
				});
				return left;
			};
			var lazyFocus = function(lazy){//在IE上延时执行focus
				if ($.browser.msie) {
					setTimeout(function(){
						lazy.focus();
					},5);
				}
			};
			var getWidth = function(item){//获取元素的宽度
				var data = item.getData(),tn = item.getTemplateName();
				$('#help_text_size').html(Fantasy.htmlEncode(data.name));
				return $('#help_text_size').width() + (tn=='default'?2:20);
			};
			var getTop = function(item){//获取元素的top坐标
				if(item.getTemplateName() == 'display' || item.getData().name.length > 0){
					eimput.css('height',(item.getData().row * 33));
				}
				return 5 + ((item.getData().row - 1) * 33) /*+ (item.getTemplateName() == 'default' && $.browser.msie ? 3 : 0)*/ ;
			};
			var getCss = function(item){//获取元素的位置及宽度信息
				var retval = {width:getWidth(item),left:getLeft(item),top:getTop(item)};
				//console.log(Fantasy.encode(retval));
				return retval;
			};
			//重置位置
			var running = false;
			var resize = function(item){
				if(running)return;
				//console.log('重置位置:'+item.getIndex()+'\t'+item.getData().name);
				var _resize = function(item){
					item.target.css(getCss(item));
				};
				if(item.getTemplateName() == 'default' && item.getData().name == ''){
					_resize(item);
				}else{
					emailView.each(function(){
						if(this.getIndex() >= item.getIndex()){
							_resize(this);
						}
					});
				}
			};
			//切换显示-将输入模板转为显示模板
			var display = function(item){
				return emailView.setTemplate(item.getIndex(),'display',true);
			};
			var synchronousText = function(){
				var emails = '';
				emailView.each(function(){
					if(this.getTemplateName() == 'display'){
						emails += (this.getData().name + (this.getData().email ? '<' + this.getData().email + '>' : '') + ';');
					}
				});
				$email.val(emails).change();
			};
			//隐藏的输入框，用于显示模板的选中时触发左、右按键事件触发
			$('.hidden-text',eimput).bind('blur',function(e){
				$(this).next('.focus').removeClass('focus');
			}).bind('keydown',function(e){
				var index = $(this).data("index");
				if(e.keyCode == 37){//左
					moveLeft(emailView.get(index));
				}else if(e.keyCode == 39){//右
					moveRight(emailView.get(index));
				}else if(e.keyCode == 8){//撤消
					emailView.remove(index);
					lazyFocus(emailView.insert(index, {}).target.find('textarea').focus());
					if(index > 0){
						resize(emailView.get(index - 1));
					}else if(index == 0 && emailView.size()>1){
						resize(emailView.get(index+1));
					}
				}else if(e.keyCode == 13){//回车
					emailView.get(index).target.mousedown();
				}
				return false;
			});/*.bind('focus',function(){
				//console.log('focus ...');
			});*/
			//emailInput上的点击时，可以在当前点击的位置插入email信息
			var emailView = eimput.mousedown(function(e){
				if(e.target == $(this).get(0)){
					var offset = $(this).offset();
					var x = e.pageX-offset.left;
					var y = e.pageY-offset.top;
					//console.log('鼠标点击位置:'+x+'-'+y);
					var index = emailView.each(function(){
						var position = this.target.position();
//						console.log(Math.abs(position.left - x)+'-'+Math.abs(position.top - y));
						if(Math.abs(position.left - x) < 15 && Math.abs(position.top - y) < 15 ){
							return this.getIndex();
						}
					});
					setTimeout(function(){
						lazyFocus(emailView.insert(index != null ? index : emailView.size(), {}).target.find('textarea').focus());
					},5);
				}
			}).view().on('remove',function(){//移除email时的操作
				if(this.getData().name==''){
					running = false;
				}
				if(this.getTemplateName() == 'display'){
					synchronousText();
				}
			}).on('add',function(data){//添加时的操作
				//1.移除其他元素的选中效果
				emailView.each(function(){
					this.target.removeClass("focus");
				});
				var zhis = this,target = zhis.target;
				switch(this.target.attr('name')){
					case 'default'://输入框模式
						zhis.interval = setInterval(function(){
							var data = zhis.getData();
							if(!data.oldname||data.name!=data.oldname){
								data.oldname = data.name;
								zhis.setData(data);
								resize(zhis);
							};
						},200);
						var text = this.target.find('textarea');
						text.bind('blur',function(e){//失去焦事件
							if(text.val()==''){//如果没有输入任何值，移除该元素.不触发resize操作
								running = true;
								emailView.remove(zhis.getIndex());
								return;
							}
							display(zhis);
							return;
						}).bind('keydown',function(e){//按键事件
							if (e.keyCode == 37 && ($(this).val() == '' || ($(e.target).selectionStart() == 0 && $(e.target).selectionEnd() == 0))){//左移   如果没有输入值或者光标已处于开始位置
								text.unbind('blur');
								moveLeft(zhis);
							}else if (e.keyCode == 39 && ($(this).val() == '' || ($(e.target).selectionStart() == $(e.target).val().length && $(e.target).selectionEnd() == $(e.target).val().length))){//右移   如果没有输入值或者光标已处于末尾位置
								text.unbind('blur');
								moveRight(zhis);
							}else if(e.keyCode == 13){//回车
								if(text.val()==''){
									running = true;
									return false;
								}
								running = true;
								var _item = display(zhis);
								lazyFocus(emailView.insert(zhis.getIndex() + 1, {}).target.find('textarea').focus());
								running = false;
								resize(_item);
								return false;
							}else if(e.keyCode == 8 && $(this).val()=='' && zhis.getIndex()>0){//撤消 移除当前元素，并将焦点设置到上一个email显示元素
								var pindex = zhis.getIndex()-1;
								var pitem = emailView.get(pindex);
								pitem.target.addClass('focus');
								zhis.target.find('textarea').blur();//触发获得焦点事件，删除元素
								lazyFocus($('.hidden-text',eimput).data('index',pindex).focus());
								return false;
							}else if(e.ctrlKey && e.keyCode == 86){// ctr 加  v 执行操作
								setTimeout(function(){
									var txt = text.val();
									var names = txt.split(/,|;|\n/g);
									running = true;
									names.each(function(index){
										if($.trim(this.toString()).length==0){
											return;
										}
										if(index>0){
											emailView.insert(zhis.getIndex() + 1, {name:$.trim(this)},'display');
										}else{
											text.val(this);
											text.blur();
										}
									});
									running = false;
									resize(zhis);
									lazyFocus(emailView.insert(zhis.getIndex() + names.length, {}).target.find('textarea').focus());
								},1);
							}else{
								//普通输入，延时重新计算位置
								setTimeout(function(){
									resize(zhis);
								},1);
							}
						});
						break;
					case 'display'://如果为显示模式
						var data = this.getData();
						//分割显示名称与email
						var re = new RegExp("\\<(\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+)\\>","i");
						if(!re.exec(data.name)){
							this.target.addClass("token-invalid");
						}else{
							data.email = data.name.match(re)[1];
							data.name = data.name.replace(re);
							this.setData(data);
							this.target.find('.token-value').html(Fantasy.htmlEncode(data.name));
						}
						//绑定点击事件,切换到编辑模式
						this.target.bind('mousedown',function(){
							if(data.email){
								data.name = data.name + '<' + data.email +'>';
							}
							var item = emailView.setTemplate(zhis.getIndex(),'default',data);
							//设置光标全选
							item.target.find('textarea').selectionRange();
				            return false;
						});
						synchronousText();
						break;
				}
				resize(this);
			});
			var names = $(this).val();
			running = true;
			names.split(/,|;|\n/g).each(function(index){
				if($.trim(this.toString()).length==0){
					return;
				}
				emailView.insert(index, {name:$.trim(this)},'display');
			});
			running = false;
			if(names.length > 0){
				resize(emailView.get(0));
			}
			$(this).hide();
			return 
		};
		
		return function(){
			var zhis = this;
			var args = arguments;
			emailInput.apply(zhis, args);
			return $(this);
		};
		
	})()
});