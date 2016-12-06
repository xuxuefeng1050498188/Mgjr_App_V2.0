package com.mgjr.view.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.PopwUtils;
import com.mgjr.Utils.SPUtils;
import com.mgjr.mangofinance.MainActivity;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.model.bean.ShakeBean;
import com.mgjr.presenter.impl.ShakeGetYestodayIncomePresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.CircleImageView;
import com.mgjr.share.CustomCommonDialog;
import com.mgjr.share.LockPatternView;
import com.mgjr.share.ShakeDetector;
import com.mgjr.view.listeners.ViewListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestureLockCheckActivity extends ActionbarActivity implements LockPatternView.OnPatternListener, ShakeDetector.Listener, ViewListener<ShakeBean> {

    private CircleImageView civ_user_portrait;
    private Handler mHandler = new Handler();
    private TextView tv_pwd_prompt;
    private PopupWindow shakeForProfitPopw;
    private View rootView;
    private boolean isDisplay = true;
    private View shakeView;
    private ImageView iv_dismiss;
    private FrameLayout shake_bg;
    private LinearLayout ll_shake;
    private LinearLayout ll_gesture_lock_check_bg;
    private ShakeDetector shakeDetector;

    private SharedPreferences sp;
    private ShakeGetYestodayIncomePresenterImpl shakeGetYestodayIncomePresenter;

    private TextView tv_pupwindow_tips;
    private TextView tv_pupwindow_yesterdayIncome;
    private TextView tv_date, tv_month;
    //昨日收益
    private double yesterdayIncome;
    //tips
    private String days;
    private boolean isCanShake;
    private String headImgUrl;
    private LockPatternView lockPatternView;
    private List<LockPatternView.Cell> lockPattern;
    private int gestureLockInputTimes = 5;
    private String type;//"0"-- 关闭手势密码 "1" 登录时验证手势密码
    private CircleImageView iv_user_touxiang;
    private RelativeLayout rl_shake_for_profit;
    private TextView tv_forget_gesture_pwd;
    private TextView tv_skip_gesture_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_lock_check);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_gesture_lock_check, null);

        type = getIntent().getStringExtra("code");
        actionbar.setVisibility(View.GONE);
        shakeGetYestodayIncomePresenter = new ShakeGetYestodayIncomePresenterImpl(this);
        initViews();
        //初始化用户头像
        civ_user_portrait = (CircleImageView) findViewById(R.id.civ_user_portrait);
        //初始化手势密码锁
        initGestureLockView();
        setInitHeadImage();
    }

    /**
     * 点击跳过执行的方法
     *
     * @param view
     */
    public void skip(View view) {
        MyActivityManager.getInstance().popCurrentActivity();
    }

    private void initViews() {
        isCanShake = (boolean) SPUtils.get(this, "LOGIN", "Shake_For_Profit_Switch", false);
        tv_forget_gesture_pwd = (TextView) findViewById(R.id.tv_forget_gesture_pwd);
        tv_skip_gesture_pwd = (TextView) findViewById(R.id.tv_skip_gesture_pwd);
        rl_shake_for_profit = (RelativeLayout) findViewById(R.id.rl_shake_for_profit);
        if ("0".equals(type)) {
            tv_forget_gesture_pwd.setVisibility(View.GONE);
            rl_shake_for_profit.setVisibility(View.GONE);
            tv_skip_gesture_pwd.setVisibility(View.VISIBLE);
            isCanShake = false;
        } else {
            tv_skip_gesture_pwd.setVisibility(View.GONE);
        }

        //查看密码中心的摇一摇是否打开
       /* if (isCanShake) {
            rl_shake_for_profit.setVisibility(View.VISIBLE);
        } else {
            rl_shake_for_profit.setVisibility(View.GONE);
        }*/
        tv_pwd_prompt = (TextView) findViewById(R.id.tv_pwd_prompt);
        ll_gesture_lock_check_bg = (LinearLayout) findViewById(R.id.ll_gesture_lock_check_bg);
        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_month.setText(new SimpleDateFormat("MMMM").format(System.currentTimeMillis()));
        tv_date.setText(new SimpleDateFormat("d").format(System.currentTimeMillis()));
    }

    private void initGestureLockView() {
        lockPatternView = (LockPatternView) findViewById(R.id.id_lock_pattern_view);
        String patternString = (String) SPUtils.get(this, "LOGIN", "GESTURE_PWD", "");
        lockPattern = LockPatternView.stringToPattern(patternString);
        lockPatternView.setOnPatternListener(this);
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
    protected void onResume() {
        super.onResume();
        //注册摇一摇
        registerShake();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shakeDetector.stop();
    }

    /**
     * 注册摇一摇
     */
    private void registerShake() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        shakeDetector = new ShakeDetector(this);
        shakeDetector.start(sensorManager);

    }

    /**
     * 当用户摇一摇触发的事件
     */
    @Override
    public void hearShake() {
        if (isCanShake) {
            if (isDisplay) {
                getYestodayIncome();
            }
        }
    }

    /*获取昨日收益**/
    private void getYestodayIncome() {
        sp = this.getSharedPreferences("LOGIN", MODE_PRIVATE);
        int id = sp.getInt("id", 0);
        String mid = String.valueOf(id);
        Map<String, String> necessaryParams = new HashMap<>();
        necessaryParams.put("mid", mid);
        shakeGetYestodayIncomePresenter.sendRequest(necessaryParams, null);
    }


    /*
    * 配置活动弹窗
    * */

    private void setPupWindow() {
        //把头像隐藏
        civ_user_portrait.setVisibility(View.INVISIBLE);
        shakeView = View.inflate(this, R.layout.activity_shake_for_profit, null);
        tv_pupwindow_tips = (TextView) shakeView.findViewById(R.id.tv_pupwindow_tips);
        iv_user_touxiang = (CircleImageView) shakeView.findViewById(R.id.iv_user_touxiang);
        //绑定弹窗的头像
        setPopwHeadImage();
        tv_pupwindow_tips.setText("您与芒果金融已携手走过了\n" + days);
        tv_pupwindow_yesterdayIncome = (TextView) shakeView.findViewById(R.id.tv_pupwindow_yesterdayIncome);
        if (yesterdayIncome == 0) {
            tv_pupwindow_yesterdayIncome.setText("暂无收益");
        } else {
            tv_pupwindow_yesterdayIncome.setText(new DecimalFormat("###,###,##0.00").format(yesterdayIncome) + "元");
        }
        iv_dismiss = (ImageView) shakeView.findViewById(R.id.iv_dismiss);
        ivDismissEnterAnim();
        iv_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_dismiss.clearAnimation();
                iv_dismiss.setVisibility(View.GONE);
                shakeForProfitPopw.dismiss();
                isDisplay = true;
            }
        });

        shakeForProfitPopw = new PopupWindow(shakeView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        shakeForProfitPopw.setAnimationStyle(R.style.shakeAnimation);
        shakeForProfitPopw.setTouchable(true);
        shakeForProfitPopw.setBackgroundDrawable(new ColorDrawable());
        shakeForProfitPopw.setFocusable(true);
        shakeForProfitPopw.setOutsideTouchable(true);
        PopwUtils.backgroundAlpha(0.5f, GestureLockCheckActivity.this);
        shakeForProfitPopw.showAtLocation(rootView, Gravity.CENTER, 0, 0);
        shakeForProfitPopw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                civ_user_portrait.setVisibility(View.VISIBLE);
                PopwUtils.backgroundAlpha(1f, GestureLockCheckActivity.this);
            }
        });
    }

    private void ivDismissEnterAnim() {
        iv_dismiss.postDelayed(new Runnable() {
            @Override
            public void run() {
                iv_dismiss.setVisibility(View.VISIBLE);
            }
        }, 500);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(iv_dismiss, "alpha", 0f, 1f);
        objectAnimator.setStartDelay(500);
        objectAnimator.setDuration(500);
        objectAnimator.start();
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                iv_dismiss.clearAnimation();
            }
        });
    }

    private void setPopwHeadImage() {
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

    /*
   * 活动弹窗进入动画
   * */
    private void setShakeAnim() {
//        ll_shake = (LinearLayout) shakeView.findViewById(R.id.ll_shake);
        TranslateAnimation ta = new TranslateAnimation(0, 0, -800, 0);
        ta.setDuration(700);
        ta.setFillBefore(false);
        shakeView.startAnimation(ta);
        /*//背景渐变的动画
        shake_bg = (FrameLayout) shakeView.findViewById(R.id.shake_bg);
        AlphaAnimation aa = new AlphaAnimation(0f, 1f);
        aa.setDuration(700);
        shake_bg.startAnimation(aa);*/
    }
    /*
   * 活动弹窗退出动画
   * */

    private void finishShakeAnim() {
        ll_shake = (LinearLayout) shakeView.findViewById(R.id.ll_shake);
        TranslateAnimation ta = new TranslateAnimation(0, 0, 0, -800);
        ta.setDuration(700);
        ta.setFillBefore(false);
        ll_shake.startAnimation(ta);
        /*//背景渐变的动画
        shake_bg = (FrameLayout) shakeView.findViewById(R.id.shake_bg);
        AlphaAnimation aa = new AlphaAnimation(1f, 0f);
        aa.setDuration(2000);
        shake_bg.startAnimation(aa);*/
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
    }

    @Override
    public void showError(OnPresenterListener listener, ShakeBean shakeBean) {
        CommonToastUtils.showToast(MainApplication.getContext(), shakeBean.getMsg());
    }

    @Override
    public void responseData(OnPresenterListener listener, ShakeBean shakeBean) {
        days = shakeBean.getDays();
        yesterdayIncome = shakeBean.getYesterdayIncome();
        if (isDisplay) {
            setPupWindow();
            isDisplay = false;
        }
//        isDisplay = !isDisplay;
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
        if (pattern.equals(lockPattern)) {
            CommonToastUtils.showToast(this, "手势密码绘制成功");
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if ("0".equals(type)) {
                        SPUtils.put(GestureLockCheckActivity.this, "LOGIN", "Gesture_Switch", false);
                        MyActivityManager.getInstance().popCurrentActivity();
                    } else {
                        MyActivityManager.getInstance().startNextActivity(MainActivity.class);
                        MyActivityManager.getInstance().popCurrentActivity();
                    }
                }
            }, 600);
        } else {
            lockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
            gestureLockInputTimes--;
            CommonToastUtils.showToast(this, "手势密码错误");
            if (gestureLockInputTimes == 0) {
                CustomCommonDialog dialog = new CustomCommonDialog(true, GestureLockCheckActivity.this, "", "手势密码已失效，请重新登录", "重新登录", true);
                dialog.setClicklistener(new CustomCommonDialog.ClickListenerInterfaceSingle() {
                    @Override
                    public void doSingleBtn() {
                        MyActivityManager.getInstance().startNextActivity(LoginActivity.class);
//                        popActivity();
                        MyActivityManager.getInstance().popCurrentActivity();
                    }
                });
                dialog.show();
                dialog.setCancelable(false);
            }
            tv_pwd_prompt.setText("手势密码绘制错误，还可输入" + gestureLockInputTimes + "次");
            gesturePromptTextAnim();
        }
    }

    private void gesturePromptTextAnim() {
        tv_pwd_prompt.setTextColor(Color.RED);
        TranslateAnimation translateAnimation = new TranslateAnimation(-5f, 5f, 0f, 0f);
        translateAnimation.setDuration(50);
        translateAnimation.setRepeatCount(3);
        tv_pwd_prompt.startAnimation(translateAnimation);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lockPatternView.clearPattern();
            }
        }, 600);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // disable back key
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 忘记手势密码
     */
    public void forgetGesturePwd(View view) {
        final CustomCommonDialog dialog = new CustomCommonDialog(GestureLockCheckActivity.this, "", "重置手势密码需要重新登录", "重新登录", "取消", true);
        dialog.setClicklistener(new CustomCommonDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                MyActivityManager.getInstance().startNextActivity(LoginActivity.class, "GestureLockCheckActivity");
                MyActivityManager.getInstance().finishSpecifiedActivity(GestureLockCheckActivity.class);
            }

            @Override
            public void doCancel() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
