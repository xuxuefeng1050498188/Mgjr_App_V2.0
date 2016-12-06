package com.mgjr.view.profile.profileproperty;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.AniCircleProgress;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.PropertyIncomeCircleProgress;
import com.mgjr.model.bean.EventBusBean.ActionMapBean;
import com.mgjr.share.BaseFrament;
import com.mgjr.view.profile.CapitalDetails.CapitalDetailsActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/10/28.
 */

public class IncomeDetailsFragment extends BaseFrament implements View.OnClickListener {

    private LinearLayout circleView;
    private PropertyIncomeCircleProgress circleProgress;
    private AniCircleProgress aniCircleProgress;
    private ScrollView scrollview_income;
    private LinearLayout layout_eventincome, layout_tyjincome, layout_lcsincome, layout_jmgincome, layout_hqbincome, layout_mgbincome;


    private ImageView profile_income_indicator1, profile_income_indicator2;

    //    private ChartView chartView;
    private int XLength;        //X轴的长度
    private int XScale;     //X的刻度长度
    private int YScale;     //Y的刻度长度
    private int YPoint;     //原点的Y坐标
    private String[] XLabels = {"7-11", "7-12", "7-13", "7-14", "7-15", "7-16", "7-17"};
    private String[] Data = {"60", "80", "120", "160", "180", "200", "240"};
    private final int yLength = 330;

    private TextView tv_hqb_income, tv_jmg_income, tv_lcs_income, tv_tyj_income, tv_activity_income, tv_yestoday_income, tv_mgb_income;
    private TextView tv_tips;
    /**
     * 总收益
     */
    private Double totalIncome;
    /**
     * 活期宝收益
     */
    private Double yslxHqb;
    /**
     * 金芒果收益
     */
    private Double yslxLoan;
    /*理财师收益*/
    private Double financialProfit;
    /*体验金收益*/
    private Double yslxXsb;
    /**
     * 活动奖励
     */
    private Double activityReward;
    /*昨日收益**/
    private Double yesterdayIncome;
    /*芒果宝收益*/
    private Double yslxMgb;

