package com.mgjr.view.common;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.ApplicationInfoUtils;
import com.mgjr.Utils.CommonTextUtils;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.SPUtils;
import com.mgjr.Utils.StringToBase64;
import com.mgjr.model.bean.RegisterBean;
import com.mgjr.model.bean.UserBean;
import com.mgjr.presenter.impl.RegisterStepTwoPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.CustomCommonDialog;
import com.mgjr.share.SubmitButton;
import com.mgjr.view.listeners.ViewListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/7.
 */
public class RegisterAcitivityStepTwo extends ActionbarActivity implements View.OnClickListener, ViewListener<RegisterBean> {
    //用户密码
    private EditText et_register_inputpwd;
    //推荐人代码输入框
    private EditText et_register_recommend;
    //注册按钮
    private SubmitButton btn_register;
    //《芒果金融用户协议》
    private TextView tv_register_agreement;
    private ImageView imgbtn_eye_register;
    //隐藏密码
    private LinearLayout layout_pwdeye_register;
    private boolean img_pwdeye_isPressed = false;
    private LinearLayout layout_et_recommed;
    //用户输入密码
    private String password;
    //推荐人代码
    private String pCode;
    //用户手机号
    private String mobile;
    private boolean isPressed = false;
    private RegisterStepTwoPresenterImpl registerStepTwoPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.layout_register_second, this);
        actionbar.setRightTextView(getResources().getString(R.string.recommend), this);
        actionbar.rightTextView.setTextSize(14);
        actionbar.setLeftImageView(R.drawable.actionbar_gray_backbtn, this);
        initView();
    }

    private void initView() {
        actionbar.leftImageView.setOnClickListener(this);
        registerStepTwoPresenter = new RegisterStepTwoPresenterImpl(this);
        layout_et_recommed = (LinearLayout) findViewById(R.id.layout_et_recommed);
        et_register_inputpwd = (EditText) findViewById(R.id.et_register_inputpwd);
        et_register_recommend = (EditText) findViewById(R.id.et_register_recommend);
        imgbtn_eye_register = (ImageView) findViewById(R.id.imgbtn_eye_register);
        layout_pwdeye_register = (LinearLayout) findViewById(R.id.layout_pwdeye_register);
        btn_register = (SubmitButton) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        tv_register_agreement = (TextView) findViewById(R.id.tv_register_agreement);
        tv_register_agreement.setOnClickListener(this);

        layout_pwdeye_register.setOnClickListener(this);

        mobile = getIntent().getStringExtra("code");
        password = et_register_inputpwd.getText().toString();
        pCode = et_register_recommend.getText().toString();

    }

    @Override
    public void onClick(View v) {
        if (v == actionbar.leftImageView) {
            final CustomCommonDialog dialog = new CustomCommonDialog(this, "", getResources().getString(R.string.issure_toquit),
                    getResources().getString(R.string.sure), getResources().getString(R.string.cancle), true);
            dialog.show();
            dialog.setClicklistener(new CustomCommonDialog.ClickListenerInterface() {
                @Override
                public void doConfirm() {
                    MyActivityManager.getInstance().startNextActivity(RegisterActivityStepOne.class, mobile);
//                    popActivity();
                    MyActivityManager.getInstance().finishSpecifiedActivity(RegisterAcitivityStepTwo.class);
                    dialog.dismiss();
                }

                @Override
                public void doCancel() {
                    dialog.dismiss();
                }

            });
        } else if (v == layout_pwdeye_register) {

            if (img_pwdeye_isPressed) {
                //隐藏密码
                et_register_inputpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imgbtn_eye_register.setBackgroundResource(R.drawable.login_eye_btn);
            } else {
                et_register_inputpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imgbtn_eye_register.setBackgroundResource(R.drawable.login_eye_btn_normal);
            }
            img_pwdeye_isPressed = !img_pwdeye_isPressed;
            et_register_inputpwd.postInvalidate();
            //切换后将EditText光标置于末尾
            CharSequence charSequence = et_register_inputpwd.getText();
            if (charSequence instanceof Spannable) {
                Spannable spanText = (Spannable) charSequence;
                Selection.setSelection(spanText, charSequence.length());
            }
        } else if (v == tv_register_agreement) {
            MyActivityManager.getInstance().startNextActivity(CommonWebViewActivity.class, "gvrp");
        } else if (v == btn_register) {
            password = et_register_inputpwd.getText().toString();
            pCode = et_register_recommend.getText().toString();

            if (password.equalsIgnoreCase("") || password.length() > 16 || password.length() < 6) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.password_limit));
            } else if (CommonTextUtils.isConSpeCharacters(password)) {
                CommonToastUtils.showToast(this, getResources().getString(R.string.password_format));
            } else {
                Map<String, String> necessaryParams = new HashMap<>();
                necessaryParams.put("mobile", mobile);
                necessaryParams.put("password", StringToBase64.stringToBase64(password));
                String mark = ApplicationInfoUtils.getAppMetaData(this, "UMENG_CHANNEL");
                if (TextUtils.isEmpty(mark)) {
                    mark = "21";
                }
                necessaryParams.put("mark", mark);
                Map<String, String> unNecessaryParams = new HashMap<>();
                unNecessaryParams.put("pCode", pCode);
                registerStepTwoPresenter.sendRequest(necessaryParams, unNecessaryParams);
                btn_register.submit();
                btn_register.setClickable(false);

            }
        } else if (v == actionbar.rightTextView) {
            if (!isPressed) {
                layout_et_recommed.setVisibility(View.VISIBLE);
                actionbar.rightTextView.setTextColor(Color.parseColor("#feaa00"));
            } else {
                layout_et_recommed.setVisibility(View.INVISIBLE);
                actionbar.rightTextView.setTextColor(Color.GRAY);
            }
            isPressed = !isPressed;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        btn_register.finish("确定");
        btn_register.setClickable(true);
    }

    @Override
    public void showError(OnPresenterListener listener, RegisterBean registerBean) {
        CommonToastUtils.showToast(this, registerBean.getMsg());
        btn_register.finish("确定");
        btn_register.setClickable(true);
    }

    @Override
    public void responseData(OnPresenterListener listener, RegisterBean registerBean) {

        if (listener instanceof RegisterStepTwoPresenterImpl) {
            btn_register.finish("确定");
            btn_register.setClickable(true);
            UserBean userBean = registerBean.getUser();
            boolean isFirstInvestment = registerBean.isFirstInvestment();
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
            String nickname = userBean.getNickname();
            String headImgUrl = userBean.getHeadImgUrl();

            boolean isReward = false;
            SPUtils.put(this, "LOGIN", "isLogined", true);
            SPUtils.put(this, "LOGIN", "isFirstInvestment", isFirstInvestment);
            SPUtils.put(this, "LOGIN", "ctime", ctime);
            SPUtils.put(this, "LOGIN", "id", id);
            SPUtils.put(this, "LOGIN", "isBindCard", isBindCard);
            SPUtils.put(this, "LOGIN", "isBindEmail", isBindEmail);
            SPUtils.put(this, "LOGIN", "isPaypwd", isPaypwd);
            SPUtils.put(this, "LOGIN", "isTruename", isTruename);
            SPUtils.put(this, "LOGIN", "memberType", memberType);
            SPUtils.put(this, "LOGIN", "name", name);
            SPUtils.put(this, "LOGIN", "paypwd", paypwd);
            SPUtils.put(this, "LOGIN", "isReward", isReward);
            SPUtils.put(this, "LOGIN", "headImgUrl", headImgUrl);
            SPUtils.put(this, "LOGIN", "nickname", nickname);

            //注册成功,把个人中心眼睛状态打开
            SPUtils.put(RegisterAcitivityStepTwo.this, "EYE_STATUS", "eye_status", false);
//            MyActivityManager.getInstance().startNextActivity(MainActivity.class, "2");
//            ChangeTabBean changeTabBean = new ChangeTabBean();
////            changeTabBean.setTag(2);
//            EventBus.getDefault().postSticky(changeTabBean);
            SPUtils.put(RegisterAcitivityStepTwo.this, "TempIntent", "fromRegister", true);
            MyActivityManager.getInstance().finishSpecifiedActivity(LoginActivity.class);
//            popActivity();
            MyActivityManager.getInstance().finishSpecifiedActivity(RegisterAcitivityStepTwo.class);
        }
    }
}
