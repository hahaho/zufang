package com.apass.zufang.mapper.zfang;
import java.util.List;
import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.entity.House;
/**
 * Created by DELL on 2018/2/7.
 */
public interface HouseMapper extends GenericMapper<House,Long> {
	/**
	 * 列表集合查询
	 * getHouseList
	 * @param entity
	 * @return
	 */
	public List<House> getHouseList(House entity);
	/**
	 * 数量查询
	 * getHouseListCount
	 * @param entity
	 * @return
	 */
	public Integer getHouseListCount(House entity);
}