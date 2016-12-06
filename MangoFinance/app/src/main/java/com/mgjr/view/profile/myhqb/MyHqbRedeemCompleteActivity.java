package com.mgjr.view.profile.myhqb;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.SPUtils;
import com.mgjr.share.ActionbarActivity;

/**
 * Created by Administrator on 2016/8/16.
 */
public class MyHqbRedeemCompleteActivity extends ActionbarActivity implements View.OnClickListener {

    private TextView tv_withdraw_request_time;
    private String time;
    private TextView tv_withdraw_complete_account;
    private String amount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_layout_redeemcomplete,this);
        this.actionbar.setCenterTextView("提交成功");
        this.actionbar.setRightTextView("完成",this);
        actionbar.leftImageView.setVisibility(View.INVISIBLE);
        this.actionbar.rightTextView.setTextSize(14);
        initView();
        getData();
    }

    private void initView() {
        tv_withdraw_request_time = (TextView) findViewById(R.id.tv_withdraw_request_time);
        tv_withdraw_complete_account = (TextView) findViewById(R.id.tv_withdraw_complete_account);
    }

    private void getData() {
        amount = getIntent().getStringExtra("code");
        time = getIntent().getStringExtra("type");
        tv_withdraw_request_time.setText(time);
        tv_withdraw_complete_account.setText(amount + "元");
    }

    @Override
    public void onClick(View v) {
        if(v == actionbar.rightTextView){
//            popActivity();
            MyActivityManager.getInstance().popCurrentActivity();
            SPUtils.put(MyHqbRedeemCompleteActivity.this,"TempIntent","intentResource","doRedeem");
        }
    }
}
