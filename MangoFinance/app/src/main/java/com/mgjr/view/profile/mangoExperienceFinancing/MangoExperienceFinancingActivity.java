package com.mgjr.view.profile.mangoExperienceFinancing;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.Double2int;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.PopwUtils;
import com.mgjr.model.bean.MyTyjBean;
import com.mgjr.presenter.impl.MyTyjPresenterImpl;
import com.mgjr.presenter.impl.NewcomerTasteTenderPresenterImpl;
import com.mgjr.presenter.impl.NewcomerTenderInvestPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.CustomCommonDialog;
import com.mgjr.view.common.CommonWebViewActivity;
import com.mgjr.view.listeners.ViewListener;
import com.mgjr.view.profile.CapitalDetails.CapitalDetailsActivity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/2.
 */
public class MangoExperienceFinancingActivity extends ActionbarActivity implements View.OnClickListener, ViewListener<MyTyjBean> {
    private ExperienceInvestRecordAdapter adapter;
    private MyTyjBean myTyjBean;
    private LinearLayout list_content;
    private LinearLayout layout_tyj_balance, layout_tyj_allbalance;
    private ListView lv_mangotyj;
    private TextView tv_incometype;
    //累计收益
    private TextView tv_mgtyj_waitincome;
    private Double ljlxNewcomer;

    //体验金余额
    private TextView tv_tyj_balance;
    private Double newcomerBalance;

    //累计获得体验金
    private TextView tv_tyj_all;
    private Double newcomerTotalAmount;

    private LinearLayout listContentView;
    private Button btn_tyj_invest;

    private MangoTyjInvestDialog investDialog;
    private SharedPreferences sp;
    private MyTyjPresenterImpl myTyjPresenter;
    private NewcomerTasteTenderPresenterImpl newcomerTasteTenderPresenter;
    private NewcomerTenderInvestPresenterImpl newcomerTenderInvestPresenter;
    private List<MyTyjBean.InvestmentRecordBean> investmentRecord;
    private String mid;
    private String code;
    private String xsbCode;
    private Double amount;

    /*dialog数据*/
    private String projtitle;//项目名
    private Double rate;//年化收益
    private String invest_period; // 标资期限
    private Double projectaccount;//项目总额
    private String repaymenttype;//还款方式
    private String interesttime;//计息时间
    private Double investableaccount;//可投金额
    private Double balance; // 体验金余额

    private int length; //投资记录条数

    private LayoutInflater inflater;
    private PopupWindow loadingPopupWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_layout_mangoexperiencefinancing, this);
        initActionbar();
        initViews();
//        initRefreshList();
    }

    private void initActionbar() {
        this.actionbar.setCenterTextView("芒果体验金");
        this.actionbar.setLeftImageView(R.drawable.invest_left_arrow, this);
        this.actionbar.setRightImageView(R.drawable.my_jmg_common_question_bg, this);
        this.actionbar.setBackgroundColor(Color.parseColor("#FFA800"));
        this.actionbar.centerTextView.setTextColor(Color.WHITE);
    }

    private void initViews() {

        sp = this.getSharedPreferences("LOGIN", MODE_PRIVATE);
        myTyjPresenter = new MyTyjPresenterImpl(this);
        newcomerTasteTenderPresenter = new NewcomerTasteTenderPresenterImpl(this);
        newcomerTenderInvestPresenter = new NewcomerTenderInvestPresenterImpl(this);
        layout_tyj_balance = (LinearLayout) findViewById(R.id.layout_tyj_balance);
        layout_tyj_allbalance = (LinearLayout) findViewById(R.id.layout_tyj_allbalance);
        list_content = (LinearLayout) findViewById(R.id.list_content);
        btn_tyj_invest = (Button) findViewById(R.id.btn_tyj_invest);
        btn_tyj_invest.setOnClickListener(this);
        layout_tyj_balance.setOnClickListener(this);
        layout_tyj_allbalance.setOnClickListener(this);
        tv_mgtyj_waitincome = (TextView) findViewById(R.id.tv_mgtyj_waitincome);
        tv_tyj_balance = (TextView) findViewById(R.id.tv_tyj_balance);
        tv_tyj_all = (TextView) findViewById(R.id.tv_tyj_all);
        lv_mangotyj = (ListView) findViewById(R.id.lv_mangotyj);
        listContentView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.itemlayout_mgtyj_list, null);
        tv_incometype = (TextView) listContentView.findViewById(R.id.tv_incometype);
        adapter = new ExperienceInvestRecordAdapter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTextData();
