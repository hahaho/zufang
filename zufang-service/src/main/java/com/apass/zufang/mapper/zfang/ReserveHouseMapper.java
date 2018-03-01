package com.apass.zufang.mapper.zfang;
import java.util.List;
import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.dto.ApprintmentJourneyQueryParams;
import com.apass.zufang.domain.entity.ReserveHouse;
import com.apass.zufang.domain.vo.ReserveHouseVo;
/**
 * Created by DELL on 2018/2/26.
 */
public interface ReserveHouseMapper extends GenericMapper<ReserveHouse,Long> {
	/**
	 * 预约行程管理 预约看房记录列表查询
	 * @param entity
	 * @return
	 */
	public List<ReserveHouseVo> getReserveHouseList(ApprintmentJourneyQueryParams entity);
}