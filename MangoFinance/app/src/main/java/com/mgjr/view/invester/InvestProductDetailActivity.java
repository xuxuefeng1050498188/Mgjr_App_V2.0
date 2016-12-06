package com.mgjr.view.invester;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.SPUtils;
import com.mgjr.Utils.ScreenUtils;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.model.bean.EventBusBean.InvestAmountBean;
import com.mgjr.model.bean.HqbBean;
import com.mgjr.model.bean.InvestProductDetailBean;
import com.mgjr.model.bean.LoanBean;
import com.mgjr.presenter.impl.InvestProductDetailPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.InvestScaleMoney;
import com.mgjr.share.SocialClient;
import com.mgjr.share.SocialDialog;
import com.mgjr.share.WeiboShareActivity;
import com.mgjr.view.common.CommonWebViewActivity;
import com.mgjr.view.common.LoginActivity;
import com.mgjr.view.listeners.ViewListener;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.mgjr.R.id.rl_jmg_investing;

public class InvestProductDetailActivity extends ActionbarActivity implements ViewListener<InvestProductDetailBean>, IUiListener, IWeiboHandler.Response, SocialDialog.ClickListenerInterface {

    private static final int REQUEST_CODE_ASK_CALL_PHONE = 0;
    @InjectView(R.id.tv_invest_jmg_product_rate)
    TextView tvInvestJmgProductRate;
    @InjectView(R.id.tv_invest_jmg_product_detail_min_purchase_amount)
    TextView tvInvestJmgProductDetailMinPurchaseAmount;
    @InjectView(R.id.tv_invest_jmg_product_detail_deadline)
    TextView tvInvestJmgProductDetailDeadline;
    @InjectView(R.id.tv_invest_jmg_product_detail_available_amount)
    TextView tvInvestJmgProductDetailAvailableAmount;
    @InjectView(R.id.tv_invest_jmg_product_detail_endtime)
    TextView tvInvestJmgProductDetailEndtime;
    @InjectView(R.id.tv_invest_jmg_product_detail_sum_amount)
    TextView tvInvestJmgProductDetailSumAmount;
    @InjectView(R.id.tv_invest_jmg_product_detail_payback_type)
    TextView tvInvestJmgProductDetailPaybackType;
    @InjectView(R.id.tv_invest_jmg_product_detail_guarantee_organization)
    TextView tvInvestJmgProductDetailGuaranteeOrganization;
    @InjectView(R.id.iv_user_touxiang)
    ImageView ivTouzi;
    @InjectView(R.id.rl_jmg_project_detail)
    RelativeLayout rlJmgProjectDetail;
    @InjectView(R.id.iv_jieshao)
    ImageView ivJieshao;
    @InjectView(R.id.rl_jmg_invest_record)
    RelativeLayout rlJmgInvestRecord;
    @InjectView(R.id.iv_xinxi)
    ImageView ivXinxi;
    @InjectView(R.id.rl_jmg_common_question)
    RelativeLayout rlJmgCommonQuestion;
    @InjectView(R.id.tv_invest_jmg_product_detail_barrage)
    TextView tvInvestJmgProductDetailBarrage;
    @InjectView(R.id.rl_invest_animationbar)
    LinearLayout rlInvestAnimationbar;
    @InjectView(R.id.pb_invest_goldmango_detail)
    ProgressBar pbInvestGoldmangoDetail;
    @InjectView(R.id.tv_invest_goldmango_detail_pbvalue)
    TextView tvInvestGoldmangoDetailPbvalue;
    @InjectView(R.id.ib_jmg_calc)
    ImageButton ibJmgCalc;
    @InjectView(rl_jmg_investing)
    RelativeLayout rlJmgInvesting;
    @InjectView(R.id.tv_invest_jmg_total_invest_record)
    TextView tvInvestJmgTotalInvestRecord;
    @InjectView(R.id.invest_product_detail_bigpicture)
    LinearLayout investProductDetailBigpicture;
    @InjectView(R.id.invest_product_detail_second_jmg)
    LinearLayout investProductDetailSecondJmg;
    @InjectView(R.id.tv_invest_newuser_product_detail_sumaccount)
    TextView tvInvestNewuserProductDetailSumaccount;
    @InjectView(R.id.tv_invest_newuser_product_detail_limitacount)
    TextView tvInvestNewuserProductDetailLimitacount;
    @InjectView(R.id.tv_invest_newuser_product_detail_paidnum)
    TextView tvInvestNewuserProductDetailPaidnum;
    @InjectView(R.id.invest_product_detail_second_newuser)
    LinearLayout investProductDetailSecondNewuser;
    @InjectView(R.id.tv_invest_hqb_product_detail_loan_sumaccount)
    TextView tvInvestHqbProductDetailLoanSumaccount;
    @InjectView(R.id.tv_invest_hqb_product_detail_calc_interest)
    TextView tvInvestHqbProductDetailCalcInterest;
    @InjectView(R.id.tv_invest_hqb_product_detail_add_interest)
    TextView tvInvestHqbProductDetailAddInterest;
    @InjectView(R.id.invest_product_detail_second_hqb)
    LinearLayout investProductDetailSecondHqb;
    @InjectView(R.id.invest_jmg_product_detail_third_layout)
    LinearLayout investJmgProductDetailThirdLayout;
    @InjectView(R.id.iv_touzi1)
    ImageView ivTouzi1;
    @InjectView(R.id.rl_hqb_buy_redeem_rules)
    RelativeLayout rlHqbBuyRedeemRules;
    @InjectView(R.id.iv_jieshao2)
    ImageView ivJieshao2;
    @InjectView(R.id.rl_hqb_product_introduce)
    RelativeLayout rlHqbProductIntroduce;
    @InjectView(R.id.iv_jieshao1)
    ImageView ivJieshao1;
    @InjectView(R.id.tv_invest_hqb_total_invest_record)
    TextView tvInvestHqbTotalInvestRecord;
    @InjectView(R.id.rl_hqb_invest_record)
    RelativeLayout rlHqbInvestRecord;
    @InjectView(R.id.iv_xinxi1)
    ImageView ivXinxi1;
    @InjectView(R.id.rl_hqb_common_question)
    RelativeLayout rlHqbCommonQuestion;
    @InjectView(R.id.invest_hqb_product_detail_third_layout)
    LinearLayout investHqbProductDetailThirdLayout;
    @InjectView(R.id.iv_touzi2)
    ImageView ivTouzi2;
    @InjectView(R.id.rl_newuser_product_introduction)
    RelativeLayout rlNewuserProductIntroduction;
    @InjectView(R.id.iv_jieshao3)
    ImageView ivJieshao3;
    @InjectView(R.id.rl_newuser_product_detail)
    RelativeLayout rlNewuserProductDetail;
    @InjectView(R.id.iv_jieshao4)
    ImageView ivJieshao4;
    @InjectView(R.id.tv_invest_newuser_total_invest_record)
    TextView tvInvestNewuserTotalInvestRecord;
    @InjectView(R.id.rl_newuser_invest_record)
    RelativeLayout rlNewuserInvestRecord;
    @InjectView(R.id.iv_xinxi2)
    ImageView ivXinxi2;
    @InjectView(R.id.rl_newuser_common_question)
    RelativeLayout rlNewuserCommonQuestion;
    @InjectView(R.id.invest_newuser_product_detail_third_layout)
    LinearLayout investNewuserProductDetailThirdLayout;
    @InjectView(R.id.invest_product_footlayout)
    LinearLayout investProductFootlayout;
    @InjectView(R.id.ib_invest_record_right_arrow)
    ImageButton ibInvestRecordRightArrow;
    @InjectView(R.id.tv_investing_text)
    TextView tvInvestingText;
    @InjectView(R.id.tv_percent)
    TextView tvPercent;
    @InjectView(R.id.fm_secend_area)
    FrameLayout fmSecendArea;
    @InjectView(R.id.fm_third_area)
    FrameLayout fmThirdArea;

