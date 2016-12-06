package com.mgjr.view.profile.myhqb;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.SPUtils;
import com.mgjr.model.bean.HqbTransactionBean;
import com.mgjr.presenter.impl.HqbTransationPresenterImpl;
import com.mgjr.presenter.impl.JmgTransationPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.RefreshListView;
import com.mgjr.view.listeners.ViewListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/26.
 */

public class MyHqbTransactionDetailActivity extends ActionbarActivity implements ViewListener<HqbTransactionBean>, View.OnClickListener, RefreshListView.OnScrollViewListener {

    private RefreshListView lv_hqb_transationdetail;
    private TextView tv_hqb_balance;
    private TextView tv_hqb_ljamount;
    private TextView layout_listtitle;

    private TextView tv_amount_left, tv_amount_right;

    private Double totalTender, totalTenderAll;

    private TextView tv_leftitem, tv_miditem, tv_rightitem;
    private ImageView img_no_content;

    private LinearLayout layout_list_nocontent;
    private HqbTransationAdapter adapter;
    private View menu;

    private PopupWindow popwindow;
    private boolean isPressed = false;
    private LayoutInflater inflater;

    private String currentType;

    private String mode, type;
    private List<String> monthList;
    private List itemList;
    private HqbTransationPresenterImpl hqbTransationPresenter;
    private JmgTransationPresenterImpl jmgTransationPresenter;
    private List<HqbTransactionBean.TransactionDetailListBean> transactionDetailList;
    private List<TextView> textViewLists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_layout_hqb_transactiondetail, this);
        initViews();
        initActionbar();
        setRefreshList();
    }

    private void setRefreshList() {
        lv_hqb_transationdetail.isLoadable = false;
        lv_hqb_transationdetail.setmOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mode.equalsIgnoreCase("hqb")) {
                    if (transactionDetailList != null) {
                        clearList();
                        adapter.notifyDataSetChanged();
                    }

                    requestNetWorkDataHqb(currentType, null, null);

                } else if (mode.equalsIgnoreCase("jmg")) {
                    if (transactionDetailList != null) {
                        clearList();
                        adapter.notifyDataSetChanged();
                    }

                    requestNetWorkDataJmg(currentType, null, null);
                }
            }
        });
    }

    private void checkIntentType() {
        if (mode.equalsIgnoreCase("hqb")) {
            if (type != null) {
                currentType = type;
                requestNetWorkDataHqb(currentType, null, null);

            } else {
                requestNetWorkDataHqb(null, null, null);
            }
        } else if (mode.equalsIgnoreCase("jmg")) {
            if (type != null) {
                currentType = type;
                requestNetWorkDataJmg(currentType, null, null);

            } else {
                requestNetWorkDataJmg(null, null, null);
            }

        }
    }

    private void requestNetWorkDataJmg(String type, String year, String month) {
        String mid = String.valueOf(SPUtils.get(this, "LOGIN", "id", 0));
        Map<String, String> params = new HashMap<>();
        params.put("mid", mid);
        Map<String, String> unnecessaryParams = null;
        if (type != null) {

            unnecessaryParams = new HashMap<>();
            if (type.equalsIgnoreCase("还本")) {
                unnecessaryParams.put("type", "还本");
            } else if (type.equalsIgnoreCase("收益")) {
                unnecessaryParams.put("type", "收益");
            } else if (type.equalsIgnoreCase("投资")) {
                unnecessaryParams.put("type", "投资");
            }
        }
        if (year != null && month != null) {
            unnecessaryParams = new HashMap<>();
            unnecessaryParams.put("year", year);
            unnecessaryParams.put("month", month);
        }
        jmgTransationPresenter.sendRequest(params, unnecessaryParams);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkIntentType();
    }

    private void initViews() {
        hqbTransationPresenter = new HqbTransationPresenterImpl(this);
        jmgTransationPresenter = new JmgTransationPresenterImpl(this);

        lv_hqb_transationdetail = (RefreshListView) findViewById(R.id.lv_hqb_transationdetail);
        lv_hqb_transationdetail.setOnScrollViewListener(this);
        tv_hqb_balance = (TextView) findViewById(R.id.tv_hqb_balance);
        tv_hqb_ljamount = (TextView) findViewById(R.id.tv_hqb_ljamount);
        layout_listtitle = (TextView) findViewById(R.id.layout_listtitle);
        tv_amount_right = (TextView) findViewById(R.id.tv_amount_right);
        tv_amount_left = (TextView) findViewById(R.id.tv_amount_left);

        layout_list_nocontent = (LinearLayout) findViewById(R.id.layout_list_nocontent);
        img_no_content = (ImageView) findViewById(R.id.img_no_content);
        adapter = new HqbTransationAdapter(this, lv_hqb_transationdetail, layout_listtitle);
        inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menu = inflater.inflate(R.layout.layout_capitaldetails_exmenu_hqb, null, false);
        tv_leftitem = (TextView) menu.findViewById(R.id.tv_leftitem);
        tv_miditem = (TextView) menu.findViewById(R.id.tv_miditem);
        tv_rightitem = (TextView) menu.findViewById(R.id.tv_rightitem);
        mode = getIntent().getStringExtra("type");
        type = getIntent().getStringExtra("code");
        itemList = new ArrayList();
        monthList = new ArrayList<>();
        textViewLists = new ArrayList<>();
        textViewLists.add(tv_leftitem);
        textViewLists.add(tv_miditem);
        textViewLists.add(tv_rightitem);
    }

    private void initActionbar() {

        if (mode.equalsIgnoreCase("hqb")) {
            if (type.equalsIgnoreCase("转入")) {
                actionbar.setCenterTextView("活期宝转入");
                tv_leftitem.setTextColor(Color.parseColor("#feaa00"));
                tv_leftitem.setBackgroundResource(R.drawable.shape_capitaildetails_title);
            } else if (type.equalsIgnoreCase("赎回")) {
                actionbar.setCenterTextView("活期宝赎回");
                tv_miditem.setTextColor(Color.parseColor("#feaa00"));
                tv_miditem.setBackgroundResource(R.drawable.shape_capitaildetails_title);
            } else if (type.equalsIgnoreCase("收益")) {
                actionbar.setCenterTextView("活期宝收益");
                tv_rightitem.setTextColor(Color.parseColor("#feaa00"));
                tv_rightitem.setBackgroundResource(R.drawable.shape_capitaildetails_title);
            }
        } else if (mode.equalsIgnoreCase("jmg")) {
            if (type.equalsIgnoreCase("投资")) {
                actionbar.setCenterTextView("金芒果投资");
                tv_leftitem.setTextColor(Color.parseColor("#feaa00"));
                tv_leftitem.setBackgroundResource(R.drawable.shape_capitaildetails_title);
            } else if (type.equalsIgnoreCase("还本")) {
                actionbar.setCenterTextView("金芒果还本");
                tv_miditem.setTextColor(Color.parseColor("#feaa00"));
                tv_miditem.setBackgroundResource(R.drawable.shape_capitaildetails_title);
            } else if (type.equalsIgnoreCase("收益")) {
                actionbar.setCenterTextView("金芒果收益");
                tv_rightitem.setTextColor(Color.parseColor("#feaa00"));
                tv_rightitem.setBackgroundResource(R.drawable.shape_capitaildetails_title);
            }
        }
        actionbar.centerImgBtn.setImageResource(R.drawable.triangle_down_btn);
        actionbar.centerImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPressed) {
                    actionbar.centerImgBtn.setRotation(180);
                    setPopwindow();
                    isPressed = false;
                } else {
                    actionbar.centerImgBtn.setRotation(0);
                    popwindow.dismiss();
                    isPressed = true;
                }
