package com.tianhe.pay.ui.cart;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianhe.pay.R;
import com.tianhe.pay.data.order.OrderItem;
import com.tianhe.pay.utils.money.Money;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {
    Context context;
    List<OrderItem> list;

    public CartItemAdapter(Context context, List<OrderItem> list) {
        this.context = context.getApplicationContext();
        this.list = list;
    }

    public void setData(List<OrderItem> list) {
        this.list = list;
    }

    @Override
    public CartItemAdapter.CartItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartItemViewHolder holder, int position) {
        OrderItem item = list.get(position);
        holder.nameTv.setText(item.getName());
        holder.goodsNoTv.setText(item.getBarcode());
        Money price = item.getPrice();
        Money oldPrice = item.getOldPrice();
        holder.priceTv.setText(context.getString(R.string.item_cart_price, price.toString()));
        if (oldPrice.compareTo(price) > 0) {
            // 如果折扣价小于原价, 显示折扣信息
            holder.oldPriceTv.setText(oldPrice.toString());
            holder.oldPriceTv.getPaint().setStrikeThruText(true);
            holder.oldPriceTv.setVisibility(View.VISIBLE);
        } else {
            holder.oldPriceTv.setVisibility(View.GONE);
            holder.oldPriceTv.getPaint().setStrikeThruText(false);
        }
        Money discount = item.getDiscountTotal();
        holder.discountTv.setText(context.getString(R.string.item_cart_discount, discount.toString()));
        holder.quantityTv.setText(context.getString(R.string.item_cart_quantity, item.getQuantity()));
        holder.dicountPrice.setText("实收: "+item.getSaleAmount().getYuan());
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class CartItemViewHolder extends RecyclerView.ViewHolder {
        ImageView iconIv;
        TextView nameTv;
        TextView goodsNoTv;     // 商品条码
        TextView discountTv;    // 小记折扣
        TextView priceTv;       // 单价
        TextView oldPriceTv;       // 单价
        TextView quantityTv;    // 数量
        TextView dicountPrice;

        public CartItemViewHolder(View itemView) {
            super(itemView);
            iconIv = itemView.findViewById(R.id.view_cart_item_icon);
            nameTv = itemView.findViewById(R.id.view_cart_item_name);
            goodsNoTv = itemView.findViewById(R.id.view_cart_item_number);
            discountTv = itemView.findViewById(R.id.view_cart_item_discount);
            priceTv = itemView.findViewById(R.id.view_cart_item_price);
            oldPriceTv = itemView.findViewById(R.id.view_cart_item_oldprice);
            quantityTv = itemView.findViewById(R.id.view_cart_item_quantity);
            dicountPrice = itemView.findViewById(R.id.view_cart_item_price_momney);
        }
    }
}
