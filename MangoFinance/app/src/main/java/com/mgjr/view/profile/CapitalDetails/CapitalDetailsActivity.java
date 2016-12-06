package com.mgjr.view.profile.CapitalDetails;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.PopwUtils;
import com.mgjr.Utils.SPUtils;
import com.mgjr.model.bean.CapitalDetailsBean;
import com.mgjr.presenter.impl.CapitalDetailsItemPresenterImpl;
import com.mgjr.presenter.impl.CapitalDetailsPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.RefreshListView;
import com.mgjr.view.listeners.ViewListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * Created by Administrator on 2016/7/26.
 */
public class
CapitalDetailsActivity extends ActionbarActivity implements ViewListener<CapitalDetailsBean>, View.OnClickListener, RefreshListView.OnScrollViewListener {

    private ImageView imgbtn_details, img_no_content;
    private TextView tv_capitaldetails_balance, tv_capitaldetails_drawcash;
    private TextView tv_title;
    private TextView tv_capitaldetails, tv_withdraw, tv_hqbinto, tv_jmginvest, tv_recharge, tv_hqbincome,
            tv_jmgincome, tv_lcsincome, tv_hqbredeem, tv_jmgredeem, tv_tyjincome, tv_reward;
    private TextView layout_listtitle;
    private TextView tv_amount_right, tv_amount_left;
    private ImageView id_img;
    private TextView tv_leftitem, tv_miditem, tv_rightitem;
    private RefreshListView lv_capitaldetails;
    private LayoutInflater inflater;

    private LinearLayout layout_list_nocontent;
    private LinearLayout childview;
    private LinearLayout rootlayout_captialdetails, layout_dealdetails;
    //ListView区域
    private FrameLayout layout_captialdetails_listarea;

    private PopupWindow popwindow, datepickerWindow;
    private boolean isPressed = false;

    private TextView btnToTop;
    private boolean gh = true;// 点击标识。防止多次点击

    private Double acountBalance;
    private Double withdrawingAmount;

    private List<TextView> textViewLists;

    private int pageNum = 1;
    private static final int pageSize = 10000;
    private SharedPreferences sp;
    private CapitalDetailsPresenterImpl capitalDetailsPresenter;
    private CapitalDetailsItemPresenterImpl capitalDetailsItemPresenter;
    private List<CapitalDetailsBean.BillDetailListBean> billlist;
    private CapitalDetailsListAdapter adapter;
    private Map<String, String> actionMap;
    private List<String> monthList;
    private List itemList;
    //日历年份
    private int cYear;
    //日历月份
    private int cMonth;
    private int day;

    private Calendar c;
    private Calendar calendar;

    //查询条件
    private String action;
    //查询年份
    private String year;
    //查询月份
    private String month;
    private View menu;
    private DatePicker datepicker;
    private PopupWindow loadingPopupWindow;
    private CapitalDetailsBean capitalDetailsBean;

    private String mode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.layout_capitaldetails, this);
        initViews();
        initActionBar();
        initRefreshList();
    }


    private void initActionBar() {
        setPopwindow();
        actionbar.centerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPressed) {
                    actionbar.centerImgBtn.setRotation(0);

                    popwindow.dismiss();
                    isPressed = true;
                } else {
                    actionbar.centerImgBtn.setRotation(180);
                    popwindow.showAsDropDown(actionbar);
                    isPressed = false;
                }

            }
        });

        actionbar.setCenterTextView("资金明细");
        actionbar.setCenterImgBtn(R.drawable.triangle_down_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPressed) {
                    actionbar.centerImgBtn.setRotation(0);

                    popwindow.dismiss();
                    isPressed = true;
                } else {
                    actionbar.centerImgBtn.setRotation(180);
                    popwindow.showAsDropDown(actionbar);
                    isPressed = false;
                }
            }
        });
        actionbar.setRightImageView(R.drawable.btn_datepicker_normal, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!isPressed) {
//                    actionbar.rightImageView.setImageResource(R.drawable.btn_datepicker_pressed);
                displayDatePicker();
//                }
            }
        });

        if (action != null) {
            actionbar.rightImageView.setVisibility(View.INVISIBLE);
            if (action.equalsIgnoreCase("10")) {
                actionbar.setCenterTextView("充值");
            } else if (action.equalsIgnoreCase("204")) {
                actionbar.setCenterTextView("活期宝赎回");
            } else if (action.equalsIgnoreCase("20,21")) {
                actionbar.setCenterTextView("提现");
            } else if (action.equalsIgnoreCase("50")) {
                actionbar.setCenterTextView("金芒果还本");
            } else if (action.equalsIgnoreCase("51")) {
                actionbar.setCenterTextView("金芒果收益");
            } else if (action.equalsIgnoreCase("80,90")) {
                actionbar.setCenterTextView("金芒果投资");
            } else if (action.equalsIgnoreCase("107")) {
                actionbar.setCenterTextView("体验金收益");
            } else if (action.equalsIgnoreCase("200")) {
                actionbar.setCenterTextView("活期宝收益");
            } else if (action.equalsIgnoreCase("202")) {
                actionbar.setCenterTextView("活期宝转入");
            } else if (action.equalsIgnoreCase("106")) {
                actionbar.setCenterTextView("理财师收益");
            } else if (action.equalsIgnoreCase("1000,1020")) {
                actionbar.setCenterTextView("活动奖励");
            }
        }


    }

    private void initRefreshList() {
        lv_capitaldetails.isLoadable = false;
        lv_capitaldetails.setmOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                if (billlist != null) {
                    clearList();
                    adapter.notifyDataSetChanged();
                }
                String selectedYear = (String) SPUtils.get(CapitalDetailsActivity.this, "CapitalDate", "year", "");
                String selectedMonth = (String) SPUtils.get(CapitalDetailsActivity.this, "CapitalDate", "month", "");
                if (null != adapter) {
                    adapter.notifyDataSetChanged();
                }
                if (action != null && selectedYear != null && selectedMonth != null) {

                    requestNetWorkData(action, selectedYear, selectedMonth);
                } else if (action != null && selectedYear == null && selectedMonth == null) {
                    requestNetWorkData(action, null, null);
                } else if (action == null && selectedYear != null && selectedMonth != null) {
                    requestNetWorkData(null, selectedYear, selectedMonth);
                } else {
                    requestNetWorkData(null, null, null);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        action = getIntent().getStringExtra("code");
        /*网络请求*/
        if (capitalDetailsBean == null) {
            if (action != null) {
                requestNetWorkData(action, year, month);
            } else {
                requestNetWorkData(null, null, null);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtils.clear(CapitalDetailsActivity.this, "CapitalDate");
    }

    private void initViews() {

        sp = this.getSharedPreferences("LOGIN", MODE_PRIVATE);

        capitalDetailsPresenter = new CapitalDetailsPresenterImpl(this);
//        capitalDetailsItemPresenter = new CapitalDetailsItemPresenterImpl(this);
        inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        img_no_content = (ImageView) findViewById(R.id.img_no_content);

        tv_capitaldetails_balance = (TextView) findViewById(R.id.tv_capitaldetails_balance);
        tv_capitaldetails_drawcash = (TextView) findViewById(R.id.tv_capitaldetails_drawcash);
        tv_amount_left = (TextView) findViewById(R.id.tv_amount_left);
        tv_amount_right = (TextView) findViewById(R.id.tv_amount_right);

        layout_captialdetails_listarea = (FrameLayout) findViewById(R.id.layout_captialdetails_listarea);
        childview = (LinearLayout) inflater.inflate(R.layout.layout_profile_property_exlist_child, null);
        layout_listtitle = (TextView) findViewById(R.id.layout_listtitle);
        layout_dealdetails = (LinearLayout) childview.findViewById(R.id.layout_dealdetails);
        layout_list_nocontent = (LinearLayout) findViewById(R.id.layout_list_nocontent);
        rootlayout_captialdetails = (LinearLayout) findViewById(R.id.rootlayout_captialdetails);

        lv_capitaldetails = (RefreshListView) findViewById(R.id.lv_capitaldetails);
        lv_capitaldetails.setOnScrollViewListener(this);
        itemList = new ArrayList<>();
        billlist = new ArrayList<>();
        adapter = new CapitalDetailsListAdapter(this, lv_capitaldetails, layout_listtitle);

        action = getIntent().getStringExtra("code");
        menu = inflater.inflate(R.layout.layout_capitaldetails_exmenu, null, false);

        id_img = (ImageView) menu.findViewById(R.id.id_img);
        id_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popwindow.dismiss();
            }
        });
    }

    /*获取网络数据*/
    private void requestNetWorkData(String action, String year, String month) {
        String mid = String.valueOf(sp.getInt("id", 0));
        Map<String, String> params = new HashMap<>();
        params.put("mid", mid);
        params.put("pageNum", pageNum + "");
        params.put("pageSize", pageSize + "");
        Map<String, String> unnecessaryParams = null;

        if (month != null && year != null) {
            if (action != null) {
                unnecessaryParams = new HashMap<>();
                unnecessaryParams.put("year", year);
                unnecessaryParams.put("month", month);
                unnecessaryParams.put("action", action);
            } else {
                unnecessaryParams = new HashMap<>();
                unnecessaryParams.put("year", year);
                unnecessaryParams.put("month", month);
            }
        } else {
            if (action != null) {
                unnecessaryParams = new HashMap<>();
                unnecessaryParams.put("action", action);
            }
        }

//        if (action != null) {
//            unnecessaryParams = new HashMap<>();
//            unnecessaryParams.put("action", action);
//        }
//        if (year != null || month != null || action != null) {
//            unnecessaryParams = new HashMap<>();
//            unnecessaryParams.put("year", year);
//            unnecessaryParams.put("month", month);
//            unnecessaryParams.put("action", action);
//        }
        if (unnecessaryParams == null) {
            capitalDetailsPresenter.sendRequest(params, null);
        } else {
            capitalDetailsPresenter.sendRequest(params, unnecessaryParams);
        }
    }


    /*月份选择器*/
    private void displayDatePicker() {
        View datePickerView = inflater.inflate(R.layout.layout_capitaldetails_datepicker, null);
        LinearLayout datepicker_content = (LinearLayout) datePickerView.findViewById(R.id.bg_datepicker);
        TranslateAnimation ta = new TranslateAnimation(0, 0, 500, 0);
        ta.setDuration(800);
        datepicker_content.startAnimation(ta);

        datepickerWindow = new PopupWindow(datePickerView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
//        datepickerWindow.setTouchable(true);
        datepickerWindow.setFocusable(true);
        datepickerWindow.setOutsideTouchable(true);
        datepickerWindow.setBackgroundDrawable(new BitmapDrawable());
        datepickerWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        PopwUtils.backgroundAlpha(0.5f, CapitalDetailsActivity.this);
        datepickerOnClick(datePickerView);
        datepickerWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopwUtils.backgroundAlpha(1f, CapitalDetailsActivity.this);
                actionbar.rightImageView.setImageResource(R.drawable.btn_datepicker_normal);
            }
        });
        datepickerWindow.showAtLocation(this.findViewById(R.id.rootlayout_captialdetails), Gravity.BOTTOM, 0, 0);
    }


    private void datepickerOnClick(final View datePickerView) {
        TextView btn_rest = (TextView) datePickerView.findViewById(R.id.btn_rest);
        TextView btn_sure = (TextView) datePickerView.findViewById(R.id.btn_sure);
        datepicker = (DatePicker) datePickerView.findViewById(R.id.datepicker);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
            if (daySpinnerId != 0) {
                View daySpinner = datepicker.findViewById(daySpinnerId);
                if (daySpinner != null) {
                    daySpinner.setVisibility(View.GONE);
                }
            }
        } else {

            ((ViewGroup) ((ViewGroup) datepicker.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
        }

//        ((ViewGroup) datepicker.getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
        String selectedYear = (String) SPUtils.get(CapitalDetailsActivity.this, "CapitalDate", "year", "");
        String selectedMonth = (String) SPUtils.get(CapitalDetailsActivity.this, "CapitalDate", "month", "");
        c = Calendar.getInstance(Locale.CHINA);
        calendar = new GregorianCalendar(2014, 12, 00);
        //设置datepciker的起始时间
        datepicker.setMinDate(calendar.getTime().getTime());
        datepicker.setMaxDate(c.getTime().getTime());
        datepicker.updateDate(cYear, cMonth + 1, 0);
        if (TextUtils.isEmpty(selectedYear) && TextUtils.isEmpty(selectedMonth)) {
            cYear = c.get(Calendar.YEAR);
            cMonth = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

        } else {
            cYear = Integer.parseInt(selectedYear);
            cMonth = Integer.parseInt(selectedMonth) - 1;
            day = 0;
        }


        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*确定按钮*/
                clearList();
//                layout_listtitle.setText((cMonth + 1) + "月");

                Date cDate = new Date();
                String dYear = new SimpleDateFormat("yyyy").format(cDate);
                String dMonth = new SimpleDateFormat("MM").format(cDate);
                if (String.valueOf(cYear).equalsIgnoreCase(dYear)) {
                    if (String.valueOf(cMonth).equalsIgnoreCase(dMonth)) {
                        layout_listtitle.setText("本月");

                    } else {
                        layout_listtitle.setText((cMonth + 1) + "月");
                    }
                } else {
                    layout_listtitle.setText(year + "年" + month + "月");
                }
                SPUtils.put(CapitalDetailsActivity.this, "CapitalDate", "year", cYear + "");
                SPUtils.put(CapitalDetailsActivity.this, "CapitalDate", "month", (cMonth + 1) + "");

                requestNetWorkData(action, cYear + "", (cMonth + 1) + "");
                datepickerWindow.dismiss();

                actionbar.rightImageView.setImageResource(R.drawable.btn_datepicker_pressed);
            }
        });

        btn_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*重置按钮*/
                Date currentDate = new Date();
                String date = new SimpleDateFormat("yyyy-MM").format(currentDate);
                String resetYear = date.substring(0, 4);
                String resetMonth = date.substring(5, 7);
                datepicker.init(Integer.parseInt(resetYear), Integer.parseInt(resetMonth), 0, null);

                SPUtils.clear(CapitalDetailsActivity.this, "CapitalDate");
                clearList();
                requestNetWorkData(action, null, null);
                datepickerWindow.dismiss();
                actionbar.rightImageView.setImageResource(R.drawable.btn_datepicker_normal);
            }
        });
        initDatePickerTime(datepicker);


    }

    private void initDatePickerTime(DatePicker datepicker) {
        datepicker.init(cYear, cMonth + 1, 0, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                // 显示当前日期、时间
                CapitalDetailsActivity.this.cYear = year;
                CapitalDetailsActivity.this.cMonth = monthOfYear;
                CapitalDetailsActivity.this.day = dayOfMonth;


            }


        });

    }


    /*下拉菜单*/
    private void setPopwindow() {

        popwindow = new PopupWindow(menu, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popwindowFindViewById(menu);
        btnOnClick(menu);
        popwindow.setTouchable(true);
        popwindow.setFocusable(true);
        popwindow.setOutsideTouchable(true);

        popwindow.setBackgroundDrawable(new ColorDrawable(55000000));
        popwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                actionbar.centerImgBtn.setRotation(0);
            }
        });
    }

    /*初始化下拉菜单中的按钮控件*/
    private void popwindowFindViewById(View menu) {
        textViewLists = new ArrayList<>();

        tv_capitaldetails = (TextView) menu.findViewById(R.id.tv_capitaldetails);
        tv_withdraw = (TextView) menu.findViewById(R.id.tv_withdraw);
        tv_hqbinto = (TextView) menu.findViewById(R.id.tv_hqbinto);
        tv_jmginvest = (TextView) menu.findViewById(R.id.tv_jmginvest);
        tv_recharge = (TextView) menu.findViewById(R.id.tv_recharge);
        tv_hqbincome = (TextView) menu.findViewById(R.id.tv_hqbincome);
        tv_jmgincome = (TextView) menu.findViewById(R.id.tv_jmgincome);
        tv_lcsincome = (TextView) menu.findViewById(R.id.tv_lcsincome);
        tv_hqbredeem = (TextView) menu.findViewById(R.id.tv_hqbredeem);
        tv_jmgredeem = (TextView) menu.findViewById(R.id.tv_jmgredeem);
        tv_tyjincome = (TextView) menu.findViewById(R.id.tv_tyjincome);
        tv_reward = (TextView) menu.findViewById(R.id.tv_reward);

        textViewLists.add(tv_capitaldetails);
        textViewLists.add(tv_withdraw);
        textViewLists.add(tv_hqbinto);
        textViewLists.add(tv_jmginvest);
        textViewLists.add(tv_recharge);
        textViewLists.add(tv_hqbincome);
        textViewLists.add(tv_jmgincome);
        textViewLists.add(tv_lcsincome);
        textViewLists.add(tv_hqbredeem);
        textViewLists.add(tv_jmgredeem);
        textViewLists.add(tv_tyjincome);
        textViewLists.add(tv_reward);

    }

    /*清除列表数据*/
    public void clearList() {
        if (billlist != null) {

            billlist.clear();
        }
        if (monthList != null) {

            monthList.clear();
        }
        if (itemList != null) {

            itemList.clear();
        }
        if (lv_capitaldetails.getAdapter() != null) {
            adapter.notifyDataSetChanged();
        }
    }


    private void btnOnClick(View menu) {

        tv_capitaldetails.setOnClickListener(this);
        tv_withdraw.setOnClickListener(this);
        tv_hqbinto.setOnClickListener(this);
        tv_jmginvest.setOnClickListener(this);
        tv_recharge.setOnClickListener(this);
        tv_hqbincome.setOnClickListener(this);
        tv_jmgincome.setOnClickListener(this);
        tv_lcsincome.setOnClickListener(this);
        tv_hqbredeem.setOnClickListener(this);
        tv_jmgredeem.setOnClickListener(this);
        tv_tyjincome.setOnClickListener(this);
        tv_reward.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        TextView currentTextView = (TextView) v;
        for (TextView textView : textViewLists) {
            if (textView == v) {
                textView.setTextColor(Color.parseColor("#feaa00"));
                textView.setBackgroundResource(R.drawable.shape_capitaildetails_title);
            } else {
                textView.setTextColor(Color.GRAY);
                textView.setBackgroundResource(R.drawable.shape_capitaildetails_btn);

            }
        }

        clearList();
        String selectedYear = (String) SPUtils.get(CapitalDetailsActivity.this, "CapitalDate", "year", "");
        String selectedMonth = (String) SPUtils.get(CapitalDetailsActivity.this, "CapitalDate", "month", "");
//        cYear = selectedYear;
//        cMonth = selectedMonth;

        switch (v.getId()) {
            case R.id.tv_capitaldetails:
                actionbar.centerTextView.setText("资金明细");
                action = null;
                requestNetWorkData(action, selectedYear, selectedMonth);
                break;
            case R.id.tv_withdraw:
                actionbar.centerTextView.setText("提现");
                action = getAciton("提现");
//                action = "20,21";
                requestNetWorkData(action, selectedYear, selectedMonth);
                break;
            case R.id.tv_hqbinto:
                actionbar.centerTextView.setText("活期宝转入");
                action = getAciton("活期宝转入");
//                action = "202";
                requestNetWorkData(action, selectedYear, selectedMonth);
                break;
            case R.id.tv_jmginvest:
                actionbar.centerTextView.setText("金芒果投资");
                action = getAciton("金芒果投资");
//                action = "80,90";
                requestNetWorkData(action, selectedYear, selectedMonth);
                break;
            case R.id.tv_recharge:
                actionbar.centerTextView.setText("充值");
                action = getAciton("充值");
//                action = "10";
                requestNetWorkData(action, selectedYear, selectedMonth);
                break;
            case R.id.tv_hqbincome:
                actionbar.centerTextView.setText("活期宝收益");
                action = getAciton("活期宝收益");
//                action = "200";
                requestNetWorkData(action, selectedYear, selectedMonth);
                break;
            case R.id.tv_jmgincome:
                actionbar.centerTextView.setText("金芒果收益");
                action = getAciton("金芒果收益");
//                action = "51";
                requestNetWorkData(action, selectedYear, selectedMonth);
                break;
            case R.id.tv_hqbredeem:
                action = getAciton("活期宝赎回");
//                action = "204";
                requestNetWorkData(action, selectedYear, selectedMonth);

                actionbar.centerTextView.setText("活期宝赎回");
                break;
            case R.id.tv_jmgredeem:
                actionbar.centerTextView.setText("金芒果还本");
                action = getAciton("金芒果还本");
//                action = "50";
                requestNetWorkData(action, selectedYear, selectedMonth);
                break;
            case R.id.tv_tyjincome:
                actionbar.centerTextView.setText("体验金收益");
                action = getAciton("体验金收益");
//                action = "107";
                requestNetWorkData(action, selectedYear, selectedMonth);
                break;
            case R.id.tv_lcsincome:
                actionbar.centerTextView.setText("理财师收益");
                action = getAciton("理财师收益");
//                action = "106";
                requestNetWorkData(action, selectedYear, selectedMonth);
                break;
            case R.id.tv_reward:
                actionbar.centerTextView.setText("活动奖励");
                action = getAciton("活动奖励");
//                action = "1000,1020";
                requestNetWorkData(action, selectedYear, selectedMonth);
                break;
        }
        clearList();
        popwindow.dismiss();
        actionbar.centerImgBtn.setRotation(0);
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
        lv_capitaldetails.setOnRefreshComplete();

    }

    @Override
    public void showError(OnPresenterListener listener, CapitalDetailsBean capitalDetailsBean) {
        dismissLoadingDialog();
        CommonToastUtils.showToast(this, capitalDetailsBean.getMsg());
    }

    @Override
    public void responseData(OnPresenterListener listener, CapitalDetailsBean
            capitalDetailsBean) {
        layout_listtitle.setVisibility(View.VISIBLE);
        this.capitalDetailsBean = capitalDetailsBean;
        /*查询条件*/

        /*设置余额、提现中金额*/
        withdrawingAmount = capitalDetailsBean.getWithdrawingAmount();
        acountBalance = capitalDetailsBean.getAcountBalance();
        setTextData();

        if (listener instanceof CapitalDetailsPresenterImpl) {
        /*资金明细表*/
            List<CapitalDetailsBean.BillDetailListBean> billDetailList = capitalDetailsBean.getBillDetailList();

            if (billDetailList == null) {
//                lv_capitaldetails.setOnLoadMoreComplete();
            } else {
                billlist.addAll(billDetailList);
                changeListStructure(billlist);
                if (itemList.size() == 0) {
                    layout_list_nocontent.setVisibility(View.VISIBLE);
                    layout_listtitle.setVisibility(View.GONE);
                    lv_capitaldetails.setOnRefreshComplete();
                } else {
                    layout_list_nocontent.setVisibility(View.GONE);
                    layout_listtitle.setVisibility(View.VISIBLE);
                    adapter.setListData(itemList);
                    if (lv_capitaldetails.getAdapter() == null) {
                        lv_capitaldetails.setAdapter(adapter);
                    }
                    if (pageNum == 1) {
                        lv_capitaldetails.setOnRefreshComplete();
                    }
                }
                adapter.notifyDataSetChanged();
//                lv_capitaldetails.onLoadMoreComplete();
            }
        }
    }

    private String getAciton(String text) {
        actionMap = capitalDetailsBean.getActionMap();
        Set set = actionMap.entrySet();
        Iterator iterator = set.iterator();
        String key = "";
        while (iterator.hasNext()) {
            Map.Entry<String, String> enter = (Map.Entry<String, String>) iterator.next();
            if (enter.getValue().equals(text)) {
                key = enter.getKey();
            }
        }

        return key;
    }

    private void setTextData() {
        tv_capitaldetails_balance.setText(new DecimalFormat("###,###,##0.00").format(acountBalance));
        tv_capitaldetails_drawcash.setText(new DecimalFormat("###,###,##0.00").format(withdrawingAmount));

    }

    /*重组数据结构*/

    public void changeListStructure(List<CapitalDetailsBean.BillDetailListBean> list) {
        monthList = new ArrayList<>();
        for (CapitalDetailsBean.BillDetailListBean map : list) {
            String date = longtoDate(map.getCtime());
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


    public String longtoDate(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
        sf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String sMonth = sf.format(time);
        return sMonth;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (adapter != null && adapter.billlist != null) {
            adapter.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }
}
