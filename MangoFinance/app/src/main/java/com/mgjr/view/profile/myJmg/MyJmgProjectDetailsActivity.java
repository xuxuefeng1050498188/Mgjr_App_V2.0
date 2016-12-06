package com.mgjr.view.profile.myJmg;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.SPUtils;
import com.mgjr.model.bean.MyJmgInvestDetailBean;
import com.mgjr.presenter.impl.MyJmgInvestDetailPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.view.listeners.ViewListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/17.
 */
public class MyJmgProjectDetailsActivity extends ActionbarActivity implements View.OnClickListener, ViewListener<MyJmgInvestDetailBean> {
    //年化收益
    private TextView tv_jmg_projectdetails_rate;
    //投资期限
    private TextView tv_jmg_projectdetails_term;
    //开始时间
    private TextView tv_jmg_projectdetails_starttime;
    //结束时间
    private TextView tv_jmg_projectdetails_endtime;
    //下个还款日
    private TextView tv_jmg_projectdetails_loanstatus;
    //投资金额
    private TextView tv_jmg_projectdetails_investaccount;
    //已获收益
    private TextView tv_jmg_projectdetails_earnedincome;
    //预期收益
    private TextView tv_jmg_projectdetails_expectincome;

    private ListView jmgProjectdetailsListView;
    private LinearLayout layout_income_details;
    private LinearLayout layout_starttime, layout_endtime;
    private JmgRepaymentPlanListAdapter adapter;
    private MyJmgInvestDetailPresenterImpl myJmgInvestDetailPresenterImpl;
    private PopupWindow loadingPopW;
    private String id;
    private MyJmgInvestDetailBean myJmgInvestDetailBean;
    private List<MyJmgInvestDetailBean.RepaymentPlanBean> repaymentPlanList;
    private MyJmgInvestDetailBean.TenderDetailBean tenderDetailBean;
    private JmgProjectsDetailListAdapter jmgProjectsDetailListAdapter;
    private ImageView iv_repayed_month, iv_progress;
    private TextView tv_has_used_gift, tv_has_used_redpacket;
    private LinearLayout list_content;
    private TextView tv_tips;
    private String nextRepayTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_jmg_projectdetails, this);
        initActionBar();
        //拿到id
        id = getIntent().getStringExtra("code");
        nextRepayTime = getIntent().getStringExtra("type");
        myJmgInvestDetailPresenterImpl = new MyJmgInvestDetailPresenterImpl(this);
        initViews();
    }

    private void initActionBar() {
        actionbar.leftImageView.setImageResource(R.drawable.invest_left_arrow);
        actionbar.setCenterTextColor(Color.parseColor("#ffffff"));
        this.actionbar.setBackgroundColor(Color.parseColor("#FFA800"));
    }


    private void initViews() {

        tv_jmg_projectdetails_rate = (TextView) findViewById(R.id.tv_jmg_projectdetails_rate);
        tv_jmg_projectdetails_term = (TextView) findViewById(R.id.tv_jmg_projectdetails_term);
        tv_jmg_projectdetails_starttime = (TextView) findViewById(R.id.tv_jmg_projectdetails_starttime);
        tv_jmg_projectdetails_endtime = (TextView) findViewById(R.id.tv_jmg_projectdetails_endtime);
        tv_jmg_projectdetails_loanstatus = (TextView) findViewById(R.id.tv_jmg_projectdetails_loanstatus);
        tv_jmg_projectdetails_investaccount = (TextView) findViewById(R.id.tv_jmg_projectdetails_investaccount);
        tv_jmg_projectdetails_earnedincome = (TextView) findViewById(R.id.tv_jmg_projectdetails_earnedincome);
        tv_jmg_projectdetails_expectincome = (TextView) findViewById(R.id.tv_jmg_projectdetails_expectincome);
        iv_repayed_month = (ImageView) findViewById(R.id.iv_repayed_month);
        iv_progress = (ImageView) findViewById(R.id.iv_progress);
        tv_has_used_gift = (TextView) findViewById(R.id.tv_has_used_gift);
        tv_has_used_redpacket = (TextView) findViewById(R.id.tv_has_used_redpacket);
        jmgProjectdetailsListView = (ListView) findViewById(R.id.lv_jmg_projectdetails);
        list_content = (LinearLayout) findViewById(R.id.list_content);
        layout_income_details = (LinearLayout) findViewById(R.id.layout_income_details);
        layout_starttime = (LinearLayout) findViewById(R.id.layout_starttime);
        layout_endtime = (LinearLayout) findViewById(R.id.layout_endtime);
        tv_tips = (TextView) findViewById(R.id.tv_tips);
    }


    @Override
    protected void onResume() {
        super.onResume();
        networkRequest();
    }


    private void networkRequest() {
        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        necessaryParams.put("mid", "" + SPUtils.get(this, "LOGIN", "id", 0));
        necessaryParams.put("tenderId", id);
        myJmgInvestDetailPresenterImpl.sendRequest(necessaryParams, null);
    }

    @Override
    public void onClick(View v) {

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
        dismissLoadingDialog();
        showSystemError();
    }


    @Override
    public void showError(OnPresenterListener listener, MyJmgInvestDetailBean myJmgInvestDetailBean) {
        dismissLoadingDialog();
        CommonToastUtils.showToast(this, myJmgInvestDetailBean.getMsg());
    }

    @Override
    public void responseData(OnPresenterListener listener, MyJmgInvestDetailBean myJmgInvestDetailBean) {
        this.myJmgInvestDetailBean = myJmgInvestDetailBean;
        repaymentPlanList = myJmgInvestDetailBean.getRepaymentPlanList();

        tenderDetailBean = myJmgInvestDetailBean.getTenderDetail();
        resetList(myJmgInvestDetailBean);
        bindData();
        bindListViewData();
    }

    private void resetList(MyJmgInvestDetailBean myJmgInvestDetailBean) {
        List<MyJmgInvestDetailBean.RepaymentPlanBean> list1 = new ArrayList<>();
        List<MyJmgInvestDetailBean.RepaymentPlanBean> list2 = new ArrayList<>();
        for (int i = 0; i < repaymentPlanList.size(); i++) {
            MyJmgInvestDetailBean.RepaymentPlanBean bean = myJmgInvestDetailBean.getRepaymentPlanList().get(i);
            int status = bean.getStatus();
            if (status == 0) {
                list1.add(bean);
            } else if (status == 1) {
                list2.add(bean);
            }
        }
        repaymentPlanList.clear();
        repaymentPlanList.addAll(list1);
        repaymentPlanList.addAll(list2);
    }


    private void bindData() {
        /*进度条*/
        int period = tenderDetailBean.getPeriod();
        if (period == 1) {
            iv_progress.setBackgroundResource(R.drawable.bg_progress_1);
        } else if (period == 3) {
            iv_progress.setBackgroundResource(R.drawable.bg_progress_3);
        } else if (period == 6) {
            iv_progress.setBackgroundResource(R.drawable.bg_progress_6);
        } else if (period == 12) {
            iv_progress.setBackgroundResource(R.drawable.bg_progress_12);
        }
        //进度条
        int repayCount = tenderDetailBean.getRepayCount();
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        iv_progress.measure(width, height);
        int measuredWidth = iv_progress.getMeasuredWidth();
        int progress = (int) (measuredWidth * repayCount / period);
        if (repayCount == period) {
            iv_repayed_month.setBackgroundResource(R.drawable.qrepayed_month_bg);
        } else {
            if (progress != measuredWidth) {
               FrameLayout.LayoutParams progressParams = (FrameLayout.LayoutParams) iv_repayed_month.getLayoutParams();
                progressParams.width = progress;
                iv_repayed_month.setLayoutParams(progressParams);
            }
        }
        actionbar.setCenterTextView(tenderDetailBean.getLoanTitle());
        tv_jmg_projectdetails_rate.setText("" + (tenderDetailBean.getRate()));
        tv_jmg_projectdetails_term.setText("" + tenderDetailBean.getPeriod());
        double preIntefee = tenderDetailBean.getPreIntefee();
        if (preIntefee != 0) {
            tv_jmg_projectdetails_expectincome.setText(new DecimalFormat("###,###,##0.00").format(preIntefee));
        } else {
            tv_jmg_projectdetails_expectincome.setTextColor(Color.parseColor("#333333"));
            tv_jmg_projectdetails_expectincome.setTextSize(12);
            tv_jmg_projectdetails_expectincome.setText("暂无收益");
        }
        double repayIntefee = tenderDetailBean.getRepayIntefee();
        if (repayIntefee == 0) {
            tv_jmg_projectdetails_earnedincome.setTextColor(Color.parseColor("#333333"));
            tv_jmg_projectdetails_earnedincome.setTextSize(12);
            tv_jmg_projectdetails_earnedincome.setText("暂无收益");
        } else {
            tv_jmg_projectdetails_earnedincome.setText(new DecimalFormat("###,###,##0.00").format(repayIntefee));
        }

        /*项目状态：（1：初审中，11：初审拒绝，2：招标中，3：复审中，30：流标，33 ：复审拒绝，100：还款中，200 ：还款完成，-1：关闭）*/
        int loanstatus = myJmgInvestDetailBean.getTenderDetail().getLoanstatus();


        if (loanstatus == 1) {
            tv_jmg_projectdetails_loanstatus.setText("初审中");
            layout_income_details.setVisibility(View.GONE);
        } else if (loanstatus == 2) {
            tv_jmg_projectdetails_loanstatus.setText("正在招标中");
            layout_income_details.setVisibility(View.GONE);
            layout_endtime.setVisibility(View.INVISIBLE);
            layout_starttime.setVisibility(View.INVISIBLE);
        } else if (loanstatus == 3) {
            tv_jmg_projectdetails_loanstatus.setText("等待复审中");
            layout_income_details.setVisibility(View.GONE);
            layout_endtime.setVisibility(View.INVISIBLE);
            layout_starttime.setVisibility(View.INVISIBLE);
        } else if (loanstatus == 11) {
            tv_jmg_projectdetails_loanstatus.setText("初审拒绝");
            layout_income_details.setVisibility(View.GONE);
        } else if (loanstatus == 331) {
            tv_jmg_projectdetails_loanstatus.setText("复审拒绝");
            layout_income_details.setVisibility(View.GONE);
        } else if (loanstatus == 100) {
            tv_jmg_projectdetails_loanstatus.setText("还款中");
            layout_income_details.setVisibility(View.VISIBLE);
        } else if (loanstatus == 200) {
            tv_jmg_projectdetails_loanstatus.setText("还款已完成");
            layout_income_details.setVisibility(View.VISIBLE);
        } else if (loanstatus == 11) {
            tv_jmg_projectdetails_loanstatus.setText("关闭");
            layout_income_details.setVisibility(View.GONE);
        }
        if (repaymentPlanList.size() == 0) {

            list_content.setVisibility(View.VISIBLE);
            if (loanstatus == 2) {
                tv_tips.setText("正在招标中,尚未生成还款计划");
            } else if (loanstatus == 3) {
                tv_tips.setText("正在复审中,尚未生成还款计划");
            }
        }


        String startTime = new SimpleDateFormat("yyyy-MM-dd").format(tenderDetailBean.getIntefeeStartTime());
        tv_jmg_projectdetails_starttime.setText(startTime);
        long expireTime = 0;
        if (loanstatus == 200) {
            expireTime = tenderDetailBean.getExpireTime();

        } else {
            expireTime = tenderDetailBean.getPreExpireTime();
        }
        String endTime = new SimpleDateFormat("yyyy-MM-dd").format(expireTime);
        tv_jmg_projectdetails_endtime.setText(endTime);


        if (nextRepayTime != null) {
            tv_jmg_projectdetails_loanstatus.setText("下个还款日  " + nextRepayTime);
        }

        tv_jmg_projectdetails_investaccount.setText("" + new DecimalFormat("###,###,##0.00").format(tenderDetailBean.getAmount()));


        /*使用加息券金额*/
        double coupon = myJmgInvestDetailBean.getCoupons();
        if (coupon == 0) {
            tv_has_used_gift.setVisibility(View.INVISIBLE);
        } else {
            tv_has_used_gift.setVisibility(View.VISIBLE);
            tv_has_used_gift.setText(coupon + "%");
        }
        /*使用红包金额*/
        double hbAmount = myJmgInvestDetailBean.getHbAmount();
        if (hbAmount == 0) {
            tv_has_used_redpacket.setVisibility(View.INVISIBLE);
        } else {
            tv_has_used_redpacket.setVisibility(View.VISIBLE);
            tv_has_used_redpacket.setText(hbAmount + "元");
        }


    }


    private void bindListViewData() {
        if (jmgProjectsDetailListAdapter == null) {
            jmgProjectsDetailListAdapter = new JmgProjectsDetailListAdapter(repaymentPlanList);
            jmgProjectdetailsListView.setAdapter(jmgProjectsDetailListAdapter);
        } else {
            jmgProjectsDetailListAdapter.notifyDataSetChanged();
        }

        if (jmgProjectsDetailListAdapter == null) {
            // pre-condition
            return;
        }

    }
}
