package com.fantasy.test.service;

import org.junit.Test;

import java.text.DecimalFormat;

/**
 * Created by lmf on 15/5/28.
 */
public class XXXTest {

    private static DecimalFormat df = new DecimalFormat("######0.00");

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

        printT_0("石化", new double[]{10992,11136,11196 }, new double[]{10884, 11040, 11040}, 3600);
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
