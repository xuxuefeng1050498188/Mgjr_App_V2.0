package com.mgjr.view.profile.rechargeWithdraw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.Constant;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.PopwUtils;
import com.mgjr.Utils.SPUtils;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.model.bean.AuthCardInfo;
import com.mgjr.model.bean.MemberBean;
import com.mgjr.model.bean.RequestRechargeBean;
import com.mgjr.presenter.impl.RechargePresenterImpl;
import com.mgjr.presenter.impl.RequestRechargePresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.SubmitButton;
import com.mgjr.view.common.CommonWebViewActivity;
import com.mgjr.view.listeners.ViewListener;
import com.mgjr.view.profile.accountsetting.ProfileSelectBankActivity;
import com.mgjr.yintong.pay.utils.BaseHelper;
import com.mgjr.yintong.pay.utils.Constants;
import com.mgjr.yintong.pay.utils.Md5Algorithm;
import com.mgjr.yintong.pay.utils.MobileSecurePayer;
import com.mgjr.yintong.pay.utils.PayOrder;
import com.mgjr.yintong.secure.demo.env.EnvConstants;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.mgjr.Utils.HideInputUtils.isShouldHideInput;

/**
 * Created by Administrator on 2016/8/3.
 */
public class RechargeActivity extends ActionbarActivity implements View.OnClickListener, ViewListener<RequestRechargeBean> {

