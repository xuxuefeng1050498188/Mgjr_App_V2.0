package com.mgjr.view.profile.profileproperty;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.AniCircleProgress;
import com.mgjr.Utils.PropertyCircleProgress;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.share.BaseFrament;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2016/7/20.
 */
public class PropertyDetailsFragment extends BaseFrament {

    private final Context context = MainApplication.getContext();
    private ScrollView srcollview_property;
    private PropertyCircleProgress circleProgress;
    private AniCircleProgress aniCircleProgress;
    private TextView tv_tips;
    /*账户余额列表**/
    private LinearLayout layout_userbalance;
    /**
     * 提现锁定金额
     */
    private LinearLayout layout_withdrawingAccount_list;
    /*活期宝*/
    private LinearLayout layout_hqb_list;
    /**
     * 赎回中金额
     */
    private LinearLayout layout_redeemingAccount_list;
    /**
     * 金芒果
     */
    private LinearLayout layout_jmg_list;
    /*投资锁定金额*/
    private LinearLayout layout_uninvest_list;
    /*芒果宝*/
    private LinearLayout layout_mgb_list;
    /*账户余额*/
    private TextView tv_user_amount;
    /*活期宝*/
    private TextView tv_huoqibao_amount;
    /*活期宝赎回中金额*/
    private TextView tv_redeem;
    /*金芒果*/
    private TextView tv_jinmango_amount;
    /*金芒果投资锁定金额*/
    private TextView tv_uninvest;
    /**
     * 提现锁定金额
     */
    private TextView tv_withdraw_lockaccount;
    /*芒果宝*/
    private TextView tv_mgb_amount;
    /**
     * 总资产
     */
    private Double totalAssets;
    /**
     * 账户余额
     */
    private Double accountBalance;
    /*提现锁定金额*/
    private Double withdrawingAmount;
    /**
     * 活期宝收益
     */
    private Double yslxHqb;
    private Double dsbjHqb;
    private Double dsbjLoan;

    /**
     * 赎回中金额
     */
    private Double redeemingjHqb;
    /**
     * 金芒果收益
     */
    private Double yslxLoan;
    /*投资锁定金额**/
    private Double frozenTenderAmount;
    /*金芒果*/
    private Double dsbjMgb;
    private int[] progresses;


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
        View layout = inflater.inflate(R.layout.layout_profile_property_details, container, false);
        initViews(layout);
        setTextData();
        initCircleProgress();

