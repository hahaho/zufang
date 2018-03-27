package com.apass.zufang.web.house;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
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

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.security.toolkit.SpringSecurityUtils;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.zufang.common.utils.MapEntryOrConverUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.dto.HouseQueryParams;
import com.apass.zufang.domain.enums.BusinessHouseTypeEnums;
import com.apass.zufang.domain.vo.HouseBagVo;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.service.apartment.ApartmentService;
import com.apass.zufang.service.house.HouseService;
import com.apass.zufang.utils.ResponsePageBody;
import com.apass.zufang.utils.ValidateUtils;
/**
 * 房源管理-房源信息管理
 * @author pyc
 *
 */
@Path("/house")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class HouseControler {

	private static final Logger logger = LoggerFactory.getLogger(HouseControler.class);
	
	@Autowired
	private HouseService houseService;
	
	@Autowired
	private ApartmentService apartmentService;
	
	/**
     * 房屋信息列表查询
     * @return
     */
	@POST
	@Path("/queryHouse")
	public Response getHouseList(Map<String,Object> paramMap){
		ResponsePageBody<HouseBagVo> respBody = new ResponsePageBody<>();
        try {
        	//根据当前登录用户，获取所属公寓的Code
        	String apartmentCode = apartmentService.getApartmentCodeByCurrentUser(SpringSecurityUtils.getCurrentUser());
        	HouseQueryParams dto = (HouseQueryParams) MapEntryOrConverUtils.converMap(HouseQueryParams.class, paramMap);
        	dto.setApartmentCode(apartmentCode);
        	if(dto.getPage() == null || dto.getRows() == null){
        		dto.setPage(1);
        		dto.setRows(10);
        	}
        	respBody = houseService.getHouseListExceptDelete(dto);
        	respBody.setMsg("房屋信息列表查询成功!");
        	return Response.success("查询房屋信息成功！", respBody);
        } catch (Exception e) {
            logger.error("getHouseList EXCEPTION --- --->{}", e);
            respBody.setMsg("房屋信息列表查询失败!");
            return Response.fail("查询房屋信息失败!");
        }
	}
	
	
	/**
	 * 添加房屋信息
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/addHouse")
	public Response addHouse(Map<String, Object> paramMap){
		try {
			logger.info("add house paramMap--->{}",GsonUtils.toJson(paramMap));
			Long apartmentId = apartmentService.getApartmentByCurrentUser(SpringSecurityUtils.getCurrentUser());
			validateParams(paramMap,false);
			HouseVo vo = getVoByParams(paramMap);
			vo.setApartmentId(apartmentId);
			houseService.addHouse(vo);
			return Response.success("添加房屋信息成功!");
		}catch (BusinessException e){
			logger.error("add house businessException---->{}",e);
			return Response.fail(e.getErrorDesc());
		}catch (Exception e) {
			logger.error("添加房屋信息失败，错误原因", e);
		    return Response.fail("添加房屋信息失败！");
		}
	}
	
	@POST
	@Path("/detail/init")
	public Response detailHouse(Map<String,Object> paramMap){
		try {
			String id = CommonUtils.getValue(paramMap, "id");
			Map<String,Object> values = houseService.getHouseDetail(id);
			return Response.success("初始化修改页面!", values);
		}catch (BusinessException e){
			logger.error("detail house businessException---->{}",e);
			return Response.fail(e.getErrorDesc());
		}catch (Exception e) {
			logger.error("信息失败，错误原因", e);
		    return Response.fail("初始化修改信息失败！");
		}
	}
	/**
	 * 修改房屋信息
	 * @return
	 */
	@POST
	@Path("/editHouse")
	public Response editHouse(Map<String, Object> paramMap){
		try {
			logger.info("edit house paramMap--->{}",GsonUtils.toJson(paramMap));
			validateParams(paramMap,true);
			HouseVo vo = getVoByParams(paramMap);
			Long apartmentId = apartmentService.getApartmentByCurrentUser(SpringSecurityUtils.getCurrentUser());
			vo.setApartmentId(apartmentId);
			houseService.editHouse(vo);
			return Response.success("修改房屋信息成功!");
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
	@POST
	@Path("/delHouse")
	public Response delHouse(Map<String, Object> paramMap){
		try {
			logger.info("del house paramMap--->{}",GsonUtils.toJson(paramMap));
			String id = CommonUtils.getValue(paramMap, "id");
			ValidateUtils.isNotBlank(id, "房屋Id为空！");
			houseService.deleteHouse(id, SpringSecurityUtils.getCurrentUser());
			return Response.success("删除房屋信息成功！");
		}catch (BusinessException e){
			logger.error("delete house businessException---->{}",e);
			return Response.fail(e.getErrorDesc());
		}catch (Exception e) {
			logger.error("删除房屋信息失败，错误原因", e);
		    return Response.fail("删除房屋信息失败！");
		}
	}
    
    
	@POST
	@Path("/downHouse")
	public Response downHouse(Map<String, Object> paramMap){
    	try {
    		logger.info("upOrDown house paramMap--->{}",GsonUtils.toJson(paramMap));
			String id = CommonUtils.getValue(paramMap, "id");
			ValidateUtils.isNotBlank(id, "房屋Id为空！");
			houseService.downHouse(id, SpringSecurityUtils.getCurrentUser());
			return Response.success("下架成功！");
		}catch (BusinessException e){
			logger.error("down house businessException---->{}",e);
			return Response.fail(e.getErrorDesc());
		}catch (Exception e) {
			logger.error("下架房屋信息失败，错误原因", e);
		    return Response.fail("操作失败！");
		}
    }
    
	@POST
	@Path("/upHouse")
    public Response upHouse(Map<String, Object> paramMap){
    	try {
    		logger.info("batchUp house paramMap--->{}",GsonUtils.toJson(paramMap));
			String id = CommonUtils.getValue(paramMap, "id");
			ValidateUtils.isNotBlank(id, "房屋Id为空！");
			String message = houseService.upHouse(id, SpringSecurityUtils.getCurrentUser());
			return Response.success(message);
		}catch (BusinessException e){
			logger.error("bathUp house businessException---->{}",e);
			return Response.fail(e.getErrorDesc());
		}catch (Exception e) {
			logger.error("上架房屋信息失败，错误原因", e);
		    return Response.fail("操作失败！");
		}
    }
    
	@POST
	@Path("/bathUpHouse")
    public Response batchUp(Map<String, Object> paramMap){
    	try {
    		logger.info("batchUp house paramMap--->{}",GsonUtils.toJson(paramMap));
			String id = CommonUtils.getValue(paramMap, "id");
			ValidateUtils.isNotBlank(id, "房屋Id为空！");
			String message = houseService.upHouses(id, SpringSecurityUtils.getCurrentUser());
			return Response.success(message);
		}catch (BusinessException e){
			logger.error("bathUp house businessException---->{}",e);
			return Response.fail(e.getErrorDesc());
		}catch (Exception e) {
			logger.error("上架房屋信息失败，错误原因", e);
		    return Response.fail("操作失败！");
		}
    }
    
	@POST
	@Path("/delpicture")
    public Response deletePicture(Map<String, Object> paramMap){
    	try {
    		logger.info("delpicture paramMap--->{}",GsonUtils.toJson(paramMap));
			String id = CommonUtils.getValue(paramMap, "id");
			ValidateUtils.isNotBlank(id, "图片Id为空！");
			houseService.delPicture(id);
			return Response.success("删除成功!");
		}catch (BusinessException e){
			logger.error("delpicture businessException---->{}",e);
			return Response.fail(e.getErrorDesc());
		}catch (Exception e) {
			logger.error("删除图片信息失败，错误原因", e);
		    return Response.fail("删除失败！");
		}
    }
	
	/**
	 * 验证所传参数
	 * @param paramMap
	 * @throws BusinessException
	 */
	public void validateParams(Map<String, Object> paramMap,boolean bl) throws BusinessException{
		
		String phone = CommonUtils.getValue(paramMap,"phone");//管家联系方式
		String rentType = CommonUtils.getValue(paramMap, "rentType");//出租方式
		String communityName = CommonUtils.getValue(paramMap, "communityName");//小区名称
		
	    String province = CommonUtils.getValue(paramMap, "province"); // 省
	    String city = CommonUtils.getValue(paramMap, "city"); // 市
	    String district = CommonUtils.getValue(paramMap, "district"); //区
	    String street = CommonUtils.getValue(paramMap, "street"); // 街道
	    String detailAddr = CommonUtils.getValue(paramMap, "detailAddr"); // 详细地址
	    
	    String room = CommonUtils.getValue(paramMap, "room"); //室
	    String hall = CommonUtils.getValue(paramMap, "hall"); //厅
	    String wei = CommonUtils.getValue(paramMap, "wei"); //卫
	    String floor = CommonUtils.getValue(paramMap, "floor"); //第几层
	    String totalFloor = CommonUtils.getValue(paramMap, "totalFloor"); //总共的楼层数
	    
	    String liftType = CommonUtils.getValue(paramMap, "liftType");//有无电梯
	    
	    String chaoxiang = CommonUtils.getValue(paramMap, "chaoxiang");//房屋朝向
	    String zhuangxiu = CommonUtils.getValue(paramMap, "zhuangxiu");//装修情况
	    
	    String acreage = CommonUtils.getValue(paramMap, "acreage");//总面积
	    
	    String totalDoors = CommonUtils.getValue(paramMap, "totalDoors");//几户合租
	    String hezuResource = CommonUtils.getValue(paramMap, "hezuResource");//出租介绍
	    String hezuChaoxiang = CommonUtils.getValue(paramMap, "hezuChaoxiang");//朝向
	    String roomAcreage = CommonUtils.getValue(paramMap, "roomAcreage");
	    
	    String rentAmt = CommonUtils.getValue(paramMap, "rentAmt");//租金
	    String zujinType = CommonUtils.getValue(paramMap, "zujinType");//租金支付方式
	    
	    String title = CommonUtils.getValue(paramMap, "title");//房屋标题
	    String picturs = CommonUtils.getValue(paramMap,"pictures");//图片
	    
	    String houseId = CommonUtils.getValue(paramMap,"houseId");
	    String locationId = CommonUtils.getValue(paramMap,"locationId");
	    if(bl){//修改
	    	ValidateUtils.isNotBlank(houseId, "房屋Id不能为!");
		    ValidateUtils.isNotBlank(locationId, "地址编号不能为空");
	    }
	    ValidateUtils.isNotBlank(phone, "请填写管家联系方式");
	    //20位长度的数字
	    ValidateUtils.isNumberAndCheckLength(phone, 20 ,"管家联系方式");
		ValidateUtils.isNotBlank(rentType, "请选择出租方式");
		ValidateUtils.isNotBlank(communityName, "请填写小区名称");
		ValidateUtils.checkLength(communityName, 2, 20, "2-20个字，可填写汉字，数字，不能填写特殊字符");

	    ValidateUtils.isNotBlank(province, "请选择省份");
	    ValidateUtils.isNotBlank(city, "请选择城市");
	    ValidateUtils.isNotBlank(district, "请选择区域");
	    ValidateUtils.isNotBlank(street, "请选择街道");
	    ValidateUtils.isNotBlank(detailAddr, "请填写详细地址");
	    ValidateUtils.checkLength(detailAddr, 2, 30, "2-30个字，可填写汉字，数字，不能填写特殊字符");
	    
	    ValidateUtils.isNotBlank(room, "请填写室");
	    ValidateUtils.checkNumberRange(room, 1, 0,"室");
	    ValidateUtils.isNotBlank(hall, "请填写厅");
	    ValidateUtils.checkNumberRange(hall, 0, 0, "厅");
	    ValidateUtils.isNotBlank(wei, "请填写卫");
	    ValidateUtils.checkNumberRange(wei, 0, 0, "卫");
	    
	    ValidateUtils.isNotBlank(floor, "请填写楼层");
	    ValidateUtils.checkNumberRange(floor, -9, 99, "楼层分布");
	    ValidateUtils.isNotBlank(totalFloor, "请填写总楼层");
	    ValidateUtils.checkNumberRange(totalFloor, 1, 99, "总楼层");
	    
	    ValidateUtils.isNotBlank(liftType, "请选择电梯情况");
	    ValidateUtils.isNotBlank(chaoxiang, "请选择朝向");
	    ValidateUtils.isNotBlank(zhuangxiu, "请选择装修情况");
	    ValidateUtils.isNotBlank(acreage, "请填写房屋面积");
    	ValidateUtils.checkNonNumberRange(acreage, 1, 9999, "房屋面积");
	    
	    if(StringUtils.equals(BusinessHouseTypeEnums.HZ_2.getCode()+"", rentType)){//如果出租类型为合租
	    	
	    	ValidateUtils.isNotBlank(totalDoors, "请填写合租户数");
	    	ValidateUtils.checkNonNumberRange(totalDoors, 1, 99, "合租户数");
	    	
	    	ValidateUtils.isNotBlank(hezuResource, "请选择出租间介绍");
	    	ValidateUtils.isNotBlank(hezuChaoxiang, "请选择出租间朝向");
	    	
	    	ValidateUtils.isNotBlank(roomAcreage, "请填写出租间房屋面积");
	    	ValidateUtils.checkNonNumberRange(roomAcreage, 1, 9999, "房屋面积");
	    }
	    
	    ValidateUtils.isNotBlank(rentAmt, "请填写租金");
	    ValidateUtils.checkNumberRange(rentAmt, 0, 0, "租金");
	    ValidateUtils.isNotBlank(zujinType, "请选择租金支付方式");
	    
	    ValidateUtils.isNotBlank(title, "请填写房源标题");
		ValidateUtils.checkLength(title, 6, 30, "请填写6-30个字");
		ValidateUtils.isNotBlank(picturs, "请上传图片");
		
		String[] pictures = StringUtils.split(picturs,",");
		if(pictures.length > 8){
			throw new BusinessException("上传图片最多8张!");
		}
	}
	
	/***
	 * paramToVo
	 * @param paramMap
	 * @return
	 */
	public HouseVo getVoByParams(Map<String, Object> paramMap){
		
		HouseVo house = new HouseVo();
		String phone = CommonUtils.getValue(paramMap, "phone");
		house.setHousekeeperTel(phone);
		String rentType = CommonUtils.getValue(paramMap, "rentType");
		house.setRentType(Byte.valueOf(rentType));
		String communityName = CommonUtils.getValue(paramMap, "communityName");
		house.setCommunityName(communityName);
	    String province = CommonUtils.getValue(paramMap, "province"); // 省
	    house.setProvince(province);
	    String city = CommonUtils.getValue(paramMap, "city"); // 市
	    house.setCity(city);
	    String district = CommonUtils.getValue(paramMap, "district"); // 区
	    house.setDistrict(district);
	    String street = CommonUtils.getValue(paramMap, "street"); //街道
	    house.setStreet(street);
	    String detailAddr = CommonUtils.getValue(paramMap, "detailAddr"); // 详细地址
	    house.setDetailAddr(detailAddr);
	    
	    String acreage = CommonUtils.getValue(paramMap, "acreage");
	    house.setAcreage(new BigDecimal(acreage));
	    
	    String room = CommonUtils.getValue(paramMap, "room"); //室
	    house.setRoom(Integer.parseInt(room));
	    String hall = CommonUtils.getValue(paramMap, "hall"); //厅
	    house.setHall(Integer.parseInt(hall));
	    String wei = CommonUtils.getValue(paramMap, "wei"); //卫
	    house.setWei(Integer.parseInt(wei));
	    String floor = CommonUtils.getValue(paramMap, "floor"); //第几层
	    house.setFloor(Integer.parseInt(floor));
	    String totalFloor = CommonUtils.getValue(paramMap, "totalFloor"); //总共的楼层数
	    house.setTotalFloor(Integer.parseInt(totalFloor));
	    
	    String liftType = CommonUtils.getValue(paramMap, "liftType");//有无电梯
	    house.setLiftType(Byte.valueOf(liftType));
	    String rentAmt = CommonUtils.getValue(paramMap, "rentAmt");
	    house.setRentAmt(new BigDecimal(rentAmt));
	    String zujinType = CommonUtils.getValue(paramMap, "zujinType");
	    house.setZujinType(Byte.valueOf(zujinType));
	    
	    String chaoxiang = CommonUtils.getValue(paramMap, "chaoxiang");
	    house.setChaoxiang(Byte.valueOf(chaoxiang));
	    String zhuangxiu = CommonUtils.getValue(paramMap, "zhuangxiu");
	    house.setZhuangxiu(Byte.valueOf(zhuangxiu));
	    
	    String peizhi = CommonUtils.getValue(paramMap,"peizhi");//配置
	    String picturs = CommonUtils.getValue(paramMap,"pictures");//图片
	    
	    if(StringUtils.isNotBlank(peizhi)){
	    	String[] peizhis = StringUtils.split(peizhi, ",");
		    house.setConfigs(Arrays.asList(peizhis));
	    }
	    
	    String[] pictures = StringUtils.split(picturs,",");
	    house.setPictures(Arrays.asList(pictures));
	    
	    if(StringUtils.equals(BusinessHouseTypeEnums.HZ_2.getCode()+"", rentType)){//如果出租类型为合租
	    	String totalDoors = CommonUtils.getValue(paramMap, "totalDoors");//几户合租
		    String hezuResource = CommonUtils.getValue(paramMap, "hezuResource");//出租介绍
		    String hezuChaoxiang = CommonUtils.getValue(paramMap, "hezuChaoxiang");//朝向
		    String roomAcreage = CommonUtils.getValue(paramMap, "roomAcreage");
		    
		    house.setTotalDoors(totalDoors);
		    house.setHezuResource(Byte.valueOf(hezuResource));
		    house.setHezuChaoxiang(Byte.valueOf(hezuChaoxiang));
		    house.setRoomAcreage(new BigDecimal(roomAcreage));
	    }
	    
	    String title = CommonUtils.getValue(paramMap, "title");
	    house.setTitle(title);
		
	    String houseId = CommonUtils.getValue(paramMap,"houseId");
	    Date date = new Date();
	    String operateName = SpringSecurityUtils.getCurrentUser();
	    
	    house.setUpdatedTime(date);
	    house.setUpdatedUser(operateName);
	    if(StringUtils.isBlank(houseId)){
	    	house.setCreatedTime(date);
	    	house.setCreatedUser(operateName);
	    	return house;
	    }
	    String locationId = CommonUtils.getValue(paramMap,"locationId");
	    if(StringUtils.isNotBlank(locationId)){
	    	house.setLocationId(locationId);
	    }
	    house.setHouseId(Long.parseLong(houseId));
		return house;
	}
}
