package com.mgjr.view.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.Constant;
import com.mgjr.Utils.CountDownTimerUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.model.bean.GetVcodeBean;
import com.mgjr.presenter.impl.GetVcodePresenterImpl;
import com.mgjr.presenter.impl.RegisterGetSmsCodePresenterImpl;
import com.mgjr.presenter.impl.RegisterStepOnePresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.SubmitButton;
import com.mgjr.view.listeners.ViewListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mgjr.Utils.HideInputUtils.isShouldHideInput;

/**
 * Created by Administrator on 2016/6/12.
 */
public class RegisterActivityStepOne extends ActionbarActivity implements View.OnClickListener, ViewListener<GetVcodeBean> {
    //注册的用户名和验证码
    private EditText et_userRegisterName, et_userRegisterCode;
    //获取验证码按钮
    private Button btn_getCode;
    //用户手机是否被注册状态
    private TextView tv_userPhoneNumberStatus;
    //跳转到下个注册界面按钮
    private SubmitButton btn_toNext_register;
    //跳转到登录界面按钮
    private TextView register_btn_toLogin;
    //图形验证码布局
    private LinearLayout layout_register_imgcode;
    //图形验证码输入框
    private EditText et_register_imgCode;
    //获取图形验证码按钮
    private Button btn_register_getImgCode;
    //图形验证码
    private String vCode;
    //    private ImageView backBtn;
    //用户名(手机号码)   、 短信验证码
    private String mobile, smsCode;

    private String returnMobile;
    private boolean isMobileNo;
    private boolean isVisiable = false;
    private CountDownTimerUtils mCountDownTimerUtils;
    private Thread mThread;

    private int clickNum = 0;//获取验证码次数
    private String status;

