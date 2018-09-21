package com.tianhe.pay.ui.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.tianhe.pay.R;
import com.tianhe.pay.ui.TianHeActivity;

public class OrderCountActivity extends TianHeActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar(R.id.toolbar);
        setTitle(R.string.drawer_left_today_statistics);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Fragment createSingleFragment() {
        return new OrderCountFragment();
    }
}
