package com.mgjr.view.profile.myhqb;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.SPUtils;
import com.mgjr.Utils.swipemenulistview.SwipeMenu;
import com.mgjr.Utils.swipemenulistview.SwipeMenuCreator;
import com.mgjr.Utils.swipemenulistview.SwipeMenuItem;
import com.mgjr.Utils.swipemenulistview.SwipeMenuListView;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.model.bean.MyHqbRunningProjectBean;
import com.mgjr.presenter.impl.HqbMutilRedeemListPresenterImpl;
import com.mgjr.presenter.impl.HqbRedeemedPresenterImpl;
import com.mgjr.presenter.impl.MyHqbRunningProjectPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.RefreshListView;
import com.mgjr.view.invester.InvestConfirmActivity;
import com.mgjr.view.listeners.ViewListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.mgjr.R.id.itemcheckbox;

/**
 * Created by Administrator on 2016/8/10.
 */
public class MyHqbRunningProjectActivity extends ActionbarActivity implements View.OnClickListener, ViewListener<MyHqbRunningProjectBean>, ProjectsListAdapter.GetTenderIds {

    private PopupWindow popwindow;

    private LinearLayout exMenu;
    private LinearLayout list_content;
    //checkbox选项
    private LinearLayout layout_checkbox;
    private LinearLayout layout_projectlist_content;  //持有项目列表区域
    private LinearLayout layout_botoom_btnlayout;   //底部按钮区域
    private LinearLayout layout_runningproject_details;  //popwindow开关
    private LayoutInflater inflater;
    private CheckBox itemCheckbox;
    private boolean isPressed = false;
    private boolean isShowCheckBox = false;

    private RelativeLayout layout_hqb_runningproject_title;

    private ImageView imgbtn_back_hqb_runningproject, imgbtn_runningproject_details;

    private TextView tvbtn_runningproject_redeem, tvbtn_runningproject_pass;
    private TextView tv_selectbtn_redeem, tv_selected_count;

    private TextView tvbtn_mybqb_projects_exmenu_running, tvbtn_mybqb_projects_exmenu_redeemed;
    private LinearLayout layout_select_project;    //可选列表状态下的 底部布局

    private SwipeMenuListView lv_hqb_runningproject;

    private ProjectsListAdapter dataListAdapter;

    private boolean isVisiable;//是否显示checkbox
    private boolean isChecked;
    //    private CheckBox checkBox_allselect;
    private Handler mHandler;
    private List<String> tenderIdList;
    private String tenderIds;
    //listview item
    private RelativeLayout layout_runningprojectlist_content;
    private LinearLayout layout_redeemedprojectlist_content;
    private RefreshListView lv_redeemedproject;

    private HqbRedeemedListAdapter redeemedAdapter;
    private MyHqbRunningProjectPresenterImpl myHqbRunningProjectPresenter;
    private List<MyHqbRunningProjectBean.TenderListBean> tenderList;
    private HqbMutilRedeemListPresenterImpl hqbMutilRedeemPresenter;
    private List<MyHqbRunningProjectBean.TenderListBean> redeembeanList, beanList;
    private HqbRedeemedPresenterImpl hqbRedeemedPresenter;
    private List<MyHqbRunningProjectBean.TenderListBean> redeemTenderList;
    private String type;
    private PopupWindow loadingPopupWindow;
    private MyHqbRunningProjectBean myHqbRunningProjectBean;
    private int pageNum = 1;
    private final String pageSize = "10";

