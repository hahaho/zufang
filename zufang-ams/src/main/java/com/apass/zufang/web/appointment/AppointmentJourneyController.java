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
import com.apass.zufang.common.utils.FarmartJavaBean;
import com.apass.zufang.domain.dto.ApprintmentJourneyQueryParams;
import com.apass.zufang.domain.vo.ReserveHouseVo;
import com.apass.zufang.service.appointment.AppointmentJourneyService;
import com.apass.zufang.utils.ResponsePageBody;
/**
 * 预约行程
 * @author Administrator
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
        	ApprintmentJourneyQueryParams entity = validateParams(map);
        	respBody = appointmentJourneyService.getReserveHouseList(entity);
        } catch (Exception e) {
            LOGGER.error("getReserveHouseList EXCEPTION --- --->{}", e);
            respBody.setMsg("预约行程管理 预约看房记录列表查询失败");
        }
        return respBody;
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
    		if(value!=null){
    			if(StringUtils.isNotBlank(value.toString())){
    				entity = (ApprintmentJourneyQueryParams) FarmartJavaBean.farmartJavaB(entity, ApprintmentJourneyQueryParams.class, value, key);
    			}
    		}
    	}
    	return entity;
	}
}