package com.apass.zufang.service.appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.apass.zufang.domain.entity.ReturnVisit;
import com.apass.zufang.mapper.zfang.ReturnVisitMapper;
public class ReturnVisitService {
	@Autowired
	private ReturnVisitMapper returnVisitMapper;
	/**
	 * createEntity 
	 * @param entity
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class})
	public int createEntity(ReturnVisit entity) {
		return returnVisitMapper.insertSelective(entity);
	}
}