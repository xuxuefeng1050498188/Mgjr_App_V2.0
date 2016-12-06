package com.mgjr.view.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mgjr.R;
import com.mgjr.share.BaseFrament;

/**
 * Created by Administrator on 2016/10/11.
 * 投资信息
 */

public class MsgCenterInvestInfoFg extends BaseFrament {
    private ListView lv_investinfo;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fg_msgcenter_investinfo, null);
        initViews(layout);
        return layout;

    }

    private void initViews(View layout) {
        lv_investinfo = (ListView) layout.findViewById(R.id.lv_investinfo);
    }
}
