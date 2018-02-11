package com.apass.zufang.service.house;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apass.zufang.domain.entity.HousePeizhi;
import com.apass.zufang.domain.enums.IsDeleteEnums;
import com.apass.zufang.mapper.zfang.HousePeizhiMapper;

@Service
public class HousePeiZhiService {

	@Autowired
	private HousePeizhiMapper peizhiMapper;
	
	@Transactional(rollbackFor = { Exception.class,RuntimeException.class})
	public void deletePeiZhiByHouseId(Long houseId){
		List<HousePeizhi> peizhis = peizhiMapper.getPeiZhiByHouseId(houseId);
		for (HousePeizhi peizhi : peizhis) {
			if(StringUtils.equals(peizhi.getIsDelete(), IsDeleteEnums.IS_DELETE_00.getCode())){
				peizhi.setIsDelete(IsDeleteEnums.IS_DELETE_01.getCode());
				peizhi.setUpdatedTime(new Date());
				peizhiMapper.updateByPrimaryKeySelective(peizhi);
			}
		}
	}
}
