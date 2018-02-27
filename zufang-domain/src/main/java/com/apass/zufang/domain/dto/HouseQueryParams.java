package com.apass.zufang.domain.dto;
import java.util.List;

import com.apass.zufang.common.model.QueryParams;

import java.util.Date;

/**
 * 
 * @author pyc
 * @email  E-mail: pengyingchao@apass.cn
 * @version V2.0
 * @createTime：2018年2月8日 下午1:41:42 
 * @description 房屋管理查询类
 */
public class HouseQueryParams extends QueryParams{
	private byte houseType;//房源是否热门
	private Long houseId;//房源ID
	private String apartmentName;//公寓名称
	private String houseTitle;//房源名称
	private String houseCode;//房源编码
	private String houseArea;//公寓所在区
	private String isDelete;//房源是否删除
	private List<Integer> status;//状态
	
	/**
	 *
	 */
	private Long apartmentId;

	private Byte type;

	private String title;

	/**
	 * 是否查询上架时间
	 * @return
     */
	private String listTimeStr;

	private Date listTime;

	public String getListTimeStr() {
		return listTimeStr;
	}

	public void setListTimeStr(String listTimeStr) {
		this.listTimeStr = listTimeStr;
	}

	public byte getHouseType() {
		return houseType;
	}
	public void setHouseType(byte houseType) {
		this.houseType = houseType;
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
	public String getHouseTitle() {
		return houseTitle;
	}
	public void setHouseTitle(String houseTitle) {
		this.houseTitle = houseTitle;
	}
	public String getHouseCode() {
		return houseCode;
	}
	public void setHouseCode(String houseCode) {
		this.houseCode = houseCode;
	}
	public String getHouseArea() {
		return houseArea;
	}
	public void setHouseArea(String houseArea) {
		this.houseArea = houseArea;
	}
	public String getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}
	public List<Integer> getStatus() {
		return status;
	}
	public void setStatus(List<Integer> status) {
		this.status = status;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getListTime() {
		return listTime;
	}

	public void setListTime(Date listTime) {
		this.listTime = listTime;
	}
}