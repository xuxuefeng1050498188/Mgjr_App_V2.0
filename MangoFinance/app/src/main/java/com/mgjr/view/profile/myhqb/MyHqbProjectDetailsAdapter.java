package com.mgjr.view.profile.myhqb;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.model.bean.MyHqbProjectDetailsBean;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.mgjr.R.id.tv_item_action;
import static com.mgjr.R.id.tv_item_count;
import static com.mgjr.R.id.tv_item_time;

/**
 * Created by Administrator on 2016/9/30.
 */

public class MyHqbProjectDetailsAdapter extends BaseAdapter {
    private Context context;
    List<MyHqbProjectDetailsBean.TransactionListBean> transactionList;


    public MyHqbProjectDetailsAdapter(Context context, List<MyHqbProjectDetailsBean.TransactionListBean> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
    }

    @Override
    public int getCount() {
        return transactionList.size();
    }

    @Override
    public Object getItem(int position) {
        return transactionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.itemlayout_hqb_projectdetails_list, null);
            holder.tv_item_count = (TextView) convertView.findViewById(tv_item_count);
            holder.tv_item_action = (TextView) convertView.findViewById(tv_item_action);
            holder.tv_item_time = (TextView) convertView.findViewById(tv_item_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setListData(position, holder);
        return convertView;
    }

    private void setListData(int position, ViewHolder holder) {
        String type = transactionList.get(position).getType();
        String amount = transactionList.get(position).getAmount() + "";
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(transactionList.get(position).getTime());
        if (amount == null) {
            holder.tv_item_count.setText("0.0");
        } else {
            if (type.equalsIgnoreCase("赎回")) {
                holder.tv_item_count.setText("-" + amount);
                holder.tv_item_count.setTextColor(Color.parseColor("#22b1f8"));
            } else if (type.equalsIgnoreCase("转入")) {
                holder.tv_item_count.setText("+" + amount);
                holder.tv_item_count.setTextColor(Color.parseColor("#fdaa01"));
            }
        }

        if (time == null) {
            holder.tv_item_time.setText("");
        } else {
            holder.tv_item_time.setText(time);
        }

        if (type == null) {
            holder.tv_item_action.setText("");
        } else {
            holder.tv_item_action.setText(type);
        }
    }

    private class ViewHolder {

        TextView tv_item_time, tv_item_action, tv_item_count;
    }
}
