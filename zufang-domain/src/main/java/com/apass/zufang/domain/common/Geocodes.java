package com.apass.zufang.domain.common;

/**
 * 具体 位置信息
 * @author zwd
 *
 */
public class Geocodes {  
    // 结构化地址信息  
    private String formatted_address;  
    // 所在省  
    private String province;  
    // 城市  
    private String city;  
    // 城市编码  
    private String citycode;  
    // 地址所在的区  
    private String district;  
    // 区域编码  
    private String adcode;  
    // 坐标点  
    private String location;  
    // 匹配级别  
    private String level;  
    
    // 地址所在的乡镇  
//  private String township;  
//  // 街道  
//  private String street;  
//  // 门牌  
//  private String number;  
  
    public String getFormatted_address() {  
        return formatted_address;  
    }  
  
    public void setFormatted_address(String formatted_address) {  
        this.formatted_address = formatted_address;  
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
  
    public String getCitycode() {  
        return citycode;  
    }  
  
    public void setCitycode(String citycode) {  
        this.citycode = citycode;  
    }  
  
    public String getDistrict() {  
        return district;  
    }  
  
    public void setDistrict(String district) {  
        this.district = district;  
    }  
  
    public String getAdcode() {  
        return adcode;  
    }  
  
    public void setAdcode(String adcode) {  
        this.adcode = adcode;  
    }  
  
    public String getLocation() {  
        return location;  
    }  
  
    public void setLocation(String location) {  
        this.location = location;  
    }  
  
    public String getLevel() {  
        return level;  
    }  
  
    public void setLevel(String level) {  
        this.level = level;  
    }  
  
}  