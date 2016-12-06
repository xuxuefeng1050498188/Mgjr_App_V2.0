package com.mgjr.view.invester.adapter;


import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.model.bean.EventBusBean.NotificationBean;
import com.mgjr.model.bean.LoanBean;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.fanrunqi.waveprogress.WaveProgressView;


/**
 * Created by Administrator on 2016/7/5.
 */
public class InvestListViewAdapter extends BaseAdapter {

    private List<LoanBean> loanBeanList;

    public InvestListViewAdapter(List<LoanBean> loanBeanList) {
        this.loanBeanList = loanBeanList;
    }

    @Override
    public int getCount() {
        //不包括活期宝
        return loanBeanList.size();
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
        LoanBean loanBean = loanBeanList.get(position);
        if (loanBean.getPeriod() == 1) {
            //新手福利标
            convertView = View.inflate(MainApplication.getContext(), R.layout.item_newuser_invest_listview, null);
            bindNewUserBidData(convertView, loanBean);
        } else {
            //秒杀标或者金芒果
            convertView = View.inflate(parent.getContext(), R.layout.item_jmg_list_invest_listview, null);
            holder = new ViewHolder(convertView);
            //status：借款状态；2：立即投资、3：满标、100：还款中、200：已还款；
            switch (loanBean.getStatus()) {
                case 2:
                    if (loanBean.getTypeid() > 10) {
                        //秒杀标
                        if (loanBean.getBstime() > System.currentTimeMillis()) {
                            bindSeckillData(loanBean, holder);
                        } else {
                            bindInvestData(loanBean, holder);
                        }
                    } else {
                        //金芒果
                        bindInvestData(loanBean, holder);
                    }
                    break;
                case 3: //满标
                    bindFullflowData(loanBean, holder);
                    break;
                case 100://还款中
                    bindPayingData(loanBean, holder);
                    break;
                case 200://已还款
                    bindPaybackData(loanBean, holder);
                    break;
            }
        }
        return convertView;
    }

    private void bindNewUserBidData(View convertView, LoanBean loanBean) {
        TextView tv_desc_newuser_welfare = (TextView) convertView.findViewById(R.id.tv_desc_newuser_welfare);
        TextView tv_rate_invest = (TextView) convertView.findViewById(R.id.tv_rate_invest);
        if (loanBean.getDtinfo() != null) {
            tv_desc_newuser_welfare.setText(loanBean.getDtinfo());
        }
        tv_rate_invest.setText("" + (loanBean.getRate() - loanBean.getZy()));
    }

    /**
     * 绑定秒杀标数据
     *
     * @param seckillBean
     * @param holder
     */
    private void bindSeckillData(final LoanBean seckillBean, final ViewHolder holder) {
        bindBaseData(seckillBean, holder);

        holder.seckillItemJmgListInvestListview.setVisibility(View.VISIBLE);
        holder.investItemJmgListInvestListview.setVisibility(View.INVISIBLE);
        holder.fullflowItemJmgListInvestListview.setVisibility(View.INVISIBLE);
        holder.payingItemJmgListInvestListview.setVisibility(View.INVISIBLE);
        holder.paybackItemJmgListInvestListview.setVisibility(View.INVISIBLE);

        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//        final long sumTime = seckillBean.getCstime() - System.currentTimeMillis();
//        holder.ivSeckillCounter.setText("" + sdf.format(seckillBean.getBstime()));
        //进行倒计时
        final long currentTimeMillis = System.currentTimeMillis();
        new CountDownTimer(Math.abs(seckillBean.getBstime() - currentTimeMillis), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                holder.ivSeckillCounter.setText("" + toClock(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                //变成可投资的标
                EventBus.getDefault().postSticky(new NotificationBean());
            }
        }.start();
    }

    public String toClock(long millisUntilFinished) {
        long hour = millisUntilFinished / (60 * 60 * 1000);
        long minute = (millisUntilFinished - hour * 60 * 60 * 1000) / (60 * 1000);
        long second = (millisUntilFinished - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000;
        if (second >= 60) {
            second = second % 60;
            minute += second / 60;
        }
        if (minute >= 60) {
            minute = minute % 60;
            hour += minute / 60;
        }
        String sh = "";
        String sm = "";
        String ss = "";
        if (hour < 10) {
            sh = "0" + String.valueOf(hour);
        } else {
            sh = String.valueOf(hour);
        }
        if (minute < 10) {
            sm = "0" + String.valueOf(minute);
        } else {
            sm = String.valueOf(minute);
        }
        if (second < 10) {
            ss = "0" + String.valueOf(second);
        } else {
            ss = String.valueOf(second);
        }
        return sh + ":" + sm + ":" + ss;
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
    }

    /**
     * 绑定投资数据
     *
     * @param jmgBean
     * @param holder
     */
    private void bindInvestData(LoanBean jmgBean, ViewHolder holder) {
        bindBaseData(jmgBean, holder);
        holder.investItemJmgListInvestListview.setVisibility(View.VISIBLE);
        holder.fullflowItemJmgListInvestListview.setVisibility(View.GONE);
        holder.payingItemJmgListInvestListview.setVisibility(View.GONE);
        holder.paybackItemJmgListInvestListview.setVisibility(View.GONE);
        holder.seckillItemJmgListInvestListview.setVisibility(View.GONE);
        //开启波浪动画
//        holder.waterWaveProgressInvest.animateWave();
        int progress = (int) (((jmgBean.getAmount() - jmgBean.getBalance()) / jmgBean.getAmount()) * 100);
        holder.waterWaveProgressInvest.setWaveColor("#FFC486");
        holder.waterWaveProgressInvest.setCurrent(progress, "");
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
