package com.apass.zufang.web.personal;

import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.service.personal.ZuFangLoginSevice;

/**
 * 个人中心 登录
 * 
 * @author ls
 * @update 2018-02-08 11:02
 *
 */
@Path("/zufangpersonal")
public class ZuFangLoginController {
	private static final Logger logger = LoggerFactory.getLogger(ZuFangLoginController.class);
	
	@Autowired
	private ZuFangLoginSevice zuFangLoginSevice;
	
	//是否登录
	@POST
	@Path("/zufanglogin")
	public Response ZuFangLogin(Map<String, Object> paramMap) {
	        try {
	        	String customerId = CommonUtils.getValue(paramMap, "customerId");
	        	if(org.apache.commons.lang3.StringUtils.isBlank(customerId)){
	        		//未登录
	        		 return Response.success("未登录操作");
	        	}else{
	        		//已登录
	        		return Response.success("登录成功",zuFangLoginSevice.zuFangifLogin(customerId));
	        	}
	        } catch (Exception e) {
	            return Response.fail("操作失败");
	        }
	    }
	
	//设置密码
	@POST
	@Path("/zufangsetpassword")
	public Response zufangsetpassword(Map<String, Object> paramMap) {
		
	        try {
	        	String customerId = CommonUtils.getValue(paramMap, "customerId");
	        	String mobile = CommonUtils.getValue(paramMap, "mobile");
	        	String password = CommonUtils.getValue(paramMap, "password");
	        	
	        	if(org.apache.commons.lang3.StringUtils.isBlank(customerId)){
	        		//用户id不能为空
	        		 return Response.success("用户id不能为空");
	        	}else if(org.apache.commons.lang3.StringUtils.isBlank(mobile)){
	        		//手机号不能为空
	        		 return Response.success("手机号不能为空");
	        	}else if(org.apache.commons.lang3.StringUtils.isBlank(password)){
	        		//密码不能为空
	        		 return Response.success("密码不能为空");
	        	}
	        	Integer zufangsetpassword = zuFangLoginSevice.zufangsetpassword(customerId,mobile,password);
	        		return Response.success("设置密码成功",zufangsetpassword);
	        	
	        } catch (Exception e) {
	            return Response.fail("操作失败");
	        }
	    }
	
	
	//密码登录
	@POST
	@Path("/zufangpasswordlogin")
	public Response zufangpasswordlogin(Map<String, Object> paramMap) {
		  try {
	        	String customerId = CommonUtils.getValue(paramMap, "customerId");
	        	String mobile = CommonUtils.getValue(paramMap, "mobile");
	        	String password = CommonUtils.getValue(paramMap, "password");
	        	
	        	if(org.apache.commons.lang3.StringUtils.isBlank(customerId)){
	        		//用户id不能为空
	        		 return Response.success("用户id不能为空");
	        	}else if(org.apache.commons.lang3.StringUtils.isBlank(mobile)){
	        		//手机号不能为空
	        		 return Response.success("手机号不能为空");
	        	}else if(org.apache.commons.lang3.StringUtils.isBlank(password)){
	        		//密码不能为空
	        		 return Response.success("密码不能为空");
	        	}
	        	Integer zufangsetpassword = zuFangLoginSevice.zufangpasswordlogin(customerId,mobile,password);
	        		return Response.success("登录成功",zufangsetpassword);
	        	
	        } catch (Exception e) {
	            return Response.fail("操作失败");
	        }
	    }
	
	
	//短信登录  注册
	@POST
	@Path("/zufangsmslogin")
	public Response zufangsmslogin(Map<String, Object> paramMap) {
	        try {  
	        	String customerId = CommonUtils.getValue(paramMap, "mobile");
	        	
	        	if(org.apache.commons.lang3.StringUtils.isBlank(customerId)){
	        		//未登录
	        		 return Response.success("未登录操作");
	        	}else{
<<<<<<< Updated upstream
	        		//已登录
//	        		return Response.success("登录成功",zuFangLoginSevice.zuFangLogin(customerId));
	        		return Response.successResponse();
=======
	        		//已登录 TODO 无zuFangLogin()方法
	        		//return Response.success("登录成功",zuFangLoginSevice.zuFangLogin(customerId));
					return null;
>>>>>>> Stashed changes
	        	}
	        } catch (Exception e) {
	            return Response.fail("操作失败");
	        }
	    }
}
