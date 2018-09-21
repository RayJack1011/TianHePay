package com.tianhe.pay.utils.money;

import java.util.Arrays;
import java.util.List;

enum CurrencyInfo {
    CNY("¥", 2, 100, 1, Arrays.asList(100, 500, 1000, 2000, 5000, 10000));

    /**
     * 货币单位的符号
     */
    final String symbol;

    /**
     * 小数点后面保留的位数
     */
    final int fractionalDigits;

    /**
     * 金额的基础单位数值(为了将浮点型转换成整数来计算)
     */
    final int subunitsPerUnit;

    /**
     * 瑞典银行舍入法的间隔值(当使用现金支付时,为了顾客方便支付,必须要将账单总额四舍五入到最小可用的货币面额)
     */
    final int swedishRoundingInterval;

    /**
     * 面额
     */
    final List<Integer> denominations;

    CurrencyInfo(String symbol, int fractionalDigits, int subunitsPerUnit,
                 int swedishRoundingInterval, List<Integer> denominations) {
        this.symbol = symbol;
        this.fractionalDigits = fractionalDigits;
        this.subunitsPerUnit = subunitsPerUnit;
        this.swedishRoundingInterval = swedishRoundingInterval;
        this.denominations = denominations;
    }
}
