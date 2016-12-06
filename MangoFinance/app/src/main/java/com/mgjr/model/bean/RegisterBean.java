package com.mgjr.model.bean;

/**
 * Created by Administrator on 2016/8/23.
 */
public class RegisterBean extends BaseBean {

    private UserBean user;
    private boolean isFirstInvestment;


    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean userBean) {
        this.user = userBean;
    }

    public boolean isFirstInvestment() {
        return isFirstInvestment;
    }

    public void setFirstInvestment(boolean firstInvestment) {
        isFirstInvestment = firstInvestment;
    }
}
