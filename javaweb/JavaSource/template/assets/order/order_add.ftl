<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<style>

    li, ul {
        list-style: none;
        list-style-type: none;
        padding: 0px;
        margin: 0px;
    }
    .ul-terms{padding:10px 5px; font-family:"Microsoft YaHei";}
    .ul-terms li {
        float: left;
        font-size: 12px;

    }
    .ul-terms-list1{width: 140px; color: #666;}
    .ul-terms-list2{width: 350px;padding-right: 50px; line-height: 20px; color: #666;}
    .ul-terms-list3{width: 70px;padding-right: 10px;font:12px/1.5 tahoma,arial; font-weight: 700; color: #f40;}
    .ul-terms-list4{width: 100px; color: #666;}
    .ul-terms-list03{font-weight:normal;color:#555;}
    .ul-terms-list5{width: 50px; color: #666;}
    .TxtCenter{text-align: center;}
    .ul-4-pad{padding-top:40px;}
    .line-h{line-height: 106px;}
    .ul-terms-list4 > small{font-size: 14px;}
    .btn-a>span{ text-align: right; display: block;}
    .btn-a a{border:1px solid #f40; padding:3px 5px; font-size: 12px;}
    .jgjgjg{font:20px/1.5 Tahoma,arial; font-weight: 700; color: #f40;padding:0px 10px;}
</style>

<script type="text/javascript">
    $(function(){
        window.orderProductsView = $('#order_products_view').view().on('remove',function(){
            var _totalProductPrice = 0;
            orderProductsView.each(function(){
                _totalProductPrice += this.getValue('subtotalPrice');
            });
            $('.totalProductPrice').html(Fantasy.number(_totalProductPrice,'0.00'));
        }).on("add",function(data){
            var _this = this;
            this.target.find('.smallNum').click(function(){
                var productQuantity = _this.getValue('productQuantity');
                if(productQuantity=='' || productQuantity<=1){
                    _this.setValue('productQuantity','1');
                }else{
                    _this.setValue('productQuantity',(productQuantity*1-1).toString());
                }
                _this.get('productQuantity').target.change();
            })
            this.target.find('.bigNum').click(function(){
                var productQuantity = _this.getValue('productQuantity');
                if(productQuantity=='' ||  productQuantity<1){
                    _this.setValue('productQuantity','1');
                }else{
                    _this.setValue('productQuantity',(productQuantity*1+1).toString());
                }
                _this.get('productQuantity').target.change();
            })
            this.get('productQuantity').target.change(function(){
                var productQuantity = _this.getValue('productQuantity');
                if(productQuantity=='' || !/^[0-9]+$/.test(productQuantity) || productQuantity<=1 || productQuantity>data.availableStock){
                    _this.setValue('productQuantity','1');
                    if(productQuantity>data.availableStock){
                        top.$.msgbox({
                            msg : "库存数量不足，最大库存数量为："+data.availableStock,
                            icon : "error"
                        });
                    }
                }
                // 商品总价
                var calPrice = Fantasy.multiply(data.productPrice,_this.getValue('productQuantity'));
                _this.setValue('subtotalPrice',Fantasy.number(calPrice,'0.00').toString());
                var _totalProductPrice = 0;
                orderProductsView.each(function(){
                    _totalProductPrice += this.getValue('subtotalPrice');
                });
                $('.totalProductPrice').html(Fantasy.number(_totalProductPrice,'0.00'));
            });
            this.get('productQuantity').target.change();
        });
        orderProductsView.setJSON([]);

        $('.cancelOrderBut').click(function(){
            if(confirm("确认取消添加订单？")){
                $(".back-page").click();
            }
        });
        $('.nextStepBut').click(function(){
            $('.formParams').empty();
            var nullProduct = true;
            orderProductsView.each(function(){
                nullProduct = false;
                $('.formParams').append("<input name='order.orderItems["+this.getIndex()+"].sn' value='"+this.getValue('sn')+"' type='hidden' />");
                $('.formParams').append("<input name='order.orderItems["+this.getIndex()+"].productQuantity' value='"+this.getValue('productQuantity')+"' type='hidden' />")
            });
            if(nullProduct){
                top.$.msgbox({
                    msg : "请选择商品后在操作!",
                    icon : "error"
                });
                return;
            }
            $('#saveForm').submit();
            return false;
        });
    });
</script>
<div class="example-box" style="padding-left:10px;padding-right:10px;" id="product_list">
    <div class="example-code">
        <!-- 取消返回 -->
        <a class="back-page"></a>
        <!-- 下一步表单 -->
        <form id="saveForm" action="${request.contextPath}/admin/dms/order/submit_selected_products.do" ajax="{type:'html',target:'closest(\'#product_list\')'}" method="post">
            <div class="formParams"></div>
        </form>
        <div class="content-box box-toggle button-toggle">
            <h3 class="content-box-header primary-bg">
                <span class="float-left"><@s.text name='mall.order.pageTitle'/></span>
                <a href="javascript:void(0)" class="float-right icon-separator btn toggle-button" title="Toggle Box">
                    <i class="glyph-icon icon-toggle icon-chevron-up"></i>
                </a>
            </h3>
            <div class="content-box-wrapper">
                <div id="order_products_view">
                    <div class="ul-terms">
                        <ul class="ul-terms">
                            <li class="ul-terms-list1">
                                &nbsp;
                            </li>
                            <li class="ul-terms-list2"><@s.text name='mall.order.goods'/></li>
                            <li class="ul-terms-list3 ul-terms-list03"><@s.text name='mall.order.goods.price'/></li>
                            <li class="ul-terms-list4 TxtCenter"><@s.text name='mall.order.goods.num'/></li>
                            <li class="ul-terms-list3 ul-terms-list03"><@s.text name='mall.order.goods.amount'/></li>
                            <li class="ul-terms-list5"><@s.text name='mall.order.index.handle'/></li>
                        </ul>
                        <div style="clear:both; height:10px;"></div>
                        <ul class="template" name="default" style="border: 1px solid #ccc; height:106px;margin-top: 10px;">
                            <li class="ul-terms-list1">
                                <span style="float:left; padding-top:45px;padding-right:5px; display:block;">&nbsp;</span>
                                <label>
                                    <img class="goodsImage" src="${request.contextPath}{goodsImage}" height="100"  width="100"/>
                                </label>
                            </li>
                            <li class="ul-terms-list2">&nbsp;{name}</li>
                            <li class="ul-terms-list3 ul-terms-list03 line-h"><strong>&nbsp;{productPrice}</strong></li>
                            <li class="ul-terms-list4 ul-4-pad">
                                <a class="smallNum">-</a>
                                <label>
                                    <input style="text-align: center;" class="view-field number" name="productQuantity" dataType="int" type="text" id="textfield" value="1" size="5" maxlength="5"/>
                                </label>
                                <a class="bigNum">+</a>
                            </li>
                            <li class="ul-terms-list3 line-h">&nbsp;
                                <span class="view-field" name="subtotalPrice" dataType="float"></span>
                            </li>
                            <li class="line-h ul-terms-list5">
                                <a class="remove"><@s.text name='mall.order.index.del'/></a>
                            </li>
                            <span style="clear:both; height:1px;overflow:hidden;display:block;font-size:1px;"> </span>
                        </ul>
                        <div class="empty" style="text-align: center;">
                            <@s.text name='mall.order.point'/>&nbsp;<a href="${request.contextPath}/admin/dms/order/goods.do?EQL_favoriteDistributors.id=<@s.property value='#user.id'/>" ajax="{type:'html',target:'closest(\'#product_list\')'}"><@s.text name='mall.order.addGoods'/></a>
                        </div>
                        <div style="clear:both; height:20px;"> </div>
                        <div class="btn-a">
            <span style="text-align: right; color: #666; font-size: 12px;">
               <@s.text name='mall.order.total'/>：<span class="jgjgjg"><@s.text name='mall.order.sign' /><span class="jgjgjg totalProductPrice"></span></span><@s.text name='mall.order.unit' />
            </span>
                            <div style="clear:both; height:20px;"> </div>
            <span>
                <a href="${request.contextPath}/admin/dms/order/goods.do?EQL_favoriteDistributors.id=<@s.property value='#user.id'/>" ajax="{type:'html',target:'closest(\'#product_list\')'}"><@s.text name='mall.order.addGoods'/></a>
                <a class="nextStepBut"><@s.text name='mall.order.next'/></a>
                <a class="cancelOrderBut"><@s.text name='mall.order.close'/></a>
            </span>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>