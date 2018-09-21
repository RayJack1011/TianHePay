package com.tianhe.pay.widget.padlock;

import android.content.res.Resources;
import android.graphics.Typeface;

public class PadlockTypeface {
    private static final String GLYPH_FONT_PATH = "fonts/padlock_glyphs.ttf";
    private static Typeface glyphTypeface;

    public PadlockTypeface() {
    }

    public static Typeface getGlyphTypeface(Resources var0) {
        if(glyphTypeface == null) {
            glyphTypeface = Typeface.createFromAsset(var0.getAssets(), "fonts/padlock_glyphs.ttf");
        }

        return glyphTypeface;
    }

    public static enum Glyph {
        C('c'),
        CHECK('='),
        PLUS('+'),
        ROUNDED_BACKSPACE('<');

        private final String string;

        Glyph(char character) {
            this.string = String.valueOf(character);
        }

        public String getLetter() {
            return this.string;
        }
    }
}
