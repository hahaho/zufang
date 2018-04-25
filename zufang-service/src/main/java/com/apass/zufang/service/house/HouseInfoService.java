package com.apass.zufang.service.house;

import com.apass.gfb.framework.logstash.LOG;
import com.apass.zufang.domain.constants.ConstantsUtil;
import com.apass.zufang.domain.entity.House;
import com.apass.zufang.domain.entity.HouseInfoRela;
import com.apass.zufang.domain.entity.HousePeizhi;
import com.apass.zufang.domain.enums.BusinessHouseTypeEnums;
import com.apass.zufang.domain.enums.FeaturesConfigurationEnums;
import com.apass.zufang.domain.vo.HouseAppSearchVo;
import com.apass.zufang.mapper.zfang.HouseInfoRelaMapper;
import com.apass.zufang.mapper.zfang.HouseMapper;
import com.apass.zufang.mapper.zfang.HousePeizhiMapper;
import com.apass.zufang.service.commons.CommonService;
import com.apass.zufang.utils.PageBean;
import com.apass.zufang.utils.ValidateUtils;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HouseInfoService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(HouseInfoService.class);
	@Value("${zufang.image.uri}")
	private String imageUri;
	/**
	 * 默认地球半径
	 */
	private static double EARTH_RADIUS = 6367000.0; // 单位：m
	@Autowired
	private HouseMapper houseMapper;
	@Autowired
	private HousePeizhiMapper peizhiMapper;
	@Autowired
	private HouseInfoRelaMapper houseInfoRelaMapper;
	@Autowired
	private HouseImgService houseImgService;

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
	 * 增添浏览量
	 * 
	 * @param house
	 */
	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public void addPageView(House house) {
		try {
			houseMapper.updateByPrimaryKeySelective(house);
		} catch (Exception e) {
			LOG.error("保存房屋详情浏览量失败!", e);
			return;
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
			// 处理合租朝向
			if(houseInfo.getHezuChaoxiang()!=null){
				BusinessHouseTypeEnums hzchaoxiang = BusinessHouseTypeEnums.valueOfCX(
						Integer.valueOf(houseInfo.getHezuChaoxiang()));
				houseInfo.setHezuChaoxiangStr(hzchaoxiang.getMessage());
			}
			// 处理合租介绍朝向
			if(houseInfo.getHezuResource()!=null){
				BusinessHouseTypeEnums hzresource = BusinessHouseTypeEnums.valueOfCZJS(
						Integer.valueOf(houseInfo.getHezuResource()));
				houseInfo.setHezuResourceStr(hzresource.getMessage());
			}

			// 替换公司Logo
			String companyLogo = houseInfo.getCompanyLogo();
			houseInfo.setCompanyLogo(imageUri + companyLogo);
			// 获取图片
			List<String> imgList = houseImgService.getImgList(houseInfo.getHouseId(), (byte) 0);
			for (String string : imgList) {
				imgUrlList.add(string);
			}
			houseInfo.setImgUrlList(imgUrlList);
			// 房屋的配置
			List<String> houseConfigList = new ArrayList<String>();
			List<HousePeizhi> housePeizhiList = peizhiMapper
					.getPeiZhiByHouseId(houseInfo.getHouseId());
			for (HousePeizhi Peizhi : housePeizhiList) {
				//先过滤枚举中没有的配置
				if(FeaturesConfigurationEnums.isValidate(Peizhi.getName())){
					houseConfigList.add(Peizhi.getName());
				}
			}
			houseInfo.setHouseConfigList(houseConfigList);
		}
	}

	/**
	 * 根据坐标查询附近房源
	 * 
	 * @return
	 */
	public List<HouseInfoRela> getNearHouseByCoordinate(Double latitude,
			Double longitude,String province,String city) {
		try {
			// setp 1 查询房源
			HouseInfoRela queryInfo = new HouseInfoRela();
			 queryInfo.setStatus(BusinessHouseTypeEnums.ZT_2.getCode().byteValue());
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

		List<HouseInfoRela> houseInfos = new ArrayList<HouseInfoRela>();
		if (ValidateUtils.listIsTrue(houseInfoList)) {
			// 按照房源距离由近到远排序
			double[] disArray = new double[houseInfoList.size()];
			HashMap<Double, HouseInfoRela> disMap = Maps.newHashMap();
			for (int i = 0; i < houseInfoList.size(); i++) {
				double disOne = CommonService.distanceSimplify(new Double(longitude), new Double(latitude),
						houseInfoList.get(i).getLongitude(), houseInfoList.get(i).getLatitude());
				for (int j = 0; j < houseInfoList.size(); j++) {
					if (disMap.containsKey(disOne)) {
						BigDecimal bigDecimal = new BigDecimal(disOne);
						disOne = bigDecimal.add(new BigDecimal(0.1)).doubleValue();
					} else {
						break;
					}
				}
				disMap.put(disOne, houseInfoList.get(i));
				disArray[i] = disOne;
			}
			Arrays.sort(disArray);

			for (int i = 0; i < disArray.length; i++) {
				double disance = disArray[i];
				houseInfos.add(disMap.get(disance));
			}
			if (houseInfos.size() > 10) {
				PageBean<HouseInfoRela> pageBean = new PageBean<HouseInfoRela>(1, 10, houseInfos);
				houseInfos = pageBean.getList();
			}
		}
		return houseInfos;
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
			queryCondition.setStatus(BusinessHouseTypeEnums.ZT_2.getCode().byteValue());
			HouseInfoRela houseInfo = houseInfoRelaMapper.getHouseInfoRelaList(
					queryCondition).get(0);
			// setp 2 根据目标房源的所在位置查询所在城市的所有房源
			HouseInfoRela queryInfo = new HouseInfoRela();
			queryInfo.setCityH(houseInfo.getCityH());
			queryInfo.setStatus(BusinessHouseTypeEnums.ZT_2.getCode().byteValue());
			queryInfo.setTargetHouseId(houseId);
			List<HouseInfoRela> houseInfoList = queryHouseInfoRela(queryInfo);
			
			if (!ValidateUtils.listIsTrue(houseInfoList)) {
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

	public List<HouseAppSearchVo> calculateDistanceAndSort2(Double latitude, Double longitude,
														List<HouseAppSearchVo> houseInfoList) {
		int number = ConstantsUtil.THE_NEARBY_HOUSES_NUMBER;
		List<HouseAppSearchVo> voList = new ArrayList<HouseAppSearchVo>();
		//计算目标房源和附近房源的距离，并绑定映射关系
		Map<Double, HouseAppSearchVo> houseDistanceMap = new HashMap<Double, HouseAppSearchVo>();
		double[] resultArray = new double[houseInfoList.size()];
		for (int i=0 ;i< houseInfoList.size();i++) {
			HouseAppSearchVo vo=houseInfoList.get(i);
			double distance = this.distanceSimplify(latitude, longitude,
					vo.getLatitude(), vo.getLongitude());

			for (int j = 0; j < houseInfoList.size(); j++) {
				if (houseDistanceMap.containsKey(distance)) {
					BigDecimal bigDecimal = new BigDecimal(distance);
					distance = bigDecimal.add(new BigDecimal(0.1)).doubleValue();
				} else {
					break;
				}
			}
			houseDistanceMap.put(distance, vo);
			resultArray[i]=distance;
		}
		//对距离按照升序排序
		Arrays.sort(resultArray);

		for (int i = 0; i < resultArray.length; i++) {
			double disance = resultArray[i];
			voList.add(houseDistanceMap.get(disance));
		}
		return voList;
	}
}
