package com.apass.zufang.mapper.zfang;
import java.util.List;
import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.entity.HouseImg;
/**
 * Created by DELL on 2018/2/7.
 */
public interface HouseImgMapper extends GenericMapper<HouseImg,Long>{
	/**
	 * getImgByHouseId
	 * @param houseImg
	 * @return
	 */
	public List<HouseImg> getImgByHouseId(HouseImg houseImg);
	
	/**
	 * queryImgInfo
	 * @param houseImg
	 * @return
	 */
	public List<HouseImg> queryImgInfo(HouseImg houseImg);
	
}
