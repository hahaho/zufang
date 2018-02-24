package com.apass.zufang.web.apartment;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.security.toolkit.SpringSecurityUtils;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.zufang.common.utils.FarmartJavaBean;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.entity.Apartment;
import com.apass.zufang.service.apartment.ApartmentService;
import com.apass.zufang.utils.ResponsePageBody;
import com.apass.zufang.utils.ValidateUtils;
/**
 * 公寓信息管理
 * @author Administrator
 *
 */
@Path("/apartment/apartmentController")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ApartmentController {
	private static final Logger LOGGER  = LoggerFactory.getLogger(ApartmentController.class);
	@Autowired
	public ApartmentService apartmentService;
	/**
     * 公寓信息管理页面
     */
    @RequestMapping("/init")
    public String init() {
        return "apartment/apartmentManagement";
    }
    /**
     * 公寓信息列表查询
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getApartmentList")
    public ResponsePageBody<Apartment> getApartmentList(Map<String,Object> map) {
        ResponsePageBody<Apartment> respBody = new ResponsePageBody<Apartment>();
        try {
        	String name = CommonUtils.getValue(map, "name");//公寓名称
        	Apartment entity = new Apartment();
        	entity.setName(name);
        	entity.setIsDelete("00");
        	respBody = apartmentService.getApartmentList(entity);
        } catch (Exception e) {
            LOGGER.error("getApartmentList EXCEPTION --- --->{}", e);
            respBody.setMsg("公寓信息列表查询失败");
        }
        return respBody;
    }
    /**
     * 公寓信息新增
     * @param entity
     * @return
     */
    @ResponseBody
    @RequestMapping("/addApartment")
	public Response addApartment(Map<String, Object> map){
		try{
			LOGGER.info("addApartment map--->{}",GsonUtils.toJson(map));
			Apartment entity = validateParams(map);
			String username = SpringSecurityUtils.getCurrentUser();
			return apartmentService.addApartment(entity,username);
		}catch(BusinessException e){
			LOGGER.error("addApartment EXCEPTION --- --->{}", e);
			return Response.fail("公寓信息新增失败！"+e.getErrorDesc());
		}catch(Exception e){
			LOGGER.error("addApartment EXCEPTION --- --->{}", e);
			return Response.fail("公寓信息新增失败！");
		}
	}
	/**
     * 公寓信息修改
     * @param entity
     * @return
     */
    @ResponseBody
    @RequestMapping("/editApartment")
	public Response editApartment(Map<String, Object> map){
		try{
			LOGGER.info("editApartment map--->{}",GsonUtils.toJson(map));
			Apartment entity = validateParams(map);
			String username = SpringSecurityUtils.getCurrentUser();
			return apartmentService.editApartment(entity,username);
		}catch(Exception e){
			LOGGER.error("editApartment EXCEPTION --- --->{}", e);
			return Response.fail("公寓信息修改失败！");
		}
	}
    /**
     * 验证参数
     * @param map
     * @throws BusinessException
     */
    private Apartment validateParams(Map<String, Object> map) throws BusinessException{
    	Set<Entry<String, Object>> set = map.entrySet();
    	String key = null;
    	Object value =null;
    	Apartment entity = new Apartment();
    	for(Entry<String, Object> entry : set){
    		key = entry.getKey();
    		value = entry.getValue();
    		if(value==null){
    			throw new BusinessException("参数" + key + "为空！");
    		}else{
    			ValidateUtils.isNotBlank(value.toString(), "参数" + key + "为空！");
    		}
    		entity = (Apartment) FarmartJavaBean.farmartJavaB(entity, Apartment.class, value, key);
    	}
    	return entity;
	}
}