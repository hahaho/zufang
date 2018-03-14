package com.apass.zufang.domain.entity;
import java.util.Date;
/**
 * 回访记录对象持久化类
 * @author Administrator
 *
 */
public class ReturnVisit {
    private Long id;

    private Long houseId;

    private Long reserveHouseId;

    private Byte visitStatus;

    private Byte rentStatus;

    private String feedBack;

    private String memo;

    private String isDelete;

    private Date createdTime;

    private Date updatedTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public Long getReserveHouseId() {
        return reserveHouseId;
    }

    public void setReserveHouseId(Long reserveHouseId) {
        this.reserveHouseId = reserveHouseId;
    }

    public Byte getVisitStatus() {
        return visitStatus;
    }

    public void setVisitStatus(Byte visitStatus) {
        this.visitStatus = visitStatus;
    }

    public Byte getRentStatus() {
        return rentStatus;
    }

    public void setRentStatus(Byte rentStatus) {
        this.rentStatus = rentStatus;
    }

    public String getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(String feedBack) {
        this.feedBack = feedBack;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
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
}