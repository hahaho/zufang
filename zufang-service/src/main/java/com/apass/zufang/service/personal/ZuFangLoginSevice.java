package com.apass.zufang.service.personal;



import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.jwt.TokenManager;
import com.apass.gfb.framework.jwt.common.SaltEncodeUtils;
import com.apass.zufang.domain.ajp.entity.GfbRegisterInfoEntity;
import com.apass.zufang.domain.constants.ConstantsUtil;
import com.apass.zufang.mapper.ajp.personal.GfbRegisterInfoEntityMapper;
/**
 * 个人中心 登录
 * 
 * @author ls
 * @update 2018-02-08 11:02
 *
 */
@Component
public class ZuFangLoginSevice {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ZuFangLoginSevice.class);

	
	@Autowired
	private GfbRegisterInfoEntityMapper gfbRegisterInfoEntityMapper;
	
	
	@Autowired
	public TokenManager tokenManager;
	/**
	 * 已登录
	 * @param customerId
	 * @return
	 */
//	public GfbCustomerEntity zuFangifLogin(String userId) {
//		return gfbRegisterInfoEntityMapper.zuFangifLogin(userId);
//	}
	
	/**
	 * 设置密码
	 * @param customerId
	 * @param mobile
	 * @param password
	 * @return
	 */
	public void zufangsetpassword(GfbRegisterInfoEntity registerInfo) throws BusinessException{
		try {
			
			// 1. 使用加密盐加密
			String salt = SaltEncodeUtils.generateDefaultSalt();
			String password = registerInfo.getPassword();
			// 2. 加密后的密码
			String newPassword = SaltEncodeUtils.sha1(password, salt);
			registerInfo.setPassword(newPassword);
			registerInfo.setSalt(salt);
			
			gfbRegisterInfoEntityMapper.zufangsetpassword(registerInfo);
		} catch (Exception e) {
			LOGGER.error("保存客户注册信息出错===》", e);
			throw new BusinessException("保存客户注册信息出错===》", e);
		}
	}

	
	/**
	 * 密码登录
	 * @param customerId
	 * @param mobile
	 * @param password
	 * @return
	 */
	public  Map<String, Object> zufangpasswordlogin(String userId, String mobile, String password)throws BusinessException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			GfbRegisterInfoEntity zuFangLogin = new GfbRegisterInfoEntity();
			zuFangLogin.setId(Long.parseLong(userId));
			zuFangLogin.setAccount(mobile);
			zuFangLogin.setPassword(password);
			//查询
			GfbRegisterInfoEntity selectMobile = gfbRegisterInfoEntityMapper.selectMobile(mobile);
			//是否有设置账号密码
			if(selectMobile == null){
				return null;
			}
			// 密码登陆失败
			if (!SaltEncodeUtils.validate(password, selectMobile.getPassword(), selectMobile.getSalt())) {
				return null;
			}
			// 6.根据appId生成token
			String token = tokenManager.createToken(selectMobile.getId().toString(), selectMobile.getAccount(),
					ConstantsUtil.TOKEN_EXPIRES_SPACE);
			returnMap.put("token", token);
			returnMap.put("id", selectMobile.getId());
			returnMap.put("account", selectMobile.getAccount());
			return returnMap;
		} catch (Exception e) {
			LOGGER.error("密码登录出错===》", e);
			throw new BusinessException("保存客户注册信息出错===》", e);
		}
	}
	
	/**
	 * 保存客户注册信息
	 * 
	 * @param registerInfo
	 */
	public Long saveRegisterInfo(GfbRegisterInfoEntity registerInfo) throws BusinessException {
		try {
			// 1. 使用加密盐加密
			String salt = SaltEncodeUtils.generateDefaultSalt();
			String password = registerInfo.getPassword();
			// 2. 加密后的密码
			String newPassword = SaltEncodeUtils.sha1(password, salt);
			registerInfo.setPassword(newPassword);
			registerInfo.setSalt(salt);
			// 3. 保存注册信息
			gfbRegisterInfoEntityMapper.insert(registerInfo);
			return registerInfo.getId();
		} catch (Exception e) {
			LOGGER.error("保存客户注册信息出错===》", e);
			throw new BusinessException("保存客户注册信息出错===》", e);
		}
	}
	
	/**
	 * 根据手机用户查询
	 * @param mobile
	 * @return
	 */
	public GfbRegisterInfoEntity zfselecetmobile(String mobile) throws BusinessException{
		return gfbRegisterInfoEntityMapper.selectMobile(mobile);
	}
	
}
