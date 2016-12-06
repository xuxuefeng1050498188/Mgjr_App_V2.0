package com.mgjr.view.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.MyFragmentPageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/11.
 * 原 消息中心
 */
public class MessageCenterActivity extends ActionbarActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private TextView tv_investinfo,tv_platformmsg;
    private ImageView img_indicator_left,img_indicator_right;
    private FrameLayout layout_vp_content;
    private ViewPager vp_messagecenter;

    private List<Fragment> fragmentList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_layout_messagecenter,this);
        actionbar.setCenterTextView("消息中心");
        actionbar.setRightImageView(R.drawable.profile_message_center_setting,this);
        initViews();
        initViewPager();
    }

    private void initViews() {
        tv_investinfo = (TextView) findViewById(R.id.tv_investinfo);
        tv_platformmsg = (TextView) findViewById(R.id.tv_platformmsg);
        tv_investinfo.setOnClickListener(this);
        tv_platformmsg.setOnClickListener(this);

        img_indicator_left = (ImageView) findViewById(R.id.img_indicator_left);
        img_indicator_right = (ImageView) findViewById(R.id.img_indicator_right);

        layout_vp_content = (FrameLayout) findViewById(R.id.layout_vp_content);

        vp_messagecenter = (ViewPager) findViewById(R.id.vp_messagecenter);
    }

    private void initViewPager() {
        fragmentList = new ArrayList<>();
        MsgCenterInvestInfoFg investInfoFg = new MsgCenterInvestInfoFg();
        PlatformMsgFg platformMsgFg = new PlatformMsgFg();
        fragmentList.add(investInfoFg);
        fragmentList.add(platformMsgFg);
        MyFragmentPageAdapter adapter = new MyFragmentPageAdapter(fragmentList,this.getSupportFragmentManager());
        vp_messagecenter.setAdapter(adapter);
        vp_messagecenter.setCurrentItem(0);
        vp_messagecenter.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position == 0){
            tv_investinfo.setTextColor(Color.parseColor("#fea901"));
            tv_platformmsg.setTextColor(Color.GRAY);
            img_indicator_left.setVisibility(View.VISIBLE);
            img_indicator_right.setVisibility(View.INVISIBLE);
        }
        else if(position == 1){
            tv_investinfo.setTextColor(Color.GRAY);
            tv_platformmsg.setTextColor(Color.parseColor("#fea901"));
            img_indicator_left.setVisibility(View.INVISIBLE);
            img_indicator_right.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        if(v == tv_investinfo){
            vp_messagecenter.setCurrentItem(0);
            tv_investinfo.setTextColor(Color.parseColor("#fea901"));
            tv_platformmsg.setTextColor(Color.GRAY);
            img_indicator_left.setVisibility(View.VISIBLE);
            img_indicator_right.setVisibility(View.INVISIBLE);
        }else if(v == tv_platformmsg){
            vp_messagecenter.setCurrentItem(1);
            tv_platformmsg.setTextColor(Color.parseColor("#fea901"));
            tv_investinfo.setTextColor(Color.GRAY);
            img_indicator_right.setVisibility(View.VISIBLE);
            img_indicator_left.setVisibility(View.INVISIBLE);
        }
    }
}
