package com.mgjr.view.common;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.CommonTextUtils;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.SPUtils;
import com.mgjr.Utils.StringToBase64;
import com.mgjr.mangofinance.MainActivity;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.model.bean.EventBusBean.ChangeTabBean;
import com.mgjr.model.bean.GetVcodeBean;
import com.mgjr.model.bean.UserBean;
import com.mgjr.presenter.impl.GetVcodePresenterImpl;
import com.mgjr.presenter.impl.LoginPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.CustomCommonDialog;
import com.mgjr.share.LoadingView;
import com.mgjr.share.SubmitButton;
import com.mgjr.view.listeners.ViewListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import static com.mgjr.Utils.HideInputUtils.isShouldHideInput;


/**
 * Created by Administrator on 2016/7/7.
 */
public class LoginActivity extends ActionbarActivity implements View.OnClickListener, ViewListener<GetVcodeBean> {
    private LoadingView loadingView;
    private ImageView imgbtn_eye_login;
    //输入的用户名、密码
    private EditText et_login_inputusername, et_login_inputpwd;
    //显示、隐藏密码btn
    private LinearLayout layout_pwdeye_login;

    private boolean imgbtn_pwdeye_isPressed = false;
    //忘记密码
    private TextView tv_forgetpwd;
    //登录按钮
    private SubmitButton btn_login;
    //跳转注册按钮
    private TextView tv_toregister;

    private String username, password, vCode;

    //图形验证码
    private LinearLayout layout_login_imgcode;
    //图形验证码输入框
    private EditText et_userLogin_imgCode;
    //图形验证码获取按钮
    private Button btn_login_getImgCode;
    //登录密码错误次数
    private int errorCount;
    //是否显示图形验证码
    private boolean isVisible = false;
    private CustomCommonDialog customCommonDialog;

    private String intentSource;
    private LoginPresenterImpl loginPresenter;

