package com.mgjr.view.common;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.PhoneUtils;
import com.mgjr.Utils.SPUtils;
import com.mgjr.Utils.UMengUtil;
import com.mgjr.mangofinance.MainActivity;
import com.mgjr.model.bean.AdBean;
import com.mgjr.presenter.impl.AdPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.APIBuilder;
import com.mgjr.share.CommomActivity;
import com.mgjr.view.listeners.ViewListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/11/3.
 */

public class WelcomeActivity extends CommomActivity implements ViewListener<AdBean> {
    private AdPresenterImpl adPresenter;

    private ImageView img_ad;

    private TextView tv_timer;
    //启动图
    private ImageView img_splash;
    private String url;

    private int time;
    private Thread mThread;
    private boolean isFirstIn;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int timeCount = msg.what;
            tv_timer.setText(timeCount + "");
            if (timeCount == 1) {
                checkIsFirstIn();
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_ad);
        img_ad = (ImageView) findViewById(R.id.img_ad);
        tv_timer = (TextView) findViewById(R.id.tv_timer);
        //设置启动图片
        img_splash = (ImageView) findViewById(R.id.img_splash);
        img_splash.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                img_splash.setVisibility(View.GONE);
                mThread.start();
            }
        }, 3000);

        adPresenter = new AdPresenterImpl(this);

        initTimer();
        resetUserInfo();
        String deviceInfo = UMengUtil.getDeviceInfo(this);
//        LogUtil.d("deviceInfo====" + deviceInfo);


    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        requestUrl();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    private void requestUrl() {
        String device = APIBuilder.getDevice();
        Map<String, String> params = new HashMap<>();
        params.put("device", device);
        adPresenter.sendRequest(params, null);
    }

    private void initTimer() {
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    time = 3;
                    for (int i = 3; i > 0; i--) {
                        Message msg = new Message();
                        time = i;
                        msg.what = time;
                        handler.sendEmptyMessage(time);
                        Thread.sleep(1000);
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private void checkIsFirstIn() {
        SharedPreferences preferences = getSharedPreferences("first_pref",
                MODE_PRIVATE);
        isFirstIn = preferences.getBoolean("isFirstIn", true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFirstIn) {
                    // start guideactivity1
                    MyActivityManager.getInstance().startNextActivity(SplashActivity.class);
                    MyActivityManager.getInstance().finishSpecifiedActivity(WelcomeActivity.class);
                } else {
                    checkBootType();
                }
            }
        }, 10);
    }

    private void checkBootType() {
        //手势密码

        boolean isLogined = (boolean) SPUtils.get(this, "LOGIN", "isLogined", false);
        boolean gestureSwitch = (boolean) SPUtils.get(this, "LOGIN", "Gesture_Switch", false);
        if (isLogined) {

            if (gestureSwitch) {

                MyActivityManager.getInstance().startNextActivity(GestureLockCheckActivity.class, "1");
                MyActivityManager.getInstance().finishSpecifiedActivity(WelcomeActivity.class);
            } else {
                MyActivityManager.getInstance().startNextActivity(MainActivity.class);
                MyActivityManager.getInstance().finishSpecifiedActivity(WelcomeActivity.class);
            }

        } else {
            MyActivityManager.getInstance().startNextActivity(MainActivity.class);
            MyActivityManager.getInstance().finishSpecifiedActivity(WelcomeActivity.class);
        }

    }


    /*
     * 判断是否第一次启动APP，是第一次就删除用户信息
	 * */
    private void resetUserInfo() {
        SharedPreferences userInfo = getSharedPreferences("LOGIN", 0);
        SharedPreferences appVersion = this.getSharedPreferences("appVersion", 0);

        String versionCode = PhoneUtils.getVersion();
        String versionName = appVersion.getString("VersionCode", null);
        if (versionName != null) {
            if (!versionCode.equalsIgnoreCase(versionName)) {
                userInfo.edit().clear().apply();
                appVersion.edit().putString("VersionCode", versionCode).apply();
            }
        } else {
            appVersion.edit().putString("VersionCode", versionCode).apply();
        }


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
    public void showError(OnPresenterListener listener, AdBean adBean) {

    }

    @Override
    public void responseData(OnPresenterListener listener, AdBean adBean) {
        if (adBean.getAdvertisement() != null) {
            url = adBean.getAdvertisement().getImage_url();
            if (url != null) {
                img_ad.setScaleType(ImageView.ScaleType.FIT_XY);
                Picasso.with(this).load(url).into(img_ad);
            }
        }
    }
}
