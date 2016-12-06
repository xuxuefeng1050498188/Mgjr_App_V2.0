package com.mgjr.view.profile.myJmg;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.SPUtils;
import com.mgjr.mangofinance.MainActivity;
import com.mgjr.model.bean.MyJmgBean;
import com.mgjr.presenter.impl.MyJmgPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.view.common.CommonWebViewActivity;
import com.mgjr.view.listeners.ViewListener;
import com.mgjr.view.profile.myhqb.MyHqbTransactionDetailActivity;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/1.
 */
public class MyJmgActivity extends ActionbarActivity implements View.OnClickListener, ViewListener<MyJmgBean> {
    //顶部动画区域、待收益区域、赎回中金额区域
    private LinearLayout layout_myjmg_topani, layout_myjmg_waitincome, layout_myjmg_redeem;
    //投资中项目、还款日历、已结束项目
    private RelativeLayout layout_runningproject_list, layout_finishedproject_list;
    private TextView tv_amount_tips;
    private String investType;

    //昨日收益，距下个还款日数，活期余额，累积收益，赎回中金额，金芒果详情入口
    private TextView tv_myjmg_waitincome, tv_myjmg_days, tv_myjmg_balance, tv_myjmg_allincome, tv_myjmg_redeemingaccount, tvbtn_myjmg_showdetail;
    private String dsbjLoan;//投资金额
    private String totalTenderAmount; //累计投资金额
    private MyJmgPresenterImpl myJmgPresenterImpl;
    private MyJmgBean myJmgBean;
    private TextView tv_investing_tender_count;
    private LinearLayout ll_myhqb_without_income;
    private RelativeLayout rl_invest_money;
    private RelativeLayout rl_sum_invest_profit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_layout_myjmg, this);
        initActionBar();
        myJmgPresenterImpl = new MyJmgPresenterImpl(this);
        initViews();
    }

    private void initActionBar() {
        actionbar.leftImageView.setImageResource(R.drawable.invest_left_arrow);
        actionbar.setCenterTextView("我的金芒果");
        actionbar.setBackgroundColor(Color.parseColor("#FFA800"));
        actionbar.centerTextView.setTextColor(Color.WHITE);
        actionbar.setRightImageView(R.drawable.my_jmg_common_question_bg, this);
    }




    private void networkRequest() {
        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        necessaryParams.put("mid", "" + SPUtils.get(this, "LOGIN", "id", 0));
        myJmgPresenterImpl.sendRequest(necessaryParams, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTopTextAni();
        networkRequest();
    }

    @Override
    protected void onPause() {
        super.onPause();
        layout_myjmg_topani.clearAnimation();
    }

    private void initViews() {
        //投资金额
        rl_invest_money = (RelativeLayout) findViewById(R.id.rl_invest_money);
        rl_invest_money.setOnClickListener(this);
        rl_sum_invest_profit = (RelativeLayout) findViewById(R.id.rl_sum_invest_profit);
        rl_sum_invest_profit.setOnClickListener(this);
//        ll_myhqb_without_income = (LinearLayout) findViewById(R.id.ll_myhqb_without_income);
        layout_myjmg_topani = (LinearLayout) findViewById(R.id.layout_myjmg_topani);
        layout_myjmg_topani.setOnClickListener(this);
        layout_myjmg_waitincome = (LinearLayout) findViewById(R.id.layout_myjmg_waitincome);
        layout_myjmg_redeem = (LinearLayout) findViewById(R.id.layout_myjmg_redeem);

        layout_runningproject_list = (RelativeLayout) findViewById(R.id.layout_runningproject_list);
        layout_finishedproject_list = (RelativeLayout) findViewById(R.id.layout_finishedproject_list);

        tv_myjmg_waitincome = (TextView) findViewById(R.id.tv_myjmg_waitincome);
        tv_myjmg_days = (TextView) findViewById(R.id.tv_myjmg_days);
        tv_myjmg_balance = (TextView) findViewById(R.id.tv_myjmg_balance);
        tv_myjmg_redeemingaccount = (TextView) findViewById(R.id.tv_myjmg_redeemingaccount);
        tvbtn_myjmg_showdetail = (TextView) findViewById(R.id.tvbtn_myjmg_showdetail);
        tv_myjmg_allincome = (TextView) findViewById(R.id.tv_myjmg_allincome);
        tv_investing_tender_count = (TextView) findViewById(R.id.tv_investing_tender_count);
        tv_amount_tips = (TextView) findViewById(R.id.tv_amount_tips);

        layout_runningproject_list.setOnClickListener(this);
        layout_finishedproject_list.setOnClickListener(this);
        layout_myjmg_waitincome.setOnClickListener(this);
        layout_myjmg_redeem.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == layout_finishedproject_list) {
            investType = "1";
            SPUtils.put(this,"TempIntent","InResourse","JmgFinished");
            MyActivityManager.getInstance().startNextActivity(MyJmgInvestingAndFinishedProjectsActivity.class, investType);
        } else if (v == layout_runningproject_list) {
            investType = "0";
            SPUtils.put(this,"TempIntent","InResourse","JmgInvesting");
            MyActivityManager.getInstance().startNextActivity(MyJmgInvestingAndFinishedProjectsActivity.class, investType);
        } else if (v == layout_myjmg_waitincome) {
            MyActivityManager.getInstance().startNextActivity(MyHqbTransactionDetailActivity.class, "收益", "jmg");
        } else if (v == layout_myjmg_redeem) {
            MyActivityManager.getInstance().startNextActivity(MyHqbTransactionDetailActivity.class, "投资", "jmg");
        } else if (v == layout_myjmg_topani) {
            //跳转到理财产品首页
            MyActivityManager.getInstance().finishSpecifiedActivity(MyJmgActivity.class);
            MyActivityManager.getInstance().startNextActivity(MainActivity.class, "1");
        } else if (v == rl_sum_invest_profit) {
            MyActivityManager.getInstance().startNextActivity(MyHqbTransactionDetailActivity.class, "收益", "jmg");
        } else if (v == rl_invest_money) {
            MyActivityManager.getInstance().startNextActivity(MyHqbTransactionDetailActivity.class, "投资", "jmg");
        } else if (v == actionbar.rightImageView) {
            MyActivityManager.getInstance().startNextActivity(CommonWebViewActivity.class, "jmgNoviceProblem");
        }

    }

    private void setTopTextAni() {

        final TranslateAnimation scrollOutAnimation = new TranslateAnimation(0.0f, 0.0f, -200f, layout_myjmg_topani.getTranslationY());
        scrollOutAnimation.setStartOffset(2000);
        scrollOutAnimation.setFillAfter(true);
        scrollOutAnimation.setRepeatCount(Animation.INFINITE);
        scrollOutAnimation.setRepeatMode(Animation.REVERSE);
        scrollOutAnimation.setDuration(1000);
        scrollOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                scrollOutAnimation.setStartOffset(5000);
            }
        });
        layout_myjmg_topani.startAnimation(scrollOutAnimation);

    }


    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @Override
    public void showError() {
        dismissLoadingDialog();
        showSystemError();
    }

    @Override
    public void showError(OnPresenterListener listener, MyJmgBean myJmgBean) {
        dismissLoadingDialog();
        CommonToastUtils.showToast(this, myJmgBean.getMsg());
    }

    @Override
    public void responseData(OnPresenterListener listener, MyJmgBean myJmgBean) {
        this.myJmgBean = myJmgBean;

        if (myJmgBean.getTenderCount() == 0){
            layout_myjmg_topani.setVisibility(View.VISIBLE);
        }else {
            layout_myjmg_topani.setVisibility(View.GONE);
        }

        dsbjLoan = new DecimalFormat("###,###,##0.00").format(myJmgBean.getDsbjLoan());
        totalTenderAmount = new DecimalFormat("###,###,##0.00").format(myJmgBean.getTotalTenderAmount());
        setData();
    }

    /**
     * 设置界面数据
     */
    private void setData() {
        if (myJmgBean.getDslxLoan() == 0) {
//            ll_myhqb_without_income.setVisibility(View.VISIBLE);
            tv_myjmg_waitincome.setText("暂无收益");
//            layout_myjmg_waitincome.setVisibility(View.GONE);
        } else {
            tv_myjmg_waitincome.setText("" + new DecimalFormat("###,###,##0.00").format(myJmgBean.getDslxLoan()));
        }
        tv_myjmg_days.setText(myJmgBean.getTipsMessage());
        double dsbjLoan = myJmgBean.getDsbjLoan();
        tv_myjmg_balance.setText("" + new DecimalFormat("###,###,##0.00").format(dsbjLoan));
        tv_myjmg_allincome.setText("" + new DecimalFormat("###,###,##0.00").format(myJmgBean.getYslxLoan()));
        int tenderCount = myJmgBean.getTenderCount();
        if (tenderCount == 0) {
            tv_investing_tender_count.setVisibility(View.INVISIBLE);
        } else {
            tv_investing_tender_count.setText(tenderCount + "个项目");
            tv_investing_tender_count.setTextColor(Color.parseColor("#333333"));
            tv_investing_tender_count.setTextSize(12);
        }

        if (myJmgBean.getFrozenTenderAmount() == 0) {
            layout_myjmg_redeem.setVisibility(View.GONE);
        } else {
            tv_amount_tips.setText("投资锁定金额");
            tv_myjmg_redeemingaccount.setText("" + new DecimalFormat("###,###,##0.00").format(myJmgBean.getFrozenTenderAmount()));
        }


    }
}
