package com.tianhe.pay.ui.cart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianhe.pay.R;
import com.tianhe.pay.data.order.OrderItem;
import com.tianhe.pay.data.order.calculate.CalculationResult;
import com.tianhe.pay.common.BaseDialog;
import com.tianhe.pay.model.CartManager;
import com.tianhe.pay.ui.TianHeFragment;
import com.tianhe.pay.utils.money.Money;

import javax.inject.Inject;

public class CartDetailFragment extends TianHeFragment implements CartDetailContract.View {
    private static final String KEY_ITEM_INDEX = "cartItemIndex";
    private static final int ID_DIALOG_MODIFY = BaseDialog.getAutoId();

    public static CartDetailFragment newInstance(int itemIndex) {
        CartDetailFragment fragment = new CartDetailFragment();
        fragment.getArguments().putInt(KEY_ITEM_INDEX, itemIndex);
        return fragment;
    }

    @Inject
    CartManager cartManager;
    @Inject
    ModifyItemPresenter presenter;
    private ViewHolder viewHolder;
    private int itemIndex;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewHolder = new ViewHolder(view);
        init();
    }

    private void init() {
        OrderItem orderItem = getItem();
        String oriPrice = orderItem.getOldPrice().toString();
        String price = orderItem.getPrice().toString();

        viewHolder.oriPriceTv.setText("原单价 " + oriPrice);
        viewHolder.currentPriceTv.setText("现单价 " + price);
        viewHolder.priceEt.setHint(price);
        viewHolder.priceEt.setText(price);
        viewHolder.priceEt.setSelection(price.length());
        viewHolder.quantityEt.setText(String.valueOf(orderItem.getQuantity()));
        registerForContextMenu(viewHolder.quantityEt);
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
        viewHolder.removeItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem();
            }
        });
    }

    private OrderItem getItem() {
        itemIndex = getArguments().getInt(KEY_ITEM_INDEX, -1);
        if (itemIndex == -1) {
            throw new IllegalArgumentException("has not set index of the modify item ");
        }
        return cartManager.getOrderItemByIndex(itemIndex);
    }

    @Override
    public void onDestroy() {
        unregisterForContextMenu(viewHolder.quantityEt);
        viewHolder = null;
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cart_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.save_modify:
                modifyItem();
                return true;
            case android.R.id.home:
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.fragment_cart_item_quantity) {
            menu.setHeaderTitle("选择数量");
            for (int i = 0; i < 10; i++) {
                menu.add(0, i, i, String.valueOf(i + 1));
            }
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        viewHolder.quantityEt.setText(String.valueOf(id + 1));
        return true;
    }

    @Override
    protected CartDetailContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void calculatedModifyItem(CalculationResult result) {
        dismissDialog(ID_DIALOG_MODIFY);
        getActivity().finish();
    }

    @Override
    public void calculateFail(String reason) {
        dismissDialog(ID_DIALOG_MODIFY);
        showMessage(reason);
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

    private void modifyItem() {
        int currentQuantity = Integer.valueOf(viewHolder.quantityEt.getText().toString());
        if (currentQuantity == 0) {
            removeItem();
            return;
        }
        Money handPrice = Money.createAsYuan(viewHolder.priceEt.getText().toString());
        OrderItem item = getItem();
        Money price = item.getPrice();
        if (price.compareTo(handPrice) < 0) {
            showMessage("手工变价只能采用更低单价");
            return;
        }
        showProgress(ID_DIALOG_MODIFY, "更新折扣信息...");
        presenter.calculateItem(itemIndex, currentQuantity, handPrice);
    }

    private void removeItem() {
        cartManager.removeItem(itemIndex);
        getActivity().finish();
    }

    private static class ViewHolder {
        TextView oriPriceTv;
        TextView currentPriceTv;
        EditText priceEt;
        ImageView reduceQuantityIv;
        ImageView addQuantityIv;
        EditText quantityEt;
        Button removeItemBtn;

        public ViewHolder(View view) {
            oriPriceTv = view.findViewById(R.id.fragment_cart_item_price_ori);
            currentPriceTv = view.findViewById(R.id.fragment_cart_item_price_now);
            priceEt = view.findViewById(R.id.fragment_cart_item_price);
            reduceQuantityIv = view.findViewById(R.id.fragment_cart_item_quantity_reduce);
            addQuantityIv = view.findViewById(R.id.fragment_cart_item_quantity_add);
            quantityEt = view.findViewById(R.id.fragment_cart_item_quantity);
            removeItemBtn = view.findViewById(R.id.fragment_cart_item_remove);
        }
    }
}
