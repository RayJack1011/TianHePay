package com.tianhe.pay.widget.marin;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MarinBaseView extends FrameLayout implements Badgeable {
    public static final String NO_TEXT = "";

    private  int buttonPadding;
    private FrameLayout customViewContainer;
    private TextView upBadge;
    private TextView upText;
    private  int shortAnimTimeMs;
    private Button primaryButton;
    private View upButtonContainer;

    public MarinBaseView(@NonNull Context context) {
        super(context);
    }

    public MarinBaseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MarinBaseView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void hideBadge() {

    }

    @Override
    public void showBadge(CharSequence badge) {

    }
}
