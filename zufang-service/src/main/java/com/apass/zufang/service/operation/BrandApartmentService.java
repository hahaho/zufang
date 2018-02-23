package com.apass.zufang.service.operation;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.utils.BaseConstants;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.dto.HouseQueryParams;
import com.apass.zufang.domain.entity.House;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.mapper.zfang.HouseMapper;
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
	/**
	 * 品牌公寓热门房源列表查询
	 * @param entity
	 * @return
	 */
	public ResponsePageBody<HouseVo> getHotHouseList(HouseQueryParams entity) {
		ResponsePageBody<HouseVo> pageBody = new ResponsePageBody<HouseVo>();
        List<HouseVo> list = houseMapper.getHotHouseList(entity);
        pageBody.setTotal(list.size());
        pageBody.setRows(list);
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
	@Transactional(rollbackFor = { Exception.class})
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
				houseD = houseService.readEntity(vo.getId());
				houseD.setSortNo(sorNo);
				houseD.setUpdatedUser(user);
				houseD.setUpdatedTime(new Date());
			}
			if(vo.getSortNo()==sorNo){
				houseU = houseService.readEntity(vo.getId());
				houseU.setSortNo(sorNo);
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
				houseD = houseService.readEntity(vo.getId());
				houseD.setSortNo(sorNo+1);
				houseD.setUpdatedUser(user);
				houseD.setUpdatedTime(new Date());
			}
			if(vo.getSortNo()==sorNo+1){
				houseU = houseService.readEntity(vo.getId());
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
        	house = houseService.readEntity(en.getId());
        	house.setSortNo(++sort);
        	house.setUpdatedTime(new Date());
        	house.setUpdatedUser(user);
        	if(houseService.updateEntity(house)!=1){
        		throw new BusinessException("热门房源取消设置失败！");
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
	public Response hotHouseSet(String houseId, String sortNo, String user) throws BusinessException {
		Integer sort = Integer.parseInt(sortNo);
        Integer sort2 = sort;
        Long id = Long.parseLong(houseId);
        House house = houseService.readEntity(id);
        house.setSortNo(sort);
        house.setUpdatedTime(new Date());
		house.setUpdatedUser(user);
		if(houseService.updateEntity(house)!=1){
			throw new BusinessException("热门房源设置失败！");
		}
		HouseQueryParams entity = new HouseQueryParams();
		entity.setIsDelete("00");
		entity.setHouseType((byte)2);
		List<HouseVo> list = houseMapper.getHotHouseList(entity);
        for(HouseVo en : list){
            if(en.getSortNo()<sort||en.getId().equals(id)){
                continue;
            }
            house = houseService.readEntity(en.getId());
            house.setSortNo(++sort2);
            house.setUpdatedTime(new Date());
            house.setUpdatedUser(user);
            if(houseService.updateEntity(house)!=1){
            	throw new BusinessException("热门房源设置失败！");
            }
        }
        return Response.success("热门房源设置成功！");
    }
}