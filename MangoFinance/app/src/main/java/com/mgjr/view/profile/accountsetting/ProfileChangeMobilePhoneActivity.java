package com.mgjr.view.profile.accountsetting;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.KeyBoardUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.R;
import com.mgjr.Utils.PhoneUtils;
import com.mgjr.Utils.PopwUtils;
import com.mgjr.Utils.SPUtils;
import com.mgjr.Utils.StringToBase64;
import com.mgjr.httpclient.HttpClient;
import com.mgjr.httpclient.callback.Callback;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.model.bean.BaseBean;
import com.mgjr.share.APIBuilder;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.InputPayPwdPopw;
import com.mgjr.share.NetUtils;
import com.mgjr.view.invester.InvestProductDetailActivity;
import com.mgjr.view.profile.rechargeWithdraw.WithdrawActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class ProfileChangeMobilePhoneActivity extends ActionbarActivity {
    private static final int REQUEST_CODE_ASK_CALL_PHONE = 0;
    private InputPayPwdPopw inputPayPwdPopw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_profile_change_phone_num, this);
        actionbar.setCenterTextView("更换手机号");
        //初始化支付密码弹窗
        initPayPwdPopw();
    }

    private void initPayPwdPopw() {
        inputPayPwdPopw = new InputPayPwdPopw();
        inputPayPwdPopw.setClickBtnListener(new InputPayPwdPopw.ClickBtnListener() {
            @Override
            public void clickConfirmBtn() {
                //提交数据,请求数据
                String paypassword = inputPayPwdPopw.getEtInputTradePwd().getText().toString();

                if (paypassword.equalsIgnoreCase("")) {
                    CommonToastUtils.showToast(ProfileChangeMobilePhoneActivity.this, "密码不能为空");
                    return;
                } else if (paypassword.length() < 6 || paypassword.length() > 16) {
                    CommonToastUtils.showToast(ProfileChangeMobilePhoneActivity.this, "密码的长度必须是6到16位之间");
                    return;
                } else if (paypassword.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0) {
                    // 包含特殊字符
                    CommonToastUtils.showToast(ProfileChangeMobilePhoneActivity.this, "密码不能有特殊字符");
                    inputPayPwdPopw.getEtInputTradePwd().requestFocus();
                    return;
                }
                //提交数据,请求数据
                paypwdCheckRequest(paypassword);
                inputPayPwdPopw.startCheckingAnim();
                KeyBoardUtils.closeKeybord(inputPayPwdPopw.getEtInputTradePwd(), ProfileChangeMobilePhoneActivity.this);
            }

            @Override
            public void clickForgetPwdBtn() {
                MyActivityManager.getInstance().startNextActivity(ProfileFindTradePwdActivity.class);
            }
        });
    }

    public void useOldPhone(View view) {
        boolean isSetPaypwd = (boolean) SPUtils.get(this, "LOGIN", "isPaypwd", false);
        if (!isSetPaypwd) {
            MyActivityManager.getInstance().startNextActivity(ProfileChangeMobilePhoneWithOldNumActivity.class);
        } else {
            //弹出输入交易密码
            View rootView = LayoutInflater.from(this).inflate(R.layout.activity_profile_change_phone_num, null);
            inputPayPwdPopw.showInputTradePwdPopw(this, rootView);
        }
    }

    /**
     * 输入交易密码的弹窗
     */
    public PopupWindow showInputTradePwdPopw(final Activity activity, View rootView) {
        View contentView = LayoutInflater.from(activity).inflate(R.layout.input_trade_pwd, null);
        final EditText etInputTradePwd = (EditText) contentView.findViewById(R.id.et_input_trade_pwd);
        final TextView dialogCanclebtn = (TextView) contentView.findViewById(R.id.dialog_canclebtn);
        final TextView dialogConfirmbtn = (TextView) contentView.findViewById(R.id.dialog_confirmbtn);
        final TextView tv_forgetpaypwd = (TextView) contentView.findViewById(R.id.tv_forgetpaypwd);
        final PopupWindow inputTradePwdPopw = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //设置动画所对应的style
        inputTradePwdPopw.setAnimationStyle(R.style.calcAnim);
        //点击空白处时，隐藏掉pop窗口
        inputTradePwdPopw.setBackgroundDrawable(new ColorDrawable());
        inputTradePwdPopw.setFocusable(true);
        inputTradePwdPopw.setOutsideTouchable(true);

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
            }
        });
        dialogCanclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputTradePwdPopw != null) {
                    KeyBoardUtils.closeKeybord(etInputTradePwd, activity);
                    inputTradePwdPopw.dismiss();
                }
            }
        });
        tv_forgetpaypwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivityManager.getInstance().startNextActivity(ProfileFindTradePwdActivity.class);
            }
        });
        dialogConfirmbtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    String paypassword = etInputTradePwd.getText().toString();

                                                    if (paypassword.equalsIgnoreCase("")) {
                                                        CommonToastUtils.showToast(ProfileChangeMobilePhoneActivity.this, "密码不能为空");
                                                        return;
                                                    } else if (paypassword.length() < 6 || paypassword.length() > 16) {
                                                        CommonToastUtils.showToast(ProfileChangeMobilePhoneActivity.this, "密码的长度必须是6到16位之间");
                                                        return;
                                                    } else if (paypassword.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0) {
                                                        // 包含特殊字符
                                                        CommonToastUtils.showToast(ProfileChangeMobilePhoneActivity.this, "密码不能有特殊字符");
                                                        etInputTradePwd.requestFocus();
                                                        return;
                                                    }
                                                    //提交数据,请求数据
                                                    paypwdCheckRequest(paypassword);
                                                    KeyBoardUtils.closeKeybord(etInputTradePwd, activity);
                                                    inputTradePwdPopw.dismiss();
                                                }
                                            }

        );
        return inputTradePwdPopw;
    }

    private void paypwdCheckRequest(String paypassword) {
        String url = APIBuilder.baseUrl + APIBuilder.version + "appSecurity/validatePayPwd";
        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        necessaryParams.put("mid", "" + SPUtils.get(this, "LOGIN", "id", 0));
        necessaryParams.put("paypwd", StringToBase64.stringToBase64(paypassword));
        List<String> keyList = new ArrayList<String>();
        keyList.add("appSecurity/validatePayPwd");
        keyList.add(necessaryParams.get("mid"));
        keyList.add(necessaryParams.get("paypwd"));
        String keyStr = APIBuilder.getKeyStr(keyList);

        necessaryParams.put("keyStr", keyStr);

        Map<String, String> params = new HashMap<String, String>();
        params.putAll(necessaryParams);
        HttpClient
                .get()
                .url(url)
                .params(params)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseResponse(Response response) throws Exception {
                        String string = response.body().string();
                        Gson gson = new GsonBuilder().create();
                        BaseBean baseBean = gson.fromJson(string, BaseBean.class);
                        return baseBean;
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        PhoneUtils.saveSystemErrorInfo(e);
                        boolean isSocketTimeoutException = (boolean) SPUtils.get(MainApplication.getContext(), "SocketTimeoutException", "isSocketTimeoutException", false);
                        if (!NetUtils.isConnected(MainApplication.getContext())) {
                            inputPayPwdPopw.paypwdCheckingError(getString(R.string.network_error));
                            CommonToastUtils.showToast(MainApplication.getContext(), getString(R.string.network_error));
                        } else if (isSocketTimeoutException) {
                            inputPayPwdPopw.paypwdCheckingError(getString(R.string.request_timeout));
                            CommonToastUtils.showToast(MainApplication.getContext(), getString(R.string.request_timeout));
                            SPUtils.clear(MainApplication.getContext(), "SocketTimeoutException");
                        } else {
                            inputPayPwdPopw.paypwdCheckingError(getString(R.string.system_error));
                            CommonToastUtils.showToast(MainApplication.getContext(), getString(R.string.system_error));
                        }
                    }

                    @Override
                    public void onResponse(Object response) {
                        BaseBean baseBean = (BaseBean) response;
                        if (baseBean.getStatus().equalsIgnoreCase("0000")) {
                            //支付密码校验成功
//                            CommonToastUtils.showToast(ProfileChangeMobilePhoneActivity.this, "支付密码校验成功!");
                            inputPayPwdPopw.paypwdCheckingSuccess();
                            inputPayPwdPopw.getInputTradePwdPopw().dismiss();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    inputPayPwdPopw.getInputTradePwdPopw().dismiss();
                                    MyActivityManager.getInstance().startNextActivity(ProfileChangeMobilePhoneWithOldNumActivity.class);
                                }
                            }, 700);
                        } else {
                            inputPayPwdPopw.paypwdCheckingError(baseBean.getMsg());
                        }
                    }
                });
    }

    public void useNewPhone(View view) {
        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_profile_change_phone_num, null);
        showNoOldPhoneNumPopw(this, rootView);
    }

    /**
     * 旧手机不能接受短信时的弹窗
     *
     * @param activity
     * @param rootView
     * @return
     */
    public void showNoOldPhoneNumPopw(final Activity activity, View rootView) {
        View contentView = LayoutInflater.from(activity).inflate(R.layout.layout_no_old_phonenum, null);
        TextView dialog_confirmbtn = (TextView) contentView.findViewById(R.id.dialog_confirmbtn);
        LinearLayout ll_call_service = (LinearLayout) contentView.findViewById(R.id.ll_call_service);

        final PopupWindow noOldPhoneNumPopw = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置动画所对应的style
        noOldPhoneNumPopw.setAnimationStyle(R.style.calcAnim);
        //点击空白处时，隐藏掉pop窗口
        noOldPhoneNumPopw.setBackgroundDrawable(new ColorDrawable());
        noOldPhoneNumPopw.setFocusable(true);
        noOldPhoneNumPopw.setOutsideTouchable(true);
        PopwUtils.backgroundAlpha(0.5f, (Activity) activity);
        noOldPhoneNumPopw.showAtLocation(rootView, Gravity.CENTER_HORIZONTAL, 0, 0);
        noOldPhoneNumPopw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopwUtils.backgroundAlpha(1f, activity);
                noOldPhoneNumPopw.dismiss();
            }
        });

        ll_call_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(ProfileChangeMobilePhoneActivity.this, Manifest.permission.CALL_PHONE);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ProfileChangeMobilePhoneActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_CALL_PHONE);
                        return;
                    } else {
                        //上面已经写好的拨号方法
                        callDirectly();
                    }
                } else {
                    //上面已经写好的拨号方法
                    callDirectly();
                }
                noOldPhoneNumPopw.dismiss();
            }
        });

        dialog_confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOldPhoneNumPopw != null) {
                    noOldPhoneNumPopw.dismiss();
                }
            }
        });

        return;
    }

    /**
     * 拨打电话
     */
    public void callDirectly() {
        String forPhoneNum = "4008976555";
        Intent intent = new Intent();
        intent.setAction("android.intent.action.CALL");
        intent.setData(Uri.parse("tel:" + forPhoneNum));
        startActivity(intent);
    }
}
