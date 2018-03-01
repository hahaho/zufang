package com.apass.zufang.service.searchhistory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apass.zufang.domain.common.Stops;
import com.apass.zufang.domain.common.WorkCityJd;
import com.apass.zufang.domain.common.WorkSubwayContent;
import com.apass.zufang.domain.entity.WorkSubway;
import com.apass.zufang.mapper.zfang.WorkCityJdMapper;
import com.apass.zufang.mapper.zfang.WorkSubwayMapper;
import com.apass.zufang.utils.ObtainGaodeLocation;

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
	 * 
	 * @param code
	 * @return
	 */
	public List<WorkCityJd> queryCityJdParentCodeList(String code) {
		return cityJddao.selectDateByParentId(code);
	}

	/**
	 * 获取当前位置下所有的子节点 地铁
	 * 
	 * @param code
	 * @return
	 */
	public List<WorkSubway> querySubwayParentCodeList(WorkSubway domin) {
		return dao.querySubwayParentCodeList(domin);
	}

	/**
	 * 接入地铁数据
	 * 
	 * @param qt
	 * @param c
	 * @param t
	 */
	public void insertInfoWorksubway(String qt, String c, String t, String city, String code) {
		List<WorkSubwayContent> resultList = ObtainGaodeLocation.getWorksubwayObject(qt, c, t);
		for (int i = 0; i < resultList.size(); i++) {
			WorkSubwayContent resultp = resultList.get(i);
			WorkSubway workSubwayp = new WorkSubway();
			workSubwayp.setLineName(resultp.getLine_name());
			workSubwayp.setSiteName("");
			workSubwayp.setLevel("1");
			workSubwayp.setNearestPoint("");
			workSubwayp.setParentCode(Long.valueOf(code));

			Integer order = dao.selectByMaxDisplayCode();
			if (null == order) {
				order = 0;
			}
			Long parentCode=Long.valueOf(order + 1);
			workSubwayp.setCode(Long.valueOf(order + 1));
			dao.insert(workSubwayp);

			for (int j = 0; j < resultp.getStops().size(); j++) {
				Stops stops = resultp.getStops().get(j);

				WorkSubway workSubways = new WorkSubway();
				workSubways.setLineName("");
				workSubways.setSiteName(stops.getName());
				workSubways.setLevel("2");
				// 获取坐标
				String[] objstr = ObtainGaodeLocation.getLocationKey(city + stops.getName());
				System.out.println(objstr.toString());
				workSubways.setNearestPoint(objstr[0] + "," + objstr[1]);
				workSubways.setParentCode(Long.valueOf(parentCode));

				Integer orderStops = dao.selectByMaxDisplayCode();
				if (null == orderStops) {
					orderStops = 0;
				}
				workSubways.setCode(Long.valueOf(orderStops + 1));
				dao.insert(workSubways);
			}
		}
	}
}
