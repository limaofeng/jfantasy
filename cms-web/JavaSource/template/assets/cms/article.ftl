<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="pageTitle">
    <@s.text name="cms.article.pageTitle"/>
</@override>
<@override name="head">
<script type="text/javascript">
    $(function () {
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
        window.categoryFilter = function (data) {

            if (!data.children) {
                delete data.children;
            }
            if (!data.parentId) {
                data.open = true;
            }
            data.isParent = true;
            return data;
        };
        //分类树
        window.categorys = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(categorys)" escapeHtml="false"/>;
        categorys.each(function () {
            categoryFilter(this);
        });
        window.categoryTree = $('#categoryTree').zTree({
            data: {
                key: {
                    name: "name"
                },
                simpleData: {
                    enable: true,
                    idKey: "code",
                    pIdKey: "parentCode"
                }
            }, edit: {
                drag: {
                    isMove: true
                },
                enable: true,
                showAddBtn: false,
                showRemoveBtn: false,
                showRenameBtn: false
            },
            callback: {
                onClick : function (e, treeId, treeNode) {
                    //查找上级节点，生成树的目录导航
//                    var nodePaths = [];
//                    for(var p=treeNode;p!=null;p=p.getParentNode()){
//                        nodePaths.push(p.name);
//                    }
//                    $('#nodePath').html(nodePaths.reverse().toString().replace(new RegExp(",","ig"),' > '));
                    //重置查询条件
                    $('#layout_right_reload').attr('href', '<@s.url namespace="/cms/article" action="index"/>?EQS_category.code=' + treeNode.code).click();
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
                    var parentCode = treeNodes[0].parentCode;
                    if(!!targetNode){
                        index = $('#'+targetNode.tId).index()+1;
                        switch(moveType){
                            case 'prev':
                                index--;
                                break;
                            case 'inner':
                                index = !targetNode.children?1:targetNode.children.length;
                                parentCode = targetNode.code;
                                break;
                            case 'next':
                                index++;
                                break;
                        }
                    }else if(moveType == 'inner'){
                        index = $('#'+treeNodes[0].tId).index()+1;
                    }
                    if(index > 0){
                        $.post('<@s.url namespace="/cms/article" action="category_move"/>',{code:treeNodes[0].code,sort:index,'parent.code':parentCode},function(){
                            top.$.msgbox({
                                msg : "顺序调整成功",
                                type : "success"
                            });
                        });
                    }
                    return stopDefault(event);
                }
            }}, categorys);



        window.moveCategoryTree = $('#moveCategoryTree').zTree({
            data: {
                key: {
                    name: "name"
                },
                simpleData: {
                    enable: true,
                    idKey: "code",
                    pIdKey: "parentCode"
                }
            }, edit: {
                drag: {
                    isMove: false
                },
                enable: true,
                showAddBtn: false,
                showRemoveBtn: false,
                showRenameBtn: false
            }});



        //添加分类
        $('.category-add').bind('click', function (e) {
            if (categoryTree.getSelectedNodes().length == 0) {
                $.msgbox({
                    msg: "请先选择一个文章栏目",
                    type: "warning"
                });
                return  false;
            }
            if (!$(this).data('href')) {
                $(this).data('href', $(this).attr('href'));
            }
            var selectdNodes = categoryTree.getSelectedNodes();
            if (selectdNodes.length == 1) {
                var treeNode = selectdNodes[0];
                var categoryCode = treeNode.code;
                $(this).attr('href', $(this).data('href') + '?categoryCode=' + categoryCode);
            }
        });
        //修改分类
        $('.category-update').bind('click', function (e) {
            if (!$(this).data('href')) {
                $(this).data('href', $(this).attr('href'));
            }
            var selectdNodes = categoryTree.getSelectedNodes();
            if (selectdNodes.length != 1) {
                $.msgbox({
                    msg: "请先选择你要修改的文章栏目",
                    type: "warning"
                });
                return stopDefault(e);
            }
            var treeNode = selectdNodes[0];
            var categoryCode = treeNode.code;
            $(this).attr('href', $(this).data('href') + '?id=' + categoryCode);
        });
        //删除栏目
        $('.category-remove').click(function (e) {
            var selectdNodes = categoryTree.getSelectedNodes();
            if (selectdNodes.length == 0) {
                $.msgbox({
                    msg: "请先选择要删除的文章栏目",
                    type: "warning"
                });
                return stopDefault(e);
            }
            var url = $(this).attr('href');
            jQuery.dialog.confirm("确认删除？", function () {
                var treeNode = selectdNodes[0];
                $.post(url + '?ids=' + treeNode.code, function () {
                    $.msgbox({
                        msg: "删除成功",
                        type: "success"
                    });
                    var onode = treeNode.getParentNode();
                    onode = onode ? onode : treeNode.getNextNode();
                    onode = onode ? onode : treeNode.getPreNode();
                    if (onode) {
                        $('#' + onode.tId + '_span').click();
                    }
                    categoryTree.removeNode(treeNode);
                });
            });
            return stopDefault(e);
        });

        <@s.if test="categorys != null">
            categoryTree.selectNode(categoryTree.getNodeByParam('code','<@s.property value="categorys[0].code"/>'), true);
        </@s.if>
    });
