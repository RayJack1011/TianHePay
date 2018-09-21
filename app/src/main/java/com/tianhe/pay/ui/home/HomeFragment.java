package com.tianhe.pay.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tianhe.pay.CommomData;
import com.tianhe.pay.R;
import com.tianhe.pay.common.BaseDialog;
import com.tianhe.pay.data.crm.member.Member;
import com.tianhe.pay.data.goods.Goods;
import com.tianhe.pay.data.order.OrderItem;
import com.tianhe.pay.model.CartManager;
import com.tianhe.pay.model.Global;
import com.tianhe.pay.di.PerActivity;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.ui.TianHeFragment;
import com.tianhe.pay.utils.Strings;
import com.tianhe.pay.utils.money.Money;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static com.tianhe.pay.model.CartManager.Event.CART_ITEM_CHANGED;
import static com.tianhe.pay.model.CartManager.Event.VIP_CHANGED;

@PerActivity
public class HomeFragment extends TianHeFragment implements HomeContract.View {
    // region Constants

    private static final int ID_DIALOG_CALCULATE_ITEM = BaseDialog.getAutoId();
    private static final int REQUEST_MEMBER_CODE = 1000;
    private static final long AMOUNT_MAX = 9999999;


    // endregion Constants

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    // region Members

    @Inject
    Global globalData;
    @Inject
    CartManager cartManager;
    @Inject
    HomeContract.Presenter presenter;

    private ViewHolder viewHolder;
    private Disposable cartDisposable;

    // endregion Members

    @Inject
    public HomeFragment() {
    }

