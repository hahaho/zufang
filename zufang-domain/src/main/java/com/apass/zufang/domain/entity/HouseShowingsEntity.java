package com.apass.zufang.domain.entity;

import java.util.Date;

public class HouseShowingsEntity {
	
	  private String  houseId;		//'房源主键',
	  private String  houseCode ;//'编码',
	  private String  apartmentId;//'公寓id',
	  private String  rentType ;// '1:整租；2:合租',
	  private String  communityName;// '小区名称',
	  private String  acreage;//'面积',
	  private String  room ;//'室',
	  private String  hall; // '厅',
	  private String  wei ;// '卫',
	  private String  floor ;// '第几层',
	  private String  totalFloor;// '总共的楼层数',
	  private String  liftType; // '1:有；2:无',
	  private String  rentAmt; // '租金/月',
	  private String  zujinType; //'1:押一付三;2:押一付一；.....',
	  private String  houseStatus; //'1:待上架；2：上架；3：下架；4：删除；5：修改',
	  private String  title; // '房源标题',
	  private String  description ;// '房源描述',
	  private String  isDelete;//'是否删除(00 默认值,  01 删除)',
	  private String  housekeeperTel;// '管家联系方式',
	  private String  totalDoors; // '几户合租',
	  private String  roomAcreage ;// '出租面积',
	  private String  idImg; // 图片id
	  private String  url; // '图片url',
	  private String  type;//'类型,0普通房源照片,1热门房源照片',
	  private Date 	  reserveDate;//预约时间
	  private String  detailAddr;
	  
	  
	public Date getReserveDate() {
		return reserveDate;
	}
	public void setReserveDate(Date reserveDate) {
		this.reserveDate = reserveDate;
	}
	public String getDetailAddr() {
		return detailAddr;
	}
	public void setDetailAddr(String detailAddr) {
		this.detailAddr = detailAddr;
	}
	public String getHouseId() {
		return houseId;
	}
	public void setHouseId(String houseId) {
		this.houseId = houseId;
	}
	public String getHouseCode() {
		return houseCode;
	}
	public void setHouseCode(String houseCode) {
		this.houseCode = houseCode;
	}
	public String getApartmentId() {
		return apartmentId;
	}
	public void setApartmentId(String apartmentId) {
		this.apartmentId = apartmentId;
	}
	public String getRentType() {
		return rentType;
	}
	public void setRentType(String rentType) {
		this.rentType = rentType;
	}
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	public String getAcreage() {
		return acreage;
	}
	public void setAcreage(String acreage) {
		this.acreage = acreage;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getHall() {
		return hall;
	}
	public void setHall(String hall) {
		this.hall = hall;
	}
	public String getWei() {
		return wei;
	}
	public void setWei(String wei) {
		this.wei = wei;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getTotalFloor() {
		return totalFloor;
	}
	public void setTotalFloor(String totalFloor) {
		this.totalFloor = totalFloor;
	}
	public String getLiftType() {
		return liftType;
	}
	public void setLiftType(String liftType) {
		this.liftType = liftType;
	}
	public String getRentAmt() {
		return rentAmt;
	}
	public void setRentAmt(String rentAmt) {
		this.rentAmt = rentAmt;
	}
	public String getZujinType() {
		return zujinType;
	}
	public void setZujinType(String zujinType) {
		this.zujinType = zujinType;
	}
	public String getHouseStatus() {
		return houseStatus;
	}
	public void setHouseStatus(String houseStatus) {
		this.houseStatus = houseStatus;
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
	public String getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
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
	public String getRoomAcreage() {
		return roomAcreage;
	}
	public void setRoomAcreage(String roomAcreage) {
		this.roomAcreage = roomAcreage;
	}
	public String getIdImg() {
		return idImg;
	}
	public void setIdImg(String idImg) {
		this.idImg = idImg;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	  
	  

}
