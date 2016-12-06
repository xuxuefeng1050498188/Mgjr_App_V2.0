package com.mgjr.view.profile.myJmg;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgjr.R;

import java.util.List;

/**
 * Created by Administrator on 2016/8/18.
 */
public class JmgRepaymentPlanListAdapter extends BaseAdapter {

    private Context context;

    private List<View> itemList;

    public JmgRepaymentPlanListAdapter(Context context, List<View> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.itemlayout_jmg_projectdetails_list, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.tv_jmg_projectdetails_listitem_time.setText("2016-8-11");
        return convertView;
    }

    class ViewHolder {
        ImageView icon_jmg_projectdetails_listitem_repayment_status;
        TextView tv_jmg_projectdetails_listitem_time,tv_jmg_projectdetails_listitem_projecttype,tv_jmg_projectdetails_listitem_incometype,tv_jmg_projectdetails_listitem_income;

        public ViewHolder(View view) {
            icon_jmg_projectdetails_listitem_repayment_status = (ImageView) view.findViewById(R.id.icon_jmg_projectdetails_listitem_repayment_status);
            tv_jmg_projectdetails_listitem_time = (TextView) view.findViewById(R.id.tv_jmg_projectdetails_listitem_time);
            tv_jmg_projectdetails_listitem_projecttype = (TextView) view.findViewById(R.id.tv_jmg_projectdetails_listitem_projecttype);
            tv_jmg_projectdetails_listitem_incometype = (TextView) view.findViewById(R.id.tv_jmg_projectdetails_listitem_incometype);
            tv_jmg_projectdetails_listitem_income = (TextView) view.findViewById(R.id.tv_jmg_projectdetails_listitem_income);
            view.setTag(this);
        }
    }
}
