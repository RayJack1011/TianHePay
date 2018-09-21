package com.tianhe.pay.ui.refund;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tianhe.pay.CommomData;
import com.tianhe.pay.R;
import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.model.PaymentSignpost;
import com.tianhe.pay.model.RefundDataManager;
import com.tianhe.pay.model.RefundablePay;
import com.tianhe.pay.ui.TianHeFragment;
import com.tianhe.pay.ui.setting.Settings;
import com.tianhe.pay.utils.Strings;
import com.tianhe.pay.utils.Times;
import com.tianhe.pay.utils.money.Money;

import javax.inject.Inject;

/**
 * 退支付方式
 */
public abstract class RefundPayFragment extends TianHeFragment {
    @Inject
    protected RefundDataManager refundDataManager;
    @Inject
    protected Settings settings;
    protected ViewHolder viewHolder;
    public int type = 0;//0、当日撤销  1、隔日退货

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refund_paid, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewHolder = new ViewHolder(view);
        if(getPaymentId().equals("04")){//银行卡
            viewHolder.layouts.setVisibility(View.VISIBLE);
        }else{
            viewHolder.layouts.setVisibility(View.GONE);
        }

        viewHolder.todayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                viewHolder.todayImg.setBackgroundResource(R.drawable.choice);
                viewHolder.todayTxt.setTextColor(getResources().getColor(R.color.text_blue));
                viewHolder.yesterdayImg.setBackgroundResource(R.drawable.choice_un);
                viewHolder.yesterdayTxt.setTextColor(getResources().getColor(R.color.btn_dark));
            }
        });
        viewHolder.yesterdayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 2;
                viewHolder.yesterdayImg.setBackgroundResource(R.drawable.choice);
                viewHolder.yesterdayTxt.setTextColor(getResources().getColor(R.color.text_blue));
                viewHolder.todayImg.setBackgroundResource(R.drawable.choice_un);
                viewHolder.todayTxt.setTextColor(getResources().getColor(R.color.btn_dark));
            }
        });
        viewHolder.titleTv.setText(getPaymentName() + "退款");
        viewHolder.refundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryRefundPaid();
            }
        });
        if (needRelNo()) {
            viewHolder.relNoContainer.setVisibility(View.VISIBLE);
        } else {
            viewHolder.relNoContainer.setVisibility(View.GONE);
        }
        if (canModifyRefundAmount()) {
            // 可以修改退款金额时, 显示总的可退金额
            viewHolder.amountTv.setText(getString(R.string.RefundableTotalAmount,
                    refundDataManager.getRemainRefund().toString()));
            viewHolder.modifyAmountView.setVisibility(View.VISIBLE);
            viewHolder.amountTv.setVisibility(View.GONE);
            viewHolder.modifyAmountEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    checkAmountWhenInput(s.toString());
                }
            });
        } else {
            // 不可修改退款金额时, 显示当前支付方式可以退款的最大金额
//            viewHolder.amountTv.setText("");
            viewHolder.modifyAmountView.setVisibility(View.GONE);
            viewHolder.amountTv.setVisibility(View.VISIBLE);
        }
    }

    private PaymentSignpost getRefundablePaymentSignpost() {
        return (PaymentSignpost) getArguments().getSerializable("refundable");
    }

    protected boolean isTraining() {
        return settings.isTraining();
    }

    protected boolean needRelNo() {
        return true;
    }

    protected boolean canModifyRefundAmount() {
        return false;
    }

    protected abstract void refund();

    protected String getPaymentName() {
        return getRefundablePay().getPaymentName();
    }

    protected String getPaymentId() {
        return getRefundablePaymentSignpost().getPaymentId();
    }

    protected String getRelNo() {
        return viewHolder.relNoEt.getText().toString();
    }

    protected String getModifyAmount() {
        return viewHolder.modifyAmountEt.getText().toString();
    }

    private RefundablePay getRefundablePay() {
        return refundDataManager.findRefundable(getRefundablePaymentSignpost());
    }

    public void refundSuccess(PaidInfo paidInfo) {
        getActivity().finish();
    }

    public void refundFailed(String reason) {
        showMessage(reason);
    }

    private void tryRefundPaid() {
        if (needRelNo()) {
            if (Strings.isBlank(getRelNo())) {
                showMessage("请输入关联号码");
                return;
            }
        }
        if (canModifyRefundAmount()) {
            String modifyAmount = getModifyAmount();
            if (Strings.isBlank(modifyAmount) ||
                    Double.valueOf(modifyAmount) == 0) {
                showMessage("退款金额不能为0!");
                return;
            }
        }
        if(getPaymentId().equals("04")){//银行卡
            if(type == 0){
                Toast.makeText(getContext(),"请选择退款方式",Toast.LENGTH_LONG).show();
                return;
            }
        }
//        if (isTraining()) {
//            trainingRefund();
//            return;
//        PaidInfo refundPaid = new PaidInfo();
//        refundPaid.setPaymentId(getPaymentId());
//        Money amount;
//        if (canModifyRefundAmount()) {
//            amount = Money.createAsYuan(getModifyAmount());
//        } else {
//            amount = new Money(getRefundablePay().getRefundableTotal());
//        }
//        refundPaid.setSaleAmount(amount);
//        refundPaid.setPaymentName(getPaymentName());
//        refundPaid.setRelationNumber(getRelNo()); // 交易参考号
//        refundPaid.setTime(Times.nowDate());
//        refundDataManager.addRefundPaid(refundPaid);
        CommomData.money = viewHolder.amountTv.getText().toString();
        CommomData.tracHost = viewHolder.relNoEt.getText().toString();
        refund();
    }

    /**
     * 练习模式下, 退款直接成功
     */
    private void trainingRefund() {
        PaidInfo refundPaid = new PaidInfo();
        refundPaid.setPaymentId(getPaymentId());
        Money amount;
        if (canModifyRefundAmount()) {
            amount = Money.createAsYuan(getModifyAmount());
        } else {
            amount = new Money(getRefundablePay().getRefundableTotal());
        }
        refundPaid.setSaleAmount(amount);
        refundPaid.setPaymentName(getPaymentName());
        refundPaid.setBillNo(getRelNo()); // 交易参考号
        refundPaid.setTime(Times.nowDate());
        refundDataManager.addRefundPaid(refundPaid);
        refundSuccess(refundPaid);
    }

    private void checkAmountWhenInput(String input) {
        if (TextUtils.isEmpty(input)) {    // 输入过程中只做合理性验证.提交前才能做空值判读
            return;
        }
        if (input.length() == 1 & ".".equals(input)) {
            viewHolder.modifyAmountEt.setText("");
            return;
        }
        Money inputAmount = Money.createAsYuan(input);
        Money remain = refundDataManager.getRemainRefund();
        if (inputAmount.compareTo(remain) > 0) {
            String max = remain.toString();
            viewHolder.modifyAmountEt.setText(max);
            viewHolder.modifyAmountEt.setSelection(max.length());
            showMessage("退款金额已超限!");
            return;
        }
    }

    protected static class ViewHolder {
        public final TextView titleTv;
        public final EditText amountTv;
        public final View relNoContainer;
        public final EditText relNoEt;
        public final Button refundBtn;
        public final ImageButton scanIb;
        public final View modifyAmountView;
        public final EditText modifyAmountEt;

        public final LinearLayout layouts;
        public final LinearLayout todayLayout,yesterdayLayout;
        ImageView todayImg,yesterdayImg;
        TextView todayTxt,yesterdayTxt;



        public ViewHolder(View view) {
            titleTv = view.findViewById(R.id.fragment_refund_pay_title);
            amountTv = view.findViewById(R.id.fragment_refund_pay_amount);
            relNoContainer = view.findViewById(R.id.fragment_refund_rel_no_container);
            relNoEt = view.findViewById(R.id.fragment_refund_rel_No);
            refundBtn = view.findViewById(R.id.fragment_refund_query_button);
            scanIb = view.findViewById(R.id.fragment_refund_pay_scan);
            modifyAmountView = view.findViewById(R.id.fragment_refund_pay_amount_input_container);
            modifyAmountEt = view.findViewById(R.id.fragment_refund_pay_amount_input);
            layouts = view.findViewById(R.id.layouts);
            todayLayout = view.findViewById(R.id.today_layout);
            yesterdayLayout = view.findViewById(R.id.yesterday_layout);
            todayImg = view.findViewById(R.id.today_img);
            yesterdayImg = view.findViewById(R.id.yesterday_img);
            todayTxt = view.findViewById(R.id.today_txt);
            yesterdayTxt = view.findViewById(R.id.yesterday_txt);
        }
    }
}
