package com.apass.zufang.domain.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
	 * 需排除的目标房源id
	 */
	private Long targetHouseId;
	/**
	 * 房屋所在省
	 */
	private String province;
	/**
	 * 房源所在城市
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
	private Byte status;
	/**
	 * 1:普通住宅...
	 */
	private Byte houseType;
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
	 * 房源小区名称
	 */
	private String communityName;
	
	/**
	 * 房屋 朝向， 1:东：2:南....
	 */
	private Byte chaoxiang;
	/**
	 * 房屋 朝向， 1:东：2:南....
	 */
	private String chaoxiangStr;
	
	/**
	 * 房屋装修类型:1:豪华装修...
	 */
	private Byte zhuangxiu;
	/**
	 * 房屋第几层
	 */
	private Integer floor;
	/**
	 * 房屋总共的楼层数
	 */
	private Integer totalFloor;
	/**
	 * 房屋     面积
	 */
	private BigDecimal acreage;
	/**
	 * 房屋     室 
	 */
	private Integer room;
	/**
	 * 房屋      厅
	 */
	private Integer hall;
	/**
	 * 房屋      卫
	 */
	private Integer wei;
	/**
	 * 房屋 租赁类型 1:整租；2:合租
	 */
	private Byte rentType;
	/**
	 * 房屋 租赁类型 1:整租；2:合租
	 */
	private String rentTypeStr;
	/**
	 * 房屋租金类型
	 */
	private Byte zujinType;
	/**
	 * 房屋租金类型Str
	 */
	private String zujinTypeStr;
	/**
	 * 租金/月
	 */
	private BigDecimal rentAmt;
	/**
	 * 房屋家具配置 list
	 */
	private List<String> houseConfigList;
	/**
	 * 房源图片url  list
	 */
	private List<String> imgUrlList;
	
	/**
	 * 上架时间
	 */
	private String listTime;
	
	/**
	 * 下架时间
	 */
	private String delistTime;
	
	/***********************************查询所用字段*************************************/
	/**
	 * 最小租金/月
	 */
	private BigDecimal minRentAmt;
	/**
	 * 最大租金/月
	 */
	private BigDecimal maxRentAmt;
	
	/**
	 * 排序字段
	 */
	private Integer sortField;
	
	/**
	 * 浏览量
	 */
	private Long pageView;
	
	/**
	 * 管家联系方式
	 */
	private String houseKeepertel;
	
	/**
	 * 几户合租
	 */
	private String totalDoors;
	
	/**
	 * 朝向
	 */
	private Long hezuChaoxiang;
	
	/**
	 * 出租介绍
	 */
	private Long hezuResource;
	
	/**
	 * 出租面积
	 */
	private BigDecimal roomAcreage;
	
	/**
	 * 房源city
	 */
	private String cityH;
	
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

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public Byte getChaoxiang() {
		return chaoxiang;
	}

	public void setChaoxiang(Byte chaoxiang) {
		this.chaoxiang = chaoxiang;
	}

	public String getChaoxiangStr() {
		return chaoxiangStr;
	}

	public void setChaoxiangStr(String chaoxiangStr) {
		this.chaoxiangStr = chaoxiangStr;
	}

	public String getRentTypeStr() {
		return rentTypeStr;
	}

	public void setRentTypeStr(String rentTypeStr) {
		this.rentTypeStr = rentTypeStr;
	}

	public Long getApartmentId() {
		return apartmentId;
	}

	public void setApartmentId(Long apartmentId) {
		this.apartmentId = apartmentId;
	}

	public List<String> getImgUrlList() {
		return imgUrlList;
	}

	public void setImgUrlList(List<String> imgUrlList) {
		this.imgUrlList = imgUrlList;
	}

	public Long getTargetHouseId() {
		return targetHouseId;
	}

	public void setTargetHouseId(Long targetHouseId) {
		this.targetHouseId = targetHouseId;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Byte getHouseType() {
		return houseType;
	}

	public void setHouseType(Byte houseType) {
		this.houseType = houseType;
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

	public Byte getRentType() {
		return rentType;
	}

	public void setRentType(Byte rentType) {
		this.rentType = rentType;
	}

	public Byte getZujinType() {
		return zujinType;
	}

	public void setZujinType(Byte zujinType) {
		this.zujinType = zujinType;
	}

	public BigDecimal getRentAmt() {
		return rentAmt;
	}

	public void setRentAmt(BigDecimal rentAmt) {
		this.rentAmt = rentAmt;
	}

	public BigDecimal getMinRentAmt() {
		return minRentAmt;
	}

	public void setMinRentAmt(BigDecimal minRentAmt) {
		this.minRentAmt = minRentAmt;
	}

	public BigDecimal getMaxRentAmt() {
		return maxRentAmt;
	}

	public void setMaxRentAmt(BigDecimal maxRentAmt) {
		this.maxRentAmt = maxRentAmt;
	}

	public Byte getZhuangxiu() {
		return zhuangxiu;
	}

	public void setZhuangxiu(Byte zhuangxiu) {
		this.zhuangxiu = zhuangxiu;
	}

	public BigDecimal getAcreage() {
		return acreage;
	}

	public void setAcreage(BigDecimal acreage) {
		this.acreage = acreage;
	}

	public List<String> getHouseConfigList() {
		return houseConfigList;
	}

	public void setHouseConfigList(List<String> houseConfigList) {
		this.houseConfigList = houseConfigList;
	}

	public Integer getSortField() {
		return sortField;
	}

	public void setSortField(Integer sortField) {
		this.sortField = sortField;
	}

	public String getListTime() {
		return listTime;
	}

	public void setListTime(String listTime) {
		this.listTime = listTime;
	}

	public String getDelistTime() {
		return delistTime;
	}

	public void setDelistTime(String delistTime) {
		this.delistTime = delistTime;
	}

	public Integer getWei() {
		return wei;
	}

	public void setWei(Integer wei) {
		this.wei = wei;
	}

	public String getZujinTypeStr() {
		return zujinTypeStr;
	}

	public void setZujinTypeStr(String zujinTypeStr) {
		this.zujinTypeStr = zujinTypeStr;
	}

	public Long getPageView() {
		return pageView;
	}

	public void setPageView(Long pageView) {
		this.pageView = pageView;
	}

	public String getHouseKeepertel() {
		return houseKeepertel;
	}

	public void setHouseKeepertel(String houseKeepertel) {
		this.houseKeepertel = houseKeepertel;
	}

	public String getTotalDoors() {
		return totalDoors;
	}

	public void setTotalDoors(String totalDoors) {
		this.totalDoors = totalDoors;
	}

	public Long getHezuChaoxiang() {
		return hezuChaoxiang;
	}

	public void setHezuChaoxiang(Long hezuChaoxiang) {
		this.hezuChaoxiang = hezuChaoxiang;
	}

	public Long getHezuResource() {
		return hezuResource;
	}

	public void setHezuResource(Long hezuResource) {
		this.hezuResource = hezuResource;
	}

	public BigDecimal getRoomAcreage() {
		return roomAcreage;
	}

	public void setRoomAcreage(BigDecimal roomAcreage) {
		this.roomAcreage = roomAcreage;
	}

	public String getCityH() {
		return cityH;
	}

	public void setCityH(String cityH) {
		this.cityH = cityH;
	}

}