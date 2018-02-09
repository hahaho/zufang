package com.apass.zufang.service.house;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
}