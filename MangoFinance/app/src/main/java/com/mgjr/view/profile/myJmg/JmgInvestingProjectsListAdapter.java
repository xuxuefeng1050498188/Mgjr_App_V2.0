package com.mgjr.view.profile.myJmg;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.model.bean.MyJmgInvestingProjectsBean;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/8/16.
 */
public class JmgInvestingProjectsListAdapter extends BaseAdapter {

    private List<MyJmgInvestingProjectsBean.JmgTenderBean> tenderList;

    public JmgInvestingProjectsListAdapter(List<MyJmgInvestingProjectsBean.JmgTenderBean> tenderList) {
        this.tenderList = tenderList;
    }

    @Override
    public int getCount() {
        return tenderList.size();
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.itemlayout_jmg_investingproject_list, null);
            holder = new ViewHolder(convertView);
            holder.iv_month_bg = (ImageView) convertView.findViewById(R.id.iv_month_bg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyJmgInvestingProjectsBean.JmgTenderBean jmgTenderBean = tenderList.get(position);
        holder.tvInvestingprojectProjectname.setText(jmgTenderBean.getLoanTitle());
        holder.tvInvestingprojectAccount.setText("" + new DecimalFormat("###,###,##0.00").format(jmgTenderBean.getAmount()));
        holder.tvInvestingprojectRate.setText("" + jmgTenderBean.getRate() + "%");
        holder.tvInvestingprojectTime.setText("" + jmgTenderBean.getPeriod() + "个月");
        holder.tv_my_jmg_investing_message.setText(jmgTenderBean.getMessage());
        //进度条
        int repayCount = jmgTenderBean.getRepayCount();

        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        holder.iv_month_bg.measure(width, height);
        int measuredWidth = holder.iv_month_bg.getMeasuredWidth();
        int period = jmgTenderBean.getPeriod();
        if (period == 1) {
            holder.iv_month_bg.setBackgroundResource(R.drawable.bg_progress_1);
        } else if (period == 3) {
            holder.iv_month_bg.setBackgroundResource(R.drawable.bg_progress_3);
        } else if (period == 6) {
            holder.iv_month_bg.setBackgroundResource(R.drawable.bg_progress_6);
        } else if (period == 12) {
            holder.iv_month_bg.setBackgroundResource(R.drawable.bg_progress_12);
        }
        int progress = measuredWidth * repayCount / period;
        ;
        if (repayCount == period) {
            holder.iv_repayed_month.setBackgroundResource(R.drawable.qrepayed_month_bg);
        } else {
            holder.iv_repayed_month.getLayoutParams();
            FrameLayout.LayoutParams progressParams = (FrameLayout.LayoutParams) holder.iv_repayed_month.getLayoutParams();
            progressParams.width = progress;
            holder.iv_repayed_month.setLayoutParams(progressParams);
        }
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
        @InjectView(R.id.tv_my_jmg_investing_message)
        TextView tv_my_jmg_investing_message;
        @InjectView(R.id.iv_repayed_month)
        ImageView iv_repayed_month;
        ImageView iv_month_bg;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