    private float startX;
    private float endX;
    private String code;
    private String type;

    private InvestProductDetailPresenterImpl investProductDetailPresenterImpl;
    private InvestProductDetailBean investProductDetailBean;
    private LoanBean loanBean;
    private List<String> barrageList;
    //投资记录条的
    private int index = 0;
    private ObjectAnimator barrageDownAnim;
    private ObjectAnimator barrageUpAnim;
    private float progressValue;
    private HqbBean hqbBean;
    private double investRate;

    //1:新手福利标 2:活期宝 3:金芒果
    private int investType = 1;
    private CountDownTimer countDownTimer;
    private boolean isCanInvest;

    private String shareTitle;
    private String shareContent;
    private String shareUrl;
    private ValueAnimator pbAnim;

    private SocialClient socialClient;
    private SocialDialog socialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.invest_product_detail, this);
        ButterKnife.inject(this);
        investProductDetailPresenterImpl = new InvestProductDetailPresenterImpl(this);
        //拿到投资列表传递传递过来的code,和用来区分是活期宝还是金芒果的type
        code = getIntent().getStringExtra("code");
        type = getIntent().getStringExtra("type");
        socialClient = new SocialClient(this);

        initActionBar();
        initHqbSurface();

    }

    private void initActionBar() {
        if ("0".equals(type)) {
            actionbar.setBackgroundColor(Color.parseColor("#23aff8"));
        } else {
            actionbar.setBackgroundColor(Color.parseColor("#FFA800"));
        }
        actionbar.leftImageView.setImageResource(R.drawable.invest_left_arrow);
        actionbar.setCenterTextColor(Color.parseColor("#ffffff"));

//        actionbar.rightImageView.setImageResource(R.drawable.phone_call_logo);
//        actionbar.rightImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                View rootView = LayoutInflater.from(InvestProductDetailActivity.this).inflate(R.layout.invest_product_detail, null);
//                showCallPopw(InvestProductDetailActivity.this, rootView);
//            }
//        });

        /*分享*/
        actionbar.rightImageView.setImageResource(R.drawable.invest_share_logo);
        actionbar.rightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socialDialog = new SocialDialog(InvestProductDetailActivity.this);
                socialDialog.setClicklistener(InvestProductDetailActivity.this);
                socialDialog.show();
            }
        });
        actionbar.secondRightImageView.setImageResource(R.drawable.phone_call_logo);
        actionbar.secondRightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View rootView = LayoutInflater.from(InvestProductDetailActivity.this).inflate(R.layout.invest_product_detail, null);
                showCallPopw(InvestProductDetailActivity.this, rootView);
            }
        });
    }

    /**
     * 一进来先初始化活期宝的背景颜色
     */
    private void initHqbSurface() {
        if ("0".equals(type)) {
            actionbar.setCenterTextView("活期宝");
//            investProductActionBar.setBackgroundColor(getResources().getColor(R.color.invest_hqb_product_detail_bg_color));
            investProductDetailBigpicture.setBackgroundColor(getResources().getColor(R.color.invest_hqb_product_detail_bg_color));
            tvInvestJmgProductDetailBarrage.setTextColor(getResources().getColor(R.color.invest_hqb_product_detail_bg_color));
            rlInvestAnimationbar.setBackgroundColor(getResources().getColor(R.color.invest_hqb_product_detail_scroll_color));
            rlJmgInvesting.setBackgroundColor(getResources().getColor(R.color.invest_hqb_product_detail_bg_color));
            investProductFootlayout.setBackgroundResource(R.drawable.shape_invest_blue_border);
            ibInvestRecordRightArrow.setImageResource(R.drawable.ib_invest_record_right_arrow);
            tvInvestingText.setText("立即转入");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        networkRequest();
    }

    private void networkRequest() {
        Map<String, String> necessaryParams = new HashMap<String, String>();
        necessaryParams.put("code", code);
        necessaryParams.put("type", type);
        investProductDetailPresenterImpl.sendRequest(necessaryParams, null);
    }


    private void setProgressBar() {
        final TextView tv_invest_goldmango_detail = (TextView) findViewById(R.id.tv_invest_goldmango_detail_pbvalue);
        final ProgressBar pb_invest_goldmango_detail = (ProgressBar) findViewById(R.id.pb_invest_goldmango_detail);
        final int screenWidth = ScreenUtils.getScreenWidth(this);// 屏幕宽度
        //进度条开始位置
        startX = 0;

        tv_invest_goldmango_detail.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.i("---", tv_invest_goldmango_detail.getMeasuredWidth() + "");
                endX = screenWidth - tv_invest_goldmango_detail.getMeasuredWidth();
                tv_invest_goldmango_detail.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);
                progressBarAnim(tv_invest_goldmango_detail, pb_invest_goldmango_detail);
            }
        });
    }

    //投资滚动条向下的动画
    private void barrageDownAnim() {
        barrageDownAnim = ObjectAnimator.ofFloat(rlInvestAnimationbar, "translationY", -200f, rlInvestAnimationbar.getTranslationY());
        barrageDownAnim.setStartDelay(2000);
        barrageDownAnim.setDuration(1000);
        barrageDownAnim.start();
        barrageDownAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                rlInvestAnimationbar.setVisibility(View.VISIBLE);
                if (barrageList != null) {
                    tvInvestJmgProductDetailBarrage.setText(barrageList.get(index++));
                    if (index == barrageList.size()) {
                        index = 0;
                    }
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                barrageUpAnim();
            }
        });
    }

    //投资滚动条向上的动画
    private void barrageUpAnim() {
        barrageUpAnim = ObjectAnimator.ofFloat(rlInvestAnimationbar, "translationY", rlInvestAnimationbar.getTranslationY(), -200f);
        barrageUpAnim.setStartDelay(5000);
        barrageUpAnim.setDuration(1000);
        barrageUpAnim.start();
        barrageUpAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                barrageDownAnim.start();
            }
        });
    }


    /**
     * 进度条动画
     *
     * @param tv_invest_goldmango_detail
     * @param pb_invest_goldmango_detail
     */
    private void progressBarAnim(final TextView tv_invest_goldmango_detail, final ProgressBar pb_invest_goldmango_detail) {
        //假设进度条到50%
        progressValue = (float) ((endX - startX) * investRate);
        pbAnim = ValueAnimator.ofFloat(startX, progressValue);
        pbAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float animatedValue = (Float) animation.getAnimatedValue();
                tv_invest_goldmango_detail.setTranslationX(animatedValue);

                //整个动画完成进度的百分比
                int progress = (int) (animation.getAnimatedFraction() * 100 * investRate);
                tv_invest_goldmango_detail.setText(progress + "%");
                pb_invest_goldmango_detail.setProgress(progress);
            }
        });
        pbAnim.setStartDelay(500);
        pbAnim.setDuration(1000);
        pbAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        pbAnim.start();

    }


    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @Override
    public void showError() {
        ibJmgCalc.setClickable(false);
        rlJmgInvesting.setClickable(false);
        dismissLoadingDialog();
        showSystemError();
    }

    @Override
    public void showError(OnPresenterListener listener, InvestProductDetailBean investProductDetailBean) {
        ibJmgCalc.setClickable(false);
        rlJmgInvesting.setClickable(false);
        dismissLoadingDialog();
        CommonToastUtils.showToast(this, investProductDetailBean.getMsg());
    }

    @Override
    public void responseData(OnPresenterListener listener, InvestProductDetailBean investProductDetailBean) {
        this.investProductDetailBean = investProductDetailBean;

        shareContent = investProductDetailBean.getShareContent();
        shareTitle = investProductDetailBean.getShareTitle();
        shareUrl = investProductDetailBean.getShareUrl();

        barrageList = investProductDetailBean.getBarrageList();
        if (barrageList != null) {
            tvInvestJmgProductDetailBarrage.setText(barrageList.get(0));
        }
        //type  0:代表活期宝  1:代表金芒果或者秒杀标
        if ("0".equals(type)) {
            hqbBean = investProductDetailBean.getHqb();
            //绑定活期宝的数据
            bindHqbDetailData();
            investType = 2;
            isCanInvest = true;
        } else {
            //活期宝和金芒果(包括新手福利标)分别绑定数据
            loanBean = investProductDetailBean.getLoan();
            //先绑定相同样式的数据
            bindJmgAndNewuserCommonData();
            //绑定金芒果和新手福利标数据
            if (investProductDetailBean.isNewcomerWelfare()) {
                //新手福利标
                bindNewUserDetailData();
                investType = 1;
                isCanInvest = true;
            } else {
                //金芒果
                bindJmgDetailData();
                investType = 3;
                setInvestButtonText(loanBean);
            }
        }
        //投资记录提示动画
        if (barrageList != null) {
            if (barrageDownAnim == null) {
                barrageDownAnim();
            }
        }
        //进度条
        setProgressBar();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (barrageUpAnim != null) {
            barrageUpAnim.cancel();
        }
        if (barrageDownAnim != null) {
            barrageDownAnim.cancel();
        }
    }

    private void setInvestButtonText(final LoanBean loanBean) {
        //遍历集合
        if (loanBean.getTypeid() > 10 && loanBean.getStatus() == 2 && loanBean.getBstime() > System.currentTimeMillis()) {
            //Typeid>10,代表是秒杀标
            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            //变成可投资的标
            long currentTimeMillis = System.currentTimeMillis();
            countDownTimer = new CountDownTimer(Math.abs(loanBean.getBstime() - currentTimeMillis), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvInvestingText.setText("" + toClock(millisUntilFinished) + "后开抢");
//                    loanBean.setBstime(millisUntilFinished);
                    rlJmgInvesting.setClickable(false);
                }

                @Override
                public void onFinish() {
                    //变成可投资的标
                    networkRequest();
                    rlJmgInvesting.setClickable(true);
                }
            };
            countDownTimer.start();
//            tvInvestingText.setText(seckillTime);

        } else {
            if (loanBean.getStatus() == 2) {
                //立即投资
                tvInvestingText.setText("立即投资");
                isCanInvest = true;
            } else if (loanBean.getStatus() == 3) {
                //满标
                tvInvestingText.setText("已满标");
                rlJmgInvesting.setClickable(false);
                rlJmgInvesting.setBackgroundColor(Color.parseColor("#f8cd79"));
                investProductFootlayout.setBackgroundResource(R.drawable.shape_invest_light_orange_border);
            } else if (loanBean.getStatus() == 100) {
                //还款中
                tvInvestingText.setText("还款中");
                rlJmgInvesting.setClickable(false);
                rlJmgInvesting.setBackgroundColor(Color.parseColor("#f8cd79"));
                investProductFootlayout.setBackgroundResource(R.drawable.shape_invest_light_orange_border);
            } else if (loanBean.getStatus() == 200) {
                //已还款
                tvInvestingText.setText("已还款");
                rlJmgInvesting.setClickable(false);
                rlJmgInvesting.setBackgroundColor(Color.parseColor("#cecece"));
                investProductFootlayout.setBackgroundResource(R.drawable.shape_invest_gray_border);
            }
        }
    }

    public String toClock(long millisUntilFinished) {
        long hour = millisUntilFinished / (60 * 60 * 1000);
        long minute = (millisUntilFinished - hour * 60 * 60 * 1000) / (60 * 1000);
        long second = (millisUntilFinished - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000;
        if (second >= 60) {
            second = second % 60;
            minute += second / 60;
        }
        if (minute >= 60) {
            minute = minute % 60;
            hour += minute / 60;
        }
        String sh = "";
        String sm = "";
        String ss = "";
        if (hour < 10) {
            sh = "0" + String.valueOf(hour);
        } else {
            sh = String.valueOf(hour);
        }
        if (minute < 10) {
            sm = "0" + String.valueOf(minute);
        } else {
            sm = String.valueOf(minute);
        }
        if (second < 10) {
            ss = "0" + String.valueOf(second);
        } else {
            ss = String.valueOf(second);
        }
        return sh + ":" + sm + ":" + ss;
    }

    private void bindHqbDetailData() {
        fmSecendArea.setVisibility(View.VISIBLE);
        fmThirdArea.setVisibility(View.VISIBLE);
        //设置整个样式
        /*investProductActionBar.setBackgroundColor(getResources().getColor(R.color.invest_hqb_product_detail_bg_color));
        investProductDetailBigpicture.setBackgroundColor(getResources().getColor(R.color.invest_hqb_product_detail_bg_color));
        tvInvestJmgProductDetailBarrage.setTextColor(getResources().getColor(R.color.invest_hqb_product_detail_bg_color));
        rlInvestAnimationbar.setBackgroundColor(getResources().getColor(R.color.invest_hqb_product_detail_scroll_color));
        rlJmgInvesting.setBackgroundColor(getResources().getColor(R.color.invest_hqb_product_detail_bg_color));
        investProductFootlayout.setBackgroundResource(R.drawable.shape_invest_blue_border);*/
        tvInvestJmgProductRate.setText("" + investProductDetailBean.getHbqRate());
        tvPercent.setVisibility(View.VISIBLE);
        DecimalFormat df = new DecimalFormat("###,###,###");
        tvInvestJmgProductDetailMinPurchaseAmount.setText("" + df.format(hqbBean.getMint()));
        tvInvestJmgProductDetailDeadline.setText("" + investProductDetailBean.getTenderTerm());
        tvInvestJmgProductDetailAvailableAmount.setText("" + df.format(hqbBean.getBalance()));

        //设置第二部分的显示隐藏
        investProductDetailSecondHqb.setVisibility(View.VISIBLE);
        investProductDetailSecondNewuser.setVisibility(View.GONE);
        investProductDetailSecondJmg.setVisibility(View.GONE);

        //设置第三部分的显示隐藏
        investHqbProductDetailThirdLayout.setVisibility(View.VISIBLE);
        investNewuserProductDetailThirdLayout.setVisibility(View.GONE);
        investJmgProductDetailThirdLayout.setVisibility(View.GONE);

        //项目总额
        DecimalFormat df1 = new DecimalFormat("###,###,##0.00");
        tvInvestHqbProductDetailLoanSumaccount.setText("" + df1.format(hqbBean.getAmount()) + "元");
        //计息方式
        tvInvestHqbProductDetailCalcInterest.setText("" + investProductDetailBean.getIncomeType());
        //加息方式
        tvInvestHqbProductDetailAddInterest.setText("" + investProductDetailBean.getIncomeIncreaseType());
        //投资记录
        int tenderCount = investProductDetailBean.getTenderCount();
        tvInvestHqbTotalInvestRecord.setText(tenderCount != 0 ? tenderCount + "" : "");
        //拿到投资进度
        investRate = 1 - (hqbBean.getBalance() / hqbBean.getAmount());

    }


    /**
     * 绑定金芒果特有的数据
     */
    private void bindJmgDetailData() {
        //设置第二部分的显示隐藏
        investProductDetailSecondJmg.setVisibility(View.VISIBLE);
        investProductDetailSecondNewuser.setVisibility(View.GONE);
        investProductDetailSecondHqb.setVisibility(View.GONE);

        //设置第三部分的显示隐藏
        investJmgProductDetailThirdLayout.setVisibility(View.VISIBLE);
        investHqbProductDetailThirdLayout.setVisibility(View.GONE);
        investNewuserProductDetailThirdLayout.setVisibility(View.GONE);
        //计算结束时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        tvInvestJmgProductDetailEndtime.setText("" + sdf.format(loanBean.getBetime()));
        //借款总额
        DecimalFormat df = new DecimalFormat("###,###,##0.00");
        tvInvestJmgProductDetailSumAmount.setText("" + df.format(loanBean.getAmount()) + "元");
        //还款方式
        tvInvestJmgProductDetailPaybackType.setText("" + investProductDetailBean.getIncomeType());
        //担保机构
        tvInvestJmgProductDetailGuaranteeOrganization.setText("" + investProductDetailBean.getGuaranteeCompany());
        //投资记录
        int tenderCount = investProductDetailBean.getTenderCount();
        tvInvestJmgTotalInvestRecord.setText(tenderCount != 0 ? tenderCount + "" : "");
    }

    /**
     * 绑定新手福利标特有的数据
     */
    private void bindNewUserDetailData() {
        actionbar.setCenterTextView("新手福利标");
        //设置第二部分的显示隐藏
        investProductDetailSecondNewuser.setVisibility(View.VISIBLE);
        investProductDetailSecondJmg.setVisibility(View.GONE);
        investProductDetailSecondHqb.setVisibility(View.GONE);

        //设置第三部分的显示隐藏
        investNewuserProductDetailThirdLayout.setVisibility(View.VISIBLE);
        investHqbProductDetailThirdLayout.setVisibility(View.GONE);
        investJmgProductDetailThirdLayout.setVisibility(View.GONE);

        //项目总额
        DecimalFormat df = new DecimalFormat("###,###,##0.00");
        tvInvestNewuserProductDetailSumaccount.setText("" + df.format(loanBean.getAmount()) + "元");
        //投资限额
        tvInvestNewuserProductDetailLimitacount.setText("" + df.format(investProductDetailBean.getTenderLimit()) + "元");
        //已购人数
        tvInvestNewuserProductDetailPaidnum.setText("" + investProductDetailBean.getTenderMembers() + "人");
        //投资记录
        int tenderCount = investProductDetailBean.getTenderCount();
        tvInvestNewuserTotalInvestRecord.setText(tenderCount != 0 ? tenderCount + "" : "");
    }

    private void bindJmgAndNewuserCommonData() {
        fmSecendArea.setVisibility(View.VISIBLE);
        fmThirdArea.setVisibility(View.VISIBLE);
        actionbar.setCenterTextView(loanBean.getTitle());
        DecimalFormat df = new DecimalFormat("###,###,###");
        tvInvestJmgProductRate.setText("" + (loanBean.getRate()));
        tvPercent.setVisibility(View.VISIBLE);
        tvInvestJmgProductDetailMinPurchaseAmount.setText("" + df.format(loanBean.getMint()));
        tvInvestJmgProductDetailDeadline.setText("" + loanBean.getPeriod() + "个月");
        tvInvestJmgProductDetailAvailableAmount.setText("" + df.format(loanBean.getBalance()));
        //拿到投资进度
        investRate = 1 - (loanBean.getBalance() / loanBean.getAmount());
    }

    @OnClick({R.id.rl_hqb_buy_redeem_rules, R.id.rl_hqb_product_introduce, R.id.rl_hqb_common_question, R.id.rl_newuser_common_question, R.id.rl_newuser_product_detail, R.id.rl_newuser_product_introduction, R.id.rl_invest_animationbar, R.id.rl_newuser_invest_record, R.id.rl_hqb_invest_record, /*R.id.ib_invest_jmg_product_share, R.id.ib_invest_jmg_product_call,*/ R.id.rl_jmg_project_detail, R.id.rl_jmg_invest_record, R.id.rl_jmg_common_question, R.id.ib_jmg_calc, rl_jmg_investing})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_invest_animationbar:
                //投资记录
                MyActivityManager.getInstance().startNextActivity(InvestRecordActivity.class, code, type);
                break;
            case R.id.rl_hqb_buy_redeem_rules:
                MyActivityManager.getInstance().startNextActivity(HqbNotesActivity.class);
                break;
            case R.id.rl_hqb_product_introduce:
                MyActivityManager.getInstance().startNextActivity(CommonWebViewActivity.class, "hqbIntroduce");
                break;
            case R.id.rl_newuser_product_introduction:
                MyActivityManager.getInstance().startNextActivity(CommonWebViewActivity.class, "newscomerIntroduce");
                break;
            case R.id.rl_jmg_project_detail:
            case R.id.rl_newuser_product_detail:
                MyActivityManager.getInstance().startNextActivity(JmgProjectDetailsActivity.class, "projectIntroduce");
                break;
            case R.id.rl_jmg_common_question:
                MyActivityManager.getInstance().startNextActivity(CommonWebViewActivity.class, "jmgNoviceProblem");
                break;
            case R.id.rl_hqb_common_question:
                MyActivityManager.getInstance().startNextActivity(CommonWebViewActivity.class, "hqbProblem");
                break;
            case R.id.rl_newuser_common_question:
                MyActivityManager.getInstance().startNextActivity(CommonWebViewActivity.class, "jmgNoviceProblem");
                break;
//
            case R.id.rl_jmg_invest_record:
            case R.id.rl_hqb_invest_record:
            case R.id.rl_newuser_invest_record:
                //投资记录
                MyActivityManager.getInstance().startNextActivity(InvestRecordActivity.class, code, type);
                break;
            case R.id.ib_jmg_calc:
                showInvestCalcPopupW();
                break;
            case rl_jmg_investing:
                boolean isLogined = (boolean) SPUtils.get(this, "LOGIN", "isLogined", false);
                if (!isLogined) {
                    MyActivityManager.getInstance().startNextActivity(LoginActivity.class);
                    return;
                }
                if ("0".equals(type)) {
                    //活期宝
                    //type  0:代表活期宝  1:代表金芒果或者秒杀标
                    MyActivityManager.getInstance().startNextActivity(InvestConfirmActivity.class, code, "0");
                } else {
                    //金芒果或者新手福利标或者秒杀标
                    if (investProductDetailBean == null) {
                        return;
                    }
                    boolean isFirstInvestment = (boolean) SPUtils.get(MainApplication.getContext(), "LOGIN", "isFirstInvestment", false);
                    if (investProductDetailBean.isNewcomerWelfare() && (!isFirstInvestment)) {
                        CommonToastUtils.showToast(MainApplication.getContext(), "您是老用户,不可投资");
                    } else {
                        MyActivityManager.getInstance().startNextActivity(InvestConfirmActivity.class, code, "1");
                    }
                }
        }
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[1-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    private void showInvestCalcPopupW() {
        final ViewHolder viewHolder;
        final View contentView = LayoutInflater.from(this).inflate(R.layout.layout_invest_calc, null);

        viewHolder = new ViewHolder(contentView);

        final PopupWindow calcPopupW = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //设置动画所对应的style
//        calcPopupW.setAnimationStyle(R.style.calcAnim);
        //点击空白处时，隐藏掉pop窗口
        calcPopupW.setBackgroundDrawable(new ColorDrawable());
        calcPopupW.setFocusable(true);
        calcPopupW.setOutsideTouchable(true);
        backgroundAlpha(0.5f);
        calcPopupW.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View rootView = LayoutInflater.from(this).inflate(R.layout.invest_product_detail, null);
        calcPopupW.showAtLocation(rootView, Gravity.CENTER, 0, 0);

        calcPopupW.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        if ("0".equals(type)) {
            viewHolder.tvInvestingText.setText("立即转入");
            viewHolder.rlJmgInvesting.setBackgroundColor(getResources().getColor(R.color.invest_hqb_product_detail_bg_color));
            viewHolder.investProductFootlayout.setBackgroundResource(R.drawable.shape_invest_blue_border);
            viewHolder.tvExpectSumIncome.setText("预计一年总收益");
        } else {
            if (loanBean.getStatus() == 2) {
                //立即投资
                viewHolder.tvInvestingText.setText("立即投资");
                isCanInvest = true;
            } else if (loanBean.getStatus() == 3) {
                //满标
                viewHolder.tvInvestingText.setText("已满标");
                viewHolder.rlJmgInvesting.setClickable(false);
                viewHolder.rlJmgInvesting.setBackgroundColor(Color.parseColor("#f8cd79"));
                viewHolder.investProductFootlayout.setBackgroundResource(R.drawable.shape_invest_light_orange_border);
            } else if (loanBean.getStatus() == 100) {
                //还款中
                viewHolder.tvInvestingText.setText("还款中");
                viewHolder.rlJmgInvesting.setClickable(false);
                viewHolder.rlJmgInvesting.setBackgroundColor(Color.parseColor("#f8cd79"));
                viewHolder.investProductFootlayout.setBackgroundResource(R.drawable.shape_invest_light_orange_border);
            } else if (loanBean.getStatus() == 200) {
                //已还款
                viewHolder.tvInvestingText.setText("已还款");
                viewHolder.rlJmgInvesting.setClickable(false);
                viewHolder.rlJmgInvesting.setBackgroundColor(Color.parseColor("#cecece"));
                viewHolder.investProductFootlayout.setBackgroundResource(R.drawable.shape_invest_gray_border);
            }
        }
        //设置数据
        viewHolder.investCalcCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcPopupW.dismiss();
            }
        });

        viewHolder.ibJmgCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcPopupW.dismiss();
            }
        });

        viewHolder.etInvestValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value = String.valueOf(s).trim().toString();
                if (!TextUtils.isEmpty(value)) {
                    int number = Integer.parseInt(value);
                    viewHolder.investScaleMongey.setValue(number);

                    double profit = 0;

                    if (investType == 1) {
                        profit = number * (loanBean.getRate()) / 100.0 / 12;
                    } else if (investType == 2) {
                        profit = number * (9.5 / 100.0);
                    } else if (investType == 3) {
                        profit = number * (loanBean.getRate()) / 100.0 / 12 * loanBean.getPeriod();
                    }
                    viewHolder.tvInvestProfit.setText(new DecimalFormat("######0.00").format(profit).toString());
                } else {
                    viewHolder.tvInvestProfit.setText("0.00");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                String temp = s.toString();
                if (temp.length() == 1 && temp.equals("0")) {
                    s.clear();
                    return;
                }
            }
        });

        viewHolder.investScaleMongey.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                viewHolder.investScaleMongey.setFocusable(true);
                viewHolder.investScaleMongey.setFocusableInTouchMode(true);
                viewHolder.investScaleMongey.requestFocus();

                return false;
            }
        });

        viewHolder.investScaleMongey.setMoveScaleInterface(new InvestScaleMoney.MoveScaleInterface() {
            @Override
            public void getValue(int value) {
                if (!viewHolder.etInvestValue.hasFocus()) {
                    double profit = 0;
                    //设置投资金额
                    viewHolder.etInvestValue.setText(value + "");
                    //设置预估收益
                    //1:新手福利标 2:活期宝 3:金芒果
                    if (investType == 1) {
                        profit = value * (loanBean.getRate()) / 100.0 / 12;
                    } else if (investType == 2) {
                        profit = value * (9.5 / 100.0);
                    } else if (investType == 3) {
                        profit = value * (loanBean.getRate()) / 100.0 / 12 * loanBean.getPeriod();
                    }
                    viewHolder.tvInvestProfit.setText(new DecimalFormat("######0.00").format(profit).toString());
                }
            }

            @Override
            public void initValue() {
                viewHolder.investScaleMongey.setValue(10000);
                viewHolder.etInvestValue.setText("10000");
                double profit = 0;
                int value = 10000;
                if (investType == 1) {
                    profit = value * (loanBean.getRate()) / 100.0 / 12;
                } else if (investType == 2) {
                    profit = value * (9.5 / 100.0);
                } else if (investType == 3) {
                    profit = value * (loanBean.getRate()) / 100.0 / 12 * loanBean.getPeriod();
                }
                viewHolder.tvInvestProfit.setText(new DecimalFormat("######0.00").format(profit).toString());
            }
        });

        viewHolder.rlJmgInvesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogined = (boolean) SPUtils.get(InvestProductDetailActivity.this, "LOGIN", "isLogined", false);
                if (!isLogined) {
                    MyActivityManager.getInstance().startNextActivity(LoginActivity.class);
                    return;
                }
                if (isCanInvest) {
                    //金芒果或者新手福利标或者秒杀标
                    String amount = viewHolder.etInvestValue.getText().toString();
                    InvestAmountBean investAmountBean = new InvestAmountBean();
                    investAmountBean.setInvestMoney(amount);
                    EventBus.getDefault().postSticky(investAmountBean);
                    if ("0".equals(type)) {
                        //活期宝
                        //type  0:代表活期宝  1:代表金芒果或者秒杀标
                        MyActivityManager.getInstance().startNextActivity(InvestConfirmActivity.class, code, "0");
                        calcPopupW.dismiss();

                    } else {
                        if (investProductDetailBean == null) {
                            return;
                        }
                        boolean isFirstInvestment = (boolean) SPUtils.get(MainApplication.getContext(), "LOGIN", "isFirstInvestment", false);
                        if (investProductDetailBean.isNewcomerWelfare() && (!isFirstInvestment)) {
                            CommonToastUtils.showToast(MainApplication.getContext(), "您是老用户,不可投资");
                        } else {
                            MyActivityManager.getInstance().startNextActivity(InvestConfirmActivity.class, code, "1");
                            calcPopupW.dismiss();
                        }
                    }
                }
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 拨打电话弹窗
     *
     * @param activity
     * @param rootView
     * @return
     */
    public PopupWindow showCallPopw(final Activity activity, View rootView) {
        View contentView = LayoutInflater.from(activity).inflate(R.layout.layout_call_phonenum, null);
        TextView dialog_canclebtn = (TextView) contentView.findViewById(R.id.dialog_canclebtn);
        TextView dialog_confirmbtn = (TextView) contentView.findViewById(R.id.dialog_confirmbtn);
        final PopupWindow callPopw = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置动画所对应的style
        callPopw.setAnimationStyle(R.style.calcAnim);
        //点击空白处时，隐藏掉pop窗口
        callPopw.setBackgroundDrawable(new ColorDrawable());
        callPopw.setFocusable(true);
        callPopw.setOutsideTouchable(true);
        backgroundAlpha(0.5f);
        callPopw.showAtLocation(rootView, Gravity.CENTER_HORIZONTAL, 0, 0);
        callPopw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

        dialog_canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callPopw != null) {
                    callPopw.dismiss();
                }
            }
        });
        dialog_confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(InvestProductDetailActivity.this, Manifest.permission.CALL_PHONE);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(InvestProductDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_CALL_PHONE);
                        return;
                    } else {
                        //上面已经写好的拨号方法
                        callDirectly();
                    }
                } else {
                    //上面已经写好的拨号方法
                    callDirectly();
                }
                callPopw.dismiss();
            }
        });
        return callPopw;
    }


    /**
     * 拨打电话
     */
    public void callDirectly() {
        String forPhoneNum = "4008976555";
        Intent intent = new Intent();
        intent.setAction("android.intent.action.CALL");
        intent.setData(Uri.parse("tel:" + forPhoneNum));
        startActivity(intent);
    }


    /**
     * 用户拨打电话权限回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    callDirectly();
                } else {
                    // Permission Denied
                    CommonToastUtils.showToast(this, "CALL_PHONE Denied");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void touchWeChatEvent() {
        if (socialClient.wxAPI.isWXAppInstalled()) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mango_logo);
            socialClient.setWebMediaMessage(shareTitle, shareContent, bitmap, shareUrl).sendRequestToWX(false);
        } else {
            CommonToastUtils.showToast(this, "请安装微信");
        }

    }

    @Override
    public void touchWeFriendEvent() {
        if (socialClient.wxAPI.isWXAppInstalled()) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mango_logo);
            socialClient.setWebMediaMessage(shareTitle, shareContent, bitmap, shareUrl).sendRequestToWX(true);
        } else {
            CommonToastUtils.showToast(this, "请安装微信");
        }

    }

    @Override
    public void touchQQEvent() {
        Bundle bundle = socialClient.setWebQQMessage(shareTitle, shareContent, "", shareUrl);
        socialClient.getTencent().shareToQQ(this, bundle, this);
    }

    @Override
    public void touchWeiboEvent() {

        if (socialClient.mWeiboShareAPI.isWeiboAppInstalled()) {
//            socialClient.setWebWithSina(shareTitle, shareContent, bitmap, shareUrl).sendRequestToSina(this);
            Bundle bundle = new Bundle();
            bundle.putString("title", shareTitle);
            bundle.putString("content", shareContent);
            bundle.putString("url", shareUrl);
            MyActivityManager.getInstance().startNextActivity(WeiboShareActivity.class, bundle);

        } else {
            CommonToastUtils.showToast(this, "请安装微博");
        }
    }

    @Override
    public void touchCancelEvent() {
        socialDialog.dismiss();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        socialClient.getWeiboShareAPI().handleWeiboResponse(intent, this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, this);
        }
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {

        if (baseResponse != null) {
            switch (baseResponse.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    Toast.makeText(this, R.string.social_share_success, Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    Toast.makeText(this, R.string.social_share_canceled, Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    Toast.makeText(this,
                            getString(R.string.social_share_failed) + "Error Message: " + baseResponse.errMsg,
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }

    }

    @Override
    public void onComplete(Object o) {
        Toast.makeText(this, R.string.social_share_success, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(UiError uiError) {
        Toast.makeText(this,
                getString(R.string.social_share_failed) + "Error Message: " + uiError.errorMessage,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCancel() {
        Toast.makeText(this, R.string.social_share_canceled, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    static class ViewHolder {
        @InjectView(R.id.invest_calc_cancle)
        ImageButton investCalcCancle;
        @InjectView(R.id.tv_invest_profit)
        TextView tvInvestProfit;
        @InjectView(R.id.tv_investing_text)
        TextView tvInvestingText;
        @InjectView(R.id.tv_invest_value)
        EditText etInvestValue;
        @InjectView(R.id.tv_expect_sum_income)
        TextView tvExpectSumIncome;
        @InjectView(R.id.invest_scale_mongey)
        InvestScaleMoney investScaleMongey;
        @InjectView(R.id.ib_jmg_calc)
        ImageButton ibJmgCalc;
        @InjectView(rl_jmg_investing)
        RelativeLayout rlJmgInvesting;
        @InjectView(R.id.invest_product_footlayout)
        LinearLayout investProductFootlayout;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
