package com.apass.zufang.web.onlinebooking;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.jwt.TokenManager;
import com.apass.gfb.framework.utils.BaseConstants;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.ajp.entity.GfbRegisterInfoEntity;
import com.apass.zufang.domain.constants.ConstantsUtil;
import com.apass.zufang.domain.entity.HouseShowingsEntity;
import com.apass.zufang.domain.vo.ReservationsShowingsEntity;
import com.apass.zufang.service.common.MobileSmsService;
import com.apass.zufang.service.onlinebooking.OnlineBookingService;
import com.apass.zufang.service.personal.ZuFangLoginSevice;
import com.apass.zufang.utils.ResponsePageBody;

/**
 * 在线预约看房
 */
@Path("/onlinebooking")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class OnlineBookingController {

	private static final Logger logger = LoggerFactory.getLogger(OnlineBookingController.class);

	@Autowired
	private OnlineBookingService onlineBookingService;

	@Autowired
	public TokenManager tokenManager;

	@Autowired
	private ZuFangLoginSevice zuFangLoginSevice;
	
	@Autowired
	private MobileSmsService mobileRandomService;
	
	
	

	/**
	 * 预约看房
	 * 
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/insertshowings")
	public Response sendRandomCode(Map<String, Object> paramMap) {

		Map<String, Object> returnMap = new HashMap<String, Object>();

		String houseId = CommonUtils.getValue(paramMap, "houseId");// 房源主键
		String userId = CommonUtils.getValue(paramMap, "userId");// 用户id
		String mobile = CommonUtils.getValue(paramMap, "mobile");// 手机号
		String name = CommonUtils.getValue(paramMap, "name");// 姓名
		String reservedate = CommonUtils.getValue(paramMap, "reservedate");// 看房时间
		String memo = CommonUtils.getValue(paramMap, "memo");// 留言
		String smsType = CommonUtils.getValue(paramMap, "smsType");// 短信类型
		String code = CommonUtils.getValue(paramMap, "code");// 验证码

		logger.info("入参 houseId" + houseId + " userId" + userId + " smsType" + smsType + " mobile" + mobile + " code"
				+ code + "reserveDate" + reservedate);
		 if (StringUtils.isBlank(mobile)) {
			// 手机号不合规
			return Response.fail("手机号不合规");
		} else if (StringUtils.isBlank(houseId)) {
			// 密码不合规
			return Response.fail("房源主键不合规");
		} else if (StringUtils.isBlank(reservedate)) {
			// 手机号不合规
			return Response.fail("看房时间不能为空");
		}else if (StringUtils.isBlank(name)) {
			// 手机号不合规
			return Response.fail("用户姓名不能为空");
		}
		try {
			// 判断是否登录
			if (StringUtils.isBlank(userId)) {
				if (StringUtils.isBlank(smsType)) {
					// 手机号不合规
					return Response.fail("类型不能为空");
				} else if (StringUtils.isBlank(code)) {
					// 手机号不合规
					return Response.fail("验证码不能为空");
				}
				// 未登录操作
//					boolean mobileCodeValidate = mobileRandomService.mobileCodeValidate(smsType,mobile,code);
//		        	//验证码真确
//		        	if(mobileCodeValidate){
				if(code.equals("123456")){
		        		//用户是否已经注册  未登录
		        		GfbRegisterInfoEntity zfselecetmobile = zuFangLoginSevice.zfselecetmobile(mobile);
			        		if(zfselecetmobile == null){
			        			GfbRegisterInfoEntity gfbRegisterInfoEntity = new GfbRegisterInfoEntity();
			        			gfbRegisterInfoEntity.setAccount(mobile);
			        			// 插入数据库
			        			Long saveRegisterInfo = zuFangLoginSevice.saveRegisterInfo(gfbRegisterInfoEntity);
			        			
			        			Integer insetReserveHouse = onlineBookingService.insetReserveHouse(houseId, saveRegisterInfo.toString(), mobile, name, reservedate,
			        					memo);
			        			// 生成token
								String token = tokenManager.createToken(String.valueOf(saveRegisterInfo), mobile,
										ConstantsUtil.TOKEN_EXPIRES_SPACE);
								returnMap.put("token", token);
								returnMap.put("account", mobile);
								returnMap.put("userId", saveRegisterInfo);
								returnMap.put("Password", "no");
								if(insetReserveHouse == 1){
									return Response.success("在线预约成功", returnMap);
								}else{
									return Response.fail("您近期行程已排满，暂时不能预约。");
								}
								
			        		}else{
			        			//已经注册    未登录
			        			GfbRegisterInfoEntity zfselecetmobile2 = zuFangLoginSevice.zfselecetmobile(mobile);
			        			
			        			//是否已经预约过
			                    Integer queryOverdue = onlineBookingService.queryOverdue(mobile,houseId);
			                    if(queryOverdue == null || queryOverdue ==0 ){
			                    	
			                    	Integer insetReserveHouse = onlineBookingService.insetReserveHouse(houseId, zfselecetmobile2.getId().toString(), mobile, name, reservedate,
			                    			memo);
			                    	// 生成token
			                    	String token = tokenManager.createToken(String.valueOf(zfselecetmobile2.getId()), mobile,
			                    			ConstantsUtil.TOKEN_EXPIRES_SPACE);
			                    	returnMap.put("token", token);
			                    	returnMap.put("account", mobile);
			                    	returnMap.put("userId", zfselecetmobile2.getId());
			                    	returnMap.put("Password", zfselecetmobile2.getPassword() == null||zfselecetmobile2.getPassword()== "" ? "no" :  "yes");
			                    	if(insetReserveHouse == 1){
			                    		return Response.success("在线预约成功", returnMap);
			                    	}else{
			                    		return Response.fail("您近期行程已排满，暂时不能预约。");
			                    	}
			                    }else{
			                    	return Response.fail("您已经预约该房源");
			                    }
			        			
			        		}
							
	        	}
		        //验证码错误
	        	return Response.fail("验证码输入错误", returnMap);
			} else {
				// 已登录操作
				
				//是否已经预约过
                Integer queryOverdue = onlineBookingService.queryOverdue(mobile,houseId);
                if(queryOverdue == null || queryOverdue ==0 ){
                	
				
					Integer insetReserveHouse = onlineBookingService.insetReserveHouse(houseId, userId, mobile, name, reservedate, memo);
					if(insetReserveHouse==1){
						// 生成token
						String token = tokenManager.createToken(String.valueOf(userId), mobile,
								ConstantsUtil.TOKEN_EXPIRES_SPACE);
						returnMap.put("token", token);
						returnMap.put("ACCOUNT", mobile);
						returnMap.put("userId", userId);
						return Response.success("在线预约成功", returnMap);
					}else{
						return Response.fail("您近期行程已排满，暂时不能预约。");
					}
                }else{
                	return Response.fail("您已经预约该房源");
                }
			}
		} catch (BusinessException e) {
			logger.error("mobile verification code send fail", e);
			return Response.fail("网络异常,请稍后再试");
		}
	}
	
	
	/**
	 * 预约看房Reservations
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/reservationsshowings")
	public Response reservationsShowings(Map<String, Object> paramMap) {
		ResponsePageBody<ReservationsShowingsEntity> respBody = new ResponsePageBody<ReservationsShowingsEntity>();
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		try {
			// 页码
			String page = CommonUtils.getValue(paramMap, "page");
			// 每页显示条数
			String rows = CommonUtils.getValue(paramMap, "rows");
			rows = StringUtils.isNotBlank(rows) ? "20": rows;
        	page = StringUtils.isNotBlank(page) ? page: "1";
			
            String userid = CommonUtils.getValue(paramMap, "userid");//用户id
            String telphone = CommonUtils.getValue(paramMap, "telphone");//电话
            
            ReservationsShowingsEntity crmety = new ReservationsShowingsEntity();
            if (null != telphone && !telphone.trim().isEmpty()) {
            	crmety.setTelphone(telphone);
            }
            if (null != userid && !userid.trim().isEmpty()) {
            	crmety.setUserId(userid);
            }
            crmety.setRows(Integer.parseInt(rows));
            crmety.setPage(Integer.parseInt(page));
            
            ResponsePageBody<HouseShowingsEntity> resultPage = onlineBookingService.queryReservations(crmety);
			
            if (resultPage == null) {
                respBody.setTotal(0);
                respBody.setStatus(BaseConstants.CommonCode.SUCCESS_CODE);
                return Response.success("在线预约成功", returnMap);
            }
            returnMap.put("total", resultPage.getTotal());
            returnMap.put("rows", resultPage.getRows());
		} catch (Exception e) {
			logger.error("预约看房查询失败", e);
	            respBody.setStatus(BaseConstants.CommonCode.FAILED_CODE);
	            respBody.setMsg("预约看房查询失败");
		}
		return Response.success("在线预约成功", returnMap);
	}

}
