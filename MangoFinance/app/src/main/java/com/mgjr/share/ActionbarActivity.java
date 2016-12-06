package com.mgjr.share;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.mgjr.Utils.MyActivityManager;
import com.mgjr.R;

/**
 * Created by wim on 16/5/30.
 */
public class ActionbarActivity extends CommomActivity {

    protected CustomActionbar actionbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout.LayoutParams actionbarParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        actionbar = new CustomActionbar(this);
        actionBarLayout.addView(actionbar, actionbarParams);

        int curIndex = MyActivityManager.getInstance().curIndexAtActivity(this);

        if (curIndex > 0) {
            actionbar.leftImageView.setVisibility(View.VISIBLE);
            actionbar.setLeftImageView(R.drawable.actionbar_gray_backbtn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyActivityManager.getInstance().popCurrentActivity();
                }
            });
        } else {
            actionbar.leftImageView.setVisibility(View.INVISIBLE);
        }

    }



}