//                isPressed = !isPressed;
            }
        });
        actionbar.setCenterLayoutOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPressed) {
                    actionbar.centerImgBtn.setRotation(180);
                    setPopwindow();
                } else {
                    actionbar.centerImgBtn.setRotation(0);
                    popwindow.dismiss();
                }
                isPressed = !isPressed;
            }
        });
    }

    private void setPopwindow() {
        popwindow = new PopupWindow(menu, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        setItems();
        popwindow.setTouchable(true);
        popwindow.setFocusable(false);
        popwindow.setOutsideTouchable(true);
        popwindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popwindow.setBackgroundDrawable(new ColorDrawable(55000000));
        popwindow.showAsDropDown(actionbar);
    }

    private void setItems() {
        switch (mode) {
            case "jmg":
                setItemTextJmg();
                itemClick();
                break;
            case "hqb":
                itemClick();
                setItemTextHqb();
                break;
        }

    }

    private void itemClick() {
        tv_leftitem.setOnClickListener(this);
        tv_miditem.setOnClickListener(this);
        tv_rightitem.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        isPressed = true;
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

        switch (v.getId()) {
            case R.id.tv_leftitem:
                if (mode.equalsIgnoreCase("jmg")) {
                    actionbar.centerTextView.setText("金芒果投资");
                    currentType = "投资";
                    requestNetWorkDataJmg(currentType, null, null);
                } else if (mode.equalsIgnoreCase("hqb")) {
                    actionbar.centerTextView.setText("活期宝转入");
                    currentType = "转入";
                    requestNetWorkDataHqb(currentType, null, null);
                }
                break;
            case R.id.tv_miditem:
                if (mode.equalsIgnoreCase("jmg")) {
                    actionbar.centerTextView.setText("金芒果还本");
                    currentType = "还本";
                    requestNetWorkDataJmg(currentType, null, null);
                } else if (mode.equalsIgnoreCase("hqb")) {
                    actionbar.centerTextView.setText("活期宝赎回");
                    currentType = "赎回";
                    requestNetWorkDataHqb(currentType, null, null);

                }
                break;
            case R.id.tv_rightitem:
                if (mode.equalsIgnoreCase("jmg")) {
                    actionbar.centerTextView.setText("金芒果收益");
                    currentType = "收益";
                    requestNetWorkDataJmg(currentType, null, null);
                } else if (mode.equalsIgnoreCase("hqb")) {
                    actionbar.centerTextView.setText("活期宝收益");
                    currentType = "收益";
                    requestNetWorkDataHqb(currentType, null, null);
                }
                break;
        }

        actionbar.centerImgBtn.setRotation(0);
        popwindow.dismiss();
    }


    private void requestNetWorkDataHqb(String type, String year, String month) {
        String mid = String.valueOf(SPUtils.get(this, "LOGIN", "id", 0));
        Map<String, String> params = new HashMap<>();
        params.put("mid", mid);
        String str = getIntent().getStringExtra("code");
        Map<String, String> unnecessaryParams = null;
        if (type != null) {

            unnecessaryParams = new HashMap<>();
            if (type.equalsIgnoreCase("转入")) {
                unnecessaryParams.put("type", "转入");
            } else if (type.equalsIgnoreCase("赎回")) {
                unnecessaryParams.put("type", "赎回");
            } else if (type.equalsIgnoreCase("收益")) {
                unnecessaryParams.put("type", "收益");
            }
        }
        if (year != null && month != null) {
            unnecessaryParams = new HashMap<>();
            unnecessaryParams.put("year", year);
            unnecessaryParams.put("month", month);
        }
        hqbTransationPresenter.sendRequest(params, unnecessaryParams);

    }

    private void clearList() {
        transactionDetailList.clear();
        itemList.clear();
        monthList.clear();
    }

    private void setItemTextJmg() {
        tv_leftitem.setText("投资");
        tv_miditem.setText("还本");
        tv_rightitem.setText("收益");
    }

    private void setItemTextHqb() {
        tv_leftitem.setText("转入");
        tv_miditem.setText("赎回");
        tv_rightitem.setText("收益");
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
    public void showError(OnPresenterListener listener, HqbTransactionBean hqbTransactionBean) {
        dismissLoadingDialog();
        CommonToastUtils.showToast(this, hqbTransactionBean.getMsg());
    }

    @Override
    public void responseData(OnPresenterListener listener, HqbTransactionBean hqbTransactionBean) {
        layout_listtitle.setVisibility(View.VISIBLE);
        itemList.clear();


        if (listener instanceof HqbTransationPresenterImpl) {

            transactionDetailList = hqbTransactionBean.getTransactionDetailList();
            lv_hqb_transationdetail.setOnRefreshComplete();
            totalTender = hqbTransactionBean.getTotalTender();
            totalTenderAll = hqbTransactionBean.getTotalTenderAll();
            changeListStructure(transactionDetailList);
            if (itemList.size() == 0) {
                layout_list_nocontent.setVisibility(View.VISIBLE);
                layout_listtitle.setVisibility(View.GONE);
                layout_list_nocontent.setClickable(false);
                lv_hqb_transationdetail.setVisibility(View.GONE);
            } else {
                layout_list_nocontent.setVisibility(View.GONE);
                layout_listtitle.setVisibility(View.VISIBLE);
                lv_hqb_transationdetail.setVisibility(View.VISIBLE);
                adapter.setListData(itemList);
                lv_hqb_transationdetail.setAdapter(adapter);

            }
            setTextDataHqb();
        } else if (listener instanceof JmgTransationPresenterImpl) {
            lv_hqb_transationdetail.setOnRefreshComplete();
            double dsbjLoan = hqbTransactionBean.getDsbjLoan();
            double totalTenderAmount = hqbTransactionBean.getTotalTenderAmount();
            transactionDetailList = hqbTransactionBean.getTransactionDetailList();
            changeListStructure(transactionDetailList);
            if (itemList.size() == 0) {
                layout_list_nocontent.setVisibility(View.VISIBLE);
                layout_listtitle.setVisibility(View.GONE);
                lv_hqb_transationdetail.setVisibility(View.GONE);
            } else {
                layout_list_nocontent.setVisibility(View.GONE);
                layout_listtitle.setVisibility(View.VISIBLE);
                adapter.setListData(itemList);
                lv_hqb_transationdetail.setAdapter(adapter);
                lv_hqb_transationdetail.setVisibility(View.VISIBLE);
            }

            setTextDataJmg(dsbjLoan, totalTenderAmount);
        }


    }


    private void changeListStructure(List<HqbTransactionBean.TransactionDetailListBean> list) {

        monthList = new ArrayList<>();
        for (HqbTransactionBean.TransactionDetailListBean map : list) {
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

    private void setTextDataHqb() {
        tv_amount_left.setText("活期余额(元)");
        tv_amount_right.setText("累计转入金额(元)");
        if (totalTender == 0) {
            tv_hqb_balance.setText("0.00");
        } else {
            tv_hqb_balance.setText(new DecimalFormat("###,###,##0.00").format(totalTender));
        }
        if (totalTenderAll == 0) {
            tv_hqb_ljamount.setText("0.00");
        } else {
            tv_hqb_ljamount.setText(new DecimalFormat("###,###,##0.00").format(totalTenderAll));
        }
    }

    private void setTextDataJmg(double dsbjLoan, double totalTenderAmount) {
        tv_amount_left.setText("投资金额(元)");
        tv_amount_right.setText("累计投资金额(元)");
        if (dsbjLoan == 0) {
            tv_hqb_balance.setText("0.00");
        } else {
            tv_hqb_balance.setText(new DecimalFormat("###,###,##0.00").format(dsbjLoan));
        }
        if (totalTenderAmount == 0) {
            tv_hqb_ljamount.setText("0.00");
        } else {
            tv_hqb_ljamount.setText(new DecimalFormat("###,###,##0.00").format(totalTenderAmount));
        }

    }

    public String longtoDate(long time) {
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
