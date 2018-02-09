package com.apass.zufang.service.operation;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apass.gfb.framework.utils.BaseConstants;
import com.apass.zufang.domain.entity.House;
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
	public HouseService houseService;
	public ResponsePageBody<House> getHouseList(House entity) {
		ResponsePageBody<House> pageBody = new ResponsePageBody<House>();
        Integer count = houseService.getHouseListCount(entity);
        List<House> response = houseService.getHouseList(entity);
        pageBody.setTotal(count);
        pageBody.setRows(response);
        pageBody.setStatus(BaseConstants.CommonCode.SUCCESS_CODE);
        return pageBody;
	}
}