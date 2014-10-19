package com.fantasy.mall.order.web.validator;

import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.mall.order.bean.Order;
import com.fantasy.mall.order.service.OrderService;
import com.fantasy.member.userdetails.MemberUser;
import com.fantasy.security.SpringSecurityUtils;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

/**
 * @Author lsz
 * @Date 2013-12-5 上午10:25:07
 * 
 */
public class OrderMemberValidator extends FieldValidatorSupport {

	private OrderService orderService;

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	@Override
	public void validate(Object object) throws ValidationException {
		String fieldName = getFieldName();
		String sn = StringUtil.nullValue(getFieldValue(fieldName, object));
		Order order = orderService.get(sn);
		if (order == null || !SpringSecurityUtils.getCurrentUser(MemberUser.class).getUsername().equals(order.getMember().getUsername())) {
			addFieldError(fieldName, object);
		}
	}

}
