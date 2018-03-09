package com.apass.zufang.service.appointment;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.apass.gfb.framework.utils.BaseConstants;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.dto.HouseAppointmentQueryParams;
import com.apass.zufang.domain.entity.HousePeizhi;
import com.apass.zufang.domain.entity.ReserveHouse;
import com.apass.zufang.domain.vo.HouseAppointmentVo;
import com.apass.zufang.service.house.HousePeiZhiService;
import com.apass.zufang.utils.ResponsePageBody;
@Service
public class PhoneAppointmentService {
	@Autowired
	private HousePeiZhiService housePeiZhiService;
	@Autowired
	private ReserveHouseService reserveHouseService;
	/**
	 * 电话预约管理 房源列表查询
	 * @param entity
	 * @return
	 */
	public ResponsePageBody<HouseAppointmentVo> getHouseListForPhoneAppointment(HouseAppointmentQueryParams entity,HouseAppointmentQueryParams count) {
		ResponsePageBody<HouseAppointmentVo> pageBody = new ResponsePageBody<HouseAppointmentVo>();
        List<HouseAppointmentVo> list = reserveHouseService.getHouseListForPhoneAppointment(entity);
        for(HouseAppointmentVo vo : list){
        	vo.setHouseAll(vo.getHouseRoom()+"室"+vo.getHouseHall()+"厅"+vo.getHouseWei()+"卫");
        }
        pageBody.setRows(list);
        list = reserveHouseService.getHouseListForPhoneAppointment(count);
        pageBody.setTotal(list.size());
        pageBody.setStatus(BaseConstants.CommonCode.SUCCESS_CODE);
        return pageBody;
	}
	/**
	 * 电话预约管理 预约看房记录新增
	 * @param entity
	 * @return
	 */
	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public Response addReserveHouse(ReserveHouse entity,String user,Date reserveDate) {
		entity.setType((byte)2);
		entity.setReserveDate(reserveDate);
		entity.setIsDelete("00");
		entity.setCreatedTime(new Date());
		entity.setUpdatedTime(new Date());
		if(reserveHouseService.createEntity(entity)==1){
			return Response.success("预约看房记录新增成功！");
		}
		return Response.fail("预约看房记录新增失败！");
	}
	@SuppressWarnings("unused")
	private List<HouseAppointmentVo> checkHouseList(List<HouseAppointmentVo> list){
		for(HouseAppointmentVo vo : list){
        	List<HousePeizhi> peizhiList = housePeiZhiService.getHousePeizhiList(vo.getHouseId());
        	Boolean houseKitchenFalg = checkhouseKitchenToiletFalg(peizhiList,"可做饭");
        	if(houseKitchenFalg){
        		vo.setHouseKitchenFalg("是");
        	}else{
        		vo.setHouseKitchenFalg("否");
        	}
        	Boolean houseToiletFalg = checkhouseKitchenToiletFalg(peizhiList,"独卫");
        	if(houseToiletFalg){
        		vo.setHouseToiletFalg("是");
        	}else{
        		vo.setHouseToiletFalg("否");
        	}
		}
		return list;
	}
	private Boolean checkhouseKitchenToiletFalg(List<HousePeizhi> peizhiList, String peizhiName) {
    	for(HousePeizhi peizhi : peizhiList){
    		if(StringUtils.equals(peizhi.getName(), peizhiName)){
    			return true;
    		}
    	}
    	return false;
	}
}