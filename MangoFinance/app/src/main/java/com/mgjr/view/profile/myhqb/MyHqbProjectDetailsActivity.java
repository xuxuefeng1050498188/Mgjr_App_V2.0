package com.mgjr.view.profile.myhqb;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.SPUtils;
import com.mgjr.model.bean.MyHqbProjectDetailsBean;
import com.mgjr.presenter.impl.MyHqbProjectDetailsPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.view.listeners.ViewListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/11.
 */
public class MyHqbProjectDetailsActivity extends ActionbarActivity implements ViewListener<MyHqbProjectDetailsBean>, View.OnClickListener {
    private LinearLayout layout_crate;
    private ListView lv_hqb_runningproject_details;   //记录列表
    private TextView tv_hqbdetails_yestodayincome; //昨日收益
    private Double yestodayincome;
    private TextView tv_hqbdetails_balance;  //持有金额
    private Double balance;
    private TextView tv_hqbdetails_incomed;   //已获收益
    private Double incomed;
    private TextView tv_hqbdetails_rate;   //年化
    private TextView tv_rate_type;
    private TextView tv_hqbdetails_crate;  //赎回时当前年化
    private Double rate;
    private TextView tv_hqbdetails_passtime;  //转入时间
    private String passtime;
    private TextView tv_hqbdetails_holdtime;   //持有时间
    private String holdtime;
    private String tenderId;
    private MyHqbProjectDetailsPresenterImpl myHqbProjectDetailsPresenter;
    private TextView tvbtn_hqbdetails_redeem;  //赎回按钮
    private int id;
    private PopupWindow loadingPopupWindow;
    private MyHqbProjectDetailsBean myHqbProjectDetailsBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_layout_myhqb_runningprojectdetails, this);
        actionbar.setBackgroundColor(Color.parseColor("#33b5ee"));
        actionbar.setLeftImageView(R.drawable.invest_left_arrow, this);
        actionbar.centerTextView.setTextColor(Color.WHITE);
        String type = getIntent().getStringExtra("rate");
        if (type.equalsIgnoreCase("running")) {
            SPUtils.put(MyHqbProjectDetailsActivity.this, "TempIntent", "intentResource", "single");
        } else if (type.equalsIgnoreCase("redeemed")) {
            SPUtils.put(MyHqbProjectDetailsActivity.this, "TempIntent", "intentResource", "redeemed");
        }
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestNewWorkData();
    }


    @Override
    protected void onPause() {
        super.onPause();
//        SPUtils.clear(MyHqbProjectDetailsActivity.this,"TempIntent");
    }

    private void initViews() {
        myHqbProjectDetailsPresenter = new MyHqbProjectDetailsPresenterImpl(this);
        lv_hqb_runningproject_details = (ListView) findViewById(R.id.lv_hqb_runningproject_details);
        layout_crate = (LinearLayout) findViewById(R.id.layout_crate);
        tv_hqbdetails_yestodayincome = (TextView) findViewById(R.id.tv_hqbdetails_yestodayincome);
        tv_hqbdetails_balance = (TextView) findViewById(R.id.tv_hqbdetails_balance);
        tv_hqbdetails_incomed = (TextView) findViewById(R.id.tv_hqbdetails_incomed);
        tv_hqbdetails_rate = (TextView) findViewById(R.id.tv_hqbdetails_rate);
        tv_rate_type = (TextView) findViewById(R.id.tv_rate_type);
        tv_hqbdetails_passtime = (TextView) findViewById(R.id.tv_hqbdetails_passtime);
        tv_hqbdetails_holdtime = (TextView) findViewById(R.id.tv_hqbdetails_holdtime);
        tv_hqbdetails_crate = (TextView) findViewById(R.id.tv_hqbdetails_crate);
        tvbtn_hqbdetails_redeem = (TextView) findViewById(R.id.tvbtn_hqbdetails_redeem);
        tvbtn_hqbdetails_redeem.setOnClickListener(this);

    }


    private void requestNewWorkData() {
        tenderId = getIntent().getStringExtra("code");
        String mid = SPUtils.get(this, "LOGIN", "id", 0) + "";
        Map<String, String> params = new HashMap<>();
        params.put("mid", mid);
        params.put("tenderId", tenderId);
        myHqbProjectDetailsPresenter.sendRequest(params, null);

    }

    @Override
    public void onClick(View v) {
        if (v == tvbtn_hqbdetails_redeem) {
            if (id != 0) {
                MyActivityManager.getInstance().startNextActivity(MyHqbRedeemedActivity.class, id + "", "single", "0");
            }
        } else if (v == actionbar.leftImageView) {
//            popActivity();
            MyActivityManager.getInstance().popCurrentActivity();
        }

    }

    @Override
    public void showLoading() {
//        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_layout_myhqb_runningprojectdetails, null);
//        loadingPopupWindow = PopwUtils.showLoadingPopw(this, rootView);
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
//        PopwUtils.dismissLoadingPopw(loadingPopupWindow);
        dismissLoadingDialog();
    }

    @Override
    public void showError() {
//        PopwUtils.dismissLoadingPopw(loadingPopupWindow);
        dismissLoadingDialog();
        showSystemError();
    }

    @Override
    public void showError(OnPresenterListener listener, MyHqbProjectDetailsBean myHqbProjectDetailsBean) {
//        PopwUtils.dismissLoadingPopw(loadingPopupWindow);
        dismissLoadingDialog();
        CommonToastUtils.showToast(this, myHqbProjectDetailsBean.getMsg());
    }

    @Override
    public void responseData(OnPresenterListener listener, MyHqbProjectDetailsBean myHqbProjectDetailsBean) {
        this.myHqbProjectDetailsBean = myHqbProjectDetailsBean;
        if (listener instanceof MyHqbProjectDetailsPresenterImpl) {
            //详情
            setTextData(myHqbProjectDetailsBean);

            //记录
            setListData(myHqbProjectDetailsBean);
        }
    }

    private void setListData(MyHqbProjectDetailsBean myHqbProjectDetailsBean) {
        List<MyHqbProjectDetailsBean.TransactionListBean> transactionList = myHqbProjectDetailsBean.getTransactionList();
        MyHqbProjectDetailsAdapter adapter = new MyHqbProjectDetailsAdapter(this, transactionList);
        lv_hqb_runningproject_details.setAdapter(adapter);
    }

    private void setTextData(MyHqbProjectDetailsBean myHqbProjectDetailsBean) {
        MyHqbProjectDetailsBean.TenderDetailBean tenderDetail = myHqbProjectDetailsBean.getTenderDetail();
        id = tenderDetail.getId();
        actionbar.centerTextView.setText(tenderDetail.getHqbTitle());
//        balance = tenderDetail.getAmount();
//        tv_hqbdetails_balance.setText(new DecimalFormat("###,###,##0.00").format(balance) + "元");
        incomed = tenderDetail.getIncomeAmount();
        tv_hqbdetails_incomed.setText(new DecimalFormat("###,###,##0.00").format(incomed));
        yestodayincome = tenderDetail.getYesterdayIncome();
        tv_hqbdetails_yestodayincome.setText(new DecimalFormat("###,###,##0.00").format(yestodayincome));
        rate = tenderDetail.getRate();
        tv_hqbdetails_rate.setText(rate + "%");
        holdtime = tenderDetail.getTenderDays() + "";
        tv_hqbdetails_holdtime.setText(holdtime + "天");
        passtime = new SimpleDateFormat("yyyy-MM-dd").format(tenderDetail.getCtime());
        tv_hqbdetails_passtime.setText(passtime);
        double amount = tenderDetail.getRemainingTenderAmount();
        if (amount == 0) {
            tv_hqbdetails_balance.setText("0.00");
            tvbtn_hqbdetails_redeem.setVisibility(View.GONE);
            tvbtn_hqbdetails_redeem.setClickable(false);
            layout_crate.setVisibility(View.VISIBLE);
            tv_hqbdetails_crate.setText(rate + "%");
            tv_hqbdetails_rate.setText(tenderDetail.getRedeemRate() + "%");
            tv_rate_type.setText("赎回时年化收益");
        } else {
            tv_hqbdetails_balance.setText(new DecimalFormat("###,###,##0.00").format(amount));
            tvbtn_hqbdetails_redeem.setVisibility(View.VISIBLE);
            tvbtn_hqbdetails_redeem.setClickable(true);
            layout_crate.setVisibility(View.GONE);
            tv_rate_type.setText("当前年化收益");
        }
    }


}
