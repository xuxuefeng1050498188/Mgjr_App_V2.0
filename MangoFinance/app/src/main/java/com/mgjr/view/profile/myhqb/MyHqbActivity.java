package com.mgjr.view.profile.myhqb;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.SPUtils;
import com.mgjr.model.bean.MyHqbBean;
import com.mgjr.presenter.impl.MyHqbPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.view.common.CommonWebViewActivity;
import com.mgjr.view.invester.InvestConfirmActivity;
import com.mgjr.view.invester.InvestProductDetailActivity;
import com.mgjr.view.listeners.ViewListener;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/1.
 */
public class MyHqbActivity extends ActionbarActivity implements View.OnClickListener, ViewListener<MyHqbBean> {

    //活期余额、累计收益、赎回中金额  动画文字 、 无收益说明
    private TextView tv_myhqb_balance, tv_myhqb_allincome, tv_myhqb_redeemingaccount, tv_myhqb_presentation, tv_noincome_tips;
    //有收益时 ：昨日收益、累计赚取收益天数
    private TextView tv_myhqb_yesincome, tv_myhqb_days, tv_projects_count;
    private int tenderCount;  //持有项目数
    private String dsbjHqb;  //活期余额
    private String totalTenderAl;
    private Double hbqYesterdayIncome;  //昨日收益
    private Double yslxHqb;  //累计收益
    private Double redeemingAmount;  //赎回中金额
    private String tipsMessage;   //您已累计赚取收益1天
    private String hbqCode;  //code
    private String showMessage;
    //持有中项目、已赎回项目
    private RelativeLayout layout_runningproject_list, layout_redeemedporject_list;
    //活期余额,累计收益
    private RelativeLayout layout_dsbjHqb, layout_yslxHqb;
    //赎回按钮、转入按钮、赎回中金额
    private LinearLayout layout_redeembtn, layout_passinbtn, layout_redeeming_balance;
    //无收益界面
    private LinearLayout layout_myhqb_noincome;
    private MyHqbPresenterImpl myHqbPresenter;
    private MyHqbBean myHqbBean;
    private ImageView imgbtn_noincome_btn_instruction;   //无收益提示按钮
    private boolean isOpen = false;
    private PopupWindow loadingPopupWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_layout_myhqb, this);
        initAcitonbar();
        initViews();
    }

    private void initAcitonbar() {
        this.actionbar.setBackgroundColor(Color.parseColor("#46bdf8"));
        this.actionbar.setCenterTextView("我的活期宝");
        this.actionbar.setLeftImageView(R.drawable.invest_left_arrow, this);
        this.actionbar.centerTextView.setTextColor(Color.WHITE);
        this.actionbar.setRightImageView(R.drawable.my_jmg_common_question_bg, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestNetWork();
        setTopTextAni();
    }

    @Override
    protected void onPause() {
        super.onPause();
        tv_myhqb_presentation.clearAnimation();
    }

    private void initViews() {
        myHqbPresenter = new MyHqbPresenterImpl(this);

        layout_runningproject_list = (RelativeLayout) findViewById(R.id.layout_runningproject_list);
        layout_redeemedporject_list = (RelativeLayout) findViewById(R.id.layout_redeemedporject_list);
        layout_yslxHqb = (RelativeLayout) findViewById(R.id.layout_yslxHqb);
        layout_dsbjHqb = (RelativeLayout) findViewById(R.id.layout_dsbjHqb);

        layout_redeembtn = (LinearLayout) findViewById(R.id.layout_redeembtn);
        layout_passinbtn = (LinearLayout) findViewById(R.id.layout_passinbtn);
        layout_myhqb_noincome = (LinearLayout) findViewById(R.id.layout_myhqb_noincome);
        layout_redeeming_balance = (LinearLayout) findViewById(R.id.layout_redeeming_balance);

        tv_myhqb_balance = (TextView) findViewById(R.id.tv_myhqb_balance);
        tv_myhqb_allincome = (TextView) findViewById(R.id.tv_myhqb_allincome);
        tv_myhqb_redeemingaccount = (TextView) findViewById(R.id.tv_myhqb_redeemingaccount);
        tv_myhqb_yesincome = (TextView) findViewById(R.id.tv_myhqb_yesincome);
        tv_myhqb_days = (TextView) findViewById(R.id.tv_myhqb_days);
        tv_myhqb_presentation = (TextView) findViewById(R.id.tv_myhqb_presentation);
        tv_noincome_tips = (TextView) findViewById(R.id.tv_noincome_tips);
        tv_projects_count = (TextView) findViewById(R.id.tv_projects_count);

        imgbtn_noincome_btn_instruction = (ImageView) findViewById(R.id.imgbtn_noincome_btn_instruction);

        layout_redeembtn.setOnClickListener(this);
        layout_passinbtn.setOnClickListener(this);
        layout_runningproject_list.setOnClickListener(this);
        layout_redeemedporject_list.setOnClickListener(this);
        imgbtn_noincome_btn_instruction.setOnClickListener(this);
        tv_myhqb_presentation.setOnClickListener(this);
        tv_myhqb_yesincome.setOnClickListener(this);
        layout_dsbjHqb.setOnClickListener(this);
        layout_yslxHqb.setOnClickListener(this);
        layout_redeeming_balance.setOnClickListener(this);
    }

    private void requestNetWork() {
        int id = (int) SPUtils.get(this, "LOGIN", "id", 0);
        String mid = id + "";
        Map<String, String> params = new HashMap<>();
        params.put("mid", mid);
        myHqbPresenter.sendRequest(params, null);
    }

    @Override
    public void onClick(View v) {
        if (v == actionbar.leftImageView) {
//            popActivity();
            MyActivityManager.getInstance().popCurrentActivity();
        } else if (v == actionbar.rightImageView) {
            MyActivityManager.getInstance().startNextActivity(CommonWebViewActivity.class, "hqbProblem");
        } else if (v == tv_myhqb_yesincome) {
            //昨日收益
            MyActivityManager.getInstance().startNextActivity(MyHqbTransactionDetailActivity.class, "收益", "hqb");
        } else if (v == layout_redeembtn) {   //赎回按钮
            if (showMessage != null && showMessage.equalsIgnoreCase("OK")) {
                CommonToastUtils.showToast(this, "您还未投资过,没有可赎回项目");
            } else if (tenderCount == 0) {
                CommonToastUtils.showToast(this, "您暂无可赎回项目");
            } else {
                MyActivityManager.getInstance().startNextActivity(MyHqbRunningProjectActivity.class, "redeem");
            }
        } else if (v == layout_passinbtn) {  //转入按钮
            MyActivityManager.getInstance().startNextActivity(InvestConfirmActivity.class, hbqCode, "0");
        } else if (v == layout_runningproject_list) {
            MyActivityManager.getInstance().startNextActivity(MyHqbRunningProjectActivity.class, "single");
        } else if (v == layout_redeemedporject_list) {
            MyActivityManager.getInstance().startNextActivity(MyHqbRunningProjectActivity.class, "redeemed");
            SPUtils.put(MyHqbActivity.this, "TempIntent", "intentResource", "redeemedProjects");
        } else if (v == imgbtn_noincome_btn_instruction) {
            if (!isOpen) {
                tv_noincome_tips.setVisibility(View.VISIBLE);

            } else {
                tv_noincome_tips.setVisibility(View.INVISIBLE);
            }
            isOpen = !isOpen;
        } else if (v == tv_myhqb_presentation) {
            if (hbqCode != null) {
                MyActivityManager.getInstance().startNextActivity(InvestProductDetailActivity.class, hbqCode, "0");
            }
        } else if (v == layout_yslxHqb) {
//            MyActivityManager.getInstance().startNextActivity(CapitalDetailsActivity.class, "200",dsbjHqb,totalTenderAl,"hqb");
            MyActivityManager.getInstance().startNextActivity(MyHqbTransactionDetailActivity.class, "收益", "hqb");
        } else if (v == layout_dsbjHqb) {
            MyActivityManager.getInstance().startNextActivity(MyHqbTransactionDetailActivity.class, "转入", "hqb");
        } else if (v == layout_redeeming_balance) {
            MyActivityManager.getInstance().startNextActivity(MyHqbTransactionDetailActivity.class, "赎回", "hqb");
        }
    }

    private void setTopTextAni() {
        final TranslateAnimation scrollOutAnimation = new TranslateAnimation(0.0f, 0.0f, -200f, tv_myhqb_presentation.getTranslationY());
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
        tv_myhqb_presentation.startAnimation(scrollOutAnimation);

    }

    @Override
    public void showLoading() {
//        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_layout_myhqb, null);
//        loadingPopupWindow = PopwUtils.showLoadingPopw(this, rootView);
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
//        PopwUtils.dismissLoadingPopw(loadingPopupWindow);
    }

    @Override
    public void showError() {
        dismissLoadingDialog();
        showSystemError();
    }

    @Override
    public void showError(OnPresenterListener listener, MyHqbBean myHqbBean) {
//        PopwUtils.dismissLoadingPopw(loadingPopupWindow);
        dismissLoadingDialog();
        CommonToastUtils.showToast(this, myHqbBean.getMsg());
    }

    @Override
    public void responseData(OnPresenterListener listener, MyHqbBean myHqbBean) {
        this.myHqbBean = myHqbBean;
        dsbjHqb = new DecimalFormat("###,###,##0.00").format(myHqbBean.getDsbjHqb());
        hbqYesterdayIncome = myHqbBean.getHbqYesterdayIncome();
        yslxHqb = myHqbBean.getYslxHqb();
        redeemingAmount = myHqbBean.getRedeemingAmount();
        tipsMessage = myHqbBean.getTipsMessage();
        hbqCode = myHqbBean.getHbqCode();
        showMessage = myHqbBean.getShowMessage();
        totalTenderAl = new DecimalFormat("###,###,##0.00").format(myHqbBean.getTotalTenderAl());
        tenderCount = myHqbBean.getTenderCount();
        setTextData();
    }

    private void setTextData() {
        Double hqbAmount = myHqbBean.getDsbjHqb() - redeemingAmount;
        tv_myhqb_balance.setText(new DecimalFormat("###,###,##0.00").format(hqbAmount));
        tv_myhqb_allincome.setText(new DecimalFormat("###,###,##0.00").format(yslxHqb));
        if (redeemingAmount == 0) {
            layout_redeeming_balance.setVisibility(View.INVISIBLE);
        } else {
            layout_redeeming_balance.setVisibility(View.VISIBLE);
            tv_myhqb_redeemingaccount.setText(new DecimalFormat("###,###,##0.00").format(redeemingAmount));
        }
        if (hbqYesterdayIncome == 0.0) {
            tv_myhqb_yesincome.setText("暂无收益");
//            tv_myhqb_yesincome.setClickable(false);
            layout_myhqb_noincome.setVisibility(View.VISIBLE);
        } else {
            tv_myhqb_yesincome.setText(new DecimalFormat("###,###,##0.00").format(hbqYesterdayIncome));
        }
        if (tipsMessage != null) {
            tv_myhqb_days.setText(tipsMessage);
        }
        if (showMessage == null) {
            tv_myhqb_presentation.clearAnimation();
            tv_myhqb_presentation.setVisibility(View.INVISIBLE);
        }

        if (hbqCode == null) {
            tv_myhqb_days.setText("您的钱包还没开始赚钱呢~");
        }
        if (tenderCount == 0) {
            tv_projects_count.setVisibility(View.INVISIBLE);
        } else {
            tv_projects_count.setText(tenderCount + "个项目");
            tv_projects_count.setVisibility(View.VISIBLE);
        }
    }
}
