package com.mgjr.view.profile.accountsetting;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgjr.R;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.LogUtil;
import com.mgjr.Utils.PhoneUtils;
import com.mgjr.Utils.PopwUtils;
import com.mgjr.Utils.SPUtils;
import com.mgjr.httpclient.HttpClient;
import com.mgjr.httpclient.callback.Callback;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.model.bean.AuthCardInfo;
import com.mgjr.model.bean.BaseBean;
import com.mgjr.share.APIBuilder;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.CustomCommonDialog;
import com.mgjr.share.addresswheel_master.model.AddressDtailsEntity;
import com.mgjr.share.addresswheel_master.model.AddressModel;
import com.mgjr.share.addresswheel_master.utils.JsonUtil;
import com.mgjr.share.addresswheel_master.utils.Utils;
import com.mgjr.share.addresswheel_master.view.ChooseAddressWheel;
import com.mgjr.share.addresswheel_master.view.listener.OnAddressChangeListener;
import com.mgjr.view.common.CommonWebViewActivity;
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

public class ProfileAddBankCardActivity extends ActionbarActivity implements OnAddressChangeListener, View.OnClickListener {

    @InjectView(R.id.et_addcard_username)
    EditText etAddcardUsername;
    @InjectView(R.id.rl_select_bank)
    RelativeLayout rlSelectBank;
    @InjectView(R.id.iv_selected_banklogo)
    ImageView ivSelectedBanklogo;
    @InjectView(R.id.tv_selected_bankname)
    TextView tvSelectedBankname;
    @InjectView(R.id.tv_selected_banklimit)
    TextView tvSelectedBanklimit;
    @InjectView(R.id.ll_selected_bank)
    LinearLayout llSelectedBank;
    @InjectView(R.id.et_addcard_cardnumber)
    EditText etAddcardCardnumber;
    @InjectView(R.id.tv_bankcard_address)
    TextView tvBankcardAddress;
    @InjectView(R.id.rl_select_bankcard_address)
    RelativeLayout rlSelectBankcardAddress;
    @InjectView(R.id.tv_input_bank_branch)
    EditText etInputBankBranch;
    @InjectView(R.id.ll_binded_bankcard_without_address)
    LinearLayout llBindedBankcardWithoutAddress;
    @InjectView(R.id.btn_confirm)
    RelativeLayout btnConfirm;

    private ChooseAddressWheel chooseAddressWheel = null;
    private String province;
    private String city;
    private String district;
    private String cardNo;
    private AuthCardInfo bankInfo;

