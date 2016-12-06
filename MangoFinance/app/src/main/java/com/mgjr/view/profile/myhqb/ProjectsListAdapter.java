package com.mgjr.view.profile.myhqb;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.model.bean.MyHqbRunningProjectBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.mgjr.R.id.tv_isredeemed;

public class ProjectsListAdapter extends BaseAdapter {

    private Context context;
    private List<MyHqbRunningProjectBean.TenderListBean> tenderList;
    private boolean visiable, isChecked1, isSelected;
    private LinearLayout layout_checkbox;
    private ListView listView;
    private Double remainingTenderAmount;  //持有金额
    private Double rate;   //当前年化收益
    private String hqbTitle;
    private Double incomeAmount;  //已获收益
    private int redeemingCount;  //赎回中数量
    private String tenderId;
    private String amount;
    private List<String> tenderIds;
    private boolean[] checks; //用于保存checkBox的选择状态
    private GetTenderIds getTenderIds;
    private String str;
    private List<Integer> checkedBoxes;
    private List<String> boxList;

    public boolean isChecked(boolean isChecked1) {
        this.isChecked1 = isChecked1;
        return isChecked1;
    }

    public boolean isVisiable(boolean isVisiable) {
        this.visiable = isVisiable;
        return visiable;
    }

    public void setTenderIds(GetTenderIds getTenderIds) {
        this.getTenderIds = getTenderIds;
    }

    public ProjectsListAdapter(Context context) {
        this.context = context;
        boxList = new ArrayList<>();
        tenderIds = new ArrayList<>();

        checkedBoxes = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return tenderList.size();
    }

    @Override
    public Object getItem(int position) {
        return tenderList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.itemlayout_hqb_runningproject_list, null);
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.img_runningproject_icon);
            holder.tv_balance = (TextView) convertView.findViewById(R.id.tv_runningproject_balance);
            holder.tv_rate = (TextView) convertView.findViewById(R.id.tv_runningproject_rate);
            holder.tv_project = (TextView) convertView.findViewById(R.id.tv_runningproject_project);
            holder.tv_action = (TextView) convertView.findViewById(R.id.tv_runningproject_action);
            holder.tv_isredeemed = (TextView) convertView.findViewById(tv_isredeemed);
            holder.tv_incomeAmount = (TextView) convertView.findViewById(R.id.tv_runningproject_incomeAmount);
            holder.itemcheckbox = (CheckBox) convertView.findViewById(R.id.itemcheckbox);
//            holder.itemcheckbox.setChecked(false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
//            holder.itemcheckbox.setChecked(false);

        }


        final int pos = position; //pos必须声明为final
        setData(position, holder);
        setListItem();
        mutiRedeem(convertView, visiable, position, pos);

        return convertView;
    }


    private void mutiRedeem(View convertView, boolean visiable, int i, final int position) {

        layout_checkbox = (LinearLayout) convertView.findViewById(R.id.layout_checkbox);
        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.itemcheckbox);
        checkBox.setTag(i);


//        if (isChecked1 == true) {
//
//            getTenderIds.getMutilRedeemTenderIds();
//            checkBox.setChecked(true);
//        } else {
//            checkBox.setChecked(false);
//            getTenderIds.cancleMutilCheck();
//        }
        if (visiable == true) {
            layout_checkbox.setVisibility(View.VISIBLE);
        } else {
            layout_checkbox.setVisibility(View.GONE);
        }

        if (boxList.contains(String.valueOf(position))) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        checkBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        CheckBox checkBox = (CheckBox) buttonView;
                        int position = (int) checkBox.getTag();

