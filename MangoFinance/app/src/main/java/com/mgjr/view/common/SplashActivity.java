package com.mgjr.view.common;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mgjr.R;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.mangofinance.MainActivity;
import com.mgjr.model.bean.EventBusBean.ChangeTabBean;
import com.mgjr.share.CommomActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/2.
 */

public class SplashActivity extends CommomActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    // 定义ViewPager对象
    private ViewPager viewPager;

    //指示器
    private LinearLayout layout_splash_indicator;

    // 定义ViewPager适配器
    private SplashViewAdapter vpAdapter;

    // 定义一个ArrayList来存放View
    private ArrayList<View> views;

    // 引导图片资源
    private static final int[] pics = {R.drawable.shui, R.drawable.feng,
            R.drawable.shan};
    private FrameLayout lastItem;
    //指示器图片
    private ImageView indicator, iv;
    // 底部小点的图片
    private ImageView[] points;

    // 记录当前选中位置
    private int currentIndex;

    private Button inBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);
        //改变是否第一次登录SP
        changeIsFirstInSp();
        // 初始化组件
        initView();
        // 初始化数据
        initData();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        // 实例化ArrayList对象
        views = new ArrayList<>();
        //指示器
        layout_splash_indicator = (LinearLayout) findViewById(R.id.layout_splash_indicator);
        // 实例化ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        // 实例化ViewPager适配器
        vpAdapter = new SplashViewAdapter(views);
        // 设置监听
        viewPager.addOnPageChangeListener(this);

        lastItem = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.layout_splash_indicator_lastitem, null);
        inBtn = (Button) lastItem.findViewById(R.id.inbtn);
        inBtn.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 定义一个布局并设置参数
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        //指示器参数
        LinearLayout.LayoutParams indicatorParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        // 初始化引导图片列表
        for (int i = 0; i <= pics.length; i++) {
            //viewpager图片资源
            if (i < pics.length) {
                iv = new ImageView(this);
                iv.setLayoutParams(mParams);
                iv.setImageResource(pics[i]);
                views.add(iv);
                //指示器图标资源
                indicator = new ImageView(this);
                layout_splash_indicator.addView(indicator, indicatorParams);
//                if (i == 0) {
//                    indicator.setBackgroundResource(R.drawable.icon_splash_indicator_selected);
//                    indicatorParams.setMargins(14, 0, 0, 0);
//                } else if(i == pics.length -1){
//                    indicatorParams.setMargins(0, 0, 14, 0);
//                    indicator.setBackgroundResource(R.drawable.icon_splash_indicator_normal);
//                }else {
//                    indicator.setBackgroundResource(R.drawable.icon_splash_indicator_normal);
//                }
            } else {

//                indicator.setBackgroundResource(R.drawable.icon_splash_last_indicator_normal);

            }
        }

        views.add(lastItem);


        // 设置数据
        //前面的views中没有数据 在前面的循环中才插入数据 而此时vpAdapter中已经有数据说明
        //初始化adapter的时候 参数传递是传引用
        viewPager.setAdapter(vpAdapter);

    }


    //改变是否第一次登录SP
    private void changeIsFirstInSp() {
        SharedPreferences preferences = getSharedPreferences(
                "first_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isFirstIn", false);
        editor.apply();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //改变指示器图标
//        for (int i = 0; i <= pics.length; i++) {
//            if (i < pics.length) {
//                if (position == i) {
//
//                    layout_splash_indicator.getChildAt(i).setBackgroundResource(R.drawable.icon_splash_indicator_selected);
//
//                } else {
//
//                    layout_splash_indicator.getChildAt(i).setBackgroundResource(R.drawable.icon_splash_indicator_normal);
//                }
//            } else {
//                if (position == pics.length) {
//                    indicator.setBackgroundResource(R.drawable.icon_splash_last_indicator_selected);
//                } else {
//                    indicator.setBackgroundResource(R.drawable.icon_splash_last_indicator_normal);
//                }
//
//            }
//        }

//        //最后一页添加点击事件
//        if (position == pics.length - 1) {
//            iv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        if (v == inBtn) {
            ChangeTabBean changeTabBean = new ChangeTabBean();
            changeTabBean.setTag(2);
            EventBus.getDefault().postSticky(changeTabBean);
            MyActivityManager.getInstance().startNextActivity(MainActivity.class);
            MyActivityManager.getInstance().finishSpecifiedActivity(SplashActivity.class);
        }
    }
}
