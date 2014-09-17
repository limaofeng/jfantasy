<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function () {
        $('.Tabcurr').click();
        //购物车列表
        var $cartItemPager = Fantasy.decode('<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(cartItemPager)" escapeHtml="false"/>');
        $('#cartItemPager').pager($('#cartsearchForm'), $cartItemPager.pageSize, $('#cart_view').view()).setJSON($cartItemPager);
        //订单列表
        var $orderPager = Fantasy.decode('<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(orderPager)" escapeHtml="false"/>');
        $('#orderPager').pager($('#searchForm'), $orderPager.pageSize, $('#order_view').view()).setJSON($orderPager);
        //收货地址
        $('#receiver_view').view().setJSON(Fantasy.decode('<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(receivers)" escapeHtml="false"/>'));
    });
</script>
<table class="formTable mb3">
    <tbody>
	<tr>
		<td class="opt-title">
			<span>会员详细信息</span>
			<ul class="Tabtable" tabs="{selectedClass:'Tabcurr',event:'click'}">
	    		<li><a href="#" tab="#account">账号信息</a></li>
			    <li><a href="#" tab="#basic">基本信息</a></li>
			    <li><a tab="#cart_view" href="#">购物车</a></li>
				<li><a tab="#order_view" href="#">订单</a></li>
				<li><a tab="#receiver_view" href="#">收货地址</a></li>
				<li><a tab="#point_view" href="${request.contextPath}/admin/member/point.do?id=<@s.property value='member.id'/>" ajax="{type:'html',target:'#point_view',otherSettings:{loadType:'html'}}">积分详情</a></li>
			</ul>
			<a href="###" class="back-page" style="float:right;padding-right:50px;">返回会员列表>></a>
			<div style="clear:both;BORDER-BOTTOM:1PX SOLID #6483A4;"></div>
			<table id="account" class="formTable mb3">
		        <tbody>
		        <tr>
		            <td class="formItem_title w100">用户账号:</td>
		            <td class="formItem_content"><@s.property value="member.username"/></td>
		            <td class="formItem_title w100">登陆密码 :</td>
		            <td class="formItem_content">******</td>
		        </tr>
		        <tr>
		            <td class="formItem_title">用户显示昵称:</td>
		            <td class="formItem_content"><@s.property value="member.nickName"/></td>
		            <td class="formItem_title ">是否启用:</td>
		            <td class="formItem_content"><@s.property value="%{member.enabled?'是':'否'}" /></td>
		        <tr>
		        <#--
		            <td class="formItem_title ">未过期:</td>
		            <td class="formItem_content"><@s.property value="%{member.accountNonExpired?'是':'否'}" /></td>
		        </tr>
		        <tr>
		            <td class="formItem_title ">未锁定:</td>
		            <td class="formItem_content"><@s.property value="%{member.accountNonLocked?'是':'否'}"/></td>
		            <td class="formItem_title ">未失效:</td>
		            <td class="formItem_content"><@s.property value="%{member.credentialsNonExpired?'是':'否'}" /></td>
		        </tr>
		         -->
		        </tbody>
		    </table>
		    <table id="basic" class="formTable mb3">
		        <tbody>
		        <tr>
		            <td class="formItem_title w100">姓名:</td>
		            <td class="formItem_content"><@s.property value="member.details.name"/></td>
		            <td class="formItem_title w100">性别:</td>
		            <td class="formItem_content">
		                <@s.property value="member.details.sex.value"/>
		            </td>
		        </tr>
		        <tr>
		            <td class="formItem_title ">生日:</td>
		            <td class="formItem_content"><@s.date name="member.details.birthday" format="yyyy-MM-dd"/></td>
		            <td class="formItem_title ">移动电话:</td>
		            <td class="formItem_content"><@s.property  value="member.details.mobile" /></td>
		        </tr>
		        <tr>
		            <td class="formItem_title ">固定电话:</td>
		            <td class="formItem_content"><@s.property  value="member.details.tel"/></td>
		            <td class="formItem_title ">E-mail:</td>
		            <td class="formItem_content">
		                <@s.property  value="member.details.email" />
		                <@s.if test="member.details.email"><span style="margin-left:15px;">已验证</span></@s.if>
		            </td>
		        </tr>
		        <tr>
		            <td class="formItem_title ">是否为vip会员:</td>
		            <td class="formItem_content"><@s.property value="%{member.details.vip?'是':'否'}" /></td>
		            <td class="formItem_title ">积分:</td>
		            <td class="formItem_content"><@s.property value="member.details.score" /></td>
		        </tr>
		        <tr>
		            <td class="formItem_title ">网址:</td>
		            <td class="formItem_content" colspan="3"><@s.property value="member.details.website" /></td>
		        </tr>
		        <tr>
		            <td class="formItem_title ">描述信息:</td>
		            <td class="formItem_content" colspan="3"><@s.property value="member.details.description" /></td>
		        </tr>
		        </tbody>
		    </table>
		    <form id="cartsearchForm" action="${request.contextPath}/admin/mall/order/search.do" method="post">
		        <@s.hidden name="EQS_cart.owner" value="member.username" />
		    </form>
		    <table id="cart_view" class="formTable mb3 listTable">
		        <thead>
		        <tr>
		            <th style="width:30px;"></th>
		            <th class="w300">商品名称</th>
		            <th class="w150">数量</th>
		            <th class="w150">价格</th>
		            <th class="w150">成本价</th>
		            <th class="w150">市场价</th>
		            <th>重量</th>
		        </tr>
		        </thead>
		        <tbody>
		        <tr align="center" class="template" name="default">
		            <td></td>
		            <td>
		                <a style="float:left;padding-left: 20px;"
		                   href="${request.contextPath}/admin/mall/goods/goods_view.do?id={product.goods.id}"
		                   ajax="{type:'html',target:'closest(\'#member-view\')'}">{product.name}</a>
		            </td>
		            <td>{quantity}</td>
		            <td>{price:number('0.00')}</td>
		            <td>{product.cost:number('0.00')}</td>
		            <td>{product.marketPrice:number('0.00')}</td>
		            <td>{product.weight}</td>
		        <tr class="empty">
		            <td class="norecord" colspan="7">暂无数据</td>
		        </tr>
		        </tbody>
		        <tfoot>
		        <tr>
		            <td colspan="7">
		                <div id="cartItemPager" class="paging digg"></div>
		            </td>
		        </tr>
		        </tfoot>
		    </table>
		    <#--订单开始 -->
		    <form id="searchForm" action="${request.contextPath}/admin/mall/order/search.do" method="post">
		        <@s.hidden name="EQL_member.id" value="member.id" />
		    </form>
		    <table id="order_view" class="formTable mb3 listTable">
		        <thead>
		        <tr>
		            <th style="width:30px;"></th>
		            <th style="width:220px;">订单编号</th>
		            <th>订单总金额</th>
		            <th>下单时间</th>
		        </tr>
		        </thead>
		        <tbody>
		        <tr align="center" class="template" name="default">
		            <td></td>
		            <td>
		                <a style="float:left;" ajax="{target:'closest(\'#member-view\')'}"
		                   href="${request.contextPath}/admin/mall/order/view.do?id={id}">{sn}</a>
		            </td>
		            <td>{totalProductPrice:number('0.00')}</td>
		            <td>{createTime:date('yyyy-MM-dd hh:mm')}</td>
		        </tr>
		        <tr class="empty">
		            <td class="norecord" colspan="4">暂无数据</td>
		        </tr>
		        </tbody>
		        <tfoot>
		        <tr>
		            <td colspan="4">
		                <div id="orderPager" class="paging digg"></div>
		            </td>
		        </tr>
		        </tfoot>
		    </table>
			<#--订单结束 -->
			<#--收货地址 -->
		    <table id="receiver_view" class="formTable mb3 listTable">
		        <thead>
		        <tr>
		            <th class="w200">收货人姓名</th>
		            <th class="w150">地区存储</th>
		            <th class="w200">收货地址</th>
		            <th class="w100">邮政编码</th>
		            <th class="w150">电话</th>
		            <th class="w150">手机</th>
		            <th>默认地址</th>
		        </tr>
		        </thead>
		        <tbody>
		        <tr align="center" class="template" name="default">
		            <td>{name}</td>
		            <td>{area.displayName}</td>
		            <td>{address}</td>
		            <td>{zipCode}</td>
		            <td>{phone}</td>
		            <td>{mobile}</td>
		            <td>{isDefault:dict({true:'是',false:'否'})}</td>
		        </tr>
		        <tr class="empty">
		            <td class="norecord" colspan="7">暂无数据</td>
		        </tr>
		        </tbody>
		    </table>
			<#--积分详情 -->
			<div id="point_view"></div>
		</td>
	</tr>
	</tbody>
</table>