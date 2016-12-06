package com.mgjr.Utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgjr.R;
import com.mgjr.httpclient.HttpClient;
import com.mgjr.httpclient.callback.Callback;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.model.bean.BaseBean;
import com.mgjr.share.APIBuilder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

import static com.mgjr.Utils.LogUtil._FILE_;
import static com.mgjr.Utils.LogUtil._TIME_;
import static com.mgjr.Utils.LogUtil.getFileLineMethod;

/**
 * Created by Administrator on 2016/10/31.
 */

public class PhoneUtils {

    public static String systemErrorReason = "";

    public static String getSystemErrorReason() {
        return systemErrorReason;
    }

    public static void setSystemErrorReason(String systemErrorReason) {
        PhoneUtils.systemErrorReason = _TIME_() + "  " + _FILE_() + " : " + getFileLineMethod() + systemErrorReason;
    }

    public static String getVersion() {
        try {
            PackageManager manager = MainApplication.getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(MainApplication.getContext().getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return MainApplication.getContext().getString(R.string.can_not_find_version_name);
        }
    }


    public static HashMap<String, String> obtainDeviceInfo() {
        HashMap<String, String> map = new HashMap<String, String>();
        PackageManager mPackageManager = MainApplication.getContext().getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = mPackageManager.getPackageInfo(MainApplication.getContext().getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

//		map.put("versionName", mPackageInfo.versionName);
        map.put("version", "" + mPackageInfo.versionCode);

        String model = Build.MODEL;
        int sdk_int = Build.VERSION.SDK_INT;
        String manufacturer = Build.MANUFACTURER;

//		map.put("model", model);
        map.put("mobile_sys_version", String.valueOf(sdk_int));
//		map.put("manufacturer", manufacturer);

        map.put("client_type", "Andoird");
        map.put("mobile_device", manufacturer + model);
//		map.put("time",getTime());

//        map.put("name", "");
//        map.put("reason", "");
        return map;
    }

    public static void saveSystemErrorInfo(Throwable throwable) {
        //保存是否是连接超时
        /*if (throwable.getCause() != null && throwable.getCause().equals(SocketTimeoutException.class)) {
            SPUtils.put(MainApplication.getContext(), "SocketTimeoutException", "isSocketTimeoutException", true);
        }*/
        if (throwable.toString().contains("SocketTimeoutException")) {
            SPUtils.put(MainApplication.getContext(), "SocketTimeoutException", "isSocketTimeoutException", true);
        }
        HashMap<String, String> deviceInfo = obtainDeviceInfo();
        if (null != deviceInfo) {
            String describe = obtainExceptionInfo(throwable);
            String client_type = deviceInfo.get("client_type");
            String mobile_device = deviceInfo.get("mobile_device");
            String mobile_sys_version = deviceInfo.get("mobile_sys_version");
            String version = deviceInfo.get("version");
            SPUtils.put(MainApplication.getContext(), "SYSTEM_ERROR_INFO", "describe", describe);
            SPUtils.put(MainApplication.getContext(), "SYSTEM_ERROR_INFO", "client_type", client_type);
            SPUtils.put(MainApplication.getContext(), "SYSTEM_ERROR_INFO", "mobile_device", mobile_device);
            SPUtils.put(MainApplication.getContext(), "SYSTEM_ERROR_INFO", "mobile_sys_version", mobile_sys_version);
            SPUtils.put(MainApplication.getContext(), "SYSTEM_ERROR_INFO", "version", version);
            SPUtils.put(MainApplication.getContext(), "SYSTEM_ERROR_INFO", "reason", systemErrorReason);
            SPUtils.put(MainApplication.getContext(), "SYSTEM_ERROR_INFO", "time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        }
    }


    public static void sendSystemErrorInfoToServer() {
        String describe = (String) SPUtils.get(MainApplication.getContext(), "SYSTEM_ERROR_INFO", "describe", "");
        String client_type = (String) SPUtils.get(MainApplication.getContext(), "SYSTEM_ERROR_INFO", "client_type", "");
        String mobile_device = (String) SPUtils.get(MainApplication.getContext(), "SYSTEM_ERROR_INFO", "mobile_device", "");
        String mobile_sys_version = (String) SPUtils.get(MainApplication.getContext(), "SYSTEM_ERROR_INFO", "mobile_sys_version", "");
        String version = (String) SPUtils.get(MainApplication.getContext(), "SYSTEM_ERROR_INFO", "version", "");
        String time = (String) SPUtils.get(MainApplication.getContext(), "SYSTEM_ERROR_INFO", "time", "");
        String reason = (String) SPUtils.get(MainApplication.getContext(), "SYSTEM_ERROR_INFO", "reason", "");
        if (!TextUtils.isEmpty(describe)) {

            String url = APIBuilder.baseUrl + APIBuilder.version + "appHome/saveAppBugCollect";
            //请求网络
            Map<String, String> necessaryParams = new HashMap<String, String>();

            necessaryParams.put("describe", time + "    " + describe);
            necessaryParams.put("client_type", client_type);
            necessaryParams.put("mobile_device", mobile_device);
            necessaryParams.put("mobile_sys_version", mobile_sys_version);
            necessaryParams.put("version", version);

            Map<String, String> unNecessaryParams = new HashMap<String, String>();
            unNecessaryParams.put("name", "ANDROID系统错误");
            unNecessaryParams.put("reason", reason);

            Map<String, String> params = new HashMap<String, String>();
            params.putAll(necessaryParams);
            if (unNecessaryParams != null) {
                params.putAll(unNecessaryParams);
            }
            HttpClient
                    .post()
                    .url(url)
                    .params(params)
                    .build()
                    .execute(new Callback() {
                        @Override
                        public Object parseResponse(Response response) throws Exception {
                            String string = response.body().string();
                            Gson gson = new GsonBuilder().create();
                            BaseBean baseBean = gson.fromJson(string, BaseBean.class);
                            return baseBean;
                        }

                        @Override
                        public void onError(Call call, Exception e) {
//                            CommonToastUtils.showToast(MainApplication.getContext(), "系统错误信息上传失败!");
                        }

                        @Override
                        public void onResponse(Object response) {
                            BaseBean baseBean = (BaseBean) response;
                            if (baseBean.getStatus().equalsIgnoreCase("0000")) {
                                //上传成功
                                SPUtils.clear(MainApplication.getContext(), "SYSTEM_ERROR_INFO");
                            } else {
//                                CommonToastUtils.showToast(MainApplication.getContext(), baseBean.getMsg());
                            }
                        }
                    });
        }
    }

    private static String obtainExceptionInfo(Throwable throwable) {
        StringWriter mStringWriter = new StringWriter();
        PrintWriter mPrintWriter = new PrintWriter(mStringWriter);
        throwable.printStackTrace(mPrintWriter);
        mPrintWriter.close();

        return mStringWriter.toString();
    }

}
