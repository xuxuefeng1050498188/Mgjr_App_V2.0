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

public class HqbBuyNotesFragment extends BaseFrament {
    private WebView wv;
    private String url;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.layout_fg_hqb_buynotes, container, false);
        initView(layout);
        return layout;
    }

    private void initView(View layout) {
        url = APIBuilder.H5Url + APIBuilder.version + APIBuilder.buyNotes();
        wv = (WebView) layout.findViewById(R.id.wv_hqb_buynotes);
        wv.setHorizontalScrollBarEnabled(false);
        wv.setVerticalScrollBarEnabled(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setClickable(false);
        wv.loadUrl(url);

        wv.setWebViewClient(new WebViewClient());
        wv.setWebChromeClient(new WebChromeClient());
    }
}
