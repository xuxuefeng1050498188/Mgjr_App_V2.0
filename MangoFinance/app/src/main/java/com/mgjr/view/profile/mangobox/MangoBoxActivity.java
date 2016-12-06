package com.mgjr.view.profile.mangobox;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.SPUtils;
import com.mgjr.model.bean.EventBusBean.MangoBoxDataBean;
import com.mgjr.model.bean.MangoBoxBean;
import com.mgjr.presenter.impl.MangoBoxPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.MyFragmentPageAdapter;
import com.mgjr.view.common.CommonWebViewActivity;
import com.mgjr.view.listeners.ViewListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/1.
 */
public class MangoBoxActivity extends ActionbarActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, ViewListener<MangoBoxBean> {

    //红包tab 、理财券tab
    private TextView tv_mangobox_redpackets, tv_mangobox_financetickets;
    //tab指示器
    private ImageView img_redpackets_indicator, img_financetickets_indicator;

    private ViewPager vp_mangobox;

    private RedpacketsFragment rpFragment;
    private FinanceTicketsFragment ftFragment;
    private List<Fragment> fragments;

    private MyFragmentPageAdapter adapter;

    private MangoBoxPresenterImpl mangoBoxPresenter;
    private MangoBoxDataBean mangoBoxDataBean;
    private MangoBoxBean mangoBoxBean;
    private PopupWindow loadingPopupWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_layout_mangobox, this);
        this.actionbar.setCenterTextView("芒果宝盒");
        this.actionbar.setRightImageView(R.drawable.confirminvest_question_btn, this);
        initViews();
        initViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestNetworkData();
    }


    private void initViews() {

        mangoBoxPresenter = new MangoBoxPresenterImpl(this);

        tv_mangobox_redpackets = (TextView) findViewById(R.id.tv_mangobox_redpackets);
        tv_mangobox_financetickets = (TextView) findViewById(R.id.tv_mangobox_financetickets);

        tv_mangobox_redpackets.setOnClickListener(this);
        tv_mangobox_financetickets.setOnClickListener(this);

        img_redpackets_indicator = (ImageView) findViewById(R.id.img_redpackets_indicator);
        img_financetickets_indicator = (ImageView) findViewById(R.id.img_financetickets_indicator);

        vp_mangobox = (ViewPager) findViewById(R.id.vp_mangobox);

        rpFragment = new RedpacketsFragment();
        ftFragment = new FinanceTicketsFragment();

        fragments = new ArrayList<>();
        fragments.add(rpFragment);
        fragments.add(ftFragment);

        adapter = new MyFragmentPageAdapter(fragments, this.getSupportFragmentManager());
    }

    @Override
    public void onClick(View v) {
        if (v == tv_mangobox_redpackets) {
            vp_mangobox.setCurrentItem(0);
            tv_mangobox_redpackets.setTextColor(Color.parseColor("#fea901"));
            tv_mangobox_financetickets.setTextColor(Color.GRAY);
            img_redpackets_indicator.setVisibility(View.VISIBLE);
            img_financetickets_indicator.setVisibility(View.INVISIBLE);
        } else if (v == tv_mangobox_financetickets) {
            vp_mangobox.setCurrentItem(1);
            tv_mangobox_financetickets.setTextColor(Color.parseColor("#fea901"));
            tv_mangobox_redpackets.setTextColor(Color.GRAY);
            img_financetickets_indicator.setVisibility(View.VISIBLE);
            img_redpackets_indicator.setVisibility(View.INVISIBLE);
        } else if (v == actionbar.rightImageView) {
            MyActivityManager.getInstance().startNextActivity(CommonWebViewActivity.class, "boxProblem");
        }
    }

    /*获取网络数据**/
    private void requestNetworkData() {
        String mid = String.valueOf(SPUtils.get(this, "LOGIN", "id", 0));
        Map<String, String> params = new HashMap<>();
        params.put("mid", mid);

        mangoBoxPresenter.sendRequest(params, null);

    }

    private void initViewPager() {
        vp_mangobox.setAdapter(adapter);
        vp_mangobox.setCurrentItem(0);

        vp_mangobox.addOnPageChangeListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            tv_mangobox_redpackets.setTextColor(Color.parseColor("#fea901"));
            tv_mangobox_financetickets.setTextColor(Color.GRAY);
            img_redpackets_indicator.setVisibility(View.VISIBLE);
            img_financetickets_indicator.setVisibility(View.INVISIBLE);
        } else if (position == 1) {
            tv_mangobox_redpackets.setTextColor(Color.GRAY);
            tv_mangobox_financetickets.setTextColor(Color.parseColor("#fea901"));
            img_redpackets_indicator.setVisibility(View.INVISIBLE);
            img_financetickets_indicator.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void showLoading() {
//        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_layout_mangobox, null);
//        loadingPopupWindow = PopwUtils.showLoadingPopw(this, rootView);
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
//        PopwUtils.dismissLoadingPopw(loadingPopupWindow);
        dismissLoadingDialog();
    }

    @Override
    public void showError() {
        dismissLoadingDialog();
//        PopwUtils.dismissLoadingPopw(loadingPopupWindow);
        showSystemError();
    }

    @Override
    public void showError(OnPresenterListener listener, MangoBoxBean mangoBoxBean) {
        dismissLoadingDialog();
//        PopwUtils.dismissLoadingPopw(loadingPopupWindow);
        CommonToastUtils.showToast(this, mangoBoxBean.getMsg());
    }

    @Override
    public void responseData(OnPresenterListener listener, MangoBoxBean mangoBoxBean) {
        this.mangoBoxBean = mangoBoxBean;
        mangoBoxDataBean = new MangoBoxDataBean();

        /*加息券数据*/

        mangoBoxDataBean.setCanUsedCouponList(mangoBoxBean.getCanUsedCouponList());    //可使用加息券
        mangoBoxDataBean.setOverCouponList(mangoBoxBean.getOverCouponList());          //已过期加息券
        mangoBoxDataBean.setUsedCouponList(mangoBoxBean.getUsedCouponList());          //已使用加息券

        /*红包数据*/
        mangoBoxDataBean.setCanUsedtHBList(mangoBoxBean.getCanUsedtHBList());           //可使用红包
        mangoBoxDataBean.setOverHBList(mangoBoxBean.getOverHBList());                   //已过期红包
        mangoBoxDataBean.setUsedtHBList(mangoBoxBean.getUsedtHBList());                  //已使用红包

        mangoBoxDataBean.setGiftMapBean(mangoBoxBean.getGiftMap());

        EventBus.getDefault().postSticky(mangoBoxDataBean);
    }

    public MangoBoxDataBean getMangoBoxDataBean() {
        return mangoBoxDataBean;
    }

    public void setMangoBoxDataBean(MangoBoxDataBean mangoBoxDataBean) {
        this.mangoBoxDataBean = mangoBoxDataBean;
    }
}
