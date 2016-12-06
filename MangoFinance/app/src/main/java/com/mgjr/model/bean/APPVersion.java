package com.mgjr.model.bean;

/**
 * Created by wim on 16/10/24.
 */

public class APPVersion extends BaseBean{
    private AppVersion appVersion;

    public AppVersion getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(AppVersion appVersion) {
        this.appVersion = appVersion;
    }

    public class  AppVersion {
        String appName;
        String appType;
        String describe;
        String apk;
        int is_forcedUpdate;
        int versionNum;
        int status;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getAppType() {
            return appType;
        }

        public void setAppType(String appType) {
            this.appType = appType;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public String getApk() {
            return apk;
        }

        public void setApk(String apk) {
            this.apk = apk;
        }

        public int getIs_forcedUpdate() {
            return is_forcedUpdate;
        }

        public void setIs_forcedUpdate(int is_forcedUpdate) {
            this.is_forcedUpdate = is_forcedUpdate;
        }

        public int getVersionNum() {
            return versionNum;
        }

        public void setVersionNum(int versionNum) {
            this.versionNum = versionNum;
        }
    }


}
