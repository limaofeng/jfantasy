Fantasy.util.jClass(Fantasy.util.Observable,(function(){
	
	var $ = (function($){
		return function(target){
			return target?(target instanceof jQuery?target:$(target)):null;
		};
	})(jQuery);
	
	return {
		
		jClass : 'Fantasy.awt.ImgCropper',
		
		initialize : function($super,container, handle, url, options) {
			this._Container = $(container);//容器对象
			this._layHandle = $(handle);//控制层
			this.Url = url;//图片地址
			
			this._layBase = $('<img />').appendTo(this._Container);//底层
			this._layCropper = $('<img />').appendTo(this._Container);//切割层
			this._layCropper.bind('load',(function(zhis){
				return function(){
					zhis.SetPos.apply(zhis,arguments);
				};
			})(this));
			//用来设置大小
			this._tempImg = $('<img />');
			this._tempImg.bind('load',(function(zhis){
				return function(){
					zhis.SetSize.apply(zhis,arguments);
				};
			})(this));
			
			this.options = Fantasy.merge(options || {},{//默认值
				Opacity:	50,//透明度(0到100)
				Color:		"",//背景色
				Width:		0,//图片高度
				Height:		0,//图片高度
				//缩放触发对象
				Resize:		false,//是否设置缩放
				Right:		"",//右边缩放对象
				Left:		"",//左边缩放对象
				Up:			"",//上边缩放对象
				Down:		"",//下边缩放对象
				RightDown:	"",//右下缩放对象
				LeftDown:	"",//左下缩放对象
				RightUp:	"",//右上缩放对象
				LeftUp:		"",//左上缩放对象
				Min:		false,//是否最小宽高限制(为true时下面min参数有用)
				minWidth:	50,//最小宽度
				minHeight:	50,//最小高度
				Scale:		false,//是否按比例缩放
				Ratio:		0,//缩放比例(宽/高)
				//预览对象设置
				Preview:	null,//预览对象
				viewWidth:	0,//预览宽度
				viewHeight:	0//预览高度
		    });
			
			this.Opacity = Math.round(this.options.Opacity);
			this.Color = this.options.Color;
			this.Scale = !!this.options.Scale;
			this.Ratio = Math.max(this.options.Ratio, 0);
			this.Width = Math.round(this.options.Width);
			this.Height = Math.round(this.options.Height);
			
			//设置预览对象
			var oPreview = $(this.options.Preview);//预览对象
			if(oPreview){
				oPreview.css({position:'relative',overflow:'hidden'});
				this.viewWidth = Math.round(this.options.viewWidth);
				this.viewHeight = Math.round(this.options.viewHeight);
				this._view = $('<img />').appendTo(oPreview);//预览图片对象
				this._view.css('position','absolute');
				this._view.bind('load',(function(zhis){
					return function(){
						zhis.SetPreview.apply(zhis,arguments);
					}
				})(this));
			}
			this._drag = new Fantasy.util.Drag(this._layHandle, { limit: true,transparent: true });//设置拖放
			this._drag.on('move',(function(zhis){
				return function(){
					zhis.SetPos.apply(zhis, arguments);
				};
			})(this));
			
			//onMove: Bind(this, this.SetPos),
			//设置缩放
			this.Resize = !!this.options.Resize;
			if(this.Resize){
				var op = this.options, _resize = new Fantasy.util.Resize(this._layHandle, { Max: true});
				_resize.on('resize',(function(zhis){
					return function(){
						zhis.SetPos.apply(zhis,arguments);
					}
				})(this));
				//设置缩放触发对象
				op.RightDown && (_resize.Set(op.RightDown, "right-down"));
				op.LeftDown && (_resize.Set(op.LeftDown, "left-down"));
				op.RightUp && (_resize.Set(op.RightUp, "right-up"));
				op.LeftUp && (_resize.Set(op.LeftUp, "left-up"));
				op.Right && (_resize.Set(op.Right, "right"));
				op.Left && (_resize.Set(op.Left, "left"));
				op.Down && (_resize.Set(op.Down, "down"));
				op.Up && (_resize.Set(op.Up, "up"));
				//最小范围限制
				this.Min = !!this.options.Min;
				this.minWidth = Math.round(this.options.minWidth);
				this.minHeight = Math.round(this.options.minHeight);
				//设置缩放对象
				this._resize = _resize;
			}
			//设置样式
			this._Container.css({position:'relative',overflow:'hidden'});
			this._layHandle.css({zIndex:200});
			this._layCropper.css({position:'absolute',zIndex:100,top:0,left:0});//对齐
			this._layBase.css({position:'absolute',top:0,left:0});
			//初始化设置
			this.Init();
		},
		 Init: function() {
		 	this.Color && (this._Container.css('backgroundColor',this.Color));//设置背景色
			//设置图片
			this._tempImg.attr('src',this.Url);
			this._layBase.attr('src',this.Url);
			this._layCropper.attr('src',this.Url);
			//设置透明
			this._layBase.css('opacity',this.Opacity / 100);
			//设置预览对象
			this._view && (this._view.attr('src',this.Url));
			//设置缩放
			if(this.Resize){
				with(this._resize){
					Scale = this.Scale; Ratio = this.Ratio; Min = this.Min; minWidth = this.minWidth; minHeight = this.minHeight;
				}
			}
		 },
  //设置切割样式
  SetPos: function() {
	//ie6渲染bug
	if(Fantasy.Browser.isMsie ||false){ with(this._layHandle.style){ zoom = .9; zoom = 1; }; };
	//获取位置参数
	var p = this.GetPos();
	//按拖放对象的参数进行切割
	this._layCropper.css('clip','rect(' + p.Top + 'px ' + (p.Left + p.Width) + 'px ' + (p.Top + p.Height) + 'px ' + p.Left + 'px)');
	//设置预览
	this.SetPreview();
  },
  //设置预览效果
  SetPreview: function() {
	if(this._view){
		//预览显示的宽和高
		var p = this.GetPos(), s = this.GetSize(p.Width, p.Height, this.viewWidth, this.viewHeight), scale = s.Height / p.Height;
		//按比例设置参数
		var pHeight = this._layBase.height() * scale, pWidth = this._layBase.width() * scale, pTop = p.Top * scale, pLeft = p.Left * scale;
		//设置预览对象
		this._view.css({width:pWidth + 'px',height:pHeight + 'px',top:- pTop + 'px ',left:- pLeft + 'px',clip:'rect(' + pTop + 'px ' + (pLeft + s.Width) + 'px ' + (pTop + s.Height) + 'px ' + pLeft + 'px)'});
	}
  },
  //设置图片大小
  SetSize: function() {
	var s = this.GetSize(this._tempImg[0].width, this._tempImg[0].height, this.Width, this.Height);
	//设置底图和切割图
	this._layBase.css({width:s.Width + "px",height:s.Height + "px"});
	this._layCropper.css({width:s.Width + "px",height:s.Height + "px"});
	//设置拖放范围
	this._drag.mxRight = s.Width; this._drag.mxBottom = s.Height;
	//设置缩放范围
	if(this.Resize){ this._resize.mxRight = s.Width; this._resize.mxBottom = s.Height; }
  },
  //获取当前样式
  GetPos: function() {
  	with(this._layHandle[0]){
		return { Top: offsetTop, Left: offsetLeft, Width: offsetWidth, Height: offsetHeight }
	}
  },
  //获取尺寸
  GetSize: function(nowWidth, nowHeight, fixWidth, fixHeight) {
	var iWidth = nowWidth, iHeight = nowHeight, scale = iWidth / iHeight;
	//按比例设置
	if(fixHeight){ iWidth = (iHeight = fixHeight) * scale; }
	if(fixWidth && (!fixHeight || iWidth > fixWidth)){ iHeight = (iWidth = fixWidth) / scale; }
	//返回尺寸对象
	return { Width: iWidth, Height: iHeight }
  }
		
	};
	
})());