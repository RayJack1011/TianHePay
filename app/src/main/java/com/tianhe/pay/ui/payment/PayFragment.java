package com.tianhe.pay.ui.payment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tianhe.pay.R;
import com.tianhe.pay.data.DataSource;
import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.data.payment.Payment;
import com.tianhe.pay.model.CartManager;
import com.tianhe.pay.ui.MoneyKeypadListener;
import com.tianhe.pay.ui.TianHeFragment;
import com.tianhe.pay.ui.setting.Settings;
import com.tianhe.pay.utils.Strings;
import com.tianhe.pay.utils.Times;
import com.tianhe.pay.utils.money.Money;
import com.tianhe.pay.widget.padlock.Padlock;

import java.text.NumberFormat;

import javax.inject.Inject;

public abstract class PayFragment extends TianHeFragment {

    private static final String KEY_PAYMENT = "payment";
    private static final String KEY_WILL_PAY_AMOUNT = "willPayAmount";
    private static final String KEY_PENDING_AMOUNT = "pendingAmount";

    @Inject
    protected CartManager cartManager;
    @Inject
    protected Settings settings;
    protected ViewHolder viewHolder;

    /** 准备支付金额 */
    protected long willPayCent = 0L;
    /** 订单当前未付总额. */
    protected Money pendingAmount;
    protected NumberFormat amountFormat;
    protected Payment payment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pay, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewHolder = new ViewHolder(view);
        if (savedInstanceState == null) {
            pendingAmount = cartManager.getPendingAmount();
        } else {
            pendingAmount = new Money(getArguments().getLong(KEY_PENDING_AMOUNT));
        }
        amountFormat = NumberFormat.getInstance();
        initView();
    }

    private void initView() {
        setTitle();

        willPayCent = getArguments().getLong(KEY_WILL_PAY_AMOUNT, 0);
        viewHolder.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPayClick();
            }
        });
        viewHolder.pendingPayTv.setText(pendingAmount.toString());
//        viewHolder.willPayTv.setText(amountFormat.format((willPayCent / 100D)));
        updateWillPayCent(willPayCent);
        initializeKeypad(viewHolder.keypad);
        if (isTraining()) {
            viewHolder.posView.setVisibility(View.GONE);
        } else if(needRelNo()) {
            viewHolder.posView.setVisibility(View.GONE);
        } else {
            viewHolder.posView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getArguments().putLong(KEY_WILL_PAY_AMOUNT, willPayCent);
        getArguments().putLong(KEY_PENDING_AMOUNT, pendingAmount.getAmount());
    }

    @Override
    public void onDestroy() {
        viewHolder = null;
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void bindPayment(Payment payment) {
        getArguments().putSerializable(KEY_PAYMENT, payment);
    }

    protected abstract void pay();

    protected String getPaymentName() {
        return getPayment().getPaymentName();
    }

    protected String getPaymentId() {
        return getPayment().getPaymentId();
    }

    protected boolean needRelNo() {
        return false;
    }

    /** 是否练习模式 */
    protected boolean isTraining() {
        return settings.isTraining();
    }

    protected Payment getPayment() {
        if (payment == null) {
            payment = (Payment) getArguments().getSerializable(KEY_PAYMENT);
        }
        return payment;
    }

    private void onPayClick() {
        if (getWillPayCent() == 0) {
            showMessage(R.string.error_pay_zero_amount);
            return;
        }
        if (isTraining()) {
            trainingPay();
        } else {
            pay();
        }
    }

    /**
     * 练习模式支付直接成功
     */
    private void trainingPay() {
        String relNo = null;
        if (needRelNo()) {
            relNo = viewHolder.relNoEt.getText().toString();
        }
        PaidInfo paidInfo = new PaidInfo();
        paidInfo.setPaymentId(getPaymentId());
        paidInfo.setSaleAmount(new Money(willPayCent));
        paidInfo.setBillNo(relNo);
        paidInfo.setPaymentName(getPaymentName());
        paidInfo.setTime(Times.nowDate());
        cartManager.addPaidInfo(paidInfo);
        getActivity().finish();
    }

    private void initializeKeypad(final Padlock padlock) {
        padlock.setOnKeyPressListener(new MoneyKeypadListener(padlock, pendingAmount.getAmount()) {
            @Override
            public void updateAmount(long newAmountCent) {
                if (newAmountCent != 0 || getWillPayCent() != 0) {
                    updateWillPayCent(newAmountCent);
                    updateKeyStates();
                }
            }

            @Override
            public long getAmount() {
                return getWillPayCent();
            }

            @Override
            public void onClearClicked() {
                if (getWillPayCent() > 0L) {
                    updateWillPayCent(0);
                    updateKeyStates();
                }
            }

            @Override
            public void onSubmitClicked() {
                // ignore
            }
        });
    }

    private void updateWillPayCent(long newAmountCent) {
        willPayCent = newAmountCent;
        String willPay = amountFormat.format(newAmountCent / 100D);
        viewHolder.willPayTv.setText(willPay);
    }

    private long getWillPayCent() {
        return willPayCent;
    }

    private void setTitle() {
        getActivity().setTitle(getPaymentName());
    }

    protected static class ViewHolder {
        public final TextView pendingPayTv;      // 待付金额
        public final TextView willPayTv;         // 当前支付
        public final Padlock keypad;
        public final Button okBtn;
        public final View posView;
        public final EditText relNoEt;

        public ViewHolder(View view) {
            this.pendingPayTv = view.findViewById(R.id.fragment_pay_uppay);
            this.willPayTv = view.findViewById(R.id.fragment_pay_willpay);
            this.keypad = view.findViewById(R.id.fragment_pay_input_keyboard);
            this.okBtn = view.findViewById(R.id.fragment_pay_ok_button);
            this.posView = view.findViewById(R.id.fragment_pay_relation_container);
            this.relNoEt = view.findViewById(R.id.fragment_pay_fillin_relationNo);
        }
    }
}
