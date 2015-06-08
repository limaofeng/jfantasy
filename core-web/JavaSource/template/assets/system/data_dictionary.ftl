<@override name="pageTitle">
    <@s.text name="system.dictionary.title"/>
    <small>
        <@s.text name="system.dictionary.description"/>
    </small>
</@override>
<@override name="head">
<script type="text/javascript">

    $(function(){
        //当浏览器窗口发生变化时,自动调整布局的js代码
        var _top = $('#layout_left').offset().top + 15,_$layout =  $('#layout_left,#layout_right'),_$layout_r = $('#layout_right');
        var _resize = function () {
            _$layout.css('minHeight', $(window).height() - _top);
            _$layout_r.triggerHandler('resize');
        };
        $(window).resize(_resize);
        $page$.one('destroy',function(){
            $(window).unbind('resize',_resize);
        });
        //修改数据
        var typeFilter = function(data) {
            if(!data.children){
                delete data.children;
            }
            if(data.code == 'root' || data.layer == 1){
                data.isParent = true;
            }
            if(!data.parentId){
                data.open = true;
            }
            return data;
        };
        window.types = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(types)" escapeHtml="false"/>;
        types.each(function(){
            typeFilter(this);
        });
        Fantasy.apply(Fantasy.util.Format, {}, {
            typeName : function(key) {
                var node = ddTypeTree.getNodeByParam('code',key);
                return !node ? key : node.name;
            }
        });
        window.ddtDetails = $('#ddtDetails').form();
        window.ddtDetails.on('change',function(data,templateName,element){
            if(templateName == 'edit'){
                if(!data['code']){
                    $('.ddt-save .button-content label',element.target).html('保存分类');
                }else{
                    $('[name="code"]',element.target).prop('readonly','readonly');
                }
                var $saveForm = $(".saveForm",element.target).ajaxForm(function(data){
                    data = typeFilter(data);
                    var node = ddTypeTree.getNodeByParam("code", data.code);
                    if(!!node){
                        ddTypeTree.updateNode(Fantasy.copy(node,data),false);
                    }else{
                        ddTypeTree.addNodes(ddTypeTree.getSelectedNodes()[0], data);
                        node = ddTypeTree.getNodeByParam("code", data.code);
                    }
                    $('#'+node.tId + '_span').click();
                    $.msgbox({
                        msg : '数据字典分类 <b>'+data.name+'</b> 保存成功',
                        type : 'success'
                    });
                });
                $('.ddt-save',element.target).click(function(e){
                    $saveForm.submit();
                    stopDefault(e);
                });
            }
        });
        window.ddTypeTree = $('#ddTypeTree').zTree({
            data:{
                key : {
                    name: "name"
                },
                simpleData: {
                    enable: true,
                    idKey: "code",
                    pIdKey: "parentCode"
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
                    $('#ddlist').show().siblings().hide();
                    $advsearch.reset();
                    $('[name="EQS_type"]',$('#searchForm')).val(treeNode.code);
                    $('#searchForm').submit();
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
                        $.post('<@s.url namespace="/system/ddt" action="save"/>',{id:treeNodes[0].id,sort:index,'parent.id':parentId},function(){
                            $.msgbox({
                                msg : "顺序调整成功",
                                type : "success"
                            });
                        });
                    }
                    return stopDefault(event);
                }
            }}, types);
        //添加数据字典分类
        $('.ddType-add').bind('click',function(e){
            e.preventDefault();
            var selectdNodes = ddTypeTree.getSelectedNodes();
            if(selectdNodes.length == 1){
                var treeNode = selectdNodes[0];
                $('#ddtDetails').show().nextAll().hide();
                ddtDetails.update({"parentCode":treeNode.code},'edit');
            }else{
                $.msgbox({
                    msg:"请先选择要添加菜单的上级菜单",
                    type:"warning"
                });
            }
        });
        //修改数据字典分类
        $('.ddType-update').bind('click',function(e){
            e.preventDefault();
            var selectdNodes = ddTypeTree.getSelectedNodes();
            if(selectdNodes.length != 1){
                $.msgbox({
                    msg:"请先选择你要修改的菜单",
                    type:"warning"
                });
                return stopDefault(e);
            }
            $('#ddtDetails').show().nextAll().hide();
            ddtDetails.update(selectdNodes[0],'edit');
        });
        //删除数据字典分类
        $('.ddType-remove').click(function(e){
            var selectdNodes = ddTypeTree.getSelectedNodes();
            if(selectdNodes.length == 0){
                $.msgbox({
                    msg:"请先选择要删除的数据字典分类",
                    type:"warning"
                });
                return stopDefault(e);
            }
            var url = $(this).attr('href'),treeNode = selectdNodes[0];
            jQuery.dialog.confirm('<h4 class="infobox-title">确认删除分类</h4><p>分类名称:<b>'+treeNode.name+'</b><br/>分类删除后,对应的数据字典也会一并删除</p>',function(){
                $.post(url+'?codes='+treeNode.code,function(){
                    var onode = treeNode.getParentNode();
                    $.msgbox({
                        msg : '数据字典分类 <b>'+treeNode.name+'</b> 删除成功',
                        type : 'success'
                    });
                    onode = onode ? onode : treeNode.getNextNode();
                    onode = onode ? onode : treeNode.getPreNode();
                    if(onode){
                        $('#'+onode.tId + '_span').click();
                    }
                    ddTypeTree.removeNode(treeNode);
                });
            });
            return stopDefault(e);
        });
        var $advsearch = $('.propertyFilter').advsearch({
            filters : [{
                name : 'S_code',
                text : '编码',
                type : 'input',
                matchType :['EQ','LIKE','LT','GT']
            },{
                name : 'S_name',
                text : '名称',
                type : 'input',
                matchType :['EQ','LIKE','GT']
            }]
        });
        //列表初始化
        var pager=<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(pager)" escapeHtml="false"/>;
        var $grid = $('#view').dataGrid($('#searchFormPanel'),$('#batchDiv'));
        $grid.data('grid').view().on('add',function(data){
            var _$actions = $('.actions',this.target);
            $('.delete',_$actions).click(function(){
                deleteMethod([data.key]);
                return false;
            });
            $('.edit',_$actions).click(function(){
                ddDetails.update(data);
                $('#ddDetails').show().prevAll().hide();
                return false;
            });
        }).on('move',function(type,t,o,invoke){
            //TODO 数据库顺序调整后，调用该方法
            invoke();
            return false;
        });
        var deleteMethod = $('.batchDelete').batchExecute($("#allChecked"),$grid.data('grid').pager(),'key','<h4 class="infobox-title">删除确认</h4><p>确认删除数据字典项[<b>{name}</b>]？</p>',function(){
            $.msgbox({
                msg : "删除成功!",
                type : "success"
            });
        });
        window.ddDetails = $('#ddDetails').form();
        window.ddDetails.on('change',function(data,templateName,element){
            if(!data['code']){
                $('.dd-save .button-content label',element.target).html('保存数据字典');
            }else{
                $('[name="code"]',element.target).prop('readonly','readonly');
            }
            var $saveForm = $(".saveForm",element.target).ajaxForm(function(data){
                $grid.data('grid').pager().reload();
                $('#ddlist').show().siblings().hide();
                $.msgbox({
                    msg : '数据字典 <b>'+data.name+'</b> 保存成功',
                    type : 'success'
                });
            });
            $('.to-list',element.target).click(function(e){
                e.preventDefault();
                $('#ddlist').show().siblings().hide();
            });
            $('.opt-doc',element.target).click(function(e){
                e.preventDefault();
                $('#ddtDetails').show().nextAll().hide();
                ddtDetails.setTemplate('default');
            });
            $('.dd-save',element.target).click(function(e){
                e.preventDefault();
                $saveForm.submit();
            });
        });
        $('.dd-add').click(function(e){
            e.preventDefault();
            $('#ddDetails').show().prevAll().hide();
            ddDetails.update({'type':$('[name="EQS_type"]',$('#searchForm')).val()});
        });
        $('#ddtDetails').show().siblings().hide();
    });
