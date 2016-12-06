package com.mgjr.view.invester;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.StringFormatUtil;
import com.mgjr.model.bean.EventBusBean.HqbInvestCompleteBean;
import com.mgjr.model.bean.EventBusBean.JmgInvestCompleteBean;
import com.mgjr.model.bean.HqbBean;
import com.mgjr.model.bean.LoanBean;
import com.mgjr.share.ActionbarActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class InvestCompletedActivity extends ActionbarActivity {

    @InjectView(R.id.tv_invested_tender_count)
    TextView tvInvestedTenderCount;
    @InjectView(R.id.confirminvest_bought_btn)
    ImageView confirminvestBoughtBtn;
    @InjectView(R.id.tv_product_name)
    TextView tvProductName;
    @InjectView(R.id.tv_invest_amount)
    TextView tvInvestAmount;
    @InjectView(R.id.tv_used_hb_amount)
    TextView tvUsedHbAmount;
    @InjectView(R.id.confirminvest_clock_btn)
    ImageView confirminvestClockBtn;
    @InjectView(R.id.tv_invest_completed_time)
    TextView tvInvestCompletedTime;
    @InjectView(R.id.tv_invest_period)
    TextView tvInvestPeriod;
    @InjectView(R.id.confirminvest_earnings_btn)
    ImageView confirminvestEarningsBtn;
    @InjectView(R.id.tv_expect_income)
    TextView tvExpectIncome;
    @InjectView(R.id.tv_year_profit)
    TextView tvYearProfit;
    @InjectView(R.id.tv_jmg_title)
    TextView tvJmgTitle;
    @InjectView(R.id.tv_jmg_period)
    TextView tvJmgPeriod;
    @InjectView(R.id.tv_jmg_rate)
    TextView tvJmgRate;
    @InjectView(R.id.tv_jmg_zy)
    TextView tvJmgZy;
    @InjectView(R.id.tv_jmg_limit_raise_rate)
    TextView tvJmgLimitRaiseRate;
    @InjectView(R.id.tv_income_desc)
    TextView tvIncomeDesc;
    @InjectView(R.id.ll_invest_count)
    LinearLayout llInvestCount;
    @InjectView(R.id.ll_recomend_bid)
    LinearLayout llRecomendBid;
    @InjectView(R.id.tv_jmg_limit_seckill)
    TextView tvJmgLimitSeckill;
    private HqbInvestCompleteBean hqbInvestCompleteBean;
    private JmgInvestCompleteBean jmgInvestCompleteBean;
    //0,活期宝  1,金芒果
    private int recommenBidType;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_invest_completed, this);
        ButterKnife.inject(this);
        initActionBar();
        EventBus.getDefault().register(this);
        bindData();
    }

    private void initActionBar() {
        actionbar.setCenterTextView("购买成功");
        actionbar.leftImageView.setVisibility(View.GONE);
        actionbar.setRightTextView("完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                popActivity();
                MyActivityManager.getInstance().popCurrentActivity();
            }
        });
    }

    private void bindData() {
        if (hqbInvestCompleteBean != null && jmgInvestCompleteBean == null) {
            //活期宝
            bindHqbData();
        } else {
            //金芒果
            bindJmgData();
        }
        llRecomendBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先退出栈里面已有的InvestProductDetailActivity
                MyActivityManager.getInstance().finishSpecifiedActivity(InvestProductDetailActivity.class);
                //type  0:代表活期宝  1:代表金芒果或者秒杀标
                if (recommenBidType == 0) {
                    MyActivityManager.getInstance().startNextActivity(InvestProductDetailActivity.class, code, "0");
                } else {
                    MyActivityManager.getInstance().startNextActivity(InvestProductDetailActivity.class, code, "1");
                }
                MyActivityManager.getInstance().popCurrentActivity();
            }
        });
    }

    private void bindJmgData() {
        int tenderCount = jmgInvestCompleteBean.getTenderCount();
        StringFormatUtil tenderCountSFU = new StringFormatUtil(InvestCompletedActivity.this, "这是您的第 " + tenderCount + " 次定期投资之旅", "" + tenderCount, R.color.text1).fillColor();
        tvInvestedTenderCount.setText(tenderCountSFU.getResult());
//        tvInvestedTenderCount.setText("" + jmgInvestCompleteBean.getTenderCount());
        if (jmgInvestCompleteBean.getPeriod() == 1) {
//            actionbar.setCenterTextView("新手福利标");
            tvProductName.setText("已购买新手福利标");
        } else {
//            actionbar.setCenterTextView(jmgInvestCompleteBean.getTitle());
            tvProductName.setText("已购买" + jmgInvestCompleteBean.getTitle());
        }
        Double amount = jmgInvestCompleteBean.getAmount();
        String investMoney = new DecimalFormat("###,###,##0.00").format(amount);
        tvInvestAmount.setText("投资金额" + investMoney + "元");
        tvYearProfit.setText("预期年化收益" + jmgInvestCompleteBean.getRate() + "%");
        if (jmgInvestCompleteBean.getHbAmount() == 0) {
            tvUsedHbAmount.setVisibility(View.GONE);
        } else {
            tvUsedHbAmount.setText("使用红包" + jmgInvestCompleteBean.getHbAmount() + "元");
        }

        long preExpireTime = jmgInvestCompleteBean.getPreExpireTime();
        String time = new SimpleDateFormat("yyyy年MM月dd日").format(preExpireTime);
        tvInvestCompletedTime.setText("预计到期时间" + time);
        tvInvestPeriod.setText("投资期限" + jmgInvestCompleteBean.getPeriod() + "个月");
        double preIncome = jmgInvestCompleteBean.getPreIncome();
        String incomeMonth = new DecimalFormat("###,###,##0.00").format(preIncome);
        tvExpectIncome.setText("预期收益" + incomeMonth + "元");
        tvIncomeDesc.setText("按月付息,到期还本");

        //绑定推荐标的数据
        if (jmgInvestCompleteBean.getHqb() != null && jmgInvestCompleteBean.getJmg() == null) {
            HqbBean hqbBean = jmgInvestCompleteBean.getHqb();
            recommenBidType = 0;
            code = hqbBean.getCode();
            tvJmgTitle.setText("活期宝");
//            tvJmgPeriod.setText("不限");
            tvJmgPeriod.setVisibility(View.GONE);
            tvJmgRate.setText("7-11");
            tvJmgZy.setText("%");
           /* if (hqbBean.getZy() != 0) {
                tvJmgZy.setText("%" + "+" + hqbBean.getZy() + "%");
            } else {
                tvJmgZy.setText("%");
            }*/
            tvJmgLimitRaiseRate.setVisibility(View.GONE);
        } else {
            LoanBean jmgBean = jmgInvestCompleteBean.getJmg();
            recommenBidType = 1;
            code = jmgBean.getCode();
            tvJmgTitle.setText(jmgBean.getTitle());
            tvJmgPeriod.setText("" + jmgBean.getPeriod() + "个月");
            tvJmgRate.setText("" + (jmgBean.getRate() - jmgBean.getZy()));
            if (jmgBean.getZy() != 0) {
                tvJmgZy.setText("%" + "+" + jmgBean.getZy() + "%");
            } else {
                tvJmgZy.setText("%");
            }

            if (jmgBean.getDtinfo() != null) {
                String[] descs = jmgBean.getDtinfo().split(",");
                if (!TextUtils.isEmpty(descs[0])) {
                    tvJmgLimitRaiseRate.setVisibility(View.VISIBLE);
                    tvJmgLimitRaiseRate.setText(descs[0]);
                }
                if (!TextUtils.isEmpty(descs[1])) {
                    tvJmgLimitSeckill.setVisibility(View.VISIBLE);
                    tvJmgLimitSeckill.setText(descs[1]);
                }
            }
        }
    }

    private void bindHqbData() {
        int tenderCount = hqbInvestCompleteBean.getTenderCount();
        StringFormatUtil tenderCountSFU = new StringFormatUtil(InvestCompletedActivity.this, "这是您的第 " + tenderCount + " 次活期理财之旅", "" + tenderCount, R.color.text1).fillColor();
        tvInvestedTenderCount.setText(tenderCountSFU.getResult());
//        tvInvestedTenderCount.setText("" + hqbInvestCompleteBean.getTenderCount());
//        actionbar.setCenterTextView("活期宝");
        tvProductName.setText("已转入活期宝");
        Double amount = hqbInvestCompleteBean.getAmount();
        String investMoney = new DecimalFormat("###,###,##0.00").format(amount);
        tvInvestAmount.setText("转入金额" + investMoney + "元");
        tvYearProfit.setText("预期年化收益" + hqbInvestCompleteBean.getRate());
        tvUsedHbAmount.setVisibility(View.GONE);

        tvInvestCompletedTime.setText("随存随取");
        tvInvestPeriod.setVisibility(View.GONE);
        double income1Year = hqbInvestCompleteBean.getIncome1Year();
        String income = new DecimalFormat("###,###,##0.00").format(income1Year);
        tvExpectIncome.setText("一年预期收益" + income + "元");
        tvIncomeDesc.setText("按日计息");

        //绑定推荐标的数据
        LoanBean recommendLoan = hqbInvestCompleteBean.getRecommendLoan();
        recommenBidType = 1;
        code = recommendLoan.getCode();
        tvJmgTitle.setText(recommendLoan.getTitle());
        tvJmgPeriod.setText("" + recommendLoan.getPeriod() + "个月");
        tvJmgRate.setText("" + (recommendLoan.getRate() - recommendLoan.getZy()));
        if (recommendLoan.getZy() != 0) {
            tvJmgZy.setText("%" + "+" + recommendLoan.getZy() + "%");
        } else {
            tvJmgZy.setText("%");
        }
        if (recommendLoan.getDtinfo() != null) {
            String[] descs = recommendLoan.getDtinfo().split(",");
            tvJmgLimitRaiseRate.setText(descs[0]);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void hqbEventBus(HqbInvestCompleteBean event) {
        hqbInvestCompleteBean = event;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void JmgEventBus(JmgInvestCompleteBean event) {
        jmgInvestCompleteBean = event;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);
    }
}
