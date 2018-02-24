package com.apass.zufang.service.house;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apass.zufang.domain.entity.Apartment;
import com.apass.zufang.domain.entity.HouseLocation;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.mapper.zfang.ApartmentMapper;
import com.apass.zufang.mapper.zfang.HouseLocationMapper;
import com.apass.zufang.mapper.zfang.HouseMapper;

@Component
public class HouseinitService {
	
	@Autowired
	private ApartmentMapper apartmentMapper;
    @Autowired
    private HouseLocationMapper HouseLocationMapper;

	/**
	 * 品牌公寓接口:默认按创建降序排列20个
	 * @return
	 */
	public List<Apartment> getApartGongyu(Apartment apartment) {
		return apartmentMapper.getApartGongyu(apartment);
	}

	/**
	 * init配置热门房源
	 * @param houseLocation 
	 * @return
	 */
	public List<HouseVo> initPeizhiLocation(HouseLocation houseLocation) {
		
		List<HouseVo> initPei = HouseLocationMapper.initPeizhiLocation(houseLocation);
		return initPei;
	}

	/**
	 * init热门房源
	 * @param houseLocation 
	 * @return
	 */
	public List<HouseVo> initHotLocation(HouseLocation houseLocation) {
		List<HouseVo> inithot = HouseLocationMapper.initHotLocation(houseLocation);
		return inithot;
	}

	/**
	 * init附近房源
	 * @param returnLLSquarePoint
	 * @return
	 */
	public List<HouseVo> initNearLocation(Map<String, Double> returnLLSquarePoint) {
		List<HouseVo> initNearHouse = HouseLocationMapper.initNearLocation(returnLLSquarePoint);
		return initNearHouse;
	}

}
