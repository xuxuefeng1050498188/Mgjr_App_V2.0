package com.mgjr.view.invester;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.SPUtils;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.model.bean.EventBusBean.NotificationBean;
import com.mgjr.model.bean.InvestBean;
import com.mgjr.model.bean.LoanBean;
import com.mgjr.presenter.impl.InvestHomePresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.BaseFrament;
import com.mgjr.share.NetUtils;
import com.mgjr.share.RefreshListView;
import com.mgjr.view.invester.adapter.InvestListViewAdapter;
import com.mgjr.view.listeners.ViewListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by wim on 16/5/23.
 */
public class InvestFragment extends BaseFrament implements ViewListener<InvestBean> {

    /**
     * 投资首页的listview
     */
    private RefreshListView mListView;

    /**
     * 投资首页的presenter
     */
    private InvestHomePresenterImpl investHomePresenterImpl;
    private InvestListViewAdapter investAdapter;
    private InvestBean investBean;
    private View layout;
    private PopupWindow loadingPopW;
    private LoanBean newUserBean;
    private List<LoanBean> loanBeanList = new ArrayList<>();
    private View hqbView;
    private KProgressHUD progressHUD;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        investHomePresenterImpl = new InvestHomePresenterImpl(this);
        layout = inflater.inflate(R.layout.invest_layout, null, false);
        //网络请求
        loanBeanList.clear();
        networkRequest();

        initInvestListView();
//        initLoadingProgressView();
        return layout;
    }

    private void initInvestListView() {
        mListView = (RefreshListView) layout.findViewById(R.id.lv_investFragment);
        mListView.isLoadable = false;
//        //添加脚布局
        mListView.addFooterView(View.inflate(MainApplication.getContext(), R.layout.item_check_invest_history, null));

        mListView.setmOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loanBeanList.clear();
                networkRequest();
            }
        });

        //为条目设置点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            String code = null;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mListView.getCount() - 1) {
                    //查看历史记录的条目
                    MyActivityManager.getInstance().startNextActivity(InvestHistoryActivity.class);
                } else if (position == 0) {
                } else if (position == 1) {
                    //活期宝详情界面
                    code = investBean.getHqb().getCode();
                    //type  0:代表活期宝  1:代表金芒果或者秒杀标
                    MyActivityManager.getInstance().startNextActivity(InvestProductDetailActivity.class, code, "0");
                } else {
                    //金芒果或者新手福利标
                    code = investBean.getLoan().get(position - 2).getCode();
                    MyActivityManager.getInstance().startNextActivity(InvestProductDetailActivity.class, code, "1");
                }
//                lastVisiblePosition = mListView.getLastVisiblePosition();
            }
        });
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
//        loanBeanList.clear();
//        networkRequest();
    }

    /**
     * 网络请求
     */
    private void networkRequest() {
        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        Map<String, String> unNecessaryParams = new HashMap<String, String>();
        int mid = (int) SPUtils.get(getContext(), "LOGIN", "id", -1);
        if (mid != -1) {
            unNecessaryParams.put("mid", mid + "");
        } else {
            unNecessaryParams.put("mid", "");
        }
        necessaryParams.put("status", "2");
        necessaryParams.put("page", "1");
        necessaryParams.put("pageSize", "10");
        investHomePresenterImpl.sendRequest(necessaryParams, unNecessaryParams);
    }


    @Override
    public void showLoading() {
//        progressHUD.show();
    }

    @Override
    public void hideLoading() {
//        progressHUD.dismiss();
    }

    @Override
    public void showError() {
        if (investAdapter != null) {
            investAdapter.notifyDataSetChanged();
        }
        if (mListView != null) {
            mListView.setOnRefreshComplete();
        }
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
    public void showError(OnPresenterListener listener, InvestBean investBean) {
        if (investAdapter != null) {
            investAdapter.notifyDataSetChanged();
        }
        if (mListView != null) {
            mListView.setOnRefreshComplete();
        }
        if (!NetUtils.isConnected(MainApplication.getContext())) {
            CommonToastUtils.showToast(MainApplication.getContext(), getString(R.string.network_error));
        } else {
            CommonToastUtils.showToast(MainApplication.getContext(), getString(R.string.system_error));
        }
    }


    @Override
    public void responseData(OnPresenterListener listener, InvestBean investBean) {
        if (mListView != null) {
            mListView.setOnRefreshComplete();
        }

        if (listener instanceof InvestHomePresenterImpl) {
            removeIllegaData(investBean);
            this.investBean = investBean;
            loanBeanList.addAll(this.investBean.getLoan());
            //绑定数据
            if (investAdapter == null) {
                //添加活期宝的头布局
                addHqbHeaderView();
                investAdapter = new InvestListViewAdapter(loanBeanList);
                mListView.setAdapter(investAdapter);
            } else {
                investAdapter.notifyDataSetChanged();
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

    /**
     * 添加listview的头布局(新手福利标和活期宝)
     */
    private void addHqbHeaderView() {
        hqbView = View.inflate(MainApplication.getContext(), R.layout.item_hqb_invest_listview, null);
        mListView.addHeaderView(hqbView);
    }


    /**
     * 当秒杀标变成投资的时候,重新请求数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void eventBus(NotificationBean event) {
        loanBeanList.clear();
        networkRequest();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

