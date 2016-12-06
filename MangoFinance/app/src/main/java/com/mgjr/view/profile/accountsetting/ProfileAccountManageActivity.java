package com.mgjr.view.profile.accountsetting;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.R;
import com.mgjr.Utils.PopwUtils;
import com.mgjr.Utils.SPUtils;
import com.mgjr.model.bean.AccountManageBean;
import com.mgjr.presenter.impl.AccountManagePresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.view.listeners.ViewListener;

import java.util.HashMap;
import java.util.Map;

public class ProfileAccountManageActivity extends ActionbarActivity implements View.OnClickListener, ViewListener<AccountManageBean> {
    private TextView tv_mobile;
    private TextView tv_bind_mail;
    private LinearLayout rl_bind_email;
    private LinearLayout rl_bindPhone;
    private ImageView iv_bind_mail_box_right_arrow;
    private boolean isBindEmal;
    private AccountManagePresenterImpl accountManagePresenterImpl;
    private AccountManageBean accountManageBean;
    private PopupWindow loadingPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_profile_account_manage, this);
        actionbar.setCenterTextView("账号管理");
        accountManagePresenterImpl = new AccountManagePresenterImpl(this);
        iv_bind_mail_box_right_arrow = (ImageView) findViewById(R.id.iv_bind_mail_box_right_arrow);
    }


    @Override
    protected void onResume() {
        super.onResume();
        networkRequest();
    }

    private void networkRequest() {
        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        necessaryParams.put("mid", "" + SPUtils.get(this, "LOGIN", "id", 0));
        accountManagePresenterImpl.sendRequest(necessaryParams, null);
    }


    @Override
    public void onClick(View v) {
        if (v == rl_bindPhone) {
            MyActivityManager.getInstance().startNextActivity(ProfileChangeMobilePhoneActivity.class);
        } else if (v == rl_bind_email) {
            if (isBindEmal) {
                CommonToastUtils.showToast(this, "您已绑定邮箱,请登录电脑端修改邮箱");
            } else {
                MyActivityManager.getInstance().startNextActivity(ProfileBindMailBoxActivity.class);
            }
        }
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @Override
    public void showError() {
        dismissLoadingDialog();
        showSystemError();
    }

    @Override
    public void showError(OnPresenterListener listener, AccountManageBean accountManageBean) {
        dismissLoadingDialog();
        CommonToastUtils.showToast(this, accountManageBean.getMsg());
    }

    @Override
    public void responseData(OnPresenterListener listener, AccountManageBean accountManageBean) {
        if (listener instanceof AccountManagePresenterImpl) {
            this.accountManageBean = accountManageBean;
            initData();
        }
    }

    private void initData() {
        tv_mobile = (TextView) findViewById(R.id.tv_mobile);
//        String str = (String) SPUtils.get(this, "LOGIN", "mobilePhone", "");
        String str = accountManageBean.getMember().getMobile();
        if (!"".equals(str)) {
            //先把手机号码保存到sp
            SPUtils.put(this, "LOGIN", "mobilePhone", str);
            String mobile = str.substring(0, str.length() - (str.substring(3)).length()) + "****" + str.substring(7);
            tv_mobile.setText(mobile);
        } else {
            tv_mobile.setText("未绑定");
        }

        rl_bind_email = (LinearLayout) findViewById(R.id.rl_bind_email);
        rl_bind_email.setOnClickListener(this);
        rl_bindPhone = (LinearLayout) findViewById(R.id.rl_bindPhone);
        rl_bindPhone.setOnClickListener(this);
        tv_bind_mail = (TextView) findViewById(R.id.tv_bind_mail);
//        String mail = (String) SPUtils.get(this, "LOGIN", "email", "");
        String mail = accountManageBean.getMember().getEmail();
        Integer eok = accountManageBean.getMember().getEok();
        StringBuilder sb = new StringBuilder();
        if (eok == 1 && !TextUtils.isEmpty(mail)) {
            int index = mail.indexOf("@");
            for (int i = 0; i < mail.length(); i++) {
                if (i > 0 && i < index - 1) {
                    sb.append("*");
                } else {
                    sb.append(mail.charAt(i));
                }
            }
            tv_bind_mail.setText(sb.toString());
            iv_bind_mail_box_right_arrow.setVisibility(View.GONE);
            isBindEmal = true;
        } else {
            tv_bind_mail.setText("未绑定");
            isBindEmal = false;
        }

    }
}
