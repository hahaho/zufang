package com.apass.zufang.search.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xiaohai on 2018/2/26.
 */
public class HouseEs implements IdAble{
    private Integer id;

    /**
     * 房源id
     * @return
     */
    private long houseId;

    /**
     * 房源编码
     * @return
     */
    private String code;

    /**
     * 公寓id
     */
    private Long apartmentId;

    /**
     * 所属公司
     */
    private String companyName;

    /**
     * 公寓名称
     */
    private String apartmentName;
    private String apartmentNamePinyin;

    /**
     * 热门房源类型：1：正常，2:精选
     */
    private Byte type;

    /**
     * 排序字段
     */
    private Integer sortNo;

    /**
     *房源出租方式：1:整租；2:合租
     */
    private Byte rentType;

    /**
     * 小区名称
     */
    private String communityName;
    /**
     * 小区名称拼音
     */
    private String communityNamePinyin;

    /**
     * 面积
     */
    private BigDecimal acreage;

    /**
     * 室
     */
    private Integer room;

    /**
     * 厅
     */
    private Integer hall;

    /**
     * 卫
     */
    private Integer wei;

    /**
     * 第几层
     */
    private Integer floor;

    /**
     * 总共的楼层数
     */
    private Integer totalFloor;

    /**
     * 有无电梯：1:有；2:无
     */
    private Byte liftType;

    /**
     * 租金
     */
    private BigDecimal rentAmt;

    /**
     * 租金交付方式：1:押一付三;2:押一付一
     */
    private Byte zujinType;

    /**
     * 朝向
     */
    private Byte chaoxiang;

    /**
     * 装修：装修情况:1:豪华装修..
     */
    private Byte zhuangxiu;

    /**
     * 房源状态：1:待上架；2：上架；3：下架；4：删除；5：修改
     */
    private Byte status;

    /**
     * 上架时间
     */
    private Date listTime;
    private String listTimeStr;

    /**
     * 下架时间
     */
    private Date delistTime;
    private String delistTimeStr;

    /**
     *房源标题
     */
    private String houseTitle;
    private String houseTitlePinyin;


    /**
     * 创建时间
     */
    private Date createdTime;
    private String createdTimeStr;

    /**
     * 修改时间
     */
    private Date updatedTime;
    private String updatedTimeStr;

    /**
     * 创建人
     */
    private String createdUser;

    /**
     * 修改人
     */
    private String updatedUser;

    /**
     * 是否删除(00 默认值,  01 删除)
     */
    private String isDelete;

    /**
     * 浏览量
     * @return
     */
    private Long pageView;

    /**
     *管家联系方式
     */
    private String housekeeperTel;

    /**
     * 几户合租
     */
    private String totalDoors;

    /**
     * 朝向， 1:东：2:南....
     */
    private Byte hezuChaoxiang;

    /**
     * 出租介绍， 1:出租主卧；2：出租次卧；3：出租隔断；4：出租床位
     */
    private Byte hezuResource;

    /**
     * 出租面积
     */
    private BigDecimal roomAcreage;

    /**
     * 所属省份
     */
    private String province;

    /**
     * 所属城市
     */
    private String city;

    /**
     * 区
     */
    private String district;

    /**
     * 街道
     */
    private String street;

    /**
     * 详细地址
     */
    private String detailAddr;
    private String detailAddrPinyin;

    /**
     * 经度
     */
    private double longitude;

    /**
     * 纬度
     */
    private double latitude;

    /**
     * 位置
     */
    private String location;

    /**
     * 图片url
     * @return
     */
    private String url;

    /**
     * 电视，空调等配置信息
     * @return
     */
    private String configName;
    private String configNamePinyin;

    /**
     * 价格区间标记:见PriceRangeEnum
     * @return
     */
    private Integer priceFlag;

    public BigDecimal getRoomAcreage() {
        return roomAcreage;
    }

    public void setRoomAcreage(BigDecimal roomAcreage) {
        this.roomAcreage = roomAcreage;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }


    public long getHouseId() {
        return houseId;
    }

