package com.mgjr.view.profile.myhqb;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.ClearEditText;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.Double2int;
import com.mgjr.Utils.KeyBoardUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.SPUtils;
import com.mgjr.Utils.StringToBase64;
import com.mgjr.model.bean.HqbSingleRedeemBean;
import com.mgjr.presenter.impl.HqbMutilRedeemConfirmPresenterImpl;
import com.mgjr.presenter.impl.HqbMutilRedeemPresenterImpl;
import com.mgjr.presenter.impl.HqbSingleRedeemConfirmPresenterImpl;
import com.mgjr.presenter.impl.HqbSingleRedeemPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.InputPayPwdPopw;
import com.mgjr.share.NetUtils;
import com.mgjr.share.PayPwdDialog;
import com.mgjr.view.listeners.ViewListener;
import com.mgjr.view.profile.accountsetting.ProfileFindTradePwdActivity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/15.
 */
public class MyHqbRedeemedActivity extends ActionbarActivity implements View.OnClickListener, ViewListener<HqbSingleRedeemBean> {
    private HqbSingleRedeemBean.TenderDetailBean singleTenderDetail, mutilTenderDetail;
    private LinearLayout rootlayout;  //当前activity根布局
    //标题
    private TextView tv_hqbredeemed_title;
    private String singleHqbTitle, mutilHqbTitle;
    /*
    * 单：持有余额   多：已获收益
    * */
    private TextView tv_hqbredeemed__balance;           //金额
    private TextView tv_hqbredeemed_account_type;      //类型
    private Double singleRemainingTenderAmount, mutilRemainingTenderAmount;

    /*
    * 单：当前年化收益   多：最高当前年化收益
    * */
    private TextView tv_hqbredeemed_rate;              //年化
    private TextView tv_hqbredeemed_rate_type;        //类型
    private Double singleRate, mutilRate;

    //输入赎回金额
    private ClearEditText et_hqbredeemed__account;

    /*
    * 单项赎回时显示 "可赎回金额"布局
    * */
    private LinearLayout layout_hqbredeemed_redeemable;
    private TextView tv_hqbredeemed_redeemable; //可赎回金额
    private Double singleRedeemable_amount, mutilRedeemable_amount;
    /*
    * 多项赎回时显示的 "温馨提示"
    * */
    private TextView tv_hqbredeemed_redeemtips;

    //预计少赚收益
    private TextView tv_hqbredeemed_expecct_income;
    private Double singleIncome, mutilIncome;
    //下一步按钮
    private Button btn_hqbredeemed_nextstep;

