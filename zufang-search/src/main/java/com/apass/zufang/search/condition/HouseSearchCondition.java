package com.apass.zufang.search.condition;

import com.apass.zufang.search.enums.SortMode;
import com.apass.gfb.framework.mybatis.page.Pagination;

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
     * 排序字段
     */
    private SortMode sortMode;

    /**
     * 开始条数
     */
    private Integer offset = (Pagination.DEFAULT_PAGE_NUM - 1) * Pagination.DEFAULT_PAGE_SIZE;

    /**
     * 多少条
     */
    private Integer pageSize = Pagination.DEFAULT_PAGE_SIZE;

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
}
