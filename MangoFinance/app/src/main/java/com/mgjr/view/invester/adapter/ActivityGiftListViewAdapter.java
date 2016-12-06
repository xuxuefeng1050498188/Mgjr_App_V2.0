package com.mgjr.view.invester.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.model.bean.ActivityGitBean;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by xuxuefeng on 2016/9/22.
 */
public class ActivityGiftListViewAdapter extends BaseAdapter {

    private final List<ActivityGitBean> activityGitList;
    private int activityGiftType;

    public ActivityGiftListViewAdapter(int activityGiftType, List<ActivityGitBean> activityGitList) {
        this.activityGiftType = activityGiftType;
        this.activityGitList = activityGitList;
    }

    @Override
    public int getCount() {
        return activityGitList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ActivityGitBean activityGitBean = activityGitList.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.layout_redpacket_bg, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置数据
        if (activityGitBean.isSelected()) {
            holder.ivSelectHb.setImageResource(R.drawable.selected_gift);
        } else {
            holder.ivSelectHb.setImageResource(R.drawable.unselected_gift);
        }
        /*if (!activityGitBean.isUseable()) {
//            holder.llUnuseableBg.setBackgroundColor(Color.parseColor("#FF000"));
        }*/
        if (activityGiftType == 1) {
            //绑定红包数据
            holder.tvActivityGiftName.setText(activityGitBean.getTitle());
            int amount = (int) activityGitBean.getAmount();
            holder.tvRedpacketsCanuseAccount.setText("" + amount + "元");
            long etime = activityGitBean.getEtime();
            String indate = new SimpleDateFormat("yyyy-MM-dd").format(etime);
            holder.tvRedpacketsCanusePeriod.setText("有效期至" + indate);
            holder.tvRedpacketsCanuseRemark.setText(activityGitBean.getRemark());
            holder.tvRedpacketsCanuseRemark.setVisibility(View.VISIBLE);
            if (!activityGitBean.isUseable()) {
                holder.llUnuseableBg.setAlpha(0.5f);
            } else {
                holder.llUnuseableBg.setAlpha(1f);
            }
        } else {
            //绑定加息券数据
            holder.tvActivityGiftName.setText(activityGitBean.getRemark());
            long etime = activityGitBean.getEtime();
            String indate = new SimpleDateFormat("yyyy-MM-dd").format(etime);
            holder.tvRedpacketsCanusePeriod.setText(indate + "到期");
            double rate = activityGitBean.getRate();
            holder.tvRedpacketsCanuseAccount.setText("+" + rate + "%");
            holder.tvGifeDesc.setVisibility(View.VISIBLE);
            holder.llUnuseableBg.setBackgroundResource(R.drawable.bg_financetickets_jiaxi);
            String time = new SimpleDateFormat("yyyy-MM-dd").format(etime);
            holder.tvRedpacketsCanusePeriod.setText(time + "到期");
            holder.tvRedpacketsCanuseRemark.setVisibility(View.INVISIBLE);
            holder.tvRedpacketsCanusePeriod.setTextColor(Color.parseColor("#333333"));
           /* if (rate > 0.5) {
                //设置不同的背景颜色
                holder.llUnuseableBg.setBackgroundResource(R.drawable.bg_financetickets_jiaxi);
            } else {
                holder.llUnuseableBg.setBackgroundResource(R.drawable.bg_financetickets_xinshou);
            }*/
            if (!activityGitBean.isUseable()) {
                holder.llUnuseableBg.setAlpha(0.5f);
            } else {
                holder.llUnuseableBg.setAlpha(1f);
            }
        }
        return convertView;
    }

    class ViewHolder {
        @InjectView(R.id.iv_select_hb)
        ImageView ivSelectHb;
        @InjectView(R.id.tv_redpackets_canuse_remark)
        TextView tvRedpacketsCanuseRemark;
        @InjectView(R.id.tv_redpackets_canuse_account)
        TextView tvRedpacketsCanuseAccount;
        @InjectView(R.id.tv_redpackets_canuse_period)
        TextView tvRedpacketsCanusePeriod;
        @InjectView(R.id.ll_redpacket_item)
        LinearLayout llRedpacketItem;
        @InjectView(R.id.layout_repacket_childview_invest)
        LinearLayout layoutRepacketChildviewInvest;
        @InjectView(R.id.ll_unuseable_bg)
        LinearLayout llUnuseableBg;
        @InjectView(R.id.tv_activity_gift_name)
        TextView tvActivityGiftName;
        @InjectView(R.id.tv_gife_desc)
        TextView tvGifeDesc;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    @Override
    public boolean isEnabled(int position) {
        for (int i = 0; i < activityGitList.size(); i++) {
            if (!activityGitList.get(position).isUseable()) {
                return false;
            }
        }
        return super.isEnabled(position);
    }
}
