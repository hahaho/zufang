package com.apass.zufang.domain.vo;

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
     * 面积
     */
    private BigDecimal acreage;

    /**
     * 租金
     */
    private BigDecimal rentAmt;
    /**
     * 整合室、厅、卫字段，显示：一室一厅一卫-55㎡
     */
    private String houseDes;

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
}