    private GetVcodePresenterImpl getVcodePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.login_layout, LoginActivity.this);
        initView();
    }

    private void initView() {
        intentSource = getIntent().getStringExtra("code");
        btn_login = (SubmitButton) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        btn_login_getImgCode = (Button) findViewById(R.id.btn_login_getImgCode);
        btn_login_getImgCode.setOnClickListener(this);

        et_login_inputusername = (EditText) findViewById(R.id.et_login_inputusername);
        et_login_inputpwd = (EditText) findViewById(R.id.et_login_inputpwd);
        et_userLogin_imgCode = (EditText) findViewById(R.id.et_userLogin_imgCode);

        layout_pwdeye_login = (LinearLayout) findViewById(R.id.layout_pwdeye_login);
        layout_pwdeye_login.setOnClickListener(this);
        imgbtn_eye_login = (ImageView) findViewById(R.id.imgbtn_eye_login);

        tv_forgetpwd = (TextView) findViewById(R.id.tv_forgetpwd);
        tv_forgetpwd.setOnClickListener(this);

        tv_toregister = (TextView) findViewById(R.id.tv_toregister);
        tv_toregister.setOnClickListener(this);

        layout_login_imgcode = (LinearLayout) findViewById(R.id.layout_login_imgcode);

        loginPresenter = new LoginPresenterImpl(this);
        getVcodePresenter = new GetVcodePresenterImpl(this);
//        loadingView = (LoadingView) findViewById(R.id.loadingview_login);
    }

    @Override
    public void onClick(View v) {

        if (v == btn_login) {
            username = et_login_inputusername.getText().toString();
            password = et_login_inputpwd.getText().toString();
            if (username.equalsIgnoreCase("")) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.mobilenumber_cant_be_null));
            } else if (password.equalsIgnoreCase("") || password.length() > 16 || password.length() < 6) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.password_limit));
            } else if (password.contains(" ")) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.password_cant_contains_space));
            } else if (CommonTextUtils.isConSpeCharacters(password)) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.password_format));
            } else if (isVisible == true && TextUtils.isEmpty(et_userLogin_imgCode.getText().toString())) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.input_imgcode));
            } else if (isVisible == true && !TextUtils.isEmpty(et_userLogin_imgCode.getText().toString()) && !et_userLogin_imgCode.getText().toString().equalsIgnoreCase(vCode)) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.input_right_imgcode));
                et_userLogin_imgCode.setText("");
                getVcodePresenter.sendRequest(null, null);
            } else {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", StringToBase64.stringToBase64(password));

                Map<String, String> uParams = new HashMap<>();
                if (vCode != null) {
                    uParams.put("vCode", vCode);
                }
                loginPresenter.sendRequest(params, uParams);
                btn_login.submit();
                btn_login.setClickable(false);
            }
        } else if (v == tv_forgetpwd) {
            MyActivityManager.getInstance().startNextActivity(ForgetPasswordActivity.class);
        } else if (v == tv_toregister) {
            MyActivityManager.getInstance().startNextActivity(RegisterActivityStepOne.class);
            MyActivityManager.getInstance().finishSpecifiedActivity(LoginActivity.class);
        } else if (v == layout_pwdeye_login) {
            if (imgbtn_pwdeye_isPressed) {
                //隐藏密码
                et_login_inputpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imgbtn_eye_login.setBackgroundResource(R.drawable.login_eye_btn);
            } else {
                et_login_inputpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imgbtn_eye_login.setBackgroundResource(R.drawable.login_eye_btn_normal);
            }
            imgbtn_pwdeye_isPressed = !imgbtn_pwdeye_isPressed;
            et_login_inputpwd.postInvalidate();
            //切换后将EditText光标置于末尾
            CharSequence charSequence = et_login_inputpwd.getText();
            if (charSequence instanceof Spannable) {
                Spannable spanText = (Spannable) charSequence;
                Selection.setSelection(spanText, charSequence.length());
            }
        } else if (v == btn_login_getImgCode) {
            getVcodePresenter.sendRequest(null, null);
            btn_login_getImgCode.setClickable(false);
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

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showError() {
        showSystemError();
        btn_login.finish("登  录");
        btn_login.setClickable(true);
        btn_login_getImgCode.setClickable(true);
    }

    @Override
    public void showError(OnPresenterListener listener, GetVcodeBean getVcodeBean) {
        CommonToastUtils.showToast(this, getVcodeBean.getMsg());
        btn_login.finish("登   录");
        btn_login.setClickable(true);
        btn_login_getImgCode.setClickable(true);
        getVcodePresenter.sendRequest(null, null);
        if (getVcodeBean.getErrorMap() != null) {
            errorCount = getVcodeBean.getErrorMap().get("loginErrorCount");
            layout_login_imgcode.setVisibility(View.VISIBLE);
            isVisible = true;
            btn_login.finish("登   录");
        }
    }

    @Override
    public void responseData(OnPresenterListener listener, GetVcodeBean getVcodeBean) {

        if (listener instanceof LoginPresenterImpl) {
//            loadingView.stopAnimation();
            btn_login.finish();
            btn_login.setClickable(true);
            if (isVisible == true) {
                //判断输入的验证码是否和服务器返回的一致
                if (et_userLogin_imgCode.getText().toString().equalsIgnoreCase(vCode)) {
                    layout_login_imgcode.setVisibility(View.GONE);
                    isVisible = false;
                    //保存用户信息
                    saveUserInfo(getVcodeBean);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            popActivity();
                            MyActivityManager.getInstance().finishSpecifiedActivity(LoginActivity.class);
                        }
                    }, 300);
                } else {
                    CommonToastUtils.showToast(this, getResources().getString(R.string.input_right_imgcode));
                    et_userLogin_imgCode.setText("");
                    getVcodePresenter.sendRequest(null, null);
                }
            } else {
                //保存用户信息
                saveUserInfo(getVcodeBean);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        popActivity();
                        MyActivityManager.getInstance().finishSpecifiedActivity(LoginActivity.class);
                    }
                }, 300);
            }
        } else if (listener instanceof GetVcodePresenterImpl) {
            btn_login_getImgCode.setClickable(true);
            vCode = getVcodeBean.getRand();
            btn_login_getImgCode.setText(vCode);
        }
    }

    private void saveUserInfo(GetVcodeBean getVcodeBean) {
        UserBean userBean = getVcodeBean.getUser();
        boolean isFirstInvestment = getVcodeBean.isFirstInvestment();
        long ctime = userBean.getCtime();
        int id = userBean.getId();
        boolean isBindCard = userBean.isBindCard();
        boolean isBindEmail = userBean.isBindEmail();
        boolean isPaypwd = userBean.isPaypwd();
        boolean isTruename = userBean.isTruename();
        long lastLoginTime = userBean.getLastLoginTime();
        int memberType = userBean.getMemberType();
        String name = userBean.getName();
        boolean paypwd = userBean.isPaypwd();
        String truename = userBean.getTruename();
        if (truename != null) {
            SPUtils.put(MainApplication.getContext(), "LOGIN", "truename", truename);
        }
        SPUtils.put(MainApplication.getContext(), "LOGIN", "isFirstInvestment", getVcodeBean.isFirstInvestment());
        //已登录状态
        SPUtils.put(this, "LOGIN", "isLogined", true);
        SPUtils.put(this, "LOGIN", "isFirstInvestment", isFirstInvestment);
        SPUtils.put(this, "LOGIN", "ctime", ctime);
        SPUtils.put(this, "LOGIN", "id", id);
        SPUtils.put(this, "LOGIN", "isBindCard", isBindCard);
        SPUtils.put(this, "LOGIN", "isBindEmail", isBindEmail);
        SPUtils.put(this, "LOGIN", "isPaypwd", isPaypwd);
        SPUtils.put(this, "LOGIN", "isTruename", isTruename);
        SPUtils.put(this, "LOGIN", "memberType", memberType);
        SPUtils.put(this, "LOGIN", "paypwd", paypwd);
        SPUtils.put(this, "LOGIN", "name", name);
        String headImgUrl = userBean.getHeadImgUrl();
        String nickname = userBean.getNickname();
        SPUtils.put(this, "LOGIN", "nickname", nickname);
        if (intentSource != null) {
            if ("GestureLockCheckActivity".equals(intentSource)) {
                MyActivityManager.getInstance().startNextActivity(MainActivity.class);
                /*ChangeTabBean changeTabBean = new ChangeTabBean();
                changeTabBean.setTag(0);
                EventBus.getDefault().postSticky(changeTabBean);*/
                notifyMainActivityChangeTab(0);
//            popActivity();
                MyActivityManager.getInstance().finishSpecifiedActivity(LoginActivity.class);
            } else {
                notifyMainActivityChangeTab(2);
                MyActivityManager.getInstance().finishSpecifiedActivity(LoginActivity.class);
            }
        }
        if (!TextUtils.isEmpty(headImgUrl)) {
            SPUtils.put(LoginActivity.this, "LOGIN", "headImgUrl", headImgUrl);
        }
        String mobilePhone = userBean.getMobile();
        if (!TextUtils.isEmpty(mobilePhone)) {
            SPUtils.put(this, "LOGIN", "mobilePhone", mobilePhone);
        }

        //清除手势密码和摇一摇的
        SPUtils.remove(this, "LOGIN", "Gesture_Switch");
        SPUtils.remove(this, "LOGIN", "Shake_For_Profit_Switch");

        //判断个人中心数据是否显示
        int lastLoginMid = (int) SPUtils.get(LoginActivity.this, "EYE_STATUS", "mid", -1);
        if (!(id == lastLoginMid)) {
            SPUtils.put(LoginActivity.this, "EYE_STATUS", "eye_status", false);
        }
    }

    public void notifyMainActivityChangeTab(int page) {
        ChangeTabBean changeTabBean = new ChangeTabBean();
        changeTabBean.setTag(page);
        EventBus.getDefault().postSticky(changeTabBean);
    }

}
