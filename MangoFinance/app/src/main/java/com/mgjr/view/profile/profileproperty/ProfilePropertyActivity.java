package com.mgjr.view.profile.profileproperty;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.MyFragmentPageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/18.
 */
public class ProfilePropertyActivity extends ActionbarActivity implements View.OnClickListener {

    private TextView tv_profile_propertytitle, tv_profile_incometitle;
    private ImageView img_profile_property_indicator, img_profile_income_indicator;
    private ViewPager vp_profile_property;

    private FrameLayout layout_profile_property_content;

    private LayoutInflater inflater;

    private View layout_property, layout_income;

    private List<Fragment> fragments;

    private FragmentManager fragmentManager;

    private PropertyDetailsFragment propertyDetailsFragment;
    private IncomeDetailsFragment incomeDetailsFragment;

    private MyFragmentPageAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.layout_profile_property, this);
        this.actionbar.setBackgroundColor(Color.WHITE);
        this.actionbar.setCenterTextView("资金总览");
        initViews();
        initViewPager();
    }

    private void initViews() {

        vp_profile_property = (ViewPager) findViewById(R.id.vp_profile_property);

        inflater = (LayoutInflater) MainApplication.getContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        fragmentManager = getSupportFragmentManager();

        layout_property = inflater.inflate(R.layout.layout_profile_property_details, null);
        layout_income = inflater.inflate(R.layout.layout_profile_property_details, null);

        incomeDetailsFragment = new IncomeDetailsFragment();
        propertyDetailsFragment = new PropertyDetailsFragment();

        fragments = new ArrayList<>();
        fragments.add(propertyDetailsFragment);
        fragments.add(incomeDetailsFragment);

        layout_profile_property_content = (FrameLayout) findViewById(R.id.layout_profile_property_content);

        tv_profile_propertytitle = (TextView) findViewById(R.id.tv_profile_propertytitle);
        tv_profile_propertytitle.setOnClickListener(this);

        tv_profile_incometitle = (TextView) findViewById(R.id.tv_profile_incometitle);
        tv_profile_incometitle.setOnClickListener(this);

        img_profile_property_indicator = (ImageView) findViewById(R.id.img_profile_property_indicator);

        img_profile_income_indicator = (ImageView) findViewById(R.id.img_profile_income_indicator);
    }

    @Override
    public void onClick(View v) {
        resetTextColor();
        if (v == tv_profile_propertytitle) {
            vp_profile_property.setCurrentItem(0);
            tv_profile_propertytitle.setTextColor(Color.parseColor("#feaa00"));
            tv_profile_incometitle.setTextColor(Color.parseColor("#333333"));
            img_profile_income_indicator.setVisibility(View.INVISIBLE);
            img_profile_property_indicator.setVisibility(View.VISIBLE);

        } else if (v == tv_profile_incometitle) {
            vp_profile_property.setCurrentItem(1);
            tv_profile_incometitle.setTextColor(Color.parseColor("#feaa00"));
            tv_profile_propertytitle.setTextColor(Color.parseColor("#333333"));
            img_profile_property_indicator.setVisibility(View.INVISIBLE);
            img_profile_income_indicator.setVisibility(View.VISIBLE);
        }
    }

    private void resetTextColor() {
        tv_profile_propertytitle.setTextColor(Color.GRAY);
        tv_profile_incometitle.setTextColor(Color.GRAY);
    }

    private void initViewPager() {
        Bundle bundle = this.getIntent().getExtras();
        int tag = Integer.parseInt(bundle.getString("code"));
        adapter = new MyFragmentPageAdapter(fragments, getSupportFragmentManager());
        vp_profile_property.setAdapter(adapter);
        vp_profile_property.addOnPageChangeListener(new MyPageChangeListener());
        if (tag != 0) {
            vp_profile_property.setCurrentItem(tag);
        }else {
            vp_profile_property.setCurrentItem(0);
        }
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                tv_profile_propertytitle.setTextColor(Color.parseColor("#feaa00"));
                tv_profile_incometitle.setTextColor(Color.GRAY);
                img_profile_property_indicator.setVisibility(View.VISIBLE);
                img_profile_income_indicator.setVisibility(View.INVISIBLE);
            } else if (position == 1) {
                tv_profile_incometitle.setTextColor(Color.parseColor("#feaa00"));
                tv_profile_propertytitle.setTextColor(Color.GRAY);
                img_profile_income_indicator.setVisibility(View.VISIBLE);
                img_profile_property_indicator.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
