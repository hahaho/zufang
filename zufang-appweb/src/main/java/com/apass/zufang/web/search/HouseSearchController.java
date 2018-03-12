package com.apass.zufang.web.search;

import static com.apass.zufang.search.enums.IndexType.HOUSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.apass.zufang.domain.common.WorkCityJd;
import com.apass.zufang.domain.entity.HouseInfoRela;
import com.apass.zufang.domain.entity.WorkSubway;
import com.apass.zufang.domain.enums.BusinessHouseTypeEnums;
import com.apass.zufang.search.enums.IndexType;
import com.apass.zufang.service.house.HouseInfoService;
import com.apass.zufang.service.nation.NationService;
import com.apass.zufang.utils.ObtainGaodeLocation;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.fieldstats.FieldStats;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.mybatis.page.Pagination;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.zufang.common.code.BusinessErrorCode;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.dto.HouseQueryParams;
import com.apass.zufang.domain.vo.HouseAppSearchVo;
import com.apass.zufang.search.condition.HouseSearchCondition;
import com.apass.zufang.search.entity.HouseEs;
import com.apass.zufang.search.enums.SortMode;
import com.apass.zufang.search.manager.ESClientManager;
import com.apass.zufang.search.manager.IndexManager;
import com.apass.zufang.search.utils.ESDataUtil;
import com.apass.zufang.service.house.HouseService;
import com.apass.zufang.service.search.SearchKeyService;
import com.apass.zufang.service.searchhistory.WorkSubwaySevice;
import com.google.common.collect.Lists;

/**
 * 商品搜索类
 */
