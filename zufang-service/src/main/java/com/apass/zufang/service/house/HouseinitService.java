package com.apass.zufang.service.house;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.logstash.LOG;
import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.zufang.domain.entity.HouseImg;
import com.apass.zufang.domain.enums.BusinessHouseTypeEnums;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.mapper.zfang.HouseImgMapper;
import com.apass.zufang.mapper.zfang.HouseLocationMapper;
import com.apass.zufang.mapper.zfang.HouseMapper;
import com.apass.zufang.service.commons.CommonService;
import com.apass.zufang.utils.PageBean;
import com.apass.zufang.utils.ValidateUtils;
import com.google.common.collect.Maps;

@Component
public class HouseinitService {
	
	@Autowired
	private HouseImgService houseImgService;
	@Autowired
	private HouseMapper houseMapper;
	@Autowired
	private HouseImgMapper houseImgMapper;
    @Autowired
    private HouseLocationMapper HouseLocationMapper;

    public static void main(String[] args) {
    	List<Integer> test = new ArrayList<Integer>();  
        //init list  
        for (int i = 1; i < 10; i++) {  
            test.add(i);    //auto boxing  
        }  
        PageBean<Integer> pageBean = new PageBean<>(1, 15, test);
        List<Integer> list = pageBean.getList();
        System.out.println("遍历超出数组的数据");
        for (Integer integer : list) {
			System.out.print(integer);
		}
        //display the list  
        System.out.println();
        System.out.println("the orginal list: ");  
        for (int i = 0; i < test.size(); i++) {  
            System.out.print(test.get(i) + " ");  
        } 
        System.out.println();
        System.out.println("test.subList(5, test.size())");
        List<Integer> subList2 = test.subList(5, test.size());
        for (Integer integer : subList2) {
			System.out.print(integer + " ");
		}
        System.out.println();
        System.out.println("集合的size()" + "test.subList(list.size(), list.size())");
        List<Integer> subList = test.subList(test.size(), test.size());
        System.out.println(subList.size());
        for (Integer integer : subList) {
        	System.out.print(integer + " ");
        }
	}
    
	public HashMap<String, Object> init() {
		
		HashMap<String, Object> resultMap = Maps.newHashMap();
		// 获取市区
		List<HouseVo> cityList = houseMapper.initCity();
		List<String> initCity = new ArrayList<>();
		for (int i = 0; i < cityList.size(); i++) {
			initCity.add(cityList.get(i).getCity());
		}
		resultMap.put("initCity", initCity);
		return resultMap;
	}
	
	public HashMap<String, Object> initHomePage(Map<String, Object> paramMap) throws BusinessException {
		
		// 定义临时变量
		final HashMap<String, Integer> finMap = Maps.newHashMap();
		HashMap<String, Object> resultMap = Maps.newHashMap();
		String city = (String) paramMap.get("city");// 城市
		
		// 获取url
		String imgList = getUrl();
		resultMap.put("initImg", imgList);
		LOG.info("init首页接口_获取url成功");
		
		// 获取房源
		HashMap<String, String> map = Maps.newHashMap();
		map.put("type", BusinessHouseTypeEnums.FY_JINGXUAN_2.getCode().toString());
		map.put("city", city);
		List<HouseVo> setHouses = initHouseByCity(map);
		finMap.put("finSize", setHouses.size());
		map.put("type", BusinessHouseTypeEnums.FY_ZHENGCHANG_1.getCode().toString());
		List<HouseVo> norHouses = initHouseByCity(map);
		
		// 封装热门房源
		String hotHouse = getHotHouse(setHouses, norHouses);
		resultMap.put("hotHouse", hotHouse);
		LOG.info("init首页接口_获取热门房源成功");
		// 封装配置房源
		String setHouse = getSetHouse(setHouses, norHouses, paramMap, finMap);
		resultMap.put("setHouse", setHouse);
		LOG.info("init首页接口_获取配置房源成功");
		return resultMap;
	}