    private HqbSingleRedeemPresenterImpl hqbSingleRedeemPresenter;
    private HqbSingleRedeemConfirmPresenterImpl hqbSingleRedeemConfirmPresenter;
    private HqbMutilRedeemPresenterImpl hqbMutilRedeemPresenter;
    private HqbMutilRedeemConfirmPresenterImpl hqbMutilRedeemConfirmPresenter;
    private String singleCode, mutilCode;
    private Double mutilamount;
    private String amount;
    private String type;
    private PayPwdDialog payPwdDialog;
    private PopupWindow loadingPopupWindow;
    private HqbSingleRedeemBean hqbSingleRedeemBean;
    private InputPayPwdPopw inputPayPwdPopw;
    private String tradePwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_layout_hqbredeemed, this);
        this.actionbar.setCenterTextView("赎回");
        initViews();
        //初始化支付密码弹窗
        initPayPwdPopw();
        checkIntentSource();
    }

    private void checkIntentSource() {
        String intentSource = getIntent().getStringExtra("rate");
        if (intentSource.equalsIgnoreCase("0")) {
            SPUtils.put(MyHqbRedeemedActivity.this, "TempIntent", "intentResource", "single");
        } else if (intentSource.equalsIgnoreCase("1")) {
            SPUtils.put(MyHqbRedeemedActivity.this, "TempIntent", "intentResource", "redeem");
        }
    }

    private void initPayPwdPopw() {
        inputPayPwdPopw = new InputPayPwdPopw();
        inputPayPwdPopw.setClickBtnListener(new InputPayPwdPopw.ClickBtnListener() {
            @Override
            public void clickConfirmBtn() {
                //提交数据,请求数据
                tradePwd = inputPayPwdPopw.getEtInputTradePwd().getText().toString();

                if (tradePwd.equalsIgnoreCase("")) {
                    CommonToastUtils.showToast(MyHqbRedeemedActivity.this, "密码不能为空");
                    return;
                } else if (tradePwd.length() < 6 || tradePwd.length() > 16) {
                    CommonToastUtils.showToast(MyHqbRedeemedActivity.this, "密码的长度必须是6到16位之间");
                    return;
                } else if (tradePwd.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0) {
                    // 包含特殊字符
                    CommonToastUtils.showToast(MyHqbRedeemedActivity.this, "密码不能有特殊字符");
                    inputPayPwdPopw.getEtInputTradePwd().requestFocus();
                    return;
                }
                //提交数据,请求数据
                doTender();
                inputPayPwdPopw.startCheckingAnim();
                KeyBoardUtils.closeKeybord(inputPayPwdPopw.getEtInputTradePwd(), MyHqbRedeemedActivity.this);
            }

            @Override
            public void clickForgetPwdBtn() {
                MyActivityManager.getInstance().startNextActivity(ProfileFindTradePwdActivity.class);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestNetwork();
    }


    private void requestNetwork() {

        if (type.equalsIgnoreCase("single")) {
            tv_hqbredeemed_account_type.setText("已获收益(元)");
            tv_hqbredeemed_rate_type.setText("当前年化收益");
            int id = (int) SPUtils.get(this, "LOGIN", "id", 0);
            String tenderId = getIntent().getStringExtra("code");
            String mid = id + "";
            Map<String, String> params = new HashMap<>();
            params.put("mid", mid);
            params.put("tenderId", tenderId);
            hqbSingleRedeemPresenter.sendRequest(params, null);
        } else if (type.equalsIgnoreCase("mutil")) {
            et_hqbredeemed__account.setCloseBtnIsVisable(true);
            tv_hqbredeemed_account_type.setText("已获收益(元)");
            tv_hqbredeemed_rate_type.setText("最高当前年化收益");
            int id = (int) SPUtils.get(this, "LOGIN", "id", 0);
            String mid = id + "";
            String tenderIds = getIntent().getStringExtra("code");
            Map<String, String> params = new HashMap<>();
            params.put("mid", mid);
            params.put("tenderIds", tenderIds);
            hqbMutilRedeemPresenter.sendRequest(params, null);
        }


    }


    private void initViews() {
        hqbSingleRedeemPresenter = new HqbSingleRedeemPresenterImpl(this);
        hqbSingleRedeemConfirmPresenter = new HqbSingleRedeemConfirmPresenterImpl(this);
        hqbMutilRedeemPresenter = new HqbMutilRedeemPresenterImpl(this);
        hqbMutilRedeemConfirmPresenter = new HqbMutilRedeemConfirmPresenterImpl(this);
        tv_hqbredeemed_title = (TextView) findViewById(R.id.tv_hqbredeemed_title);
        tv_hqbredeemed__balance = (TextView) findViewById(R.id.tv_hqbredeemed__balance);
        tv_hqbredeemed_account_type = (TextView) findViewById(R.id.tv_hqbredeemed_account_type);
        tv_hqbredeemed_rate = (TextView) findViewById(R.id.tv_hqbredeemed_rate);
        tv_hqbredeemed_rate_type = (TextView) findViewById(R.id.tv_hqbredeemed_rate_type);
        tv_hqbredeemed_redeemable = (TextView) findViewById(R.id.tv_hqbredeemed_redeemable);
        tv_hqbredeemed_redeemtips = (TextView) findViewById(R.id.tv_hqbredeemed_redeemtips);
        tv_hqbredeemed_expecct_income = (TextView) findViewById(R.id.tv_hqbredeemed_expecct_income);

        et_hqbredeemed__account = (ClearEditText) findViewById(R.id.et_hqbredeemed__account);

        rootlayout = (LinearLayout) findViewById(R.id.rootlayout_hqb_singleredeem_comfirm);
        layout_hqbredeemed_redeemable = (LinearLayout) findViewById(R.id.layout_hqbredeemed_redeemable);

        btn_hqbredeemed_nextstep = (Button) findViewById(R.id.btn_hqbredeemed_nextstep);
        btn_hqbredeemed_nextstep.setOnClickListener(this);
        type = getIntent().getStringExtra("type");
        if (type.equalsIgnoreCase("single")) {
            set30incomeData();
        }
    }

    //30天少赚收益
    private void set30incomeData() {

        et_hqbredeemed__account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString();
                if (!TextUtils.isEmpty(s1)) {
                    if (Double.parseDouble(s1) == 0) {
                        CommonToastUtils.showToast(MyHqbRedeemedActivity.this, "请输入大于1的整数");
                    } else {
                        double v = Double.parseDouble(s1) * singleRate / 1200;
                        if (v != 0) {
                            tv_hqbredeemed_expecct_income.setText(new DecimalFormat("###,###,##0.00").format(v));
                            tv_hqbredeemed_expecct_income.setTextColor(Color.parseColor("#feaa00"));
                        } else {
                            tv_hqbredeemed_expecct_income.setText("0.00");
                            tv_hqbredeemed_expecct_income.setTextColor(Color.parseColor("#333333"));
                        }
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v == btn_hqbredeemed_nextstep) {
            amount = et_hqbredeemed__account.getText().toString().trim();
            if (TextUtils.isEmpty(amount)) {
                CommonToastUtils.showToast(this, "赎回金额不能为空");
            } else {
                if (type.equalsIgnoreCase("single")) {
                    Double inputAmount = 0.0;
                    int allAmount = 0;
                    inputAmount = Double.parseDouble(amount);
                    allAmount = Double2int.double2Int(singleRemainingTenderAmount);

                    if (inputAmount < 1) {
                        CommonToastUtils.showToast(this, "赎回金额不能小于1元");
                    } else if (inputAmount > allAmount) {
                        CommonToastUtils.showToast(this, "输入金额超过可赎回金额了");
                    } else {
                        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_layout_hqbredeemed, null);
                        inputPayPwdPopw.showInputTradePwdPopw(this, rootView);
                        btn_hqbredeemed_nextstep.setClickable(false);
                    }
                } else if (type.equalsIgnoreCase("mutil")) {
                    View rootView = LayoutInflater.from(this).inflate(R.layout.activity_layout_hqbredeemed, null);
                    inputPayPwdPopw.showInputTradePwdPopw(this, rootView);
                    btn_hqbredeemed_nextstep.setClickable(false);
                }


            }
        }

    }

   /* private void showDialog() {
        payPwdDialog = new PayPwdDialog(this);
        payPwdDialog.show();
        payPwdDialog.setClicklistener(new PayPwdDialog.ClickListenerInterface() {
            @Override
            public void doConfirm(Editable text) {

                doTender(text);

            }

            @Override
            public void doCancel() {
                payPwdDialog.dismiss();
            }
        });
    }*/

    private void doTender() {
        if (type.equalsIgnoreCase("single")) {

            String mid = (int) SPUtils.get(MyHqbRedeemedActivity.this, "LOGIN", "id", 0) + "";
            amount = et_hqbredeemed__account.getText().toString().trim();
            Map<String, String> params = new HashMap<>();
            params.put("mid", mid);
            params.put("tenderId", singleCode);
            params.put("amount", amount);
            params.put("paypwd", StringToBase64.stringToBase64(tradePwd));
            hqbSingleRedeemConfirmPresenter.sendRequest(params, null);

        } else if (type.equalsIgnoreCase("mutil")) {

            String mid = (int) SPUtils.get(MyHqbRedeemedActivity.this, "LOGIN", "id", 0) + "";
            String tenderIds = getIntent().getStringExtra("code");
            amount = et_hqbredeemed__account.getText().toString().trim();

            Map<String, String> params = new HashMap<>();
            params.put("mid", mid);
            params.put("tenderIds", tenderIds);
            params.put("amount", mutilamount + "");
            params.put("paypwd", StringToBase64.stringToBase64(tradePwd));
            hqbMutilRedeemConfirmPresenter.sendRequest(params, null);
        }


    }


    @Override
    public void showLoading() {
//        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_layout_hqbredeemed, null);
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
//        CommonToastUtils.showToast(this, getResources().getString(R.string.system_error));
        if (!NetUtils.isConnected(this)) {
            inputPayPwdPopw.paypwdCheckingError(getResources().getString(R.string.network_error));
        } else {
            inputPayPwdPopw.paypwdCheckingError(getResources().getString(R.string.system_error));
        }

    }

    @Override
    public void showError(OnPresenterListener listener, HqbSingleRedeemBean hqbSingleRedeemBean) {
//        CommonToastUtils.showToast(this, hqbSingleRedeemBean.getMsg());
        inputPayPwdPopw.paypwdCheckingError(hqbSingleRedeemBean.getMsg());
        dismissLoadingDialog();
    }

    @Override
    public void responseData(OnPresenterListener listener, HqbSingleRedeemBean hqbSingleRedeemBean) {
        this.hqbSingleRedeemBean = hqbSingleRedeemBean;
        btn_hqbredeemed_nextstep.setClickable(true);
        if (listener instanceof HqbSingleRedeemPresenterImpl) {//单项赎回
            singleIncome = hqbSingleRedeemBean.getIncome();
            singleTenderDetail = hqbSingleRedeemBean.getTenderDetail();
            singleCode = singleTenderDetail.getId() + "";

            setSigleRedeemData();

        } else if (listener instanceof HqbMutilRedeemPresenterImpl) { //多项赎回
            mutilIncome = hqbSingleRedeemBean.getIncometotal30();
            mutilRate = hqbSingleRedeemBean.getRateMax();
            mutilamount = hqbSingleRedeemBean.getRedeemAmountTotal();
            mutilHqbTitle = hqbSingleRedeemBean.getTips();

            setMutilRedeemData();
        } else if (listener instanceof HqbSingleRedeemConfirmPresenterImpl) {//单项赎回确认
            inputPayPwdPopw.paypwdCheckingSuccess();
            final String amount = hqbSingleRedeemBean.getAmount() + "";
            long mtime = hqbSingleRedeemBean.getTime();
            final String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(mtime);
//            SPUtils.put(MyHqbRedeemedActivity.this,"TempData","amount",amount);
//            SPUtils.put(MyHqbRedeemedActivity.this,"TempData","time",time);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MyActivityManager.getInstance().startNextActivity(MyHqbRedeemCompleteActivity.class, amount, time);
//            popActivity();
                    MyActivityManager.getInstance().finishSpecifiedActivity(MyHqbRedeemedActivity.class);
                    inputPayPwdPopw.getInputTradePwdPopw().dismiss();
                }
            }, 700);

        } else if (listener instanceof HqbMutilRedeemConfirmPresenterImpl) {
            inputPayPwdPopw.paypwdCheckingSuccess();
            final String amount = hqbSingleRedeemBean.getAmount() + "";
            long mtime = hqbSingleRedeemBean.getTime();
            final String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(mtime);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    inputPayPwdPopw.getInputTradePwdPopw().dismiss();
                    MyActivityManager.getInstance().startNextActivity(MyHqbRedeemCompleteActivity.class, amount, time);
//            popActivity();
                    MyActivityManager.getInstance().finishSpecifiedActivity(MyHqbRedeemedActivity.class);
                }
            }, 700);
        }
    }

    private void setMutilRedeemData() {
        //标题
        if (mutilHqbTitle != null) {
            tv_hqbredeemed_title.setText(mutilHqbTitle);
        }
        double incometotal = hqbSingleRedeemBean.getIncometotal();
        if (incometotal != 0) {
            tv_hqbredeemed__balance.setText(new DecimalFormat("###,###,##0.00").format(incometotal));
        } else {
            tv_hqbredeemed__balance.setText("0.00");
        }
        et_hqbredeemed__account.setText(mutilamount + "");
        et_hqbredeemed__account.setClickable(false);
        et_hqbredeemed__account.setEnabled(false);
        //可赎回金额
        tv_hqbredeemed_redeemable.setText("多项赎回仅支持全额赎回");
        //rate
        if (mutilRate != null) {
            tv_hqbredeemed_rate.setText(mutilRate + "%");
        } else {
            tv_hqbredeemed_rate.setText("0.0%");
        }
        //30天少赚
        if (mutilIncome != null) {
            tv_hqbredeemed_expecct_income.setText(new DecimalFormat("###,###,##0.00").format(mutilIncome));
            tv_hqbredeemed_expecct_income.setTextColor(Color.parseColor("#feaa00"));

        } else {
            tv_hqbredeemed_expecct_income.setText("0.0");
            tv_hqbredeemed_expecct_income.setTextColor(Color.parseColor("#333333"));
        }

    }

    private void setSigleRedeemData() {
        singleHqbTitle = singleTenderDetail.getHqbTitle();
        if (singleHqbTitle != null) {
            tv_hqbredeemed_title.setText(singleHqbTitle);
        }

        singleRemainingTenderAmount = singleTenderDetail.getRemainingTenderAmount();
        Double incomeAmount = hqbSingleRedeemBean.getTenderDetail().getIncomeAmount();

        if (incomeAmount != null) {
            tv_hqbredeemed__balance.setText(new DecimalFormat("###,###,##0.00").format(incomeAmount));
        } else {
            tv_hqbredeemed__balance.setText("0.00");
        }

        singleRedeemable_amount = singleTenderDetail.getAmount();
        if (singleRedeemable_amount != null) {
            tv_hqbredeemed_redeemable.setText(new DecimalFormat("###,###,##0.00").format(singleRemainingTenderAmount) + "元");
        } else {
            tv_hqbredeemed_redeemable.setText("0.00");
        }

        singleRate = singleTenderDetail.getRate();
        if (singleRate != null) {
            tv_hqbredeemed_rate.setText(singleRate + "%");
        } else {
            tv_hqbredeemed_rate.setText("0.0%");
        }
        /*30天少赚收益*/

    }
}
