<@override name="pageTitle">
    <@s.text name="security.menu.title"/>
    <small>
        <@s.text name="security.menu.description"/>
    </small>
</@override>
<@override name="head">
<script type="text/javascript">
    $(function(){
        //当浏览器窗口发生变化时,自动调整布局的js代码
        var _top =  $('#layout_left').offset().top + 15;
        var _$layout = $('#layout_left,#menuDetails');
        var _resize = function () {
            _$layout.css('minHeight',$(window).height() - _top);
        };
        $(window).resize(_resize);
        $page$.on('destroy',function(){
            $(window).unbind('resize',_resize);
        });
        //修改数据
        var menuFilter = function(data) {
            data._icon = data.icon;
            data._url = data.url;
            delete data.icon;
            delete data.url;
            if(!data.children){
                delete data.children;
            }
            if(data.type == 'menu' || data.layer == 1){
                data.isParent = true;
            }
            if(!data.parentId){
                data.open = true;
            }
            return data;
        };
        window.menus = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(menuTree)" escapeHtml="false"/>;
        menus.each(function(){
            menuFilter(this);
        });
        var menuTypes = [{key:"url",value:"链接"},{key:"menu",value:"菜单"},{key:"javascript",value:"脚本"},{key:"other",value:"其他"}];
        Fantasy.apply(Fantasy.util.Format, {}, {
            menuType : function(key) {
                return menuTypes.each(function(){
                    if(this.key == key){
                        return this.value;
                    }
                });
            }
        });
        $('#menuType').select3({value:'key',text:'value'}).load(menuTypes);
        window.menuDetails = $('#menuDetails').form();
        window.menuDetails.on('change',function(data,templateName,element){
            if(templateName == 'edit'){
                if(!data['id']){
                    $('.menu-view',element.target).hide();
                    $('.menu-save .button-content label',element.target).html('保存菜单');
                }
                var _$icon = $('[name=icon]',element.target),_$popoverTarget = _$icon.parent().prev(),_oldIconVal = _$icon.val();
                _$popoverTarget.on('shown.bs.popover',function(){
                    var popover = $(this).data('bs.popover');
                    var _$allIcons = $('.scrollable-content a',popover.tip()).filter(function(){
                        return $(this).find('i.glyph-icon').length;
                    }).click(function(e){
                        e.preventDefault();
                        _$popoverTarget.find('i.glyph-icon').removeClass(_oldIconVal).addClass($(this).attr('title'));
                        _$icon.val($(this).attr('title'));
                        popover.toggle();
                    });
                    $('#search',popover.tip()).on('change',function(){
                        var _keyword = $(this).val();
                        _$allIcons.show().filter(function(){
                            return $(this).attr('title').indexOf(_keyword)==-1;
                        }).hide();
                    })
                });
                var $saveForm = $(".saveForm",element.target).ajaxForm(function(data){
                    data = menuFilter(data);
                    var node = menuTree.getNodeByParam("id", data.id);
                    if(!!node){
                        menuTree.updateNode(Fantasy.copy(node,data),false);
                    }else{
                        menuTree.addNodes(menuTree.getSelectedNodes()[0], data);
                        node = menuTree.getNodeByParam("id", data.id);
                    }
                    $('#'+node.tId + '_span').click();
                    $.msgbox({
                        msg : '菜单 <b>'+data.name+'</b> 保存成功',
                        type : 'success'
                    });
                });
                $('.menu-save',element.target).click(function(e){
                    $saveForm.submit();
                    stopDefault(e);
                });
            }
        });
        window.menuTree = $('#menuTree').zTree({
            data:{
                key : {
                    name: "name"
                },
                simpleData: {
                    enable: true,
                    idKey: "id",
                    pIdKey: "parentId"
                }
            },edit: {
                drag:{
                    isMove:true
                },
                enable:true,
                showAddBtn:false,
                showRemoveBtn:false,
                showRenameBtn:false
            } ,callback:{
                onClick : function (e, treeId, treeNode) {
                    menuDetails.update(treeNode,'view');
                    return false;
                },
                beforeDrop:function(treeId, treeNodes){
                    if(!treeNodes || treeNodes.length==0){
                        return false;
                    }
                    return confirm('是否确定移动['+treeNodes[0].name+']菜单');
                },
                onDrop:function(event, treeId, treeNodes, targetNode, moveType){
                    if(!treeNodes || treeNodes.length==0){
                        return false;
                    }
                    var index = 0;
                    var parentId = treeNodes[0].parentId;
                    if(!!targetNode){
                        index = $('#'+targetNode.tId).index()+1;
                        switch(moveType){
                            case 'prev':
                                index--;
                                break;
                            case 'inner':
                                index = !targetNode.children?1:targetNode.children.length;
                                parentId = targetNode.id;
                                break;
                            case 'next':
                                index++;
                                break;
                        }
                    }else if(moveType == 'inner'){
                        index = $('#'+treeNodes[0].tId).index()+1;
                    }
                    if(index > 0){
                        $.post('<@s.url namespace="/security/menu" action="save"/>',{id:treeNodes[0].id,sort:index,'parent.id':parentId},function(){
                            $.msgbox({
                                msg : "顺序调整成功",
                                type : "success"
                            });
                        });
                    }
                    return stopDefault(event);
                }
            }});
        //添加菜单
        $('.menu-add').bind('click',function(e){
            e.preventDefault();
            var selectdNodes = menuTree.getSelectedNodes();
            if(selectdNodes.length == 1){
                var treeNode = selectdNodes[0];
                menuDetails.update({'parentId':treeNode.id,'_icon':'icon-chevron-right','type':'menu'},'edit');
            }else{
                $.msgbox({
                    msg:"请先选择要添加菜单的上级菜单",
                    type:"warning"
                });
            }
        });
        $('.menu-add-root').click(function(e){
            e.preventDefault();
            menuTree.cancelSelectedNode();
            menuDetails.update({'_icon':'icon-chevron-right','type':'menu'},'edit');
        });
        //修改菜单
        $('.menu-update').bind('click',function(e){
            e.preventDefault();
            var selectdNodes = menuTree.getSelectedNodes();
            if(selectdNodes.length != 1){
                $.msgbox({
                    msg:"请先选择你要修改的菜单",
                    type:"warning"
                });
                return stopDefault(e);
            }
            menuDetails.setTemplate('edit');
        });
        //删除菜单
        $('.menu-remove').click(function(e){
            var selectdNodes = menuTree.getSelectedNodes();
            if(selectdNodes.length == 0){
                $.msgbox({
                    msg:"请先选择要删除的菜单",
                    type:"warning"
                });
                return stopDefault(e);
            }
            var url = $(this).attr('href'),treeNode = selectdNodes[0];
            jQuery.dialog.confirm('<h4 class="infobox-title">删除确认</h4><p>确认删除菜单:<b>'+treeNode.name+'</b>?</p>',function(){
                $.post(url+'?id='+treeNode.id,function(){
                    var onode = treeNode.getParentNode();
                    $.msgbox({
                        msg : '菜单 <b>'+treeNode.name+'</b> 删除成功',
                        type : 'success'
                    });
                    onode = onode ? onode : treeNode.getNextNode();
                    onode = onode ? onode : treeNode.getPreNode();
                    if(onode){
                        $('#'+onode.tId + '_span').click();
                    }
                    menuTree.removeNode(treeNode);
                });
            });
            return stopDefault(e);
        });
        menuTree.setJSON(menus);
    });
