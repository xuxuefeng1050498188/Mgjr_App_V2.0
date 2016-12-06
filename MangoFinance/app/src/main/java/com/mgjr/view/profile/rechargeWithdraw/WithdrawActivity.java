package com.mgjr.view.profile.rechargeWithdraw;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgjr.R;
import com.mgjr.Utils.ClearEditText;
import com.mgjr.Utils.KeyBoardUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.NumberUtils;
import com.mgjr.Utils.PopwUtils;
import com.mgjr.Utils.SPUtils;
import com.mgjr.Utils.StringToBase64;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.model.bean.EventBusBean.WithdrawCompleteBean;
import com.mgjr.model.bean.RequestWithdrawBean;
import com.mgjr.presenter.impl.RequestWithdrawPresenterImpl;
import com.mgjr.presenter.impl.WithdrawPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.InputPayPwdPopw;
import com.mgjr.share.NetUtils;
import com.mgjr.share.addresswheel_master.model.AddressDtailsEntity;
import com.mgjr.share.addresswheel_master.model.AddressModel;
import com.mgjr.share.addresswheel_master.utils.JsonUtil;
import com.mgjr.share.addresswheel_master.utils.Utils;
import com.mgjr.share.addresswheel_master.view.ChooseAddressWheel;
import com.mgjr.share.addresswheel_master.view.listener.OnAddressChangeListener;
import com.mgjr.view.common.CommonWebViewActivity;
import com.mgjr.view.invester.InvestConfirmActivity;
import com.mgjr.view.listeners.ViewListener;
import com.mgjr.view.profile.accountsetting.ProfileAddBankCardActivity;
import com.mgjr.view.profile.accountsetting.ProfileFindTradePwdActivity;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.mgjr.Utils.HideInputUtils.isShouldHideInput;

/**
 * Created by Administrator on 2016/8/9.
 */
public class WithdrawActivity extends ActionbarActivity implements View.OnClickListener, ViewListener<RequestWithdrawBean>, OnAddressChangeListener {

    private static final String MIN_WITHDRAW_AMOUNT = "2";
    private static final String MAX_WITHDRAW_AMOUNT = "1000000";
    //银行名，银行卡尾号，银行限额提示，可提现金额，手续费
    private TextView tv_withdraw_bankname, tv_withdraw_bankcardnumber, tv_withdraw_tips, tv_withdraw_account, tv__withdraw_commission;
    //输入提现金额
    private EditText et_withdraw_account;
    //删除按钮
    private ImageView imgbtn_delete_withdrawaccount;
    private Button btn_withdraw_next;
    private RequestWithdrawBean requestWithdrawBean;
    private LinearLayout ll_unbind_bankcard;
    private EditText et_input_bankcard_name;
    private RelativeLayout rl_add_bankcard;
    private LinearLayout ll_binded_bankcard;
    private ImageView iv_withdraw_bankcard_logo;
    private LinearLayout ll_binded_bankcard_without_address;
    private RelativeLayout rl_select_bankcard_address;
    private ClearEditText et_input_bank_branch;
    private TextView tv_actual_withdraw_money;

    //界面的状态
    private int bindCardState;
    private TextView tv_bankcard_address;
    private RequestWithdrawPresenterImpl requestWithdrawPresenterImpl;
    private WithdrawPresenterImpl withdrawPresenterImpl;

    private String money;
    private Integer mid;
    private int channel;
    private String cardNo;
    private String tradePwd;
    private RequestWithdrawBean.AccountWithdrawBean accountWithdraw;
    private PopupWindow inputTradePwdPopw;
    private PopupWindow loadingPopupWindow;
    private ChooseAddressWheel chooseAddressWheel = null;
    private double accountBalance;
    private String province;
    private String city;
    private String district;
    private boolean hasAuthRealName;
    private PopupWindow realNameAuthPopupWindow;

