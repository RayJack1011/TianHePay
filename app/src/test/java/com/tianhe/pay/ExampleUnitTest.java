package com.tianhe.pay;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);

        BigDecimal cents = new BigDecimal("0.015").movePointRight(2);

        System.out.println(cents.movePointLeft(2).longValue());
        System.out.println(cents);
        System.out.println(cents.setScale(0, RoundingMode.HALF_UP));
        DecimalFormat format = new DecimalFormat("0.00");
        System.out.println(format.format(cents.movePointLeft(2)));
    }

}