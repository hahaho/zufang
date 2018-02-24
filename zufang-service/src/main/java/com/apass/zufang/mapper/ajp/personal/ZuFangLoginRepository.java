package com.apass.zufang.mapper.ajp.personal;

import com.apass.gfb.framework.annotation.MyBatisRepository;
import com.apass.zufang.domain.ajp.entity.ZuFangLoginEntity;

@MyBatisRepository
public class ZuFangLoginRepository {
	/**是否登录
	 * 
	 * @param customerId
	 * @return
	 */
	public ZuFangLoginEntity zuFangifLogin(String customerId) {
		
		return null;
	}
	
	public Integer zufangsetpassword(ZuFangLoginEntity zuFangLogin) {
		return null;
		
	}

	public Integer zufangpasswordlogin(ZuFangLoginEntity zuFangLogin) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
