<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<script type="text/javascript">
$(function(){
    window.book = Fantasy.decode('${u:serialize(SPRING_SECURITY_CONTEXT.authentication.principal.data["contacts"])}');
    var getGroupId = function(){
        return groupslist.each(function(){
            if(this.target.hasClass('focus')){
                return this.getData().id+";";
            }
        });
    };
    //联系人分组列表
    window.groupslist = $("#groupslist").view().on('remove',function(){
        setTimeout(function(){$('#groupslist').sly('reload');},100);
        if(this.target.hasClass('focus')){
            groupslist.get(this.getIndex()-1).target.click();
        }
    }).on('add',function(data){
                var item = this;
                this.check = function(linkmans){
                    if(!linkmans){
                        linkmans = this.loadData();
                    }
                    if(linkmans <= 0)
                        return;
                    if(linkmans.length == linkmans.filter(function(){
                        return this.checked;
                    }).length){
                        item.target.find('.checkbox').addClass('checked');
                    }else{
                        item.target.find('.checkbox').removeClass('checked');
                    };
                };
                this.loadData = function(){
                    if(data.state=='forever'){
                        return book.linkmans;
                    }else{
                        var linkmans = [];
                        book.linkmans.each(function(){
                            if(this.groups.split(";").indexOf(data.id) > -1){
                                linkmans.push(this);
                            }
                        });
                        return linkmans;
                    }
                };
                this.target.find('.checkbox').click(function(e){
                    if(item.loadData().length == 0){
                        top.$.msgbox({
                            msg : "该分组下没有联系人!",
                            icon : "warning"
                        });
                        return stopDefault(e);
                    }
                    if($(this).hasClass('checked')){
                        $(this).removeClass('checked');
                    }else{
                        $(this).addClass('checked');
                    }
                    if(item.target.hasClass('focus')){//点击显示的分组
                        contactlist.each(function(){
                            this.target.find('.checkbox').each(function(){
                                if($(e.target).hasClass('checked') != $(this).hasClass('checked')){
                                    $(this).data('_event_source','group').click().removeData('_event_source');
                                }
                            });
                        });
                    }else{//点击隐藏的分组
                        item.loadData().each(function(){
                            this.checked = $(e.target).hasClass('checked');
                            window.changeEmail(this,this.checked);
                        });
                        groupslist.each(function(){
                            this.check();
                        });
                        contactlist.each(function(){
                            this.check();
                        });
                    }
                    return stopDefault(e);
                });
                if(item.getTemplateName() == 'default'){
                    if(data.state=='forever'){
                        this.target.css('border-radius','10px 10px 0px 0px');
                    }
                    this.target.click(function(){
                        if($(this).hasClass('focus') && data.state!='forever'){
                            groupslist.setTemplate(item.getIndex(),'update').target.addClass('focus');
                            return false;
                        }
                        groupslist.each(function(){
                            if(this.getTemplateName() != 'default'){
                                if(this.data.name != this.getData().name){
                                    var _item = this;
                                    $.post('${pageContext.request.contextPath}/admin/contacts/savegroup.do',_item.getData(),function(data){
                                        groupslist.setTemplate(_item.getIndex(),'default',data);
                                    });
                                }else{
                                    groupslist.setTemplate(this.getIndex(),'default',false);
                                }
                            }
                            this.target.removeClass('focus');
                        });
                        $('.contacts-title').html(data.name);
                        $(this).addClass('focus');
                    });
                }else{
                    this.target.click(function(){
                        groupslist.each(function(){
                            this.target.removeClass('focus');
                        });
                        $('.contacts-title').html(data.name);
                        $(this).addClass('focus');
                    });
                    this.target.find('textarea').selectionRange(data.name.length).bind('keydown',function(e){
                        if(e.keyCode==13){
                            $.post('${pageContext.request.contextPath}/admin/contacts/savegroup.do',item.getData(),function(data){
                                groupslist.setTemplate(item.getIndex(),'default',data).target.click();
                            });
                            return false;
                        }
                    });
                    this.target.find('.addDelete').click(function(){
                        $.dialog.confirm("是否删除联系人分组["+data.name+"]？",function(){
                            $.post('${pageContext.request.contextPath}/admin/contacts/deletegroup.do',{ids:[data.id]},function(data){
                                top.$.msgbox({
                                    msg : "删除成功!",
                                    icon : "success",
                                    callback:function(){
                                    }
                                });
                                groupslist.remove(item.getIndex());
                            });
                        });
                    }).switchStyle('mousedown','down');
                }
                if(this.getTemplateName() == 'update' && this.getIndex() == groupslist.size() - 1){
                    $('#groupslist').sly('reload').sly('toEnd');
                }
                this.target.click(function(){
                    contactlist.clear();
                    var linkmans = item.loadData();
                    contactlist.setJSON(linkmans);
                    if(linkmans && linkmans.length > 0){
                        contactlist.get(0).target.click();
                    }
                    item.check(linkmans);
                });
                item.check();
            });
    //联系人组添加
    $('#add_group').click(function(){
        if($(this).hasClass('sel')){
            $(this).removeClass('sel').find('.sc-button-label').html('修改');
        }
        groupslist.each(function(){
            if(this.getTemplateName() != 'default'){
                if(this.data.name != this.getData().name){
                    var _item = this;
                    $.post('${pageContext.request.contextPath}/admin/contacts/savegroup.do',_item.getData(),function(data){
                        groupslist.setTemplate(_item.getIndex(),'default',data);
                    });
                }else{
                    groupslist.setTemplate(this.getIndex(),'default',false);
                }
            }
            this.target.removeClass('focus');
        });
        var data = {name:'未命名分组-'+window.groupslist.size(),'book.id':book.id};
        $.post('${pageContext.request.contextPath}/admin/contacts/savegroup.do',data,function(data){
            var item = groupslist.add(data,'update');
            item.target.click();
        });
    });
    //联系人分组编辑
    $('#operateGroup').click(function(){
        if($(this).hasClass('sel')){
            $(this).removeClass('sel').find('.sc-button-label').html('修改');
            groupslist.each(function(){
                if(this.data.state!='forever' && this.getTemplateName() != 'default'){
                    if(this.data.name != this.getData().name){
                        var _item = this;
                        $.post('${pageContext.request.contextPath}/admin/contacts/savegroup.do',_item.getData(),function(data){
                            groupslist.setTemplate(_item.getIndex(),'default',data);
                        });
                    }else{
                        groupslist.setTemplate(this.getIndex(),'default',false);
                    }
                }
            });
        }else{
            $(this).addClass('sel').find('.sc-button-label').html('完成');
            groupslist.each(function(){
                if(this.data.state!='forever' && this.getTemplateName() != 'update'){
                    groupslist.setTemplate(this.getIndex(),'update');
                }
            });
        }
    });

    window.contactlist = $("#contactlist").view().on('remove',function(data){
        if(contactlist.size()>0){
            setTimeout(function(){$('#contactlist').sly('reload');},100);
        }
        book.linkmans.remove('id',data.id);
    }).on('add',function(data){
                var item = this;
                this.check = function(){//同步最新状态
                    var _index = book.linkmans.indexOf('id',data.id);
                    if(_index == -1){
                        throw new Error('没找到');
                    }
                    data =  book.linkmans[_index];
                    item.setData(data);
                    if(data.checked){
                        item.target.find('.checkbox').addClass('checked');
                    }else{
                        item.target.find('.checkbox').removeClass('checked');
                    }
                };
                this.target.find('.checkbox').click(function(e){
                    var _index = book.linkmans.indexOf('id',data.id);
                    if(_index == -1){
                        throw new Error('没找到');
                    }
                    data =  book.linkmans[_index];
                    if($(this).hasClass('checked')){
                        $(this).removeClass('checked');
                    }else{
                        $(this).addClass('checked');
                    }
                    data.checked = !data.checked;
                    item.setData(data);
                    if(!$(this).data('_event_source') || $(this).data('_event_source')!='group'){
                        groupslist.each(function(){
                            this.check();
                        });
                    }
                    return stopDefault(e);
                });
                if(data.checked){
                    this.target.find('.checkbox').addClass('checked');
                }else{
                    this.target.find('.checkbox').removeClass('checked');
                }
                var index = book.linkmans.indexOf('id',data.id);
                if(index!=-1){
                    book.linkmans[index] = data;
                }else{
                    book.linkmans.push(data);
                }
                this.target.bind('click',function(e){
                    if($(this).hasClass('focus')){
                        if($('#contactedit').is(":hidden")){
                            $('#switch_model').click();
                        }
                    }
                    contactlist.each(function(){
                        this.target.removeClass('focus');
                    });
                    $(this).addClass('focus');
                    $('#contactedit').template().applyData(data,{noitemtIterate:true});
                    stopDefault(e);
                });

                /*
                if(item.getTemplateName() == 'default'){
                    this.target.click(function(){
                        if($(this).hasClass('focus')){
                            contactlist.setTemplate(item.getIndex(),'update').target.addClass('focus');
                            return false;
                        }
                        contactlist.each(function(){
                            if(this.getTemplateName() != 'default'){
                                var re = new RegExp("\\<(\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+)\\>","i");
                                var _item = this;
                                var data = this.getData();
                                if(re.exec(data.name)){
                                    data.email = data.name.match(re)[1];
                                    data.name = data.name.replace(re);
                                }
                                if(this.data.name != data.name || this.data.email != data.email){
                                    $.post('/admin/contacts/savelinkman.do',data,function(data){
                                        contactlist.setTemplate(_item.getIndex(),'default',data);
                                    });
                                }else{
                                    contactlist.setTemplate(this.getIndex(),'default',false);
                                }
                            }
                            this.target.removeClass('focus');
                        });
                        $(this).addClass('focus');
                    }).find('.sc-label').html(data.name+'<'+data.email+'>');
                }else{
                    this.target.click(function(){
                        contactlist.each(function(){
                            this.target.removeClass('focus');
                        });
                        $(this).addClass('focus');
                    });
                    this.target.find('textarea').val(data.name+'<'+data.email+'>').selectionRange(data.name.length).bind('keydown',function(e){
                        data = item.getData();
                        if(e.keyCode==13){
                            var re = new RegExp("\\<(\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+)\\>","i");
                            if(re.exec(data.name)){
                                data.email = data.name.match(re)[1];
                                data.name = data.name.replace(re);
                            }
                            $.post('/admin/contacts/savelinkman.do',data,function(data){
                                contactlist.setTemplate(item.getIndex(),'default',data).target.click();
                            });
                            return false;
                        }
                    });
                    this.target.find('.addDelete').click(function(){
                        $.dialog.confirm("是否删除联系人["+data.name+"]？",function(){
                            $.post('/admin/contacts/deletelinkman.do',{ids:[data.id]},function(data){
                                top.$.msgbox({
                                    msg : "删除成功!",
                                    icon : "success",
                                    callback:function(){
                                    }
                                });
                                contactlist.remove(item.getIndex());
                            });
                        });
                    });
                }*/
                if(this.getTemplateName() == 'update' && this.getIndex() == contactlist.size() - 1){
                    $('#contactlist').sly('reload').sly('toEnd');
                }
            });
    $('#add_contact').click(function(){

        /*
        contactlist.each(function(){
            if(this.getTemplateName() != 'default'){
                var re = new RegExp("\\<(\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+)\\>","i");
                var _item = this;
                var data = this.getData();
                if(re.exec(data.name)){
                    data.email = data.name.match(re)[1];
                    data.name = data.name.replace(re);
                }
                if(this.data.name != data.name){
                    $.post('/admin/contacts/savelinkman.do',data,function(data){
                        contactlist.setTemplate(_item.getIndex(),'default',data);
                    });
                }else{
                    contactlist.setTemplate(this.getIndex(),'default',false);
                }
            }
            this.target.removeClass('focus');
        });
        var data = {name:'未命名联系人'+window.contactlist.size(),'email':"xxxxx@xxxx.com",'book.id':book.id,'group':getGroupId()};
        $.post('/admin/contacts/savelinkman.do',data,function(data){
            var item = contactlist.add(data,'update');
            item.target.click();
            if(!initContactScrollBar){
                $('#contactlist').sly( options ).sly('toStart');
            }
        });*/
    });

    //添加默认所有联系人

    $('#switch_model').bind('click',function(){
        if($(this).parent().hasClass('contact-view')){
            $(this).attr('title','显示联系人详细信息').parent().removeClass('contact-view');
            $('#contactedit').hide();
            $('#group_border').show().next().show();
            $('#contactlist').width($('#contactlist').data('width'));
            $('#contact_list_div').removeClass('viewmodel');
            $('.contacts-tfoot').removeClass('moveLeft');
        }else{
            $('#contact_list_div').addClass('viewmodel');
            $('#group_border').hide().next().hide();//$('#group_list_div').hide();
            $(this).attr('title','显示群组').parent().addClass('contact-view');
            $('.contacts-tfoot').addClass('moveLeft');
            $('#contactedit').show();
        }
    }).switchStyle('mousedown','down');

    window.initScrollBar = function(){
        //联系人分组滚动条
        var options = $('#groupslist').data("options");
        options = $.extend({},options,{
            scrollBar: $("#groupslist_scrollbar")
        });
        try{$('#groupslist').sly( options ).sly('toStart');}catch(e){}
        //联系人列表滚动条
        options = $('#contactlist').data("options");
        options = $.extend({},options,{
            scrollBar: $("#contactlist_scrollbar")
        });

        var initContactScrollBar = false;
        if(book.linkmans && book.linkmans.length > 0){
            initContactScrollBar = true;
            try{$('#contactlist').sly( options ).sly('toStart');}catch(e){}
        }
    };

});
</script>
</@override>
<@override name="container">
<div class="contacts" style="position:relative;left:0px;">
    <div class="title-groups">
        <div class="group-title">组别</div>
        <div class="contacts-title">所有联系人</div>
        <div id="switch_model" class="img contact" title="显示联系人详细信息"></div>
    </div>
    <div style="display:block;height:50px;"></div>
    <div id="group_border" class="source-list-border" style="cursor: default;height: 385px;left: 27px;position: absolute;width: 222px;z-index: 0;"></div>
    <div id="group_list_div" class="slyWrap vertical-r">
        <div id="groupslist_scrollbar" class="scrollbar" style="height:384px;">
            <div class="handle"></div>
        </div>
        <div id="groupslist" style="width:250px;height:385px;margin-top: 1px;" class="groups-list sly" data-options='{ "itemNav": "smart", "dragContent": 1, "startAt": 10, "scrollBy": 1, "elasticBounds": 1 }'>
            <div style="width:250px;">
                <div class="item template" name="default">
                    <label class="sc-label">{name}</label>
                    <div class="checkbox"></div>
                </div>
                <div class="item update template" name="update">
                    <textarea rows="1" cols="1" class="view-field" name="name"></textarea>
                    <div class="addDelete delete-icon"></div>
                    <div class="checkbox"></div>
                </div>
            </div>
        </div>
    </div>
    <div id="contact_list_div" class="slyWrap vertical-r">
        <div id="contactlist_scrollbar" class="scrollbar" style="height:384px;margin-right:-44px;">
            <div class="handle"></div>
        </div>
        <div id="contactlist" class="contact-list source-list-border" data-options='{ "itemNav": "smart", "dragContent": 1, "startAt": 10, "scrollBy": 1, "elasticBounds": 1 }'>
            <div style="width:350px;">
                <div class="item template" name="default">
                    <label class="sc-label">{name}</label>
                    <div class="checkbox"></div>
                </div>
            </div>
        </div>
    </div>
    <div id="contactedit" class="slyWrap vertical-r" style="padding-left:19px;display:none;">
        <div style="width:350px;height:385px;" class="contact-list source-list-border">
            <div style="width:344px;padding:8px;">
                <table class="formTable" id="contacteditview" style="width:335px;">
                    <tbody>
                    <tr>
                        <td style="width:85px;height:18px;" class="formItem_title">姓名 :</td>
                        <td class="formItem_content"><#=$data.name#></td>
                        <td style="width:3px;" class="formItem_content"></td>
                    </tr>
                    <tr>
                        <td style="height:18px;" class="formItem_title w100">性别 :</td>
                        <td class="formItem_content w"><#=Fantasy.util.Format.sex($data.sex)#></td>
                        <td class="formItem_content"></td>
                    </tr>
                    <tr>
                        <td style="height:18px;" class="formItem_title ">电话:</td>
                        <td class="formItem_content"><#=$data.mobile#></td>
                        <td class="formItem_content"></td>
                    </tr>
                    <tr>
                        <td style="height:18px;" class="formItem_title ">E-mail:</td>
                        <td class="formItem_content"><#=$data.email#></td>
                        <td class="formItem_content"></td>
                    </tr>
                    <tr>
                        <td style="height:18px;" class="formItem_title ">部门 :</td>
                        <td class="formItem_content"><#=$data.department#></td>
                        <td class="formItem_content"></td>
                    </tr>
                    <tr>
                        <td style="height:18px;" class="formItem_title ">职务 :</td>
                        <td class="formItem_content"><#=$data.job#></td>
                        <td class="formItem_content"></td>
                    </tr>
                    <tr>
                        <td class="formItem_title" style="vertical-align:top;">备注 :</td>
                        <td class="formItem_content" style="height:150px;vertical-align:top;"><#=$data.description#></td>
                        <td class="formItem_content"></td>
                    </tr>
                    <tr>
                        <td style="height:48px;" class="formItem_title"></td>
                        <td class="formItem_content">
                        <td class="formItem_content"></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="contacts-tfoot">
        <div id="operateGroup" class="atv3 square sc-view sc-button-view button sc-regular-size" style="left:25px;width:56px;bottom:15px;height:26px;min-width:64px" title="">
            <div class="left"></div>
            <div class="middle"></div>
            <div class="right"></div>
            <label class="sc-button-label sc-regular-size ellipsis">修改</label>
        </div>
        <div id="add_group" style="left: 216px; width: 29px; bottom: 15px; height: 26px;min-width:0px;" class="atv3 square sc-view sc-button-view add-button button sc-regular-size icon" title="新建组别">
            <div class="left"></div>
            <div class="middle"></div>
            <div class="right"></div>
            <label class="sc-button-label sc-regular-size icon">
                <img class="icon add-icon" src="data:image/gif;base64,R0lGODlhAQABAJAAAP///wAAACH5BAUQAAAALAAAAAABAAEAAAICBAEAOw==">
            </label>
        </div>
        <div id="add_contact" style="left:590px;width:29px;bottom:15px;height:26px;min-width:0px;display:none;" class="atv3 square sc-view sc-button-view add-button button sc-regular-size icon" title="新建联系人">
            <div class="left"></div>
            <div class="middle"></div>
            <div class="right"></div>
            <label class="sc-button-label sc-regular-size icon">
                <img class="icon add-icon" src="data:image/gif;base64,R0lGODlhAQABAJAAAP///wAAACH5BAUQAAAALAAAAAABAAEAAAICBAEAOw==">
            </label>
        </div>
        <div class="atv3 square sc-view sc-button-view button sc-regular-size operate-contact" style="left:561px;width:56px;bottom:15px;height:26px;min-width:64px;display:none;" title="">
            <div class="left"></div>
            <div class="middle"></div>
            <div class="right"></div>
            <label class="sc-button-label sc-regular-size ellipsis">修改</label>
        </div>
    </div>
</div>
</@override>
<@extends name="../base.ftl"/>