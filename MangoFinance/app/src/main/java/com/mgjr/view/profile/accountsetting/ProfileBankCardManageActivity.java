package com.mgjr.view.profile.accountsetting;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.KeyBoardUtils;
import com.mgjr.Utils.LogUtil;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.PhoneUtils;
import com.mgjr.Utils.SPUtils;
import com.mgjr.httpclient.HttpClient;
import com.mgjr.httpclient.callback.Callback;
import com.mgjr.model.bean.BankCardManageBean;
import com.mgjr.model.bean.BaseBean;
import com.mgjr.presenter.impl.RequestBankCardInfoPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.APIBuilder;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.CircleImageView;
import com.mgjr.share.CustomCommonDialog;
import com.mgjr.share.InputPayPwdPopw;
import com.mgjr.share.addresswheel_master.model.AddressDtailsEntity;
import com.mgjr.share.addresswheel_master.model.AddressModel;
import com.mgjr.share.addresswheel_master.utils.JsonUtil;
import com.mgjr.share.addresswheel_master.utils.Utils;
import com.mgjr.share.addresswheel_master.view.ChooseAddressWheel;
import com.mgjr.share.addresswheel_master.view.listener.OnAddressChangeListener;
import com.mgjr.view.common.CommonWebViewActivity;
import com.mgjr.view.listeners.ViewListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class ProfileBankCardManageActivity extends ActionbarActivity implements ViewListener<BankCardManageBean>, OnAddressChangeListener {
    @InjectView(R.id.civ_bank_logo_icon)
    CircleImageView civBankLogoIcon;
    @InjectView(R.id.tv_bank_name)
    TextView tvBankName;
    @InjectView(R.id.tv_bank_code)
    TextView tvBankCode;
    @InjectView(R.id.tv_bankcard_address)
    TextView tvBankcardAddress;
    @InjectView(R.id.rl_select_bankcard_address)
    RelativeLayout rlSelectBankcardAddress;
    @InjectView(R.id.tv_input_bank_branch)
    TextView tvInputBankBranch;
    @InjectView(R.id.ll_binded_bankcard_without_address)
    LinearLayout llBindedBankcardWithoutAddress;
    @InjectView(R.id.btn_change_bank_bard)
    Button btnChangeBankBard;
    @InjectView(R.id.btn_unbind_bank_bard)
    TextView btnUnbindBankBard;
    @InjectView(R.id.activity_bank_card_manage)
    LinearLayout activityBankCardManage;
    @InjectView(R.id.ll_bankcard_info_bg)
    LinearLayout llBankcardInfoBg;
    @InjectView(R.id.ll_change_bank_card_branch)
    LinearLayout llChangeBankCardBranch;
    private RequestBankCardInfoPresenterImpl requestBankCardInfoPresenterImpl;
    private ChooseAddressWheel chooseAddressWheel = null;
    private BankCardManageBean bankCardManageBean;
    private String bankBranchAddress;
    private String province;
    private String city;
    private String district;
    private String cardNo;
    private double rechargeAmount;
    private double withdrawAmount;

    private InputPayPwdPopw inputModifyBankAddressPopw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_bank_card_manage, this);
        ButterKnife.inject(this);
        requestBankCardInfoPresenterImpl = new RequestBankCardInfoPresenterImpl(this);
        initActionBar();
        //初始化本地的省市县三级联动
        initAndroidWheel();
        //初始化修改昵称弹窗
        initModifyBankAddressPopw();

    }

    private void initActionBar() {
        actionbar.setCenterTextView("银行卡管理");
        actionbar.setRightImageView(R.drawable.confirminvest_question_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivityManager.getInstance().startNextActivity(CommonWebViewActivity.class, "bankCardProblem");
            }
        });
        activityBankCardManage.setVisibility(View.GONE);
    }


    private void initModifyBankAddressPopw() {
        inputModifyBankAddressPopw = new InputPayPwdPopw();
        inputModifyBankAddressPopw.setClickBtnListener(new InputPayPwdPopw.ClickBtnListener() {
            @Override
            public void clickConfirmBtn() {
                //提交数据,请求数据
                bankBranchAddress = inputModifyBankAddressPopw.getEtInputTradePwd().getText().toString();

                if (TextUtils.isEmpty(bankBranchAddress)) {
                    CommonToastUtils.showToast(ProfileBankCardManageActivity.this, "开户支行不能为空");
                    inputModifyBankAddressPopw.getEtInputTradePwd().requestFocus();
                    return;
                } else if (bankBranchAddress.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0) {
                    // 包含特殊字符
                    CommonToastUtils.showToast(ProfileBankCardManageActivity.this, "开户支行不能有特殊字符");
                    inputModifyBankAddressPopw.getEtInputTradePwd().requestFocus();
                    return;
                } else {
                    //提交数据,请求数据
                    sendModifyBankInfoRequest();
                    KeyBoardUtils.closeKeybord(inputModifyBankAddressPopw.getEtInputTradePwd(), ProfileBankCardManageActivity.this);
                }
            }

            @Override
            public void clickForgetPwdBtn() {

            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        networkRequest();
    }

    private void networkRequest() {
        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        necessaryParams.put("mid", "" + SPUtils.get(this, "LOGIN", "id", 0));
        requestBankCardInfoPresenterImpl.sendRequest(necessaryParams, null);
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
    public void showError(OnPresenterListener listener, BankCardManageBean bankCardManageBean) {
        dismissLoadingDialog();
        CommonToastUtils.showToast(this, bankCardManageBean.getMsg());
    }


    @Override
    public void responseData(OnPresenterListener listener, BankCardManageBean bankCardManageBean) {
        this.bankCardManageBean = bankCardManageBean;
        bindData();
        rechargeAmount = bankCardManageBean.getAccountBankcard().getRechargeamount();
        withdrawAmount = bankCardManageBean.getAccountBankcard().getWithdrawamount();
    }

    private void bindData() {
        if (bankCardManageBean.getAccountBankcard() == null) {
            return;
        }
        activityBankCardManage.setVisibility(View.VISIBLE);
        tvBankName.setText(bankCardManageBean.getAccountBankcard().getBanktruename());
        cardNo = bankCardManageBean.getAccountBankcard().getCardno();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cardNo.length(); i++) {
            if (i == 4 || i == cardNo.length() - 4) {
                sb.append(" ");
            }
            if (i > 3 && i < cardNo.length() - 4) {
                sb.append("*");
            } else {
                sb.append(cardNo.charAt(i));
            }

        }
        tvBankCode.setText(sb.toString());
        setBankCardInfoBg(bankCardManageBean.getAccountBankcard().getBankname());
        getBankIconFromNetwork(bankCardManageBean.getAccountBankcard().getBankicon());
        String province = bankCardManageBean.getAccountBankcard().getProvince();
        String city = bankCardManageBean.getAccountBankcard().getCity();
        String area = bankCardManageBean.getAccountBankcard().getArea();
        if (!"".equals(province)) {
            tvBankcardAddress.setText(province + " " + city + " " + area);
        }
        if (!"".equals(bankCardManageBean.getAccountBankcard().getAddress())) {
            tvInputBankBranch.setText(bankCardManageBean.getAccountBankcard().getAddress());
        }
    }

    private void setBankCardInfoBg(String banktruename) {
        switch (banktruename) {
            case "GDB":
            case "PAB":
            case "BOC":
            case "ICBC":
            case "CMBC":
            case "HXB":
                llBankcardInfoBg.setBackgroundResource(R.drawable.bank_red_logo);
                break;
            case "ABC":
            case "PSBC":
                llBankcardInfoBg.setBackgroundResource(R.drawable.bank_green_logo);
                break;
            default:
                llBankcardInfoBg.setBackgroundResource(R.drawable.bank_blue_logo);
                break;
        }
    }

    private void getBankIconFromNetwork(final String bankIconUrl) {
        Picasso.with(ProfileBankCardManageActivity.this)
                .load(bankIconUrl)
                .into(civBankLogoIcon);
    }


    @OnClick({R.id.ll_change_bank_card_branch, R.id.rl_select_bankcard_address, R.id.btn_change_bank_bard, R.id.btn_unbind_bank_bard})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_select_bankcard_address:
                Utils.hideKeyBoard(this);
                chooseAddressWheel.show(view);
                break;
            case R.id.btn_change_bank_bard:
                checkIsCanChangeBankCard();
                break;
            case R.id.btn_unbind_bank_bard:
                checkIsCanUnBindBankCard();
                break;
            case R.id.ll_change_bank_card_branch:
                //修改地址
                View rootView = LayoutInflater.from(this).inflate(R.layout.activity_bank_card_manage, null);
                inputModifyBankAddressPopw.showInputTradePwdPopw(this, rootView);
                changeInputBankAddressPopwStyle();
                break;
        }
    }

    private void changeInputBankAddressPopwStyle() {
        inputModifyBankAddressPopw.getTv_forgetpaypwd().setVisibility(View.GONE);
        inputModifyBankAddressPopw.getDialog_title().setText("开户支行");
        inputModifyBankAddressPopw.getEtInputTradePwd().setText(tvInputBankBranch.getText());
        inputModifyBankAddressPopw.getEtInputTradePwd().setHint("请输入开户支行名称");
        inputModifyBankAddressPopw.getEtInputTradePwd().setInputType(InputType.TYPE_CLASS_TEXT);
    }


    private void sendModifyBankInfoRequest() {
        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        necessaryParams.put("mid", "" + SPUtils.get(this, "LOGIN", "id", 0));
        necessaryParams.put("cardNo", "" + cardNo);
        Map<String, String> unNecessaryParams = new HashMap<String, String>();
        if (province != null) {
            unNecessaryParams.put("province", "" + province);
            unNecessaryParams.put("city", "" + city);
            unNecessaryParams.put("area", "" + district);
        }
        if (bankBranchAddress != null) {
            unNecessaryParams.put("bankAddress", "" + bankBranchAddress);
        }
        sendRequest(necessaryParams, unNecessaryParams);
    }


    private void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams) {
        String url = APIBuilder.baseUrl + APIBuilder.version + APIBuilder.modifyBankCardInfoUrl();

        List<String> keyList = new ArrayList<String>();

        keyList.add(APIBuilder.modifyBankCardInfoUrl());
        keyList.add(necessaryParams.get("mid"));
        keyList.add(necessaryParams.get("cardNo"));
        String keyStr = APIBuilder.getKeyStr(keyList);

        necessaryParams.put("keyStr", keyStr);

        Map<String, String> params = new HashMap<String, String>();
        params.putAll(necessaryParams);
        if (unNecessaryParams != null) {
            params.putAll(unNecessaryParams);
        }
        HttpClient
                .post()
                .url(url)
                .params(params)
                .build()
                .execute(new CustomCallback());

    }

    class CustomCallback extends Callback<BaseBean> {
        @Override
        public BaseBean parseResponse(Response response) throws Exception {
            String string = response.body().string();
            LogUtil.e(string);
            Gson gson = new GsonBuilder().create();
            BaseBean baseBean = gson.fromJson(string, BaseBean.class);
            return baseBean;
        }

        @Override
        public void onError(Call call, Exception e) {
//            CommonToastUtils.showToast(ProfileBankCardManageActivity.this, e.toString());
            PhoneUtils.saveSystemErrorInfo(e);
            showSystemError();
        }

        @Override
        public void onResponse(BaseBean response) {
            if (response != null && response.getStatus().equalsIgnoreCase("0000")) {
//                CommonToastUtils.showToast(ProfileBankCardManageActivity.this, "银行卡信息更改成功！");
                if (bankBranchAddress != null) {
                    tvInputBankBranch.setText(bankBranchAddress);
                    inputModifyBankAddressPopw.getInputTradePwdPopw().dismiss();
                }
            } else {
                CommonToastUtils.showToast(ProfileBankCardManageActivity.this, response.getMsg());
            }
        }
    }


    private void checkIsCanChangeBankCard() {
        rechargeAmount = bankCardManageBean.getAccountBankcard().getRechargeamount();
        withdrawAmount = bankCardManageBean.getAccountBankcard().getWithdrawamount();
        if (withdrawAmount >= rechargeAmount) {
            MyActivityManager.getInstance().startNextActivity(ProfileAddBankCardActivity.class, "", "1");
//            MyActivityManager.getInstance().finishSpecifiedActivity(ProfileBankCardManageActivity.class);
        } else {
            final CustomCommonDialog dialog = new CustomCommonDialog(true, ProfileBankCardManageActivity.this, "温馨提示", "为了保证资金同卡进出，您需要将账户上所有资金提出到原有银行卡上后才可更换银行卡", "确定", false);
            dialog.setClicklistener(new CustomCommonDialog.ClickListenerInterfaceSingle() {
                @Override
                public void doSingleBtn() {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    private void checkIsCanUnBindBankCard() {
        rechargeAmount = bankCardManageBean.getAccountBankcard().getRechargeamount();
        withdrawAmount = bankCardManageBean.getAccountBankcard().getWithdrawamount();
        if (withdrawAmount >= rechargeAmount) {
            final CustomCommonDialog dialog = new CustomCommonDialog(ProfileBankCardManageActivity.this, "", "您确定要解除绑定的银行卡吗？", "解除", "取消", true);
            dialog.setClicklistener(new CustomCommonDialog.ClickListenerInterface() {
                @Override
                public void doConfirm() {
                    requestUnBindBankCardData();
                }

                @Override
                public void doCancel() {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else {
            final CustomCommonDialog dialog = new CustomCommonDialog(true, ProfileBankCardManageActivity.this, "温馨提示", "为了保证资金同卡进出，您需要将账户上所有资金提出到原有银行卡上后才可解除绑定", "确定", false);
            dialog.setClicklistener(new CustomCommonDialog.ClickListenerInterfaceSingle() {
                @Override
                public void doSingleBtn() {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    private void requestUnBindBankCardData() {
        String url = APIBuilder.baseUrl + APIBuilder.version + "appAccount/unbindBankCard";
        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        necessaryParams.put("mid", "" + SPUtils.get(this, "LOGIN", "id", 0));
        List<String> keyList = new ArrayList<String>();
        keyList.add("appAccount/unbindBankCard");
        keyList.add(necessaryParams.get("mid"));
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
                        BaseBean baseBean = gson.fromJson(string, BaseBean.class);
                        return baseBean;
                    }

                    @Override
                    public void onError(Call call, Exception e) {
//                        CommonToastUtils.showToast(ProfileBankCardManageActivity.this, e.toString());
                        PhoneUtils.saveSystemErrorInfo(e);
                        showSystemError();
                    }

                    @Override
                    public void onResponse(Object response) {
                        BaseBean baseBean = (BaseBean) response;
                        if (baseBean.getStatus().equalsIgnoreCase("0000")) {
                            //解绑成功
//                            CommonToastUtils.showToast(ProfileBankCardManageActivity.this, "银行卡解绑成功!");
                            CommonToastUtils.showSuccessToast("解绑成功");
//                            popActivity();
                            MyActivityManager.getInstance().popCurrentActivity();
                        } else {
                            CommonToastUtils.showToast(ProfileBankCardManageActivity.this, baseBean.getMsg());
                        }
                    }
                });
    }

    @Override
    public void onAddressChange(String province, String city, String district) {
        tvBankcardAddress.setText(province + " " + city + " " + district);
        this.province = province;
        this.city = city;
        this.district = district;
        sendModifyBankInfoRequest();
    }
}
