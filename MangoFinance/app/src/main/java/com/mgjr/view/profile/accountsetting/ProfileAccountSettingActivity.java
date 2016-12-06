package com.mgjr.view.profile.accountsetting;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.SPUtils;
import com.mgjr.model.bean.AccountSettingBean;
import com.mgjr.presenter.impl.AccountSettingPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.CustomCommonDialog;
import com.mgjr.view.listeners.ViewListener;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

public class ProfileAccountSettingActivity extends ActionbarActivity implements ViewListener<AccountSettingBean>, View.OnClickListener {

    private ImageView iv_user_touxiang, profile_account__setting_selected_btn;
    private boolean phoneNotification;
    private RelativeLayout rl_profile_phone_notification;
    private TextView tv_user_nickname;
    private TextView tv_logout;
    private AccountSettingPresenterImpl accountSettingPresenterImpl;
    private AccountSettingBean accountSettingBean;
    private ImageButton ib_edit_userinfo;
    private RelativeLayout rl_profile_pwd_manager;
    private RelativeLayout rl_uncheck_realname;
    private TextView tv_checked_realname;
    private RelativeLayout rl_realName_Authentication;
    private RelativeLayout rl_unbind_bankcard;
    private LinearLayout ll_bind_bankcard;
    private RelativeLayout rl_bindCard;
    private ImageView iv_bind_banklogo;
    private TextView tv_bind_bankname;
    private TextView tv_no_real_name;
    private String headImgUrl;
    private RelativeLayout rl_head_image_area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_profile_account_setting, this);
        actionbar.setCenterTextView("账户设置");
        accountSettingPresenterImpl = new AccountSettingPresenterImpl(this);

        //初始化id和点击事件
        initViews();
    }

    private void initViews() {
        //初始化头像部分
        rl_head_image_area = (RelativeLayout) findViewById(R.id.rl_head_image_area);
        rl_head_image_area.setOnClickListener(this);
        iv_user_touxiang = (ImageView) findViewById(R.id.iv_user_touxiang);
        tv_user_nickname = (TextView) findViewById(R.id.tv_user_nickname);
        ib_edit_userinfo = (ImageButton) findViewById(R.id.ib_edit_userinfo);
        ib_edit_userinfo.setOnClickListener(this);
        //初始化实名认证部分
        rl_uncheck_realname = (RelativeLayout) findViewById(R.id.rl_uncheck_realname);
        rl_realName_Authentication = (RelativeLayout) findViewById(R.id.rl_realName_Authentication);
        tv_checked_realname = (TextView) findViewById(R.id.tv_checked_realname);
        tv_no_real_name = (TextView) findViewById(R.id.tv_no_real_name);
        //初始化是否绑定银行卡部分
        rl_unbind_bankcard = (RelativeLayout) findViewById(R.id.rl_unbind_bankcard);
        rl_bindCard = (RelativeLayout) findViewById(R.id.rl_bindCard);
        ll_bind_bankcard = (LinearLayout) findViewById(R.id.ll_bind_bankcard);
        iv_bind_banklogo = (ImageView) findViewById(R.id.iv_bind_banklogo);
        tv_bind_bankname = (TextView) findViewById(R.id.tv_bind_bankname);

        rl_profile_pwd_manager = (RelativeLayout) findViewById(R.id.rl_profile_pwd_manager);
        rl_profile_pwd_manager.setOnClickListener(this);
        rl_profile_phone_notification = (RelativeLayout) findViewById(R.id.rl_profile_phone_notification);
        profile_account__setting_selected_btn = (ImageView) findViewById(R.id.profile_account__setting_selected_btn);
        phoneNotification = true;
        rl_profile_phone_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNotification) {
                    profile_account__setting_selected_btn.setImageResource(R.drawable.profile_account__setting_unselected_btn);
                } else {
                    profile_account__setting_selected_btn.setImageResource(R.drawable.profile_account__setting_selected_btn);
                }
                phoneNotification = !phoneNotification;
            }
        });
        tv_logout = (TextView) findViewById(R.id.tv_logout);
        tv_logout.setOnClickListener(this);
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
        accountSettingPresenterImpl.sendRequest(necessaryParams, null);
    }


    public void accountmanage(View view) {
        MyActivityManager.getInstance().startNextActivity(ProfileAccountManageActivity.class);
    }

    public void more(View view) {
        MyActivityManager.getInstance().startNextActivity(ProfileMoreInfoActivity.class);

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
    public void showError(OnPresenterListener listener, AccountSettingBean accountSettingBean) {
        dismissLoadingDialog();
        CommonToastUtils.showToast(this, accountSettingBean.getMsg());
    }

    @Override
    public void responseData(OnPresenterListener listener, AccountSettingBean accountSettingBean) {
        this.accountSettingBean = accountSettingBean;
        if (!TextUtils.isEmpty(accountSettingBean.getHeadImgUrl())) {
            SPUtils.put(this, "LOGIN", "headImgUrl", accountSettingBean.getHeadImgUrl());
        }
        //绑定头像的数据
        bindTouxiangData();
        //设置是否实名认证
        setIsRealNameAuthentication();
        //设置是否已经绑卡
        setIsBindCard();
        //把邮箱存储在sp中
        saveBindMailBox();
    }

    private void saveBindMailBox() {
        String email = accountSettingBean.getMember().getEmail();
        if (!TextUtils.isEmpty(email))
            SPUtils.put(this, "LOGIN", "email", email);
    }

    private void setIsBindCard() {
        AccountSettingBean.AccountBankcardBean accountBankcard = accountSettingBean.getAccountBankcard();
        if (accountBankcard == null) {
            //未绑卡
            rl_unbind_bankcard.setVisibility(View.VISIBLE);
            ll_bind_bankcard.setVisibility(View.GONE);
            rl_bindCard.setClickable(true);
            rl_bindCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyActivityManager.getInstance().startNextActivity(ProfileAddBankCardActivity.class, "", "0");
                }
            });
        } else {
            //已绑卡
            rl_unbind_bankcard.setVisibility(View.GONE);
            ll_bind_bankcard.setVisibility(View.VISIBLE);
            tv_bind_bankname.setText(accountBankcard.getBanktruename());
//            iv_bind_banklogo.setImageResource(BankCardUtils.getBankLogoResource(accountBankcard.getBankname()));
            Picasso.with(this)
                    .load(accountBankcard.getBankicon())
                    .into(iv_bind_banklogo);
            rl_bindCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyActivityManager.getInstance().startNextActivity(ProfileBankCardManageActivity.class);
                }
            });
        }
    }

    private void setIsRealNameAuthentication() {
        if (!TextUtils.isEmpty(accountSettingBean.getMember().getTruename())) {
            //已经实名了
            rl_uncheck_realname.setVisibility(View.GONE);
            tv_checked_realname.setVisibility(View.VISIBLE);
            StringBuilder sb = new StringBuilder();
            String truename = accountSettingBean.getMember().getTruename();
            for (int i = 0; i < truename.length(); i++) {
                if (i == 0) {
                    sb.append(truename.charAt(i));
                } else {
                    sb.append("*");
                }
            }
            sb.append("(");
            String idcode = accountSettingBean.getMember().getIdcode();
            for (int i = 0; i < idcode.length(); i++) {
                if (i <= 3 || i >= idcode.length() - 4) {
                    sb.append(idcode.charAt(i));
                } else {
                    sb.append("*");
                }
            }
            sb.append(")");
            tv_checked_realname.setText(sb.toString());
            rl_realName_Authentication.setClickable(false);
        } else {
            tv_no_real_name.setVisibility(View.VISIBLE);
            rl_realName_Authentication.setClickable(true);
            rl_realName_Authentication.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyActivityManager.getInstance().startNextActivity(ProfileRealNameAuthenticationActivity.class);
                }
            });
        }
    }

    private void bindTouxiangData() {
        //设置头像图片和昵称
        setHeadImage();
        setNickName();
    }

    private void setHeadImage() {
        headImgUrl = (String) SPUtils.get(this, "LOGIN", "headImgUrl", "");

        if (!TextUtils.isEmpty(headImgUrl)) {
            Picasso.with(this)
                    .load(headImgUrl)
                    .placeholder(R.drawable.mango_baby_4)
                    .error(R.drawable.mango_baby_4)
                    .into(iv_user_touxiang);

        } else {
            Picasso
                    .with(this)
                    .cancelRequest(iv_user_touxiang);
            iv_user_touxiang.setImageResource(R.drawable.mango_baby_4);
        }
    }


    private void setNickName() {
        String nickname = accountSettingBean.getNickname();
        if (!TextUtils.isEmpty(nickname)) {
            tv_user_nickname.setText(nickname);
            SPUtils.put(this, "LOGIN", "nickname", nickname);
        } else {
            String userName = (String) SPUtils.get(this, "LOGIN", "name", "");
            tv_user_nickname.setText(userName);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_edit_userinfo:
            case R.id.rl_head_image_area:
                MyActivityManager.getInstance().startNextActivity(ProfileMyInfosActivity.class);
                break;

            case R.id.rl_profile_pwd_manager:
                MyActivityManager.getInstance().startNextActivity(ProfilePwdManageActivity.class);
                break;
            case R.id.tv_logout:
                logout();
                break;
        }
    }

    private void logout() {
        final CustomCommonDialog logoutDialog = new CustomCommonDialog(this, "温馨提示", "您确认安全退出该账户吗?", "退出", "取消", false);
        logoutDialog.show();
        logoutDialog.setClicklistener(new CustomCommonDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                int mid = (int) SPUtils.get(ProfileAccountSettingActivity.this, "LOGIN", "id", -1);
                SPUtils.put(ProfileAccountSettingActivity.this, "EYE_STATUS", "mid", mid);
                SPUtils.clear(ProfileAccountSettingActivity.this, "LOGIN");
//                popActivity();
                MyActivityManager.getInstance().popCurrentActivity();
                logoutDialog.dismiss();
            }

            @Override
            public void doCancel() {
                logoutDialog.dismiss();
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
