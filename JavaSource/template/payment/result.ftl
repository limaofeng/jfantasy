<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>支付结果</title>
<meta name="Author" content="Fantasy Team" />
<meta name="Copyright" content="Fantasy" />
<link rel="icon" href="favicon.ico" type="image/x-icon" />
<link href="${request.contextPath}/template/shop/css/base.css" rel="stylesheet" type="text/css" />
<link href="${request.contextPath}/template/shop/css/shop.css" rel="stylesheet" type="text/css" />
<!--[if lte IE 6]>
	<script type="text/javascript">
		// 解决IE6透明PNG图片BUG
		DD_belatedPNG.fix(".belatedPNG");
	</script>
<![endif]-->
</head>
<body class="paymentResult">
	<div class="body">
		<div class="blank"></div>
		<div class="paymentResultDetail">
			<#if payment.paymentType == "deposit">
				<h2>
					<span class="icon success">&nbsp;</span>
					预存款支付成功,支付总金额<span class="red">${payment.totalAmount?string(currencyFormat)}</span>
				</h2>
			<#elseif payment.paymentType == "offline">
				<h2>
					<span class="icon success">&nbsp;</span>
					请根据您选择的支付方式进行付款,支付总金额<span class="red">${payment.totalAmount?string(currencyFormat)}</span>
				</h2>
				<p>
					${(payment.paymentConfig.description)!}
				</p>
			<#else>
				<h2>
					<span class="icon success">&nbsp;</span>
					在线支付成功,支付总金额<span class="red">${payment.totalAmount?string(currencyFormat)}</span>
				</h2>
			</#if>
			<div class="buttonArea">
				<input type="submit" class="formButton" onclick="location.href='${base}/'" value="返回首页" />
			</div>
		</div>
		<div class="blank"></div>
	</div>
	<div class="blank"></div>
</body>
</html>