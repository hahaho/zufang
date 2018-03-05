package com.apass.zufang.service.appointment;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apass.gfb.framework.utils.BaseConstants;
import com.apass.gfb.framework.utils.DateFormatUtil;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.dto.ApprintmentJourneyQueryParams;
import com.apass.zufang.domain.entity.ReserveHouse;
import com.apass.zufang.domain.entity.ReturnVisit;
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
	public ResponsePageBody<ReserveHouseVo> getReserveHouseList(ApprintmentJourneyQueryParams entity) {
		ResponsePageBody<ReserveHouseVo> pageBody = new ResponsePageBody<ReserveHouseVo>();
        List<ReserveHouseVo> list = reserveHouseService.getReserveHouseList(entity);
        for(ReserveHouseVo vo : list){
        	String reserveType = vo.getType()==(byte)1?"在线预约":"电话预约";
        	vo.setReserveType(reserveType);
        	vo.setCreatedDateTime(DateFormatUtil.dateToString(vo.getCreatedTime()));
        	vo.setReserveType(DateFormatUtil.dateToString(vo.getReserveDate()));
        }
        pageBody.setTotal(list.size());
        pageBody.setRows(list);
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
        	String reserveType = vo.getType()==(byte)1?"在线预约":"电话预约";
        	vo.setReserveType(reserveType);
        	vo.setCreatedDateTime(DateFormatUtil.dateToString(vo.getCreatedTime()));
        	vo.setReserveType(DateFormatUtil.dateToString(vo.getReserveDate()));
        }
		return Response.success("预约行程管理 看房记录导出成功！",list);
	}
}