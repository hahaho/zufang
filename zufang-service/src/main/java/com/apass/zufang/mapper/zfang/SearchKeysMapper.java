package com.apass.zufang.mapper.zfang;

import java.util.List;

import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.entity.SearchKeys;
import org.apache.ibatis.annotations.Param;

/**
 * Created by DELL on 2018/2/7.
 */
public interface SearchKeysMapper extends GenericMapper<SearchKeys,Long>{
	/**
	 * 设备号查询
	 * @param deviceId
	 * @return
	 */
	public List<SearchKeys> queryDeviceIdHistory(String deviceId) ;
	/**
	 * 用户id查询
	 * @param userId
	 * @return
	 */
	public List<SearchKeys> queryUserIdHistory(String userId);
	
	/**
	 * 设备ID删除
	 * @param deviceId
	 * @return
	 */
	public Object deleteDeviceIdHistory(String deviceId) ;
	
	/**
	 * 用户id删除
	 * @param userId
	 * @return
	 */
	public Object deleteUserIdHistory(String userId) ;

	/**
	 * 获取热门搜索
	 * @return
	 */
	public List<SearchKeys> hotSearch(@Param("startDate")String startDate, @Param("endDate")String endDate);

	/**
	 * 获取10条常用搜素
	 * @return
	 */
	public List<SearchKeys> commonSearch(@Param("userId")String userId,@Param("deviceId")String deviceId);

	/**
	 * 删除keys
	 * @return
	 */
	public Integer deleteSearchKey(@Param("userId")String userId,@Param("deviceId")String deviceId);
}
