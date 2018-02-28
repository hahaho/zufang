package com.apass.zufang.service.searchhistory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.zufang.domain.entity.SearchKeys;
import com.apass.zufang.mapper.zfang.SearchHistoryMapper;

/**
 * 搜索历史
 * 
 * @author ls
 * @update 2018-02-09 11:02
 *
 */
@Service
public class SearchHistorySevice {
	
	@Autowired
	private SearchHistoryMapper searchHistoryDao;
	
	/**
	 * 设备ID查询
	 * @param deviceId
	 * @return
	 */
	public List<SearchKeys> queryDeviceIdHistory(String deviceId)throws BusinessException {
		 return searchHistoryDao.queryDeviceIdHistory(deviceId);
		 
	}
	/**
	 * 用户ID查询
	 * @param customerId
	 * @return
	 */ 
	public List<SearchKeys> queryCustomerIdHistory(String userId)throws BusinessException {
		return searchHistoryDao.queryCustomerIdHistory(userId);
		
	}
	
	/**
	 * 设备ID删除
	 * @param customerId
	 * @return
	 */
	public Object deleteDeviceIdHistory(String deviceId) throws BusinessException{
		return searchHistoryDao.deleteDeviceIdHistory(deviceId);
	}
	
	/**
	 * 用户ID删除
	 * @param customerId
	 * @return
	 */
	public Object deleteCustomerIdHistory(String userId) throws BusinessException{
		return searchHistoryDao.deleteCustomerIdHistory(userId);
	}

	
}
