<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<script type="text/javascript">
    $(function(){
        //列表初始化
        var page = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(pager)" escapeHtml="false"/>;
        var param = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(#parameters)" escapeHtml="false"/>;
        $('#searchForm').submit(function(){
            if($('#EQS_parent_id').val() == ''){
                $('#EQS_parent_id').attr('name','NULL_parent');
                delete $('#pager').pager().options.postData['EQS_parent.id'];
            }else{
                $('#EQS_parent_id').attr('name','EQS_parent.id');
                delete $('#pager').pager().options.postData['NULL_parent'];
            }
        }).resetForm(param);
        $('#pager').pager($('#searchForm'),page.pageSize,$('#area_view').view().on("add",function(data){
            this.target.find('.id').click(function(e){
                $('#searchForm').find('option[value="'+data.id+'"]').parent().val(data.id).change();
                return stopDefault(e);
            });
            this.target.find('.view').ajax({type:'html',target:'closest(\'#area_list\')',callback:function(type, target){target.initialize();}});
            this.target.find('.edit').ajax({type:'html',target:'closest(\'#area_list\')',callback:function(type, target){target.initialize();}});
            this.target.find('.delete').click(function(e){
                deleteMethod([data.id]);
                return stopDefault(e);
            });
        })).setJSON(page).setPostData(param);

        //编辑产品
        $('.area-edit').data('href',$('.area-edit').attr('href')).click(function(e){
            var ids=$(".select_msg:checked").val();
            if(!ids||ids.length!=1){
                top.$.msgbox({
                    msg : "请选择一条数据!",
                    icon : "warning"
                });
                return stopDefault(e);
            }
            $(this).attr("href",$(this).data("href").replace(/{id}/g,ids[0]));
        }).ajax({type:'html',target:'closest(\'#area_list\')',callback:function(type, target){target.initialize();}});
        var deleteMethod = $('.batchDelete').batchExecute($("#allChecked"),$('#pager').pager(),'id','确认要删除地区[{name}]？',function(){
            top.$.msgbox({
                msg : "删除成功!",
                icon : "success"
            });
        });
        $('#EQS_parent_id').change(function(){
            $('#searchForm').submit();
        });
        Fantasy.area.selects($('#rootArea'),'',$('#EQS_parent_id'),'<select style="width:200px;"><option value="">--请选择查询的地区--</option></select>');
    });
</script>
</@override>
<@override name="container">
<div id="area_list" style="margin: 3px;">
    <form id="searchForm" action="${request.contextPath}/admin/system/area_search.do" method="post">
        <@s.hidden name="EQS_parent.id"/>
        <table class="formTable mb3">
            <caption>区域查询条件</caption>
            <tbody>
            <tr>
                <td class="formItem_title w100">所属区域：</td>
                <td class="formItem_content">
                    <select id="rootArea" style="width:200px;"><option value="">--请选择查询的地区--</option></select>
                </td>
            </tr>
            </tbody>
        </table>
    </form>
    <a class="ui_button" onclick="$('#searchForm').submit()">查询</a>
    <a class="ui_button area-add" href="${request.contextPath}/admin/system/area_add.do" ajax="{type:'html',target:'closest(\'#area_list\')'}">添加区域</a>
    <a class="ui_button area-edit" href="${request.contextPath}/admin/system/area_edit.do?id={id}">编辑区域</a>
    <a class="ui_button batchDelete" href="${request.contextPath}/admin/system/area_delete.do">批量删除区域</a>
    <span id="area_check_info"></span>
    <table id="area_view" class="formTable mb3 listTable w600">
        <caption>查询结果列表</caption>
        <thead>
        <tr>
            <th style="width:30px;"><input id="allChecked" type="checkbox" checkAll=".select_msg"/></th>
            <th class="sort" orderBy="id" style="width:230px;">名称</th>
            <th class="sort" orderBy="displayName">完整地区名称</th>
            <th style="width:200px;">路径</th>
            <th class="sort" orderBy="layer" style="width:100px;">层级</th>
        </tr>
        </thead>
        <tbody>
        <tr align="center" class="template" name="default" >
            <td><input type="checkbox" class="select_msg" name="select_msg" value="{id}"/></td>
            <td>
                <a style="float:left;padding-left: 20px;" href="#" class="id">{name}</a>
                <div style="float:right;padding-right:5px;" toggle="closest('tr')">
                    <a class="delete" href="${request.contextPath}/admin/system//area_delete.do">删除</a>/
                    <a class="edit" href="${request.contextPath}/admin/system/area_edit.do?id={id}">编辑</a>
                </div>
            </td>
            <td>{displayName}</td>
            <td>{path}</td>
            <td>{layer}</td>
        </tr>
        <tr class="empty"><td class="norecord" colspan="5">暂无数据</td></tr>
        </tbody>
        <tfoot>
        <tr>
            <td colspan="5">
                <div id="pager" class="paging digg"></div>
            </td>
        </tr>
        </tfoot>
    </table>
</div>
</@override>
<@extends name="../base.ftl"/>