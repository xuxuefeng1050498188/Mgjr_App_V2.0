package com.mgjr.view.common;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/2.
 */

public class SplashViewAdapter extends PagerAdapter {


    //界面列表
    private ArrayList<View> views;

    public SplashViewAdapter(ArrayList<View> views) {
        this.views = views;
    }


    //获得当前界面总数
    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        return 0;
    }

    //初始化position位置的界面
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(views.get(position), 0);
        return views.get(position);
    }

    //判断是否由对象生成界面
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(views.get(position));
    }


    /**
     * @param args
     */
    public static void main(String[] args) {

    }
}
