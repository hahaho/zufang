package com.apass.zufang.search.enums;

import java.io.Serializable;

/**
 * type: enum
 * 排序规则枚举类
 *
 * @author xianzhi.wang
 * @date 2017/8/15
 * @see
 * @since JDK 1.8
 */
public enum SortMode implements Serializable {
    ID_ASC("id", false),
    ID_DESC("id", true),

    /**
     * 浏览量
     */
    PAGEVIEW_ASC("pageView", false),
    PAGEVIEW_DESC("pageView", true),
    /**
     * 上架时间
     */
    ORDERVALUE_ASC("listTime", false),
    ORDERVALUE_DESC("listTime", true),
    /**
     * 创建时间
     */
    TIMECREATED_ASC("createdTime", false),
    TIMECREATED_DESC("createdTime", true),
    /**
     * 租金
     */
    RENTAMT_ASC("rentAmt", false),
    RENTAMT_DESC("rentAmt", true);


    private final String sortField;
    private final boolean desc;

    private SortMode(String sortField, boolean desc) {
        this.sortField = sortField;
        this.desc = desc;
    }

    public String getSortField() {
        return sortField;
    }

    public boolean isDesc() {
        return desc;
    }
}
