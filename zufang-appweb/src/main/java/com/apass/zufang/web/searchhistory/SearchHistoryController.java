package com.apass.zufang.web.searchhistory;

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
import com.apass.zufang.domain.entity.SearchKeys;
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
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class SearchHistoryController {

private static final Logger logger = LoggerFactory.getLogger(ZuFangLoginController.class);
	
	@Autowired
	private SearchHistorySevice searchHistorySevice;
	
	/**
	 *  搜索历史
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/queryhistory")
	public Response queryhistory(Map<String, Object> paramMap) {
	        try {
	        	
//	        	String userId = CommonUtils.getValue(paramMap, "userId");
	        	String deviceId = CommonUtils.getValue(paramMap, "deviceId");
	        	logger.info("入参 ：customerId———————>"+"    deviceId———————>"+deviceId);
	        	Map<Object, Object> map = new HashMap<>();
	        	if(StringUtils.isBlank(deviceId)){
	        		 return Response.fail("设备ID不能为空"+deviceId);
	        	}
//	        	if(org.apache.commons.lang3.StringUtils.isBlank(userId)){
	        		//设备ID
	        		List<SearchKeys> list = searchHistorySevice.queryDeviceIdHistory(deviceId);
	        		map.put("list", list);
	        		 return Response.success("设备ID搜索",map);
	        	/*}else{
	        		//用户ID
	        		List<SearchKeys> list =searchHistorySevice.queryCustomerIdHistory(userId);
	        		map.put("list", list);
	        		return Response.success("用户ID搜索",map);
	        	}*/
	        } catch (Exception e) {
				logger.error("queryhistory error------>",e);
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
//	        	String userId = CommonUtils.getValue(paramMap, "userId");
	        	String deviceId = CommonUtils.getValue(paramMap, "deviceId");
	        	logger.info("入参 ：customerId———————>"+"      deviceId———————>"+deviceId);
	        	if(StringUtils.isBlank(deviceId)){
	        		 return Response.fail("设备ID不能为空");
	        	}
	        	/*if(org.apache.commons.lang3.StringUtils.isBlank(userId)){
	        	*/	//设备ID删除	
	        		 return Response.success("设备IDs删除成功",searchHistorySevice.deleteDeviceIdHistory(deviceId));
	        	/*}else{
	        		//用户ID删除
	        		return Response.success("用户ID删除成功",searchHistorySevice.deleteCustomerIdHistory(userId));
	        	}*/
	        } catch (Exception e) {
	        	logger.error("删除搜索历史失败——————"+e);
	            return Response.fail("操作失败");
	        }
	    }
}
