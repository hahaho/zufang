package com.apass.zufang.service.house;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.logstash.LOG;
import com.apass.zufang.domain.common.WorkCityJd;
import com.apass.zufang.domain.dto.WorkCityJdParams;
import com.apass.zufang.domain.enums.BusinessHouseTypeEnums;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.mapper.zfang.HouseLocationMapper;
import com.apass.zufang.mapper.zfang.HouseMapper;
import com.apass.zufang.mapper.zfang.WorkCityJdMapper;
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
    private HouseLocationMapper HouseLocationMapper;
    
	@Autowired
	private WorkCityJdMapper cityJddao;

	public HashMap<String, Object> init() {
		
		HashMap<String, Object> resultMap = Maps.newHashMap();
		// 获取市区
		List<HouseVo> cityList = houseMapper.initCity();
		List<String> result=new ArrayList<>();
		for (int i = 0; i < cityList.size(); i++) {
//			KeyValue entity=new KeyValue();
//			entity.setKey(Integer.valueOf(cityList.get(i).getCode()));
			result.add(cityList.get(i).getCity());
		}
		resultMap.put("result", result);
		return resultMap;
	}
	
	public HashMap<String, Object> initHomePage(Map<String, Object> paramMap) throws BusinessException {
		
		// 定义临时变量
		final HashMap<String, Integer> finMap = Maps.newHashMap();
		HashMap<String, Object> resultMap = Maps.newHashMap();
		String city = (String) paramMap.get("city");// 城市
		city = CommonService.cityValidation(city);
		
		// 获取url
		List<String> imgList = houseImgService.initImg();;
		resultMap.put("initImg", imgList);
		LOG.info("init首页接口_获取url成功");
		
		// 获取房源
		HashMap<String, String> map = Maps.newHashMap();
		map.put("type", BusinessHouseTypeEnums.FYLX_2.getCode().toString());
		map.put("city", city);
		List<HouseVo> setHouses = initHouseByCity(map);
		finMap.put("finSize", setHouses.size());
		map.put("type", BusinessHouseTypeEnums.FYLX_1.getCode().toString());
		List<HouseVo> norHouses = initHouseByCity(map);
		
		// 封装热门房源
		List<HouseVo> hotHouse = getHotHouse(setHouses, norHouses);
		resultMap.put("hotHouse", hotHouse);
		LOG.info("init首页接口_获取热门房源成功");
		// 封装配置房源
		List<HouseVo> setHouse = getSetHouse(setHouses, norHouses, paramMap, finMap);
		resultMap.put("setHouse", setHouse);
		LOG.info("init首页接口_获取配置房源成功");
		return resultMap;
	}

	/**
	 * init附近房源
	 * @param city
	 * @return
	 */
	public List<HouseVo> initNearLocation(String city) {
		//List<HouseVo> initNearHouse = HouseLocationMapper.initNearLocation(city);
		//return initNearHouse;
		return null;
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

	private List<HouseVo> getHotHouse(List<HouseVo> setHouses, List<HouseVo> norHouses) throws BusinessException {

		List<HouseVo> hotHouse = null;
		// 配置房源非空
		if (ValidateUtils.listIsTrue(setHouses)) {

			int size = setHouses.size();
			// 配置房源为空
			if (size >= 5) {
				hotHouse = new PageBean<>(1, 5, setHouses).getList();
			} else {
				if (ValidateUtils.listIsTrue(norHouses)) {
					hotHouse = setHouses;
					List<HouseVo> list = new PageBean<>(1, 5 - size, norHouses).getList();
					for (HouseVo houseVo : list) {
						hotHouse.add(houseVo);
					}
				}
				// 配置房源为空
			}
		} else {
			if (ValidateUtils.listIsTrue(norHouses)) {
				hotHouse = new PageBean<>(1, 5, norHouses).getList();
			}
		}
		gitImg(hotHouse);
		return hotHouse;
	}

	private void gitImg(List<HouseVo> hotHouse) {
		if (ValidateUtils.listIsTrue(hotHouse)) {
			// 追加图片
			for (HouseVo houseVo : hotHouse) {
				List<String> imgList = houseImgService.getImgList(houseVo.getHouseId(), (byte) 0);
				houseVo.setPictures(imgList);
			}
		}
	}

	private List<HouseVo> getSetHouse(List<HouseVo> setHouses, List<HouseVo> norHouses, Map<String, Object> paramMap,
			HashMap<String, Integer> finMap) throws BusinessException {

		List<HouseVo> nearHouses = new ArrayList<HouseVo>();
		// 获取用户当前经纬度追加2公里经纬度,
		String pageNum = (String) paramMap.get("pageNum");// 页码
		String longitude = (String) paramMap.get("longitude");// 经度
		String latitude = (String) paramMap.get("latitude");// 纬度
		if (StringUtils.isAnyBlank(longitude, latitude)) {
			// 去除按流量排进热门的数据
			nearHouses = addSetHouse(setHouses, norHouses, finMap);
		} else {
			ValidateUtils.isNotBlank("请求参数丢失数据", longitude, latitude);
			// 去除按流量排进热门的数据
			nearHouses = removeSetHouse(setHouses, norHouses, finMap);
			if (ValidateUtils.listIsTrue(nearHouses)) {
				// if (ValidateUtils.listIsTrue(nearHouses1)) {
				// for (int i = 0; i < nearHouses.size(); i++) {
				// Long houseId = nearHouses.get(i).getHouseId();
				// for (int j = 0; j < nearHouses1.size(); j++) {
				// Long houseIdj = nearHouses1.get(j).getHouseId();
				// if (houseId.equals(houseIdj)) {
				// nearHouses.remove(i);
				// break;
				// }
				// }
				// }
				// }
				// 按照房源距离由近到远排序
				double[] disArray = new double[nearHouses.size()];
				HashMap<Double, HouseVo> disMap = Maps.newHashMap();
				for (int i = 0; i < nearHouses.size(); i++) {
					double disOne = CommonService.distanceSimplify(new Double(longitude), new Double(latitude),
							nearHouses.get(i).getLongitude(), nearHouses.get(i).getLatitude());
					if (disMap.containsKey(disOne)) {
						BigDecimal bigDecimal = new BigDecimal(disOne);
						disOne = bigDecimal.add(new BigDecimal(0.00001)).doubleValue();
					}
					disMap.put(disOne, nearHouses.get(i));
					disArray[i] = disOne;
				}
				Arrays.sort(disArray);

				if (disArray.length > 10) {
					for (int i = 0; i < disArray.length; i++) {
						double disance = disArray[i];
						nearHouses.add(disMap.get(disance));
					}
				}

			}
		}
		if (ValidateUtils.listIsTrue(nearHouses)) {
		if (nearHouses.size() > 10) {
			PageBean<HouseVo> pageBean = new PageBean<HouseVo>(new Integer(pageNum) + 1, 10, nearHouses);
			nearHouses = pageBean.getList();
		}
		gitImg(nearHouses);
		}
		return nearHouses;
	}

	private List<HouseVo> addSetHouse(List<HouseVo> setHouse, List<HouseVo> norHouses, HashMap<String, Integer> finMap) {
		
		try {
			Integer currSize = finMap.get("finSize");
			
			List<HouseVo> addSetList = null;
			// 配置房源大于5
			if (currSize <= 5) {
				// @1:正常房源+配置房源>5
				if (norHouses.size() + currSize > 5) {
					addSetList = norHouses.subList(5 - currSize, norHouses.size());
				}
				// @2:正常房源+配置房源<5
			}else {
				
			}
			return addSetList;
		} catch (Exception e) {
			return null;
		}
}
	private List<HouseVo> removeSetHouse(List<HouseVo> setHouse, List<HouseVo> norHouses, HashMap<String, Integer> finMap) {
		
		List<HouseVo> removeList = null;
		try {
			Integer currSize = finMap.get("finSize");

			// 配置房源大于5
			if (currSize < 5) {
				// @1:正常房源+配置房源>5
				if (norHouses.size() + currSize > 5) {
					removeList = norHouses.subList(0, 5 - currSize);
				}
				// @2:正常房源+配置房源<5
			}

			return removeList;
		} catch (Exception e) {
			return null;
		}
	}
	public HashMap<String, Object> queryCityCode(String city) {
		
		HashMap<String, Object> resultMap = Maps.newHashMap();
		// 获取市区
		WorkCityJdParams entity=new WorkCityJdParams();
		entity.setCity(city);
		WorkCityJd result = cityJddao.selectCityByName(entity);
		if(null==result){
			city=city.replace("市","");
			WorkCityJdParams entityProvince=new WorkCityJdParams();
			entityProvince.setProvince(city);
			result = cityJddao.selectCityByName(entityProvince);
		}
		resultMap.put("code", result.getCode());
		return resultMap;
	}
}
