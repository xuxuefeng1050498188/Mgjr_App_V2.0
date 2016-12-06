package com.mgjr.view.invester;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgjr.R;
import com.mgjr.Utils.ApplicationInfoUtils;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.KeyBoardUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.PhoneUtils;
import com.mgjr.Utils.PopwUtils;
import com.mgjr.Utils.SPUtils;
import com.mgjr.Utils.StringFormatUtil;
import com.mgjr.Utils.StringToBase64;
import com.mgjr.httpclient.HttpClient;
import com.mgjr.httpclient.callback.Callback;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.model.bean.ActivityGitBean;
import com.mgjr.model.bean.EventBusBean.HqbInvestCompleteBean;
import com.mgjr.model.bean.EventBusBean.InvestAmountBean;
import com.mgjr.model.bean.EventBusBean.JmgInvestCompleteBean;
import com.mgjr.model.bean.HqbBean;
import com.mgjr.model.bean.LoanBean;
import com.mgjr.model.bean.ProductInvestConfirmBean;
import com.mgjr.presenter.impl.ProductInvestConfirmPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.APIBuilder;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.CustomCommonDialog;
import com.mgjr.share.InputPayPwdPopw;
import com.mgjr.share.NetUtils;
import com.mgjr.view.common.CommonWebViewActivity;
import com.mgjr.view.invester.adapter.ActivityGiftListViewAdapter;
import com.mgjr.view.listeners.ViewListener;
import com.mgjr.view.profile.accountsetting.ProfileFindTradePwdActivity;
import com.mgjr.view.profile.rechargeWithdraw.RechargeActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.mgjr.Utils.HideInputUtils.isShouldHideInput;

public class InvestConfirmActivity extends ActionbarActivity implements ViewListener<ProductInvestConfirmBean>, View.OnClickListener {

