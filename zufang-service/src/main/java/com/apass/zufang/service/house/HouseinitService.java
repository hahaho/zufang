package com.apass.zufang.service.house;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apass.zufang.domain.entity.HouseImg;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.mapper.zfang.HouseImgMapper;
import com.apass.zufang.mapper.zfang.HouseLocationMapper;
import com.apass.zufang.mapper.zfang.HouseMapper;

@Component
public class HouseinitService {
	
	@Autowired
	private HouseMapper houseMapper;
	@Autowired
	private HouseImgMapper houseImgMapper;
    @Autowired
    private HouseLocationMapper HouseLocationMapper;

	/**
	 * init附近房源
	 * @param returnLLSquarePoint
	 * @return
	 */
	public List<HouseVo> initNearLocation(Map<String, Double> returnLLSquarePoint) {
		List<HouseVo> initNearHouse = HouseLocationMapper.initNearLocation(returnLLSquarePoint);
		return initNearHouse;
	}

	/**
	 * init城市
	 * @return
	 */
	public List<String> initCity() {
		List<HouseVo> cityList = houseMapper.initCity();
		List<String> initCity = new ArrayList<>();
		for (int i = 0; i < cityList.size(); i++) {
			initCity.add(cityList.get(i).getCity());
		}
		return initCity;
	}
	
	/**
	 * initImg
	 * @return
	 */
	public List<String> initImg() {
		List<HouseImg> initImg = houseImgMapper.initImg();
		List<String> initCity = new ArrayList<>();
		for (int i = 0; i < initImg.size(); i++) {
			initCity.add(initImg.get(i).getUrl());
		}
		return initCity;
	}

	public List<HouseVo> initHouseByCity(HashMap<String, String> map) {
		 List<HouseVo> initHouse = HouseLocationMapper.initHouseByCity(map);
		return initHouse;
	}
	
}
