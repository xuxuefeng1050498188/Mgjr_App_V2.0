package com.mgjr.view.profile.myJmg;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.SPUtils;
import com.mgjr.model.bean.MyJmgFinishedProjectsBean;
import com.mgjr.model.bean.MyJmgInvestingProjectsBean;
import com.mgjr.presenter.impl.MyJmgFinishedProjectsPresenterImpl;
import com.mgjr.presenter.impl.MyJmgInvestingProjectsPresenterImpl;
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
 * Created by Administrator on 2016/8/16.
 */
public class MyJmgInvestingAndFinishedProjectsActivity extends ActionbarActivity implements ViewListener<MyJmgInvestingProjectsBean>, View.OnClickListener {

    private RefreshListView lv_myjmg_investingprojects;

    private RefreshListView lv_my_jmg_finished_projects;
    private LayoutInflater inflater;

    private LinearLayout exMenu;
    private LinearLayout list_content;
    private PopupWindow popWindow;

    private boolean isPressed = false;
    private MyJmgInvestingProjectsPresenterImpl jmgInvestingProjectsPresenterImpl;
    private MyJmgFinishedProjectsPresenterImpl myJmgFinishedProjectsPresenterImpl;
    private PopupWindow loadingPopW;
    private List<MyJmgInvestingProjectsBean.JmgTenderBean> investingBeanList;
    private MyJmgInvestingProjectsBean myJmgInvestingProjectsBean;
    private List<MyJmgInvestingProjectsBean.JmgTenderBean> tenderList;
    private JmgInvestingProjectsListAdapter mJmgInvestingProjectsListAdapter;
    private JmgFinishedProjectsListAdapter myJmgFinishedProjectsListAdapter;
    private List<MyJmgFinishedProjectsBean.OverTenderListBean> overTenderList;
    private TextView tvbtn_jmg_pupwindow_finished;
    private TextView tvbtn_jmg_pupwindow_invest;
    private String type;
    private String intentStr;
    private int pageNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_layout_myjmg_investingprojects, this);
        jmgInvestingProjectsPresenterImpl = new MyJmgInvestingProjectsPresenterImpl(this);
        myJmgFinishedProjectsPresenterImpl = new MyJmgFinishedProjectsPresenterImpl(this);
        type = getIntent().getStringExtra("code");
        initActionBar();
        initViews();

    }

    private void initActionBar() {
        if ("0".equals(type)) {
            actionbar.setCenterTextView("投资中项目");

        } else {
            actionbar.setCenterTextView("已结束项目");
        }
        actionbar.setCenterImgBtn(R.drawable.triangle_down_btn, this);
        actionbar.setCenterLayoutOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPressed) {
                    actionbar.centerImgBtn.setRotation(180);
                    isPressed = true;
                    setPupWindow();
                } else {
                    actionbar.centerImgBtn.setRotation(0);
                    if (popWindow != null) {
                        popWindow.dismiss();
                    }
                    isPressed = false;
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        intentStr = (String) SPUtils.get(this, "TempIntent", "InResourse", "");
        pageNum = 1;
//        if ("0".equals(type) || intentStr.equalsIgnoreCase("JmgInvesting")) {
//            enterInvestingProjects();
//        } else if ("1".equals(type) || intentStr.equalsIgnoreCase("JmgFinished")) {
        if ("1".equals(type) || intentStr.equalsIgnoreCase("JmgFinished")) {
            enterFinishedProjects();
        } else if ("0".equals(type) || intentStr.equalsIgnoreCase("JmgInvesting")) {
            enterInvestingProjects();
        }


        if (mJmgInvestingProjectsListAdapter != null) {
            tenderList.clear();
            mJmgInvestingProjectsListAdapter.notifyDataSetChanged();
        }
        if (myJmgFinishedProjectsListAdapter != null) {
            overTenderList.clear();
            myJmgFinishedProjectsListAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        SPUtils.clear(this, "TempIntent");
    }

    private void requestInvestingProjectData() {

        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        necessaryParams.put("mid", "" + SPUtils.get(this, "LOGIN", "id", 0));
        necessaryParams.put("pageNum", pageNum + "");
        necessaryParams.put("pageSize", "50");
        jmgInvestingProjectsPresenterImpl.sendRequest(necessaryParams, null);
//        LogUtil.e("申请网络数据");
    }

    private void initViews() {
        tenderList = new ArrayList<>();
        overTenderList = new ArrayList<>();
        lv_myjmg_investingprojects = (RefreshListView) findViewById(R.id.lv_my_jmg_investing_projects);
        lv_my_jmg_finished_projects = (RefreshListView) findViewById(R.id.lv_my_jmg_finished_projects);
        list_content = (LinearLayout) findViewById(R.id.list_content);

        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        exMenu = (LinearLayout) inflater.inflate(R.layout.layout_jmg_pupwindow, null);
        popWindow = new PopupWindow(exMenu, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        tvbtn_jmg_pupwindow_invest = (TextView) exMenu.findViewById(R.id.tvbtn_jmg_pupwindow_invest);
        tvbtn_jmg_pupwindow_finished = (TextView) exMenu.findViewById(R.id.tvbtn_jmg_pupwindow_finished);
        initRefreshList();
    }

    private void initRefreshList() {
        lv_myjmg_investingprojects.setmOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                if (tenderList != null) {
                    tenderList.clear();
                    if (mJmgInvestingProjectsListAdapter != null) {
                        mJmgInvestingProjectsListAdapter.notifyDataSetChanged();
                    }
                }

                requestInvestingProjectData();
            }
        });
        lv_myjmg_investingprojects.setmOnLoadMoreListener(new RefreshListView.OnLoadMoreListener() {
            @Override
            public void OnLoadMore() {
                pageNum++;
                requestInvestingProjectData();
            }
        });

        lv_my_jmg_finished_projects.setmOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                if (overTenderList != null) {
                    overTenderList.clear();
                    if (myJmgFinishedProjectsListAdapter != null) {
                        myJmgFinishedProjectsListAdapter.notifyDataSetChanged();
                    }
                }
                requestFinishedProjectData();
            }
        });

        lv_my_jmg_finished_projects.setmOnLoadMoreListener(new RefreshListView.OnLoadMoreListener() {
            @Override
            public void OnLoadMore() {
                pageNum++;
                requestFinishedProjectData();
            }
        });
    }

    private void setPupWindow() {

        tvbtn_jmg_pupwindow_invest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterInvestingProjects();

            }
        });

        tvbtn_jmg_pupwindow_finished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterFinishedProjects();

            }
        });
        popWindow.setBackgroundDrawable(new ColorDrawable());
        popWindow.setTouchable(true);
        popWindow.setFocusable(false);
        popWindow.setOutsideTouchable(true);
        popWindow.showAsDropDown(actionbar);
    }

    private void enterFinishedProjects() {
        overTenderList.clear();
        pageNum = 1;
        type = "1";
        intentStr = "JmgFinished";
        actionbar.centerImgBtn.setRotation(0);
        tvbtn_jmg_pupwindow_finished.setTextColor(Color.parseColor("#feaa00"));
        tvbtn_jmg_pupwindow_finished.setBackgroundResource(R.drawable.shape_capitaildetails_title);
        tvbtn_jmg_pupwindow_invest.setTextColor(Color.GRAY);
        tvbtn_jmg_pupwindow_invest.setBackgroundResource(R.drawable.shape_capitaildetails_btn);
        lv_my_jmg_finished_projects.setVisibility(View.VISIBLE);
        lv_myjmg_investingprojects.setVisibility(View.GONE);
        actionbar.setCenterTextView("已结束项目");
        if (popWindow != null) {
            popWindow.dismiss();
        }
        requestFinishedProjectData();
    }

    private void requestFinishedProjectData() {


        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        necessaryParams.put("mid", "" + SPUtils.get(this, "LOGIN", "id", 0));
        necessaryParams.put("pageNum", pageNum + "");
        necessaryParams.put("pageSize", "50");
        myJmgFinishedProjectsPresenterImpl.sendRequest(necessaryParams, null);
    }

    public void enterInvestingProjects() {
        tenderList.clear();
        pageNum = 1;
        type = "0";
        intentStr = "JmgInvesting";
        actionbar.centerImgBtn.setRotation(0);
        tvbtn_jmg_pupwindow_invest.setTextColor(Color.parseColor("#feaa00"));
        tvbtn_jmg_pupwindow_invest.setBackgroundResource(R.drawable.shape_capitaildetails_title);
        tvbtn_jmg_pupwindow_finished.setTextColor(Color.GRAY);
        tvbtn_jmg_pupwindow_finished.setBackgroundResource(R.drawable.shape_capitaildetails_btn);

        lv_my_jmg_finished_projects.setVisibility(View.GONE);
        lv_myjmg_investingprojects.setVisibility(View.VISIBLE);
        actionbar.setCenterTextView("投资中项目");
        if (popWindow != null) {
            popWindow.dismiss();
        }
        requestInvestingProjectData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtils.clear(this, "TempIntent");
    }

    @Override
    public void showLoading() {
//        View rootview = LayoutInflater.from(this).inflate(R.layout.activity_layout_myjmg_investingprojects, null);
//        loadingPopW = PopwUtils.showLoadingPopw(this, rootview);
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
//        PopwUtils.dismissLoadingPopw(loadingPopW);
        dismissLoadingDialog();
    }

    @Override
    public void showError() {
//        PopwUtils.dismissLoadingPopw(loadingPopW);
        dismissLoadingDialog();
        showSystemError();
    }


    @Override
    public void showError(OnPresenterListener listener, MyJmgInvestingProjectsBean myJmgInvestingProjectsBean) {
        dismissLoadingDialog();
        CommonToastUtils.showToast(this, myJmgInvestingProjectsBean.getMsg());
    }

    @Override
    public void responseData(OnPresenterListener listener, MyJmgInvestingProjectsBean myJmgInvestingProjectsBean) {
//        PopwUtils.dismissLoadingPopw(loadingPopW);
        this.myJmgInvestingProjectsBean = myJmgInvestingProjectsBean;
        if (listener instanceof MyJmgInvestingProjectsPresenterImpl) {
            lv_myjmg_investingprojects.setOnRefreshComplete();
            investingBeanList = myJmgInvestingProjectsBean.getTenderList();
            if (investingBeanList == null) {
                lv_myjmg_investingprojects.setOnLoadMoreComplete();
            } else {
                tenderList.addAll(investingBeanList);
                if (tenderList.size() == 0) {
                    list_content.setVisibility(View.VISIBLE);
                } else {
                    if (pageNum == 1) {
                        if (mJmgInvestingProjectsListAdapter == null) {
                            mJmgInvestingProjectsListAdapter = new JmgInvestingProjectsListAdapter(tenderList);
                            lv_myjmg_investingprojects.setAdapter(mJmgInvestingProjectsListAdapter);
                        }
                    } else {
                        if (mJmgInvestingProjectsListAdapter != null) {

                            mJmgInvestingProjectsListAdapter.notifyDataSetChanged();
                        }
                    }
                    setOnClickListener();
                    list_content.setVisibility(View.INVISIBLE);
                }
                lv_myjmg_investingprojects.setOnLoadMoreComplete();
                if (mJmgInvestingProjectsListAdapter != null) {
                    mJmgInvestingProjectsListAdapter.notifyDataSetChanged();
                }
            }


        } else {
            List<MyJmgFinishedProjectsBean.OverTenderListBean> finishedList = myJmgInvestingProjectsBean.getOverTenderList();

            lv_my_jmg_finished_projects.setOnRefreshComplete();
            if (finishedList == null) {
                lv_my_jmg_finished_projects.setOnLoadMoreComplete();
            } else {
                overTenderList.addAll(finishedList);
                if (overTenderList.size() != 0) {
                    if (pageNum == 1) {
                        if (myJmgFinishedProjectsListAdapter == null) {
                            myJmgFinishedProjectsListAdapter = new JmgFinishedProjectsListAdapter(overTenderList);
                            lv_my_jmg_finished_projects.setAdapter(myJmgFinishedProjectsListAdapter);
                        }
                    }
                } else {
                    if (myJmgFinishedProjectsListAdapter != null) {

                        myJmgFinishedProjectsListAdapter.notifyDataSetChanged();
                    }
                }

                list_content.setVisibility(View.INVISIBLE);
                setOnClickListener();
            }
            lv_my_jmg_finished_projects.setOnLoadMoreComplete();
            if (overTenderList.size() == 0) {
                list_content.setVisibility(View.VISIBLE);
                lv_my_jmg_finished_projects.setVisibility(View.GONE);
            } else {
                list_content.setVisibility(View.GONE);
                lv_my_jmg_finished_projects.setVisibility(View.VISIBLE);
            }
            if (myJmgFinishedProjectsListAdapter != null) {
                myJmgFinishedProjectsListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setOnClickListener() {
        lv_myjmg_investingprojects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long nextRepayTime = tenderList.get(position - 1).getNextRepayTime();
                String time = null;
                if (nextRepayTime != 0) {
                    time = new SimpleDateFormat("yyyy-MM-dd").format(nextRepayTime);
                }

                MyActivityManager.getInstance().startNextActivity(MyJmgProjectDetailsActivity.class, "" + tenderList.get(position - 1).getId(), time);
            }
        });
        lv_my_jmg_finished_projects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyActivityManager.getInstance().startNextActivity(MyJmgProjectDetailsActivity.class, "" + overTenderList.get(position - 1).getId());
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
