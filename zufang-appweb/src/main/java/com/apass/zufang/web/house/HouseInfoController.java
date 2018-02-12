package com.apass.zufang.web.house;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.entity.HouseInfoRela;
import com.apass.zufang.service.house.HouseInfoService;
import com.apass.zufang.service.house.HouseService;

@Path("/house")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class HouseInfoController {

	@Autowired
	private HouseService houseService;

	@Autowired
	private HouseInfoService houseInfoService;
	/**
	 * 查根据houseId显示房屋信息
	 * 
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/getHouseInfo")
	public Response getHouseInfo(Map<String, Object> paramMap) {
		try {
			String houseId = CommonUtils.getValue(paramMap, "houseId");
			
			Map<String,Object>  resultMap =new HashMap<String,Object>();
			HouseInfoRela queryCondition=new HouseInfoRela();
			queryCondition.setHouseId(Long.valueOf(houseId));
			List<HouseInfoRela> houseInfoList =houseInfoService.queryHouseInfoRela(queryCondition);
			resultMap.put("houseInfoList", houseInfoList);
			return Response.success("设置密码成功",resultMap);
		} catch (Exception e) {
			return Response.fail("操作失败");
		}
	}
	
	/**
	 * 查询附近房源
	 * 
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/nearbyHouseInfo")
	public Response getNearbyHouseInfo(Map<String, Object> paramMap) {
		try {
			String houseId = CommonUtils.getValue(paramMap, "houseId");
			
			
			
		} catch (Exception e) {
			return Response.fail("操作失败");
		}
		return null;
	}

}
