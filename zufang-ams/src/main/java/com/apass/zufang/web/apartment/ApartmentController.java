package com.apass.zufang.web.apartment;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.security.toolkit.SpringSecurityUtils;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.zufang.common.utils.FarmartJavaBean;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.dto.ApprintmentQueryParams;
import com.apass.zufang.domain.entity.Apartment;
import com.apass.zufang.domain.enums.CityEnums;
import com.apass.zufang.domain.vo.ApartmentVo;
import com.apass.zufang.service.apartment.ApartmentService;
import com.apass.zufang.utils.ResponsePageBody;
/**
 * 公寓管理-公寓信息管理
 * @author haotian
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
	@GET
	@Path("/init")
    public String init() {
        return "apartment/apartmentManagement";
    }
    /**
     * 公寓信息列表查询
     * @param request
     * @return
     */
    @POST
	@Path("/getApartmentList")
    public ResponsePageBody<ApartmentVo> getApartmentList(Map<String,Object> map) {
        ResponsePageBody<ApartmentVo> respBody = new ResponsePageBody<ApartmentVo>();
        try {
        	LOGGER.info("getApartmentList map--->{}",GsonUtils.toJson(map));
        	String name = CommonUtils.getValue(map, "name");//公寓名称
        	String page = CommonUtils.getValue(map, "page");
        	String rows = CommonUtils.getValue(map, "rows");
        	ApprintmentQueryParams entity = new ApprintmentQueryParams();
        	entity.setPage(Integer.parseInt(page));
        	entity.setRows(Integer.parseInt(rows));
        	entity.setName(name);
        	entity.setIsDelete("00");
        	respBody = apartmentService.getApartmentList(entity);
        } catch (BusinessException e) {
            LOGGER.error("getApartmentList EXCEPTION --- --->{}", e);
            respBody.setMsg("公寓信息列表查询失败");
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
    @POST
	@Path("/addApartment")
	public Response addApartment(Map<String, Object> map){
		try{
			LOGGER.info("addApartment map--->{}",GsonUtils.toJson(map));
			String province = CommonUtils.getValue(map, "province");
			String provinceCode = CommonUtils.getValue(map, "provinceCode");
			String cityCode = CommonUtils.getValue(map, "cityCode");
			String areaCode = CommonUtils.getValue(map, "areaCode");
			if(CityEnums.isContains(province)){
				cityCode = provinceCode;//去掉后缀zxs
				areaCode = areaCode.substring(0, areaCode.length()-1);//去掉后缀T
			}
			provinceCode = String.format("%02d",Integer.parseInt(provinceCode));
			provinceCode = provinceCode.substring(provinceCode.length()-2, provinceCode.length());
//			provinceCode = provinceCode.length()==1?"0"+provinceCode:provinceCode;
//			provinceCode = provinceCode.length()>2?provinceCode.substring(provinceCode.length()-2, provinceCode.length()):provinceCode;
			cityCode = String.format("%02d",Integer.parseInt(cityCode));
			cityCode = cityCode.substring(cityCode.length()-2, cityCode.length());
//			cityCode = cityCode.length()==1?"0"+cityCode:cityCode;
//			cityCode = cityCode.length()>2?cityCode.substring(cityCode.length()-2, cityCode.length()):cityCode;
			areaCode = String.format("%02d",Integer.parseInt(areaCode));
			areaCode = areaCode.substring(areaCode.length()-2, areaCode.length());
//			areaCode = areaCode.length()==1?"0"+areaCode:areaCode;
//			areaCode = areaCode.length()>2?areaCode.substring(areaCode.length()-2, areaCode.length()):areaCode;
			String code = provinceCode+cityCode+areaCode;
			Apartment entity = (Apartment) FarmartJavaBean.map2entity(new Apartment(), Apartment.class, map);
			String username = SpringSecurityUtils.getCurrentUser();
			return apartmentService.addApartment(entity,username,code);
//		}catch(BusinessException e){
//			LOGGER.error("addApartment EXCEPTION --- --->{}", e);
//			return Response.fail("公寓信息新增失败,"+e.getErrorDesc());
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
    @POST
   	@Path("/editApartment")
	public Response editApartment(Map<String, Object> map){
		try{
			LOGGER.info("editApartment map--->{}",GsonUtils.toJson(map));
			Apartment entity = (Apartment) FarmartJavaBean.map2entity(new Apartment(), Apartment.class, map);
			String apartmentId = CommonUtils.getValue(map, "id");
			entity.setId(Long.parseLong(apartmentId));
			String username = SpringSecurityUtils.getCurrentUser();
			return apartmentService.editApartment(entity,username);
		}catch(Exception e){
			LOGGER.error("editApartment EXCEPTION --- --->{}", e);
			return Response.fail("公寓信息修改失败！");
		}
	}
}