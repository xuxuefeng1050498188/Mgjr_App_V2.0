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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/1.
 */
public class RedPacketExAdapter extends BaseExpandableListAdapter {

    private LayoutInflater inflater;
    private FrameLayout childview_null;
    LinearLayout childview_canuse, childview_overuse, childview_used;
    private TextView tv_tips;

    //可用红包列表
    private List<MangoBoxBean.CanUsedtHBListBean> canUsedtHBList;
    //过期红包列表
    private List<MangoBoxBean.OverHBListBean> overHBList;
    //已使用红包
    private List<MangoBoxBean.UsedtHBListBean> usedtHBList;


    //红包金额，红包有效期
    private TextView tv_redpackets_canuse_account, tv_redpackets_canuse_period, tv_redpackets_canuse_remark, tv_redpackets_name;

    /*已使用*/
    private TextView tv_redpackets_used_title, tv_redpackets_used_projectname, tv_redpackets_used_period, tv_redpackets_used_account, tv_redpackets_used_remark;
    //已过期
    private TextView tv_redpackets_overuse_account, tv_redpackets_overuse_remark, tv_redpackets_period_unuseable, tv_redpackets_overuse_title;

    private final String[] groupTitles = {"可使用红包", "已使用红包", "已过期红包"};
    private int[] groupIcons = {R.drawable.triangle_down_btn, R.drawable.triangle_down_btn, R.drawable.triangle_down_btn};

    private List<View> childViews;

    public RedPacketExAdapter(List<MangoBoxBean.CanUsedtHBListBean> canUsedtHBList, List<MangoBoxBean.OverHBListBean> overHBList, List<MangoBoxBean.UsedtHBListBean> usedtHBList) {
        this.canUsedtHBList = canUsedtHBList;
        this.overHBList = overHBList;
        this.usedtHBList = usedtHBList;
    }

