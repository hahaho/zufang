package com.apass.zufang.mapper.ajp.personal;

import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.ajp.entity.GfbRegisterInfoEntity;
import com.apass.zufang.domain.entity.Apartment;

public interface GfbRegisterInfoEntityMapper extends GenericMapper<Apartment, Long> {
	/**
	 * 已登录
	 * 
	 * @param customerId
	 * @return
	 */
//	public GfbCustomerEntity zuFangifLogin(String customerId);

	/**
	 * 设置密码
	 * 
	 * @param customerId
	 * @param mobile
	 * @param password
	 * @return
	 */
	public Integer zufangsetpassword(GfbRegisterInfoEntity zuFangLogin);

	/**
	 * 密码登录
	 * 
	 * @param customerId
	 * @param mobile
	 * @param password
	 * @return
	 */
	public Integer zufangpasswordlogin(GfbRegisterInfoEntity zuFangLogin);

	/**
	 * 短信登录
	 * 
	 * @param customerId
	 * @return
	 */
	public Integer insert(GfbRegisterInfoEntity registerInfo);
	
	/**
	 * 手机号查询
	 * @param mobile
	 * @return
	 */
	public GfbRegisterInfoEntity selectMobile(String mobile);


}