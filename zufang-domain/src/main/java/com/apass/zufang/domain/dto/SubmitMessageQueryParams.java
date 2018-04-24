package com.apass.zufang.domain.dto;
import com.apass.zufang.common.model.QueryParams;
/**
 * 意见反馈功能查询工具类
 * @author Administrator
 *
 */
public class SubmitMessageQueryParams extends QueryParams{
	private String phone;
	private String submitTimeStart;
	private String submitTimeEnd;
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSubmitTimeStart() {
		return submitTimeStart;
	}
	public void setSubmitTimeStart(String submitTimeStart) {
		this.submitTimeStart = submitTimeStart;
	}
	public String getSubmitTimeEnd() {
		return submitTimeEnd;
	}
	public void setSubmitTimeEnd(String submitTimeEnd) {
		this.submitTimeEnd = submitTimeEnd;
	}
}