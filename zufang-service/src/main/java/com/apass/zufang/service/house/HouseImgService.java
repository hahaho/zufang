
package com.apass.zufang.service.house;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.zufang.domain.entity.HouseImg;
import com.apass.zufang.domain.enums.HomeInitEnum;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.mapper.zfang.HouseImgMapper;
import com.apass.zufang.utils.ValidateUtils;
import com.google.common.collect.Maps;
@Service
public class HouseImgService {
	
	@Autowired
	private HouseImgMapper houseImgMapper;
	
	@Value("${zufang.image.uri}")
	private String imageUri;
	/**
	 * 根据房屋Id，批量删除图片信息
	 * deleteImgByHouseId
	 * @param houseId
	 */
	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public void deleteImgByHouseId(Long houseId){
		houseImgMapper.deleteImgByHouseId(houseId);
	}
	
	/**
	 * 首页初始信息
	 * @return
	 */
	public List<HashMap<String, String>> initImg() {
		
		List<HashMap<String, String>> initCity = new ArrayList<>();
		
		HashMap<String, String> resMap1 = Maps.newHashMap();
		// 方便循环遍历
		resMap1.put("img", HomeInitEnum.INIT_HOUSEIMG_1.getMessage());
		resMap1.put("url", HomeInitEnum.INIT_URL_1.getMessage());
		resMap1.put("title", HomeInitEnum.INIT_TITLE_1.getMessage());
		initCity.add(resMap1);
		
		HashMap<String, String> resMap2 = Maps.newHashMap();
		resMap2.put("img", HomeInitEnum.INIT_HOUSEIMG_2.getMessage());
		resMap2.put("url", HomeInitEnum.INIT_URL_2.getMessage());
		resMap2.put("title", HomeInitEnum.INIT_TITLE_2.getMessage());
		initCity.add(resMap2);
		
		return initCity;
	}
	/**
	 * getHouseImgList
	 * @param houseId
	 * @return
	 */
	public List<HouseImg> getHouseImgList(Long houseId,byte type){
		HouseImg houseImg = new HouseImg();
		houseImg.setType(type);
		houseImg.setHouseId(houseId);
		return houseImgMapper.getImgByHouseId(houseImg);
	}
	
	/**
	 * 获取imgList
	 * @param houseId
	 * @param type
	 * @return
	 */
	public List<String> getImgList(Long houseId, byte type) {
		HouseImg houseImg = new HouseImg();
		houseImg.setType(type);
		houseImg.setHouseId(houseId);
		List<HouseImg> houseImgList = houseImgMapper.getImgByHouseId(houseImg);
		List<String> imgUrlList = new ArrayList<String>();
		if (ValidateUtils.listIsTrue(houseImgList)) {
		for (HouseImg Img : houseImgList) {
			if(Img.getUrl().contains("http")){
				imgUrlList.add(Img.getUrl());
			}else{
				imgUrlList.add(imageUri+Img.getUrl());
			}
		}
		}
		return imgUrlList;
	}
	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public void insertImg(HouseVo houseVo) throws BusinessException{
		if(null == houseVo || CollectionUtils.isEmpty(houseVo.getPictures())){
			throw new BusinessException("图片参数不能为空!");
		}
		for (String pic : houseVo.getPictures()) {
			HouseImg img = new HouseImg();
			if(null == houseVo.getCreatedTime()){
				houseVo.setCreatedTime(new Date());
			}
			img.setHouseId(houseVo.getHouseId());
			img.setCreatedTime(houseVo.getCreatedTime());
			img.setUpdatedTime(houseVo.getUpdatedTime());
			if(StringUtils.isNotBlank(pic)){
				img.setUrl(pic);
				houseImgMapper.insertSelective(img);
			}
		}
	}
}
