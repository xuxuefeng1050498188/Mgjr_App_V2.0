package com.mgjr.mangofinance;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.PhoneUtils;
import com.mgjr.Utils.SPUtils;
import com.mgjr.httpclient.HttpClient;
import com.mgjr.httpclient.callback.Callback;
import com.mgjr.httpclient.callback.FileCallBack;
import com.mgjr.httpclient.request.RequestCall;
import com.mgjr.model.bean.APPVersion;
import com.mgjr.model.bean.BaseBean;
import com.mgjr.model.bean.EventBusBean.ChangeTabBean;
import com.mgjr.model.bean.NoiceBean;
import com.mgjr.presenter.impl.APPUpdatePresenterImpl;
import com.mgjr.presenter.impl.NoticePresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.APIBuilder;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.TabView;
import com.mgjr.share.addresswheel_master.utils.Utils;
import com.mgjr.view.home.HomeFragment;
import com.mgjr.view.invester.InvestFragment;
import com.mgjr.view.listeners.ViewListener;
import com.mgjr.view.profile.ProfileFragment;
import com.mgjr.view.profile.accountsetting.UpgradeDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;


public class MainActivity extends ActionbarActivity implements TabView.TabViewListener, ViewPager.OnPageChangeListener, ViewListener<APPVersion>, UpgradeDialog.ClickListenerInterface {

    private static final int TabHome = 0;
    private static final int TabInvest = 1;
    private static final int TabAccount = 2;

    private TabView tabView;

    private HomeFragment homeFragment;
    private InvestFragment investFragment;
    private ProfileFragment profileFragment;

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private List<Fragment> mTabs = new ArrayList<Fragment>();

    private ViewPager mViewPager;
    private MainViewPagerAdapter adapter;

    private long mExitTime;

    private APPUpdatePresenterImpl updatePresenter;

    private UpgradeDialog upgradeDialog;
    private ProgressDialog progressDialog;

    private String downloadUrl;
    private int versionNum;
    private String name;
    String appName;
    String describe;
    private int is_forcedUpdate;
    private int status;

    private int curTag;

    private int currentVersionCode;

    private RequestCall call;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    private NoticePresenterImpl noticePresenter;
    private int isClose;

    //    private AppIsShowInterface appIsShowInterface;
    private boolean isShow;

    private Handler isShowHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //网上找的解决方法
        mTabs.clear();
        setBaseContent(R.layout.activity_main, this);
        EventBus.getDefault().register(this);
        this.actionBarLayout.setVisibility(View.GONE);

        checkForUpdates();
        setNotice();

