package com.apass.zufang.domain.entity;

import java.util.Date;

public class ZfangSpiderHouseEntity {
    private Long id;

    private Long apartmentId;

    private String city;

    private String url;
    private String host;

    private String extHouseId;

    private Date lastJobTime;

    private Date createdTime;

    private Date updatedTime;

    private String isDelete;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Long apartmentId) {
        this.apartmentId = apartmentId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExtHouseId() {
        return extHouseId;
    }

    public void setExtHouseId(String extHouseId) {
        this.extHouseId = extHouseId;
    }

    public Date getLastJobTime() {
        return lastJobTime;
    }

    public void setLastJobTime(Date lastJobTime) {
        this.lastJobTime = lastJobTime;
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

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}