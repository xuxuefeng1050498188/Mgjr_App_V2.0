package com.mgjr.view.profile.accountsetting;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.LogUtil;
import com.mgjr.Utils.PhoneUtils;
import com.mgjr.Utils.SPUtils;
import com.mgjr.httpclient.HttpClient;
import com.mgjr.httpclient.callback.Callback;
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

public class ProfileBindMailBoxActivity extends ActionbarActivity {

    private EditText et_bind_mail;
    private SubmitButton btn_bind_emailbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_profile_bind_mail_box, this);
        actionbar.setCenterTextView("邮箱绑定");
        et_bind_mail = (EditText) findViewById(R.id.et_bind_mail);
        btn_bind_emailbox = (SubmitButton) findViewById(R.id.btn_bind_emailbox);
    }

    public void bindEmail(View view) {
        String mail = et_bind_mail.getText().toString();
        if (TextUtils.isEmpty(mail)) {
            CommonToastUtils.showToast(this, "邮箱地址不能为空");
            return;
        }
        if (!mail.matches("^[a-zA-Z0-9_]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$")) {
            CommonToastUtils.showToast(this, "邮件地址格式不正确");
            return;
        }
        btn_bind_emailbox.submit();
        String url = APIBuilder.baseUrl + APIBuilder.version + "appSecurity/bindEmail";
        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        necessaryParams.put("mid", "" + SPUtils.get(this, "LOGIN", "id", 0));
        necessaryParams.put("email", mail);
        List<String> keyList = new ArrayList<String>();
        keyList.add("appSecurity/bindEmail");
        keyList.add(necessaryParams.get("mid"));
        keyList.add(necessaryParams.get("email"));
        String keyStr = APIBuilder.getKeyStr(keyList);

        necessaryParams.put("keyStr", keyStr);

        Map<String, String> params = new HashMap<String, String>();
        params.putAll(necessaryParams);
        HttpClient
                .get()
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
//            CommonToastUtils.showToast(ProfileBindMailBoxActivity.this, e.toString());
            PhoneUtils.saveSystemErrorInfo(e);
            showSystemError();
            btn_bind_emailbox.finish("确  定");
        }

        @Override
        public void onResponse(BaseBean response) {
            btn_bind_emailbox.finish("确  定");
            if (response.getStatus().equalsIgnoreCase("0000")) {
                showDialog();
            } else {
                CommonToastUtils.showToast(ProfileBindMailBoxActivity.this, response.getMsg());
            }
        }
    }

    private void showDialog() {
        final CustomCommonDialog dialog = new CustomCommonDialog(true, ProfileBindMailBoxActivity.this, "验证邮件已发送", "验证邮件已发送至" + et_bind_mail.getText().toString() + "，请登录邮箱并点击邮件中的链接完成操作", "确定", false);
        dialog.show();
        dialog.setClicklistener(new CustomCommonDialog.ClickListenerInterfaceSingle() {
            @Override
            public void doSingleBtn() {
                dialog.dismiss();
            }
        });
//        CommonToastUtils.showToast(ProfileBindMailBoxActivity.this, "邮箱绑定成功！");
//                SPUtils.put(ProfileBindMailBoxActivity.this, "LOGIN", "email", et_bind_mail.getText().toString());
    }

    public void showError() {
        showSystemError();
    }
}
