package com.apass.zufang.mapper.zfang;
import java.util.ArrayList;
import java.util.List;

import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.dto.HouseAppointmentQueryParams;
import com.apass.zufang.domain.dto.HouseQueryParams;
import com.apass.zufang.domain.entity.Apartment;
import com.apass.zufang.domain.entity.House;
import com.apass.zufang.domain.vo.HouseAppointmentVo;
import com.apass.zufang.domain.vo.HouseVo;
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
	public List<House> getHouseList(HouseQueryParams entity);
	/**
	 * 数量查询
	 * getHouseListCount
	 * @param entity
	 * @return
	 */
	public Integer getHouseListCount(HouseQueryParams entity);
	/**
	 * 品牌公寓热门房源列表查询
	 * @param entity
	 * @return
	 */
	public List<HouseVo> getHotHouseList(HouseQueryParams entity);
	/**
	 * init城市
	 * @return 
	 */
	public List<HouseVo> initCity();
	/**
	 * 查询房源List
	 * @param list
	 * @return
	 */
	public List<Apartment> getHouseByID(ArrayList<String> list);
	/**
	 * 电话预约管理 房源列表查询
	 * @param entity
	 * @return
	 */
	public List<HouseAppointmentVo> getHouseListForPhoneAppointment(HouseAppointmentQueryParams entity);
}