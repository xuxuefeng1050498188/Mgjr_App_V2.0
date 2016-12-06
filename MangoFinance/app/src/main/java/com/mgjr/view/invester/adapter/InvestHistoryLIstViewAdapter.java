package com.mgjr.view.invester.adapter;


import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgjr.mangofinance.MainApplication;
import com.mgjr.R;
import com.mgjr.model.bean.InvestBean;
import com.mgjr.model.bean.LoanBean;
import com.mgjr.share.WaterWaveProgress;
import com.mgjr.view.invester.InvestItemBubbleView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.fanrunqi.waveprogress.WaveProgressView;


/**
 * Created by Administrator on 2016/7/5.
 */
public class InvestHistoryLIstViewAdapter extends BaseAdapter {

    private List<LoanBean> jmgBidList;

    private Handler handler = new Handler();

    public InvestHistoryLIstViewAdapter(List<LoanBean> jmgBidList) {
        this.jmgBidList = jmgBidList;
    }

    @Override
    public int getCount() {
        return this.jmgBidList.size();
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_jmg_list_invest_listview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //金芒果
        LoanBean jmgBean = jmgBidList.get(position);
        //status：借款状态；2：立即投资、3：满标、100：还款中、200：已还款；
        if (jmgBean.getStatus() == 3) {
            //满标
            bindFullflowData(jmgBean, holder);
        } else if (jmgBean.getStatus() == 100) {
            //还款中
            bindPayingData(jmgBean, holder);
        } else if (jmgBean.getStatus() == 200) {
            //已还款
            bindPaybackData(jmgBean, holder);
        }
        return convertView;
    }

    /**
     * 绑定基本数据
     *
     * @param loanBean
     * @param holder
     */
    private void bindBaseData(LoanBean loanBean, ViewHolder holder) {
        holder.tvJmgTitle.setText(loanBean.getTitle());
        holder.tvJmgPeriod.setText("" + loanBean.getPeriod() + "个月");
        holder.tvJmgRate.setText("" + (loanBean.getRate() - loanBean.getZy()));
        if (loanBean.getZy() != 0) {
            holder.tvJmgZy.setText("%" + "+" + loanBean.getZy() + "%");
        } else {
            holder.tvJmgZy.setText("%");
        }

        if (loanBean.getDtinfo() != null) {
            String[] descs = loanBean.getDtinfo().split(",");
            if (!TextUtils.isEmpty(descs[0])) {
                holder.tvJmgLimitRaiseRate.setVisibility(View.VISIBLE);
                holder.tvJmgLimitRaiseRate.setText(descs[0]);
            }
            if (!TextUtils.isEmpty(descs[1])) {
                holder.tvJmgLimitSeckill.setVisibility(View.VISIBLE);
                holder.tvJmgLimitSeckill.setText(descs[1]);
            }
        }
    }

    private void bindPaybackData(LoanBean jmgBean, ViewHolder holder) {
        bindBaseData(jmgBean, holder);
        holder.paybackItemJmgListInvestListview.setVisibility(View.VISIBLE);
        holder.seckillItemJmgListInvestListview.setVisibility(View.INVISIBLE);
        holder.investItemJmgListInvestListview.setVisibility(View.INVISIBLE);
        holder.fullflowItemJmgListInvestListview.setVisibility(View.INVISIBLE);
        holder.payingItemJmgListInvestListview.setVisibility(View.INVISIBLE);
    }

    private void bindPayingData(LoanBean jmgBean, ViewHolder holder) {
        bindBaseData(jmgBean, holder);
        holder.payingItemJmgListInvestListview.setVisibility(View.VISIBLE);
        holder.paybackItemJmgListInvestListview.setVisibility(View.INVISIBLE);
        holder.seckillItemJmgListInvestListview.setVisibility(View.INVISIBLE);
        holder.investItemJmgListInvestListview.setVisibility(View.INVISIBLE);
        holder.fullflowItemJmgListInvestListview.setVisibility(View.INVISIBLE);
    }

    private void bindFullflowData(LoanBean jmgBean, ViewHolder holder) {
        bindBaseData(jmgBean, holder);
        holder.fullflowItemJmgListInvestListview.setVisibility(View.VISIBLE);
        holder.payingItemJmgListInvestListview.setVisibility(View.INVISIBLE);
        holder.paybackItemJmgListInvestListview.setVisibility(View.INVISIBLE);
        holder.seckillItemJmgListInvestListview.setVisibility(View.INVISIBLE);
        holder.investItemJmgListInvestListview.setVisibility(View.INVISIBLE);
//        addBubbles(holder.investRlContainer);
    }

    /**
     * 添加两个气泡
     *
     * @param viewGroup
     */
    private void addBubbles(final ViewGroup viewGroup) {
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 3; i++) {
                    SystemClock.sleep(1000);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            viewGroup.addView(new InvestItemBubbleView(MainApplication.getContext()));
                        }
                    });
                }
            }
        }.start();
    }

    static class ViewHolder {
        @InjectView(R.id.tv_jmg_title)
        TextView tvJmgTitle;
        @InjectView(R.id.tv_jmg_period)
        TextView tvJmgPeriod;
        @InjectView(R.id.tv_jmg_rate)
        TextView tvJmgRate;
        @InjectView(R.id.tv_jmg_zy)
        TextView tvJmgZy;
        @InjectView(R.id.water_wave_progress_invest)
        WaveProgressView waterWaveProgressInvest;
        @InjectView(R.id.invest_item_jmg_list_invest_listview)
        FrameLayout investItemJmgListInvestListview;
        @InjectView(R.id.fullflow_item_jmg_list_invest_listview)
        FrameLayout fullflowItemJmgListInvestListview;
        @InjectView(R.id.iv_invest_thunder)
        ImageView ivInvestThunder;
        @InjectView(R.id.paying_item_jmg_list_invest_listview)
        FrameLayout payingItemJmgListInvestListview;
        @InjectView(R.id.payback_item_jmg_list_invest_listview)
        FrameLayout paybackItemJmgListInvestListview;
        @InjectView(R.id.iv_seckill_counter)
        TextView ivSeckillCounter;
        @InjectView(R.id.seckill_item_jmg_list_invest_listview)
        FrameLayout seckillItemJmgListInvestListview;
        @InjectView(R.id.invest_rl_container)
        FrameLayout investRlContainer;
        @InjectView(R.id.tv_jmg_limit_raise_rate)
        TextView tvJmgLimitRaiseRate;
        @InjectView(R.id.tv_jmg_limit_seckill)
        TextView tvJmgLimitSeckill;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
