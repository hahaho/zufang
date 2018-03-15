package com.apass.zufang.service.appointment;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.apass.gfb.framework.utils.BaseConstants;
import com.apass.gfb.framework.utils.DateFormatUtil;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.dto.ApprintmentJourneyQueryParams;
import com.apass.zufang.domain.entity.ReserveHouse;
import com.apass.zufang.domain.entity.ReturnVisit;
import com.apass.zufang.domain.enums.BusinessHouseTypeEnums;
import com.apass.zufang.domain.vo.ReserveHouseVo;
import com.apass.zufang.utils.ResponsePageBody;
@Service
public class AppointmentJourneyService {
	@Autowired
	private ReserveHouseService reserveHouseService;
	@Autowired
	private ReturnVisitService returnVisitService;
	/**
	 * 预约行程管理 预约看房记录列表查询
	 * @param entity
	 * @return
	 */
	public ResponsePageBody<ReserveHouseVo> getReserveHouseList(ApprintmentJourneyQueryParams entity,ApprintmentJourneyQueryParams count) {
		ResponsePageBody<ReserveHouseVo> pageBody = new ResponsePageBody<ReserveHouseVo>();
        List<ReserveHouseVo> list = reserveHouseService.getReserveHouseList(entity);
        for(ReserveHouseVo vo : list){
        	//预约类型
        	String reserveType = vo.getType()==(byte)1?"在线预约":"电话预约";
        	vo.setReserveType(reserveType);
        	//预约和看房时间
        	vo.setCreatedDateTime(DateFormatUtil.dateToString(vo.getCreatedTime(),null));
        	vo.setReserveDateTime(DateFormatUtil.dateToString(vo.getReserveDate(),DateFormatUtil.YYYY_MM_DD_HH_MM));
        	//户型
        	vo.setHouseAll(vo.getHouseRoom()+"室"+vo.getHouseHall()+"厅"+vo.getHouseWei()+"卫");
        	//面积
        	vo.setHouseAcreage(vo.getHouseAcreage().substring(0, vo.getHouseAcreage().length()-2));
        	vo.setHouseRoomAcreage(vo.getHouseRoomAcreage().substring(0, vo.getHouseRoomAcreage().length()-2));
        	//付款方式
        	String zujintype = vo.getHouseZujinType();
        	Integer zujinType = Integer.parseInt(zujintype.toString());
        	if(zujinType==BusinessHouseTypeEnums.YJLX_1.getCode()){
        		vo.setHouseZujinType(BusinessHouseTypeEnums.YJLX_1.getMessage());
        	}else if(zujinType==BusinessHouseTypeEnums.YJLX_2.getCode()){
        		vo.setHouseZujinType(BusinessHouseTypeEnums.YJLX_2.getMessage());
        	}else if(zujinType==BusinessHouseTypeEnums.YJLX_3.getCode()){
        		vo.setHouseZujinType(BusinessHouseTypeEnums.YJLX_3.getMessage());
        	}else if(zujinType==BusinessHouseTypeEnums.YJLX_4.getCode()){
        		vo.setHouseZujinType(BusinessHouseTypeEnums.YJLX_4.getMessage());
        	}else if(zujinType==BusinessHouseTypeEnums.YJLX_5.getCode()){
        		vo.setHouseZujinType(BusinessHouseTypeEnums.YJLX_5.getMessage());
        	}else if(zujinType==BusinessHouseTypeEnums.YJLX_6.getCode()){
        		vo.setHouseZujinType(BusinessHouseTypeEnums.YJLX_6.getMessage());
        	}else if(zujinType==BusinessHouseTypeEnums.YJLX_7.getCode()){
        		vo.setHouseZujinType(BusinessHouseTypeEnums.YJLX_7.getMessage());
        	}else if(zujinType==BusinessHouseTypeEnums.YJLX_8.getCode()){
        		vo.setHouseZujinType(BusinessHouseTypeEnums.YJLX_8.getMessage());
        	}else if(zujinType==BusinessHouseTypeEnums.YJLX_9.getCode()){
        		vo.setHouseZujinType(BusinessHouseTypeEnums.YJLX_9.getMessage());
        	}else{
        		vo.setHouseZujinType(BusinessHouseTypeEnums.YJLX_10.getMessage());
        	}
        }
        pageBody.setRows(list);
        list = reserveHouseService.getReserveHouseList(count);
        pageBody.setTotal(list.size());
        pageBody.setStatus(BaseConstants.CommonCode.SUCCESS_CODE);
        return pageBody;
	}
	/**
	 * 预约行程管理 预约看房记录编辑
	 * @param reserveHouseId
	 * @param username
	 * @return
	 */
	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public Response editReserveHouse(ReserveHouse entity, String username,Date reserveDate) {
		if(reserveDate.getTime()>new Date().getTime()){
			return Response.fail("预约行程管理  预约看房记录编辑失败,看房时间选择错误,请重新选择！");
		}
		entity.setReserveDate(reserveDate);
		if(reserveHouseService.updateEntity(entity)==1){
			return Response.success("预约行程管理 预约看房记录编辑成功！");
		}
		return Response.fail("预约行程管理 预约看房记录编辑失败！");
	}
	/**
	 * 预约行程管理 预约看房记录删除
	 * @param reserveHouseId
	 * @param username
	 * @return
	 */
	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public Response deleReserveHouse(String reserveHouseId, String username) {
		ReserveHouse entity = reserveHouseService.readEntity(Long.parseLong(reserveHouseId));
		entity.setIsDelete("01");
		entity.setUpdatedTime(new Date());
		if(reserveHouseService.updateEntity(entity)==1){
			return Response.success("预约行程管理 预约看房记录删除成功！");
		}
		return Response.fail("预约行程管理 预约看房记录删除失败！");
	}
	/**
	 * 预约行程管理 客户回访记录新增
	 * @param map
	 * @return
	 */
	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public Response addReturnVisit(ReturnVisit entity, String username) {
		entity.setIsDelete("00");
		entity.setCreatedTime(new Date());
		entity.setUpdatedTime(new Date());
		if(returnVisitService.createEntity(entity)==1){
			return Response.success("预约行程管理 客户回访记录新增成功！");
		}
		return Response.fail("预约行程管理 客户回访记录新增失败！");
	}
	/**
	 * 预约行程管理 看房记录导出
	 * @param entity
	 * @return
	 */
	public Response downLoadReserveHouseList(ApprintmentJourneyQueryParams entity) {
		List<ReserveHouseVo> list = reserveHouseService.getReserveHouseList(entity);
        for(ReserveHouseVo vo : list){
        	vo.setName(encryptCustomerName(vo.getName()));
        	String reserveType = vo.getType()==(byte)1?"在线预约":"电话预约";
        	vo.setReserveType(reserveType);
        	//预约和看房时间
        	vo.setCreatedDateTime(DateFormatUtil.dateToString(vo.getCreatedTime(),null));
        	vo.setReserveDateTime(DateFormatUtil.dateToString(vo.getReserveDate(),DateFormatUtil.YYYY_MM_DD_HH_MM));
        }
		return Response.success("预约行程管理 看房记录导出成功！",list);
	}
	/**
	 * encryptCustomerName
	 * 加密客户姓名
	 * @param name
	 * @return
	 */
	private String encryptCustomerName(String name) {
		if(StringUtils.isBlank(name)){
			return "";
		}
		Integer length = name.length();
		if(length<2){
			return name;
		}else if(length==2){
			return name.substring(0, 1)+"*";
		}else{
			return name.substring(0, 1)+"*"+name.substring(length-1, length);
		}
	}
}