</script>
</@override>
<@override name="pageContent">
    <div class="col-md-3 pad0L">
        <div class="content-box pad5A mrg0B pad0B" id="layout_left">
            <a title="<@s.text name="system.dictionary.button.category.add"/>" class="btn medium primary-bg ddType-add" href="javascript:;">
                <span class="button-content">
                    <i class="glyph-icon icon-plus float-left"></i>
                    <@s.text name="system.dictionary.button.category.add"/>
                </span>
            </a>
            <a title="<@s.text name="system.dictionary.button.category.edit"/>" class="btn medium primary-bg ddType-update" href="javascript:;">
                <span class="button-content">
                    <i class="glyph-icon icon-edit float-left"></i>
                    <@s.text name="system.dictionary.button.category.edit"/>
                </span>
            </a>
            <a title="<@s.text name="system.dictionary.button.category.delete"/>" class="btn medium primary-bg ddType-remove" href="<@s.url namespace="/system/ddt" action="delete"/>">
                <span class="button-content">
                    <i class="glyph-icon icon-remove float-left"></i>
                    <@s.text name="system.dictionary.button.category.delete"/>
                </span>
            </a>

            <div class="divider mrg5B"></div>
            <ul id="ddTypeTree" class="ztree"></ul>
        </div>
    </div>
    <div class="col-md-9 pad0L pad0R">
        <div class="content-box pad5A mrg0B pad0B grid-panel" id="layout_right">
        <div id="ddtDetails">
            <div class="template mrg5A" name="default">
                <h3>
                    What is Fides Admin
                </h3>
                <p class="font-gray-dark">
                    The AUI Framework is a HTML/CSS/JS framework that helps you quickly build modern, beautiful and accessible user interfaces for interactive web applications. It is powered by jQuery and includes a lot of UI interactions, effects, widgets, elements, components and themes. These make AUI a very powerful companion when it comes to solving all kinds of use cases. Read through the documentation to get an idea of how many widgets and elements are available.
                </p>
            </div>
            <div class="template mrg5A" name="edit">
                <div class="infobox warning-bg mrg10B">
                    <p><i class="glyph-icon icon-exclamation mrg10R"></i> <@s.text name="system.dictionary.add.prompt"/></p>
                </div>
                <@s.form namespace="/system/ddt" action="save" cssClass="form-bordered saveForm" method="post">
                    <input type="hidden" class="view-field" name="parent.code" mapping="parentCode"/>
                    <div class="form-row">
                        <div class="form-label col-md-2">
                            <label for="">
                                <@s.text name="system.dictionary.children.code"/>
                            </label>
                        </div>
                        <div class="form-input col-md-10">
                            <input type="text" class="view-field" name="code"/>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-label col-md-2">
                            <label for="">
                                <@s.text name="system.dictionary.children.name"/>
                            </label>
                        </div>
                        <div class="form-input col-md-10">
                            <input type="text" class="view-field" name="name"/>
                        </div>
                    </div>
                    <div class="form-row pad0B">
                        <div class="form-label col-md-2">
                            <label for="">
                                <@s.text name="system.dictionary.children.description"/>
                            </label>
                        </div>
                        <div class="form-input col-md-10">
                            <input name="description" class="view-field small-textarea"/>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-input col-md-10 col-md-offset-2">
                            <a title=" <@s.text name="system.dictionary.children.button.save"/>" class="btn medium primary-bg radius-all-4 ddt-save" href="javascript:;">
                                <span class="button-content">
                                    <i class="glyph-icon icon-save float-left"></i>
                                    <label> <@s.text name="system.dictionary.children.button.save"/></label>
                                </span>
                            </a>
                            <a title=" <@s.text name="system.dictionary.children.button.prompt"/>" class="btn medium bg-gray radius-all-4 hover-white switch" template="default" href="javascript:;">
                                <span class="button-content">
                                    <i class="glyph-icon icon-exclamation font-gray-dark float-left"></i>
                                    <@s.text name="system.dictionary.children.button.prompt"/>
                                </span>
                            </a>
                        </div>
                    </div>
                </@s.form>
            </div>
        </div>
        <div id="ddlist">
            <div class="button-panel pad5A" id="searchFormPanel">
                <@s.form id="searchForm" namespace="/system/dd" action="search" method="post">
                <a title=" <@s.text name="system.dictionary.button.add"/>" class="btn medium primary-bg dd-add" href="javascript:;">
                    <span class="button-content">
                        <i class="glyph-icon icon-plus float-left"></i>
                        <@s.text name="system.dictionary.button.add"/>
                    </span>
                </a>
                <input name="EQS_type" type="hidden"/>
                <div class="propertyFilter">
                </div>
                <div class="form-search">
                    <input type="text" name="LIKES_name_OR_code" title="" data-placement="bottom" class="input tooltip-button ac_input" placeholder="Search..." autocomplete="off" style="display: inline-block; width: 200px;">
                    <i class="glyph-icon icon-search"></i>
                </div>
                </@s.form>
            </div>
            <div id="batchDiv">
                <a title=" <@s.text name="system.dictionary.button.batchdelete"/>" class="btn small primary-bg batchDelete" href="<@s.url namespace="/system/dd" action="delete"/>">
                    <span class="button-content">
                        <i class="glyph-icon  icon-trash  float-left"></i>
                        <@s.text name="system.dictionary.button.batchdelete"/>
                    </span>
                </a>
            </div>
            <#--
            <span id="config_check_info"></span>-->
            <table id="view" class="custom-table table table-hover mrg5B text-center">
                <thead>
                <tr>
                    <th class="pad15L" style="width:20px;">
                        <input id="allChecked" class="custom-checkbox bg-white" checkAll=".ddKey" type="checkbox" <#--checktip="{message:'您选中了{num}条记录',tip:'#config_check_info'}"--> />
                    </th>
                    <th class="sort" orderBy="code"> <@s.text name="system.dictionary.list.code"/></th>
                    <th class="sort" orderBy="name"> <@s.text name="system.dictionary.list.name"/></th>
                    <th class="sort" orderBy="type"> <@s.text name="system.dictionary.list.type"/></th>
                    <th class="center actions"> <@s.text name="system.dictionary.list.actions"/></th>
                </tr>
                </thead>
                <tbody>
                <tr class="template" name="default">
                    <td><input class="ddKey custom-checkbox" type="checkbox" value="{key}"/></td>
                    <td>{code}</td>
                    <td class="font-bold">{name}</td>
                    <td>{type:configTypeName()}</td>
                    <td class="pad0T pad0B center">
                        <div class="dropdown actions">
                            <a href="javascript:;" title="" class="btn medium bg-blue" data-toggle="dropdown">
                                <span class="button-content">
                                    <i class="glyph-icon font-size-11 icon-cog"></i>
                                    <i class="glyph-icon font-size-11 icon-chevron-down"></i>
                                </span>
                            </a>
                            <ul class="dropdown-menu float-right">
                                <li>
                                    <a href="javascript:;" class="up" title=" <@s.text name="system.dictionary.list.actions.moveup"/>">
                                        <i class="glyph-icon icon-circle-arrow-up mrg5R"></i>
                                        <@s.text name="system.dictionary.list.actions.moveup"/>
                                    </a>
                                </li>
                                <li>
                                    <a href="javascript:;" class="down" title=" <@s.text name="system.dictionary.list.actions.movedown"/>">
                                        <i class="glyph-icon icon-circle-arrow-down mrg5R"></i>
                                        <@s.text name="system.dictionary.list.actions.movedown"/>
                                    </a>
                                </li>
                                <li>
                                    <a href="javascript:;" class="edit" title=" <@s.text name="system.dictionary.list.actions.edit"/>">
                                        <i class="glyph-icon icon-edit mrg5R"></i>
                                        <@s.text name="system.dictionary.list.actions.edit"/>
                                    </a>
                                </li>
                                <li class="divider"></li>
                                <li>
                                    <a href="${request.contextPath}/system/dd/delete.do?keys={type}:{code}" class="font-red delete" title=" <@s.text name="system.dictionary.list.actions.delete"/>">
                                        <i class="glyph-icon icon-remove mrg5R"></i>
                                        <@s.text name="system.dictionary.list.actions.delete"/>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
            <div class="divider mrg0T"></div>
        </div>
        <div id="ddDetails">
            <div class="template mrg5A" name="default">
                <div class="infobox warning-bg mrg10B">
                    <p><i class="glyph-icon icon-exclamation mrg10R"></i><@s.text name="system.dictionary.add.prompt"/></p>
                </div>
                <@s.form namespace="/system/dd" action="save" cssClass="form-bordered saveForm" method="post">
                <#--
                <input type="hidden" class="view-field" name="parent.code" mapping="parentCode"/>
                -->
                    <div class="form-row">
                        <div class="form-label col-md-2">
                            <label for="">
                                <@s.text name="system.dictionary.config.category"/>
                            </label>
                        </div>
                        <div class="form-input col-md-10">
                            <input type="hidden" class="view-field" name="type"/>
                            <input type="text" name="typeName" disabled="disabled" value="{type:typeName()}"/>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-label col-md-2">
                            <label for="">
                                <@s.text name="system.dictionary.config.code"/>
                            </label>
                        </div>
                        <div class="form-input col-md-10">
                            <input type="text" class="view-field" name="code" mapping="" formatter=""/>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-label col-md-2">
                            <label for="">
                                <@s.text name="system.dictionary.config.name"/>
                            </label>
                        </div>
                        <div class="form-input col-md-10">
                            <input type="text" class="view-field" name="name"/>
                        </div>
                    </div>
                    <div class="form-row pad0B">
                        <div class="form-label col-md-2">
                            <label for="">
                                <@s.text name="system.dictionary.config.description"/>
                            </label>
                        </div>
                        <div class="form-input col-md-10">
                            <input name="description" class="view-field small-textarea"/>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-input col-md-10 col-md-offset-2">
                            <a title=" <@s.text name="system.dictionary.config.button.return"/>" class="btn medium primary-bg radius-all-4 to-list" href="javascript:;">
                                    <span class="button-content">
                                        <i class="glyph-icon icon-reply float-left"></i>
                                        <@s.text name="system.dictionary.config.button.return"/>
                                    </span>
                            </a>
                            <a title=" <@s.text name="system.dictionary.config.button.save"/>" class="btn medium primary-bg radius-all-4 dd-save" href="javascript:;">
                                    <span class="button-content">
                                        <i class="glyph-icon icon-save float-left"></i>
                                        <label> <@s.text name="system.dictionary.config.button.save"/></label>
                                    </span>
                            </a>
                            <a title=" <@s.text name="system.dictionary.config.button.prompt"/>" class="btn medium bg-gray radius-all-4 hover-white opt-doc" href="javascript:;">
                                    <span class="button-content">
                                        <i class="glyph-icon icon-exclamation font-gray-dark float-left"></i>
                                        <@s.text name="system.dictionary.config.button.prompt"/>
                                    </span>
                            </a>
                        </div>
                    </div>
                </@s.form>
            </div>
        </div>
        </div>

    </div>
</@override>
<@extends name="../wrapper.ftl"/>