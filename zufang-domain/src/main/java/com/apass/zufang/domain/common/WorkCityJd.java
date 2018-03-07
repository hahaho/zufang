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

    private Date createDate;
    private Date updateDate;
    
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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