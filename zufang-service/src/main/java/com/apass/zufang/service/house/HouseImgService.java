package com.apass.zufang.service.house;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apass.zufang.domain.entity.HouseImg;
import com.apass.zufang.domain.enums.IsDeleteEnums;
import com.apass.zufang.mapper.zfang.HouseImgMapper;

@Service
public class HouseImgService {

	@Autowired
	private HouseImgMapper imgMapper;
	
	@Transactional(rollbackFor = { Exception.class,RuntimeException.class})
	public void deleteImgByHouseId(Long houseId){
		List<HouseImg> imgs = imgMapper.getImgByHouseId(houseId);
		for (HouseImg img : imgs) {
			if(StringUtils.equals(img.getIsDelete(), IsDeleteEnums.IS_DELETE_00.getCode())){
				img.setIsDelete(IsDeleteEnums.IS_DELETE_01.getCode());
				img.setUpdatedTime(new Date());
				imgMapper.updateByPrimaryKeySelective(img);
			}
		}
	}
}
