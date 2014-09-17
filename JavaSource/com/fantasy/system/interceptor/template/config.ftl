$(function() {
	var _types = ${types};
	var _configs = ${configs};
	if (top.window == window && Fantasy.config) {// 如果为顶级window同时Fantasy.config已经存在，即为更新操作
		window.types = _types, window.configs = _configs;
		return;
	}
	window.types = _types, window.configs = _configs;
	var config = top.window != window && top.window.Fantasy.config ? top.window.Fantasy.config : {// 将子窗口的Fantasy.config对象指向top.window.Fantasy.config
	
				lastModified : new Date('${DateUtil.now().toGMTString()}').format('yyyyMMddhhmmss'),
				
				types : function() {
					return types;
				},
				get : function(type, key) {
					return configs.each(function() {
						if (this.type == type && this.code == key.toString())
							return this;
					});
				},
				list : function(configKey) {
					var list = [];
					configs.each(function() {
                        if(configKey.indexOf(':') == -1 ? this.type == configKey : this.parentKey == configKey){
							list.push(Fantasy.clone(this));
						}
					});
					return list;
				},
				tree : function(configKey) {
					var list = [];
					configs.each(function() {
                        if(configKey.indexOf(':') == -1 ? this.type == configKey : this.parentKey == configKey){
                            list.push(Fantasy.clone(this));
                        }
					});
					return list;
				},
				reload : function(){
					var number = 5,loadScript = function(){
						number--;
						$.getScript($.includePath + 'static/js/config.js?' + Math.floor(Math.random() * 100),function(script,success,xhr){
							var newLastModified = new Date(xhr.getResponseHeader("Last-Modified")).format('yyyyMMddhhmmss');
							if(number > 0){
								if(Fantasy.config.lastModified == newLastModified){
									setTimeout(loadScript,1000);
								}else{
									Fantasy.config.lastModified = newLastModified;
								}
							}
						});
					};
					loadScript();
				},
				update : function() {
					if (arguments.length == 0) {
						$.getScript($.includePath + 'static/js/config.js?' + Math.floor(Math.random() * 100));
					} else {
						this.add.apply(this, arguments);
					}
				},
				add : function(config, callback) {
					var lazy = function(){
						$.ajax({ url: request.getContextPath() + '/admin/system/config/save.do', data: config, success: function(data){
							configs.push(data);
							if (callback) {
								callback.apply(this, [ data ]);
							}
							Fantasy.config.update();
						},error:function(){
							var data = configs.each(function() {
								if (this.type == config.type && this.name == config.name && this.parentKey == config.parentKey)
									return this;
							});
							if (data && callback) {
								callback.apply(this, [ data ]);
							}
						},type: 'POST'});
					};
					if(config.code){
						lazy();
					}else{
						$.post(request.getContextPath() + '/common/guid.do',function(data){
							config.code = data.guid;
							lazy();
						})
					}
				},
				addType : function(type, callback) {
					var lazy = function(){
						$.ajax({ url: request.getContextPath() + '/admin/system/config/typesave.do', data: type, success: function(data){
							types.push(data);
							if (callback) {
								callback.apply(this, [ data ]);
							}
							Fantasy.config.update();
						},error:function(){
							var data = types.each(function() {
								if (this.code == type.code)
									return this;
							});
							if (data && callback) {
								callback.apply(this, [ data ]);
							}
						},type: 'POST'});
					};
					if(type.code){
						lazy();
					}else{
						$.post(request.getContextPath() + '/common/guid.do',function(data){
							config.code = data.guid;
							lazy();
						})
					}
				},
				remove : function() {
				}
			};
	Fantasy.apply(Fantasy, {}, {
		config : config
	});
	Fantasy.apply(Fantasy.util.Format, {}, {
		configName : function(value, type) {
			var config = Fantasy.config.get(type, value);
			return config ? config.name : value;
		},
		configTypeName : function(key) {
			var type = Fantasy.config.types().each(function() {
				if (this.code == key) {
					return this;
				}
			});
			return type ? type.name : key;
		}
	});

});