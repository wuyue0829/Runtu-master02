package com.mac.runtu.utils;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Arith {

    private static ArrayList<BigDecimal> mList;

    /**
     * 提供精确加法计算的add方法
     *
     * @param value1
     *         被加数
     * @param value2
     *         加数
     * @return 两个参数的和
     */
    public static double add(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.add(b2).doubleValue();
    }

    public static double add(float value1, float value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.add(b2).floatValue();
    }

    public static float add(ArrayList<Float> list) {

        BigDecimal allMonery = null;

        mList = new ArrayList<BigDecimal>();

        for (int i = 0; i < list.size(); i++) {
            Double aDouble = Double.valueOf(list.get(i));
            BigDecimal b1 = new BigDecimal(aDouble);
            mList.add(b1);
        }

        //计算
        allMonery = mList.get(0);
        for (int i = 1; i < mList.size(); i++) {
            allMonery = allMonery.add(mList.get(i));
        }



        return allMonery.setScale(2,   BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static float getFloat(float allPay) {

        BigDecimal allMonery = new BigDecimal(allPay);
        return allMonery.setScale(2,   BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 提供精确减法运算的sub方法
     *
     * @param value1
     *         被减数
     * @param value2
     *         减数
     * @return 两个参数的差
     */
    public static double sub(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确乘法运算的mul方法
     *
     * @param value1
     *         被乘数
     * @param value2
     *         乘数
     * @return 两个参数的积
     */
    public static double mul(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供精确的除法运算方法div
     *
     * @param value1
     *         被除数
     * @param value2
     *         除数
     * @param scale
     *         精确范围
     * @return 两个参数的商
     * @throws IllegalAccessException
     */
    public static double div(double value1, double value2, int scale) throws
            IllegalAccessException {
        //如果精确范围小于0，抛出异常信息
        if (scale < 0) {
            throw new IllegalAccessException("精确度不能小于0");
        }
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.divide(b2, scale).doubleValue();
    }
}