package com.tianhe.pay.ui.order;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianhe.pay.R;
import com.tianhe.pay.data.order.OrderStatistics;
import com.tianhe.pay.common.BaseDialog;
import com.tianhe.pay.data.order.OrderStatisticsPayMerged;
import com.tianhe.pay.ui.TianHeFragment;
import com.tianhe.pay.utils.Strings;
import com.tianhe.pay.utils.Times;
import com.tianhe.pay.utils.money.Money;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class OrderCountFragment extends TianHeFragment
        implements OrderCountContract.View {

    private static final int ID_DIALOG_LOADING = BaseDialog.getAutoId();
    public static final String ARGS_KEY_STATISTICS = "statistics";
    public static final int ID_DIALOG_PRINT = BaseDialog.getAutoId();

    private ViewHolder viewHolder;

    @Inject
    OrderCountContract.Presenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_today_count, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewHolder = new ViewHolder(view);
        viewHolder.dateBtn.setText(Times.yyyy_MM_dd(new Date()));
        viewHolder.dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDate();
            }
        });

        viewHolder.dateImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDate();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        load();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.print, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.print) {
            printStatistics();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void printStatistics() {
        OrderStatistics statistics = getCurrentStatistics();
        if (isNotEmptyStatistics(statistics)) {
            showProgress(ID_DIALOG_PRINT, "正在打印统计数据...");
            presenter.print(statistics);
        } else {
            showMessage("没有报表数据");
        }
    }

    @Override
    public void printStatisticsSuccess() {
        dismissDialog(ID_DIALOG_PRINT);
    }

    @Override
    public void printStatisticsFail(String reason) {
        dismissDialog(ID_DIALOG_PRINT);
        if (Strings.isBlank(reason)) {
            showMessage("打印失败!");
        } else {
            showMessage("打印失败: " + reason);
        }
    }

    private boolean isNotEmptyStatistics(OrderStatistics statistics) {
        return statistics != null
                && (statistics.getRefundCount() != 0 || statistics.getSaleCount() != 0);
    }

    private OrderStatistics getCurrentStatistics() {
        return (OrderStatistics) getArguments().getSerializable(ARGS_KEY_STATISTICS);
    }

    @Override
    protected OrderCountContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void renderStatistic(OrderStatistics statistics) {
        statistics.setCountDate(viewHolder.dateBtn.getText().toString());
        dismissDialog(ID_DIALOG_LOADING);
        setCurrentStatistics(statistics);
        viewHolder.saleCountTv.setText(statistics.getSaleCount() + "单");
        viewHolder.saleTotalTv.setText("¥" + statistics.getSaleTotals().toString());
        viewHolder.saleTotalTv.setText("¥" + statistics.getSaleTotals().toString());
        viewHolder.refundCountTv.setText(statistics.getRefundCount() + "单");
        viewHolder.refundTotalTv.setText("¥" + statistics.getRefundTotal().negate().toString());
        viewHolder.statisticsTotalTv.setText("¥" + statistics.getStatisticsTotal().toString());
        List<OrderStatisticsPayMerged> list = statistics.mergePayById();
        viewHolder.paymentsView.removeAllViews();
        if (list.size() == 0) {
            return;
        }
        for (OrderStatisticsPayMerged osp : list) {
            View childView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_today_count_pay, viewHolder.paymentsView, false);
            TextView nameTv = childView.findViewById(R.id.item_today_count_pay_name);
            TextView payTotalsTv = childView.findViewById(R.id.item_today_count_pay_total);
            TextView refundTotalsTv = childView.findViewById(R.id.item_today_count_refund_total);
            TextView payCountTv = childView.findViewById(R.id.item_today_count_pay_count);//payTotalsTv-refundTotalsTv

            nameTv.setText(osp.getPaymentName());
            payTotalsTv.setText("¥" + osp.getSaleTotals().toString());
            refundTotalsTv.setText("¥" + osp.getRefundTotals().negate().toString());
            //计算小计，买单和退货相减,hujie
            long paycount= osp.getSaleTotals().getAmount()-osp.getRefundTotals().getAmount();
            payCountTv.setText("¥" + new Money(paycount).toString());
            viewHolder.paymentsView.addView(childView);
        }
    }

    private void setCurrentStatistics(OrderStatistics statistics) {
        getArguments().putSerializable(ARGS_KEY_STATISTICS, statistics);
    }

    @Override
    public void queryStatisticFail(String reason) {
        dismissDialog(ID_DIALOG_LOADING);
        showMessage("查询失败!");
    }

    private void load() {
        showProgress(ID_DIALOG_LOADING, "正在统计数据");
        presenter.queryStatistic(getCountDate());
    }

    private void showSelectDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getCountDate());

        DatePickerDialog datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                updateCountDate(year, month, dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    @NonNull
    private Date getCountDate() {
        String date = viewHolder.dateBtn.getText().toString();
        return Times.yyyy_MM_ddParse(date);
    }

    private void updateCountDate(int year, int month, int dayOfMonth) {
        Date date = Times.asDate(year, month, dayOfMonth);
        String dateStr = Times.yyyy_MM_dd(date);
        viewHolder.dateBtn.setText(dateStr);
        load();
    }

    private static class ViewHolder {
        public TextView saleCountTv;
        public TextView saleTotalTv;
        public TextView refundCountTv;
        public TextView refundTotalTv;
        public TextView statisticsTotalTv;
        public LinearLayout paymentsView;
        Button dateBtn;
        ImageView dateImg;

        public ViewHolder(View view) {
            saleCountTv = view.findViewById(R.id.fragment_order_today_sale_count);
            saleTotalTv = view.findViewById(R.id.fragment_order_today_sale_total);
            refundCountTv = view.findViewById(R.id.fragment_order_today_refund_count);
            refundTotalTv = view.findViewById(R.id.fragment_order_today_refund_totals);
            statisticsTotalTv = view.findViewById(R.id.fragment_order_today_totals);
            paymentsView = view.findViewById(R.id.layout_order_statistics_payments);

            dateBtn = view.findViewById(R.id.fragment_order_count_date);
            dateImg = view.findViewById(R.id.fragment_order_count_date1);
        }
    }
}
