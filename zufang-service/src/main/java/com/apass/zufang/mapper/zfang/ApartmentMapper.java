package com.apass.zufang.mapper.zfang;
import java.util.List;
import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.entity.Apartment;
public interface ApartmentMapper extends GenericMapper<Apartment,Long> {
	/**
	 * getApartmentList
	 * @param entity
	 * @return
	 */
	public List<Apartment> getApartmentList(Apartment entity);
	/**
	 * getApartmentListCount
	 * @param entity
	 * @return
	 */
	public Integer getApartmentListCount(Apartment entity);
}