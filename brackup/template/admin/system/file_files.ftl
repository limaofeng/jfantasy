<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<script type="text/javascript">
    $(function(){
        $('.file-path').bind('click',function(){
            var zhis = this;
            listFiles('/',function(){
                $(zhis).nextAll('span.filenode').remove();
            });
        });
        $('#pager').pager(Fantasy.decode('<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(files)" escapeHtml="false"/>'),10,$('#view').view().on('add',function(data){
            this.target.find('.in-files').click(function(){
                if(!data.directory){
                    top.$.msgbox({
                        msg : "只能打开文件夹!",
                        icon : "warning"
                    });
                    return false;
                }
                listFiles(data.absolutePath,function(){
                    $('#file_nav').append($('<span class="filenode" data-path="'+data.absolutePath+'">&#62;<a>'+data.name+'</a></span>').bind('click',function(){
                        var zhis = this;
                        listFiles($(this).data('path'),function(){
                            $(zhis).nextAll().remove();
                        });
                    }));
                });
                return false;
            });
        }));
        var listFiles = function(path,callback){
            $.post('${request.contextPath}/admin/system/file/listfiles.do',{fileManagerId:'<@s.property value="#parameters[\'fileManagerId\']"/>',path:path},function(data){
                $('#pager').pager().setJSON(data,callback);
            });
        }
        Fantasy.decode('<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(nodes)" escapeHtml="false"/>').each(function(i){
            var data = this;
            $('#file_nav').append($('<span class="filenode" data-path="'+data.absolutePath+'">&#62;<a>'+data.name+'</a></span>').bind('click',function(){
                var zhis = this;
                listFiles($(this).data('path'),function(){
                    $(zhis).nextAll().remove();
                });
            }));
        });

    });
</script>
</@override>
<@override name="container">
<#--
<a class="ui_button add" href="${request.contextPath}/admin/system/file/ftp_add.do">添加</a>
<a class="ui_button edit" href="${request.contextPath}/admin/system/file/ftp_edit.do?id={id}">编辑</a>
<a class="ui_button batchDelete" href="${request.contextPath}/admin/system/file/ftp_delete.do">删除</a>
-->
<table id="view" class="formTable mb3 listTable">
    <caption id="file_nav">文件列表:<a class="file-path" data-path="/" style="margin-left:10px;">根目录</a><a href="###" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
    <thead>
    <tr>
        <th style="width: 30px;"><input id="allChecked" type="checkbox" checkAll=".checkId" checktip="{message:'您选中了{num}条记录',tip:'#check_info'}" ></th>
        <th style="width:350px;" class="sort" orderBy="name">名称</th>
        <th style="width:250px;" class="sort" orderBy="type">类型</th>
        <th style="width:250px;" class="sort" orderBy="code">大小</th>
        <th>日期</th>
    </tr>
    </thead>
    <tbody>
    <tr align="center" class="template" name="default">
        <td><input type="checkbox" class="checkId" value="{id}"/></td>
        <td>
            <span style="float:left;padding-left: 20px;"><a class="in-files" href="${request.contextPath}/admin/system/file/ftp_view.do?id={id}">{name}</a></span>
            <div style="float:right;padding-right:5px;" toggle="closest('tr')">
                <a class="delete" href="${request.contextPath}/admin/system/file/ftp_delete.do?id={id}">删除</a>/
                <a href="${request.contextPath}/admin/system/file/ftp_edit.do?id={id}" ajax="{type:'html',target:'closest(\'#page_list\')'}">编辑</a>
            </div>
        </td>
        <td>{contentType}</td>
        <td>{size:fileSize()}</td>
        <td>{lastModified}</td>
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
</@override>
<@extends name="../base.ftl"/>