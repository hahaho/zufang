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
import com.apass.zufang.domain.common.WorkCityJd;
import com.apass.zufang.domain.entity.Apartment;
import com.apass.zufang.domain.entity.WorkSubway;
import com.apass.zufang.domain.enums.PriceRangeEnum;
import com.apass.zufang.service.house.ApartHouseService;
import com.apass.zufang.service.searchhistory.WorkSubwaySevice;
import com.apass.zufang.utils.ValidateUtils;

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
	public Response getApartGongyu(Map<String, Object> paramMap) {
		try {
			String communityName = CommonUtils.getValue(paramMap, "communityName");// 区域
			ValidateUtils.isNotBlank(communityName, "品牌名称无数据");

			Apartment apartment = new Apartment();

			List<Apartment> resultApartment = apartHouseService.getApartByCity(apartment);

			return Response.success("success", GsonUtils.toJson(resultApartment));
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
			PriceRangeEnum[] result = PriceRangeEnum.values();
			return Response.success("success", GsonUtils.toJson(result));
		} catch (Exception e) {
			LOG.error("查询价格失败！", e);
			return Response.fail("查询价格失败！");
		}
	}

	/**
	 * 区域
	 * 
	 * @return
	 */
	@POST
	@Path("/getArea")
	public Response getArea(Map<String, Object> paramMap) {
		try {
			String code = CommonUtils.getValue(paramMap, "code");// 当前位置的
																				// 编码
			ValidateUtils.isNotBlank(code, "编码无数据");

			List<WorkCityJd> result = workSubwaySevice.queryCityJdParentCodeList(code);

			return Response.success("success", GsonUtils.toJson(result));
		} catch (BusinessException e) {
			LOG.error("查询区域失败！", e);
			return Response.fail(e.getErrorDesc());
		} catch (Exception e) {
			LOG.error("查询区域失败！", e);
			return Response.fail("查询区域失败！");
		}
	}

	/**
	 * 地铁线路/或站点
	 * 
	 * @return
	 */
	@POST
	@Path("/getSubwayLineAndSite")
	public Response getSubwayLine(Map<String, Object> paramMap) {
		try {
			String code = CommonUtils.getValue(paramMap, "code");// 当前位置的编码
			String level = CommonUtils.getValue(paramMap, "level");// 节点
			
			ValidateUtils.isNotBlank(code, "编码无数据");

			WorkSubway domin=new WorkSubway();
			domin.setLevel(level);
			domin.setParentCode(Long.valueOf(code));;
			List<WorkSubway> result=workSubwaySevice.querySubwayParentCodeList(domin);

			return Response.success("success", GsonUtils.toJson(result));
		} catch (BusinessException e) {
			LOG.error("查询地铁线路失败！", e);
			return Response.fail(e.getErrorDesc());
		} catch (Exception e) {
			LOG.error("查询地铁线路失败！", e);
			return Response.fail("查询地铁线路失败！");
		}
	}
}
