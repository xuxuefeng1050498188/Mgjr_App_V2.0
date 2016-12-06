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
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.MyFragmentPageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/12.
 */

public class HqbNotesActivity extends ActionbarActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager viewpager;
    private TextView tv_buyNotes,tv_redeemNotes;
    private ImageView img_indicator_left,img_indicator_right;
    private List<Fragment> fragmentList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_layout_hqbnotes,this);
        actionbar.setCenterTextView("买入、赎回须知");
        initView();
    }
    private void initView() {
        tv_buyNotes = (TextView) findViewById(R.id.tv_buyNotes);
        tv_redeemNotes = (TextView) findViewById(R.id.tv_redeemNotes);
        tv_buyNotes.setOnClickListener(this);
        tv_redeemNotes.setOnClickListener(this);
        img_indicator_left = (ImageView) findViewById(R.id.img_indicator_left);
        img_indicator_right = (ImageView) findViewById(R.id.img_indicator_right);

        viewpager = (ViewPager) findViewById(R.id.vp_hqbnotes);
        HqbBuyNotesFragment hqbBuyNotesFragment = new HqbBuyNotesFragment();
        HqbRedeemNotesFragment hqbRedeemNotesFragment = new HqbRedeemNotesFragment();
        fragmentList = new ArrayList<>();
        fragmentList.add(hqbBuyNotesFragment);
        fragmentList.add(hqbRedeemNotesFragment);
        MyFragmentPageAdapter adapter = new MyFragmentPageAdapter(fragmentList,this.getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);
        viewpager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == tv_buyNotes){
            viewpager.setCurrentItem(0);
            tv_buyNotes.setTextColor(Color.parseColor("#fea901"));
            tv_redeemNotes.setTextColor(Color.GRAY);
            img_indicator_left.setVisibility(View.VISIBLE);
            img_indicator_right.setVisibility(View.INVISIBLE);
        }else if(v == tv_redeemNotes){
            viewpager.setCurrentItem(1);
            tv_redeemNotes.setTextColor(Color.parseColor("#fea901"));
            tv_buyNotes.setTextColor(Color.GRAY);
            img_indicator_right.setVisibility(View.VISIBLE);
            img_indicator_left.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position == 0){
            tv_buyNotes.setTextColor(Color.parseColor("#fea901"));
            tv_redeemNotes.setTextColor(Color.GRAY);
            img_indicator_left.setVisibility(View.VISIBLE);
            img_indicator_right.setVisibility(View.INVISIBLE);
        }
        else if(position == 1){
            tv_buyNotes.setTextColor(Color.GRAY);
            tv_redeemNotes.setTextColor(Color.parseColor("#fea901"));
            img_indicator_left.setVisibility(View.INVISIBLE);
            img_indicator_right.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
