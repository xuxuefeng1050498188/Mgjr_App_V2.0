package com.mgjr.model.bean;

/**
 * Created by Administrator on 2016/8/26.
 */
public class GetVcodeBean extends LoginBean {
    private String rand;

//    private UserBean user;

//    @Override
//    public UserBean getUser() {
//        return user;
////    }

//    @Override
//    public void setUser(UserBean user) {
//        this.user = user;
//    }

    public String getRand() {
        return rand;
    }

    public void setRand(String rand) {
        this.rand = rand;
    }
}
