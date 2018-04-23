package com.apass.zufang.domain.dto;
import com.apass.zufang.common.model.QueryParams;

/**
 * 
 * @author pyc
 * @email  E-mail: pengyingchao@apass.cn
 * @version V2.0
 * @createTime：2018年2月8日 下午1:41:42 
 * @description banner管理查询类
 */
public class BannerQueryParams extends QueryParams{
	
	private String bannerType;
	
	private String isDelete;//banner是否删除

	public String getBannerType() {
		return bannerType;
	}

	public void setBannerType(String bannerType) {
		this.bannerType = bannerType;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}
	
}