    private static final int HQB_TYPE = 0;
    private static final int JMG_TYPE = 1;
    @InjectView(R.id.tv_product_name)
    TextView tvProductName;
    @InjectView(R.id.tv_product_rate)
    TextView tvProductRate;
    @InjectView(R.id.tv_product_period)
    TextView tvProductPeriod;
    @InjectView(R.id.tv_product_balance)
    TextView tvProductBalance;
    @InjectView(R.id.tv_confirm_invest_account_balance)
    TextView tvConfirmInvestAccountBalance;
    @InjectView(R.id.btn_recharge)
    Button btnRecharge;
    @InjectView(R.id.btn_recharge_all)
    TextView btnRechargeAll;
    @InjectView(R.id.et_invest_money)
    EditText etInvestMoney;
    @InjectView(R.id.ll_hqb_product_tips)
    LinearLayout llHqbProductTips;
    @InjectView(R.id.iv_user_touxiang)
    ImageView ivUserTouxiang;
    @InjectView(R.id.rl_jmg_red_packet)
    RelativeLayout rlJmgRedPacket;
    @InjectView(R.id.iv_ouzit)
    ImageView ivOuzit;
    @InjectView(R.id.rl_jmg_finance_ticket)
    RelativeLayout rlJmgFinanceTicket;
    @InjectView(R.id.tv_final_pay_money)
    TextView tvFinalPayMoney;
    @InjectView(R.id.tv_jmg_product_profit)
    TextView tvJmgProductProfit;
    @InjectView(R.id.btn_agreement_logo)
    ImageButton ibAgreementLogo;
    @InjectView(R.id.btn_confirm_buy)
    RelativeLayout btnConfirmBuy;
    @InjectView(R.id.ll_jmg_pay_state)
    LinearLayout llJmgPayState;
    @InjectView(R.id.rl_product_period)
    RelativeLayout rlProductPeriod;
    @InjectView(R.id.id_tv_without_red_packet)
    TextView idTvWithoutRedPacket;
    @InjectView(R.id.tv_used_red_packet)
    TextView tvUsedRedPacket;
    @InjectView(R.id.tv_sum_red_packet)
    TextView tvSumRedPacket;
    @InjectView(R.id.id_tv_has_red_packet)
    LinearLayout idTvHasRedPacket;
    @InjectView(R.id.iv_right_arrow_red_packet)
    ImageView ivRightArrowRedPacket;
    @InjectView(R.id.id_tv_without_finance_ticket)
    TextView idTvWithoutFinanceTicket;
    @InjectView(R.id.tv_used_finance_ticket)
    TextView tvUsedFinanceTicket;
    @InjectView(R.id.tv_sum_finance_ticket)
    TextView tvSumFinanceTicket;
    @InjectView(R.id.id_tv_has_finance_ticket)
    LinearLayout idTvHasFinanceTicket;
    @InjectView(R.id.iv_right_arrow_finance_ticket)
    ImageView ivRightArrowFinanceTicket;
    @InjectView(R.id.tv_canTenderAmount)
    TextView tvCanTenderAmount;
    @InjectView(R.id.textView7)
    TextView textView7;
    @InjectView(R.id.tv_confirm_submit)
    TextView tvConfirmSubmit;
    private ProductInvestConfirmPresenterImpl productInvestConfirmPresenterImpl;
    private String code;
    private String type;
    private PopupWindow loadingPopW;
    private ProductInvestConfirmBean productInvestConfirmBean;
    private HqbBean hqbBean;
    private boolean isSelectAgreementLogo = true;
    private LoanBean loanBean;
    /**
     * 加息券的集合
     */
    private List<ActivityGitBean> couponList;
    /**
     * 红包的集合
     */
    private List<ActivityGitBean> hbList;
    /**
     * 红包或者是加息券的类型的区分字段  1:红包 2:加息券
     */
    private int activityGiftType;
    private ActivityGiftListViewAdapter activityGiftListViewAdapter;
    private int calcedInvestMoney;
    private String investMoney;
    private int productType;
    private TextView tv_bondTransfer;
    private LinkedHashMap<Integer, ArrayList<ActivityGitBean>> processedCouponMap;
    private boolean hasAuthRealName;
    private LinearLayout layout_forgetpaypwd;
    private LinearLayout ll_cancle_confirm_btn;
    private RelativeLayout rl_checking_anim;
    private ImageView iv_checking;
    private TextView tv_paypwd_checking;
    private ObjectAnimator rotateAnim;
    private TextView tv_paypwd_state;
    private InputPayPwdPopw inputPayPwdPopw;
    private boolean isBackup = true;
    private PopupWindow showRealNameAuthPopw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_confirm_investing, this);
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);
        productInvestConfirmPresenterImpl = new ProductInvestConfirmPresenterImpl(this);
        //拿到产品详情传递传递过来的code,和用来区分是活期宝还是金芒果的type
        code = getIntent().getStringExtra("code");
        type = getIntent().getStringExtra("type");
        hasAuthRealName = (boolean) SPUtils.get(this, "LOGIN", "isTruename", false);
        //实名认证
        if (hasAuthRealName) {
            networkRequest();
        }
        //初始化背景颜色与布局
        initBackground();
        //给金额输入框添加监听事件,改变完之后需要刷新数据
        setPricePoint(etInvestMoney);
        initActionBar();
        //初始化支付密码弹窗
        initPayPwdPopw();
    }

    private void initPayPwdPopw() {
        inputPayPwdPopw = new InputPayPwdPopw();
        inputPayPwdPopw.setClickBtnListener(new InputPayPwdPopw.ClickBtnListener() {
            @Override
            public void clickConfirmBtn() {
                String paypassword = inputPayPwdPopw.getEtInputTradePwd().getText().toString();

                if (paypassword.equalsIgnoreCase("")) {
                    CommonToastUtils.showToast(InvestConfirmActivity.this, "密码不能为空");
                    return;
                } else if (paypassword.length() < 6 || paypassword.length() > 16) {
                    CommonToastUtils.showToast(InvestConfirmActivity.this, "密码的长度必须是6到16位之间");
                    return;
                } else if (paypassword.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0) {
                    // 包含特殊字符
                    CommonToastUtils.showToast(InvestConfirmActivity.this, "密码不能有特殊字符");
                    inputPayPwdPopw.getEtInputTradePwd().requestFocus();
                    return;
                }
                //提交数据,请求数据
                checkRequest(paypassword);
                inputPayPwdPopw.startCheckingAnim();
                KeyBoardUtils.closeKeybord(inputPayPwdPopw.getEtInputTradePwd(), InvestConfirmActivity.this);
            }

            @Override
            public void clickForgetPwdBtn() {
                KeyBoardUtils.closeKeybord(inputPayPwdPopw.getEtInputTradePwd(), InvestConfirmActivity.this);
                MyActivityManager.getInstance().startNextActivity(ProfileFindTradePwdActivity.class);
            }
        });
    }

    private void initActionBar() {

        actionbar.setRightImageView(R.drawable.confirminvest_question_btn, this);
        actionbar.rightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivityManager.getInstance().startNextActivity(CommonWebViewActivity.class, "investmentProblem");
            }
        });

        actionbar.leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etInvestMoney.getText().toString())) {
                    final CustomCommonDialog dialog = new CustomCommonDialog(InvestConfirmActivity.this, "", "收益这么高的产品，确定放弃购买吗？", "我再想想", "去意已决", true);
                    dialog.setClicklistener(new CustomCommonDialog.ClickListenerInterface() {
                        @Override
                        public void doConfirm() {
//                            popActivity();
                            dialog.dismiss();
                        }

                        @Override
                        public void doCancel() {
                            MyActivityManager.getInstance().popCurrentActivity();
                        }
                    });
                    dialog.show();
                } else {
//                    popActivity();
                    MyActivityManager.getInstance().popCurrentActivity();
                }
            }
        });

        tv_bondTransfer = (TextView) findViewById(R.id.tv_bondTransfer);
        tv_bondTransfer.setOnClickListener(this);
    }


    private void initBackground() {
        if ("0".equals(type)) {
            actionbar.setCenterTextView("活期宝转入");
            tvConfirmSubmit.setText("确认转入");
            rlJmgRedPacket.setVisibility(View.GONE);
            rlJmgFinanceTicket.setVisibility(View.GONE);
//            llJmgPayState.setVisibility(View.GONE);
            rlProductPeriod.setVisibility(View.GONE);
            tvProductName.setTextColor(0xff00a0ea);
            btnConfirmBuy.setBackgroundColor(0xff00a0ea);
            ibAgreementLogo.setImageResource(R.drawable.selected_gift);
            etInvestMoney.setHint("1元起投,当日计息");
        } else {
            actionbar.setCenterTextView("确认投资");
            llHqbProductTips.setVisibility(View.GONE);
            ibAgreementLogo.setImageResource(R.drawable.confirminvest_check_agree_btn);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            //实名认证
            hasAuthRealName = (boolean) SPUtils.get(this, "LOGIN", "isTruename", false);
            isRealNameAuth();
        }
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        //实名认证
//        if (hasAuthRealName) {
//            //请求网络
//            networkRequest();
//        }
//    }

    private boolean isRealNameAuth() {

        if (!hasAuthRealName) {
            View rootView = LayoutInflater.from(this).inflate(R.layout.activity_confirm_investing, null);
            showRealNameAuthPopw = PopwUtils.showRealNameAuthPopw(this, rootView);
        } else {
            if (showRealNameAuthPopw != null) {
                showRealNameAuthPopw.dismiss();
                PopwUtils.backgroundAlpha(1, this);
            }
//            PopwUtils.dismissLoadingPopw(showRealNameAuthPopw);
        }
        return hasAuthRealName;
    }

    private void networkRequest() {
        Map<String, String> necessaryParams = new HashMap<String, String>();
        necessaryParams.put("mid", "" + SPUtils.get(this, "LOGIN", "id", 0));
        necessaryParams.put("code", code);
        necessaryParams.put("type", type);
        productInvestConfirmPresenterImpl.sendRequest(necessaryParams, null);
//        LogUtil.e("申请投资确认的网络数据");
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void eventBus(InvestAmountBean event) {
        etInvestMoney.setText(event.getInvestMoney());
        if (!TextUtils.isEmpty(event.getInvestMoney())) {
            calcedInvestMoney = (int) (Double.parseDouble(event.getInvestMoney()) / 100);
        } else {
            calcedInvestMoney = 0;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);
    }

    public void setPricePoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
                if (s.length() > 8) {
                    editText.setText(s.subSequence(0, 8));
                    editText.setSelection(8);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String temp = s.toString();
                if (temp.length() == 1 && temp.equals("0")) {
                    s.clear();
                    return;
                }
                //特别注意:投资金额以及计算之后的的金额应该在每次输入的时候计算,不能再打开弹窗重新计算,否则出错
                investMoney = etInvestMoney.getText().toString();
                if ("1".equals(type)) {
                    //如果是金芒果才需要做这些
                    if (!TextUtils.isEmpty(investMoney)) {
                        calcedInvestMoney = (int) (Double.parseDouble(investMoney) / 100);
                    } else {
                        calcedInvestMoney = 0;
                    }
                    //如果充值金额有变化,重置红包和加息券数据
                    resetData(1);
                    resetData(2);
                    //更新界面
                    refreshCouponData();
                    refreshHbData();
                }
                //设置预期收益
                setFinalData();

            }
        });

    }

    private void setFinalData() {
        String money = etInvestMoney.getText().toString();
        if (!TextUtils.isEmpty(money)) {
            double actualMoney = Double.parseDouble(money);
            if ("1".equals(type)) {
                //金芒果
                llJmgPayState.setVisibility(View.VISIBLE);
                String payMoney = new DecimalFormat("###,###,##0.00").format(actualMoney);
                tvFinalPayMoney.setText(payMoney + "元");
                double sum = 0;
                for (int i = 0; i < couponList.size(); i++) {
                    if (couponList.get(i).isSelected()) {
                        sum += couponList.get(i).getRate();
                    }
                }
                double profit = actualMoney * (sum + (loanBean.getRate())) / 12 / 100 * loanBean.getPeriod();
                String payProfit = new DecimalFormat("###,###,##0.00").format(profit);
                tvJmgProductProfit.setText(payProfit + "元");

            } else {
                //活期宝
                String payMoney = new DecimalFormat("###,###,##0.00").format(actualMoney);
                tvFinalPayMoney.setText(payMoney + "元");
                double profit = actualMoney * (9.5 / 100.0);
                String payProfit = new DecimalFormat("###,###,##0.00").format(profit);
                tvJmgProductProfit.setText(payProfit + "元");
            }
        } else {
            tvFinalPayMoney.setText("0.00元");
            tvJmgProductProfit.setText("0.00元");
        }
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
    public void showError(OnPresenterListener listener, ProductInvestConfirmBean productInvestConfirmBean) {
        dismissLoadingDialog();
        CommonToastUtils.showToast(this, productInvestConfirmBean.getMsg());
    }

    @Override
    public void responseData(OnPresenterListener listener, ProductInvestConfirmBean productInvestConfirmBean) {
        this.productInvestConfirmBean = productInvestConfirmBean;
        if (productInvestConfirmBean.getHqb() != null && productInvestConfirmBean.getLoan() == null) {
            productType = HQB_TYPE;

            code = productInvestConfirmBean.getHqb().getCode();

        } else if (productInvestConfirmBean.getHqb() == null && productInvestConfirmBean.getLoan() != null) {
            productType = JMG_TYPE;
        }
        if ("0".equals(type)) {
            //活期宝
            hqbBean = productInvestConfirmBean.getHqb();
            if (hqbBean != null) {
                bindHqbData();
            }
        } else {
            //金芒果或者新手福利标
            loanBean = productInvestConfirmBean.getLoan();
            couponList = productInvestConfirmBean.getCouponList();
            hbList = productInvestConfirmBean.getHbList();
            if (loanBean.getPeriod() == 1) {
                etInvestMoney.setHint("100元起购");
                rlJmgFinanceTicket.setVisibility(View.GONE);
            } else {
                etInvestMoney.setHint("1000元起购");
            }
            bindJmgData();
        }
    }

    private void bindJmgData() {
        if (loanBean.getPeriod() == 1) {
            //新手福利标
            tvProductName.setText("新手福利标");
        } else {
            tvProductName.setText(loanBean.getTitle());
        }
        tvProductRate.setText("" + (loanBean.getRate()) + "%");
        tvProductPeriod.setText("" + loanBean.getPeriod() + "个月");
        String productBalance = new DecimalFormat("###,###,##0.00").format(loanBean.getBalance());
        tvProductBalance.setText("" + productBalance);
        if (productInvestConfirmBean.getAccountBalance() != null) {
            String accountBalance = new DecimalFormat("###,###,##0.00").format(productInvestConfirmBean.getAccountBalance());
            tvConfirmInvestAccountBalance.setText("" + accountBalance + "元");
        }
        //设置红包的数据
        if (hbList.size() > 0) {
            //有红包,设置数据
            Double hbAmount = productInvestConfirmBean.getHbAmount();
            tvSumRedPacket.setText("" + hbAmount + "元可用");
            tvUsedRedPacket.setText("未使用红包");
            //整行可点击
            rlJmgRedPacket.setVisibility(View.VISIBLE);
            rlJmgRedPacket.setClickable(true);

        } else {
            //没有红包
            /*idTvWithoutRedPacket.setVisibility(View.VISIBLE);
            idTvHasRedPacket.setVisibility(View.GONE);
            ivRightArrowRedPacket.setVisibility(View.GONE);*/
            //整行不可点击
            rlJmgRedPacket.setClickable(false);
            rlJmgRedPacket.setVisibility(View.GONE);
        }

        //设置加息券的数据
        if (couponList.size() > 0) {
            //有加息券,设置数据
            tvSumFinanceTicket.setText("" + couponList.size() + "张可用");
            tvUsedFinanceTicket.setText("未使用加息券");
            //整行可点击
            rlJmgFinanceTicket.setVisibility(View.VISIBLE);
            rlJmgFinanceTicket.setClickable(true);
        } else {
            //没有加息券
            /*idTvWithoutFinanceTicket.setVisibility(View.VISIBLE);
            idTvHasFinanceTicket.setVisibility(View.GONE);
            ivRightArrowFinanceTicket.setVisibility(View.GONE);*/
            //整行不可点击
            rlJmgFinanceTicket.setClickable(false);
            rlJmgFinanceTicket.setVisibility(View.GONE);
        }
        setFinalData();
        /*tvFinalPayMoney.setText("0.00元");
        tvJmgProductProfit.setText("0.00元");*/
    }

    private void bindHqbData() {
        if (productInvestConfirmBean.getCanTenderAmount() != 0) {
            tvCanTenderAmount.setText("" + new DecimalFormat("###,###,##0.00").format(productInvestConfirmBean.getCanTenderAmount()) + "元");
        }
        tvProductName.setText("活期宝");
        textView7.setText("预期一年收益");
        tvProductRate.setText(productInvestConfirmBean.getHbqRate());
        String productBalance = new DecimalFormat("###,###,##0.00").format(hqbBean.getBalance());
        tvProductBalance.setText("" + productBalance);
        String accountBalance = new DecimalFormat("###,###,##0.00").format(productInvestConfirmBean.getAccountBalance());
        tvConfirmInvestAccountBalance.setText("" + accountBalance + "元");
        /*tvFinalPayMoney.setText("0.00元");
        tvJmgProductProfit.setText("0.00元");*/
        setFinalData();
    }


    @OnClick({R.id.btn_recharge, R.id.btn_recharge_all, R.id.rl_jmg_red_packet, R.id.rl_jmg_finance_ticket, R.id.btn_confirm_buy, R.id.btn_agreement_logo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_recharge:
                //点击充值
                MyActivityManager.getInstance().startNextActivity(RechargeActivity.class);
                break;
            case R.id.btn_recharge_all:
                if ("0".equals(type)) {
                    int money = (productInvestConfirmBean.getAccountBalance() > hqbBean.getBalance() ? (hqbBean.getBalance()) : (productInvestConfirmBean.getAccountBalance())).intValue();
                    etInvestMoney.setText("" + money);
                } else {
                    etInvestMoney.setText("" + (productInvestConfirmBean.getAccountBalance() > loanBean.getBalance() ? loanBean.getBalance() : productInvestConfirmBean.getAccountBalance()).intValue());
                }
                break;
            case R.id.btn_agreement_logo:
                if (isSelectAgreementLogo) {
                    ibAgreementLogo.setImageResource(R.drawable.unselected_gift);
                } else {
                    if ("0".equals(type)) {
                        ibAgreementLogo.setImageResource(R.drawable.selected_gift);
                    } else {
                        ibAgreementLogo.setImageResource(R.drawable.confirminvest_check_agree_btn);
                    }
                }
                isSelectAgreementLogo = !isSelectAgreementLogo;
                break;

            case R.id.rl_jmg_red_packet:

                //红包选择弹窗
                activityGiftType = 1;
                /*if (Double.parseDouble(etInvestMoney.getText().toString()) < 1000) {
                    CommonToastUtils.showToast(this, "投资金额不能小于1000!");
                    return;
                }*/
                showActivityGitPopupW(activityGiftType);
                break;
            case R.id.rl_jmg_finance_ticket:
                /*if (Double.parseDouble(etInvestMoney.getText().toString()) < 1000) {
                    CommonToastUtils.showToast(this, "投资金额不能小于1000!");
                    return;
                }*/
                //加息券选择弹窗
                activityGiftType = 2;
                showActivityGitPopupW(activityGiftType);
                break;
            case R.id.btn_confirm_buy:
                //检验数据是否合法,并与后台交互
                checkInvestData();
                break;
            case R.id.tv_bondTransfer:
                MyActivityManager.getInstance().startNextActivity(CommonWebViewActivity.class, "bondTransfer");
                break;
        }
    }

    /**
     * 红包或者加息券的弹窗
     *
     * @param activityGiftType
     */
    private void showActivityGitPopupW(final int activityGiftType) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.layout_red_packet_and_finace, null);
        //初始化
        TextView tv_selected_git_type = (TextView) contentView.findViewById(R.id.tv_selected_git_type);
        TextView tv_useable_activity_gift = (TextView) contentView.findViewById(R.id.tv_useable_activity_gift);
        final TextView tv_selected_activity_gift = (TextView) contentView.findViewById(R.id.tv_selected_activity_gift);
        ListView activity_gift_listview = (ListView) contentView.findViewById(R.id.activity_gift_listview);
        ImageButton invest_calc_cancle = (ImageButton) contentView.findViewById(R.id.invest_calc_cancle);
        RelativeLayout btn_confirm_invest = (RelativeLayout) contentView.findViewById(R.id.btn_confirm_invest);

        final PopupWindow selectActivityGiftPopupw = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //设置动画所对应的style
        selectActivityGiftPopupw.setAnimationStyle(R.style.jmg_invest_anim);
        //点击空白处时，隐藏掉pop窗口
        selectActivityGiftPopupw.setBackgroundDrawable(new ColorDrawable());
        selectActivityGiftPopupw.setFocusable(true);
        selectActivityGiftPopupw.setOutsideTouchable(true);
        PopwUtils.backgroundAlpha(0.5f, this);
        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_confirm_investing, null);
        selectActivityGiftPopupw.showAtLocation(rootView, Gravity.CENTER, 0, 0);

        //把红包和加息券的数据做一个备份
        final List<ActivityGitBean> copyHbList = new ArrayList<>();
