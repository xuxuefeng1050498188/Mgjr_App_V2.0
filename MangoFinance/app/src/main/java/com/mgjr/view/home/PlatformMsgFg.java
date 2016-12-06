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
 * 平台公告
 */

public class PlatformMsgFg extends BaseFrament {
    private ListView lv_platformmsg;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fg_msgcenter_platformmsg, null);
        initViews(layout);
        return layout;

    }

    private void initViews(View layout) {
        lv_platformmsg = (ListView) layout.findViewById(R.id.lv_platformmsg);
    }

}
