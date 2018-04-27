package com.apass.zufang.domain.vo;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 看房行程 专用 电话预约看房VO实体类
 * @author Administrator
 *
 */
public class ReserveHouseVo{
	private Long id;//预约看房字段
    private Long houseId;
    private Byte type;
    private String userId;
    private String telphone;
    private String name;
    private Date reserveDate;
    private String memo;
    private String isDelete;
    private Date createdTime;
    private Date updatedTime;
    private Byte reserveStatus;
    private String reserveType;//预约看房新增字段 预约类型
    private String createdDateTime;//预约看房新增字段 申请预约时间
    private String reserveDateTime;//预约看房新增字段 看房时间
    
    private Long apartmentId;//所属公寓ID
	private String apartmentName;//公寓名称
	private String apartmentCode;//公寓编码
	
	private String communityName;//小区名称
	private String houseTitle;//房源名称标题
    private String houseCode;//房源编码
    private String houseStatus;//房源状态
    
    private String houseProvince;//房源省
    private String houseCity;//房源市
    private String houseDistrict;//房源区
    private String houseStreet;//房源街道
    private String houseDetailAddr;//房源门牌号
    
    private BigDecimal houseRentAmt;//房源租金
    private String houseZujinType;//房源付款方式1:押一付三;2:押一付一；.....
    private String houseRoom;//房源室
    private String houseHall;//房源厅
    private String houseWei;//房源卫
    private String houseAll;//房源户型 拼接室厅卫
    
    private String houseRentType;//房源出租类型 1:整租；2:合租
    private String houseAcreage;//房源总面积
    private String houseRoomAcreage;//房源合租面积
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
	public Byte getReserveStatus() {
		return reserveStatus;
	}
	public void setReserveStatus(Byte reserveStatus) {
		this.reserveStatus = reserveStatus;
	}
	public String getReserveType() {
		return reserveType;
	}
	public void setReserveType(String reserveType) {
		this.reserveType = reserveType;
	}
	public String getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public String getReserveDateTime() {
		return reserveDateTime;
	}
	public void setReserveDateTime(String reserveDateTime) {
		this.reserveDateTime = reserveDateTime;
	}
	public Long getApartmentId() {
		return apartmentId;
	}
	public void setApartmentId(Long apartmentId) {
		this.apartmentId = apartmentId;
	}
	public String getApartmentName() {
		return apartmentName;
	}
	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}
	public String getApartmentCode() {
		return apartmentCode;
	}
	public void setApartmentCode(String apartmentCode) {
		this.apartmentCode = apartmentCode;
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
	public String getHouseCode() {
		return houseCode;
	}
	public void setHouseCode(String houseCode) {
		this.houseCode = houseCode;
	}
	public String getHouseStatus() {
		return houseStatus;
	}
	public void setHouseStatus(String houseStatus) {
		this.houseStatus = houseStatus;
	}
	public String getHouseProvince() {
		return houseProvince;
	}
	public void setHouseProvince(String houseProvince) {
		this.houseProvince = houseProvince;
	}
	public String getHouseCity() {
		return houseCity;
	}
	public void setHouseCity(String houseCity) {
		this.houseCity = houseCity;
	}
	public String getHouseDistrict() {
		return houseDistrict;
	}
	public void setHouseDistrict(String houseDistrict) {
		this.houseDistrict = houseDistrict;
	}
	public String getHouseStreet() {
		return houseStreet;
	}
	public void setHouseStreet(String houseStreet) {
		this.houseStreet = houseStreet;
	}
	public String getHouseDetailAddr() {
		return houseDetailAddr;
	}
	public void setHouseDetailAddr(String houseDetailAddr) {
		this.houseDetailAddr = houseDetailAddr;
	}
	public BigDecimal getHouseRentAmt() {
		return houseRentAmt;
	}
	public void setHouseRentAmt(BigDecimal houseRentAmt) {
		this.houseRentAmt = houseRentAmt;
	}
	public String getHouseZujinType() {
		return houseZujinType;
	}
	public void setHouseZujinType(String houseZujinType) {
		this.houseZujinType = houseZujinType;
	}
	public String getHouseRoom() {
		return houseRoom;
	}
	public void setHouseRoom(String houseRoom) {
		this.houseRoom = houseRoom;
	}
	public String getHouseHall() {
		return houseHall;
	}
	public void setHouseHall(String houseHall) {
		this.houseHall = houseHall;
	}
	public String getHouseWei() {
		return houseWei;
	}
	public void setHouseWei(String houseWei) {
		this.houseWei = houseWei;
	}
	public String getHouseAll() {
		return houseAll;
	}
	public void setHouseAll(String houseAll) {
		this.houseAll = houseAll;
	}
	public String getHouseRentType() {
		return houseRentType;
	}
	public void setHouseRentType(String houseRentType) {
		this.houseRentType = houseRentType;
	}
	public String getHouseAcreage() {
		return houseAcreage;
	}
	public void setHouseAcreage(String houseAcreage) {
		this.houseAcreage = houseAcreage;
	}
	public String getHouseRoomAcreage() {
		return houseRoomAcreage;
	}
	public void setHouseRoomAcreage(String houseRoomAcreage) {
		this.houseRoomAcreage = houseRoomAcreage;
	}
}