//        copyHbList.addAll(hbList);
        backUpData(copyHbList, hbList);
        final List<ActivityGitBean> copyCouponList = new ArrayList<>();
        backUpData(copyCouponList, couponList);
        //备份按照比例计算之后可用红包的金额
        final int copyCalcedInvestMoney = this.calcedInvestMoney;

        if (activityGiftType == 1) {
            //红包
            tv_selected_git_type.setText("选择红包");
            //可使用红包
            final Double hbAmount = productInvestConfirmBean.getHbAmount();
            StringFormatUtil useableRedPacket = new StringFormatUtil(this, "可使用" + hbAmount + "元", "" + hbAmount, R.color.text1).fillColor();
            tv_useable_activity_gift.setText(useableRedPacket.getResult());
            //已选择红包
            int sum = getSumSelectHb();
            StringFormatUtil selectedRedPacket = new StringFormatUtil(this, "已选择" + sum + "元", "" + sum, R.color.text1).fillColor();
            tv_selected_activity_gift.setText(selectedRedPacket.getResult());
            //处理红包数据

            processHbData(this.calcedInvestMoney);
            activityGiftListViewAdapter = new ActivityGiftListViewAdapter(activityGiftType, hbList);
            activity_gift_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //点击某个条目,先记录当前状态
                    boolean previousStatus = hbList.get(position).isSelected();
                    hbList.get(position).setSelected(!previousStatus);
                    //已选择红包
                    int sum = getSumSelectHb();
                    //如果点击之前的状态是true,需要加上开始减去的投资金额
                    if ((previousStatus == true)) {
                        InvestConfirmActivity.this.calcedInvestMoney += hbList.get(position).getAmount();
                    } else {
                        InvestConfirmActivity.this.calcedInvestMoney -= hbList.get(position).getAmount();
                    }
                    StringFormatUtil selectedRedPacket = new StringFormatUtil(InvestConfirmActivity.this, "已选择" + sum + "元", "" + sum, R.color.text1).fillColor();
                    tv_selected_activity_gift.setText(selectedRedPacket.getResult());
                    //重新处理集合
                    processHbData(InvestConfirmActivity.this.calcedInvestMoney);
                    activityGiftListViewAdapter.notifyDataSetChanged();
                }
            });
        } else {
            //加息券
            tv_selected_git_type.setText("选择加息券");
            //可使用加息券
            StringFormatUtil useableFinaceTicket = new StringFormatUtil(this, "可使用" + couponList.size() + "张", "" + couponList.size(), R.color.text1).fillColor();
            tv_useable_activity_gift.setText(useableFinaceTicket.getResult());
            //已选择加息券
            int sum = getSumSelectedCoupon();
            StringFormatUtil selectedFinaceTicket = new StringFormatUtil(this, "已选择" + sum + "张", "" + sum, R.color.text1).fillColor();
            tv_selected_activity_gift.setText(selectedFinaceTicket.getResult());
            activityGiftListViewAdapter = new ActivityGiftListViewAdapter(activityGiftType, couponList);
            processCouponData();
            activity_gift_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ActivityGitBean activityGitBean = couponList.get(position);
                    updataCouonData(activityGitBean);
                    //点击某个条目,先记录当前状态
                    /*boolean previousStatus = couponList.get(position).isSelected();
                    couponList.get(position).setSelected(!previousStatus);*/
                    //已选择加息券
                    int sum = getSumSelectedCoupon();
                    StringFormatUtil selectedFinaceTicket = new StringFormatUtil(InvestConfirmActivity.this, "已选择" + sum + "张", "" + sum, R.color.text1).fillColor();
                    tv_selected_activity_gift.setText(selectedFinaceTicket.getResult());
                    activityGiftListViewAdapter.notifyDataSetChanged();
                }
            });
        }
        //设置listview数据
        activity_gift_listview.setAdapter(activityGiftListViewAdapter);

        //点击取消按钮
        invest_calc_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //是否是备份数据
                isBackup = true;
