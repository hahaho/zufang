package com.apass.zufang.web.house;

import java.util.ArrayList;
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
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.common.WorkCityJd;
import com.apass.zufang.domain.entity.Apartment;
import com.apass.zufang.domain.entity.WorkSubway;
import com.apass.zufang.domain.enums.PriceRangeEnum;
import com.apass.zufang.service.house.ApartHouseService;
import com.apass.zufang.service.searchhistory.WorkSubwaySevice;
import com.apass.zufang.utils.ValidateUtils;
import com.google.common.collect.Maps;

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
			
			Map<Integer,Object> resultMap=Maps.newHashMap();
			
			PriceRangeEnum[] result = PriceRangeEnum.values();
			for (int i = 0; i < result.length; i++) {
				resultMap.put(result[i].getVal(), result[i].getDesc());
			}
			
			return Response.success("success", resultMap);
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

			WorkSubway domin=new WorkSubway();
			domin.setParentCode(Long.valueOf(code));;
			/**
			 * 获取地铁及其站点
			 */
			List<WorkSubway> result=workSubwaySevice.getSubwayLineAndSiteOrAre(domin);
			/**
			 * 获取位置区域
			 */
			List<WorkCityJd> resultJd = workSubwaySevice.queryCityJdParentCodeList(code);
			
			List<WorkSubway> resultJdJson=new ArrayList<WorkSubway>();
			
			for (WorkCityJd workCityJd : resultJd) {
				List<WorkSubway> resultJdJsonTemp=new ArrayList<WorkSubway>();
				WorkSubway temp=new WorkSubway(); 
				temp.setId(workCityJd.getId());
				temp.setCode(Long.valueOf(workCityJd.getCode()));
				temp.setLineName(workCityJd.getCity());
				if(!workCityJd.getResultList().isEmpty()){
					for (WorkCityJd workSubwaysss : workCityJd.getResultList()) {
						WorkSubway tempsss=new WorkSubway();
						temp.setId(workSubwaysss.getId());
						tempsss.setCode(Long.valueOf(workSubwaysss.getCode()));
						tempsss.setSiteName(workSubwaysss.getDistrict());
						resultJdJsonTemp.add(tempsss);
					}
					temp.setResultList(resultJdJsonTemp);
				}
				resultJdJson.add(temp);
			}
			
			Map<String,Object> resultMap=Maps.newHashMap();
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
}
