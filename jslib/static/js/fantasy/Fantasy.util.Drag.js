//============================================================
//[描    述]  Fantasy.awt.Drag		主要功能：页面拖拽效果
//============================================================
Fantasy.util.jClass(Fantasy.util.Observable,(function(){
	
	var $ = (function($){
		return function(target){
			return target?(target instanceof jQuery?target:$(target)):null;
		};
	})(jQuery);
	
	return {
			jClass : 'Fantasy.util.Drag',

			/**
			 * 构造方法
			 * 
			 * @param {Object}
			 *            $super 父类方法
			 * @param {Object}
			 *            config 初始设置
			 */
			initialize : function($super,target, options) {//settings
				$super(new Array(['start','move','stop','option']));
				
				this.target = $(target);//拖放对象
				this._x = this._y = 0;//记录鼠标相对拖放对象的位置
				this._marginLeft = this._marginTop = 0;//记录margin
				//事件对象(用于绑定移除事件)
				this._fM = (function(zthis){
					return function(event){
						zthis.move.apply(zthis,[event||window.event]);
					};
				})(this);
				this._fS = (function(zthis){
					return function(event){
						zthis.stop.apply(zthis,[event||window.event]);
					};
				})(this);
				this.options = Fantasy.merge(options || {},{//默认值
					handle:			this.target,//设置触发对象（不设置则使用拖放对象）
					limit:			false,//是否设置范围限制(为true时下面参数有用,可以是负数)
					mxLeft:			0,//左边限制
					mxRight:		9999,//右边限制
					mxTop:			0,//上边限制
					mxBottom:		9999,//下边限制
					container:		null,//指定限制在容器内
					lockX:			false,//是否锁定水平方向拖放
					lockY:			false,//是否锁定垂直方向拖放
					lock:			false,//是否锁定
					transparent:	false//是否透明
				});
				this.on('option',function(key,value){
					switch(key){
						case 'handle':
						case 'container':
							this[key] = $(value);
						break;
						default:
							this[key] = value;
					}
				});
				this.limit = !!this.options.limit;
				this.mxLeft = parseInt(this.options.mxLeft);
				this.mxRight = parseInt(this.options.mxRight);
				this.mxTop = parseInt(this.options.mxTop);
				this.mxBottom = parseInt(this.options.mxBottom);
				
				this.lockX = !!this.options.lockX;
				this.lockY = !!this.options.lockY;
				this.lock = !!this.options.lock;
				
				this.handle = this.options.handle ? $(this.options.handle) : null;
				this.container = this.options.container ? $(this.options.container) : null;
				
				this.target.css('position','absolute');
				
				if(Fantasy.Browser.isMsie && !!this.options.transparent){//透明
					this.handle.append($('<div></div>').css({width:'100%',height:'100%',backgroundColor:'#fff',filter:'alpha(opacity:0)',fontSize:0}));//填充拖放对象
				}
				this.repair();//修正范围
				this.handle.bind('mousedown',(function(zthis){
					return function(event){
						zthis.start.apply(zthis,[event||window.event]);
					};
				})(this));
			},
			  
			  
		  start: function(oEvent) {//准备拖动
			if(this.lock){ return; }
			this.repair();
			
			this._x = oEvent.clientX - this.target.left();//记录鼠标相对拖放对象的位置
			this._y = oEvent.clientY - this.target.top();
			
			this._marginLeft = Fantasy.toInt(this.target.css('marginLeft'))||0;//记录margin
			this._marginTop = Fantasy.toInt(this.target.css('marginTop'))||0;
			
			$(document).bind('mousemove',this._fM);//mousemove时移动 mouseup时停止
			$(document).bind('mouseup',this._fS);
			if(Fantasy.Browser.isMsie){
				this.handle.bind('losecapture',this._fS);//焦点丢失
				this.handle[0].setCapture();//设置鼠标捕获
			}else{
				$(window).bind('blur',this._fS);//焦点丢失
				oEvent.preventDefault();//阻止默认动作
			};
			this.fireEvent('start',this);//附加程序
		  },
			  
			  repair: function() {//修正范围
				if(this.limit){//修正错误范围参数
					this.mxRight = Math.max(this.mxRight, this.mxLeft + this.target[0].offsetWidth);
					this.mxBottom = Math.max(this.mxBottom, this.mxTop + this.target[0].offsetHeight);
					//如果有容器必须设置position为relative或absolute来相对或绝对定位，并在获取offset之前设置
					//!this.container || this.container.css('position') == "relative" || this.container.css('position') == "absolute" || (this.container.css('position','relative'));
				}
			  },
			  
			  move: function(oEvent) {//拖动
				if(this.lock){ this.stop(); return; };//判断是否锁定
				window.getSelection?window.getSelection().removeAllRanges():document.selection.empty();//清除选择
				var iLeft = oEvent.clientX - this._x, iTop = oEvent.clientY - this._y;//设置移动参数
				if(this.limit){//设置范围限制
					var mxLeft = this.mxLeft, mxRight = this.mxRight, mxTop = this.mxTop, mxBottom = this.mxBottom;//设置范围参数
					if(!!this.container){//如果设置了容器，再修正范围参数
						mxLeft = Math.max(mxLeft, 0);
						mxTop = Math.max(mxTop, 0);
						mxRight = Math.min(mxRight, this.container[0].clientWidth);
						mxBottom = Math.min(mxBottom, this.container[0].clientHeight);
					};
					iLeft = Math.max(Math.min(iLeft, mxRight - this.target[0].offsetWidth), mxLeft);//修正移动参数
					iTop = Math.max(Math.min(iTop, mxBottom - this.target[0].offsetHeight), mxTop);
				}
				if(!this.lockX){//设置位置，并修正margin
					this.target.css('left',iLeft - this._marginLeft + 'px')
				}
				if(!this.lockY){ 
					this.target.css('top',iTop - this._marginTop + 'px')
				}
				this.fireEvent('move',this);//附加程序
			  },
			  
			  stop: function() {//停止拖动
				$(document).unbind('mousemove',this._fM);//移除事件
				$(document).unbind('mouseup',this._fS);
				if(Fantasy.Browser.isMsie){
					this.handle.unbind('losecapture',this._fS);
					this.handle[0].releaseCapture();
				}else{
					$(window).unbind('blur',this._fS);
				};
				this.fireEvent('stop',this);//附加程序
			  }
	
		};

})());