    private RegisterStepOnePresenterImpl registerStepOnePresenter;
    private RegisterGetSmsCodePresenterImpl registerGetSmsCodePresenter;
    private GetVcodePresenterImpl getVcodePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.layout_register_first, this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        autoShowKeyboard();
        resetMobile();
        //请求图形验证码
        getVcodePresenter.sendRequest(null, null);
    }

    /*从第二步返回记住手机号码*/
    private void resetMobile() {
        returnMobile = getIntent().getStringExtra("code");
        if (returnMobile != null) {
            et_userRegisterName.setText(returnMobile);
        }
    }

    /*
        * 自动打开软键盘
        * */
    private void autoShowKeyboard() {

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            public void run() {

                InputMethodManager inputManager = (InputMethodManager) et_userRegisterName.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_userRegisterName, 0);
            }

        }, 998);

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

    private void initView() {

        registerGetSmsCodePresenter = new RegisterGetSmsCodePresenterImpl(this);
        registerStepOnePresenter = new RegisterStepOnePresenterImpl(this);
        getVcodePresenter = new GetVcodePresenterImpl(this);
        layout_register_imgcode = (LinearLayout) findViewById(R.id.layout_register_imgcode);

        et_userRegisterName = (EditText) findViewById(R.id.et_userRegisterName);
        et_userRegisterCode = (EditText) findViewById(R.id.et_userRegisterCode);
        et_register_imgCode = (EditText) findViewById(R.id.et_register_imgCode);
        register_btn_toLogin = (TextView) findViewById(R.id.register_btn_toLogin);

        btn_getCode = (Button) findViewById(R.id.btn_register1_getCode);
        btn_getCode.setOnClickListener(this);
        btn_register_getImgCode = (Button) findViewById(R.id.btn_register_getImgCode);
        btn_register_getImgCode.setOnClickListener(this);

        btn_toNext_register = (SubmitButton) findViewById(R.id.btn_toNext_register);

        btn_toNext_register.setOnClickListener(this);
        register_btn_toLogin.setOnClickListener(this);

        mCountDownTimerUtils = new CountDownTimerUtils(btn_getCode, 60000, 1000);
    }


    @Override
    public void onClick(View v) {

        mobile = et_userRegisterName.getText().toString();
        smsCode = et_userRegisterCode.getText().toString();

        if (v == btn_toNext_register) {
            if (TextUtils.isEmpty(mobile)) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.input_mobilenumber));
            } else if (mobile.equalsIgnoreCase("")) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.mobilenumber_cant_be_null));
            } else if (isVisiable == true && TextUtils.isEmpty(et_register_imgCode.getText().toString())) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.input_imgcode));
            } else if (isMobileNum(mobile) == false) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.input_right_mobilenumber));
                return;
            } else if (isVisiable == true && !TextUtils.isEmpty(et_register_imgCode.getText().toString()) && !et_register_imgCode.getText().toString().equalsIgnoreCase(vCode)) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.input_right_imgcode));
                getVcodePresenter.sendRequest(null, null);
            } else if (smsCode.equalsIgnoreCase("")) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.smscode_cant_be_null));
            } else if (smsCode.contains(" ")) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.smscode_cant_contains_space));
            } else {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", mobile);
                params.put("smsCode", smsCode);
                registerStepOnePresenter.sendRequest(params, null);
                btn_toNext_register.submit();
                btn_toNext_register.setClickable(false);
            }

        } else if (v == register_btn_toLogin) {
            MyActivityManager.getInstance().startNextActivity(LoginActivity.class, "fromRegister");
            MyActivityManager.getInstance().finishSpecifiedActivity(RegisterActivityStepOne.class);
        } else if (v == btn_getCode) {

            if (mobile.equalsIgnoreCase("")) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.mobilenumber_cant_be_null));
                return;
            } else if (mobile.length() != 11) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.mobilenumber_limit));
                return;

            } else if (isMobileNum(mobile) == false) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.input_right_mobilenumber));
                return;
            } else if (TextUtils.isEmpty(et_register_imgCode.getText().toString())) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.input_imgcode));
            } else if (!TextUtils.isEmpty(et_register_imgCode.getText().toString()) && !et_register_imgCode.getText().toString().equalsIgnoreCase(vCode)) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.input_right_imgcode));
                et_register_imgCode.setText("");
                getVcodePresenter.sendRequest(null, null);
            } else {

                Map<String, String> params = new HashMap<String, String>();
                Map<String, String> unnecessaryparams = new HashMap<String, String>();

                params.put("mobile", mobile);
                params.put("action", Constant.GET_SMSCODE_TYPE_REGISTER);
                unnecessaryparams.put("vCode", vCode);
                registerGetSmsCodePresenter.sendRequest(params, unnecessaryparams);
                btn_getCode.setClickable(false);
            }
        } else if (v == btn_register_getImgCode) {
            getVcodePresenter.sendRequest(null, null);
            btn_register_getImgCode.setClickable(false);
        }
    }

    public boolean isMobileNum(String mobiles) {
        Pattern p = Pattern.compile("^1(3[0-9]|4[57]|5[0-35-9]|7[01678]|8[0-9])\\d{8}$");
        Matcher m = p.matcher(mobiles);
        isMobileNo = m.matches();
        return isMobileNo;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError() {
        showSystemError();
//        loadingView.stopAnimation();
        btn_toNext_register.finish("下一步");
        btn_toNext_register.setClickable(true);
        btn_register_getImgCode.setClickable(true);
        btn_getCode.setClickable(true);
    }

    @Override
    public void showError(OnPresenterListener listener, GetVcodeBean getVcodeBean) {
        CommonToastUtils.showToast(this, getVcodeBean.getMsg().toString());
        et_register_imgCode.setText("");
        getVcodePresenter.sendRequest(null, null);
        btn_toNext_register.finish("下一步");
        btn_toNext_register.setClickable(true);
        mCountDownTimerUtils.refresh();
        btn_register_getImgCode.setClickable(true);
        btn_getCode.setClickable(true);
    }

    @Override
    public void responseData(OnPresenterListener listener, GetVcodeBean getVcodeBean) {

        if (listener instanceof RegisterStepOnePresenterImpl) {
            btn_toNext_register.finish("下一步");
            btn_toNext_register.setClickable(true);
//            if (clickNum < 3) {
//                MyActivityManager.getInstance().startNextActivity(RegisterAcitivityStepTwo.class, mobile);
//                MyActivityManager.getInstance().finishSpecifiedActivity(RegisterActivityStepOne.class);
//            } else {
            if (et_register_imgCode.getText().toString().equalsIgnoreCase(vCode)) {
                MyActivityManager.getInstance().startNextActivity(RegisterAcitivityStepTwo.class, mobile);
                MyActivityManager.getInstance().finishSpecifiedActivity(RegisterActivityStepOne.class);
            } else {
                CommonToastUtils.showToast(this, getResources().getString(R.string.input_right_imgcode));
                et_register_imgCode.setText("");
                getVcodePresenter.sendRequest(null, null);
            }
//            }
        } else if (listener instanceof GetVcodePresenterImpl) {
            btn_register_getImgCode.setClickable(true);
            vCode = getVcodeBean.getRand();
            btn_register_getImgCode.setText(vCode);
        } else if (listener instanceof RegisterGetSmsCodePresenterImpl) {
            btn_getCode.setClickable(true);
            status = getVcodeBean.getStatus();
            if (status.equalsIgnoreCase("0000")) {
                mCountDownTimerUtils.start();
                CommonToastUtils.showToast(this, getResources().getString(R.string.smscode_has_send));
//                clickNum++;
//                if (clickNum >= 3) {
//                    isVisiable = true;
                layout_register_imgcode.setVisibility(View.VISIBLE);
//                mCountDownTimerUtils.cancel();
//                    clickNum = 0;
//                }
            }
        }
    }

}
