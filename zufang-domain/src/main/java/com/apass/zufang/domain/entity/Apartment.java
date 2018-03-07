package com.apass.zufang.domain.entity;
import java.util.Date;
/**
 * 公寓对象持久化类
 * @author Administrator
 *
 */
public class Apartment {
    private Long id;

    private String code;

    private String name;

    private String companyName;

    private String province;

    private String city;

    private String area;

    private String bankName;

    private String bankCard;
    private String isDelete;
    private String companyLogo;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }
	public String getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}
	public String getCompanyLogo() {
		return companyLogo;
	}
	public void setCompanyLogo(String companyLogo) {
		this.companyLogo = companyLogo;
	}
	private Date createdTime;
    private Date updatedTime;
    private String createdUser;
    private String updatedUser;
    public String getCreatedUser() {
        return createdUser;
    }
    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }
    public String getUpdatedUser() {
        return updatedUser;
    }
    public void setUpdatedUser(String createdUser) {
        this.createdUser = createdUser;
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
    public void fillUser(String user){
        setUpdatedUser(user);
    }
    public void fillTime(){
        setUpdatedTime(new Date());
    }
    public void fillAllUser(String user){
        setCreatedUser(user);
        setUpdatedUser(user);
    }
    public void fillAllTime(){
        setCreatedTime(new Date());
        setUpdatedTime(new Date());
    }
    public void fillAllField(String user){
        fillAllUser(user);
        fillAllTime();
    }
    public void fillField(String user){
        fillUser(user);
        fillTime();
    }
}