//                refreshHbData();
//                refreshCouponData();
                selectActivityGiftPopupw.dismiss();
            }

        });

       /* selectActivityGiftPopupw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopwUtils.backgroundAlpha(1f, InvestConfirmActivity.this);
                refreshHbData();
                refreshCouponData();

            }
        });*/

        //点击确定选中按钮
        btn_confirm_invest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityGiftType == 1) {
                    //确认活期宝选中数据
                    refreshHbData();
                    isBackup = false;
                    selectActivityGiftPopupw.dismiss();
                } else {
                    //确认金芒果选中数据
                    isBackup = false;
                    refreshCouponData();
                    selectActivityGiftPopupw.dismiss();
                }
            }
        });

        selectActivityGiftPopupw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (isBackup) {
                    //点击取消,恢复到进入弹窗的初始化状态
                    hbList.clear();
                    couponList.clear();
                    backUpData(hbList, copyHbList);
//                hbList = copyHbList;
//                couponList = copyCouponList;
                    backUpData(couponList, copyCouponList);
                    InvestConfirmActivity.this.calcedInvestMoney = copyCalcedInvestMoney;
                }
                //当退出弹窗的时候,重置isBackup
                isBackup = true;
                refreshHbData();
                refreshCouponData();
                setFinalData();
                PopwUtils.backgroundAlpha(1f, InvestConfirmActivity.this);
            }
        });

    }

    private void backUpData(List<ActivityGitBean> copyList, List<ActivityGitBean> hbList) {
        for (int i = 0; i < hbList.size(); i++) {
            ActivityGitBean bean = new ActivityGitBean();
            bean.setSelected(hbList.get(i).isSelected());
            bean.setUseable(hbList.get(i).isUseable());
            bean.setAmount(hbList.get(i).getAmount());
            bean.setTitle(hbList.get(i).getTitle());
            bean.setEtime(hbList.get(i).getEtime());
            bean.setId(hbList.get(i).getId());
            bean.setMid(hbList.get(i).getMid());
            bean.setRemark(hbList.get(i).getRemark());
            bean.setRate(hbList.get(i).getRate());
            bean.setFlag(hbList.get(i).getFlag());
            bean.setGtime(hbList.get(i).getGtime());
            bean.setStatus(hbList.get(i).getStatus());
            bean.setTenderid(hbList.get(i).getTenderid());
            bean.setNoatamount(hbList.get(i).getNoatamount());
            bean.setTypeid(hbList.get(i).getTypeid());
            bean.setStime(hbList.get(i).getStime());
            copyList.add(bean);
        }
    }


    private void updataCouonData(ActivityGitBean activityGitBean) {
        Integer typeid = activityGitBean.getTypeid();
        ArrayList<ActivityGitBean> arrayList = processedCouponMap.get(typeid);
        for (int i = 0; i < arrayList.size(); i++) {
            ActivityGitBean bean = arrayList.get(i);
            if (activityGitBean == bean) {
                bean.setSelected(!bean.isSelected());
                bean.setUseable(true);
            } else {
//                bean.setSelected(false);
                bean.setUseable(!bean.isUseable());
            }
        }
        //迭代map
        couponList.clear();
        Iterator iter = processedCouponMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            couponList.addAll((Collection<? extends ActivityGitBean>) entry.getValue());
        }
    }

    /**
     * 处理加息券集合的数据
     */
    private void processCouponData() {
        processedCouponMap = new LinkedHashMap<>();
        for (int i = 0; i < couponList.size(); i++) {
            ActivityGitBean couponBean = couponList.get(i);
            ArrayList<ActivityGitBean> arrayList = processedCouponMap.get(couponBean.getTypeid());
            if (arrayList == null) {
                arrayList = new ArrayList<>();
                arrayList.add(couponBean);
                processedCouponMap.put(couponBean.getTypeid(), arrayList);
            } else {
                arrayList.add(couponBean);
            }
        }
        //迭代map
        couponList.clear();
        Iterator iter = processedCouponMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            couponList.addAll((Collection<? extends ActivityGitBean>) entry.getValue());
        }
    }

    private int getSumSelectedCoupon() {
        int sum = 0;
        for (int i = 0; i < couponList.size(); i++) {
            if (couponList.get(i).isSelected()) {
                sum++;
            }
        }
        return sum;
    }

    private int getSumSelectHb() {
        int sum = 0;
        for (int i = 0; i < hbList.size(); i++) {
            if (hbList.get(i).isSelected()) {
                sum += hbList.get(i).getAmount();
            }
        }
        return sum;
    }

    /**
     * 刷新加息券界面的数据
     */
    private void refreshCouponData() {
        double sum = 0;
        for (int i = 0; i < couponList.size(); i++) {
            if (couponList.get(i).isSelected()) {
                sum += couponList.get(i).getRate();
            }
        }
//        tvUsedFinanceTicket.setText((sum == 0) ? "未使用加息券" : "已加息" + sum + "%");
        if (sum == 0) {
            tvUsedFinanceTicket.setText("未使用加息券");
            tvUsedFinanceTicket.setTextColor(0xff666666);
        } else {
            tvUsedFinanceTicket.setText("已加息" + sum + "%");
            tvUsedFinanceTicket.setTextColor(0xff23aff8);
        }
    }

    /**
     * 刷新红包界面的数据
     */
    private void refreshHbData() {
        int sum = getSumSelectHb();
//        tvUsedRedPacket.setText((sum == 0) ? "未使用红包" : "已使用" + sum + "元");
        if (sum == 0) {
            tvUsedRedPacket.setText("未使用红包");
            tvUsedRedPacket.setTextColor(0xff666666);
        } else {
            tvUsedRedPacket.setText("已使用" + sum + "元");
            tvUsedRedPacket.setTextColor(0xff23aff8);
        }
    }

    /**
     * 重置红包或者加息券的数据
     *
     * @param activityGiftType
     */
    private void resetData(int activityGiftType) {
        if (hbList == null || couponList == null) {
            return;
        }
        if (activityGiftType == 1) {
            for (int i = 0; i < hbList.size(); i++) {
                hbList.get(i).setSelected(false);
                hbList.get(i).setUseable(true);
            }
        } else {
            for (int i = 0; i < couponList.size(); i++) {
                couponList.get(i).setSelected(false);
                couponList.get(i).setUseable(true);
            }
        }
    }

    /**
     * @param investMoney
     */
    private void processHbData(int investMoney) {
        //可用红包集合
        List<ActivityGitBean> useableRedPacket = new ArrayList<>();
        //不可用红包
        List<ActivityGitBean> unUseableRedPacket = new ArrayList<>();
        //按比例计算完毕之后的投资金额大小

        for (int i = 0; i < hbList.size(); i++) {
            ActivityGitBean activityGitBean = hbList.get(i);
            if (activityGitBean.isSelected()) {
                activityGitBean.setUseable(true);
//                useableRedPacket.add(activityGitBean);
                continue;
            }
            double amount = activityGitBean.getAmount();
            if (investMoney < amount) {
                //如果红包金额小于按比例计算之后的投资金额,该红包不可用
                activityGitBean.setUseable(false);
//                unUseableRedPacket.add(activityGitBean);
            } else {
                //可用的红包
                activityGitBean.setUseable(true);
//                useableRedPacket.add(activityGitBean);
            }
        }
        /*hbList.clear();
        hbList.addAll(useableRedPacket);
        hbList.addAll(unUseableRedPacket);*/
    }

    private void checkInvestData() {
        if (!isSelectAgreementLogo) {
            CommonToastUtils.showToast(this, "请先同意《债权转让协议》");
            return;
        }
        String touziMoney = etInvestMoney.getText().toString();
        if (TextUtils.isEmpty(touziMoney)) {
            CommonToastUtils.showToast(this, "请输入充值金额");
            return;
        }
        double touzi = Double.parseDouble(touziMoney);
        if (touzi > productInvestConfirmBean.getAccountBalance()) {
            CommonToastUtils.showToast(this, "账户余额不足,请重新输入");
            return;
        }
        if (productType == HQB_TYPE) {
            if (touzi < 1) {
                CommonToastUtils.showToast(this, "起投金额为1元,请重新输入");
                return;
            }
            double syketou = hqbBean.getBalance();// 剩余可投
            if (touzi > syketou) {
                CommonToastUtils.showToast(this, "可投余额不足,请重新输入");
                return;
            }
          /*  if (touzi > 200000) {
                CommonToastUtils.showToast(this, "活期宝投资金额不得超过20万");
                return;
            }*/
        }
        if (productType == JMG_TYPE) {
            double syketou = loanBean.getBalance();// 剩余可投
            if (syketou < loanBean.getMint()) {
                if (touzi > syketou) {
                    CommonToastUtils.showToast(this, "可投余额不足,请重新输入");
                    return;
                }
            } else {
                if (touzi < loanBean.getMint()) {
                    CommonToastUtils.showToast(this, "起投金额为" + loanBean.getMint() + "元,请重新输入");
                    return;
                }
                if (touzi > syketou) {
                    CommonToastUtils.showToast(this, "可投余额不足,请重新输入");
                    return;
                }
            }
        }
        if ((null != hbList && hbList.size() != 0 && getSumSelectHb() == 0) || (null != couponList && couponList.size() != 0) && getSumSelectedCoupon() == 0) {
            final CustomCommonDialog dialog = new CustomCommonDialog(this, "温馨提示", "您还有未使用的红包或加息券,确认继续吗？", "返回", "继续", false);
            dialog.show();
            dialog.setClicklistener(new CustomCommonDialog.ClickListenerInterface() {
                @Override
                public void doConfirm() {
                    dialog.dismiss();
                }

                @Override
                public void doCancel() {
                    //弹出输入交易密码
                    dialog.dismiss();
                    View rootView = LayoutInflater.from(InvestConfirmActivity.this).inflate(R.layout.activity_confirm_investing, null);
                    inputPayPwdPopw.showInputTradePwdPopw(InvestConfirmActivity.this, rootView);
                }
            });
        } else {
            View rootView = LayoutInflater.from(InvestConfirmActivity.this).inflate(R.layout.activity_confirm_investing, null);
            inputPayPwdPopw.showInputTradePwdPopw(InvestConfirmActivity.this, rootView);
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
        layout_forgetpaypwd = (LinearLayout) contentView.findViewById(R.id.layout_forgetpaypwd);
        ll_cancle_confirm_btn = (LinearLayout) contentView.findViewById(R.id.ll_cancle_confirm_btn);
        rl_checking_anim = (RelativeLayout) contentView.findViewById(R.id.rl_checking_anim);
        iv_checking = (ImageView) contentView.findViewById(R.id.iv_checking);
        tv_paypwd_checking = (TextView) contentView.findViewById(R.id.tv_paypwd_checking);
        tv_paypwd_state = (TextView) contentView.findViewById(R.id.tv_paypwd_state);
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
                                                    String paypassword = etInputTradePwd.getText().toString();

                                                    if (paypassword.equalsIgnoreCase("")) {
                                                        CommonToastUtils.showToast(InvestConfirmActivity.this, "密码不能为空！");
                                                        return;
                                                    } else if (paypassword.length() < 6 || paypassword.length() > 16) {
                                                        CommonToastUtils.showToast(InvestConfirmActivity.this, "密码的长度必须是6到16位之间");
                                                        return;
                                                    } else if (paypassword.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0) {
                                                        // 包含特殊字符
                                                        CommonToastUtils.showToast(InvestConfirmActivity.this, "密码不能有特殊字符！");
                                                        etInputTradePwd.requestFocus();
                                                        return;
                                                    }
                                                    //提交数据,请求数据
                                                    checkRequest(paypassword);
                                                    startCheckingAnim();
                                                    KeyBoardUtils.closeKeybord(etInputTradePwd, activity);
                                                }
                                            }

        );
        return inputTradePwdPopw;
    }

    private void startCheckingAnim() {
        layout_forgetpaypwd.setVisibility(View.GONE);
        ll_cancle_confirm_btn.setVisibility(View.GONE);
        rl_checking_anim.setVisibility(View.VISIBLE);
        rotateAnim = ObjectAnimator.ofFloat(iv_checking, "rotation", 0f, 360f);
        rotateAnim.setDuration(500);
        rotateAnim.setRepeatCount(ValueAnimator.RESTART);
        rotateAnim.setRepeatCount(10000);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.start();
    }

    private void paypwdCheckingError(String errorMsg) {
        layout_forgetpaypwd.setVisibility(View.VISIBLE);
        ll_cancle_confirm_btn.setVisibility(View.VISIBLE);
        rl_checking_anim.setVisibility(View.GONE);
        if (rotateAnim != null) {
            rotateAnim.cancel();
        }
        tv_paypwd_state.setVisibility(View.VISIBLE);
        tv_paypwd_state.setText(errorMsg);
    }


    private void paypwdCheckingSuccess() {
        if (rotateAnim != null) {
            rotateAnim.cancel();
        }
        iv_checking.clearAnimation();
        iv_checking.setRotation(0);
        iv_checking.setImageResource(R.drawable.paypwd_check_success_bg);
        tv_paypwd_checking.setText("");
    }*/

    /**
     * 提交数据,确认投资
     *
     * @param paypassword
     */
    private void checkRequest(String paypassword) {
        if ("0".equals(type)) {
            //活期宝
            submitHqbInvestData(paypassword);
        } else {
            //金芒果
            submitJmgInvestData(paypassword);
        }
    }

    /**
     * 金芒果确认购买
     *
     * @param paypassword
     */
    private void submitJmgInvestData(String paypassword) {
        String url = APIBuilder.baseUrl + APIBuilder.version + "appJmg/doTender";
        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        Map<String, String> unNecessaryParams = new HashMap<String, String>();
        necessaryParams.put("mid", "" + SPUtils.get(this, "LOGIN", "id", 0));
        necessaryParams.put("code", code);
        necessaryParams.put("device", "ANDROID");
        String channelId = ApplicationInfoUtils.getAppMetaData(this, "UMENG_CHANNEL");
        if (TextUtils.isEmpty(channelId)) {
            channelId = "21";
        }
        necessaryParams.put("mark", channelId);
        necessaryParams.put("amount", etInvestMoney.getText().toString().trim());
        necessaryParams.put("paypwd", StringToBase64.stringToBase64(paypassword));

        List<String> keyList = new ArrayList<String>();
        keyList.add("appJmg/doTender");
        keyList.add(necessaryParams.get("mid"));
        keyList.add(necessaryParams.get("code"));
        keyList.add(necessaryParams.get("amount"));
        keyList.add(necessaryParams.get("paypwd"));
        keyList.add(necessaryParams.get("device"));
        keyList.add(necessaryParams.get("mark"));
        String keyStr = APIBuilder.getKeyStr(keyList);
        necessaryParams.put("keyStr", keyStr);

        unNecessaryParams.put("gifts", jointHbData());
        unNecessaryParams.put("coupons", jointCouponData());

        Map<String, String> params = new HashMap<String, String>();
        params.putAll(necessaryParams);
        params.putAll(unNecessaryParams);
        HttpClient
                .post()
                .url(url)
                .params(params)
                .build()
                .execute(new Callback() {
                             @Override
                             public Object parseResponse(Response response) throws Exception {
                                 String string = response.body().string();
                                 Gson gson = new GsonBuilder().create();
                                 JmgInvestCompleteBean jmgInvestCompleteBean = gson.fromJson(string, JmgInvestCompleteBean.class);
                                 return jmgInvestCompleteBean;
                             }

                             @Override
                             public void onError(Call call, Exception e) {
                                 PhoneUtils.saveSystemErrorInfo(e);
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
//                                 CommonToastUtils.showToast(InvestConfirmActivity.this, e.toString());

                             }

                             @Override
                             public void onResponse(Object response) {
                                 final JmgInvestCompleteBean jmgInvestCompleteBean = (JmgInvestCompleteBean) response;
                                 if (jmgInvestCompleteBean.getStatus().equalsIgnoreCase("0000")) {
                                     //投资成功
                                     inputPayPwdPopw.paypwdCheckingSuccess();
//                                     CommonToastUtils.showToast(InvestConfirmActivity.this, "金芒果投资成功!");
                                     SPUtils.put(MainApplication.getContext(), "LOGIN", "isFirstInvestment", false);
                                     new Handler().postDelayed(new Runnable() {
                                         @Override
                                         public void run() {
                                             MyActivityManager.getInstance().startNextActivity(InvestCompletedActivity.class);
//                                     popActivity();
                                             MyActivityManager.getInstance().finishSpecifiedActivity(InvestConfirmActivity.class);
                                             EventBus.getDefault().postSticky(jmgInvestCompleteBean);
                                         }
                                     }, 700);

                                 } else {
//                                     CommonToastUtils.showToast(InvestConfirmActivity.this, jmgInvestCompleteBean.getMsg());
                                     inputPayPwdPopw.paypwdCheckingError(jmgInvestCompleteBean.getMsg());
                                 }
                             }
                         }
                );
    }

    /**
     * 组拼红包数据
     *
     * @return
     */
    public String jointHbData() {
        List<ActivityGitBean> selectedHbList = new ArrayList<>();
        for (int i = 0; i < hbList.size(); i++) {
            ActivityGitBean activityGitBean = hbList.get(i);
            if (activityGitBean.isSelected()) {
                selectedHbList.add(activityGitBean);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < selectedHbList.size(); i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(selectedHbList.get(i).getId());
        }
        return sb.toString();
    }

    /**
     * 组拼加息券数据
     *
     * @return
     */
    public String jointCouponData() {
        List<ActivityGitBean> selectedCouponList = new ArrayList<>();
        for (int i = 0; i < couponList.size(); i++) {
            ActivityGitBean activityGitBean = couponList.get(i);
            if (activityGitBean.isSelected()) {
                selectedCouponList.add(activityGitBean);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < selectedCouponList.size(); i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(selectedCouponList.get(i).getId());
        }
        return sb.toString();
    }

    /**
     * 活期宝确认购买
     *
     * @param paypassword
     */
    private void submitHqbInvestData(String paypassword) {
        String url = APIBuilder.baseUrl + APIBuilder.version + "appHqb/doTender";
        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        necessaryParams.put("mid", "" + SPUtils.get(this, "LOGIN", "id", 0));
        necessaryParams.put("code", code);
        necessaryParams.put("device", "ANDROID");
//        String channelId = ApplicationInfoUtils.getAppMetaData(this, "");
        String channelId = ApplicationInfoUtils.getAppMetaData(this, "UMENG_CHANNEL");
        if (TextUtils.isEmpty(channelId)) {
            channelId = "21";
        }
        necessaryParams.put("mark", channelId);
        necessaryParams.put("amount", etInvestMoney.getText().toString().trim());
        necessaryParams.put("paypwd", StringToBase64.stringToBase64(paypassword));
        List<String> keyList = new ArrayList<String>();
        keyList.add("appHqb/doTender");
        keyList.add(necessaryParams.get("mid"));
        keyList.add(necessaryParams.get("code"));
        keyList.add(necessaryParams.get("amount"));
        keyList.add(necessaryParams.get("paypwd"));
        keyList.add(necessaryParams.get("device"));
        keyList.add(necessaryParams.get("mark"));
        String keyStr = APIBuilder.getKeyStr(keyList);

        necessaryParams.put("keyStr", keyStr);

        Map<String, String> params = new HashMap<String, String>();
        params.putAll(necessaryParams);
        HttpClient
                .post()
                .url(url)
                .params(params)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseResponse(Response response) throws Exception {
                        String string = response.body().string();
                        Gson gson = new GsonBuilder().create();
                        HqbInvestCompleteBean hqbInvestCompleteBean = gson.fromJson(string, HqbInvestCompleteBean.class);
                        return hqbInvestCompleteBean;
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        PhoneUtils.saveSystemErrorInfo(e);
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
                    }

                    @Override
                    public void onResponse(Object response) {
                        final HqbInvestCompleteBean hqbInvestCompleteBean = (HqbInvestCompleteBean) response;
                        if (hqbInvestCompleteBean.getStatus().equalsIgnoreCase("0000")) {
                            //投资成功
//                            CommonToastUtils.showToast(InvestConfirmActivity.this, "活期宝投资成功!");
//                            popActivity();
                            SPUtils.put(MainApplication.getContext(), "LOGIN", "isFirstInvestment", false);
                            inputPayPwdPopw.paypwdCheckingSuccess();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    MyActivityManager.getInstance().startNextActivity(InvestCompletedActivity.class);
                                    MyActivityManager.getInstance().finishSpecifiedActivity(InvestConfirmActivity.class);
                                    EventBus.getDefault().postSticky(hqbInvestCompleteBean);
                                }
                            }, 700);


                        } else {
//                            CommonToastUtils.showToast(InvestConfirmActivity.this, hqbInvestCompleteBean.getMsg());
                            inputPayPwdPopw.paypwdCheckingError(hqbInvestCompleteBean.getMsg());
                        }
                    }
                });
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
}
