package com.apass.zufang.web.personal;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.jwt.TokenManager;
import com.apass.gfb.framework.jwt.common.SaltEncodeUtils;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.zufang.domain.ajp.entity.GfbRegisterInfoEntity;
import com.apass.zufang.domain.constants.ConstantsUtil;
import com.apass.zufang.service.common.MobileSmsService;
import com.apass.zufang.service.personal.ZuFangLoginSevice;
import com.apass.zufang.domain.Response;
import javax.ws.rs.core.MediaType;

/**
 * 个人中心 登录
 * 
 * @author ls
 * @update 2018-02-08 11:02
 *
 */
@Path("/zufangpersonal")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ZuFangLoginController {
	private static final Logger logger = LoggerFactory.getLogger(ZuFangLoginController.class);
	
	@Autowired
	private ZuFangLoginSevice zuFangLoginSevice;
	
	@Autowired
	private MobileSmsService mobileRandomService;
	
	@Autowired
	public TokenManager tokenManager;
	
	/**
	 * 是否登录
	 * @param paramMap
	 * @return
	 */
//	@POST
//	@Path("/zufanglogin")
//	public Response ZuFangLogin(Map<String, Object> paramMap) {
//	        try {
//	        	String userId = CommonUtils.getValue(paramMap, "userId");
//	        	if(org.apache.commons.lang3.StringUtils.isBlank(userId)){
//	        		//未登录
//	        		 return Response.success("未登录操作");
//	        	}else{
//	        		//已登录
//	        		return Response.success("登录成功",zuFangLoginSevice.zuFangifLogin(userId));
//	        	}
//	        } catch (Exception e) {
//	        	logger.info("判断登录失败"+e);
//	            return Response.fail("操作失败");
//	        }
//	    }
	
	/**
	 * 设置密码
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/zufangsetpassword")
	public Response zufangsetpassword(Map<String, Object> paramMap) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
	        try {
	        	String userId = CommonUtils.getValue(paramMap, "userId");
	        	String mobile = CommonUtils.getValue(paramMap, "mobile");
	        	String password = CommonUtils.getValue(paramMap, "password");
	        	String smsType = CommonUtils.getValue(paramMap, "smsType");// 类型
	        	String code = CommonUtils.getValue(paramMap, "code");// 验证码
	        	logger.info("入参 ：userId————>"+userId+" mobile————>"+mobile+" password—————>"+password+" smsType—————>"+smsType+" code—————>"+code);
//	        	if(org.apache.commons.lang3.StringUtils.isBlank(userId)){
//	        		//用户id不合规
//	        		 return Response.fail("用户id不合规");
//	        	}else 
	        		if(org.apache.commons.lang3.StringUtils.isBlank(mobile)){
	        		//手机号不合规
	        		 return Response.fail("手机号不合规");
	        	}else if(org.apache.commons.lang3.StringUtils.isBlank(password)
	        			&& password.length()>6 && password.length()<20){
	        		//密码不合规
	        		 return Response.fail("密码不合规");
	        	}else if(org.apache.commons.lang3.StringUtils.isBlank(smsType)){
	        		//手机号不合规
	        		 return Response.fail("类型不能为空");
	        	}else if(org.apache.commons.lang3.StringUtils.isBlank(code)){
	        		//手机号不合规
	        		 return Response.fail("验证码不能为空");
	        	}
	        	
	        	//验证码校验
	        	boolean mobileCodeValidate = mobileRandomService.mobileCodeValidate(smsType,mobile,code);
	        	if(mobileCodeValidate){
	        		// 3.检查是否已注册(通过注册手机号检查)
					GfbRegisterInfoEntity userInfo = zuFangLoginSevice.zfselecetmobile(mobile);
					
					if (userInfo == null) {
						returnMap.put("operationResult", false);
						returnMap.put("displayInfo", "该手机号尚未注册");
						return Response.fail("该手机号尚未注册",returnMap);
					}
					//校验新旧密码是否一致
					String newPassword = SaltEncodeUtils.sha1(password, userInfo.getSalt());
					if(newPassword.equals(userInfo.getPassword())){
						returnMap.put("operationResult", false);
						returnMap.put("displayInfo", "此密码与旧密码一致");
						return Response.fail("此密码与旧密码一致",returnMap);
					}
						userInfo.setPassword(password);
						// 4. 客户保存重置密码
						zuFangLoginSevice.zufangsetpassword(userInfo);
						returnMap.put("operationResult", true);
						returnMap.put("displayInfo", "用户重置密码成功");
						returnMap.put("gfbRegisterIn", userInfo);
						return Response.success("用户重置密码成功", returnMap);
	        	}else{
	        		return Response.fail("验证码错误");
	        	}
	        } catch (Exception e) {
	        	logger.error("设置密码失败"+e);
	            return Response.fail("操作失败");
	        }
	    }
	
	
	/**
	 * 密码登录
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/zufangpasswordlogin")
	public Response zufangpasswordlogin(Map<String, Object> paramMap)throws BusinessException  {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		  try {
	        	String userId = CommonUtils.getValue(paramMap, "userId");
	        	String mobile = CommonUtils.getValue(paramMap, "mobile");
	        	String password = CommonUtils.getValue(paramMap, "password");
	        	logger.info("入参 ：userId————>"+userId+" mobile————>"+mobile+" password—————>"+password);
	        	
//	        	if(org.apache.commons.lang3.StringUtils.isBlank(userId)){
//	        		//用户id不能为空
//	        		 return Response.success("用户id不能为空");
//	        	}else 
	        	if(org.apache.commons.lang3.StringUtils.isBlank(mobile)){
	        		//手机号不能为空
	        		 return Response.success("手机号不能为空");
	        	}else if(org.apache.commons.lang3.StringUtils.isBlank(password)){
	        		//密码不能为空
	        		 return Response.success("密码不能为空");
	        	}
	        	
	        	returnMap  = zuFangLoginSevice.zufangpasswordlogin(mobile,password);
	        	
	        	if(returnMap==null){
	        		return Response.fail("请设置好密码",returnMap);
	        	}
	        		return Response.success("登录成功",returnMap);
	        	
	        } catch (Exception e) {
	        	logger.error("密码登录失败"+e);
	            return Response.fail("操作失败");
	        }
	    }
	//验证码登录
	@POST
	@Path("/zfsmslogin")
	public Response zfsmslogin(Map<String, Object> paramMap)throws BusinessException {
		Map<String, Object> resultMap = new HashMap<>();
		  try {
	        	String mobile = CommonUtils.getValue(paramMap, "mobile");// 手机
	    		String smsType = CommonUtils.getValue(paramMap, "smsType");// 类型
	    		String code = CommonUtils.getValue(paramMap, "code");// 验证码
	    		
	    		logger.info("入参 ：smsType————>"+smsType+" mobile————>"+mobile+" code—————>"+code);
	        	if(org.apache.commons.lang3.StringUtils.isBlank(smsType)){
	        		//类型不合规
	        		 return Response.success("类型不合规");
	        	}else if(org.apache.commons.lang3.StringUtils.isBlank(mobile)){
	        		//手机号不合规
	        		 return Response.success("手机号不合规");
	        	}
	        	//发短信到手机
	        boolean mobileCodeValidate = mobileRandomService.mobileCodeValidate(smsType,mobile,code);
	        	//验证真确返回客户
	        	if(mobileCodeValidate){
	        		 GfbRegisterInfoEntity zfselecetmobile = zuFangLoginSevice.zfselecetmobile(mobile);
	        		//是否已注册
	        		if(zfselecetmobile == null){
	        			//注册   并且登录
	        			GfbRegisterInfoEntity gfbRegisterInfoEntity = new GfbRegisterInfoEntity();
	        			gfbRegisterInfoEntity.setAccount(mobile);
	        			//插入数据库
	        			Long id= zuFangLoginSevice.saveRegisterInfo(gfbRegisterInfoEntity);
		        		//生成token   
		        		String token = tokenManager.createToken( String.valueOf(id), mobile, ConstantsUtil.TOKEN_EXPIRES_SPACE);
		        		resultMap.put("token", token);
		        		resultMap.put("account", mobile);
		        		resultMap.put("userId", id);
		        		return Response.success("验证码真确登录成功",resultMap);
	        		}else{
	        		//已注册用户
	        		String token = tokenManager.createToken(null, mobile, ConstantsUtil.TOKEN_EXPIRES_SPACE);
	        		resultMap.put("token", token);
	        		resultMap.put("account", zfselecetmobile.getAccount());
	        		resultMap.put("userId", zfselecetmobile.getId());
	        		return Response.success("验证码真确登录成功",resultMap);
	        		}
	        }else{
	        	return Response.fail("短信验证失败",mobileCodeValidate);
	        }
	        	
	        } catch (Exception e) {
	        	logger.error("验证码登录失败"+e);
	            return Response.fail("操作失败");
	        }
	    }
	//发送验证码  时判断是不是新用户
	@POST
	@Path("/zfsend")
	public Response zfsend(Map<String, Object> paramMap)throws BusinessException {
		
		Map<String, Object> resultMap = new HashMap<>();
		String mobile = CommonUtils.getValue(paramMap, "mobile");// 微信号
		String smsType = CommonUtils.getValue(paramMap, "smsType");// 微信号
		logger.info("入参 mobile"+mobile+"smsType"+smsType);
		
		if (StringUtils.isAnyBlank(mobile, smsType)) {
			return Response.fail("验证码接收手机号不能为空");
		}
		try {
			mobileRandomService.sendMobileVerificationCode(smsType, mobile);
			
			 GfbRegisterInfoEntity zfselecetmobile = zuFangLoginSevice.zfselecetmobile(mobile);
			 if(zfselecetmobile == null){
				 resultMap.put("user", "xinyonghu");
			 }else{
				 resultMap.put("user", "laoyonghu");
			 }
			
			return Response.success("验证码发送成功,请注意查收",resultMap);
		} catch (BusinessException e) {
			logger.error("mobile verification code send fail", e);
			return Response.fail("网络异常,发送验证码失败,请稍后再试");
		}
	}

}
