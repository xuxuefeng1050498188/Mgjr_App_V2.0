package com.mgjr.view.profile.myJmg;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.model.bean.MyJmgFinishedProjectsBean;
import com.mgjr.model.bean.MyJmgInvestingProjectsBean;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/8/16.
 */
public class JmgFinishedProjectsListAdapter extends BaseAdapter {

    private List<MyJmgFinishedProjectsBean.OverTenderListBean> overTenderList;

    public JmgFinishedProjectsListAdapter(List<MyJmgFinishedProjectsBean.OverTenderListBean> overTenderList) {
        this.overTenderList = overTenderList;
    }

    @Override
    public int getCount() {
        return overTenderList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.itemlayout_jmg_finished_project_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyJmgFinishedProjectsBean.OverTenderListBean overTenderBean = overTenderList.get(position);
        holder.tvInvestingprojectProjectname.setText(overTenderBean.getLoanTitle());
        holder.tvInvestingprojectAccount.setText("" + new DecimalFormat("###,###,##0.00").format(overTenderBean.getAmount()));
        holder.tvInvestingprojectRate.setText("" + (overTenderBean.getRate() + overTenderBean.getZy()) + "%");
        holder.tvInvestingprojectTime.setText("" + overTenderBean.getPeriod() + "个月");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        holder.tvProjectStartTime.setText("" + sdf.format(overTenderBean.getIntefeeStartTime()));
        holder.tvProjectFinishedTime.setText("" + sdf.format(overTenderBean.getExpireTime()));
        return convertView;
    }


    static class ViewHolder {
        @InjectView(R.id.tv_investingproject_projectname)
        TextView tvInvestingprojectProjectname;
        @InjectView(R.id.tv_investingproject_rate)
        TextView tvInvestingprojectRate;
        @InjectView(R.id.tv_investingproject_time)
        TextView tvInvestingprojectTime;
        @InjectView(R.id.tv_investingproject_account)
        TextView tvInvestingprojectAccount;
        @InjectView(R.id.tv_project_start_time)
        TextView tvProjectStartTime;
        @InjectView(R.id.tv_project_finished_time)
        TextView tvProjectFinishedTime;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}

