package com.mgjr.view.profile;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.SPUtils;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.model.bean.EventBusBean.ActionMapBean;
import com.mgjr.model.bean.EventBusBean.MyInfosBean;
import com.mgjr.model.bean.UserCenterBean;
import com.mgjr.presenter.impl.UserCenterPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.BaseFrament;
import com.mgjr.share.CircleImageView;
import com.mgjr.share.NetUtils;
import com.mgjr.share.WaveView;
import com.mgjr.view.common.CommonWebViewActivity;
import com.mgjr.view.common.LoginActivity;
import com.mgjr.view.listeners.ViewListener;
import com.mgjr.view.profile.CapitalDetails.CapitalDetailsActivity;
import com.mgjr.view.profile.FinancialPlanner.FinancialPlannerActivity;
import com.mgjr.view.profile.accountsetting.ProfileAccountSettingActivity;
import com.mgjr.view.profile.activityzone.ProfileEventsListActivity;
import com.mgjr.view.profile.mangoExperienceFinancing.MangoExperienceFinancingActivity;
import com.mgjr.view.profile.mangobox.MangoBoxActivity;
import com.mgjr.view.profile.myJmg.MyJmgActivity;
import com.mgjr.view.profile.myhqb.MyHqbActivity;
import com.mgjr.view.profile.profileproperty.ProfilePropertyActivity;
import com.mgjr.view.profile.rechargeWithdraw.RechargeActivity;
import com.mgjr.view.profile.rechargeWithdraw.WithdrawActivity;
import com.mgjr.view.profile.servicecenter.ProfileServiceCenterActivity;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wim on 16/5/23.
 */


public class ProfileFragment extends BaseFrament implements View.OnClickListener, ViewListener<UserCenterBean>, SlidingView.getContentViewTop {

    private CircleImageView avatorView;
    private WaveView waveView;
    private WaveView.WaveHelper mWaveHelper;
    private int mBorderColor = Color.parseColor("#44FFFFFF");

    private boolean isAuth;

    private RelativeLayout authView;
    private RelativeLayout unauthView;
    //总资产，累计收益，昨日收益，昵称
    private TextView allestate_textview, tv_total_income, tv_yestodayincome, nick_textview;

    private ImageView imgbtn_menu;
    private ImageView mailImageBtn;
    private ImageView eyebtn_allbalance;

    private SlidingView slidingView;

    private int contentViewTop;

    private WaveView.WaveHelper waveHelper;

    private LayoutInflater inflater;

    //未登录状态头布局
    private LinearLayout toplayout_profile_unlogin;
    //下方内容区域
    private LinearLayout layout_profile_content;
    //未登录状态 登录按钮
    private Button loginbtn_profilefg;

    private TextView tv_profile_userbalance;
    private TextView tv_profile_hqbaccount, tv_profile_jmgaccount;

    private LinearLayout layout_profile_property, header_view;
    private RelativeLayout layout_profile_userbalance, layout_topview_logined;
    private LinearLayout layout_profile_myhqb, layout_profile_myjmg;
    private ProfileCell list_mangobox;
    private ProfileCell profile_service_center;
    private ProfileCell profile_events_list;
    private ProfileCell layout_profile_tyj;
    private RelativeLayout financialplanner_list;
    //充值、提现按钮
    private Button btn_recharge, btn_withdraw;

    private int downY;

    private UserCenterPresenterImpl userCenterPresenter;

    private SharedPreferences sp;

    /**
     * 总资产
     */
    private Double totalAssets;
    /**
     * 总收益
     */
    private Double totalIncome;
    /**
     * 账户余额
     */
    private Double accountBalance;
    /*提现锁定金额*/
    private Double withdrawingAmount;
    /**
     * 活期宝收益
     */
    private Double yslxHqb;
    /**
     * 赎回中金额
     */
    private Double redeemingjHqb;
    /**
     * 金芒果收益
     */
    private Double yslxLoan;
    /*投资锁定金额**/
    private Double frozenTenderAmount;

    /**
     * 金芒果待收
     */
    private Double dsbjLoan;
    /**
     * 活期宝待收
     */
    private Double dsbjHqb;
    /*理财师收益*/
    private Double financialProfit;
    /*昨日收益**/
    private Double yesterdayIncome;
    /*体验金收益*/
    private Double yslxXsb;
    /**
     * 活动奖励
     */
    private Double activityReward;
    /*体验金金额*/
    private Double xsbAmount;
    /*红包金额*/
    private Double hbAmount;
    /*头像地址*/
    private String headImgUrl;
    private Bitmap headImage;