    public void setHouseId(long houseId) {
        this.houseId = houseId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Long apartmentId) {
        this.apartmentId = apartmentId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public Byte getRentType() {
        return rentType;
    }

    public void setRentType(Byte rentType) {
        this.rentType = rentType;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getCommunityNamePinyin() {
        return communityNamePinyin;
    }

    public void setCommunityNamePinyin(String communityNamePinyin) {
        this.communityNamePinyin = communityNamePinyin;
    }

    public BigDecimal getAcreage() {
        return acreage;
    }

    public void setAcreage(BigDecimal acreage) {
        this.acreage = acreage;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    public Integer getHall() {
        return hall;
    }

    public void setHall(Integer hall) {
        this.hall = hall;
    }

    public Integer getWei() {
        return wei;
    }

    public void setWei(Integer wei) {
        this.wei = wei;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getTotalFloor() {
        return totalFloor;
    }

    public void setTotalFloor(Integer totalFloor) {
        this.totalFloor = totalFloor;
    }

    public Byte getLiftType() {
        return liftType;
    }

    public void setLiftType(Byte liftType) {
        this.liftType = liftType;
    }

    public BigDecimal getRentAmt() {
        return rentAmt;
    }

    public void setRentAmt(BigDecimal rentAmt) {
        this.rentAmt = rentAmt;
    }

    public Byte getZujinType() {
        return zujinType;
    }

    public void setZujinType(Byte zujinType) {
        this.zujinType = zujinType;
    }

    public Byte getChaoxiang() {
        return chaoxiang;
    }

    public void setChaoxiang(Byte chaoxiang) {
        this.chaoxiang = chaoxiang;
    }

    public Byte getZhuangxiu() {
        return zhuangxiu;
    }

    public void setZhuangxiu(Byte zhuangxiu) {
        this.zhuangxiu = zhuangxiu;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getListTime() {
        return listTime;
    }

    public void setListTime(Date listTime) {
        this.listTime = listTime;
    }

    public String getListTimeStr() {
        return listTimeStr;
    }

    public void setListTimeStr(String listTimeStr) {
        this.listTimeStr = listTimeStr;
    }

    public Date getDelistTime() {
        return delistTime;
    }

    public void setDelistTime(Date delistTime) {
        this.delistTime = delistTime;
    }

    public String getDelistTimeStr() {
        return delistTimeStr;
    }

    public void setDelistTimeStr(String delistTimeStr) {
        this.delistTimeStr = delistTimeStr;
    }

    public String getHouseTitle() {
        return houseTitle;
    }

    public void setHouseTitle(String houseTitle) {
        this.houseTitle = houseTitle;
    }

    public String getHouseTitlePinyin() {
        return houseTitlePinyin;
    }

    public void setHouseTitlePinyin(String houseTitlePinyin) {
        this.houseTitlePinyin = houseTitlePinyin;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedTimeStr() {
        return createdTimeStr;
    }

    public void setCreatedTimeStr(String createdTimeStr) {
        this.createdTimeStr = createdTimeStr;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getUpdatedTimeStr() {
        return updatedTimeStr;
    }

    public void setUpdatedTimeStr(String updatedTimeStr) {
        this.updatedTimeStr = updatedTimeStr;
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

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public Long getPageView() {
        return pageView;
    }

    public void setPageView(Long pageView) {
        this.pageView = pageView;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDetailAddr() {
        return detailAddr;
    }

    public void setDetailAddr(String detailAddr) {
        this.detailAddr = detailAddr;
    }

    public String getDetailAddrPinyin() {
        return detailAddrPinyin;
    }

    public void setDetailAddrPinyin(String detailAddrPinyin) {
        this.detailAddrPinyin = detailAddrPinyin;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigNamePinyin() {
        return configNamePinyin;
    }

    public void setConfigNamePinyin(String configNamePinyin) {
        this.configNamePinyin = configNamePinyin;
    }

    public String getHousekeeperTel() {
        return housekeeperTel;
    }

    public void setHousekeeperTel(String housekeeperTel) {
        this.housekeeperTel = housekeeperTel;
    }

    public String getTotalDoors() {
        return totalDoors;
    }

    public void setTotalDoors(String totalDoors) {
        this.totalDoors = totalDoors;
    }

    public Byte getHezuChaoxiang() {
        return hezuChaoxiang;
    }

    public void setHezuChaoxiang(Byte hezuChaoxiang) {
        this.hezuChaoxiang = hezuChaoxiang;
    }

    public Byte getHezuResource() {
        return hezuResource;
    }

    public void setHezuResource(Byte hezuResource) {
        this.hezuResource = hezuResource;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getPriceFlag() {
        return priceFlag;
    }

    public void setPriceFlag(Integer priceFlag) {
        this.priceFlag = priceFlag;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public String getApartmentNamePinyin() {
        return apartmentNamePinyin;
    }

    public void setApartmentNamePinyin(String apartmentNamePinyin) {
        this.apartmentNamePinyin = apartmentNamePinyin;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
