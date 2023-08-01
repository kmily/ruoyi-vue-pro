package cn.iocoder.yudao.module.radar.utils;

/**
 * Created by l09655 on 2022/7/28.
 * Bit位运算工具类
 */
public class BitUtil {

    /**
     * @param num 要修改第index位为1的数
     * @param index
     * @return long
     * @description: TODO 判断参数第index位是否为1，不是则改为1
     * @author l09655 2022年07月28日
     */
    public static long setIndexBitToOne(long num, int index) {
        //1左移index位
        long spNum = 1L << index;
        //如果参数与上1左移index位的结果spNum不等于spNum，那么说明该位是0，返回num+spNum
        if (spNum != (num & spNum)) {
            return num + spNum;
        } else {
            return num;
        }
    }
}