    /*芒果宝*/
    private Double dsbjMgb;
    private Double yslxMgb;

    private boolean isLogined = false;
    private boolean isPressed = false;
    private boolean isChangedNickname = false;
    private boolean isHideAmount = false;

    Handler waveHandler = new Handler() {
        public void handleMessage(Message msg) {
            mWaveHelper.start();
            super.handleMessage(msg);
        }
    };


    private Bundle bundle;
    private KProgressHUD progressHUD;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.profile_layout, container, false);
        initViews(layout);
        EventBus.getDefault().register(this);
        initLoadingProgressView();
        return layout;
    }

    private void initLoadingProgressView() {
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkIsLogin();
    }


    /**
     * 通过我的信息界面修改完了头像或者昵称之后要通知账户设置界面更改相应信息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void eventBus(MyInfosBean event) {
        if (event.getHeadImage() != null) {
            avatorView.setImageBitmap(event.getHeadImage());
        }
    }

    private void initViews(View layout) {

        userCenterPresenter = new UserCenterPresenterImpl(this);

        inflater = (LayoutInflater) MainApplication.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sp = MainApplication.getContext().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);

        layout_profile_content = (LinearLayout) inflater.inflate(R.layout.profile_content_layout, null);
        tv_profile_jmgaccount = (TextView) layout.findViewById(R.id.tv_profile_jmgaccount);
        tv_profile_hqbaccount = (TextView) layout.findViewById(R.id.tv_profile_hqbaccount);
        tv_profile_userbalance = (TextView) layout.findViewById(R.id.tv_profile_userbalance);

        avatorView = (CircleImageView) layout.findViewById(R.id.avator_button);
        /**账户余额*/
        layout_profile_userbalance = (RelativeLayout) layout.findViewById(R.id.layout_profile_userbalance);
        layout_profile_myhqb = (LinearLayout) layout.findViewById(R.id.layout_profile_myhqb);
        layout_profile_myjmg = (LinearLayout) layout.findViewById(R.id.layout_profile_myjmg);
        header_view = (LinearLayout) layout.findViewById(R.id.header_view);

        layout_topview_logined = (RelativeLayout) layout.findViewById(R.id.layout_topview_logined);

        layout_profile_userbalance.setOnClickListener(this);
        layout_profile_myhqb.setOnClickListener(this);
        layout_profile_myjmg.setOnClickListener(this);

        avatorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击头像进入账户设置界面
                MyActivityManager.getInstance().startNextActivity(ProfileAccountSettingActivity.class);
            }
        });
        slidingView = (SlidingView) layout.findViewById(R.id.sliding_view);
        slidingView.setGetTop(this);
        authView = (RelativeLayout) layout.findViewById(R.id.auth_top_view);
        unauthView = (RelativeLayout) layout.findViewById(R.id.unauth_top_view);

        waveView = (WaveView) layout.findViewById(R.id.wave_view);
        waveView.setBorder(0, mBorderColor);
        waveView.setShapeType(WaveView.ShapeType.SQUARE);
