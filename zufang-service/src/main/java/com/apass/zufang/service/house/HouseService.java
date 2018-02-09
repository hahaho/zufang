package com.apass.zufang.service.house;
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
}