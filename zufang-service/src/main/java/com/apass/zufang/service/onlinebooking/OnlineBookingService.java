package com.apass.zufang.service.onlinebooking;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.utils.BaseConstants;
import com.apass.zufang.domain.entity.ReserveHouse;
import com.apass.zufang.mapper.zfang.ReserveHouseMapper;
import com.apass.zufang.utils.ResponsePageBody;

/**
 * 在线预约看房
 */
@Component
public class OnlineBookingService {
	private static final Logger LOGGER = LoggerFactory.getLogger(OnlineBookingService.class);

	@Autowired
	private ReserveHouseMapper reserveHouseMapper;

	/**
	 * 插入数据库
	 */
	public Integer insetReserveHouse(String houseId, String userId, String telphone, String name, String reservedate,String memo) throws BusinessException {
		ReserveHouse setreserveHouse = new ReserveHouse();
		try {
			java.text.SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			setreserveHouse.setHouseId(Long.parseLong(houseId));// 房源主键
			setreserveHouse.setUserId(userId);// 用户ID
			setreserveHouse.setType(new Byte("1"));
			setreserveHouse.setTelphone(telphone);// 联系方式
			setreserveHouse.setName(name);// 姓名
			setreserveHouse.setReserveDate(formatter.parse(reservedate));// 看房时间
			setreserveHouse.setMemo(memo);// 留言
			setreserveHouse.setIsDelete("00");
			setreserveHouse.setCreatedTime(new Date());
			setreserveHouse.setUpdatedTime(new Date());
			
			//判断是否重复   或者  已经预约了5次
			ReserveHouse selectByPrimaryKey = reserveHouseMapper.selectByPrimaryKey(setreserveHouse.getHouseId());
			
			Integer selectrepeat = reserveHouseMapper.selectrepeat(telphone);
			if(selectByPrimaryKey == null && selectrepeat <= 5){
				reserveHouseMapper.insert(setreserveHouse);
				return 1;
			}else{
				return 0;
			}
		} catch (Exception e) {
			LOGGER.error("插入数据出错", e);
		}
		return null;
	}
	
	
	public ResponsePageBody<ReserveHouse> queryReservations(ReserveHouse crmety) {
		ResponsePageBody<ReserveHouse> body = new ResponsePageBody<>();
		List<ReserveHouse> houseList = reserveHouseMapper.getHouseLists(crmety);
		body.setRows(houseList);
		body.setTotal(reserveHouseMapper.getCount(crmety.getTelphone()));
		body.setStatus(BaseConstants.CommonCode.SUCCESS_CODE);
		return body;
	}
}