    // region Lifecycle

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewHolder = new ViewHolder(view);
        initView();
    }

    private void initView() {
        getActivity().setTitle(null);
        // subtotal
        viewHolder.subTotalAndChargeBtn.setOnClickListener(new View.OnClickListener() {//结算
            @Override
            public void onClick(View v) {
                if (cartManager.getCartSize() > 0) {
                    //清空变量，防止签购单打印错误,hujie
                    CommomData.signName = null;
                    CommomData.authUser = null;
                    entryCart();
                } else {
                    Toast.makeText(getContext(), "请选购商品", Toast.LENGTH_LONG).show();
                }
//                entryCheckout();
            }
        });
        viewHolder.addBarcodeTv.setHintTextColor(getResources().getColor(R.color.text_primary_dark));
        // barcode
        registerForContextMenu(viewHolder.addBarcodeTv);
        viewHolder.addBarcodeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.showContextMenu();
            }
        });
        viewHolder.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitKeypadItem();
            }
        });
        viewHolder.priceTv.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String lastInputContent = dest.toString();
                String newInput = source.toString();
                if (lastInputContent.contains(".")) {
                    if (newInput.contains(".")) {
                        return "";
                    }
                    int index = lastInputContent.indexOf(".");
                    if (dend - index >= 3) {
                        return "";
                    }
                } else if ("0".equals(lastInputContent)) {
                    if ("0".equals(newInput)) {
                        return "";
                    }
                }
                return null;
            }
        }});
        viewHolder.priceTv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String price = v.getText().toString();
                    if (price.endsWith(".")) {
                        v.setText(price.substring(0, price.length() - 1));
                    }
                    hideSoftInput();
                    return true;
                }
                return false;
            }
        });

        viewHolder.addQuantityIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuantity();
            }
        });
        viewHolder.reduceQuantityIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reduceQuantity();
            }
        });
        viewHolder.lastItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLastItemDetail();
            }
        });

        viewHolder.deleteVipIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartManager.setVip(null);
            }
        });
        cartDisposable = cartManager.cartChanged().subscribeWith(new DefaultObserver<String>() {
            @Override
            public void onNext(@NonNull String cartEvent) {
                dealCartEvent(cartEvent);
            }
        });
        viewHolder.clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOrClear();
            }
        });
    }

    /**
     * 加入购物车
     */
    private void entryCart() {
        nav.enterCart(this);
    }


    private void resetOrClear() {
        String barcode = viewHolder.addBarcodeTv.getText().toString();
        String price = viewHolder.priceTv.getText().toString();
        String quantity = viewHolder.quantityEt.getText().toString();
        boolean clearCart = Strings.isBlank(barcode)
                && Strings.isBlank(price)
                && (Strings.isBlank(quantity) || "1".equals(quantity));
        if (clearCart) {
            if (!cartManager.isEmpty()) {
                showClearCartDialog();
            } else {
                showMessage("购物车空空的!");
            }
        } else {
            viewHolder.goodsInfo.setText("0件商品，共计：0.00元");
            resetKeypad();
        }
    }

    private void showLastItemDetail() {
        int cartSize = cartManager.getCartSize();
        if (cartSize > 0) {
            nav.enterCartDetail(this, cartSize - 1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        CommomData.signName = null;
        CommomData.isCouponReturn =false;
        updateSubtotal();
        updateVip();
        updateLastItem();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        cartDisposable.dispose();
        unregisterForContextMenu(viewHolder.addBarcodeTv);
        viewHolder = null;
        super.onDestroy();
    }

    // endregion Lifecycle

    // region Inherited Methods

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_MEMBER_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Member member = (Member) data.getSerializableExtra("data");
                cartManager.setVip(member);
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_vip, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_vip) {
            nav.enterQueryCrmMemberForResult(this, REQUEST_MEMBER_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.keypad_panel_add_barcode) {
            menu.setHeaderTitle(R.string.home_select_goods_dialog_title);
            List<Goods> goodsList = globalData.getSellingGoods();
            for (int i = 0; i < goodsList.size(); i++) {
                Goods goods = goodsList.get(i);
                menu.add(0, i, i, goods.getBarcode() + "\t" + goods.getName());
            }
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Goods selected = globalData.getSellingGoods().get(item.getOrder());
        selectGoods(selected);
        return true;
    }

    @Override
    protected HomeContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void calculatedSuccess() {
        dismissDialog(ID_DIALOG_CALCULATE_ITEM);
        viewHolder.goodsInfo.setText(cartManager.getCartSize() + "件商品，共计：" + cartManager.getSubTotal() + "元");
        resetKeypad();
    }

    @Override
    public void calculatedFail(String reason) {
        dismissDialog(ID_DIALOG_CALCULATE_ITEM);
        if (Strings.isBlank(reason)) {
            showMessage("无法获取商品折扣信息");
        } else {
            showMessage("无法获取商品折扣信息" + reason);
        }
    }

    private void dealCartEvent(String event) {
        if (VIP_CHANGED.equals(event)) {
            updateVip();
        } else if (CART_ITEM_CHANGED.equals(event)) {
            updateSubtotal();
            updateLastItem();
        }
    }

    private void updateLastItem() {
        OrderItem lastItem = cartManager.getLastItem();
        if (lastItem == null) {
            viewHolder.lastItemContainer.setVisibility(View.GONE);
        } else {
            viewHolder.lastItemNameTv.setText(lastItem.getName());
            viewHolder.lastItemNumTv.setText(lastItem.getBarcode());
            Money oldPrice = lastItem.getOldPrice();
            Money price = lastItem.getPrice();
            viewHolder.lastItemPriceTv.setText(getString(R.string.item_cart_price, price.toString()));
            viewHolder.disCountPrice.setText("实收: " + lastItem.getSaleAmount().getYuan());
            if (oldPrice.compareTo(price) > 0) {
                // 如果折扣价小于原价, 显示折扣信息
                viewHolder.lastItemOldPriceTv.setText(oldPrice.toString());
                viewHolder.lastItemOldPriceTv.getPaint().setStrikeThruText(true);
                viewHolder.lastItemOldPriceTv.setVisibility(View.VISIBLE);
            } else {
                viewHolder.lastItemOldPriceTv.setVisibility(View.GONE);
                viewHolder.lastItemOldPriceTv.getPaint().setStrikeThruText(false);
            }
            Money discount = lastItem.getDiscountTotal();
            viewHolder.lastItemDiscountTv.setText(getString(R.string.item_cart_discount, discount.toString()));
            viewHolder.lastItemQuantityTv.setText(getString(R.string.item_cart_quantity, lastItem.getQuantity()));
            viewHolder.lastItemContainer.setVisibility(View.VISIBLE);
        }
    }

    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(viewHolder.priceTv.getWindowToken(), 0);
    }

    private void updateSubtotal() {
//        Money subtotal = cartManager.getSubTotal();
        Money subtotal = cartManager.getAdjustedTotal();
        viewHolder.subTotalAndChargeBtn.setText(
                String.format(getString(R.string.subtotal_and_charge_button),
                        subtotal.toString()));
    }

    private void updateVip() {
        if (cartManager.hasVip()) {
            Member member = cartManager.currentVip();
            viewHolder.vipContainerView.setVisibility(View.VISIBLE);
            viewHolder.vipNoTv.setText(member.getVipNo());
        } else {
            viewHolder.vipContainerView.setVisibility(View.GONE);
        }
    }

    private void reduceQuantity() {
        int currentQuantity = 0;
        int length = viewHolder.quantityEt.getText().length();
        if (length != 0) {
            currentQuantity = Integer.valueOf(viewHolder.quantityEt.getText().toString());
        }
        if (currentQuantity > 1) {
            viewHolder.reduceQuantityIv.setEnabled(true);
            String quantityStr = String.valueOf(currentQuantity - 1);
            viewHolder.quantityEt.setText(quantityStr);
            viewHolder.quantityEt.setSelection(quantityStr.length());
        } else {
            viewHolder.reduceQuantityIv.setEnabled(false);
        }
        if (!viewHolder.addQuantityIv.isEnabled()) {
            viewHolder.addQuantityIv.setEnabled(true);
        }
    }

    private void addQuantity() {
        int newQuantity;
        int length = viewHolder.quantityEt.getText().length();
        if (length == 0) {
            newQuantity = 1;
        } else {
            newQuantity = Integer.valueOf(viewHolder.quantityEt.getText().toString()) + 1;
        }
        if (newQuantity >= 9999) {
            viewHolder.addQuantityIv.setEnabled(false);
        } else {
            viewHolder.addQuantityIv.setEnabled(true);
        }
        if (!viewHolder.reduceQuantityIv.isEnabled()) {
            viewHolder.reduceQuantityIv.setEnabled(true);
        }
        String quantityStr = String.valueOf(newQuantity);
        viewHolder.quantityEt.setText(quantityStr);
        viewHolder.quantityEt.setSelection(quantityStr.length());
    }

    private boolean commitKeypadItem() {
        if (!hasSelectedGoods()) {
            showMessage(R.string.error_home_select_goods_null);
            return false;
        }
        String price = viewHolder.priceTv.getText().toString();
        String discountPrices = viewHolder.discountPrice.getText().toString();
        if (!TextUtils.isEmpty(discountPrices)) {
            if (Strings.isBlank(discountPrices)
                    || ".".equals(discountPrices)
                    || "0".equals(discountPrices)) {
                showMessage("无效的价格!");
                return false;
            }
            if (price.endsWith(".")) {
                price = price.substring(0, price.length() - 1);
            }
        }
        if (Strings.isBlank(price)
                || ".".equals(price)
                || "0".equals(price)) {
            showMessage("无效的价格!");
            return false;
        }
        if (price.endsWith(".")) {
            price = price.substring(0, price.length() - 1);
        }
        long priceCent;
//        if(TextUtils.isEmpty(discountPrices)){
        priceCent = new BigDecimal(price).movePointRight(2).longValue();
//        }else{
//            priceCent = new BigDecimal(discountPrices).movePointRight(2).longValue();
//        }
        if (TextUtils.isEmpty(discountPrices)) {
            discountPrices = "0";
        }
        long discountPricesm = new BigDecimal(discountPrices).movePointRight(2).longValue();
        if(priceCent < discountPricesm){
            Toast.makeText(getActivity(),"折扣价金额不能大于原价",Toast.LENGTH_LONG).show();
            return false;
        }
        if (priceCent == 0) {
            showMessage("商品价格不能为0!");
            return false;
        }
        String quantity = viewHolder.quantityEt.getText().toString();
        if (Strings.isBlank(quantity)) {
            showMessage("无效的商品数量!");
            return false;
        }
        cartManager.setKeypadQuantity(Integer.valueOf(quantity));
        cartManager.setKeypadhandDiscountInput(discountPricesm);
        cartManager.setKeypadhandDiscount(priceCent - discountPricesm);
        cartManager.setKeypadPrice(priceCent);
        showProgress(ID_DIALOG_CALCULATE_ITEM, "获取折扣信息...");
        presenter.calculateKaypadItem();
        return true;
    }

    private void resetKeypad() {
        viewHolder.addBarcodeTv.setText(null);
        viewHolder.priceTv.setText(null);
        viewHolder.quantityEt.setText("1");
        viewHolder.quantityEt.setSelection(1);
        viewHolder.discountPrice.setText(null);
        cartManager.setKeypadPrice(0);

    }

    private boolean hasSelectedGoods() {
        return viewHolder.addBarcodeTv.getText().length() != 0;
    }

    private void selectGoods(Goods goods) {
        viewHolder.addBarcodeTv.setText(goods.getBarcode());
        cartManager.setKeypadGoods(goods);
    }

    private void showClearCartDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
        builder.title(R.string.home_empty_cart_dialog_title);
        builder.content(R.string.home_empty_cart_dialog_content);
        builder.negativeText(R.string.home_empty_cart_dialog_cancel);
        builder.positiveText(R.string.home_empty_cart_dialog_ok);
        builder.onAny(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                switch (which) {
                    case POSITIVE:
                        cartManager.clear();
                        break;
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }


    // endregion Inherited Methods

    // region Anonymous Classes

    private static class ViewHolder {
        View vipContainerView;
        TextView vipNoTv;
        ImageView deleteVipIv;

        EditText priceTv;
        TextView addBarcodeTv; // 选择条码
        ImageView reduceQuantityIv;
        ImageView addQuantityIv;
        EditText quantityEt;

        Button subTotalAndChargeBtn;
        Button addToCartBtn;
        Button clearBtn;
//        Padlock padlock;

        View lastItemContainer;
        TextView lastItemNameTv;
        TextView lastItemNumTv;         // 条码
        TextView lastItemQuantityTv;
        TextView lastItemPriceTv;
        TextView disCountPrice;//实收
        TextView lastItemOldPriceTv;
        TextView lastItemDiscountTv;
        TextView discountPrice;
        TextView goodsInfo;//手工折扣价

        public ViewHolder(View view) {
            vipContainerView = view.findViewById(R.id.fragment_home_vip_container);
            vipNoTv = view.findViewById(R.id.fragment_home_vip_no);
            deleteVipIv = view.findViewById(R.id.fragment_home_vip_delete);

//            padlock = view.findViewById(R.id.keypad_panel_padview);
            priceTv = view.findViewById(R.id.keypad_panel_price);
            addBarcodeTv = view.findViewById(R.id.keypad_panel_add_barcode);
            reduceQuantityIv = view.findViewById(R.id.fragment_cart_item_quantity_reduce);
            addQuantityIv = view.findViewById(R.id.fragment_cart_item_quantity_add);
            quantityEt = view.findViewById(R.id.fragment_cart_item_quantity);
//            quantityEt.setFocusable(false);
//            quantityEt.setEnabled(false);
            subTotalAndChargeBtn = view.findViewById(R.id.fragment_home_charge_button);
            addToCartBtn = view.findViewById(R.id.fragment_home_add_item);
            clearBtn = view.findViewById(R.id.fragment_home_clear_button);
            goodsInfo = view.findViewById(R.id.goods_infos);

            lastItemContainer = view.findViewById(R.id.view_cart_item_container);
            lastItemNameTv = view.findViewById(R.id.view_cart_item_name);
            lastItemNumTv = view.findViewById(R.id.view_cart_item_number);
            lastItemQuantityTv = view.findViewById(R.id.view_cart_item_quantity);
            lastItemPriceTv = view.findViewById(R.id.view_cart_item_price);
            disCountPrice = view.findViewById(R.id.view_cart_item_price_momney);
            lastItemOldPriceTv = view.findViewById(R.id.view_cart_item_oldprice);
            lastItemDiscountTv = view.findViewById(R.id.view_cart_item_discount);
            discountPrice = view.findViewById(R.id.keypad_panel_discount_price);

        }
    }

    // endregion Anonymous Classes
}
