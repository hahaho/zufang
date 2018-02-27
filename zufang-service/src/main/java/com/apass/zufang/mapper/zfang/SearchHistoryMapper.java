package com.apass.zufang.mapper.zfang;

import java.util.List;

import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.entity.SearchKeys;

/**
 * 
 * 搜索历史
 * @author ls
 * @update 2018-02-09 11:02
 *
 */
public interface SearchHistoryMapper extends GenericMapper<SearchKeys,Long>  {
	/**
	 * 设备号查询
	 * @param deviceId
	 * @return
	 */
	public List<SearchKeys> queryDeviceIdHistory(String deviceId) ;
	/**
	 * 用户id查询
	 * @param customerId
	 * @return
	 */
	public List<SearchKeys> queryCustomerIdHistory(String userId);
	
	/**
	 * 设备ID删除
	 * @param deviceId
	 * @return
	 */
	public Integer deleteDeviceIdHistory(String deviceId) ;
	
	/**
	 * 用户id删除
	 * @param customerId
	 * @return
	 */
	public Integer deleteCustomerIdHistory(String userId) ;
	
	
}
