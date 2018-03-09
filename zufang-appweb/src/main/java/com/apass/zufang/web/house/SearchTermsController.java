package com.apass.zufang.web.house;

import java.util.ArrayList;
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
import com.apass.zufang.domain.common.KeyValue;
import com.apass.zufang.domain.common.WorkCityJd;
import com.apass.zufang.domain.entity.Apartment;
import com.apass.zufang.domain.entity.WorkSubway;
import com.apass.zufang.domain.enums.BusinessHouseTypeEnums;
import com.apass.zufang.domain.enums.FeaturesConfigurationEnums;
import com.apass.zufang.domain.enums.HuxingEnums;
import com.apass.zufang.domain.enums.PriceRangeEnum;
import com.apass.zufang.service.house.ApartHouseService;
import com.apass.zufang.service.searchhistory.WorkSubwaySevice;
import com.apass.zufang.utils.ValidateUtils;
import com.google.common.collect.Maps;

import net.sf.json.JSONArray;

/**
 * 搜索项接口
 * 
 * @author zwd
 *
 */
@Path("/searchTerms")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class SearchTermsController {

	@Autowired
	private ApartHouseService apartHouseService;

	@Autowired
	private WorkSubwaySevice workSubwaySevice;

	/**
	 * 品牌公寓
	 * 
	 * @return
	 */
	@POST
	@Path("/getBrandApartment")
	public Response getBrandApartment(Map<String, Object> paramMap) {
		try {
			String city = CommonUtils.getValue(paramMap, "city");// 区域
			ValidateUtils.isNotBlank(city, "品牌名称无数据");

			Apartment apartment = new Apartment();
			apartment.setCity(city);

			List<Apartment> resultApartment = apartHouseService.getApartmentBylistCity(apartment);

			return Response.success("success", resultApartment);
		} catch (BusinessException e) {
			LOG.error("查询品牌公寓失败！", e);
			return Response.fail(e.getErrorDesc());
		} catch (Exception e) {
			LOG.error("查询品牌公寓失败！", e);
			return Response.fail("查询品牌公寓失败！");
		}
	}

	/**
	 * 价格
	 * 
	 * @return
	 */
	@POST
	@Path("/getPrice")
	public Response getPrice(Map<String, Object> paramMap) {
		try {

			List<KeyValue> keyValueList = new ArrayList<KeyValue>();
			PriceRangeEnum[] result = PriceRangeEnum.values();
			for (int i = 0; i < result.length; i++) {
				KeyValue keyValue = new KeyValue();
				keyValue.setKey(result[i].getVal());
				keyValue.setValue(result[i].getDesc());
				keyValueList.add(keyValue);
			}
			JSONArray featuresJsonArray = JSONArray.fromObject(keyValueList);
			
			return Response.success("success", featuresJsonArray);
		} catch (Exception e) {
			LOG.error("查询价格失败！", e);
			return Response.fail("查询价格失败！");
		}
	}

	/**
	 * 查询位置区域及地铁线路
	 * 
	 * @return
	 */
	@POST
	@Path("/getSubwayLineAndSiteOrAre")
	public Response getSubwayLineAndSiteOrAre(Map<String, Object> paramMap) {
		try {
			String code = CommonUtils.getValue(paramMap, "code");// 当前位置的编码

			ValidateUtils.isNotBlank(code, "编码无数据");

			WorkSubway domin = new WorkSubway();
			domin.setParentCode(Long.valueOf(code));
			;
			/**
			 * 获取地铁及其站点
			 */
			List<WorkSubway> result = workSubwaySevice.getSubwayLineAndSiteOrAre(domin);
			/**
			 * 获取位置区域
			 */
			List<WorkCityJd> resultJd = workSubwaySevice.queryCityJdParentCodeList(code);

			List<WorkSubway> resultJdJson = new ArrayList<WorkSubway>();

			for (WorkCityJd workCityJd : resultJd) {
				List<WorkSubway> resultJdJsonTemp = new ArrayList<WorkSubway>();
				WorkSubway temp = new WorkSubway();
				temp.setId(workCityJd.getId());
				temp.setCode(Long.valueOf(workCityJd.getCode()));
				if(StringUtils.isBlank(workCityJd.getCity())){
					temp.setLineName(workCityJd.getDistrict());
				}else{
					temp.setLineName(workCityJd.getCity());
				}
				
				if (workCityJd.getResultList()!=null) {
					for (WorkCityJd workSubwaysss : workCityJd.getResultList()) {
						WorkSubway tempsss = new WorkSubway();
						temp.setId(workSubwaysss.getId());
						tempsss.setCode(Long.valueOf(workSubwaysss.getCode()));
						
						if(StringUtils.isBlank(workCityJd.getCity())){
							tempsss.setSiteName(workSubwaysss.getTowns());
						}else{
							tempsss.setSiteName(workSubwaysss.getDistrict());
						}
						
						
						resultJdJsonTemp.add(tempsss);
					}
					temp.setResultList(resultJdJsonTemp);
				}
				resultJdJson.add(temp);
			}

			Map<String, Object> resultMap = Maps.newHashMap();
			resultMap.put("workSubway", result);
			resultMap.put("workCityJd", resultJdJson);

			return Response.success("success", resultMap);
		} catch (BusinessException e) {
			LOG.error("查询位置区域及地铁线路失败！", e);
			return Response.fail(e.getErrorDesc());
		} catch (Exception e) {
			LOG.error("查询位置区域及地铁线路失败！", e);
			return Response.fail("查询位置区域及地铁线路失败！");
		}
	}

	/**
	 * 筛选
	 * 
	 * @return
	 */
	@POST
	@Path("/getSelectFilter")
	public Response getSelectFilter(Map<String, Object> paramMap) {
		try {

			Map<String, Object> resultMap = Maps.newHashMap();
			// 类型 合租 整租
			List<KeyValue> sharedType = new ArrayList<KeyValue>();
			KeyValue HZ_0 = new KeyValue();
			HZ_0.setKey(BusinessHouseTypeEnums.HZ_0.getCode());
			HZ_0.setValue(BusinessHouseTypeEnums.HZ_0.getMessage());
			sharedType.add(HZ_0);
			KeyValue HZ_1 = new KeyValue();
			HZ_1.setKey(BusinessHouseTypeEnums.HZ_1.getCode());
			HZ_1.setValue(BusinessHouseTypeEnums.HZ_1.getMessage());
			sharedType.add(HZ_1);
			KeyValue HZ_2 = new KeyValue();
			HZ_2.setKey(BusinessHouseTypeEnums.HZ_2.getCode());
			HZ_2.setValue(BusinessHouseTypeEnums.HZ_2.getMessage());
			sharedType.add(HZ_2);
			JSONArray sharedTypeJsonArray = JSONArray.fromObject(sharedType);

			// 户型
			List<KeyValue> huxingType = new ArrayList<KeyValue>();
			HuxingEnums[] resultHuxingEnums = HuxingEnums.values();
			for (int i = 0; i < resultHuxingEnums.length; i++) {
				KeyValue keyValue = new KeyValue();
				keyValue.setKey(resultHuxingEnums[i].getCode());
				keyValue.setValue(resultHuxingEnums[i].getMessage());
				huxingType.add(keyValue);
			}
			JSONArray huxingTypeJsonArray = JSONArray.fromObject(huxingType);
			// 特色配置
			List<KeyValue> features = new ArrayList<KeyValue>();
			FeaturesConfigurationEnums[] featuresEnums = FeaturesConfigurationEnums.values();
			for (int i = 0; i < featuresEnums.length; i++) {
				KeyValue keyValue = new KeyValue();
				keyValue.setKey(featuresEnums[i].getCode());
				keyValue.setValue(featuresEnums[i].getMessage());
				features.add(keyValue);
			}
			JSONArray featuresJsonArray = JSONArray.fromObject(features);

			resultMap.put("sharedType", sharedTypeJsonArray);
			resultMap.put("roomType", huxingTypeJsonArray);
			resultMap.put("FeaturesConfiguration", featuresJsonArray);

			return Response.success("success", resultMap);
		} catch (Exception e) {
			LOG.error("查询位置区域及地铁线路失败！", e);
			return Response.fail("查询位置区域及地铁线路失败！");
		}
	}

}
