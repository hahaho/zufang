package com.apass.zufang.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

/**
 * Created by xiaohai on 2018/3/4.
 */
public class HouseAppSearchVo {
    /**
     * 图片url
     */
    private String url;
    /**
     * 房源标题
     */
    private String houseTitle;
    /**
     * 房源描述
     */
    private String description;
    /**
     * 详细地址
     */
    private String detailAddr;
    /**
     * 室
     */
    private Integer room;

    /**
     * 厅
     */
    private Integer hall;
    /**
     * 卫
     */
    private Integer wei;

    /**
     * 层
     */
    private Integer floor;

    /**
     * 房屋总面积
     */
    private BigDecimal acreage;

    /**
     * 出租面积
     */
    private BigDecimal roomAcreage;
    /**
     * 租金
     */
    private BigDecimal rentAmt;
    /**
     * 整合室、厅、卫字段，显示：一室一厅一卫-55㎡
     */
    private String houseDes;

    /**
     * 房屋id
     */
    @JsonIgnore
    private Long houseId;
    /**
     * 房屋所在经度
     */
    @JsonIgnore
    private double longitude;
    /**
     * 房屋所在纬度
     */
    @JsonIgnore
    private double latitude;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHouseTitle() {
        return houseTitle;
    }

    public void setHouseTitle(String houseTitle) {
        this.houseTitle = houseTitle;
    }

    public String getDetailAddr() {
        return detailAddr;
    }

    public void setDetailAddr(String detailAddr) {
        this.detailAddr = detailAddr;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    public Integer getHall() {
        return hall;
    }

    public void setHall(Integer hall) {
        this.hall = hall;
    }

    public Integer getWei() {
        return wei;
    }

    public void setWei(Integer wei) {
        this.wei = wei;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public BigDecimal getAcreage() {
        return acreage;
    }

    public void setAcreage(BigDecimal acreage) {
        this.acreage = acreage;
    }

    public BigDecimal getRentAmt() {
        return rentAmt;
    }

    public void setRentAmt(BigDecimal rentAmt) {
        this.rentAmt = rentAmt;
    }

    public String getHouseDes() {
        return houseDes;
    }

    public void setHouseDes(String houseDes) {
        this.houseDes = houseDes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getRoomAcreage() {
        return roomAcreage;
    }

    public void setRoomAcreage(BigDecimal roomAcreage) {
        this.roomAcreage = roomAcreage;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
