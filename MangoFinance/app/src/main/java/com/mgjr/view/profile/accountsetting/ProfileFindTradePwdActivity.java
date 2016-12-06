package com.mgjr.view.profile.accountsetting;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.CountDownTimerUtils;
import com.mgjr.Utils.LogUtil;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.PhoneUtils;
import com.mgjr.Utils.SPUtils;
import com.mgjr.Utils.StringToBase64;
import com.mgjr.httpclient.HttpClient;
import com.mgjr.httpclient.callback.Callback;
import com.mgjr.model.bean.BaseBean;
import com.mgjr.share.APIBuilder;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.SubmitButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.mgjr.Utils.HideInputUtils.isShouldHideInput;

public class ProfileFindTradePwdActivity extends ActionbarActivity {

    @InjectView(R.id.et_login_pwd)
    EditText etLoginPwd;
    @InjectView(R.id.et_new_trade_pwd)
    EditText etNewTradePwd;
    @InjectView(R.id.et_confirm_trade_pwd)
    EditText etConfirmTradePwd;
    @InjectView(R.id.et_identity_number)
    EditText etIdentityNumber;
    @InjectView(R.id.tv_phonenum)
    TextView tvPhonenum;
    @InjectView(R.id.et_input_vcode)
    EditText etInputVcode;
    @InjectView(R.id.tv_get_vcode)
    Button tvGetVcode;
    @InjectView(R.id.btn_confirm)
    SubmitButton btnConfirm;
    @InjectView(R.id.ll_input_identity_num)
    LinearLayout llInputIdentityNum;

