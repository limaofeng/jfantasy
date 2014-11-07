function ajaxScroll(zhis,settings){
    //是否加载
    var boolAjax=!!settings.boolAjax?settings.boolAjax:true;
    //滚动区域
    var area=zhis;
    //滚动内容
    var content=settings.content;
    //提前多少像素加载
    var advanceSize=!!settings.advanceSize?settings.advanceSize:0;
    //滚动方式1：向下滚动时触发，2向上滚动时触发
    var type=!!settings.type?settings.type:1;
    //加载中div
    var loadDiv=settings.loadDiv;
    //加载的分页对象
    var pager=settings.pager;
    //ajax路径
    var url=settings.url;
    //请求参数
    var param=settings.param;
    //ajax方法
    var ajaxCallback=settings.ajaxCallback;
    //view对象
    var view=settings.view;
    //是否启用view.insert插入数据
    var isViewInsert=!!settings.isViewInsert?settings.isViewInsert:true;
    var callback=!!settings.callback?settings.callback:function (){
        if (pager.totalPage > pager.currentPage) {
            boolAjax=false,pager.currentPage += 1;
            var reqParam={"pager.currentPage": pager.currentPage};
            $.each(param, function(key, value) {
                reqParam[key]=value;
            });
            if(!!loadDiv)loadDiv.show();
            $.get(url, reqParam,function (data) {
                pager=data;
                if(isViewInsert){
                    for(var i= 0,count=data.pageItems.length;i<count;i++){
                        view.insert(data.pageItems[i]);
                    }
                }
                if(!!ajaxCallback)ajaxCallback();
                boolAjax=true;
                if(!!loadDiv)loadDiv.hide();
            }, "json");
        }
    }
    area.scroll(function () {
        if (type==2&&boolAjax&&area.scrollTop() <= 0) {
            callback();
        }
        if (type==1&&boolAjax&&(content.height() + content.scrollTop()+advanceSize) >= content.height()) {
            callback();
        }
    });
    return {
        resultAjax:function(bool){
            if(bool!=undefined) boolAjax=bool;
            else return boolAjax;
        }
    }
}

jQuery.fn.extend({
    ajaxScroll: function (settings) {
        return new ajaxScroll($(this),settings);
    }

});