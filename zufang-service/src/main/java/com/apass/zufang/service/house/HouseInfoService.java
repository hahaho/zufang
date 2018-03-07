package com.apass.zufang.service.house;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apass.zufang.domain.constants.ConstantsUtil;
import com.apass.zufang.domain.entity.HouseImg;
import com.apass.zufang.domain.entity.HouseInfoRela;
import com.apass.zufang.domain.entity.HousePeizhi;
import com.apass.zufang.domain.enums.BusinessHouseTypeEnums;
import com.apass.zufang.domain.enums.IsDeleteEnums;
import com.apass.zufang.mapper.zfang.HouseImgMapper;
import com.apass.zufang.mapper.zfang.HouseInfoRelaMapper;
import com.apass.zufang.mapper.zfang.HousePeizhiMapper;

@Service
public class HouseInfoService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(HouseInfoService.class);

	/**
	 * 默认地球半径
	 */
	private static double EARTH_RADIUS = 6367000.0; // 单位：m
	@Autowired
	private HouseImgMapper houseImgMapper;
	@Autowired
	private HousePeizhiMapper peizhiMapper;
	@Autowired
	private HouseInfoRelaMapper houseInfoRelaMapper;

	/**
	 * 查询房源信息
	 * 
	 * @param queryCondition
	 * @return
	 */
	public List<HouseInfoRela> queryHouseInfoRela(HouseInfoRela queryCondition) {
		try {
			List<HouseInfoRela> houseInfoList = houseInfoRelaMapper
					.getHouseInfoRelaList(queryCondition);
			if (houseInfoList != null && houseInfoList.size() != 0) {
				this.dealHouseRela(houseInfoList);
			}
			return houseInfoList;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 处理房源特殊数据
	 * 
	 * @param houseInfoList
	 */
	public void dealHouseRela(List<HouseInfoRela> houseInfoList) {
		for (HouseInfoRela houseInfo : houseInfoList) {
			// 房屋的图片
			List<String> imgUrlList = new ArrayList<String>();
			HouseImg orHouseImg = new HouseImg();
			orHouseImg.setHouseId(houseInfo.getHouseId());
			// 处理租赁类型
			if(houseInfo.getRentType()!=null){
			BusinessHouseTypeEnums rentType = BusinessHouseTypeEnums.valueOfHZ(
					Integer.valueOf(houseInfo.getRentType()));
			houseInfo.setRentTypeStr(rentType.getMessage());
			}
			// 处理租金类型
			if(houseInfo.getZujinType()!=null){
			BusinessHouseTypeEnums zujinType = BusinessHouseTypeEnums.valueOfYJLX(
					Integer.valueOf(houseInfo.getZujinType()));
			houseInfo.setZujinTypeStr(zujinType.getMessage());
			}
			// 处理朝向
			if(houseInfo.getChaoxiang()!=null){
				BusinessHouseTypeEnums chaoxiang = BusinessHouseTypeEnums.valueOfCX(
						Integer.valueOf(houseInfo.getChaoxiang()));
				houseInfo.setChaoxiangStr(chaoxiang.getMessage());
			}

			orHouseImg.setIsDelete(IsDeleteEnums.IS_DELETE_00.getCode());
			List<HouseImg> houseImgList = houseImgMapper
					.queryImgInfo(orHouseImg);
			for (HouseImg houseImg : houseImgList) {
				imgUrlList.add(houseImg.getUrl());
			}
			houseInfo.setImgUrlList(imgUrlList);
			// 房屋的配置
			List<String> houseConfigList = new ArrayList<String>();
			List<HousePeizhi> housePeizhiList = peizhiMapper
					.getPeiZhiByHouseId(houseInfo.getHouseId());
			for (HousePeizhi Peizhi : housePeizhiList) {
				houseConfigList.add(Peizhi.getName());
			}
			houseInfo.setHouseConfigList(houseConfigList);
		}
	}

	/**
	 * 根据坐标查询附近房源
	 * 
	 * @param houseId
	 *            目标房源
	 * @param number
	 *            附近房源数量
	 * @return
	 */
	public List<HouseInfoRela> getNearHouseByCoordinate(Double latitude,
			Double longitude,String province,String city) {
		try {
			// setp 1 查询房源
			HouseInfoRela queryInfo = new HouseInfoRela();
			 queryInfo.setProvince(province);
			 queryInfo.setCity(city);
			List<HouseInfoRela> houseInfoList = houseInfoRelaMapper
					.getHouseInfoRelaList(queryInfo);
			if (houseInfoList == null || houseInfoList.size() <= 0) {
				return null;
			}
			// setp 2 根据目标经纬度和房源list 根据距离进行排序并取前number的房源数据
			return calculateDistanceAndSort(latitude, longitude, houseInfoList);
		} catch (Exception e) {
			LOGGER.error("根据坐标查询附近房源getNearbyhouseInfo出错==》", e);
			throw e;
		}
	}
	
    /**
     * 根据目标经纬度和房源list 根据距离进行排序并取前number的房源数据
     * 
     * @param latitude
     * @param longitude
     * @param houseInfoList
     * @return
     */
	public List<HouseInfoRela> calculateDistanceAndSort(Double latitude, Double longitude,
			List<HouseInfoRela> houseInfoList) {
		int number = ConstantsUtil.THE_NEARBY_HOUSES_NUMBER;
		List<HouseInfoRela> searchHouseInfoList = new ArrayList<HouseInfoRela>();
		// setp 3 计算目标房源和附近房源的距离，并绑定映射关系
		Map<Double, Long> houseDistanceMap = new HashMap<Double, Long>();
		double[] resultArray = new double[houseInfoList.size()];
		for (int i=0 ;i< houseInfoList.size();i++) {
			HouseInfoRela houseLocation=houseInfoList.get(i);
			double distance = this.distanceSimplify(latitude, longitude,
			houseLocation.getLatitude(), houseLocation.getLongitude());
			if (houseDistanceMap.get(distance) != null) {
				BigDecimal distanceBig = new BigDecimal(distance);
				// 相同的距离 需要处理 （后一个加上0.0001）
				distanceBig = distanceBig.add(new BigDecimal("0.0001"));
				distance = distanceBig.doubleValue();
			}
			houseDistanceMap.put(distance, houseLocation.getHouseId());
			resultArray[i]=distance;
		}
		// setp 4 对距离按照升序排序
		Arrays.sort(resultArray);
		// setp 5 取得前number的houseId 的list
		List<Long> houseIdList = new ArrayList<Long>();
		int value = resultArray.length > number ? number : resultArray.length;
		for (int i = 0; i < value; i++) {
			double disance = resultArray[i];
			houseIdList.add(houseDistanceMap.get(disance));
		}
		// setp 6 根据list 查询附近房源的具体信息
		for (Long newHouseId : houseIdList) {
			HouseInfoRela finalHouseInfo = houseInfoRelaMapper
					.getHouseInfoByHouseId(newHouseId);
			searchHouseInfoList.add(finalHouseInfo);
		}
		if (searchHouseInfoList != null && searchHouseInfoList.size() != 0) {
			this.dealHouseRela(searchHouseInfoList);
		}
		return searchHouseInfoList;
	}

	/**
	 * 查询指定目标附近房源
	 * 
	 * @param houseId
	 *            目标房源
	 *            附近房源数量
	 * @return
	 */
	public List<HouseInfoRela> getNearbyhouseId(long houseId) {
		try {
			// setp 1 根据目标房源id查询目标房源所在位置信息 (province，citycode)
			HouseInfoRela queryCondition = new HouseInfoRela();
			queryCondition.setHouseId(houseId);
			HouseInfoRela houseInfo = houseInfoRelaMapper.getHouseInfoRelaList(
					queryCondition).get(0);
			// setp 2 根据目标房源的所在位置查询所在城市的所有房源
			HouseInfoRela queryInfo = new HouseInfoRela();
			queryInfo.setProvince(houseInfo.getProvince());
			queryInfo.setCity(houseInfo.getCity());
			queryInfo.setTargetHouseId(houseId);
			List<HouseInfoRela> houseInfoList = houseInfoRelaMapper
					.getHouseInfoRelaList(queryInfo);
			if (houseInfoList == null || houseInfoList.size() <= 0) {
				return null;
			}
			// setp 3 根据目标经纬度和房源list 根据距离进行排序并取前number的房源数据
			return calculateDistanceAndSort(houseInfo.getLatitude(), houseInfo.getLongitude(), houseInfoList);
		} catch (Exception e) {
			LOGGER.error("获取附近房源方法getNearbyhouseInfo出错==》", e);
			throw e;
		}
	}

	/**
	 * 获取两个经纬度点的距离
	 * 
	 * @param goalLat
	 *            目的地纬度
	 * @param goalLng
	 *            目的地经度
	 * @param lat
	 *            附近房源纬度
	 * @param lng
	 *            附近房源经度
	 * @return
	 */
	public double distanceSimplify(double goalLat, double goalLng, double lat,
			double lng) {
		double dx = goalLng - lng; // 经度差值
		double dy = goalLat - lat; // 纬度差值
		double b = (goalLat + lat) / 2.0; // 平均纬度
		double Lx = Math.toRadians(dx) * EARTH_RADIUS
				* Math.cos(Math.toRadians(b)); // 东西距离
		double Ly = EARTH_RADIUS * Math.toRadians(dy); // 南北距离
		return Math.sqrt(Lx * Lx + Ly * Ly); // 用平面的矩形对角距离公式计算总距离
	}
}
