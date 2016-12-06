package com.mgjr.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.mgjr.R;

import java.util.Stack;

/**
 * Created by wim on 16/5/31.
 */
public class MyActivityManager {

    public volatile Stack<Activity> activityStack = new Stack<Activity>();
    private static volatile MyActivityManager instance;

    public static MyActivityManager getInstance() {
        if (instance == null) {
            instance = new MyActivityManager();
        }
        return instance;
    }

    public int getCount() {
        if (activityStack.empty()) {
            return 0;
        }
        return activityStack.size();
    }

    public Stack<Activity> getActivityStack() {
        return activityStack;
    }

    public void popActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            finishActivity(activity);
        }
    }

    public int curIndexAtActivity(Activity activity) {
        if (!activityStack.empty() && activityStack.contains(activity)) {
            return activityStack.indexOf(activity);
        }
        return 0;
    }

    public Activity currentActivity() {
        Activity activity = null;
        if (!activityStack.empty()) {
            activity = activityStack.lastElement();
        }
        return activity;
    }

    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        boolean isAdd = true;
        for (Activity temp : activityStack) {
            if (temp.getLocalClassName().equals(activity.getLocalClassName())) {
                isAdd = false;

            }
        }
        if (isAdd) {
            activityStack.add(activity);
        }
    }

//    public void pushActivity(Activity activity){
//        if (activityStack == null){
//            activityStack=new Stack<Activity>();
//        }
//        activityStack.add(activity);
//    }


    public void popOtherActivity(Class cls) {
        if (null == cls) {
            return;
        }

        for (Activity activity : activityStack) {
            if (null == activity || activity.getClass().equals(cls)) {
                continue;
            }

            activity.finish();
        }
    }

    public void popCurrentActivity() {
        Activity activity = null;
        if (!activityStack.empty()) {
            activity = activityStack.lastElement();
            finishActivity(activity);
        }
    }

    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
//            activity.overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
            activity = null;
        }
    }

    public void popAllActivity() {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            activity.finish();
            popActivity(activity);
        }
    }

    public void finishSpecifiedActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    public void startNextActivity(Class<?> activity) {
        Activity curActivity = currentActivity();
        if (null != curActivity) {
            Intent intent = new Intent(curActivity, activity);
            curActivity.startActivity(intent);
            curActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }

    }

    public void startNextActivity(Class<?> activity, String str) {
        Activity curActivity = currentActivity();
        if (curActivity != null) {
            Intent intent = new Intent(curActivity, activity);
            intent.putExtra("code", str);
            curActivity.startActivity(intent);
            curActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }

    }


    public void startNextActivityForResult(Intent intent, int requestCode, Class<?> desActivity) {
        Activity curActivity = currentActivity();
        if (desActivity != null) {
            intent.setClass(curActivity, desActivity);
        }
        curActivity.startActivityForResult(intent, requestCode);
        curActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void startNextActivity(Class<?> activity, Bundle bundle) {
        Activity curActivity = currentActivity();
        if (null != curActivity) {
            Intent intent = new Intent(curActivity, activity);
            intent.putExtras(bundle);
            curActivity.startActivity(intent);
            curActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }

    }


    public void startNextActivity(Class<?> activity, String str, String type) {
        Activity curActivity = currentActivity();
        if (null != curActivity) {
            Intent intent = new Intent(curActivity, activity);
            intent.putExtra("code", str);
            intent.putExtra("type", type);
            curActivity.startActivity(intent);
            curActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }


    }

    public void startNextActivity(Class<?> activity, String str1, String str2, String str3) {
        Activity curActivity = currentActivity();
        if (null != curActivity) {
            Intent intent = new Intent(curActivity, activity);
            intent.putExtra("code", str1);
            intent.putExtra("type", str2);
            intent.putExtra("rate", str3);
            curActivity.startActivity(intent);
            curActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }
    }

    public void startNextActivity(Class<?> activity, String str1, String str2, String str3, String str4) {
        Activity curActivity = currentActivity();
        if (null != curActivity) {
            Intent intent = new Intent(curActivity, activity);
            intent.putExtra("code", str1);
            intent.putExtra("type", str2);
            intent.putExtra("rate", str3);
            intent.putExtra("mode", str4);
            curActivity.startActivity(intent);
            curActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }

    }


}
