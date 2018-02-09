package com.apass.zufang.web.operations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.apass.zufang.domain.entity.House;
import com.apass.zufang.service.operation.BrandApartmentService;
import com.apass.zufang.utils.ResponsePageBody;
/**
 * 品牌公寓热门房源配置
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/operations/brandApartmentController")
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
    @RequestMapping("/getHouseList")
    public ResponsePageBody<House> getHouseList(House entity) {
        ResponsePageBody<House> respBody = new ResponsePageBody<House>();
        try {
        	entity.setIsDelete("00");
        	respBody = brandApartmentService.getHouseList(entity);
        } catch (Exception e) {
            LOGGER.error("getHouseList EXCEPTION --- --->{}", e);
            respBody.setMsg("品牌公寓热门房源列表查询失败");
        }
        return respBody;
    }
}