package com.mgjr.Utils;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mgjr.mangofinance.MainApplication;
import com.mgjr.view.common.GestureLockCheckActivity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/10/8.
 */

public class AppOnFregroundTimerServices extends Service {
//    private int i = 0;
//    private TimerServiceListener timerServiceListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTimer();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startTimer() {

        final Timer timer = new Timer();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (isAppOnFreground()) {
                    //在后台
                    SPUtils.put(MainApplication.getContext(), "LOGIN", "Gesture_Switch", false);
                }
            }
        };
        timer.schedule(task, 1000);
    }

    public interface TimerServiceListener {
        void getTime(int i);
    }

    /**
     * 是否在后台
     *
     * @return
     */
    private boolean isAppOnFreground() {
        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        String curPackageName = getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> app = am.getRunningAppProcesses();
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
    }
}