//        waveView.setWaveColor();
        mWaveHelper = waveView.getWaveHelper();

        eyebtn_allbalance = (ImageView) layout.findViewById(R.id.eyebtn_allbalance);
        eyebtn_allbalance.setOnClickListener(this);

        imgbtn_menu = (ImageView) layout.findViewById(R.id.imgbtn_menu);
        imgbtn_menu.setOnClickListener(this);

        mailImageBtn = (ImageView) layout.findViewById(R.id.mail_img_btn);
        mailImageBtn.setOnClickListener(this);

        list_mangobox = (ProfileCell) layout.findViewById(R.id.list_mangobox);
        list_mangobox.setOnClickListener(this);
        //服务中心点击事件初始化
        profile_service_center = (ProfileCell) layout.findViewById(R.id.profile_service_center);
        profile_service_center.setOnClickListener(this);
        financialplanner_list = (RelativeLayout) layout.findViewById(R.id.financialplanner_list);
        financialplanner_list.setOnClickListener(this);

        profile_events_list = (ProfileCell) layout.findViewById(R.id.profile_events_list);
        profile_events_list.setOnClickListener(this);

        layout_profile_tyj = (ProfileCell) layout.findViewById(R.id.layout_profile_tyj);
        layout_profile_tyj.setOnClickListener(this);

        btn_withdraw = (Button) layout.findViewById(R.id.btn_withdraw);
        btn_recharge = (Button) layout.findViewById(R.id.btn_recharge);

        btn_recharge.setOnClickListener(this);
        btn_withdraw.setOnClickListener(this);

        toplayout_profile_unlogin = (LinearLayout) layout.findViewById(R.id.toplayout_profile_unlogin);
        loginbtn_profilefg = (Button) layout.findViewById(R.id.loginbtn_profilefg);
        loginbtn_profilefg.setOnClickListener(this);

        allestate_textview = (TextView) layout.findViewById(R.id.allestate_textview);
        tv_total_income = (TextView) layout.findViewById(R.id.tv_total_income);
        tv_yestodayincome = (TextView) layout.findViewById(R.id.tv_yestodayincome);
        nick_textview = (TextView) layout.findViewById(R.id.nick_textview);
        allestate_textview.setOnClickListener(this);
        tv_total_income.setOnClickListener(this);
        tv_yestodayincome.setOnClickListener(this);

        bundle = new Bundle();
        int color = Color.argb(100, 104, 183, 246);
        int bebindColor = Color.argb(50, 104, 183, 246);
        waveView.setWaveColor(color, bebindColor);

