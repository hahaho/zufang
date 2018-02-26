package com.apass.zufang.web.house;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.logstash.LOG;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.entity.Apartment;
import com.apass.zufang.domain.entity.HouseLocation;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.service.commons.CommonService;
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
	public Response handleAdvanceStatus() {
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
	 * init热门房源
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/initHotHouseList")
	public Response initHotHouse(Map<String, Object> paramMap) {
		
		try {
			
			String city = CommonUtils.getValue(paramMap, "city");// 城市
			String province = CommonUtils.getValue(paramMap, "province");// 省份
			String district = CommonUtils.getValue(paramMap, "district");// 区域
			ValidateUtils.isNotBlank("请求参数丢失数据", city, province, district);
			
			HouseLocation houseLocation = new HouseLocation();
			houseLocation.setCity("%"+city+"%");
			houseLocation.setProvince("%"+province+"%");
			houseLocation.setDistrict("%"+district+"%");
			
			// 从后台配置读取热门房源
			List<HouseVo> searchPeizhi = houseinitService.initPeizhiLocation(houseLocation);
//			if(searchPeizhi!=null && !searchPeizhi.isEmpty()){
//				//不为空的情况
//				if (searchPeizhi.size() < 6) {
//					List<HouseVo> hotHouseList = houseinitService.initHotLocation(houseLocation);
//					for (int i=0; i<6-hotHouseList.size(); i++) {
//						searchPeizhi.add(hotHouseList.get(i));
//				}
//				}
//				}else{
//				//为空的情况 按照按浏览量（7天）降序排列房源
//				searchPeizhi = houseinitService.initHotLocation(houseLocation);
//				}
			if (!ValidateUtils.listIsTrue(searchPeizhi)) {
				searchPeizhi = houseinitService.initHotLocation(houseLocation);
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
	 * init附近房源
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/initNearHouseList")
	public Response initNearHouse(Map<String, Double> paramMap) {
		
		try {
			
			// 获取用户当前经纬度追加2公里经纬度,
			Double longitude = paramMap.get("longitude");// 经度
			Double latitude = paramMap.get("latitude");// 维度
			ValidateUtils.isNotBlank("请求参数丢失数据", longitude, latitude);
			Map<String, Double> returnLLSquarePoint = CommonService.renturnLngLat(longitude, latitude, new Double(2000));
			
			List<HouseVo> initNearHouse = houseinitService.initNearLocation(returnLLSquarePoint);
			
			return Response.success("初始附近房源成功！", GsonUtils.toJson(initNearHouse));
		}catch (BusinessException e){
			LOG.error("初始热门房源失败！",e);
			return Response.fail(e.getErrorDesc());
		} catch (Exception e) {
			LOG.error("初始附近房源失败！", e);
			return Response.fail("初始附近房源失败！");
		}
	}
	
	/**
	 * 品牌公寓
	 * @return
	 */
	@POST
	@Path("/getApartGongyu")
	public Response getApartGongyu(Map<String, Object> paramMap) {
		
		try {
			
			String communityName = CommonUtils.getValue(paramMap, "communityName");// 区域
			ValidateUtils.isNotBlank(communityName, "品牌名称无数据");
			
			Apartment apartment = new Apartment();
			apartment.setCompanyName(communityName);
			
			List<Apartment> resultApartment = houseinitService.getApartGongyu(apartment);
			
			return Response.success("success", GsonUtils.toJson(resultApartment));
		}catch (BusinessException e){
			LOG.error("查询品牌公寓失败！",e);
			return Response.fail(e.getErrorDesc());
		} catch (Exception e) {
			LOG.error("查询品牌公寓失败！", e);
			return Response.fail("查询品牌公寓失败！");
		}
	}
}
