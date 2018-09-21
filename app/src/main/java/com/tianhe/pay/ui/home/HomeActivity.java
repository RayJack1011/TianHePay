package com.tianhe.pay.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tianhe.pay.CommomData;
import com.tianhe.pay.R;
import com.tianhe.pay.data.DataSource;
import com.tianhe.pay.model.CartManager;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.ui.Nav;
import com.tianhe.pay.ui.TianHeActivity;
import com.tianhe.pay.ui.returncoupon.ReturnCouponActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

import static com.tianhe.pay.model.CartManager.Event.CART_ITEM_CHANGED;

public class HomeActivity extends TianHeActivity {

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra(EXTRA_START_REASON, REASON_NORMAL);
        return intent;
    }

    public static Intent getStartIntentByPos(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra(EXTRA_START_REASON, REASON_POS);
        return intent;
    }

    private static final String EXTRA_START_REASON = "startReason";
    private static final String REASON_NORMAL = "normal";
    private static final String REASON_POS = "pos";

    @Inject
    CartManager cartManager;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView titleTv;
    private TextView cartItemCount;
    private ActionBarDrawerToggle drawerToggle;
    private Disposable cartItemCountDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.findViewById(R.id.cart_header_current_sale).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!cartManager.isEmpty()) {
//                    entryCart();
//                } else {
//                    showMessage(R.string.error_cart_empty);
//                }
//                entryCart();
            }
        });
        setSupportActionBar(toolbar);
        titleTv = toolbar.findViewById(R.id.cart_header_current_sale_label);
        setTitle();
        cartItemCount = toolbar.findViewById(R.id.cart_header_sale_quantity);
        cartItemCountDisposable = cartManager.cartChanged().subscribeWith(new DefaultObserver<String>() {
            @Override
            public void onNext(String cartEvent) {
                if (CART_ITEM_CHANGED.equals(cartEvent)) {
                    updateCount();
                }
            }
        });


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_home_drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.activity_home_nav_drawer_left);
        navigationView.setNavigationItemSelectedListener(new NavListener(this, drawerLayout, nav, cartManager));
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideSoftInput();
            }
        };

        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommomData.signName = null;
        updateCount();
    }

    @Override
    protected void onDestroy() {
        navigationView.setNavigationItemSelectedListener(null);
        drawerLayout.removeDrawerListener(drawerToggle);
        cartItemCountDisposable.dispose();
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        dealStartReason();
    }

    @Override
    protected Fragment createSingleFragment() {
        return HomeFragment.newInstance();
    }

    @Override
    protected int layoutOfContentView() {
        return R.layout.activity_home;
    }

    @Override
    protected int containerViewId() {
        return R.id.activity_home_frame_container;
    }

    private void entryCart() {
        nav.enterCart(this);
    }

    private void updateCount() {
        cartItemCount.setText(String.valueOf(cartManager.getSaleCount()));
    }

    private void dealStartReason() {
        String reason = getIntent().getStringExtra(EXTRA_START_REASON);
        boolean startInPos = REASON_POS.equals(reason);
        boolean inPosMode = cartManager.inPosMode();
        if (startInPos != inPosMode) {
            cartManager.changeMode(startInPos);
            setTitle();
            if (startInPos) {
                showMessage("进入补录模式");
            }
        }
    }

    private void setTitle() {
        boolean inPosMode = cartManager.inPosMode();
        if (!inPosMode) {
            titleTv.setText(R.string.cart_actionbar_label);
        } else {
            titleTv.setText("手工补录");
        }
    }

    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private static class NavListener implements NavigationView.OnNavigationItemSelectedListener {

        private Activity hostContext;
        private DrawerLayout drawerLayout;
        private Nav nav;
        private CartManager cartManager;

        public NavListener(Activity hostContext,
                           DrawerLayout drawerLayout, Nav nav,
                           CartManager cartManager) {
            this.hostContext = hostContext;
            this.drawerLayout = drawerLayout;
            this.nav = nav;
            this.cartManager = cartManager;
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            boolean navigated = navToDestination(item.getItemId());
            if (navigated) {
                drawerLayout.closeDrawers();
            }
            return navigated;
        }

        private boolean navToDestination(int menuId) {
            switch (menuId) {
                case R.id.nav_home:
                    if (cartManager.inPosMode() && !cartManager.isEmpty()) {
                        showClearCartDialog(true);
                    } else {
                        nav.enterHome(hostContext);
                    }
                    return true;
                case R.id.nav_bill_history:
                    nav.enterOrderHistory(hostContext);
                    return true;
                case R.id.nav_today_statistics:
                    nav.enterTodayStatistics(hostContext);
                    return true;
                case R.id.nav_refund:
                    nav.enterQueryRefundableOrder(hostContext);
                    return true;
                case R.id.nav_reprint:
                    nav.enterWechatAliQuerySelector(hostContext);
                    return true;
                case R.id.nav_single_coupon://退券
//                    hostContext.startActivity(new Intent(hostContext, ReturnCouponActivity.class));
//                    nav.enterCouponReturn(hostContext);
//                    return true;
                case R.id.nav_settings:
                    nav.enterSettings(hostContext);
                    return true;
                case R.id.nav_single://单品退货
                    return true;
//                case R.id.nav_about:
//                    nav.enterAbout(hostContext);
//                    return true;
                default:
                    return false;
            }
        }

        private void showClearCartDialog(final boolean inPosMode) {
            String content;
            if (inPosMode) {
                content = "是否清空补录模式购物车?";
            } else {
                content = "是否清空销售模式购物车?";
            }
            MaterialDialog.Builder builder = new MaterialDialog.Builder(hostContext);
            builder.title(R.string.home_empty_cart_dialog_title);
            builder.content(content);
            builder.negativeText(R.string.home_empty_cart_dialog_cancel);
            builder.positiveText(R.string.home_empty_cart_dialog_ok);
            builder.onAny(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog dialog, DialogAction which) {
                    switch (which) {
                        case POSITIVE:
                            cartManager.clear();
                            if (inPosMode) {
                                nav.enterHome(hostContext);
                            } else {
                                nav.enterHomeByPos(hostContext);
                            }
                            break;
                    }
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }
}


