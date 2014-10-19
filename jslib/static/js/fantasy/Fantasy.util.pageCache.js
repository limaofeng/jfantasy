var pageCache = Fantasy.util.PageCache = new (Fantasy.util.jClass({
	
	jClass : 'Fantasy.util.PageCache',
	
	initialize : function(prefix) {
		this.prefix = (typeof (prefix) != "string") ? "" : "prefix_" + prefix + "_";
		var thisz = this;
		$(function(){
			thisz.loadCache();//加载缓存
			thisz.BindEvent(thisz);//给页面控件绑定事件，用于记录缓存
		})
	},

	setValue : function(obj) {
		var tmpObj = document.getElementsByName(obj.name);
		if (obj.type == "text" || obj.type == "checkbox" || obj.type == "textarea") {
			for ( var i = 0; i < tmpObj.length; i++) {
				if (obj == tmpObj[i]) {
					Fantasy.util.Cookie.put(this.prefix + obj.form.name + "_" + obj.name	+ "_" + i.toString(),(obj.type == "checkbox") ? obj.checked : obj.value);
					break;
				}
			}
		} else if (obj.type == "radio" || obj.type == "select-one") {
			Fantasy.util.Cookie.put(this.prefix + obj.form.name + "_" + obj.name,obj.value);
		}
	},

	// 清除页面缓存
	clearCache : function() {
		var tmpObj;
		var tmpName;
		for ( var i = 0; i < document.forms.length; i++) {
			for ( var j = 0; j < document.forms[i].elements.length; j++) {
				tmpObj = document.forms[i].elements[j];
				if (tmpObj.type == "text" || tmpObj.type == "checkbox" || tmpObj.type == "textarea") {
					for ( var k = 0; k < document.getElementsByName(tmpObj.name).length; k++) {
						tmpName = this.prefix + document.forms[i].name + "_" + tmpObj.name + "_" + k.toString();
						if (Fantasy.util.Cookie.get(tmpName) != null)
							Fantasy.util.Cookie.remove(tmpName);
					}
				} else if (tmpObj.type == "radio" || tmpObj.type == "select-one") {
					tmpName = this.prefix + document.forms[i].name + "_" + tmpObj.name;
					if (Fantasy.util.Cookie.get(tmpName) != null)
						Fantasy.util.Cookie.remove(tmpName);
				}
			}
		}
	},

	/**
	 * 加载页面缓存
	 */
	loadCache : function() {
		var tmpValue;
		var tmpObj;
		for ( var i = 0; i < document.forms.length; i++) {
			for ( var j = 0; j < document.forms[i].elements.length; j++) {
				tmpValue = null;
				tmpObj = document.forms[i].elements[j];
				if (tmpObj.type == "text" || tmpObj.type == "checkbox" || tmpObj.type == "textarea") {
					for ( var k = 0; k < document.getElementsByName(tmpObj.name).length; k++) {
						if (document.forms[i].elements[j] == document.getElementsByName(tmpObj.name)[k]) {
							tmpValue = Fantasy.util.Cookie.get(this.prefix+ document.forms[i].name + "_"	+ tmpObj.name + "_" + k.toString());
							break;
						}
					}
					if (tmpValue != null) {
						if (tmpObj.type == "checkbox") {
							tmpObj.checked = tmpValue;
						} else {
							tmpObj.value = tmpValue;
						}
					}
				} else if (tmpObj.type == "radio") {
					tmpValue = Fantasy.util.Cookie.get(this.prefix + document.forms[i].name + "_" + tmpObj.name);
					if (tmpValue != null) {
						if (tmpObj.value == tmpValue)
							tmpObj.checked = true;
					}
				} else if (tmpObj.type == "select-one") {
					tmpValue = Fantasy.util.Cookie.get(this.prefix + document.forms[i].name + "_" + tmpObj.name);
					if (tmpValue != null) {
						for ( var k = 0; k < tmpObj.length; k++) {
							if (tmpObj.options[k].value == tmpValue) {
								tmpObj.options[k].selected = true;
								break;
							}
						}
					}
				}
			}
		}
	},
	
	/**
	 * 绑定控件事件
	 */
	BindEvent : function(CacheObj) {
		var arr;
		var i;
		arr = document.getElementsByTagName("INPUT");
		for (i = 0; i < arr.length; i++) {
			if (arr[i].type == "text") {
				arr[i].onblur = function() {
					CacheObj.setValue(this);
				}
			} else if (arr[i].type == "radio" || arr[i].type == "checkbox") {
				arr[i].onclick = function() {
					CacheObj.setValue(this);
				}
			}
		}

		arr = document.getElementsByTagName("TEXTAREA");
		for (i = 0; i < arr.length; i++) {
			arr[i].onblur = function() {
				CacheObj.setValue(this);
			}
		}

		arr = document.getElementsByTagName("SELECT");
		for (i = 0; i < arr.length; i++) {
			arr[i].onblur = function() {
				CacheObj.setValue(this);
			}
		}
	}
}))();