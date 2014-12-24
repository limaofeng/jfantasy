<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function(){
        var list = $('#goodsParameter').list($('#goodsParameterForm'),<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(goods.customGoodsParameterValues)" escapeHtml="false" default="[]"/>);

    });
</script>

<div class="pad10L pad10R">
<div class="example-box">
<div class="tabs ui-tabs ui-widget ui-widget-content ui-corner-all">
<ul>
    <li>
        <a title="基本信息" href="#normal-tabs-1">
            基本信息
        </a>
    </li>
    <li>
        <a title="商品详情" href="#normal-tabs-2">
            商品详情
        </a>
    </li>
    <li>
        <a title="收货人信息" href="#normal-tabs-3">
            收货人信息
        </a>
    </li>
    <li>
        <a title="配送方式" href="#normal-tabs-4">
            配送方式
        </a>
    </li>
</ul>
<a href="javascript:;" class="btn small hover-black float-right back-page" title="" style="margin-top: -30px;margin-right: 30px">
    <i class="glyph-icon icon-reply"></i>
</a>
<div id="normal-tabs-1">
    <div class="row">
        <div class="col-md-6">
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        系统编号：
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-left">
                        <@s.property value="order.sn"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        支付状态：
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-left">
                        <@s.property value="order.paymentStatus.value"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        发货状态：
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-left">
                        <@s.property value="order.shippingStatus.value"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        物流公司：
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-left">
                        <@s.property value="order.deliveryType.defaultDeliveryCorp.name"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-6">
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        订单状态：
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-right">
                        <@s.property value="order.orderStatus.value"/>
                    </div>
                </div>
            </div>

            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        商品数量：
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-right">
                        <@s.property value="order.totalProductQuantity"/>
                    </div>
                </div>
            </div>

            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        订单总额：
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-right">
                        <@s.property value="order.totalAmount"/>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>
<div id="normal-tabs-2">
    <table class="table table-hover mrg5B text-center">
        <thead>
        <tr>
            <th>商品名称</th>
            <th>商品条码</th>
            <th>商品数量</th>
            <th>商品单价</th>
            <th>商品总价</th>
        </tr>
        </thead>
        <tbody>
            <@s.iterator var="item" value="order.orderItems">
                <tr align="center" class="template" name="default" >
                    <td><@s.property value="#item.name" /></td>
                    <td><@s.property value="#item.sn" /></td>
                    <td><@s.property value="#item.productQuantity" /></td>
                    <td>
                         <@s.text name="global.format.money">
                            <@s.param value="#item.productPrice" />
                        </@s.text>
                    </td>
                    <td>
                        <@s.text name="global.format.money">
                            <@s.param value="#item.productPrice*#item.productQuantity" />
                        </@s.text>
                    </td>
                </tr>
            </@s.iterator>
        </tbody>
    </table>
</div>
<div id="normal-tabs-3">
    <div class="row">
        <div class="col-md-6">
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        姓名：
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-left">
                        <@s.property value="order.shipName" />
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        邮编：
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-left">
                        <@s.property value="order.shipZipCode" />
                    </div>
                </div>
            </div>
        <@s.if test="order.memo!=null && order.memo!=''">
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        留言：
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-left">
                        <@s.property value="order.memo" />
                    </div>
                </div>
            </div>
        </@s.if>
        </div>
        <div class="col-md-6">
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        电话：
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-right">
                        <@s.if test="order.shipPhone!=null && order.shipPhone!=''">
                            <@s.property value="order.shipPhone" />
                        </@s.if>
                                        <@s.else>
                            <@s.property value="order.shipMobile" />
                        </@s.else>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-3">
                    <label for="">
                        地址：
                    </label>
                </div>
                <div class="form-input col-md-9">
                    <div class="append-right">
                        <@s.property value="order.shipAddress" />
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="normal-tabs-4">
    <table class="table table-hover mrg5B text-center">
        <thead>
        <tr>
            <th>配送方式</th>
            <th>配送费用</th>
            <th>配送类型</th>
            <th>默认物流</th>
        </tr>
        </thead>
        <tbody>
        <tr align="center" class="template" name="default" >
            <td><@s.property value="order.deliveryTypeName" /></td>
            <td>
                <@s.text name="global.format.money">
                                    <@s.param value="order.deliveryFee"/>
                                </@s.text>
            </td>
            <td>
                <@s.if test="order.deliveryType.method.toString() == 'deliveryAgainstPayment'">
                    先付款后发货
                </@s.if>
                <@s.elseif test="order.deliveryType.method.toString() == 'deliveryAgainstPayment'">
                    货到付款
                </@s.elseif>
            </td>
            <td><@s.property value="order.deliveryType.defaultDeliveryCorp.name" /></td>
        </tr>
        </tbody>
    </table>
</div>

</div>

<div class="form-row">
    <div class="form-input col-md-10">
        <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "  title="返回" >
                            <span class="glyph-icon icon-separator">
                                  <i class="glyph-icon icon-reply"></i>
                            </span>
                             <span class="button-content">
                                    返回
                             </span>
        </a>
    </div>
</div>
</div>
</div>
