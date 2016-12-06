package com.mgjr.view.invester;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class JmgGuaranteeInfoFragment extends BaseFrament {
    private WebView wv;
    private String url;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.layout_fg_jmg_guaranteeinfo,container,false);
        initView(layout);
        return layout;
    }

    private void initView(View layout) {
        url = APIBuilder.H5Url + APIBuilder.version + APIBuilder.guaranteeInfo();
        wv = (WebView) layout.findViewById(R.id.wv_jmg_guaranteeinfo);
        wv.setHorizontalScrollBarEnabled(false);
        wv.setVerticalScrollBarEnabled(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setClickable(false);
        wv.loadUrl(url);
        wv.setWebViewClient(new WebViewClient());
        wv.setWebChromeClient(new WebChromeClient());

    }
}
