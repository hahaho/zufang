package com.apass.zufang.service.house;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.utils.BaseConstants;
import com.apass.zufang.domain.dto.HouseQueryParams;
import com.apass.zufang.domain.entity.Apartment;
import com.apass.zufang.domain.entity.House;
import com.apass.zufang.domain.entity.HouseImg;
import com.apass.zufang.domain.entity.HouseLocation;
import com.apass.zufang.domain.entity.HousePeizhi;
import com.apass.zufang.domain.enums.IsDeleteEnums;
import com.apass.zufang.domain.enums.RentTypeEnums;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.mapper.zfang.ApartmentMapper;
import com.apass.zufang.mapper.zfang.HouseImgMapper;
import com.apass.zufang.mapper.zfang.HouseLocationMapper;
import com.apass.zufang.mapper.zfang.HouseMapper;
import com.apass.zufang.mapper.zfang.HousePeizhiMapper;
import com.apass.zufang.utils.FileUtilsCommons;
import com.apass.zufang.utils.LngLatUtils;
import com.apass.zufang.utils.ResponsePageBody;
import com.apass.zufang.utils.ToolsUtils;
/**
 * 房源管理
 * @author Administrator
 *
 */
@Service
public class HouseService {
	
	private static final Logger logger = LoggerFactory.getLogger(HouseService.class);
	
	/** * 图片服务器地址*/
    @Value("${nfs.rootPath}")
    private String rootPath;
    /*** 房屋图片存放地址*/

    @Value("${nfs.house}")
    private String nfsHouse;
	
	@Autowired
	private HouseMapper houseMapper;
	
	@Autowired
	private ApartmentMapper apartmentMapper;
	
	@Autowired
	private HouseLocationMapper locationMapper;
	
	@Autowired
	private HousePeizhiMapper peizhiMapper; 
	
	@Autowired
	private HouseImgMapper imgMapper;

	
	@Autowired
	private HouseLocationService locationService;
	
	@Autowired
	private HouseImgService imgService;
	
	@Autowired
	private HousePeiZhiService peizhiService;

	
	public ResponsePageBody<House> getHouseListExceptDelete(HouseQueryParams dto){
		ResponsePageBody<House> body = new ResponsePageBody<>();
		dto.setIsDelete(IsDeleteEnums.IS_DELETE_00.getCode());
		List<House> houseList = houseMapper.getHouseList(dto);
		body.setRows(houseList);
		body.setTotal(houseMapper.getHouseListCount(dto));
		body.setStatus(BaseConstants.CommonCode.SUCCESS_CODE);
		return body;
	}
	
	/**
	 * 添加房屋信息
	 * @throws BusinessException
	 */
	@Transactional(rollbackFor = { Exception.class,RuntimeException.class})
	public void addHouse(HouseVo houseVo) throws BusinessException{
		
		Apartment part = apartmentMapper.selectByPrimaryKey(houseVo.getApartmentId());
		if(null == part || StringUtils.isBlank(part.getCode())){
			throw new BusinessException("房屋所属公寓的编号不存在!");
		}
		House house = new House();
		BeanUtils.copyProperties(houseVo, house);
		house.setCode(ToolsUtils.getLastStr(part.getCode(), 2).concat(String.valueOf(ToolsUtils.fiveRandom())));
		
		/*** 添加房屋信息入库*/
		Integer record = houseMapper.insertSelective(house);
		
		/*** 添加位置入库*/
		HouseLocation location = new HouseLocation();
		BeanUtils.copyProperties(houseVo, location);
		location.setHouseId(record.longValue());
		getAddressLngLat(houseVo, location);
		locationMapper.insertSelective(location);
		
		/*** 添加图片入库*/
		for (String pic : houseVo.getPictures()) {
			HouseImg img = new HouseImg();
			img.setHouseId(record.longValue());
			img.setCreatedTime(houseVo.getCreatedTime());
			img.setUpdatedTime(houseVo.getUpdatedTime());
			if(StringUtils.isNotBlank(pic)){
				String picture1Url2 = nfsHouse + "_" + System.currentTimeMillis() + ".jpg";
				byte[] picture1Byte = Base64Utils.decodeFromString(pic);
				FileUtilsCommons.uploadByteFilesUtil(rootPath, picture1Url2, picture1Byte);
				img.setUrl(picture1Url2);
				imgMapper.insertSelective(img);
			}
		}
		/*** 添加配置入库*/
		for (String config : houseVo.getConfigs()) {
			HousePeizhi peizhi = new HousePeizhi();
			peizhi.setHouseId(record.longValue());
			peizhi.setCreatedTime(houseVo.getCreatedTime());
			peizhi.setUpdatedTime(houseVo.getUpdatedTime());
			if(StringUtils.isNotBlank(config)){
				peizhi.setName(config);
				peizhiMapper.insertSelective(peizhi);
			}
		}
	}
	
