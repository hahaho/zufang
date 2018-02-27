package com.apass.zufang.service.appointment;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.apass.gfb.framework.utils.BaseConstants;
import com.apass.zufang.domain.dto.HouseAppointmentQueryParams;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.mapper.zfang.HouseMapper;
import com.apass.zufang.utils.ResponsePageBody;
public class PhoneAppointmentService {
	@Autowired
	private HouseMapper houseMapper;
	/**
	 * 电话预约管理 房源列表查询
	 * @param entity
	 * @return
	 */
	public ResponsePageBody<HouseVo> getHouseListForPhoneAppointment(HouseAppointmentQueryParams entity) {
		ResponsePageBody<HouseVo> pageBody = new ResponsePageBody<HouseVo>();
        List<HouseVo> list = houseMapper.getHouseListForPhoneAppointment(entity);
        pageBody.setTotal(list.size());
        pageBody.setRows(list);
        pageBody.setStatus(BaseConstants.CommonCode.SUCCESS_CODE);
        return pageBody;
	}
}
