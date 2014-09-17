<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<link rel="stylesheet" href="${request.contextPath}/static/css/pop.css"/>
<style type="text/css">
#container{
	position:fixed;
}
</style>
<script type="text/javascript">
    $(function(){
	//当浏览器窗口发生变化时,自动调整布局的js代码
	$(window).resize(function(){
		$('#layout_right').width($('#container').width() - $("#layout_left").width() - 30);
		$('#layout_left').height($('#container').height()- 46);
		$('#layout_right').height($('#container').height()-20);
	});
	$(window).resize();
	//修改数据
	var menuFilter = function(data) {
	  	data._icon = data.icon;
		data._url = data.url;
		delete data.icon;
		delete data.url;
        if(!data.children){
            delete data.children;
        }
		if(data.type == 'menu'){
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
	var menuTypes = [{key:"url",value:"链接"},{key:"menu",value:"菜单"},{key:"javascript",value:"脚本"},{key:"other",value:"其他"}]
	Fantasy.apply(Fantasy.util.Format, {}, {
		menuType : function(key) {
			return menuTypes.each(function(){
				if(this.key == key){
					return this.value;
				}
			});
		}
	});
	var view = $('#view').view().on('add',function(data){
		if(this.getTemplateName() == 'edit'){
			var $saveForm = this.target.find(".saveForm").ajaxForm(function(data){
				data = menuFilter(data);
				var node = menuTree.getNodeByParam("id", data.id);
				if(!!node){
					menuTree.updateNode(Fantasy.copy(node,data),false);
				}else{
					menuTree.addNodes(menuTree.getSelectedNodes()[0], data);
					node = menuTree.getNodeByParam("id", data.id);
				}
				$('#'+node.tId + '_span').click();
				top.$.msgbox({
					msg : "保存成功",
					icon : "success"
				});
			});
			this.target.find('.save').click(function(){
				$saveForm.submit();
				return false;
			});
		}
	}).setJSON([{}]);
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
				view.setTemplate(0,'view',treeNode);
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
                    $.post('<@s.url namespace="/admin/security/menu" action="save"/>',{id:treeNodes[0].id,sort:index,'parent.id':parentId},function(){
                        top.$.msgbox({
                            msg : "顺序调整成功",
                            icon : "success"
                        });
                    });
                }
                return stopDefault(event);
            }
	}}, menus);
	//添加菜单
 	$('.menu-add').bind('click',function(e){
		var selectdNodes = menuTree.getSelectedNodes();
		if(selectdNodes.length == 1){
			var treeNode = selectdNodes[0];
			view.setTemplate(0,'edit',{"parentId":treeNode.id});
		}else{
			view.setTemplate(0,'edit',{});
		}
	});
	//修改菜单
	$('.menu-update').bind('click',function(e){
		var selectdNodes = menuTree.getSelectedNodes();
		if(selectdNodes.length != 1){
			top.$.msgbox({
				msg:"请先选择你要修改的菜单",
				icon:"warning"
			});
			return stopDefault(e);
		}
		view.setTemplate(0,'edit');
	});
	//删除菜单
	$('.menu-remove').click(function(e){
		var selectdNodes = menuTree.getSelectedNodes();
		if(selectdNodes.length == 0){
			top.$.msgbox({
				msg:"请先选择要删除的菜单",
				icon:"warning"
			});
			return stopDefault(e);
		}
		var url = $(this).attr('href');
		jQuery.dialog.confirm("确认删除？",function(){
			var treeNode = selectdNodes[0];
			$.post(url+'?id='+treeNode.id,function(){
				top.$.msgbox({
					msg : "删除成功",
					icon : "success"
				});
				var onode = treeNode.getParentNode();
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

});
</script>
</@override>
<@override name="container">
    <!-- 左边 -->
    <div id="layout_left" style="display: inline;width: 250px;height:100%;padding: 5px;float:left;">
        <a class="ui_button menu-add" href="###">添加</a>
        <a class="ui_button menu-update" href="###">修改</a>
        <a class="ui_button menu-remove" href="${request.contextPath}/admin/security/menu/delete.do">删除</a>
        <div style="border: 1px solid #88a0ba;width: 100%;display: block;height:100%;margin-top: 5px;margin-bottom: -200px;">
            <div class="bg-caption">
                <span class="bgc-title">所有栏目</span>
            </div>
            <ul id="menuTree" class="ztree"> </ul>
        </div>
    </div>
	<!-- 右边 -->
     <div id="layout_right"  style="display: block;width: 500px;height:100%;padding: 5px;float:left;">
        <div id="view" style="border: 1px solid #88a0ba;width: 100%;display: block;height:100%;margin-top: 5px;overflow: auto;">
            <div style="margin: 3px;" class="template" name="default">
                <h1>菜单操作文档</h1>
            </div>

            <div style="margin: 3px;" class="template" name="view">
                <table class="formTable mb3">
                    <caption>基本信息<a href="###" style="float:right;padding-right:50px;" class="switch" template="edit">编辑</a></caption>
                    <tbody>
                        <tr>
                            <td class="formItem_title w100">名称：</td>
                            <td class="formItem_content"><label name="name" class="view-field"><label></td>
                            <td class="formItem_title w100">菜单类型：</td>
                            <td class="formItem_content">{type:menuType()}</td>
                        </tr>
                        <tr>
                            <td class="formItem_title w100">菜单值：</td>
                            <td class="formItem_content" colspan="3"><label name="value" class="view-field"><label></td>
                        </tr>
                        <tr>
                            <td class="formItem_title ">描述：</td>
                            <td class="formItem_content" colspan="3"><label name="description" class="view-field"><label></td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div style="margin: 3px;" class="template" name="edit">
            	<form class="saveForm" action="${request.contextPath}/admin/security/menu/save.do" method="post">
            	 	<input type="hidden" class="view-field" name="parent.id" mapping="parentId"/>
                    <input type="hidden" class="view-field" name="id"/>
                    <table class="formTable mb3">
                        <caption>基本信息<a href="###" class="switch back" template="view" style="float:right;padding-right:50px;">取消</a></caption>
                        <tbody>
                            <tr>
                                <td class="formItem_title w100">名称：</td>
                                <td class="formItem_content">
                                    <input type="text" class="view-field" name="name"/>
                                </td>
                                <td class="formItem_title w100">菜单类型：</td>
                                <td class="formItem_content">
                                    <@s.select name="type" cssClass="view-field" list="@com.fantasy.security.bean.enums.MenuType@values()" listValue="value" style="width:250px;"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="formItem_title w100">菜单值：</td>
                                <td class="formItem_content" colspan="3">
                                    <textarea name="value" class="view-field" style="width:98%;"></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td class="formItem_title ">描述：</td>
                                <td class="formItem_content" colspan="3">
                                    <textarea name="description" class="view-field" style="width:98%;"></textarea>
                                </td>
                            </tr>
                            <tr>
                            	<td class="formItem_title "></td>
                                <td class="formItem_content" colspan="3">
                                	<a class="ui_button save">保存</a>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                 </form>
            </div>
        </div>
    </div>
</@override>
<@extends name="../base.ftl"/>