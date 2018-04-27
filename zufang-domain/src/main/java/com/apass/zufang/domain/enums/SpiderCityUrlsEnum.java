package com.apass.zufang.domain.enums;

/**
 * Created by DELL on 2018/4/27.
 * 爬虫，不同城市对应不同省份，city
 */
public enum  SpiderCityUrlsEnum {
    MG_SH("上海","上海","www.mogoroom.com"),
    MG_BJ("北京","北京","bj.mogoroom.com"),
    MG_SZ("广东","深圳市","sz.mogoroom.com"),
    MG_HZ("浙江","杭州市","hz.mogoroom.com"),
    MG_NJ("江苏","南京市","nj.mogoroom.com"),
    MG_CD("四川","成都市","cd.mogoroom.com"),
    MG_XA("陕西","西安市","xa.mogoroom.com"),
    MG_CQ("重庆","重庆","cq.mogoroom.com"),
    MG_GZ("广东","广州市","gz.mogoroom.com"),
    MG_TJ("天津","天津","tj.mogoroom.com"),


    ;



    private String province;
    private String city;
    private String url;

    private SpiderCityUrlsEnum(String province,String city,String url){
        this.province = province;
        this.city = city;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public static SpiderCityUrlsEnum getEnum(String url){
        for(SpiderCityUrlsEnum e : values()){
            if(url.contains(e.getUrl())){
                return e;
            }
        }
        return null;
    }

}
