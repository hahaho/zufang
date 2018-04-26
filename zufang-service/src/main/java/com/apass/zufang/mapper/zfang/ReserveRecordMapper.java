package com.apass.zufang.mapper.zfang;
import java.util.List;
import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.entity.ReserveRecord;
/**
 * 预约看房记录变更表
 * @date 2018/4/26.
 */
public interface ReserveRecordMapper extends GenericMapper<ReserveRecord,Long> {
	/**
	 * 
	 * @param entity
	 * @return
	 */
	public List<ReserveRecord> getReserveRecordList(ReserveRecord entity);
}