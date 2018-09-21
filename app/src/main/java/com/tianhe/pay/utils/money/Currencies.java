package com.tianhe.pay.utils.money;

import java.util.List;

/**
 * 工具类, 根据{@link CurrencyCode}转换成需要的数据
 */
public class Currencies {
    static CurrencyInfo getCurrencyInfo(CurrencyCode currencyCode) {
        return CurrencyInfo.valueOf(currencyCode.name());
    }

    /**
     * @param currencyCode 货币代码
     * @return 货币符号字符
     */
    public static String getCurrencySymbol(CurrencyCode currencyCode) {
        return getCurrencyInfo(currencyCode).symbol;
    }

    /**
     * @param currencyCode 货币代码
     * @return 货币的面值
     */
    public static List<Integer> getDenominations(CurrencyCode currencyCode) {
        return getCurrencyInfo(currencyCode).denominations;
    }

    /**
     * @param currencyCode 货币代码
     * @return 货币面值的基础单位. denomination ＝ 实际面值 * subunitPerUnit.
     * @see Currencies#getDenominations
     */
    public static int getSubunitsPerUnit(CurrencyCode currencyCode) {
        return getCurrencyInfo(currencyCode).subunitsPerUnit;
    }

    /**
     * @param currencyCode 货币代码
     * @return 小数点后面保留的位数
     */
    public static int getFractionDigits(CurrencyCode currencyCode) {
        return getCurrencyInfo(currencyCode).fractionalDigits;
    }

    /**
     * @param currencyCode 货币代码
     * @return (现金)最小结算单位
     */
    public static int getSwedishRoundingInterval(CurrencyCode currencyCode) {
        return getCurrencyInfo(currencyCode).swedishRoundingInterval;
    }
}
