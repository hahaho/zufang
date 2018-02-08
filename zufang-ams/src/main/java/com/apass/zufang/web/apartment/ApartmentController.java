package com.apass.zufang.web.apartment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.apass.gfb.framework.security.toolkit.SpringSecurityUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.entity.Apartment;
import com.apass.zufang.service.apartment.ApartmentService;
import com.apass.zufang.utils.ResponsePageBody;
/**
 * 公寓信息管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/apartment/apartmentController")
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
    public ResponsePageBody<Apartment> getApartmentList(Apartment entity) {
        ResponsePageBody<Apartment> respBody = new ResponsePageBody<Apartment>();
        try {
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
	public Response addApartment(@RequestBody Apartment entity){
		try{
			String username = SpringSecurityUtils.getCurrentUser();
			return apartmentService.addApartment(entity,username);
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
	public Response editApartment(@RequestBody Apartment entity){
		try{
			String username = SpringSecurityUtils.getCurrentUser();
			return apartmentService.editApartment(entity,username);
		}catch(Exception e){
			LOGGER.error("editApartment EXCEPTION --- --->{}", e);
			return Response.fail("公寓信息修改失败！");
		}
	}
}