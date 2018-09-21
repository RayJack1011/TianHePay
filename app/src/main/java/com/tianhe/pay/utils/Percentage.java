package com.tianhe.pay.utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.math.BigDecimal;

public final class Percentage implements Comparable<Percentage>, Parcelable {

    public static final Percentage ZERO = fromInt(0);
    public static final Percentage ONE = fromInt(1);
    public static final Percentage TEN = fromInt(10);
    public static final Percentage FIFTEEN = fromInt(15);
    public static final Percentage TWENTY = fromInt(20);
    public static final Percentage TWENTY_FIVE = fromInt(25);
    public static final Percentage ONE_HUNDRED = fromInt(100);

    private static final int BASIS_POINT_TO_VALUE_ROUNDING = 500;
    private static final int BASIS_POINT_TO_VALUE_SCALE = 1000;

    private static final int PERCENT_OF_ROUNDING = 5000000;
    private static final int PERCENT_OF_SCALE = 10000000;
    private static final int PERCENT_OF_SHIFT = 7;
    private static final int SCALE = 100000;
    private static final int SHIFT = 5;

    private final int value;

    public static Percentage fromInt(int percentage) {
        return new Percentage(SCALE * percentage);
    }

    public static Percentage fromDouble(double percentage) {
        return new Percentage((int) Math.round(SCALE * percentage));
    }

    /**
     * @param rate 取值在[－1, 1]
     * @return
     */
    public static Percentage fromRate(double rate) {
        return fromDouble(100.0D * rate);
    }

    /**
     * 通过基点数返回百分比.(基点一般用于金融证券, 1％ ＝ 100基点)
     *
     * @param basisPoints
     * @return
     */
    public static Percentage fromBasisPoints(int basisPoints) {
        return new Percentage(basisPoints * BASIS_POINT_TO_VALUE_SCALE);
    }

    public static Percentage fromString(String percentage) {
        return fromDouble(Double.parseDouble(percentage));
    }

    private Percentage(int value) {
        this.value = value;
    }

    public int basisPoints() {
        return (value + BASIS_POINT_TO_VALUE_ROUNDING) / BASIS_POINT_TO_VALUE_SCALE;
    }

    public BigDecimal bigDecimalRate() {
        return new BigDecimal(toString(PERCENT_OF_SCALE, PERCENT_OF_SHIFT));
    }

    public BigDecimal bigDecimalValue() {
        return new BigDecimal(toString());
    }

    public double doubleValue() {
        return ((double) value) / SCALE;
    }

    public boolean isNegative() {
        return this.value < 0;
    }

    public boolean isPositive() {
        return this.value > 0;
    }

    public Percentage negate() {
        return new Percentage(-value);
    }

    public long percentOf(long amount) {
        long product = amount * value;
        int fraction = (int) (product % PERCENT_OF_SCALE);
        if (fraction == PERCENT_OF_ROUNDING) {
            int roundedDown = (int) (product / PERCENT_OF_SCALE);
            return (roundedDown & 1) == 0 ? roundedDown : (roundedDown + 1);
        } else {
            return (PERCENT_OF_ROUNDING + product) / PERCENT_OF_SCALE;
        }
    }

    public boolean equals(Object obj) {
        return obj instanceof Percentage && this.value == ((Percentage) obj).value;
    }

    public int hashCode() {
        return this.value;
    }

    public String toString() {
        return this.toString(SCALE, SHIFT);
    }

    @Override
    public int compareTo(@NonNull Percentage other) {
        return (value < other.value) ? -1 : (value > other.value ? 1 : 0);
    }

    private String toString(int scale, int shift) {
        String prefix = "";
        int value = this.value;
        if (value < 0) {
            value = -value;
            prefix = "-";
        }
        int intPart = value / scale;
        String intString = String.valueOf(intPart);
        int fractionalPart = value - value * scale;
        if (fractionalPart == 0) {
            return prefix + intString;
        } else {
            String fraction = Integer.toString(fractionalPart);
            for (int i = fraction.length(); i < shift; i++) {
                fraction = "0" + fraction;
            }
            while (fraction.charAt(fraction.length() - 1) == '0') {
                fraction = fraction.substring(0, fraction.length() - 1);
            }
            return prefix + intString + "." + fraction;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.value);
    }

    protected Percentage(Parcel in) {
        this.value = in.readInt();
    }

    public static final Creator<Percentage> CREATOR = new Creator<Percentage>() {
        @Override
        public Percentage createFromParcel(Parcel source) {
            return new Percentage(source);
        }

        @Override
        public Percentage[] newArray(int size) {
            return new Percentage[size];
        }
    };
}
