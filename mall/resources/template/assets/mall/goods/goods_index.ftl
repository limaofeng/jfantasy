<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function(){
        var $advsearch = $('.propertyFilter').advsearch({
            filters : [{
                name : 'S_sn',
                text : '编号',
                type : 'input',
                matchType :['EQ','LIKE']
            },{
                name : 'S_name',
                text : '名称',
                type : 'input',
                matchType :['EQ','LIKE']
            },{
                name : 'S_brand.name',
                text : '品牌名称',
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

            //批量上架
            this.target.find('.batchUp').click(function(e){
                upMethod([data.id]);
                return stopDefault(e);
            });
            //批量下架
            this.target.find('.batchDown').click(function(e){
                downMethod([data.id]);
                return stopDefault(e);
            });

        });
        $grid.setJSON(pager);
        var deleteMethod = $('.batchDelete').batchExecute($("#allChecked"),$grid.data('grid').pager(),'id','确认要删除商品[{sn}]？',function(){
            $.msgbox({
                msg : "删除成功!",
                type : "success"
            });
        });
        var upMethod = $('.batchUp').batchExecute($("#allChecked"),$grid.data('grid').pager(),'id','确认要上架商品[{sn}]？',function(){
            $.msgbox({
                msg : "上架成功!",
                type : "success"
            });
        });
        var downMethod = $('.batchDown').batchExecute($("#allChecked"),$grid.data('grid').pager(),'id','确认要下架商品[{sn}]？',function(){
            $.msgbox({
                msg : "下架成功!",
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
                            $.post('${request.contextPath}/mall/goods/move.do',{ids:ids,categoryId:categoryId},function(){
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
<@s.form id="searchForm" namespace="/mall/goods" action="search" method="post">
    <@s.hidden name="EQL_category.id" value="%{category.id}"/>
    <a title="<@s.text name='mall.goods.addTitle'/>" class="btn medium primary-bg dd-add" href="<@s.url namespace="/mall/goods" action="add?id=%{category.id}"/>" target="after:closest('#page-content')">
            <span class="button-content">
                <i class="glyph-icon icon-plus float-left"></i>
                <@s.text name='mall.goods.addTitle'/>
            </span>
    </a>
    <div class="propertyFilter"></div>
    <div class="form-search">
        <input type="text" name="LIKES_name_OR_sn" title="" data-placement="bottom" class="input tooltip-button ac_input" placeholder="Search..." autocomplete="off" style="display: inline-block; width: 200px;">
        <i class="glyph-icon icon-search"></i>
    </div>
</@s.form>
</div>
<div class="batch">
    <a title="<@s.text name='mall.goods.batchDel'/>" class="btn small primary-bg batchDelete" href="<@s.url namespace="/mall/goods" action="delete"/>">
        <span class="button-content">
            <i class="glyph-icon  icon-trash  float-left"></i>
            <@s.text name='mall.goods.batchDel'/>
        </span>
    </a>
    <a title="<@s.text name='mall.goods.batchUp'/>" class="btn small primary-bg batchUp" href="<@s.url namespace="/mall/goods" action="goodsUp"/>">
        <span class="button-content">
            <i class="glyph-icon  icon-long-arrow-up  float-left"></i>
            <@s.text name='mall.goods.batchUp'/>
        </span>
    </a>
    <a title="<@s.text name='mall.goods.batchDown'/>" class="btn small primary-bg batchDown" href="<@s.url namespace="/mall/goods" action="goodsDown"/>">
        <span class="button-content">
            <i class="glyph-icon  icon-long-arrow-down  float-left"></i>
            <@s.text name='mall.goods.batchDown'/>
        </span>
    </a>
    <a href="javascript:void(0)" class="small primary-bg btn " title="<@s.text name='mall.goods.batchMove'/>" id="batchMove">
        <span class="button-content">
            <i class="glyph-icon icon-move float-left"></i>
            <@s.text name='mall.goods.batchMove'/>
        </span>
    </a>
</div>
<table id="view" class="table table-hover mrg5B text-center">
    <thead>
    <tr>
        <th class="pad15L" style="width:20px;">
            <input id="allChecked" class="custom-checkbox bg-white" checkAll=".id" type="checkbox" <#--checktip="{message:'您选中了{num}条记录',tip:'#config_check_info'}"--> />
        </th>
        <th class="sort"><@s.text name='mall.goods.title'/></th>
        <th class="sort" style="width:100px"><@s.text name='mall.goods.price'/></th>
        <th class="sort" style="width:80px"><@s.text name='mall.goods.marketable'/></th>
        <th class="text-center actions"><@s.text name='mall.goods.handle'/></th>
    </tr>
    </thead>
    <tbody>
    <tr class="template" name="default">
        <td class="pad5R"><input class="id custom-checkbox" type="checkbox" value="{id}"/></td>
        <td class="pad5L">
            <div class="large btn info-icon float-left mrg5R">
                <img data-src="holder.js/38x38/simple" class="img-small view-field" name="defaultGoodsImagePath"/>
            </div>
            <h4 class="infobox-title">[{sn}] {name}</h4>
            <p><span class="label bg-purple mrg5R">{brand.name}</span> </p>
        </td>
        <td>{price}</td>
        <td>{marketable:dict({'true':'<@s.text name='mall.goods.true'/>','false':'<@s.text name='mall.goods.false'/>'})}</td>
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
                        <a href="<@s.url namespace="/mall/goods" action="view?id={id}&categoryId=%{category.id}"/>" class="view" title="" target="after:closest('#page-content')">
                            <i class="glyph-icon icon-external-link-sign mrg5R"></i>
                            <@s.text name='mall.goods.view'/>
                        </a>
                    </li>
                    <li>
                        <a title="" class="edit" href="<@s.url namespace="/mall/goods" action="edit?id={id}&categoryId=%{category.id}"/>" target="after:closest('#page-content')">
                            <i class="glyph-icon icon-edit mrg5R"></i>
                        <@s.text name='mall.goods.edit'/>
                        </a>
                    </li>
                    <li class="divider"></li>
                    <li>
                        <a href="<@s.url namespace="/mall/goods" action="delete?id={id}"/>" class="font-red delete" title="">
                            <i class="glyph-icon icon-remove mrg5R"></i>
                            <@s.text name='mall.goods.delete'/>
                        </a>
                    </li>
                </ul>
            </div>
        </td>
    </tr>
    </tbody>
</table>
<div class="divider mrg0T"></div>
