package com.apass.zufang.service.appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.apass.zufang.domain.entity.ReserveHouse;
import com.apass.zufang.mapper.zfang.ReserveHouseMapper;

@Service
public class ReserveHouseService {
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
}