    private String loginPwd;
    private String newTradePwd;
    private String confirmTradePwd;
    private String identityNum;
    private CountDownTimerUtils countDownTimerUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_profile_find_trade_pwd, this);
        ButterKnife.inject(this);
        actionbar.setCenterTextView("找回交易密码");
        countDownTimerUtils = new CountDownTimerUtils(tvGetVcode, 60000, 1000);
        initViews();
    }

    private void initViews() {
        boolean hasAuthRealName = (boolean) SPUtils.get(this, "LOGIN", "isTruename", false);
        if (!hasAuthRealName) {
            /*identityNum = (String) SPUtils.get(MainApplication.getContext(), "LOGIN", "idCode", "");
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            for (int i = 0; i < identityNum.length(); i++) {
                if (i <= 3 || i >= identityNum.length() - 4) {
                    sb.append(identityNum.charAt(i));
                } else {
                    sb.append("*");
                }
            }
            sb.append(")");
            etIdentityNumber.setText(sb.toString());
            etIdentityNumber.setFocusable(false);*/
            llInputIdentityNum.setVisibility(View.GONE);
        }
        String mobilePhone = (String) SPUtils.get(ProfileFindTradePwdActivity.this, "LOGIN", "mobilePhone", "");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mobilePhone.length(); i++) {
            if (i > 2 && i < 7) {
                sb.append("*");
            } else {
                sb.append(mobilePhone.charAt(i));
            }
        }
        tvPhonenum.setText("验证码将发送至" + sb.toString());
    }

    @OnClick({R.id.tv_get_vcode, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_get_vcode:
                getVcode();
                break;
            case R.id.btn_confirm:
                if (checkData()) {
                    btnConfirm.submit();
                    requestNetwork();
                }
                break;
        }
    }

    private void requestNetwork() {
        String url = APIBuilder.baseUrl + APIBuilder.version + "appSecurity/forgetPayPassword";
        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        Map<String, String> unNecessaryParams = new HashMap<String, String>();
        necessaryParams.put("mid", "" + SPUtils.get(this, "LOGIN", "id", 0));
        necessaryParams.put("loginpwd", StringToBase64.stringToBase64(loginPwd));
        necessaryParams.put("newpaypwd", StringToBase64.stringToBase64(newTradePwd));
        necessaryParams.put("confirmpwd", StringToBase64.stringToBase64(confirmTradePwd));
        String vcode = etInputVcode.getText().toString();
        necessaryParams.put("smsCode", vcode);
        List<String> keyList = new ArrayList<String>();
        keyList.add("appSecurity/forgetPayPassword");
        keyList.add(necessaryParams.get("mid"));
        keyList.add(necessaryParams.get("loginpwd"));
        keyList.add(necessaryParams.get("newpaypwd"));
        keyList.add(necessaryParams.get("confirmpwd"));
        keyList.add(necessaryParams.get("smsCode"));
        String keyStr = APIBuilder.getKeyStr(keyList);

        necessaryParams.put("keyStr", keyStr);

        unNecessaryParams.put("idcode", identityNum);
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

    private boolean checkData() {
        //获取et的数据
        loginPwd = etLoginPwd.getText().toString().trim();
        newTradePwd = etNewTradePwd.getText().toString().trim();
        confirmTradePwd = etConfirmTradePwd.getText().toString().trim();
        identityNum = etIdentityNumber.getText().toString().toString();
        if (TextUtils.isEmpty(loginPwd)) {
            CommonToastUtils.showToast(ProfileFindTradePwdActivity.this, "登录密码不能为空");
            return false;
        } else if (loginPwd.length() < 6 || loginPwd.length() > 16) {
            CommonToastUtils.showToast(ProfileFindTradePwdActivity.this, "登录密码的长度必须是6到16位之间");
            return false;
        } else if (loginPwd.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0) {
            // 包含特殊字符
            CommonToastUtils.showToast(ProfileFindTradePwdActivity.this, "登录密码不能有特殊字符");
            return false;
        } else if (TextUtils.isEmpty(confirmTradePwd)) {
            CommonToastUtils.showToast(ProfileFindTradePwdActivity.this, "确认密码不能为空");
            return false;
        } else if (confirmTradePwd.length() < 6 || confirmTradePwd.length() > 16) {
            CommonToastUtils.showToast(ProfileFindTradePwdActivity.this, "确认密码的长度必须是6到16位之间");
            return false;
        } else if (confirmTradePwd.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0) {
            // 包含特殊字符
            CommonToastUtils.showToast(ProfileFindTradePwdActivity.this, "确认密码不能有特殊字符");
            return false;
        } else if (TextUtils.isEmpty(newTradePwd)) {
            CommonToastUtils.showToast(ProfileFindTradePwdActivity.this, "交易密码不能为空");
            return false;
        } else if (newTradePwd.length() < 6 || newTradePwd.length() > 16) {
            CommonToastUtils.showToast(ProfileFindTradePwdActivity.this, "交易确认密码的长度必须是6到16位之间");
            return false;
        } else if (newTradePwd.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0) {
            // 包含特殊字符
            CommonToastUtils.showToast(ProfileFindTradePwdActivity.this, "交易密码不能有特殊字符");
            return false;
        } else if (!newTradePwd.equals(confirmTradePwd)) {
            CommonToastUtils.showToast(ProfileFindTradePwdActivity.this, "两次交易密码不一致");
            return false;
        }
        if (llInputIdentityNum.getVisibility() != View.GONE && TextUtils.isEmpty(identityNum)) {
            CommonToastUtils.showToast(this, "身份证号码不能为空");
            return false;
        }
        if (TextUtils.isEmpty(etInputVcode.getText().toString())) {
            CommonToastUtils.showToast(this, "短信验证码不能为空");
            return false;
        }
        return true;
    }

    public void getVcode() {
        String url = APIBuilder.baseUrl + APIBuilder.version + "appAuth/smsByMid";
        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        necessaryParams.put("mid", "" + SPUtils.get(this, "LOGIN", "id", 0));
        necessaryParams.put("action", "resetPaypwd");
        List<String> keyList = new ArrayList<String>();
        keyList.add("appAuth/smsByMid");
        keyList.add(necessaryParams.get("mid"));
        keyList.add(necessaryParams.get("action"));
        String keyStr = APIBuilder.getKeyStr(keyList);

        necessaryParams.put("keyStr", keyStr);

        Map<String, String> params = new HashMap<String, String>();
        params.putAll(necessaryParams);
        HttpClient
                .post()
                .url(url)
                .params(params)
                .build()
                .execute(new GetVcodeCallback());
    }

    class GetVcodeCallback extends Callback<BaseBean> {
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
            showError();
        }

        @Override
        public void onResponse(BaseBean response) {
            if (response.getStatus().equalsIgnoreCase("0000")) {
                CommonToastUtils.showToast(ProfileFindTradePwdActivity.this, "获取到短信验证码");
                countDownTimerUtils.start();
            } else {
                CommonToastUtils.showToast(ProfileFindTradePwdActivity.this, response.getMsg());
            }
        }
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
//            CommonToastUtils.showToast(ProfileFindTradePwdActivity.this, e.toString());
            PhoneUtils.saveSystemErrorInfo(e);
            btnConfirm.finish("确   认");
            showSystemError();
        }

        @Override
        public void onResponse(BaseBean response) {
            btnConfirm.finish("确   认");
            if (response.getStatus().equalsIgnoreCase("0000")) {
//                CommonToastUtils.showToast(ProfileFindTradePwdActivity.this, "交易设置成功！");
                CommonToastUtils.showSuccessToast("修改成功");
                SPUtils.put(getApplicationContext(), "LOGIN", "isPaypwd", true);
//                popActivity();
                MyActivityManager.getInstance().popCurrentActivity();
            } else {
                CommonToastUtils.showToast(ProfileFindTradePwdActivity.this, response.getMsg());
            }

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


    public void showError() {
        showSystemError();
    }
}
