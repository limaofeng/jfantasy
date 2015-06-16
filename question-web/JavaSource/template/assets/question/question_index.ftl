<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function(){
        var $advsearch = $('.propertyFilter').advsearch({
            filters : [{
                name : 'S_title',
                text : '标题',
                type : 'input',
                matchType :['EQ','LIKE']
            },{
                name : 'D_createTime',
                text : '创建时间',
                type : 'input',
                matchType :['LT','GT']
            }]
        });
        //列表初始化
        var pager=<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(pager)" escapeHtml="false"/>;
        var $grid = $('#view').dataGrid($('#searchFormPanel'),$('.batch'));
        $grid.data('grid').view().on('add',function(data){
            //批量删除
            this.target.find('.delete').click(function(e){
                deleteMethod([data.id]);
                return stopDefault(e);
            });
            //批量打开
           /* this.target.find('.issue').click(function(e){
                deleteIssue([data.id]);
                return stopDefault(e);
            });*/
            //批量关闭
            this.target.find('.batchnoIssue').click(function(e){
                deletenoIssue([data.id]);
                return stopDefault(e);
            });
        });
        $grid.setJSON(pager);
        var deleteMethod = $('.batchDelete').batchExecute($("#allChecked"),$grid.data('grid').pager(),'id','确认要删除问题[{title}]？',function(){
            $.msgbox({
                msg : "删除成功!",
                type : "success"
            });
        });

     /*   var deleteIssue = $('.batchIssue').batchExecute($("#allChecked"),$grid.data('grid').pager(),'id','确认要打开问题[{title}]？',function(){
            $.msgbox({
                msg : "打开成功!",
                type : "success"
            });
        });*/

        var deletenoIssue = $('.batchnoIssue').batchExecute($("#allChecked"),$grid.data('grid').pager(),'id','确认要关闭问题[{title}]？',function(){
            $.msgbox({
                msg : "关闭成功!",
                type : "success"
            });
        });
        $("#batchMove").click(function(){
            moveCategoryTree.setJSON(categoryTree.getNodes());
            $( "#basic-dialog" ).dialog({
                modal: !0,
                dialogClass: "modal-dialog",
                overlayClass: "bg-white opacity-60",
                buttons: [
                    {
                        text: "确定",
                        click: function() {
                            if (moveCategoryTree.getSelectedNodes().length == 0) {
                                $.msgbox({
                                    msg: "请先选择一个分类",
                                    type: "warning"
                                });
                                return  false;
                            }
                            var selectNode = moveCategoryTree.getSelectedNodes();
                            var categoryId = selectNode[0].id;
                            var ids = $('.id[type="checkbox"]:checked').vals();
                            $.post('${request.contextPath}/question/move.do',{ids:ids,categoryId:categoryId},function(){
                                top.$.msgbox({
                                    msg : "移动成功",
                                    type : "success",
                                    callback:function(){
                                        $('#pager').pager().reload();
                                    }
                                });
                            });
                            $( this ).dialog( "close" );
                        }
                    }
                ]
            });
        });

    });
</script>
<div id="searchFormPanel" class="button-panel pad5A">
<@s.form id="searchForm" namespace="/question" action="search" method="post">
    <@s.hidden name="EQL_category.id" value="%{category.id}"/>
    <a title="问题添加" class="btn medium primary-bg dd-add" href="<@s.url namespace="/question" action="add?id=%{category.id}"/>" target="after:closest('#page-content')" ><#-- style="visibility: hidden"-->
            <span class="button-content">
                <i class="glyph-icon icon-plus float-left"></i>
                问题添加
            </span>
    </a>
    <div class="propertyFilter"></div>
    <div class="form-search">
        <input type="text" name="LIKES_title" title="" data-placement="bottom" class="input tooltip-button ac_input" placeholder="Search..." autocomplete="off" style="display: inline-block; width: 200px;">
        <i class="glyph-icon icon-search"></i>
    </div>
</@s.form>
</div>
<div class="batch">
    <a title="批量删除" class="btn small primary-bg batchDelete" href="<@s.url namespace="/question" action="delete"/>">
        <span class="button-content">
            <i class="glyph-icon  icon-trash  float-left"></i>
           批量删除
        </span>
    </a>
   <#-- <a class="btn small primary-bg batchIssue " title="批量打开"  href="<@s.url namespace="/question" action="run"/>">
        <span class="button-content">
            <i class="glyph-icon icon-rss float-left"></i>
             批量打开
        </span>
    </a>-->
    <a class="btn small primary-bg batchnoIssue" title="批量关闭"  href="<@s.url namespace="/question" action="close"/>">
        <span class="button-content">
            <i class="glyph-icon icon-ban-circle float-left"></i>
            批量关闭
        </span>
    </a>
    <a href="javascript:void(0)" class="small primary-bg btn " title="批量移动" id="batchMove">
        <span class="button-content">
            <i class="glyph-icon icon-move float-left"></i>
            批量移动
        </span>
    </a>
</div>
<table id="view" class="table table-hover mrg5B text-center">
    <thead>
    <tr>
        <th class="pad15L" style="width:20px;">
            <input id="allChecked" class="custom-checkbox bg-white" checkAll=".id" type="checkbox" <#--checktip="{message:'您选中了{num}条记录',tip:'#config_check_info'}"--> />
        </th>
        <th class="sort" orderBy="id" style="text-align: center;">问题标题</th>
        <th style="width: 140px;">问题分类</th>
        <th class="sort" style="width:120px" orderBy="size">回答数量</th>
        <th class="text-center actions">操作</th>
    </tr>
    </thead>
    <tbody>
    <tr class="template" name="default">
        <td class="pad5R"><input class="id custom-checkbox" type="checkbox" value="{id}"/></td>
        <td>
            <h4 class="infobox-title"> {title}</h4>
            <p><span class="label bg-purple mrg5R">{lastTime}</span> {askQuestion}</p>
        </td>
        <td>{category.name}</td>
        <td>{size}</td>
        <td class="pad0T pad0B text-center">
            <div class="dropdown actions">
                <a href="javascript:;" title="" class="btn medium hover-black" data-toggle="dropdown">
                    <span class="button-content">
                        <i class="glyph-icon font-size-11 icon-cog"></i>
                        <i class="glyph-icon font-size-11 icon-chevron-down"></i>
                    </span>
                </a>
                <ul class="dropdown-menu float-right">
                 <#--   <li>
                        <a href="<@s.url namespace="/question" action="view?id={id}&categoryId=%{category.id}"/>" class="view" title="" target="after:closest('#page-content')">
                            <i class="glyph-icon icon-external-link-sign mrg5R"></i>
                            详情
                        </a>
                    </li>-->
                    <li>
                        <a title="回答问题" class="edit" href="<@s.url namespace="/question" action="askquestion?id={id}&categoryId=%{category.id}"/>" target="after:closest('#page-content')">
                            <i class="glyph-icon icon-edit mrg5R"></i>
                            回答问题
                        </a>
                    </li>
                    <li class="divider"></li>
                    <li>
                        <a href="<@s.url namespace="/question" action="delete?id={id}"/>" class="font-red delete" title="">
                            <i class="glyph-icon icon-remove mrg5R"></i>
                           删除
                        </a>
                    </li>
                </ul>
            </div>
        </td>
    </tr>
    </tbody>
</table>
<div class="divider mrg0T"></div>
