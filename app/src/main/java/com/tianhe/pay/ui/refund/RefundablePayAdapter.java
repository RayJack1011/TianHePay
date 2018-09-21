package com.tianhe.pay.ui.refund;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianhe.pay.R;
import com.tianhe.pay.model.PaymentSignpost;
import com.tianhe.pay.model.RefundablePay;
import com.tianhe.pay.utils.Res;
import com.tianhe.pay.utils.money.NumberFormats;

import java.text.NumberFormat;
import java.util.List;

public class RefundablePayAdapter extends RecyclerView.Adapter<RefundablePayAdapter.ViewHolder> {

//    private List<PaymentSignpost> list;
    private List<RefundablePay> list;
    private Context context;

    public RefundablePayAdapter(List<RefundablePay> list, Context context) {
        this.list = list;
        this.context = context.getApplicationContext();
//        format = NumberFormats.getInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_refund_paid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        PaymentSignpost signpost = list.get(position);
        RefundablePay refundable = list.get(position);
        holder.iconIv.setImageResource(refundable.getSignpost().getIcon());
        holder.nameAndAmountTv.setText(refundable.getPaymentName() + "退款");
        holder.itemView.setTag(refundable);
//        if (Boolean.TRUE == refundEnable) {
//            holder.refundUnableIv.setVisibility(View.VISIBLE);
//            holder.refundEnableIb.setVisibility(View.GONE);
//        } else {
//            holder.refundUnableIv.setVisibility(View.GONE);
//            holder.refundEnableIb.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconIv;
        TextView nameAndAmountTv;
        ImageButton refundEnableIb;
        ImageView refundUnableIv;

        public ViewHolder(View itemView) {
            super(itemView);
            iconIv = itemView.findViewById(R.id.item_refund_paid_icon);
            nameAndAmountTv = itemView.findViewById(R.id.item_refund_paid_name_and_amount);
            refundEnableIb = itemView.findViewById(R.id.item_refund_paid_enable);
            refundUnableIv = itemView.findViewById(R.id.item_refund_paid_unable);
        }
    }
}