//


    }


    private void checkIsLogin() {
        isLogined = sp.getBoolean("isLogined", false);

        if (!isLogined) { //未登录
            toplayout_profile_unlogin.setVisibility(View.VISIBLE);
            layout_topview_logined.setVisibility(View.INVISIBLE);
            imgbtn_menu.setVisibility(View.GONE);
            avatorView.setVisibility(View.GONE);
            mailImageBtn.setVisibility(View.GONE);
            nick_textview.setVisibility(View.GONE);
            slidingView.expanded = false;

            setDataUnlogin();
            //禁止slidingView滑动
            slidingView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });


        } else {  //已登录
            toplayout_profile_unlogin.setVisibility(View.GONE);
            layout_topview_logined.setVisibility(View.VISIBLE);
            mailImageBtn.setVisibility(View.VISIBLE);
            imgbtn_menu.setVisibility(View.VISIBLE);
            avatorView.setVisibility(View.VISIBLE);
            nick_textview.setVisibility(View.VISIBLE);
            slidingView.expanded = true;

            slidingView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            setDataLogined();
            isHideAmount = (boolean) SPUtils.get(MainApplication.getContext(), "EYE_STATUS", "eye_status", false);
            if (isHideAmount) {
                eyebtn_allbalance.setImageResource(R.drawable.profile_key_btn);
                hideAllAmount();
            } else {
                eyebtn_allbalance.setImageResource(R.drawable.profile_key_btn_normal);
            }

        }
    }

    private void setDataLogined() {
        sp = MainApplication.getContext().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        int id = sp.getInt("id", 0);
        String mid = String.valueOf(id);
        Map<String, String> necessaryParams = new HashMap<>();
        necessaryParams.put("mid", mid);
        userCenterPresenter.sendRequest(necessaryParams, null);
    }


    private void setDataUnlogin() {
        mWaveHelper.cancel();
        tv_profile_hqbaccount.setText("——");
        tv_profile_jmgaccount.setText("——");
        tv_profile_userbalance.setText("——");
//        tv_profile_userbalance.setTextSize(ScreenUtils.dipToPx(getActivity(),12));
        list_mangobox.setDetailTextView("");
        layout_profile_tyj.setDetailTextView("");
    }

    @Override
    public void onStart() {
        super.onStart();
        waveHandler.sendEmptyMessage(0);

    }

    @Override
    public void onStop() {
        super.onStop();
        mWaveHelper.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {

        if (v == avatorView) {
            if (isLogined) {
                MyActivityManager.getInstance().startNextActivity(ProfileAccountSettingActivity.class);
            } else {
                CommonToastUtils.showToast(MainApplication.getContext(), "您未登录,请登录后查看");
                MyActivityManager.getInstance().startNextActivity(LoginActivity.class);
            }
        } else if (v == layout_profile_userbalance) {
            if (isLogined == true) {
                MyActivityManager.getInstance().startNextActivity(CapitalDetailsActivity.class);

            } else {
                CommonToastUtils.showToast(MainApplication.getContext(), "您未登录,请登录后查看");
                MyActivityManager.getInstance().startNextActivity(LoginActivity.class);
            }

        } else if (v == imgbtn_menu) {
            MyActivityManager.getInstance().startNextActivity(CapitalDetailsActivity.class);
        } else if (v == profile_service_center) {
            MyActivityManager.getInstance().startNextActivity(ProfileServiceCenterActivity.class);
        } else if (v == profile_events_list) {
            MyActivityManager.getInstance().startNextActivity(ProfileEventsListActivity.class);
        } else if (v == list_mangobox) {
            if (isLogined == true) {
                MyActivityManager.getInstance().startNextActivity(MangoBoxActivity.class);
            } else {

                CommonToastUtils.showToast(MainApplication.getContext(), "您未登录,请登录后查看");
                MyActivityManager.getInstance().startNextActivity(LoginActivity.class);
            }

        } else if (v == layout_profile_myhqb) {
            if (isLogined == true) {
                MyActivityManager.getInstance().startNextActivity(MyHqbActivity.class);
            } else {
                CommonToastUtils.showToast(MainApplication.getContext(), "您未登录,请登录后查看");
                MyActivityManager.getInstance().startNextActivity(LoginActivity.class);
            }
        } else if (v == layout_profile_myjmg) {
            if (isLogined == true) {
                MyActivityManager.getInstance().startNextActivity(MyJmgActivity.class);
            } else {
                CommonToastUtils.showToast(MainApplication.getContext(), "您未登录,请登录后查看");
                MyActivityManager.getInstance().startNextActivity(LoginActivity.class);
            }
        } else if (v == layout_profile_tyj) {
            if (isLogined == true) {
                MyActivityManager.getInstance().startNextActivity(MangoExperienceFinancingActivity.class);
            } else {
                layout_profile_tyj.setDetailTextView("");

                CommonToastUtils.showToast(MainApplication.getContext(), "您未登录,请登录后查看");
                MyActivityManager.getInstance().startNextActivity(LoginActivity.class);
            }

        } else if (v == btn_recharge) {
            if (isLogined) {
                MyActivityManager.getInstance().startNextActivity(RechargeActivity.class);
            } else {
                CommonToastUtils.showToast(MainApplication.getContext(), "您未登录,请登录后查看");
                MyActivityManager.getInstance().startNextActivity(LoginActivity.class);
            }
        } else if (v == btn_withdraw) {
            if (isLogined) {
                MyActivityManager.getInstance().startNextActivity(WithdrawActivity.class);
            } else {
                CommonToastUtils.showToast(MainApplication.getContext(), "您未登录,请登录后查看");
                MyActivityManager.getInstance().startNextActivity(LoginActivity.class);
            }

        } else if (v == loginbtn_profilefg) {
            MyActivityManager.getInstance().startNextActivity(LoginActivity.class);
        } else if (v == mailImageBtn) {
            MyActivityManager.getInstance().startNextActivity(CommonWebViewActivity.class, "notice");
        } else if (v == financialplanner_list) {
            if (isLogined) {
                MyActivityManager.getInstance().startNextActivity(FinancialPlannerActivity.class);
            } else {
                CommonToastUtils.showToast(MainApplication.getContext(), "您未登录,请登录后查看");
                MyActivityManager.getInstance().startNextActivity(LoginActivity.class);
            }
        } else if (v == eyebtn_allbalance) {
            if (isPressed) {
                eyebtn_allbalance.setImageResource(R.drawable.profile_key_btn_normal);
                SPUtils.put(MainApplication.getContext(), "EYE_STATUS", "eye_status", false);
                isPressed = false;
                setTextData();
            } else {
                eyebtn_allbalance.setImageResource(R.drawable.profile_key_btn);
                isPressed = true;
                hideAllAmount();
                SPUtils.put(MainApplication.getContext(), "EYE_STATUS", "eye_status", true);
            }
        } else if (v == allestate_textview) {
            bundle.putString("code", "0");
            putBundleData();
            MyActivityManager.getInstance().startNextActivity(ProfilePropertyActivity.class, bundle);
        } else if (v == tv_total_income) {
            bundle.putString("code", "1");
            putBundleData();


            MyActivityManager.getInstance().startNextActivity(ProfilePropertyActivity.class, bundle);
        } else if (v == tv_yestodayincome) {
            bundle.putString("code", "1");
            putBundleData();
            MyActivityManager.getInstance().startNextActivity(ProfilePropertyActivity.class, bundle);
        }
    }

    private void hideAllAmount() {
        allestate_textview.setText("****");
        tv_total_income.setText("****");
        tv_yestodayincome.setText("****");
        tv_profile_userbalance.setText("****");
        tv_profile_hqbaccount.setText("****");
        tv_profile_jmgaccount.setText("****");
        if (xsbAmount != null && xsbAmount != 0) {
            layout_profile_tyj.setDetailTextView("****");
        }
        if (hbAmount != null && hbAmount != 0) {
            list_mangobox.setDetailTextView("****");
        }
    }

    private void putBundleData() {
    /*总资产*/
        if (totalAssets != null) {
            bundle.putDouble("totalAssets", totalAssets);
        } else {
            bundle.putDouble("totalAssets", 0.00);
        }
        if (accountBalance != null) {
            bundle.putDouble("accountBalance", accountBalance);
        } else {
            bundle.putDouble("accountBalance", 0.00);
        }
        if (withdrawingAmount != null) {
            bundle.putDouble("withdrawingAmount", withdrawingAmount);
        } else {
            bundle.putDouble("withdrawingAmount", 0.00);
        }
        if (dsbjLoan != null) {

            bundle.putDouble("dsbjLoan", dsbjLoan);
        } else {
            bundle.putDouble("dsbjLoan", 0.00);
        }
        if (redeemingjHqb != null) {

            bundle.putDouble("redeemingjHqb", redeemingjHqb);
        } else {
            bundle.putDouble("redeemingjHqb", 0.00);
        }
        if (dsbjHqb != null) {

            bundle.putDouble("dsbjHqb", dsbjHqb);
        } else {
            bundle.putDouble("dsbjHqb", 0.00);
        }
        if (frozenTenderAmount != null) {

            bundle.putDouble("frozenTenderAmount", frozenTenderAmount);
        } else {
            bundle.putDouble("frozenTenderAmount", 0.00);
        }
        /**总收益*/
        if (totalIncome != null) {

            bundle.putDouble("totalIncome", totalIncome);
        } else {
            bundle.putDouble("totalIncome", 0.00);
        }
        if (yslxHqb != null) {

            bundle.putDouble("yslxHqb", yslxHqb);
        } else {
            bundle.putDouble("yslxHqb", 0.00);
        }
        if (yslxLoan != null) {
            bundle.putDouble("yslxLoan", yslxLoan);
        } else {
            bundle.putDouble("yslxLoan", 0.00);
        }

        if (financialProfit != null) {
            bundle.putDouble("financialProfit", financialProfit);
        } else {
            bundle.putDouble("financialProfit", 0.00);
        }
        if (yesterdayIncome != null) {
            bundle.putDouble("yesterdayIncome", yesterdayIncome);
        } else {
            bundle.putDouble("yesterdayIncome", 0.00);
        }
        //芒果宝
        if (dsbjMgb != null) {
            bundle.putDouble("dsbjMgb", dsbjMgb);
        } else {
            bundle.putDouble("dsbjMgb", 0.00);
        }
        if (yslxMgb != null) {
            bundle.putDouble("yslxMgb", yslxMgb);
        } else {
            bundle.putDouble("yslxMgb", 0.00);
        }
        if (activityReward != null) {
            bundle.putDouble("activityReward", activityReward);
        } else {
            bundle.putDouble("activityReward", 0.00);
        }
        if (yslxXsb != null) {
            bundle.putDouble("yslxXsb", yslxXsb);
        } else {
            bundle.putDouble("yslxXsb", 0.00);
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        mWaveHelper.cancel();
    }

    public void changeAuthStatus() {
        if (isAuth) {
            authView.setVisibility(View.VISIBLE);
            unauthView.setVisibility(View.GONE);

        } else {
            unauthView.setVisibility(View.VISIBLE);
            authView.setVisibility(View.GONE);

        }
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showError() {
        boolean isSocketTimeoutException = (boolean) SPUtils.get(MainApplication.getContext(), "SocketTimeoutException", "isSocketTimeoutException", false);
        if (!NetUtils.isConnected(MainApplication.getContext())) {
            CommonToastUtils.showToast(MainApplication.getContext(), getString(R.string.network_error));
        } else if (isSocketTimeoutException) {
            CommonToastUtils.showToast(MainApplication.getContext(), getString(R.string.request_timeout));
            SPUtils.clear(MainApplication.getContext(), "SocketTimeoutException");
        } else {
            CommonToastUtils.showToast(MainApplication.getContext(), getString(R.string.system_error));
        }
    }

    @Override
    public void showError(OnPresenterListener listener, UserCenterBean userCenterBean) {
        CommonToastUtils.showToast(MainApplication.getContext(), userCenterBean.getMsg());
    }

    @Override
    public void responseData(OnPresenterListener listener, UserCenterBean userCenterBean) {

        Map actionMap = userCenterBean.getActionMap();
        ActionMapBean actionMapBean = new ActionMapBean();
        actionMapBean.setActionMap(actionMap);
        EventBus.getDefault().postSticky(actionMapBean);

        //账户余额
        accountBalance = userCenterBean.getAccountBalance();
        //活期宝待收本金
        yslxHqb = userCenterBean.getYslxHqb();
        //金芒果待收本金
        dsbjLoan = userCenterBean.getDsbjLoan();
        //总资产
        totalAssets = userCenterBean.getTotalAssets();
        //昨日收益
        yesterdayIncome = userCenterBean.getYesterdayIncome();
        //累计收益
        totalIncome = userCenterBean.getTotalIncome();
        //真实姓名
        String truename = userCenterBean.getMember().getTruename();
        //活期宝收益
        dsbjHqb = userCenterBean.getDsbjHqb();
        //体验金
        xsbAmount = userCenterBean.getXsbAmount();
        //红包金额
        hbAmount = userCenterBean.getHbAmount();
        /*设置用户头像*/
        headImgUrl = userCenterBean.getHeadImgUrl();
        if (headImage != null) {
//            setHeadImage();
            //把头像地址保存
            SPUtils.put(MainApplication.getContext(), "LOGIN", "headImgUrl", headImgUrl);
        }
        setHeadImage();
        String nickname = userCenterBean.getNickname();
        if (!TextUtils.isEmpty(nickname)) {
            nick_textview.setText(nickname);
            SPUtils.put(MainApplication.getContext(), "LOGIN", "nickname", nickname);
        } else {
//            String userName = (String) SPUtils.get(MainApplication.getContext(), "LOGIN", "name", "");
            nick_textview.setText(userCenterBean.getMember().getUsername());
        }

//        slidingView.expandView(false);

        dsbjMgb = userCenterBean.getDsbjMgb();
        yslxMgb = userCenterBean.getYslxMgb();

        //把身份证号码存入本地
        String idcode = userCenterBean.getMember().getIdcode();
        if (!TextUtils.isEmpty(idcode)) {
            SPUtils.put(MainApplication.getContext(), "LOGIN", "idCode", idcode);
        }

        if (truename != null) {
            SPUtils.put(MainApplication.getContext(), "LOGIN", "truename", truename);
            SPUtils.put(MainApplication.getContext(), "LOGIN", "isTruename", true);
        }


        String email = userCenterBean.getMember().getEmail();
        if (!TextUtils.isEmpty(email)) {
            SPUtils.put(MainApplication.getContext(), "LOGIN", "email", email);
        }
        String mobilePhone = userCenterBean.getMember().getMobile();
        if (!TextUtils.isEmpty(mobilePhone)) {
            SPUtils.put(MainApplication.getContext(), "LOGIN", "mobilePhone", mobilePhone);
        }


        /*资金总览数据*/
        withdrawingAmount = userCenterBean.getWithdrawingAmount();
        redeemingjHqb = userCenterBean.getRedeemingjHqb();
        frozenTenderAmount = userCenterBean.getFrozenTenderAmount();

        /*总收益数据*/
        yslxLoan = userCenterBean.getYslxLoan();
        financialProfit = userCenterBean.getFinancialProfit();
        yslxXsb = userCenterBean.getYslxXsb();
        activityReward = userCenterBean.getActivityReward();
        yesterdayIncome = userCenterBean.getYesterdayIncome();

        if (!isHideAmount) {
            setTextData();
        }
        int lcs = userCenterBean.getMember().getLcs();
//        if (lcs == 0) {
//            financialplanner_list.setDetailTextView("投资1000 +,成为芒果理财师");
//        } else if (lcs == 1) {
//            financialplanner_list.setDetailTextView("普通理财师");
//        } else if (lcs == 2) {
//            financialplanner_list.setDetailTextView("金牌理财师");
//        }

    }


    private void setTextData() {

        allestate_textview.setText(new DecimalFormat("###,###,##0.00").format(totalAssets));
        if (totalIncome == 0) {
            tv_total_income.setText("暂无收益");
            tv_total_income.setTextSize(14);
        } else {
            tv_total_income.setText(new DecimalFormat("###,###,##0.00").format(totalIncome));
            tv_total_income.setTextSize(20);
        }
        if (yesterdayIncome == 0) {
            tv_yestodayincome.setText("暂无收益");
            tv_yestodayincome.setTextSize(14);
        } else {
            tv_yestodayincome.setText(new DecimalFormat("###,###,##0.00").format(yesterdayIncome));
            tv_yestodayincome.setTextSize(20);
        }

        if (xsbAmount != 0) {
            layout_profile_tyj.setDetailTextView(new DecimalFormat("###,###,##0.00").format(xsbAmount));
        } else {
            layout_profile_tyj.setDetailTextView("");
        }
        if (hbAmount != 0) {
            list_mangobox.setDetailTextView(new DecimalFormat("###,###,##0.00").format(hbAmount));
        } else {
            list_mangobox.setDetailTextView("");
        }

        if (accountBalance != null) {
            tv_profile_userbalance.setText(new DecimalFormat("###,###,##0.00").format(accountBalance));
        } else {
            tv_profile_userbalance.setText("0.00");
        }
        if (totalAssets == null || totalAssets == 0) {
            allestate_textview.setText("暂无收益");
            allestate_textview.setTextSize(16);
        } else {
            allestate_textview.setText(new DecimalFormat("###,###,##0.00").format(totalAssets));
            allestate_textview.setTextSize(32);
        }
//        int hqbCount = 0;
        if (dsbjHqb != null) {
//            hqbCount = Double2int.double2Int(dsbjHqb);
            tv_profile_hqbaccount.setText(new DecimalFormat("###,###,##0.00").format(dsbjHqb));
        } else {
            tv_profile_hqbaccount.setText("0.00");
        }
//        int jmgCount = 0;
        if (dsbjLoan != null) {
//            jmgCount = Double2int.double2Int(dsbjLoan);
            Double jmg = dsbjLoan + frozenTenderAmount;
            tv_profile_jmgaccount.setText(new DecimalFormat("###,###,##0.00").format(jmg));
        } else {
            tv_profile_jmgaccount.setText("0.00");
        }

//        tv_profile_hqbaccount.setText(new DecimalFormat("###,###,##0.00").format(hqbCount));
//        tv_profile_jmgaccount.setText(new DecimalFormat("###,###,##0.00").format(jmgCount));
    }

    private void setHeadImage() {
        headImgUrl = (String) SPUtils.get(MainApplication.getContext(), "LOGIN", "headImgUrl", "");
        if (!TextUtils.isEmpty(headImgUrl)) {
            Picasso.with(MainApplication.getContext())
                    .load(headImgUrl)
                    .placeholder(R.drawable.mango_baby_4)
                    .error(R.drawable.mango_baby_4)
                    .into(avatorView);
        } else {
            Picasso
                    .with(MainApplication.getContext())
                    .cancelRequest(avatorView);
            avatorView.setImageResource(R.drawable.mango_baby_4);
        }
    }

    private void setNickName(String nickname) {
        if (!TextUtils.isEmpty(nickname)) {
            nick_textview.setText(nickname);
            SPUtils.put(MainApplication.getContext(), "LOGIN", "nickname", nickname);
        } else {
            String userName = (String) SPUtils.get(MainApplication.getContext(), "LOGIN", "name", "");
            nick_textview.setText(userName);
        }
    }

    @Override
    public void getTop(int top) {
        this.contentViewTop = top;
        if (contentViewTop == 0) {
            allestate_textview.setClickable(false);
            tv_total_income.setClickable(false);
            tv_yestodayincome.setClickable(false);
            eyebtn_allbalance.setClickable(false);
        } else {
            allestate_textview.setClickable(true);
            tv_total_income.setClickable(true);
            tv_yestodayincome.setClickable(true);
            eyebtn_allbalance.setClickable(true);
        }
    }
}
