package com.mgjr.view.home;

import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mgjr.R;
import com.mgjr.Utils.ScreenUtils;
import com.mgjr.mangofinance.MainApplication;

/**
 * Created by Administrator on 2016/9/19.
 */
public class HomepageViewPagerListener implements ViewPager.OnPageChangeListener {

    private ImageView img1, img2;
    private int x = 1;

    public HomepageViewPagerListener(ImageView img1, ImageView img2) {
        this.img1 = img1;
        this.img2 = img2;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) img1.getLayoutParams();
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) img2.getLayoutParams();
        if (position == 0) {
            int w1 = ScreenUtils.dipToPx(MainApplication.getContext(), 50);
            int w2 = ScreenUtils.dipToPx(MainApplication.getContext(), 25);
//            LogUtil.d("OffSet : " + positionOffset);

            params1.width = (int) (w1 - (w1 * positionOffset) / 2) ;
            img1.setLayoutParams(params1);
//            LogUtil.d("W1 : " + ((w1 - (w1 * positionOffset) / 2) ) + "");

            params2.width = (int) (w2 + (w1 * positionOffset) / 2) ;
            img2.setLayoutParams(params2);
//            LogUtil.d("W2 : " + ((w2 + (w1 * positionOffset) / 2) ) + "");
        }
//        else if (position == 1) {
//            int w1 = ScreenUtils.dipToPx(MainApplication.getContext(), 20);
//            int w2 = ScreenUtils.dipToPx(MainApplication.getContext(), 50);
//            params1.width = (int) (w1 + (w2 * (positionOffset)) / 2) ;
//            img1.setLayoutParams(params1);
//            LogUtil.d("W1 : " + ((w1 - (w2 * (positionOffset)) / 2) + ""));
//
//            params2.width = (int) (w2 - (w2 * (positionOffset)) / 2) ;
//            img2.setLayoutParams(params2);
//            LogUtil.d("W1 : " + ((w1 - (w2 - (w2 * (positionOffset)) / 2) + "")));
//        }
    }

    @Override
    public void onPageSelected(int position) {
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) img1.getLayoutParams();
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) img2.getLayoutParams();
        if (position == 1) {
            img2.setBackgroundResource(R.drawable.qindicator1_homepage);
            img1.setBackgroundResource(R.drawable.qindicator2_homepage);
            params1.width = ScreenUtils.dipToPx(MainApplication.getContext(), 25);
            params2.width = ScreenUtils.dipToPx(MainApplication.getContext(), 50);
            img1.setLayoutParams(params1);
            img2.setLayoutParams(params2);

        } else if (position == 0) {
            img1.setBackgroundResource(R.drawable.qindicator1_homepage);
            img2.setBackgroundResource(R.drawable.qindicator2_homepage);
            params1.width = ScreenUtils.dipToPx(MainApplication.getContext(), 50);
            params2.width = ScreenUtils.dipToPx(MainApplication.getContext(), 25);
            img1.setLayoutParams(params1);
            img2.setLayoutParams(params2);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
