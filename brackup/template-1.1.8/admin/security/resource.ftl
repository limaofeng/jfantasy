<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<script type="text/javascript">
$(function(){
	//当浏览器窗口发生变化时,自动调整布局的js代码
	$(window).resize(function(){
		$('#layout_right').width($('#container').width() - $("#layout_left").width() - 30);
		$('#layout_left').height($('#container').height()- 46);
		$('#layout_right').height($('#container').height()-45);
	});
	$(window).resize();
	//分类树
	window.resources = Fantasy.decode('<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(resources)" escapeHtml="false"/>');
	window.resourceTree = $('#resourceTree').zTree({
		dataFilter:function(data,call){
			data.each(function(){
				if(this.type!='url'){
					this.isParent = true;
				}
				if(!!this.children){
					call.apply(window,[this.children,call]);
				}
			});
			return data;
		},
		data:{
		key : {
			name: "name"
		},
		simpleData: {
			enable: true,
			idKey: "id"
		}
	},edit: {
		showRemoveBtn:false,
		showRenameBtn:false
	} ,
	callback:{
		onClick : function (e, treeId, treeNode) {
			//查找上级节点，生成树的目录导航
			var nodePaths = [];
			for(var p=treeNode;p!=null;p=p.getParentNode()){
				nodePaths.push(p.name);
			}
			$('#nodePath').html(nodePaths.reverse().toString().replace(new RegExp(",","ig"),' > '));
			//重置查询条件
			$('#searchForm').resetForm();
			$('#searchForm').find('[name="EQL_parentResources.id"]').val(treeNode.id);
			var data = Fantasy.parseQuery($('#searchForm').serialize());
			//将表单绑定到列表
			$('#pager').pager().setPostData(data);
			//重新加载列表
			$('#pager').pager().reload();
			return false;
		}
	}}, resources);
	
	//添加group
 	$('.resource-add').bind('click',function(e){
		if(!$(this).data('href')){
			$(this).data('href',$(this).attr('href'));
		}
		var selectdNodes = resourceTree.getSelectedNodes();
		if(selectdNodes.length == 1){
			var treeNode = selectdNodes[0];
			var resourceId = treeNode.id;
			$(this).attr('href',$(this).data('href')+'?id='+ resourceId+"&type=group");
		}else{
			$(this).attr('href',$(this).data('href')+"?type=group");
		}
	}).ajax({type:'html',target:'#container',callback:function(type, target){target.initialize();}});
	
	//添加url
	$('.add').bind('click',function(e){
		if(!$(this).data('href')){
			$(this).data('href',$(this).attr('href'));
		}
		var selectdNodes = resourceTree.getSelectedNodes();
		if(selectdNodes.length == 1){
			var treeNode = selectdNodes[0];
			var resourceId = treeNode.id;
			$(this).attr('href',$(this).data('href')+'?id=' + resourceId+"&type=url");
		}else{
			$(this).attr('href',$(this).data('href')+"?type=url");
		}
	}).ajax({type:'html',target:'#container',callback:function(type, target){target.initialize();}});
	
	
	//修改分类
	$('.resource-update').bind('click',function(e){
		if(!$(this).data('href')){
			$(this).data('href',$(this).attr('href'));
		}
		var selectdNodes = resourceTree.getSelectedNodes();
		if(selectdNodes.length != 1){
			top.$.msgbox({
				msg:"请先选择你要修改的商品栏目",
				icon:"warning"
			});
			return stopDefault(e);
		}
		var treeNode = selectdNodes[0];
		var resourceId = treeNode.id;
		$(this).attr('href',$(this).data('href')+'?id=' + resourceId);
	}).ajax({type:'html',target:'#container',callback:function(type, target){target.initialize();}});
	//删除商品栏目
	$('.resource-remove').click(function(e){
		var selectdNodes = resourceTree.getSelectedNodes();
		if(selectdNodes.length == 0){
			top.$.msgbox({
				msg:"请先选择要删除的商品栏目",
				icon:"warning"
			});
			return stopDefault(e);
		}
		var url = $(this).attr('href');
		jQuery.dialog.confirm("确认删除？",function(){
			var treeNode = selectdNodes[0];
			$.post(url+'?ids='+treeNode.id,function(){
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
				resourceTree.removeNode(treeNode);
				
				//重置查询条件
				$('#searchForm').resetForm();
				$('#searchForm').find('[name="EQL_parentResources.id"]').val(treeNode.id);
				var data = Fantasy.parseQuery($('#searchForm').serialize());
				//将表单绑定到列表
				$('#pager').pager().setPostData(data);
				//重新加载列表
				$('#pager').pager().reload();
				
			});
		});
		return stopDefault(e);
	});
	
	//列表内容	
	var page = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(pager)" escapeHtml="false"/>;
	$('#pager').pager($('#searchForm'),page.pageSize,$('#resource_view').view().on("add",function(data){
		this.target.find('.delete').click(function(e){
			deleteMethod([data.id]);
			return stopDefault(e);
		});
	})).setJSON(page);
	
	//批量删除
	var deleteMethod = $('.batchDelete').batchExecute($("#allChecked"),$('#pager').pager(),'id','确认要删除[{name}]？',function(){
		top.$.msgbox({
			msg : "删除成功!",
			icon : "success"
		});
	});
});
</script>
</@override>
<@override name="container">
<div id="container" style="position:fixed;width:100%;height:100%;display: block;">
	<div id="layout_left" style="display: inline;width: 178px;height:100%;padding: 5px;float:left;">
		<a class="ui_button resource-add" href="${request.contextPath}/admin/security/resource/add.do">添加</a>
		<a class="ui_button resource-update" href="${request.contextPath}/admin/security/resource/edit.do">修改</a>
		<a class="ui_button resource-remove" href="${request.contextPath}/admin/security/resource/delete.do">删除</a>
		<div style="border: 1px solid #88a0ba;width: 100%;display: block;height:100%;margin-top: 5px;margin-bottom: -200px;">
			<div class="bg-caption">
				<span class="bgc-title">所有栏目</span>
			</div>
			<ul id="resourceTree" class="ztree"> </ul>
		</div>
	</div>
	<div id="layout_right"  style="display: block;width: 500px;height:100%;padding: 5px;float:left;">
		<span style="height: 27px;line-height: 35px;display: block;">
		位置:<span id="nodePath"> </span>
		</span>
		<div style="border: 1px solid #88a0ba;width: 100%;display: block;height:100%;margin-top: 5px;overflow: auto;">
			<div id="resource_list" style="margin: 3px;">
			<form id="searchForm" action="${request.contextPath}/admin/security/resource/search.do" method="post">
				<input type="hidden" name="EQL_parentResources.id"/>
			
			</form>
			<div style="padding-top:2px;padding-bottom: 3px;">
			<a class="ui_button" onclick="$('#searchForm').submit()">查询</a>
			<a class="ui_button add" href="${request.contextPath}/admin/security/resource/add.do">添加</a>
			<a class="ui_button batchDelete" href="${request.contextPath}/admin/security/resource/delete.do">批量删除</a>
			<span id="resource_check_info"> </span>
			</div>
			<table id="resource_view" class="formTable mb3 listTable">
				<caption>查询结果列表</caption>
				<thead>
					<tr>
						<th style="width:30px;">
							<input id="allChecked" type="checkbox" checkAll=".select_msg" checktip="{message:'您选中了{num}条记录',tip:'#resource_check_info'}" />
						</th>
						<th style="width:20%;">名称</th>
						<th style="">值</th>
						<th style="width:5%;">类型</th>
						<th style="width:5%;">是否启用</th>
					</tr>
				</thead>
				<tbody>
					<tr align="center" class="template" name="default" >
						<td><input type="checkbox" class="select_msg" value="{id}"/></td>
						<td>
							{name}
							<div style="float:right;padding-right:10px;" toggle="closest('tr')">
								<a class="delete" href="###">删除</a>/
								<a href="${request.contextPath}/admin/security/resource/edit.do?id={id}" ajax="{type:'html',target:'closest(\'#container\')'}" >编辑</a>
							</div>
						</td>
						<td>{value}</td>
						<td>{type}</td>
						<td>{enabled:dict({'true':'是','false':'否'})}</td>
					</tr>
					<tr class="empty"><td class="norecord" colspan="5">暂无数据</td></tr>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="5">
						<div id="pager" class="paging digg"> </div>
						</td>
					</tr>
				</tfoot>
			</table>
		</div>
		</div>
	</div>
</div>
</@override>
<@extends name="../base.ftl"/>