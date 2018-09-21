package com.tianhe.pay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianhe.pay.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangya3 on 2018/4/8.
 */

public class OrderListAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mlist;

    public OrderListAdapter(Context context, ArrayList<String> orderList) {
        this.mContext = context;
        this.mlist = orderList;

    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_order_list, null);
        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.orlderItem.setText(mlist.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView orlderItem;

        public ViewHolder(View view) {
            orlderItem = view.findViewById(R.id.order);
        }
    }
}
