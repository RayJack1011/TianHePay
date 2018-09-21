package com.tianhe.pay.utils.money;

public class SwedishRounding {

    /**
     * @param money 原金额
     * @return 舍入后的金额
     */
    public static Money apply(Money money) {
        int roundingUnit = getCashRounding(money.getCurrencyCode());
        return new Money(roundedAmount(money.getAmount(), roundingUnit), money.getCurrencyCode());
    }

    /**
     * @param money 原金额
     * @return 舍入后的金额与原金额的差值
     */
    public static Money getDifference(Money money) {
        int roundingUnit = getCashRounding(money.getCurrencyCode());
        if (roundingUnit == 0) {
            return Money.zeroMoney();
        } else {
            return new Money(roundedAmount(money.getAmount(), roundingUnit) - money.getAmount(),
                    money.getCurrencyCode());
        }
    }

    public static boolean isRequired(Money money) {
        int roundingUnit = getCashRounding(money.getCurrencyCode());
        return (roundingUnit != 0) && money.getAmount() % roundingUnit != 0L;
    }

    private static long roundedAmount(long amount, long roundingUnit) {
        return (2 * amount + roundingUnit) / (2L * roundingUnit) * roundingUnit;
    }

    private static int getCashRounding(CurrencyCode currencyCode) {
        return Currencies.getSwedishRoundingInterval(currencyCode);
    }
}
