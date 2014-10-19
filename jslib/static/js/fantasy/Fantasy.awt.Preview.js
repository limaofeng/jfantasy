Fantasy.util.jClass(Fantasy.util.Observable,{
	
	jClass:'Fantasy.awt.Preview',
	
	initialize : function($super,file,img,options) {
		this.options = Fantasy.extend(options,{mode:Fantasy.awt.Preview.MODE,ratio:0,maxWidth:0,maxHeight:0});
		this.file = file;
		this.img = img;			
		//设置数据获取程序
		this.getData = this.getDataFun(this.options.mode);
		//设置预览显示程序
		this._show = this.options.mode !== "filter" ? this._simpleShow : this._filterShow;
	},
	//开始预览
	preview: function() {
		if (this.file) {
			this._preview(this.getData());
		}
	},
	//预览程序
	_preview: function(data) {
		//空值或相同的值不执行显示
		if ( !!data && data !== this._data ) {			
			this._data = data; this._show();
		}
	},
	
	getDataFun : function(mode) {
		switch (mode) {
			case "filter":
				return this._filterData;
			case "domfile":
				return this._domfileData;
			case "remote":
				return this._remoteData;
			case "simple":
				return this._simpleData;
			default:
				return this._simpleData;
		}
	},
	
	// 滤镜数据获取程序
	_filterData: function() {
		this.file.select();
		try{
			return document.selection.createRange().text;
		} finally { document.selection.empty(); }
	},
	
	// domfile数据获取程序
	_domfileData: function() {
		return window.URL.createObjectURL(this.file[0].files[0]);//this.file[0].files[0].getAsDataURL();
	},	
	
	// 远程数据获取程序
	_remoteData: function() {
		this._setUpload();
		this._upload && this._upload.upload();
	},
	
	// 一般数据获取程序
	_simpleData: function() {
		return this.file.val();
	},
	
	//一般显示
	_simpleShow: function() {
		this._simplePreload();
		this._preload.src = this._data;
	},
	
	_filterShow: function() {
		this._filterPreload();
		var preload = this._preload[0],
			data = this._data.replace(/[)'"%]/g, function(s){ return escape(escape(s)); });
		try{
			preload.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = data;
		}catch(e){ this._error("filter error"); return; }
		//设置滤镜并显示
		this.img[0].style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='scale',src=\"" + data + "\")";
		this._imgShow( Fantasy.awt.Preview.TRANSPARENT, preload.offsetWidth, preload.offsetHeight );
	},
	//显示预览
	_imgShow: function(src, width, height) {
		var ratio = Math.max( 0, this.options.ratio ) || Math.min( 1,
				Math.max( 0, this.options.maxWidth ) / width  || 1,
				Math.max( 0, this.options.maxHeight ) / height || 1
				);
		//设置预览尺寸
		this.img.width(Math.round( width * ratio ) + "px");
		this.img.height(Math.round( height * ratio ) + "px");
		if(Fantasy.Browser.isMsie && Fantasy.Browser.version < 8){
			this.img.attr('src',this.file.val());
		}else{
			this.img.attr('src',src);
		}
	},
	  //设置一般预载图片对象
	  _simplePreload: function() {
		if ( !this._preload ) {
			var preload = this._preload = new Image(), oThis = this,
				onload = function(){ oThis._imgShow( oThis._data, this.width, this.height ); };
			this._onload = function(){ this.onload = null; onload.call(this); }
			preload.onload = Fantasy.Browser.isMsie ? this._onload : onload;
			preload.onerror = function(){ oThis._error(); };
		} else if ( Fantasy.Browser.isMsie ) {
			this._preload.onload = this._onload;
		}
	  },
	  //设置滤镜预载图片对象
	  _filterPreload: function() {
		if ( !this._preload ) {
			var preload = this._preload = $('<div></div>');
			//隐藏并设置滤镜
			preload.attr('style',"position: absolute; filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='image'); WIDTH: 1px; HEIGHT: 1px; VISIBILITY: hidden; TOP: -9999px; LEFT: -9999px;");
			//插入body
			$('body').prepend(preload);
		}
	  }
});

try{
	//根据浏览器获取模式
	Fantasy.awt.Preview.MODE = (Fantasy.Browser.isMsie && Fantasy.Browser.version == 8) ? "filter" : Fantasy.Browser.isMozilla ? "domfile" : Fantasy.Browser.isOpera || Fantasy.Browser.isSafari ? "remote" : "simple";
	//透明图片
	Fantasy.awt.Preview.TRANSPARENT = Fantasy.Browser.isMsie && (Fantasy.Browser.version == 6 || Fantasy.Browser.version == 7) ? "mhtml:" + document.scripts[document.scripts.length - 1].getAttribute("src", 4) + "!blankImage" : "data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==";
}catch (e) {
}