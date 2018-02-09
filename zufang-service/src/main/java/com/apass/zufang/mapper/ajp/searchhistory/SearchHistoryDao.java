package com.apass.zufang.mapper.ajp.searchhistory;

import java.util.List;

import com.apass.gfb.framework.annotation.MyBatisRepository;

/**
 * 
 * 
 * @author ls
 * @update 2018-02-09 11:02
 *
 */
@MyBatisRepository
public class SearchHistoryDao {
	/**
	 * 设备号查询
	 * @param deviceId
	 * @return
	 */
	public List<String> queryDeviceIdHistory(String deviceId) {
		return null;
	}
	/**
	 * 用户id查询
	 * @param customerId
	 * @return
	 */
	public List<String> queryCustomerIdHistory(String customerId) {
		return null;
	}
	
	/**
	 * 设备号查询
	 * @param deviceId
	 * @return
	 */
	public Object deleteDeviceIdHistory(String deviceId) {
		return null;
	}
	
	/**
	 * 用户id查询
	 * @param customerId
	 * @return
	 */
	public Object deleteCustomerIdHistory(String customerId) {
		return null;
	}
	
	
}
