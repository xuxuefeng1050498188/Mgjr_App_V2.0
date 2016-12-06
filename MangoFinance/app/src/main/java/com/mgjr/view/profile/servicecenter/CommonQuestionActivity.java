package com.mgjr.view.profile.servicecenter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.MyFragmentPageAdapter;

import java.util.ArrayList;
import java.util.List;

public class CommonQuestionActivity extends ActionbarActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private TextView tv_hotproblem, tv_classproblem;
    private ImageView img_indicator_left, img_indicator_right;
    private ViewPager viewpager;
    private List<Fragment> fragmentList;
    private WebView wv;
    private HotProblemFragment hotProblemFragment;
    private ClassProblemFragment classProblemFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_profile_common_question, this);
        actionbar.setCenterTextView("常见问题");
        initView();

    }

    private void initView() {
        actionbar.leftImageView.setOnClickListener(this);
        tv_hotproblem = (TextView) findViewById(R.id.tv_hotproblem);
        tv_classproblem = (TextView) findViewById(R.id.tv_classproblem);
        tv_classproblem.setOnClickListener(this);
        tv_hotproblem.setOnClickListener(this);
        img_indicator_left = (ImageView) findViewById(R.id.img_indicator_left);
        img_indicator_right = (ImageView) findViewById(R.id.img_indicator_right);


        viewpager = (ViewPager) findViewById(R.id.viewpager_common_question);
        hotProblemFragment = new HotProblemFragment();
        classProblemFragment = new ClassProblemFragment();
        fragmentList = new ArrayList<>();
        fragmentList.add(hotProblemFragment);
        fragmentList.add(classProblemFragment);
        MyFragmentPageAdapter adapter = new MyFragmentPageAdapter(fragmentList, this.getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);
        viewpager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            tv_hotproblem.setTextColor(Color.parseColor("#fea901"));
            tv_classproblem.setTextColor(Color.GRAY);
            img_indicator_left.setVisibility(View.VISIBLE);
            img_indicator_right.setVisibility(View.INVISIBLE);
            wv = hotProblemFragment.getWebView();
        } else if (position == 1) {
            tv_hotproblem.setTextColor(Color.GRAY);
            tv_classproblem.setTextColor(Color.parseColor("#fea901"));
            img_indicator_left.setVisibility(View.INVISIBLE);
            img_indicator_right.setVisibility(View.VISIBLE);
            wv = classProblemFragment.getWebView();

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        if (v == tv_hotproblem) {
            viewpager.setCurrentItem(0);
            tv_hotproblem.setTextColor(Color.parseColor("#fea901"));
            tv_classproblem.setTextColor(Color.GRAY);
            img_indicator_left.setVisibility(View.VISIBLE);
            img_indicator_right.setVisibility(View.INVISIBLE);
        } else if (v == tv_classproblem) {
            viewpager.setCurrentItem(1);
            tv_classproblem.setTextColor(Color.parseColor("#fea901"));
            tv_hotproblem.setTextColor(Color.GRAY);
            img_indicator_right.setVisibility(View.VISIBLE);
            img_indicator_left.setVisibility(View.INVISIBLE);
        } else if (v == actionbar.leftImageView) {

            if (wv != null && wv.canGoBack()) {
                wv.goBack();
            } else {
                MyActivityManager.getInstance().popCurrentActivity();
            }
        }
    }

}
