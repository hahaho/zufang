package com.apass.zufang.service.house;

import java.util.List;

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
	public List<House> getHouseList(House entity) {
		return houseMapper.getHouseList(entity);
	}
	public Integer getHouseListCount(House entity) {
		return houseMapper.getHouseListCount(entity);
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
		
		/*if(StringUtils.equals(house.getStatus(), cs2)){
			
		}*/
	}
}