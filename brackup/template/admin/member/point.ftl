<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function(){
        //列表初始化
        var page = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(pager)" escapeHtml="false"/>;
        $('#pager').pager($("#searchForm"),page.pageSize,$('#view').view().on('add',function(data){
            this.target.find('.delete').click(function(){
             	console.log(data.id);
                deleteMethod([data.id]);
                return false;
            });
            if(data.status=="pay"){
            	this.target.find('.status').css('color','red').html("- "+data.score);
            }else if(data.status=="add"){
             	this.target.find('.status').css('color','green').html("+ "+data.score);
            }
            if(data.url==null || data.url==''){
            	this.target.find('.title').html(data.title);
            }else{
            	this.target.find('.title').html("<a target='_blank' href='"+data.url+"'>"+data.title+"</a>");
            }
        })).setJSON(page);
    });
</script>
<div style="margin-top:10px;">
	<table style="background:#E0E7ED;height:100px;width:100%;">
	    <tr>
	    	<td style="padding-left:120px;padding-top:10px;">
	    		<span>可用积分</span></br>
	    		<span style="color:green; font-size:24px;"><@s.property value="member.details.score" /></span>
	    	</td>
	        <td style="padding-left:120px;padding-top:10px;">
	        	<span>将要过期的积分</span></br>
	        	<span style="color:red; font-size:24px;">0</span>
	        </td>
	    </tr>
	</table>
</div>
<div style="margin-top:10px;">
<table id="view" class="formTable mb3 listTable">
     <thead>
	    <tr>
	    	<th>来源/用途</th>
	        <th>积分变化</th>
	        <th>创建日期</th>
	        <th>备注</th>
	    </tr>
    </thead>
    <tbody>
	    <tr align="center" class="template" name="default" >
	        <td class="title">
	        </td>
	        <td class="status"></td>
	        <td>
	            {createTime}
	        </td>
	        <td>{description}</td>
	    </tr>
	    <tr class="empty"><td class="norecord" colspan="4">暂无数据</td></tr>
    </tbody>
    <tfoot>
	    <tr>
	        <td colspan="4">
	            <div id="pager" class="paging digg"></div>
	        </td>
	    </tr>
    </tfoot>
</table>
</div>