package com.apass.zufang.web.searchhistory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.service.searchhistory.SearchHistorySevice;
import com.apass.zufang.web.personal.ZuFangLoginController;

/**
 * 搜索历史
 * 
 * @author ls
 * @update 2018-02-09 11:02
 *
 */
@Path("/zfsearchhistory")
public class SearchHistoryController {

private static final Logger logger = LoggerFactory.getLogger(ZuFangLoginController.class);
	
	@Autowired
	private SearchHistorySevice<?> searchHistorySevice;
	
	/**
	 *  搜索历史
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/queryhistory")
	public Response queryhistory(Map<String, Object> paramMap) {
	        try {
	        	String customerId = CommonUtils.getValue(paramMap, "customerId");
	        	String deviceId = CommonUtils.getValue(paramMap, "deviceId");
	        	
	        	Map<Object, Object> map = new HashMap<>();
	        	if(org.apache.commons.lang3.StringUtils.isBlank(customerId)){
	        		//设备ID
	        		List<String> list = searchHistorySevice.queryDeviceIdHistory(deviceId);
	        		map.put("list", list);
	        		 return Response.success("设备ID搜索",map);
	        	}else{
	        		//用户ID
	        		List<String> list =searchHistorySevice.queryCustomerIdHistory(customerId);
	        		return Response.success("用户ID搜索",list);
	        	}
	        } catch (Exception e) {
	            return Response.fail("操作失败");
	        }
	    }
	/**
	 *  删除搜索历史
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/deletehistory")
	public Response deletehistory(Map<String, Object> paramMap) {
		 try {
	        	String customerId = CommonUtils.getValue(paramMap, "customerId");
	        	String deviceId = CommonUtils.getValue(paramMap, "deviceId");
	        	if(org.apache.commons.lang3.StringUtils.isBlank(customerId)){
	        		//设备ID删除	
	        		 return Response.success("设备ID搜索",searchHistorySevice.deleteDeviceIdHistory(deviceId));
	        	}else{
	        		//用户ID删除
	        		return Response.success("用户ID搜索",searchHistorySevice.deleteCustomerIdHistory(customerId));
	        	}
	        } catch (Exception e) {
	            return Response.fail("操作失败");
	        }
	    }
}