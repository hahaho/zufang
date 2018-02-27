package com.apass.zufang.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

public class House {
    private Long id;

    private String code;

    private Long apartmentId;

    private Byte type;

    private Integer sortNo;

    private Byte rentType;

    private String communityName;

    private BigDecimal acreage;

    private Integer room;

    private Integer hall;

    private Integer wei;

    private Integer floor;

    private Integer totalFloor;

    private Byte liftType;

    private BigDecimal rentAmt;

    private Byte zujinType;

    private Byte chaoxiang;

    private Byte zhuangxiu;

    private Byte status;

    private Date listTime;

    private Date delistTime;

    private String title;

    private String description;

    private Date createdTime;

    private Date updatedTime;

    private String createdUser;

    private String updatedUser;

    private String isDelete;

    private Long pageView;

    private String housekeeperTel;

    private String totalDoors;

    private Byte hezuChaoxiang;

    private Byte hezuResource;

    private BigDecimal roomAcreage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Long apartmentId) {
        this.apartmentId = apartmentId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public Byte getRentType() {
        return rentType;
    }

    public void setRentType(Byte rentType) {
        this.rentType = rentType;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public BigDecimal getAcreage() {
        return acreage;
    }

    public void setAcreage(BigDecimal acreage) {
        this.acreage = acreage;
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

    public Integer getTotalFloor() {
        return totalFloor;
    }

    public void setTotalFloor(Integer totalFloor) {
        this.totalFloor = totalFloor;
    }

    public Byte getLiftType() {
        return liftType;
    }

    public void setLiftType(Byte liftType) {
        this.liftType = liftType;
    }

    public BigDecimal getRentAmt() {
        return rentAmt;
    }

    public void setRentAmt(BigDecimal rentAmt) {
        this.rentAmt = rentAmt;
    }

    public Byte getZujinType() {
        return zujinType;
    }

    public void setZujinType(Byte zujinType) {
        this.zujinType = zujinType;
    }

    public Byte getChaoxiang() {
        return chaoxiang;
    }

    public void setChaoxiang(Byte chaoxiang) {
        this.chaoxiang = chaoxiang;
    }

    public Byte getZhuangxiu() {
        return zhuangxiu;
    }

    public void setZhuangxiu(Byte zhuangxiu) {
        this.zhuangxiu = zhuangxiu;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getListTime() {
        return listTime;
    }

    public void setListTime(Date listTime) {
        this.listTime = listTime;
    }

    public Date getDelistTime() {
        return delistTime;
    }

    public void setDelistTime(Date delistTime) {
        this.delistTime = delistTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public Long getPageView() {
        return pageView;
    }

    public void setPageView(Long pageView) {
        this.pageView = pageView;
    }

    public String getHousekeeperTel() {
        return housekeeperTel;
    }

    public void setHousekeeperTel(String housekeeperTel) {
        this.housekeeperTel = housekeeperTel;
    }

    public String getTotalDoors() {
        return totalDoors;
    }

    public void setTotalDoors(String totalDoors) {
        this.totalDoors = totalDoors;
    }

    public Byte getHezuChaoxiang() {
        return hezuChaoxiang;
    }

    public void setHezuChaoxiang(Byte hezuChaoxiang) {
        this.hezuChaoxiang = hezuChaoxiang;
    }

    public Byte getHezuResource() {
        return hezuResource;
    }

    public void setHezuResource(Byte hezuResource) {
        this.hezuResource = hezuResource;
    }

    public BigDecimal getRoomAcreage() {
        return roomAcreage;
    }

    public void setRoomAcreage(BigDecimal roomAcreage) {
        this.roomAcreage = roomAcreage;
    }
}