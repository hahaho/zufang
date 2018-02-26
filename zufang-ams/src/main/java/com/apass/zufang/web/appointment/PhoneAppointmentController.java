package com.apass.zufang.web.appointment;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

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
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.zufang.common.utils.FarmartJavaBean;
import com.apass.zufang.domain.dto.HouseAppointmentQueryParams;
import com.apass.zufang.domain.dto.HouseQueryParams;
import com.apass.zufang.domain.entity.Apartment;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.service.appointment.PhoneAppointmentService;
import com.apass.zufang.utils.ResponsePageBody;
import com.apass.zufang.utils.ValidateUtils;
/**
 * 电话预约
 * @author Administrator
 *
 */
@Path("/appointment/phoneAppointmentController")
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
    public ResponsePageBody<HouseVo> getHouseListForPhoneAppointment(Map<String,Object> map) {
        ResponsePageBody<HouseVo> respBody = new ResponsePageBody<HouseVo>();
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
     * 验证参数
     * @param map
     * @throws BusinessException
     */
    private HouseAppointmentQueryParams validateParams(Map<String, Object> map) throws BusinessException{
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