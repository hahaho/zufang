package com.apass.zufang.domain.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * 
 * @author pyc
 * @email  E-mail: pengyingchao@apass.cn
 * @version V2.0
 * @createTime：2018年2月8日 下午1:41:42 
 * @description 房屋管理类
 */
public class HouseVo{

	private Long id;
	
    private String code;//编码（公寓编号【省市区地区码后两位+两位自增数字】后两位+5位随机数）

    private Long apartmentId;//所属公寓
    
    private String housekeeperTel;

    private Integer sortNo;//排序字段

    private Byte rentType;//1:整租；2:合租

    private String communityName;//小区名称

    private String province;//省

    private String city;//市

    private String district;//区域

    private BigDecimal acreage;//面积

    private String street;//街道

    private String detailAddr;//详细地址

    private Integer room;//室

    private Integer hall;//厅

    private Integer wei;//卫

    private Integer floor;//第几层

    private Integer totalFloor;//总共的楼层数

    private Byte liftType;//1:有；2:无（电梯情况）

    private BigDecimal rentAmt;//租金/月

    private Byte zujinType;//1:押一付三;2:押一付一；.....

    private Byte chaoxiang;//朝向， 1:东：2:南....

    private Byte zhuangxiu;//装修情况:1:豪华装修...
    
    private String title;//房源标题

    private String description;//房源描述
    
    private Date createdTime;

    private Date updatedTime;

    private String createdUser;

    private String updatedUser;
    
    private List<String> pictures;//图片
    
    private List<String> configs;//房屋配置
    
    private String isDelete;//房源是否删除
    
	private String apartmentName;//公寓名称
	
	private String houseCode;//房源编码
	
	private String locationId;//地址Id
	
	private String totalDoors;//几户合租

    private Byte hezuChaoxiang;//朝向

    private Byte hezuResource;//出租介绍

	private String longitude;//经度
	
	private String latitude;//维度

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
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
	
	public String getHousekeeperTel() {
		return housekeeperTel;
	}

	public void setHousekeeperTel(String housekeeperTel) {
		this.housekeeperTel = housekeeperTel;
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

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public BigDecimal getAcreage() {
		return acreage;
	}

	public void setAcreage(BigDecimal acreage) {
		this.acreage = acreage;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
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

	public List<String> getPictures() {
		return pictures;
	}

	public void setPictures(List<String> pictures) {
		this.pictures = pictures;
	}

	public List<String> getConfigs() {
		return configs;
	}

	public void setConfigs(List<String> configs) {
		this.configs = configs;
	}
	
	public String getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}
	
	public String getApartmentName() {
		return apartmentName;
	}
	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}
	public String getHouseCode() {
		return houseCode;
	}
	public void setHouseCode(String houseCode) {
		this.houseCode = houseCode;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
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
	
}