    private static final String TAG = "RechargeActivity";
    @InjectView(R.id.et_addcard_username)
    EditText etAddcardUsername;
    @InjectView(R.id.rl_select_bank)
    RelativeLayout rlSelectBank;
    @InjectView(R.id.et_addcard_cardnumber)
    EditText etAddcardCardnumber;
    @InjectView(R.id.et_addcard_recharge_account)
    EditText etAddcardRechargeAccount;
    @InjectView(R.id.tv_addcard_userbalance)
    TextView tvAddcardUserbalance;
    @InjectView(R.id.btn_addcard_recharge)
    SubmitButton btnAddcardRecharge;
    @InjectView(R.id.iv_selected_banklogo)
    ImageView ivSelectedBanklogo;
    @InjectView(R.id.tv_selected_bankname)
    TextView tvSelectedBankname;
    @InjectView(R.id.tv_selected_banklimit)
    TextView tvSelectedBanklimit;
    @InjectView(R.id.ll_selected_bank)
    LinearLayout llSelectedBank;
    @InjectView(R.id.ll_no_card_bind)
    LinearLayout llNoCardBind;
    @InjectView(R.id.iv_bank_logo)
    ImageView ivBankLogo;
    @InjectView(R.id.tv_bank_name)
    TextView tvBankName;
    @InjectView(R.id.tv_bank_card_number)
    TextView tvBankCardNumber;
    @InjectView(R.id.tv_bank_limit_state)
    TextView tvBankLimitState;
    @InjectView(R.id.ll_has_card_bind)
    LinearLayout llHasCardBind;
    @InjectView(R.id.ll_rootView)
    LinearLayout llRootView;
    private RequestRechargePresenterImpl requestRechargePresenterImpl;
    private RechargePresenterImpl rechargePresenterImpl;
    private RequestRechargeBean requestRechargeBean;
    /*
     * 支付验证方式 0：标准版本， 1：卡前置方式，此两种支付方式接入时，只需要配置一种即可，Demo为说明用。可以在menu中选择支付方式
	 */
    private int pay_type_flag = 1;
    private boolean isBindCard;
    private String money;
    private Integer mid;
    private int channel;
    private String cardNo;
    private String cardCode;
    private PopupWindow loadingPopW;
    private PopupWindow realNameAuthPopupWindow;
    private boolean hasAuthRealName;
    private boolean isRequestRecharge;  //连接连连申请充值数据


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_layout_recharge, this);
        ButterKnife.inject(this);
        llRootView.setVisibility(View.GONE);
        requestRechargePresenterImpl = new RequestRechargePresenterImpl(this);
        rechargePresenterImpl = new RechargePresenterImpl(this);
        this.actionbar.setCenterTextView("充值");
        this.actionbar.setRightImageView(R.drawable.confirminvest_question_btn, this);

        mid = (Integer) SPUtils.get(this, "LOGIN", "id", 0);
        hasAuthRealName = (boolean) SPUtils.get(this, "LOGIN", "isTruename", false);
        setPricePoint(etAddcardRechargeAccount);
    }

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
            View rootView = LayoutInflater.from(this).inflate(R.layout.activity_layout_recharge, null);
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
        if (hasAuthRealName && requestRechargeBean == null) {
            requestRechargeData();
        }
    }

    /**
     * 界面刚进入时从后台获取数据
     */
    private void requestRechargeData() {
        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        necessaryParams.put("mid", "" + SPUtils.get(this, "LOGIN", "id", 0));
        requestRechargePresenterImpl.sendRequest(necessaryParams, null);
    }

    /**
     * 根据后台拿到的bankcode返回logo
     *
     * @param cardCode
     * @return
     */
    public int getImageResource(String cardCode) {
        int logoResId = 0;
        switch (cardCode) {
            case "ABC":
                logoResId = R.drawable.logo_abc_0;
                break;
            case "ICBC":
                logoResId = R.drawable.logo_icbc_1;
                break;
            case "CMBC":
                logoResId = R.drawable.logo_cmbc_2;
                break;
            case "BOC":
                logoResId = R.drawable.logo_boc_3;
                break;
            case "CBC":
                logoResId = R.drawable.logo_cbc_4;
                break;
            case "CEB":
                logoResId = R.drawable.logo_ceb_5;
                break;
            case "HXB":
                logoResId = R.drawable.logo_hxb_6;
                break;
            case "CNCB":
                logoResId = R.drawable.logo_cncb_7;
                break;
            case "CIB":
                logoResId = R.drawable.logo_cib_8;
                break;
            case "PAB":
                logoResId = R.drawable.logo_pab_9;
                break;
            case "SPDB":
                logoResId = R.drawable.logo_spdb_10;
                break;
            case "PSBC":
                logoResId = R.drawable.logo_psbc_11;
                break;
            case "GDB":
                logoResId = R.drawable.logo_gdb_12;
                break;
            case "BCM":
                logoResId = R.drawable.logo_bcm_13;
                break;
            case "CMSB":
                logoResId = R.drawable.logo_cmsb_14;
                break;
        }
        return logoResId;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            rlSelectBank.setVisibility(View.GONE);
            llSelectedBank.setVisibility(View.VISIBLE);
            AuthCardInfo bankInfo = (AuthCardInfo) data.getSerializableExtra("bankBean");
//            ivSelectedBanklogo.setImageResource(bankInfo.getLogoResId());
            Picasso.with(RechargeActivity.this)
                    .load(bankInfo.getBankicon())
                    .into(ivSelectedBanklogo);
            tvSelectedBankname.setText(bankInfo.getBankname());
            tvSelectedBanklimit.setText(bankInfo.getRemark());
            cardCode = bankInfo.getBankcode();
        }
    }

    @OnClick({R.id.ll_selected_bank, R.id.rl_select_bank, R.id.btn_addcard_recharge})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_selected_bank:
            case R.id.rl_select_bank:
                startActivityForResult(new Intent(this, ProfileSelectBankActivity.class), 0);
                break;
            case R.id.btn_addcard_recharge:
                //验证输入的金额是否合法,并调用充值的接口
                checkData();
                break;
        }
        if (view == actionbar.rightImageView) {
            MyActivityManager.getInstance().startNextActivity(CommonWebViewActivity.class, "paycheckProblem");
        }
    }

    private void checkData() {
        if (!isBindCard) {
            //如果没有绑卡
            /*//先验证是否输入持卡人姓名
            if (TextUtils.isEmpty(etAddcardUsername.getText().toString().trim())) {
                CommonToastUtils.showToast(this, "请输入持卡人姓名！");
                return;
            }*/
            //在判断是否选择了银行卡
            if (llSelectedBank.getVisibility() != View.VISIBLE) {
                CommonToastUtils.showToast(this, "请选择开户银行");
                return;
            }
            //再验证是否输入了银行卡号码
            if (TextUtils.isEmpty(etAddcardCardnumber.getText().toString().trim())) {
                CommonToastUtils.showToast(this, "请输入银行卡号");
                return;
            }
        }
        money = etAddcardRechargeAccount.getText().toString();
        if (TextUtils.isEmpty(money)) {
            CommonToastUtils.showToast(this, "请输入充值金额");
            return;
        } /*else if ((Double.parseDouble(money) <= new Double(0) || Double
                .parseDouble(money) > new Double(1000000))) {
            Toast.makeText(this, "充值金额必须大于0元，并且小于等于100万元",
                    Toast.LENGTH_SHORT).show();
            return;
        }*/
        //如果输入数据没有问题,先判断是否绑卡成功,再充值
        requestLianLianData();
        isRequestRecharge = true;
        btnAddcardRecharge.submit();

    }


    /**
     * 申请链接连连支付所需要的数据
     */
    private void requestLianLianData() {
        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        necessaryParams.put("mid", "" + mid);
        necessaryParams.put("amount", money);
        necessaryParams.put("channel", "" + channel);
        //获取卡号
        if (!isBindCard) {
            //如果没有绑卡,从输入中获取
            cardNo = etAddcardCardnumber.getText().toString().trim().replace(" ", "");
        } else {
            //如果已经绑卡,直接从返回的数据中获取
            cardNo = requestRechargeBean.getCardNo();
        }
        necessaryParams.put("cardNo", "" + cardNo);
        necessaryParams.put("bankCode", "" + cardCode);
        necessaryParams.put("device", "" + 5);
        rechargePresenterImpl.sendRequest(necessaryParams, null);
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
        if (isRequestRecharge) {
            btnAddcardRecharge.finish("充值失败");
        }
    }


    @Override
    public void showError(OnPresenterListener listener, RequestRechargeBean requestRechargeBean) {
        dismissLoadingDialog();
        CommonToastUtils.showToast(this, requestRechargeBean.getMsg());
        if (isRequestRecharge) {
            btnAddcardRecharge.finish("充值失败");
        }
    }

    @Override
    public void responseData(OnPresenterListener listener, RequestRechargeBean requestRechargeBean) {
        this.requestRechargeBean = requestRechargeBean;
        if (listener instanceof RequestRechargePresenterImpl) {
            //刚进入界面,判断是否有绑卡,根据是否绑卡来确定
            bindData();
            //获取渠道号
            channel = requestRechargeBean.getChannel();
            if (requestRechargeBean.getCardCode() != null) {
                cardCode = requestRechargeBean.getCardCode();
            }
        } else if (listener instanceof RechargePresenterImpl) {
            //后台绑卡成功,直接调用连连接口
            Message message = mHandler
                    .obtainMessage(Constant.TOPUP_SUCCESS);
            mHandler.sendMessage(message);
            if (isRequestRecharge) {
                btnAddcardRecharge.finish("充   值");
            }
        }
    }

    private void bindData() {
        llRootView.setVisibility(View.VISIBLE);
        //初始化界面
        if (TextUtils.isEmpty(requestRechargeBean.getCardNo())) {
            //没有绑定卡
            llNoCardBind.setVisibility(View.VISIBLE);
            llHasCardBind.setVisibility(View.GONE);
            etAddcardCardnumber.addTextChangedListener(new CustomTextWatcher());
            rlSelectBank.setVisibility(View.VISIBLE);
            llSelectedBank.setVisibility(View.GONE);
            String truename = (String) SPUtils.get(MainApplication.getContext(), "LOGIN", "truename", "");
            etAddcardUsername.setText(truename);
            etAddcardUsername.setFocusable(false);
            etAddcardRechargeAccount.setHint("请输入充值金额");
            isBindCard = false;
        } else {
            //已经绑卡
            llNoCardBind.setVisibility(View.GONE);
            llHasCardBind.setVisibility(View.VISIBLE);
//            ivBankLogo.setImageResource(getImageResource(requestRechargeBean.getCardCode()));
            Picasso.with(RechargeActivity.this)
                    .load(requestRechargeBean.getBankicon())
                    .into(ivBankLogo);
            tvBankName.setText(requestRechargeBean.getCardName());
            tvBankCardNumber.setText("尾号" + requestRechargeBean.getCardNo().substring(requestRechargeBean.getCardNo().length() - 4));
            tvBankLimitState.setText(requestRechargeBean.getRemark());

            isBindCard = true;
        }
        tvAddcardUserbalance.setText("" + new DecimalFormat("###,###,##0.00").format(requestRechargeBean.getAmount()) + "元");
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


    private String beforeStr = "";
    private String afterStr = "";
    private String changeStr = "";
    private int index = 0;
    private boolean changeIndex = true;

    class CustomTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            beforeStr = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            afterStr = s.toString();
            if (changeIndex)
                index = etAddcardCardnumber.getSelectionStart();
        }

        @Override
        public void afterTextChanged(Editable s) {
            if ("".equals(s.toString()) || s.toString() == null || beforeStr.equals(afterStr)) {
                changeIndex = true;
                return;
            }
            changeIndex = false;
            char c[] = s.toString().replace(" ", "").toCharArray();
            changeStr = "";
            for (int i = 0; i < c.length; i++) {
                changeStr = changeStr + c[i] + ((i + 1) % 4 == 0 && i + 1 != c.length ? " " : "");
            }
            if (afterStr.length() > beforeStr.length()) {
                if (changeStr.length() == index + 1) {
                    index = changeStr.length() - afterStr.length() + index;
                }
                if (index % 5 == 0 && changeStr.length() > index + 1) {
                    index++;
                }
            } else if (afterStr.length() < beforeStr.length()) {
                if ((index + 1) % 5 == 0 && index > 0 && changeStr.length() > index + 1) {
                    //  index--;
                } else {
                    index = changeStr.length() - afterStr.length() + index;
                    if (afterStr.length() % 5 == 0 && changeStr.length() > index + 1) {
                        index++;
                    }
                }
            }
            etAddcardCardnumber.setText(changeStr);
            etAddcardCardnumber.setSelection(index);
        }
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

    private Handler mHandler = createHandler();

    private Handler createHandler() {
        return new Handler() {
            public void handleMessage(Message msg) {

                switch (msg.what) {

                    case Constant.TOPUP_SUCCESS:
                        PayOrder order = null;
                        if (pay_type_flag == 0) {
                           /* // 标准支付
                            order = constructGesturePayOrder();
                            String content4Pay = BaseHelper.toJSONString(order);
                            // 关键 content4Pay
                            // 用于提交到支付SDK的订单支付串，如遇到签名错误的情况，请将该信息帖给我们的�?术支�?
                            Log.i(RechargeActivity.class.getSimpleName(), content4Pay);

                            MobileSecurePayer msp = new MobileSecurePayer();
                            boolean bRet = msp.pay(content4Pay, mHandler,
                                    Constants.RQF_PAY, RechargeActivity.this, false);

                            Log.i(RechargeActivity.class.getSimpleName(),
                                    String.valueOf(bRet));*/

                        } else if (pay_type_flag == 1) {
                            // TODO 卡前置方�?, 如果传入的是卡号，卡号必须大于等�?14�?
                           /* if (etAddcardCardnumber.getText().toString().length() < 14) {
                                Toast.makeText(RechargeActivity.this,
                                        "卡前置模式，必须填入正确银行卡卡号或者协议号", Toast.LENGTH_LONG)
                                        .show();
                                return;
                            }*/
//                            JSONObject ar = (JSONObject) msg.obj;
                            String code = requestRechargeBean.getAccountRecharge().getCode();
                            if (TextUtils.isEmpty(code)) {
                                Toast.makeText(RechargeActivity.this, "充值订单号为空!",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                            order = constructPreCardPayOrder();
                            String content4Pay = BaseHelper.toJSONString(order);
                            Log.i(RechargeActivity.class.getSimpleName(), content4Pay);

                            MobileSecurePayer msp = new MobileSecurePayer();
                            boolean bRet = msp.pay(content4Pay, mHandler,
                                    Constants.RQF_PAY, RechargeActivity.this, false);

                            Log.i(RechargeActivity.class.getSimpleName(),
                                    String.valueOf(bRet));

                        }

                        break;

                    case Constants.RQF_PAY: {
                        String strRet = (String) msg.obj;
                        JSONObject objContent = BaseHelper.string2JSON(strRet);
                        String retCode = objContent.optString("ret_code");
                        String retMsg = objContent.optString("ret_msg");
                        if (Constants.RET_CODE_SUCCESS.equals(retCode)) {
                            // TODO
                            // 卡前置模式返回的银行卡绑定协议号，用来下次支付时使用，此处仅作为示例使用。正式接入时去掉
                            // if (pay_type_flag == 1) {
                            // TextView tv_agree_no = (TextView)
                            // findViewById(R.id.tv_agree_no);
                            // tv_agree_no.setVisibility(View.VISIBLE);
                            // tv_agree_no.setText(objContent.optString(
                            // "agreementno", ""));
                            //
                            // }

                            String resulPay = objContent.optString("result_pay");
                            if (Constants.RESULT_PAY_SUCCESS
                                    .equalsIgnoreCase(resulPay)) {
                                /*BaseHelper.showDialog(RechargeActivity.this, "提示",
                                        "支付成功，交易状态码:" + retCode,
                                        android.R.drawable.ic_dialog_alert);*/
//                                MyActivityManager.getInstance().popCurrentActivity();
                                // processTopup();
                                /*BaseHelper.showDialog(RechargeActivity.this, "提示",
                                        "支付成功", android.R.drawable.ic_dialog_alert);*/
                                CommonToastUtils.showSuccessToast("充值成功");

                                mHandler.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {

                                        MyActivityManager.getInstance().popCurrentActivity();
//                                MyActivityManager.getInstance().startNextActivity(ProfileFragment.class);

                                    }
                                }, 1000);

                            } else {
                                BaseHelper.showDialog(RechargeActivity.this, "提示",
                                        retMsg + "，交易状态码:" + retCode,
                                        android.R.drawable.ic_dialog_alert);

                            }

                        } else if (Constants.RET_CODE_PROCESS.equals(retCode)) {
                            String resulPay = objContent.optString("result_pay");
                            if (Constants.RESULT_PAY_PROCESSING
                                    .equalsIgnoreCase(resulPay)) {
                                BaseHelper.showDialog(RechargeActivity.this, "提示",
                                        objContent.optString("ret_msg") + "交易状态码:"
                                                + retCode,
                                        android.R.drawable.ic_dialog_alert);

                            }

                        } else {
                            BaseHelper.showDialog(RechargeActivity.this, "提示", retMsg
                                            + "，交易状态码:" + retCode,
                                    android.R.drawable.ic_dialog_alert);

                        }

                       /* mHandler.postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                MyActivityManager.getInstance().popCurrentActivity();
//                                MyActivityManager.getInstance().startNextActivity(ProfileFragment.class);

                            }
                        }, 1000);*/

                    }
                    break;
                }
                super.handleMessage(msg);
            }

        };

    }

    /*private PayOrder constructGesturePayOrder() {

        RequestRechargeBean.AccountRechargeBean accountRecharge = requestRechargeBean.getAccountRecharge();
        AccountSettingBean.MemberBean member = requestRechargeBean.getMember();
        String bankCard = accountRecharge.getBank();
        String code = accountRecharge.getCode();
        String amount = new DecimalFormat("#.00").format(accountRecharge.getAmount()).toString();
        String idcode = member.getIdcode();
        long ctime = member.getCtime();
        String truename = member.getTruename();
        String phone = member.getMobile();
        String userId = "" + member.getId();
        String old_partner = EnvConstants.PARTNER;
        String md5Key = EnvConstants.MD5_KEY;
        String busi_partner = "101001";
        String signType = "MD5";
        String frms_ware_category = "2009";
        String simOrder = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());
        String orderNum = code;

        PayOrder order = new PayOrder();
        order.setBusi_partner("101001");
        order.setNo_order(orderNum);
        order.setDt_order(orderNum);
        order.setName_goods("充值");
        order.setNotify_url("http://beta.www.youtx.com/youtxpay/LianlianPay/YTPay_Notify.aspx");
        // MD5 签名方式
        order.setSign_type(PayOrder.SIGN_TYPE_MD5);
        // RSA 签名方式
        // order.setSign_type(PayOrder.SIGN_TYPE_RSA);
        order.setValid_order("100");

        order.setUser_id(mid);
        order.setId_no(idcode);
        order.setAcct_name(truename);
        order.setMoney_order(ed_money.getText().toString());

        order.setFlag_modify("1");
        String sign = "";
        order.setOid_partner(EnvConstants.PARTNER);
        String content = BaseHelper.sortParam(order);
        // MD5 签名方式, 签名方式包括两种，一种是MD5，一种是RSA
        // 这个在商户站管理里有对验签方式和签名Key的配置�??
        sign = Md5Algorithm.getInstance().sign(content, EnvConstants.MD5_KEY);
        // RSA 签名方式
        // sign = Rsa.sign(content, EnvConstants.RSA_PRIVATE);
        order.setSign(sign);
        return order;
    }*/

    private PayOrder constructPreCardPayOrder() {
        RequestRechargeBean.AccountRechargeBean accountRecharge = requestRechargeBean.getAccountRecharge();
        MemberBean member = requestRechargeBean.getMember();
        String bankCard = accountRecharge.getBank().replace(" ", "");

        String code = accountRecharge.getCode();
        String amount = new DecimalFormat("#.00").format(accountRecharge.getAmount()).toString();
        String idcode = member.getIdcode();
        long ctime = member.getCtime();
        String truename = member.getTruename();
        String userId = "" + member.getId();
        String old_partner = EnvConstants.PARTNER;
        String simOrder = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());
        // 用服务端生成的订单号
        String orderNum = code;// GetOrderNumber.createOrderNo();
        Log.e(TAG, "订单号:" + orderNum);
        //生成订单
        PayOrder order = new PayOrder();
        order.setOid_partner(old_partner);
        order.setSign_type(PayOrder.SIGN_TYPE_MD5);
        order.setBusi_partner("101001");
        order.setNo_order(orderNum);
        order.setDt_order(simOrder);
        order.setName_goods("充值");
        Log.e(TAG, "Notify_url:" + "https://www.hnmgjr.com/" + "payNoticeAuthPay");
        order.setNotify_url("https://www.hnmgjr.com/" + "payNoticeAuthPay");
        // MD5 签名方式
        // RSA 签名方式
        // order.setSign_type(PayOrder.SIGN_TYPE_RSA);
        order.setValid_order("100");
