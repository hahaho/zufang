package com.apass.zufang.web.onlinebooking;
import java.util.Date;
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
import com.apass.gfb.framework.security.toolkit.SpringSecurityUtils;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.gfb.framework.utils.DateFormatUtil;
import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.ajp.entity.GfbRegisterInfoEntity;
import com.apass.zufang.domain.constants.ConstantsUtil;
import com.apass.zufang.domain.entity.HouseShowingsEntity;
import com.apass.zufang.domain.entity.ReserveHouse;
import com.apass.zufang.domain.vo.ReservationsShowingsEntity;
import com.apass.zufang.service.appointment.AppointmentJourneyService;
import com.apass.zufang.service.appointment.OnlineAppointmentService;
import com.apass.zufang.service.common.MobileSmsService;
import com.apass.zufang.service.personal.ZuFangLoginSevice;
import com.apass.zufang.utils.ResponsePageBody;
import com.apass.zufang.utils.ValidateUtils;
/**
 * APP端在线预约看房模块
 */
@Path("/onlinebooking")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class OnlineBookingController {
	private static final Logger LOGGER = LoggerFactory.getLogger(OnlineBookingController.class);
	@Autowired
	private OnlineAppointmentService onlineAppointmentService;
	@Autowired
	public TokenManager tokenManager;
	@Autowired
	private ZuFangLoginSevice zuFangLoginSevice;
	@Autowired
	private MobileSmsService mobileRandomService;
	@Autowired
	public AppointmentJourneyService appointmentJourneyService;
	/**
	 * 在线预约看房新增
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/insertshowings")
	public Response insertshowings(Map<String, Object> paramMap) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			LOGGER.info("insertshowings map--->{}",GsonUtils.toJson(paramMap));
			String houseId = CommonUtils.getValue(paramMap, "houseId");// 房源主键
			String userId = CommonUtils.getValue(paramMap, "userId");// 用户id
			String mobile = CommonUtils.getValue(paramMap, "mobile");// 手机号
			String name = CommonUtils.getValue(paramMap, "name");// 姓名
			String reservedate = CommonUtils.getValue(paramMap, "reservedate");// 看房时间
			String memo = CommonUtils.getValue(paramMap, "memo");// 留言
			String smsType = CommonUtils.getValue(paramMap, "smsType");// 短信类型
			String code = CommonUtils.getValue(paramMap, "code");// 验证码
			ValidateUtils.isNotBlank(mobile, "手机号码不可为空！");
			ValidateUtils.isNotBlank(houseId, "房源主键不可为空！");
			ValidateUtils.isNotBlank(reservedate, "看房时间不可为空！");
			ValidateUtils.isNotBlank(name, "用户姓名不可为空！");
			String user = SpringSecurityUtils.getCurrentUser();
			if (StringUtils.isBlank(userId)) {//未登录判断
				ValidateUtils.isNotBlank(smsType, "类型不可为空！");
				ValidateUtils.isNotBlank(code, "验证码不可为空！");
				boolean codeFalg = mobileRandomService.getCode(smsType,mobile);
				boolean mobileFalg = mobileRandomService.mobileCodeValidate(smsType,mobile,code);
	        	if(codeFalg){
	        		throw new BusinessException("验证码已失效，请重新获取!");
	        	}
		        if(!mobileFalg){
		        	throw new BusinessException("验证码错误，请重新输入!");
			    }
		        //用户是否已经注册  未登录
        		GfbRegisterInfoEntity zfselecetmobile = zuFangLoginSevice.zfselecetmobile(mobile);
        		if(zfselecetmobile == null){
        			GfbRegisterInfoEntity gfbRegisterInfoEntity = new GfbRegisterInfoEntity();
        			gfbRegisterInfoEntity.setAccount(mobile);
        			// 插入数据库
        			Long saveRegisterInfo = zuFangLoginSevice.saveRegisterInfo(gfbRegisterInfoEntity);
        			Integer insetReserveHouse = onlineAppointmentService.insetReserveHouse(houseId, saveRegisterInfo.toString(), mobile, name, reservedate,memo,user);
        			// 生成token
					String token = tokenManager.createToken(String.valueOf(saveRegisterInfo), mobile, ConstantsUtil.TOKEN_EXPIRES_SPACE);
					returnMap.put("token", token);
					returnMap.put("account", mobile);
					returnMap.put("userId", saveRegisterInfo);
					returnMap.put("Password", "no");
					if(insetReserveHouse == 1){
						return Response.success("在线预约成功", returnMap);
					}else{
						throw new BusinessException("您近期行程已排满，暂时不能预约!");
					}
        		}else{
        			//已经注册    未登录
        			GfbRegisterInfoEntity zfselecetmobile2 = zuFangLoginSevice.zfselecetmobile(mobile);
        			//是否已经预约过
                    Integer queryOverdue = onlineAppointmentService.queryOverdue(mobile,houseId);
                    if(queryOverdue == null || queryOverdue ==0 ){
                    	Integer insetReserveHouse = onlineAppointmentService.insetReserveHouse(houseId, zfselecetmobile2.getId().toString(), mobile, name, reservedate,memo,user);
                    	// 生成token
                    	String token = tokenManager.createToken(String.valueOf(zfselecetmobile2.getId()), mobile,ConstantsUtil.TOKEN_EXPIRES_SPACE);
                    	returnMap.put("token", token);
                    	returnMap.put("account", mobile);
                    	returnMap.put("userId", zfselecetmobile2.getId());
                    	returnMap.put("Password", zfselecetmobile2.getPassword() == null||zfselecetmobile2.getPassword()== "" ? "no" :  "yes");
                    	if(insetReserveHouse == 1){
                    		return Response.success("在线预约成功", returnMap);
                    	}else{
                    		throw new BusinessException("您近期行程已排满，暂时不能预约!");
                    	}
                    }else{
                    	throw new BusinessException("您已经预约该房源!");
                    }
        		}
			} else {//已登录判断
				//是否已经预约过
                Integer queryOverdue = onlineAppointmentService.queryOverdue(mobile,houseId);
                if(queryOverdue == null || queryOverdue ==0 ){
					Integer insetReserveHouse = onlineAppointmentService.insetReserveHouse(houseId, userId, mobile, name, reservedate, memo,user);
					if(insetReserveHouse==1){
						// 生成token
						String token = tokenManager.createToken(String.valueOf(userId), mobile,ConstantsUtil.TOKEN_EXPIRES_SPACE);
						returnMap.put("token", token);
						returnMap.put("ACCOUNT", mobile);
						returnMap.put("userId", userId);
						return Response.success("在线预约成功", returnMap);
					}else{
						throw new BusinessException("您近期行程已排满，暂时不能预约!");
					}
                }else{
                	throw new BusinessException("您已经预约该房源!");
                }
			}
		} catch (BusinessException e) {
			LOGGER.error("insertshowings BUSINESSEXCEPTION", e);
			return Response.fail("在线预约看房失败，"+e.getErrorDesc());
		} catch (Exception e) {
			LOGGER.error("insertshowings Exception", e);
			return Response.fail("网络异常,请稍后再试");
		}
	}
	/**
	 * 在线预约看房记录查询
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/reservationsshowings")
	public Response reservationsShowings(Map<String, Object> paramMap) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			LOGGER.info("reservationsShowings map--->{}",GsonUtils.toJson(paramMap));
			String page = CommonUtils.getValue(paramMap, "page");
			String rows = CommonUtils.getValue(paramMap, "rows");
			rows = StringUtils.isNotBlank(rows) ? "20": rows;
        	page = StringUtils.isNotBlank(page) ? page: "1";
            String userid = CommonUtils.getValue(paramMap, "userid");//用户id
            String telphone = CommonUtils.getValue(paramMap, "telphone");//电话
            ReservationsShowingsEntity crmety = new ReservationsShowingsEntity();
            crmety.setRows(Integer.parseInt(rows));
            crmety.setPage(Integer.parseInt(page));
            if (telphone!=null) {
            	crmety.setTelphone(telphone);
            }
            if (userid!=null) {
            	crmety.setUserId(userid);
            }
            crmety.setReserveStatus("3");
            ResponsePageBody<HouseShowingsEntity> resultPage = onlineAppointmentService.queryReservations(crmety);
/*            if (resultPage == null) {
                respBody.setTotal(0);
                respBody.setStatus(BaseConstants.CommonCode.SUCCESS_CODE);
                return Response.success("在线预约记录查询成功", returnMap);
            }*/
            returnMap.put("total", resultPage.getTotal());
            returnMap.put("rows", resultPage.getRows());
            return Response.success("在线预约记录查询成功", returnMap);
		} catch (BusinessException e) {
			LOGGER.error("reservationsshowings BusinessException", e);
			return Response.fail("在线预约记录查询失败", returnMap);
		} catch (Exception e) {
			LOGGER.error("reservationsshowings Exception", e);
			return Response.fail("在线预约记录查询失败", returnMap);
		}
	}
	/**
	 * 功能未知。。。
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/whetherabout")
	public Response whetherabout(Map<String, Object> paramMap) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			LOGGER.info("whetherabout map--->{}",GsonUtils.toJson(paramMap));
            String houseId = CommonUtils.getValue(paramMap, "houseId");//用户id
            String telphone = CommonUtils.getValue(paramMap, "telphone");//电话
            ValidateUtils.isNotBlank(telphone, "电话号码不可为空！");
            ValidateUtils.isNotBlank(houseId, "房源主键不可为空！");
            ReservationsShowingsEntity crmety = new ReservationsShowingsEntity();
            crmety.setTelphone(telphone);
            crmety.setUserId(houseId);
            Integer resultPage = onlineAppointmentService.queryOverdue(telphone,houseId);
            if(resultPage < 1){
            	returnMap.put("isabout", "no");
            	return Response.success("没预约过房源", returnMap);
            }else{
            	returnMap.put("isabout", "yes");
            	return Response.fail("已经有预约房源",returnMap);
            }
		} catch (Exception e) {
			LOGGER.error("预约看房查询失败", e);
			returnMap.put("isabout", "yes");
			return Response.fail("预约看房查询失败",returnMap);
		}
	}
	/**
	 * 预约行程管理 预约看房记录删除  (取消)
	 * @param map
	 * @return
	 */
	@POST
	@Path("/deleReserveHouse")
    public Response deleReserveHouse(Map<String,Object> map) {
        try {
        	LOGGER.info("deleReserveHouse map--->{}",GsonUtils.toJson(map));
        	String username = SpringSecurityUtils.getCurrentUser();
        	String reserveHouseId = CommonUtils.getValue(map, "id");//预约看房记录ID
        	return appointmentJourneyService.deleReserveHouse(reserveHouseId,username);
        }catch (BusinessException e){
        	LOGGER.error("deleReserveHouse BUSINESSEXCEPTION---->{}",e);
			return Response.fail("预约行程管理 预约看房删除失败！"+e.getErrorDesc());
        } catch (Exception e) {
            LOGGER.error("deleReserveHouse EXCEPTION --- --->{}", e);
            return Response.fail("预约行程管理 预约看房删除失败！");
        }
    }
	/**
	 * 预约行程管理 预约看房记录编辑  只预留编辑看房时间接口
	 * @param map
	 * @return
	 */
	@POST
	@Path("/editReserveHouse")
    public Response editReserveHouse(Map<String,Object> map) {
        try {
        	LOGGER.info("editReserveHouse map--->{}",GsonUtils.toJson(map));
        	String username = SpringSecurityUtils.getCurrentUser();
        	String id = CommonUtils.getValue(map, "id");
    		ValidateUtils.isNotBlank(id, "看房记录不可为空！");
        	String reserveDate = CommonUtils.getValue(map, "reserveDate");
    		ValidateUtils.isNotBlank(reserveDate, "看房时间不可为空！");
    		Date date = DateFormatUtil.string2date(reserveDate+":00",DateFormatUtil.YYYY_MM_DD_HH_MM_SS);
    		ReserveHouse entity = new ReserveHouse();
    		entity.setId(Long.parseLong(id));
        	return appointmentJourneyService.editReserveHouse(entity,username,date);
        }catch (BusinessException e){
        	LOGGER.error("editReserveHouse BUSINESSEXCEPTION---->{}",e);
			return Response.fail("预约行程管理 预约看房编辑失败！"+e.getErrorDesc());
        } catch (Exception e) {
            LOGGER.error("editReserveHouse EXCEPTION --- --->{}", e);
            return Response.fail("预约行程管理 预约看房编辑失败！");
        }
    }
	/**
	 * 在线预约看房   看房历史记录查询
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/getReserveHouseHistoryList")
	public Response getReserveHouseHistoryList(Map<String, Object> paramMap) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			LOGGER.info("getReserveHouseHistoryList map--->{}",GsonUtils.toJson(paramMap));
			String page = CommonUtils.getValue(paramMap, "page");
			String rows = CommonUtils.getValue(paramMap, "rows");
			rows = StringUtils.isNotBlank(rows) ? "20": rows;
        	page = StringUtils.isNotBlank(page) ? page: "1";
            String userid = CommonUtils.getValue(paramMap, "userid");//用户id
            String telphone = CommonUtils.getValue(paramMap, "telphone");//电话
            ReservationsShowingsEntity crmety = new ReservationsShowingsEntity();
            crmety.setRows(Integer.parseInt(rows));
            crmety.setPage(Integer.parseInt(page));
            if (telphone!=null) {
            	crmety.setTelphone(telphone);
            }
            if (userid!=null) {
            	crmety.setUserId(userid);
            }
            ResponsePageBody<HouseShowingsEntity> resultPage = onlineAppointmentService.queryReservations(crmety);
/*            if (resultPage == null) {
                respBody.setTotal(0);
                respBody.setStatus(BaseConstants.CommonCode.SUCCESS_CODE);
                return Response.success("在线预约记录查询成功", returnMap);
            }*/
            returnMap.put("total", resultPage.getTotal());
            returnMap.put("rows", resultPage.getRows());
            return Response.success("在线预约看房历史记录查询成功", returnMap);
		} catch (BusinessException e) {
			LOGGER.error("reservationsshowings BusinessException", e);
			return Response.fail("在线预约看房历史记录查询失败", returnMap);
		} catch (Exception e) {
			LOGGER.error("reservationsshowings Exception", e);
			return Response.fail("在线预约看房历史记录查询失败", returnMap);
		}
	}
}