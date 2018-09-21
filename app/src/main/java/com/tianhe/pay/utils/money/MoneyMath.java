package com.tianhe.pay.utils.money;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tianhe.pay.utils.Preconditions;

import java.math.BigDecimal;
import java.util.Comparator;

public class MoneyMath {
    private static final int BASIS_POINTS_PER_HALF_UNIT = 5000;
    private static final int BASIS_POINTS_PER_UNIT = 10000;
    public static final Comparator<Money> COMPARATOR = new Comparator<Money>() {
        @Override
        public int compare(Money a, Money b) {
            if (a.getCurrencyCode() != b.getCurrencyCode()) {
                throw new IllegalArgumentException(
                        "Cannot compare different currencies: " + a.getCurrencyCode() + ", " + b.getCurrencyCode());
            }
            return (a.getAmount() < b.getAmount()) ? -1 : ((a.getAmount() == b.getAmount()) ? 0 : 1);
        }
    };

    private MoneyMath() {
    }

    public static BigDecimal asDecimalWholeUnits(Money money) {
        int perUnit = Currencies.getSubunitsPerUnit(money.getCurrencyCode());
        return new BigDecimal(money.getAmount()).divide(new BigDecimal(perUnit));
    }

    public static Money divide(Money a, BigDecimal b) {
        return new Money(new BigDecimal(a.getAmount())
                .divide(b, 2, java.math.RoundingMode.HALF_EVEN)
                .longValue());
    }

    public static BigDecimal divide(Money a, Money b) {
        assertSameCurrencies(a, b);
        return new BigDecimal(a.getAmount())
                .divide(new BigDecimal(b.getAmount()), 2, java.math.RoundingMode.HALF_EVEN);
    }

    public static boolean greaterThan(Money a, Money b) {
        assertSameCurrencies(a, b);
        return a.getAmount() > b.getAmount();
    }

    public static boolean greaterThanNullSafe(Money a, Money b) {
        return (a != null && b != null) && greaterThan(a, b);
    }

    public static boolean greaterThanOrEqualTo(Money a, Money b) {
        assertSameCurrencies(a, b);
        return a.getAmount() >= b.getAmount();
    }

    public static boolean isEqual(Money a, Money b) {
        return (a.getCurrencyCode() == b.getCurrencyCode())
                && (a.getAmount() == b.getAmount());
    }

    public static boolean isNegative(Money money) {
        return money != null && money.getAmount() < 0L;
    }

    public static boolean isPositive(Money money) {
        return money != null && money.getAmount() > 0L;
    }

    public static Money max(Money a, Money b) {
        return a.getAmount() > b.getAmount() ? a : b;
    }

    public static Money min(Money a, Money b) {
        return a.getAmount() < b.getAmount() ? a : b;
    }

    public static Money multiply(Money basePrice, int quantity) {
        return new Money(basePrice.getAmount() * (long) quantity);
    }

    public static Money multiplyByBasisPoints(Money basePrice, int basisPoints) {
        return new Money((basePrice.getAmount() * basisPoints + BASIS_POINTS_PER_HALF_UNIT) / BASIS_POINTS_PER_UNIT);
    }

    public static Money negate(Money money) {
        return new Money(-money.getAmount());
    }

    /**
     * @param fullAmount 比例的分母
     * @param partialAmount 比例的分子
     * @param amountToProrate 需要分配/分割的金额
     * @return 按比例分配后的金额
     */
    public static Money prorateAmountNullSafe(Money fullAmount, Money partialAmount, Money amountToProrate) {
        return (fullAmount != null && partialAmount != null && amountToProrate != null
                && partialAmount.getAmount() != 0L && fullAmount.getAmount() != 0L)
                ? divide(amountToProrate, divide(fullAmount, partialAmount))
                : amountToProrate;
    }

    @NonNull
    public static Money subtract(Money a, Money b) {
        Preconditions.nonNull(a, "a");
        Preconditions.nonNull(b, "b");
        assertSameCurrencies(a, b);
        return new Money(a.getAmount() - b.getAmount());
    }

    @NonNull
    public static Money subtractNullSafe(Money a, Money b) {
        Preconditions.nonNull(a, "a");
        return b == null ? a : subtract(a, b);
    }

    @NonNull
    public static Money subtractWithZeroMinimum(Money a, Money b) {
        assertSameCurrencies(a, b);
        return new Money(Math.max(0L, a.getAmount() - b.getAmount()));
    }

    public static Money sum(Money a, Money b) {
        assertSameCurrencies(a, b);
        return new Money(a.getAmount() + b.getAmount());
    }

    @Nullable
    public static Money sumNullSafe(Money a, Money b) {
        return a == null ? b : (b == null ? a : sum(a, b));
    }

    private static void assertSameCurrencies(Money a, Money b) {
        if (a.getCurrencyCode() != b.getCurrencyCode()) {
            throw new IllegalArgumentException(
                    "Cannot do math with different currencies: "
                            + a.getCurrencyCode() + ", " + b.getCurrencyCode());
        }
    }
}
