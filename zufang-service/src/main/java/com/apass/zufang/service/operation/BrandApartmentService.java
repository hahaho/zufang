package com.apass.zufang.service.operation;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.utils.BaseConstants;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.dto.HouseQueryParams;
import com.apass.zufang.domain.entity.House;
import com.apass.zufang.domain.entity.HouseImg;
import com.apass.zufang.domain.enums.BusinessHouseTypeEnums;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.mapper.zfang.HouseImgMapper;
import com.apass.zufang.mapper.zfang.HouseMapper;
import com.apass.zufang.service.house.HouseImgService;
import com.apass.zufang.service.house.HouseService;
import com.apass.zufang.utils.ResponsePageBody;
/**
 * 品牌公寓热门房源配置
 * @author Administrator
 *
 */
@Service
public class BrandApartmentService {
	@Autowired
	private HouseMapper houseMapper;
	@Autowired
	private HouseService houseService;
	@Autowired
	private HouseImgService houseImgService;
	@Autowired
	private HouseImgMapper houseImgMapper;
	/**
	 * 品牌公寓热门房源列表查询
	 * @param entity
	 * @return
	 */
	public ResponsePageBody<HouseVo> getHotHouseList(HouseQueryParams entity) {
		ResponsePageBody<HouseVo> pageBody = new ResponsePageBody<HouseVo>();
        List<HouseVo> list = houseMapper.getHotHouseList(entity);
        for(HouseVo vo : list){
        	vo.setDescription(vo.getRoom()+"室"+vo.getHall()+"厅"+vo.getWei()+"卫");
        	Byte zujintype = vo.getZujinType();
        	Integer zujinType = Integer.parseInt(zujintype.toString());
        	if(zujinType==BusinessHouseTypeEnums.YJLX_1.getCode()){
        		vo.setZujinTypeStr(BusinessHouseTypeEnums.YJLX_1.getMessage());;
        	}else if(zujinType==BusinessHouseTypeEnums.YJLX_2.getCode()){
        		vo.setZujinTypeStr(BusinessHouseTypeEnums.YJLX_2.getMessage());;
        	}else{
        		vo.setZujinTypeStr(BusinessHouseTypeEnums.YJLX_3.getMessage());;
        	}
        	Integer status = Integer.parseInt(vo.getHouseStatus());
        	if(status==BusinessHouseTypeEnums.ZT_WSHAGNJIA_1.getCode()){
        		vo.setHouseStatus(BusinessHouseTypeEnums.ZT_WSHAGNJIA_1.getMessage());
        	}else if(zujinType==BusinessHouseTypeEnums.ZT_SHAGNJIA_2.getCode()){
        		vo.setHouseStatus(BusinessHouseTypeEnums.ZT_SHAGNJIA_2.getMessage());
        	}else if(zujinType==BusinessHouseTypeEnums.ZT_XIAJIA_3.getCode()){
        		vo.setHouseStatus(BusinessHouseTypeEnums.ZT_XIAJIA_3.getMessage());
        	}else if(zujinType==BusinessHouseTypeEnums.ZT_SHANGCHU_4.getCode()){
        		vo.setHouseStatus(BusinessHouseTypeEnums.ZT_SHANGCHU_4.getMessage());
        	}else{
        		vo.setHouseStatus(BusinessHouseTypeEnums.ZT_XIUGAI_5.getMessage());
        	}
        }
        pageBody.setRows(list);
        entity.setStartRecordIndex(null);
        list = houseMapper.getHotHouseList(entity);
        pageBody.setTotal(list.size());
        pageBody.setStatus(BaseConstants.CommonCode.SUCCESS_CODE);
        return pageBody;
	}
	/**
	 * 品牌公寓热门房源  热门房源上移
	 * @param houseId
	 * @param user
	 * @return
	 * @throws BusinessException 
	 */
	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public Response hotHouseMoveUp(String houseId,String user) throws BusinessException {
		Long id = Long.parseLong(houseId);
		House house = houseService.readEntity(id);
		Integer sorNo = house.getSortNo();
		if(sorNo==1){
			return Response.fail("热门房源上移失败,位于热门首位数据！");
		}
		HouseQueryParams entity = new HouseQueryParams();
		entity.setIsDelete("00");
		entity.setHouseType((byte)2);
		House houseD = null;
		House houseU = null;
		List<HouseVo> list = houseMapper.getHotHouseList(entity);
		for(HouseVo vo : list){
			if(vo.getSortNo()==sorNo-1){
				houseD = houseService.readEntity(vo.getHouseId());
				houseD.setSortNo(sorNo);
				houseD.setUpdatedUser(user);
				houseD.setUpdatedTime(new Date());
			}
			if(vo.getSortNo()==sorNo){
				houseU = houseService.readEntity(vo.getHouseId());
				houseU.setSortNo(sorNo-1);
				houseU.setUpdatedUser(user);
				houseU.setUpdatedTime(new Date());
				break;
			}
		}
		if(houseService.updateEntity(houseD)==1){
			if(houseService.updateEntity(houseU)==1){
				return Response.success("热门房源上移成功！");
			}
		}
		throw new BusinessException("热门房源上移失败！");
	}
	/**
	 * 品牌公寓热门房源  热门房源下移
	 * @param houseId
	 * @param user
	 * @return
	 * @throws BusinessException 
	 */
	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public Response hotHouseMoveDown(String houseId, String user) throws BusinessException {
		Long id = Long.parseLong(houseId);
		House house = houseService.readEntity(id);
		Integer sorNo = house.getSortNo();
		if(sorNo==5){
			return Response.fail("热门房源下移失败,位于热门末位数据！");
		}
		HouseQueryParams entity = new HouseQueryParams();
		entity.setIsDelete("00");
		entity.setHouseType((byte)2);
		House houseD = null;
		House houseU = null;
		List<HouseVo> list = houseMapper.getHotHouseList(entity);
		for(HouseVo vo : list){
			if(vo.getSortNo()==sorNo){
				houseD = houseService.readEntity(vo.getHouseId());
				houseD.setSortNo(sorNo+1);
				houseD.setUpdatedUser(user);
				houseD.setUpdatedTime(new Date());
			}
			if(vo.getSortNo()==sorNo+1){
				houseU = houseService.readEntity(vo.getHouseId());
				houseU.setSortNo(sorNo);
				houseU.setUpdatedUser(user);
				houseU.setUpdatedTime(new Date());
				break;
			}
		}
		if(houseService.updateEntity(houseD)==1){
			if(houseService.updateEntity(houseU)==1){
				return Response.success("热门房源下移成功！");
			}
		}
		throw new BusinessException("热门房源下移失败！");
	}
	/**
	 * 品牌公寓热门房源  热门房源取消设置
	 * @param houseId
	 * @param user
	 * @return
	 * @throws BusinessException 
	 */
	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public Response hotHouseCancel(String houseId, String user) throws BusinessException {
		Long id = Long.parseLong(houseId);
		House house = houseService.readEntity(id);
		house.setSortNo(0);
		house.setUpdatedTime(new Date());
		house.setUpdatedUser(user);
		if(houseService.updateEntity(house)!=1){
			throw new BusinessException("热门房源取消设置失败！");
		}
		HouseQueryParams entity = new HouseQueryParams();
		entity.setIsDelete("00");
		entity.setHouseType((byte)2);
		List<HouseVo> list = houseMapper.getHotHouseList(entity);
		Integer sort = 0;
        for(HouseVo en : list){
        	house = houseService.readEntity(en.getHouseId());
        	house.setSortNo(++sort);
        	house.setUpdatedTime(new Date());
        	house.setUpdatedUser(user);
        	if(houseService.updateEntity(house)!=1){
        		throw new BusinessException("热门房源取消设置失败,更新排序异常！");
        	}
        }
		return Response.success("热门房源取消设置成功！");
	}
	/**
	 * 品牌公寓热门房源  热门房源设置
	 * @param houseId
	 * @param sorNo
	 * @param user
	 * @return
	 * @throws BusinessException 
	 */
	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public Response hotHouseSet(String houseId, String url, String user) throws BusinessException {
		Long id = Long.parseLong(houseId);
        HouseQueryParams entity = new HouseQueryParams();
		entity.setIsDelete("00");
		entity.setHouseType((byte)2);
		List<HouseVo> list = houseMapper.getHotHouseList(entity);
		if(list!=null&&list.size()>4){
			throw new BusinessException("热门房源设置失败,热门房源数量已达上限！");
		}
        House house = houseService.readEntity(id);
        if(list==null||list.size()==0){
        	house.setSortNo(1);
        }else{
        	house.setSortNo(list.size()+1);
        }
        house.setUpdatedTime(new Date());
		house.setUpdatedUser(user);
		if(houseService.updateEntity(house)!=1){
			throw new BusinessException("热门房源设置失败！");
		}
		List<HouseImg> imglist = houseImgService.getHouseImgList(id,(byte)1);
		if(imglist==null||imglist.size()==0){
			HouseImg houseimg = new HouseImg();
			houseimg.setHouseId(id);
			houseimg.setIsDelete("00");
			houseimg.setUrl(url);
			houseimg.setType((byte)1);
			houseimg.setCreatedTime(new Date());
			houseimg.setUpdatedTime(new Date());
			houseImgMapper.insertSelective(houseimg);
		}else{
			HouseImg houseimg = imglist.get(0);
			houseimg.setUrl(url);
			houseimg.setUpdatedTime(new Date());
			houseImgMapper.updateByPrimaryKeySelective(houseimg);
		}
        return Response.success("热门房源设置成功！");
    }
	/**
	 * 品牌公寓热门房源  热门房源编辑
	 * @param houseId
	 * @param sortNo
	 * @param url
	 * @param user
	 * @return
	 * @throws BusinessException 
	 */
	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public Response hotHouseEdit(String houseId, String sortNo, String url, String user) throws BusinessException {
		Long id = Long.parseLong(houseId);
		Integer sort = Integer.parseInt(sortNo);
        Integer sort2 = sort;
		House house = houseService.readEntity(id);
		house.setSortNo(sort);
		house.setUpdatedTime(new Date());
		house.setUpdatedUser(user);
		if(houseService.updateEntity(house)!=1){
			throw new BusinessException("热门房源编辑失败！");
		}else{
			if(!StringUtils.isBlank(url)){
				List<HouseImg> imglist = houseImgService.getHouseImgList(id,(byte)1);
				if(imglist==null||imglist.size()==0){
					HouseImg houseimg = new HouseImg();
					houseimg.setHouseId(id);
					houseimg.setIsDelete("00");
					houseimg.setUrl(url);
					houseimg.setType((byte)1);
					houseimg.setCreatedTime(new Date());
					houseimg.setUpdatedTime(new Date());
					houseImgMapper.insertSelective(houseimg);
				}else{
					HouseImg houseimg = imglist.get(0);
					houseimg.setUrl(url);
					houseimg.setUpdatedTime(new Date());
					houseImgMapper.updateByPrimaryKeySelective(houseimg);
				}
			}
		}
		HouseQueryParams entity = new HouseQueryParams();
		entity.setIsDelete("00");
		entity.setHouseType((byte)2);
		List<HouseVo> list = houseMapper.getHotHouseList(entity);
        for(HouseVo en : list){
            if(en.getSortNo()<sort||en.getHouseId().equals(id)){
                continue;
            }
            house = houseService.readEntity(en.getHouseId());
            house.setSortNo(++sort2);
            house.setUpdatedTime(new Date());
            house.setUpdatedUser(user);
            if(houseService.updateEntity(house)!=1){
            	throw new BusinessException("热门房源编辑失败,更新排序异常！");
            }
        }
        return Response.success("热门房源编辑成功！");
	}
}