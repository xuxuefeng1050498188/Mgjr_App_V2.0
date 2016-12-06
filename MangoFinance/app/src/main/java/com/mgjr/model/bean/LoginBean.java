package com.mgjr.model.bean;

import java.util.Map;

/**
 * Created by wim on 16/8/16.
 */
public class LoginBean extends BaseBean{
    private boolean isFirstInvestment;

    private UserBean user;

    private Map<String,Integer> errorMap;

    public Map<String, Integer> getErrorMap() {
        return errorMap;
    }

    public void setErrorMap(Map<String, Integer> errorMap) {
        this.errorMap = errorMap;
    }

    public boolean isFirstInvestment() {
        return isFirstInvestment;
    }

    public void setFirstInvestment(boolean firstInvestment) {
        isFirstInvestment = firstInvestment;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
}
