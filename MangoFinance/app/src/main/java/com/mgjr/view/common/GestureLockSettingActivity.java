package com.mgjr.view.common;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.SPUtils;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.CircleImageView;
import com.mgjr.share.CommomActivity;
import com.mgjr.share.LockPatternView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class GestureLockSettingActivity extends ActionbarActivity implements
        LockPatternView.OnPatternListener {

    private CircleImageView civ_user_portrait;
    private Handler mHandler = new Handler();
    private TextView tv_pwd_prompt;
    private SharedPreferences sp;
    private String headImgUrl;
    private TextView tv_month;
    private TextView tv_date;

    /**
     * 下面是关于手势密码的变量和初始化
     */
    private LockPatternView lockPatternView;
    private static final int STEP_1 = 1; // 开始
    private static final int STEP_2 = 2; // 第一次绘制完成
    private static final int STEP_3 = 3; // 第二次设置手势完成
    private int step;
    private List<LockPatternView.Cell> choosePattern;
    private boolean confirm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_lock_setting);
        actionbar.setVisibility(View.GONE);
        initViews();
        initGestureLockView();
    }

    private void initViews() {
        tv_pwd_prompt = (TextView) findViewById(R.id.tv_pwd_prompt);
        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_month.setText(new SimpleDateFormat("MMMM").format(System.currentTimeMillis()));
        tv_date.setText(new SimpleDateFormat("d").format(System.currentTimeMillis()));
        //初始化用户头像
        civ_user_portrait = (CircleImageView) findViewById(R.id.civ_user_portrait);
        //初始化手势密码锁
        setInitHeadImage();
    }

    private void initGestureLockView() {
        lockPatternView = (LockPatternView) findViewById(R.id.id_gestureLockView);
        lockPatternView.setOnPatternListener(this);
        step = STEP_1;
        updateGestureLockView();
    }

    private void updateGestureLockView() {
        switch (step) {
            case STEP_1:
                choosePattern = null;
                confirm = false;
                lockPatternView.clearPattern();
                lockPatternView.enableInput();
                break;
            case STEP_2:
                tv_pwd_prompt.setText("请再次输入手势密码进行确认");
                lockPatternView.disableInput();
                lockPatternView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lockPatternView.clearPattern();
                        lockPatternView.enableInput();
                    }
                }, 600);
                break;
            case STEP_3:
                if (confirm) {
                    //手势密码设置成功
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SPUtils.put(GestureLockSettingActivity.this, "LOGIN", "GESTURE_PWD", LockPatternView.patternToString(choosePattern));
                            SPUtils.put(GestureLockSettingActivity.this, "LOGIN", "Gesture_Switch", true);
                            SPUtils.put(GestureLockSettingActivity.this, "LOGIN", "Shake_For_Profit_Switch", true);
                            MyActivityManager.getInstance().popCurrentActivity();
//                            MyActivityManager.getInstance().startNextActivity(GestureLockCheckActivity.class);
                        }
                    }, 600);
                } else {
                    /*lockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                    lockPatternView.enableInput();*/
                    lockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                    lockPatternView.disableInput();
                    lockPatternView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lockPatternView.clearPattern();
                            lockPatternView.enableInput();
                        }
                    }, 600);
                }
                break;
            default:
                break;
        }
    }


    /**
     * 点击跳过执行的方法
     *
     * @param view
     */
    public void skip(View view) {
        MyActivityManager.getInstance().popCurrentActivity();
    }

    private void setInitHeadImage() {
        headImgUrl = (String) SPUtils.get(this, "LOGIN", "headImgUrl", "");
        if (!TextUtils.isEmpty(headImgUrl)) {

            Picasso.with(this)
                    .load(headImgUrl)
                    .placeholder(R.drawable.mango_baby_4)
                    .error(R.drawable.mango_baby_4)
                    .into(civ_user_portrait);
        } else {
            Picasso
                    .with(this)
                    .cancelRequest(civ_user_portrait);
            civ_user_portrait.setImageResource(R.drawable.mango_baby_4);
        }

    }

    @Override
    public void onPatternStart() {

    }

    @Override
    public void onPatternCleared() {

    }

    @Override
    public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {

    }

    @Override
    public void onPatternDetected(List<LockPatternView.Cell> pattern) {
        if (pattern.size() < LockPatternView.MIN_LOCK_PATTERN_SIZE) {
            CommonToastUtils.showToast(this, "至少连接4个点，请重试");
            lockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
            lockPatternView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    lockPatternView.clearPattern();
                }
            }, 600);
            return;
        }
        if (choosePattern == null) {
            choosePattern = new ArrayList<LockPatternView.Cell>(pattern);
            step = STEP_2;
            updateGestureLockView();
            return;
        }
        if (choosePattern.equals(pattern)) {
            CommonToastUtils.showToast(this, "手势密码绘制成功");
            confirm = true;
        } else {
            confirm = false;
            CommonToastUtils.showToast(this, "与第一次绘制不一致，请重新绘制");
        }
        step = STEP_3;
        updateGestureLockView();
    }
}