    private int[] progresses;
    private Map<String, String> actionMap;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                aniCircleProgress.setVisibility(View.GONE);

            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.layout_profile_income_details, container, false);
        initViews(layout);
        EventBus.getDefault().register(this);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTextData();
        initCircleView();
        if (totalIncome != 0) {
            circleAni();
            tv_tips.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);
    }

    private void circleAni() {


        new Thread(new Runnable() {
            @Override
            public void run() {
//                int stutas = 100;
//                for (int i = 0; i <= 100; i++) {
//                    int pro = stutas--;
//                    aniCircleProgress.setCricleColor(Color.WHITE);
//                    aniCircleProgress.setProgress(pro);
//                    aniCircleProgress.postInvalidate();
//
//                    Message message = new Message();
//                    message.what = pro;
//                    handler.sendMessage(message);
//                    try {
//                        Thread.sleep(20);
//
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        }).start();


    }

    private void initViews(View layout) {
        handler = new Handler();
        circleProgress = (PropertyIncomeCircleProgress) layout.findViewById(R.id.circleProgress);
        aniCircleProgress = (AniCircleProgress) layout.findViewById(R.id.anicircleProgress);
        aniCircleProgress.setMax(100);

        scrollview_income = (ScrollView) layout.findViewById(R.id.scrollview_income);

        tv_hqb_income = (TextView) layout.findViewById(R.id.tv_hqb_income);
        tv_jmg_income = (TextView) layout.findViewById(R.id.tv_jmg_income);
        tv_lcs_income = (TextView) layout.findViewById(R.id.tv_lcs_income);
        tv_tyj_income = (TextView) layout.findViewById(R.id.tv_tyj_income);
        tv_activity_income = (TextView) layout.findViewById(R.id.tv_activity_income);
        tv_yestoday_income = (TextView) layout.findViewById(R.id.tv_yestoday_income);
        tv_mgb_income = (TextView) layout.findViewById(R.id.tv_mgb_income);
        tv_tips = (TextView) layout.findViewById(R.id.tv_tips);
        layout_eventincome = (LinearLayout) layout.findViewById(R.id.layout_eventincome);
        layout_tyjincome = (LinearLayout) layout.findViewById(R.id.layout_tyjincome);
        layout_lcsincome = (LinearLayout) layout.findViewById(R.id.layout_lcsincome);
        layout_jmgincome = (LinearLayout) layout.findViewById(R.id.layout_jmgincome);
        layout_hqbincome = (LinearLayout) layout.findViewById(R.id.layout_hqbincome);
        layout_mgbincome = (LinearLayout) layout.findViewById(R.id.layout_mgbincome);
        layout_mgbincome.setOnClickListener(this);
        layout_hqbincome.setOnClickListener(this);
        layout_jmgincome.setOnClickListener(this);
        layout_lcsincome.setOnClickListener(this);
        layout_tyjincome.setOnClickListener(this);
        layout_eventincome.setOnClickListener(this);
    }

    private void setTextData() {
        Bundle bundle = new Bundle();
        bundle = this.getActivity().getIntent().getExtras();

        totalIncome = bundle.getDouble("totalIncome");
        yslxHqb = bundle.getDouble("yslxHqb");
        yslxLoan = bundle.getDouble("yslxLoan");
        financialProfit = bundle.getDouble("financialProfit");
        yslxXsb = bundle.getDouble("yslxXsb");
        activityReward = bundle.getDouble("activityReward");
        yesterdayIncome = bundle.getDouble("yesterdayIncome");
        yslxMgb = bundle.getDouble("yslxMgb");

        tv_hqb_income.setText(new DecimalFormat("###,###,##0.00").format(yslxHqb) + "");
        tv_jmg_income.setText(new DecimalFormat("###,###,##0.00").format(yslxLoan) + "");
        tv_lcs_income.setText(new DecimalFormat("###,###,##0.00").format(financialProfit) + "");
        tv_tyj_income.setText(new DecimalFormat("###,###,##0.00").format(yslxXsb) + "");
        tv_activity_income.setText(new DecimalFormat("###,###,##0.00").format(activityReward) + "");
        if (yesterdayIncome == 0) {
            tv_yestoday_income.setText("0.00");
        } else {
            tv_yestoday_income.setText(new DecimalFormat("###,###,##0.00").format(yesterdayIncome) + "");
        }
        if (yslxMgb == 0) {
            layout_mgbincome.setVisibility(View.GONE);
        } else {
            tv_mgb_income.setText(new DecimalFormat("###,###,##0.00").format(yslxMgb) + "");
        }
    }

    private void initCircleView() {
        Double max = yslxHqb + yslxLoan + yslxMgb + yslxXsb + financialProfit + activityReward;
        circleProgress.setMax(max);
        circleProgress.setRoundWidth(40);
        circleProgress.setTextColor(Color.DKGRAY);
        circleProgress.setTextSize(32);
        circleProgress.setText(new DecimalFormat("###,###,##0.00").format(totalIncome));

//        progresses = new int[]{balance, withdrawlock, hqb, redeem, jmg, investLock,mgb};
        if (totalIncome != 0) {
            circleProgress.setMax(max);

        } else {
            aniCircleProgress.setCricleColor(Color.GRAY);
            tv_tips.setVisibility(View.VISIBLE);
            showNullCicle();
        }

        circleProgress.setTotalIncome(totalIncome);
        circleProgress.setYslxHqb(yslxHqb);
        circleProgress.setYslxLoan(yslxLoan);
        circleProgress.setFinancialProfit(financialProfit);
        circleProgress.setYslxXsb(yslxXsb);
        circleProgress.setActivityReward(activityReward);
        circleProgress.setMgbIncome(yslxMgb);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void eventBus(ActionMapBean actionMapBean ) {
        this.actionMap = actionMapBean.getActionMap();
    }


    private String getAciton(String text) {
        Set set = actionMap.entrySet();
        Iterator iterator = set.iterator();
        String key = "";
        while (iterator.hasNext()) {
            Map.Entry<String, String> enter = (Map.Entry<String, String>) iterator.next();
            if (enter.getValue().equals(text)) {
                key = enter.getKey();
            }
        }
        return key;
    }

    @Override
    public void onClick(View v) {
        if (v == layout_eventincome) {
            MyActivityManager.getInstance().startNextActivity(CapitalDetailsActivity.class, getAciton("活动奖励"));
        } else if (v == layout_tyjincome) {
            MyActivityManager.getInstance().startNextActivity(CapitalDetailsActivity.class, getAciton("体验金收益"));
        } else if (v == layout_lcsincome) {
            MyActivityManager.getInstance().startNextActivity(CapitalDetailsActivity.class, getAciton("理财师收益"));
        } else if (v == layout_jmgincome) {
            MyActivityManager.getInstance().startNextActivity(CapitalDetailsActivity.class, getAciton("金芒果收益"));
        } else if (v == layout_hqbincome) {
            MyActivityManager.getInstance().startNextActivity(CapitalDetailsActivity.class, getAciton("活期宝收益"));
        } else if (v == layout_mgbincome) {
            //芒果宝收益
        }
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                profile_income_indicator1.setBackgroundResource(R.drawable.indicator_incomedetails_vp_selected);
                profile_income_indicator2.setBackgroundResource(R.drawable.indicator_incomedetails_vp_normal);
            } else if (position == 1) {
                profile_income_indicator2.setBackgroundResource(R.drawable.indicator_incomedetails_vp_selected);
                profile_income_indicator1.setBackgroundResource(R.drawable.indicator_incomedetails_vp_normal);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void showNullCicle() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 100; i++) {
                    int pro = i++;
                    aniCircleProgress.setProgress(pro);
                    aniCircleProgress.postInvalidate();
                    aniCircleProgress.setCricleColor(Color.parseColor("#cccccc"));
                    Message message = new Message();
                    message.what = pro;
                    handler.sendMessage(message);
                    try {
                        Thread.sleep(20);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
