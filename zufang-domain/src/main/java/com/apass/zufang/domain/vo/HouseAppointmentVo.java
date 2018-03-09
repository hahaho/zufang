package com.apass.zufang.domain.vo;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 电话预约专用  公寓房源列表VO实体类
 * @author Administrator
 *
 */
public class HouseAppointmentVo{
	private Long apartmentId;//所属公寓ID
	private String apartmentName;//公寓名称
	private String apartmentCode;//公寓编码
	
	private Long houseId;//房源ID
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
    private String houseDescription;//房源描述
    
    private String houseKitchenFalg;//房源独立厨房
	private String houseToiletFalg;//房源独立卫生间
	private String houseHezuResource;//房源合租房介绍， 1:出租主卧；2：出租次卧；3：出租隔断；4：出租床位
	private String houseChaoxiang;//房源朝向， 1:东：2:南....
    private String houseHezuChaoxiang;//房源合租房朝向， 1:东：2:南....
    private Date createdTime;
    private String createdDateTime;
    private String createdUser;
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
	public Long getHouseId() {
		return houseId;
	}
	public void setHouseId(Long houseId) {
		this.houseId = houseId;
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
	public String getHouseDescription() {
		return houseDescription;
	}
	public void setHouseDescription(String houseDescription) {
		this.houseDescription = houseDescription;
	}
	public String getHouseKitchenFalg() {
		return houseKitchenFalg;
	}
	public void setHouseKitchenFalg(String houseKitchenFalg) {
		this.houseKitchenFalg = houseKitchenFalg;
	}
	public String getHouseToiletFalg() {
		return houseToiletFalg;
	}
	public void setHouseToiletFalg(String houseToiletFalg) {
		this.houseToiletFalg = houseToiletFalg;
	}
	public String getHouseHezuResource() {
		return houseHezuResource;
	}
	public void setHouseHezuResource(String houseHezuResource) {
		this.houseHezuResource = houseHezuResource;
	}
	public String getHouseChaoxiang() {
		return houseChaoxiang;
	}
	public void setHouseChaoxiang(String houseChaoxiang) {
		this.houseChaoxiang = houseChaoxiang;
	}
	public String getHouseHezuChaoxiang() {
		return houseHezuChaoxiang;
	}
	public void setHouseHezuChaoxiang(String houseHezuChaoxiang) {
		this.houseHezuChaoxiang = houseHezuChaoxiang;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public String getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public String getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
}