package org.jfantasy.pay.other;

import org.junit.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class BigDecimalTest {

    @Test
    public void compareTo(){
        System.out.println(BigDecimal.valueOf(0.13).compareTo(BigDecimal.valueOf(0.13)));

        System.out.println(BigDecimal.valueOf(0.14).compareTo(BigDecimal.valueOf(0.13)));

        System.out.println(BigDecimal.valueOf(0.13).compareTo(BigDecimal.valueOf(0.14)));
    }

    @Test
    public void format(){
       DecimalFormat format = new DecimalFormat("#0.00");
//        setScale(1,BigDecimal.ROUND_DOWN)直接删除多余的小数位，如2.35会变成2.3
//        setScale(1,BigDecimal.ROUND_UP)进位处理，2.35变成2.4
//        setScale(1,BigDecimal.ROUND_HALF_UP)四舍五入，2.35变成2.4
//        setScaler(1,BigDecimal.ROUND_HALF_DOWN)四舍五入，2.35变成2.3，如果是5则向下舍

        BigDecimal bigDecimal = BigDecimal.valueOf(0.135);

        System.out.println(format.format(bigDecimal));

        System.out.println(bigDecimal.setScale(2,BigDecimal.ROUND_DOWN));

    }


}
