package com.apass.zufang.mapper.ajp.personal;

import java.util.List;

import com.apass.gfb.framework.annotation.MyBatisRepository;
import com.apass.gfb.framework.mybatis.support.BaseMybatisRepository;
import com.apass.zufang.domain.ajp.entity.ZuFangLoginEntity;

@MyBatisRepository
public class ZuFangLoginRepository extends BaseMybatisRepository<ZuFangLoginEntity, Long>  {
	/**
	 * 已登录
	 * @param customerId
	 * @return
	 */
	public ZuFangLoginEntity zuFangifLogin(String customerId) {
		return getSqlSession().selectOne(getSQL("zuFangifLogin"), customerId);
	}
	
	
	/**
	 * 设置密码
	 * @param customerId
	 * @param mobile
	 * @param password
	 * @return
	 */
	public Integer zufangsetpassword(ZuFangLoginEntity zuFangLogin) {
		return getSqlSession().selectOne(getSQL("zufangsetpassword"), zuFangLogin);
		
	}
	
	/**
	 * 密码登录
	 * @param customerId
	 * @param mobile
	 * @param password
	 * @return
	 */
	public Integer zufangpasswordlogin(ZuFangLoginEntity zuFangLogin) {
		return getSqlSession().selectOne(getSQL("zufangpasswordlogin"), zuFangLogin);
	}


	/**
	 * 手机验证码登录   插入数据库
	 * 
	 */
	public void Mobilelogin(String mobile) {
		
	}
	

}
