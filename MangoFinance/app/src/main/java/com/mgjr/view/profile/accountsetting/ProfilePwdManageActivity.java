package com.mgjr.view.profile.accountsetting;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.SPUtils;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.view.common.ForgetPasswordActivity;
import com.mgjr.view.common.GestureLockCheckActivity;
import com.mgjr.view.common.GestureLockSettingActivity;
import com.mylhyl.superdialog.SuperDialog;

import java.util.ArrayList;
import java.util.List;

public class ProfilePwdManageActivity extends ActionbarActivity {

    private static final String AUTH_FOR_CLOSE_GESTURE_PWD = "0";
    private RelativeLayout rl_reset_gesture_pwd;
    private ImageView imgswitch_gesture;
    private boolean gesture_switch;
    private boolean isCanShake;
    private PopupWindow loginPwdPopWindow;
    private boolean tradePwd;
    private TextView tv_unset_trade_pwd;
    private PopupWindow tradePwdPopWindow;
    private ImageView iv_shake_btn;
    private RelativeLayout rl_shake_for_profit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_profile_pwd_manage, this);
        actionbar.setCenterTextView("密码管理");
        initView();
    }

    private void initView() {

        rl_reset_gesture_pwd = (RelativeLayout) findViewById(R.id.rl_reset_gesture_pwd);
        rl_shake_for_profit = (RelativeLayout) findViewById(R.id.rl_shake_for_profit);
        imgswitch_gesture = (ImageView) findViewById(R.id.iv_switch_gesture);
        iv_shake_btn = (ImageView) findViewById(R.id.iv_shake_btn);
        tv_unset_trade_pwd = (TextView) findViewById(R.id.tv_unset_trade_pwd);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initGestureSwitch();
        isSetPayPwd();
    }

    private void isSetPayPwd() {
        tradePwd = (boolean) SPUtils.get(this, "LOGIN", "isPaypwd", false);
        if (tradePwd) {
            tv_unset_trade_pwd.setVisibility(View.GONE);
        }
    }

    private void initGestureSwitch() {
        gesture_switch = (boolean) SPUtils.get(this, "LOGIN", "Gesture_Switch", false);
        if (!gesture_switch) {
            //关闭
//            rl_reset_gesture_pwd.setVisibility(View.GONE);
//            rl_shake_for_profit.setVisibility(View.GONE);
            imgswitch_gesture.setBackgroundResource(R.drawable.profile_account__setting_unselected_btn);
        } else {
            //打开
//            rl_reset_gesture_pwd.setVisibility(View.VISIBLE);
//            rl_shake_for_profit.setVisibility(View.VISIBLE);
            imgswitch_gesture.setBackgroundResource(R.drawable.profile_account__setting_selected_btn);
        }
        //初始化摇一摇
        initShake();
    }


    private void initShake() {
        isCanShake = (boolean) SPUtils.get(this, "LOGIN", "Shake_For_Profit_Switch", false);
        if (!isCanShake) {
            //关闭
            iv_shake_btn.setBackgroundResource(R.drawable.profile_account__setting_unselected_btn);
        } else {
            //打开
            iv_shake_btn.setBackgroundResource(R.drawable.profile_account__setting_selected_btn);
        }
    }

    public void loginPwd(View view) {
        MyActivityManager.getInstance().startNextActivity(ProfileModifyLoginPwdActivity.class);
        ;
    }

    /**
     * 登录密码弹窗
     */
    private void showLoginPwdPopupWindow() {

        final List<String> list = new ArrayList<>();
        list.add("修改登录密码");
        list.add("忘记登录密码");

        new SuperDialog.Builder(this)
//                .setAlpha(0.5f)
                .setCanceledOnTouchOutside(true)
                .setGravity(Gravity.CENTER)
                .setItems(list, new SuperDialog.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        switch (position) {
                            case 0:
                                MyActivityManager.getInstance().startNextActivity(ProfileModifyLoginPwdActivity.class);
                                break;
                            case 1:
                                MyActivityManager.getInstance().startNextActivity(ForgetPasswordActivity.class);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .build();
       /* //设置contentView
        View contentView = LayoutInflater.from(this).inflate(R.layout.layout_select_header_image, null);
        loginPwdPopWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        loginPwdPopWindow.setContentView(contentView);
        TextView tv_cancle = (TextView) contentView.findViewById(R.id.tv_cancle);
        TextView tv_modify_login_pwd = (TextView) contentView.findViewById(R.id.tv_camera);
        TextView tv_forget_login_pwd = (TextView) contentView.findViewById(R.id.tv_gallery);
        TextView tv_mgbaby = (TextView) contentView.findViewById(R.id.tv_mgbaby);

        tv_modify_login_pwd.setText("修改登录密码");
//        tv_forget_login_pwd.setText("忘记登录密码");
        tv_forget_login_pwd.setVisibility(View.GONE);
        tv_mgbaby.setVisibility(View.GONE);

        tv_modify_login_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到修改登录密码
                MyActivityManager.getInstance().startNextActivity(ProfileModifyLoginPwdActivity.class);
                loginPwdPopWindow.dismiss();
            }
        });
        *//*tv_forget_login_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到忘记登录密码界面
                MyActivityManager.getInstance().startNextActivity(ForgetPasswordActivity.class);
                loginPwdPopWindow.dismiss();
            }
        });*//*

        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginPwdPopWindow != null) {
                    loginPwdPopWindow.dismiss();
                }
            }
        });
        //显示PopupWindow
        loginPwdPopWindow.setAnimationStyle(R.style.calcAnim);
        View rootview = LayoutInflater.from(this).inflate(R.layout.activity_profile_pwd_manage, null);
        loginPwdPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);*/
    }

    public void setTradePwd(View view) {
        //如果没有设置交易密码,点击,进入设置交易密码界面

        if (!tradePwd) {
            MyActivityManager.getInstance().startNextActivity(ProfileSetPayPwdActivity.class);
        } else {
            //如果已经设置了交易密码,弹窗
            showTradePwdPopupWindow();
        }

    }

    /**
     * 交易密码弹窗
     */
    private void showTradePwdPopupWindow() {
        final List<String> list = new ArrayList<>();
        list.add("修改交易密码");
        list.add("忘记交易密码");

        new SuperDialog.Builder(this)
//                .setAlpha(0.5f)
                .setCanceledOnTouchOutside(true)
                .setGravity(Gravity.CENTER)
                .setItems(list, new SuperDialog.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        switch (position) {
                            case 0:
                                //跳转到修改交易密码
                                MyActivityManager.getInstance().startNextActivity(ProfileModifyTradePwdActivity.class);
                                break;
                            case 1:
                                //跳转到忘记交易密码界面(找回交易密码)
                                MyActivityManager.getInstance().startNextActivity(ProfileFindTradePwdActivity.class);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .build();
        //设置contentView
        /*View contentView = LayoutInflater.from(this).inflate(R.layout.layout_select_header_image, null);
        tradePwdPopWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        tradePwdPopWindow.setContentView(contentView);
        TextView tv_cancle = (TextView) contentView.findViewById(R.id.tv_cancle);
        TextView tv_modify_trade_pwd = (TextView) contentView.findViewById(R.id.tv_camera);
        TextView tv_forget_trade_pwd = (TextView) contentView.findViewById(R.id.tv_gallery);
        TextView tv_mgbaby = (TextView) contentView.findViewById(R.id.tv_mgbaby);

        tv_modify_trade_pwd.setText("修改交易密码");
        tv_forget_trade_pwd.setText("忘记交易密码");
        tv_mgbaby.setVisibility(View.GONE);

        tv_modify_trade_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到修改交易密码
                MyActivityManager.getInstance().startNextActivity(ProfileModifyTradePwdActivity.class);
                tradePwdPopWindow.dismiss();
            }
        });
        tv_forget_trade_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到忘记交易密码界面(找回交易密码)
                MyActivityManager.getInstance().startNextActivity(ProfileFindTradePwdActivity.class);
                tradePwdPopWindow.dismiss();
            }
        });

        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tradePwdPopWindow != null) {
                    tradePwdPopWindow.dismiss();
                }
            }
        });
        //显示PopupWindow
        tradePwdPopWindow.setAnimationStyle(R.style.calcAnim);
        View rootview = LayoutInflater.from(this).inflate(R.layout.activity_profile_pwd_manage, null);
        tradePwdPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);*/
    }

    public void resetGestureLock(View view) {
        MyActivityManager.getInstance().startNextActivity(GestureLockSettingActivity.class);
    }

    public void gesturelock(View view) {
        if (gesture_switch) {
            //手势密码开关已经打开,点击关闭前先跳转到验证手势密码界面
            MyActivityManager.getInstance().startNextActivity(GestureLockCheckActivity.class, AUTH_FOR_CLOSE_GESTURE_PWD);
           /* SPUtils.put(this, "LOGIN", "Gesture_Switch", false);
            imgswitch_gesture.setBackgroundResource(R.drawable.profile_account__setting_unselected_btn);
            CommonToastUtils.showToast(this, "手势密码关闭");
            rl_reset_gesture_pwd.setVisibility(View.GONE);
            rl_shake_for_profit.setVisibility(View.GONE);*/
        } else {
            //手势密码开始是关闭的,点击跳转到设置手势密码
            MyActivityManager.getInstance().startNextActivity(GestureLockSettingActivity.class);
           /* SPUtils.put(this, "LOGIN", "Gesture_Switch", true);
            imgswitch_gesture.setBackgroundResource(R.drawable.profile_account__setting_selected_btn);
            CommonToastUtils.showToast(this, "手势密码开启");
            rl_reset_gesture_pwd.setVisibility(View.VISIBLE);
            rl_shake_for_profit.setVisibility(View.VISIBLE);*/
        }
    }

    /**
     * 摇一摇点击处理
     *
     * @param view
     */
    public void shake(View view) {
        if (isCanShake) {
            SPUtils.put(this, "LOGIN", "Shake_For_Profit_Switch", false);
            iv_shake_btn.setBackgroundResource(R.drawable.profile_account__setting_unselected_btn);
            CommonToastUtils.showToast(this, "摇一摇关闭");
        } else {
            SPUtils.put(this, "LOGIN", "Shake_For_Profit_Switch", true);
            iv_shake_btn.setBackgroundResource(R.drawable.profile_account__setting_selected_btn);
            CommonToastUtils.showToast(this, "摇一摇开启");
        }
        isCanShake = !isCanShake;
    }

}
