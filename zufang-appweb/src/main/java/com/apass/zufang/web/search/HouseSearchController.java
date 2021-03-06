package com.apass.zufang.web.search;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.mybatis.page.Pagination;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.zufang.common.code.BusinessErrorCode;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.common.WorkCityJd;
import com.apass.zufang.domain.dto.HouseQueryParams;
import com.apass.zufang.domain.entity.HouseInfoRela;
import com.apass.zufang.domain.entity.WorkSubway;
import com.apass.zufang.domain.enums.BusinessHouseTypeEnums;
import com.apass.zufang.domain.enums.HuxingEnums;
import com.apass.zufang.domain.enums.PriceRangeEnum;
import com.apass.zufang.domain.enums.TrafficEnums;
import com.apass.zufang.domain.vo.HouseAppSearchVo;
import com.apass.zufang.search.condition.HouseSearchCondition;
import com.apass.zufang.search.entity.HouseEs;
import com.apass.zufang.search.enums.IndexType;
import com.apass.zufang.search.enums.SortFieldEnum;
import com.apass.zufang.search.enums.SortMode;
import com.apass.zufang.search.manager.ESClientManager;
import com.apass.zufang.search.manager.IndexManager;
import com.apass.zufang.search.utils.ESDataUtil;
import com.apass.zufang.service.house.HouseInfoService;
import com.apass.zufang.service.house.HouseService;
import com.apass.zufang.service.nation.NationService;
import com.apass.zufang.service.search.SearchKeyService;
import com.apass.zufang.service.searchhistory.WorkSubwaySevice;
import com.apass.zufang.utils.ObtainGaodeLocation;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.apass.zufang.search.enums.IndexType.HOUSE;

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
	private NationService nationService;
	@Autowired
	private ObtainGaodeLocation obtainGaodeLocation;

	/**
	 * 直辖市
	 */
	private static final String[] CENTRL_CITY_ARRAY = {"1", "2", "3", "4","32","52993"};
	private static final List<String> CENTRL_CITY_LIST = Arrays.asList(CENTRL_CITY_ARRAY);

	private static final String[] CENTRL_CITY_ARRAY2 = {"北京市", "上海市", "重庆市", "天津市"};
	private static final List<String> CENTRL_CITY_LIST2 = Arrays.asList(CENTRL_CITY_ARRAY2);

	/**
	 * 周围几公里距离:5公理
	 */
	private static final double ROUND_DISTANSE = 5;

	@Value("${zufang.image.uri}")
	private String appWebDomain;
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
			if(StringUtils.isEmpty(deviceId)){
				throw new RuntimeException("请传入设备号deviceId");
			}
			// 用户id:用户没登陆也可以搜索
			String userId = CommonUtils.getValue(paramMap, "userId");
			//页面和数量
			String page = CommonUtils.getValue(paramMap, "page");
			String rows = CommonUtils.getValue(paramMap, "rows");
			//当前地址
			String city = CommonUtils.getValue(paramMap, "city");


			//首页搜索接收的参数
			String searchValue = CommonUtils.getValue(paramMap, "searchValue");
			//点击整租合租所传参数
			String rentType = CommonUtils.getValue(paramMap, "rentType");

			if(StringUtils.isEmpty(searchValue) && StringUtils.isEmpty(rentType)){
				throw new RuntimeException("搜索内容和租房类型不能同时为空");
			}
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
			if(StringUtils.isEmpty(city)){
				throw new RuntimeException("请传入当前所有城市");
			}
			if(CENTRL_CITY_LIST2.contains(city)){
				city = city.substring(0, city.length()-1);
			}
			houseSearchCondition.setCity(city);
			houseSearchCondition.setSortMode(SortMode.PAGEVIEW_DESC);

			Map<String, Object> returnMap = new HashMap<String, Object>();
			List<HouseAppSearchVo> list = new ArrayList<HouseAppSearchVo>();
			Boolean searchValueFalge = false;
			if(StringUtils.isNotEmpty(searchValue)){
				String regex = "^[a-zA-Z0-9\\u4e00-\\u9fa5\\ ()（）.\\[\\]+=/\\-_\\【\\】]+$";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(searchValue);
//				if (matcher.matches()) {
					searchValueFalge = true;
					// 插入数据:搜索记录
					searchKeyService.addCommonSearchKeys(searchValue, userId, deviceId);
//				}
				houseSearchCondition.setHouseTitle(searchValue);
			}

			if(StringUtils.isNotEmpty(rentType)){
				searchValueFalge = true;
				houseSearchCondition.setRentType(rentType);
			}


			Pagination<HouseEs> pagination = new Pagination<>();
			if (searchValueFalge) {
				pagination = IndexManager.HouseSearch(houseSearchCondition);
			}

			for (HouseEs houseEs : pagination.getDataList()) {
				list.add(houseEsToHouseAppSearchVo(houseEs));
			}
			Integer totalCount = pagination.getTotalCount();
			returnMap.put("totalCount", totalCount);

			returnMap.put("houseDataList", list);
			return Response.successResponse(returnMap);

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
		LOGGER.info("房屋筛选执行,参数:{}", GsonUtils.toJson(paramMap));
		try{
			//首页搜索接收的参数
			String searchValue = CommonUtils.getValue(paramMap, "searchValue");
			String city = CommonUtils.getValue(paramMap, "city");
			if(StringUtils.isEmpty(city)){
				throw new RuntimeException("请传入地址！");
			}
			if(CENTRL_CITY_LIST2.contains(city)){
				city = city.substring(0, city.length()-1);
			}
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
			if(StringUtils.isNotEmpty(searchValue)){
				MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(searchValue,
						"communityName","houseTitle", "detailAddr","apartmentName","district").operator(Operator.AND);
				multiMatchQueryBuilder.field("communityName", 1.5f);
				multiMatchQueryBuilder.field("houseTitle", 2f);
				multiMatchQueryBuilder.field("detailAddr", 1f);
				multiMatchQueryBuilder.field("apartmentName", 1f);
				multiMatchQueryBuilder.field("district", 2f);

				boolQueryBuilder.must(multiMatchQueryBuilder);
			}

			MultiMatchQueryBuilder multiMatchQueryBuilder2 = QueryBuilders.multiMatchQuery(city,
					"province","city", "district").operator(Operator.AND);
			multiMatchQueryBuilder2.field("province", 2f);
			multiMatchQueryBuilder2.field("city", 2f);
			multiMatchQueryBuilder2.field("district", 1f);
			boolQueryBuilder.must(multiMatchQueryBuilder2);
			if(StringUtils.isNotEmpty(apartmentName)){
				boolQueryBuilder.must(QueryBuilders.matchQuery("apartmentName",apartmentName).operator(Operator.AND));
			}
				if(StringUtils.isNotEmpty(priceFlag) && !priceFlag.equals(String.valueOf(PriceRangeEnum.PRICE_ALL.getVal()))){
				boolQueryBuilder.must(QueryBuilders.termQuery("priceFlag",priceFlag).boost(2.5f));
			}
			//如果类型选不限，则不加此条件
			if(StringUtils.isNotEmpty(rentType)){
				if(StringUtils.equals(BusinessHouseTypeEnums.HZ_1.getCode().toString(),rentType)
						|| StringUtils.equals(BusinessHouseTypeEnums.HZ_2.getCode().toString(),rentType)){
					boolQueryBuilder.must(QueryBuilders.termQuery("rentType",rentType).boost(2.5f));
				}
			}
			if(StringUtils.isNotEmpty(room)){
				String[] roomArr = room.split(",");
				List roomList = Arrays.asList(roomArr);
				//如果户型选不限,则无此条件
				if(!roomList.contains(HuxingEnums.HUXING_0.getCode().toString())){
					if(roomList.contains(HuxingEnums.HUXING_MAX.getCode().toString())){
						boolQueryBuilder.should(QueryBuilders.rangeQuery("room").gt(4));
					}
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
									.termsQuery("room", roomArr[0],roomArr[1],roomArr[2],roomArr[3]).boost(1.5f));
							break;
						default:
							break;
					}
				}
			}
			if(StringUtils.isNotEmpty(configName)){
				boolQueryBuilder.must(QueryBuilders.matchQuery("configName",configName).operator(Operator.AND));
			}
			if(StringUtils.isNotEmpty(subCode) && StringUtils.isNotEmpty(areaCode)){
				throw new RuntimeException("subCode和areaCode不可同时传入!");
			}

			String[] location = null;
			//传入地铁线路编码
			if(StringUtils.isNotEmpty(subCode)){
				//根据code查询经纬度，计算距离
				WorkSubway workSubway = workSubwaySevice.selectSubwaybyCode(subCode);
				LOGGER.info("subCode:{}查询地铁线路表结果：{}",subCode,GsonUtils.toJson(workSubway));
				String nearestPoint = workSubway.getNearestPoint();
				location = nearestPoint.split(",");
				if(location==null || location.length!=2){
					throw new RuntimeException("地铁线路表经纬度数据有误!");
				}
				boolQueryBuilder.must(QueryBuilders.geoDistanceQuery("location")
						.point(Double.valueOf(location[1]), Double.valueOf(location[0]))
						.distance(ROUND_DISTANSE, DistanceUnit.KILOMETERS));
			}

			//传入区域编码
			if(StringUtils.isNotEmpty(areaCode)) {
				//根据code查询经纬度，计算距离
				//towns
				WorkCityJd townJd = nationService.selectWorkCityByCode(areaCode);
				//district
				WorkCityJd districtJd = nationService.selectWorkCityByCode(String.valueOf(townJd.getParent()));
				//city
				WorkCityJd cityJd = nationService.selectWorkCityByCode(String.valueOf(districtJd.getParent()));
				String address = null;
				StringBuffer sb = new StringBuffer();
				//说明是直辖市，townJd无数据，areaCode为县code
				if(CENTRL_CITY_LIST.contains(cityJd.getCode())){
					address = sb.append(cityJd.getProvince()).append(districtJd.getCity())
							.append(townJd.getDistrict()).toString();
				}else{
					//province
					WorkCityJd provinceJd = nationService.selectWorkCityByCode(String.valueOf(cityJd.getParent()));
					address = sb.append(provinceJd.getProvince()).append(cityJd.getCity())
							.append(districtJd.getDistrict()).append(townJd.getTowns()).toString();
				}

				location = obtainGaodeLocation.getLocation(address);
				LOGGER.info("参数address:{}调用ObtainGaodeLocation.getgetLocation方法返回数据：{}",address,location);
				boolQueryBuilder.must(QueryBuilders.geoDistanceQuery("location")
						.point(Double.valueOf(location[1]), Double.valueOf(location[0]))
						.distance(ROUND_DISTANSE, DistanceUnit.KILOMETERS));
			}

			SearchRequestBuilder serachBuilder = ESClientManager.getClient().prepareSearch()
					.addSort(SortMode.PAGEVIEW_DESC.getSortField(),SortOrder.DESC)
					.setTypes(IndexType.HOUSE.getDataName())
					.setQuery(boolQueryBuilder)
                    .setFrom(offset).setSize(row);
			SearchResponse response = serachBuilder.execute().actionGet();
			SearchHit[] hits = response.getHits().getHits();

			Map<String, Object> returnMap = new HashMap<String, Object>();
			returnMap.put("totalCount", response.getHits().getTotalHits());
			//查询出的总房源：未先地点时返回
			List<HouseAppSearchVo> houseList = Lists.newArrayList();
			for(SearchHit hit: hits){
				//如果位置筛选不为空，计算所查结果与目标经纬度的距离
				HouseEs houseEs  = (HouseEs)ESDataUtil.readValue(hit.source(), HOUSE.getTypeClass());
				houseList.add(houseEsToHouseAppSearchVo(houseEs));
			}

			returnMap.put("houseDataList", houseList);
			return Response.success("ES查询成功",returnMap);
		}catch (Exception e){
			LOGGER.error("ES查询失败！",e);
			return Response.fail("ES查询失败");
		}

	}

	/**
	 * 前缀搜索查询
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path(value = "/search/prefix")
	public Response prefixSearch(@RequestBody Map<String, Object> paramMap){
        List<String> results = Lists.newArrayList();

        try{
            String searchValue = CommonUtils.getValue(paramMap, "searchValue");
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

            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                    .should(QueryBuilders.prefixQuery("detailAddr", searchValue))
                    .should(QueryBuilders.prefixQuery("street", searchValue))
                    .should(QueryBuilders.prefixQuery("district", searchValue))
                    .should(QueryBuilders.prefixQuery("houseTitle", searchValue));

            SearchRequestBuilder searchBuilder = ESClientManager.getClient().prepareSearch()
                    .setTypes(IndexType.HOUSE.getDataName())
                    .setQuery(boolQueryBuilder)
                    .setFrom(offset).setSize(row);
            SearchResponse response = searchBuilder.get();
            SearchHit[] hits = response.getHits().getHits();
            for(SearchHit hit : hits){
                HouseEs houseEs  = (HouseEs)ESDataUtil.readValue(hit.source(), HOUSE.getTypeClass());
                results.add(houseEs.getDetailAddr());
            }

            return Response.success("前缀查询成功",results);
        }catch(Exception e){
            LOGGER.error("前缀查询失败！",e);
            return Response.fail("前缀查询失败");
        }
	}

    /**
     * 附近房源搜索查询
	 * 思路:根据交通方式和时间计算距离，去es中把对应数据查出
     * @param paramMap
     * @return
     */
    @POST
    @Path(value = "/search/traffix")
    public Response traffixSearch(@RequestBody Map<String, Object> paramMap){
		LOGGER.info("the interface of nearHouse params are:{}", GsonUtils.toJson(paramMap));

		HouseSearchCondition condition = new HouseSearchCondition();
		try{

			//筛选字段:类型,户型,房屋配置
			String rentType = CommonUtils.getValue(paramMap, "rentType");
			String room = CommonUtils.getValue(paramMap, "room");
			String configName = CommonUtils.getValue(paramMap, "configName");
			condition.setRentType(rentType);
			condition.setRoom(room);
			condition.setConfigName(configName);

			//附近房源时间，位置，出行方式
			String minute = CommonUtils.getValue(paramMap, "minute");
			String area = CommonUtils.getValue(paramMap, "area");
			String trafficType = CommonUtils.getValue(paramMap, "trafficType");

			//根据交通方式和时间计算
			String distance = TrafficEnums.getDistance(Integer.valueOf(trafficType==null ? "1" :trafficType),
					Double.valueOf(minute==null ?"30":minute));
			if(StringUtils.isEmpty(distance)){
				throw new RuntimeException("交通方式参数有误：只能是1，2，3，4");
			}
			condition.setDistance(distance);

			//根据位置计算经纬度
			String[] location = obtainGaodeLocation.getLocation(area);
			LOGGER.info("位置area:{}调用ObtainGaodeLocation.getLocation方法返回数据：{}",area,Arrays.toString(location));
			condition.setLongitude(location[0]);
			condition.setLatitude(location[1]);

			//排序字段:pageView,listTime,rentAmt 与排序方式desc,asc
			String sort = CommonUtils.getValue(paramMap, "sort");
			String order = CommonUtils.getValue(paramMap, "order");

			if(StringUtils.isEmpty(sort)){
				condition.setSortMode(SortMode.DISTANCE_DESC);
			}else{
				if (SortFieldEnum.SORTFIE_DEFAULT.getField().equals(sort)) {//默认:根据距离排序由近及远
					if (StringUtils.equalsIgnoreCase("DESC", order)) {
						condition.setSortMode(SortMode.DISTANCE_DESC);
					} else {
						condition.setSortMode(SortMode.DISTANCE_DESC);
					}
				} else if (SortFieldEnum.SORTFIE_NEWESET.getField().equals(sort)) {//新品
					if (StringUtils.equalsIgnoreCase("DESC", order)) {
						condition.setSortMode(SortMode.ORDERLIST_DESC);
					} else {
						condition.setSortMode(SortMode.ORDERLIST_ASC);
					}
				} else if (SortFieldEnum.SORTFIE_PRICE.getField().equals(sort)) {// 价格
					if (StringUtils.equalsIgnoreCase("DESC", order)) {
						condition.setSortMode(SortMode.RENTAMT_DESC);
					} else {
						condition.setSortMode(SortMode.RENTAMT_ASC);
					}
				} else {
					throw new RuntimeException("缺少排序字段和排序方式");
				}
			}

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

			condition.setOffset(offset);
			condition.setPageSize(row);

			Pagination<HouseEs> pagination = IndexManager.traffixSearch(condition);

			List<HouseAppSearchVo> houseList = Lists.newArrayList();
			houseList.addAll(pagination.getDataList().stream().map(this::houseEsToHouseAppSearchVo).collect(Collectors.toList()));

			Map<String,Object> resultMap = Maps.newHashMap();
			resultMap.put("houseList",houseList);
			resultMap.put("totalcount",pagination.getTotalCount());

			return Response.success("附近房源查询成功",resultMap);
		}catch (Exception e){
			LOGGER.error("ES查询失败！",e);
			return Response.fail("ES查询失败:"+e.getMessage());
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
		if(StringUtils.isNotEmpty(houseEs.getUrl())){
			if(!houseEs.getUrl().contains("http")){
				vo.setUrl(appWebDomain+houseEs.getUrl().split(",")[0]);
			}else {
				vo.setUrl(houseEs.getUrl().split(",")[0]);
			}
		}
		vo.setHouseTitle(houseEs.getHouseTitle());
		vo.setDetailAddr(houseEs.getDetailAddr());
		vo.setRoom(houseEs.getRoom());
		vo.setHall(houseEs.getHall());
		vo.setWei(houseEs.getWei());
		vo.setFloor(houseEs.getFloor());
		vo.setAcreage(houseEs.getAcreage());
		vo.setRoomAcreage(houseEs.getRoomAcreage());
		vo.setRentAmt(houseEs.getRentAmt().setScale(0, BigDecimal.ROUND_DOWN));
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
