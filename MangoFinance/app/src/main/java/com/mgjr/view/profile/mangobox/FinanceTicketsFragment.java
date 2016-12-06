package com.mgjr.view.profile.mangobox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.model.bean.EventBusBean.MangoBoxDataBean;
import com.mgjr.model.bean.MangoBoxBean;
import com.mgjr.share.BaseFrament;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Administrator on 2016/8/1.
 */
public class FinanceTicketsFragment extends BaseFrament implements ExpandableListView.OnGroupClickListener, ExpandableListView.OnGroupExpandListener {

    private MangoBoxDataBean mangoBoxBean;
    private MangoBoxBean.GiftMapBean giftMapBean;
    private ExpandableListView exlist_financetickets;
    private FinanceTicketsExAdapter adapter;
    private boolean isPressed = false;
    //可用加息券
    private TextView tv_ft_canuse_number;
    //累计获得加息券
    private TextView tv_ft_all_number;
    //可用加息券列表
    private List<MangoBoxBean.CanUsedCouponListBean> canUsedCouponList;
    //过期加息券列表
    private List<MangoBoxBean.OverCouponListBean> overCouponList;
    //已使用加息券
    private List<MangoBoxBean.UsedCouponListBean> usedCouponList;
    private int lastClick = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fg_financetickets, null);
        initViews(layout);
        EventBus.getDefault().register(this);
        return layout;
    }

    private void initViews(View layout) {
        exlist_financetickets = (ExpandableListView) layout.findViewById(R.id.exlist_financetickets);
        exlist_financetickets.setGroupIndicator(null);
        exlist_financetickets.setOnGroupClickListener(this);
        exlist_financetickets.setOnGroupExpandListener(this);
        tv_ft_all_number = (TextView) layout.findViewById(R.id.tv_ft_all_number);
        tv_ft_canuse_number = (TextView) layout.findViewById(R.id.tv_ft_canuse_number);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void ftEventBus(MangoBoxDataBean event) {
        this.mangoBoxBean = event;
        canUsedCouponList = mangoBoxBean.getCanUsedCouponList();
        overCouponList = mangoBoxBean.getOverCouponList();
        usedCouponList = mangoBoxBean.getUsedCouponList();
        giftMapBean = mangoBoxBean.getGiftMapBean();
        setData();
    }


    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        ImageView groupIndicator = (ImageView) v.findViewById(R.id.img_group_indicator);

        if (groupIndicator.getRotation() == 180) {
            groupIndicator.setRotation(0);

        } else {
            groupIndicator.setRotation(180);

        }

        return false;
    }

    private void setData() {

        int couponNum = giftMapBean.getCouponNum();
        int allCouponNum = giftMapBean.getAllCouponNum();
        if (couponNum == 0) {
            tv_ft_canuse_number.setText("0");
        } else {
            tv_ft_canuse_number.setText(couponNum + "");
        }
        if (allCouponNum == 0) {
            tv_ft_all_number.setText("0");
        } else {
            tv_ft_all_number.setText(allCouponNum + "");
        }
        adapter = new FinanceTicketsExAdapter(canUsedCouponList, overCouponList, usedCouponList);
        exlist_financetickets.setAdapter(adapter);
        exlist_financetickets.expandGroup(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onGroupExpand(int groupPosition) {
//        for (int i = 0, count = exlist_financetickets
//                .getExpandableListAdapter().getGroupCount(); i < count; i++) {
//            if (groupPosition != i) {// 关闭其他分组
//                exlist_financetickets.collapseGroup(i);
//            }
//        }

    }
}
