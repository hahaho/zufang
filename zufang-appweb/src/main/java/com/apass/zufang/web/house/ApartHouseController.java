package com.apass.zufang.web.house;

import java.util.ArrayList;
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
import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.entity.ApartHouseList;
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
	 * initImg
	 * @return
	 */
	@POST
	@Path("/initImg")
	public Response initImg() {
		try {
			HashMap<String, Object> resultMap = Maps.newHashMap();
			// 获取市区
			List<String> imgList = apartHouseService.initImg();
			resultMap.put("initImg", imgList);
			return Response.success("初始公寓轮播图成功！", GsonUtils.toJson(resultMap));
		} catch (Exception e) {
			LOG.error("初始公寓轮播图失败！", e);
			return Response.fail("初始公寓轮播图失败！");
		}
	}
	
	/**
	 * 查询公寓房源信息
	 * @return
	 */
	@POST
	@Path("/getApartHouse")
	public Response getApartByCity(Map<String, Object> paramMap) {
		
		HashMap<String, Object> resultMap = Maps.newHashMap();
		try {
			ArrayList<ApartHouseList> apartHouseList = apartHouseService.getApartByCity(paramMap);
	
			resultMap.put("apartHouse", apartHouseList);
			return Response.success("查询公寓房源信息！", GsonUtils.toJson(resultMap));
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
			ValidateUtils.isNotBlank("查询公寓请求参数丢失数据！", houseId, pageNum);
			
			List<HouseVo> apartList = apartHouseService.getHouseById(houseId, pageNum);
			resultMap.put("house", apartList);
			return Response.success("查询房源List成功！", GsonUtils.toJson(resultMap));
		} catch (Exception e) {
			LOG.error("查询房源List失败！", e);
			return Response.fail("查询房源List失败！");
		}
	}

}
