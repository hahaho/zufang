package com.apass.zufang.service.house;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.zufang.domain.entity.HousePeizhi;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.mapper.zfang.HousePeizhiMapper;
@Service
public class HousePeiZhiService {
	@Autowired
	private HousePeizhiMapper peizhiMapper;
	/**
	 * deletePeiZhiByHouseId
	 * @param houseId
	 */
	@Transactional(rollbackFor = { Exception.class,RuntimeException.class})
	public void deletePeiZhiByHouseId(Long houseId){
		peizhiMapper.deletePeiZhiByHouseId(houseId);
	}
	
	@Transactional(rollbackFor = { Exception.class,RuntimeException.class})
	public void insertPeiZhi(HouseVo houseVo) throws BusinessException{
		if(null == houseVo || CollectionUtils.isEmpty(houseVo.getConfigs())){
			throw new BusinessException("配置参数不能为空!");
		}
		for (String config : houseVo.getConfigs()) {
			HousePeizhi peizhi = new HousePeizhi();
			peizhi.setHouseId(houseVo.getHouseId());
			peizhi.setCreatedTime(houseVo.getCreatedTime());
			peizhi.setUpdatedTime(houseVo.getUpdatedTime());
			if(StringUtils.isNotBlank(config)){
				peizhi.setName(config);
				peizhiMapper.insertSelective(peizhi);
			}
		}
	}
	
	/**
	 * getHousePeizhiList
	 * @param houseId
	 * @return
	 */
	public List<HousePeizhi> getHousePeizhiList(Long houseId){
		return peizhiMapper.getPeiZhiByHouseId(houseId);
	}
}