</script>
</@override>
<@override name="pageContent">
    <div class="col-md-4 pad0L">
        <div class="content-box pad5A mrg0B pad0B" id="layout_left">
            <a title="<@s.text name="security.menu.button.add"/>" class="btn medium primary-bg menu-add" href="javascript:;">
                <span class="button-content"><i class="glyph-icon icon-plus float-left"></i><@s.text name="security.menu.button.add"/></span>
            </a>
            <a title="<@s.text name="security.menu.button.edit"/>" class="btn medium primary-bg menu-update" href="javascript:;">
                <span class="button-content"><i class="glyph-icon icon-edit float-left"></i><@s.text name="security.menu.button.edit"/></span>
            </a>
            <a title="<@s.text name="security.menu.button.delete"/>" class="btn medium primary-bg menu-remove" href="<@s.url namespace="/security/menu" action="delete"/>">
                <span class="button-content"><i class="glyph-icon icon-remove float-left"></i><@s.text name="security.menu.button.delete"/></span>
            </a>
            <a title="<@s.text name="security.menu.button.add.root"/>" class="btn medium primary-bg menu-add-root" href="<@s.url namespace="/security/menu" action="delete"/>">
                <span class="button-content"><i class="glyph-icon icon-folder-open float-left"></i> <@s.text name="security.menu.button.add.root"/></span>
            </a>
            <div class="divider mrg5B"></div>
            <ul id="menuTree" class="ztree"> </ul>
        </div>
    </div>
    <div class="col-md-8 pad0L pad0R">
        <div class="content-box pad5A mrg0B pad0B" id="menuDetails">
                <div class="template mrg5A" name="default">
                    <h3>
                        What is Fides Admin
                    </h3>
                    <p class="font-gray-dark">
                        The AUI Framework is a HTML/CSS/JS framework that helps you quickly build modern, beautiful and accessible user interfaces for interactive web applications. It is powered by jQuery and includes a lot of UI interactions, effects, widgets, elements, components and themes. These make AUI a very powerful companion when it comes to solving all kinds of use cases. Read through the documentation to get an idea of how many widgets and elements are available.
                    </p>
                </div>
                <div class="template mrg5A" name="view">
                    <div class="infobox warning-bg mrg10B">
                        <p><i class="glyph-icon icon-exclamation mrg10R"></i>To view the available grid system options &amp; configurations you can visit the <a title="Fides Admin Grid System documentation" target="_blank" href="grid.html">Fides Admin Grid System documentation</a> page.</p>
                    </div>
                    <div class="form-bordered">
                    <div class="form-row">
                        <div class="form-label col-md-2">
                            <label for="">
                                <@s.text name="security.menu.view.name"/>
                            </label>
                        </div>
                        <div class="form-input col-md-10">
                            <input name="name" disabled="disabled" class="view-field"/>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-label col-md-2">
                            <label for="">
                                <@s.text name="security.menu.view.icon"/>
                            </label>
                        </div>
                        <div class="form-input col-md-10">
                            <div class="input-append-wrapper">
                                <div class="input-append ui-state-default">
                                    <i class="glyph-icon {_icon}"></i>
                                </div>
                                <div class="append-left">
                                    <input name="icon" disabled="disabled" class="view-field" mapping="_icon"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-label col-md-2">
                            <label for="">
                                <@s.text name="security.menu.view.type"/>
                            </label>
                        </div>
                        <div class="form-input col-md-10">
                            <select class="chosen-select" disabled="disabled" data-placeholder="未选择菜单类型">
                                <option>{type:menuType()}</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-row pad0B">
                        <div class="form-label col-md-2">
                            <label for="">
                                <@s.text name="security.menu.view.value"/>
                            </label>
                        </div>
                        <div class="form-input col-md-10">
                            <input name="value" disabled="disabled" class="view-field"/>
                        </div>
                    </div>
                    <div class="form-row pad0B">
                        <div class="form-label col-md-2">
                            <label for="">
                                <@s.text name="security.menu.view.description"/>
                            </label>
                        </div>
                        <div class="form-input col-md-10">
                            <input name="description" disabled="disabled" class="view-field"/>
                        </div>
                    </div>
                    <div class="form-row pad0B">
                        <div class="form-input col-md-10 col-md-offset-2">
                            <a title=" <@s.text name="security.menu.view.button.edit"/>" class="btn medium primary-bg radius-all-4 switch" template="edit" href="javascript:;">
                                <span class="button-content"><i class="glyph-icon icon-edit float-left"></i> <@s.text name="security.menu.view.button.edit"/></span>
                            </a>
                            <a title=" <@s.text name="security.menu.view.button.prmot"/>" class="btn medium bg-gray radius-all-4 hover-white switch" template="default" href="javascript:;">
                                <span class="button-content"><i class="glyph-icon icon-exclamation font-gray-dark float-left"></i>
                                    <@s.text name="security.menu.view.button.prmot"/>
                                </span>
                            </a>
                        </div>
                    </div>
                    </div>
                </div>
                <div class="template mrg5A" name="edit">
                    <div class="infobox warning-bg mrg10B">
                        <p><i class="glyph-icon icon-exclamation mrg10R"></i>To view the available grid system options &amp; configurations you can visit the <a title="Fides Admin Grid System documentation" target="_blank" href="grid.html">Fides Admin Grid System documentation</a> page.</p>
                    </div>
                    <@s.form namespace="/security/menu" action="save" cssClass="form-bordered saveForm" method="post">
                        <input type="hidden" class="view-field" name="parent.id" mapping="parentId"/>
                        <input type="hidden" class="view-field" name="id"/>
                        <div class="form-row">
                            <div class="form-label col-md-2">
                                <label for="">
                                    <@s.text name="security.menu.edit.name"/>
                                </label>
                            </div>
                            <div class="form-input col-md-10">
                                <input type="text" class="view-field" name="name"/>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-2">
                                <label for="">
                                    <@s.text name="security.menu.edit.icon"/>
                                </label>
                            </div>
                            <div class="form-input col-md-10">
                                <div class="input-append-wrapper">
                                    <a class="btn input-append ui-state-default popover-button" data-placement="bottom" title="请选择菜单的图标" data-id="#notif-box-example">
                                        <i class="glyph-icon {_icon}"></i>
                                    </a>
                                    <div class="append-left">
                                        <input type="text" name="icon" class="view-field"  mapping="_icon"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-2">
                                <label for="">
                                    <@s.text name="security.menu.edit.type"/>
                                </label>
                            </div>
                            <div class="form-input col-md-10">
                                <select id="menuType" name="type" class="view-field chosen-select" data-placeholder="请选择菜单类型!"></select>
                            </div>
                        </div>
                        <div class="form-row pad0B">
                            <div class="form-label col-md-2">
                                <label for="">
                                    <@s.text name="security.menu.edit.value"/>
                                </label>
                            </div>
                            <div class="form-input col-md-10">
                                <input name="value" class="view-field" />
                            </div>
                        </div>
                        <div class="form-row pad0B">
                            <div class="form-label col-md-2">
                                <label for="">
                                    <@s.text name="security.menu.edit.description"/>
                                </label>
                            </div>
                            <div class="form-input col-md-10">
                                <input name="description" class="view-field small-textarea"/>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-input col-md-10 col-md-offset-2">
                                <a title="<@s.text name="security.menu.edit.button.return"/>" class="btn medium primary-bg radius-all-4 switch menu-view" href="javascript:;" template="view">
                                    <span class="button-content">
                                        <i class="glyph-icon icon-reply float-left"></i>
                                        <@s.text name="security.menu.edit.button.return"/>
                                    </span>
                                </a>
                                <a title="<@s.text name="security.menu.edit.button.save"/>" class="btn medium primary-bg radius-all-4 menu-save" href="javascript:;">
                                    <span class="button-content">
                                        <i class="glyph-icon icon-save float-left"></i>
                                        <label><@s.text name="security.menu.edit.button.save"/></label>
                                    </span>
                                </a>
                                <a title="<@s.text name="security.menu.edit.button.prmot"/>" class="btn medium bg-gray radius-all-4 hover-white switch" template="default" href="javascript:;">
                                    <span class="button-content">
                                        <i class="glyph-icon icon-exclamation font-gray-dark float-left"></i>
                                        <@s.text name="security.menu.edit.button.prmot"/>
                                    </span>
                                </a>
                            </div>
                        </div>
                    </@s.form>
                </div>
        </div>
        <div id="notif-box-example" class="hide medium-box">
            <div class="popover-title display-block clearfix form-row pad10A">
                <div class="form-input">
                    <div class="form-input-icon">
                        <i class="glyph-icon icon-search transparent"></i>
                        <input type="text" placeholder="输入查询的关键字" id="search" class="radius-all-100" />
                    </div>
                </div>
            </div>
            <div class="scrollable-content scrollable-small pad15A small-box">
                <#include "../icons.ftl"/>
            </div>
            <div class="pad10A button-pane button-pane-alt text-center">
                <a href="javascript:;" class="btn medium primary-bg">
                    <span class="button-content">View all notifications</span>
                </a>
            </div>
        </div>

    </div>
</@override>
<@extends name="../wrapper.ftl"/>