package com.apass.zufang.web.appointment;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.security.toolkit.SpringSecurityUtils;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.gfb.framework.utils.DateFormatUtil;
import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.zufang.common.utils.FarmartJavaBean;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.dto.ApprintmentJourneyQueryParams;
import com.apass.zufang.domain.entity.ReserveHouse;
import com.apass.zufang.domain.entity.ReturnVisit;
import com.apass.zufang.domain.vo.ReserveHouseVo;
import com.apass.zufang.service.appointment.AppointmentJourneyService;
import com.apass.zufang.utils.ResponsePageBody;
import com.apass.zufang.utils.ValidateUtils;
/**
 * 预约中心-看房行程
 * @author haotian
 *
 */
@Path("/appointment/appointmentJourneyController")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class AppointmentJourneyController {
	private static final Logger LOGGER  = LoggerFactory.getLogger(AppointmentJourneyController.class);
	@Autowired
	public AppointmentJourneyService appointmentJourneyService;
	/**
	 * 预约行程管理页面
	 * @return
	 */
	@GET
	@Path("/init")
    public String init() {
        return "appointment/appointmentJourney";
    }
    /**
     * 预约行程管理 预约看房记录列表查询
     * @param map
     * @return
     */
	@POST
	@Path("/getReserveHouseList")
    public ResponsePageBody<ReserveHouseVo> getReserveHouseList(Map<String,Object> map) {
        ResponsePageBody<ReserveHouseVo> respBody = new ResponsePageBody<ReserveHouseVo>();
        try {
        	LOGGER.info("getReserveHouseList map--->{}",GsonUtils.toJson(map));
        	ApprintmentJourneyQueryParams entity = validateParams(map);
        	respBody = appointmentJourneyService.getReserveHouseList(entity);
        } catch (Exception e) {
            LOGGER.error("getReserveHouseList EXCEPTION --- --->{}", e);
            respBody.setMsg("预约行程管理 预约看房记录列表查询失败");
        }
        return respBody;
    }
	/**
	 * 预约行程管理 预约看房记录编辑
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
    		ValidateUtils.isNotBlank(id, "参数id为空！");
    		
        	String name = CommonUtils.getValue(map, "name");
    		ValidateUtils.isNotBlank(name, "参数name为空！");
    		
        	String reserveDate = CommonUtils.getValue(map, "reserveDate");
    		ValidateUtils.isNotBlank(reserveDate, "参数reserveDate为空！");
    		Date date = DateFormatUtil.string2date(reserveDate,DateFormatUtil.YYYY_MM_DD_HH_MM_SS);
    		
    		String memo = CommonUtils.getValue(map, "memo");
    		
    		ReserveHouse entity = new ReserveHouse();
    		entity.setId(Long.parseLong(id));
    		entity.setName(name);
    		entity.setReserveDate(date);
    		entity.setMemo(memo);
        	return appointmentJourneyService.editReserveHouse(entity,username,date);
        } catch (Exception e) {
            LOGGER.error("editReserveHouse EXCEPTION --- --->{}", e);
            return Response.fail("预约行程管理 预约看房记录编辑失败！");
        }
    }
	/**
	 * 预约行程管理 预约看房记录删除
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
        } catch (Exception e) {
            LOGGER.error("deleReserveHouse EXCEPTION --- --->{}", e);
            return Response.fail("预约行程管理 预约看房记录删除失败！");
        }
    }
	/**
	 * 预约行程管理 客户回访记录新增
	 * @param map
	 * @return
	 */
	@POST
	@Path("/addReturnVisit")
    public Response addReturnVisit(Map<String,Object> map) {
        try {
        	LOGGER.info("addReturnVisit map--->{}",GsonUtils.toJson(map));
        	String username = SpringSecurityUtils.getCurrentUser();
        	ReturnVisit entity = new ReturnVisit();
        	
        	String houseId = CommonUtils.getValue(map, "houseId");
        	ValidateUtils.isNotBlank(houseId, "参数houseId为空！");
        	String reserveHouseId = CommonUtils.getValue(map, "reserveHouseId");
        	ValidateUtils.isNotBlank(reserveHouseId, "参数reserveHouseId为空！");
        	entity.setHouseId(Long.parseLong(houseId));
        	entity.setReserveHouseId(Long.parseLong(reserveHouseId));
        	
        	String visitStatus = CommonUtils.getValue(map, "visitStatus");
        	ValidateUtils.isNotBlank(visitStatus, "参数visitStatus为空！");
        	String rentStatus = CommonUtils.getValue(map, "rentStatus");
        	ValidateUtils.isNotBlank(rentStatus, "参数rentStatus为空！");
        	byte visit = (byte)1;
        	if(StringUtils.equals("未看房", visitStatus)){
        		visit = (byte)1;
        	}else if(StringUtils.equals("不满意", visitStatus)){
        		visit = (byte)2;
        	}else if(StringUtils.equals("满意", visitStatus)){
        		visit = (byte)3;
        	}else if(StringUtils.equals("时间延后 ", visitStatus)){
        		visit = (byte)4;
        	}
        	byte rent = (byte)1;
        	if(StringUtils.equals("否", rentStatus)){
        		rent = (byte)0;
        	}else if(StringUtils.equals("是", rentStatus)){
        		rent = (byte)1;
        	}
        	entity.setVisitStatus(visit);
        	entity.setRentStatus(rent);
        	
        	String feedBack = CommonUtils.getValue(map, "feedBack");
        	String memo = CommonUtils.getValue(map, "memo");
        	entity.setFeedBack(feedBack);
        	entity.setMemo(memo);
        	
        	return appointmentJourneyService.addReturnVisit(entity,username);
        } catch (Exception e) {
            LOGGER.error("addReturnVisit EXCEPTION --- --->{}", e);
            return Response.fail("预约行程管理 客户回访记录新增！");
        }
    }
	/**
	 * 预约行程管理 看房记录导出
	 * @param map
	 * @return
	 */
	@POST
	@Path("/downLoadReserveHouseList")
	public Response downLoadReserveHouseList(Map<String,Object> map){
		try{
			LOGGER.info("downLoadReserveHouseList map--->{}",GsonUtils.toJson(map));
			ApprintmentJourneyQueryParams entity = validateParams(map);
	    	return appointmentJourneyService.downLoadReserveHouseList(entity);
		}catch(Exception e){
			LOGGER.error("downLoadReserveHouseList EXCEPTION --- --->{}", e);
			return Response.fail("预约行程管理 看房记录导出失败！");
		}
	}
	/**
     * 验证参数
     * @param map
     * @throws BusinessException
     */
    private ApprintmentJourneyQueryParams validateParams(Map<String, Object> map) {
    	Set<Entry<String, Object>> set = map.entrySet();
    	String key = null;
    	Object value =null;
    	ApprintmentJourneyQueryParams entity = new ApprintmentJourneyQueryParams();
    	for(Entry<String, Object> entry : set){
    		key = entry.getKey();
    		value = entry.getValue();
    		if(value!=null&&StringUtils.isNotBlank(value.toString())){
				entity = (ApprintmentJourneyQueryParams) FarmartJavaBean.farmartJavaB(entity, ApprintmentJourneyQueryParams.class, value, key);
    		}
    	}
    	return entity;
	}
}