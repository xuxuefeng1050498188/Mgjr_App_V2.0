package com.mgjr.view.profile.mangoExperienceFinancing;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mgjr.R;

/**
 * Created by Administrator on 2016/8/9.
 */
public class MangoTyjInvestDialog extends Dialog {

    private Context context;
    private String title;
    private String rate;//年化收益
    private String invest_period; // 标资期限
    private String balance; // 体验金余额
    private String projectaccount;//项目总额
    private String repaymenttype;//还款方式
    private String interesttime;//计息时间
    private String investableaccount;//可投金额

    private ImageButton img_closebtn;

    private Button btn_invest;
    private ClickListenerInterface clickListenerInterface;
    private  EditText et_mgtyj_dialog_investaccount;


    public MangoTyjInvestDialog(Context context, String title,String rate,
                          String invest_period, String projectaccount, String repaymenttype,
                          String interesttime, String investableaccount,String balance) {
        super(context);
        this.context = context;
        this.title = title;
        this.projectaccount = projectaccount;
        this.rate = rate;
        this.invest_period = invest_period;
        this.interesttime = interesttime;
        this.repaymenttype = repaymenttype;
        this.balance = balance;
        this.investableaccount = investableaccount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        init();

    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_mangotyj_investdialog, null);
        setContentView(view);

        et_mgtyj_dialog_investaccount = (EditText) view.findViewById(R.id.et_mgtyj_dialog_investaccount);

        TextView tv_title = (TextView) view
                .findViewById(R.id.tv_mgtyj_dialog__dialogtitle);
        TextView tv_rate = (TextView) view
                .findViewById(R.id.tv_mgtyj_dialog_rate);
        TextView tv_period = (TextView) view
                .findViewById(R.id.tv_mgtyj_dialog_period);
        TextView tv_projectaccount = (TextView) view
                .findViewById(R.id.tv_mgtyj_dialog_projectaccount);
        TextView tv_repaymenttype = (TextView) view
                .findViewById(R.id.tv_mgtyj_dialog_repaymenttype);
        TextView tv_interesttime = (TextView) view
                .findViewById(R.id.tv_mgtyj_dialog_interesttime);
        TextView tv_investableaccount = (TextView) view.findViewById(R.id.tv_mgtyj_dialog_investableaccount);
        TextView tv_tyjbalance = (TextView) view.findViewById(R.id.tv_mgtyj_dialog_tyjbalance);
        et_mgtyj_dialog_investaccount.setText(balance);
        tv_title.setText(title);
        tv_rate.setText(rate + "%");
        tv_period.setText(invest_period);
        tv_projectaccount.setText(projectaccount + "元");
        tv_repaymenttype.setText(repaymenttype);
        tv_interesttime.setText(interesttime);
        tv_investableaccount.setText(investableaccount);
        tv_tyjbalance.setText(balance + "元");

        img_closebtn = (ImageButton) view.findViewById(R.id.imgbtn_mgtyj_dialog_close_btn);
        img_closebtn.setOnClickListener(new clickListener());

        btn_invest = (Button) view.findViewById(R.id.btn_mgtyj_dialog_invest);
        btn_invest.setOnClickListener(new clickListener());

    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            if (v == img_closebtn) {
                clickListenerInterface.doCancel();
            }
            if (v == btn_invest) {
                clickListenerInterface.doConfirm(et_mgtyj_dialog_investaccount.getText());
            }
        }

    };

    public interface ClickListenerInterface {

        public void doConfirm(Editable text);

        public void doCancel();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInvest_period() {
        return invest_period;
    }

    public void setInvest_period(String invest_period) {
        this.invest_period = invest_period;
    }

    public String getInvestableaccount() {
        return investableaccount;
    }

    public void setInvestableaccount(String investableaccount) {
        this.investableaccount = investableaccount;
    }

    public String getInteresttime() {
        return interesttime;
    }

    public void setInteresttime(String interesttime) {
        this.interesttime = interesttime;
    }

    public String getRepaymenttype() {
        return repaymenttype;
    }

    public void setRepaymenttype(String repaymenttype) {
        this.repaymenttype = repaymenttype;
    }

    public String getProjectaccount() {
        return projectaccount;
    }

    public void setProjectaccount(String projectaccount) {
        this.projectaccount = projectaccount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }


    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}

