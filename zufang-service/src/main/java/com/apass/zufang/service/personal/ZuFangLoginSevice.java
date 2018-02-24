package com.apass.zufang.service.personal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apass.zufang.domain.ajp.entity.ZuFangLoginEntity;
import com.apass.zufang.mapper.ajp.personal.ZuFangLoginRepository;
/**
 * 个人中心 登录
 * 
 * @author ls
 * @update 2018-02-08 11:02
 *
 */
@Component
public class ZuFangLoginSevice {
	@Autowired
	private ZuFangLoginRepository zuFangLoginDao;
	
	//是否登录
	public ZuFangLoginEntity zuFangifLogin(String customerId) {
		return zuFangLoginDao.zuFangifLogin(customerId);
	}
	//设置密码
	public Integer zufangsetpassword(String customerId, String mobile, String password) {
		ZuFangLoginEntity zuFangLogin = new ZuFangLoginEntity();
		zuFangLogin.setCustomerId(customerId);
		zuFangLogin.setMobile(mobile);
		zuFangLogin.setZuFangPassword(password);
		return zuFangLoginDao.zufangsetpassword(zuFangLogin);
	}
	//密码登录
	public Integer zufangpasswordlogin(String customerId, String mobile, String password) {
		ZuFangLoginEntity zuFangLogin = new ZuFangLoginEntity();
		zuFangLogin.setCustomerId(customerId);
		zuFangLogin.setMobile(mobile);
		zuFangLogin.setZuFangPassword(password);
		return zuFangLoginDao.zufangpasswordlogin(zuFangLogin);
	}
	
	//短信登录
	public String zufangsmslogin(String customerId) {
		return null;
	}
	
	//

}
