package com.apass.zufang.web.operations;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apass.gfb.framework.security.toolkit.SpringSecurityUtils;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.zufang.domain.Response;
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
     */
    @RequestMapping("/init")
    public String init() {
        return "operations/brandApartment";
    }
    /**
     * 品牌公寓热门房源列表查询
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getHotHouseList")
    public ResponsePageBody<HouseVo> getHotHouseList(HouseVo entity) {
        ResponsePageBody<HouseVo> respBody = new ResponsePageBody<HouseVo>();
        try {
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
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/hotHouseMoveUp")
    public Response hotHouseMoveUp(Map<String, Object> map) {
        try {
        	String houseId = CommonUtils.getValue(map, "houseId");
        	ValidateUtils.isNotBlank(houseId, "热门房源ID为空！");
        	String username = SpringSecurityUtils.getCurrentUser();
        	return brandApartmentService.hotHouseMoveUp(houseId,username);
        } catch (Exception e) {
            LOGGER.error("hotHouseMoveUp EXCEPTION --- --->{}", e);
            return Response.fail("品牌公寓热门房源  热门房源上移失败");
        }
    }
}