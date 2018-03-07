package com.apass.zufang.domain.entity;
import java.util.Date;
<<<<<<< .merge_file_a01536


import com.apass.zufang.common.model.QueryParams;
public class ReserveHouse extends QueryParams{


=======
public class ReserveHouse {
>>>>>>> .merge_file_a18940
    private Long id;

    private Long houseId;

    private Byte type;

    private String userId;

    private String telphone;

    private String name;

    private Date reserveDate;

    private String memo;

    private String isDelete;
<<<<<<< .merge_file_a01536

    
    private Date createdTime;
    
    private Date updatedTime;
    

=======
    private Date createdTime;
    private Date updatedTime;
>>>>>>> .merge_file_a18940
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

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(Date reserveDate) {
        this.reserveDate = reserveDate;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

}