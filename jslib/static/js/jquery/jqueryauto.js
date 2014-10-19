//表示当前高亮的节点
var highlightindex = -1;
var timeoutId;
$(document).ready(function(){
    var keywordsInput = $("#keywords");
    var keywordsInputOffset = keywordsInput.offset();
    var text1="";
    //隐藏自动补全框并通过CSS设置补全框的位置大小及样式
    $("#auto").hide().css("border","1px black solid").css("position","absolute")
            .css("top","118px")
            .css("*top","118px")
            .css("z-index","8888")
            .css("line-height","18px")
            .css("background","#fff")
            .css("left","389px").width(keywordsInput.width());
    //添加键盘按下并弹起的事件
    keywordsInput.keyup(function(event){
        //处理文本框中的键盘事件
        var myEvent = event||window.event;
        var keyCode = myEvent.keyCode;
        //如果输入的是字母，退格，delete，空格或者数字键，应该将文本框中的最新信息发送给服务器,其中，空格键和数字键的加入使得输入中文也能支持~~
        if((keyCode >= 65 && keyCode<=90) || (keyCode >= 48 && keyCode <= 57) ||(keyCode>=96 && keyCode<=105) || keyCode == 46 || keyCode == 8 || keyCode == 32){
           //获取文本框的内容
            var keywordsText = $("#keywords").val();
            var modelType = $("#modelType").val();
            var autoNode = $("#auto");
            text1=keywordsText;
            if(keywordsText!="" && keywordsText.length > 3){
            //将文本框中的内容发送到服务器端
            //对上次未完成的延时操作进行取消
            clearTimeout(timeoutId);
            //对于服务器端进行交互延迟200ms，避免快速打字造成的频繁请求
           // keywordsText = encodeURIComponent(keywordsText);
            timeoutId = setTimeout(function(){
                $.getJSON("/isp/auto/complete/searchAutoCompleteInfo.do", {
                	param : keywordsText,
                	proFlag : false,
                	modelType : modelType
    			},function(data){
                autoNode.html(" ");
                for(var i = 0;i < data.length; i++){
    				var autoComplete = data[i];
    				 //新建div节点将单词内容加入到新建的节点中,将新建的节点加入到弹出框的节点中
    				var newDivNode = $("<div>").attr("id",i);
    				//var htmlVal = autoComplete.chineseName + "次数:" + autoComplete.searchCount;
    				var htmlVal = autoComplete.chineseName;
    				//var htmlVal = "<input type='hidden' value="+autoCompleteId+" />" + autoComplete.chineseName;
    				//var htmlVal = "<span><input type='hidden' name='' id='' value='"+111+"'/>" + autoComplete.chineseName + "</span>";
    				//var searchCount = autoComplete.searchCount;
    				//var id = autoComplete.id;
                    newDivNode.html(htmlVal).appendTo(autoNode);
                    //增加鼠标进入事件，高亮节点;
                    newDivNode.mouseover(function(){
                        if(highlightindex != -1){
                            $("#auto").children("div").eq(highlightindex).css("background-color","white");
                        }
                        highlightindex = $(this).attr("id");
                        $(this).css("background-color","yellow");
                    });
                    //增加鼠标移出事件，取消当前高亮节点
                    newDivNode.mouseout(function(){
                        $(this).css("background-color","white");
                    });
                    //增加鼠标点击事件，可以进行补全
                    newDivNode.click(function(){
                        var comText = $(this).text();
                        $("#auto").hide();
                        highlightindex=-1;
                        $("#keywords").val(comText);
                    });
    			}
                //如果服务器端有数据返回，则显示弹出框
                if(data.length >0){
                    autoNode.show();
                }else {
                	autoNode.hide();
                	highlightindex=-1;
                }
            },"");
            },200);
            }else{
                autoNode.hide();
                highlightindex=-1;
                }
        } else if(keyCode == 38 || keyCode==40){
            //如果输入的是向上38向下40按键
            if(keyCode == 38){
                var autoNodes = $("#auto").children("div");
                if (highlightindex != -1) {
                    autoNodes.eq(highlightindex).css("background-color","white");
                    highlightindex--;
                } else {
                    highlightindex = autoNodes.length - 1;    
                }
                //让高亮的内容在文本框中显示出来
                 if(highlightindex <= -1){
                	$("#keywords").val(text1);
                }else{
                	 var comText = $("#auto").children("div").eq(highlightindex).text();
                	$("#keywords").val(comText);
                }
               //让现在被高亮的内容变成黄色
                autoNodes.eq(highlightindex).css("background-color","yellow");
            }
            if(keyCode == 40){
                var autoNodes = $("#auto").children("div");
                if (highlightindex != -1) {
                    autoNodes.eq(highlightindex).css("background-color","white");
                }
                highlightindex++;
                if (highlightindex == autoNodes.length) {
                    highlightindex = -1;
                }
                //让高亮的内容在文本框中显示出来
                if(highlightindex <= -1){
                	$("#keywords").val(text1);
                }else{
                	 var comText = $("#auto").children("div").eq(highlightindex).text();
                	$("#keywords").val(comText);
                }
                 //让现在被高亮的内容变成黄色
                autoNodes.eq(highlightindex).css("background-color","yellow");
            }
            
        }else if(keyCode == 13){
            //如果按下的是回车
            //下拉框有高亮的内容
            if(highlightindex !=-1)
            {
                var comText = $("#auto").hide().children("div").eq(highlightindex).text();
                highlightindex=-1;
                $("#keywords").val(comText);//将文本框内容改成选中项
                //$("form:first").submit(); //提交form。若没有这句话，按下回车后，仅仅只改变了文本框里的内容，但是由于form本身就监控了回车按键默认为submit，提交的是文本框改变之前的内容，解决这个问题最简单的方式就是在文本框内容改变以后强制提交form的内容，此时，提交的内容就是选中项。
            }
            else{
                //下拉框没有高亮的内容
                $("#auto").hide();
              //让文本框失去焦点
                $("#keywords").get(0).blur();
            }
        }
    });
});