        setFragments();
        setTab();
        if (getIntent().getStringExtra("code") != null) {
            int position = Integer.parseInt(getIntent().getStringExtra("code"));
            onPageSelected(position);
        }

//        List<String > list = null;
//        list.add(null);
        sendCrashInfoToServer();
        PhoneUtils.sendSystemErrorInfoToServer();

    }


    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onEvent(this, "banner");
    }

    private void checkForUpdates() {
        currentVersionCode = Utils.getVersionCode(MainApplication.getContext());

        String name = getVersion();

        String channel = Utils.getChannelCode(this);
        Map<String, String> unNecessaryParams = new HashMap<>();
        unNecessaryParams.put("channel", channel);

        updatePresenter = new APPUpdatePresenterImpl(this);
        updatePresenter.sendRequest(null, unNecessaryParams);

        upgradeDialog = new UpgradeDialog(this);
        upgradeDialog.setClicklistener(this);
        upgradeDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                    return true;
                } else {
                    return true;
                }
            }
        });


        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("下载进度");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                    return true;
                } else {
                    return true;
                }
            }
        });
    }

    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return this.getString(R.string.can_not_find_version_name);
        }
    }

    private void sendCrashInfoToServer() {
        String crashInfo = (String) SPUtils.get(MainApplication.getContext(), "CRASH_INFO", "CrashInfo", "");
        if (!TextUtils.isEmpty(crashInfo)) {
            String describe = (String) SPUtils.get(MainApplication.getContext(), "CRASH_INFO", "describe", "");
            String client_type = (String) SPUtils.get(MainApplication.getContext(), "CRASH_INFO", "client_type", "");
            String mobile_device = (String) SPUtils.get(MainApplication.getContext(), "CRASH_INFO", "mobile_device", "");
            String mobile_sys_version = (String) SPUtils.get(MainApplication.getContext(), "CRASH_INFO", "mobile_sys_version", "");
            String version = (String) SPUtils.get(MainApplication.getContext(), "CRASH_INFO", "version", "");

            String url = APIBuilder.baseUrl + APIBuilder.version + "appHome/saveAppBugCollect";
            //请求网络
            Map<String, String> necessaryParams = new HashMap<String, String>();

            necessaryParams.put("describe", describe);
            necessaryParams.put("client_type", client_type);
            necessaryParams.put("mobile_device", mobile_device);
            necessaryParams.put("mobile_sys_version", mobile_sys_version);
            necessaryParams.put("version", version);

            Map<String, String> unNecessaryParams = new HashMap<String, String>();
            unNecessaryParams.put("name", "");
            unNecessaryParams.put("reason", "");

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

                        }

                        @Override
                        public void onResponse(Object response) {
                            BaseBean baseBean = (BaseBean) response;
                            if (baseBean.getStatus().equalsIgnoreCase("0000")) {
                                //上传成功
                                SPUtils.clear(MainActivity.this, "CRASH_INFO");
                            } else {
                                CommonToastUtils.showToast(MainActivity.this, baseBean.getMsg());
                            }
                        }
                    });
        }
    }


    private void setNotice() {
        noticePresenter = new NoticePresenterImpl(new ViewListener<NoiceBean>() {
            @Override
            public void showLoading() {

            }

            @Override
            public void hideLoading() {

            }

            @Override
            public void showError() {

            }

            @Override
            public void showError(OnPresenterListener listener, NoiceBean noiceBean) {
            }

            @Override
            public void responseData(OnPresenterListener listener, NoiceBean noiceBean) {
                if (noiceBean.getType() != 0) {
                    isClose = noiceBean.getIsClose();
                    String content = noiceBean.getContext();
                    String title = noiceBean.getTitle();
                    upgradeDialog.show();
                    if (isClose == 0) {
                        upgradeDialog.cancelButton.setVisibility(View.VISIBLE);
                        upgradeDialog.sureButton.setVisibility(View.GONE);

                    } else {
                        upgradeDialog.line.setVisibility(View.GONE);
                        upgradeDialog.cancelButton.setVisibility(View.GONE);
                        upgradeDialog.sureButton.setVisibility(View.GONE);
                    }
                    upgradeDialog.titleTextView.setText(title);
                    upgradeDialog.contentTextView.setText(content);
                }


            }
        });
        noticePresenter.sendRequest();
    }

    @Override
    protected void onStart() {
        super.onStart();
        float fontScale = this.getResources().getDisplayMetrics().scaledDensity;
        float scale = this.getResources().getDisplayMetrics().density;

    }

    public void setTab() {
        tabView = (TabView) findViewById(R.id.tabVview);
        tabView.setListener(this);

        Map<String, Object> first = new HashMap<>();
        first.put(TabView.NORMAL_IMAGE, R.drawable.homepage);
        first.put(TabView.SELECTED_IMAGE, R.drawable.btn_homepage);
        first.put(TabView.TITLE, "精选");

        Map<String, Object> second = new HashMap<>();
        second.put(TabView.NORMAL_IMAGE, R.drawable.invest);
        second.put(TabView.SELECTED_IMAGE, R.drawable.btn_invest);
        second.put(TabView.TITLE, "理财");

        Map<String, Object> third = new HashMap<>();
        third.put(TabView.NORMAL_IMAGE, R.drawable.account);
        third.put(TabView.SELECTED_IMAGE, R.drawable.btn_account);
        third.put(TabView.TITLE, "我");

        List<Map<String, Object>> list = new ArrayList<>();
        list.add(first);
        list.add(second);
        list.add(third);

        tabView.setup(list);

    }


    public void setFragments() {
        fragmentManager = getSupportFragmentManager();

        homeFragment = new HomeFragment();
        investFragment = new InvestFragment();
        profileFragment = new ProfileFragment();

        mTabs.add(homeFragment);
        mTabs.add(investFragment);
        mTabs.add(profileFragment);

        adapter = new MainViewPagerAdapter(getSupportFragmentManager(), mTabs);

        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);

        transaction = fragmentManager.beginTransaction();
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void eventBus(ChangeTabBean event) {
        int tag = event.getTag();
        onPageSelected(tag);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().removeAllStickyEvents();

        SharedPreferences preferences = this.getSharedPreferences("first_in",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().apply();
    }

    @Override
    public void onTabClick(View v) {
        int tag = Integer.parseInt(v.getTag().toString());
        if (tag == curTag) {
            return;
        }
        curTag = tag;
        mViewPager.setCurrentItem(tag);

        changeTab(tag);
    }

    private void changeTab(int tag) {
        curTag = tag;
        transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        if (tag == TabHome) {
            if (homeFragment == null) {
                homeFragment = new HomeFragment();
                transaction.add(R.id.id_viewpager, homeFragment);
            } else {

                transaction.show(homeFragment);
            }
//            SharedPreferences preferences = MainApplication.getContext().getSharedPreferences("first_in",
//                    MODE_PRIVATE);
//            boolean isFirstIn = preferences.getBoolean("isFirstIn", true);
//            if (isFirstIn) {
//                homeFragment.setPupWindow();
//            }
//            homeFragment.checkMemberType();

        } else if (tag == TabInvest) {
            if (investFragment == null) {
                investFragment = new InvestFragment();
                transaction.add(R.id.id_viewpager, investFragment);
            } else {

                transaction.show(investFragment);
            }
        } else if (tag == TabAccount) {
            if (profileFragment == null) {
                profileFragment = new ProfileFragment();
                transaction.add(R.id.id_viewpager, profileFragment);
            } else {
                transaction.show(profileFragment);
            }

        }
        transaction.commit();
//        transaction.commitAllowingStateLoss();

        this.changeTabContent(tag);
    }

    private void hideFragments(FragmentTransaction transaction) {
/*

        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (investFragment != null) {
            transaction.hide(investFragment);
        }
        if (profileFragment != null) {
            transaction.hide(profileFragment);
        }
*/

    }


    private void changeTabContent(int tag) {
        if (tag == TabHome) {
            this.actionbar.setCenterTextView("首页");

        } else if (tag == TabInvest) {
            this.actionbar.setCenterTextView("理财");
        } else if (tag == TabAccount) {

        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (tabView != null) {

            tabView.selectedAtIndex(position);
        }
        if (position == 0) {

            SharedPreferences preferences = MainApplication.getContext().getSharedPreferences("first_in",
                    MODE_PRIVATE);
            boolean isFirstIn = preferences.getBoolean("isFirstIn", true);
            if (isFirstIn) {
                homeFragment.setPupWindow();
            }

        }
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            MobclickAgent.onKillProcess(this);
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                CommonToastUtils.showToast(this, "再按一次退出程序");
                mExitTime = System.currentTimeMillis();

            } else {
                if (is_forcedUpdate != 1) {
                    MyActivityManager.getInstance().finishActivity(this);
                }

            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
//                && event.getAction() == KeyEvent.ACTION_DOWN
//                && event.getRepeatCount() == 0) {
//            //具体的操作代码
//            if (is_forcedUpdate == 1) {
//                return false;
//            }
//        }
//        return super.dispatchKeyEvent(event);
//    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showError(OnPresenterListener listener, APPVersion appVersion) {
        Log.d("ss==", String.valueOf(appVersion));
    }

    @Override
    public void responseData(OnPresenterListener listener, APPVersion appVersion) {
        if (appVersion.getAppVersion() != null) {
            versionNum = appVersion.getAppVersion().getVersionNum();
            downloadUrl = appVersion.getAppVersion().getApk();
            appName = appVersion.getAppVersion().getAppName();
            describe = appVersion.getAppVersion().getDescribe();
            is_forcedUpdate = appVersion.getAppVersion().getIs_forcedUpdate();

            status = appVersion.getAppVersion().getStatus();
            if (status == 0) {
                if (downloadUrl != null) {

                    call = HttpClient.get().url(downloadUrl).build();
                }

                if (currentVersionCode < versionNum) {
                    upgradeDialog.show();
                    if (is_forcedUpdate == 2) {
                        upgradeDialog.setForceupdate(false);
                        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        call.cancel();
                                        dialog.dismiss();
                                    }
                                });
                    } else {
                        upgradeDialog.setForceupdate(true);
                    }
                }
                upgradeDialog.titleTextView.setText(appName);
                upgradeDialog.contentTextView.setText(describe);
            }


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
        if (call != null) {
            call.execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), name) {
                @Override
                public void inProgress(float progress, long total) {
                    int count = (int) (progress * 100);

                    progressDialog.setProgress(count);
                }

                @Override
                public void onError(Call call, Exception e) {
                }

                @Override
                public void onResponse(File response) {
                    openFile(response);
                }
            });
        }
    }

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


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }


}
