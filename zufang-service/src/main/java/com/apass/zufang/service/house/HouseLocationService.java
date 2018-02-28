package com.apass.zufang.service.house;

import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.zufang.domain.entity.HouseLocation;
import com.apass.zufang.domain.enums.IsDeleteEnums;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.mapper.zfang.HouseLocationMapper;
import com.apass.zufang.utils.ObtainGaodeLocation;

/**
 * 房源地址管理
 * @author Administrator
 *
 */
@Service
public class HouseLocationService {

	@Autowired
	private HouseLocationMapper locationMapper;
	
	@Transactional(rollbackFor = { Exception.class,RuntimeException.class})
	public void deleteLocationByHouseId(Long houseId){
		//根据房屋Id，查询状态为00 的地址信息
		HouseLocation location = locationMapper.getLocationByHouseId(houseId);
		if(StringUtils.equals(location.getIsDelete(), IsDeleteEnums.IS_DELETE_00.getCode())){
			location.setIsDelete(IsDeleteEnums.IS_DELETE_01.getCode());
			location.setUpdatedTime(new Date());
			locationMapper.updateByPrimaryKeySelective(location);
		}
	}
	
	@Transactional(rollbackFor = { Exception.class,RuntimeException.class})
	public void insertOrUpdateLocation(HouseVo houseVo) throws BusinessException{
		if(null == houseVo){
			throw new BusinessException("地址参数不能为空!");
		}
		HouseLocation location = new HouseLocation();
		if(StringUtils.isNotBlank(houseVo.getLocationId())){
			location = locationMapper.selectByPrimaryKey(Long.parseLong(houseVo.getLocationId()));
		}
		BeanUtils.copyProperties(houseVo, location);
		getAddressLngLat(houseVo, location);
		if(null == location.getId()){
			locationMapper.insertSelective(location);
		}else{
			locationMapper.updateByPrimaryKeySelective(location);
		}
	}
	
	/**
	 * 根据指定的位置获取经纬度
	 * @param houseVo
	 * @param location
	 */
	public void getAddressLngLat(HouseVo houseVo,HouseLocation location){
		StringBuffer buffer = new StringBuffer();
		buffer.append(houseVo.getProvince()).append(houseVo.getCity()).append(houseVo.getDistrict());
		buffer.append(houseVo.getStreet()).append(houseVo.getDetailAddr());
		String[] lnglat = ObtainGaodeLocation.getLocation(buffer.toString());
		if(null != lnglat){
			location.setLongitude(Double.parseDouble(lnglat[0]));
			location.setLatitude(Double.parseDouble(lnglat[1]));
		}
	}
}
