package com.apass.zufang.domain.entity;

import java.util.Date;

public class Banner {
    private Long id;

    private String bannerName;

    private String bannerImgUrl;

    private Byte bannerType;

    private Long bannerSort;

    private String activityUrl;

    private String createUser;

    private String updateUser;

    private Date createTime;

    private Date updateTime;

    private String isDelete;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    public String getBannerImgUrl() {
        return bannerImgUrl;
    }

    public void setBannerImgUrl(String bannerImgUrl) {
        this.bannerImgUrl = bannerImgUrl;
    }

    public Byte getBannerType() {
        return bannerType;
    }

    public void setBannerType(Byte bannerType) {
        this.bannerType = bannerType;
    }

    public Long getBannerSort() {
        return bannerSort;
    }

    public void setBannerSort(Long bannerSort) {
        this.bannerSort = bannerSort;
    }

    public String getActivityUrl() {
        return activityUrl;
    }

    public void setActivityUrl(String activityUrl) {
        this.activityUrl = activityUrl;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }
}