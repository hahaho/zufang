package com.apass.zufang.service.operation;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apass.gfb.framework.utils.BaseConstants;
import com.apass.zufang.domain.Response;
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
	public ResponsePageBody<HouseVo> getHotHouseList(HouseVo entity) {
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
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class})
	public Response hotHouseMoveUp(String houseId,String user) {
		Long id = Long.parseLong(houseId);
		House house = houseService.readEntity(id);
		Integer sorNo = house.getSortNo();
		if(sorNo==1){
			return Response.success("热门房源上移失败,位于热门首位数据！");
		}
		HouseVo entity = new HouseVo();
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
		return Response.fail("热门房源上移失败！");
	}
}