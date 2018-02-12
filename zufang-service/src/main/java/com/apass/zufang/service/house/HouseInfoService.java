package com.apass.zufang.service.house;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apass.zufang.domain.entity.HouseInfoRela;
import com.apass.zufang.domain.entity.HouseLocation;
import com.apass.zufang.mapper.zfang.HouseInfoRelaMapper;
import com.apass.zufang.mapper.zfang.HouseMapper;

@Service
public class HouseInfoService {
	/**
	 * 默认地球半径
	 */
	private static double EARTH_RADIUS = 6367000.0; // 单位：m
	
	@Autowired
	private HouseMapper houseMapper;
	
	@Autowired
	private HouseInfoRelaMapper houseInfoRelaMapper;
	
	public List<HouseInfoRela> queryHouseInfoRela(HouseInfoRela  queryCondition) {
		List<HouseInfoRela> result= houseInfoRelaMapper.getHouseInfoRelaList(queryCondition);
		return result;
	}
	
	/**
	 * 获取附近房源
	 * 
	 * @param houseId
	 *            目标房源
	 * @param number
	 *            附近房源数量
	 * @return
	 */

	public List<HouseLocation> getNearbyhouseInfo(long houseId, int number) {
		// setp 1 根据目标房源id查询目标房源所在位置信息 (province，citycode)
		HouseInfoRela queryCondition= new HouseInfoRela();
		queryCondition.setHouseId(houseId);
		HouseInfoRela houseInfo = houseInfoRelaMapper.getHouseInfoRelaList(queryCondition).get(0);
		// setp 2 根据目标房源的所在位置查询所在城市的所有房源
		HouseInfoRela queryInfo= new HouseInfoRela();
		queryInfo.setProvince(houseInfo.getProvince());
		queryInfo.setCity(houseInfo.getCity());
		List<HouseInfoRela> houseInfoList = houseInfoRelaMapper.getHouseInfoRelaList(queryInfo);
		
		// setp 3 计算目标房源和附近房源的距离，并绑定映射关系
		Map<Double, Long> houseDistanceMap = new HashMap<Double, Long>();
		double[] resultArray = new double[houseInfoList.size()];
		for (HouseInfoRela houseLocation : houseInfoList) {
			double distance = distanceSimplify(houseInfo.getLatitude(),
					houseInfo.getLongitude(), houseLocation.getLatitude(),
					houseLocation.getLongitude());
			houseDistanceMap.put(distance, houseLocation.getHouseId());
			Arrays.fill(resultArray, distance);
		}
		// setp 4 对距离按照升序排序
		Arrays.sort(resultArray);
		// setp 5 取得前number的houseId 的list
		List<Long> houseIdList = new ArrayList<Long>();
		for (int i = 0; i < number; i++) {
			double disance = resultArray[i];
			houseIdList.add(houseDistanceMap.get(disance));
		}
		// setp 6 根据list 查询附近房源的具体信息
		List<HouseLocation> result = new ArrayList<HouseLocation>();
		return result;
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
