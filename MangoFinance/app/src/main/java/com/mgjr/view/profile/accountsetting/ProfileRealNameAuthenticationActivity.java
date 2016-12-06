package com.mgjr.view.profile.accountsetting;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgjr.R;
import com.mgjr.Utils.ClearEditText;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.KeyBoardUtils;
import com.mgjr.Utils.LogUtil;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.PhoneUtils;
import com.mgjr.Utils.PopwUtils;
import com.mgjr.Utils.SPUtils;
import com.mgjr.httpclient.HttpClient;
import com.mgjr.httpclient.callback.Callback;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.model.bean.BaseBean;
import com.mgjr.presenter.impl.TruenameAuthPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.APIBuilder;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.CustomCommonDialog;
import com.mgjr.share.SubmitButton;
import com.mgjr.view.listeners.ViewListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Response;

import static com.mgjr.Utils.HideInputUtils.isShouldHideInput;

public class ProfileRealNameAuthenticationActivity extends ActionbarActivity implements View.OnClickListener, ViewListener<BaseBean> {
    /**
     * 认证按钮
     */
    private SubmitButton btn_auth;
    /**
     * 输入真是姓名、身份证号
     */
    private ClearEditText et_truename, et_userID;
    private String truename, idcode, mid;
    private final String idtype = "1";

    private TruenameAuthPresenterImpl truenameAuthPresenter;
    private SharedPreferences sp;
    private String paypassword;
    private String confirmPaypassword;
    private PopupWindow inputTradePwdPopw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_profile_real_name_authentication, this);
        initActionBar();
        initView();
    }

    private void initActionBar() {
        actionbar.setCenterTextView("实名认证");
        actionbar.leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CustomCommonDialog dialog = new CustomCommonDialog(ProfileRealNameAuthenticationActivity.this, "温馨提示", "您确定放弃实名认证吗？", "继续", "放弃", false);
                dialog.setClicklistener(new CustomCommonDialog.ClickListenerInterface() {
                    @Override
                    public void doConfirm() {
//                        popActivity();
                        dialog.dismiss();
                    }

                    @Override
                    public void doCancel() {
                        MyActivityManager.getInstance().popCurrentActivity();
                    }

                });
                dialog.show();
            }
        });
    }


    private void initView() {

        truenameAuthPresenter = new TruenameAuthPresenterImpl(this);
        sp = this.getSharedPreferences("LOGIN", MODE_PRIVATE);

        et_truename = (ClearEditText) findViewById(R.id.et_truename);
        et_userID = (ClearEditText) findViewById(R.id.et_userID);

        btn_auth = (SubmitButton) findViewById(R.id.btn_auth);
        btn_auth.setOnClickListener(this);

        isRealNameAuth();

    }

    private void isRealNameAuth() {
        boolean isPaypwd = (boolean) SPUtils.get(this, "LOGIN", "isPaypwd", false);
        if (!isPaypwd) {
            //弹出输入交易密码
            final View rootView = LayoutInflater.from(this).inflate(R.layout.activity_profile_real_name_authentication, null);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showSetTradePwdPopw(ProfileRealNameAuthenticationActivity.this, rootView);
                }
            }, 800);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        autoShowKeyboard();
    }

    /*
       * 自动打开软键盘
       * */
    private void autoShowKeyboard() {

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            public void run() {

                InputMethodManager inputManager = (InputMethodManager) et_truename.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_truename, 0);
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

    @Override
    public void onClick(View v) {

        if (v == btn_auth) {
            truename = et_truename.getText().toString().trim();
            idcode = et_userID.getText().toString();
            if (TextUtils.isEmpty(truename)) {
                CommonToastUtils.showToast(this, "姓名不能为空");
                return;
            }
            if (TextUtils.isEmpty(idcode)) {
                CommonToastUtils.showToast(this, "身份证号码不能为空");
                return;
            }
            if (idcode.length() < 18) {
                CommonToastUtils.showToast(this, "请输入18位的身份证号码");
                return;
            }
            String lastNum = idcode.substring(idcode.length() - 1);
            if (lastNum.matches("^[A-Za-z]+$") && !lastNum.matches("[A-Z]")) {
                CommonToastUtils.showToast(this, "最后一位字母请输入大写X");
                return;
            }
            btn_auth.submit();
            requestRealNameAuthData();
        }

    }

    private void requestRealNameAuthData() {
        int id = sp.getInt("id", 0);
        mid = String.valueOf(id);

        Map<String, String> necessaryParams = new HashMap<>();
        necessaryParams.put("mid", mid);
        necessaryParams.put("truename", truename);
        necessaryParams.put("idcode", idcode);
        necessaryParams.put("idtype", idtype);

        truenameAuthPresenter.sendRequest(necessaryParams, null);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


    @Override
    public void showError(OnPresenterListener listener, BaseBean baseBean) {
        CommonToastUtils.showToast(this, baseBean.getMsg());
        btn_auth.finish("认证失败!");
    }

    @Override
    public void responseData(OnPresenterListener listener, BaseBean baseBean) {
        CommonToastUtils.showToast(this, "认证成功");
        btn_auth.finish("认证成功!");
        SPUtils.put(ProfileRealNameAuthenticationActivity.this, "LOGIN", "isTruename", true);
        SPUtils.put(MainApplication.getContext(), "LOGIN", "truename", truename);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                popActivity();
                MyActivityManager.getInstance().popCurrentActivity();
            }
        }, 1000);
    }

    /**
     * 设置交易密码的弹窗
     */
    public void showSetTradePwdPopw(final Activity activity, View rootView) {
        View contentView = LayoutInflater.from(activity).inflate(R.layout.set_trade_pwd, null);
        final EditText etInputTradePwd = (EditText) contentView.findViewById(R.id.et_input_trade_pwd);
        final EditText etInputConfirmTradePwd = (EditText) contentView.findViewById(R.id.et_input_confirm_trade_pwd);
        final TextView dialogCanclebtn = (TextView) contentView.findViewById(R.id.dialog_canclebtn);
        final TextView dialogConfirmbtn = (TextView) contentView.findViewById(R.id.dialog_confirmbtn);
        final TextView tv_forgetpaypwd = (TextView) contentView.findViewById(R.id.tv_forgetpaypwd);
        tv_forgetpaypwd.setVisibility(View.GONE);
        inputTradePwdPopw = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //设置动画所对应的style
        inputTradePwdPopw.setAnimationStyle(R.style.calcAnim);
        //点击空白处时，隐藏掉pop窗口
        inputTradePwdPopw.setBackgroundDrawable(new ColorDrawable());
        inputTradePwdPopw.setFocusable(true);
        inputTradePwdPopw.setOutsideTouchable(false);
        PopwUtils.backgroundAlpha(0.5f, (Activity) activity);
        //自动弹出键盘以及上移弹窗,一定要注意顺序
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                KeyBoardUtils.openKeybord(etInputTradePwd, activity);
            }
        }, 800);
        inputTradePwdPopw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        inputTradePwdPopw.showAtLocation(rootView, Gravity.CENTER, 0, 0);
        inputTradePwdPopw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopwUtils.backgroundAlpha(1f, activity);
