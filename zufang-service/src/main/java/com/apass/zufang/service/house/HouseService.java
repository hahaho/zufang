package com.apass.zufang.service.house;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.zufang.domain.entity.House;
import com.apass.zufang.mapper.zfang.HouseMapper;
/**
 * 房源管理
 * @author Administrator
 *
 */
@Service
public class HouseService {
	
	@Autowired
	private HouseMapper houseMapper;
	
	@Transactional(rollbackFor = { Exception.class})
	public Integer createEntity(House entity){
		return houseMapper.insertSelective(entity);
	}
	public House readEntity(House entity){
		return houseMapper.selectByPrimaryKey(entity.getId());
	}
	public House readEntity(Long id){
		return houseMapper.selectByPrimaryKey(id);
	}
	@Transactional(rollbackFor = { Exception.class})
	public Integer updateEntity(House entity){
		return houseMapper.updateByPrimaryKeySelective(entity);
	}
	@Transactional(rollbackFor = { Exception.class})
	public Integer deleteEntity(House entity){
		return houseMapper.deleteByPrimaryKey(entity.getId());
	}
	@Transactional(rollbackFor = { Exception.class})
	public Integer deleteEntity(Long id){
		return houseMapper.deleteByPrimaryKey(id);
	}
	
	/**
	 * 删除房屋信息
	 * @param id 房屋Id
	 * @throws BusinessException 
	 */
	public void deleteHouse(String id) throws BusinessException{
		
		if(StringUtils.isBlank(id)){
			throw new BusinessException("房屋Id不能为空!");
		}
		Long Id = Long.parseLong(id);
		
		House house = houseMapper.selectByPrimaryKey(Id);
		
		if(StringUtils.equals(house.getStatus(), cs2)){
			
		}
	}
}