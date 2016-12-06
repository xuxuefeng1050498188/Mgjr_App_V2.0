package com.mgjr.view.invester;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.PopwUtils;
import com.mgjr.Utils.SPUtils;
import com.mgjr.model.bean.InvestRecordBean;
import com.mgjr.presenter.impl.InvestRecordPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.RefreshListView;
import com.mgjr.view.listeners.ViewListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class InvestRecordActivity extends ActionbarActivity implements ViewListener<InvestRecordBean> {

    /*@InjectView(R.id.tv_invest_record_tenderKing_username)
    TextView tvInvestRecordTenderKingUsername;
    @InjectView(R.id.iv_invest_record_tenderKing)
    ImageView ivInvestRecordTenderKing;
    @InjectView(R.id.tv_invest_record_tenderKing_amount)
    TextView tvInvestRecordTenderKingAmount;
    @InjectView(R.id.tv_invest_record_firstTender_phonenum)
    TextView tvInvestRecordFirstTenderPhonenum;
    @InjectView(R.id.iv_invest_record_firstTender)
    ImageView ivInvestRecordFirstTender;
    @InjectView(R.id.tv_invest_record_firstTender_time)
    TextView tvInvestRecordFirstTenderTime;
    @InjectView(R.id.listview_invest_record)
    RefreshListView listviewInvestRecord;*/
    @InjectView(R.id.ll_no_invest_record)
    LinearLayout llNoInvestRecord;
    @InjectView(R.id.fl_first_tender_area)
    FrameLayout flFirstTenderArea;
    @InjectView(R.id.invest_listview_tittle)
    FrameLayout investListviewTittle;
    private RefreshListView listview_invest_record;
    private String code;
    private InvestRecordPresenterImpl investRecordPresenterImpl;
    private String type;
    private InvestRecordBean investRecordBean;
    private InvestRecordAdapter investRecordAdapter;
    private PopupWindow loadingPopW;
    private int mid;
    private ImageView ivInvestRecordTenderKing;
    private ImageView ivInvestRecordFirstTender;
    private TextView tvInvestRecordTenderKingUsername;
    private TextView tvInvestRecordTenderKingAmount;
    private TextView tvInvestRecordFirstTenderPhonenum;
    private TextView tvInvestRecordFirstTenderTime;
    private int pageNum = 1;
    private List<InvestRecordBean.TenderBean> tenderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_invest_record, this);
        ButterKnife.inject(this);
        actionbar.setCenterTextView("投资记录");


        investRecordPresenterImpl = new InvestRecordPresenterImpl(this);
        mid = (int) SPUtils.get(this, "LOGIN", "id", -1);
        //拿到产品详情传递传递过来的code
        code = getIntent().getStringExtra("code");
        type = getIntent().getStringExtra("type");
        initInvestRecordListView();

    }

    private void initInvestRecordListView() {
//        flFirstTenderArea.setVisibility(View.GONE);
//        investListviewTittle.setVisibility(View.GONE);
        llNoInvestRecord.setVisibility(View.GONE);
        listview_invest_record = (RefreshListView) findViewById(R.id.listview_invest_record);
        View firstTenderView = LayoutInflater.from(this).inflate(R.layout.layout_invest_record_first_tender, null);
        View listViewTittleView = LayoutInflater.from(this).inflate(R.layout.layout_invest_record_listview_tittle, null);
        ivInvestRecordTenderKing = (ImageView) firstTenderView.findViewById(R.id.iv_invest_record_tenderKing);
        ivInvestRecordFirstTender = (ImageView) firstTenderView.findViewById(R.id.iv_invest_record_firstTender);
        tvInvestRecordTenderKingUsername = (TextView) firstTenderView.findViewById(R.id.tv_invest_record_tenderKing_username);
        tvInvestRecordTenderKingAmount = (TextView) firstTenderView.findViewById(R.id.tv_invest_record_tenderKing_amount);
        tvInvestRecordFirstTenderPhonenum = (TextView) firstTenderView.findViewById(R.id.tv_invest_record_firstTender_phonenum);
        tvInvestRecordFirstTenderTime = (TextView) firstTenderView.findViewById(R.id.tv_invest_record_firstTender_time);
        listview_invest_record.addHeaderView(firstTenderView);
        listview_invest_record.addHeaderView(listViewTittleView);
        listview_invest_record.setmOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;

                tenderList.clear();

                networkRequest();
            }
        });

        listview_invest_record.setmOnLoadMoreListener(new RefreshListView.OnLoadMoreListener() {
            @Override
            public void OnLoadMore() {
                pageNum++;
                networkRequest();
            }
        });

    }

    /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            networkRequest();
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        networkRequest();
    }

    //请求数据
    private void networkRequest() {

        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        necessaryParams.put("code", code);
        necessaryParams.put("pageNum", "" + pageNum);
        necessaryParams.put("pageSize", "" + 30);
        necessaryParams.put("type", type);
        investRecordPresenterImpl.sendRequest(necessaryParams, null);
    }

    @Override
    public void showLoading() {
        if (pageNum == 1) {
            /*final View rootview = LayoutInflater.from(this).inflate(R.layout.activity_invest_record, null);


            if (loadingPopW == null) {
                loadingPopW = PopwUtils.showLoadingPopw(this);
            }
            rootview.post(new Runnable() {
                public void run() {
                    loadingPopW.showAtLocation(rootview, Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            });*/
            showLoadingDialog();
        }

    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @Override
    public void showError() {
        listview_invest_record.setOnRefreshComplete();
        dismissLoadingDialog();
        showSystemError();
    }

    @Override
    public void showError(OnPresenterListener listener, InvestRecordBean investRecordBean) {
        listview_invest_record.setOnRefreshComplete();
        dismissLoadingDialog();
        CommonToastUtils.showToast(this, investRecordBean.getMsg());
    }

    @Override
    public void responseData(OnPresenterListener listener, InvestRecordBean investRecordBean) {
        this.investRecordBean = investRecordBean;
        if (investRecordBean.getFirstTender() == null && pageNum == 1) {
            bindNoInvestRecordData();
            return;
        }
        //绑定标王和首标的数据
        bindData();
        listview_invest_record.setOnRefreshComplete();
        if (investRecordBean.getTenderList() != null) {
            tenderList.addAll(investRecordBean.getTenderList());
//            processInvestRecordData(tenderList);
            listview_invest_record.setOnLoadMoreComplete();
            if (investRecordAdapter != null) {
                investRecordAdapter.notifyDataSetChanged();
            } else {
                investRecordAdapter = new InvestRecordAdapter();
                listview_invest_record.setAdapter(investRecordAdapter);
            }
        } else {
            listview_invest_record.setOnLoadMoreComplete();
        }
    }


    private void processInvestRecordData(List<InvestRecordBean.TenderBean> tenderList) {
        ArrayList<InvestRecordBean.TenderBean> myInvestRecordList = new ArrayList<>();
        ArrayList<InvestRecordBean.TenderBean> othersInvestRecordList = new ArrayList<>();

        for (int i = 0; i < tenderList.size(); i++) {
            InvestRecordBean.TenderBean tenderBean = tenderList.get(i);
            if (mid != -1 && tenderBean.getMid() == mid) {
                tenderBean.setUsername("我");
                myInvestRecordList.add(tenderBean);
            } else {
                othersInvestRecordList.add(tenderBean);
            }
        }
        tenderList.clear();
        tenderList.addAll(myInvestRecordList);
        tenderList.addAll(othersInvestRecordList);
    }

    public void bindNoInvestRecordData() {
        listview_invest_record.setVisibility(View.GONE);
        investListviewTittle.setVisibility(View.GONE);
        llNoInvestRecord.setVisibility(View.VISIBLE);
//        flFirstTenderArea.setVisibility(View.VISIBLE);
        ivInvestRecordTenderKing = (ImageView) flFirstTenderArea.findViewById(R.id.iv_invest_record_tenderKing);
        ivInvestRecordFirstTender = (ImageView) flFirstTenderArea.findViewById(R.id.iv_invest_record_firstTender);
        tvInvestRecordTenderKingUsername = (TextView) flFirstTenderArea.findViewById(R.id.tv_invest_record_tenderKing_username);
        tvInvestRecordTenderKingAmount = (TextView) flFirstTenderArea.findViewById(R.id.tv_invest_record_tenderKing_amount);
        tvInvestRecordFirstTenderPhonenum = (TextView) flFirstTenderArea.findViewById(R.id.tv_invest_record_firstTender_phonenum);
        tvInvestRecordFirstTenderTime = (TextView) flFirstTenderArea.findViewById(R.id.tv_invest_record_firstTender_time);

        ivInvestRecordTenderKing.setBackgroundResource(R.drawable.invest_record_tenderking_null);
        ivInvestRecordFirstTender.setBackgroundResource(R.drawable.invest_record_firsttender_null);

        //绑定标王数据
        tvInvestRecordTenderKingUsername.setText("尚未诞生");
        tvInvestRecordTenderKingAmount.setText("我来抢!");
        tvInvestRecordTenderKingAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipToInvestConfirmActivity();
            }
        });
        //绑定首标数据
        tvInvestRecordFirstTenderPhonenum.setText("尚未诞生");
        tvInvestRecordFirstTenderTime.setText("我来比!");
        tvInvestRecordFirstTenderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipToInvestConfirmActivity();
            }
        });
    }

    private void bindData() {
        //绑定首标数据
        if (investRecordBean.getFirstTender() != null) {
           /* if (pageNum == 1) {
                tvInvestRecordTenderKingUsername.setText("尚未诞生");
                tvInvestRecordTenderKingAmount.setText("我来抢!");
                tvInvestRecordTenderKingAmount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        skipToInvestConfirmActivity();
                    }
                });
            }
            */
            tvInvestRecordFirstTenderTime.setText(investRecordBean.getFirstTender().getSeckillTime());
            tvInvestRecordFirstTenderPhonenum.setText(investRecordBean.getFirstTender().getUsername());

        }
        if (investRecordBean.getTenderKing() != null) {
/*
            if (pageNum == 1) {
                tvInvestRecordFirstTenderPhonenum.setText("尚未诞生");
                tvInvestRecordFirstTenderTime.setText("我来比!");
                tvInvestRecordFirstTenderTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        skipToInvestConfirmActivity();
                    }
                });
            }*/

            //绑定标王数据
            tvInvestRecordTenderKingAmount.setText(formatAmount(investRecordBean.getTenderKing().getAmount()));
            tvInvestRecordTenderKingUsername.setText(investRecordBean.getTenderKing().getUsername());
        }
    }

    private void skipToInvestConfirmActivity() {
        if ("0".equals(type)) {
            //活期宝
            //type  0:代表活期宝  1:代表金芒果或者秒杀标
            MyActivityManager.getInstance().startNextActivity(InvestConfirmActivity.class, code, "0");
        } else {
            //金芒果或者新手福利标或者秒杀标
            MyActivityManager.getInstance().startNextActivity(InvestConfirmActivity.class, code, "1");
        }
    }

    //格式化数据
    private String formatAmount(Double amount) {
        if (amount < 10000) {
            return "￥" + new DecimalFormat("###,###,###").format(amount) + "元";
        } else {
//            return "￥" + new DecimalFormat("#.00").format(amount / 10000) + "万元";
            return "￥" + new DecimalFormat("###,###,###").format(amount) + "元";
        }
    }


    class InvestRecordAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return tenderList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.item_invest_record, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //设置条目数据
            InvestRecordBean.TenderBean tenderBean = tenderList.get(position);
            /*if (investRecordBean.getTenderList().get(position).getUsername() == null) {
                tenderBean = investRecordBean.getFirstTender();
            }*/
            if (tenderBean.getFromsource() == 0) {
                holder.ivItemInvestRecordTerminal.setImageResource(R.drawable.invest_record_computer_bg);
            } else {
                holder.ivItemInvestRecordTerminal.setImageResource(R.drawable.invest_record_phone_bg);
            }
            holder.tvItemInvestRecordUsername.setText(tenderBean.getUsername());
            holder.ivItemInvestRecordAmount.setText(new DecimalFormat("###,###,###").format(tenderBean.getAmount()));
            holder.ivItemInvestRecordDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(tenderBean.getCtime())));
            holder.ivItemInvestRecordTime.setText(new SimpleDateFormat(" HH:mm:ss").format(new Date(tenderBean.getCtime())));

            return convertView;
        }

        class ViewHolder {
            @InjectView(R.id.iv_item_invest_record_terminal)
            ImageView ivItemInvestRecordTerminal;
            @InjectView(R.id.tv_item_invest_record_username)
            TextView tvItemInvestRecordUsername;
            @InjectView(R.id.iv_item_invest_record_amount)
            TextView ivItemInvestRecordAmount;
            @InjectView(R.id.iv_item_invest_record_date)
            TextView ivItemInvestRecordDate;
            @InjectView(R.id.iv_item_invest_record_time)
            TextView ivItemInvestRecordTime;

            ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }
        }
    }


}
