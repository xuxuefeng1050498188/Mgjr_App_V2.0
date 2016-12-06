package com.mgjr.view.profile.myhqb;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.model.bean.MyHqbRunningProjectBean;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */

public class HqbRedeemedListAdapter extends BaseAdapter {
    List<MyHqbRunningProjectBean.TenderListBean> hqbTenderList;
    private ListView listView;
    private Context context;

    private Double incomeAmount;  //已获收益
    private Double rate;
    private String projectTitle;
    private long time;

    public HqbRedeemedListAdapter(List<MyHqbRunningProjectBean.TenderListBean> hqbTenderList, ListView listView, Context context) {
        this.hqbTenderList = hqbTenderList;
        this.listView = listView;
        this.context = context;
    }

    @Override
    public int getCount() {
        return hqbTenderList.size();
    }

    @Override
    public Object getItem(int position) {
        return hqbTenderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.itemlayout_hqb_redeemed_projectlist, null);
            new HqbRedeemedListAdapter.ViewHolder(convertView);
        }
        ViewHolder holder = (HqbRedeemedListAdapter.ViewHolder) convertView.getTag();
        setData(position, holder);

        setListItemClick();
        return convertView;
    }

    private void setListItemClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String code = hqbTenderList.get(position -1).getId() + "";
                MyActivityManager.getInstance().startNextActivity(MyHqbProjectDetailsActivity.class, code, "0","redeemed");
            }
        });
    }

    private void setData(int position, ViewHolder holder) {
        incomeAmount = hqbTenderList.get(position).getIncomeAmount();
        rate = hqbTenderList.get(position).getRedeemRate();
        projectTitle = hqbTenderList.get(position).getHqbTitle();
        time = hqbTenderList.get(position).getLastRedeemTime();
        if (incomeAmount != null) {
            holder.tv_balance.setText(incomeAmount + "");
        } else {
            holder.tv_balance.setText("0.0");
        }
        if (rate != null) {
            holder.tv_rate.setText(rate + "%");
        } else {
            holder.tv_rate.setText("0.0%");
        }
        if (time != 0) {
            holder.tv_time.setText(new SimpleDateFormat("yyyy-MM-dd").format(time));
        } else {
            holder.tv_time.setText("- -");
        }
        if (projectTitle != null) {
            holder.tv_project.setText(projectTitle);
        } else {
            holder.tv_project.setText("活期宝");
        }
    }

    class ViewHolder {
        ImageView iv_icon;
        TextView tv_balance, tv_rate, tv_project, tv_time, tv_action;

        public ViewHolder(View view) {
            iv_icon = (ImageView) view.findViewById(R.id.img_runningproject_icon);
            tv_balance = (TextView) view.findViewById(R.id.tv_redeemproject_incomeAmount);
            tv_rate = (TextView) view.findViewById(R.id.tv_redeemproject_rate);
            tv_project = (TextView) view.findViewById(R.id.tv_redeemproject_project);
            tv_time = (TextView) view.findViewById(R.id.tv_redeemproject_time);
            view.setTag(this);
        }
    }
}
