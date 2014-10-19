/*
 * TouchSliderBox v1.0.1
 * Base on TouchScroll(http://www.qiqiboy.com/2012/04/01/touchscroll-v1-0-publish.html)
 * By qiqiboy, http://www.qiqiboy.com, http://weibo.com/qiqiboy, 2012/04/06
 */

window.TouchSliderBox=(function(){
	var stack=[],
		ready=false,
		SliderBox,
		fix=function(){
			SliderBox=function(box){
				this.container=typeof box=='string'?this.$(box):box;
				try{
					var ul=this.container.getElementsByTagName('ul')[0],
						lis=ul.getElementsByTagName('li');
					this.index=0;
					this.imgNum=lis.length;
					this.imgWidth=lis[0].offsetWidth+parseInt(this.css(lis[0],'margin-left'))+parseInt(this.css(lis[0],'margin-right'));
					ul.style.width=this.imgNum*this.imgWidth+'px';
					this.setup();
				}catch(e){
					this.error=e.message;	
				}
			}
			SliderBox.prototype=new TouchScroll({id:null,width:4,opacity:0.6,color:'#999',minLength:10,ondrag:function(flag,distance){
				if(flag===0){
					var index,
						offset=parseInt(this.element.style.left),
						_num=Math.round(this.clientWidth/this.imgWidth);
					if(offset>0)index=0;
					else if(offset<this.clientWidth-this.scrollWidth){
						index=this.imgNum;
					}else{
						index=Math.round(-offset/this.imgWidth);
						if(distance){
							index-=distance/Math.abs(distance);	
						}
					}
					if(index<=this.imgNum-_num){
						this.slideTo(index);
						return false;
					}
				}
			}});
			SliderBox.prototype.slideTo=function(index){
				var _num=Math.floor(this.clientWidth/this.imgWidth);
				if(index<0)index=0;
				else if(index>this.imgNum-_num)index=this.imgNum-_num;
				var offset=parseInt(this.element.style.left),
					finalOffset=-this.imgWidth*index;
				
				/*--------- 修改 start------------*/
				var w = 0;
				jQuery(this.container).find('li').slice(0,index).each(function(){
					w += jQuery(this).width();
				});
				finalOffset=-w;
				/*--------- 修改 end------------*/
				
				if(!offset && !finalOffset)finalOffset=40;
				this._scroll(0,finalOffset-offset);
				this.index=index;
			}
			SliderBox.prototype.prev=function(){
				this.slideTo(this.index-1);
			}
			SliderBox.prototype.next=function(){
				this.slideTo(this.index+1);
			}
			SliderBox.prototype.mouseScroll=function(e){
				this.preventDefault(e);
				this.msNow=new Date();
				if(this.msOld && this.msNow-this.msOld<100)return;
				e=e||window.event;
				var wheelDelta=e.wheelDelta || e.detail && e.detail*-1 || 0,
					flag;//这里flag指鼠标滚轮的方向，1表示向上，-1向下
				if(this.wrapper && wheelDelta){
					flag=wheelDelta/Math.abs(wheelDelta);
					this.slideTo(this.index-flag);
					this.msOld=this.msNow;
				}
			}
		}
	/*
	if(typeof window.TouchScroll == 'undefined'){
		var s=document.createElement('script');
		s.src="http://qiqiboy.sinaapp.com/touchscroll/touchScroll.js";
		s.onload=s.onerror=s.onreadystatechange=function(){
			if(s&&s.readyState&&s.readyState!='loaded'&&s.readyState!='complete'){
				return;
			}
			s.onload=onerror=s.onreadystatechange=null;
			fix(); s.parentNode.removeChild(s); ready=true;
			while(stack.length){
				(stack.shift())();
			}
		}
		document.getElementsByTagName("head")[0].appendChild(s);
	}else{
		fix();
		ready=true;
	}
	*/
	fix();
	ready=true;
	
	return function(box,t){
		return new SliderBox(box);
	};
})();