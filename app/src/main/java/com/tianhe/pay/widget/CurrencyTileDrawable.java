package com.tianhe.pay.widget;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.text.TextPaint;

import com.tianhe.pay.R;
import com.tianhe.pay.utils.Views;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.graphics.Paint.SUBPIXEL_TEXT_FLAG;
import static android.graphics.PixelFormat.TRANSLUCENT;

public class CurrencyTileDrawable extends Drawable {
    private final NinePatchDrawable background;
    private final String currencySymbol;
    private final TextPaint currencySymbolPaint;
    private final int dimension;
    private final float textSize;

    public CurrencyTileDrawable(Resources res, String currencySymbol, int dime) {
        this.currencySymbol = currencySymbol;
        this.dimension = dime;
        this.textSize = (dime * 0.7F);
        this.currencySymbolPaint = new TextPaint(ANTI_ALIAS_FLAG | SUBPIXEL_TEXT_FLAG);
        this.currencySymbolPaint.setTextSize(this.textSize);
        this.currencySymbolPaint.setTextAlign(Paint.Align.CENTER);
        this.currencySymbolPaint.setColor(res.getColor(R.color.marin_dark_gray));
        this.currencySymbolPaint.setFlags(currencySymbolPaint.getFlags() | ANTI_ALIAS_FLAG | SUBPIXEL_TEXT_FLAG);
        this.background = Views.get9PatchSized(res, R.drawable.marin_custom_tile, dime);
    }

    public void draw(Canvas canvas) {
        this.background.draw(canvas);
        float textX = this.dimension / 2;
        Rect rect = new Rect();
        this.currencySymbolPaint.getTextBounds(this.currencySymbol, 0, this.currencySymbol.length(), rect);
        float textY = (this.dimension / 2)
                - (this.currencySymbolPaint.descent() + this.currencySymbolPaint.ascent() / 2.0F)
                + (this.textSize * 0.06125F);
        canvas.drawText(this.currencySymbol, textX, textY, this.currencySymbolPaint);
    }

    public int getIntrinsicHeight() {
        return this.dimension;
    }

    public int getIntrinsicWidth() {
        return this.dimension;
    }

    public int getOpacity() {
        return TRANSLUCENT;
    }

    public void setAlpha(int alpha) {
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.currencySymbolPaint.setColorFilter(colorFilter);
        this.background.setColorFilter(colorFilter);
    }
}
