package com.apass.zufang.service.house;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.apass.gfb.framework.utils.DateFormatUtil;
import com.apass.zufang.domain.enums.YesNo;
import com.apass.zufang.search.entity.HouseEs;
import com.apass.zufang.search.utils.Pinyin4jUtil;
import com.google.common.collect.Lists;
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
import com.apass.zufang.domain.enums.HouseAuditEnums;
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
import com.apass.zufang.utils.ObtainGaodeLocation;
import com.apass.zufang.utils.ResponsePageBody;
import com.apass.zufang.utils.ToolsUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
/**
 * 房源管理
 * @author Administrator
 *
 */
@Service
public class HouseService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HouseService.class);
	
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

	/*** 房屋信息管理列表 */
	public ResponsePageBody<House> getHouseListExceptDelete(HouseQueryParams dto){
		ResponsePageBody<House> body = new ResponsePageBody<>();
		dto.setIsDelete(IsDeleteEnums.IS_DELETE_00.getCode());
		
		List<Integer> status = Lists.newArrayList();
		status.add(RentTypeEnums.ZT_WSHAGNJIA_1.getCode());
		status.add(RentTypeEnums.ZT_SHAGNJIA_2.getCode());
		status.add(RentTypeEnums.ZT_XIAJIA_3.getCode());
		dto.setStatus(status);
		List<House> houseList = houseMapper.getHouseList(dto);
		body.setRows(houseList);
		body.setTotal(houseMapper.getHouseListCount(dto));
		body.setStatus(BaseConstants.CommonCode.SUCCESS_CODE);
		return body;
	}
	
	/*** 房屋信息审核管理列表*/
	public ResponsePageBody<House> getHouseAuditListExceptDelete(HouseQueryParams dto){
		ResponsePageBody<House> body = new ResponsePageBody<>();
		dto.setIsDelete(IsDeleteEnums.IS_DELETE_00.getCode());
		
		List<Integer> status = Lists.newArrayList();
		status.add(RentTypeEnums.ZT_XIUGAI_5.getCode());
		dto.setStatus(status);
		List<House> houseList = houseMapper.getHouseList(dto);
		body.setRows(houseList);
		body.setTotal(houseMapper.getHouseListCount(dto));
		body.setStatus(BaseConstants.CommonCode.SUCCESS_CODE);
		return body;
	}
	
	/*** 添加房屋信息* @throws BusinessException*/
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
				/**
				 * TODO 验证图片大小 最少2张，最多8张，每张图片不得大于2M，支持.jpg.png，尺寸待定
				 */
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
	 * @param houseVo
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
		
		/** 如果是首次添加，修改，状态不变，如果是下架，修改后，状态变为5*/
		if(house.getStatus().intValue() == RentTypeEnums.ZT_XIAJIA_3.getCode()){
			house.setStatus(RentTypeEnums.ZT_XIUGAI_5.getCode().byteValue());
		}
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
		String[] lnglat = ObtainGaodeLocation.getLocation(buffer.toString());
		if(null != lnglat){
			location.setLongitude(Double.parseDouble(lnglat[0]));
			location.setLatitude(Double.parseDouble(lnglat[1]));
		}
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
		if(house.getStatus().intValue() == RentTypeEnums.ZT_SHANGCHU_4.getCode()){
			throw new BusinessException("房屋状态不允许删除!");
		}
		if(house.getStatus().intValue() == RentTypeEnums.ZT_SHAGNJIA_2.getCode()){
			throw new BusinessException("请将房源下架后重试!");
		}
		house.setStatus(RentTypeEnums.ZT_SHANGCHU_4.getCode().byteValue());
		house.setIsDelete(IsDeleteEnums.IS_DELETE_01.getCode());
		house.setUpdatedTime(new Date());
		house.setUpdatedUser(updateUser);
		houseMapper.updateByPrimaryKeySelective(house);
	}
	
	public Map<String,Object> getHouseDetail(String id) throws BusinessException{
		
		Map<String,Object> values = Maps.newHashMap();
		
		House house = houseMapper.selectByPrimaryKey(Long.parseLong(id));
		
		
		return values;
	}
	
	@Transactional(rollbackFor = { Exception.class,RuntimeException.class})
	public void auditHouse(String id,String status,String updateUser) throws BusinessException{
		if(StringUtils.isBlank(id)){
			throw new BusinessException("房屋Id不能为空!");
		}
		House house = houseMapper.selectByPrimaryKey(Long.parseLong(id));
		/**如果查询房屋信息为空*/
		if(null == house){
			throw new BusinessException("房屋信息不存在！");
		}
		if(house.getStatus().intValue() != RentTypeEnums.ZT_XIUGAI_5.getCode()){
			throw new BusinessException("房屋状态不允许操作!");
		}
		if(StringUtils.equals(HouseAuditEnums.HOUSE_AUDIT_0.getCode(), status)){
			house.setStatus(RentTypeEnums.ZT_SHAGNJIA_2.getCode().byteValue());
		}else{
			house.setStatus(RentTypeEnums.ZT_XIAJIA_3.getCode().byteValue());
		}
		house.setUpdatedTime(new Date());
		house.setUpdatedUser(updateUser);
		houseMapper.updateByPrimaryKeySelective(house);
	}
	
	@Transactional(rollbackFor = { Exception.class,RuntimeException.class})
	public void downHouse(String id,String updateUser) throws BusinessException{
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
			house.setUpdatedTime(new Date());
			house.setUpdatedUser(updateUser);
			houseMapper.updateByPrimaryKeySelective(house);
		}
	}
	
	@Transactional(rollbackFor = { Exception.class,RuntimeException.class})
	public String upHouse(String id,String updateUser) throws BusinessException{
		
		if(StringUtils.isBlank(id)){
			throw new BusinessException("房屋Id不能为空!");
		}
		House house = houseMapper.selectByPrimaryKey(Long.parseLong(id));
		int status = house.getStatus().intValue();
		//如果房屋的状态为待上架，正常上架
		if(house.getStatus().intValue() == RentTypeEnums.ZT_SHANGCHU_4.getCode()){
			return "房屋状态不允许操作！";
		}
		if(house.getStatus().intValue() == RentTypeEnums.ZT_WSHAGNJIA_1.getCode() 
				|| house.getStatus().intValue() == RentTypeEnums.ZT_XIAJIA_3.getCode()){
			if(house.getStatus().intValue() == RentTypeEnums.ZT_WSHAGNJIA_1.getCode()){
				house.setStatus(RentTypeEnums.ZT_XIUGAI_5.getCode().byteValue());
			}else{
				house.setStatus(RentTypeEnums.ZT_SHAGNJIA_2.getCode().byteValue());
			}
			house.setUpdatedTime(new Date());
			house.setUpdatedUser(updateUser);
			houseMapper.updateByPrimaryKeySelective(house);
		}
		return status == 1 ? "房屋上架成功!":"首次录入房源需审核，通过后自动上架，请等待审核结果!";
	}
	
	@Transactional(rollbackFor = { Exception.class,RuntimeException.class})
	public String upHouses(String id,String updateUser) throws BusinessException{
		
		if(StringUtils.isBlank(id)){
			throw new BusinessException("房屋Id不能为空!");
		}
		String[] ids = StringUtils.split(",");
		
		int waitUp = 0;//未修改的房屋信息统计
		int others = 0;//修改后的房屋信息统计
		
		for (String str : ids) {
			House house = houseMapper.selectByPrimaryKey(Long.parseLong(str));
			//如果房屋状态为删除，则跳过
			if(house.getStatus().intValue() == RentTypeEnums.ZT_SHANGCHU_4.getCode()){
				continue;
			}
			//如果房屋的状态为待上架，正常上架
			if(house.getStatus().intValue() == RentTypeEnums.ZT_WSHAGNJIA_1.getCode() 
					|| house.getStatus().intValue() == RentTypeEnums.ZT_XIAJIA_3.getCode()){
				if(house.getStatus().intValue() == RentTypeEnums.ZT_WSHAGNJIA_1.getCode()){
					house.setStatus(RentTypeEnums.ZT_XIUGAI_5.getCode().byteValue());
					others++;
				}else{
					house.setStatus(RentTypeEnums.ZT_SHAGNJIA_2.getCode().byteValue());
					waitUp++;
				}
				house.setUpdatedTime(new Date());
				house.setUpdatedUser(updateUser);
				houseMapper.updateByPrimaryKeySelective(house);
			}
		}
		
		if(others == 0 && waitUp == 0){
			return "不能重复上架！";
		}
		if(waitUp == 0){
			return "部分房源上架成功，剩余房源需审核，通过后自动上架，请等待审核结果!";
		}
		if(others == 0){
			return "房源批量上架成功！";
		}
		return "";
	}
	
	/*** 删除图片信息 * @throws BusinessException */
	@Transactional(rollbackFor = { Exception.class,RuntimeException.class})
	public void delPicture(String id) throws BusinessException{
		
		if(StringUtils.isBlank(id)){
			throw new BusinessException("图片Id不能为空!");
		}
		HouseImg img = imgMapper.selectByPrimaryKey(Long.parseLong(id));
		if(null == img){
			throw new BusinessException("图片信息不存在!");
		}
		/*** 是否要删除原图片 */
		File file = new File(img.getUrl());
		if(file.exists()){
			file.delete();
		}
		img.setIsDelete(IsDeleteEnums.IS_DELETE_01.getCode());
		img.setUpdatedTime(new Date());
		imgMapper.updateByPrimaryKey(img);
	}
	
	
	public House readEntity(Long id){
		return houseMapper.selectByPrimaryKey(id);
	}
	@Transactional(rollbackFor = { Exception.class})
	public Integer updateEntity(House entity){
		return houseMapper.updateByPrimaryKeySelective(entity);
	}

	/**
	 * 查询要上传ES的数据库数据
	 * @param index
	 * @param bach_size
     * @return
     */
	public List<House> selectUpGoods(int index, int bach_size) {
		HouseQueryParams dto = new HouseQueryParams();
		List<Integer> statuList = Lists.newArrayList();
		statuList.add(RentTypeEnums.ZT_SHAGNJIA_2.getCode());
		dto.setStatus(statuList);
		dto.setIsDelete(IsDeleteEnums.IS_DELETE_00.getCode());
		dto.setListTimeStr("yes");
		dto.setPage(index);
		dto.setRows(bach_size);
		dto.setPageParams(bach_size,index);
		List<House> houseList = houseMapper.getHouseList(dto);

		return houseList;
	}

	/**
	 * HouseList转HoueEsList
	 * @param houses
	 * @return
     */
	public List<HouseEs> getHouseEsList(List<House> houses) {
		List<HouseEs> houseEsList = Lists.newArrayList();
		for (House h : houses) {
			HouseEs houseEs = houseInfoToHouseEs(h,null,null,null);
			if (null == houseEs) {
				continue;
			}
			LOGGER.info("houseEsList add houseId {} ...", houseEs.getId());
			houseEsList.add(houseEs);
		}
		LOGGER.info("houseEsList add houseSize {} ...", houseEsList.size());
		return houseEsList;
	}

	/**
	 * House转HouseEs
	 * @param h
	 * @return
     */
	private HouseEs houseInfoToHouseEs(House h,HouseImg hImg, HouseLocation hLocation, HousePeizhi hPeizhi) {
		HouseEs houseEs = new HouseEs();
		try{
			if(h!=null){
				houseEs.setId(Integer.valueOf(h.getId()+""));
				houseEs.setHouseId(h.getId());
				houseEs.setCode(h.getCode());
				houseEs.setApartmentId(h.getApartmentId());
				houseEs.setType(h.getType());
				houseEs.setSortNo(h.getSortNo());
				houseEs.setRentType(h.getRentType());
				houseEs.setCommunityName(h.getCommunityName());
				houseEs.setCommunityNamePinyin(Pinyin4jUtil.converterToSpell(h.getCommunityName()));
				houseEs.setAcreage(h.getAcreage());
				houseEs.setRoom(h.getRoom());
				houseEs.setHall(h.getHall());
				houseEs.setWei(h.getWei());
				houseEs.setFloor(h.getFloor());
				houseEs.setTotalFloor(h.getTotalFloor());
				houseEs.setLiftType(h.getLiftType());
				houseEs.setRentAmt(h.getRentAmt());
				houseEs.setZujinType(h.getZujinType());
				houseEs.setChaoxiang(h.getChaoxiang());
				houseEs.setZhuangxiu(h.getZhuangxiu());
				houseEs.setStatus(h.getStatus());
				houseEs.setListTime(h.getListTime());
				String listTimeStr = DateFormatUtil.dateToString(h.getListTime(),DateFormatUtil.YYYY_MM_DD_HH_MM_SS);
				houseEs.setListTimeStr(listTimeStr);
				houseEs.setDelistTime(h.getDelistTime());
				String delistStr = DateFormatUtil.dateToString(h.getDelistTime(),DateFormatUtil.YYYY_MM_DD_HH_MM_SS);
				houseEs.setDelistTimeStr(delistStr);
				houseEs.setDescription(h.getDescription());
				houseEs.setDescriptionPinyin(Pinyin4jUtil.converterToSpell(h.getDescription()));
				houseEs.setCreatedTime(h.getCreatedTime());
				houseEs.setCreatedTimeStr(DateFormatUtil.dateToString(h.getCreatedTime(),DateFormatUtil.YYYY_MM_DD_HH_MM_SS));
				houseEs.setUpdatedTime(h.getUpdatedTime());
				houseEs.setUpdatedTimeStr(DateFormatUtil.dateToString(h.getUpdatedTime(),DateFormatUtil.YYYY_MM_DD_HH_MM_SS));
				houseEs.setCreatedUser(h.getCreatedUser());
				houseEs.setUpdatedUser(h.getUpdatedUser());
				houseEs.setIsDelete(h.getIsDelete());
				houseEs.setPageView(h.getPageView());
				houseEs.setHousekeeperTel(h.getHousekeeperTel());
			}
			if(hLocation!=null){
				houseEs.setProvince(hLocation.getProvince());
				houseEs.setCity(hLocation.getCity());
				houseEs.setDistrict(hLocation.getDistrict());
				houseEs.setStreet(hLocation.getStreet());
				houseEs.setDetailAddr(hLocation.getDetailAddr());
				houseEs.setDetailAddrPinyin(Pinyin4jUtil.converterToSpell(hLocation.getDetailAddr()));
				houseEs.setLongitude(hLocation.getLongitude());
				houseEs.setLatitude(hLocation.getLatitude());
			}
			if(hImg!=null){
				houseEs.setUrl(hImg.getUrl());
			}
			if(hPeizhi!=null){
				houseEs.setConfigName(hPeizhi.getName());
				houseEs.setConfigNamePinyin(Pinyin4jUtil.converterToSpell(hPeizhi.getName()));
			}

			return  houseEs;
		}catch (Exception e){
			LOGGER.error("--------houseInfoToHouseEs Exception------{}",e);
		}


		return null;
	}
}