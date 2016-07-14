package org.jfantasy.pay;

import java.math.BigDecimal;

/**
 * 积分转换器,用于积分消费时的转换
 */
public interface PointTransform {

    BigDecimal convert(Long point);

}
