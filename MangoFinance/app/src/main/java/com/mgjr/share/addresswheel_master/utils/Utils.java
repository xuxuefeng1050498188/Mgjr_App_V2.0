package com.mgjr.share.addresswheel_master.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.mgjr.R;

import java.io.InputStream;

/**
 * Created by xiangzhihong on 2016/4/8 on 11:50.
 */
public class Utils {

    public static void hideKeyBoard(Activity context) {
        if (context != null && context.getCurrentFocus() != null) {
            ((InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(context.getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }



    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static String readAssert(Context context,  String fileName){
        String jsonString="";
        String resultString="";
        try {
            InputStream inputStream=context.getResources().getAssets().open(fileName);
            byte[] buffer=new byte[inputStream.available()];
            inputStream.read(buffer);
            resultString=new String(buffer,"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            int  versionCode = info.versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return 0;
    }

    public static String  getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String  versionCode = info.versionName;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public static int pxToDip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String getChannelCode(Context context) {

        String code = getMetaData(context, "CHANNEL");

        if (code != null) {

            return code;

        }

        return "11";

    }

    private static String getMetaData(Context context, String key) {

        try {

            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(

                    context.getPackageName(), PackageManager.GET_META_DATA);

            Object value = ai.metaData.get(key);

            if (value != null) {

                return value.toString();

            }

        } catch (Exception e) {

        }
        return null;

    }
}
