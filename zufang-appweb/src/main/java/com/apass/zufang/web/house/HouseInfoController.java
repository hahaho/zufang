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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.constants.ConstantsUtil;
import com.apass.zufang.domain.entity.HouseInfoRela;
import com.apass.zufang.service.house.HouseInfoService;
import com.apass.zufang.service.house.HouseService;
import com.apass.zufang.utils.GfbLogUtils;

@Path("/house")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class HouseInfoController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(HouseInfoController.class);

	@Autowired
	private HouseService houseService;

	@Autowired
	private HouseInfoService houseInfoService;

	/**
	 * 房屋详情页：根据houseId显示房屋信息
	 * 
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/getHouseInfoRela")
	public Response getHouseInfoRela(Map<String, Object> paramMap) {
		try {
			String houseId = CommonUtils.getValue(paramMap, "houseId");
			GfbLogUtils.info("房屋详情页：根据houseId显示房屋信息getHouseInfoRela,全量参数信息:"
					+ paramMap);
			if (StringUtils.isAnyEmpty(houseId)) {
				return Response.fail("houseId不能为空");
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			// 目标房源信息
			HouseInfoRela queryCondition = new HouseInfoRela();
			queryCondition.setHouseId(Long.valueOf(houseId));
			List<HouseInfoRela> targetHouseInfoList = houseInfoService
					.queryHouseInfoRela(queryCondition);
			resultMap.put("targetHouseInfo", targetHouseInfoList.get(0));
			return Response.success("操作成功", resultMap);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return Response.fail("操作失败");
		}
	}

	/**
	 * 根据houseId查询查询附近房源
	 * 
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/getNearlyHouseInfo")
	public Response getNearlyHouseInfo(Map<String, Object> paramMap) {
		String houseId = CommonUtils.getValue(paramMap, "houseId");
		GfbLogUtils.info("根据houseId查询查询附近房源getNearlyHouseInfo,全量参数信息:"
				+ paramMap);
		if (StringUtils.isAnyEmpty(houseId)) {
			return Response.fail("houseId不能为空");
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			// 附近房源信息
			List<HouseInfoRela> nearlyHouseInfoList = houseInfoService
					.getNearbyhouseId(Long.valueOf(houseId));
			resultMap.put("nearlyHouseInfoList", nearlyHouseInfoList);
			return Response.success("操作成功", resultMap);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return Response.fail("操作失败");
		}
	}

	/**
	 * 推荐房源
	 * 
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/getNearHouseByCoordinate")
	public Response getNearHouseByCoordinate(Map<String, Object> paramMap) {
		String latitude = CommonUtils.getValue(paramMap, "latitude");// 纬度
		String longitude = CommonUtils.getValue(paramMap, "longitude");// 经度
		String province = CommonUtils.getValue(paramMap, "province");// 纬度
		String city = CommonUtils.getValue(paramMap, "city");// 经度
		GfbLogUtils.info("根据houseId查询查询附近房源getNearHouseByCoordinate,全量参数信息:"
				+ paramMap);
		
		if (StringUtils.isAnyEmpty(province,city)) {
			return Response.fail("省和市不能为空");
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if (StringUtils.isAnyBlank(latitude, longitude)) {
				HouseInfoRela queryCondition = new HouseInfoRela();
				queryCondition.setProvince("上海");
				queryCondition.setCity("上海市");
				queryCondition
						.setSortField(ConstantsUtil.THE_NEARBY_HOUSES_NUMBER);// 按照上架时间倒序排列，取前十条数据
				List<HouseInfoRela> nearlyHouseInfoList = houseInfoService
						.queryHouseInfoRela(queryCondition);
				resultMap.put("nearlyHouseInfoList", nearlyHouseInfoList);
				return Response.success("操作成功", resultMap);
			}
			// 附近房源信息
			List<HouseInfoRela> nearlyHouseInfoList = houseInfoService
					.getNearHouseByCoordinate(Double.parseDouble(latitude),
							Double.parseDouble(longitude),province,city);
			resultMap.put("nearlyHouseInfoList", nearlyHouseInfoList);
			return Response.success("操作成功", resultMap);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return Response.fail("操作失败");
		}
	}

}
