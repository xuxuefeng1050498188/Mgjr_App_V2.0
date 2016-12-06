package com.mgjr.view.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mgjr.R;
import com.mgjr.Utils.CommonTextUtils;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.Constant;
import com.mgjr.Utils.CountDownTimerUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.StringToBase64;
import com.mgjr.model.bean.BaseBean;
import com.mgjr.presenter.impl.GetForgetLoginPwdSmsCodePresenterImpl;
import com.mgjr.presenter.impl.ResetLoginPwdPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.SubmitButton;
import com.mgjr.view.listeners.ViewListener;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mgjr.Utils.HideInputUtils.isShouldHideInput;

/**
 * Created by Administrator on 2016/7/8.
 */
public class ForgetPasswordActivity extends ActionbarActivity implements View.OnClickListener, ViewListener<BaseBean> {

    private GetForgetLoginPwdSmsCodePresenterImpl forgetLoginPwdPresenter;

    private ResetLoginPwdPresenterImpl resetLoginPwdPresenter;

    private Button btn_forgetpassword_getcode;
    private SubmitButton btn_forgetpassword_sure;

    private EditText et_forgetpassword_phonenumber, et_forgetpassword_smscode, et_register_inputnewpwd;
    private ImageView imgbtn_eye_forgetpwd;
    private String mobile, smsCode, newpwd;

    private CountDownTimerUtils mCountDownTimerUtils;

    private LinearLayout layout_pwdeye_forgetpwd;

    //    private ImageView backBtn;
    private boolean img_forgetpwd_pwdeye_isPressed = false;

    private boolean isMobileNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.layout_activity_forgetpasswod, this);
        actionbar.setCenterTextView(getResources().getString(R.string.getforgetpwd));
        initView();
    }

    private void initView() {

        forgetLoginPwdPresenter = new GetForgetLoginPwdSmsCodePresenterImpl(this);
        resetLoginPwdPresenter = new ResetLoginPwdPresenterImpl(this);
        imgbtn_eye_forgetpwd = (ImageView) findViewById(R.id.imgbtn_eye_forgetpwd);
        btn_forgetpassword_getcode = (Button) findViewById(R.id.btn_forgetpassword_getcode);
        btn_forgetpassword_sure = (SubmitButton) findViewById(R.id.btn_forgetpassword_sure);
        btn_forgetpassword_getcode.setOnClickListener(this);
        btn_forgetpassword_sure.setOnClickListener(this);

        et_forgetpassword_phonenumber = (EditText) findViewById(R.id.et_forgetpassword_phonenumber);
        et_forgetpassword_smscode = (EditText) findViewById(R.id.et_forgetpassword_smscode);
        et_register_inputnewpwd = (EditText) findViewById(R.id.et_register_inputnewpwd);

        layout_pwdeye_forgetpwd = (LinearLayout) findViewById(R.id.layout_pwdeye_forgetpwd);
        layout_pwdeye_forgetpwd.setOnClickListener(this);

        mCountDownTimerUtils = new CountDownTimerUtils(btn_forgetpassword_getcode, 60000, 1000);
    }

    @Override
    public void onClick(View v) {
        mobile = et_forgetpassword_phonenumber.getText().toString();

        if (v == layout_pwdeye_forgetpwd) {

            if (img_forgetpwd_pwdeye_isPressed) {
                //隐藏密码
                et_register_inputnewpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imgbtn_eye_forgetpwd.setBackgroundResource(R.drawable.login_eye_btn);
            } else {
                et_register_inputnewpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imgbtn_eye_forgetpwd.setBackgroundResource(R.drawable.login_eye_btn_normal);
            }
            img_forgetpwd_pwdeye_isPressed = !img_forgetpwd_pwdeye_isPressed;
            et_register_inputnewpwd.postInvalidate();
            //切换后将EditText光标置于末尾
            CharSequence charSequence = et_register_inputnewpwd.getText();
            if (charSequence instanceof Spannable) {
                Spannable spanText = (Spannable) charSequence;
                Selection.setSelection(spanText, charSequence.length());
            }
        } else if (v == btn_forgetpassword_sure) {  //确定按钮
            newpwd = et_register_inputnewpwd.getText().toString();
            smsCode = et_forgetpassword_smscode.getText().toString();
            if (mobile.equalsIgnoreCase("")) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.mobilenumber_cant_be_null));
            } else if (mobile.length() != 11) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.mobilenumber_limit));
            } else if (isMobileNO(mobile) == false) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.input_right_mobilenumber));
            } else if (newpwd.equalsIgnoreCase("") || newpwd.length() > 16 || newpwd.length() < 6) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.password_limit));
            } else if (newpwd.contains(" ")) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.password_cant_contains_space));
            } else if (CommonTextUtils.isConSpeCharacters(newpwd)) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.password_format));
            } else {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", mobile);
                params.put("smsCode", smsCode);
                params.put("newpwd", StringToBase64.stringToBase64(newpwd));
                resetLoginPwdPresenter.sendRequest(params, null);
                btn_forgetpassword_sure.submit();
                btn_forgetpassword_sure.setClickable(false);
            }

        } else if (v == btn_forgetpassword_getcode) {//获取验证码

            if (mobile.equalsIgnoreCase("")) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.mobilenumber_cant_be_null));
            } else if (mobile.length() != 11) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.mobilenumber_limit));
            } else if (isMobileNO(mobile) == false) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.input_right_mobilenumber));
            } else {

                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", mobile);
                params.put("action", Constant.GET_SMSCODE_TYPE_FORGETLOGINPWD);
                forgetLoginPwdPresenter.sendRequest(params, null);
                btn_forgetpassword_getcode.setClickable(false);
            }
        }

    }

    public boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        isMobileNo = m.matches();
        return isMobileNo;
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

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError() {
        showSystemError();
        btn_forgetpassword_sure.finish("确定");
        btn_forgetpassword_sure.setClickable(true);
        btn_forgetpassword_getcode.setClickable(true);
    }

    @Override
    public void showError(OnPresenterListener listener, BaseBean baseBean) {
        CommonToastUtils.showToast(this, baseBean.getMsg());
        btn_forgetpassword_sure.finish("确定");
        btn_forgetpassword_sure.setClickable(true);
        mCountDownTimerUtils.refresh();
        btn_forgetpassword_getcode.setClickable(true);
    }

    @Override
    public void responseData(OnPresenterListener listener, BaseBean baseBean) {
        if (listener instanceof ResetLoginPwdPresenterImpl) {
//            CommonToastUtils.showToast(this, getResources().getString(R.string.reset_pwd_sucess));
            CommonToastUtils.showSuccessToast("修改成功");
            btn_forgetpassword_sure.finish("确定");
            btn_forgetpassword_sure.setClickable(true);
//            popActivity();
            MyActivityManager.getInstance().popCurrentActivity();
        } else if (listener instanceof GetForgetLoginPwdSmsCodePresenterImpl) {
            btn_forgetpassword_getcode.setClickable(true);
            String status = baseBean.getStatus();
            if (status.equalsIgnoreCase("0000")) {
                mCountDownTimerUtils.start();
                CommonToastUtils.showToast(this, getResources().getString(R.string.smscode_has_send));
            }
        }
    }
}
