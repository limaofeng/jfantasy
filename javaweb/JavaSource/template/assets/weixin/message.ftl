<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="pageTitle">
消息列表
<small>
    当用户主动发消息给公众号并在发送消息的48小时内才能回复消息
</small>
</@override>
<@override name="head">
<script language="javascript" type="text/javascript" src="${request.contextPath}/js/jquery.scroll.js" charset="utf-8"></script>
<script type="text/javascript">

    $(function() {
        //var socket = new WebSocket('ws://211.100.41.186:9999');//http://211.100.41.186:9999/-
        var socket = new WebSocket('ws://'+window.location.host+request.getContextPath()+'/weixin/msg');
        // 打开Socket
        socket.onerror = function(event){
            console.log(event);
        };
        socket.onopen = function(event) {
            console.log(event);
            // 监听消息
            socket.onmessage = function(event) {
                var data=JSON.parse(event.data);
                $("#userView li").each(function(){
                    if($(this).data("openid")==data.fromUserName){
                        if($(this).hasClass("current_user")){
                            messageView.insert(messageView.getData().length,data);
                            //当接受的消息是当前聊天的用户时，设置消息为已读
                            socket.send(data.fromUserName);
                            $("#messageScroll").scrollTop($("#messagePager").height());
                        }else{
                            $(this).find(".unreadSize").text(data.userInfo.unReadSize);
                            $(this).find(".unreadSize").show();
                        }
                    }
                });
            };
            // 监听Socket的关闭
            socket.onclose = function(event) {
                console.log('Client notified socket has closed', event);
            };
            // 关闭Socket....
            //socket.close()
        };

        $("#sendMsg").click(function(){
            socket.send($("#msg").val());
        });
        //当浏览器窗口发生变化时,自动调整布局的js代码
        $(window).resize(function () {
            var _$gridPanel = $('.grid-panel');
            if(!!_$gridPanel.length){
                _$gridPanel.css('height', $(window).height() - (_$gridPanel.offset().top+36));
                _$gridPanel.triggerHandler('resize');
            }
        });
        var userPager=<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(userPager)" escapeHtml="false"/>;
        var messagePager=<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(messagePager)" escapeHtml="false"/>;
        messagePager.pageItems.reverse();
        //当消息数为0时不能发送消息
        if(messagePager.pageItems.length==0){
            $('#content').attr("readonly","readonly");
        }
        //如果消息总条数大于分页条数时显示更多消息按钮
        if (messagePager.totalCount>messagePager.pageSize) {
                $("#moreMessage").data("time",messagePager.pageItems[0].createTime);
        }else{
            $("#moreMessage").hide();
        }
        var userView=$("#userView").view().on("add",function(data){
            this.target.find('img').attr("src",data.headimgurl);
            this.target.data("openId",data.openId);
            $(this.target).click(function(){
                var zhis=this,size=$(zhis).find(".unreadSize").text();
                $.get("${request.contextPath}/weixin/message/search.do", {"pager.pageSize":size==0?3:size,"EQS_userInfo.openid":$(this).data("openid")},
                        function (data) {
                            $(".current_user").removeClass("current_user");
                            $(zhis).addClass("current_user");
                            messagePager=data;
                            messagePager.pageItems.reverse();
                            messageView.setJSON(data.pageItems);
                            data.pageItems.length==0?$('#content').attr("readonly","readonly"):$('#content').removeAttr("readonly");
                            messagePager.totalPage <= messagePager.currentPage? $("#moreMessage").hide():$("#moreMessage").show();
                            if(data.pageItems.length>0)
                                $("#moreMessage").data("time",data.pageItems[0].createTime);
                            $("#messageScroll").scrollTop($("#messagePager").height());
                            $(zhis).find(".unreadSize").hide();
                        }, "json");
            });
            $(this.target).data("openid",data.openid);
            if(this.getIndex()==0){
                $(this.target).addClass("current_user");
            }
            this.target.find(".unreadSize").text(data.unReadSize);
            if(data.unReadSize==0){
                this.target.find(".unreadSize").hide();
            }
        }).setJSON(userPager.pageItems);

        var messageView=$("#messagePager").view().on("add",function(data){
            if(this.getTemplateName() =="left")
                return ;
            if(data.type=="send"){
                messageView.setTemplate(this.getIndex(),"left");
            }
            if(this.getTemplateName() =="default"){
                this.target.find('.imgHead').attr("src",data.userInfo.headimgurl);
            }
            //$("#messageScroll").scrollTop($("#messagePager").height());
        });
        messageView.setJSON(messagePager.pageItems);
        $("#moreMessage").click(function(){
            var param={"EQS_userInfo.openid":$(".current_user").data("openid")}
            if(!!$("#moreMessage").data("time")){
                param["LTL_createTime"]=$("#moreMessage").data("time");
            }
            $.get("${request.contextPath}/weixin/message/search.do",param,function (data) {

                for(var i= 0,count=data.pageItems.length;i<count;i++){
                    messageView.insert(0,data.pageItems[i]);
                }
                if (data.totalPage!=data.currentPage) {
                        $("#moreMessage").data("time",data.pageItems[data.pageItems.length-1].createTime);
                }else{
                    $("#moreMessage").hide();
                }
                messagePager=data;
                $("#messageScroll").scrollTop($("#messagePager .template:eq(0)").height()*(data.pageItems.length+5));
            }, "json");
        })/*
        var messageScroll=$("#messageScroll").ajaxScroll({
            content:$("#messagePager"),
            type:2,
            callback:function(){
                if (messagePager.totalPage > messagePager.currentPage) {
                    messageScroll.resultAjax(false);
                    messagePager.currentPage += 1;
                    $.get("${request.contextPath}/weixin/message/search.do", {"EQS_userInfo.openid":$(".current_user").data("openid"),"pager.currentPage": messagePager.currentPage},function (data) {
                        messagePager=data;
                        for(var i= 0,count=data.pageItems.length;i<count;i++){
                            messageView.insert(0,data.pageItems[i]);
                        }
                        messageScroll.resultAjax(true);
                    }, "json");
                }
            }
        });*/
        $("#enterMessage").click(function(){
            $.post("${request.contextPath}/weixin/message/send.do",{content:$("#content").val(),"userInfo.openid":$(".current_user").data("openid"),msgType:"text"},function(data){
                if(data=="0"){
                    messageView.insert({content:$("#content").val()},"left")
                    $("#content").val("");
                    $("#messageScroll").scrollTop($("#messagePager").height());
                }else if(data=="0"){
                    $.msgbox({
                        msg : "消息发送失败!",
                        type : "warning"
                    });
                }

            });

        });
        $('.key13').bind('keypress',function(event){
            if(event.keyCode == "13")
            {
                $("#"+$(this).data("id")).click();
            }
        });
        $("#userScroll").ajaxScroll({
            content:$("#userView"),
            advanceSize:50,
            loadDiv:$(".loadUser"),
            pager:userPager,
            url:"${request.contextPath}/weixin/message/searchUserInfo.do",
            param:{"pager.pageSize":6},
            view:userView,
            ajaxCallback:function(){
                $(".notUser").hide();
            }
        });
        //用户搜索按钮
        $("#searchWxBtn").click(function(){
            var value=$("#searchWx").val();
            var bool=true;
            var ishidden=false;
            //在本地回话列表里面查找是否有匹配用户有则显示
            $("#userView li").each(function(){
                if( $(this).is(":hidden")&&$(this).is(".template"))
                    ishidden=true;
                if($(this).is(".template")&&$(this).find("span").text().toUpperCase().indexOf(value.toUpperCase())>=0){
                    $("#userView li").hide();
                    $(this).show();
                    bool=false;
                    return;
                }
            });
            if(value==""&&ishidden){
                $("#userView .template").show();
                    return;
            }
            //如果没有本地匹配项就在后台查询
            if(bool){
                $.post("${request.contextPath}/weixin/message/searchUserInfo.do", {"LIKES_nickname":value},
                function (data) {
                    userPager=data;
                    userView.setJSON(data.pageItems);
                    if(data.pageItems.length==0) $(".notUser").show();
                }, "json");
            }

        });
    });
