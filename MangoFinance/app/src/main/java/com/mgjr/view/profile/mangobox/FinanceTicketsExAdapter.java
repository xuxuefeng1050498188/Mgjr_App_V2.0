package com.mgjr.view.profile.mangobox;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.model.bean.MangoBoxBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/1.
 */
public class FinanceTicketsExAdapter extends BaseExpandableListAdapter {

    private LayoutInflater inflater;
    private FrameLayout childview_null;
    private LinearLayout childview_xinshou, childview_jiaxi, childview_used, childview_overuse;
    private TextView tv_tips;
    //可用加息券列表
    private List<MangoBoxBean.CanUsedCouponListBean> canUsedCouponList;
    //过期加息券列表
    private List<MangoBoxBean.OverCouponListBean> overCouponList;
    //已使用加息券
    private List<MangoBoxBean.UsedCouponListBean> usedCouponList;

    /*可使用加息券-新手//加息券率，红包有效期，备注*/
    private TextView tv_financetickets_canuse_rate, tv_financetickets_canuse_period, tv_financetickets_canuse_remark;
    /*可使用加息券*/
    private TextView tv_financetickets_jiaxi_canuse_rate, tv_financetickets_jiaxi_canuse_period, tv_financetickets_jiaxi_canuse_remark;

    /*已使用*/
    private TextView tv_financetickets_used_period, tv_financetickets_used_remark, tv_financetickets_used_rate, tv_financetickets_used_title;

    /*已过期加息券//*/
    private TextView tv_financetickets_over_rate, tv_financetickets_over_period, tv_financetickets_over_remark;


    private final String[] groupTitles = {"可使用理财券", "已使用理财券", "已过期理财券"};
    private int[] groupIcons = {R.drawable.triangle_down_btn, R.drawable.triangle_down_btn, R.drawable.triangle_down_btn};

    private List<View> childViews;

    public FinanceTicketsExAdapter(List<MangoBoxBean.CanUsedCouponListBean> canUsedCouponList, List<MangoBoxBean.OverCouponListBean> overCouponList, List<MangoBoxBean.UsedCouponListBean> usedCouponList) {
        this.canUsedCouponList = canUsedCouponList;
        this.overCouponList = overCouponList;
        this.usedCouponList = usedCouponList;

    }

