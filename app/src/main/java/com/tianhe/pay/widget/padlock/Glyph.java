package com.tianhe.pay.widget.padlock;

public enum Glyph {
    C('C'),
    CHECK('='),
    PLUS('+'),
    BACKSPACE('<');

    private final String string;

    Glyph(char character) {
        this.string = String.valueOf(character);
    }

    public String getLetter() {
        return this.string;
    }
}
