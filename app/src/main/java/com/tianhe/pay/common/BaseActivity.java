package com.tianhe.pay.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public abstract class BaseActivity extends AppCompatActivity
        implements FragmentManagersProvider, FragmentStack {

    // region Members

    protected FragmentManagers fragmentManagers;

    // endregion Members

    // region Lifecycle

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedState) {
        // because super.onCreate() can rebuild FragmentManager state,
        // so not need to check savedState == null,
        fragmentManagers = new FragmentManagers(this);
        fragmentManagers.onActivityCreate(savedState);
    }

    @Override
    protected void onDestroy() {
        fragmentManagers.onDestroy();
        fragmentManagers = null;
        super.onDestroy();
    }

    // endregion Lifecycle

    // region Inherited Methods

    @Override
    public FragmentManagers provideFragmentManagers() {
        return fragmentManagers;
    }

    // endregion Inherited Methods

    // region Instance Methods

    protected void setToolbar(int toolbarId) {
        Toolbar toolbar = (Toolbar) findViewById(toolbarId);
        if (toolbar != null) {
            setToolbar(toolbar);
            toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        }
    }

    protected void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
    }

    protected Toolbar findToolbar(int toolbarId) {
        return (Toolbar) findViewById(toolbarId);
    }

    protected FragmentManager getFM() {
        return getSupportFragmentManager();
    }

    // endregion Instance Methods
}
