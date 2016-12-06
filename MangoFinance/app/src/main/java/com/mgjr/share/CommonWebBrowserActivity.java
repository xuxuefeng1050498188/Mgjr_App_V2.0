package com.mgjr.share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.SPUtils;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.model.bean.EventBusBean.ChangeTabBean;
import com.mgjr.model.bean.EventsBean;
import com.mgjr.model.bean.HomepageRecommendProjectsBean;
import com.mgjr.view.common.LoginActivity;
import com.mgjr.view.profile.activityzone.ProfileEventsListActivity;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;
import java.util.Set;


/**
 * Created by Administrator on 2016/10/13.
 */

public class CommonWebBrowserActivity extends ActionbarActivity implements View.OnClickListener, SocialDialog.ClickListenerInterface, IUiListener {
    private WebView webView;
    private Map jumpParams;
    private String url;
    private SocialDialog socialDialog;
    private SocialClient socialClient;
    private String shareTitle;
    private String shareContent;
    private String shareUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_layout_commonbrowser, this);
        shareTitle = getIntent().getStringExtra("code");
        shareContent = getIntent().getStringExtra("type");
        shareUrl = getIntent().getStringExtra("rate");
        if (shareTitle != null) {
            actionbar.setCenterTextView(shareTitle);
            actionbar.centerTextView.setTextSize(16);
        } else {
            actionbar.setCenterTextView("活动详情");
        }

        actionbar.setRightImageView(R.drawable.wv_share_logo, this);
        socialClient = new SocialClient(this);
        initWebView();
        EventBus.getDefault().register(this);
    }

    private void initWebView() {
        webView = (WebView) findViewById(R.id.wv_browser);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void eventBus(HomepageRecommendProjectsBean.AppBannersBean event) {
        url = event.getTo_url();
        jumpParams = event.getJumpParams();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void eventBus(HomepageRecommendProjectsBean.Posters event) {
        url = event.getTo_url();
        jumpParams = event.getJumpParams();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void eventBus(EventsBean.ActivityListBean event) {
        url = event.getActivity_url();
        jumpParams = event.getJumpParams();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (url != null) {
            setWebViewData();
        }
    }

    private void setWebViewData() {

        final int mid = (int) SPUtils.get(MainApplication.getContext(), "LOGIN", "id", -1);
        if (!url.contains(String.valueOf(mid)) && mid != -1) {
            url = url + "?mid=" + mid;
        }
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoadingDialog();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dismissLoadingDialog();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!TextUtils.isEmpty(url) && (jumpParams != null)) {
                    Set<String> bannerUrlSet = jumpParams.keySet();
                    for (String urlStr : bannerUrlSet) {
                        if (url.contains(urlStr)) {
                            //跳转app的界面
                            Map map = (Map) jumpParams.get(urlStr);
                            String activityName = (String) map.get("class");
                            int tag = -1;
                            if (map.containsKey("tag")) {
                                tag = Integer.parseInt((String) map.get("tag"));
                            }
                            String code = "";
                            if (map.containsKey("code")) {
                                code = (String) map.get("code");
                            }
                            if (activityName.contains("MainActivity")) {
                                ChangeTabBean changeTabBean = new ChangeTabBean();
                                changeTabBean.setTag(tag);
                                EventBus.getDefault().postSticky(changeTabBean);
                                MyActivityManager.getInstance().finishSpecifiedActivity(CommonWebBrowserActivity.class);
                                MyActivityManager.getInstance().finishSpecifiedActivity(ProfileEventsListActivity.class);
                            } else if (activityName.contains("RegisterActivityStepOne") || activityName.contains("LoginActivity") || activityName.contains("InvestProductDetailActivity")) {
                                jumpToSpecifiedActivity(activityName, tag, code);
                            } else {
                                if (mid == -1) {
                                    //未登录
                                    MyActivityManager.getInstance().startNextActivity(LoginActivity.class);
                                } else {
                                    //已登录
                                    jumpToSpecifiedActivity(activityName, tag, code);
                                }
                            }
                        } else if (url.startsWith("tel:")) {
                            //打电话
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                        }
                    }
                }
                return true;
            }
        });
    }

    private void jumpToSpecifiedActivity(String activityName, int tag, String code) {
        try {
            MyActivityManager.getInstance().startNextActivity(Class.forName(activityName), code, String.valueOf(tag));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().removeAllStickyEvents();
    }

    @Override
    public void onClick(View v) {
        socialDialog = new SocialDialog(CommonWebBrowserActivity.this);
        socialDialog.setClicklistener(CommonWebBrowserActivity.this);
        socialDialog.show();
    }


    @Override
    public void touchWeChatEvent() {
        if (socialClient.wxAPI.isWXAppInstalled()) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mango_logo);
            socialClient.setWebMediaMessage(shareTitle, shareContent, bitmap, shareUrl).sendRequestToWX(false);
        } else {
            CommonToastUtils.showToast(this, "请安装微信");
        }

    }

    @Override
    public void touchWeFriendEvent() {
        if (socialClient.wxAPI.isWXAppInstalled()) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mango_logo);
            socialClient.setWebMediaMessage(shareTitle, shareContent, bitmap, shareUrl).sendRequestToWX(true);
        } else {
            CommonToastUtils.showToast(this, "请安装微信");
        }

    }

    @Override
    public void touchQQEvent() {
        String imageUrl = "http://192.168.30.5/static/mg/image/wx.png";
        Bundle bundle = socialClient.setWebQQMessage(shareTitle, shareContent, imageUrl, shareUrl);
        socialClient.getTencent().shareToQQ(this, bundle, this);
    }

    @Override
    public void touchWeiboEvent() {

        if (socialClient.mWeiboShareAPI.isWeiboAppInstalled()) {
//            socialClient.setWebWithSina(shareTitle, shareContent, bitmap, shareUrl).sendRequestToSina(this);
            Bundle bundle = new Bundle();
            bundle.putString("title", shareTitle);
            bundle.putString("content", shareContent);
            bundle.putString("url", shareUrl);
            MyActivityManager.getInstance().startNextActivity(WeiboShareActivity.class, bundle);

        } else {
            CommonToastUtils.showToast(this, "请安装微博");
        }
    }

    @Override
    public void touchCancelEvent() {
        socialDialog.dismiss();
    }

    @Override
    public void onComplete(Object o) {
        Toast.makeText(this, R.string.social_share_success, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(UiError uiError) {
        Toast.makeText(this,
                getString(R.string.social_share_failed) + "Error Message: " + uiError.errorMessage,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCancel() {
        Toast.makeText(this, R.string.social_share_canceled, Toast.LENGTH_LONG).show();
    }
}
