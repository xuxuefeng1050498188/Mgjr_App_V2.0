package com.mgjr.share;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.SPUtils;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.view.common.GestureLockCheckActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by wim on 16/5/30.
 */
public class CommomActivity extends AppCompatActivity {

    private static final int statusColor = Color.parseColor("#67B4EA");

    public static LinearLayout actionBarLayout;
    public LinearLayout rootContentLayout;

    private static boolean isActive = true;
    private static long time;
    private KProgressHUD progressHUD;

    /**
     * 重写这个方法实现全局修改字体
     */
 /*   @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commom_layout);
        setStatusBarColor();

        MyActivityManager.getInstance().pushActivity(this);

        actionBarLayout = (LinearLayout) findViewById(R.id.actionbar_layout);
        rootContentLayout = (LinearLayout) findViewById(R.id.rootcontent_layout);

        initLoadingProgressView();
    }


    public void setBaseContent(int layoutID, Context context) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layoutID, null);
        LinearLayout.LayoutParams sp_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(sp_params);
        rootContentLayout.addView(view);
    }

    public static int getActionBarHeight() {
        return actionBarLayout.getMeasuredHeight();
    }

    public void setStatusBarColor() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(statusColor);
            ViewGroup mContentView = (ViewGroup) this.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, true);
            }


        }
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isAppOnFreground()) {
            //进入后台
            isActive = false;
            long currentTimeMillis = System.currentTimeMillis();
            SPUtils.put(MainApplication.getContext(), "ENTER_BACKGROUNT_TIME", "currentTime", currentTimeMillis);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isActive) {
            //从后台唤醒
            isActive = true;
            long lastEnterBackgrountTime = (long) SPUtils.get(MainApplication.getContext(), "ENTER_BACKGROUNT_TIME", "currentTime", -1L);
            time = System.currentTimeMillis() - lastEnterBackgrountTime;
            if (lastEnterBackgrountTime != -1 && time > 300000) {
                boolean gestureSwitch = (boolean) SPUtils.get(this, "LOGIN", "Gesture_Switch", false);
                if (gestureSwitch) {
                    MyActivityManager.getInstance().startNextActivity(GestureLockCheckActivity.class);
                }
            }
            SPUtils.clear(MainApplication.getContext(), "ENTER_BACKGROUNT_TIME");
        }
        //友盟
        MobclickAgent.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initLoadingProgressView() {
        progressHUD = KProgressHUD.create(CommomActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0f);
    }

    public void showLoadingDialog() {
        progressHUD.show();
    }

    public void dismissLoadingDialog() {
        if (progressHUD != null) {
            progressHUD.dismiss();
        }
    }

    public void showSystemError() {
        boolean isSocketTimeoutException = (boolean) SPUtils.get(MainApplication.getContext(), "SocketTimeoutException", "isSocketTimeoutException", false);
        if (!NetUtils.isConnected(this)) {
            CommonToastUtils.showToast(this, getString(R.string.network_error));
        } else if (isSocketTimeoutException) {
            CommonToastUtils.showToast(this, getString(R.string.request_timeout));
            SPUtils.clear(MainApplication.getContext(), "SocketTimeoutException");
        } else {
            CommonToastUtils.showToast(this, getString(R.string.system_error));
        }
    }

    /**
     * 是否在前台运行
     *
     * @return
     */
    private boolean isAppOnFreground() {
        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        String curPackageName = getApplicationContext().getPackageName();
        List<android.app.ActivityManager.RunningAppProcessInfo> app = am.getRunningAppProcesses();
        if (app == null) {
            return false;
        }
        for (android.app.ActivityManager.RunningAppProcessInfo a : app) {
            if (a.processName.equals(curPackageName) &&
                    a.importance == android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
        /*ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        if(!TextUtils.isEmpty(curPackageName)&&curPackageName.equals(getPackageName())){
			return true;
		}
		return false;*/
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            MyActivityManager.getInstance().popCurrentActivity();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration conf = new Configuration();
        conf.setToDefaults();
        res.updateConfiguration(conf, res.getDisplayMetrics());
        return res;
    }


    /*点击edittext外部区域隐藏键盘*/
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if (isShouldHideInput(v, ev)) {
//
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//            }
//            return super.dispatchTouchEvent(ev);
//        }
//        // 必不可少，否则所有的组件都不会有TouchEvent了
//        if (getWindow().superDispatchTouchEvent(ev)) {
//            return true;
//        }
//        return onTouchEvent(ev);
//    }
}
