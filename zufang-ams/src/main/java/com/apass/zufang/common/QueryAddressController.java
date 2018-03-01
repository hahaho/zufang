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
import java.util.List;
import java.util.Map;

/**
 * 说明：根据地址code查相应省，市，区 通用方法
 * 
 * @author xiaohai
 * @version 1.0
 * @date 2017年1月12日
 */
@Path("/nation")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class QueryAddressController {
	
	@Autowired
	private NationService nationService;
	/**
	 * 日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryAddressController.class);



	@POST
	@Path("/v1/queryNations")
	public List<WorkCityJd> queryCity(Map<String,String> paramMap){
		String districtCode = paramMap.get("code");
		//此时查询的是省份
		if(StringUtils.isBlank(districtCode)){
			districtCode = "0";
		}
		LOGGER.info("查询参数,parent:{}",districtCode);
		
		List<WorkCityJd> jdlist = nationService.queryDistrictForAms(districtCode);
		
		return jdlist;
	}


}
