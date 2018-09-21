package com.tianhe.pay.ui.welcome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tianhe.pay.R;
import com.tianhe.pay.ui.TianHeActivity;

public class WelcomeActivity extends TianHeActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideToolbar();
    }

    private void hideToolbar() {
        Toolbar toolbar = findToolbar(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setVisibility(View.GONE);
        }
    }

    @Override
    protected Fragment createSingleFragment() {
        return new WelcomeFragment();
    }

}
