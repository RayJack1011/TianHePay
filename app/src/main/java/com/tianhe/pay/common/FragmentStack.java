package com.tianhe.pay.common;

import android.support.v4.app.Fragment;

import com.tianhe.pay.common.StackableFragment;

public interface FragmentStack {
    boolean push(StackableFragment stackable, Fragment replace);

    boolean pop(StackableFragment stackable, Fragment replace);
}
