package com.apass.zufang.domain.common;

import java.util.Date;
import java.util.List;

public class WorkCityJd {
    /**
     * ID
     */
    private Long   id;
    /**
     * 编码
     */
    private String code;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 地区
     */
    private String district;
    /**
     * 城镇
     */
    private String  towns;
    /**
     * 父节点
     */
    private Long   parent;
    /**
     * 首字母
     */
    private String prefix;
  
    private List<WorkCityJd> resultList;

    private Date createdTime;
    private Date updatedTime;
    
    private String level;
    

	public List<WorkCityJd> getResultList() {
		return resultList;
	}

	public void setResultList(List<WorkCityJd> resultList) {
		this.resultList = resultList;
	}

	public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getTowns() {
        return towns;
    }

    public void setTowns(String towns) {
        this.towns = towns;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}