	private String getUrl() {
		
		List<HouseImg> imgList = houseImgMapper.initImg();
		PageBean<HouseImg> pageBean = new PageBean<>(1, 10, imgList);
		imgList = pageBean.getList();
		List<String> initImg = new ArrayList<>();
		for (int i = 0; i < imgList.size(); i++) {
			initImg.add(imgList.get(i).getUrl());
		}
		return GsonUtils.toJson(initImg);
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

	/**
	 * 通过城市查询热门房源
	 * @param map
	 * @return
	 */
	public List<HouseVo> initHouseByCity(HashMap<String, String> map) {
		
		 List<HouseVo> initHouse = HouseLocationMapper.initHouseByCity(map);
		return initHouse;
	}

	private String getHotHouse(List<HouseVo> setHouses, List<HouseVo> norHouses) throws BusinessException {
		
		// 从后台配置读取热门房源
		int size = setHouses.size();
		if(ValidateUtils.listIsTrue(setHouses)){
			//不为空的情况
			if (size < 5) {
				if (!ValidateUtils.listIsTrue(norHouses)) {
					int currsize = 5-size;
					if (currsize > norHouses.size()) {
						currsize = norHouses.size();
					}
				for (int i=0; i<currsize; i++) {
					setHouses.add(norHouses.get(i));
				}
			}
			}else {
				setHouses = setHouses.subList(0, 5);
			}
			}else{
			//为空的情况 按照按浏览量（7天）降序排列房源
			if (ValidateUtils.listIsTrue(norHouses)) {
				setHouses = new PageBean<>(1, 5, norHouses).getList();
			}
			//追加图片
			for (HouseVo houseVo : setHouses) {
				List<String> imgList = houseImgService.getImgList(houseVo.getHouseId(), (byte)1);
				houseVo.setPictures(imgList);
			}
			}
		return GsonUtils.toJson(setHouses);
	}
	private String getSetHouse(List<HouseVo> setHouses, List<HouseVo> norHouses, Map<String, Object> paramMap, HashMap<String, Integer> finMap) throws BusinessException {
		
		List<HouseVo> nearHouses = null;
		// 获取用户当前经纬度追加2公里经纬度,
		String longitude = (String) paramMap.get("longitude");// 经度
		String latitude = (String) paramMap.get("latitude");// 纬度
		String pageNum = (String) paramMap.get("pageNum");// 页码
		if (StringUtils.isAnyBlank(longitude, latitude)) {
			// 去除按流量排进热门的数据
			nearHouses = removeHotHouse(setHouses, norHouses, finMap);
		}else{
			ValidateUtils.isNotBlank("请求参数丢失数据", longitude, latitude);
			Map<String, Double> returnLLSquarePoint = CommonService.renturnLngLat(new Double(longitude), new Double(latitude), new Double(2000));
			nearHouses = initNearLocation(returnLLSquarePoint);
			// 去除按流量排进热门的数据
//			norHouses = removeHotHouse(setHouses, norHouses, finMap);
//			for (HouseVo houseVo : norHouses) {
//				nearHouses.add(houseVo);
//			}
			if (ValidateUtils.listIsTrue(nearHouses)) {
				// 使用冒泡排列经纬距离
				for (int i = 1; i < nearHouses.size(); i++) {
					for (int j = 0; j < nearHouses.size()-1; j++) {
						double disOne = CommonService.distanceSimplify(new Double(longitude), new Double(latitude), nearHouses.get(j).getLongitude(), nearHouses.get(j).getLatitude());
						double disTwo = CommonService.distanceSimplify(new Double(longitude), new Double(latitude), nearHouses.get(j+1).getLongitude(), nearHouses.get(j+1).getLatitude());
						if (disOne < disTwo) {
							HouseVo temp = new HouseVo();
							HouseVo temp1 = nearHouses.get(j);
							HouseVo temp2 = nearHouses.get(j+1);
							temp = temp1;
							temp1 = temp2;
							temp2 = temp;
						}
					}
				}
			}
		}
		if (ValidateUtils.listIsTrue(nearHouses) && nearHouses.size() > 10) {
			PageBean<HouseVo> pageBean = new PageBean<HouseVo>(new Integer(pageNum)+1, 10, nearHouses);
			nearHouses = pageBean.getList();
		}
		
		return GsonUtils.toJson(nearHouses);
	}

	private List<HouseVo> removeHotHouse(List<HouseVo> hotHouse, List<HouseVo> norHouses, HashMap<String, Integer> finMap) {
		
		List<HouseVo> removeList = norHouses;
		try {
			Integer currSize = finMap.get("finSize");

			// 配置房源大于5
			if (currSize > 5) {
				List<HouseVo> subList = hotHouse.subList(5, currSize);
				for (HouseVo houseVo : subList) {
					removeList.add(houseVo);
				}
			// 配置房源小于5
			} else {
				// @1:正常房源+配置房源>5
				if (norHouses.size() + currSize > 5) {
					removeList = norHouses.subList(5 - currSize, norHouses.size());
				}
				// @2:正常房源+配置房源<5
			}

			return removeList;
		} catch (Exception e) {
			return null;
		}
	}

}
