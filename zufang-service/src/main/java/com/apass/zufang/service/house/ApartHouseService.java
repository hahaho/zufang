package com.apass.zufang.service.house;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.logstash.LOG;
import com.apass.zufang.domain.dto.ApartHouseList;
import com.apass.zufang.domain.entity.Apartment;
import com.apass.zufang.domain.enums.HomeInitEnum;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.mapper.zfang.ApartmentMapper;
import com.apass.zufang.mapper.zfang.HouseMapper;
import com.apass.zufang.service.commons.CommonService;
import com.apass.zufang.utils.PageBean;
import com.apass.zufang.utils.ValidateUtils;
import com.google.common.collect.Maps;

@Component
public class ApartHouseService {
	
	@Autowired
	private HouseImgService houseImgService;
	@Autowired
	private ApartmentMapper apartmentMapper;
	@Autowired
	private HouseMapper houseMapper;
    @Value("${zufang.image.uri}")
    private String imageUri;
    
    /**
     * initImg
     * @return
     */
    public List<String> initImg() {
    	
    	List<String> initCity = new ArrayList<>();
    	initCity.add(HomeInitEnum.INIT_APARTIMG.getMessage());
    	return initCity;
    }

	/**
	 * 获取公寓Id
	 * @return
	 */
	public HashMap<String, Object> getApartByCity(Map<String, Object> paramMap) throws BusinessException {
		
		// 获取公寓循环图
		HashMap<String, Object> resultMap = Maps.newHashMap();
		List<String> initImg = initImg();
		resultMap.put("initImg", initImg);
		
		// 获取公寓列表以及对应房源
		String city = (String) paramMap.get("city");
		city = CommonService.cityValidation(city);
		String pageNum = (String) paramMap.get("pageNum");
		ValidateUtils.isNotBlank("查询公寓请求参数丢失数据！", city, pageNum);
		Apartment apartment = new Apartment();
		apartment.setCity(city);
		ArrayList<ApartHouseList> apartHouseList = new ArrayList<ApartHouseList>();
		List<Apartment> resultApartment = apartmentMapper.getApartByCity(apartment);
		if (ValidateUtils.listIsTrue(resultApartment)) {
		PageBean<Apartment> pageBean = new PageBean<Apartment>(new Integer(pageNum)+1, 4, resultApartment);
		resultApartment = pageBean.getList();
		if (ValidateUtils.listIsTrue(resultApartment)) {
		LOG.info("查询公寓房源信息_获取公寓成功！");
		for (int i = 0; i < resultApartment.size(); i++) {
			HashMap<String, String> paramMap1 = Maps.newHashMap();
			paramMap1.put("city", city);
			paramMap1.put("apartId", resultApartment.get(i).getId().toString());
			List<HouseVo> houseListById = houseMapper.getHouseById(paramMap1);
			int amountH = houseListById.size();
			if (ValidateUtils.listIsTrue(houseListById)) {
			if (houseListById.size() > 4) {
				PageBean<HouseVo> pageBean1 = new PageBean<HouseVo>(new Integer(pageNum)+1, 5, houseListById);
				houseListById = pageBean1.getList();
				}
			for (HouseVo houseVo : houseListById) {
				houseVo.setPictures(houseImgService.getImgList(houseVo.getHouseId(), (byte) 0));
			}
			}
			ApartHouseList eachAPH = new ApartHouseList();
			eachAPH.setId(resultApartment.get(i).getId());
			eachAPH.setName(resultApartment.get(i).getName());
			eachAPH.setArea(resultApartment.get(i).getArea());
			eachAPH.setCity(resultApartment.get(i).getCity());
			eachAPH.setAmountH(amountH);
//			List<String> imgList = houseImgService.getImgList(resultApartment.get(i).getId(), (byte) 1);
			eachAPH.setRows(houseListById);
			apartHouseList.add(eachAPH);
			}
		}
		}
		resultMap.put("apartHouses",apartHouseList);
		return resultMap;
	}
	
	public List<HouseVo> getHouseById(String apartId, String city, String pageNum, String pageSize) {
		
		HashMap<String, String> paramMap = Maps.newHashMap();
		paramMap.put("apartId", apartId);
		paramMap.put("city", city);
		List<HouseVo> apartList = houseMapper.getHouseById(paramMap);
		if (StringUtils.isBlank(pageSize)) {
			pageSize = "20";
		}
		PageBean<HouseVo> pageBean = new PageBean<HouseVo>(new Integer(pageNum)+1, new Integer(pageSize), apartList);
		apartList = pageBean.getList();
		LOG.info("查询房源List_获取房源信息成功！");
		for (HouseVo houseVo : apartList) {
			List<String> imgList = houseImgService.getImgList(houseVo.getHouseId(), (byte) 0);
			houseVo.setPictures(imgList);
		}
		return apartList;
	}
	
	public List<Apartment> getApartmentBylistCity(Apartment apartment) {
		List<Apartment> result = apartmentMapper.getApartmentBylistCity(apartment);
		if(result.isEmpty()){
			String city=apartment.getCity().replace("市","");
			apartment.setCity(city);
			result=apartmentMapper.getApartmentBylistCity(apartment);
		}
		return result;
	}

	/**
	 * 查询所有未被删除公寓
	 * @param paramMap
	 * @return
     */
	public List<Apartment> listAllValidApartment(Map<String,String> paramMap) {
		return apartmentMapper.listAllValidApartment(paramMap);
	}
}