    @Override
    public int getGroupCount() {
        return groupTitles.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition == 0) {
            if (canUsedtHBList.size() == 0) {
                return 1;
            }
            return canUsedtHBList.size();
        } else if (groupPosition == 1) {
            if (usedtHBList.size() == 0) {
                return 1;
            }
            return usedtHBList.size();
        } else {
            if (overHBList.size() == 0) {
                return 1;
            }
            return overHBList.size();
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupTitles[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (groupPosition == 0) {
            return canUsedtHBList.get(childPosition);
        } else if (groupPosition == 1) {
            return usedtHBList.get(childPosition);
        } else {
            return overHBList.get(childPosition);
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
            convertView = inflater.inflate(R.layout.layout_redpacket_exlist_grouplayout, null);
        }
        LinearLayout groupLayout = (LinearLayout) convertView.findViewById(R.id.layout_red_exlist_grouplayout);
        TextView groupTitle = (TextView) groupLayout.findViewById(R.id.tv_grouplist_title_rp);
        groupTitle.setText(groupTitles[groupPosition]);
        groupTitle.setTextColor(Color.BLACK);
        ImageView groupIndicator = (ImageView) convertView.findViewById(R.id.img_group_indicator_rp);
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
//        convertView = inflater.inflate(R.layout.layout_redpacketslist_childview_useable, null);
//        childview_canuse = (LinearLayout) convertView.findViewById(R.id.layout_repacket_childview_invest);

        childview_canuse = (LinearLayout) inflater.inflate(R.layout.layout_redpacketslist_childview_useable,null);
        childview_used = (LinearLayout) inflater.inflate(R.layout.layout_redpacketslist_childview_used, null);
        childview_overuse = (LinearLayout) inflater.inflate(R.layout.layout_redpacketslist_childview_overuse, null);
        childview_null = (FrameLayout) inflater.inflate(R.layout.layout_redpacketslist_childview_null, null);
        tv_tips = (TextView) childview_null.findViewById(R.id.tv_tips);

        /*可使用*/
        tv_redpackets_name = (TextView) childview_canuse.findViewById(R.id.tv_redpackets_name);
        tv_redpackets_canuse_account = (TextView) childview_canuse.findViewById(R.id.tv_redpackets_canuse_account);
        tv_redpackets_canuse_period = (TextView) childview_canuse.findViewById(R.id.tv_redpackets_canuse_period);
        tv_redpackets_canuse_remark = (TextView) childview_canuse.findViewById(R.id.tv_redpackets_canuse_remark);
        if (canUsedtHBList.size() != 0) {
            if (groupPosition == 0) {
                long etime = canUsedtHBList.get(childPosition).getEtime();
                if (etime == 0) {
                    tv_redpackets_canuse_period.setText("");
                } else {
                    tv_redpackets_canuse_period.setText("有效期至" + new SimpleDateFormat("yyyy-MM-dd").format(etime));
                }
                tv_redpackets_name.setText(canUsedtHBList.get(childPosition).getTitle());
                tv_redpackets_canuse_account.setText(new DecimalFormat("###,###,##0").format(canUsedtHBList.get(childPosition).getAmount()) + "元");
                tv_redpackets_canuse_remark.setText(canUsedtHBList.get(childPosition).getRemark());
            }
        }
        /*已使用*/
        tv_redpackets_used_title = (TextView) childview_used.findViewById(R.id.tv_redpackets_used_title);
        tv_redpackets_used_projectname = (TextView) childview_used.findViewById(R.id.tv_redpackets_used_projectname);
        tv_redpackets_used_period = (TextView) childview_used.findViewById(R.id.tv_redpackets_used_period);
        tv_redpackets_used_account = (TextView) childview_used.findViewById(R.id.tv_redpackets_used_account);
        tv_redpackets_used_remark = (TextView) childview_used.findViewById(R.id.tv_redpackets_used_remark);
        if (usedtHBList.size() != 0) {
            if (groupPosition == 1) {
                tv_redpackets_used_title.setText(usedtHBList.get(childPosition).getTitle());
                long gtime = usedtHBList.get(childPosition).getGtime();
                if (gtime == 0) {
                    tv_redpackets_used_period.setText("");
                } else {
                    tv_redpackets_used_period.setText(new SimpleDateFormat("yyyy-MM-dd").format(gtime) + "已使用");
                }
                tv_redpackets_used_account.setText(new DecimalFormat("###,###,##0").format(usedtHBList.get(childPosition).getAmount()) + "元");
                tv_redpackets_used_remark.setText(usedtHBList.get(childPosition).getRemark());
                tv_redpackets_used_projectname.setText(usedtHBList.get(childPosition).getLoanTitle());
            }
        }

        /*已过期*/
        tv_redpackets_overuse_title = (TextView) childview_overuse.findViewById(R.id.tv_redpackets_overuse_title);
        tv_redpackets_overuse_account = (TextView) childview_overuse.findViewById(R.id.tv_redpackets_overuse_account);
        tv_redpackets_overuse_remark = (TextView) childview_overuse.findViewById(R.id.tv_redpackets_overuse_remark);
        tv_redpackets_period_unuseable = (TextView) childview_overuse.findViewById(R.id.tv_redpackets_period_unuseable);
        if (overHBList.size() != 0) {
            if (groupPosition == 2) {
                tv_redpackets_overuse_title.setText(overHBList.get(childPosition).getTitle());
                tv_redpackets_overuse_account.setText(new DecimalFormat("###,###,##0").format(overHBList.get(childPosition).getAmount()) + "元");
                tv_redpackets_overuse_remark.setText(overHBList.get(childPosition).getRemark());
                long etime = overHBList.get(childPosition).getEtime();
                if (etime == 0) {
                    tv_redpackets_period_unuseable.setText("");
                } else {
                    tv_redpackets_period_unuseable.setText(new SimpleDateFormat("yyyy-MM-dd").format(etime) + "已过期");
                }
            }
        }

        childViews = new ArrayList<>();
        childViews.add(childview_canuse);
        childViews.add(childview_used);
        childViews.add(childview_overuse);
        childViews.add(childview_null);

        if (groupPosition == 0) {
            if (canUsedtHBList.size() == 0) {
                tv_tips.setText("暂无可使用红包");
                tv_tips.setTextColor(Color.parseColor("#666666"));
                return childview_null;
            }
            return childview_canuse;
        } else if (groupPosition == 1) {
            if (usedtHBList.size() == 0) {
                tv_tips.setText("暂无已使用红包");
                tv_tips.setTextColor(Color.parseColor("#666666"));
                return childview_null;
            }
            return childview_used;
        } else {
            if (overHBList.size() == 0) {
                tv_tips.setText("暂无已过期红包");
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
