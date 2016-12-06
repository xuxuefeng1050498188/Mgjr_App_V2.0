package com.mgjr.view.profile.messagecenter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mgjr.R;
import com.mgjr.share.ActionbarActivity;

public class ProfileInfomationSettingActivity extends ActionbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_profile_infomation_setting, this);
        actionbar.setCenterTextView("消息设置");
    }
}
