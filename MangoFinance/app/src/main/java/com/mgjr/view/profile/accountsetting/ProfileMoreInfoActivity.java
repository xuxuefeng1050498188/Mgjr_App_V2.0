package com.mgjr.view.profile.accountsetting;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.httpclient.HttpClient;
import com.mgjr.httpclient.callback.FileCallBack;
import com.mgjr.httpclient.request.RequestCall;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.model.bean.APPVersion;
import com.mgjr.presenter.impl.APPUpdatePresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.addresswheel_master.utils.Utils;
import com.mgjr.view.common.CommonWebViewActivity;
import com.mgjr.view.listeners.ViewListener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import static com.mgjr.R.id.update_textview;

public class ProfileMoreInfoActivity extends ActionbarActivity implements ViewListener<APPVersion>, UpgradeDialog.ClickListenerInterface {

    private APPUpdatePresenterImpl updatePresenter;

    private int currentVersionCode;
    private String currentVersionName;
    private String downloadUrl;
    private int versionNum;
    private String name;
    String appName;
    String describe;

    private ImageView redImageView;
    private TextView updateTextView;

    private UpgradeDialog upgradeDialog;
    private ProgressDialog progressDialog;

    private RequestCall call;

    private Map<String, String> unNecessaryParams;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_profile_more_info, this);
        actionbar.setCenterTextView("更多");

        redImageView = (ImageView) findViewById(R.id.red_imageview);
        updateTextView = (TextView) findViewById(update_textview);


        String channel = Utils.getChannelCode(this);
        unNecessaryParams = new HashMap<>();
        unNecessaryParams.put("channel", channel);


        updatePresenter = new APPUpdatePresenterImpl(this);
        upgradeDialog = new UpgradeDialog(this);
        upgradeDialog.setClicklistener(this);


        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("下载进度");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        call.cancel();
                        dialog.dismiss();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentVersionCode = Utils.getVersionCode(this);
        currentVersionName = Utils.getVersionName(this);
        updatePresenter.sendRequest(null, unNecessaryParams);

    }

//    public void layout_feedback(View view){
//        MyActivityManager.getInstance().startNextActivity(ProfileFeedBackActivity.class);
//    }


    public void contact_us(View view) {
        MyActivityManager.getInstance().startNextActivity(CommonWebViewActivity.class, "contactUs");
    }

    public void checkUpdate(View view) {
        if (currentVersionCode < versionNum) {

            upgradeDialog.show();
            upgradeDialog.setForceupdate(false);
            upgradeDialog.setContent(appName, describe);

        } else {
            CommonToastUtils.showToast(MainApplication.getContext(), "已是最新版本");
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
    public void showError(OnPresenterListener listener, APPVersion appVersion) {
        CommonToastUtils.showToast(MainApplication.getContext(), appVersion.getMsg());
    }

    @Override
    public void responseData(OnPresenterListener listener, APPVersion appVersion) {
        versionNum = appVersion.getAppVersion().getVersionNum();
        downloadUrl = appVersion.getAppVersion().getApk();
        appName = appVersion.getAppVersion().getAppName();
        describe = appVersion.getAppVersion().getDescribe();
        upgradeDialog.setContent(appName, describe);

        call = HttpClient.get().url(downloadUrl).build();

        if (currentVersionCode < versionNum) {
            redImageView.setVisibility(View.VISIBLE);
            updateTextView.setVisibility(View.VISIBLE);
        } else {
            redImageView.setVisibility(View.GONE);
            updateTextView.setVisibility(View.VISIBLE);
            updateTextView.setText("已是最新版本");
        }

    }

    @Override
    public void onSure() {
        name = "mangofinance" + versionNum + ".apk";
        upgradeDialog.dismiss();

        progressDialog.setProgress(59);
        progressDialog.show();


        if (Build.VERSION.SDK_INT >= 23) {
            int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );
                return;
            } else {
                downLoad();
            }
        } else {
            downLoad();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission Granted
            downLoad();
        } else {
            CommonToastUtils.showToast(this, "更新失败");
            // Permission Denied
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    public void downLoad() {
        call.execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), name) {
            @Override
            public void inProgress(float progress, long total) {
                int count = (int) (progress * 100);

                progressDialog.setProgress(count);
            }

            @Override
            public void onError(Call call, Exception e) {
                Log.d("err==", String.valueOf(e.getMessage()));
            }

            @Override
            public void onResponse(File response) {
                Log.d("done===", String.valueOf(response.getAbsolutePath()));
                openFile(response);
            }
        });
    }

//    public void downLoad(){
//        //取得系统的下载服务
//        String name = "mangofinance_"+ versionNum+".apk";
//
//        DownloadManager downloadManager= (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//        String downUrl= downloadUrl;
//        //创建下载请求对象
//        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(downUrl));
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,name);
//        request.allowScanningByMediaScanner();
//        request.setVisibleInDownloadsUi(true);
//        request.setNotificationVisibility(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        long refernece = downloadManager.enqueue(request);
//        SharedPreferences sPreferences = getSharedPreferences("downloadapk", 0);
//
//        sPreferences.edit().putLong("plato", refernece).commit();
//    }

    private void openFile(File file) {
        Log.e("OpenFile", file.getName());
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

    @Override
    public void onCancel() {
        upgradeDialog.dismiss();
    }
}
