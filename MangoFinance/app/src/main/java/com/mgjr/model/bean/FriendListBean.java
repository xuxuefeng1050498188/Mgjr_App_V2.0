package com.mgjr.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/8.
 */

public class FriendListBean extends BaseBean {

    private List<FriendsListBean> friendsList;

    public List<FriendsListBean> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(List<FriendsListBean> friendsList) {
        this.friendsList = friendsList;
    }

    public static class FriendsListBean {
        private String ctime;
        private double jlamount;
        private String mobile;
        private String username;
        private int whenlcs;

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public double getJlamount() {
            return jlamount;
        }

        public void setJlamount(double jlamount) {
            this.jlamount = jlamount;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getWhenlcs() {
            return whenlcs;
        }

        public void setWhenlcs(int whenlcs) {
            this.whenlcs = whenlcs;
        }
    }
}
