package com.mgjr.view.invester;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgjr.R;

/**
 * Created by xuxuefeng on 2016/8/8.
 */
public class InvestBuyAndRedemptionRulesViewPagerAdapter extends PagerAdapter {

    /**
     * 标题
     */
    String[] titles = {"1、买入须知", "2、赎回须知"};
    String[] pagetitles = {"买入须知", "赎回须知"};
    /**
     * 内容
     */
    String[] contents = {"内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容", "内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容"};
    private TextView tv_title;
    private TextView tv_content;

    @Override
    public CharSequence getPageTitle(int position) {
        return pagetitles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(container.getContext(), R.layout.layout_buy_and_redemption_rules, null);
        tv_title = (TextView) view.findViewById(R.id.tv_buy_and_redemption_rules_title);
        tv_content = (TextView) view.findViewById(R.id.tv_buy_and_redemption_rules_content);
        tv_title.setText(titles[position]);
        tv_content.setText(contents[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
