package com.mgjr.view.profile.activityzone;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.model.bean.EventsBean;
import com.mgjr.presenter.impl.EventsPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.APIBuilder;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.CommonWebBrowserActivity;
import com.mgjr.share.CustomCommonDialog;
import com.mgjr.view.listeners.ViewListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

public class ProfileEventsListActivity extends ActionbarActivity implements ViewListener<EventsBean>, AdapterView.OnItemClickListener {

    private ListView mListView;
    private EventsPresenterImpl eventPresenter;
    private EventsBean mEventsBean;

    private String activity_url;
    private PopupWindow loadingPopupWindow;
    private ProfileEventsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_profile_events_list, this);
        actionbar.setCenterTextView("活动专区");
        eventPresenter = new EventsPresenterImpl(this);
        mListView = (ListView) findViewById(R.id.lv_profile_events_list);
    }

    /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        requestNetwork();
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        requestNetwork();
    }

    private void requestNetwork() {
        Map<String, String> params = new HashMap<>();
        params.put("device", APIBuilder.getDevice());
        eventPresenter.sendRequest(params, null);
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
    public void showError(OnPresenterListener listener, EventsBean eventsBean) {
        dismissLoadingDialog();
        CommonToastUtils.showToast(this, eventsBean.getMsg());
    }

    @Override
    public void responseData(OnPresenterListener listener, EventsBean eventsBean) {
        this.mEventsBean = eventsBean;
        if (adapter == null) {
            adapter = new ProfileEventsListAdapter(eventsBean.getActivityList(), this);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(this);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        activity_url = mEventsBean.getActivityList().get(position).getActivity_url();
        String title = mEventsBean.getActivityList().get(position).getTitle();
        int status = mEventsBean.getActivityList().get(position).getStatus();
        if (status == 2) {
//            CommonToastUtils.showToast(this, "本活动已经结束啦~感谢您的关注与支持");
            final CustomCommonDialog dialog = new CustomCommonDialog(true, this, "温馨提示", "本活动已经结束啦~\n感谢您的关注与支持", "知道了", false);
            dialog.setClicklistener(new CustomCommonDialog.ClickListenerInterfaceSingle() {
                @Override
                public void doSingleBtn() {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else {
            EventsBean.ActivityListBean activityListBean = mEventsBean.getActivityList().get(position);
            String title1 = activityListBean.getTitle();
            String shareContent = activityListBean.getShareContent();
            String shareUrl = activityListBean.getShareUrl();
            EventBus.getDefault().postSticky(activityListBean);
            MyActivityManager.getInstance().startNextActivity(CommonWebBrowserActivity.class, title1, shareContent, shareUrl);
        }

    }
}
