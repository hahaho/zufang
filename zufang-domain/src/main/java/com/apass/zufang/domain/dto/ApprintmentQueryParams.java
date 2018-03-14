package com.apass.zufang.domain.dto;
import com.apass.zufang.common.model.QueryParams;
/**
 * 公寓管理功能查询工具类
 * @author Administrator
 *
 */
public class ApprintmentQueryParams extends QueryParams{
	private String id;//公寓ID
	private String name;//公寓名称
	private String code;//公寓名称
	private String isDelete;//公寓名称
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}
}