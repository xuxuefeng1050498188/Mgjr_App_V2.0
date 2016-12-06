package com.mgjr.view.profile.myJmg;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.model.bean.MyJmgInvestDetailBean;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.mgjr.R.id.icon_jmg_projectdetails_listitem_repayment_status;

/**
 * Created by Administrator on 2016/8/16.
 */
public class JmgProjectsDetailListAdapter extends BaseAdapter {

    private List<MyJmgInvestDetailBean.RepaymentPlanBean> repaymentPlanList;

    public JmgProjectsDetailListAdapter(List<MyJmgInvestDetailBean.RepaymentPlanBean> repaymentPlanList) {
        this.repaymentPlanList = repaymentPlanList;
    }

    @Override
    public int getCount() {
        return repaymentPlanList.size();
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
            convertView = View.inflate(parent.getContext(), R.layout.itemlayout_jmg_projectdetails_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyJmgInvestDetailBean.RepaymentPlanBean jmgRepaymentBean = repaymentPlanList.get(position);
        holder.tvJmgProjectdetailsListitemIncome.setText("" + new DecimalFormat("###,###,##0.00").format(jmgRepaymentBean.getAmount()));
        holder.tvJmgProjectdetailsListitemIncometype.setText(jmgRepaymentBean.getType());

        String time = new SimpleDateFormat("yyyy-MM-dd").format(jmgRepaymentBean.getPreRepayTime());
        holder.tvJmgProjectdetailsListitemTime.setText(time + "(" + jmgRepaymentBean.getNo() + "/" + jmgRepaymentBean.getPeriod() + ")");

        /*待还、已还状态图片 0待还，1已还*/
        int status = repaymentPlanList.get(position).getStatus();
        if (status == 0) {
            holder.iconJmgProjectdetailsListitemRepaymentStatus.setBackgroundResource(R.drawable.icon_jmg_noreturn);
            convertView.setBackgroundColor(Color.WHITE);
        } else if (status == 1) {
            convertView.setBackgroundColor(Color.parseColor("#e5e5e5"));
            holder.iconJmgProjectdetailsListitemRepaymentStatus.setBackgroundResource(R.drawable.icon_jmg_returned);
        }



        return convertView;
    }


    static class ViewHolder {
        @InjectView(icon_jmg_projectdetails_listitem_repayment_status)
        ImageView iconJmgProjectdetailsListitemRepaymentStatus;
        @InjectView(R.id.tv_jmg_projectdetails_listitem_time)
        TextView tvJmgProjectdetailsListitemTime;
        @InjectView(R.id.tv_jmg_projectdetails_listitem_projecttype)
        TextView tvJmgProjectdetailsListitemProjecttype;
        @InjectView(R.id.tv_jmg_projectdetails_listitem_incometype)
        TextView tvJmgProjectdetailsListitemIncometype;
        @InjectView(R.id.tv_jmg_projectdetails_listitem_income)
        TextView tvJmgProjectdetailsListitemIncome;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
