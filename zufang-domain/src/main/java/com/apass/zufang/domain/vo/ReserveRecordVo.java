package com.apass.zufang.domain.vo;
import java.util.Date;
/**
 * 看房行程 专用  看房记录变更VO实体类
 * @author Administrator
 *
 */
public class ReserveRecordVo {
    private Long id;
    private Long reserveHouseId;
    private Byte operateType;
    private Date reserveDate;
    private Date operateTime;
    private String remark;
    private Date createdTime;
    private Date updatedTime;
    private String createdUser;
    private String updatedUser;
    
    private String operateTimeStr;
    private String operateTypeStr;
    private String reserveDateStr;

	public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getReserveHouseId() {
        return reserveHouseId;
    }
    public void setReserveHouseId(Long reserveHouseId) {
        this.reserveHouseId = reserveHouseId;
    }
    public Byte getOperateType() {
        return operateType;
    }
    public void setOperateType(Byte operateType) {
        this.operateType = operateType;
    }
    public Date getReserveDate() {
		return reserveDate;
	}
	public void setReserveDate(Date reserveDate) {
		this.reserveDate = reserveDate;
	}
    public Date getOperateTime() {
        return operateTime;
    }
    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public Date getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
    public Date getUpdatedTime() {
        return updatedTime;
    }
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
    public String getCreatedUser() {
        return createdUser;
    }
    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }
    public String getUpdatedUser() {
        return updatedUser;
    }
    public void setUpdatedUser(String updatedUser) {
        this.updatedUser = updatedUser;
    }
    
    public String getOperateTimeStr() {
		return operateTimeStr;
	}
	public void setOperateTimeStr(String operateTimeStr) {
		this.operateTimeStr = operateTimeStr;
	}
	public String getOperateTypeStr() {
		return operateTypeStr;
	}
	public void setOperateTypeStr(String operateTypeStr) {
		this.operateTypeStr = operateTypeStr;
	}
	public String getReserveDateStr() {
		return reserveDateStr;
	}
	public void setReserveDateStr(String reserveDateStr) {
		this.reserveDateStr = reserveDateStr;
	}
}