package com.mgjr.view.profile.accountsetting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.LogUtil;
import com.mgjr.R;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.PopwUtils;
import com.mgjr.model.bean.AuthCardInfo;
import com.mgjr.presenter.impl.SelectBankPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.view.listeners.ViewListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ProfileSelectBankActivity extends ActionbarActivity implements ViewListener<com.mgjr.model.bean.bankBean> {

    @InjectView(R.id.lv_select_bank)
    ListView lvSelectBank;

    private SelectBankPresenterImpl selectBankPresenterImpl;
    private com.mgjr.model.bean.bankBean bankBean;
    private SelectBankListViewAdapter selectBankListViewAdapter;
    private int[] bankLogos = {R.drawable.logo_abc_0, R.drawable.logo_icbc_1, R.drawable.logo_cmbc_2, R.drawable.logo_boc_3,
            R.drawable.logo_cbc_4, R.drawable.logo_ceb_5, R.drawable.logo_hxb_6, R.drawable.logo_cncb_7, R.drawable.logo_cib_8,
            R.drawable.logo_pab_9, R.drawable.logo_spdb_10, R.drawable.logo_psbc_11, R.drawable.logo_gdb_12, R.drawable.logo_bcm_13, R.drawable.logo_cmsb_14};
    private PopupWindow loadingPopW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_profile_limit_state, this);
        ButterKnife.inject(this);
        actionbar.setCenterTextView("选择银行");
        selectBankPresenterImpl = new SelectBankPresenterImpl(this);
        initSelectBankListView();

    }

    private void initSelectBankListView() {
        //添加头布局
        addHeaderViews();
        lvSelectBank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("bankBean", (Serializable) bankBean.getBankList().get(position - 1));
                //设置返回数据
                ProfileSelectBankActivity.this.setResult(0, intent);
                //关闭Activity
//                popActivity();
                MyActivityManager.getInstance().popCurrentActivity();
            }
        });
    }


    private void addHeaderViews() {
        FrameLayout inflate = (FrameLayout) View.inflate(this, R.layout.header_view_select_bank_listview, null);
        lvSelectBank.addHeaderView(inflate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        networkRequest();
    }

    private void networkRequest() {
        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        selectBankPresenterImpl.sendRequest(necessaryParams, null);
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
    public void showError(OnPresenterListener listener, com.mgjr.model.bean.bankBean bankBean) {
        dismissLoadingDialog();
        CommonToastUtils.showToast(this, bankBean.getMsg());
    }

    @Override
    public void responseData(OnPresenterListener listener, com.mgjr.model.bean.bankBean bankBean) {
        this.bankBean = bankBean;
        /*if (bankBean.getBankList() != null) {
            for (int i = 0; i < bankBean.getBankList().size(); i++) {
                bankBean.getBankList().get(i).setLogoResId(bankLogos[i]);
            }
        }*/
        //绑定数据
        if (bankBean.getBankList() != null) {
            if (selectBankListViewAdapter == null) {
                selectBankListViewAdapter = new SelectBankListViewAdapter();
                lvSelectBank.setAdapter(selectBankListViewAdapter);
            } else {
                selectBankListViewAdapter.notifyDataSetChanged();
            }
        }
    }


    public class SelectBankListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return bankBean.getBankList().size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AuthCardInfo authCardInfo = bankBean.getBankList().get(position);
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.item_select_bank_listview, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
//            holder.bankLogo.setImageResource(authCardInfo.getLogoResId());
            Picasso.with(ProfileSelectBankActivity.this)
                    .load(authCardInfo.getBankicon())
                    .into(holder.bankLogo);
            holder.bankName.setText(authCardInfo.getBankname());
            holder.bankRemark.setText(authCardInfo.getRemark());
            return convertView;
        }


        public class ViewHolder {
            @InjectView(R.id.bankLogo)
            ImageView bankLogo;
            @InjectView(R.id.bankName)
            TextView bankName;
            @InjectView(R.id.bankRemark)
            TextView bankRemark;

            ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }
        }
    }
}
