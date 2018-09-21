package com.tianhe.pay.ui;

import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.BoolRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.StringRes;

import com.tianhe.pay.utils.Res;

public class RealRes implements Res {

    private Context context;

    public RealRes(Context context) {
        this.context = context;
    }

    @Override
    public boolean getBoolean(@BoolRes int id) {
        return context.getResources().getBoolean(id);
    }

    @Override
    public int getColor(@ColorRes int colorId) {
        return context.getResources().getColor(colorId);
    }

    @Override
    public float getDimension(@DimenRes int dimenId) {
        return context.getResources().getDimension(dimenId);
    }

    @Override
    public int getDimensionPixelSize(@DimenRes int dimenId) {
        return context.getResources().getDimensionPixelSize(dimenId);
    }

    @Override
    public int getInteger(@IntegerRes int id) {
        return context.getResources().getInteger(id);
    }

    @Override
    public String getString(@StringRes int stringId) {
        return context.getString(stringId);
    }

    @Override
    public String getString(@StringRes int stringId, Object... args) {
        return context.getString(stringId, args);
    }

    @Override
    public String[] getStringArray(@ArrayRes int arrId) {
        return context.getResources().getStringArray(arrId);
    }
}
