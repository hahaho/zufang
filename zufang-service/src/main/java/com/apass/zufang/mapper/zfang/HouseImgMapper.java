package com.apass.zufang.mapper.zfang;

import java.util.List;

import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.entity.HouseImg;

/**
 * Created by DELL on 2018/2/7.
 */
public interface HouseImgMapper extends GenericMapper<HouseImg,Long>{
	
	List<HouseImg> getImgByHouseId(Long houseId);
}
