package com.mgjr.model.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/14.
 */
public class HomepageRecommendProjectsBean extends BaseBean {


    private HqbBean hqb;

    private LoanBean loan;

    private List<String> messageList;

    public HqbBean getHqb() {
        return hqb;
    }

    public void setHqb(HqbBean hqb) {
        this.hqb = hqb;
    }

    public LoanBean getLoan() {
        return loan;
    }

    public void setLoan(LoanBean loan) {
        this.loan = loan;
    }

    public List<String> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<String> messageList) {
        this.messageList = messageList;
    }


    public static class HqbBean {
        private String code;
        private int id;
        private String period;
        private double rate;
        private int tenderMemberCount;
        private String title;

        public void setRate(double rate) {
            this.rate = rate;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public Double getRate() {
            return rate;
        }

        public void setRate(Double rate) {
            this.rate = rate;
        }

        public int getTenderMemberCount() {
            return tenderMemberCount;
        }

        public void setTenderMemberCount(int tenderMemberCount) {
            this.tenderMemberCount = tenderMemberCount;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class LoanBean {
        private String code;
        private int id;
        private int period;
        private double rate;
        private int tenderMemberCount;
        private String title;
        private double zy;
        private double preIncome10000;

        public void setRate(double rate) {
            this.rate = rate;
        }

        public void setZy(double zy) {
            this.zy = zy;
        }

        public double getPreIncome10000() {
            return preIncome10000;
        }

        public void setPreIncome10000(double preIncome10000) {
            this.preIncome10000 = preIncome10000;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getId() {
            return id;
        }

        public int getPeriod() {
            return period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Double getRate() {
            return rate;
        }

        public void setRate(Double rate) {
            this.rate = rate;
        }

        public void setZy(Double zy) {
            this.zy = zy;
        }

        public int getTenderMemberCount() {
            return tenderMemberCount;
        }

        public void setTenderMemberCount(int tenderMemberCount) {
            this.tenderMemberCount = tenderMemberCount;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Double getZy() {
            return zy;
        }
    }

    private List<AppBannersBean> appbanners;

    public List<AppBannersBean> getAppbannersList() {
        return appbanners;
    }

    public void setAppbannersList(List<AppBannersBean> appbanners) {
        this.appbanners = appbanners;
    }

    public static class AppBannersBean {
        private String app_type;
        private String ctime;
        private int fromsource;
        private int id;
        private String image_url;
        private int sort;
        private int status;
        private String title;
        private String to_url;
        private String shareUrl;
        private String shareContent;
        private int type;

        private Map jumpParams;

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }

        public String getShareContent() {
            return shareContent;
        }

        public void setShareContent(String shareContent) {
            this.shareContent = shareContent;
        }

        public Map getJumpParams() {
            return jumpParams;
        }

        public void setJumpParams(Map jumpParams) {
            this.jumpParams = jumpParams;
        }

        public String getApp_type() {
            return app_type;
        }

        public void setApp_type(String app_type) {
            this.app_type = app_type;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public int getFromsource() {
            return fromsource;
        }

        public void setFromsource(int fromsource) {
            this.fromsource = fromsource;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTo_url() {
            return to_url;
        }

        public void setTo_url(String to_url) {
            this.to_url = to_url;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    private List<Posters> posters;

    public List<AppBannersBean> getAppbanners() {
        return appbanners;
    }

    public void setAppbanners(List<AppBannersBean> appbanners) {
        this.appbanners = appbanners;
    }

    public List<Posters> getPosters() {
        return posters;
    }

    public void setPosters(List<Posters> posters) {
        this.posters = posters;
    }

    public static class Posters {
        public String getApp_type() {
            return app_type;
        }

        public void setApp_type(String app_type) {
            this.app_type = app_type;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public int getFromsource() {
            return fromsource;
        }

        public void setFromsource(int fromsource) {
            this.fromsource = fromsource;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTo_url() {
            return to_url;
        }

        public void setTo_url(String to_url) {
            this.to_url = to_url;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        private Map jumpParams;

        public Map getJumpParams() {
            return jumpParams;
        }

        public void setJumpParams(Map jumpParams) {
            this.jumpParams = jumpParams;
        }

        public String getShareContent() {
            return shareContent;
        }

        public void setShareContent(String shareContent) {
            this.shareContent = shareContent;
        }

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }

        private String app_type;
        private String ctime;
        private int fromsource;
        private int id;
        private String image_url;
        private int sort;
        private int status;
        private String title;
        private String to_url;
        private String shareUrl;
        private int type;
        private String shareContent;
    }


}