    @Override
    public int getGroupCount() {
        return groupTitles.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition == 0) {
            if (canUsedCouponList.size() == 0) {
                return 1;
            }
            return canUsedCouponList.size();
        } else if (groupPosition == 1) {
            if (usedCouponList.size() == 0) {
                return 1;
            }
            return usedCouponList.size();
        } else {
            if (overCouponList.size() == 0) {
                return 1;
            }
            return overCouponList.size();
        }

    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupTitles[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        if (groupPosition == 0) {

            return canUsedCouponList.get(childPosition);
        } else if (groupPosition == 1) {

            return usedCouponList.get(childPosition);
        } else {

            return overCouponList.get(childPosition);
        }
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            inflater = (LayoutInflater) MainApplication.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_profile_property_exlist_group, null);
        }
        LinearLayout groupLayout = (LinearLayout) convertView.findViewById(R.id.layout_profile_property_exlist_group);
        TextView groupTitle = (TextView) groupLayout.findViewById(R.id.tv_grouplist_title);
        groupTitle.setText(groupTitles[groupPosition]);
        groupTitle.setTextColor(Color.BLACK);
        ImageView groupIndicator = (ImageView) convertView.findViewById(R.id.img_group_indicator);
        if (groupPosition == 0) {
            if (isExpanded) {
                groupIndicator.setRotation(180);
            } else {
                groupIndicator.setRotation(0);
            }

        }
        return groupLayout;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) MainApplication.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        childview_xinshou = (LinearLayout) inflater.inflate(R.layout.layout_financetickets_childview_xinshou, null);
        childview_jiaxi = (LinearLayout) inflater.inflate(R.layout.layout_financetickets_childview_jiaxi, null);
        childview_used = (LinearLayout) inflater.inflate(R.layout.layout_financetickets_childview_used, null);
        childview_overuse = (LinearLayout) inflater.inflate(R.layout.layout_financetickets_childview_overuse, null);
        childview_null = (FrameLayout) inflater.inflate(R.layout.layout_financetickets_childview_null, null);
        tv_tips = (TextView) childview_null.findViewById(R.id.tv_tips);
        /**可使用*/
        tv_financetickets_canuse_rate = (TextView) childview_xinshou.findViewById(R.id.tv_financetickets_canuse_rate);
        tv_financetickets_canuse_period = (TextView) childview_xinshou.findViewById(R.id.tv_financetickets_canuse_period);
        tv_financetickets_canuse_remark = (TextView) childview_xinshou.findViewById(R.id.tv_financetickets_canuse_remark);
        if (canUsedCouponList.size() != 0) {
            if (groupPosition == 0) {
                long etime = canUsedCouponList.get(childPosition).getEtime();
                if (etime == 0) {
                    tv_financetickets_canuse_period.setText("");
                } else {
                    tv_financetickets_canuse_period.setText(new SimpleDateFormat("yyyy-MM-dd").format(etime) + "到期");
                }
                tv_financetickets_canuse_rate.setText("+" + canUsedCouponList.get(childPosition).getRate() + "%");
                tv_financetickets_canuse_remark.setText(canUsedCouponList.get(childPosition).getRemark());
            }
        }
        /*已使用*/
        tv_financetickets_used_period = (TextView) childview_used.findViewById(R.id.tv_financetickets_used_period);
        tv_financetickets_used_remark = (TextView) childview_used.findViewById(R.id.tv_financetickets_used_remark);
        tv_financetickets_used_rate = (TextView) childview_used.findViewById(R.id.tv_financetickets_used_rate);
        tv_financetickets_used_title = (TextView) childview_used.findViewById(R.id.tv_financetickets_used_title);
        if (usedCouponList.size() != 0) {
            if (groupPosition == 1) {
                long gtime = usedCouponList.get(childPosition).getGtime();
                if (gtime == 0) {
                    tv_financetickets_used_period.setText("");
                } else {
                    tv_financetickets_used_period.setText(new SimpleDateFormat("yyyy-MM-dd").format(gtime) + "已使用");
                }
                tv_financetickets_used_remark.setText(usedCouponList.get(childPosition).getRemark());
                tv_financetickets_used_rate.setText("+" + usedCouponList.get(childPosition).getRate() + "%");
                tv_financetickets_used_title.setText(usedCouponList.get(childPosition).getLoanTitle());
            }
        }

        /*已过期*/
        tv_financetickets_over_rate = (TextView) childview_overuse.findViewById(R.id.tv_financetickets_over_rate);
        tv_financetickets_over_period = (TextView) childview_overuse.findViewById(R.id.tv_financetickets_over_period);
        tv_financetickets_over_remark = (TextView) childview_overuse.findViewById(R.id.tv_financetickets_over_remark);

        if (groupPosition == 2 && overCouponList.size() != 0) {
            long etime = overCouponList.get(childPosition).getEtime();
            if (etime == 0) {
                tv_financetickets_over_period.setText("");
            } else {
                tv_financetickets_over_period.setText(new SimpleDateFormat("yyyy-MM-dd").format(etime) + "已过期");
            }
            tv_financetickets_over_rate.setText("+" + overCouponList.get(childPosition).getRate() + "%");
            tv_financetickets_over_remark.setText(overCouponList.get(childPosition).getRemark());
        }
        childViews = new ArrayList<>();
        childViews.add(childview_xinshou);
        childViews.add(childview_jiaxi);
        childViews.add(childview_overuse);
        childViews.add(childview_null);


        if (groupPosition == 0) {
            if (canUsedCouponList.size() == 0) {
                tv_tips.setText("暂无可使用加息券");
                tv_tips.setTextColor(Color.parseColor("#666666"));
                return childview_null;
            }
            return childview_xinshou;
        } else if (groupPosition == 1) {
            if (usedCouponList.size() == 0) {
                tv_tips.setText("暂无已使用加息券");
                tv_tips.setTextColor(Color.parseColor("#666666"));
                return childview_null;
            }
            return childview_used;
        } else {
            if (overCouponList.size() == 0) {
                tv_tips.setText("暂无已过期加息券");
                tv_tips.setTextColor(Color.parseColor("#666666"));
                return childview_null;
            }
            return childview_overuse;
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


}
