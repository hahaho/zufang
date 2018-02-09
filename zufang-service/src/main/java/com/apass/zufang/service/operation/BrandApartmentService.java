package com.apass.zufang.service.operation;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apass.gfb.framework.utils.BaseConstants;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.mapper.zfang.HouseMapper;
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
	public ResponsePageBody<HouseVo> getHouseList(HouseVo entity) {
		ResponsePageBody<HouseVo> pageBody = new ResponsePageBody<HouseVo>();
        List<HouseVo> list = houseMapper.getHotHouseList(entity);
        pageBody.setTotal(list.size());
        pageBody.setRows(list);
        pageBody.setStatus(BaseConstants.CommonCode.SUCCESS_CODE);
        return pageBody;
	}
}