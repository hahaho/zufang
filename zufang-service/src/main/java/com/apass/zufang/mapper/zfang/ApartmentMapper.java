package com.apass.zufang.mapper.zfang;
import java.util.List;
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
	 * getApartmentListCount
	 * @param entity
	 * @return
	 */
	public Integer getApartmentListCodeCount(ApprintmentQueryParams entity);
	/**
	 * 获取公寓Id
	 * @return
	 */
	public List<Apartment> getApartByCity(Apartment entity);
	
	public List<Apartment> getApartmentBylistCity(Apartment entity);
}