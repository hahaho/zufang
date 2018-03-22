package com.apass.zufang.domain.dto;
import com.apass.zufang.common.model.QueryParams;
/**
 * 看房行程功能查询工具类
 * @author Administrator
 *
 */
public class ApprintmentJourneyQueryParams extends QueryParams{
	private String name;//姓名
	private String telphone;//电话
	private String createdFloor;//预约时间下限
	private String createdCeiling;//预约时间上限
	private String apartmentName;//公寓名称
	private String rentAmtFloor;//租金下限
	private String rentAmtCeiling;//租金上限
	private String houseCode;//房源编号
	private String houseProvince;//房源省
	private String houseCity;//房源城市
	private String houseDistrict;//房源区域
	private String houseStreet;//房源街道
	private String reserveDateFloor;//看房时间下限
	private String reserveDateCeiling;//看房时间上限
	
	private String houseId;//houseId
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTelphone() {
		return telphone;
	}
	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}
	public String getCreatedFloor() {
		return createdFloor;
	}
	public void setCreatedFloor(String createdFloor) {
		this.createdFloor = createdFloor;
	}
	public String getCreatedCeiling() {
		return createdCeiling;
	}
	public void setCreatedCeiling(String createdCeiling) {
		this.createdCeiling = createdCeiling;
	}
	public String getApartmentName() {
		return apartmentName;
	}
	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}
	public String getRentAmtFloor() {
		return rentAmtFloor;
	}
	public void setRentAmtFloor(String rentAmtFloor) {
		this.rentAmtFloor = rentAmtFloor;
	}
	public String getRentAmtCeiling() {
		return rentAmtCeiling;
	}
	public void setRentAmtCeiling(String rentAmtCeiling) {
		this.rentAmtCeiling = rentAmtCeiling;
	}
	public String getHouseCode() {
		return houseCode;
	}
	public void setHouseCode(String houseCode) {
		this.houseCode = houseCode;
	}
	public String getHouseProvince() {
		return houseProvince;
	}
	public void setHouseProvince(String houseProvince) {
		this.houseProvince = houseProvince;
	}
	public String getHouseCity() {
		return houseCity;
	}
	public void setHouseCity(String houseCity) {
		this.houseCity = houseCity;
	}
	public String getHouseDistrict() {
		return houseDistrict;
	}
	public void setHouseDistrict(String houseDistrict) {
		this.houseDistrict = houseDistrict;
	}
	public String getHouseStreet() {
		return houseStreet;
	}
	public void setHouseStreet(String houseStreet) {
		this.houseStreet = houseStreet;
	}
	public String getReserveDateFloor() {
		return reserveDateFloor;
	}
	public void setReserveDateFloor(String reserveDateFloor) {
		this.reserveDateFloor = reserveDateFloor;
	}
	public String getReserveDateCeiling() {
		return reserveDateCeiling;
	}
	public void setReserveDateCeiling(String reserveDateCeiling) {
		this.reserveDateCeiling = reserveDateCeiling;
	}
	public String getHouseId() {
		return houseId;
	}
	public void setHouseId(String houseId) {
		this.houseId = houseId;
	}
}