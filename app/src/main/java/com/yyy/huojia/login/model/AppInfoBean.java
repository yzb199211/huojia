package com.yyy.huojia.login.model;

public class AppInfoBean {

    /**
     * sAppVersion :
     * sAppApk :
     */

    private int iAppVersion;
    private String sAppApk;
    private int iTimeout;

    public int getSAppVersion() {
        return iAppVersion;
    }

    public void setSAppVersion(int sAppVersion) {
        this.iAppVersion = sAppVersion;
    }

    public String getSAppApk() {
        return sAppApk;
    }

    public void setSAppApk(String sAppApk) {
        this.sAppApk = sAppApk;
    }

    public int getiTimeout() {
        return iTimeout;
    }

    public void setiTimeout(int iTimeout) {
        this.iTimeout = iTimeout;
    }
}
