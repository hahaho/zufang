package com.apass.zufang.domain.common;
/**
 * 具体详细地址
 * @author zwd
 *
 */
public class CoordinateAddress {
	
	private String name;//名称
	
	private String id;
	
	private String admCode;//邮政编码
	
	private String province;//省
	
	private String city;//市
	
	private String area;//区
	
	private String addr;//详细描述
	
	private double[] nearestPoint;//经纬度
	
	private double  distance;//距离

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAdmCode() {
		return admCode;
	}

	public void setAdmCode(String admCode) {
		this.admCode = admCode;
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

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public double[] getNearestPoint() {
		return nearestPoint;
	}

	public void setNearestPoint(double[] nearestPoint) {
		this.nearestPoint = nearestPoint;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	
}
