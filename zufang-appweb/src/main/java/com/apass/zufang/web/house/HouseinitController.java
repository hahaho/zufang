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
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.entity.HouseLocation;
import com.apass.zufang.domain.enums.BusinessHouseTypeEnums;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.service.commons.CommonService;
import com.apass.zufang.service.house.HouseinitService;
import com.apass.zufang.utils.PageBean;
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
			return Response.success("初始城市地址成功！", cityList);
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
			// 获取url
			List<String> imgList = houseinitService.initImg();
			return Response.success("初始imgUrl成功！", imgList);
		} catch (Exception e) {
			LOG.error("初始imgUrl失败！", e);
			return Response.fail("初始imgUrl失败！");
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
			map.put("type", BusinessHouseTypeEnums.FY_JINGXUAN_2.getCode().toString());
			map.put("city", city);
			
			// 从后台配置读取热门房源
			List<HouseVo> searchPeizhi = houseinitService.initHouseByCity(map);
			map.put("type", BusinessHouseTypeEnums.FY_ZHENGCHANG_1.getCode().toString());
			if(ValidateUtils.listIsTrue(searchPeizhi)){
				//不为空的情况
				if (searchPeizhi.size() < 5) {
					List<HouseVo> hotHouseList = houseinitService.initHouseByCity(map);
					if (!ValidateUtils.listIsTrue(hotHouseList)) {
						int size = searchPeizhi.size();
					for (int i=0; i<5-size; i++) {
						searchPeizhi.add(hotHouseList.get(i));
					}
				}
				}else {
					searchPeizhi = searchPeizhi.subList(0, 5);
				}
				}else{
				//为空的情况 按照按浏览量（7天）降序排列房源
				searchPeizhi = houseinitService.initHouseByCity(map);
				if (ValidateUtils.listIsTrue(searchPeizhi) && searchPeizhi.size() > 5) {
					searchPeizhi = searchPeizhi.subList(0, 5);
				}
				}
			return Response.success("初始热门房源成功！", searchPeizhi);
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
			String city = (String) paramMap.get("city");// 城市
			int pageNum = (int) paramMap.get("pageNum");
			HashMap<String, String> map = Maps.newHashMap();
			map.put("city", city);
			if (StringUtils.isAnyBlank(longitude, latitude)) {
				HouseLocation houseLocation = new HouseLocation();
				houseLocation.setCity(city);
				
				map.put("type", BusinessHouseTypeEnums.FY_ZHENGCHANG_1.getCode().toString());
				initNearHouse = houseinitService.initHouseByCity(map);
				// 去除按流量排进热门的数据
				initNearHouse = removeHotHouse(initNearHouse, map, BusinessHouseTypeEnums.FY_JINGXUAN_2);
			}else{
				ValidateUtils.isNotBlank("请求参数丢失数据", longitude, latitude);
				Map<String, Double> returnLLSquarePoint = CommonService.renturnLngLat(new Double(longitude), new Double(latitude), new Double(2000));
				initNearHouse = houseinitService.initNearLocation(returnLLSquarePoint);
				// 去除按流量排进热门的数据
				initNearHouse = removeHotHouse(initNearHouse, map, BusinessHouseTypeEnums.FY_JINGXUAN_2);
				if (ValidateUtils.listIsTrue(initNearHouse)) {
					// 使用冒泡排列经纬距离
					for (int i = 1; i < initNearHouse.size(); i++) {
						for (int j = 0; j < initNearHouse.size()-1; j++) {
							double disOne = CommonService.distanceSimplify(new Double(longitude), new Double(latitude), initNearHouse.get(j).getLongitude(), initNearHouse.get(j).getLatitude());
							double disTwo = CommonService.distanceSimplify(new Double(longitude), new Double(latitude), initNearHouse.get(j+1).getLongitude(), initNearHouse.get(j+1).getLatitude());
							if (disOne < disTwo) {
								HouseVo temp = new HouseVo();
								HouseVo temp1 = initNearHouse.get(j);
								HouseVo temp2 = initNearHouse.get(j+1);
								temp = temp1;
								temp1 = temp2;
								temp2 = temp;
							}
						}
					}
				}
			}
			if (ValidateUtils.listIsTrue(initNearHouse)) {
				PageBean<HouseVo> pageBean = new PageBean<HouseVo>(pageNum+1, 10, initNearHouse);
				initNearHouse = pageBean.getList();
			}
			
			return Response.success("初始推荐房源成功！", initNearHouse);
		}catch (BusinessException e){
			LOG.error("初始推荐房源失败！",e);
			return Response.fail(e.getErrorDesc());
		} catch (Exception e) {
			LOG.error("初始推荐房源失败！", e);
			return Response.fail("初始推荐房源失败！");
		}
	}

	private List<HouseVo> removeHotHouse(List<HouseVo> hotHouse, HashMap<String, String> map, BusinessHouseTypeEnums type) {
		
		try {
			map.put("type", type.getCode().toString());
			List<HouseVo> Peizhi = houseinitService.initHouseByCity(map);
			if (Peizhi.size() < 5) {
			hotHouse = hotHouse.subList(0, 5-Peizhi.size());
			}
			return hotHouse;
		} catch (Exception e) {
			//如果查询推荐房源数量小于热门房源不返回数据
			hotHouse = null;
		}
		return hotHouse;
	}
}
