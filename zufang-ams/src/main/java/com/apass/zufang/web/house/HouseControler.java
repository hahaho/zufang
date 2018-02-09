package com.apass.zufang.web.house;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.apass.esp.common.code.BusinessErrorCode;
import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.enums.RentTypeEnums;
import com.apass.zufang.service.house.HouseService;
import com.apass.zufang.utils.ValidateUtils;

@Path("/house")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class HouseControler {

	private static final Logger logger = LoggerFactory.getLogger(HouseControler.class);
	
	@Autowired
	private HouseService houseService;
	
	/**
	 * 添加房屋信息
	 * @param paramMap
	 * @return
	 */
	public Response addHouse(Map<String, Object> paramMap){
		try {
			validateParams(paramMap);
			logger.info("");
			return Response.successResponse();
		}catch (BusinessException e){
			logger.error("add house businessException---->{}",e);
			return Response.fail(e.getErrorDesc());
		}catch (Exception e) {
			logger.error("添加房屋信息失败，错误原因", e);
		    return Response.fail("添加房屋信息失败！");
		}
	}
	
	/**
	 * 修改房屋信息
	 * @return
	 */
	public Response editHouse(Map<String, Object> paramMap){
		try {
			validateParams(paramMap);
			logger.info("");
			return Response.success("");
		}catch (BusinessException e){
			logger.error("edit house businessException---->{}",e);
			return Response.fail(e.getErrorDesc());
		}catch (Exception e) {
			logger.error("修改房屋信息失败，错误原因", e);
		    return Response.fail("修改房屋信息失败！");
		}
	}
	
	/**
	 * 删除房屋信息
	 * @param paramMap
	 * @return
	 */
	public Response delHouse(Map<String, Object> paramMap){
		try {
			String Id = CommonUtils.getValue(paramMap, "Id");
			ValidateUtils.isNotBlank(Id, "房屋Id为空！");
			//houseService
			return Response.success("");
		}catch (BusinessException e){
			logger.error("delete house businessException---->{}",e);
			return Response.fail(e.getErrorDesc());
		}catch (Exception e) {
			logger.error("删除房屋信息失败，错误原因", e);
		    return Response.fail("删除房屋信息失败！");
		}
	}
	
	/**
	 * 验证所传参数
	 * @param paramMap
	 * @throws BusinessException
	 */
	public void validateParams(Map<String, Object> paramMap) throws BusinessException{
		
		String apartmentId = CommonUtils.getValue(paramMap, "apartmentId");
		String rentType = CommonUtils.getValue(paramMap, "rentType");
		String communityName = CommonUtils.getValue(paramMap, "communityName");
		
	    String province = CommonUtils.getValue(paramMap, "province"); // 省
	    String city = CommonUtils.getValue(paramMap, "city"); // 市
	    String area = CommonUtils.getValue(paramMap, "area"); // 区
	    String district = CommonUtils.getValue(paramMap, "district"); //街道
	    String detailAddr = CommonUtils.getValue(paramMap, "detailAddr"); // 详细地址
	    
	    String acreage = CommonUtils.getValue(paramMap, "acreage");
	    
	    String room = CommonUtils.getValue(paramMap, "room"); //室
	    String hall = CommonUtils.getValue(paramMap, "hall"); //厅
	    String wei = CommonUtils.getValue(paramMap, "wei"); //卫
	    String floor = CommonUtils.getValue(paramMap, "floor"); //第几层
	    String totalFloor = CommonUtils.getValue(paramMap, "totalFloor"); //总共的楼层数
	    
	    String liftType = CommonUtils.getValue(paramMap, "liftType");//有无电梯
	    String rentAmt = CommonUtils.getValue(paramMap, "rentAmt");
	    String zujinType = CommonUtils.getValue(paramMap, "zujinType");
	    
	    String chaoxiang = CommonUtils.getValue(paramMap, "chaoxiang");
	    String zhuangxiu = CommonUtils.getValue(paramMap, "zhuangxiu");
	    String houseType = CommonUtils.getValue(paramMap, "houseType");
	    String title = CommonUtils.getValue(paramMap, "title");
	    String description = CommonUtils.getValue(paramMap, "description");
	    
	    ValidateUtils.isNotBlank(apartmentId, "请选择所属公寓");
		ValidateUtils.isNotBlank(rentType, "请选择出租方式");
		ValidateUtils.isNotBlank(communityName, "请填写小区名称");
		ValidateUtils.checkLength(communityName, 2, 20, "2-20个字，可填写汉字，数字，不能填写特殊字符");
	    
	    ValidateUtils.isNotBlank(province, "请选择省份");
	    ValidateUtils.isNotBlank(city, "请选择城市");
	    ValidateUtils.isNotBlank(area, "请选择区域");
	    ValidateUtils.isNotBlank(district, "请选择街道");
	    ValidateUtils.isNotBlank(detailAddr, "请填写详细地址");
	    ValidateUtils.checkLength(detailAddr, 2, 30, "2-30个字，可填写汉字，数字，不能填写特殊字符");
	    
	    
	    if(StringUtils.equals(rentType, RentTypeEnums.HZ_HEZU_2.getCode()+"")){
	    	ValidateUtils.isNotBlank(acreage, "请填写房屋面积");
	    	ValidateUtils.checkNonNumberRange(acreage, 1, 9999, "房屋面积");
	    }
	    
	    ValidateUtils.isNotBlank(room, "请填写室");
	    ValidateUtils.checkNumberRange(room, 0, 0,"室");
	    ValidateUtils.isNotBlank(hall, "请填写厅");
	    ValidateUtils.checkNumberRange(hall, 0, 0, "厅");
	    ValidateUtils.isNotBlank(wei, "请填写卫");
	    ValidateUtils.checkNumberRange(wei, 0, 0, "卫");
	    
	    ValidateUtils.isNotBlank(floor, "请填写楼层");
	    ValidateUtils.checkNumberRange(floor, -9, 99, "楼层分布");
	    ValidateUtils.isNotBlank(totalFloor, "请填写总楼层");
	    ValidateUtils.checkNumberRange(floor, 1, 99, "总楼层");
	    
	    ValidateUtils.isNotBlank(liftType, "请选择电梯情况");
	    ValidateUtils.isNotBlank(rentAmt, "请填写租金");
	    ValidateUtils.checkNumberRange(rentAmt, 0, 0, "租金");
	    ValidateUtils.isNotBlank(zujinType, "请选择租金支付方式");
	    
	    ValidateUtils.isNotBlank(chaoxiang, "请选择朝向");
	    ValidateUtils.isNotBlank(zhuangxiu, "请选择装修情况");
	    ValidateUtils.isNotBlank(houseType, "请选择房屋类型");
	    
	    ValidateUtils.isNotBlank(title, "请填写房源标题");
		ValidateUtils.checkLength(title, 6, 30, "请填写6-30个字");
		ValidateUtils.checkValueLength(description, 8, 100, "请填写10-800个字");
	}
}
