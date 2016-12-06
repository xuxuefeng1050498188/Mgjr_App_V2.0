package com.mgjr.Utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * Created by xuxuefeng on 2016/10/26.
 */

public class ApplicationInfoUtils {
    /* 获取application中指定的meta-data
    * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
    */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = String.valueOf(applicationInfo.metaData.getInt(key));
                    }
                }


            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        return resultData;
    }
}
