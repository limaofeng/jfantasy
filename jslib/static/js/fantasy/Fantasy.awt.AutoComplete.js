//document.write('<link rel="stylesheet" href="'+request.getContextPath()+'/static/js/fantasy/style/autocomplete.skin/default/autocmplete.css" id="autocmplete-skin"/>');
Fantasy.util.jClass(Fantasy.util.Observable, (function() {

	var themes = new Fantasy.util.Map();
	
	function loadTheme(themeName,callback){
		var csslink = request.getContextPath()+'/static/js/fantasy/style/autocomplete.skin/'+themeName+'/autocmplete.css';
		var htmlsrc = request.getContextPath()+'/static/js/fantasy/style/autocomplete.skin/'+themeName+'/autocomplete.html';
		var theme = themes.get(themeName);
		if(!theme){
			theme = {
					getPanel : function(){
						return this.panel?$(this.panel):null;
					}
				};
			callback = (function(call){
				return function(){
					var iframe = $('<iframe src="'+htmlsrc+'" style="display:none;"></iframe>');
					$('body').prepend(iframe);
					iframe.bind('load',function(){
						theme.panel = iframe.contents().find("body").html();
						iframe.remove();
						call.apply(window,[theme.getPanel()]);
					});
					themes.put(themeName,theme);
				};
			})(callback);
		}
		var href = $('#autocmplete-skin').attr('href');
		if(String.prototype.substring.apply(href,href.indexOf('?')==-1?[0]:[0,href.indexOf('?')])!=csslink)
			$('#autocmplete-skin').attr('href',csslink+'?'+new Date().getTime());
		callback.apply(window,[theme.getPanel()]);
		return theme;
	}
	
	return {

		jClass : 'Fantasy.awt.AutoComplete',

		initialize : function($super, target, options) {
			$super(new Array( [ 'change' ]));
			this.options = Fantasy.merge(options || {}, {
				theme : 'default',
				positionAdjust : [ function(w,t){
					return t.height() + 9;
				}, 0 ],
				event : 'keyup',
				param : {},
				items : [],
				highlight: function(value, term) {
					return value.replace(new RegExp("(?![^&;]+;)(?!<[^<>]*)(" + term.replace(/([\^\$\(\)\[\]\{\}\*\.\+\?\|\\])/gi, "\\$1") + ")(?![^<>]*>)(?![^&;]+;)", "gi"), "<strong>$1</strong>");
				},
				click:function(data,t){
					t.val(data.text);
				},
				mapping:{}
			});
			var cache = new Fantasy.util.Cache(this.getClass().toString());
			loadTheme(options.theme,(function(zhis){
				return function(panel){
					zhis.box = new Fantasy.awt.Box(Fantasy.merge(options,{
						widget:panel,
						trigger:target
					})).on('show',function(w,t){
						if(t.val().length==0){
							zhis.box.hide();
							return;
						}
						if(!zhis.view){
							zhis.view = w.find('.view').view().on('dataFilter',function(data){
								for(var f in zhis.options.mapping){
									data[f] = data[zhis.options.mapping[f]];
								}
							}).on('add',function(data){
								if(options.highlight){
									this.target.html(options.highlight(this.target.html(),t.val()));
								}
								this.target.bind('click',function(){
									options.click.apply(zhis.view,[data,t]);
									zhis.box.hide();
								});
							});
						};
						if(options.url){
							var data = zhis.getParam(t);
							var key = options.url+'-'+Fantasy.encode(data);
							var cobject = cache.getObject(key);
							if(cobject){
								zhis.view.setJSON(cobject);
							}else{
								$.getJSON(options.url,data,function(list){
									cache.putObject(key,list);
									zhis.view.setJSON(list);
								});
							}
						}else if(zhis.options.json){
							zhis.view.setJSON(Fantasy.search(zhis.options.json,function(){
								return new RegExp(t.val(),"gi").test(this.text);
							}));
						}
					});
				};
			})(this));
		},
		
		getParam : function(t){
			return jQuery.isFunction(this.options.param)?this.options.param.apply(this,[t.val()]):(function(zhis){
				zhis.options.param[t.attr('name')] = t.val();
				return zhis.options.param;
			})(this);
		}

	};

})());