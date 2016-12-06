package com.mgjr.view.profile.servicecenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mgjr.R;
import com.mgjr.share.APIBuilder;
import com.mgjr.share.BaseFrament;

/**
 * Created by Administrator on 2016/10/12.
 */

public class HotProblemFragment extends BaseFrament {
    private WebView wv;

    private String url;

    public WebView getWebView() {
        return wv;
    }

    public void setWevView(WebView wv) {
        this.wv = wv;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1: {
                    webViewGoBack();
                }
                break;
            }
        }
    };

    private void webViewGoBack() {
        wv.goBack();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.layout_fg_hotproblem, container, false);
        initView(layout);
        return layout;
    }

    private void initView(View layout) {
        url = APIBuilder.H5Url + APIBuilder.version + APIBuilder.hotProblem();
        wv = (WebView) layout.findViewById(R.id.wv_hotproblem);
        wv.setHorizontalScrollBarEnabled(false);
        wv.setVerticalScrollBarEnabled(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setClickable(false);
        wv.loadUrl(url);

        wv.setWebViewClient(new WebViewClient());
        wv.setWebChromeClient(new WebChromeClient());

        wv.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) {
                    handler.sendEmptyMessage(1);
                    return true;
                }
                return false;
            }

        });
    }


}
