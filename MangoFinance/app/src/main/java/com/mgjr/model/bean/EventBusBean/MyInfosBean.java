package com.mgjr.model.bean.EventBusBean;

import android.graphics.Bitmap;

import com.mgjr.model.bean.AccountSettingBean;

/**
 * Created by xuxuefeng on 2016/9/6.
 * 用来封装账户设置界面传递到我的信息界面的数据
 */
public class MyInfosBean {
    private AccountSettingBean accountSettingBean;
    private Bitmap headImage;
    private String nickName;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public AccountSettingBean getAccountSettingBean() {
        return accountSettingBean;
    }

    public void setAccountSettingBean(AccountSettingBean accountSettingBean) {
        this.accountSettingBean = accountSettingBean;
    }

    public Bitmap getHeadImage() {
        return headImage;
    }

    public void setHeadImage(Bitmap headImage) {
        this.headImage = headImage;
    }
}
