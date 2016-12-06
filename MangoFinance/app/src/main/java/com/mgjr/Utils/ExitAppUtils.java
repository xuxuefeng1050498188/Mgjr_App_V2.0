package com.mgjr.Utils;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by wim on 16/8/29.
 */
public class ExitAppUtils {
    private List<Activity> mActivityList = new LinkedList<Activity>();
    private static ExitAppUtils instance = new ExitAppUtils();


    private ExitAppUtils(){};


    public static ExitAppUtils getInstance(){
        return instance;
    }


    public void addActivity(Activity activity){
        mActivityList.add(activity);
    }


    public void delActivity(Activity activity){
        mActivityList.remove(activity);
    }


    public void exit(){
        for(Activity activity : mActivityList){
            activity.finish();
        }

        System.exit(0);
    }
}