        return layout;

    }

    @Override
    public void onResume() {
        super.onResume();
        if (totalAssets != 0) {
            circleAni();
            tv_tips.setVisibility(View.GONE);
        }
    }

    private void initViews(View layout) {

        handler = new Handler();

        circleProgress = (PropertyCircleProgress) layout.findViewById(R.id.circleProgress);
        aniCircleProgress = (AniCircleProgress) layout.findViewById(R.id.anicircleProgress);
        aniCircleProgress.setMax(100);

        srcollview_property = (ScrollView) layout.findViewById(R.id.srcollview_property);

        layout_userbalance = (LinearLayout) layout.findViewById(R.id.layout_userbalance_list);
        layout_hqb_list = (LinearLayout) layout.findViewById(R.id.layout_hqb_list);
        layout_redeemingAccount_list = (LinearLayout) layout.findViewById(R.id.layout_redeemingAccount_list);
        layout_jmg_list = (LinearLayout) layout.findViewById(R.id.layout_jmg_list);
        layout_uninvest_list = (LinearLayout) layout.findViewById(R.id.layout_uninvest_list);
        layout_withdrawingAccount_list = (LinearLayout) layout.findViewById(R.id.layout_withdrawingAccount_list);
        layout_uninvest_list = (LinearLayout) layout.findViewById(R.id.layout_uninvest_list);
        layout_mgb_list = (LinearLayout) layout.findViewById(R.id.layout_mgb_list);

//        layout_jmg_list.setOnClickListener(this);
//        layout_redeemingAccount_list.setOnClickListener(this);
//        layout_hqb_list.setOnClickListener(this);
//        layout_userbalance.setOnClickListener(this);
//        layout_mgb_list.setOnClickListener(this);
//        layout_withdrawingAccount_list.setOnClickListener(this);
//        layout_uninvest_list.setOnClickListener(this);

        tv_user_amount = (TextView) layout.findViewById(R.id.tv_user_amount);
        tv_huoqibao_amount = (TextView) layout.findViewById(R.id.tv_huoqibao_amount);
        tv_redeem = (TextView) layout.findViewById(R.id.tv_redeem);
        tv_jinmango_amount = (TextView) layout.findViewById(R.id.tv_jinmango_amount);
        tv_uninvest = (TextView) layout.findViewById(R.id.tv_uninvest);
        tv_withdraw_lockaccount = (TextView) layout.findViewById(R.id.tv_withdraw_lockaccount);
        tv_mgb_amount = (TextView) layout.findViewById(R.id.tv_mgb_amount);
        tv_tips = (TextView) layout.findViewById(R.id.tv_tips);
    }

    private void setTextData() {

        Bundle bundle = this.getActivity().getIntent().getExtras();

        totalAssets = bundle.getDouble("totalAssets");
        accountBalance = bundle.getDouble("accountBalance");
        withdrawingAmount = bundle.getDouble("withdrawingAmount");
//        yslxHqb = bundle.getDouble("yslxHqb");
        redeemingjHqb = bundle.getDouble("redeemingjHqb");
        yslxLoan = bundle.getDouble("yslxLoan");
        frozenTenderAmount = bundle.getDouble("frozenTenderAmount");
        dsbjMgb = bundle.getDouble("dsbjMgb");
        dsbjHqb = bundle.getDouble("dsbjHqb");
        dsbjLoan = bundle.getDouble("dsbjLoan");

        //判断是否有体现锁定金额
        if (accountBalance == null) {
            tv_user_amount.setText("0.00");
        } else {
            tv_user_amount.setText(new DecimalFormat("###,###,##0.00").format(accountBalance) + "");
        }
        if (withdrawingAmount == 0.0) {
            layout_withdrawingAccount_list.setVisibility(View.GONE);
        } else {
            layout_withdrawingAccount_list.setVisibility(View.VISIBLE);
            tv_withdraw_lockaccount.setText(new DecimalFormat("###,###,##0.00").format(withdrawingAmount) + "");
        }
        //判断是否有赎回中金额
        if (dsbjHqb != null && redeemingjHqb != null) {
            tv_huoqibao_amount.setText(new DecimalFormat("###,###,##0.00").format(dsbjHqb - redeemingjHqb) + "");
        } else {
            tv_huoqibao_amount.setText("0.00");
        }
        if (redeemingjHqb.equals(0.0)) {
            layout_redeemingAccount_list.setVisibility(View.GONE);
        } else {
            layout_redeemingAccount_list.setVisibility(View.VISIBLE);
            tv_redeem.setText(new DecimalFormat("###,###,##0.00").format(redeemingjHqb) + "");
        }
        //判断是否有投资锁定金额
        if (dsbjLoan == null) {
            tv_jinmango_amount.setText("0.00");
        } else {
            tv_jinmango_amount.setText(new DecimalFormat("###,###,##0.00").format(dsbjLoan) + "");
        }
        if (frozenTenderAmount == 0.0) {
            layout_uninvest_list.setVisibility(View.GONE);
        } else {
            layout_uninvest_list.setVisibility(View.VISIBLE);
            tv_uninvest.setText(new DecimalFormat("###,###,##0.00").format(frozenTenderAmount) + "");
        }
        /*芒果宝*/
        if (dsbjMgb == 0 || dsbjMgb == null) {
            layout_mgb_list.setVisibility(View.GONE);
        } else {
            layout_mgb_list.setVisibility(View.VISIBLE);
            tv_mgb_amount.setText(new DecimalFormat("###,###,##0.00").format(dsbjMgb) + "");
        }

    }

    private void circleAni() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int stutas = 100;
//                for (int i = 0; i <= 100; i++) {
//                    int pro = stutas--;
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
    private void initCircleProgress() {
        if (totalAssets != 0.0) {
            circleProgress.setMax(totalAssets);
        } else {
            aniCircleProgress.setCricleColor(Color.GRAY);
            tv_tips.setVisibility(View.VISIBLE);
            showNullCicle();
        }
        circleProgress.setBalance(accountBalance);
        circleProgress.setWithdrawlock(withdrawingAmount);
        circleProgress.setHqb(dsbjHqb - redeemingjHqb);
        circleProgress.setRedeem(redeemingjHqb);
        circleProgress.setJmg(dsbjLoan);
        circleProgress.setInvestLock(frozenTenderAmount);
        circleProgress.setMgb(dsbjMgb);

        circleProgress.setRoundWidth(40);
        circleProgress.setTextColor(Color.DKGRAY);
        circleProgress.setTextSize(32);
        if (totalAssets != null) {
            circleProgress.setText(new DecimalFormat("###,###,##0.00").format(totalAssets));
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
