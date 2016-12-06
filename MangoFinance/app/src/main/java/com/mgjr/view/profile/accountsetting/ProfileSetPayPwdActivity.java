package com.mgjr.view.profile.accountsetting;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.LogUtil;
import com.mgjr.Utils.PhoneUtils;
import com.mgjr.Utils.SPUtils;
import com.mgjr.Utils.StringToBase64;
import com.mgjr.httpclient.HttpClient;
import com.mgjr.httpclient.callback.Callback;
import com.mgjr.R;
import com.mgjr.model.bean.BaseBean;
import com.mgjr.share.APIBuilder;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.CustomCommonDialog;
import com.mgjr.share.SubmitButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class ProfileSetPayPwdActivity extends ActionbarActivity {

    private EditText etTradePwd, etConfirmTradePwd;
    private SubmitButton btnConfirm;
    private String tradePwd;
    private String confirmTradePwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_profile_trade_pwd, this);
        initActionBar();
        etTradePwd = (EditText) findViewById(R.id.et_trade_pwd);
        etConfirmTradePwd = (EditText) findViewById(R.id.et_confirm_trade_pwd);
        btnConfirm = (SubmitButton) findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkData()) {
                    btnConfirm.submit();
                    requestNetwork();
                }
            }
        });
    }

    private void initActionBar() {
        actionbar.setCenterTextView("交易密码");
        actionbar.leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CustomCommonDialog dialog = new CustomCommonDialog(ProfileSetPayPwdActivity.this, "温馨提示", "交易密码对您的资金安全非常重要，\n" +
                        "您确定放弃设置吗？", "继续", "放弃", false);
                dialog.setClicklistener(new CustomCommonDialog.ClickListenerInterface() {
                    @Override
                    public void doConfirm() {
                        MyActivityManager.getInstance().popCurrentActivity();
                        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
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

    private void requestNetwork() {
        String url = APIBuilder.baseUrl + APIBuilder.version + "appSecurity/setPayPwd";
        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        necessaryParams.put("mid", "" + SPUtils.get(this, "LOGIN", "id", 0));
        necessaryParams.put("paypwd", StringToBase64.stringToBase64(tradePwd));
        necessaryParams.put("paypwd2", StringToBase64.stringToBase64(confirmTradePwd));
        List<String> keyList = new ArrayList<String>();
        keyList.add("appSecurity/setPayPwd");
        keyList.add(necessaryParams.get("mid"));
        keyList.add(necessaryParams.get("paypwd"));
        keyList.add(necessaryParams.get("paypwd2"));
        String keyStr = APIBuilder.getKeyStr(keyList);

        necessaryParams.put("keyStr", keyStr);

        Map<String, String> params = new HashMap<String, String>();
        params.putAll(necessaryParams);
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
            btnConfirm.finish("确   定");
            showError();
        }

        @Override
        public void onResponse(BaseBean response) {
            btnConfirm.finish("确   定");
            if (response.getStatus().equalsIgnoreCase("0000")) {
//                CommonToastUtils.showToast(ProfileSetPayPwdActivity.this, "交易密码设置成功！");
                CommonToastUtils.showSuccessToast("设置成功");
                SPUtils.put(getApplicationContext(), "LOGIN", "isPaypwd", true);
//                MyActivityManager.getInstance().startNextActivity(ProfilePwdManageActivity.class);
//                popActivity();
                MyActivityManager.getInstance().popCurrentActivity();
            } else {
                CommonToastUtils.showToast(ProfileSetPayPwdActivity.this, response.getMsg());
            }

        }
    }

    public void showError() {
        showSystemError();
    }

    private boolean checkData() {
        //获取两个个et的数据
        tradePwd = etTradePwd.getText().toString().trim();
        confirmTradePwd = etConfirmTradePwd.getText().toString().trim();
        if (tradePwd.equalsIgnoreCase("")) {
            CommonToastUtils.showToast(ProfileSetPayPwdActivity.this, "密码不能为空");
            return false;
        } else if (tradePwd.length() < 6 || tradePwd.length() > 16) {
            CommonToastUtils.showToast(ProfileSetPayPwdActivity.this, "密码的长度必须是6到16位之间");
            return false;
        } else if (tradePwd.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0) {
            // 包含特殊字符
            CommonToastUtils.showToast(ProfileSetPayPwdActivity.this, "密码不能有特殊字符");
            return false;
        } else if (confirmTradePwd.equalsIgnoreCase("")) {
            CommonToastUtils.showToast(ProfileSetPayPwdActivity.this, "确认密码不能为空");
            return false;
        } else if (confirmTradePwd.length() < 6 || confirmTradePwd.length() > 16) {
            CommonToastUtils.showToast(ProfileSetPayPwdActivity.this, "确认密码的长度必须是6到16位之间");
            return false;
        } else if (confirmTradePwd.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0) {
            // 包含特殊字符
            CommonToastUtils.showToast(ProfileSetPayPwdActivity.this, "确认密码不能有特殊字符");
            return false;
        } else if (!tradePwd.equals(confirmTradePwd)) {
            CommonToastUtils.showToast(ProfileSetPayPwdActivity.this, "两次密码不一致");
            return false;
        }
        return true;
    }
}
