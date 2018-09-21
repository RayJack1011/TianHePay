package com.tianhe.pay.ui.checkout;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianhe.pay.R;
import com.tianhe.pay.data.payment.Payment;
import com.tianhe.pay.model.PaymentSignpost;

import java.util.List;

public class UsablePaymentAdapter extends RecyclerView.Adapter<UsablePaymentAdapter.PaymentViewHolder> {
    private List<Payment> payments;

    public UsablePaymentAdapter(List<Payment> payments) {
        this.payments = payments;
    }

    @Override
    public PaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_payment, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PaymentViewHolder holder, int position) {
        Payment payment = payments.get(position);
        PaymentSignpost ps = PaymentSignpost.fromPaymentId(payment.getPaymentId());
        holder.iconIv.setImageResource(ps.getIcon());
        holder.nameTv.setText(payment.getPaymentName());
        holder.itemView.setTag(payment);
    }

    @Override
    public int getItemCount() {
        return (payments == null) ? 0 : payments.size();
    }

    static class PaymentViewHolder extends RecyclerView.ViewHolder {
        ImageView iconIv;
        TextView nameTv;

        public PaymentViewHolder(View itemView) {
            super(itemView);
            iconIv = itemView.findViewById(R.id.view_payment_icon);
            nameTv = itemView.findViewById(R.id.view_payment_name);
        }
    }
}
