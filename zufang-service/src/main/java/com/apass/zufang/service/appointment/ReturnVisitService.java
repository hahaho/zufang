package com.apass.zufang.service.appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.apass.zufang.domain.entity.ReturnVisit;
import com.apass.zufang.mapper.zfang.ReturnVisitMapper;
@Service
public class ReturnVisitService {
	@Autowired
	private ReturnVisitMapper returnVisitMapper;
	/**
	 * createEntity 
	 * @param entity
	 * @return
	 */
	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public int createEntity(ReturnVisit entity) {
		return returnVisitMapper.insertSelective(entity);
	}
}