//                        boxList.add(String.valueOf(position));
//                        getTenderIds.singleIsChecked(position, checkBox);
//                        checkedBoxes.add(position);
//
//                        if (isChecked) {
//                            if (boxList.size() < 3 && !boxList.contains(String.valueOf(position))) {
                        if (isChecked) {
                            if (boxList.size() < 9 && !boxList.contains(String.valueOf(position))) {
                                boxList.add(String.valueOf(position));
                                getTenderIds.singleIsChecked(position, checkBox);
                            }

                            if (!boxList.contains(String.valueOf(position))) {
                                checkBox.setChecked(false);
                            }
                        } else {
                            if (boxList.contains(String.valueOf(position))) {
                                boxList.remove(String.valueOf(position));
                                getTenderIds.singleIsChecked(position, checkBox);
                            }
                        }


                    }


                }
        );
    }

    private void setData(int position, ViewHolder holder) {
        //持有金额
        remainingTenderAmount = tenderList.get(position).getRemainingTenderAmount();
        if (remainingTenderAmount != null) {
            amount = new DecimalFormat("###,###,##0.00").format(remainingTenderAmount);
            holder.tv_balance.setText(amount);
        } else {
            holder.tv_balance.setText("0.0");
        }
        //当前年化收益
        rate = tenderList.get(position).getRate();
        if (rate != null) {
            holder.tv_rate.setText(rate + "%");
        } else {
            holder.tv_rate.setText("0.0" + "%");
        }
        //项目名
        hqbTitle = tenderList.get(position).getHqbTitle();
        if (hqbTitle != null) {
            holder.tv_project.setText(hqbTitle);
        }
        //已获收益
        incomeAmount = tenderList.get(position).getIncomeAmount();
        if (incomeAmount != null) {
            holder.tv_incomeAmount.setText(new DecimalFormat("###,###,##0.00").format(incomeAmount));
        } else {
            holder.tv_incomeAmount.setText("0.0" + "");
        }
        //赎回中数量
        redeemingCount = tenderList.get(position).getRedeemingCount();
        if (redeemingCount > 0) {
            holder.tv_isredeemed.setVisibility(View.VISIBLE);
            holder.iv_icon.setVisibility(View.VISIBLE);
        } else {
            holder.tv_isredeemed.setVisibility(View.GONE);
            holder.iv_icon.setVisibility(View.GONE);
        }
    }

    private void setListItem() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (visiable == true) {
//                    listView.setEnabled(false);
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.itemcheckbox);
                    getTenderIds.singleIsChecked(position - 1, checkBox);
                } else {
                    tenderId = tenderList.get(position - 1).getId() + "";
                    MyActivityManager.getInstance().startNextActivity(MyHqbProjectDetailsActivity.class, tenderId,"str","running");
                }
            }
        });

    }


    public void setData(List<MyHqbRunningProjectBean.TenderListBean> tenderList, ListView lv_hqb_runningproject) {
        this.tenderList = tenderList;
        this.listView = lv_hqb_runningproject;
        checks = new boolean[tenderList.size()];
    }

    public void setData(List<MyHqbRunningProjectBean.TenderListBean> tenderList) {
        this.tenderList = tenderList;
    }

    public void setListView(ListView lv_hqb_runningproject) {
        this.listView = lv_hqb_runningproject;
    }

    public void clearCheckedStatus(String str) {
        this.str = str;
        boxList.clear();
        if (tenderList != null) {
            if (str.equalsIgnoreCase("clear")) {
                for (int i = 0; i < tenderList.size(); i++) {

                    isSelected = tenderList.get(i).isChecked;
                    isSelected = false;
                }
            } else if (str.equalsIgnoreCase("cancle")) {

                for (int i = 0; i < tenderList.size(); i++) {

                    isSelected = tenderList.get(i).isChecked;
                    isSelected = true;
                }

            }
        }
    }

    class ViewHolder {
        ImageView iv_icon;
        TextView tv_balance, tv_rate, tv_project, tv_action, tv_isredeemed, tv_incomeAmount;
        CheckBox itemcheckbox;
//        public ViewHolder(View view) {
//            iv_icon = (ImageView) view.findViewById(R.id.img_runningproject_icon);
//            tv_balance = (TextView) view.findViewById(R.id.tv_runningproject_balance);
//            tv_rate = (TextView) view.findViewById(R.id.tv_runningproject_rate);
//            tv_project = (TextView) view.findViewById(R.id.tv_runningproject_project);
//            tv_action = (TextView) view.findViewById(R.id.tv_runningproject_action);
//            tv_isredeemed = (TextView) view.findViewById(R.id.tv_isredeemed);
//            tv_incomeAmount = (TextView) view.findViewById(R.id.tv_runningproject_incomeAmount);
//            view.setTag(this);
//        }
    }

    public interface GetTenderIds {
        void getMutilRedeemTenderIds();

        void cancleMutilCheck();

        void singleIsChecked(int position, CheckBox checkBox);
    }

}
