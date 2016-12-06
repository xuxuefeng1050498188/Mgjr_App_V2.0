package com.mgjr.view.profile.mangoExperienceFinancing;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.model.bean.MyTyjBean;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2016/11/2.
 */

public class ExperienceInvestRecordAdapter extends BaseAdapter {
    private List<MyTyjBean.InvestmentRecordBean> investmentRecord;
    private Context context;
    private LayoutInflater inflater;

    public ExperienceInvestRecordAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) MainApplication.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setListData(List<MyTyjBean.InvestmentRecordBean> investmentRecord){
        this.investmentRecord = investmentRecord;
    }

    @Override
    public int getCount() {
        return investmentRecord.size();
    }

    @Override
    public Object getItem(int position) {
        return investmentRecord.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.itemlayout_mgtyj_list, null);
            holder.tv_incometype = (TextView) convertView.findViewById(R.id.tv_incometype);
            holder.tv_listitem_state = (TextView) convertView.findViewById(R.id.tv_listitem_state);
            holder.tv_listitem_income = (TextView) convertView.findViewById(R.id.tv_listitem_income);
            holder.tv_listitem_investaccount = (TextView) convertView.findViewById(R.id.tv_listitem_investaccount);
            holder.tv_listitem_investtime = (TextView) convertView.findViewById(R.id.tv_listitem_investtime);
            holder.tv_listitem_projectname = (TextView) convertView.findViewById(R.id.tv_listitem_projectname);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        bindData(holder, position);

        return convertView;
    }

    private void bindData(ViewHolder holder, int position) {

        String statuStr = null;
        String type = null;
        int status = investmentRecord.get(position).getStatus();
        if (status == 0) {
            statuStr = "还款中";
            type = "预期收益(元)";
            holder.tv_listitem_state.setTextColor(Color.parseColor("#999999"));
        } else if (status == 1) {
            statuStr = "已还款";
            type = "已获收益(元)";
            holder.tv_listitem_state.setTextColor(Color.parseColor("#666666"));
        }
        holder.tv_listitem_state.setText(statuStr);
        holder.tv_incometype.setText(type);
        holder.tv_listitem_projectname.setText(investmentRecord.get(position).getTitle());
        holder.tv_listitem_investtime.setText(new SimpleDateFormat("yyyy-MM-dd").format(investmentRecord.get(position).getCtime()));
        holder.tv_listitem_investaccount.setText(new DecimalFormat("###,###,##0.00").format(investmentRecord.get(position).getAmount()));
        holder.tv_listitem_income.setText(new DecimalFormat("###,###,##0.00").format(investmentRecord.get(position).getProfit()));
    }

    class ViewHolder {
        TextView tv_listitem_projectname;
        TextView tv_listitem_investtime;
        TextView tv_listitem_investaccount;
        TextView tv_listitem_income;
        TextView tv_incometype;
        TextView tv_listitem_state;

    }
}
