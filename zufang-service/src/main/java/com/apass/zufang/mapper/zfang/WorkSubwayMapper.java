package com.apass.zufang.mapper.zfang;

import java.util.List;

import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.entity.WorkSubway;

/**
 * Created by DELL on 2018/2/27.
 */
public interface WorkSubwayMapper extends GenericMapper<WorkSubway,Long> {
	
	/**
	 * 根据parent查询子集
	 * @return
     */
	List<WorkSubway> querySubwayParentCodeList(WorkSubway domin);
}