</script>
<style>
    .chat-box .popover{width:auto;}
    .chat-box .popover.left {
        float: right;
    }
    .notifications-box li{
        height:46px;
        line-height: 46px;;
    }
    .chat-time{
        text-align: center;
    }
    .chat-box .popover.left .arrow:after {
        border-left-color: #fff;
    }
    .chat-box .popover.right .arrow:after {
        border-right-color: #fff;
    }
    .current_user{
        zoom:1.3;
        background: #2381E9;
    }
    .current_user p span{
        color: #797979;
        background: #e9ecf1;
    }
    .badge-absolute{
        right:-5px;
        left:auto;
    }
    #userScroll .badge, .label{
        min-width: 0.8em;
        height: 1.4em;
        line-height: 1.4em;
    }
    .notification-text{width:100%;text-align: center;}
    .loadUser,.notUser{display:none;}
    #userView .head{width:45px; height: 45px;}
    #userView .head img{height: 39px;width: 39px;}
</style>
</@override>
<@override name="pageContent">
<div class="row" id="messagePage">
    <div class="col-md-4">
        <div class="content-box bg-white">
            <h3 class="content-box-header ui-state-default">
                <div class="glyph-icon icon-separator transparent">
                    <i class="glyph-icon icon-comments"></i>
                </div>
                <span class="pad0L">微信用户</span>
            </h3>
            <div class="button-pane button-pane-top pad10A">
                <div class="form-row pad0B">
                    <div class="form-input col-md-12">
                        <div class="form-input-icon">
                            <i class="glyph-icon icon-search transparent" id="searchWxBtn"></i>
                            <input type="text" placeholder="微信昵称" class="radius-all-100 key13" data-id="searchWxBtn"name="" id="searchWx">
                        </div>
                    </div>
                </div>
            </div>
            <div class="content-box-wrapper">
                <div class="scrollable-content grid-panel" id="userScroll" tabindex="5005" style="overflow: hidden; outline: none;">
                    <ul class="notifications-box" id="userView">
                        <li  class="template" name="default">
                            <div class="large btn info-icon float-left mrg5R dropdown head">
                                <a data-toggle="dropdown" href="javascript:;" title="">
                                    <span class="badge badge-absolute bg-orange unreadSize" ></span>
                                    <img data-src="holder.js/38x38/simple" class="img-small view-field"/>
                                </a>
                            </div>
                            <p><span class="label bg-purple mrg5R">{nickname}</span></p>
                        </li>
                        <li class="loadUser">
                            <span class="notification-text">正在加载...</span>
                        </li>
                        <li class="notUser">
                            <div class="notification-text">无匹配项</div>
                        </li>
                    </ul>

                </div>
            </div>
        </div>
    </div>
    <div class="col-md-8">
        <div class="content-box bg-white">
            <h3 class="content-box-header ui-state-default">
                <div class="glyph-icon icon-separator transparent">
                    <i class="glyph-icon icon-comments"></i>
                </div>
                <span class="pad0L">聊天框</span>
            </h3>
            <div class="content-box-wrapper">
                <div class="scrollable-content grid-panel" id="messageScroll" tabindex="5004" style="overflow: hidden; outline: none;">

                    <ul class="chat-box" id="messagePager" style="width: 99%;">
                        <li style="text-align:center;" id="moreMessage"><a href="javascript:;" style="color:#2381E9; ">查看更多消息...</a></li>
                        <li  class="template" name="default">
                            <div class="chat-author">
                                <img data-src="holder.js/45x45/simple" class="imgHead"  width="36" />
                            </div>
                            <div class="popover left no-shadow">
                                <div class="arrow"><div class="arrow" style="border-left-color: #E9ECF1;top: 0px;right: -11px;"></div></div>
                                <div class="popover-content">
                                    {content}
                                </div>
                            </div>
                        </li>
                        <li class="float-left template" name="left">
                            <div class="chat-author">
                                <@s.img src="%{#adminUser.user.details.avatar.absolutePath}" ratio="45x45"/>
                            </div>
                            <div class="popover right no-shadow">
                                <div class="arrow"><div class="arrow" style="top: 0px;left: -12px;border-right-color: #E9ECF1;"></div></div>
                                <div class="popover-content">
                                    {content}
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="button-pane pad10A">
                <div class="form-row pad0B">
                    <div class="form-input col-lg-12">
                        <div class="input-append-wrapper input-append-right">
                            <a href="javascript:;" id="enterMessage" class="btn input-append primary-bg tooltip-button" title="" data-original-title="发送">
                                <i class="glyph-icon icon-mail-reply"></i>
                            </a>
                            <div class="append-right">
                                <input type="text" class="key13" data-id="enterMessage" placeholder="在这里说点什么" name="" id="content">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>
</@override>
<@extends name="../wrapper.ftl"/>