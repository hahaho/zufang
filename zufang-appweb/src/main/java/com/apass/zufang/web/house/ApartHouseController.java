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

import com.apass.gfb.framework.logstash.LOG;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.service.house.ApartHouseService;
import com.apass.zufang.utils.ValidateUtils;
import com.google.common.collect.Maps;

@Path("/apartHouse")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ApartHouseController {

    @Autowired
    private ApartHouseService apartHouseService;
	
	/**
	 * 查询公寓房源信息
	 * @return
	 */
	@POST
	@Path("/getApartHouse")
	public Response getApartByCity(Map<String, Object> paramMap) {
		
		try {
			HashMap<String, Object> resultMap = apartHouseService.getApartByCity(paramMap);
	
			return Response.success("查询公寓房源信息！", resultMap);
			} catch (Exception e) {
				LOG.error("查询公寓房源信息失败！", e);
				return Response.fail("查询公寓房源信息失败！");
			}
	}

	/**
	 * 查询房源List
	 * @return
	 */
	@POST
	@Path("/getHouseById")
	public Response getHouseById(Map<String, Object> paramMap) {
		
		HashMap<String, Object> resultMap = Maps.newHashMap();
		try {
			
			String houseId = (String) paramMap.get("apartId");
			String pageNum = (String) paramMap.get("pageNum");
			String pageSize = (String) paramMap.get("pageSize");
			ValidateUtils.isNotBlank("查询公寓请求参数丢失数据！", houseId, pageNum);
			
			List<HouseVo> apartList = apartHouseService.getHouseById(houseId, pageNum, pageSize);
			resultMap.put("house", apartList);
			return Response.success("查询房源List成功！", resultMap);
		} catch (Exception e) {
			LOG.error("查询房源List失败！", e);
			return Response.fail("查询房源List失败！");
		}
	}

}
