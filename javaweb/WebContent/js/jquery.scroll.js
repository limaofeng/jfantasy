window.scrollDiv={theme:{
    html:function(settings){
        var scroll='<div class="content-box bg-white" style="margin:0px">'+((!settings.searchOption.title)?'':'<h3 class="content-box-header ui-state-default">'+
            '<div class="glyph-icon icon-separator transparent">'+
            '<i class="glyph-icon icon-reorder"></i>'+
            '</div>'+
            '<span class="pad0L">'+settings.searchOption.title+'</span>'+
            '</h3>')+
            '<div class="optionScrollBtn" style="background: #F7F9FC!important;padding-top: 10px;padding-left: 10px;"></div>'+
            (!settings.searchOption.searchName?'':'<div class="button-pane button-pane-top pad10A">'+
                '<div class="form-row pad0B"><div class="form-input col-md-12"><div class="form-input-icon">'+
                '<i class="glyph-icon icon-search transparent searchScrollBtn"></i>'+
                '<input type="text" name="'+settings.searchOption.searchName+'"  placeholder="'+settings.searchOption.searchText+'" class="radius-all-100 key13 searchInput" data-class="searchScrollBtn"name="">'+
                '</div></div></div></div>')+
                '<div class="scrollable-content  grid-panel scrollViewDiv" tabindex="5005" style="overflow: hidden; outline: none;">'+
                    '<ul class="notifications-box scrollView" style="border:0px;">'+
                        '<li  class="template" name="default" style="height:46px;line-height: 46px;">'+
                            '<div class="large btn info-icon float-left mrg5R dropdown head" style="width:45px; height: 45px;">'+
                            '<a data-toggle="dropdown" href="javascript:;" title="">'+
                            '<img data-src="holder.js/38x38/simple" class="img-small view-field" style="height: 39px;width: 39px;"/>'+
                            '</a>'+
                            '</div>'+
                            '<p><span class="label bg-purple mrg5R titleName" style="min-width: 0.8em;height: 1.4em;line-height: 1.4em;"></span></p>'+
                        '</li>'+
                        '<li class="loadScroll" style="display:none;">'+
                        '<span class="notification-text" style=" width:100%;text-align: center;">正在加载...</span>'+
                        '</li>'+
                        '<li class="notScroll" style="display:none;">'+
                        '<div class="notification-text" style=" width:100%;text-align: center;">无匹配项</div>'+
                        '</li>'+
                    '</ul>'+
                '</div>'+
            '</div>';
        return $(scroll);
    }
}
}

