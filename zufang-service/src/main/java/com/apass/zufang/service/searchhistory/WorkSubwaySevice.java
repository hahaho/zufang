package com.apass.zufang.service.searchhistory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apass.zufang.domain.common.WorkCityJd;
import com.apass.zufang.domain.entity.WorkSubway;
import com.apass.zufang.mapper.zfang.WorkCityJdMapper;
import com.apass.zufang.mapper.zfang.WorkSubwayMapper;

/**
 * 地铁
 * 
 * @author zwd
 *
 */
@Service
public class WorkSubwaySevice {
	
	@Autowired
	private WorkSubwayMapper dao;
	
	@Autowired
	private WorkCityJdMapper cityJddao;
	
	/**
	 * 获取当前位置下所有的子节点 区域
	 * @param code
	 * @return
	 */ 
	public List<WorkCityJd> queryCityJdParentCodeList(String code) {
		return cityJddao.selectDateByParentId(code);
	}

	/**
	 * 获取当前位置下所有的子节点 地铁
	 * @param code
	 * @return
	 */ 
	public List<WorkSubway> querySubwayParentCodeList(WorkSubway domin) {
		return dao.querySubwayParentCodeList(domin);
	}
}
