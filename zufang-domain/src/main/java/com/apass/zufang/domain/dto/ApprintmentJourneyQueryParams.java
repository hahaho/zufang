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
	private String reserveDateFloor;//预约时间下限
	private String reserveDateCeiling;//预约时间上限
	private String apartmentName;//公寓名称
//	rent_amt
	private String communityName;//小区名称
	private String houseTitle;//房源标题(名称)
	private String houseCode;//房源编号
	private String houseCity;//房源城市
	private String houseDistrict;//房源区域
	private String houseStreet;//房源街道
	private String houseStatus;//房源状态
	private String houseAcreageFloor;//房源面积下限
	private String houseAcreageCeiling;//房源面积上限
	private String houseKitchenFalg;//房源独立厨房
	private String houseToiletFalg;//房源独立卫生间
	private String houseCreatedFloor;//房源创建时间下限
	private String houseCreatedCeiling;//房源创建时间上限
	public String getApartmentName() {
		return apartmentName;
	}
	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
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
	public String getHouseStatus() {
		return houseStatus;
	}
	public void setHouseStatus(String houseStatus) {
		this.houseStatus = houseStatus;
	}
	public String getHouseAcreageFloor() {
		return houseAcreageFloor;
	}
	public void setHouseAcreageFloor(String houseAcreageFloor) {
		this.houseAcreageFloor = houseAcreageFloor;
	}
	public String getHouseAcreageCeiling() {
		return houseAcreageCeiling;
	}
	public void setHouseAcreageCeiling(String houseAcreageCeiling) {
		this.houseAcreageCeiling = houseAcreageCeiling;
	}
	public String getHouseKitchenFalg() {
		return houseKitchenFalg;
	}
	public void setHouseKitchenFalg(String houseKitchenFalg) {
		this.houseKitchenFalg = houseKitchenFalg;
	}
	public String getHouseToiletFalg() {
		return houseToiletFalg;
	}
	public void setHouseToiletFalg(String houseToiletFalg) {
		this.houseToiletFalg = houseToiletFalg;
	}
	public String getHouseCreatedFloor() {
		return houseCreatedFloor;
	}
	public void setHouseCreatedFloor(String houseCreatedFloor) {
		this.houseCreatedFloor = houseCreatedFloor;
	}
	public String getHouseCreatedCeiling() {
		return houseCreatedCeiling;
	}
	public void setHouseCreatedCeiling(String houseCreatedCeiling) {
		this.houseCreatedCeiling = houseCreatedCeiling;
	}
}