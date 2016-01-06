package org.jfantasy.test.service;

import org.jfantasy.framework.util.common.DateUtil;
import org.junit.Test;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lmf on 15/5/28.
 */
public class XXXTest {

    private static DecimalFormat df = new DecimalFormat("######0.00");
    private static DecimalFormat _df = new DecimalFormat("######0");

    public static class X {

        double x;
        int number;

        public double getX() {
            return x;
        }

        X(double x, int number) {
            this.x = x;
            this.number = number;
        }

        public double xaoj() {
            return x * number;
        }

        public int getNumber() {
            return number;
        }
    }

    @Test
    public void die() {
        Date now = DateUtil.add(DateUtil.now(), Calendar.DATE, 1);
        double sz = 3686;
        double jine = 253562;
        while (jine > 0 && "2015".equals(DateUtil.format(now,"yyyy"))) {
            int week = DateUtil.getTimeField(now, Calendar.DAY_OF_WEEK);
            if(week == 1 || week == 7){
                now = DateUtil.add(now,Calendar.DATE,1);
                System.out.println("");
                continue;
            }
            jine = jine - (jine * 0.1);
            sz = sz - (sz * 0.06);
            System.out.println(_df.format(sz) + "\t" + DateUtil.format(now,"yyyy-MM-dd") + "\t" + DateUtil.getChineseWeekName(now) + "\t" + df.format(jine));
            now = DateUtil.add(now,Calendar.DATE,1);
        }
    }

    @Test
    public void test() {
        double[] ru = {5877, 3918, 9675};
        double[] cu = {9835, 6965, 2985};
        double gu = 1000d;
        System.out.println("2015-06-02");
        printT_0("一重", cu, ru, gu);
        ru = new double[]{2658, 886, 7088};
        cu = new double[]{897, 8970, 897};
        gu = 1200d;
        printT_0("石化", cu, ru, gu);

        System.out.println("\n\n==========================================================");

        ru = new double[]{9330, 9305};
        cu = new double[]{9485, 9460};
        gu = 1000d;
        System.out.println("2015-06-03");
        printT_0("一重", cu, ru, gu);

        ru = new double[]{5123, 5568, 10800, 10800};
        cu = new double[]{10944, 11064, 10992};
        gu = 1200d * 3;
        printT_0("石化", cu, ru, gu);

        System.out.println("\n\n==========================================================");
        System.out.println("2015-06-04");
        printT_0("一重", new double[]{9395}, new double[]{9240}, 500);

        printT_0("石化", new double[]{11040, 11160}, new double[]{10740, 2727, 8181}, 2400);


        System.out.println("\n\n==========================================================");
        System.out.println("2015-06-05");
        printT_0("一重", new double[]{0}, new double[]{0}, 500);//9480元买入未出

        printT_0("石化", new double[]{10992, 11136, 11196}, new double[]{10884, 11040, 11040}, 3600);

        System.out.println("\n\n==========================================================");
        System.out.println("2015-06-08");
        printT_0("一重", new double[]{9685}, new double[]{9480}, 500);

        printT_0("石化", new double[]{11448, 11412, 11532}, new double[]{11340, 11280, 11280}, 3600);

        System.out.println("\n\n==========================================================");
        System.out.println("2015-06-23");
        printT_0("石化", new double[]{10500, 10260, 5112, 852, 4260, 10272}, new double[]{10080, 10080, 10140, 2580, 7740}, 1200 * 4);

        System.out.println("\n\n==========================================================");
        System.out.println("2015-06-24");
        printT_0("石化", new double[]{19300, 10986}, new double[]{10800, 12454, 6706}, 1200 + 2000);

        System.out.println("\n\n==========================================================");
        System.out.println("2015-07-13");
        printT_0("石化", new double[]{(13008d / 1600 * 1300), 16400}, new double[]{8536, 7810, 3734, 798, 5043.36}, 3300);

        System.out.println("\n\n==========================================================");
        System.out.println("2015-07-14");
        printT_0("石化", new double[]{5572 + 7164 + 796 + 2388, 13464}, new double[]{15660, 13192}, 3700);
        printT_0("交建", new double[]{1788+16101}, new double[]{1750+8750+6980}, 1000);
//        double sj = 9.96;
//        System.out.println("出:"+ df.format(sj + sj * 0.03));
//        dianjagus(sj + sj * 0.03);
//        System.out.println("加:"+ df.format(sj - sj * 0.02));
//        dianjagus(sj - sj * 0.02);
//        System.out.println("入:"+ df.format(sj - sj * 0.06));
//        dianjagus(sj - sj * 0.06);

//        dianjagus(9.1);

//        print(xs, 8.78);

    }

    public void printT_0(String name, double[] cu, double[] ru, double gu) {
        System.out.println(name + ":" + df.format(sy(cu, ru)) + "\t差价:" + df.format(sy(cu, ru) / gu) + "\t操作股数:" + gu);
    }

    public double sy(double[] cus, double[] rus) {
        double cut = 0, rut = 0;
        for (double cu : cus) {
            cut += cu;
        }
        for (double ru : rus) {
            rut += ru;
        }
        return cut - rut;
    }

    private void dianjagus(double gjg) {
        double zj = 0;
        int zs = 0;
        while (zj < 10000) {
            zj += gjg;
            zs++;
        }
        System.out.println("股数:" + zs + "\t\t\t\t花费:" + df.format(zs * gjg) + "\t\t\t\t单价:" + df.format(gjg));
    }

    public static void print(X[] xs, double sj) {
        double tx = 50;
        int tnumber = 0;
        for (X x : xs) {
            tx += x.xaoj();
            tnumber += x.getNumber();
        }
        System.out.println("共计花费:" + df.format(tx));
        System.out.println("股数：" + tnumber);
        double dj = tx / tnumber;
        System.out.println("单价:" + df.format(dj));
        System.out.println("每股浮动:" + df.format(sj - dj));
        System.out.println("盈利：" + df.format((sj - dj) * tnumber));

//        System.out.println("\n\n预计出货价格");
//        double zj = 0;
//        for (X x : xs) {
//            double tj = x.getX() + x.getX() * 0.03;
//            double sy = (tj - x.getX())*x.getNumber()-10;
//            zj+=sy;
//            System.out.println("购入价格:" + x.getX() + " - " + x.getNumber() + "\t\t出货:" + df.format(tj) + "\t\t金额:"+ df.format(tj * x.getNumber()) +"\t\t预计收益:" + df.format(sy));
//        }
//        System.out.println("总收益:" + df.format(zj));
    }

}
