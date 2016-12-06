package com.mgjr.view.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.mgjr.R;
import com.mgjr.share.ActionbarActivity;

/**
 * Created by Administrator on 2016/10/11.
 */

public class AppMessageCenterActivity extends ActionbarActivity implements View.OnClickListener {
    private ListView lv_app_msgcenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_layout_app_msgcenter,this);
        actionbar.setCenterTextView("平台公告");
        actionbar.setRightImageView(R.drawable.profile_message_center_setting,this);
        initViews();
        initListView();
    }



    private void initViews() {
        lv_app_msgcenter = (ListView) findViewById(R.id.lv_app_msgcenter);
    }

    @Override
    public void onClick(View v) {
        if(v == actionbar.rightImageView){

        }
    }

    private void initListView() {
        MsgCenterListAdapter adapter = new MsgCenterListAdapter(this);
        lv_app_msgcenter.setAdapter(adapter);
    }
}
