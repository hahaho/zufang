package com.apass.esp.search.condition;

import com.apass.esp.search.enums.SortMode;
import com.apass.gfb.framework.mybatis.page.Pagination;

/**
 * Created by xianzhi.wang on 2017/5/23.
 */
public class GoodsSearchCondition {

    private String goodsName;//商品名称模糊
    
    private String cateGoryName;//分类名称模糊
    
    private String skuAttr;//规格模糊

    private SortMode sortMode;//排序字段

    private Integer offset = (Pagination.DEFAULT_PAGE_NUM - 1) * Pagination.DEFAULT_PAGE_SIZE; // 开始条数

    private Integer pageSize = Pagination.DEFAULT_PAGE_SIZE;// 多少条

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

    public String getCateGoryName() {
        return cateGoryName;
    }

    public void setCateGoryName(String cateGoryName) {
        this.cateGoryName = cateGoryName;
    }

    public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getSkuAttr() {
		return skuAttr;
	}

	public void setSkuAttr(String skuAttr) {
		this.skuAttr = skuAttr;
	}
    
}
