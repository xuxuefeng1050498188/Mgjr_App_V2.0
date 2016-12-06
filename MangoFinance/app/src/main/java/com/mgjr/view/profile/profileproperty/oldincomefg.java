/*
package com.mgjr.view.profile.profileproperty;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.AniCircleProgress;
import com.mgjr.Utils.Double2int;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.PropertyIncomeCircleProgress;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.share.BaseFrament;
import com.mgjr.view.profile.CapitalDetails.CapitalDetailsActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

*/
/**
 * Created by Administrator on 2016/7/20.
 *//*

public class a extends BaseFrament implements View.OnClickListener {

    private LayoutInflater inflater;
    private LinearLayout circleView;
    private PropertyIncomeCircleProgress circleProgress;
    private AniCircleProgress aniCircleProgress;
    private ScrollView scrollview_income;
    private LinearLayout layout_eventincome, layout_tyjincome, layout_lcsincome, layout_jmgincome, layout_hqbincome, layout_mgbincome;

    private ViewPager vp_profile_income;

    private ImageView profile_income_indicator1, profile_income_indicator2;

    private List<View> views;
    private IncomeDetailsAdapter adapter;

    private PropertyIncomeCircleProgress circleprogress;
    private ChartView chartView;
    private int XLength;        //X轴的长度
    private int XScale;     //X的刻度长度
    private int YScale;     //Y的刻度长度
    private int YPoint;     //原点的Y坐标
    private String[] XLabels = {"7-11", "7-12", "7-13", "7-14", "7-15", "7-16", "7-17"};
    private String[] Data = {"60", "80", "120", "160", "180", "200", "240"};
    private final int yLength = 330;

    private TextView tv_hqb_income, tv_jmg_income, tv_lcs_income, tv_tyj_income, tv_activity_income, tv_yestoday_income, tv_mgb_income;

    */
/**
     * 总收益
     *//*

    private Double totalIncome;
    */
/**
     * 活期宝收益
     *//*

    private Double yslxHqb;
    */
/**
     * 金芒果收益
     *//*

    private Double yslxLoan;
    */
/*理财师收益*//*

    private Double financialProfit;
    */
/*体验金收益*//*

    private Double yslxXsb;
    */
/**
     * 活动奖励
     *//*

    private Double activityReward;
    */
/*昨日收益**//*

    private Double yesterdayIncome;
    */
/*芒果宝收益*//*

    private Double yslxMgb;

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
        setTextData();
        initViewPager(layout);
        initCircleView();
        initChartView();
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        circleAni();
    }

    private void circleAni() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                int stutas = 100;
                for (int i = 0; i <= 100; i++) {
                    int pro = stutas--;

                    aniCircleProgress.setProgress(pro);
                    aniCircleProgress.postInvalidate();

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

    private void initViews(View layout) {
        handler = new Handler();
        inflater = (LayoutInflater) MainApplication.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        circleView = (LinearLayout) inflater.inflate(R.layout.layout_incomedetails_circleview, null);
        circleprogress = (PropertyIncomeCircleProgress) circleView.findViewById(R.id.circleProgress);
        aniCircleProgress = (AniCircleProgress) circleView.findViewById(R.id.anicircleProgress);
        aniCircleProgress.setMax(100);
        chartView = new ChartView(MainApplication.getContext());

        scrollview_income = (ScrollView) layout.findViewById(R.id.scrollview_income);


        tv_hqb_income = (TextView) layout.findViewById(R.id.tv_hqb_income);
        tv_jmg_income = (TextView) layout.findViewById(R.id.tv_jmg_income);
        tv_lcs_income = (TextView) layout.findViewById(R.id.tv_lcs_income);
        tv_tyj_income = (TextView) layout.findViewById(R.id.tv_tyj_income);
        tv_activity_income = (TextView) layout.findViewById(R.id.tv_activity_income);
        tv_yestoday_income = (TextView) layout.findViewById(R.id.tv_yestoday_income);
        tv_mgb_income = (TextView) layout.findViewById(R.id.tv_mgb_income);

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

        if (totalIncome == null) {
            aniCircleProgress.setBackgroundColor(Color.GRAY);
        }

        tv_hqb_income.setText(new DecimalFormat("###,###,##0.00").format(yslxHqb) + "元");
        tv_jmg_income.setText(new DecimalFormat("###,###,##0.00").format(yslxLoan) + "元");
        tv_lcs_income.setText(new DecimalFormat("###,###,##0.00").format(financialProfit) + "元");
        tv_tyj_income.setText(new DecimalFormat("###,###,##0.00").format(yslxXsb) + "元");
        tv_activity_income.setText(new DecimalFormat("###,###,##0.00").format(activityReward) + "元");
        tv_yestoday_income.setText(new DecimalFormat("###,###,##0.00").format(yesterdayIncome) + "元");
        if (yslxMgb == 0) {
            layout_mgbincome.setVisibility(View.GONE);
        } else {
            tv_mgb_income.setText(new DecimalFormat("###,###,##0.00").format(yslxMgb) + "元");
        }
    }


    private void initViewPager(View layout) {

        views = new ArrayList<>();
        views.add(circleView);
        views.add(chartView);

        adapter = new IncomeDetailsAdapter(views);
        vp_profile_income.setAdapter(adapter);
        vp_profile_income.addOnPageChangeListener(new MyPageChangeListener());
    }

    private void initChartView() {
        WindowManager wm = (WindowManager) MainApplication.getContext().getSystemService(MainApplication.getContext().WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        int width = outMetrics.widthPixels;
        int height = yLength;
        XLength = width;
        YPoint = height;

        XScale = XLength / 7;
        YScale = yLength / 7;

        chartView.SetInfo(XLength, XScale, YPoint, XLabels, yLength, YScale, Data);

    }

    private void initCircleView() {
        circleprogress.setMax(Double2int.double2Int(totalIncome));
        circleprogress.setRoundWidth(24);
        circleprogress.setTextColor(Color.DKGRAY);
        circleprogress.setTextSize(32);
        circleprogress.setText(totalIncome.toString());

        circleprogress.setTotalIncome(totalIncome);
        circleprogress.setYslxHqb(yslxHqb);
        circleprogress.setYslxLoan(yslxLoan);
        circleprogress.setFinancialProfit(financialProfit);
        circleprogress.setYslxXsb(yslxXsb);
        circleprogress.setActivityReward(activityReward);

    }

    @Override
    public void onClick(View v) {
        if (v == layout_eventincome) {
            MyActivityManager.getInstance().startNextActivity(CapitalDetailsActivity.class, "1000,1020");
        } else if (v == layout_tyjincome) {
            MyActivityManager.getInstance().startNextActivity(CapitalDetailsActivity.class, "107");
        } else if (v == layout_lcsincome) {
            MyActivityManager.getInstance().startNextActivity(CapitalDetailsActivity.class, "1010");
        } else if (v == layout_jmgincome) {
            MyActivityManager.getInstance().startNextActivity(CapitalDetailsActivity.class, "51");
        } else if (v == layout_hqbincome) {
            MyActivityManager.getInstance().startNextActivity(CapitalDetailsActivity.class, "200");
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
}
*/