//                boolean isPaypwd = (boolean) SPUtils.get(getApplicationContext(), "LOGIN", "isPaypwd", false);
//                if (!isPaypwd) {
////                    popActivity();
//                    MyActivityManager.getInstance().popCurrentActivity();
//                }
            }
        });
        dialogCanclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputTradePwdPopw != null) {
                    KeyBoardUtils.closeKeybord(etInputTradePwd, activity);
                    inputTradePwdPopw.dismiss();
                }
//                popActivity();
                MyActivityManager.getInstance().popCurrentActivity();
            }
        });
        dialogConfirmbtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    paypassword = etInputTradePwd.getText().toString();
                                                    confirmPaypassword = etInputConfirmTradePwd.getText().toString();

                                                    if (paypassword.equalsIgnoreCase("")) {
                                                        CommonToastUtils.showToast(ProfileRealNameAuthenticationActivity.this, "密码不能为空");
                                                        return;
                                                    } else if (paypassword.length() < 6 || paypassword.length() > 16) {
                                                        CommonToastUtils.showToast(ProfileRealNameAuthenticationActivity.this, "密码的长度必须是6到16位之间");
                                                        return;
                                                    } else if (paypassword.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0) {
                                                        // 包含特殊字符
                                                        CommonToastUtils.showToast(ProfileRealNameAuthenticationActivity.this, "密码不能有特殊字！");
                                                        etInputTradePwd.requestFocus();
                                                        return;
                                                    } else if (confirmPaypassword.equalsIgnoreCase("")) {
                                                        CommonToastUtils.showToast(ProfileRealNameAuthenticationActivity.this, "确认密码不能为空");
                                                        return;
                                                    } else if (confirmPaypassword.length() < 6 || paypassword.length() > 16) {
                                                        CommonToastUtils.showToast(ProfileRealNameAuthenticationActivity.this, "确认密码的长度必须是6到16位之间");
                                                        return;
                                                    } else if (confirmPaypassword.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0) {
                                                        // 包含特殊字符
                                                        CommonToastUtils.showToast(ProfileRealNameAuthenticationActivity.this, "确认密码不能有特殊字符");
                                                        etInputTradePwd.requestFocus();
                                                        return;
                                                    } else if (!paypassword.equals(confirmPaypassword)) {
                                                        CommonToastUtils.showToast(ProfileRealNameAuthenticationActivity.this, "两次密码不一致");
                                                        return;
                                                    }
                                                    //提交数据,请求数据
                                                    requestSetPayPwdNetwork();
                                                    KeyBoardUtils.closeKeybord(etInputTradePwd, activity);
//                                                    inputTradePwdPopw.dismiss();
                                                }
                                            }

        );
        return;
    }


    /**
     * 监听用户的返回键
     */
   /* public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (inputTradePwdPopw != null) {
                popActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
*/
    private void requestSetPayPwdNetwork() {
        String url = APIBuilder.baseUrl + APIBuilder.version + "appSecurity/setPayPwd";
        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        necessaryParams.put("mid", "" + SPUtils.get(this, "LOGIN", "id", 0));
        necessaryParams.put("paypwd", paypassword);
        necessaryParams.put("paypwd2", confirmPaypassword);
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
//            CommonToastUtils.showToast(ProfileRealNameAuthenticationActivity.this, e.toString());
            PhoneUtils.saveSystemErrorInfo(e);
            showSystemError();
        }

        @Override
        public void onResponse(BaseBean response) {
            if (response.getStatus().equalsIgnoreCase("0000")) {
                CommonToastUtils.showToast(ProfileRealNameAuthenticationActivity.this, "交易密码设置成功");
                SPUtils.put(getApplicationContext(), "LOGIN", "isPaypwd", true);
            } else {
                CommonToastUtils.showToast(ProfileRealNameAuthenticationActivity.this, response.getMsg());
            }
            inputTradePwdPopw.dismiss();
        }
    }

    public void showError() {
        showSystemError();
        btn_auth.finish("认证失败!");
    }
}
