package com.apass.zufang.domain.vo;
import com.apass.zufang.common.model.CreatedUser;
/**
 * 公寓管理功能查询结果对象
 * @author Administrator
 *
 */
public class ApartmentVo extends CreatedUser{
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
    private String fullCompanyLogo;
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
	public String getFullCompanyLogo() {
		return fullCompanyLogo;
	}
	public void setFullCompanyLogo(String fullCompanyLogo) {
		this.fullCompanyLogo = fullCompanyLogo;
	}
}