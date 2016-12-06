package com.mgjr.model.bean;

/**
 * Created by Administrator on 2016/10/8.
 */

public class FinancialPlannerBean extends BaseBean {

    private int friendsCount;
    private int friendsTenderCount;
    private double jlAmount;

    private String shareContent;
    private String shareTitle;
    private String shareUrl;

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    private MemberBean member;

    public int getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(int friendsCount) {
        this.friendsCount = friendsCount;
    }

    public int getFriendsTenderCount() {
        return friendsTenderCount;
    }

    public void setFriendsTenderCount(int friendsTenderCount) {
        this.friendsTenderCount = friendsTenderCount;
    }

    public double getJlAmount() {
        return jlAmount;
    }

    public void setJlAmount(double jlAmount) {
        this.jlAmount = jlAmount;
    }

    public MemberBean getMember() {
        return member;
    }

    public void setMember(MemberBean member) {
        this.member = member;
    }

    public static class MemberBean {
        private String activityflag;
        private String attrs;
        private long becomepttime;
        private long becometime;
        private long ctime;
        private int ctype;
        private String email;
        private int eok;
        private int fromsource;
        private String hashcode;
        private int id;
        private String idcode;
        private int idtype;
        private int lcs;
        private String logip;
        private String logpwd;
        private long logtime;
        private int membertype;
        private String mobile;
        private int mok;
        private String paypwd;
        private int pmid;
        private String roles;
        private int sourceid;
        private int status;
        private String truename;
        private String username;
        private int vip;

        public String getActivityflag() {
            return activityflag;
        }

        public void setActivityflag(String activityflag) {
            this.activityflag = activityflag;
        }

        public String getAttrs() {
            return attrs;
        }

        public void setAttrs(String attrs) {
            this.attrs = attrs;
        }

        public long getBecomepttime() {
            return becomepttime;
        }

        public void setBecomepttime(long becomepttime) {
            this.becomepttime = becomepttime;
        }

        public long getBecometime() {
            return becometime;
        }

        public void setBecometime(long becometime) {
            this.becometime = becometime;
        }

        public long getCtime() {
            return ctime;
        }

        public void setCtime(long ctime) {
            this.ctime = ctime;
        }

        public int getCtype() {
            return ctype;
        }

        public void setCtype(int ctype) {
            this.ctype = ctype;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getEok() {
            return eok;
        }

        public void setEok(int eok) {
            this.eok = eok;
        }

        public int getFromsource() {
            return fromsource;
        }

        public void setFromsource(int fromsource) {
            this.fromsource = fromsource;
        }

        public String getHashcode() {
            return hashcode;
        }

        public void setHashcode(String hashcode) {
            this.hashcode = hashcode;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIdcode() {
            return idcode;
        }

        public void setIdcode(String idcode) {
            this.idcode = idcode;
        }

        public int getIdtype() {
            return idtype;
        }

        public void setIdtype(int idtype) {
            this.idtype = idtype;
        }

        public int getLcs() {
            return lcs;
        }

        public void setLcs(int lcs) {
            this.lcs = lcs;
        }

        public String getLogip() {
            return logip;
        }

        public void setLogip(String logip) {
            this.logip = logip;
        }

        public String getLogpwd() {
            return logpwd;
        }

        public void setLogpwd(String logpwd) {
            this.logpwd = logpwd;
        }

        public long getLogtime() {
            return logtime;
        }

        public void setLogtime(long logtime) {
            this.logtime = logtime;
        }

        public int getMembertype() {
            return membertype;
        }

        public void setMembertype(int membertype) {
            this.membertype = membertype;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getMok() {
            return mok;
        }

        public void setMok(int mok) {
            this.mok = mok;
        }

        public String getPaypwd() {
            return paypwd;
        }

        public void setPaypwd(String paypwd) {
            this.paypwd = paypwd;
        }

        public int getPmid() {
            return pmid;
        }

        public void setPmid(int pmid) {
            this.pmid = pmid;
        }

        public String getRoles() {
            return roles;
        }

        public void setRoles(String roles) {
            this.roles = roles;
        }

        public int getSourceid() {
            return sourceid;
        }

        public void setSourceid(int sourceid) {
            this.sourceid = sourceid;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTruename() {
            return truename;
        }

        public void setTruename(String truename) {
            this.truename = truename;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getVip() {
            return vip;
        }

        public void setVip(int vip) {
            this.vip = vip;
        }
    }
}
