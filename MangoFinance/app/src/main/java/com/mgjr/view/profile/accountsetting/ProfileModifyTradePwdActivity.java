package com.mgjr.view.profile.accountsetting;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgjr.R;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.LogUtil;
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

import okhttp3.Call;
import okhttp3.Response;

public class ProfileModifyTradePwdActivity extends ActionbarActivity {
    private EditText etOldTradePwd, etNewTradePwd, etConfirmTradePwd;
    private String oldTradePwd;
    private String newTradePwd;
    private String confirmTradePwd;
    private SubmitButton btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_profile_modify_trade_pwd, this);
        actionbar.setCenterTextView("修改交易密码");
        etOldTradePwd = (EditText) findViewById(R.id.et_old_trade_pwd);
        etNewTradePwd = (EditText) findViewById(R.id.et_new_trade_pwd);
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

    private void requestNetwork() {
        String url = APIBuilder.baseUrl + APIBuilder.version + "appSecurity/changePayPwd";
        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        necessaryParams.put("mid", "" + SPUtils.get(this, "LOGIN", "id", 0));
        necessaryParams.put("oldpwd", StringToBase64.stringToBase64(oldTradePwd));
        necessaryParams.put("newpwd", StringToBase64.stringToBase64(newTradePwd));
        necessaryParams.put("confirmpwd", StringToBase64.stringToBase64(confirmTradePwd));
        List<String> keyList = new ArrayList<String>();
        keyList.add("appSecurity/changePayPwd");
        keyList.add(necessaryParams.get("mid"));
        keyList.add(necessaryParams.get("oldpwd"));
        keyList.add(necessaryParams.get("newpwd"));
        keyList.add(necessaryParams.get("confirmpwd"));
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
            btnConfirm.finish("确   认");
            showError();
        }

        @Override
        public void onResponse(BaseBean response) {
            btnConfirm.finish("确   认");
            if (response.getStatus().equalsIgnoreCase("0000")) {
//                CommonToastUtils.showToast(ProfileModifyTradePwdActivity.this, "支付密码修改成功！");
                CommonToastUtils.showSuccessToast("修改成功");
                SPUtils.put(getApplicationContext(), "LOGIN", "isPaypwd", true);
//                MyActivityManager.getInstance().startNextActivity(ProfilePwdManageActivity.class);
//                popActivity();
                MyActivityManager.getInstance().popCurrentActivity();
            } else {
                CommonToastUtils.showToast(ProfileModifyTradePwdActivity.this, response.getMsg());
            }

        }
    }

    public void showError() {
        showSystemError();
    }

    private boolean checkData() {
        //获取三个et的数据
        oldTradePwd = etOldTradePwd.getText().toString().trim();
        newTradePwd = etNewTradePwd.getText().toString().trim();
        confirmTradePwd = etConfirmTradePwd.getText().toString().trim();
        if (TextUtils.isEmpty(oldTradePwd)) {
            CommonToastUtils.showToast(this, "当前密码不能为空");
            return false;
        }
        if (TextUtils.isEmpty(newTradePwd)) {
            CommonToastUtils.showToast(this, "新密码不能为空");
            return false;
        }
        if (TextUtils.isEmpty(confirmTradePwd)) {
            CommonToastUtils.showToast(this, "确认密码密码不能为空");
            return false;
        }
        if (!newTradePwd.equals(confirmTradePwd)) {
            CommonToastUtils.showToast(this, "两次密码设置不一样");
            return false;
        }
        if (oldTradePwd.length() < 6 || oldTradePwd.length() > 16) {
            CommonToastUtils.showToast(ProfileModifyTradePwdActivity.this, "旧密码的长度必须是6到16位之间");
            return false;
        }
        if (newTradePwd.length() < 6 || newTradePwd.length() > 16) {
            CommonToastUtils.showToast(ProfileModifyTradePwdActivity.this, "新密码的长度必须是6到16位之间");
            return false;
        }
        if (confirmTradePwd.length() < 6 || confirmTradePwd.length() > 16) {
            CommonToastUtils.showToast(ProfileModifyTradePwdActivity.this, "确认密码的长度必须是6到16位之间");
            return false;
        }
        if (oldTradePwd.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0) {
            // 包含特殊字符
            CommonToastUtils.showToast(ProfileModifyTradePwdActivity.this, "旧密码不能有特殊字符");
            return false;
        }
        if (newTradePwd.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0) {
            // 包含特殊字符
            CommonToastUtils.showToast(ProfileModifyTradePwdActivity.this, "新密码不能有特殊字符");
            return false;
        }
        if (confirmTradePwd.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0) {
            // 包含特殊字符
            CommonToastUtils.showToast(ProfileModifyTradePwdActivity.this, "确认密码不能有特殊字符");
            return false;
        }
        return true;
    }
}
