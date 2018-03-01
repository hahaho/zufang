package com.apass.zufang.service.onlinebooking;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.zufang.domain.entity.ReserveHouse;
import com.apass.zufang.mapper.zfang.ReserveHouseMapper;

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
	public void insetReserveHouse(String houseId,String userId,String telphone,String name,String reservedate,String memo)throws BusinessException{
		ReserveHouse setreserveHouse = new ReserveHouse();
		try {
		java.text.SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd");
			setreserveHouse.setHouseId(Long.parseLong(houseId));//房源主键
			setreserveHouse.setUserId(userId);//用户ID
			setreserveHouse.setType(new Byte("1"));
			setreserveHouse.setTelphone(telphone);//联系方式
			setreserveHouse.setName(name);//姓名
			setreserveHouse.setReserveDate(formatter.parse(reservedate));//看房时间
			setreserveHouse.setMemo(memo);//留言
			setreserveHouse.setIsDelete("00");
			setreserveHouse.setCreatedTime(new Date());
			setreserveHouse.setUpdatedTime(new Date());
			//插入数据库
			reserveHouseMapper.insert(setreserveHouse);
			
		} catch (Exception e) {
			LOGGER.error("插入数据出错", e);
		}
	}
}
