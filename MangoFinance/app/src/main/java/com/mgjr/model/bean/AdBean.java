package com.mgjr.model.bean;

/**
 * Created by Administrator on 2016/11/3.
 */

public class AdBean extends BaseBean {

    private AdvertisementBean advertisement;

    public AdvertisementBean getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(AdvertisementBean advertisement) {
        this.advertisement = advertisement;
    }

    public static class AdvertisementBean {
        private String app_type;
        private String ctime;
        private int fromsource;
        private int id;
        private String image_url;
        private Object jumpParams;
        private int sort;
        private int status;
        private String title;
        private String to_url;
        private int type;

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

        public Object getJumpParams() {
            return jumpParams;
        }

        public void setJumpParams(Object jumpParams) {
            this.jumpParams = jumpParams;
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
}
