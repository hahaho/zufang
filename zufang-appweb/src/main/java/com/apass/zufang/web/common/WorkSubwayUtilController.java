package com.apass.zufang.web.common;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.apass.gfb.framework.logstash.LOG;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.service.searchhistory.WorkSubwaySevice;

/**
 *地铁数据公用接口
 * 
 * @author zwd
 *
 */
@Path("/util/workSubway")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class WorkSubwayUtilController {

	@Autowired
	private WorkSubwaySevice workSubwaySevice;


	/**
	 * 录入地铁数据
	 * 
	 * @return
	 */
	@POST
	@Path("/insertInfo")
	public Response insertInfo(Map<String, Object> paramMap) {
		try {
			String qt = CommonUtils.getValue(paramMap, "qt");
			String c= CommonUtils.getValue(paramMap, "c");
			String t = CommonUtils.getValue(paramMap, "t"); 
			String city = CommonUtils.getValue(paramMap, "city"); //城市
			String code = CommonUtils.getValue(paramMap, "code"); //编码
			
			workSubwaySevice.insertInfoWorksubway(qt, c, t, city, code);

			return Response.success("success", "录入"+city+"地铁数据成功");
		} catch (Exception e) {
			LOG.error("录入地铁数据失败！", e);
			return Response.fail("录入地铁数据失败！");
		}
	}

}
