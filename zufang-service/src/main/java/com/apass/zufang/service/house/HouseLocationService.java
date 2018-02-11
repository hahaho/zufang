package com.apass.zufang.service.house;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apass.zufang.domain.entity.HouseLocation;
import com.apass.zufang.domain.enums.IsDeleteEnums;
import com.apass.zufang.mapper.zfang.HouseLocationMapper;

/**
 * 房源地址管理
 * @author Administrator
 *
 */
@Service
public class HouseLocationService {

	@Autowired
	private HouseLocationMapper locationMapper;
	
	@Transactional(rollbackFor = { Exception.class,RuntimeException.class})
	public void deleteLocationByHouseId(Long houseId){
		//根据房屋Id，查询状态为00 的地址信息
		HouseLocation location = locationMapper.getLocationByHouseId(houseId);
		if(StringUtils.equals(location.getIsDelete(), IsDeleteEnums.IS_DELETE_00.getCode())){
			location.setIsDelete(IsDeleteEnums.IS_DELETE_01.getCode());
			location.setUpdatedTime(new Date());
			locationMapper.updateByPrimaryKeySelective(location);
		}
		
	}
}
