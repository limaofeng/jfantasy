package org.jfantasy.pay.service;

import org.jfantasy.pay.PointTransform;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AmountPointTransform implements PointTransform {

    @Override
    public BigDecimal convert(Long point) {
        return BigDecimal.valueOf(point / 100);
    }

}
