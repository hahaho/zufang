package com.apass.zufang.service.appointment;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apass.zufang.domain.dto.ApprintmentJourneyQueryParams;
import com.apass.zufang.domain.dto.HouseAppointmentQueryParams;
import com.apass.zufang.domain.entity.ReserveHouse;
import com.apass.zufang.domain.vo.HouseAppointmentVo;
import com.apass.zufang.domain.vo.ReserveHouseVo;
import com.apass.zufang.mapper.zfang.HouseMapper;
import com.apass.zufang.mapper.zfang.ReserveHouseMapper;
@Service
public class ReserveHouseService {
	@Autowired
	private HouseMapper houseMapper;
	@Autowired
	private ReserveHouseMapper reserveHouseMapper;
	/**
	 * createEntity 
	 * @param entity
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class})
	public int createEntity(ReserveHouse entity) {
		return reserveHouseMapper.insertSelective(entity);
	}
	/**
	 * updateEntity
	 * @param entity
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class})
	public int updateEntity(ReserveHouse entity) {
		return reserveHouseMapper.updateByPrimaryKeySelective(entity);
	}
	/**
	 * readEntity
	 * @param id
	 * @return
	 */
	public ReserveHouse readEntity(Long id) {
		return reserveHouseMapper.selectByPrimaryKey(id);
	}
	/**
	 * 电话预约管理 房源列表查询
	 * @param entity
	 * @return
	 */
	public List<HouseAppointmentVo> getHouseListForPhoneAppointment(HouseAppointmentQueryParams entity) {
		return houseMapper.getHouseListForPhoneAppointment(entity);
	}
	/**
	 * 预约行程管理 预约看房记录列表查询
	 * @param entity
	 * @return
	 */
	public List<ReserveHouseVo> getReserveHouseList(ApprintmentJourneyQueryParams entity) {
		return reserveHouseMapper.getReserveHouseList(entity);
	}
}