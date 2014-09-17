<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<link rel="stylesheet" href="${request.contextPath}/static/css/pop.css"/>
<script type="text/javascript">
    $(function(){
        //列表初始化
        var typeView = $('#type_view').view().on('add',function(data){
            var zhis = this;
            //取消
            this.target.find('.cancel').click(function(e){
                if(!data.code){
                    typeView.remove(zhis.getIndex());
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
                    typeView.setTemplate(zhis.getIndex(),"default",data);
                });
                return stopDefault(e);
            });
            //删除
            this.target.find('.delete').click(function(e){
                deleteMethod([data.code]);
                return stopDefault(e);
            });
            this.target.initialize();
        }).setJSON(Fantasy.decode('<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(types)" escapeHtml="false"/>'));
        //查询表单异步操作
        $("#searchForm").ajaxForm(function(data){
            $('#type_view').setJSON(data);
        });
        var deleteMethod = $('.batchDelete').batchExecute($("[checkAll='.configId']"),typeView,'code','是否确认删除分类[{name}]？<br/>删除分类会自动删除对应的配置项',function(params){
            top.$.msgbox({
                msg : "删除成功!",
                icon : "success"
            });
            params.codes.each(function(){
                var row = $('#type_view').view().find("code",this.toString());
                typeView.remove(row.getIndex());
            });
        });
        //添加联系人组
        $("#typeAdd").click(function(e){
            if(typeView.each(function(){
                if(typeView.get(this.getIndex()).getTemplateName()=='update'){
                    return false;
                }
            })==false){
                top.$.msgbox({
                    msg : "请您先保存后,在继续添加!",
                    icon : "warning"
                });
            }else{
                typeView.insert(0,{},'update');
            }
            return stopDefault(e);
        });
    });
</script>
</@override>
<@override name="container">
<a class="ui_button add" id="typeAdd" >添加</a>
<a class="ui_button batchDelete" href="${request.contextPath}/admin/system/config/deletetype.do">批量删除</a>
<span id="check_info"></span>
<table id="type_view" class="formTable mb3 listTable">
    <caption>查询结果列表 </caption>
    <thead>
    <tr>
        <th style="width: 30px;"><input checkAll=".configId" type="checkbox" checktip="{message:'您选中了{num}条记录',tip:'#check_info'}"/></th>
        <th class="sort" orderBy="code" style="width:170px;">编码</th>
        <th class="sort" orderBy="name">名称</th>
        <th class="sort" orderBy="description">描述</th>
    </tr>
    </thead>
    <tbody>
    <tr align="center" class="template" name="default" >
        <td><input class="configId" type="checkbox" value="{code}"/></td>
        <td><span style="float:left;padding-left: 5px;">{code}</span></td>
        <td>
            <span style="float:left;padding-left: 20px;">{name}</span>
            <div style="float:right;padding-right:5px;" toggle="closest('tr')">
                <a class="delete" href="${request.contextPath}/admin/contacts/deletegroup.do?codes={id}">删除</a>/
                <a class="switch only" template="update">编辑</a>
            </div>
        </td>
        <td><span style="float:left;">{description}</span></td>
    </tr>
    <tr align="center" class="template" name="update" >
        <td><input class="checkBox_groupId" type="checkbox" value="{id}"/></td>
        <td><span style="float:left;padding-left: 5px;"><input class="view-field ui_input_text" style="width:150px;" type="text" name="code"/></span>
        <td><span style="float:left;padding-left: 20px;"><input class="view-field ui_input_text w350" type="text" name="name"/></span>
            <div style="float:right;padding-right:5px;margin-top:4px;" toggle="closest('tr')">
                <a class="switch back cancel" template="default" href="${request.contextPath}/admin/system/config/deletetype.do?ids={id}">取消</a>/
                <a class="save" href="${request.contextPath}/admin/system/config/savetype.do">保存</a>
            </div>
        </td>
        <td><input name="description" class="view-field ui_input_text" style="width:80%;" type="text"/></td>
    </tr>
    <tr class="empty"><td class="norecord" colspan="4">暂无数据</td></tr>
    </tbody>
</table>
</@override>
<@extends name="../base.ftl"/>