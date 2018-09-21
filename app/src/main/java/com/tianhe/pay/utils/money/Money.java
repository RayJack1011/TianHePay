package com.tianhe.pay.utils.money;

import com.tianhe.pay.utils.Strings;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Money implements Serializable {

    private static final BigDecimal MAX_CENTS_BIG_DECIMAL = BigDecimal.valueOf(0x1FFFFFFFFFFFFFL);
    private static final long MAX_CENTS_VALUE = 0x1FFFFFFFFFFFFFL;
    private static final BigDecimal MIN_CENTS_BIG_DECIMAL = BigDecimal.valueOf(-0x1FFFFFFFFFFFFFL);
    private static final long MIN_CENTS_VALUE = -0x1FFFFFFFFFFFFFL;
    private static final CurrencyCode DEFAULT = CurrencyCode.CNY;
    private static final Money ZERO_MONEY = new Money(0);

    public static Money zeroMoney() {
        return ZERO_MONEY;
    }

    private static CurrencyCode commonCurrencyCode(CurrencyCode code1, CurrencyCode code2) {
        if (code1 == CurrencyCode.NO_CURRENCY) {
            return code2;
        }
        if (code2 == CurrencyCode.NO_CURRENCY) {
            return code1;
        }
        if (code1 == code2) {
            return code1;
        }
        throw new RuntimeException("MismatchedCurrency");
    }

    private static BigDecimal round(BigDecimal value, RoundingMode roundingMode) {
        return value.setScale(0, roundingMode.toMathRoundingMode());
    }

    private static long validateCentsAmount(BigDecimal centsAmount, String message) throws MoneyOverflowException {
        if (centsAmount.compareTo(MAX_CENTS_BIG_DECIMAL) <= 0 && centsAmount.compareTo(MIN_CENTS_BIG_DECIMAL) >= 0) {
            return centsAmount.longValue();
        } else {
            throw new MoneyOverflowException(message + "Bounds: [" + MIN_CENTS_VALUE + "," + MAX_CENTS_VALUE + "].");
        }
    }

    private BigDecimal bigDecimalCentsAmount;
    private final CurrencyCode currencyCode;
    private final long amount;

    public Money(long amount) {
        this.amount = amount;
        this.currencyCode = DEFAULT;
    }

    private Money(String amount) {
        if (amount == null) {
            this.amount = 0;
        } else {
            BigDecimal cents = new BigDecimal(amount)
                    .movePointRight(2)
                    .setScale(0, RoundingMode.NORMAL.toMathRoundingMode());
            this.amount = cents.longValueExact();
        }
        this.currencyCode = DEFAULT;
    }

    public Money(long amount, CurrencyCode currencyCode) throws MoneyOverflowException {
        this.bigDecimalCentsAmount = null;
        if (currencyCode == null) {
            throw new IllegalArgumentException("Cannot create money without a currency code.");
        } else if (currencyCode == CurrencyCode.NO_CURRENCY && amount != 0L) {
            throw new IllegalArgumentException(
                    "Cannot create money without a currency code unless it\'s a zero amount.");
        } else if (amount > MAX_CENTS_VALUE) {
            throw new MoneyOverflowException(
                    "Cannot create money with a cents amount greater than " + MAX_CENTS_VALUE);
        } else if (amount < MIN_CENTS_VALUE) {
            throw new MoneyOverflowException(
                    "Cannot create money with a cents amount less than " + MIN_CENTS_VALUE);
        } else {
            this.amount = amount;
            this.currencyCode = currencyCode;
        }
    }

    public static Money createAsYuan(String amount) {
        return new Money(amount);
    }

    public long getAmount() {
        return amount;
    }

    public CurrencyCode getCurrencyCode() {
        return currencyCode;
    }

    public Money negate() {
        if (getAmount() == 0) {
            return this;
        }
        return new Money(-this.getAmount(), this.getCurrencyCode());
    }

    public Money abs() {
        if (isNegate()) {
            return negate();
        }
        return this;
    }

    public boolean isNegate() {
        return this.getAmount() < 0;
    }

    public Money add(Money otherMoney) throws MoneyOverflowException {
        CurrencyCode currencyCode = commonCurrencyCode(this.getCurrencyCode(), otherMoney.getCurrencyCode());
        BigDecimal result = this.bigDecimalCentsAmount().add(otherMoney.bigDecimalCentsAmount());
        long centsAmount = validateCentsAmount(result,
                "Add operation results in value outside the bounds allowed in a cents amount.");
        return new Money(centsAmount, currencyCode);
    }

    public Money multiply(String multiplicand, Money.RoundingMode roundingMode) throws MoneyOverflowException {
        return this.multiply(new BigDecimal(multiplicand), roundingMode);
    }

    public Money multiply(BigDecimal multiplicand, Money.RoundingMode roundingMode) throws MoneyOverflowException {
        BigDecimal result = this.bigDecimalCentsAmount().multiply(multiplicand);
        result = round(result, roundingMode);
        long centAmount = validateCentsAmount(result,
                "Multiply operation results in value outside the bounds allowed in a cents amount.");
        return new Money(centAmount, this.getCurrencyCode());
    }

    public Money multiply(BigDecimal multiplicand) throws MoneyOverflowException {
        return this.multiply(multiplicand, RoundingMode.NORMAL);
    }

    public Money divide(BigDecimal divisor, Money.RoundingMode roundingMode) throws MoneyOverflowException {
        BigDecimal result = this.bigDecimalCentsAmount().divide(divisor);
        result = round(result, roundingMode);
        long centAmount = validateCentsAmount(result,
                "Multiply operation results in value outside the bounds allowed in a cents amount.");
        return new Money(centAmount, this.getCurrencyCode());
    }

    public Money subtract(Money otherMoney) throws MoneyOverflowException {
        CurrencyCode currencyCode = commonCurrencyCode(this.getCurrencyCode(), otherMoney.getCurrencyCode());
        BigDecimal result = this.bigDecimalCentsAmount().subtract(otherMoney.bigDecimalCentsAmount());
        long centAmount = validateCentsAmount(result,
                "Subtract operation results in value outside the bounds allowed in a cents amount.");
        return new Money(centAmount, currencyCode);
    }

    public double getYuan() {
        return bigDecimalCentsAmount().movePointLeft(2).doubleValue();
    }

    public int compareTo(Money otherMoney) {
        commonCurrencyCode(this.getCurrencyCode(), otherMoney.getCurrencyCode());
        return (this.getAmount() < otherMoney.getAmount()) ? -1
                : ((this.getAmount() == otherMoney.getAmount()) ? 0 : 1);
    }

    @Override
    public boolean equals(Object obj) {
        if (this != obj) {
            return false;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Money other = (Money) obj;
        return this.getAmount() == other.getAmount()
                && this.getCurrencyCode() == other.getCurrencyCode();
    }

    public int hashCode() {
        return (int) (this.getAmount() ^ this.getAmount() >>> 32) * 31 + this.getCurrencyCode().hashCode();
    }

    @Override
    public String toString() {
//        return String.format("%.2f", getYuan());
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(bigDecimalCentsAmount().movePointLeft(2));
    }

    private synchronized BigDecimal bigDecimalCentsAmount() {
        if (this.bigDecimalCentsAmount == null) {
            this.bigDecimalCentsAmount = BigDecimal.valueOf(this.getAmount());
        }
        return bigDecimalCentsAmount;
    }

    public static Money createAsFen(String total_fee) {
        if (Strings.isBlank(total_fee)) {
            return Money.zeroMoney();
        }
        return new Money(Long.valueOf(total_fee));
    }

    public static enum RoundingMode {
        BANKERS,
        TRUNCATE,
        NORMAL;

        java.math.RoundingMode toMathRoundingMode() {
            switch (this) {
                case BANKERS:
                    return java.math.RoundingMode.HALF_EVEN;
                case TRUNCATE:
                    return java.math.RoundingMode.DOWN;
                case NORMAL:
                    return java.math.RoundingMode.HALF_UP;
                default:
                    throw new RuntimeException("Money.RoundingMode." + name() + " can not convert to Math" +
                            ".RoundingMode");
            }
        }
    }

    public static class MoneyOverflowException extends RuntimeException {
        public MoneyOverflowException(String message) {
            super(message);
        }
    }

}
