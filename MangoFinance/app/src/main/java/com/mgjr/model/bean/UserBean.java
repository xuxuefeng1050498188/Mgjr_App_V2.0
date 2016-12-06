package com.mgjr.model.bean;

/**
 * Created by wim on 16/8/16.
 */
public class UserBean extends BaseBean {
    private long ctime;
    private Integer id;
    private boolean isBindCard;
    private boolean isBindEmail;
    private boolean isPaypwd;
    private boolean isTruename;
    private long lastLoginTime;
    private Integer memberType;
    private String name;
    private String headImgUrl;
    private String nickname;
    private String truename;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    private String mobile ;

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isBindCard() {
        return isBindCard;
    }

    public void setBindCard(boolean bindCard) {
        isBindCard = bindCard;
    }

    public boolean isBindEmail() {
        return isBindEmail;
    }

    public void setBindEmail(boolean bindEmail) {
        isBindEmail = bindEmail;
    }

    public boolean isPaypwd() {
        return isPaypwd;
    }

    public void setPaypwd(boolean paypwd) {
        isPaypwd = paypwd;
    }

    public boolean isTruename() {
        return isTruename;
    }

    public void setTruename(boolean truename) {
        isTruename = truename;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getMemberType() {
        return memberType;
    }

    public void setMemberType(Integer memberType) {
        this.memberType = memberType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
