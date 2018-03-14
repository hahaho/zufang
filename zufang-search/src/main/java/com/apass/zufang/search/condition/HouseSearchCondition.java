package com.apass.zufang.search.condition;

import com.apass.zufang.search.enums.SortMode;
import com.apass.gfb.framework.mybatis.page.Pagination;

import java.math.BigDecimal;

/**
 * Created by xianzhi.wang on 2017/5/23.
 */
public class HouseSearchCondition {

    /**
     * 小区名称模糊
     */
    private String communityName;
    /**
     * 房源标题模糊
     */
    private String houseTitle;

    /**
     * 区域模糊
     */
    private String detailAddr;

    /**
     * 1:整租；2:合租
     */
    private Byte rentType;

    /**
     * 公寓名称
     */
    private String apartmentName;
    /**
     * 价格
     */
    private BigDecimal rentAmt;

    /**
     * 室
     */
    private Integer room;

    /**
     * 电视，空调等配置信息
     * @return
     */
    private String configName;

    /**
     * 价格区间标记:见PriceRangeEnum
     * @return
     */
    private Integer priceFlag;

    /**
     * 排序字段
     */
    private SortMode sortMode;
    /**
     * 定位或传入地址
     */
    private String city;

    /**
     * 开始条数
     */
    private Integer offset = (Pagination.DEFAULT_PAGE_NUM - 1) * Pagination.DEFAULT_PAGE_SIZE;

    /**
     * 多少条
     */
    private Integer pageSize = Pagination.DEFAULT_PAGE_SIZE;

    public Integer getPriceFlag() {
        return priceFlag;
    }

    public void setPriceFlag(Integer priceFlag) {
        this.priceFlag = priceFlag;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public SortMode getSortMode() {
        return sortMode;
    }

    public void setSortMode(SortMode sortMode) {
        this.sortMode = sortMode;
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

    public String getDetailAddr() {
        return detailAddr;
    }

    public void setDetailAddr(String detailAddr) {
        this.detailAddr = detailAddr;
    }

    public Byte getRentType() {
        return rentType;
    }

    public void setRentType(Byte rentType) {
        this.rentType = rentType;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public BigDecimal getRentAmt() {
        return rentAmt;
    }

    public void setRentAmt(BigDecimal rentAmt) {
        this.rentAmt = rentAmt;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
