package com.mgjr.view.home;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.GetScreenSize;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.SPUtils;
import com.mgjr.Utils.ScreenUtils;
import com.mgjr.Utils.SimpleViewPagerAdapter;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.model.bean.HomepageRecommendProjectsBean;
import com.mgjr.presenter.impl.HomepageBannerPresenterImpl;
import com.mgjr.presenter.impl.HomepageEventsWindowPresenterImpl;
import com.mgjr.presenter.impl.HomepageRecommendProjectsPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.APIBuilder;
import com.mgjr.share.BaseFrament;
import com.mgjr.share.FixedSpeedScroller;
import com.mgjr.share.LoopViewPager;
import com.mgjr.share.NetUtils;
import com.mgjr.share.WaveView;
import com.mgjr.view.common.LoginActivity;
import com.mgjr.view.common.RegisterActivityStepOne;
import com.mgjr.view.listeners.ViewListener;
import com.mgjr.view.profile.mangoExperienceFinancing.MangoExperienceFinancingActivity;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.mgjr.R.id.img_closebtn;

/**
 * Created by wim on 16/5/23.
 */
public class HomeFragment extends BaseFrament implements View.OnClickListener, ViewListener<HomepageRecommendProjectsBean>, ViewPager.OnPageChangeListener, Animator.AnimatorListener {
    //底部标的展示viewpager , 头部信息展示viewpager
    private ViewPager vp_homepage_events_banner, viewpager_tender_homepage;
    private LinearLayout layout_indicator;
    private LoopViewPager vp_homepage_banner_nologin;
    private LinearLayout layout_toast;
    private WaveView waveView;

    private WaveView.WaveHelper mWaveHelper;
    private int mBorderColor = Color.parseColor("#FFFFFF");

    private TextView tv_tologin_homepage_unlogin;

    private Button btn_homepage_register;

    //滚动文字tv
    private TextView tv_toast_homepage;
    //根布局 , 头部已登录界面 ， 未登录界面
    private LinearLayout layout_poster;

    private TranslateAnimation animationOut, animationIn;
    private ValueAnimator mAnimator;

    private LinearLayout.LayoutParams lp;
    private FrameLayout layout_homepage_nologin;
    private ImageView imgContent;
    private ImageView img_bg_tender_vp1, img_bg_tender_vp2;

    //底部viewpager子view
    private LinearLayout recommendProjectOne, recommendProjectTwo;
    private LayoutInflater inflater;

    private SimpleViewPagerAdapter tenderAdapter;
    //推荐标
    private List<View> tenderBannerViews;
    //推荐标code
    private List<String> codes;
    //未登录状态下的banner
    private List<ImageView> homepageBanners;
    //海报banner
    private List<View> posterBanners;
    /*//推荐标名、投资人数、年化收益率、投资期限*/
    private TextView tv_homepage_recommend_project_one_name, tv_homepage_recommend_project_one_tenderMemberCount,
            tv_homepage_recommend_project_one_rate, tv_homepage_recommend_project_one_term;
    private TextView tv_tender_tips;
    private String projectName_one, tenderMemberCount_one, rate_one, term_one;

    private TextView tv_homepage_recommend_project_two_name, tv_homepage_recommend_project_two_tenderMemberCount,
            tv_homepage_recommend_project_two_rate, tv_homepage_recommend_project_two_term;
    private String projectName_two, tenderMemberCount_two, rate_two, term_two;
    private TextView tv_tender_tips2;

    private ImageView img_poster_homepage, img_poster_icon;
    private ImageView img1, img2;
    private ImageView img_bg_tender_vp, iv_bg_poster_vp;
    private PopupWindow pw;
    private View contentView;
    private LinearLayout posterBg;
//    private LinearLayout layout_mailimg;

    private HomepageBannerAdapter homepageBannerAdapter;

    private HomepagePosterWindowAdapter posterAdapter;