//        if (myTyjBean != null) {
//            getTextData();
//        }
    }

    private void getTextData() {

        int id = sp.getInt("id", 0);
        mid = String.valueOf(id);

        Map<String, String> params = new HashMap<>();
        params.put("mid", mid);

        myTyjPresenter.sendRequest(params, null);
    }

    @Override
    public void onClick(View v) {
        if (v == actionbar.rightImageView) {
            MyActivityManager.getInstance().startNextActivity(CommonWebViewActivity.class, "experienceGoldProblem");
        } else if (v == actionbar.leftImageView) {
//            popActivity();
            MyActivityManager.getInstance().popCurrentActivity();
        } else if (v == btn_tyj_invest) {

            //判断是否为新用户
            int memberType = sp.getInt("memberType", 0);
            if (newcomerBalance != null) {
                if (newcomerBalance >= 100) {


                    //请求弹窗显示内容数据
                    mid = String.valueOf(sp.getInt("id", 0));
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("mid", mid);
                    newcomerTasteTenderPresenter.sendRequest(params, null);


                } else {
                    final CustomCommonDialog dialog = new CustomCommonDialog(true, MangoExperienceFinancingActivity.this, "温馨提示", "很抱歉,您当前体验金余额不足", "确定", false
                    );
                    dialog.show();
                    dialog.setClicklistener(new CustomCommonDialog.ClickListenerInterfaceSingle() {
                        @Override
                        public void doSingleBtn() {
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                final CustomCommonDialog dialog = new CustomCommonDialog(true, MangoExperienceFinancingActivity.this, "温馨提示", "很抱歉,您当前体验金余额不足", "确定", false
                );
                dialog.show();
                dialog.setClicklistener(new CustomCommonDialog.ClickListenerInterfaceSingle() {
                    @Override
                    public void doSingleBtn() {
                        dialog.dismiss();
                    }
                });
            }

        } else if (v == layout_tyj_balance) {
            MyActivityManager.getInstance().startNextActivity(CapitalDetailsActivity.class, "107");
        } else if (v == layout_tyj_allbalance) {
            MyActivityManager.getInstance().startNextActivity(CapitalDetailsActivity.class, "107");
        }

    }

    private void showDialog() {
        inflater = LayoutInflater.from(this);
        //设置弹出内容
        investDialog = new MangoTyjInvestDialog(this, projtitle, rate.toString(), invest_period, new DecimalFormat("###,###,##0.00").format(projectaccount), repaymenttype, interesttime, new DecimalFormat("###,###,##0.00").format(investableaccount), new DecimalFormat("###,###,##0.00").format(balance));
        investDialog.setCancelable(false);

        investDialog.show();
        investDialog.setClicklistener(new MangoTyjInvestDialog.ClickListenerInterface() {
            @Override
            public void doConfirm(Editable text) {
                doInvest(text.toString());
            }

            @Override
            public void doCancel() {
                investDialog.dismiss();
            }
        });
    }

    private void doInvest(String s) {
        mid = String.valueOf(sp.getInt("id", 0));
        code = myTyjBean.getCode();
        amount = myTyjBean.getTicketBalance();
        Map<String, String> params = new HashMap<String, String>();
        params.put("mid", mid);
        params.put("code", code);
        params.put("amount", amount + "");
        newcomerTenderInvestPresenter.sendRequest(params, null);
    }

    /**
     * 设置投资记录数据
     */
    private void bindListData() {
        if (investmentRecord != null) {
            adapter.setListData(investmentRecord);
            if (lv_mangotyj.getAdapter() == null) {
                lv_mangotyj.setAdapter(adapter);
            }
            adapter.notifyDataSetChanged();
        }
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
    public void showError(OnPresenterListener listener, MyTyjBean myTyjBean) {
        CommonToastUtils.showToast(this, myTyjBean.getMsg());
        dismissLoadingDialog();
    }

    @Override
    public void responseData(OnPresenterListener listener, MyTyjBean myTyjBean) {
        this.myTyjBean = myTyjBean;
        if (listener instanceof MyTyjPresenterImpl) {
            investmentRecord = myTyjBean.getInvestmentRecord();
            if (investmentRecord.size() == 0) {
                list_content.setVisibility(View.VISIBLE);
            } else {
                list_content.setVisibility(View.GONE);
            }
            ljlxNewcomer = myTyjBean.getLjlxNewcomer();
            newcomerBalance = myTyjBean.getNewcomerBalance();
            newcomerTotalAmount = myTyjBean.getNewcomerTotalAmount();
            tv_mgtyj_waitincome.setText(String.valueOf(ljlxNewcomer));
            if (ljlxNewcomer == null) {
                tv_mgtyj_waitincome.setText("0.0");
            } else {
                tv_mgtyj_waitincome.setText(new DecimalFormat("###,###,##0.00").format(ljlxNewcomer));
            }
            if (newcomerBalance == null) {
                tv_tyj_balance.setText("0.0");
            } else {
                tv_tyj_balance.setText(new DecimalFormat("###,###,##0.00").format(newcomerBalance));
            }
            if (newcomerTotalAmount == null) {
                tv_tyj_all.setText("0.0");
            } else {
                tv_tyj_all.setText(new DecimalFormat("###,###,##0.00").format(newcomerTotalAmount));
            }
            bindListData(); //投资记录


        } else if (listener instanceof NewcomerTasteTenderPresenterImpl) {
            this.myTyjBean = myTyjBean;
            xsbCode = myTyjBean.getCode();
            //dialog数据
            projtitle = myTyjBean.getTitle();
            rate = myTyjBean.getRate();
            invest_period = myTyjBean.getTenderTerm();
            balance = myTyjBean.getTicketBalance();
            projectaccount = myTyjBean.getAmount();
            repaymenttype = myTyjBean.getRepaymentType();
            interesttime = myTyjBean.getIncomeTime();
            investableaccount = myTyjBean.getXstybBalance();
            if (xsbCode == null) {
                final CustomCommonDialog dialog = new CustomCommonDialog(true, this, "温馨提示", "很抱歉,暂时没有可投资的新手体验标", "确定", false);
                dialog.show();
                dialog.setClicklistener(new CustomCommonDialog.ClickListenerInterfaceSingle() {
                    @Override
                    public void doSingleBtn() {
                        dialog.dismiss();
                    }
                });

            } else {
                showDialog();
            }
        } else if (listener instanceof NewcomerTenderInvestPresenterImpl) {

            CommonToastUtils.showToast(this, "投资成功");

            investDialog.dismiss();

            getTextData();//刷新页面
        }


    }
}
