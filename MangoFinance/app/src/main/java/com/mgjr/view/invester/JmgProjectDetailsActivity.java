package com.mgjr.view.invester;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.MyFragmentPageAdapter;
import com.mgjr.view.common.CommonWebViewActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/12.
 */
public class JmgProjectDetailsActivity extends ActionbarActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private ViewPager vp_jmg_projectdetails;
    private TextView tv_projectIntroduce, tv_businessInfo, tv_guaranteeInfo;
    private ImageView img_indicator_left, img_indicator_mid, img_indicator_right;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_layout_jmg_projectdetails, this);
        actionbar.setBackgroundColor(Color.parseColor("#feaa00"));
        actionbar.setCenterTextView("项目详情");
        actionbar.setRightTextView("协议范本", this);
        actionbar.setLeftImageView(R.drawable.invest_left_arrow, this);
        actionbar.centerTextView.setTextColor(Color.WHITE);
        actionbar.rightTextView.setTextColor(Color.WHITE);
        actionbar.rightTextView.setTextSize(14);
        initViews();
        initViewPager();
    }


    private void initViews() {
        vp_jmg_projectdetails = (ViewPager) findViewById(R.id.vp_jmg_projectdetails);
        tv_projectIntroduce = (TextView) findViewById(R.id.tv_projectIntroduce);
        tv_businessInfo = (TextView) findViewById(R.id.tv_businessInfo);
        tv_guaranteeInfo = (TextView) findViewById(R.id.tv_guaranteeInfo);
        tv_projectIntroduce.setOnClickListener(this);
        tv_businessInfo.setOnClickListener(this);
        tv_guaranteeInfo.setOnClickListener(this);

        img_indicator_left = (ImageView) findViewById(R.id.img_indicator_left);
        img_indicator_mid = (ImageView) findViewById(R.id.img_indicator_mid);
        img_indicator_right = (ImageView) findViewById(R.id.img_indicator_right);

        fragmentList = new ArrayList<>();
    }

    private void initViewPager() {
        JmgProjectIntroduceFragment jmgProjectIntroduceFragment = new JmgProjectIntroduceFragment();
        JmgBusinessInfoFragment jmgBusinessInfoFragment = new JmgBusinessInfoFragment();
        JmgGuaranteeInfoFragment jmgGuaranteeInfoFragment = new JmgGuaranteeInfoFragment();
        fragmentList.add(jmgProjectIntroduceFragment);
        fragmentList.add(jmgBusinessInfoFragment);
        fragmentList.add(jmgGuaranteeInfoFragment);
        MyFragmentPageAdapter adapter = new MyFragmentPageAdapter(fragmentList, this.getSupportFragmentManager());
        vp_jmg_projectdetails.setAdapter(adapter);
        vp_jmg_projectdetails.setCurrentItem(0);
        vp_jmg_projectdetails.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            tv_projectIntroduce.setTextColor(Color.parseColor("#fea901"));
            tv_businessInfo.setTextColor(Color.GRAY);
            tv_guaranteeInfo.setTextColor(Color.GRAY);
            img_indicator_left.setVisibility(View.VISIBLE);
            img_indicator_right.setVisibility(View.INVISIBLE);
            img_indicator_mid.setVisibility(View.INVISIBLE);
            vp_jmg_projectdetails.setCurrentItem(0);
        } else if (position == 1) {
            tv_businessInfo.setTextColor(Color.parseColor("#fea901"));
            tv_projectIntroduce.setTextColor(Color.GRAY);
            tv_guaranteeInfo.setTextColor(Color.GRAY);
            img_indicator_left.setVisibility(View.INVISIBLE);
            img_indicator_right.setVisibility(View.INVISIBLE);
            img_indicator_mid.setVisibility(View.VISIBLE);
            vp_jmg_projectdetails.setCurrentItem(1);
        } else if (position == 2) {
            tv_guaranteeInfo.setTextColor(Color.parseColor("#fea901"));
            tv_projectIntroduce.setTextColor(Color.GRAY);
            tv_businessInfo.setTextColor(Color.GRAY);
            img_indicator_left.setVisibility(View.INVISIBLE);
            img_indicator_right.setVisibility(View.VISIBLE);
            img_indicator_mid.setVisibility(View.INVISIBLE);
            vp_jmg_projectdetails.setCurrentItem(2);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        if (v == actionbar.leftImageView) {
//            popActivity();
            MyActivityManager.getInstance().popCurrentActivity();
        } else if (v == actionbar.rightTextView) {
            MyActivityManager.getInstance().startNextActivity(CommonWebViewActivity.class, "bondTransfer");
        } else if (v == tv_projectIntroduce) {
            tv_projectIntroduce.setTextColor(Color.parseColor("#fea901"));
            tv_businessInfo.setTextColor(Color.GRAY);
            tv_guaranteeInfo.setTextColor(Color.GRAY);
            img_indicator_left.setVisibility(View.VISIBLE);
            img_indicator_right.setVisibility(View.INVISIBLE);
            img_indicator_mid.setVisibility(View.INVISIBLE);
            vp_jmg_projectdetails.setCurrentItem(0);
        } else if (v == tv_businessInfo) {
            tv_businessInfo.setTextColor(Color.parseColor("#fea901"));
            tv_projectIntroduce.setTextColor(Color.GRAY);
            tv_guaranteeInfo.setTextColor(Color.GRAY);
            img_indicator_left.setVisibility(View.INVISIBLE);
            img_indicator_right.setVisibility(View.INVISIBLE);
            img_indicator_mid.setVisibility(View.VISIBLE);
            vp_jmg_projectdetails.setCurrentItem(1);
        } else if (v == tv_guaranteeInfo) {
            tv_guaranteeInfo.setTextColor(Color.parseColor("#fea901"));
            tv_businessInfo.setTextColor(Color.GRAY);
            tv_projectIntroduce.setTextColor(Color.GRAY);
            img_indicator_left.setVisibility(View.INVISIBLE);
            img_indicator_right.setVisibility(View.VISIBLE);
            img_indicator_mid.setVisibility(View.INVISIBLE);
            vp_jmg_projectdetails.setCurrentItem(2);
        }
    }
}