    //广告栏关闭按钮
    private ImageView closeBtn;
    private LinearLayout eventsBg;
    private AnimatorSet startSet, endSet;
    private double preIncome10000;
    private boolean isPull = false;
    private boolean isLogined = false;//登录状态
    private boolean isReward = false;
    private View layout;
    private HomepageRecommendProjectsPresenterImpl homepageRecommendProjectsPresenterImpl;
    private HomepageEventsWindowPresenterImpl homepageEventsBannerPresenterImpl;
    private HomepageBannerPresenterImpl homepageBannerPresenter;
    private List<String> bannerUrls;
    private List<String> posterUrls;
    private LinearLayout bannerContent;
    private FixedSpeedScroller mScroller;
    private int index = 0;
    private List<String> messageList;

    private List<HomepageRecommendProjectsBean.AppBannersBean> appbannersList;
    private List<HomepageRecommendProjectsBean.Posters> postersList;
    private PopupWindow loadingPopupWindow;
    private TranslateAnimation posterTranAniout, posterTranAniin;
    private int page = 0;
    private boolean isShow = false;

    private boolean isFirstIn;
    private AppIsShowInterface appIsShowInterface;


    /**
     * 显示下一页
     */
    private static final int SHOW_NEXT_PAGE = 0;
    private float sceenHeight;

    private Handler posterHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
//                checkMemberType();
            }
        }
    };

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_NEXT_PAGE:
                    showNextPage();
                    break;
            }
        }
    };


    Handler waveHandler = new Handler() {
        public void handleMessage(Message msg) {
            mWaveHelper.start();
            super.handleMessage(msg);
        }
    };


    private KProgressHUD progressHUD;

    private boolean isShowed;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.home_layout, null, false);
        initViews(layout);
        initPosterWindow();

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkIsLogin();
        //请求首页标数据
        requestNetworkData();
        requestPosterImg();
        setIndicatorState();
        if (mAnimator != null) {
            mAnimator.start();
        }
        layout_poster.setVisibility(View.VISIBLE);
