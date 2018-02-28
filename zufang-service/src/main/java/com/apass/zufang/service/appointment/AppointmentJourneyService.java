package com.apass.zufang.service.appointment;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.apass.gfb.framework.utils.BaseConstants;
import com.apass.zufang.domain.dto.ApprintmentJourneyQueryParams;
import com.apass.zufang.domain.vo.ReserveHouseVo;
import com.apass.zufang.utils.ResponsePageBody;
@Service
public class AppointmentJourneyService {
	@Autowired
	private ReserveHouseService reserveHouseService;
	/**
	 * 预约行程管理 预约看房记录列表查询
	 * @param entity
	 * @return
	 */
	public ResponsePageBody<ReserveHouseVo> getReserveHouseList(ApprintmentJourneyQueryParams entity) {
		ResponsePageBody<ReserveHouseVo> pageBody = new ResponsePageBody<ReserveHouseVo>();
        List<ReserveHouseVo> list = reserveHouseService.getReserveHouseList(entity);
        pageBody.setTotal(list.size());
        pageBody.setRows(list);
        pageBody.setStatus(BaseConstants.CommonCode.SUCCESS_CODE);
        return pageBody;
	}
}