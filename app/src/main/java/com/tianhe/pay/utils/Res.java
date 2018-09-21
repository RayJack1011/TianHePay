package com.tianhe.pay.utils;

import android.support.annotation.ArrayRes;
import android.support.annotation.BoolRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.StringRes;

public interface Res {
    boolean getBoolean(@BoolRes int id);

    int getColor(@ColorRes int colorId);

    float getDimension(@DimenRes int dimenId);

    int getDimensionPixelSize(@DimenRes int dimenId);

    int getInteger(@IntegerRes int id);

    String getString(@StringRes int stringId);

    String getString(@StringRes int stringId, Object... args);

    String[] getStringArray(@ArrayRes int arrId);
}
