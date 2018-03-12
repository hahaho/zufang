package com.apass.zufang.domain.entity;

import java.util.Date;

import com.apass.zufang.common.model.QueryParams;

public class AppVersionEntity extends QueryParams {
    
    private Long Id;
    
    /**
     * 版本号
     */
    private String versionName;
    /**
     * 版本编码
     */
    private String versionCode;
    /**
     * 文件路径
     */
    private String fileRoute;
    /**
     * 文件大小
     */
    private String appSize;
    /**
     * 是否强制升级   0 否     1 是
     */
    private String upgradeflag;
    /**
     * 升级描述
     */
    private String explains;
    
    private String distribution;
    /**
     * ios版本号
     */
    private String iosVersionname;
    /**
     * ios版本编码
     */
    private String iosVersioncode;
    /**
     * ios 包大小
     */
    private String iosAppsize;
    /**
     * ios 是否强制升级   0 否     1 是
     */
    private String iosUpgradeflag;
    /**
     * ios  升级描述
     */
    private String iosExplains;
    
    private String iosDistribution;
    
    private String downloanurl;
    
    private String indexbanner;
    
    private String mybanner;
    
    private Date   createdDate;
    
    private boolean isReleased = false;

    public boolean isReleased() {
        return isReleased;
    }

    public void setReleased(boolean isReleased) {
        this.isReleased = isReleased;
    }

    public String getIdStr() {
        return String.valueOf(this.Id.longValue());
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getFileRoute() {
        return fileRoute;
    }

    public void setFileRoute(String fileRoute) {
        this.fileRoute = fileRoute;
    }

    public String getAppSize() {
        return appSize;
    }

    public void setAppSize(String appSize) {
        this.appSize = appSize;
    }

    public String getUpgradeflag() {
        return upgradeflag;
    }

    public void setUpgradeflag(String upgradeflag) {
        this.upgradeflag = upgradeflag;
    }

    public String getExplains() {
        return explains;
    }

    public void setExplains(String explains) {
        this.explains = explains;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public String getIosVersionname() {
        return iosVersionname;
    }

    public void setIosVersionname(String iosVersionname) {
        this.iosVersionname = iosVersionname;
    }

    public String getIosVersioncode() {
        return iosVersioncode;
    }

    public void setIosVersioncode(String iosVersioncode) {
        this.iosVersioncode = iosVersioncode;
    }

    public String getIosAppsize() {
        return iosAppsize;
    }

    public void setIosAppsize(String iosAppsize) {
        this.iosAppsize = iosAppsize;
    }

    public String getIosUpgradeflag() {
        return iosUpgradeflag;
    }

    public void setIosUpgradeflag(String iosUpgradeflag) {
        this.iosUpgradeflag = iosUpgradeflag;
    }

    public String getIosExplains() {
        return iosExplains;
    }

    public void setIosExplains(String iosExplains) {
        this.iosExplains = iosExplains;
    }

    public String getIosDistribution() {
        return iosDistribution;
    }

    public void setIosDistribution(String iosDistribution) {
        this.iosDistribution = iosDistribution;
    }

    public String getDownloanurl() {
        return downloanurl;
    }

    public void setDownloanurl(String downloanurl) {
        this.downloanurl = downloanurl;
    }

    public String getIndexbanner() {
        return indexbanner;
    }

    public void setIndexbanner(String indexbanner) {
        this.indexbanner = indexbanner;
    }

    public String getMybanner() {
        return mybanner;
    }

    public void setMybanner(String mybanner) {
        this.mybanner = mybanner;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }


}
