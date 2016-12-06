package com.mgjr.view.common;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.mgjr.R;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.share.APIBuilder;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.SocialClient;

/**
 * Created by Administrator on 2016/10/12.
 */

public class CommonWebViewActivity extends ActionbarActivity implements View.OnClickListener {
    private WebView wv;
    private Button btn_open_weixin;
    private String intentUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_layout_commonwebview, this);
        btn_open_weixin = (Button) findViewById(R.id.btn_open_weixin);
        initViews();
        actionbar.leftImageView.setOnClickListener(this);
        actionbar.centerTextView.setTextSize(16);
    }

    private void initViews() {
        String url = "";
        wv = (WebView) findViewById(R.id.wv_common);
        intentUrl = getIntent().getStringExtra("code");
        if (intentUrl.equalsIgnoreCase("experienceGoldProblem")) {
            actionbar.setCenterTextView("关于芒果体验金");
            url = APIBuilder.H5Url + APIBuilder.version + APIBuilder.experienceGoldProblem();
            initWebView(url);
        } else if (intentUrl.equalsIgnoreCase("hqbProblem")) {
            actionbar.setCenterTextView("关于活期宝");
            url = APIBuilder.H5Url + APIBuilder.version + APIBuilder.hqbProblem();
            initWebView(url);
        } else if (intentUrl.equalsIgnoreCase("jmgNoviceProblem")) {
            actionbar.setCenterTextView("关于金芒果");
            url = APIBuilder.H5Url + APIBuilder.version + APIBuilder.jmgNoviceProblem();
            initWebView(url);
        } else if (intentUrl.equalsIgnoreCase("boxProblem")) {
            actionbar.setCenterTextView("关于芒果宝盒");
            url = APIBuilder.H5Url + APIBuilder.version + APIBuilder.boxProblem();
            initWebView(url);
        } else if (intentUrl.equalsIgnoreCase("cashProblem")) {
            actionbar.setCenterTextView("关于提现");
            url = APIBuilder.H5Url + APIBuilder.version + APIBuilder.cashProblem();
            initWebView(url);
        } else if (intentUrl.equalsIgnoreCase("paycheckProblem")) {
            actionbar.setCenterTextView("关于充值");
            url = APIBuilder.H5Url + APIBuilder.version + APIBuilder.paycheckProblem();
            initWebView(url);
        } else if (intentUrl.equalsIgnoreCase("contactUs")) {
            actionbar.setCenterTextView("联系我们");
            url = APIBuilder.H5Url + APIBuilder.version + APIBuilder.contactUs();
            initWebView(url);
        } else if (intentUrl.equalsIgnoreCase("newscomerIntroduce")) {
            actionbar.setCenterTextView("新手福利标产品介绍");
            url = APIBuilder.H5Url + APIBuilder.version + APIBuilder.newscomerIntroduce();
            initWebView(url);
        }else if (intentUrl.equalsIgnoreCase("projectIntroduce")) {
            actionbar.setCenterTextView("金芒果项目介绍");
            url = APIBuilder.H5Url + APIBuilder.version + APIBuilder.projectIntroduce();
            initWebView(url);
        } else if (intentUrl.equalsIgnoreCase("hqbIntroduce")) {
            actionbar.setCenterTextView("活期宝项目介绍");
            url = APIBuilder.H5Url + APIBuilder.version + APIBuilder.hqbIntroduce();
            initWebView(url);
        } else if (intentUrl.equalsIgnoreCase("bankCardProblem")) {
            actionbar.setCenterTextView("关于银行卡");
            url = APIBuilder.H5Url + APIBuilder.version + APIBuilder.bankCardProblem();
            initWebView(url);
        } else if (intentUrl.equalsIgnoreCase("investmentProblem")) {
            actionbar.setCenterTextView("关于投资");
            url = APIBuilder.H5Url + APIBuilder.version + APIBuilder.investmentProblem();
            initWebView(url);
        } else if (intentUrl.equalsIgnoreCase("wxCustomerProblem")) {
            actionbar.setCenterTextView("微信客服");
            btn_open_weixin.setVisibility(View.VISIBLE);
            btn_open_weixin.setOnClickListener(this);
            url = APIBuilder.H5Url + APIBuilder.version + APIBuilder.wxCustomerProblem();
            initWebView(url);
        } else if (intentUrl.equalsIgnoreCase("aboutUs")) {
            actionbar.setCenterTextView("关于我们");
            url = APIBuilder.H5Url + APIBuilder.version + APIBuilder.aboutUs();
            initWebView(url);
        } else if (intentUrl.equalsIgnoreCase("platformSecurity")) {
            actionbar.setCenterTextView("平台安全");
            url = APIBuilder.H5Url + APIBuilder.version + APIBuilder.platformSecurity();
            initWebView(url);
        } else if (intentUrl.equalsIgnoreCase("gvrp")) {
            actionbar.setCenterTextView("《芒果金融用户协议》");
            url = APIBuilder.H5Url + APIBuilder.version + APIBuilder.gvrp();
            initWebView(url);
        } else if (intentUrl.equalsIgnoreCase("bondTransfer")) {
            actionbar.setCenterTextView("债券转让协议");
            url = APIBuilder.H5Url + APIBuilder.version + APIBuilder.bondTransfer();
            initWebView(url);
        } else if (intentUrl.equalsIgnoreCase("notice")) {
            actionbar.setCenterTextView("平台公告");
            url = APIBuilder.H5Url + APIBuilder.version + APIBuilder.notice();
            initWebView(url);
        } else if (intentUrl.equalsIgnoreCase("lcRaiders")) {
            actionbar.setCenterTextView("规则攻略");
            url = APIBuilder.H5Url + APIBuilder.version + APIBuilder.lcRaiders();
            initWebView(url);
        } else if (intentUrl.contains("vipwebchat6303")) {
            actionbar.setCenterTextView("在线客服");
            initWebView(intentUrl);
        }
    }

    private void initWebView(String url) {
        wv.setHorizontalScrollBarEnabled(true);
        wv.setVerticalScrollBarEnabled(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.setClickable(false);
        wv.getSettings().setDomStorageEnabled(true);
        wv.setWebChromeClient(new WebChromeClient());
        wv.setWebViewClient(new WebViewClient() {
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
        });
        wv.loadUrl(url);
    }

    @Override
    // 覆盖Activity类的onKeyDown(int keyCode,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) {
            if (intentUrl.contains("vipwebchat6303")) {
                MyActivityManager.getInstance().popCurrentActivity();
            } else {
                wv.goBack(); // goBack()表示返回WebView的上一页面
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        if (v == actionbar.leftImageView) {
            if (intentUrl.contains("vipwebchat6303")) {
                MyActivityManager.getInstance().popCurrentActivity();
                return;
            }
            if (wv.canGoBack()) {
                wv.goBack();
            } else {
                MyActivityManager.getInstance().popCurrentActivity();
            }
        } else if (v == btn_open_weixin) {
            SocialClient.getInstance(this).wxAPI.openWXApp();
        }
    }
}
