package com.tianhe.pay.widget.padlock;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.tianhe.pay.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Padlock extends ViewGroup {
    private static final int DELETE_TYPE_BACKSPACE_GLYPH = 2;
    private static final int DELETE_TYPE_CLEAR_GLYPH = 0;
    private static final int DELETE_TYPE_CLEAR_OR_CANCEL = -1;
    private static final int DELETE_TYPE_CLEAR_TEXT = 1;
    private static final int KEY_REPEAT_TIMEOUT = 100;
    private static final float LEFT_BACKSPACE_SCALING_FACTOR = 0.75F;
    private static final int LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
    private static final int RESOURCE_NOT_SET = -1;
    private static final String SEPARATOR = " ";
    private static final int SUBMIT_ADD = 1;
    private static final int SUBMIT_CHECK = 2;
    private static final int SUBMIT_GONE = 0;
    private static final int SUBMIT_INVISIBLE = 3;
    private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
    private static final int TINY_DEVICE_DP = 350;
    protected final TextPaint backspaceDisabledPaint;
    private Handler backspaceHandler;
    protected final TextPaint backspacePaint;
    private final Runnable backspaceRunnable;
    protected StateListDrawable backspaceSelector;
    protected final TextPaint checkPaint;
    protected final TextPaint clearFallbackGlyphDisabledPaint;
    protected final TextPaint clearFallbackGlyphPaint;
    protected final TextPaint clearGlyphDisabledPaint;
    protected final TextPaint clearGlyphPaint;
    protected final TextPaint clearTextDisabledPaint;
    protected final TextPaint clearTextPaint;
    private final int deleteType;
    protected final TextPaint digitDisabledPaint;
    protected final TextPaint digitPaint;
    protected StateListDrawable digitSelector;
    private final boolean drawLeftLine;
    private final boolean drawRightLine;
    private final boolean drawTopLine;
    private final boolean drawBottomLine;
    private final boolean horizontalDividerStyle;
    private float keyHeight;
    private float keyWidth;
    private final Map<Key, ButtonInfo> keys;
    protected final TextPaint lettersDisabledPaint;
    protected final TextPaint lettersPaint;
    private final int lettersTypefaceSize;
    protected final Paint linePaint;
    protected final float lineSpacingExtra;
    private final int lineWidth;
    private final List<Padlock.CanvasLine> lines;
    private Handler longPressHandler;
    protected Padlock.OnKeyPressListener onKeyPressListener;
    private final boolean pinMode;
    protected Padlock.PinPadLeftButtonState pinPadLeftButtonState;
    protected Padlock.PinPadRightButtonState pinPadRightButtonState;
    private SparseArray<ButtonInfo> pressedButtons;
    private final boolean showDecimal;
    protected final TextPaint submitDisabledPaint;
    protected final TextPaint submitPaint;
    protected StateListDrawable submitSelector;
    private final int submitType;
    private final boolean tabletMode;
    private final int typefaceSize;

    public Padlock(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setWillNotDraw(false);
        boolean tinyDevice;
        if (smallestWidth(context) < TINY_DEVICE_DP) {
            tinyDevice = true;
        } else {
            tinyDevice = false;
        }

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Padlock);
        this.tabletMode = typedArray.getBoolean(R.styleable.Padlock_tabletMode, false);
        this.pinMode = typedArray.getBoolean(R.styleable.Padlock_pinMode, false);
        this.drawLeftLine = typedArray.getBoolean(R.styleable.Padlock_drawLeftLine, false);
        this.drawTopLine = typedArray.getBoolean(R.styleable.Padlock_drawTopLine, true);
        this.drawBottomLine = typedArray.getBoolean(R.styleable.Padlock_drawBottomLine, false);
        this.drawRightLine = typedArray.getBoolean(R.styleable.Padlock_drawRightLine, false);
        this.horizontalDividerStyle = typedArray.getBoolean(R.styleable.Padlock_horizontalDividerStyle, false);
        this.showDecimal = typedArray.getBoolean(R.styleable.Padlock_showDecimal, false);
        if (this.isPinMode()) {
            submitType = SUBMIT_CHECK;
        } else {
            submitType = typedArray.getInt(R.styleable.Padlock_submitType, SUBMIT_ADD);
        }
        if (this.isPinMode()) {
            deleteType = DELETE_TYPE_CLEAR_OR_CANCEL;
        } else {
            deleteType = typedArray.getInt(R.styleable.Padlock_deleteType, DELETE_TYPE_CLEAR_GLYPH);
        }

        int letterSize = typedArray.getDimensionPixelSize(R.styleable.Padlock_lettersTextSize, RESOURCE_NOT_SET);
        Resources resources = this.getResources();
        if (letterSize == RESOURCE_NOT_SET) {
            if (tinyDevice) {
                letterSize = resources.getDimensionPixelSize(R.dimen.keypad_letters_text_size_small);
            } else {
                letterSize = resources.getDimensionPixelSize(R.dimen.keypad_letters_text_size);
            }
            this.lettersTypefaceSize = letterSize;
        } else {
            this.lettersTypefaceSize = letterSize;
        }

        int btnTextSize = typedArray.getDimensionPixelSize(R.styleable.Padlock_buttonTextSize, RESOURCE_NOT_SET);
        if (btnTextSize == RESOURCE_NOT_SET) {
            if (this.isPinMode()) {
                if (tinyDevice) {
                    btnTextSize = resources.getDimensionPixelSize(R.dimen.pinpad_text_size_small);
                } else {
                    btnTextSize = resources.getDimensionPixelSize(R.dimen.pinpad_text_size);
                }
                this.typefaceSize = btnTextSize;
            } else {
                if (tinyDevice) {
                    btnTextSize = resources.getDimensionPixelSize(R.dimen.keypad_text_size_small);
                } else {
                    btnTextSize = resources.getDimensionPixelSize(R.dimen.keypad_text_size);
                }
                this.typefaceSize = btnTextSize;
            }
        } else {
            this.typefaceSize = btnTextSize;
        }

        if (this.isPinMode()) {
            this.pinPadRightButtonState = Padlock.PinPadRightButtonState.SKIP_ENABLED;
            this.pinPadLeftButtonState = Padlock.PinPadLeftButtonState.CANCEL;
            this.setFilterTouchesWhenObscured(true);
        }

        this.digitSelector = (StateListDrawable) typedArray.getDrawable(R.styleable.Padlock_buttonSelector);
        if (this.digitSelector == null) {
            this.digitSelector = (StateListDrawable) resources.getDrawable(R.drawable.keypad_selector);
        }

        this.submitSelector = (StateListDrawable) typedArray.getDrawable(R.styleable.Padlock_submitSelector);
        if (this.submitSelector == null) {
            int selector;
            if (this.submitType == SUBMIT_ADD) {
                selector = R.drawable.keypad_selector;
            } else {
                selector = R.drawable.check_selector;
            }
            this.submitSelector = (StateListDrawable) resources.getDrawable(selector);
        }

        this.backspaceSelector = (StateListDrawable) typedArray.getDrawable(R.styleable.Padlock_backspaceSelector);
        if (this.backspaceSelector == null) {
            this.backspaceSelector = (StateListDrawable) resources.getDrawable(R.drawable.keypad_selector);
        }

        int lineColor = typedArray.getColor(R.styleable.Padlock_lineColor, resources.getColor(R.color.line_color));
        this.linePaint = new Paint();
        if (this.tabletMode) {
            this.linePaint.setStrokeWidth(2.0F);
            this.lineWidth = 2;
        } else {
            this.linePaint.setStrokeWidth(0.0F);
            this.lineWidth = 1;
        }
        this.linePaint.setColor(lineColor);

        TextPaint basicPaint = new TextPaint();
        basicPaint.setFlags(Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG);
        basicPaint.setTextAlign(Paint.Align.CENTER);
        basicPaint.setTextSize(this.typefaceSize);
//        TextPaint basicGlyphTypefacePaint = new TextPaint(basicPaint);
//        basicGlyphTypefacePaint.setTypeface(PadlockTypeface.getGlyphTypeface(resources));
        TextPaint basicMarinGlyphTypefacePaint = new TextPaint(basicPaint);
//        var10.setTypeface(MarinTypeface.getGlyphFont(var12));
        basicMarinGlyphTypefacePaint.setTextSize((float) resources.getDimensionPixelSize(R.dimen.marin_glyph_font_size));
        int digitColor = typedArray.getColor(R.styleable.Padlock_digitColor, resources.getColor(R.color.black_light));
        int digitColorDisabled = typedArray.getColor(R.styleable.Padlock_digitDisabledColor, resources.getColor(R.color.text_disabled));
        this.digitPaint = new TextPaint(basicPaint);
        this.digitPaint.setColor(digitColor);
        this.digitDisabledPaint = new TextPaint(basicPaint);
        this.digitDisabledPaint.setColor(digitColorDisabled);
        int defaultSubmitColor;
        int defaultSubmitDisabledColor;
        if (this.submitType == 1) {
            defaultSubmitColor = R.color.add_color;
            defaultSubmitDisabledColor = R.color.add_color_disabled;
        } else {
            defaultSubmitColor = R.color.check_color;
            defaultSubmitDisabledColor = R.color.check_color_disabled;
        }

        int submitColor = typedArray.getColor(R.styleable.Padlock_submitColor, resources.getColor(defaultSubmitColor));
        int submitDisabledColor = typedArray.getColor(R.styleable.Padlock_submitDisabledColor, resources.getColor(defaultSubmitDisabledColor));
        this.submitPaint = new TextPaint(basicPaint);
        this.submitPaint.setColor(submitColor);
        this.submitDisabledPaint = new TextPaint(basicPaint);
        this.submitDisabledPaint.setColor(submitDisabledColor);
        this.clearGlyphPaint = new TextPaint(basicPaint);
        this.clearGlyphPaint.setColor(digitColor);
        this.clearGlyphDisabledPaint = new TextPaint(basicPaint);
        this.clearGlyphDisabledPaint.setColor(digitColorDisabled);
        int clearTextColor = typedArray.getColor(R.styleable.Padlock_clearTextColor, digitColor);
        int clearTextDisabledColor = typedArray.getColor(R.styleable.Padlock_clearTextDisabledColor, digitColor);
        this.clearTextPaint = new TextPaint(basicPaint);
        this.clearTextPaint.setColor(clearTextColor);
        this.clearTextDisabledPaint = new TextPaint(basicPaint);
        this.clearTextDisabledPaint.setColor(clearTextDisabledColor);
        this.clearFallbackGlyphPaint = new TextPaint(basicMarinGlyphTypefacePaint);
        this.clearFallbackGlyphPaint.setColor(clearTextColor);
        this.clearFallbackGlyphDisabledPaint = new TextPaint(basicMarinGlyphTypefacePaint);
        this.clearFallbackGlyphDisabledPaint.setColor(clearTextDisabledColor);
        if (!this.tabletMode && this.deleteType != DELETE_TYPE_BACKSPACE_GLYPH) {
            this.backspacePaint = null;
            this.backspaceDisabledPaint = null;
        } else {
            int backspaceColor = typedArray.getColor(R.styleable.Padlock_backspaceColor, resources.getColor(R.color.black_light));
            int backspaceColorDisabled = typedArray.getColor(R.styleable.Padlock_backspaceDisabledColor,
                    resources.getColor(R.color.backspace_disabled));
            this.backspacePaint = new TextPaint(basicPaint);
            this.backspacePaint.setColor(backspaceColor);
            this.backspaceDisabledPaint = new TextPaint(basicPaint);
            this.backspaceDisabledPaint.setColor(backspaceColorDisabled);
            if (this.deleteType == DELETE_TYPE_BACKSPACE_GLYPH) {
                float scaledTypefaceSize = (float) this.typefaceSize * LEFT_BACKSPACE_SCALING_FACTOR;
                this.backspacePaint.setTextSize(scaledTypefaceSize);
                this.backspaceDisabledPaint.setTextSize(scaledTypefaceSize);
            }
        }

        this.checkPaint = new TextPaint(basicPaint);
        int checkColor;
        if (this.isPinMode()) {
            checkColor = typedArray.getColor(R.styleable.Padlock_pinSubmitColor, resources.getColor(R.color.pin_submit_color));
        } else {
            checkColor = submitColor;
        }
        this.checkPaint.setColor(checkColor);
        if (this.isPinMode()) {
            this.lineSpacingExtra = (float) typedArray.getDimensionPixelSize(R.styleable.Padlock_lineSpacingExtra,
                    resources.getDimensionPixelSize(R.dimen.keypad_line_spacing_extra));
            int lettersColor = typedArray.getColor(R.styleable.Padlock_lettersColor, resources.getColor(R.color.letters_text_color));
            int lettersColorDisabled = typedArray.getColor(R.styleable.Padlock_lettersDisabledColor, resources.getColor(R.color
                    .letters_text_color_disabled));
            this.lettersPaint = new TextPaint(basicPaint);
            this.lettersPaint.setTextSize((float) this.lettersTypefaceSize);
            this.lettersPaint.setColor(lettersColor);
            this.lettersDisabledPaint = new TextPaint(basicPaint);
            this.lettersDisabledPaint.setTextSize((float) this.lettersTypefaceSize);
            this.lettersDisabledPaint.setColor(lettersColorDisabled);
        } else {
            this.lineSpacingExtra = 0.0F;
            this.lettersPaint = null;
            this.lettersDisabledPaint = null;
        }

        this.keys = new LinkedHashMap<>();
        this.keys.put(Key.UNKNOWN, this.buildNone());
        if (this.isPinMode()) {
            this.keys.put(Key.ONE, this.buildDigit(1, "", Key.ONE));
            this.keys.put(Key.TWO, this.buildDigit(2, "ABC", Key.TWO));
            this.keys.put(Key.THREE, this.buildDigit(3, "DEF", Key.THREE));
            this.keys.put(Key.FOUR, this.buildDigit(4, "GHI", Key.FOUR));
            this.keys.put(Key.FIVE, this.buildDigit(5, "JKL", Key.FIVE));
            this.keys.put(Key.SIX, this.buildDigit(6, "MNO", Key.SIX));
            this.keys.put(Key.SEVEN, this.buildDigit(7, "PQRS", Key.SEVEN));
            this.keys.put(Key.EIGHT, this.buildDigit(8, "TUV", Key.EIGHT));
            this.keys.put(Key.NINE, this.buildDigit(9, "WXYZ", Key.NINE));
            this.keys.put(Key.ZERO, this.buildDigit(0, null, Key.ZERO));
        } else {
            this.keys.put(Key.ONE, this.buildDigit(1, Key.ONE));
            this.keys.put(Key.TWO, this.buildDigit(2, Key.TWO));
            this.keys.put(Key.THREE, this.buildDigit(3, Key.THREE));
            this.keys.put(Key.FOUR, this.buildDigit(4, Key.FOUR));
            this.keys.put(Key.FIVE, this.buildDigit(5, Key.FIVE));
            this.keys.put(Key.SIX, this.buildDigit(6, Key.SIX));
            this.keys.put(Key.SEVEN, this.buildDigit(7, Key.SEVEN));
            this.keys.put(Key.EIGHT, this.buildDigit(8, Key.EIGHT));
            this.keys.put(Key.NINE, this.buildDigit(9, Key.NINE));
            this.keys.put(Key.ZERO, this.buildDigit(0, Key.ZERO));
        }

        if (this.showDecimal) {
            this.keys.put(Key.DECIMAL, this.buildDecimal());
        }

        if (this.tabletMode) {
            this.keys.put(Key.SUBMIT, this.buildRoundedPlus());
            this.keys.put(Key.BACKSPACE, this.buildBackspaceGlyph());
            if (!this.showDecimal) {
                this.keys.put(Key.DOUBLE_ZERO, this.buildDoubleZero());
            }
        } else if (this.submitType != 0 && this.submitType != 3) {
            this.keys.put(Key.SUBMIT, this.buildSubmitKeyType());
            this.addClearOrBackspace();
        } else {
            this.addClearOrBackspace();
        }

        this.lines = new ArrayList<>(9);
        this.pressedButtons = new SparseArray<>();
        this.longPressHandler = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message var1) {
                int var4 = var1.what;
                float var2 = (float) var1.arg1;
                float var3 = (float) var1.arg2;
                Padlock.this.handleLongPress(var4, var2, var3);
                return true;
            }
        });
        this.backspaceHandler = new Handler(Looper.getMainLooper());
        this.backspaceRunnable = new Runnable() {
            public void run() {
                ButtonInfo var1 = Padlock.this.keys.get(Key.BACKSPACE);
                var1.click((var1.getLeft() + var1.getRight()) / 2.0F, (var1.getTop() + var1.getBottom()) / 2.0F);
                Padlock.this.backspaceHandler.postDelayed(Padlock.this.backspaceRunnable, KEY_REPEAT_TIMEOUT);
            }
        };
        typedArray.recycle();
    }

    private void addClearOrBackspace() {
        switch (this.deleteType) {
            case DELETE_TYPE_CLEAR_GLYPH:
                this.keys.put(Key.CLEAR_OR_CANCEL, this.buildClearGlyph());
                return;
            case DELETE_TYPE_CLEAR_TEXT:
                this.keys.put(Key.CLEAR_OR_CANCEL, this.buildClearText());
                return;
            case DELETE_TYPE_BACKSPACE_GLYPH:
                this.keys.put(Key.BACKSPACE, this.buildBackspaceGlyph());
                return;
            case DELETE_TYPE_CLEAR_OR_CANCEL:
                this.keys.put(Key.CLEAR_OR_CANCEL, this.buildClearOrCancel());
                return;
            default:
                throw new IllegalStateException("Unknown Delete Button Type!");
        }
    }

    private ButtonInfo buildAdd() {
        return new ButtonInfo(this, this.getSubmitSelector(), this.submitPaint, this.submitDisabledPaint,
//                Glyph.PLUS.getLetter(), Padlock.ContentType.PADLOCK_GLYPH, Key.PLUS) {
                "添加", Padlock.ContentType.PADLOCK_GLYPH, Key.PLUS) {
            protected void click(float var1, float var2) {
                if (Padlock.this.onKeyPressListener != null) {
                    Padlock.this.onKeyPressListener.onSubmitClicked();
                }

            }
        };
    }

    private ButtonInfo buildBackspaceGlyph() {
        return new ButtonInfo(this, this.getBackspaceSelector(), this.backspacePaint, this.backspaceDisabledPaint,
                Glyph.BACKSPACE.getLetter(), Padlock.ContentType.PADLOCK_GLYPH, Key.BACKSPACE) {
            protected void click(float var1, float var2) {
                if (Padlock.this.onKeyPressListener != null) {
                    Padlock.this.onKeyPressListener.onBackspaceClicked();
                }

            }
        };
    }

    private ButtonInfo buildCheckOrSkip() {
        return new CheckOrSkipButton(this, this.getSubmitSelector(), this.digitPaint, this.digitDisabledPaint, this
                .checkPaint, Padlock.ContentType.PADLOCK_GLYPH, Key.CHECK_OR_SKIP);
    }

    private ButtonInfo buildClearFallbackGlyph() {
        return new ClearButtonInfo(this, this.getDigitSelector(), this.clearFallbackGlyphPaint, this
                .clearFallbackGlyphDisabledPaint, Glyph.BACKSPACE
                .getLetter(), Padlock.ContentType.MARIN_GLYPH, Key.CLEAR_OR_CANCEL);
    }

    private ButtonInfo buildClearGlyph() {
        return new ClearButtonInfo(this, this.getDigitSelector(), this.clearGlyphPaint, this
//                .clearGlyphDisabledPaint, Glyph.C.getLetter(), Padlock.ContentType.PADLOCK_GLYPH, Key
                .clearGlyphDisabledPaint, "清除", Padlock.ContentType.PADLOCK_GLYPH, Key
                .CLEAR_OR_CANCEL);
    }

    private ButtonInfo buildClearOrCancel() {
        return new ClearOrCancelButton(this, this.getDigitSelector(), this.digitPaint, this.digitDisabledPaint,
                Padlock.ContentType.PADLOCK_GLYPH, Key.CLEAR_OR_CANCEL);
    }

    private ButtonInfo buildClearText() {
        return new ClearButtonInfo(this, this.getDigitSelector(), this.clearTextPaint, this.clearTextDisabledPaint,
                this.getResources().getString(R.string.keypad_panel_text_clear), Padlock.ContentType.TEXT, Key
                .CLEAR_OR_CANCEL);
    }

    private ButtonInfo buildDecimal() {
        return new ButtonInfo(this, this.getDigitSelector(), this.digitPaint, this.digitDisabledPaint, ".", Padlock
                .ContentType.TEXT, Key.DECIMAL) {
            protected void click(float var1, float var2) {
                if (Padlock.this.onKeyPressListener != null) {
                    Padlock.this.onKeyPressListener.onDecimalClicked();
                }

            }
        };
    }

    private ButtonInfo buildDigit(int var1, Key var2) {
        return this.buildDigit(var1, (String) null, var2);
    }

    private ButtonInfo buildDigit(final int var1, String var2, final Key var3) {
        return new ButtonInfo(this, this.getDigitSelector(), this.digitPaint, this.digitDisabledPaint, String
                .valueOf(var1), this.lettersPaint, this.lettersDisabledPaint, this.separatedWith(var2, SEPARATOR), Padlock
                .ContentType.TEXT, var3) {
            protected void click(float var1x, float var2) {
                if (Padlock.this.onKeyPressListener != null) {
                    if (!Padlock.this.isPinMode()) {
                        Padlock.this.onKeyPressListener.onDigitClicked(var1);
                        return;
                    }

                    Padlock.this.onKeyPressListener.onPinDigitEntered(var1x, var2);
                }

            }
        };
    }

    private ButtonInfo buildDoubleZero() {
        return new ButtonInfo(this, this.getDigitSelector(), this.digitPaint, this.digitDisabledPaint, "00", this
                .lettersPaint, this.lettersDisabledPaint, (String) null, Padlock.ContentType.TEXT, Key
                .DOUBLE_ZERO) {
            protected void click(float var1, float var2) {
                if (Padlock.this.onKeyPressListener != null) {
                    Padlock.this.onKeyPressListener.onDigitClicked(0);
                    Padlock.this.onKeyPressListener.onDigitClicked(0);
                }

            }
        };
    }

    private ButtonInfo buildNone() {
        return new ButtonInfo(this, (StateListDrawable) null, (TextPaint) null, (TextPaint) null, (String) null,
                Padlock.ContentType.TEXT, Key.UNKNOWN) {
            protected void click(float var1, float var2) {
            }

            public boolean contains(float x, float y) {
                return false;
            }

            public void draw(Canvas canvas) {
            }
        };
    }

    private ButtonInfo buildRoundedPlus() {
        return new ButtonInfo(this, this.getSubmitSelector(), this.submitPaint, this.submitDisabledPaint, Glyph
                .PLUS.getLetter(), Padlock.ContentType.PADLOCK_GLYPH, Key.ROUNDED_PLUS) {
            protected void click(float var1, float var2) {
                if (Padlock.this.onKeyPressListener != null) {
                    Padlock.this.onKeyPressListener.onSubmitClicked();
                }
            }
        };
    }

    private ButtonInfo buildSubmitKeyType() {
        switch (this.submitType) {
            case SUBMIT_GONE:
                return this.buildNone();
            case SUBMIT_ADD:
                return this.buildAdd();
            case SUBMIT_CHECK:
                return this.buildCheckOrSkip();
            default:
                throw new IllegalStateException("Unknown Submit Button Type!");
        }
    }

    private void cancelOutside(MotionEvent event) {
        for (int index = 0; index < event.getPointerCount(); ++index) {
            int pointerId = event.getPointerId(index);
            float x = event.getX(index);
            float y = event.getY(index);
            ButtonInfo button = this.pressedButtons.get(pointerId);
            if (button != null && !button.contains(x, y)) {
                button.clearPress();
                this.longPressHandler.removeMessages(pointerId);
                this.backspaceHandler.removeCallbacks(this.backspaceRunnable);
                this.pressedButtons.remove(pointerId);
                this.invalidate();
            }
        }
    }

    private ButtonInfo findButton(float x, float y) {
        for (ButtonInfo buttonInfo : keys.values()) {
            if (buttonInfo.contains(x, y)) {
                return buttonInfo;
            }
        }
        return null;
    }

    private StateListDrawable getBackspaceSelector() {
        return (StateListDrawable) this.backspaceSelector.getConstantState().newDrawable();
    }

    private StateListDrawable getDigitSelector() {
        return (StateListDrawable) this.digitSelector.getConstantState().newDrawable();
    }

    private Padlock.CanvasLine getLeftEdgeLine(final float leftX, final float topY, final float bottomY) {
        return new Padlock.CanvasLine() {
            public void draw(Canvas canvas) {
                canvas.drawRect(leftX, topY, (float) Padlock.this.lineWidth + leftX, bottomY, Padlock.this.linePaint);
            }
        };
    }

    private Padlock.CanvasLine getRightEdgeLine(final float rightX, final float topY, final float bottomY) {
        return new Padlock.CanvasLine() {
            public void draw(Canvas canvas) {
                canvas.drawRect(rightX - Padlock.this.lineWidth, topY, rightX, bottomY, Padlock.this.linePaint);
            }
        };
    }

    private StateListDrawable getSubmitSelector() {
        return (StateListDrawable) this.submitSelector.getConstantState().newDrawable();
    }

    private Padlock.CanvasLine getTopEdgeLine(final float leftX, final float rightX, final float topY) {
        return new Padlock.CanvasLine() {
            public void draw(Canvas canvas) {
                canvas.drawRect(leftX, topY, rightX, Padlock.this.lineWidth + topY, Padlock.this.linePaint);
            }
        };
    }

    private Padlock.CanvasLine getBottomEdgeLine(final float leftX, final float rightX, final float bottomY) {
        return new Padlock.CanvasLine() {
            public void draw(Canvas canvas) {
                canvas.drawRect(leftX, bottomY - Padlock.this.lineWidth, rightX, bottomY, Padlock.this.linePaint);
            }
        };
    }

    private void handleLongPress(int pointerId, float x, float y) {
        ButtonInfo button = this.pressedButtons.get(pointerId);
        if (button != null && button.onLongClick(x, y)) {
            button.clearPress();
            this.invalidate();
        }
    }

    private void handleTouch(long eventTime, int pointerId, int action, float x, float y) {
        ButtonInfo buttonInfo;
        if (action == MotionEvent.ACTION_DOWN) {
            buttonInfo = this.findButton(x, y);
            if (buttonInfo != null && !this.isPressed(buttonInfo)) {
                this.pressedButtons.append(pointerId, buttonInfo);
                if (buttonInfo.equals(this.keys.get(Key.BACKSPACE))) {
                    this.backspaceHandler.postDelayed(this.backspaceRunnable, (long) (TAP_TIMEOUT + KEY_REPEAT_TIMEOUT));
                } else if (!this.tabletMode && buttonInfo.equals(this.keys.get(Key.CLEAR_OR_CANCEL))) {
                    this.longPressHandler.sendMessageAtTime(this.longPressHandler.obtainMessage(pointerId, (int) x,
                            (int) y), (long) TAP_TIMEOUT + eventTime + (long) LONGPRESS_TIMEOUT);
                }

                buttonInfo.onPressed(x, y);
                this.invalidate();
            }
        } else if (action == MotionEvent.ACTION_UP) {
            buttonInfo = this.pressedButtons.get(pointerId);
            if (buttonInfo != null) {
                this.longPressHandler.removeMessages(pointerId);
                this.backspaceHandler.removeCallbacks(this.backspaceRunnable);
                this.pressedButtons.remove(pointerId);
                buttonInfo.onClick(x, y);
                this.invalidate();
            }
        }
    }

    private void initializeMobileLayout() {
        float x0 = 0.0F;
        float x1 = this.keyWidth;
        float x2 = this.keyWidth * 2.0F;
        float x3 = this.keyWidth * 3.0F;
        float y0 = 0.0F;
        float y1 = this.keyHeight;
        float y2 = this.keyHeight * 2.0F;
        float y3 = this.keyHeight * 3.0F;
        float y4 = this.keyHeight * 4.0F;
        keys.get(Key.ONE).setLocation(x0, y0, x1, y1);
        keys.get(Key.TWO).setLocation(x1, y0, x2, y1);
        keys.get(Key.THREE).setLocation(x2, y0, x3, y1);
        keys.get(Key.FOUR).setLocation(x0, y1, x1, y2);
        keys.get(Key.FIVE).setLocation(x1, y1, x2, y2);
        keys.get(Key.SIX).setLocation(x2, y1, x3, y2);
        keys.get(Key.SEVEN).setLocation(x0, y2, x1, y3);
        keys.get(Key.EIGHT).setLocation(x1, y2, x2, y3);
        keys.get(Key.NINE).setLocation(x2, y2, x3, y3);
        Key deleteKey;
        if (this.deleteType == DELETE_TYPE_BACKSPACE_GLYPH) {
            deleteKey = Key.BACKSPACE;
        } else {
            deleteKey = Key.CLEAR_OR_CANCEL;
        }

        ButtonInfo deleteButton = this.keys.get(deleteKey);
        deleteButton.setLocation(x0, y3, x1, y4);
        if (deleteKey == Key.CLEAR_OR_CANCEL) {
            float buttonWidth = (x1 - x0);
            float contentWidth = deleteButton.getContentPaint().measureText(deleteButton.getContent());
            if(contentWidth > buttonWidth) {
                deleteButton = this.buildClearFallbackGlyph();
                deleteButton.setLocation(x0, y3, x1, y4);
                this.keys.put(deleteKey, deleteButton);
            }
        }

        if (this.submitType == SUBMIT_INVISIBLE) {
            this.keys.get(Key.ZERO).setLocation(x1, y3, x2, y4);
        } else if (this.submitType == SUBMIT_GONE) {
            if (this.showDecimal) {
                this.keys.get(Key.ZERO).setLocation(x1, y3, x2, y4);
                this.keys.get(Key.DECIMAL).setLocation(x2, y3, x3, y4);
            } else {
                this.keys.get(Key.ZERO).setLocation(x1, y3, x3, y4);
            }
        } else {
            this.keys.get(Key.ZERO).setLocation(x1, y3, x2, y4);
            this.keys.get(Key.SUBMIT).setLocation(x2, y3, x3, y4);
        }

        this.lines.clear();
        if (this.horizontalDividerStyle) {
            float padding = this.getResources().getDimension(R.dimen.keypad_horizontal_divider_padding);
            float leftCol0 = x0 + padding;
            float rightCol0 = x1 - padding;
            float leftCol1 = x1 + padding;
            float rightCol1 = x2 - padding;
            float leftCol2 = x2 + padding;
            float rightCol2 = x3 - padding;
            this.lines.add(new Padlock.Line(leftCol0, y1, rightCol0, y1));
            this.lines.add(new Padlock.Line(leftCol0, y2, rightCol0, y2));
            this.lines.add(new Padlock.Line(leftCol0, y3, rightCol0, y3));
            this.lines.add(new Padlock.Line(leftCol1, y1, rightCol1, y1));
            this.lines.add(new Padlock.Line(leftCol1, y2, rightCol1, y2));
            this.lines.add(new Padlock.Line(leftCol1, y3, rightCol1, y3));
            this.lines.add(new Padlock.Line(leftCol2, y1, rightCol2, y1));
            this.lines.add(new Padlock.Line(leftCol2, y2, rightCol2, y2));
            this.lines.add(new Padlock.Line(leftCol2, y3, rightCol2, y3));
        } else {
            if (this.drawTopLine) {
                this.lines.add(this.getTopEdgeLine(x0, x3, y0));
            }
            if (this.drawBottomLine) {
                this.lines.add(this.getBottomEdgeLine(x0, x3, y4));
            }
            if (this.drawLeftLine) {
                this.lines.add(this.getLeftEdgeLine(x0, y0, y4));
            }
            if (this.drawRightLine) {
                this.lines.add(this.getRightEdgeLine(x3, y0, y4));
            }
            this.lines.add(new Padlock.Line(x0, y1, x3, y1));
            this.lines.add(new Padlock.Line(x0, y2, x3, y2));
            this.lines.add(new Padlock.Line(x0, y3, x3, y3));
            this.lines.add(new Padlock.Line(x1, y0, x1, y4));
            if (this.submitType == 0 && !this.showDecimal) {
                this.lines.add(new Padlock.Line(x2, y0, x2, y3));
            } else {
                this.lines.add(new Padlock.Line(x2, y0, x2, y4));
            }
        }
    }

    private void initializeTabletLayout() {
        float x0 = 0.0F;
        float x1 = this.keyWidth;
        float x2 = this.keyWidth * 2.0F;
        float x3 = this.keyWidth * 3.0F;
        float x4 = this.keyWidth * 4.0F;
        float y0 = 0.0F;
        float y1 = this.keyHeight;
        float y2 = this.keyHeight * 2.0F;
        float y3 = this.keyHeight * 3.0F;
        float y4 = this.keyHeight * 4.0F;
        this.keys.get(Key.ONE).setLocation(x0, y0, x1, y1);
        this.keys.get(Key.TWO).setLocation(x1, y0, x2, y1);
        this.keys.get(Key.THREE).setLocation(x2, y0, x3, y1);
        this.keys.get(Key.FOUR).setLocation(x0, y1, x1, y2);
        this.keys.get(Key.FIVE).setLocation(x1, y1, x2, y2);
        this.keys.get(Key.SIX).setLocation(x2, y1, x3, y2);
        this.keys.get(Key.SEVEN).setLocation(x0, y2, x1, y3);
        this.keys.get(Key.EIGHT).setLocation(x1, y2, x2, y3);
        this.keys.get(Key.NINE).setLocation(x2, y2, x3, y3);
        if (this.showDecimal) {
            this.keys.get(Key.DECIMAL).setLocation(x0, y3, x1, y4);
            this.keys.get(Key.ZERO).setLocation(x1, y3, x3, y4);
        } else {
            this.keys.get(Key.DOUBLE_ZERO).setLocation(x0, y3, x2, y4);
            this.keys.get(Key.ZERO).setLocation(x2, y3, x3, y4);
        }

        this.keys.get(Key.SUBMIT).setLocation(x3, y2, x4, y4);
        this.keys.get(Key.BACKSPACE).setLocation(x3, y0, x4, y2);
        this.lines.clear();
        if (this.drawTopLine) {
            this.lines.add(this.getTopEdgeLine(x0, x4, y0));
        }
        if (this.drawBottomLine) {
            this.lines.add(this.getBottomEdgeLine(x0, x4, y4));
        }

        if (this.drawLeftLine) {
            this.lines.add(this.getLeftEdgeLine(x0, y0, y4));
        }

        if (this.drawRightLine) {
            this.lines.add(this.getRightEdgeLine(x4, y0, y4));
        }

        this.lines.add(new Padlock.Line(x0, y1, x3, y1));
        this.lines.add(new Padlock.Line(x0, y2, x4, y2));
        this.lines.add(new Padlock.Line(x0, y3, x3, y3));
        if (this.showDecimal) {
            this.lines.add(new Padlock.Line(x1, y0, x1, y4));
            this.lines.add(new Padlock.Line(x2, y0, x2, y3));
        } else {
            this.lines.add(new Padlock.Line(x1, y0, x1, y3));
            this.lines.add(new Padlock.Line(x2, y0, x2, y4));
        }
        this.lines.add(new Padlock.Line(x3, y0, x3, y4));
    }

    private boolean isPressed(ButtonInfo buttonInfo) {
        return this.pressedButtons.indexOfValue(buttonInfo) >= 0;
    }

    private String separatedWith(String source, String delim) {
        if (source != null && !source.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append(source.charAt(0));
            for (int i = 1; i < source.length(); ++i) {
                sb.append(delim);
                sb.append(source.charAt(i));
            }
            return sb.toString();
        } else {
            return source;
        }
    }

    private static int smallestWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (Math.min(metrics.widthPixels, metrics.heightPixels) / metrics.density);
    }

    public ButtonInfo getButtonInfo(Key key) {
        return this.keys.get(key);
    }

    public int getLettersTypefaceSize() {
        return this.lettersTypefaceSize;
    }

    public float getLineSpaceExtra() {
        return this.lineSpacingExtra;
    }

    public int getTypefaceSize() {
        return this.typefaceSize;
    }

    public boolean hasAllButtonsEnabled() {
        for (ButtonInfo buttonInfo : keys.values()) {
            if (buttonInfo.isEnabled()) {
                return true;
            }
        }
        return false;
    }

    public boolean inPinMode() {
        return this.isPinMode();
    }

    public boolean isBackspaceEnabled() {
        ButtonInfo buttonInfo = this.keys.get(Key.BACKSPACE);
        return buttonInfo != null && buttonInfo.isEnabled();
    }

    public boolean isClearEnabled() {
        ButtonInfo buttonInfo = this.keys.get(Key.CLEAR_OR_CANCEL);
        return buttonInfo != null && buttonInfo.isEnabled();
    }

    public boolean isDigitsEnabled() {
        return this.keys.get(Key.ZERO).isEnabled();
    }

    public boolean isSubmitEnabled() {
        ButtonInfo buttonInfo = this.keys.get(Key.SUBMIT);
        return buttonInfo != null && buttonInfo.isEnabled();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(ButtonInfo buttonInfo : keys.values()) {
            buttonInfo.draw(canvas);
        }
        for(CanvasLine line : lines) {
            line.draw(canvas);
        }
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (this.tabletMode) {
            this.initializeTabletLayout();
        } else {
            this.initializeMobileLayout();
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int totalHeight = MeasureSpec.getSize(heightMeasureSpec);
        int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        this.keyHeight = totalHeight / 4.0F;
        if (this.tabletMode) {
            this.keyWidth = totalWidth / 4.0F;
        } else {
            this.keyWidth = totalWidth / 3.0F;
        }
        this.setMeasuredDimension(resolveSize(totalWidth, widthMeasureSpec), resolveSize(totalHeight, heightMeasureSpec));
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.isPinMode() && event.getSource() != InputDevice.SOURCE_TOUCHSCREEN) {
            return true;
        } else {
            int index = event.getActionIndex();
            int pointerId = event.getPointerId(index);
            float x = event.getX(index);
            float y = event.getY(index);
            index = event.getActionMasked();
            switch (index) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_UP:
                    this.handleTouch(event.getEventTime(), pointerId, index, x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    this.cancelOutside(event);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    this.pressedButtons.clear();
                    this.longPressHandler.removeCallbacksAndMessages(null);
                    this.backspaceHandler.removeCallbacksAndMessages(null);
                    Iterator var6 = this.keys.values().iterator();
                    while (var6.hasNext()) {
                        ((ButtonInfo) var6.next()).clearPress();
                    }
                    this.invalidate();
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    this.handleTouch(event.getEventTime(), pointerId, MotionEvent.ACTION_DOWN, x, y);
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    this.handleTouch(event.getEventTime(), pointerId, MotionEvent.ACTION_UP, x, y);
                    break;
                default:
                    return false;
            }
            return true;
        }
    }

    public void setAllButtonsEnabled(boolean enable) {
        for(ButtonInfo buttonInfo : keys.values()) {
            buttonInfo.setEnabled(enable);
        }
        this.invalidate();
    }

    public void setBackspaceEnabled(boolean enable) {
        ButtonInfo buttonInfo = this.keys.get(Key.BACKSPACE);
        if (buttonInfo != null) {
            buttonInfo.setEnabled(enable);
            this.invalidate();
        }
    }

    public void setClearEnabled(boolean enable) {
        ButtonInfo buttonInfo = this.keys.get(Key.CLEAR_OR_CANCEL);
        if (buttonInfo != null) {
            buttonInfo.setEnabled(enable);
            this.invalidate();
        }
    }

    public void setDeleteColor(int var1) {
        switch (this.deleteType) {
            case DELETE_TYPE_CLEAR_GLYPH:
                this.clearGlyphPaint.setColor(var1);
                break;
            case DELETE_TYPE_CLEAR_TEXT:
                this.clearTextPaint.setColor(var1);
                this.clearFallbackGlyphPaint.setColor(var1);
                break;
            case DELETE_TYPE_BACKSPACE_GLYPH:
                this.backspacePaint.setColor(var1);
                break;
            case DELETE_TYPE_CLEAR_OR_CANCEL:
                return;
        }
        this.invalidate();
    }

    public void setDeleteColorDisabled(int color) {
        switch (this.deleteType) {
            case DELETE_TYPE_CLEAR_GLYPH:
                this.clearGlyphDisabledPaint.setColor(color);
                break;
            case DELETE_TYPE_CLEAR_TEXT:
                this.clearTextDisabledPaint.setColor(color);
                this.clearFallbackGlyphDisabledPaint.setColor(color);
                break;
            case DELETE_TYPE_BACKSPACE_GLYPH:
                this.backspaceDisabledPaint.setColor(color);
                break;
            case DELETE_TYPE_CLEAR_OR_CANCEL:
                return;
        }
        this.invalidate();
    }

    public void setDigitColor(int color) {
        this.digitPaint.setColor(color);
        this.invalidate();
    }

    public void setDigitColorDisabled(int color) {
        this.digitDisabledPaint.setColor(color);
        this.invalidate();
    }

    public void setDigitsEnabled(boolean enable) {
        this.keys.get(Key.ZERO).setEnabled(enable);
        this.keys.get(Key.ONE).setEnabled(enable);
        this.keys.get(Key.TWO).setEnabled(enable);
        this.keys.get(Key.THREE).setEnabled(enable);
        this.keys.get(Key.FOUR).setEnabled(enable);
        this.keys.get(Key.FIVE).setEnabled(enable);
        this.keys.get(Key.SIX).setEnabled(enable);
        this.keys.get(Key.SEVEN).setEnabled(enable);
        this.keys.get(Key.EIGHT).setEnabled(enable);
        this.keys.get(Key.NINE).setEnabled(enable);
        if (this.tabletMode && !this.showDecimal) {
            this.keys.get(Key.DOUBLE_ZERO).setEnabled(enable);
        }
        this.invalidate();
    }

    public void setLineColor(int color) {
        this.linePaint.setColor(color);
        this.invalidate();
    }

    public void setOnKeyPressListener(Padlock.OnKeyPressListener listener) {
        this.onKeyPressListener = listener;
    }

    public void setPinPadLeftButtonState(Padlock.PinPadLeftButtonState state) {
        if (!this.isPinMode()) {
            throw new IllegalStateException("Pinpad left state can be only set in pinpad mode.");
        } else {
            this.pinPadLeftButtonState = state;
            ((ClearOrCancelButton) this.keys.get(Key.CLEAR_OR_CANCEL)).updateState();
        }
    }

    public void setPinPadRightButtonState(Padlock.PinPadRightButtonState state) {
        if (!this.isPinMode()) {
            throw new IllegalStateException("Pinpad right state can be only set in pinpad mode.");
        } else {
            this.pinPadRightButtonState = state;
            ((CheckOrSkipButton) this.keys.get(Key.SUBMIT)).updateState();
        }
    }

    public void setSubmitEnabled(boolean enabled) {
        ButtonInfo buttonInfo = this.keys.get(Key.SUBMIT);
        if (buttonInfo != null) {
            buttonInfo.setEnabled(enabled);
            this.invalidate();
        }
    }

    public void setTypeface(Typeface var1) {
        this.digitPaint.setTypeface(var1);
        this.digitDisabledPaint.setTypeface(var1);
        if (this.isPinMode()) {
            this.lettersPaint.setTypeface(var1);
            this.lettersDisabledPaint.setTypeface(var1);
        }
        if (this.deleteType == DELETE_TYPE_CLEAR_TEXT) {
            this.clearTextPaint.setTypeface(var1);
            this.clearTextDisabledPaint.setTypeface(var1);
        }
        this.invalidate();
    }

    public boolean isPinMode() {
        return pinMode;
    }

    private interface CanvasLine {
        void draw(Canvas var1);
    }

    public static enum ContentType {
        MARIN_GLYPH,
        PADLOCK_GLYPH,
        TEXT;
    }

    private class Line implements Padlock.CanvasLine {
        private float endX;
        private float endY;
        private float startX;
        private float startY;

        private Line(float startX, float startY, float endX, float endY) {
            this.startX = startX;
            this.endX = endX;
            this.startY = startY;
            this.endY = endY;
        }

        public void draw(Canvas canvas) {
            canvas.drawLine(startX, startY, endX, endY, Padlock.this.linePaint);
        }
    }

    public interface OnKeyPressListener {
        void onBackspaceClicked();

        void onCancelClicked();

        void onClearClicked();

        void onClearLongPressed();

        void onDecimalClicked();

        void onDigitClicked(int var1);

        void onPinDigitEntered(float var1, float var2);

        void onSkipClicked();

        void onSubmitClicked();
    }

    public abstract static class OnKeyPressListenerAdapter implements Padlock.OnKeyPressListener {
        public OnKeyPressListenerAdapter() {
        }

        public void onBackspaceClicked() {
        }

        public void onCancelClicked() {
        }

        public void onClearClicked() {
        }

        public void onClearLongPressed() {
        }

        public void onDecimalClicked() {
        }

        public void onDigitClicked(int var1) {
        }

        public void onPinDigitEntered(float var1, float var2) {
        }

        public void onSkipClicked() {
        }

        public void onSubmitClicked() {
        }
    }

    public static enum PinPadLeftButtonState {
        CANCEL,
        CANCEL_DISABLED,
        CLEAR,
        CLEAR_DISABLED;
    }

    public static enum PinPadRightButtonState {
        CHECK_INVALID,
        CHECK_VALID,
        SKIP_DISABLED,
        SKIP_ENABLED;
    }
}
