package com.apass.zufang.web.house;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.apass.gfb.framework.logstash.LOG;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.entity.Apartment;
import com.apass.zufang.domain.entity.HouseInfoRela;
import com.apass.zufang.service.house.HouseinitService;

import cn.jpush.api.utils.StringUtils;


@Path("/home")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class HouseinitController {
	
    @Autowired
    private HouseinitService houseinitService;
	
    /**
     * 初始app地址
     * @param paramMap
     * @return
     */
	@POST
	@Path("/init")
	public Response handleAdvanceStatus(Map<String, Object> paramMap) {
//		String city = CommonUtils.getValue(paramMap, "city");// 城市
		try {
			return Response.success("success");
		} catch (Exception e) {
			LOG.error("初始app地址失败", e);
			return Response.fail("初始app地址失败");
		}
	}

	/**
	 * 搜索
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/search")
	public Response getSerach(Map<String, Object> paramMap) {
		
		try {
			
			String city = CommonUtils.getValue(paramMap, "city");// 区域
			Long houseId = CommonUtils.getLong(paramMap, "houseId");// 房源名
			String district = CommonUtils.getValue(paramMap, "district");// 小区
			
			HouseInfoRela paramHouse = new HouseInfoRela();
			paramHouse.setDistrict(district);
			paramHouse.setCity(city);
			paramHouse.setHouseId(houseId);
//			List<Apartment> results = houseinitService.getHourseInfos(paramHouse);
			return Response.success("success");
		} catch (Exception e) {
			LOG.error("搜索房源失败", e);
			return Response.fail("搜索房源失败");
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
			LOG.info(GsonUtils.toJson(paramMap));
			if (StringUtils.isEmpty(communityName)) {
				return Response.fail("品牌名称无数据");
			}
			Apartment apartment = new Apartment();
			apartment.setCompanyName(communityName);
			List<Apartment> resultApartment = houseinitService.getApartGongyu(apartment);
			return Response.success("success", resultApartment);
		} catch (Exception e) {
			LOG.error("查询品牌公寓失败", e);
			return Response.fail("查询品牌公寓失败");
		}
	}
}
