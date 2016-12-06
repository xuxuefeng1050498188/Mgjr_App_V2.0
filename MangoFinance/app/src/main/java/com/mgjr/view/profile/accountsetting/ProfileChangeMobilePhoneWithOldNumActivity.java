package com.mgjr.view.profile.accountsetting;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.Constant;
import com.mgjr.Utils.CountDownTimerUtils;
import com.mgjr.Utils.LogUtil;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.PhoneUtils;
import com.mgjr.Utils.SPUtils;
import com.mgjr.httpclient.HttpClient;
import com.mgjr.httpclient.callback.Callback;
import com.mgjr.model.bean.BaseBean;
import com.mgjr.model.bean.GetVcodeBean;
import com.mgjr.presenter.impl.ChangeMobilePresenterImpl;
import com.mgjr.presenter.impl.RegisterGetSmsCodePresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.APIBuilder;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.view.listeners.ViewListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Response;

public class ProfileChangeMobilePhoneWithOldNumActivity extends ActionbarActivity implements View.OnClickListener, ViewListener<GetVcodeBean> {
    private RegisterGetSmsCodePresenterImpl changeMobileGetSmsCodePresenter;
    private ChangeMobilePresenterImpl changeMobilePresenter;
    private GetVcodeBean getVcodeBean;
    private TextView tv_mobile;
    private RelativeLayout btn_sure;
    private Button btnGetVcode;
    private EditText et_input_smscode;
    private String smsCode;
    private String mobile;
    private String action;
    private CountDownTimerUtils countDownTimerUtils;
    private boolean isSuccessGetVcode;
    private String vcode;
    private LinearLayout ll_receive_state;
    private EditText et_input_phone_num;
    private TextView tv_sure_btn;
    private LinearLayout ll_input_new_phone_num;
    private boolean isRequestNewVcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_profile_change_phone_num_with_old_num, this);
        actionbar.setCenterTextView("更换手机号");
        initViews();
        initData();
    }


    private void initViews() {
        ll_input_new_phone_num = (LinearLayout) findViewById(R.id.ll_input_new_phone_num);
        tv_sure_btn = (TextView) findViewById(R.id.tv_sure_btn);
        ll_receive_state = (LinearLayout) findViewById(R.id.ll_receive_state);
        et_input_phone_num = (EditText) findViewById(R.id.et_input_phone_num);
        changeMobileGetSmsCodePresenter = new RegisterGetSmsCodePresenterImpl(this);
        changeMobilePresenter = new ChangeMobilePresenterImpl(this);
        tv_mobile = (TextView) findViewById(R.id.tv_mobile);
        btn_sure = (RelativeLayout) findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(this);
        et_input_smscode = (EditText) findViewById(R.id.et_input_smscode);
        btnGetVcode = (Button) findViewById(R.id.getVcode);
        btnGetVcode.setOnClickListener(this);
        countDownTimerUtils = new CountDownTimerUtils(btnGetVcode, 60000, 1000);
        ll_input_new_phone_num.setVisibility(View.GONE);


    }

    private void initData() {
        mobile = (String) SPUtils.get(this, "LOGIN", "mobilePhone", "");
        String smobile = mobile.substring(0, mobile.length() - (mobile.substring(3)).length()) + "****" + mobile.substring(7);
        tv_mobile.setText(smobile);
    }

    @Override
    public void onClick(View v) {
        if (v == btnGetVcode) {
            //获取验证码
            getVcode();

        } else if (v == btn_sure) {
            if (ll_input_new_phone_num.getVisibility() == View.GONE) {
                if (isSuccessGetVcode == false) {
                    CommonToastUtils.showToast(this, "请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(et_input_smscode.getText().toString())) {
                    CommonToastUtils.showToast(this, "验证码不能为空");
                    return;
                }
                requestOldNumNetwork();
            } else {
               /* btnGetVcode.setText("获取验证码");
                btnGetVcode.setTextColor(Color.parseColor("#72CAF4"));
                btnGetVcode.setClickable(true);//重新获得点击
                btnGetVcode.setBackgroundResource(R.drawable.shape_invest_list_item_blue_rectangle);  //还原背景色*/
                requestNewNetwork();
            }

        }
    }

    public void getVcode() {
        if (isRequestNewVcode) {
            action = Constant.GET_SMSCODE_TYPE_CHANGE_MOBILE;
            if (TextUtils.isEmpty(et_input_phone_num.getText().toString())) {
                CommonToastUtils.showToast(this, "新手机号码不能为空");
                return;
            }
            if (!et_input_phone_num.getText().toString().matches("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$")) {
                CommonToastUtils.showToast(this, "新手机号码格式不正确");
                return;
            }
            mobile = et_input_phone_num.getText().toString();
        } else {
            action = Constant.GET_SMSCODE_TYPE_CHANGE_MOBILE_OLD;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobile);
        params.put("action", action);
        changeMobileGetSmsCodePresenter.sendRequest(params, null);
    }

    public void requestNewNetwork() {

        if (TextUtils.isEmpty(et_input_phone_num.getText().toString())) {
            CommonToastUtils.showToast(this, "新手机号码不能为空");
            return;
        }
        if (!et_input_phone_num.getText().toString().matches("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$")) {
            CommonToastUtils.showToast(this, "新手机号码格式不正确");
            return;
        }
        if (TextUtils.isEmpty(et_input_smscode.getText().toString())) {
            CommonToastUtils.showToast(this, "验证码不能为空");
            return;
        }
        String mid = SPUtils.get(this, "LOGIN", "id", 0) + "";
        String smsCode = et_input_smscode.getText().toString();
        if (smsCode.equalsIgnoreCase("")) {
            CommonToastUtils.showToast(this, getResources().getString(R.string.smscode_cant_be_null));
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("mid", mid);
            params.put("mobile", et_input_phone_num.getText().toString());
            params.put("smsCode", et_input_smscode.getText().toString());
            changeMobilePresenter.sendRequest(params, null);
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(OnPresenterListener listener, GetVcodeBean getVcodeBean) {
        CommonToastUtils.showToast(this, getVcodeBean.getMsg());
    }

    @Override
    public void responseData(OnPresenterListener listener, GetVcodeBean getVcodeBean) {
        if (listener instanceof RegisterGetSmsCodePresenterImpl) {
            if (getVcodeBean.getStatus().equalsIgnoreCase("0000")) {
                CommonToastUtils.showToast(ProfileChangeMobilePhoneWithOldNumActivity.this, "短信验证码已发送至手机");
                countDownTimerUtils.start();
                isSuccessGetVcode = true;
            } else {
                CommonToastUtils.showToast(ProfileChangeMobilePhoneWithOldNumActivity.this, getVcodeBean.getMsg());
            }
        } else {
            CommonToastUtils.showToast(ProfileChangeMobilePhoneWithOldNumActivity.this, "手机号码修改成功");
//            popActivity();
            MyActivityManager.getInstance().finishSpecifiedActivity(ProfileChangeMobilePhoneActivity.class);
            MyActivityManager.getInstance().popCurrentActivity();
        }
    }

    private void requestOldNumNetwork() {
        String url = APIBuilder.baseUrl + APIBuilder.version + "appSecurity/validateOldPhone";
        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        necessaryParams.put("mobile", mobile);
        necessaryParams.put("smsCode", et_input_smscode.getText().toString());
        List<String> keyList = new ArrayList<String>();
        keyList.add("appSecurity/validateOldPhone");
        keyList.add(necessaryParams.get("mobile"));
        keyList.add(necessaryParams.get("smsCode"));
        String keyStr = APIBuilder.getKeyStr(keyList);

        necessaryParams.put("keyStr", keyStr);

        Map<String, String> params = new HashMap<String, String>();
        params.putAll(necessaryParams);
        HttpClient
                .post()
                .url(url)
                .params(params)
                .build()
                .execute(new OldPhoneNumValidateCallBack());
    }

    class OldPhoneNumValidateCallBack extends Callback<BaseBean> {
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
                CommonToastUtils.showToast(ProfileChangeMobilePhoneWithOldNumActivity.this, "旧手机验证成功");
                updateBg();
            } else {
                CommonToastUtils.showToast(ProfileChangeMobilePhoneWithOldNumActivity.this, response.getMsg());
            }

        }
    }

    public void updateBg() {
        ll_receive_state.setVisibility(View.GONE);
        ll_input_new_phone_num.setVisibility(View.VISIBLE);
        et_input_smscode.setText("");
        tv_sure_btn.setText("提交");
        isRequestNewVcode = true;
        countDownTimerUtils.refresh();
    }

    public void showError() {
        showSystemError();
    }
}