    private InputPayPwdPopw inputPayPwdPopw;
    private LinearLayout ll_withdraw_root_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_layout_withdraw, this);
        this.actionbar.setCenterTextView("提现");
        this.actionbar.setRightImageView(R.drawable.confirminvest_question_btn, this);

        requestWithdrawPresenterImpl = new RequestWithdrawPresenterImpl(this);
        withdrawPresenterImpl = new WithdrawPresenterImpl(this);
        hasAuthRealName = (boolean) SPUtils.get(this, "LOGIN", "isTruename", false);
        initViews();
        //初始化本地的省市县三级联动
        initAndroidWheel();
        //初始化支付密码弹窗
        initPayPwdPopw();
    }

    private void initPayPwdPopw() {
        inputPayPwdPopw = new InputPayPwdPopw();
        inputPayPwdPopw.setClickBtnListener(new InputPayPwdPopw.ClickBtnListener() {
            @Override
            public void clickConfirmBtn() {
                //提交数据,请求数据
                tradePwd = inputPayPwdPopw.getEtInputTradePwd().getText().toString();

                if (tradePwd.equalsIgnoreCase("")) {
                    CommonToastUtils.showToast(WithdrawActivity.this, "密码不能为空");
                    return;
                } else if (tradePwd.length() < 6 || tradePwd.length() > 16) {
                    CommonToastUtils.showToast(WithdrawActivity.this, "密码的长度必须是6到16位之间");
                    return;
                } else if (tradePwd.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0) {
                    // 包含特殊字符
                    CommonToastUtils.showToast(WithdrawActivity.this, "密码不能有特殊字符");
                    inputPayPwdPopw.getEtInputTradePwd().requestFocus();
                    return;
                }
                //提交数据,请求数据
                requestWithdraw();
                inputPayPwdPopw.startCheckingAnim();
                KeyBoardUtils.closeKeybord(inputPayPwdPopw.getEtInputTradePwd(), WithdrawActivity.this);
            }

            @Override
            public void clickForgetPwdBtn() {
                MyActivityManager.getInstance().startNextActivity(ProfileFindTradePwdActivity.class);
            }
        });
    }

    /**
     * 在获取焦点的时候验证是否实名认证
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            //先判断是否实名认证
            hasAuthRealName = (boolean) SPUtils.get(this, "LOGIN", "isTruename", false);
            isRealNameAuth();
        }
    }

    private void isRealNameAuth() {
        if (!hasAuthRealName) {
            View rootView = LayoutInflater.from(this).inflate(R.layout.activity_layout_withdraw, null);
            realNameAuthPopupWindow = PopwUtils.showRealNameAuthPopw(this, rootView);
        } else {
            if (realNameAuthPopupWindow != null) {
                realNameAuthPopupWindow.dismiss();
                PopwUtils.backgroundAlpha(1, this);
            }
//            PopwUtils.dismissLoadingPopw(realNameAuthPopupWindow);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hasAuthRealName = (boolean) SPUtils.get(this, "LOGIN", "isTruename", false);
        if (hasAuthRealName) {
            networkRequest();
        }
    }

    private void networkRequest() {
        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        necessaryParams.put("mid", "" + SPUtils.get(this, "LOGIN", "id", 0));
        requestWithdrawPresenterImpl.sendRequest(necessaryParams, null);
    }


    private void initViews() {
        ll_withdraw_root_view = (LinearLayout) findViewById(R.id.ll_withdraw_root_view);
        ll_withdraw_root_view.setVisibility(View.GONE);
        //未绑卡状态初始化
        ll_unbind_bankcard = (LinearLayout) findViewById(R.id.ll_unbind_bankcard);
        et_input_bankcard_name = (EditText) findViewById(R.id.et_input_bankcard_name);
        rl_add_bankcard = (RelativeLayout) findViewById(R.id.rl_add_bankcard);
        rl_add_bankcard.setOnClickListener(this);
        //已绑卡状态初始化
        ll_binded_bankcard = (LinearLayout) findViewById(R.id.ll_binded_bankcard);
        iv_withdraw_bankcard_logo = (ImageView) findViewById(R.id.iv_withdraw_bankcard_logo);
        tv_withdraw_bankname = (TextView) findViewById(R.id.tv_withdraw_bankname);
        tv_withdraw_bankcardnumber = (TextView) findViewById(R.id.tv_withdraw_bankcardnumber);
        tv_withdraw_tips = (TextView) findViewById(R.id.tv_withdraw_tips);
        // 选择地址初始化
        ll_binded_bankcard_without_address = (LinearLayout) findViewById(R.id.ll_binded_bankcard_without_address);
        rl_select_bankcard_address = (RelativeLayout) findViewById(R.id.rl_select_bankcard_address);
        et_input_bank_branch = (ClearEditText) findViewById(R.id.tv_input_bank_branch);
        tv_bankcard_address = (TextView) findViewById(R.id.tv_bankcard_address);
        rl_select_bankcard_address.setOnClickListener(this);

        boolean isBindCard = (boolean) SPUtils.get(this, "LOGIN", "isBindCard", false);
        if (!isBindCard) {
            setNoBindCardBg();
        }

        //充值金额共有部分
        tv_withdraw_account = (TextView) findViewById(R.id.tv_withdraw_account);
        tv__withdraw_commission = (TextView) findViewById(R.id.tv__withdraw_commission);
        et_withdraw_account = (EditText) findViewById(R.id.et_withdraw_account);
//        imgbtn_delete_withdrawaccount = (ImageView) findViewById(R.id.imgbtn_delete_withdrawaccount);
        btn_withdraw_next = (Button) findViewById(R.id.btn_withdraw_next);
        btn_withdraw_next.setOnClickListener(this);
        tv_actual_withdraw_money = (TextView) findViewById(R.id.tv_actual_withdraw_money);
        mid = (Integer) SPUtils.get(this, "LOGIN", "id", 0);
        setPricePoint(et_withdraw_account);
        et_withdraw_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String withdrawMoney = et_withdraw_account.getText().toString();
                if (!TextUtils.isEmpty(withdrawMoney)) {
                    tv_actual_withdraw_money.setVisibility(View.VISIBLE);
                    double actualWithdrawMoney = Double.parseDouble(withdrawMoney);
                    if (actualWithdrawMoney >= 2) {
                        String format = new DecimalFormat("###,###,##0.00").format((actualWithdrawMoney - 1));
                        tv_actual_withdraw_money.setText("实际到账:" + format + "元");
                    }
                } else {
                    tv_actual_withdraw_money.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void setNoBindCardBg() {
        ll_unbind_bankcard.setVisibility(View.VISIBLE);
        ll_binded_bankcard.setVisibility(View.GONE);
        ll_binded_bankcard_without_address.setVisibility(View.GONE);
        String truename = (String) SPUtils.get(MainApplication.getContext(), "LOGIN", "truename", "");
        et_input_bankcard_name.setText(truename);
        et_input_bankcard_name.setFocusable(false);
        bindCardState = 0;
    }

    private void initAndroidWheel() {
        initWheel();
        initData();
    }

    private void initWheel() {
        chooseAddressWheel = new ChooseAddressWheel(this);
        chooseAddressWheel.setOnAddressChangeListener(this);
    }

    private void initData() {
        String address = Utils.readAssert(this, "address.txt");
        AddressModel model = JsonUtil.parseJson(address, AddressModel.class);
        if (model != null) {
            AddressDtailsEntity data = model.Result;
            if (data == null) return;
//            tv_bankcard_address.setText(data.Province + " " + data.City + " " + data.Area);
            if (data.ProvinceItems != null && data.ProvinceItems.Province != null) {
                chooseAddressWheel.setProvince(data.ProvinceItems.Province);
                chooseAddressWheel.defaultValue("湖南省", "长沙市", "岳麓区");
            }
        }
    }

    @Override
    public void onAddressChange(String province, String city, String district) {
        tv_bankcard_address.setText(province + " " + city + " " + district);
        this.province = province;
        this.city = city;
        this.district = district;
    }

    public static void setPricePoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            }

                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {
                                                String temp = s.toString();
                                                if (temp.length() == 1 && temp.equals("0")) {
                                                    s.clear();
                                                }
                                                if (temp.length() == 1 && temp.equals(".")) {
                                                    s.clear();
                                                }
                                                int posDot = temp.indexOf(".");
                                                if (posDot <= 0) return;
                                                if (temp.length() - posDot - 1 > 2) {
                                                    s.delete(posDot + 3, posDot + 4);
                                                }
                                            }

                                        }

        );

    }

    @Override
    public void onClick(View v) {

        if (v == rl_add_bankcard) {
            MyActivityManager.getInstance().startNextActivity(ProfileAddBankCardActivity.class, "", "0");
        } else if (v == rl_select_bankcard_address) {
            Utils.hideKeyBoard(this);
            chooseAddressWheel.show(v);
        } else if (v == btn_withdraw_next) {
            //点击下一步
            checkData();
        } else if (v == actionbar.rightImageView) {
            MyActivityManager.getInstance().startNextActivity(CommonWebViewActivity.class, "cashProblem");
        }
    }

    private void checkData() {
        if (bindCardState == 0) {
            //没有绑卡
            /*if (TextUtils.isEmpty(et_input_bankcard_name.getText().toString().trim())) {
                CommonToastUtils.showToast(this, "持卡人姓名不能为空");
                return;
            }*/
            boolean isBindCard = (boolean) SPUtils.get(this, "LOGIN", "isBindCard", false);
            if (!isBindCard) {
                CommonToastUtils.showToast(this, "请先添加银行卡");
                return;
            }
        } else if (bindCardState == 1) {
            //已经绑卡,但没有支行信息
            if ("".equals(tv_bankcard_address.getText().toString())) {
                CommonToastUtils.showToast(this, "请先选择银行开户地");
                return;
            }
            if (TextUtils.isEmpty(et_input_bank_branch.getText().toString().trim())) {
                CommonToastUtils.showToast(this, "请输入开户支行名称");
                return;
            }
        }
        //处理提现
        processWithDraw();
    }

    private void processWithDraw() {
        try {
            money = et_withdraw_account.getText().toString();

            if (money.equalsIgnoreCase("")) {

                CommonToastUtils.showToast(this, "请输入您要提现的金额");

            } else if (money.contains(" ")) {

                CommonToastUtils.showToast(this, "金额不能有空格");

            } else if (TextUtils.isEmpty(money)) {

                CommonToastUtils.showToast(this, "请输入您要提现的金额");

            } else if (Double.parseDouble(money) < Double
                    .parseDouble(MIN_WITHDRAW_AMOUNT) || Double.parseDouble(money) > Double
                    .parseDouble(MAX_WITHDRAW_AMOUNT)) {

                CommonToastUtils.showToast(this, "提现金额必须大于等于" + MIN_WITHDRAW_AMOUNT + "元，并且小于等于" + MAX_WITHDRAW_AMOUNT + "元");

            } else if (Double.parseDouble(money) > requestWithdrawBean.getAmount()) {
                CommonToastUtils.showToast(this, "提现金额必须小于等于" + requestWithdrawBean.getAmount() + "元");

            } else {
                //弹窗,正在提现
                //弹窗,输入交易密码
                View rootView = LayoutInflater.from(this).inflate(R.layout.activity_layout_withdraw, null);
                inputPayPwdPopw.showInputTradePwdPopw(this, rootView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 输入交易密码的弹窗
     */
    /*public PopupWindow showInputTradePwdPopw(final Activity activity, View rootView) {
        View contentView = LayoutInflater.from(activity).inflate(R.layout.input_trade_pwd, null);
        final EditText etInputTradePwd = (EditText) contentView.findViewById(R.id.et_input_trade_pwd);
        final TextView dialogCanclebtn = (TextView) contentView.findViewById(R.id.dialog_canclebtn);
        final TextView dialogConfirmbtn = (TextView) contentView.findViewById(R.id.dialog_confirmbtn);
        final TextView tv_forgetpaypwd = (TextView) contentView.findViewById(R.id.tv_forgetpaypwd);
        final PopupWindow inputTradePwdPopw = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //设置动画所对应的style
        inputTradePwdPopw.setAnimationStyle(R.style.calcAnim);
        //点击空白处时，隐藏掉pop窗口
        inputTradePwdPopw.setBackgroundDrawable(new ColorDrawable());
        inputTradePwdPopw.setFocusable(true);
        inputTradePwdPopw.setOutsideTouchable(true);

        PopwUtils.backgroundAlpha(0.5f, (Activity) activity);
        //自动弹出键盘以及上移弹窗,一定要注意顺序
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                KeyBoardUtils.openKeybord(etInputTradePwd, activity);
            }
        }, 800);
        inputTradePwdPopw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        inputTradePwdPopw.showAtLocation(rootView, Gravity.CENTER, 0, 0);
        inputTradePwdPopw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopwUtils.backgroundAlpha(1f, activity);
            }
        });
        dialogCanclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputTradePwdPopw != null) {
                    KeyBoardUtils.closeKeybord(etInputTradePwd, activity);
                    inputTradePwdPopw.dismiss();
                }
            }
        });
        tv_forgetpaypwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivityManager.getInstance().startNextActivity(ProfileFindTradePwdActivity.class);
            }
        });
        dialogConfirmbtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    tradePwd = etInputTradePwd.getText().toString();

                                                    if (tradePwd.equalsIgnoreCase("")) {
                                                        CommonToastUtils.showToast(WithdrawActivity.this, "密码不能为空！");
                                                        return;
                                                    } else if (tradePwd.length() < 6 || tradePwd.length() > 16) {
                                                        CommonToastUtils.showToast(WithdrawActivity.this, "密码的长度必须是6到16位之间");
                                                        return;
                                                    } else if (tradePwd.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0) {
                                                        // 包含特殊字符
                                                        CommonToastUtils.showToast(WithdrawActivity.this, "密码不能有特殊字符！");
                                                        etInputTradePwd.requestFocus();
                                                        return;
                                                    }
                                                    //提交数据,请求数据
                                                    requestWithdraw();
                                                    KeyBoardUtils.closeKeybord(etInputTradePwd, activity);
                                                    inputTradePwdPopw.dismiss();
                                                }
                                            }

        );
        return inputTradePwdPopw;
    }*/
    private void requestWithdraw() {
        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        Map<String, String> unNecessaryParams = null;
        necessaryParams.put("mid", "" + mid);
        necessaryParams.put("amount", money);
        necessaryParams.put("channel", "" + channel);
        necessaryParams.put("cardNo", "" + cardNo);
        necessaryParams.put("paypwd", "" + StringToBase64.stringToBase64(tradePwd));
        necessaryParams.put("device", "" + 5);
        if (bindCardState == 1) {
            unNecessaryParams = new HashMap<>();
            //绑卡但没有完善信息
            unNecessaryParams.put("province", province);
            unNecessaryParams.put("city", city);
            unNecessaryParams.put("area", district);
            unNecessaryParams.put("bankAddress", et_input_bank_branch.getText().toString().trim());
        }

        withdrawPresenterImpl.sendRequest(necessaryParams, unNecessaryParams);
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
        boolean isSocketTimeoutException = (boolean) SPUtils.get(MainApplication.getContext(), "SocketTimeoutException", "isSocketTimeoutException", false);
        if (!NetUtils.isConnected(MainApplication.getContext())) {
            inputPayPwdPopw.paypwdCheckingError(getString(R.string.network_error));
            CommonToastUtils.showToast(MainApplication.getContext(), getString(R.string.network_error));
        } else if (isSocketTimeoutException) {
            inputPayPwdPopw.paypwdCheckingError(getString(R.string.request_timeout));
            CommonToastUtils.showToast(MainApplication.getContext(), getString(R.string.request_timeout));
            SPUtils.clear(MainApplication.getContext(), "SocketTimeoutException");
        } else {
            inputPayPwdPopw.paypwdCheckingError(getString(R.string.system_error));
            CommonToastUtils.showToast(MainApplication.getContext(), getString(R.string.system_error));
        }
        /*if (!NetUtils.isConnected(this)) {
            inputPayPwdPopw.paypwdCheckingError(getResources().getString(R.string.network_error));
        } else {
            inputPayPwdPopw.paypwdCheckingError(getResources().getString(R.string.system_error));
        }*/
    }

    @Override
    public void showError(OnPresenterListener listener, RequestWithdrawBean requestWithdrawBean) {
        dismissLoadingDialog();
//        CommonToastUtils.showToast(this, requestWithdrawBean.getMsg());
        inputPayPwdPopw.paypwdCheckingError(requestWithdrawBean.getMsg());

    }

    @Override
    public void responseData(OnPresenterListener listener, final RequestWithdrawBean requestWithdrawBean) {
        this.requestWithdrawBean = requestWithdrawBean;
        if (listener instanceof RequestWithdrawPresenterImpl) {
            setInitData();
        } else if (listener instanceof WithdrawPresenterImpl) {
            accountWithdraw = requestWithdrawBean.getAccountWithdraw();
            if (inputTradePwdPopw != null) {
                inputTradePwdPopw.dismiss();
            }
//            CommonToastUtils.showSuccessToast("提现成功!");
            inputPayPwdPopw.paypwdCheckingSuccess();
            //组拼数据
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendWithdrawCompleteData(requestWithdrawBean);
                }
            }, 700);

        }
    }

    private void sendWithdrawCompleteData(RequestWithdrawBean requestWithdrawBean) {
        WithdrawCompleteBean withdrawCompleteBean = new WithdrawCompleteBean();
        withdrawCompleteBean.setAccountAmount(requestWithdrawBean.getAccountAmount());
        Gson gson = new GsonBuilder().create();
        withdrawCompleteBean.setAccountWithdraw(accountWithdraw);
        EventBus.getDefault().postSticky(withdrawCompleteBean);
        MyActivityManager.getInstance().startNextActivity(WithdrawCompleteActivity.class);
//        popActivity();
        MyActivityManager.getInstance().finishSpecifiedActivity(WithdrawActivity.class);
    }

    private void setInitData() {
        ll_withdraw_root_view.setVisibility(View.VISIBLE);
        //界面初始化
        if (requestWithdrawBean.getAccountBankcard() == null) {
            //未绑卡
//            setNoBindCardBg();
        } else {
            if ("".equals(requestWithdrawBean.getAccountBankcard().getProvince())) {
                //已绑卡,但没有填充支行信息
                ll_unbind_bankcard.setVisibility(View.GONE);
                ll_binded_bankcard.setVisibility(View.VISIBLE);
                ll_binded_bankcard_without_address.setVisibility(View.VISIBLE);
                bindCardState = 1;
            } else {
                //已绑卡,并且已经填充了支行信息
                ll_unbind_bankcard.setVisibility(View.GONE);
                ll_binded_bankcard.setVisibility(View.VISIBLE);
                ll_binded_bankcard_without_address.setVisibility(View.GONE);
                bindCardState = 2;
            }
            tv_withdraw_bankname.setText(requestWithdrawBean.getCardName());
            //获取银行卡号码
            cardNo = requestWithdrawBean.getAccountBankcard().getCardno();
            tv_withdraw_bankcardnumber.setText("尾号" + cardNo.substring(cardNo.length() - 4));
//            tv_withdraw_tips.setText(requestWithdrawBean.getRemark());
            tv_withdraw_tips.setVisibility(View.GONE);
            Picasso.with(WithdrawActivity.this)
                    .load(requestWithdrawBean.getBankicon())
                    .into(iv_withdraw_bankcard_logo);
        }

        channel = requestWithdrawBean.getChannel();
        //账户余额
        accountBalance = requestWithdrawBean.getAmount();
        DecimalFormat df = new DecimalFormat("###,###,##0.00");
        if (accountBalance == 0) {
            tv_withdraw_account.setText("0.00元");
        } else {
            tv_withdraw_account.setText(df.format(accountBalance) + "元");
        }
    }


    /*点击edittext外部区域隐藏键盘*/
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }


    class ViewHolder {
        @InjectView(R.id.dialog_title)
        TextView dialogTitle;
        @InjectView(R.id.et_input_trade_pwd)
        EditText etInputTradePwd;
        @InjectView(R.id.line)
        TextView line;
        @InjectView(R.id.dialog_canclebtn)
        TextView dialogCanclebtn;
        @InjectView(R.id.dialog_confirmbtn)
        TextView dialogConfirmbtn;
        @InjectView(R.id.tv_forgetpaypwd)
        TextView tvForgetPaypwd;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
