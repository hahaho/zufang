package com.apass.zufang.web.house;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.logstash.LOG;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.entity.HouseLocation;
import com.apass.zufang.domain.enums.RentTypeEnums;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.service.commons.CommonService;
import com.apass.zufang.service.house.HouseinitService;
import com.apass.zufang.utils.ValidateUtils;
import com.google.common.collect.Maps;


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
			// 获取市区
			List<String> cityList = houseinitService.initCity();
			return Response.success("初始城市地址成功！", GsonUtils.toJson(cityList));
		} catch (Exception e) {
			LOG.error("初始城市地址失败！", e);
			return Response.fail("初始城市地址失败！");
		}
	}
	
	/**
	 * initImg
	 * @return
	 */
	@POST
	@Path("/initImg")
	public Response initImg() {
		try {
			// 获取市区
			List<String> cityList = houseinitService.initImg();
			return Response.success("初始城市地址成功！", GsonUtils.toJson(cityList));
		} catch (Exception e) {
			LOG.error("初始城市地址失败！", e);
			return Response.fail("初始城市地址失败！");
		}
	}

	/**
	 * init热门房源
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/initHotHouseList")
	public Response initHotHouse(Map<String, Object> paramMap) {
		
		try {
			
			String city = CommonUtils.getValue(paramMap, "city");// 城市
			ValidateUtils.isNotBlank("请求参数丢失数据", city);
			
			HashMap<String, String> map = Maps.newHashMap();
			map.put("city", city);
			map.put("type", RentTypeEnums.FY_JINGXUAN_2.getCode().toString());
			
			// 从后台配置读取热门房源
			List<HouseVo> searchPeizhi = houseinitService.initHouseByCity(map);
//			if(ValidateUtils.listIsTrue(searchPeizhi)){
//				//不为空的情况
//				if (searchPeizhi.size() < 6) {
//					List<HouseVo> hotHouseList = houseinitService.initHotLocation(houseLocation);
//					if (!ValidateUtils.listIsTrue(hotHouseList)) {
//						if (hotHouseList.size() > 6) {
//							hotHouseList = searchPeizhi.subList(0, 7);
//						}
//					for (int i=0; i<6-hotHouseList.size(); i++) {
//						searchPeizhi.add(hotHouseList.get(i));
//					}
//				}
//				}else {
//					searchPeizhi = searchPeizhi.subList(0, 7);
//				}
//				}else{
//				//为空的情况 按照按浏览量（7天）降序排列房源
//				searchPeizhi = houseinitService.initHotLocation(houseLocation);
//				}
			if (!ValidateUtils.listIsTrue(searchPeizhi)) {
				map.put("type", RentTypeEnums.FY_ZHENGCHANG_1.getCode().toString());
				searchPeizhi = houseinitService.initHouseByCity(map);
			}
			return Response.success("初始热门房源成功！", GsonUtils.toJson(searchPeizhi));
		}catch (BusinessException e){
			LOG.error("初始热门房源失败！",e);
			return Response.fail(e.getErrorDesc());
		} catch (Exception e) {
			LOG.error("初始热门房源失败！", e);
			return Response.fail("初始热门房源失败！");
		}
	}
	
	/**
	 * init推荐房源
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/initNearHouseList")
	public Response initNearHouse(Map<String, Object> paramMap) {
		
		try {
			
			// 获取用户当前经纬度追加2公里经纬度,
			List<HouseVo> initNearHouse = null;
			String longitude = (String) paramMap.get("longitude");// 经度
			String latitude = (String) paramMap.get("latitude");// 维度
			if (StringUtils.isAnyBlank(longitude, latitude)) {
				String city = (String) paramMap.get("city");
				HouseLocation houseLocation = new HouseLocation();
				houseLocation.setCity(city);
				
				HashMap<String, String> map = Maps.newHashMap();
				map.put("city", city);
				map.put("type", RentTypeEnums.FY_ZHENGCHANG_1.getCode().toString());
				// #林去除按流量排进热门的数据
			}else{
				ValidateUtils.isNotBlank("请求参数丢失数据", longitude, latitude);
				Map<String, Double> returnLLSquarePoint = CommonService.renturnLngLat(new Double(longitude), new Double(latitude), new Double(2000));
				initNearHouse = houseinitService.initNearLocation(returnLLSquarePoint);
				// #林去除按流量排进热门的数据
				// #林以距离排列list经纬度
			}
			
			return Response.success("初始推荐房源成功！", GsonUtils.toJson(initNearHouse));
		}catch (BusinessException e){
			LOG.error("初始推荐房源失败！",e);
			return Response.fail(e.getErrorDesc());
		} catch (Exception e) {
			LOG.error("初始推荐房源失败！", e);
			return Response.fail("初始推荐房源失败！");
		}
	}
}
