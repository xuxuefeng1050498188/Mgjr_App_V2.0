package com.mgjr.view.invester;

import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.mgjr.R;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.PagerSlidingTabStrip;

public class InvestBuyAndRedemptionRulesActivity extends ActionbarActivity {

    private ViewPager viewpager_invest_buy_and_redemption;
    private PagerSlidingTabStrip pager_sliding_tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_buy_and_redemption_rules, this);
        initActionBar();
        viewpager_invest_buy_and_redemption = (ViewPager) findViewById(R.id.viewpager_invest_buy_and_redemption);
        viewpager_invest_buy_and_redemption.setAdapter(new InvestBuyAndRedemptionRulesViewPagerAdapter());
        pager_sliding_tabs = (PagerSlidingTabStrip) findViewById(R.id.pager_sliding_tabs);
        pager_sliding_tabs.setViewPager(viewpager_invest_buy_and_redemption);
       /* pager_sliding_tabs.setTabViewFactory(new PagerSlidingTabStrip.TabViewFactory() {
            @Override
            public void addTabs(ViewGroup parent, int currentItemPosition) {
                TextView textView = null;
                for (int i = 0; i < 2; i++) {
                    textView = new TextView(parent.getContext());
                    if (i == 0) {
                        textView.setText("买入须知");
                    } else if (i == 1) {
                        textView.setText("赎回须知");
                    }
                    parent.addView(textView);
                }
            }
        });*/

    }

    private void initActionBar() {
        actionbar.setCenterTextView("买入、赎回规则");
    }
}
