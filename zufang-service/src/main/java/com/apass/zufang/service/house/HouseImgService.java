
package com.apass.zufang.service.house;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.zufang.domain.entity.HouseImg;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.mapper.zfang.HouseImgMapper;
@Service
public class HouseImgService {
	@Autowired
	private HouseImgMapper houseImgMapper;
	/**
	 * 根据房屋Id，批量删除图片信息
	 * deleteImgByHouseId
	 * @param houseId
	 */
	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public void deleteImgByHouseId(Long houseId){
		houseImgMapper.deleteImgByHouseId(houseId);
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
	
	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public void insertImg(HouseVo houseVo) throws BusinessException{
		if(null == houseVo || CollectionUtils.isEmpty(houseVo.getPictures())){
			throw new BusinessException("图片参数不能为空!");
		}
		for (String pic : houseVo.getPictures()) {
			HouseImg img = new HouseImg();
			if(null == houseVo.getCreatedTime()){
				houseVo.setCreatedTime(new Date());
			}
			img.setHouseId(houseVo.getHouseId());
			img.setCreatedTime(houseVo.getCreatedTime());
			img.setUpdatedTime(houseVo.getUpdatedTime());
			if(StringUtils.isNotBlank(pic)){
				img.setUrl(pic);
				houseImgMapper.insertSelective(img);
			}
		}
	}
}
