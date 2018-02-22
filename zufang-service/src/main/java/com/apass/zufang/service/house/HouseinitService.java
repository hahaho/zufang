package com.apass.zufang.service.house;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apass.zufang.domain.entity.Apartment;
import com.apass.zufang.mapper.zfang.ApartmentMapper;

@Component
public class HouseinitService {
	
    @Autowired
    private ApartmentMapper apartmentMapper;

	/**
	 * 品牌公寓接口:默认按创建降序排列20个
	 * @return
	 */
	public List<Apartment> getApartGongyu(Apartment apartment) {
		return apartmentMapper.getApartGongyu(apartment);
	}

}
