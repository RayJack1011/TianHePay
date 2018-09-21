package com.tianhe.pay.ui.cart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tianhe.pay.R;
import com.tianhe.pay.data.order.OrderItem;
import com.tianhe.pay.common.SavablePresenter;
import com.tianhe.pay.model.CartManager;
import com.tianhe.pay.ui.TianHeFragment;
import com.tianhe.pay.ui.util.RecyclerItemClickListener;

import java.util.List;

import javax.inject.Inject;

public class CartFragment extends TianHeFragment {
    @Inject
    CartManager cartManager;
    ViewHolder viewHolder;
    RecyclerItemClickListener itemClickListener;
    CartItemAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewHolder = new ViewHolder(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        renderCart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected SavablePresenter getPresenter() {
        return null;
    }

    public void renderCart() {
        if (!cartManager.isEmpty()) {
            viewHolder.emptyView.setVisibility(View.GONE);
            viewHolder.itemsRv.setVisibility(View.VISIBLE);
        } else {
            viewHolder.emptyView.setVisibility(View.VISIBLE);
            viewHolder.itemsRv.setVisibility(View.GONE);
        }
        List<OrderItem> items = cartManager.getCartItems();
        if (adapter == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(OrientationHelper.VERTICAL);
            viewHolder.itemsRv.setLayoutManager(layoutManager);
            adapter = new CartItemAdapter(getContext(), items);
            registerPaymentItemClickListener(viewHolder.itemsRv);
        } else {
            adapter.setData(items);
        }
        viewHolder.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entryCheckout();
            }
        });

        viewHolder.cleerAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClearCartDialog();
            }
        });
        viewHolder.itemsRv.setAdapter(adapter);
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
                        adapter.notifyDataSetChanged();
                        break;
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void entryCheckout() {
        if (cartManager.isEmpty()) {
            showMessage(R.string.error_cart_empty);
            return;
        }
        nav.enterCheckout(this);
    }

    private void registerPaymentItemClickListener(RecyclerView recyclerView) {
        if (itemClickListener == null) {
            itemClickListener = new RecyclerItemClickListener(recyclerView);
        }
        itemClickListener.setItemListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                enterCartItemDetail(position);
            }
        });
        recyclerView.addOnItemTouchListener(itemClickListener);
    }

    private void enterCartItemDetail(int itemIndex) {
        nav.enterCartDetail(this, itemIndex);
    }

    private static class ViewHolder {
        View emptyView;
        RecyclerView itemsRv;
        TextView pay;
        TextView cleerAll;

        public ViewHolder(View view) {
            emptyView = view.findViewById(R.id.fragment_cart_empty_container);
            itemsRv = view.findViewById(R.id.fragment_cart_list);
            pay = view.findViewById(R.id.fragment_home_charge_buttons);
            cleerAll = view.findViewById(R.id.fragment_home_clear_button);
        }
    }
}