//        order.setMoney_order(etAddcardRechargeAccount.getText().toString());
        order.setMoney_order(amount);
        order.setCard_no(bankCard);
        order.setNo_agree("");//
        order.setRisk_item(constructRiskItem());
        order.setUser_id(userId);
        order.setId_no(idcode);
        order.setId_type("0");
        order.setAcct_name(truename);
        String sign = "";
        order.setOid_partner(EnvConstants.PARTNER);
        String content = BaseHelper.sortParam(order);
        // MD5 签名方式
        sign = Md5Algorithm.getInstance().sign(content, EnvConstants.MD5_KEY);
        // RSA 签名方式
        // sign = Rsa.sign(content, EnvConstants.RSA_PRIVATE);
        order.setSign(sign);
        return order;
    }

    private String constructRiskItem() {
        JSONObject mRiskItem = new JSONObject();
        try {
            mRiskItem.put("user_info_bind_phone", requestRechargeBean.getMember().getMobile());
            String time = new SimpleDateFormat("yyyyMMddHHmmss").format(requestRechargeBean.getMember().getCtime());
            mRiskItem.put("user_info_dt_register", time);
            mRiskItem.put("frms_ware_category", "2009");
            mRiskItem.put("user_info_full_name", requestRechargeBean.getMember().getTruename());
            mRiskItem.put("user_info_id_no", requestRechargeBean.getMember().getIdcode());
            mRiskItem.put("user_info_identify_state", "1");
            mRiskItem.put("user_info_identify_type", "3");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mRiskItem.toString();
    }
}
