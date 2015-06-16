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
                name : 'S_releaseDate',
                text : '发布时间',
                type : 'input',
                matchType :['EQ','LIKE']
            }]
        });
        //列表初始化
        var pager=<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(pager)" escapeHtml="false"/>;
        var $grid = $('#view').dataGrid($('#searchFormPanel'),$('.batch'));
        $grid.data('grid').view().on('add',function(data){
            this.target.find('.delete').click(function(e){
                deleteMethod([data.id]);
                return stopDefault(e);
            });

            this.target.find('.issue').click(function(e){
                deleteIssue([data.id]);
                return stopDefault(e);
            });
            this.target.find('.batchnoIssue').click(function(e){
                deletenoIssue([data.id]);
                return stopDefault(e);
            });


        });
        $grid.setJSON(pager);
        var deleteMethod = $('.batchDelete').batchExecute($("#allChecked"),$grid.data('grid').pager(),'id','确认要删除文章[{title}]？',function(){
            $.msgbox({
                msg : "删除成功!",
                type : "success"
            });
        });

        var deleteIssue = $('.batchIssue').batchExecute($("#allChecked"),$grid.data('grid').pager(),'id','确认要发布文章[{title}]？',function(){
            $.msgbox({
                msg : "发布成功!",
                type : "success"
            });
        });

        var deletenoIssue = $('.batchnoIssue').batchExecute($("#allChecked"),$grid.data('grid').pager(),'id','确认要关闭文章[{title}]？',function(){
            $.msgbox({
                msg : "关闭成功!",
                type : "success"
            });
        });

        $('.dd-add').click(function(){
            if (categoryTree.getSelectedNodes().length == 0) {
                $.msgbox({
                    msg: "请先选择一个分类",
                    type: "warning"
                });
                return false;
            }
            if (!$(this).data('href')) {
                $(this).data('href', $(this).attr('href'));
            }
            var selectdNodes = categoryTree.getSelectedNodes();
            if (selectdNodes.length == 1) {
                var treeNode = selectdNodes[0];
                var categoryCode = treeNode.code;
                $(this).attr('href', $(this).data('href') + '?categoryCode=' + categoryCode);
            }

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
                            var categoryCode = selectNode[0].code;
                            var ids = $('.id[type="checkbox"]:checked').vals();
                            $.post('${request.contextPath}/cms/article/move.do',{ids:ids,categoryCode:categoryCode},function(){
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
<@s.form id="searchForm" namespace="/cms/article" action="search" method="post">
    <@s.hidden name="EQS_category.code" value="%{category.code}"/>
    <a title="<@s.text name='operation.add.article' />" class="btn medium primary-bg dd-add" href="<@s.url namespace="/cms/article" action="article_add"/>" target="after:closest('#page-content')">
        <span class="button-content">
            <i class="glyph-icon icon-plus float-left"></i>
            <@s.text name='operation.add.article' />
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
    <a title="<@s.text name='operation.remove.batch' />" class="btn small primary-bg batchDelete" href="<@s.url namespace="/cms/article" action="article_delete"/>">
        <span class="button-content">
            <i class="glyph-icon  icon-trash  float-left"></i>
        <@s.text name='operation.remove.batch' />
        </span>
    </a>
    <a class="btn small primary-bg batchIssue " title="<@s.text name='operation.issue.batch' />"  href="<@s.url namespace="/cms/article" action="article_issue"/>">
        <span class="button-content">
            <i class="glyph-icon icon-rss float-left"></i>
            <@s.text name='operation.issue.batch' />
        </span>
    </a>
    <a class="btn small primary-bg batchnoIssue" title="<@s.text name='operation.unissue.batch' />"  href="<@s.url namespace="/cms/article" action="article_colse"/>">
        <span class="button-content">
            <i class="glyph-icon icon-ban-circle float-left"></i>
        <@s.text name='operation.unissue.batch' />
        </span>
    </a>
    <a href="javascript:void(0)" class="small primary-bg btn " title="<@s.text name='operation.move.batch' />" id="batchMove">
        <span class="button-content">
            <i class="glyph-icon icon-move float-left"></i>
            <@s.text name='operation.move.batch' />
        </span>
    </a>
</div>
<table id="view" class="table table-hover mrg5B text-center">
    <thead>
    <tr>
        <th class="pad15L" style="width:20px;">
            <input id="allChecked" class="custom-checkbox bg-white" checkAll=".id" type="checkbox" <#--checktip="{message:'您选中了{num}条记录',tip:'#config_check_info'}"--> />
        </th>
        <th><@s.text name='article.title' /></th>
        <th class="sort" orderBy="issue"><@s.text name='article.issue' /></th>
        <th class="sort" orderBy="releaseDate"><@s.text name='article.issue.date' /></th>
        <th class="text-center"><@s.text name='operation' /></th>
    </tr>
    </thead>
    <tbody>
    <tr class="template" name="default">
        <td class="pad5R"><input class="id custom-checkbox" type="checkbox" value="{id}"/></td>
        <td class="font-bold pad5L">{title} </td>
        <td>{issue:dict({'true':'<@s.text name='article.issue.true' />','false':'<@s.text name='article.issue.false' />'})}</td>
        <td>{releaseDate:date('yyyy-MM-dd')}</td>
        <td class="pad0T pad0B text-center">
            <div class="dropdown actions">
                <a href="javascript:;" title="" class="btn medium hover-black" data-toggle="dropdown">
                    <span class="button-content">
                        <i class="glyph-icon font-size-11 icon-cog"></i>
                        <i class="glyph-icon font-size-11 icon-chevron-down"></i>
                    </span>
                </a>
                <ul class="dropdown-menu float-right">
                    <li>
                        <a href="<@s.url namespace="/cms/article" action="article_view?id={id}"/>" class="view" title="" target="after:closest('#page-content')">
                            <i class="glyph-icon icon-external-link-sign mrg5R"></i>
                            <@s.text name='operation.view' />
                        </a>
                    </li>
                    <li>
                        <a title="" class="edit" href="<@s.url namespace="/cms/article" action="article_edit?id={id}"/>" target="after:closest('#page-content')">
                            <i class="glyph-icon icon-edit mrg5R"></i>
                            <@s.text name='operation.edit' />
                        </a>
                    </li>
                    <li class="divider"></li>
                    <li>
                        <a href="<@s.url namespace="/cms/article" action="article_delete?id={id}"/>" class="font-red delete" title="">
                            <i class="glyph-icon icon-remove mrg5R"></i>
                            <@s.text name='operation.remove' />
                        </a>
                    </li>
                </ul>
            </div>
        </td>
    </tr>
    </tbody>
</table>
<div class="divider mrg0T"></div>
