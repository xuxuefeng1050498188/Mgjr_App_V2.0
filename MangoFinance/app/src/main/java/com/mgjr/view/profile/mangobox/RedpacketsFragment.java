package com.mgjr.view.profile.mangobox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.model.bean.EventBusBean.MangoBoxDataBean;
import com.mgjr.model.bean.MangoBoxBean;
import com.mgjr.share.BaseFrament;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Administrator on 2016/8/1.
 */
public class RedpacketsFragment extends BaseFrament implements ExpandableListView.OnGroupClickListener, ExpandableListView.OnGroupExpandListener {

    private MangoBoxDataBean mangoBoxBean;
    private MangoBoxBean.GiftMapBean giftMapBean;

    private ExpandableListView exlist_redpackets;
    private TextView tv_rp_balance, tv_rp_allincome;

    private RedPacketExAdapter adapter;


    private LinearLayout groupLayout;

    private ImageView groupIndicator;

    //可用红包列表
    private List<MangoBoxBean.CanUsedtHBListBean> canUsedtHBList;
    //过期红包列表
    private List<MangoBoxBean.OverHBListBean> overHBList;
    //已使用红包
    private List<MangoBoxBean.UsedtHBListBean> usedtHBList;
    private int lastClick = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fg_redpackets, null);
        initViews(layout);
        if (mangoBoxBean == null) {
            EventBus.getDefault().register(this);
        }
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
    }



    private void initViews(View layout) {

        tv_rp_balance = (TextView) layout.findViewById(R.id.tv_rp_balance);
        tv_rp_allincome = (TextView) layout.findViewById(R.id.tv_rp_allincome);

        groupLayout = (LinearLayout) LayoutInflater.from(MainApplication.getContext()).inflate(R.layout.layout_redpacket_exlist_grouplayout, null, false);


        exlist_redpackets = (ExpandableListView) layout.findViewById(R.id.exlist_redpackets);
        exlist_redpackets.setGroupIndicator(null);
        exlist_redpackets.setOnGroupClickListener(this);
        exlist_redpackets.setOnGroupExpandListener(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void rpEventBus(MangoBoxDataBean event) {
        this.mangoBoxBean = event;
        canUsedtHBList = mangoBoxBean.getCanUsedtHBList();
        overHBList = mangoBoxBean.getOverHBList();
        usedtHBList = mangoBoxBean.getUsedtHBList();
        giftMapBean = mangoBoxBean.getGiftMapBean();
        setData();
        adapter = new RedPacketExAdapter(canUsedtHBList, overHBList, usedtHBList);
        exlist_redpackets.setAdapter(adapter);
        exlist_redpackets.expandGroup(0);
    }

    private void setData() {
        tv_rp_balance.setText(new DecimalFormat("###,###,##0.00").format(giftMapBean.getHbAmount()));
        tv_rp_allincome.setText(new DecimalFormat("###,###,##0.00").format(giftMapBean.getAllHbAmount()));
    }


    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        ImageView groupIndicator = (ImageView) v.findViewById(R.id.img_group_indicator_rp);


        if (groupIndicator.getRotation() == 180) {
            groupIndicator.setRotation(0);

        } else {
            groupIndicator.setRotation(180);

        }


        return false;

//        if(lastClick == -1)
//        {
//            exlist_redpackets.expandGroup(groupPosition);
//        }
//
//        if(lastClick != -1 && lastClick != groupPosition)
//        {
//            exlist_redpackets.collapseGroup(lastClick);
//            exlist_redpackets.expandGroup(groupPosition);
//        }
//        else if(lastClick == groupPosition)
//        {
//            if(exlist_redpackets.isGroupExpanded(groupPosition))
//                exlist_redpackets.collapseGroup(groupPosition);
//            else if(!exlist_redpackets.isGroupExpanded(groupPosition))
//                exlist_redpackets.expandGroup(groupPosition);
//        }
//
//        lastClick = groupPosition;
//        return true;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onGroupExpand(int groupPosition) {
//        for (int i = 0, count = exlist_redpackets
//                .getExpandableListAdapter().getGroupCount(); i < count; i++) {
//            if (groupPosition != i) {// 关闭其他分组
//                exlist_redpackets.collapseGroup(i);
//            }
//        }
    }
}
