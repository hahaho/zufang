package com.apass.zufang.web.house;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.security.toolkit.SpringSecurityUtils;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.dto.HouseQueryParams;
import com.apass.zufang.domain.entity.House;
import com.apass.zufang.service.house.HouseService;
import com.apass.zufang.utils.ResponsePageBody;
import com.apass.zufang.utils.ValidateUtils;
@Path("/house/audit")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class HouseAuditController {
	
	private static final Logger logger = LoggerFactory.getLogger(HouseAuditController.class);
	
	@Autowired
	private HouseService houseService;
	
	/**
     * 房屋信息审核列表查询
     * @return
     */
	@POST
	@Path("/queryHouse")
	public Response getHouseList(Map<String,Object> paramMap){
		ResponsePageBody<House> respBody = new ResponsePageBody<House>();
        try {
        	logger.info("query house paramMap--->{}",GsonUtils.toJson(paramMap));
        	String apartmentName = CommonUtils.getValue(paramMap, "apartmentName");//公寓名称
        	String houseTitle = CommonUtils.getValue(paramMap, "houseTitle");//房源名称
        	String houseCode = CommonUtils.getValue(paramMap, "houseCode");//房源编码
        	String houseArea = CommonUtils.getValue(paramMap, "houseArea");//公寓所在区
        	HouseQueryParams dto = new HouseQueryParams();
        	dto.setApartmentName(apartmentName);
        	dto.setHouseTitle(houseTitle);
        	dto.setHouseCode(houseCode);
        	dto.setHouseArea(houseArea);
        	respBody = houseService.getHouseAuditListExceptDelete(dto);
        	respBody.setMsg("房屋信息审核列表查询成功!");
        	return Response.success("查询房屋审核信息成功！", respBody);
        } catch (Exception e) {
            logger.error("getHouseList EXCEPTION --- --->{}", e);
            respBody.setMsg("房屋信息审核列表查询失败!");
            return Response.fail("查询房屋审核信息失败!");
        }
	}
	
	@POST
	@Path("/audit")
	public Response auditHouse(Map<String,Object> paramMap){
		try {
			logger.info("audit house paramMap--->{}",GsonUtils.toJson(paramMap));
			String id = CommonUtils.getValue(paramMap, "id");
			String status = CommonUtils.getValue(paramMap, "status");/** 暂定 （0： 审核通过  1： 审核拒绝）*/
			ValidateUtils.isNotBlank(id, "房屋Id为空！");
			houseService.auditHouse(id,status, SpringSecurityUtils.getCurrentUser());
			return Response.success("审核房屋信息成功！");
		}catch (BusinessException e){
			logger.error("audit house businessException---->{}",e);
			return Response.fail(e.getErrorDesc());
		}catch (Exception e) {
			logger.error("审核房屋信息失败，错误原因", e);
		    return Response.fail("审核房屋信息失败！");
		}
	}
	
	@POST
	@Path("/detail")
	public Response detailHouse(Map<String,Object> paramMap){
		
		try {
			String id = CommonUtils.getValue(paramMap, "id");
			Map<String,Object> values = houseService.getHouseDetail(id);
			return Response.success("获取详情成功!", values);
		}catch (BusinessException e){
			logger.error("detail house businessException---->{}",e);
			return Response.fail(e.getErrorDesc());
		}catch (Exception e) {
			logger.error("信息失败，错误原因", e);
		    return Response.fail("审核房屋信息失败！");
		}
	}
	
}
