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
	private HouseImgMapper houseImgMapper;
	/**
	 * deleteImgByHouseId
	 * @param houseId
	 */
	@Transactional(rollbackFor = { Exception.class,RuntimeException.class})
	public void deleteImgByHouseId(Long houseId){
		HouseImg houseImg = new HouseImg();
		houseImg.setType((byte)0);
		houseImg.setHouseId(houseId);
		List<HouseImg> imgs = houseImgMapper.getImgByHouseId(houseImg);
		for (HouseImg img : imgs) {
			if(StringUtils.equals(img.getIsDelete(), IsDeleteEnums.IS_DELETE_00.getCode())){
				img.setIsDelete(IsDeleteEnums.IS_DELETE_01.getCode());
				img.setUpdatedTime(new Date());
				houseImgMapper.updateByPrimaryKeySelective(img);
			}
		}
	}
	/**
	 * getHouseImgList
	 * @param houseId
	 * @return
	 */
	public List<HouseImg> getHouseImgList(Long houseId,byte type){
		HouseImg houseImg = new HouseImg();
		houseImg.setType(type);
		houseImg.setHouseId(houseId);
		return houseImgMapper.getImgByHouseId(houseImg);
	}
	
	/**
	 * getHouseImgList
	 * @param houseId
	 * @return
	 */
	public List<HouseImg> getHouseImgList(Long houseId){
		HouseImg houseImg = new HouseImg();
		houseImg.setHouseId(houseId);
		return houseImgMapper.getImgByHouseId(houseImg);
	}
}
