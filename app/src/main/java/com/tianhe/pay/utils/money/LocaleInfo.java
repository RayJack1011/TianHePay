package com.tianhe.pay.utils.money;

enum  LocaleInfo {
    ZH('.', ',', "¤ #,##0.00", "#,##0.###");

    final char decimalSeparator;
    final char groupingSeparator;
    final String currencyPattern;
    final String numberPattern;

    LocaleInfo(char decimalSeparator, char groupingSeparator,
                      String currencyPattern, String numberPattern) {
        this.decimalSeparator = decimalSeparator;
        this.groupingSeparator = groupingSeparator;
        this.currencyPattern = currencyPattern;
        this.numberPattern = numberPattern;
    }
}