    private String type;  // 0,代表添加银行卡,1 代表更换银行卡

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_profile_add_bank_card, this);

        ButterKnife.inject(this);
        //省市县地址选择初始化
        initSSXAddress();
        initViews();
        type = getIntent().getStringExtra("type");
        initActionBar();
    }


    private void initActionBar() {
        if ("0".equals(type)) {
            actionbar.setCenterTextView("添加银行卡");
        } else {
            actionbar.setCenterTextView("更换银行卡");
        }
        actionbar.setRightImageView(R.drawable.confirminvest_question_btn, this);
        actionbar.leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CustomCommonDialog dialog = new CustomCommonDialog(ProfileAddBankCardActivity.this, "温馨提示", "您确定放弃添加银行卡吗？", "确定", "放弃", false);
                dialog.setClicklistener(new CustomCommonDialog.ClickListenerInterface() {
                    @Override
                    public void doConfirm() {
//                        popActivity();
                        MyActivityManager.getInstance().popCurrentActivity();
                    }

                    @Override
                    public void doCancel() {
                        dialog.dismiss();
                    }

                });
                dialog.show();
            }
        });
    }

    private void initViews() {
//        setPricePoint(etAddcardCardnumber);
        etAddcardCardnumber.addTextChangedListener(new CustomTextWatcher());
        String truename = (String) SPUtils.get(MainApplication.getContext(), "LOGIN", "truename", "");
        etAddcardUsername.setText(truename);
        etAddcardUsername.setFocusable(false);
    }

    /**
     * 在界面获取焦点的时候,验证是否已经实名认证过了
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            isRealNameAuth();
        }
    }

    private void isRealNameAuth() {
        boolean hasAuthRealName = (boolean) SPUtils.get(this, "LOGIN", "isTruename", false);
        if (!hasAuthRealName) {
            View rootView = LayoutInflater.from(this).inflate(R.layout.activity_profile_add_bank_card, null);
            PopwUtils.showRealNameAuthPopw(this, rootView);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            rlSelectBank.setVisibility(View.GONE);
            llSelectedBank.setVisibility(View.VISIBLE);
            bankInfo = (AuthCardInfo) data.getSerializableExtra("bankBean");
            Picasso.with(ProfileAddBankCardActivity.this)
                    .load(bankInfo.getBankicon())
                    .into(ivSelectedBanklogo);
            tvSelectedBankname.setText(bankInfo.getBankname());
            tvSelectedBanklimit.setText(bankInfo.getRemark());
        }
    }


    @OnClick({R.id.ll_selected_bank, R.id.rl_select_bank, R.id.rl_select_bankcard_address, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_selected_bank:
            case R.id.rl_select_bank:
                Intent intent = new Intent();
                MyActivityManager.getInstance().startNextActivityForResult(intent, 0, ProfileSelectBankActivity.class);
                break;
            case R.id.rl_select_bankcard_address:
                Utils.hideKeyBoard(this);
                chooseAddressWheel.show(view);
                break;
            case R.id.btn_confirm:
                checkData();
                break;
        }
        if (view == actionbar.rightImageView) {
            MyActivityManager.getInstance().startNextActivity(CommonWebViewActivity.class, "bankCardProblem");
        }
    }

    private void checkData() {
        //先验证是否输入持卡人姓名
       /* if (TextUtils.isEmpty(etAddcardUsername.getText().toString().trim())) {
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
        //判断是否选择了银行卡开户所在地
        if (TextUtils.isEmpty(tvBankcardAddress.getText())) {
            CommonToastUtils.showToast(this, "请选择开户银行所在地");
            return;
        }
        //判断是否输入了银行卡开户支行名称
        if (TextUtils.isEmpty(etInputBankBranch.getText().toString().trim())) {
            CommonToastUtils.showToast(this, "请输入银行卡开户支行名称");
            return;
        }
        cardNo = etAddcardCardnumber.getText().toString().replace(" ", "");
        networkRequest();
    }

    private void networkRequest() {
        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        necessaryParams.put("mid", "" + SPUtils.get(this, "LOGIN", "id", 0));
        necessaryParams.put("bankCode", bankInfo.getBankcode());
        necessaryParams.put("cardNo", "" + cardNo);
        Map<String, String> unNecessaryParams = new HashMap<String, String>();
        unNecessaryParams.put("province", "" + province);
        unNecessaryParams.put("city", "" + city);
        unNecessaryParams.put("area", "" + district);
        unNecessaryParams.put("bankAddress", "" + etInputBankBranch.getText().toString());
        sendRequest(necessaryParams, unNecessaryParams);

    }

    private void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams) {
        String url = null;
        if ("0".equals(type)) {
            url = APIBuilder.baseUrl + APIBuilder.version + APIBuilder.bindBankCardUrl();
        } else {
            url = APIBuilder.baseUrl + APIBuilder.version + APIBuilder.changeBankCardUrl();
        }

        List<String> keyList = new ArrayList<String>();
        if ("0".equals(type)) {
            keyList.add(APIBuilder.bindBankCardUrl());
        } else {
            keyList.add(APIBuilder.changeBankCardUrl());
        }
        keyList.add(necessaryParams.get("mid"));
        keyList.add(necessaryParams.get("bankCode"));
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
            PhoneUtils.saveSystemErrorInfo(e);
            showSystemError();
//            CommonToastUtils.showToast(ProfileAddBankCardActivity.this, e.toString());
        }

        @Override
        public void onResponse(BaseBean response) {
            if (response != null && response.getStatus().equalsIgnoreCase("0000")) {
                if ("0".equals(type)) {
//                    CommonToastUtils.showToast(ProfileAddBankCardActivity.this, "银行卡绑定成功！");
                    CommonToastUtils.showSuccessToast("添加成功");
                } else {
//                    CommonToastUtils.showToast(ProfileAddBankCardActivity.this, "银行卡更换成功！");
                    CommonToastUtils.showSuccessToast("更换成功");
                }
//                popActivity();
                MyActivityManager.getInstance().popCurrentActivity();
            } else {
                CommonToastUtils.showToast(ProfileAddBankCardActivity.this, response.getMsg());
            }
        }
    }


    /**
     * 接下来四个函数是省市县三级联动的相关初始化和监听
     */
    private void initSSXAddress() {
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
//            tvBankcardAddress.setText(data.Province + " " + data.City + " " + data.Area);
            if (data.ProvinceItems != null && data.ProvinceItems.Province != null) {
                chooseAddressWheel.setProvince(data.ProvinceItems.Province);
//                chooseAddressWheel.defaultValue(data.Province, data.City, data.Area);
                chooseAddressWheel.defaultValue("湖南省", "长沙市", "岳麓区");
            }
        }
    }

    @Override
    public void onAddressChange(String province, String city, String district) {
        tvBankcardAddress.setText(province + " " + city + " " + district);
        this.province = province;
        this.city = city;
        this.district = district;
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
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
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
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });

    }
}
