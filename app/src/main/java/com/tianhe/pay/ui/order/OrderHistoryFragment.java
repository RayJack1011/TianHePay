package com.tianhe.pay.ui.order;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianhe.pay.R;
import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.common.BaseDialog;
import com.tianhe.pay.ui.TianHeFragment;
import com.tianhe.pay.ui.util.RecyclerItemClickListener;
import com.tianhe.pay.utils.Times;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class OrderHistoryFragment extends TianHeFragment
        implements OrderHistoryContract.View {

    private static final int ID_DIALOG_LOADING_HISTORY = BaseDialog.getAutoId();

    @Inject
    OrderHistoryContract.Presenter presenter;
    private ViewHolder viewHolder;
    OrderHistoryAdapter adapter;
    RecyclerItemClickListener itemClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_history, container, false);
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
        viewHolder.icDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDate();
            }
        });
    }

    private void showSelectDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getSelectedDate());

        DatePickerDialog datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                updateDate(year, month, dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    @NonNull
    private Date getSelectedDate() {
        String date = viewHolder.dateBtn.getText().toString();
        return Times.yyyy_MM_ddParse(date);
    }

    private void updateDate(int year, int month, int dayOfMonth) {
        Date date = Times.asDate(year, month, dayOfMonth);
        String dateStr = Times.yyyy_MM_dd(date);
        viewHolder.dateBtn.setText(dateStr);
        syncData();
    }

    @Override
    public void onResume() {
        super.onResume();
        syncData();
//        loadData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.refresh:
                syncData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void syncData() {
        showProgress(ID_DIALOG_LOADING_HISTORY, "正在更新数据...");
        Date syncDate = getSelectedDate();
        presenter.syncHistory(syncDate);
    }

    private void loadData() {
        showProgress(ID_DIALOG_LOADING_HISTORY, "正在查询...");
        presenter.queryHistory();
    }

    @Override
    protected OrderHistoryContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void renderHistory(List<Order> orders) {
        dismissDialog(ID_DIALOG_LOADING_HISTORY);
        if (orders == null || orders.size() == 0) {
            viewHolder.emptyView.setVisibility(View.VISIBLE);
            viewHolder.orderRv.setVisibility(View.GONE);
            return;
        }
        Log.e("qqq", "当日交易列表数据----->" + new Gson().toJson(orders));
        viewHolder.emptyView.setVisibility(View.GONE);
        viewHolder.orderRv.setVisibility(View.VISIBLE);
        if (adapter == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(OrientationHelper.VERTICAL);
            viewHolder.orderRv.setLayoutManager(layoutManager);
            registerPaymentItemClickListener(viewHolder.orderRv);
            adapter = new OrderHistoryAdapter(getContext(), orders);
            viewHolder.orderRv.setAdapter(adapter);
        } else {
            adapter.setData(orders);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void queryHistoryFail(String reason) {
        dismissDialog(ID_DIALOG_LOADING_HISTORY);
    }

    @Override
    public void syncHistorySuccess() {
        dismissDialog(ID_DIALOG_LOADING_HISTORY);
        // TODO
    }

    @Override
    public void syncHistoryFail(String reason) {
        dismissDialog(ID_DIALOG_LOADING_HISTORY);
        if (reason == null) {
            showMessage("更新数据失败");
        } else {
            showMessage("更新数据失败: " + reason);
        }
    }

    private void registerPaymentItemClickListener(RecyclerView recyclerView) {
        if (itemClickListener == null) {
            itemClickListener = new RecyclerItemClickListener(recyclerView);
        }
        itemClickListener.setItemListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {

                Order order = (Order) view.getTag();
                entryDetail(order);
            }
        });
        recyclerView.addOnItemTouchListener(itemClickListener);
    }

    private void entryDetail(Order order) {
        nav.enterOrderHistoryDetail(this, order);
    }

    private static class ViewHolder {
        Button dateBtn;
        View emptyView;
        RecyclerView orderRv;
        ImageButton searchIb;
        ImageView icDate;

        public ViewHolder(View view) {
            dateBtn = view.findViewById(R.id.fragment_order_history_date);
            emptyView = view.findViewById(R.id.order_history_empty_container);
            orderRv = view.findViewById(R.id.fragment_order_history_list);
            searchIb = view.findViewById(R.id.fragment_order_history_query_button);
            icDate = view.findViewById(R.id.fragment_order_history_date1);
        }
    }
}
