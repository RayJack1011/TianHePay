package com.tianhe.pay.ui.cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.tianhe.pay.R;
import com.tianhe.pay.ui.TianHeActivity;

public class CartDetailActivity extends TianHeActivity {

    private static final String EXTRA_ITEM_INDEX = "itemIndex";

    public static Intent getStartIntent(Context context, int index) {
        Intent intent = new Intent(context, CartDetailActivity.class);
        intent.putExtra(EXTRA_ITEM_INDEX, index);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar(R.id.toolbar);
        setTitle("商品详情");
    }

    @Override
    protected Fragment createSingleFragment() {
        return CartDetailFragment.newInstance(getItemIndex());
    }

    private int getItemIndex() {
        return getIntent().getIntExtra(EXTRA_ITEM_INDEX, 0);
    }
}
