<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="pageTitle">
<@s.text name='mall.goods.pageTitle'/>
<small>
    You can use the Messaging List component to create all kinds of messaging systems for your application users.
</small>
</@override>
<@override name="head">
<script type="text/javascript">
    $(function () {
        //当浏览器窗口发生变化时,自动调整布局的js代码
        var _top = $('#layout_left').offset().top + 15,_resize = function () {
            $('#layout_left,#layout_right').css('minHeight', $(window).height() - _top);
            $('#layout_right').triggerHandler('resize');
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
                    idKey: "id",
                    pIdKey: "parentId"
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
                onClick: function (e, treeId, treeNode) {
                    //查找上级节点，生成树的目录导航
                    var nodePaths = [];
                    for (var p = treeNode; p != null; p = p.getParentNode()) {
                        nodePaths.push(p.name);
                    }
                    //重置查询条件
                    $('#layout_right_reload').attr('href', '<@s.url namespace="/mall/goods" action="index"/>?EQL_category.id=' + treeNode.id).click();
                    return false;
                },
                beforeDrop: function (treeId, treeNodes) {
                    if (!treeNodes || treeNodes.length == 0) {
                        return false;
                    }
                    return confirm('是否确定移动[' + treeNodes[0].name + ']菜单');
                },
                onDrop: function (event, treeId, treeNodes, targetNode, moveType) {
                    if (!treeNodes || treeNodes.length == 0) {
                        return false;
                    }
                    var index = 0;
                    var parentId = treeNodes[0].parentId;
                    if (!!targetNode) {
                        index = $('#' + targetNode.tId).index() + 1;
                        switch (moveType) {
                            case 'prev':
                                index--;
                                break;
                            case 'inner':
                                index = !targetNode.children ? 1 : targetNode.children.length;
                                parentId = targetNode.id;
                                break;
                            case 'next':
                                index++;
                                break;
                        }
                    } else if (moveType == 'inner') {
                        index = $('#' + treeNodes[0].tId).index() + 1;
                    }
                    if (index > 0) {
                        $.post('<@s.url namespace="/mall/goods/category" action="move"/>', {id: treeNodes[0].id, sort: index, 'parent.id': parentId}, function () {
                            $.msgbox({
                                msg: "顺序调整成功",
                                type: "success"
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
                    idKey: "id",
                    pIdKey: "parentId"
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
                    msg: "请先选择一个分类",
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
                var categoryId = treeNode.id;
                $(this).attr('href', $(this).data('href') + '?id=' + categoryId);
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
                    msg: "请先选择你要修改的商品栏目",
                    type: "warning"
                });
                return stopDefault(e);
            }
            var treeNode = selectdNodes[0];
            var categoryId = treeNode.id;
            $(this).attr('href', $(this).data('href') + '?id=' + categoryId);
        });
        //删除商品栏目
        $('.category-remove').click(function (e) {
            var selectdNodes = categoryTree.getSelectedNodes();
            if (selectdNodes.length == 0) {
                $.msgbox({
                    msg: "请先选择要删除的商品栏目",
                    type: "warning"
                });
                return stopDefault(e);
            }
            var url = $(this).attr('href');
            jQuery.dialog.confirm("确认删除？", function () {
                var treeNode = selectdNodes[0];
                $.post(url + '?ids=' + treeNode.id, function () {
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
        <@s.if test="category != null">
            categoryTree.selectNode(categoryTree.getNodeByParam('id',<@s.property value="category.id"/>), true);
        </@s.if>


    });
</script>
</@override>
<@override name="pageContent">
<div class="col-md-3 pad0L">
    <div class="content-box pad5A mrg0B pad0B scrollable-content" id="layout_left">
        <a title="<@s.text name='mall.goods.category.addBtn' />" class="btn medium primary-bg category-add" href="<@s.url namespace="/mall/goods/category" action="add"/>" target="after:closest('#page-content')">
            <span class="button-content">
                <i class="glyph-icon icon-plus float-left"></i>
                <@s.text name='mall.goods.category.addValue' />
            </span>
        </a>
        <a title="<@s.text name='mall.goods.category.updateBtn' />" class="btn medium primary-bg category-update" href="<@s.url namespace="/mall/goods/category" action="edit"/>"  target="after:closest('#page-content')">
            <span class="button-content">
                <i class="glyph-icon icon-edit float-left"></i>
                <@s.text name='mall.goods.category.updateValue' />
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
                    <a title="<@s.text name='mall.goods.category.deleteBtn' />" class="category-remove" href="<@s.url namespace="/mall/goods/category" action="delete"/>">
                        <i class="glyph-icon icon-remove mrg5R"></i>
                        <@s.text name='mall.goods.category.deleteValue' />
                    </a>
                </li>
                <li>
                    <a title="<@s.text name='mall.goods.category.rootAddTitle' />" class="root-category-add" href="<@s.url namespace="/mall/goods/category" action="root_add"/>" target="after:closest('#page-content')">
                         <i class="glyph-icon icon-plus mrg5R"></i>
                        <@s.text name='mall.goods.category.rootAddTitle' />
                    </a>
                </li>
        </div>
        <div class="divider mrg5B"></div>
        <ul id="categoryTree" class="ztree"></ul>
    </div>
</div>
<div class="col-md-9 pad0L pad0R">
    <a id="layout_right_reload" href="" target="html:#layout_right" style="display:none;"></a>
    <#--
    <span style="height: 27px;line-height: 35px;display: block;">
    位置:<span id="nodePath"
             style="padding-left:5px;"><@s.if test="category != null"><@s.property value="category.name"/></@s.if></span>
    </span>

    <div style="border: 1px solid #88a0ba;width: 100%;display: block;height:100%;margin-top: 5px;overflow: auto;">-->
    <div class="content-box pad5A mrg0B pad0B grid-panel" id="layout_right" >
        <@s.set name="index_path" value="%{@com.fantasy.mall.goods.service.GoodsService@getTemplatePath(\"/template/" + pageTheme + "/mall/goods/goods_index{sign}.ftl\",category)}"/>
		<#include "${index_path}">
    </div>
    <div class="hide" id="basic-dialog" title="<@s.text name='mall.goods.category.moveTitle' />" style="width: 400px;height: 400px;">
        <div class="pad10A">
            <ul id="moveCategoryTree" class="ztree"></ul>
        </div>
    </div>
</div>
</@override>
<@extends name="../../wrapper.ftl"/>