@Path("/app/house")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class HouseSearchController {
	private static final Logger LOGGER = LoggerFactory.getLogger(HouseSearchController.class);

	@Autowired
	private SearchKeyService searchKeyService;
	@Autowired
	private HouseService houseService;
	@Autowired
	private WorkSubwaySevice workSubwaySevice;
	@Autowired
	private HouseInfoService houseInfoService;
	@Autowired
	private NationService nationService;

	/**
	 * 添加致搜索记录表
	 * @param paramMap
	 * @return
     */
    @POST
    @Path(value = "/addCommon")
    public Response addCommonSearchKeys(Map<String, Object> paramMap){
    	
    	String searchValue = CommonUtils.getValue(paramMap, "searchValue");
    	String userId = CommonUtils.getValue(paramMap, "userId");
    	String deviceId = CommonUtils.getValue(paramMap,"deviceId");
    	if(!StringUtils.isBlank(searchValue)){
    		searchKeyService.addCommonSearchKeys(searchValue,userId,deviceId);
    	}
    	return Response.success("添加成功!");
    }
    

    @POST
    @Path(value = "/delete")
    public Response delteSearchKeys(Map<String,Object> paramMap){
    	
    	String deviceId = CommonUtils.getValue(paramMap, "deviceId");
    	try {
    		if(StringUtils.isBlank(deviceId)){
    			throw new BusinessException("参数传值有误!");
    		}
    		searchKeyService.deleteSearchKeysByDeviceId(deviceId);
		}catch(BusinessException e){
			return Response.fail(e.getErrorDesc());
		}catch (Exception e) {
			return Response.fail(e.getMessage());
		}
    	return Response.success("删除成功!");
    }

	/**
     * 首页查询
     * @param paramMap
     * @return
     */
	@POST
	@Path(value = "/search")
	public Response search2(@RequestBody Map<String, Object> paramMap) {
		try {
			LOGGER.info("首页搜索执行,参数:{}", GsonUtils.toJson(paramMap));

			HouseSearchCondition houseSearchCondition = new HouseSearchCondition();
			//搜索必传参数
			// 设备号
			String deviceId = CommonUtils.getValue(paramMap, "deviceId");
			// 用户号
			String userId = CommonUtils.getValue(paramMap, "userId");
			//页面和数量
			String page = CommonUtils.getValue(paramMap, "page");
			String rows = CommonUtils.getValue(paramMap, "rows");

			//首页搜索接收的参数
			String searchValue = CommonUtils.getValue(paramMap, "searchValue");
			//点击整租合租所传参数
			String rentType = CommonUtils.getValue(paramMap, "rentType");

			Integer pages = null;
			Integer row = null;
			if (StringUtils.isNotEmpty(rows)) {
				row = Integer.valueOf(rows);
			} else {
				row = 20;
			}
			pages = StringUtils.isEmpty(page) ? 1 : Integer.valueOf(page);
			Integer offset = (pages-1)*row;
			houseSearchCondition.setOffset(offset);
			houseSearchCondition.setPageSize(row);

			Map<String, Object> returnMap = new HashMap<String, Object>();
			List<HouseAppSearchVo> list = new ArrayList<HouseAppSearchVo>();
			if(StringUtils.isEmpty(rentType)){
				String regex = "^[a-zA-Z0-9\\u4e00-\\u9fa5\\ ()（）.\\[\\]+=/\\-_\\【\\】]+$";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(searchValue);
				Boolean searchValueFalge = false;
				if (matcher.matches()) {
					searchValueFalge = true;
					// 插入数据:搜索记录
					searchKeyService.addCommonSearchKeys(searchValue, userId, deviceId);
				}
				if(StringUtils.isEmpty(searchValue)){
					throw new RuntimeException("输入关键字才能搜索哦");
				}
				houseSearchCondition.setSortMode(SortMode.PAGEVIEW_DESC);
				houseSearchCondition.setApartmentName(searchValue);
				houseSearchCondition.setCommunityName(searchValue);
				houseSearchCondition.setDetailAddr(searchValue);
				houseSearchCondition.setHouseTitle(searchValue);

				long before = System.currentTimeMillis();
				Pagination<HouseEs> pagination = new Pagination<>();
				if (searchValueFalge) {
					pagination = IndexManager.HouseSearch(houseSearchCondition);
				}

				for (HouseEs houseEs : pagination.getDataList()) {
					list.add(houseEsToHouseAppSearchVo(houseEs));
				}
				long after = System.currentTimeMillis();

				LOGGER.info("Es查询用时：" + (after - before)/1000+"s");
				Integer totalCount = pagination.getTotalCount();
				returnMap.put("totalCount", totalCount);

				returnMap.put("houseDataList", list);
				return Response.successResponse(returnMap);
			}else {
				TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("rentType", rentType);
				SearchRequestBuilder serachBuilder = ESClientManager.getClient().prepareSearch()
						.addSort(SortMode.PAGEVIEW_DESC.getSortField(),SortOrder.DESC)
						.setTypes(HOUSE.getDataName())
						.setQuery(termQueryBuilder)
						.setFrom(offset).setSize(row);
				SearchResponse response = serachBuilder.execute().actionGet();

				SearchHits searchHits = response.getHits();
				for (SearchHit hit : searchHits.getHits()) {
					HouseEs houseEs =(HouseEs) ESDataUtil.readValue(hit.source(), IndexType.HOUSE.getTypeClass());
					list.add(houseEsToHouseAppSearchVo(houseEs));
				}
				int total = (int) searchHits.getTotalHits();

				returnMap.put("totalCount", total);
				returnMap.put("houseDataList", list);
				return Response.successResponse(returnMap);
			}
		} catch (Exception e) {
			LOGGER.error("ES查询，出现异常,--Exception--:{}",e);
			// 当用ES查询时出错时查询数据库的数据
			Map<String, Object> returnMap2 = new HashMap<>();
			try {
				returnMap2 = searchMysqlDate(paramMap);
				return Response.successResponse(returnMap2);
			} catch (Exception e1) {
				LOGGER.error("查询数据库存失败,Exception:{}",e1);
				return Response.fail(BusinessErrorCode.LOAD_INFO_FAILED.getMsg());
			}
		}
	}

	/**
	 * 搜索结果页查询
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path(value = "/search/filter")
	public Response searchFilter(@RequestBody Map<String, Object> paramMap) {
		try{
			String apartmentName = CommonUtils.getValue(paramMap, "apartmentName");
			String priceFlag = CommonUtils.getValue(paramMap,"priceFlag");
			String rentType = CommonUtils.getValue(paramMap, "rentType");
			String room = CommonUtils.getValue(paramMap, "room");
			String configName = CommonUtils.getValue(paramMap, "configName");
			String areaCode = CommonUtils.getValue(paramMap, "areaCode");
			String subCode = CommonUtils.getValue(paramMap, "subCode");

			String page = CommonUtils.getValue(paramMap, "page");
			String rows = CommonUtils.getValue(paramMap, "rows");
			Integer pages = null;
			Integer row = null;
			if (StringUtils.isNotEmpty(rows)) {
				row = Integer.valueOf(rows);
			} else {
				row = 20;
			}
			pages = StringUtils.isEmpty(page) ? 1 : Integer.valueOf(page);
			Integer offset = (pages-1)*row;

			/**
			 * 思路：先根据品牌、价格、筛选条件查询房源List,然后遍历结果计算每个house与目标位置距离，如果<1km,返回。否则过虑掉
			 */
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
			if(StringUtils.isNotEmpty(apartmentName)){
				boolQueryBuilder.must(QueryBuilders.matchQuery("apartmentName",apartmentName));
			}
			if(StringUtils.isNotEmpty(priceFlag)){
				boolQueryBuilder.must(QueryBuilders.termQuery("priceFlag",priceFlag).boost(1.5f));
			}
			//如果户型选不限，则不加此条件
			if(StringUtils.isNotEmpty(rentType)){
				if(StringUtils.equals(BusinessHouseTypeEnums.HZ_1.getCode().toString(),rentType)
						|| StringUtils.equals(BusinessHouseTypeEnums.HZ_2.getCode().toString(),rentType)){
					boolQueryBuilder.must(QueryBuilders.termQuery("rentType",rentType).boost(1.5f));
				}
			}
			if(StringUtils.isNotEmpty(room)){
				String[] roomArr = room.split(",");
				switch(roomArr.length)
				{
					case 1:
						boolQueryBuilder.must(QueryBuilders.termsQuery("room", roomArr[0]).boost(1.5f));
						break;
					case 2:
						boolQueryBuilder.must(QueryBuilders.termsQuery("room", roomArr[0],roomArr[1]).boost(1.5f));
						break;
					case 3:
						boolQueryBuilder.must(QueryBuilders.termsQuery("room", roomArr[0],roomArr[1],roomArr[2]).boost(1.5f));
						break;
					case 4:
						boolQueryBuilder.must(QueryBuilders
								.termsQuery("room", roomArr[0],roomArr[1],roomArr[2],roomArr[3]).boost(1.5f));
						break;
					case 5:
						boolQueryBuilder.must(QueryBuilders
								.termsQuery("room", roomArr[0],roomArr[1],roomArr[2],roomArr[3],roomArr[4]).boost(1.5f));
						break;
					default:
						break;
				}

			}
			if(StringUtils.isNotEmpty(configName)){
				String[] configArr = configName.split(",");
				switch(configArr.length)
				{
					case 1:
						boolQueryBuilder.must(QueryBuilders.termsQuery("configName", configArr[0]).boost(1.5f));
						break;
					case 2:
						boolQueryBuilder.must(QueryBuilders.termsQuery("configName", configArr[0],configArr[1]).boost(1.5f));
						break;
					case 3:
						boolQueryBuilder.must(QueryBuilders.termsQuery("configName", configArr[0],configArr[1],configArr[2]).boost(1.5f));
						break;
					case 4:
						boolQueryBuilder.must(QueryBuilders
								.termsQuery("configName", configArr[0],configArr[1],configArr[2],configArr[3]).boost(1.5f));
						break;
					case 5:
						boolQueryBuilder.must(QueryBuilders
								.termsQuery("configName", configArr[0],configArr[1],configArr[2],configArr[3],configArr[4]).boost(1.5f));
						break;
					default:
						break;
				}
			}

			SearchRequestBuilder serachBuilder = ESClientManager.getClient().prepareSearch()
					.addSort(SortMode.PAGEVIEW_DESC.getSortField(),SortOrder.DESC)
					.setTypes(IndexType.HOUSE.getDataName())
					.setQuery(boolQueryBuilder)
					.setFrom(pages).setSize(row);
			serachBuilder.setFrom(offset).setSize(row);
			SearchResponse response = serachBuilder.execute().actionGet();
			SearchHit[] hits = response.getHits().getHits();

			Map<String, Object> returnMap = new HashMap<String, Object>();
			returnMap.put("totalCount", response.getHits().getTotalHits());
			//查询出的总房源：未先地点时返回
			List<HouseAppSearchVo> houseList = Lists.newArrayList();
			//5公里内的房源
			List<HouseAppSearchVo> houseList2 = Lists.newArrayList();
			//附近房源排序calculateDistanceAndSort方法需要此参数
			List<HouseInfoRela> houseRelas = Lists.newArrayList();
			String[] location = null;
			for(SearchHit hit: hits){
				//如果位置筛选不为空，计算所查结果与目标经纬度的距离
				HouseEs houseEs  = (HouseEs)ESDataUtil.readValue(hit.source(), HOUSE.getTypeClass());
				houseRelas.add(houseEsToHouseRElas(houseEs));
				houseList.add(houseEsToHouseAppSearchVo(houseEs));
				if(StringUtils.isNotEmpty(subCode)){
					//根据code查询经纬度，计算距离
					WorkSubway workSubway = workSubwaySevice.selectSubwaybyCode(subCode);
					String nearestPoint = workSubway.getNearestPoint();
					location = nearestPoint.split(",");
					double longitude = houseEs.getLongitude();
					double latitude = houseEs.getLatitude();

					double distance = houseInfoService.distanceSimplify(Double.valueOf(location[0]),Double.valueOf(location[1]),longitude,latitude);
					if(distance>5000d){
						houseList2.add(houseEsToHouseAppSearchVo(houseEs));
					}
				}
				if(StringUtils.isNotEmpty(areaCode)){
					//根据code查询经纬度，计算距离
					WorkCityJd workCityJd = nationService.selectWorkCityByCode(areaCode);
					StringBuffer sb = new StringBuffer();
					String address = sb.append(workCityJd.getProvince()).append(workCityJd.getCity())
							.append(workCityJd.getDistrict()).append(workCityJd.getTowns()).toString();
					location = new ObtainGaodeLocation().getLocation(address);
					double longitude = houseEs.getLongitude();
					double latitude = houseEs.getLatitude();

					double distance = houseInfoService.distanceSimplify(Double.valueOf(location[0]),Double.valueOf(location[1]),longitude,latitude);
					if(distance>5000d){
						houseList2.add(houseEsToHouseAppSearchVo(houseEs));
					}
				}
			}

			if(StringUtils.isNotEmpty(subCode) || StringUtils.isNotEmpty(areaCode)){
				List<HouseAppSearchVo> list = houseInfoService.calculateDistanceAndSort2(Double.valueOf(location[0]),Double.valueOf(location[1]),houseList2);
				returnMap.put("houseDataList", list);
				return Response.success("ES查询成功",returnMap);
			}

			returnMap.put("houseDataList", houseList);
			return Response.success("ES查询成功",returnMap);
		}catch (Exception e){
			LOGGER.error("ES查询失败！",e);
			return Response.fail("ES查询失败");
		}

	}

	/**
	 * HouseEs-->HouseInfoRela
	 * @param houseEs
	 * @return
     */
	private HouseInfoRela houseEsToHouseRElas(HouseEs houseEs) {
		HouseInfoRela houseInfoRela = new HouseInfoRela();
		houseInfoRela.setApartmentId(houseEs.getApartmentId());
		houseInfoRela.setHouseId(houseEs.getHouseId());
		houseInfoRela.setProvince(houseEs.getProvince());
		houseInfoRela.setCity(houseEs.getCity());

		houseInfoRela.setLatitude(houseEs.getLatitude());
		houseInfoRela.setLongitude(houseEs.getLongitude());
		return  houseInfoRela;
	}

	/**
	 * HouseEs-->HouseAppSearchVo
	 * @param houseEs
	 * @return
	 */
	private HouseAppSearchVo houseEsToHouseAppSearchVo(HouseEs houseEs) {
		HouseAppSearchVo vo = new HouseAppSearchVo();
		vo.setUrl(houseEs.getUrl());
		vo.setHouseTitle(houseEs.getHouseTitle());
		vo.setDetailAddr(houseEs.getDetailAddr());
		vo.setRoom(houseEs.getRoom());
		vo.setHall(houseEs.getHall());
		vo.setWei(houseEs.getWei());
		vo.setFloor(houseEs.getFloor());
		vo.setAcreage(houseEs.getAcreage());
		vo.setRoomAcreage(houseEs.getRoomAcreage());
		vo.setRentAmt(houseEs.getRentAmt());
		vo.setHouseId(houseEs.getHouseId());
		vo.setLatitude(houseEs.getLatitude());
		vo.setLongitude(houseEs.getLongitude());

		StringBuffer sb = new StringBuffer();
		String houseDes = sb.append(houseEs.getRoom()).append(houseEs.getHall())
				.append(houseEs.getWei()).append("-").append(houseEs.getAcreage()).toString();
		vo.setHouseDes(houseDes);
		return vo;
	}

	/**
     * 当用ES查询时出错时查询数据库的数据
     * @return
     * @throws Exception 
     */
	public Map<String, Object> searchMysqlDate(Map<String, Object> paramMap) throws Exception {
		String searchValue = CommonUtils.getValue(paramMap, "searchValue");
		// 顺序(desc（降序），asc（升序）)
		String order = CommonUtils.getValue(paramMap, "order");
		String page = CommonUtils.getValue(paramMap, "page");
		String rows = CommonUtils.getValue(paramMap, "rows");

		if (StringUtils.isEmpty(order)) {
			// 降序
			order = "DESC";
		}
		//存储返回数据
		List<HouseAppSearchVo> list = Lists.newArrayList();
		HouseQueryParams houseQueryParams = new HouseQueryParams();
		houseQueryParams.setHouseTitle(searchValue);
		houseQueryParams.setCommunityName(searchValue);
		houseQueryParams.setDetailAddr(searchValue);
		houseQueryParams.setPageParams(Integer.valueOf(rows),Integer.valueOf(page));

		list = houseService.queryHouseBasicEntityByEntity(houseQueryParams);
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("totalCount", list.size());
		returnMap.put("houseDataList", list);
		return returnMap;
	}
}
