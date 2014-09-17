<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<link rel="stylesheet" href="${request.contextPath}/static/css/pop.css"/>
<script type="text/javascript">
    $(function(){
        //列表初始化
        var gruopView = $('#group_view').view().on('add',function(data){
            var zhis = this;
            //取消
            this.target.find('.cancel').click(function(e){
                if(!data.id){
                    gruopView.remove(zhis.getIndex());
                }
                return stopDefault(e);
            });
            //保存
            this.target.find('.save').click(function(e){
                $.post($(this).attr("href"),zhis.getData(),function(data){
                    top.$.msgbox({
                        msg : "保存成功!",
                        icon : "success"
                    });
                    gruopView.setTemplate(zhis.getIndex(),"default",data);
                });
                return stopDefault(e);
            });
            //删除
            this.target.find('.delete').click(function(e){
                deleteGroup([data.id]);
                return stopDefault(e);
            });
            this.target.initialize();
        }).setJSON(Fantasy.decode('<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(groups)" escapeHtml="false"/>'));
        //查询表单异步操作
        $("#searchForm").ajax(function(data){
            //$('#group_view').setPostData(Fantasy.parseQuery($('#searchForm').serialize()));
            $('#group_view').setJSON(data);
        });
        $('.batchDelete').click(function(e){
            var ids=$(".checkBox_groupId:checked").val();
            deleteGroup(ids);
            return stopDefault(e);
        });
        var deleteGroup = function(ids){
            if(ids){
                var names = [];
                ids.each(function(){
                    var row = $('#group_view').view().find("id",this.toString());
                    names.push(row.data.name);
                });
                jQuery.dialog.confirm("确认删除联系人组["+names+"]？",function(){
                    $.post('${request.contextPath}/admin/contacts/deletegroup.do',{ids:ids},function(data){
                        top.$.msgbox({
                            msg : "删除成功!",
                            icon : "success"
                        });
                        ids.each(function(){
                            var row = $('#group_view').view().find("id",this.toString());
                            gruopView.remove(row.getIndex());
                        });
                        var origin = layoutManager.getWindow('contacts_list');
                        if(origin){
                            origin.$('#pager').pager().reload();//刷新列表
                        }
                    });
                });
            }else{
                top.$.msgbox({
                    msg : "您没有选中记录!",
                    icon : "warning"
                });
            }
        };
        //添加联系人组
        $("#groupAdd").click(function(e){
            if(gruopView.each(function(){
                if(gruopView.get(this.getIndex()).getTemplateName()=='update'){
                    return false;
                }
            })==false){
                top.$.msgbox({
                    msg : "请您先保存后,在继续添加!",
                    icon : "warning"
                });
            }else{
                gruopView.insert(0,{},'update');
            }
            return stopDefault(e);
        });
    });
</script>
</@override>
<@override name="container">
<a class="ui_button add" id="groupAdd" >添加</a>
<a class="ui_button batchDelete">批量删除</a>
<span id="check_info"></span>
<table id="group_view" class="formTable mb3 listTable">
    <caption>查询结果列表 </caption>
    <thead>
    <tr>
        <th style="width: 30px;"><input checkAll=".checkBox_groupId" type="checkbox" checktip="{message:'您选中了{num}条记录',tip:'#check_info'}"/></th>
        <th class="sort" orderBy="name" >姓名</th>
        <th class="sort" orderBy="description" >备注</th>
    </tr>
    </thead>
    <tbody>
    <tr align="center" class="template" name="default" >
        <td><input class="checkBox_groupId" type="checkbox" value="{id}"/></td>
        <td>
            <span style="float:left;padding-left: 20px;">{name}</span>
            <div style="float:right;padding-right:5px;" toggle="closest('tr')">
                <a class="delete" href="%{contextPath}/admin/contacts/deletegroup.do?ids={id}">删除</a>/
                <a class="switch only" template="update">编辑</a>
            </div>
        </td>
        <td>{description}</td>
    </tr>
    <tr align="center" class="template" name="update" >
        <td><input class="checkBox_groupId" type="checkbox" value="{id}"/></td>
        <td><span style="float:left;padding-left: 20px;"><input class="view-field ui_input_text w350" type="text" name="name"/></span>
            <div style="float:right;padding-right:5px;margin-top:4px;" toggle="closest('tr')">
                <a class="switch back cancel" template="default" href="%{contextPath}/admin/security/user_delete.do?ids={id}">取消</a>/
                <a class="save" href="%{contextPath}/admin/contacts/savegroup.do">保存</a>
            </div>
        </td>
        <td><input name="description" class="view-field ui_input_text" style="width:80%;" type="text"/></td>
    </tr>
    <tr class="empty"><td class="norecord" colspan="3">暂无数据</td></tr>
    </tbody>
</table>
</@override>
<@extends name="../base.ftl"/>