	/**
	 * 修改房屋信息
	 * @param house
	 * @throws BusinessException 
	 */
	@Transactional(rollbackFor = { Exception.class,RuntimeException.class})
	public void editHouse(HouseVo houseVo) throws BusinessException{
		if(null == houseVo.getId()){
			throw new BusinessException("房屋Id不能为空!");
		}
		Apartment part = apartmentMapper.selectByPrimaryKey(houseVo.getApartmentId());
		if(null == part || StringUtils.isBlank(part.getCode())){
			throw new BusinessException("房屋所属公寓的编号不存在!");
		}
		House house = houseMapper.selectByPrimaryKey(houseVo.getId());
		BeanUtils.copyProperties(houseVo, house);
		house.setCode(ToolsUtils.getLastStr(part.getCode(), 2).concat(String.valueOf(ToolsUtils.fiveRandom())));
		
		/*** 添加房屋信息入库*/
		houseMapper.updateByPrimaryKeySelective(house);
		
		
		locationService.deleteLocationByHouseId(house.getId());//删除位置信息
		/*** 添加位置入库*/
		HouseLocation location = new HouseLocation();
		BeanUtils.copyProperties(houseVo, location);
		location.setHouseId(house.getId());
		getAddressLngLat(houseVo, location);
		locationMapper.insertSelective(location);
		
		
		imgService.deleteImgByHouseId(house.getId());//删除图片记录
		/*** 添加图片入库*/
		for (String pic : houseVo.getPictures()) {
			HouseImg img = new HouseImg();
			img.setHouseId(house.getId());
			img.setCreatedTime(houseVo.getCreatedTime());
			img.setUpdatedTime(houseVo.getUpdatedTime());
			if(StringUtils.isNotBlank(pic)){
				String picture1Url2 = nfsHouse + "_" + System.currentTimeMillis() + ".jpg";
				byte[] picture1Byte = Base64Utils.decodeFromString(pic);
				FileUtilsCommons.uploadByteFilesUtil(rootPath, picture1Url2, picture1Byte);
				img.setUrl(picture1Url2);
				imgMapper.insertSelective(img);
			}
		}
		
		peizhiService.deletePeiZhiByHouseId(house.getId());//删除配置记录
		/*** 添加配置入库*/
		for (String config : houseVo.getConfigs()) {
			HousePeizhi peizhi = new HousePeizhi();
			peizhi.setHouseId(house.getId());
			peizhi.setCreatedTime(houseVo.getCreatedTime());
			peizhi.setUpdatedTime(houseVo.getUpdatedTime());
			if(StringUtils.isNotBlank(config)){
				peizhi.setName(config);
				peizhiMapper.insertSelective(peizhi);
			}
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
		Map<String,Double> lnglat = LngLatUtils.getLngAndLat(buffer.toString());
		location.setLongitude(lnglat.get("lng"));
		location.setLatitude(lnglat.get("lat"));
	}
	
	/**
	 * 删除房屋信息
	 * @param id 房屋Id
	 * @throws BusinessException 
	 */
	@Transactional(rollbackFor = { Exception.class,RuntimeException.class})
	public void deleteHouse(String id,String updateUser) throws BusinessException{
		if(StringUtils.isBlank(id)){
			throw new BusinessException("房屋Id不能为空!");
		}
		House house = houseMapper.selectByPrimaryKey(Long.parseLong(id));
		/**如果查询房屋信息为空*/
		if(null == house){
			throw new BusinessException("房屋信息不存在！");
		}
		/***房屋状态为上架或者删除时，不允许删除*/
		if(house.getStatus().intValue() == RentTypeEnums.ZT_SHAGNJIA_2.getCode()
				||house.getStatus().intValue() == RentTypeEnums.ZT_SHANGCHU_4.getCode()){
			throw new BusinessException("房屋状态不允许删除!");
		}
		house.setStatus(RentTypeEnums.ZT_SHANGCHU_4.getCode().byteValue());
		house.setUpdatedTime(new Date());
		house.setUpdatedUser(updateUser);
		houseMapper.updateByPrimaryKeySelective(house);
	}
	
	@Transactional(rollbackFor = { Exception.class,RuntimeException.class})
	public void upOrDownHouse(String id,String updateUser) throws BusinessException{
		if(StringUtils.isBlank(id)){
			throw new BusinessException("房屋Id不能为空!");
		}
		House house = houseMapper.selectByPrimaryKey(Long.parseLong(id));
		/**如果查询房屋信息为空*/
		if(null == house){
			throw new BusinessException("房屋信息不存在！");
		}
		/***房屋状态为上架或者删除时，不允许删除*/
		if(house.getStatus().intValue() == RentTypeEnums.ZT_SHANGCHU_4.getCode()){
			throw new BusinessException("房屋状态不允许进行上下架操作!");
		}
		
		if(house.getStatus().intValue() == RentTypeEnums.ZT_SHAGNJIA_2.getCode()){
			house.setStatus(RentTypeEnums.ZT_XIAJIA_3.getCode().byteValue());
		}else{
			house.setStatus(RentTypeEnums.ZT_SHAGNJIA_2.getCode().byteValue());
		}
		house.setUpdatedTime(new Date());
		house.setUpdatedUser(updateUser);
		houseMapper.updateByPrimaryKeySelective(house);
	}
	
	public House readEntity(Long id){
		return houseMapper.selectByPrimaryKey(id);
	}
	@Transactional(rollbackFor = { Exception.class})
	public Integer updateEntity(House entity){
		return houseMapper.updateByPrimaryKeySelective(entity);
	}
}