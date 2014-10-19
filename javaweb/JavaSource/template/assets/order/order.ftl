<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="pageTitle">
订单维护
<small>
    Order
</small>
</@override>
<@override name="head">
<script type="text/javascript">
    $(function(){
        //当浏览器窗口发生变化时,自动调整布局的js代码
        var _resize = function () {
            var _$gridPanel = $('.grid-panel');
            if(!!_$gridPanel.length){
                _$gridPanel.css('minHeight', $(window).height() - (_$gridPanel.offset().top + 15));
                _$gridPanel.triggerHandler('resize');
            }
        };
        $(window).resize(_resize);
        $page$.one('destroy',function(){
            $(window).unbind('resize',_resize);
        });
        var $advsearch = $('.propertyFilter').advsearch({
            filters : [{
                name : 'S_code',
                text : '编码',
                type : 'input',
                matchType :['EQ','LIKE','LT','GT']
            },{
                name : 'S_name',
                text : '名称',
                type : 'input',
                matchType :['EQ','LIKE','GT']
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
        });
        $grid.setJSON(pager);
        var deleteMethod = $('.batchDelete').batchExecute($("#allChecked"),$grid.data('grid').pager(),'id','是否确认删除[{sn}]订单？',function(){
            $.msgbox({
                msg : "删除成功!",
                type : "success"
            });
        });
    });
</script>
</@override>
<@override name="pageContent">
<div id="searchFormPanel" class="button-panel pad5A">
    <@s.form id="searchForm" namespace="/order" action="search" method="post">
        <a title="<@s.text name= 'mall.order.index.add' />" class="btn medium primary-bg dd-add" href="<@s.url namespace="/order" action="add"/>" ajax="{type:'html',target:'closest(\'#page-content\')'}">
            <span class="button-content">
                <i class="glyph-icon icon-plus float:left"></i>
                <@s.text name= 'mall.order.index.add' />
            </span>
        </a>
        <div class="propertyFilter">
        </div>
        <div class="form-search">
            <input type="text" name="LIKES_sn" title="" data-placement="bottom" class="input tooltip-button ac_input" placeholder="Search..." autocomplete="off" style="display: inline-block; width: 200px;">
            <i class="glyph-icon icon-search"></i>
        </div>
    </@s.form>
</div>
<div class="batch">
    <a title="<@s.text name= 'mall.order.index.batchDel' />" class="btn small primary-bg batchDelete" href="<@s.url namespace="/order" action="delete"/>">
        <span class="button-content">
            <i class="glyph-icon icon-trash float-left"></i>
            <@s.text name= 'mall.order.index.batchDel' />
        </span>
    </a>
</div>
<div class="grid-panel">
    <table id="view" class="table table-hover mrg5B text-center">
        <thead>
        <tr>
            <th class="pad15L" style="width:20px;">
                <input id="allChecked" class="custom-checkbox bg-white" checkAll=".id" type="checkbox" <#--checktip="{message:'您选中了{num}条记录',tip:'#config_check_info'}"--> />
            </th>
            <th class="sort" orderBy="sn"><@s.text name= 'mall.order.index.sn' /></th>
            <th><@s.text name= 'mall.order.index.orderStatus' /></th>
            <th><@s.text name= 'mall.order.index.paymentStatus' /></th>
            <th><@s.text name= 'mall.order.index.totalAmount' /></th>
            <th><@s.text name= 'mall.order.index.feature' /></th>
            <th class="text-center"><@s.text name= 'mall.order.index.handle' /></th>
        </tr>
        </thead>
        <tbody>
        <tr class="template" name="default">
            <td><input class="id custom-checkbox" type="checkbox" value="{id}"/></td>
            <td class="font-bold">{sn} </td>
            <td>{orderStatus:dict({'unprocessed':'<@s.text name= 'mall.order.index.orderStatus.unprocessed' />','processed':'<@s.text name= 'mall.order.index.orderStatus.processed' />','completed':'<@s.text name= 'mall.order.index.orderStatus.completed' />','invalid':'<@s.text name= 'mall.order.index.orderStatus.invalid' />'})}</td>
            <td>{paymentStatus:dict({'unpaid':'<@s.text name= 'mall.order.index.paymentStatus.unpaid' />','partPayment':'<@s.text name= 'mall.order.index.paymentStatus.partPayment' />','paid':'<@s.text name= 'mall.order.index.paymentStatus.paid' />','partRefund':'<@s.text name= 'mall.order.index.paymentStatus.partRefund' />','refunded':'<@s.text name= 'mall.order.index.paymentStatus.refunded' />'})}</td>
            <td>{totalAmount}</td>
            <td>{feature:dict({'singleton':'<@s.text name= 'mall.order.index.feature.singleton' />','singleItem':'<@s.text name= 'mall.order.index.feature.singleItem' />','more':'<@s.text name= 'mall.order.index.feature.more' />'})}</td>
            <td class="pad0T pad0B text-center">
                <div class="dropdown actions">
                    <a href="javascript:;" title="" class="btn medium bg-blue" data-toggle="dropdown">
                        <span class="button-content">
                            <i class="glyph-icon font-size-11 icon-cog"></i>
                            <i class="glyph-icon font-size-11 icon-chevron-down"></i>
                        </span>
                    </a>
                    <ul class="dropdown-menu float-right">
                        <li>
                            <a title="<@s.text name= 'mall.order.index.detail' />" class="view" href="<@s.url namespace="/order" action="view?id={id}"/>" ajax="{type:'html',target:'closest(\'#page-content\')'}">
                                <i class="glyph-icon icon-external-link-sign mrg5R"></i>
                                <@s.text name= 'mall.order.index.detail' />
                            </a>
                        </li>
                        <li>
                            <a title="<@s.text name= 'mall.order.index.edit' />" class="edit" href="<@s.url namespace="/order" action="edit?id={id}"/>" ajax="{type:'html',target:'closest(\'#page-content\')'}">
                                <i class="glyph-icon icon-edit mrg5R"></i>
                                <@s.text name= 'mall.order.index.edit' />
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="<@s.url namespace="/order" action="delete?id={id}"/>" class="font-red delete" title="<@s.text name= 'mall.order.index.del' />">
                                <i class="glyph-icon icon-remove mrg5R"></i>
                                <@s.text name= 'mall.order.index.del' />
                            </a>
                        </li>
                    </ul>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<div class="divider mrg0T"></div>
</@override>
<@extends name="../wrapper.ftl"/>