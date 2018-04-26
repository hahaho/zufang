package com.apass.zufang.service.appointment;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.apass.zufang.domain.entity.ReserveRecord;
import com.apass.zufang.mapper.zfang.ReserveRecordMapper;
@Service
public class ReserveRecordService {
	@Autowired
	private ReserveRecordMapper reserveRecordMapper;
	/**
	 * createEntity 
	 * @param entity
	 * @return
	 */
	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public int createEntity(ReserveRecord entity) {
		return reserveRecordMapper.insertSelective(entity);
	}
	/**
	 * updateEntity
	 * @param entity
	 * @return
	 */
	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public int updateEntity(ReserveRecord entity) {
		return reserveRecordMapper.updateByPrimaryKeySelective(entity);
	}
	/**
	 * readEntity
	 * @param id
	 * @return
	 */
	public ReserveRecord readEntity(Long id) {
		return reserveRecordMapper.selectByPrimaryKey(id);
	}
	/**
	 * 预约行程管理 预约看房记录变更表弹窗列表查询
	 * @param entity
	 * @return
	 */
	public List<ReserveRecord> getReserveRecordList(ReserveRecord entity) {
		return reserveRecordMapper.getReserveRecordList(entity);
	}
}