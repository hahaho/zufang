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
import com.apass.zufang.domain.dto.HouseAppointmentQueryParams;
import com.apass.zufang.domain.entity.ReserveHouse;
import com.apass.zufang.domain.vo.HouseAppointmentVo;
import com.apass.zufang.service.appointment.PhoneAppointmentService;
import com.apass.zufang.utils.ResponsePageBody;
import com.apass.zufang.utils.ValidateUtils;
/**
 * 电话预约
 * @author Administrator
 *
 */
@Path("/application/appointment/phoneAppointmentController")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class PhoneAppointmentController {
	private static final Logger LOGGER  = LoggerFactory.getLogger(PhoneAppointmentController.class);
	@Autowired
	public PhoneAppointmentService phoneAppointmentService;
	/**
	 * 电话预约管理页面
	 * @return
	 */
	@GET
	@Path("/init")
    public String init() {
        return "appointment/phoneAppointment";
    }
	/**
     * 电话预约管理 房源列表查询
     * @param map
     * @return
     */
	@POST
	@Path("/getHouseListForPhoneAppointment")
    public ResponsePageBody<HouseAppointmentVo> getHouseListForPhoneAppointment(Map<String,Object> map) {
        ResponsePageBody<HouseAppointmentVo> respBody = new ResponsePageBody<HouseAppointmentVo>();
        try {
        	LOGGER.info("getHouseListForPhoneAppointment map--->{}",GsonUtils.toJson(map));
        	HouseAppointmentQueryParams entity = validateParams(map);
        	respBody = phoneAppointmentService.getHouseListForPhoneAppointment(entity);
        } catch (Exception e) {
            LOGGER.error("getHouseListForPhoneAppointment EXCEPTION --- --->{}", e);
            respBody.setMsg("电话预约管理 房源列表查询失败");
        }
        return respBody;
    }
    /**
     * 电话预约管理 预约看房记录新增
     * @param map
     * @return
     */
    @POST
	@Path("/addReserveHouse")
    public Response addReserveHouse(Map<String, Object> map) {
    	try {
    		LOGGER.info("addApartment map--->{}",GsonUtils.toJson(map));
    		String houseId = CommonUtils.getValue(map, "houseId");
    		ValidateUtils.isNotBlank(houseId, "参数houseId为空！");
    		String userId = CommonUtils.getValue(map, "userId");
    		ValidateUtils.isNotBlank(userId, "参数userId为空！");
    		
    		String telphone = CommonUtils.getValue(map, "telphone");
    		ValidateUtils.isNotBlank(telphone, "参数telphone为空！");
    		String name = CommonUtils.getValue(map, "name");
    		ValidateUtils.isNotBlank(name, "参数name为空！");
    		
    		String reserveDate = CommonUtils.getValue(map, "reserveDate");
    		ValidateUtils.isNotBlank(reserveDate, "参数reserveDate为空！");
    		String memo = CommonUtils.getValue(map, "memo");
    		
    		ReserveHouse entity = new ReserveHouse();
    		entity.setHouseId(Long.parseLong(houseId));
    		entity.setUserId(userId);
    		
    		entity.setTelphone(telphone);
    		entity.setName(name);
    		entity.setMemo(memo);
    		Date date = DateFormatUtil.string2date(reserveDate,DateFormatUtil.YYYY_MM_DD_HH_MM_SS);
    		String username = SpringSecurityUtils.getCurrentUser();
    		return phoneAppointmentService.addReserveHouse(entity,username,date);
    	} catch (Exception e) {
    		LOGGER.error("getHouseListForPhoneAppointment EXCEPTION --- --->{}", e);
    		return Response.fail("电话预约管理 预约看房失败！");
    	}
    }
    /**
     * 验证参数
     * @param map
     * @throws BusinessException
     */
    private HouseAppointmentQueryParams validateParams(Map<String, Object> map) {
    	Set<Entry<String, Object>> set = map.entrySet();
    	String key = null;
    	Object value =null;
    	HouseAppointmentQueryParams entity = new HouseAppointmentQueryParams();
    	for(Entry<String, Object> entry : set){
    		key = entry.getKey();
    		value = entry.getValue();
    		if(value!=null){
    			if(StringUtils.isNotBlank(value.toString())){
    				entity = (HouseAppointmentQueryParams) FarmartJavaBean.farmartJavaB(entity, HouseAppointmentQueryParams.class, value, key);
    			}
    		}
    	}
    	return entity;
	}
}