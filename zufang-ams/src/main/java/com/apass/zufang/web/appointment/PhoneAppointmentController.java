package com.apass.zufang.web.appointment;
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
    		ReserveHouse entity = validateParams2(map);
    		String username = SpringSecurityUtils.getCurrentUser();
    		return phoneAppointmentService.addReserveHouse(entity,username);
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
    private ReserveHouse validateParams2(Map<String, Object> map) throws BusinessException{
    	Set<Entry<String, Object>> set = map.entrySet();
    	String key = null;
    	Object value =null;
    	ReserveHouse entity = new ReserveHouse();
    	for(Entry<String, Object> entry : set){
    		key = entry.getKey();
    		value = entry.getValue();
    		if(value==null){
    			throw new BusinessException("参数" + key + "为空！");
    		}else{
    			ValidateUtils.isNotBlank(value.toString(), "参数" + key + "为空！");
    		}
    		entity = (ReserveHouse) FarmartJavaBean.farmartJavaB(entity, ReserveHouse.class, value, key);
    	}
    	return entity;
	}
}