</script>
</@override>
<@override name="pageContent">
<div class="col-md-3 pad0L">
    <div class="content-box pad5A mrg0B pad0B" id="layout_left">
        <a title="添加分类" class="btn medium primary-bg category-add" href="<@s.url namespace="/cms/article" action="category_add"/>" target="after:closest('#page-content')">
            <span class="button-content">
                <i class="glyph-icon icon-plus float-left"></i>
                <@s.text name="operation.add"/>
            </span>
        </a>
        <a title="编辑分类" class="btn medium primary-bg category-update" href="<@s.url namespace="/cms/article" action="category_edit"/>" target="after:closest('#page-content')">
            <span class="button-content">
                <i class="glyph-icon icon-edit float-left"></i>
                <@s.text name="operation.edit"/>
            </span>
        </a>
        <div class="dropdown btn-group">
            <a href="javascript:;" class="btn medium primary-bg" title="" data-toggle="dropdown">
                <span class="button-content text-center float-none font-size-11 text-transform-upr">
                    <i class="glyph-icon icon-caret-down float-right"></i>
                    <@s.text name="operation.more"/>
                </span>
            </a>
            <ul class="dropdown-menu float-right">
                <li>
                    <a title="删除分类" class="category-remove" href="<@s.url namespace="/cms/article" action="category_delete"/>" target="after:closest('#page-content')">
                        <i class="glyph-icon icon-remove mrg5R"></i>
                        <@s.text name="operation.remove"/>
                    </a>
                </li>
                <li>
                    <a title="添加根" class="root-add" href="<@s.url namespace="/cms/article" action="category_add?categoryCode=%{@com.fantasy.system.util.SettingUtil@getValue('cms')}"/>" target="after:closest('#page-content')">
                        <i class="glyph-icon icon-plus mrg5R"></i>
                        <@s.text name="operation.add.root"/>
                    </a>
                </li>
            </ul>
        </div>
        <div class="divider mrg5B"></div>
        <ul id="categoryTree" class="ztree"></ul>
    </div>
</div>
<div class="col-md-9 pad0L pad0R">
    <a id="layout_right_reload" href="" target="html:next('#layout_right')" style="display:none;"></a>
    <div class="content-box pad5A mrg0B pad0B grid-panel" id="layout_right" >
        <@s.set name="index_path" value="%{@com.fantasy.cms.service.CmsService@getTemplatePath(\"/template/" + pageTheme + "/cms/article_index{code}.ftl\",category)}"/>
            <#include "${index_path}">
    </div>
    <div class="hide" id="basic-dialog" title="选择移动分类" style="width: 400px;height: 400px;">
        <div class="pad10A">
            <ul id="moveCategoryTree" class="ztree"></ul>
        </div>
    </div>
</div>
</@override>
<@extends name="../wrapper.ftl"/>