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
	@Path("/init")
	public Response initCity() {
		
		try {
			HashMap<String, Object> init = houseinitService.init();
			return Response.success("初始城市地址成功！", GsonUtils.toJson(init));
		} catch (Exception e) {
			LOG.error("初始城市地址失败！", e);
			return Response.fail("初始城市地址失败！");
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
			String city = (String) paramMap.get("city");// 城市
			String pageNum = (String) paramMap.get("pageNum");// 页码
			ValidateUtils.isNotBlank("初始首页请求参数丢失数据！", city, pageNum);
			
			HashMap<String, Object> initHomePage = houseinitService.initHomePage(paramMap);
			return Response.success("初始首页数据成功！", initHomePage);
		}catch (BusinessException e){
			LOG.error("初始首页数据失败！",e);
			return Response.fail(e.getErrorDesc());
		} catch (Exception e) {
			LOG.error("初始首页数据失败！", e);
			return Response.fail("初始首页数据失败！");
		}
	}

}
