var UiUtil = {
		/**
		 * 名称：getAbsoluteLocation		属于:UiUtil
		 * 描述：获取Html元素 在浏览器中的位置
		 * @return
		 */
		getAbsoluteLocation : function(element){
		    if ( arguments.length != 1 || element == null ){
		        return null;
		    }
		    var offsetTop = element.offsetTop;
		    var offsetLeft = element.offsetLeft;
		    var offsetWidth = element.offsetWidth;
		    var offsetHeight = element.offsetHeight;
		    while( element = element.offsetParent )
		    {
		        offsetTop += element.offsetTop;
		        offsetLeft += element.offsetLeft;
		    }
		    return { absoluteTop: offsetTop, absoluteLeft: offsetLeft,offsetWidth: offsetWidth, offsetHeight:offsetHeight};
		},
		
		/**
		 * 名称：getCenter		属于:UiUtil
		 * 描述：获取window 的屏幕上下居中的坐标
		 * @return
		 */
		getCenter : function(element) {
			var _windowWidth = window.screen.width;// 取得屏幕分辨率宽度
			var _windowHeight = window.screen.height;// 取得屏幕分辨率高度
	
			var yScroll;// 取滚动条高度
			if (self.pageYOffset) {
				yScroll = self.pageYOffset;
			} else if (document.documentElement
					&& document.documentElement.scrollTop) {
				yScroll = document.documentElement.scrollTop;
			} else if (document.body) {
				yScroll = document.body.scrollTop;
			}
	
			element.top = _windowHeight / 2 - 250 + yScroll;
			element.left = _windowWidth / 2 - element.width / 2;
	
			return element;
		},
	
		/**
		 * 名称：window		属于:UiUtil
		 * 描述：获取window 的屏幕信息
		 * @return
		 */
		window : function() {
			var _windowWidth = window.screen.width;// 取得屏幕分辨率宽度
			var _windowHeight = window.screen.height;// 取得屏幕分辨率高度
			var yScroll;// 取滚动条高度
			if (self.pageYOffset) {
				yScroll = self.pageYOffset;
			} else if (document.documentElement
					&& document.documentElement.scrollTop) {
				yScroll = document.documentElement.scrollTop;
			} else if (document.body) {
				yScroll = document.body.scrollTop;
			}
			var element = new Object();
			element.top = yScroll;
			element.left = 0;
			element.width = _windowWidth;
			element.height = _windowHeight;
			return element;
		}
	};