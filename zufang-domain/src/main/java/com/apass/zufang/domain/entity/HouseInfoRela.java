package com.apass.zufang.domain.entity;

import java.util.Date;

public class HouseInfoRela {
	
	/**
	 * 公寓id
	 */
	private Long apartmentId;
	/**
	 * 房屋id
	 */
	private Long houseId;
	/**
	 * 房屋所在省
	 */
	private String province;
	/**
	 * 房屋所在城市
	 */
	private String city;
	/**
	 * 房屋所在区
	 */
	private String district;
	/**
	 * 房屋所在街道
	 */
	private String street;
	/**
	 * 房屋详细地址
	 */
	private String detailAddr;
	/**
	 * 房屋所在经度
	 */
	private double longitude;
	/**
	 * 房屋所在纬度
	 */
	private double latitude;
	/**
	 * 房屋现状1:待上架；2：上架；3：下架；4：删除
	 */
	private String status;
	/**
	 * 1:普通住宅...
	 */
	private String houseType;
	/**
	 * 房屋创建时间
	 */
	private Date houseCreatedTime;
	/**
	 * 房屋所在的公寓名称
	 */
	private String companyName;
	/**
	 * 房源标题
	 */
	private String title;
	/**
	 * 房源描述
	 */
	private String description;
	/**
	 * 房源小区名称
	 */
	private String communityName;
	
	/**
	 * 房屋 朝向， 1:东：2:南....
	 */
	private String chaoxiang;
	
	/**
	 * 房屋装修情况:1:豪华装修...
	 */
	private String zhuangxiu;
	/**
	 * 房屋第几层
	 */
	private String floor;
	/**
	 * 房屋总共的楼层数
	 */
	private String totalFloor;
	/**
	 * 房屋     面积
	 */
	private String acreage;
	/**
	 * 房屋     室 
	 */
	private String room;
	/**
	 * 房屋      厅
	 */
	private String hall;
	/**
	 * 房屋 1:整租；2:合租
	 */
	private String rentType;
	/**
	 * 房屋
	 */
	private String zujinType;
	/**
	 * 租金/月
	 */
	private String rentAmt;
	/**
	 * 房屋家具配置 
	 */
	private String houseConfiguration;
	
	/***********************************查询所用字段*************************************/
	/**
	 * 最小租金/月
	 */
	private String minRentAmt;
	/**
	 * 最大租金/月
	 */
	private String maxRentAmt;

	public Long getHouseId() {
		return houseId;
	}

	public void setHouseId(Long houseId) {
		this.houseId = houseId;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getDetailAddr() {
		return detailAddr;
	}

	public void setDetailAddr(String detailAddr) {
		this.detailAddr = detailAddr;
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

	public Date getHouseCreatedTime() {
		return houseCreatedTime;
	}

	public void setHouseCreatedTime(Date houseCreatedTime) {
		this.houseCreatedTime = houseCreatedTime;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public String getChaoxiang() {
		return chaoxiang;
	}

	public void setChaoxiang(String chaoxiang) {
		this.chaoxiang = chaoxiang;
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

	public String getRentType() {
		return rentType;
	}

	public void setRentType(String rentType) {
		this.rentType = rentType;
	}

	public String getZujinType() {
		return zujinType;
	}

	public void setZujinType(String zujinType) {
		this.zujinType = zujinType;
	}

	public String getRentAmt() {
		return rentAmt;
	}

	public void setRentAmt(String rentAmt) {
		this.rentAmt = rentAmt;
	}

	public String getHouseConfiguration() {
		return houseConfiguration;
	}

	public void setHouseConfiguration(String houseConfiguration) {
		this.houseConfiguration = houseConfiguration;
	}

	public Long getApartmentId() {
		return apartmentId;
	}

	public void setApartmentId(Long apartmentId) {
		this.apartmentId = apartmentId;
	}

	public String getZhuangxiu() {
		return zhuangxiu;
	}

	public void setZhuangxiu(String zhuangxiu) {
		this.zhuangxiu = zhuangxiu;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHouseType() {
		return houseType;
	}

	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}

	public String getMinRentAmt() {
		return minRentAmt;
	}

	public void setMinRentAmt(String minRentAmt) {
		this.minRentAmt = minRentAmt;
	}

	public String getMaxRentAmt() {
		return maxRentAmt;
	}

	public void setMaxRentAmt(String maxRentAmt) {
		this.maxRentAmt = maxRentAmt;
	}

}