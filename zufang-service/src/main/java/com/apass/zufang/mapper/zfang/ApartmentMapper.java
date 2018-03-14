package com.apass.zufang.mapper.zfang;
import java.util.List;
import java.util.Map;

import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.dto.ApprintmentQueryParams;
import com.apass.zufang.domain.entity.Apartment;
import com.apass.zufang.domain.vo.ApartmentVo;
public interface ApartmentMapper extends GenericMapper<Apartment,Long> {
	/**
	 * getApartmentList
	 * @param entity
	 * @return
	 */
	public List<ApartmentVo> getApartmentList(ApprintmentQueryParams entity);
	/**
	 * getApartmentListCount
	 * @param entity
	 * @return
	 */
	public Integer getApartmentListCount(ApprintmentQueryParams entity);
	/**
	 * getApartmentListNameCount
	 * @param entity
	 * @return
	 */
	public List<ApartmentVo> getApartmentListNameCount(ApprintmentQueryParams entity);
	/**
	 * getApartmentListCount
	 * @param entity
	 * @return
	 */
	public Integer getApartmentListCodeCount(ApprintmentQueryParams entity);
	/**
	 * 公寓初始图片
	 * @return
	 */
	public List<ApartmentVo> getApartmentList();
	/**
	 * 获取公寓Id
	 * @return
	 */
	public List<Apartment> getApartByCity(Apartment entity);
	/**
	 * 获取公寓下房源
	 * @param entity
	 * @return
	 */
	public List<Apartment> getApartmentBylistCity(Apartment entity);

	List<Apartment> listAllValidApartment(Map<String, String> paramMap);
}