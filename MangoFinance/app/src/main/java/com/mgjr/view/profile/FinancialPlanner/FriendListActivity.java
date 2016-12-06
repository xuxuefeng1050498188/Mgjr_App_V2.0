package com.mgjr.view.profile.FinancialPlanner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.SPUtils;
import com.mgjr.model.bean.FriendListBean;
import com.mgjr.presenter.impl.FriendListPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.RefreshListView;
import com.mgjr.view.listeners.ViewListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/8.
 */
public class FriendListActivity extends ActionbarActivity implements ViewListener<FriendListBean> {
    private LinearLayout layout_friendlist_content, layout_tabtitle,layout_list_nocontent;
    private TextView tv_friendlist_friendsCount, tv_friendlist_friendsTenderCount, tv_friendlist_amount;
    private RefreshListView lv_friendlist;
    private String friendsCount, friendsTenderCount, jlAmount;
    private FriendListPresenterImpl friendListPresenter;
    private PopupWindow loadingPopupWindow;
    private FriendListAdapter adapter;
    private FriendListBean friendListBean;
    private List<FriendListBean.FriendsListBean> friendsList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_layout_friendlist, this);
        actionbar.setCenterTextView("好友列表");
        actionbar.centerTextView.setTextSize(18);
        initViews();
        initRefreshList();
    }



    @Override
    protected void onResume() {
        super.onResume();
        requestNetworkData();
    }

    private void initViews() {
        friendListPresenter = new FriendListPresenterImpl(this);
        layout_list_nocontent = (LinearLayout) findViewById(R.id.layout_list_nocontent);
        layout_friendlist_content = (LinearLayout) findViewById(R.id.layout_friendlist_content);
        layout_tabtitle = (LinearLayout) findViewById(R.id.layout_tabtitle);
        tv_friendlist_friendsCount = (TextView) findViewById(R.id.tv_friendlist_friendsCount);
        tv_friendlist_friendsTenderCount = (TextView) findViewById(R.id.tv_friendlist_friendsTenderCount);
        tv_friendlist_amount = (TextView) findViewById(R.id.tv_friendlist_amount);

        lv_friendlist = (RefreshListView) findViewById(R.id.lv_friendlist);

        friendsCount = getIntent().getStringExtra("code");
        friendsTenderCount = getIntent().getStringExtra("type");
        jlAmount = getIntent().getStringExtra("rate");
        tv_friendlist_friendsCount.setText(friendsCount);
        tv_friendlist_friendsTenderCount.setText(friendsTenderCount);
        tv_friendlist_amount.setText(jlAmount);
    }

    private void initRefreshList() {
        lv_friendlist.isLoadable = false;
        lv_friendlist.setmOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (friendsList != null) {
                    friendsList.clear();
                    adapter.notifyDataSetChanged();
                }
                requestNetworkData();
            }
        });
    }


    private void requestNetworkData() {
        String mid = SPUtils.get(this, "LOGIN", "id", 0) + "";
        Map<String, String> params = new HashMap<>();
        params.put("mid", mid);
        friendListPresenter.sendRequest(params, null);
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
    public void showError(OnPresenterListener listener, FriendListBean friendListBean) {
        CommonToastUtils.showToast(this, friendListBean.getMsg());
        dismissLoadingDialog();
    }

    @Override
    public void responseData(OnPresenterListener listener, FriendListBean friendListBean) {
        this.friendListBean = friendListBean;
        friendsList = friendListBean.getFriendsList();
        lv_friendlist.setOnRefreshComplete();
        if (friendsList.size() == 0) {
            layout_list_nocontent.setVisibility(View.VISIBLE);
//            layout_tabtitle.setVisibility(View.GONE);
        }else {
            layout_list_nocontent.setVisibility(View.GONE);
//            layout_tabtitle.setVisibility(View.VISIBLE);
        }
        adapter = new FriendListAdapter(friendsList, this);
        lv_friendlist.setAdapter(adapter);
    }
}
