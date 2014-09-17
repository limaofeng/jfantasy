<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function(){
        //列表初始化
        var page = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(pager)" escapeHtml="false"/>;
        $('#pager').pager($('#searchForm'),page.pageSize,$('#dataInferface_view').view().on("add",function(data){
            this.target.find('.delete').click(function(e){
                deleteMethod([data.id]);
                return stopDefault(e);
            });
        })).setJSON(page);
        
        var deleteMethod = $('.batchDelete').batchExecute($("#allChecked"),$('#pager').pager(),'id','确认要删除[{name}]？',function(){
            top.$.msgbox({
                msg : "删除成功!",
                icon : "success"
            });
        });
		$('.searchForm').click(function(){
			$('#searchForm').submit();
			return false;
		});
    });
</script>
<div id="dataInferface_list" style="margin: 3px;">
    <form id="searchForm" action="${request.contextPath}/admin/swp/page/face-search.do" method="post">
    	<@s.hidden name="EQL_template.id" value="#parameters['EQL_template.id']" />
        <table class="formTable mb3">
            <caption>数据查询条件 <a href="###" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
            <tbody>
            <tr>
                <td class="formItem_title w100">名称：</td>
                <td class="formItem_content"><@s.textfield name="LIKES_name" cssClass="ui_input_text w200" /></td>
                <td class="formItem_title w100">Key：</td>
                <td class="formItem_content"><@s.textfield name="LIKES_key" cssClass="ui_input_text w200" /></td>
                <td class="formItem_title w100">code：</td>
                <td class="formItem_content"><@s.textfield name="LIKES_code" cssClass="ui_input_text w200" /></td>
            </tr>
            </tbody>
        </table>
    </form>
    <a class="ui_button searchForm">查询</a>
    <a class="ui_button" href="${request.contextPath}/admin/swp/page/face-add.do?templateId=<@s.property value="#parameters['EQL_template.id']"/>" target="after:closest('.ajax-load-div')">添加</a>
    <a class="ui_button batchDelete" href="${request.contextPath}/admin/swp/page/face-delete.do" >删除</a>
    <span id="dataInferface_check_info"></span>
    <table id="dataInferface_view" class="formTable mb3 listTable w600">
        <caption>查询结果列表</caption>
        <thead>
        <tr>
            <th style="width:30px;"><input id="allChecked" type="checkbox" checkAll=".select_msg"></th>
            <th class="sort" orderBy="id">数据名称</th>
            <th style="width:20%;">key</th>
            <th style="width:20%;">code</th>
            <th style="width:20%;">javaType</th>
            <th style="width:20%;">defaultValue</th>
        </tr>
        </thead>
        <tbody>
        <tr class="template" name="default" >
            <td><input type="checkbox" class="select_msg" name="select_msg" value="{id}"/></td>
            <td>
                <a href="${request.contextPath}/admin/swp/page/face-view.do?id={id}" target="after:closest('.ajax-load-div')">{name}</a>
                <div style="float:right;padding-right:5px;" toggle="closest('tr')">
                    <a class="delete" href="${request.contextPath}/admin/swp/page/face-delete.do">删除</a>/
                    <a href="${request.contextPath}/admin/swp/page/face-edit.do?id={id}" target="after:closest('.ajax-load-div')">修改</a>
                </div>
            </td>
            <td>{key}</td>
            <td>{code}</td>
            <td>{dataType}</td>
            <td>{defaultValue}</td>
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
