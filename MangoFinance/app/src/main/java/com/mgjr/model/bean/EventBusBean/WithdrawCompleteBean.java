package com.mgjr.model.bean.EventBusBean;

import com.mgjr.model.bean.AccountBankcardBean;
import com.mgjr.model.bean.BaseBean;
import com.mgjr.model.bean.RequestWithdrawBean;

/**
 * Created by xuxuefeng on 2016/10/21.
 */

public class WithdrawCompleteBean extends BaseBean {

    private double accountAmount;

    private RequestWithdrawBean.AccountWithdrawBean accountWithdraw;

    public RequestWithdrawBean.AccountWithdrawBean getAccountWithdraw() {
        return accountWithdraw;
    }

    public void setAccountWithdraw(RequestWithdrawBean.AccountWithdrawBean accountWithdraw) {
        this.accountWithdraw = accountWithdraw;
    }

    public double getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(double accountAmount) {
        this.accountAmount = accountAmount;
    }
}
