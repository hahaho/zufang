package com.apass.zufang.domain.vo;

import java.math.BigDecimal;

public class HouseBagVo {
	
	private Long apartId;//公寓Id
	
	private String apartName;//公寓名称
	
	private Long houseId;//房源Id
	
	private String houseCode;//房源编号
	
	private String title;//房源名称
	
	private Integer status;//状态
	
	private String communityName;//小区名称
	
	private Integer room;//室
	
	private Integer hall;//厅
	
	private Integer wei;//卫
	
	private BigDecimal rentAmt;//租金
	
	private Integer zujinType;//租金支付方式

	public Long getApartId() {
		return apartId;
	}

	public void setApartId(Long apartId) {
		this.apartId = apartId;
	}

	public String getApartName() {
		return apartName;
	}

	public void setApartName(String apartName) {
		this.apartName = apartName;
	}

	public Long getHouseId() {
		return houseId;
	}

	public void setHouseId(Long houseId) {
		this.houseId = houseId;
	}

	public String getHouseCode() {
		return houseCode;
	}

	public void setHouseCode(String houseCode) {
		this.houseCode = houseCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
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

	public BigDecimal getRentAmt() {
		return rentAmt;
	}

	public void setRentAmt(BigDecimal rentAmt) {
		this.rentAmt = rentAmt;
	}

	public Integer getZujinType() {
		return zujinType;
	}

	public void setZujinType(Integer zujinType) {
		this.zujinType = zujinType;
	}
}
