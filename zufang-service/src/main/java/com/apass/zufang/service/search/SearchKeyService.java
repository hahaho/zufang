package com.apass.zufang.service.search;

import com.apass.zufang.domain.entity.SearchKeys;
import com.apass.zufang.mapper.zfang.SearchKeysMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SearchKeyService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchKeyService.class);
	
	@Autowired
	private SearchKeysMapper keysMapper;

	public void addCommonSearchKeys(String keyValue,String userId,String deviceId){
		LOGGER.info("addCommonSearchKeys is come in,params:",keyValue+"XXXX"+userId+"XXXX"+deviceId);
		SearchKeys record = new SearchKeys();
		record.setKeyValue(keyValue);
		if(StringUtils.isNotBlank(userId)){
			record.setUserId(userId);
		}
		if(StringUtils.isNotBlank(deviceId)){
			record.setDeviceId(deviceId);
		}
		record.setCreatedTime(new Date());
		record.setUpdatedTime(new Date());
		keysMapper.insertSelective(record);
	}
	


	public void deleteSearchKeysByDeviceId(String deviceId){
		keysMapper.deleteSearchKey("",deviceId);
	}
	
	public List<SearchKeys> hotSearch(String startDate1,String startDate2){
		return keysMapper.hotSearch(startDate1,startDate2);
	}
	
	public List<SearchKeys> commonSearchByUserId(String userId){
		return keysMapper.commonSearch(userId,"");
	}
	
	public List<SearchKeys> commonSearchByDeviceId(String deviceId){
		return keysMapper.commonSearch("",deviceId);
	}
}