//        posterHandler.sendEmptyMessageDelayed(0,1000);
        SharedPreferences preferences = MainApplication.getContext().getSharedPreferences("first_in",
                MODE_PRIVATE);
        boolean isFirstIn = preferences.getBoolean("isFirstIn", true);
        if (isFirstIn) {
            setPupWindow();
        }
        checkMemberType();

    }


    @Override
    public void onStart() {
        super.onStart();
//        waveHandler.sendEmptyMessage(0);
        mWaveHelper.start();

    }

    @Override
    public void onPause() {
        super.onPause();
        mWaveHelper.cancel();
        tv_toast_homepage.clearAnimation();
        pw.dismiss();
        layout_indicator.removeAllViews();
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        vp_homepage_banner_nologin.setCurrentItem(0);
        vp_homepage_events_banner.setCurrentItem(0);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
        } else {
            //相当于Fragment的onPause
        }
    }


    /*检查登录状态，切换banner状态*/
    private void checkIsLogin() {
        isLogined = (boolean) SPUtils.get(MainApplication.getContext(), "LOGIN", "isLogined", false);
        if (isLogined) {

            layout_homepage_nologin.setVisibility(View.GONE);
        } else {
            page = 0;
            layout_homepage_nologin.setVisibility(View.VISIBLE);
//            layout_mailimg.setVisibility(View.GONE);
            mWaveHelper.cancel();
            tv_toast_homepage.clearAnimation();
        }
    }

    /*请求banner数据*/
    private void requestBannerImg() {
        String device = APIBuilder.getDevice();
        Map<String, String> params = new HashMap<>();
        params.put("device", device);
        homepageBannerPresenter.sendRequest(params, null);
    }

    public void checkMemberType() {
        isLogined = (boolean) SPUtils.get(MainApplication.getContext(), "LOGIN", "isLogined", false);

        if (isLogined == true) {

            boolean isFromRegister = (boolean) SPUtils.get(MainApplication.getContext(), "TempIntent", "fromRegister", false);
            if (isFromRegister) {
                layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        displayEventsRewardWindow();
                        SPUtils.clear(MainApplication.getContext(), "TempIntent");
                    }
                }, 1000);
            }
        }

    }

    /*红包弹窗*/
    private void displayEventsRewardWindow() {
        View eventContentView = inflater.inflate(R.layout.layout_event_reward, null);
        final PopupWindow eventsRewardWindow = new PopupWindow(eventContentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        //图片内容
        ImageView content = (ImageView) eventContentView.findViewById(R.id.img_eventreward_content);
        content.setImageResource(R.drawable.img_redpacket_reward);
        //关闭按钮
        ImageView colseBtn = (ImageView) eventContentView.findViewById(R.id.colseimgbtn_eventreward);
        //立即查看 按钮
        Button btn = (Button) eventContentView.findViewById(R.id.btn_eventreward_showdetails);
        btn.setVisibility(View.GONE);
        eventsRewardWindow.setFocusable(true);
        eventsRewardWindow.setOutsideTouchable(false);
        eventsRewardWindow.setTouchable(true);
        eventsRewardWindow.update();
        eventsRewardWindow.setBackgroundDrawable(new ColorDrawable(77000000));
        eventsRewardWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

        colseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsRewardWindow.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //弹出红包、体验金奖励窗口
                        displayExperienceWindow();
                    }
                }, 1000);
            }
        });

    }

    /*体验金弹窗*/
    private void displayExperienceWindow() {
        View eventContentView = inflater.inflate(R.layout.layout_event_reward, null);
        final PopupWindow eventsRewardWindow = new PopupWindow(eventContentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        //图片内容
        ImageView content = (ImageView) eventContentView.findViewById(R.id.img_eventreward_content);
//        content.setImageResource(R.drawable.experince_bg);
        Picasso.with(getActivity()).load(R.drawable.experince_bg).into(content);
        //关闭按钮
        ImageView colseBtn = (ImageView) eventContentView.findViewById(R.id.colseimgbtn_eventreward);
        //立即查看 按钮
        Button btn = (Button) eventContentView.findViewById(R.id.btn_eventreward_showdetails);
        eventsRewardWindow.setFocusable(true);
        eventsRewardWindow.setOutsideTouchable(false);
        eventsRewardWindow.setTouchable(true);
        eventsRewardWindow.update();
        eventsRewardWindow.setBackgroundDrawable(new ColorDrawable(77000000));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                eventsRewardWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
            }
        }, 200);

        colseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsRewardWindow.dismiss();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsRewardWindow.dismiss();
                MyActivityManager.getInstance().startNextActivity(MangoExperienceFinancingActivity.class);
            }
        });

    }


    private void requestNetworkData() {
        isLogined = (boolean) SPUtils.get(MainApplication.getContext(), "LOGIN", "isLogined", false);
        String mid = SPUtils.get(MainApplication.getContext(), "LOGIN", "id", 0) + "";
        Map<String, String> unnecessaryParams = null;
        if (isLogined == true) {
            unnecessaryParams = new HashMap<>();
            unnecessaryParams.put("mid", mid);
            homepageRecommendProjectsPresenterImpl.sendRequest(null, unnecessaryParams);
        } else {
            homepageRecommendProjectsPresenterImpl.sendRequest(null, null);
            requestBannerImg();
        }
    }

    private void initViews(View layout) {
        //屏幕高度
        sceenHeight = GetScreenSize.getSceenHeight(MainApplication.getContext());
        isLogined = (boolean) SPUtils.get(MainApplication.getContext(), "LOGIN", "isLogined", false);
        homepageRecommendProjectsPresenterImpl = new HomepageRecommendProjectsPresenterImpl(this);
        homepageEventsBannerPresenterImpl = new HomepageEventsWindowPresenterImpl(this);
        homepageBannerPresenter = new HomepageBannerPresenterImpl(this);
        inflater = (LayoutInflater) MainApplication.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        waveView = (WaveView) layout.findViewById(R.id.wave_view);

        vp_homepage_banner_nologin = (LoopViewPager) layout.findViewById(R.id.vp_homepage_banner_nologin);
        viewpager_tender_homepage = (ViewPager) layout.findViewById(R.id.viewpager_tender_homepage);
        layout_homepage_nologin = (FrameLayout) layout.findViewById(R.id.layout_homepage_nologin);
        tv_tologin_homepage_unlogin = (TextView) layout.findViewById(R.id.tv_tologin_homepage_unlogin);
        btn_homepage_register = (Button) layout.findViewById(R.id.btn_homepage_register);
        btn_homepage_register.setOnClickListener(this);
        tv_tologin_homepage_unlogin.setOnClickListener(this);
        img_poster_homepage = (ImageView) layout.findViewById(R.id.img_poster_homepage);
        img_poster_icon = (ImageView) layout.findViewById(R.id.img_poster_icon);
        layout_poster = (LinearLayout) layout.findViewById(R.id.layout_poster);
        layout_poster.setOnClickListener(this);

        contentView = inflater.inflate(R.layout.layout_content_events, null);
        eventsBg = (LinearLayout) contentView.findViewById(R.id.layout_bg_events);
        bannerContent = (LinearLayout) contentView.findViewById(R.id.layout_banner_content);
        posterBg = (LinearLayout) contentView.findViewById(R.id.layout_bg_events);


        img1 = (ImageView) layout.findViewById(R.id.img1);
        img2 = (ImageView) layout.findViewById(R.id.img2);

//        img_bg_tender_vp = (ImageView) layout.findViewById(R.id.img_bg_tender_vp);
        iv_bg_poster_vp = (ImageView) contentView.findViewById(R.id.iv_bg_poster_vp);


        layout_toast = (LinearLayout) layout.findViewById(R.id.layout_toast);
        tv_toast_homepage = (TextView) layout.findViewById(R.id.tv_toast_homepage);

        recommendProjectOne = (LinearLayout) inflater.inflate(R.layout.layout_newcomer_tender_homepage, null);
        recommendProjectTwo = (LinearLayout) inflater.inflate(R.layout.layout_jmg_tender_homepage, null);
//        layout_mailimg = (LinearLayout) layout.findViewById(R.id.layout_mailimg);
//        layout_mailimg.setOnClickListener(this);
        tv_homepage_recommend_project_one_name = (TextView) recommendProjectOne.findViewById(R.id.tv_homepage_recommend_project_one_name);
        tv_homepage_recommend_project_one_tenderMemberCount = (TextView) recommendProjectOne.findViewById(R.id.tv_homepage_recommend_project_one_tenderMemberCount);
        tv_homepage_recommend_project_one_rate = (TextView) recommendProjectOne.findViewById(R.id.tv_homepage_recommend_project_one_rate);
        tv_homepage_recommend_project_one_term = (TextView) recommendProjectOne.findViewById(R.id.tv_homepage_recommend_project_one_term);
        tv_tender_tips = (TextView) recommendProjectOne.findViewById(R.id.tv_tender_tips);
        img_bg_tender_vp1 = (ImageView) recommendProjectOne.findViewById(R.id.img_bg_tender_vp1);

        tv_homepage_recommend_project_two_name = (TextView) recommendProjectTwo.findViewById(R.id.tv_homepage_recommend_project_two_name);
        tv_homepage_recommend_project_two_tenderMemberCount = (TextView) recommendProjectTwo.findViewById(R.id.tv_homepage_recommend_project_two_tenderMemberCount);
        tv_homepage_recommend_project_two_rate = (TextView) recommendProjectTwo.findViewById(R.id.tv_homepage_recommend_project_two_rate);
        tv_homepage_recommend_project_two_term = (TextView) recommendProjectTwo.findViewById(R.id.tv_homepage_recommend_project_two_term);
        tv_tender_tips2 = (TextView) recommendProjectTwo.findViewById(R.id.tv_tender_tips2);
        img_bg_tender_vp2 = (ImageView) recommendProjectTwo.findViewById(R.id.img_bg_tender_vp2);

        tenderBannerViews = new ArrayList<>();
        homepageBanners = new ArrayList<>();
        posterBanners = new ArrayList<>();
        codes = new ArrayList<>();
        bannerUrls = new ArrayList<>();
        posterUrls = new ArrayList<>();

        lp = (LinearLayout.LayoutParams) img_poster_homepage.getLayoutParams();

        int[] shadows = {R.drawable.img_bg_banner_vp, R.drawable.img_bg_banner_vp, R.drawable.img_bg_banner_vp};
        homepageBannerAdapter = new HomepageBannerAdapter(MainApplication.getContext(), shadows);
//        homepageBannerAdapter.setShadowImg(shadows);
        int[] posterBg = {R.drawable.img_bg_poster_vp, R.drawable.img_bg_poster_vp, R.drawable.img_bg_poster_vp};
        posterAdapter = new HomepagePosterWindowAdapter(MainApplication.getContext(), posterBg);
        mScroller = new FixedSpeedScroller(getActivity());
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FixedSpeedScroller(vp_homepage_banner_nologin.getContext(), new AccelerateInterpolator());
            mField.set(vp_homepage_banner_nologin, mScroller);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
//        vp_homepage_banner_nologin.setPageTransformer(true, new CubeTransformer());

        initViewPager();
        initWaveView();

    }

    /*波浪动画*/
    private void initWaveView() {
        mWaveHelper = waveView.getWaveHelper();
        int color = Color.WHITE;
        int bebindColor = Color.parseColor("#b7d8eb");
        waveView.setWaveColor(bebindColor, color);

        waveView.setBorder(0, mBorderColor);
        waveView.setShapeType(WaveView.ShapeType.SQUARE);

    }

    /*
    * 设置推荐标viewpager
    * */
    @TargetApi(Build.VERSION_CODES.DONUT)
    private void initViewPager() {

        tenderBannerViews.add(recommendProjectOne);
        tenderBannerViews.add(recommendProjectTwo);
        tenderAdapter = new SimpleViewPagerAdapter(tenderBannerViews);

        viewpager_tender_homepage.setAdapter(tenderAdapter);
        viewpager_tender_homepage.addOnPageChangeListener(new HomepageViewPagerListener(img1, img2));
    }


    /*
     * 初始化viewpager的indicator的状态(左边宽，右边窄）
     * */
    private void setIndicatorState() {

        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) img1.getLayoutParams();
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) img2.getLayoutParams();
        int w1 = ScreenUtils.dipToPx(getActivity(), 50);
        int w2 = ScreenUtils.dipToPx(getActivity(), 25);
        params1.width = w1;
        params2.width = w2;
        img1.setLayoutParams(params1);
        img2.setLayoutParams(params2);

    }

    /*
    * 滚动文字动画
    * */

    public void setToastAni() {
        float currentY = 0;
        float toastPoint = sceenHeight / 3 + 200;

        ObjectAnimator startAlphaAnimator = ObjectAnimator.ofFloat(tv_toast_homepage, "alpha", 0f, 1f);
        ObjectAnimator startTranslationAnimator = ObjectAnimator.ofFloat(tv_toast_homepage, "translationY", toastPoint, currentY);
        startSet = new AnimatorSet();
        startSet.setDuration(1000);
        startSet.setupEndValues();
        startSet.play(startAlphaAnimator).with(startTranslationAnimator);
        startSet.setInterpolator(new OvershootInterpolator(0.2f));
        startSet.addListener(new HomepageAnimationListener());
        startSet.start();
        ObjectAnimator endAlphaAnimator = ObjectAnimator.ofFloat(tv_toast_homepage, "alpha", 1f, 0f);
        ObjectAnimator endTranslationAnimator = ObjectAnimator.ofFloat(tv_toast_homepage, "translationY", 0, -toastPoint);
        endSet = new AnimatorSet();
        endSet.setStartDelay(5000);
        endSet.setDuration(500);
        endSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                index++;
                if (isLogined) {
                    if (index >= messageList.size()) {
                        index = 0;
                    }
                    tv_toast_homepage.setText(messageList.get(index));
                    startSet.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        endSet.play(endAlphaAnimator).with(endTranslationAnimator);


    }

    /*
    * 点击事件
    * */
    @Override
    public void onClick(View v) {
//        if (v == layout_mailimg) {
//            MyActivityManager.getInstance().startNextActivity(AppMessageCenterActivity.class);
//        } else
        if (v == layout_poster) {
            setPupWindow();
            setEventsContentAni();
            layout_poster.startAnimation(animationOut);
            mAnimator.cancel();
            layout_poster.setVisibility(View.GONE);
        } else if (v == closeBtn) {
            layout_poster.setVisibility(View.VISIBLE);
            layout_poster.startAnimation(animationIn);
            mAnimator.start();
            setEventsExitAni();
        } else if (v == tv_tologin_homepage_unlogin) {
            MyActivityManager.getInstance().startNextActivity(LoginActivity.class);
        } else if (v == btn_homepage_register) {
            MyActivityManager.getInstance().startNextActivity(RegisterActivityStepOne.class);
        }
    }

    /*
   * 活动弹窗进入动画
   * */
    private void setEventsContentAni() {
        ObjectAnimator posterInTranslationAnimator = ObjectAnimator.ofFloat(vp_homepage_events_banner, "translationY", -sceenHeight, 0);
        posterInTranslationAnimator.setDuration(600);

        ObjectAnimator posterInAlphaAnimator = ObjectAnimator.ofFloat(closeBtn, "alpha", 0.0f, 1.0f);
        posterInAlphaAnimator.setDuration(600);
//        posterInAlphaAnimator.start();

        AnimatorSet set = new AnimatorSet();
        set.play(posterInTranslationAnimator).with(posterInAlphaAnimator);
//        set.setupEndValues();
        set.setDuration(800);
        set.start();
    }

    /*
    * 活动弹窗退出动画
    * */
    private void setEventsExitAni() {
        ObjectAnimator posterQuitTranslationAnimator = ObjectAnimator.ofFloat(vp_homepage_events_banner, "translationY", 0, -2 * sceenHeight);
        posterQuitTranslationAnimator.setDuration(600);
        ObjectAnimator posterQuitAlphaAnimator = ObjectAnimator.ofFloat(closeBtn, "alpha", 1.0f, 0.0f);
        posterQuitAlphaAnimator.setDuration(600);
//        posterQuitAlphaAnimator.start();
        AnimatorSet set = new AnimatorSet();
        set.play(posterQuitTranslationAnimator).with(posterQuitAlphaAnimator);
//        set.setupEndValues();
        set.setDuration(800);
        set.start();
        set.addListener(this);
    }

    /*
    * 配置活动弹窗
    * */
    public void setPupWindow() {
//        requestPosterImg();
        pw.update();
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                SharedPreferences preferences = getActivity().getSharedPreferences(
                        "first_in", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isFirstIn", false);
                editor.apply();
                vp_homepage_events_banner.setCurrentItem(0);
            }
        });
        layout.postDelayed(new Runnable() {
            @Override
            public void run() {
                pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
            }
        }, 500);
    }

    /*初始化拉杆布局*/
    private void initPosterWindow() {
        View contentView = inflater.inflate(R.layout.layout_content_events, null);
        layout_indicator = (LinearLayout) contentView.findViewById(R.id.layout_indicator);

        vp_homepage_events_banner = (ViewPager) contentView.findViewById(R.id.vp_homepage_events_banner);
//        vp_homepage_events_banner.setPageMargin(0);
        vp_homepage_events_banner.setCurrentItem(0);
        vp_homepage_events_banner.setAdapter(posterAdapter);

//        setEventsContentAni();
        closeBtn = (ImageView) contentView.findViewById(img_closebtn);
        closeBtn.setOnClickListener(this);
        pw = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        pw.setTouchable(true);
        pw.setBackgroundDrawable(new ColorDrawable(88000000));
        pw.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        setPosterAni();

        animationOut = new TranslateAnimation(0, 0, 0, -sceenHeight / 6);
        animationOut.setDuration(300);

        animationIn = new TranslateAnimation(0, 0, -sceenHeight / 6, 0);
        animationIn.setDuration(300);

    }

    private void setPosterAni() {
        //1.调用ofInt(int...values)方法创建ValueAnimator对象
        mAnimator = ValueAnimator.ofFloat(sceenHeight / 7, sceenHeight / 10);
        //2.为目标对象的属性变化设置监听器


        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 3.为目标对象的属性设置计算好的属性值
                float animatorValue = (float) animation.getAnimatedValue();
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) img_poster_homepage.getLayoutParams();
                lp.height = (int) animatorValue;
                img_poster_homepage.setLayoutParams(lp);
            }
        });
        //4.设置动画的持续时间、是否重复及重复次数等属性
        mAnimator.setDuration(650);
        mAnimator.setRepeatCount(-1);
        mAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        //5.为ValueAnimator设置目标对象并开始执行动画
        mAnimator.start();
    }

    private void initPosterBannerIndicator() {
        vp_homepage_events_banner.addOnPageChangeListener(this);
        LinearLayout.LayoutParams indicatorLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < posterUrls.size(); i++) {
            ImageView indicator = new ImageView(MainApplication.getContext());
            if (i == 0) {
                indicatorLp.setMargins(0, 0, 12, 0);
                indicator.setBackgroundResource(R.drawable.shape_indicator_y);
            } else {
                indicator.setBackgroundResource(R.drawable.shape_indicator_n);
            }
            layout_indicator.addView(indicator, indicatorLp);
        }
    }


    /*请求海报数据*/
    private void requestPosterImg() {
        String device = APIBuilder.getDevice();
        Map<String, String> params = new HashMap<>();
        params.put("device", device);
        homepageEventsBannerPresenterImpl.sendRequest(params, null);
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
    public void showError(OnPresenterListener listener, HomepageRecommendProjectsBean homepageRecommendProjectsBean) {
        CommonToastUtils.showToast(getActivity(), homepageRecommendProjectsBean.getMsg());
    }

    @Override
    public void responseData(OnPresenterListener listener, HomepageRecommendProjectsBean homepageRecommendProjectsBean) {
//        img_bg_tender_vp.setVisibility(View.GONE);
        iv_bg_poster_vp.setVisibility(View.GONE);
        img_bg_tender_vp1.setVisibility(View.GONE);
        img_bg_tender_vp2.setVisibility(View.GONE);
        if (listener instanceof HomepageRecommendProjectsPresenterImpl) {
            codes.clear();
            //首页推荐标\
            String hqbCode = homepageRecommendProjectsBean.getHqb().getCode();
            String jmgCode = homepageRecommendProjectsBean.getLoan().getCode();
            codes.add(hqbCode);
            codes.add(jmgCode);
            tenderAdapter.setCodes(codes);

            projectName_one = homepageRecommendProjectsBean.getHqb().getTitle();
            tenderMemberCount_one = homepageRecommendProjectsBean.getHqb().getTenderMemberCount() + "";
            rate_one = homepageRecommendProjectsBean.getHqb().getRate() + "";
            term_one = homepageRecommendProjectsBean.getHqb().getPeriod();
            setRecommendProjectOneTextData();

            projectName_two = homepageRecommendProjectsBean.getLoan().getTitle();
            rate_two = (homepageRecommendProjectsBean.getLoan().getRate()) + "";
            term_two = homepageRecommendProjectsBean.getLoan().getPeriod() + "";
            tenderMemberCount_two = homepageRecommendProjectsBean.getLoan().getTenderMemberCount() + "";
            preIncome10000 = homepageRecommendProjectsBean.getLoan().getPreIncome10000();
            setRecommendProjectTwoTextData(homepageRecommendProjectsBean);
            messageList = homepageRecommendProjectsBean.getMessageList();
            if (messageList != null) {
                index = 0;

                tv_toast_homepage.setText(messageList.get(index));
                setToastAni();
            }

        } else if (listener instanceof HomepageBannerPresenterImpl) {

            //banner
            appbannersList = homepageRecommendProjectsBean.getAppbannersList();
            SPUtils.put(getActivity(), "BannerSize", "size", appbannersList.size());
            bannerUrls.clear();
            if (!isLogined) {
                for (int i = 0; i < appbannersList.size(); i++) {
                    String bannerImgUrl = appbannersList.get(i).getImage_url();
                    bannerUrls.add(bannerImgUrl);
                }
                initHomepageBanner(appbannersList);
            }
        } else if (listener instanceof HomepageEventsWindowPresenterImpl) {
            //poster
            posterUrls.clear();
            postersList = homepageRecommendProjectsBean.getPosters();
            for (int i = 0; i < postersList.size(); i++) {
                String posterImgUrl = postersList.get(i).getImage_url();
                posterUrls.add(posterImgUrl);
            }
            initPosterImg(postersList);
        }
    }

    /*未登录状态下 banner数据*/
    private void initHomepageBanner(List<HomepageRecommendProjectsBean.AppBannersBean> appbannersList) {
//        if (homepageBannerAdapter != null) {
        int size = (int) SPUtils.get(getActivity(), "BannerSize", "size", -1);
        if (bannerUrls.size() != 0) {
            homepageBannerAdapter.setData(MainApplication.getContext(), bannerUrls, this.appbannersList);
            if (vp_homepage_banner_nologin.getAdapter() == null) {

                vp_homepage_banner_nologin.setAdapter(homepageBannerAdapter);
            }
            vp_homepage_banner_nologin.setCurrentItem(vp_homepage_banner_nologin.getAdapter().getCount() / 2);  // 滑到一半的地方
            //切换速度
            mScroller.setmDuration(500);
            // 3秒钟后显示下一页
            homepageBannerAdapter.notifyDataSetChanged();
//            handler.postDelayed(task, 3000);
            handler.removeMessages(SHOW_NEXT_PAGE);
            handler.sendEmptyMessageDelayed(SHOW_NEXT_PAGE, 3000);
//            }
        }
    }

    /**
     * 显示下一页
     */
    public void showNextPage() {
        if (page > bannerUrls.size() - 1) {
            page = 0;
        } else {
            page += 1;
        }
        vp_homepage_banner_nologin.setCurrentItem(page);

//        int currentItem = vp_homepage_banner_nologin.getCurrentItem();  // 获取ViewPager当前显示的是哪一页
//        if (currentItem == bannerUrls.size() - 1) {
//            vp_homepage_banner_nologin.setCurrentItem(0);
//        } else {
//            vp_homepage_banner_nologin.setCurrentItem(currentItem + 1);
//        }
        handler.sendEmptyMessageDelayed(SHOW_NEXT_PAGE, 3000);
    }

    /*海报数据*/
    private void initPosterImg(List<HomepageRecommendProjectsBean.Posters> postersList) {

        posterAdapter.setData(MainApplication.getContext(), posterUrls, postersList);

        posterAdapter.notifyDataSetChanged();
        //海报指示点
        if (layout_indicator.getChildCount() == 0) {
            initPosterBannerIndicator();
        }
    }

    /*推荐标1*/
    private void setRecommendProjectOneTextData() {
        tv_tender_tips.setText("产品特色");
        tv_homepage_recommend_project_one_name.setText(projectName_one);
        tv_homepage_recommend_project_one_tenderMemberCount.setText("一元起投,逐月加息");
        tv_homepage_recommend_project_one_rate.setText("7~11");
        tv_homepage_recommend_project_one_term.setText(term_one);
    }

    /*推荐标2*/
    private void setRecommendProjectTwoTextData(HomepageRecommendProjectsBean homepageRecommendProjectsBean) {
        String monthCount = homepageRecommendProjectsBean.getLoan().getPeriod() + "";
        if (monthCount.equalsIgnoreCase("1")) {
            tv_tender_tips2.setText("投资人数");
            tv_homepage_recommend_project_two_tenderMemberCount.setText("已有" + tenderMemberCount_two + "人加入");
            tv_homepage_recommend_project_two_tenderMemberCount.setTextSize(14);
        } else {
            tv_tender_tips2.setText("万元收益(元)");
            tv_homepage_recommend_project_two_tenderMemberCount.setText(new DecimalFormat("###,###,##0.00").format(preIncome10000));
        }
        tv_homepage_recommend_project_two_name.setText(projectName_two);
        tv_homepage_recommend_project_two_rate.setText(rate_two);
        tv_homepage_recommend_project_two_term.setText(term_two);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (posterUrls == null) {
            return;
        } else {
            for (int i = 0; i < posterUrls.size(); i++) {
                if (layout_indicator != null && layout_indicator.getChildAt(i) != null) {
                    if (posterUrls.size() != 0) {
                        if (position == i) {
                            layout_indicator.getChildAt(i).setBackgroundResource(R.drawable.shape_indicator_y);
                        } else {
                            layout_indicator.getChildAt(i).setBackgroundResource(R.drawable.shape_indicator_n);
                        }
                    }
                }
            }
        }
    }

    /*广告窗退出动画监听*/

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        pw.dismiss();
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }


    public class HomepageAnimationListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

            endSet.start();

        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

    public interface AppIsShowInterface {

        void isShow(boolean isShow);

    }

}
