package com.tianhe.pay.ui.order;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tianhe.pay.R;
import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.data.payment.PaidInfo;

import java.text.DecimalFormat;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryHolder> {
    Context context;
    List<Order> list;

    public OrderHistoryAdapter(Context context, List<Order> list) {
        this.context = context.getApplicationContext();
        this.list = list;
    }

    public void setData(List<Order> list) {
        this.list = list;
    }

    @Override
    public OrderHistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_hostory, parent, false);
        return new OrderHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderHistoryHolder holder, int position) {
        Order order = list.get(position);
        long moneys = 0;
        for(PaidInfo paidInfo:order.getPaidInfos()){
            moneys += paidInfo.getSaleAmount().getAmount();
        }
        Log.e("qqq",moneys+"::::;jine");
         double moneyes = (double) (moneys/100.0);
        // #.00 表示两位小数 #.0000四位小数
        //方式三
        //%.2f %. 表示 小数点前任意位数   2 表示两位小数 格式后的结果为f 表示浮点型
        String str2 = String.format("%.2f", moneyes);
//        DecimalFormat df2 =new DecimalFormat("#.00");
//        String str2 =df2.format(moneyes);
        holder.saleNoTv.setText(context.getString(R.string.item_order_history_saleno,
                order.getOrderHeader().getSaleNo()));
        holder.timeTv.setText(context.getString(R.string.item_order_history_time,
                order.getOrderHeader().getTime()));
        if (order.isRefund()) {
            //如果是退货单，这里需要显示负号 hujie
            holder.totalTv.setText(context.getString(R.string.item_order_history_total,
                    "-"+str2+""));
        } else {
            holder.totalTv.setText(context.getString(R.string.item_order_history_total,
                    str2+""));
        }
        holder.discountTv.setText(context.getString(R.string.item_order_history_discount,
                order.getDiscountTotal().negate().toString()));
        holder.itemView.setTag(order);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class OrderHistoryHolder extends RecyclerView.ViewHolder {
        TextView saleNoTv;
        TextView timeTv;
        TextView totalTv;
        TextView discountTv;

        public OrderHistoryHolder(View itemView) {
            super(itemView);
            saleNoTv = itemView.findViewById(R.id.view_order_item_orderId);
            timeTv = itemView.findViewById(R.id.view_order_item_order_date);
            totalTv = itemView.findViewById(R.id.view_order_item_total);
            discountTv = itemView.findViewById(R.id.view_order_item_discount);
        }
    }
}
