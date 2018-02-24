package com.apass.zufang.service.searchhistory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apass.zufang.mapper.zfang.SearchHistoryRepository;

/**
 * 搜索历史
 * 
 * @author ls
 * @update 2018-02-09 11:02
 *
 */
@Component
public class SearchHistorySevice {
	
	@Autowired
	private SearchHistoryRepository searchHistoryDao;
	
	/**
	 * 设备ID查询
	 * @param deviceId
	 * @return
	 */
	public List<String> queryDeviceIdHistory(String deviceId) {
		 return searchHistoryDao.queryDeviceIdHistory(deviceId);
		 
	}
	/**
	 * 用户ID查询
	 * @param customerId
	 * @return
	 */ 
	public List<String> queryCustomerIdHistory(String customerId) {
		return searchHistoryDao.queryCustomerIdHistory(customerId);
		
	}
	
	/**
	 * 设备ID删除
	 * @param customerId
	 * @return
	 */
	public Object deleteDeviceIdHistory(String deviceId) {
		return searchHistoryDao.deleteDeviceIdHistory(deviceId);
	}
	
	/**
	 * 用户ID删除
	 * @param customerId
	 * @return
	 */
	public Object deleteCustomerIdHistory(String customerId) {
		return searchHistoryDao.deleteCustomerIdHistory(customerId);
	}

	
}
