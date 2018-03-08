package com.apass.zufang.service.house;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.apass.zufang.domain.vo.ApartmentVo;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.mapper.zfang.ApartmentMapper;
import com.apass.zufang.mapper.zfang.HouseMapper;
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
    	List<ApartmentVo> initImg = apartmentMapper.getApartmentList();
    	PageBean<ApartmentVo> pageBean = new PageBean<>(1, 10, initImg);
    	initImg = pageBean.getList();
    	List<String> initCity = new ArrayList<>();
    	for (ApartmentVo img : initImg) {
    		initCity.add(imageUri + "/static" +img.getCompanyLogo());
		}
    	for (int i = 0; i < initImg.size(); i++) {
    	}
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
		String pageNum = (String) paramMap.get("pageNum");
		ValidateUtils.isNotBlank("查询公寓请求参数丢失数据！", city, pageNum);
		Apartment apartment = new Apartment();
		apartment.setCity(city);
		ArrayList<ApartHouseList> apartHouseList = new ArrayList<ApartHouseList>();
		List<Apartment> resultApartment = apartmentMapper.getApartByCity(apartment);
		if (ValidateUtils.listIsTrue(resultApartment)) {
		if (resultApartment.size() > 4) {
			PageBean<Apartment> pageBean = new PageBean<Apartment>(new Integer(pageNum)+1, 4, resultApartment);
			resultApartment = pageBean.getList();
		}
		LOG.info("查询公寓房源信息_获取公寓成功！");
		for (int i = 0; i < resultApartment.size(); i++) {
			List<HouseVo> houseListById = houseMapper.getHouseById(Arrays.asList(resultApartment.get(i).getId()));
			if (ValidateUtils.listIsTrue(houseListById)) {
			if (houseListById.size() > 4) {
				PageBean<HouseVo> pageBean1 = new PageBean<HouseVo>(new Integer(pageNum)+1, 5, houseListById);
				houseListById = pageBean1.getList();
				}
			for (HouseVo houseVo : houseListById) {
				houseVo.setPictures(houseImgService.getImgList(resultApartment.get(i).getId(), (byte) 0));
			}
			}
			ApartHouseList eachAPH = new ApartHouseList();
			eachAPH.setId(resultApartment.get(i).getId());
			eachAPH.setName(resultApartment.get(i).getName());
			eachAPH.setArea(resultApartment.get(i).getArea());
//			List<String> imgList = houseImgService.getImgList(resultApartment.get(i).getId(), (byte) 1);
			eachAPH.setRows(houseListById);
			apartHouseList.add(eachAPH);
			}
		resultMap.put("apartHouses",apartHouseList);
		}
		return resultMap;
	}
	
	public List<HouseVo> getHouseById(String houseId, String pageNum, String pageSize) {
		List<HouseVo> apartList = houseMapper.getHouseById(Arrays.asList(houseId));
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
		return apartmentMapper.getApartmentBylistCity(apartment);
	}

	public List<Apartment> listAllApartment() {
		return null;
	}
}
