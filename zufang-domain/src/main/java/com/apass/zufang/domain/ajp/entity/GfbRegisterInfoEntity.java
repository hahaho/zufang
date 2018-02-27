package com.apass.zufang.domain.ajp.entity;

import java.util.Date;

public class GfbRegisterInfoEntity {
    private Long id;

    private String account;

    private String password;

    private String salt;

    private String handPassword;

    private String email;

    private String deviceId;

    private Integer faceScore;

    private Date createdDate;

    private Date updatedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHandPassword() {
        return handPassword;
    }

    public void setHandPassword(String handPassword) {
        this.handPassword = handPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getFaceScore() {
        return faceScore;
    }

    public void setFaceScore(Integer faceScore) {
        this.faceScore = faceScore;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}