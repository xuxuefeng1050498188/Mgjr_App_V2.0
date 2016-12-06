package com.mgjr.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import com.mgjr.httpclient.HttpClient;
import com.mgjr.mangofinance.MainActivity;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.share.APIBuilder;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import okhttp3.Response;

/**
 * Created by wim on 16/8/24.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";

    private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().toString();

    private static CrashHandler instance = new CrashHandler();
    private Context mContext;

    private CrashHandler() {
    }

    private String filePath;

    public static CrashHandler getInstance() {
        return instance;
    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Map<String, String> crashInfo = obtainDeviceInfo(mContext, ex);

        saveCrashInfoToSp(crashInfo);
        saveToFile(crashInfo);
        showToast(mContext, "很抱歉，程序遭遇异常，即将退出！");

        try {
            thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        ExitAppUtils.getInstance().exit();
        restartApp();//发生崩溃异常时,重启应用
    }

    private void saveCrashInfoToSp(Map<String, String> crashInfo) {
        SPUtils.put(MainApplication.getContext(), "CRASH_INFO", "CrashInfo", crashInfo.toString());
        SPUtils.put(MainApplication.getContext(), "CRASH_INFO", "describe", crashInfo.get("describe"));
        SPUtils.put(MainApplication.getContext(), "CRASH_INFO", "client_type", crashInfo.get("client_type"));
        SPUtils.put(MainApplication.getContext(), "CRASH_INFO", "mobile_device", crashInfo.get("mobile_device"));
        SPUtils.put(MainApplication.getContext(), "CRASH_INFO", "mobile_sys_version", crashInfo.get("mobile_sys_version"));
        SPUtils.put(MainApplication.getContext(), "CRASH_INFO", "version", crashInfo.get("version"));
    }


    public void restartApp() {
        Intent intent = new Intent(MainApplication.getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MainApplication.getContext().startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
    }

    private void showToast(final Context context, final String msg) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }).start();
    }

    public void setCrashHandler(Context context) {
        mContext = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
//		filePath = SDCARD_ROOT + File.separator + "crash" + File.separator+ "crash.txt";
        filePath = mContext.getExternalCacheDir() + File.separator + "crashs.txt";
    }

    private HashMap<String, String> obtainDeviceInfo(Context context, Throwable ex) {
        HashMap<String, String> map = new HashMap<String, String>();
        PackageManager mPackageManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = mPackageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
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

        String content = obtainExceptionInfo(ex);
        map.put("describe", content);
        map.put("client_type", "Andoird");
        map.put("mobile_device", manufacturer + model);
//		map.put("time",getTime());

        map.put("name", "");
        map.put("reason", "");
        return map;
    }

    private String obtainExceptionInfo(Throwable throwable) {
        StringWriter mStringWriter = new StringWriter();
        PrintWriter mPrintWriter = new PrintWriter(mStringWriter);
        throwable.printStackTrace(mPrintWriter);
        mPrintWriter.close();

        return mStringWriter.toString();
    }

    private String getTime() {
        System.setProperty("user.timezone", "Asia/Shanghai");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String times = format.format(new Date());

        return times;
    }

    private void saveToFile(Map<String, String> info) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                boolean isCreate = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedWriter.write("时间:" + info.get("time") + "\r\n");
            bufferedWriter.write("model:" + info.get("model") + "\r\n");
            bufferedWriter.write("sdk:" + info.get("sdk") + "\r\n");
            bufferedWriter.write("manufacturer:" + info.get("manufacturer") + "\r\n");
            bufferedWriter.write("versionName:" + info.get("versionName") + "\r\n");
            bufferedWriter.write("versionCode:" + info.get("versionCode") + "\r\n");
            bufferedWriter.write("content:" + info.get("content") + "\r\n");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void getCrashFile() {
        File file = new File(filePath);
        if (file.exists()) {
            FileInputStream inStream = null;
            try {
                inStream = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                try {
                    while ((len = inStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        outStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                try {
                    String content = outStream.toString("utf8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    public void clear() {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}