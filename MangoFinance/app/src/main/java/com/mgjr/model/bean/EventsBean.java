package com.mgjr.model.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/2.
 */
public class EventsBean extends BaseBean {



    private List<ActivityListBean> activityList;

    public List<ActivityListBean> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<ActivityListBean> activityList) {
        this.activityList = activityList;
    }

    public static class ActivityListBean {
        private String activity_url;
        private String end_time;
        private String explain_time;
        private String fromsource;
        private int id;
        private String image_url;
        private String start_time;
        private int status;
        private String title;
        private String shareContent;
        private String shareUrl;
        private Map jumpParams;

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }

        public Map getJumpParams() {
            return jumpParams;
        }

        public void setJumpParams(Map jumpParams) {
            this.jumpParams = jumpParams;
        }

        public String getActivity_url() {
            return activity_url;
        }

        public void setActivity_url(String activity_url) {
            this.activity_url = activity_url;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getExplain_time() {
            return explain_time;
        }

        public void setExplain_time(String explain_time) {
            this.explain_time = explain_time;
        }

        public String getFromsource() {
            return fromsource;
        }

        public void setFromsource(String fromsource) {
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

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
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

        public String getShareContent() {
            return shareContent;
        }

        public void setShareContent(String shareContent) {
            this.shareContent = shareContent;
        }
    }
}
