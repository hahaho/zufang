package com.apass.zufang.web.house;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.logstash.LOG;
import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.service.house.HouseinitService;
import com.apass.zufang.utils.ValidateUtils;


@Path("/home")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class HouseinitController {
	
    @Autowired
    private HouseinitService houseinitService;
	
    /**
     * init城市
     * @param paramMap
     * @return
     */
	@POST
	@Path("/initCity")
	public Response initCity() {
		
		try {
			HashMap<String, Object> init = houseinitService.init();
			return Response.success("initCity_初始城市地址成功！", init);
		} catch (Exception e) {
			LOG.error("initCity_初始城市地址失败！", e);
			return Response.fail("initCity_初始城市地址失败！");
		}
	}
	
    /**
     * 根据城市获取CODE
     * @param paramMap
     * @return
     */
	@POST
	@Path("/queryCityCode")
	public Response queryCityCode(Map<String, Object> paramMap) {
		
		try {
			String city = (String) paramMap.get("city");// 城市
			
			HashMap<String, Object> init = houseinitService.queryCityCode(city);
			
			return Response.success("获取城市code成功！", init);
		} catch (Exception e) {
			LOG.error("获取城市code失败！", e);
			return Response.fail("获取城市code失败！");
		}
	}
    
	/**
	 * init首页接口
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/initHomePage")
	public Response initHomePage(Map<String, Object> paramMap) {
		
		try {
			LOG.info("initHomePage_" + GsonUtils.toJson(paramMap));
			String city = (String) paramMap.get("city");// 城市
			String pageNum = (String) paramMap.get("pageNum");// 页码
			ValidateUtils.isNotBlank("initHomePage_请求参数丢失数据！", city, pageNum);
			
			HashMap<String, Object> initHomePage = houseinitService.initHomePage(paramMap);
			LOG.info("initHomePage_返回数据:" + GsonUtils.toJson(initHomePage));
			return Response.success("初始首页数据成功！", initHomePage);
		}catch (BusinessException e){
			LOG.error("initHomePage_初始首页数据失败！",e);
			return Response.fail(e.getErrorDesc());
		} catch (Exception e) {
			LOG.error("initHomePage_初始首页数据失败！", e);
			return Response.fail("初始首页数据失败！");
		}
	}

}
