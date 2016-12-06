package com.mgjr.view.profile.rechargeWithdraw;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgjr.R;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.model.bean.EventBusBean.WithdrawCompleteBean;
import com.mgjr.model.bean.WithdrawCompleteInfoBean;
import com.mgjr.share.ActionbarActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/8/9.
 */
public class WithdrawCompleteActivity extends ActionbarActivity implements View.OnClickListener {
    @InjectView(R.id.confirminvest_bought_btn)
    ImageView confirminvestBoughtBtn;
    @InjectView(R.id.tv_withdraw_request_time)
    TextView tvWithdrawRequestTime;
    @InjectView(R.id.tv_withdraw_complete_bankname)
    TextView tvWithdrawCompleteBankname;
    @InjectView(R.id.tv_withdraw_complete_account)
    TextView tvWithdrawCompleteAccount;
    @InjectView(R.id.confirminvest_clock_btn)
    ImageView confirminvestClockBtn;
    @InjectView(R.id.tv_withdraw_complete_balance)
    TextView tvWithdrawCompleteBalance;
    private WithdrawCompleteBean withdrawCompleteBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_layout_withdraw_complete, this);
        initActionBar();

        ButterKnife.inject(this);
        EventBus.getDefault().register(this);
    }

    private void initActionBar() {
        this.actionbar.setCenterTextView("提交成功");
        this.actionbar.leftImageView.setVisibility(View.INVISIBLE);
        this.actionbar.setRightTextView("完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                popActivity();
                MyActivityManager.getInstance().popCurrentActivity();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void eventBus(WithdrawCompleteBean event) {
        if (event != null) {
            //设置数据
            withdrawCompleteBean = event;
            setData();
        }
    }

    private void setData() {
        String requestTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(withdrawCompleteBean.getAccountWithdraw().getCtime());
        tvWithdrawRequestTime.setText(requestTime);

        String info = withdrawCompleteBean.getAccountWithdraw().getInfo();
        Gson gson = new GsonBuilder().create();
        WithdrawCompleteInfoBean infoBean = gson.fromJson(info, WithdrawCompleteInfoBean.class);
        String cardNo = infoBean.getCardNo();
        String cardInfos = infoBean.getBankName() + "(" + cardNo.substring(cardNo.length() - 4) + ")";
        tvWithdrawCompleteBankname.setText(cardInfos);
        tvWithdrawCompleteAccount.setText("" + new DecimalFormat("###,###,##0.00").format(withdrawCompleteBean.getAccountWithdraw().getAmount()) + "元");
        tvWithdrawCompleteBalance.setText("" + new DecimalFormat("###,###,##0.00").format(withdrawCompleteBean.getAccountAmount()) + "元");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {

    }
}
