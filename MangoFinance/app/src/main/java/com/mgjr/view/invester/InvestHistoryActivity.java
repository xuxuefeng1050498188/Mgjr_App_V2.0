package com.mgjr.view.invester;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.LogUtil;
import com.mgjr.R;
import com.mgjr.Utils.PopwUtils;
import com.mgjr.Utils.SPUtils;
import com.mgjr.model.bean.InvestBean;
import com.mgjr.model.bean.InvestProductDetailBean;
import com.mgjr.model.bean.LoanBean;
import com.mgjr.presenter.impl.InvestHomePresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.RefreshListView;
import com.mgjr.view.invester.adapter.InvestHistoryLIstViewAdapter;
import com.mgjr.view.invester.adapter.InvestListViewAdapter;
import com.mgjr.view.listeners.ViewListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvestHistoryActivity extends ActionbarActivity implements ViewListener<InvestBean> {

    private RefreshListView mListView;

    private InvestHomePresenterImpl investHomePresenterImpl;
    private InvestHistoryLIstViewAdapter investHistoryAdapter;
    private InvestBean investBean;
    private int pageNum = 1;
    private List<LoanBean> jmgList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_invest_history, this);
        actionbar.setCenterTextView("历史项目");

        investHomePresenterImpl = new InvestHomePresenterImpl(this);
        initInvestHistoryListView();
    }

    private void initInvestHistoryListView() {
        mListView = (RefreshListView) findViewById(R.id.lv_investfragment);

        //绑定数据
        if (investHistoryAdapter == null) {
            investHistoryAdapter = new InvestHistoryLIstViewAdapter(jmgList);
            mListView.setAdapter(investHistoryAdapter);
        }
        mListView.setmOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                jmgList.clear();
                networkRequest();
            }
        });

        mListView.setmOnLoadMoreListener(new RefreshListView.OnLoadMoreListener() {
            @Override
            public void OnLoadMore() {
                pageNum++;
                networkRequest();
            }
        });
        //为条目设置点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyActivityManager.getInstance().startNextActivity(InvestProductDetailActivity.class, jmgList.get(position - 1).getCode(), "1");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        networkRequest();
    }

    private void networkRequest() {
        Map<String, String> necessaryParams = new HashMap<String, String>();
        necessaryParams.put("mid", "" + SPUtils.get(this, "LOGIN", "id", 0));
        necessaryParams.put("status", "3");
        necessaryParams.put("page", "" + this.pageNum);
        necessaryParams.put("pageSize", "5");
        investHomePresenterImpl.sendRequest(necessaryParams, null);
        LogUtil.e("申请网络数据");
    }

    @Override
    public void showLoading() {
        if (pageNum == 1) {
            showLoadingDialog();
        }
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @Override
    public void showError() {
        mListView.setOnRefreshComplete();
        dismissLoadingDialog();
        showSystemError();
    }

    @Override
    public void showError(OnPresenterListener listener, InvestBean investBean) {
        mListView.setOnRefreshComplete();
        dismissLoadingDialog();
        CommonToastUtils.showToast(this, investBean.getMsg());
    }

    @Override
    public void responseData(OnPresenterListener listener, InvestBean investBean) {
        if (listener instanceof InvestHomePresenterImpl) {
            removeIllegaData(investBean);
            this.investBean = investBean;
            mListView.setOnRefreshComplete();
            if (investBean.getLoan() != null) {
                jmgList.addAll(investBean.getLoan());
                mListView.setOnLoadMoreComplete();
                if (investHistoryAdapter != null)
                    investHistoryAdapter.notifyDataSetChanged();
            } else {
                //数据全部加载完毕
                mListView.setOnRefreshComplete();
            }
        }
    }

    private void removeIllegaData(InvestBean investBean) {
        //遍历集合
        for (int i = 0; i < investBean.getLoan().size(); i++) {
            LoanBean loanBean = investBean.getLoan().get(i);
            if ((loanBean.getStatus() == 0 || loanBean.getStatus() == 1)) {
                investBean.getLoan().remove(loanBean);
            }
        }
    }
}
