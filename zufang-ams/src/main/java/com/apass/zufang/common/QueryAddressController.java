package com.apass.zufang.common;

import com.apass.zufang.domain.common.WorkCityJd;
import com.apass.zufang.service.nation.NationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 说明：根据地址code查相应省，市，区 通用方法
 * 
 * @author xiaohai
 * @version 1.0
 * @date 2017年1月12日
 */
@Path("/application/nation")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class QueryAddressController {
	
	@Autowired
	private NationService nationService;
	/**
	 * 日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryAddressController.class);
	/**
	 * 直辖市
	 */
	private static final String[] CENTRL_CITY_ARRAY = {"1", "2", "3", "4"};
	private static final List<String> CENTRL_CITY_LIST = Arrays.asList(CENTRL_CITY_ARRAY);

	private static final String[] CENTRL_CITY_ARRAY2 = {"1zxs", "2zxs", "3zxs", "4zxs"};
	private static final List<String> CENTRL_CITY_LIST2 = Arrays.asList(CENTRL_CITY_ARRAY2);


	@POST
	@Path("/v1/queryNations")
	public List<WorkCityJd> queryCity(Map<String,String> paramMap){
		try{
			String districtCode = paramMap.get("code");
			//此时查询的是省份
			if(StringUtils.isBlank(districtCode)){
				districtCode = "0";
			}
			LOGGER.info("查询参数,parent:{}",districtCode);

			List<WorkCityJd> jdlist = nationService.queryDistrictForAms(districtCode);
			if(CENTRL_CITY_LIST.contains(districtCode)){
				WorkCityJd workCityJd = nationService.selectWorkCityByCode(districtCode);
				//如果是直辖市，每个code后+zxs
				workCityJd.setCode(workCityJd.getCode()+"zxs");
				jdlist.clear();
				jdlist.add(workCityJd);
			}
			if(CENTRL_CITY_LIST2.contains(districtCode)){
				jdlist.clear();
				jdlist = nationService.queryDistrictForAms(districtCode.substring(0,1));
			}

			return jdlist;
		}catch (Exception e){
			LOGGER.error("查询失败,---Exception---",e);
			return null;
		}

	}


}
