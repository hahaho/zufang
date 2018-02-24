package com.apass.zufang.web.operations;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.security.toolkit.SpringSecurityUtils;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.dto.HouseQueryParams;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.service.operation.BrandApartmentService;
import com.apass.zufang.utils.ResponsePageBody;
import com.apass.zufang.utils.ValidateUtils;
/**
 * 品牌公寓热门房源配置
 * @author Administrator
 *
 */
@Path("/operations/brandApartmentController")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class BrandApartmentController {
	private static final Logger LOGGER  = LoggerFactory.getLogger(BrandApartmentController.class);
	@Autowired
	public BrandApartmentService brandApartmentService;
	/**
	 * 品牌公寓热门房源配置页面
	 * @return
	 */
    @RequestMapping("/init")
    public String init() {
        return "operations/brandApartment";
    }
    /**
     * 品牌公寓热门房源列表查询
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("/getHotHouseList")
    public ResponsePageBody<HouseVo> getHotHouseList(Map<String,Object> map) {
        ResponsePageBody<HouseVo> respBody = new ResponsePageBody<HouseVo>();
        try {
        	String houseType = CommonUtils.getValue(map, "houseType");//房源是否热门
        	String apartmentName = CommonUtils.getValue(map, "apartmentName");//公寓名称
        	String houseTitle = CommonUtils.getValue(map, "houseTitle");//房源名称
        	String houseCode = CommonUtils.getValue(map, "houseCode");//房源编码
        	String houseArea = CommonUtils.getValue(map, "houseArea");//公寓所在区
        	HouseQueryParams entity = new HouseQueryParams();
        	entity.setApartmentName(apartmentName);
        	entity.setHouseTitle(houseTitle);
        	entity.setHouseCode(houseCode);
        	entity.setHouseArea(houseArea);
        	if(StringUtils.isNotBlank(houseType)){
        		entity.setHouseType((byte)Integer.parseInt(houseType));
        	}
        	entity.setIsDelete("00");
        	respBody = brandApartmentService.getHotHouseList(entity);
        } catch (Exception e) {
            LOGGER.error("getHouseList EXCEPTION --- --->{}", e);
            respBody.setMsg("品牌公寓热门房源列表查询失败");
        }
        return respBody;
    }
    /**
     * 品牌公寓热门房源  热门房源上移
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("/hotHouseMoveUp")
    public Response hotHouseMoveUp(Map<String, Object> map) {
        try {
        	LOGGER.info("hotHouseMoveUp map--->{}",GsonUtils.toJson(map));
        	String houseId = CommonUtils.getValue(map, "houseId");
        	ValidateUtils.isNotBlank(houseId, "热门房源ID为空！");
        	String username = SpringSecurityUtils.getCurrentUser();
        	return brandApartmentService.hotHouseMoveUp(houseId,username);
        } catch (BusinessException e) {
            LOGGER.error("hotHouseMoveUp EXCEPTION --- --->{}", e);
            return Response.fail("品牌公寓热门房源,"+e.getErrorDesc());
        }catch (Exception e) {
            LOGGER.error("hotHouseMoveUp EXCEPTION --- --->{}", e);
            return Response.fail("品牌公寓热门房源  热门房源上移失败");
        }
    }
    /**
     * 品牌公寓热门房源  热门房源上移
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("/hotHouseMoveDown")
    public Response hotHouseMoveDown(Map<String, Object> map) {
        try {
        	LOGGER.info("hotHouseMoveDown map--->{}",GsonUtils.toJson(map));
        	String houseId = CommonUtils.getValue(map, "houseId");
        	ValidateUtils.isNotBlank(houseId, "热门房源ID为空！");
        	String username = SpringSecurityUtils.getCurrentUser();
        	return brandApartmentService.hotHouseMoveDown(houseId,username);
        } catch (BusinessException e) {
            LOGGER.error("hotHouseMoveDown EXCEPTION --- --->{}", e);
            return Response.fail("品牌公寓热门房源,"+e.getErrorDesc());
        }catch (Exception e) {
            LOGGER.error("hotHouseMoveDown EXCEPTION --- --->{}", e);
            return Response.fail("品牌公寓热门房源  热门房源下移失败");
        }
    }
    /**
     * 品牌公寓热门房源  热门房源取消设置
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("/hotHouseCancel")
    public Response hotHouseCancel(Map<String, Object> map) {
        try {
        	LOGGER.info("hotHouseCancel map--->{}",GsonUtils.toJson(map));
        	String houseId = CommonUtils.getValue(map, "houseId");
        	ValidateUtils.isNotBlank(houseId, "热门房源ID为空！");
        	String username = SpringSecurityUtils.getCurrentUser();
        	return brandApartmentService.hotHouseCancel(houseId,username);
        } catch (BusinessException e) {
            LOGGER.error("hotHouseCancel EXCEPTION --- --->{}", e);
            return Response.fail("品牌公寓热门房源,"+e.getErrorDesc());
        }catch (Exception e) {
            LOGGER.error("hotHouseCancel EXCEPTION --- --->{}", e);
            return Response.fail("品牌公寓热门房源  热门房源取消设置失败");
        }
    }
    /**
     * 品牌公寓热门房源  设置为热门房源
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("/hotHouseSet")
    public Response hotHouseSet(Map<String, Object> map) {
        try {
        	LOGGER.info("hotHouseSet map--->{}",GsonUtils.toJson(map));
        	String houseId = CommonUtils.getValue(map, "houseId");
        	String sortNo = CommonUtils.getValue(map, "sortNo");
        	String url = CommonUtils.getValue(map, "url");
        	ValidateUtils.isNotBlank(houseId, "热门房源ID为空！");
        	ValidateUtils.isNotBlank(sortNo, "热门房源排序为空！");
        	ValidateUtils.isNotBlank(url, "热门房源图片为空！");
        	String username = SpringSecurityUtils.getCurrentUser();
        	return brandApartmentService.hotHouseSet(houseId,sortNo,url,username);
        } catch (BusinessException e) {
            LOGGER.error("hotHouseSet EXCEPTION --- --->{}", e);
            return Response.fail("品牌公寓热门房源,"+e.getErrorDesc());
        }catch (Exception e) {
            LOGGER.error("hotHouseSet EXCEPTION --- --->{}", e);
            return Response.fail("品牌公寓热门房源  热门房源设置失败");
        }
    }
    /**
     * 品牌公寓热门房源  热门房源编辑
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("/hotHouseEdit")
    public Response hotHouseEdit(Map<String, Object> map) {
        try {
        	LOGGER.info("hotHouseEdit map--->{}",GsonUtils.toJson(map));
        	String houseId = CommonUtils.getValue(map, "houseId");
        	String sortNo = CommonUtils.getValue(map, "sortNo");
        	String url = CommonUtils.getValue(map, "url");
        	ValidateUtils.isNotBlank(houseId, "热门房源ID为空！");
        	ValidateUtils.isNotBlank(sortNo, "热门房源排序为空！");
        	String username = SpringSecurityUtils.getCurrentUser();
        	return brandApartmentService.hotHouseEdit(houseId,sortNo,url,username);
        } catch (BusinessException e) {
            LOGGER.error("hotHouseEdit EXCEPTION --- --->{}", e);
            return Response.fail("品牌公寓热门房源,"+e.getErrorDesc());
        }catch (Exception e) {
            LOGGER.error("hotHouseEdit EXCEPTION --- --->{}", e);
            return Response.fail("品牌公寓热门房源  热门房源设置失败");
        }
    }
}