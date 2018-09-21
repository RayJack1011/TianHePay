package com.tianhe.pay.utils.money;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class NumberFormats {
    private static final String STANDARD_DIGITS = "0123456789  ";
    private static final LocaleInfo DEFAULT = LocaleInfo.ZH;

    public static String getAllowedDigits(Locale locale, CurrencyCode currencyCode) {
        DecimalFormatSymbols decimalFormatSymbols =
                getCurrencyFormat(locale, currencyCode).getDecimalFormatSymbols();
        return STANDARD_DIGITS + decimalFormatSymbols.getCurrencySymbol()
                + decimalFormatSymbols.getGroupingSeparator() + decimalFormatSymbols.getDecimalSeparator();
    }

    private static LocaleInfo getBestLocaleInfo(Locale locale) {
        String language = locale.getLanguage().toUpperCase();
        String country = locale.getCountry();
        LocaleInfo localeInfo;
        if(country != null && country.length() != 0) {
            localeInfo = getLocaleInfoByKey(String.format("%s_%s", language, country));
            if(localeInfo != null) {
                return localeInfo;
            }
        }
        localeInfo = getLocaleInfoByKey(language);
        if(localeInfo == null) {
            return DEFAULT;
        } else {
            return localeInfo;
        }
    }

    public static DecimalFormat getCurrencyFormat(Locale locale, CurrencyCode currencyCode) {
        LocaleInfo localeInfo = getBestLocaleInfo(locale);
        CurrencyInfo currencyInfo = Currencies.getCurrencyInfo(currencyCode);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        symbols.setCurrency(Currency.getInstance(currencyCode.name()));
        symbols.setCurrencySymbol(currencyInfo.symbol);
        symbols.setGroupingSeparator(localeInfo.groupingSeparator);
        symbols.setDecimalSeparator(localeInfo.decimalSeparator);
        DecimalFormat format = new DecimalFormat(localeInfo.currencyPattern, symbols);
        format.setMinimumFractionDigits(currencyInfo.fractionalDigits);
        format.setMaximumFractionDigits(currencyInfo.fractionalDigits);
        return format;
    }

    public static NumberFormat getInstance() {
        return getNumberFormat(Locale.getDefault());
    }

    public static NumberFormat getInstance(Locale locale) {
        return getNumberFormat(locale);
    }

    public static NumberFormat getIntegerInstance() {
        return getIntegerInstance(Locale.getDefault());
    }

    public static NumberFormat getIntegerInstance(Locale locale) {
        NumberFormat decimalFormat = getInstance(locale);
        decimalFormat.setMaximumFractionDigits(0);
        return decimalFormat;
    }

    private static LocaleInfo getLocaleInfoByKey(String key) {
        try {
            return LocaleInfo.valueOf(key);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    static NumberFormat getNumberFormat(Locale locale) {
        LocaleInfo localeInfo = getBestLocaleInfo(locale);
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setGroupingSeparator(localeInfo.groupingSeparator);
        decimalFormatSymbols.setDecimalSeparator(localeInfo.decimalSeparator);
        return new DecimalFormat(localeInfo.currencyPattern, decimalFormatSymbols);
    }
}
