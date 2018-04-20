package com.apass.zufang.domain.entity;

import java.util.Date;

public class Banner {
    private Long id;

    private Byte bannerType;

    private String bannerImgUrl;

    private Long bannerSortNo;

    private String bannerUrl;

    private Date createdTime;

    private Date updatedTime;

    private String createdUser;

    private String updatedUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getBannerType() {
        return bannerType;
    }

    public void setBannerType(Byte bannerType) {
        this.bannerType = bannerType;
    }

    public String getBannerImgUrl() {
        return bannerImgUrl;
    }

    public void setBannerImgUrl(String bannerImgUrl) {
        this.bannerImgUrl = bannerImgUrl;
    }

    public Long getBannerSortNo() {
        return bannerSortNo;
    }

    public void setBannerSortNo(Long bannerSortNo) {
        this.bannerSortNo = bannerSortNo;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String getUpdatedUser() {
        return updatedUser;
    }

    public void setUpdatedUser(String updatedUser) {
        this.updatedUser = updatedUser;
    }
}