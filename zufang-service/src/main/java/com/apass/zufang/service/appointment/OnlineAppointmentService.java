package com.apass.zufang.service.appointment;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.utils.BaseConstants;
import com.apass.zufang.domain.entity.HouseShowingsEntity;
import com.apass.zufang.domain.entity.ReserveHouse;
import com.apass.zufang.domain.vo.ReservationsShowingsEntity;
import com.apass.zufang.mapper.zfang.ReserveHouseMapper;
import com.apass.zufang.utils.ResponsePageBody;
/**
 * 在线预约看房
 */
@Component
public class OnlineAppointmentService {
	private static final Logger LOGGER = LoggerFactory.getLogger(OnlineAppointmentService.class);
	@Autowired
	private ReserveHouseMapper reserveHouseMapper;
	@Value("${zufang.image.uri}")
	private String imageUri;
	/**
	 * 插入数据库
	 * @param houseId
	 * @param userId
	 * @param telphone
	 * @param name
	 * @param reservedate
	 * @param memo
	 * @return
	 * @throws BusinessException
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
			setreserveHouse.setReserveStatus((byte)1);//新增的看房行程  为1：已预约（首次预约成功默认状态）  默认状态
			//判断是否重复   
			Integer selectByPrimaryKey = reserveHouseMapper.selectRepeat(setreserveHouse);
			//已经预约了5次
			Integer selectrepeat = reserveHouseMapper.selectrepeat(telphone);
			if(selectByPrimaryKey < 1 && selectrepeat < 5){
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
	/**
	 * queryReservations
	 * @param crmety
	 * @return
	 * @throws BusinessException
	 */
	public ResponsePageBody<HouseShowingsEntity> queryReservations(ReservationsShowingsEntity crmety)throws BusinessException {
 		ResponsePageBody<HouseShowingsEntity> body = new ResponsePageBody<>();
		List<HouseShowingsEntity> houseList = reserveHouseMapper.getHouseLists(crmety);
		//设置图片地址
		for (HouseShowingsEntity houseShowingsEntity : houseList) {
			if(houseShowingsEntity.getUrl().contains("http")){
				houseShowingsEntity.setUrl(houseShowingsEntity.getUrl());
			}else{
				houseShowingsEntity.setUrl(imageUri+houseShowingsEntity.getUrl());
			}

		}
		body.setRows(houseList);
		body.setTotal(reserveHouseMapper.getCount(crmety.getTelphone()));
		body.setStatus(BaseConstants.CommonCode.SUCCESS_CODE);
		return body;
	}
	/**
	 * 是否过期
	 * @param telphone
	 * @param houseId
	 * @return
	 */
	public Integer queryOverdue(String telphone ,String houseId) {
		ReservationsShowingsEntity reservationsShowingsEntity = new ReservationsShowingsEntity();
		reservationsShowingsEntity.setTelphone(telphone);
		reservationsShowingsEntity.setHouseId(Long.parseLong(houseId));
		return reserveHouseMapper.queryOverdue(reservationsShowingsEntity);
	}
}