package com.mgjr.view.profile.FinancialPlanner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.SPUtils;
import com.mgjr.model.bean.TenderRewardListBean;
import com.mgjr.presenter.impl.TenderRewardListPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.RefreshListView;
import com.mgjr.view.listeners.ViewListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/10.
 */
public class TenderRewardListActivity extends ActionbarActivity implements ViewListener<TenderRewardListBean>,RefreshListView.OnScrollViewListener {
    //累计收益
    private TextView tv_allincome;
    private RefreshListView lv_tenderlist;
    private LinearLayout layout_list_nocontent;
    private ImageView img_no_content;
    private TenderRewardListPresenterImpl tenderRewardListPresenter;
    private List<String> monthList;
    private List itemList;
    private TenderRewardListAdapter adapter;
    private PopupWindow loadingPopupWindow;
    private TenderRewardListBean tenderRewardListBean;
    private TextView layout_listtitle;
    private List<TenderRewardListBean.LcsTenderRewardListBean> tenderRewardList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_layout_tenderrewardlist, this);
        actionbar.setCenterTextView("奖励明细");
        initView();
        initRefreshList();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (tenderRewardListBean == null) {
                requestNetworkData();
            }
        }
    }

    private void requestNetworkData() {
        String mid = SPUtils.get(this, "LOGIN", "id", 0) + "";
        Map<String, String> params = new HashMap<>();
        params.put("mid", mid);
        tenderRewardListPresenter.sendRequest(params, null);
    }

    private void initView() {
        tenderRewardListPresenter = new TenderRewardListPresenterImpl(this);
        layout_list_nocontent = (LinearLayout) findViewById(R.id.layout_list_nocontent);
        layout_listtitle = (TextView) findViewById(R.id.layout_listtitle);
        tv_allincome = (TextView) findViewById(R.id.tv_allincome);
        lv_tenderlist = (RefreshListView) findViewById(R.id.lv_tenderlist);
        img_no_content = (ImageView) findViewById(R.id.img_no_content);
        adapter = new TenderRewardListAdapter(this,layout_listtitle);

        tv_allincome.setText(getIntent().getStringExtra("code"));


        lv_tenderlist.isLoadable = false;
    }


    private void initRefreshList() {
        lv_tenderlist.isLoadable = false;
        lv_tenderlist.setmOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (tenderRewardList != null) {
                    tenderRewardList.clear();
                    adapter.notifyDataSetChanged();
                }

                requestNetworkData();
            }
        });
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
    public void showError(OnPresenterListener listener, TenderRewardListBean tenderRewardListBean) {
        dismissLoadingDialog();
        CommonToastUtils.showToast(this, tenderRewardListBean.getMsg());
    }

    @Override
    public void responseData(OnPresenterListener listener, TenderRewardListBean tenderRewardListBean) {
        lv_tenderlist.setOnRefreshComplete();
        layout_listtitle.setVisibility(View.VISIBLE);
        this.tenderRewardListBean = tenderRewardListBean;
        String mobile = tenderRewardListBean.getMobile();
        tenderRewardList = tenderRewardListBean.getTenderRewardList();
        changeListStructure(tenderRewardList);
        if (itemList.size() == 0) {
            layout_list_nocontent.setVisibility(View.VISIBLE);
            layout_listtitle.setVisibility(View.GONE);
        } else {
            layout_list_nocontent.setVisibility(View.GONE);
            layout_listtitle.setVisibility(View.VISIBLE);
            adapter.setMobile(mobile);
            adapter.setListData(itemList);
            adapter.setListTitle(lv_tenderlist);
            lv_tenderlist.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }

    private void changeListStructure(List<TenderRewardListBean.LcsTenderRewardListBean> list) {
        itemList = new ArrayList();
        monthList = new ArrayList<>();
        for (TenderRewardListBean.LcsTenderRewardListBean map : list) {
            String date = longtoDate(map.getTime());
            if (!monthList.contains(date)) {
                monthList.add(date);
                Map<String, String> monthMap = new HashMap<>();
                monthMap.put("date", date);
                itemList.add(monthMap);
                itemList.add(map);
            } else {
                itemList.add(map);
            }
        }

    }

    private String longtoDate(long time) {
        String sMonth = new SimpleDateFormat("yyyy-MM").format(time);
        return sMonth;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (adapter != null && adapter.billlist != null) {
            adapter.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }
}
