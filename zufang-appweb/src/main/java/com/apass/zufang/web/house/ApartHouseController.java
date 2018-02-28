package com.apass.zufang.web.house;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.apass.gfb.framework.logstash.LOG;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.entity.ApartHouseList;
import com.apass.zufang.domain.entity.Apartment;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.service.house.ApartHouseService;
import com.apass.zufang.utils.PageBean;

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
			// 获取市区
			List<String> cityList = apartHouseService.initImg();
			return Response.success("初始城市地址成功！", cityList);
		} catch (Exception e) {
			LOG.error("初始城市地址失败！", e);
			return Response.fail("初始城市地址失败！");
		}
	}
	
	/**
	 * 查询公寓Id
	 * @return
	 */
	@POST
	@Path("/getApartCodes")
	public Response getApartByCity(Map<String, Object> paramMap) {
		
		try {
			String city = (String) paramMap.get("city");
			int pageNum = (int) paramMap.get("pageNum");
			Apartment apartment = new Apartment();
			apartment.setCity(city);
			List<Apartment> resultApartment = apartHouseService.getApartByCity(apartment);
			PageBean<Apartment> pageBean = new PageBean<Apartment>(pageNum+1, 4, resultApartment);
			resultApartment = pageBean.getList();
			ApartHouseList apartHouseList = new ApartHouseList();
			for (Apartment eachApart : resultApartment) {
				List<HouseVo> houseListByCode = apartHouseService.getHouseByCodes((ArrayList<String>) Arrays.asList(eachApart.getCode()));
				PageBean<HouseVo> pageBean1 = new PageBean<HouseVo>(pageNum+1, 5, houseListByCode);
				List<HouseVo> list = pageBean1.getList();
				apartHouseList.setId(eachApart.getId());
				apartHouseList.setCode(eachApart.getCode());
				apartHouseList.setName(eachApart.getName());
				apartHouseList.setRows(list);
			}
	
			return Response.success("success", apartHouseList);
			} catch (Exception e) {
				LOG.error("查询品牌公寓失败！", e);
				return Response.fail("查询品牌公寓失败！");
			}
	}
	/**
	 * 查询房源List
	 * @return
	 */
	@POST
	@Path("/getHouseByCodes")
	public Response getHouseByCodes(Map<String, Object> paramMap) {
		
		try {
			String str = (String) paramMap.get("codes");
			int pageNum = (int) paramMap.get("pageNum");
			if (StringUtils.isBlank(str)) {
				return Response.fail("请求参数为空！");
			}
			String[] split = str.split(",");
			ArrayList<String> list = new ArrayList<>();
			for (int i = 0; i < split.length; i++) {
				list.add(split[i]);
			}
			List<HouseVo> apartList = apartHouseService.getHouseByCodes(list);
			PageBean<HouseVo> pageBean = new PageBean<HouseVo>(pageNum+1, 20, apartList);
			apartList = pageBean.getList();
			return Response.success("success", apartList);
		} catch (Exception e) {
			LOG.error("查询品牌公寓失败！", e);
			return Response.fail("查询品牌公寓失败！");
		}
	}
	
}
