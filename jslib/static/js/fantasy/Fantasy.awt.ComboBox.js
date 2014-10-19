jQuery.include('js/fantasy/skin/comboBox/default/style.css');
/**
 * ComboBox 
 * @param {Object} $super
 * @param {Object} source
 * @param {Object} settings
 */
Fantasy.util.jClass(Fantasy.util.Observable,{
	
	jClass : 'Fantasy.awt.ComboBox',
	
	initialize : function($super,trigger,settings){
		$super(['change']);
		var zthis = this;
		var property = new Fantasy.data.Property(this, ['trigger','positionAdjust','event','size']);
	    property.setPropertyValue(settings, {
	    	trigger:trigger,
	    	positionAdjust:[0,0],
	    	event : 'click',
	    	template:$(Fantasy.awt.ComboBox.template)
	    });
	    
	    this.settings = settings == null ? new Object() : settings;
	    this.dataMap = new Fantasy.util.Map();
		this.source = trigger;
		if(Fantasy.awt.ComboBox.effect){
			this.comboBox = $(Fantasy.awt.ComboBox.template);
			$('body').append(this.comboBox);
			//var temp = this.comboBox.find('select');
			this.offset = new Object();
			var source_position = this.source.position();	
			this.comboBox.css({top:source_position.top,left:source_position.left});			
			this.comboBoxInput = this.comboBox.find('.ComboBox-input');
			this.comboBoxDropdown = this.comboBox.find('.ComboBox-dropdown');
			this.comboBoxUl = this.comboBox.find('.ComboBox-ul');
			this.comboBoxDiv = this.comboBox.find('.ComboBox-div');
			this.comboBoxInput.css({height:source_position.height-2,width:source_position.width-2});			
			if(Fantasy.Browser.isMsie){
				this.comboBoxInput.css({height:zthis.offset.height-2,width:zthis.offset.width-3});
			}
			var position = zthis.comboBoxInput.position();
			this.comboBoxDropdown.css({left:source_position.width-15,top:source_position.height/2-9});
			this.comboBox.css({width:source_position.width + position.paddingLeft + position.paddingRight});
			this.comboBoxDropdown.click(function(){
				zthis.comboBoxInput.focus();
				if(zthis.comboBoxDiv.is(':hidden')){
					zthis.show(null,'');
				}else{
					zthis.comboBoxDiv.hide();
				}
			});
			var keys = [40,38];
			//用于避免回车时 提交表单
			this.comboBoxInput.keypress(function(e){if(e.keyCode == 13){return false;}});
			this.comboBoxInput.bind('keyup',function(e){
				if(keys.indexOf(e.keyCode)>-1){
					if(zthis.comboBoxDiv.is(':hidden')){
						zthis.comboBoxDiv.show();
					}
					if(e.keyCode == 40){
						var lihover = zthis.comboBoxUl.find('.ComboBox-ul-li-hover');
						var next = lihover.next('.ComboBox-li');					
						var first = zthis.comboBoxUl.find('.ComboBox-li').first();
						
						lihover.removeClass('ComboBox-ul-li-hover');
						if(lihover.html() == null || next.html() == null){
							first.addClass('ComboBox-ul-li-hover');
						}else{
							next.addClass('ComboBox-ul-li-hover');
						}
					}else if(e.keyCode == 38){
						var lihover = zthis.comboBoxUl.find('.ComboBox-ul-li-hover');
						var prev = lihover.prev();
						var last = zthis.comboBoxUl.find('.ComboBox-li').last();
						
						lihover.removeClass('ComboBox-ul-li-hover');
						if(lihover.html() == null || prev.html() == null){
							last.addClass('ComboBox-ul-li-hover');
						}else{
							prev.addClass('ComboBox-ul-li-hover');
						}
					}
					return false;
				}
				if(e.keyCode == 13){
					var lihover = zthis.comboBoxUl.find('.ComboBox-ul-li-hover');
					if(lihover.html() != null){
						lihover.click();
						zthis.seach(zthis.comboBoxInput.val());
						return false;
					}
				}
				zthis.show(1,zthis.comboBoxInput.val());
			});
			$('body').click(function(){
				var isClick = false;
				if(zthis.comboBoxDiv[0] == jQuery.getEvent().parents('.ComboBox-div')[0]){
					isClick = true;
				}
				if(!isClick)
				zthis.comboBox.children().each(function(){	
					var event = jQuery.getEvent();
					if(event && this == event[0]){
						isClick = true;
						return;
					}
				});
				if(!isClick){
					zthis.comboBoxDiv.hide();
				}
			});
		}
		this.source.change(function(){
			zthis.fireEvent('change',zthis,[zthis.dataMap.get($(this).val())[0].data]);
		});
	},
	
	seach : function(val){
		if(this.dataMap.isEmpty()){
			this.loadData();
		}
		var seachVal = Fantasy.seach(this.dataMap.values(),function(){
			return $(this).html().indexOf(val) > -1;
		});		
		return seachVal;
	},
	
	setJSON : function(data,mapping,callback){
		if(!data)return;		
		var zthis = this;
		if(mapping.root){
			data = eval('data.'+mapping.root);
		}
		zthis.source.children().remove();
		if(Fantasy.isArray(data)){
			data.each(function(){
				var $option = $('<option></option>');
				$option[0].data = this;
				$option.html(this[mapping.text]);
				$option.attr('value',this[mapping.value]);
				zthis.source.append($option);
			});
		}else{
			var $option = $('<option></option>');
			$option[0].data = data;
			$option.html(data[mapping.text]);
			$option.attr('value',data[mapping.value]);
			zthis.source.append($option);
		}		
		if(Fantasy.awt.ComboBox.effect){
			this.comboBoxInput.attr('val',this.source.val());
			this.comboBoxInput.val(this.source.text());
			zthis.loadData();
		}else{
			this.dataMap.clear();
			zthis.source.find('option').each(function(){
				zthis.dataMap.put($(this).attr('value'),$(this));
			});
			if(callback)callback.call(this);
		}
		this.change();
	},
	
	loadData : function(){
		var zthis = this;
		this.dataMap = new Fantasy.util.Map();
		zthis.comboBoxUl.children().remove();
		this.source.find('option').each(function(index){
			var li = zthis.add(this);
			zthis.dataMap.put(li.attr('val'),li);
		});
	},
	
	refresh : function(){
	},
	
	add : function(option){
		var zthis = this;
		var li = $('<li>'+$(option).html()+'</li>');
		li.attr('val',$(option).attr('value'));
		li[0].data = option.data;
		li.click(function(){
			zthis.comboBoxInput.val($(this).html());
			zthis.comboBoxInput[0].li = this;
			zthis.comboBoxInput.attr('val',$(this).attr('val'));
			zthis.source.val($(this).attr('val'));			
			zthis.change();
			zthis.comboBoxDiv.hide();
			zthis.comboBoxUl.find('.ComboBox-ul-li-hover').removeClass('ComboBox-ul-li-hover');
		});
		li.hover(function() {
			$(this).addClass('ComboBox-ul-li-hover');
		}, function() {
			$(this).removeClass('ComboBox-ul-li-hover');
		});
		li.addClass('ComboBox-li');
		return li;
	},
	
	setPager : function(paging,seach){
		paging.curpage = paging.curpage == null ? 1 : paging.curpage;
		paging.totalpage = parseInt(paging.total/paging.size) + (paging.total%paging.size == 0 ? 0 : 1);
		var zthis = this;
		this.comboBoxUl.find('.ComboBox-pager').remove();
		if(this.settings && this.settings.size){
			this.comboBoxPager = $('<li class="ComboBox-pager">'+
			'<div class="">'+
			'<a href="javascript:void(0);" class="first"></a>'+
			'<a href="javascript:void(0);" class="prev"></a>'+
			'<span class="split"></span>'+
			'<a href="javascript:void(0);" class="refresh"></a>'+
			'<span class="split"></span>'+
			'<a href="javascript:void(0);" class="next"></a>'+
			'<a href="javascript:void(0);" class="last"></a>'+			
			'</div>'+
			'</li>');
			this.comboBoxPager.find('a').click(function(){return false;});
			this.comboBoxPager.first = this.comboBoxPager.find('.first');
			this.comboBoxPager.prev = this.comboBoxPager.find('.prev');
			this.comboBoxPager.refresh = this.comboBoxPager.find('.refresh');
			this.comboBoxPager.next = this.comboBoxPager.find('.next');
			this.comboBoxPager.last = this.comboBoxPager.find('.last');
			
			var getTitie = function(number){
				return '第'+number+'页';
			};
			
			this.comboBoxPager.first.attr('title',getTitie(1));
			this.comboBoxPager.prev.attr('title',getTitie(paging.curpage==1?1:(paging.curpage-1)));
			this.comboBoxPager.refresh.attr('title',getTitie(paging.curpage));
			this.comboBoxPager.next.attr('title',getTitie(paging.curpage==paging.totalpage?paging.totalpage:(paging.curpage+1)));
			this.comboBoxPager.last.attr('title',getTitie(paging.totalpage));
			
			
			
			if(paging.curpage == 1){
				this.comboBoxPager.first.addClass('disabled');
				this.comboBoxPager.prev.addClass('disabled');
			}else{
				this.comboBoxPager.first.click(function(){
					zthis.show(1,seach);
				});
				this.comboBoxPager.prev.click(function(){
					zthis.show(paging.curpage-1,seach);
				});
			}
			if(paging.totalpage == paging.curpage){
				this.comboBoxPager.next.addClass('disabled');
				this.comboBoxPager.last.addClass('disabled');
			}else{
				this.comboBoxPager.next.click(function(){
					zthis.show(paging.curpage+1,seach);
				});
				this.comboBoxPager.last.click(function(){
					zthis.show(paging.totalpage,seach);
				});
			}
			this.comboBoxPager.refresh.click(function(){
				$(this).addClass('wait');
				zthis.comboBoxDiv.mask('loading...');
				zthis.refresh();
				setTimeout(function(){
					zthis.comboBoxDiv.unmask();
					zthis.comboBoxPager.refresh.removeClass('wait');
				},1000);

			});
			this.comboBoxUl.append(this.comboBoxPager);
		}
	},
	
	setJSONUrl : function(url,mapping,callback){
		var zthis = this;
		if(zthis.comboBox)
		zthis.comboBox.mask('loading...');
		jQuery.ajax({url:url,type:'POST',success:function(data){		
			zthis.setJSON(data,mapping);
			if(callback)callback();
			if(zthis.comboBox)
			zthis.comboBox.unmask();
		},error:function(){
			if(zthis.comboBox)
			zthis.comboBox.unmask();
		}});
	},
	
	change : function(){
		this.source.change();
	},
	
	getShowData : function(curpage,seach){
		var zthis = this;
		var array = new Array();
		var data = zthis.seach(seach!=null?seach:zthis.comboBoxInput.val());
		data.each(function(index){
			var _size = (index - (zthis.settings.size * (curpage -1)));
			if(zthis.settings.size && _size >= zthis.settings.size || _size < 0){
				return;
			}
			array.push(this);
		});
		return {total:data.length,data:array};
	},
	
	show : function(curpage,seach){
		var zthis = this;
		var position = zthis.comboBoxInput.position();
		//设置显示位置
		this.comboBoxUl.css({width:this.comboBox.position().width - 2});
		//加载显示数据
		this.comboBoxUl.children('.ComboBox-li').remove();
		curpage = curpage == null ? 1 : curpage;
		var seachData = this.getShowData(curpage,seach);
		seachData.data.each(function(index){
			var li = this;
			var last = zthis.comboBoxUl.find('.ComboBox-li').last();
			if(last!=null&&last.html()!=null){
				last.after(li);
			}else{
				if(zthis.comboBoxUl.find('.ComboBox-pager').html()!=null)
					zthis.comboBoxUl.find('.ComboBox-pager').before(li);
				else
					zthis.comboBoxUl.append(li);
			}
			if(zthis.comboBoxInput.val() == li.html() && zthis.comboBoxInput.attr('val') == li.attr('val')){
				li.addClass('ComboBox-ul-li-hover');
			}else{
				li.removeClass('ComboBox-ul-li-hover');
			}
		});
		this.setPager({total:seachData.total,size:zthis.settings.size,curpage:curpage},seach);		
		this.comboBoxDiv.show();
		this.comboBoxDiv.css({height:this.comboBoxUl.height()+5});
	},
	
	setValue : function(val){
		this.source.val(val);
		this.comboBoxInput.attr('val',this.source.val());
		this.comboBoxInput.val(this.source.text());
	}

});

Fantasy.awt.ComboBox.effect = true;
jQuery.get(request.getContextPath() + '/js/fantasy/skin/comboBox/default/template.html',function(data){
	Fantasy.awt.ComboBox.template = data;
});