package com.tianhe.pay.ui.auth;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tianhe.pay.R;
import com.tianhe.pay.ui.TianHeActivity;

public class QueryAuthActivity extends TianHeActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findToolbar(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
    }

    @Override
    protected Fragment createSingleFragment() {
        return new QueryAuthFragment();
    }

}