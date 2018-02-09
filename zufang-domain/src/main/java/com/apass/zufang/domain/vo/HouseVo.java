package com.apass.zufang.domain.vo;

import java.math.BigDecimal;
import com.apass.zufang.common.model.QueryParams;
/**
 * 
 * @author pyc
 * @email  E-mail: pengyingchao@apass.cn
 * @version V2.0
 * @createTime：2018年2月8日 下午1:41:42 
 * @description 房屋管理类
 */
public class HouseVo extends QueryParams{

	
    private String code;//编码（公寓编号【省市区地区码后两位+两位自增数字】后两位+5位随机数）

    private Long apartmentId;//所属公寓

    private Integer sortNo;//排序字段

    private Byte rentType;//1:整租；2:合租

    private String communityName;//小区名称

    private String province;//省

    private String city;//市

    private String area;//区域

    private BigDecimal acreage;//面积

    private String district;//街道

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

    private Byte houseType;//1:普通住宅...

    private String title;//房源标题

    private String description;//房源描述
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

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
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

	public Byte getHouseType() {
		return houseType;
	}

	public void setHouseType(Byte houseType) {
		this.houseType = houseType;
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
	private String isDelete;//房源是否删除
	private Long houseId;//房源ID
	private String apartmentName;//公寓名称
	private String houseCode;//房源编码
	public String getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}
	public Long getHouseId() {
		return houseId;
	}
	public void setHouseId(Long houseId) {
		this.houseId = houseId;
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
}