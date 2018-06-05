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
import com.apass.zufang.common.utils.MapEntryOrConverUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.dto.HouseQueryParams;
import com.apass.zufang.domain.vo.HouseBagVo;
import com.apass.zufang.service.apartment.ApartmentService;
import com.apass.zufang.service.house.HouseService;
import com.apass.zufang.utils.ResponsePageBody;
import com.apass.zufang.utils.ValidateUtils;
/**
 * 房源管理-房源审核管理
 * @author pyc
 *
 */
@Path("/house/audit")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class HouseAuditController {
	
	private static final Logger logger = LoggerFactory.getLogger(HouseAuditController.class);
	
	@Autowired
	private HouseService houseService;
	
	@Autowired
	private ApartmentService apartmentService;
	/**
     * 房屋信息审核列表查询
     * @return
     */
	@POST
	@Path("/queryHouse")
	public Response getHouseList(Map<String,Object> paramMap){
		ResponsePageBody<HouseBagVo> respBody = new ResponsePageBody<>();
        try {
        	logger.info("query house paramMap--->{}",GsonUtils.toJson(paramMap));
        	//根据当前登录用户，获取所属公寓的Code
        	String apartmentCode = apartmentService.getApartmentCodeByCurrentUser(SpringSecurityUtils.getCurrentUser());
        	HouseQueryParams dto = (HouseQueryParams) MapEntryOrConverUtils.converMap(HouseQueryParams.class, paramMap);
        	dto.setApartmentCode(apartmentCode);
        	if(dto.getPage() == null || dto.getRows() == null){
        		dto.setPage(1);
        		dto.setRows(10);
        	}
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
	@Path("/review")
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
			logger.info("detailHouse paramMap--->{}",GsonUtils.toJson(paramMap));
			String id = CommonUtils.getValue(paramMap, "id");
			Map<String,Object> values = houseService.getHouseDetail(id);
			return Response.success("获取详情成功!", values);
		}catch (BusinessException e){
			logger.error("detail house businessException---->{}",e);
			return Response.fail(e.getErrorDesc());
		}catch (Exception e) {
			logger.error("信息失败，错误原因", e);
		    return Response.fail("获取详情信息失败！");
		}
	}
	
}