function ajaxScroll(zhis,settings){
    //是否加载
    var boolAjax=!!settings.boolAjax?settings.boolAjax:true;
    //滚动区域
    var area=zhis;
    //滚动内容
    var content=settings.content;
    //过滤方法
    var filter=!!settings.searchOption?settings.searchOption.filter:undefined;
    //显示的数据字段
    var data=settings.data;
    //提前多少像素加载
    var advanceSize=!!settings.advanceSize?settings.advanceSize:0;
    //滚动方式1：向下滚动时触发，2向上滚动时触发
    var type=!!settings.type?settings.type:1;
    //加载中div
    var loadDiv=settings.loadDiv;
    //没有匹配项的div
    var notDiv=settings.notDiv;
    //加载的分页对象
    var pager=settings.pager;
    //ajax路径
    var url=settings.url;
    //请求参数
    var param=settings.param;
    //ajax方法
    var ajaxCallback=settings.ajaxCallback;
    //搜索框文字
    var searchText=!!settings.searchText?settings.searchText:"搜索";
    //view对象
    var view=settings.view;
    //搜索配置项
    var searchOption=!!settings.searchOption?settings.searchOption:{searchText:"搜索"};
    //是否启用view.insert插入数据
    var isViewInsert=settings.isViewInsert!=undefined?settings.isViewInsert:true;
    //初始化的时候是否清空子节点
    var isEmptyInit=settings.isEmptyInit!=undefined?settings.isEmptyInit:false;
    //是否显示选中效果
    var isShowCurent=settings.isShowCurent!=undefined?settings.isShowCurent:false;
    //view add方法
    var viewAdd=settings.viewAdd;
    //操作的按钮[{href:"/wp/",ajax:"",name="新增",click=function}]
    var optionBtn=settings.optionBtn;
    //选中样式
    var currentCss=!!settings.currentCss?settings.currentCss:{current:{"zoom":"1.2"},not:{"zoom":"1"}};

    //触发滚动的方法
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
                if(!!notDiv)notDiv.hide();
                if(!!loadDiv)loadDiv.hide();
                boolAjax=true;
            }, "json");
        }
    };
    var getValue=function(data,s){
        var ary= s.trim().split("."),value="";
        $.each(ary,function(i,o){
            if(o.indexOf("[")>0&&!data[o.substring(0,o.indexOf("["))]||!data[o]&&o.indexOf("[")<0){
                value="";
                return;
            }
            var val=o.indexOf("[")>0?data[o.substring(0,o.indexOf("["))][o.substring(o.indexOf("[")+1,o.indexOf("]"))]:data[o];
            i+1==ary.length?value=val:data=val;
        })
        return value;
    }
    var imgSize=function (url,size){
        return url.substring(0,url.lastIndexOf("."))+"_"+size+url.substring(url.lastIndexOf("."));
    };
    var showCurrent=function(element){
        $(".scrollView .template").each(function(){
            $(this).removeClass("curent").removeClass("primary-bg").css(currentCss.not);
        })
        element.addClass("curent").addClass("primary-bg").css(currentCss.current);
    };
    var load=function(){
        var param={};
        param[$(".searchInput").attr("name")]=$(".searchInput").val();
        $.post(url,param,function (data) {
            pager=data;
            view.setJSON(data.pageItems);
            if(!!notDiv)
                data.pageItems.length==0?notDiv.show():notDiv.hide();
        }, "json");
    }

    if(isEmptyInit){
        $(zhis).empty();
        settings.searchOption=searchOption;
        var scroll=window.scrollDiv.theme.html(settings);
        $(zhis).append(scroll);
        $(zhis).initialize();
    }
    if(!notDiv)notDiv=$(zhis).find(".notScroll");
    if(!loadDiv)loadDiv=$(zhis).find(".loadScroll");

    if(!!optionBtn){
        for(var i=0;i< optionBtn.length;i++){
            var item=optionBtn[i],btn;
            if(i>=3&&optionBtn.length>3){
                if(i==3){
                    var more=$('<div class="dropdown btn-group">'+
                        '            <a href="javascript:;" class="btn medium primary-bg" title="" data-toggle="dropdown">'+
                        '                <span class="button-content text-center float-none font-size-11 text-transform-upr">'+
                        '                    <i class="glyph-icon icon-caret-down float-right"></i>'+
                        '                   更多操作</span>'+
                        '            </a>'+
                        '            <ul class="dropdown-menu float-right   ">'+
                        '            </ul>'+
                        '        </div>');
                    $(".optionScrollBtn").append(more);
                }
                btn=$('<li>'+
                    '                    <a title="'+item.name+'" class="optionScrollBtn'+i+'" data-href="'+item.href+'"  ajax="'+item.ajax+'">'+
                    '                       <i class="glyph-icon icon-plus mrg5R"></i>'+item.name+
                    '                   </a>'+
                    '                </li>');

            }else{
                btn=$('<a title="'+item.name+'" style="margin-right:10px" class="btn medium primary-bg optionScrollBtn'+i+'" data-href="'+item.href+'" '+(!!item.target?' target="'+item.target+'"':'')+' ><span class="button-content">'+item.name+'</span></a>');
            }

            if(!!item.click) btn.on("click",item.click);
            optionBtn.length>3&&i>=3?$(".optionScrollBtn").find(".dropdown-menu").append(btn):$(".optionScrollBtn").append(btn);
        }
        $('.optionScrollBtn a[target]', $(zhis)).filter(function(){
            return !$(this).data('_target') && ['_blank','_self','_parent','_top'].indexOf($(this).attr('target')) == -1;
        }).data('_target',true).each(function () {
            $(this).target();
        });
    }else{
        $('.optionScrollBtn').remove();
    }

    if(!view){
        view=zhis.find(".scrollView").view().on("add",function(d){
            var zhis=this;
            if(!!data){
                var value=getValue(d,data.img.path);
                this.target.find("img").attr("src",request.getContextPath()+(!!data.img.imgSize?imgSize(value,data.img.imgSize):value));
                this.target.find(".titleName").html(getValue(d,data.title));
            }

            if(!!viewAdd)viewAdd(this,d);
            this.target.on("click",function(){
                if(!!optionBtn){
                    for(var i=0;i< optionBtn.length;i++){
                        var btn=$(".optionScrollBtn"+i),href=btn.data("href"),str="";
                        while(true){
                            if(href.lastIndexOf("$[")<0)
                                break;
                            str=href.substring(href.lastIndexOf("$[")+2,href.lastIndexOf("]"));
                            href=href.replace("$["+str+"]",d[str]);
                        }
                        btn.attr("href",!optionBtn[i].click?href:"javascript:;");
                        if(!!optionBtn[i])btn.data("d", d);
                    }
                }
                if(isShowCurent) showCurrent($(this));
            });
            if(isShowCurent&&this.target.index()==0) this.target.click();
        });
        view.setJSON(pager.pageItems);
        zhis.find(".grid-panel").removeClass("grid-panel");
    }
    $('.key13').bind('keypress',function(event){
        if(event.keyCode == "13")
        {
            $("."+$(this).data("class")).click();
        }
    });
    //搜索按钮
    $(".searchScrollBtn").click(function(){
        var value=$(".searchInput").val();
        var result=[];
        if(value.trim()!=""&&!!filter){
            $.each(view.getData(),function(i,e){
                if(typeof filter =="function"&&filter(e,value)){
                    result.push(i);
                }else if(!!filter){
                    $.each(filter,function(g,d){
                        if(getValue(e,d).toUpperCase().indexOf(value.toUpperCase())>=0){
                            result.push(i);
                        };
                    })
                }
            });
            if(result.length!=0){
                var templates=$(".scrollView .template");
                $(".scrollView li").hide();
                for(var i=0;i<result.length;i++){
                    templates.eq(result[i]).show();
                }
                showCurrent(templates.eq(result[0]));
                return;
            }
        }else{
            //如果为空的话就将隐藏的显示
            var ishidden=false;
            $(".scrollViewDiv li").each(function(){
                if( $(this).is(":hidden")&&$(this).is(".template"))
                    ishidden=true;
            });
            if(ishidden){
                $(".scrollViewDiv .template").show();
                return;
            }
        }
        //加载数据
        load();
    });


    area.scroll(function () {
        if (type==2&&boolAjax&&area.scrollTop() <= 0) {
            callback();
        }
        if (type==1&&boolAjax&&(content.height() + content.scrollTop()+advanceSize) >= content.height()) {
            callback();
        }
    });
    this.resultAjax=function(bool){
        if(bool!=undefined) boolAjax=bool;
        else return boolAjax;
    };
    this.load=function(){
        load();
    };
}

jQuery.fn.extend({
    ajaxScroll: (function(_ajaxuScroll){
        return  function (settings) {
            return new _ajaxuScroll($(this),settings);
        }
     })(ajaxScroll)
});