    private Set<String> listSet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_layout_hqbrunningproject, this);
        type = getIntent().getStringExtra("code");

        dataListAdapter = new ProjectsListAdapter(MyHqbRunningProjectActivity.this);
        dataListAdapter.setTenderIds(this);

        initAcitionbar();
        initViews();
        initRefreshListViewRunning();
        initRefreshListViewRedeemed();
    }

    private void initAcitionbar() {
        actionbar.setCenterTextView("持有中项目", this);
        actionbar.setCenterImgBtn(R.drawable.triangle_down_btn, this);
        actionbar.setCenterLayoutOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isPressed) {
                    actionbar.centerImgBtn.setRotation(180);
                    setPupWindow();
                } else {
                    actionbar.centerImgBtn.setRotation(0);
                    popwindow.dismiss();
                }
                isPressed = !isPressed;

            }
        });
    }

    //持有中项目列表下拉刷新
    private void initRefreshListViewRunning() {
        lv_hqb_runningproject.setmOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                listSet.clear();
                if (tenderList != null) {
                    tenderList.clear();
                    dataListAdapter.notifyDataSetChanged();
                }

                requestNetwork();
            }
        });
        lv_hqb_runningproject.setmOnLoadMoreListener(new RefreshListView.OnLoadMoreListener() {
            @Override
            public void OnLoadMore() {
                pageNum++;
                requestNetwork();
            }
        });
    }

    //已赎回项目列表下拉刷新
    private void initRefreshListViewRedeemed() {
        lv_redeemedproject.setmOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                listSet.clear();
                if (redeemTenderList != null) {
                    redeemTenderList.clear();
                    redeemedAdapter.notifyDataSetChanged();
                }
                requestRedeemedProjectData();
            }
        });
        lv_redeemedproject.setmOnLoadMoreListener(new RefreshListView.OnLoadMoreListener() {
            @Override
            public void OnLoadMore() {
                pageNum++;
                requestRedeemedProjectData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        initAcitionbar();

        String intentStr = (String) SPUtils.get(MyHqbRunningProjectActivity.this, "TempIntent", "intentResource", "");
        if (!intentStr.equals("")) {
            type = intentStr;
        }

        pageNum = 1;

        if (type != null) {
            tenderList.clear();
            listSet.clear();
            checkShowType();
        }

        if (dataListAdapter != null) {
            tenderList.clear();
            dataListAdapter.notifyDataSetChanged();
        }
        if (redeemedAdapter != null) {
            redeemTenderList.clear();
            redeemedAdapter.notifyDataSetChanged();
        }

    }


    private void changeDisplayStyle() {

        isVisiable = false;
        actionbar.leftTextView.setVisibility(View.GONE);
        actionbar.leftImageView.setVisibility(View.VISIBLE);
        layout_select_project.setVisibility(View.GONE);
        layout_botoom_btnlayout.setVisibility(View.VISIBLE);


//        checkBox_allselect.setChecked(false);
        dataListAdapter.isChecked(false);
        dataListAdapter.isVisiable(isVisiable);
        dataListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        type = "";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtils.clear(MyHqbRunningProjectActivity.this, "TempIntent");
    }

    private void checkShowType() {
        if (type.equalsIgnoreCase("redeemed")) {
            requestRedeemedProjectData();
            actionbar.centerTextView.setText("已赎回项目");
            layout_runningprojectlist_content.setVisibility(View.GONE);
            layout_redeemedprojectlist_content.setVisibility(View.VISIBLE);

            tvbtn_mybqb_projects_exmenu_redeemed.setTextColor(Color.parseColor("#feaa00"));
            tvbtn_mybqb_projects_exmenu_redeemed.setBackgroundResource(R.drawable.shape_capitaildetails_title);
            tvbtn_mybqb_projects_exmenu_running.setTextColor(Color.GRAY);
            tvbtn_mybqb_projects_exmenu_running.setBackgroundResource(R.drawable.shape_capitaildetails_btn);
        } else if (type.equalsIgnoreCase("single")) {
            requestNetwork();
        } else if (type.equalsIgnoreCase("redeem")) {
            isShowCheckBox = true;
            actionbar.centerTextView.setText("选择赎回项目");
            changePageState();
            requestNetwork();
        } else if (type.equalsIgnoreCase("redeemedProjects")) {
            requestRedeemedProjectData();
            actionbar.centerTextView.setText("已赎回项目");
            layout_runningprojectlist_content.setVisibility(View.GONE);
            layout_redeemedprojectlist_content.setVisibility(View.VISIBLE);
            tvbtn_mybqb_projects_exmenu_redeemed.setTextColor(Color.parseColor("#feaa00"));
            tvbtn_mybqb_projects_exmenu_redeemed.setBackgroundResource(R.drawable.shape_capitaildetails_title);
            tvbtn_mybqb_projects_exmenu_running.setTextColor(Color.GRAY);
            tvbtn_mybqb_projects_exmenu_running.setBackgroundResource(R.drawable.shape_capitaildetails_btn);
        } else if (type.equalsIgnoreCase("doRedeem")) {
            requestNetwork();
            dataListAdapter.isVisiable(false);
            actionbar.leftTextView.setVisibility(View.GONE);
            actionbar.leftImageView.setVisibility(View.VISIBLE);
            layout_select_project.setVisibility(View.GONE);
            layout_botoom_btnlayout.setVisibility(View.VISIBLE);
            dataListAdapter.clearCheckedStatus("clear");
            if (tenderIdList != null) {
                tenderIdList.clear();
                tv_selected_count.setText(tenderIdList.size() + "");
            }
            tenderIds = "";
        } else {

            requestNetwork();
        }
    }

    private void initViews() {
        tenderList = new ArrayList<>();
        redeemTenderList = new ArrayList<>();
        listSet = new HashSet<>();

        hqbRedeemedPresenter = new HqbRedeemedPresenterImpl(this);
        myHqbRunningProjectPresenter = new MyHqbRunningProjectPresenterImpl(this);
        hqbMutilRedeemPresenter = new HqbMutilRedeemListPresenterImpl(this);
        layout_projectlist_content = (LinearLayout) findViewById(R.id.layout_projectlist_content);
        layout_botoom_btnlayout = (LinearLayout) findViewById(R.id.layout_botoom_btnlayout);
//        checkBox_allselect = (CheckBox) findViewById(R.id.checkBox_allselect);
        tv_selectbtn_redeem = (TextView) findViewById(R.id.tv_selectbtn_redeem);
        tv_selected_count = (TextView) findViewById(R.id.tv_selected_count);
        layout_select_project = (LinearLayout) findViewById(R.id.layout_select_project);
        layout_checkbox = (LinearLayout) findViewById(R.id.layout_checkbox);
        list_content = (LinearLayout) findViewById(R.id.list_content);
        lv_hqb_runningproject = (SwipeMenuListView) findViewById(R.id.lv_hqb_runningproject);
        itemCheckbox = (CheckBox) findViewById(itemcheckbox);
        mHandler = new Handler();

        layout_runningprojectlist_content = (RelativeLayout) findViewById(R.id.layout_runningprojectlist_content);
        tvbtn_runningproject_redeem = (TextView) findViewById(R.id.tvbtn_runningproject_redeem);
        tvbtn_runningproject_pass = (TextView) findViewById(R.id.tvbtn_runningproject_pass);
        tvbtn_runningproject_pass.setOnClickListener(this);
        tvbtn_runningproject_redeem.setOnClickListener(this);
        tv_selectbtn_redeem.setOnClickListener(this);
        inflater = (LayoutInflater) this.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        exMenu = (LinearLayout) inflater.inflate(R.layout.layout_myhqb_projects_exmenu, null, false);
        popwindow = new PopupWindow(exMenu, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        tvbtn_mybqb_projects_exmenu_running = (TextView) exMenu.findViewById(R.id.tvbtn_mybqb_projects_exmenu_running);
        tvbtn_mybqb_projects_exmenu_redeemed = (TextView) exMenu.findViewById(R.id.tvbtn_mybqb_projects_exmenu_redeemed);

        /*已赎回项目*/
        lv_redeemedproject = (RefreshListView) findViewById(R.id.lv_redeemedproject);
        layout_redeemedprojectlist_content = (LinearLayout) findViewById(R.id.layout_redeemedprojectlist_content);

        initListView();


    }

    private void requestNetwork() {
        int id = (int) SPUtils.get(this, "LOGIN", "id", 0);
        String mid = id + "";

        Map<String, String> params = new HashMap<>();
        params.put("mid", mid);
        params.put("pageNum", pageNum + "");
        params.put("pageSize", pageSize);
        myHqbRunningProjectPresenter.sendRequest(params, null);
    }

    @Override
    public void onClick(View v) {

        if (v == tvbtn_runningproject_redeem) {
            //赎回
            changePageState();

        } else if (v == actionbar.leftTextView) {
            initAcitionbar();
            changeDisplayStyle();
            if (dataListAdapter == null) {
                dataListAdapter = new ProjectsListAdapter(MyHqbRunningProjectActivity.this);
            }
            if (isVisiable) {
            } else {
                dataListAdapter.clearCheckedStatus("clear");
//                dataListAdapter.clearCheckedStatus("clear");
                if (tenderIdList != null) {
                    tenderIdList.clear();
                    tv_selected_count.setText(tenderIdList.size() + "");
                }
                tenderIds = "";
            }
        } else if (v == tvbtn_runningproject_pass) {
            //转入
            MyActivityManager.getInstance().startNextActivity(InvestConfirmActivity.class, "", "0");

        } else if (v == tv_selectbtn_redeem) {
            //多项赎回
            StringBuilder sb = new StringBuilder();
            if (tenderIdList != null) {
                for (int i = 0; i < tenderIdList.size(); i++) {
                    if (i == tenderIdList.size() - 1) {
                        sb.append(tenderIdList.get(i));
                    } else
                        sb.append(tenderIdList.get(i) + ",");
                }

                //请求网络
                String mid = SPUtils.get(this, "LOGIN", "id", 0) + "";
                tenderIds = sb.toString();

//                if (tenderIds != null && tenderIds.length() > 0) {
//                    Map<String, String> params = new HashMap<>();
//                    params.put("mid", mid);
//                    params.put("tenderIds", tenderIds);
//                    hqbMutilRedeemPresenter.sendRequest(params, null);
//                } else {
//                    CommonToastUtils.showToast(this, "请至少选择一笔赎回");
//                }
                if (tenderIds.length() == 0) {
                    CommonToastUtils.showToast(this, "请至少选择一笔赎回");
                } else if (tenderIdList.size() == 1) {
                    MyActivityManager.getInstance().startNextActivity(MyHqbRedeemedActivity.class, tenderIds, "single", "1");
                } else {
                    MyActivityManager.getInstance().startNextActivity(MyHqbRedeemedActivity.class, tenderIds, "mutil", "1");
                }
            }

        }
    }

    private void changePageState() {
        isVisiable = true;
        if (dataListAdapter == null) {
            dataListAdapter = new ProjectsListAdapter(MyHqbRunningProjectActivity.this);
        }
        dataListAdapter.isVisiable(isVisiable);
        actionbar.centerTextView.setText("选择赎回项目");
        actionbar.centerImgBtn.setVisibility(View.GONE);
        actionbar.setCenterImgBtn(0, null);
        actionbar.setCenterLayoutOnClick(null);
        actionbar.centerImgBtn.setClickable(false);

        layout_botoom_btnlayout.setVisibility(View.GONE);
        actionbar.leftTextView.setVisibility(View.VISIBLE);
        actionbar.setleftTextView("取消", this);
        actionbar.leftImageView.setVisibility(View.GONE);
        layout_select_project.setVisibility(View.VISIBLE);
        dataListAdapter.notifyDataSetChanged();

    }

    private void setPupWindow() {

        popwindow.setTouchable(true);
        popwindow.setOutsideTouchable(false);
        popwindow.setFocusable(false);
        popwindow.setBackgroundDrawable(new ColorDrawable(77000000));
        popwindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popwindow.update();
        tvbtn_mybqb_projects_exmenu_redeemed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //下拉菜单已赎回项目
                redeemTenderList.clear();
                pageNum = 1;
                actionbar.centerImgBtn.setRotation(0);
                actionbar.centerTextView.setText("已赎回项目");
                layout_runningprojectlist_content.setVisibility(View.GONE);
                layout_redeemedprojectlist_content.setVisibility(View.VISIBLE);
                tvbtn_mybqb_projects_exmenu_redeemed.setTextColor(Color.parseColor("#feaa00"));
                tvbtn_mybqb_projects_exmenu_redeemed.setBackgroundResource(R.drawable.shape_capitaildetails_title);
                tvbtn_mybqb_projects_exmenu_running.setTextColor(Color.GRAY);
                tvbtn_mybqb_projects_exmenu_running.setBackgroundResource(R.drawable.shape_capitaildetails_btn);
                requestRedeemedProjectData();
                popwindow.dismiss();
                SPUtils.put(MyHqbRunningProjectActivity.this, "TempIntent", "intentResource", "redeemed");
            }
        });
        tvbtn_mybqb_projects_exmenu_running.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //下拉菜单持有中项目
                tenderList.clear();
                pageNum = 1;
                actionbar.centerImgBtn.setRotation(0);
                actionbar.centerTextView.setText("持有中项目");
                layout_runningprojectlist_content.setVisibility(View.VISIBLE);
                layout_redeemedprojectlist_content.setVisibility(View.GONE);
                tvbtn_mybqb_projects_exmenu_running.setTextColor(Color.parseColor("#feaa00"));
                tvbtn_mybqb_projects_exmenu_redeemed.setTextColor(Color.GRAY);
                tvbtn_mybqb_projects_exmenu_redeemed.setBackgroundResource(R.drawable.shape_capitaildetails_btn);
                tvbtn_mybqb_projects_exmenu_running.setBackgroundResource(R.drawable.shape_capitaildetails_title);
                requestNetwork();
                popwindow.dismiss();
                SPUtils.put(MyHqbRunningProjectActivity.this, "TempIntent", "intentResource", "single");
            }
        });
        popwindow.showAsDropDown(actionbar);


    }

    /* 已赎回数据*/
    public void requestRedeemedProjectData() {
        String mid = SPUtils.get(this, "LOGIN", "id", 0) + "";
        Map<String, String> params = new HashMap<>();
        params.put("mid", mid);
        params.put("pageNum", pageNum + "");
        params.put("pageSize", "10");
        hqbRedeemedPresenter.sendRequest(params, null);
    }

    private void initListView() {
        com.mgjr.Utils.swipemenulistview.SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(MainApplication.getContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.parseColor("#feaa00")));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setBtnText("赎回");
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        lv_hqb_runningproject.setMenuCreator(creator);


        lv_hqb_runningproject.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        int id = tenderList.get(position).getId();
                        MyActivityManager.getInstance().startNextActivity(MyHqbRedeemedActivity.class, id + "", "single", "0");
                        break;
                    case 1:

                        break;
                }
                return false;
            }
        });
        // set SwipeListener
        lv_hqb_runningproject.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

    }

    private void bindListData() {
        if (tenderList != null) {


            dataListAdapter.setData(tenderList, lv_hqb_runningproject);

            if (lv_hqb_runningproject.getAdapter() == null) {
                lv_hqb_runningproject.setAdapter(dataListAdapter);
            }
//            if (dataListAdapter != null) {
//
            dataListAdapter.notifyDataSetChanged();
//            }
        }
    }


    /*
    * dp转px
    * */
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
//        PopwUtils.dismissLoadingPopw(loadingPopupWindow);
        dismissLoadingDialog();
    }

    @Override
    public void showError() {
//        PopwUtils.dismissLoadingPopw(loadingPopupWindow);
        dismissLoadingDialog();
        showSystemError();
    }

    @Override
    public void showError(OnPresenterListener listener, MyHqbRunningProjectBean myHqbRunningProjectBean) {
//        PopwUtils.dismissLoadingPopw(loadingPopupWindow);
        dismissLoadingDialog();
        CommonToastUtils.showToast(this, myHqbRunningProjectBean.getMsg());
    }

    @Override
    public void responseData(OnPresenterListener listener, MyHqbRunningProjectBean myHqbRunningProjectBean) {
        listSet.clear();
        this.myHqbRunningProjectBean = myHqbRunningProjectBean;
        if (listener instanceof MyHqbRunningProjectPresenterImpl) {
            beanList = myHqbRunningProjectBean.getTenderList();
            lv_hqb_runningproject.setOnRefreshComplete();
            if (beanList != null) {
                lv_hqb_runningproject.setOnLoadMoreComplete();

                List<MyHqbRunningProjectBean.TenderListBean> list1 = new ArrayList<>();
                List<MyHqbRunningProjectBean.TenderListBean> list2 = new ArrayList<>();
                for (int i = 0; i < beanList.size(); i++) {
                    MyHqbRunningProjectBean.TenderListBean bean = myHqbRunningProjectBean.getTenderList().get(i);
                    int redeemingCount = bean.getRedeemingCount();
                    String code = bean.getCode();
                    if (!listSet.contains(code)) {
                        listSet.add(code);
                        if (redeemingCount > 0) {
                            list1.add(bean);
                        } else {
                            list2.add(bean);
                        }
                    }
                }
                tenderList.addAll(list1);
                tenderList.addAll(list2);
                if (tenderList.size() != 0) {
                    if (pageNum == 1) {
                        bindListData();
                    } else {
                        dataListAdapter.notifyDataSetChanged();
                    }
                    list_content.setVisibility(View.INVISIBLE);
                    lv_hqb_runningproject.setVisibility(View.VISIBLE);
                } else {
                    list_content.setVisibility(View.VISIBLE);
                    lv_hqb_runningproject.setVisibility(View.INVISIBLE);
                }

                lv_hqb_runningproject.setOnLoadMoreComplete();
            } else {
                lv_hqb_runningproject.setOnLoadMoreComplete();
            }

            if (tenderList.size() == 0) {
                list_content.setVisibility(View.VISIBLE);
                lv_hqb_runningproject.setVisibility(View.GONE);
            } else {
                list_content.setVisibility(View.GONE);
                lv_hqb_runningproject.setVisibility(View.VISIBLE);
            }
        } else if (listener instanceof HqbMutilRedeemListPresenterImpl) {
//            if (tenderIdList.size() == 0) {
//                CommonToastUtils.showToast(this, "请至少选择一笔赎回");
//            } else if (tenderIdList.size() == 1) {
//                MyActivityManager.getInstance().startNextActivity(MyHqbRedeemedActivity.class, tenderIds, "single","1");
//            } else {
//                MyActivityManager.getInstance().startNextActivity(MyHqbRedeemedActivity.class, tenderIds, "mutil","1");
//            }
        } else if (listener instanceof HqbRedeemedPresenterImpl) {
            /*已赎回项目*/
            redeembeanList = myHqbRunningProjectBean.getTenderList();
            lv_redeemedproject.setOnRefreshComplete();
            if (redeembeanList == null) {

                lv_redeemedproject.setOnLoadMoreComplete();
            } else {
                redeemTenderList.addAll(redeembeanList);
                if (redeemTenderList.size() != 0) {
                    if (pageNum == 1) {
                        if (redeemedAdapter == null) {
                            redeemedAdapter = new HqbRedeemedListAdapter(redeemTenderList, lv_redeemedproject, MyHqbRunningProjectActivity.this);
                            lv_redeemedproject.setAdapter(redeemedAdapter);
                        }
                    } else {
                        redeemedAdapter.notifyDataSetChanged();
                    }

                }
                lv_redeemedproject.setOnLoadMoreComplete();
            }

            if (redeemedAdapter != null) {
                redeemedAdapter.notifyDataSetChanged();
            }

            if (redeemTenderList.size() != 0) {

                list_content.setVisibility(View.GONE);
                lv_redeemedproject.setVisibility(View.VISIBLE);
            } else {

                list_content.setVisibility(View.VISIBLE);
                lv_redeemedproject.setVisibility(View.GONE);
            }
        }

    }


    @Override
    public void getMutilRedeemTenderIds() {
        if (tenderIdList == null) {
            tenderIdList = new ArrayList<>();
        }
        for (int i = 0; i < tenderList.size(); i++) {
            String id = tenderList.get(i).getId() + "";
            if (!tenderIdList.contains(id)) {
                tenderIdList.add(id);
            }
        }
        tv_selected_count.setText(tenderIdList.size() + "");
    }

    @Override
    public void cancleMutilCheck() {
        if (tenderIdList == null) {
            tenderIdList = new ArrayList<>();
        }
        tenderIdList.clear();
        tv_selected_count.setText(tenderIdList.size() + "");
    }

    @Override
    public void singleIsChecked(int position, CheckBox checkBox) {

        if (tenderIdList == null) {
            tenderIdList = new ArrayList<>();
        }
        if (position >= 0 && position < tenderList.size()) {
            String id = tenderList.get(position).getId() + "";
            if (checkBox.isChecked()) {
                if (!tenderIdList.contains(id)) {
                    tenderIdList.add(id);
                }
            } else {
                tenderIdList.remove(id);
            }
            tv_selected_count.setText(tenderIdList.size() + "");
        }

    }
}
