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

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
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
			HouseSearchCondition houseSearchCondition = new HouseSearchCondition();
			//搜索必传参数
			// 设备号
			String deviceId = CommonUtils.getValue(paramMap, "deviceId");
			// 用户号
			String userId = CommonUtils.getValue(paramMap, "userId");
			// 排序字段(default:默认;pageView)
			String sort = CommonUtils.getValue(paramMap, "sort");

			//页面和数量
			String page = CommonUtils.getValue(paramMap, "page");
			String rows = CommonUtils.getValue(paramMap, "rows");

			//首页搜索接收的参数
			String searchValue = CommonUtils.getValue(paramMap, "searchValue");
			//点击整租合租所传参数
			String rentType = CommonUtils.getValue(paramMap, "rentType");
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

				//searchValue = Pinyin4jUtil.converterToSpell(searchValue);
				houseSearchCondition.setSortMode(SortMode.PAGEVIEW_DESC);
				houseSearchCondition.setApartmentName(searchValue);
				houseSearchCondition.setCommunityName(searchValue);
				houseSearchCondition.setDetailAddr(searchValue);
				houseSearchCondition.setHouseTitle(searchValue);

				Map<String, Object> returnMap = new HashMap<String, Object>();

				long before = System.currentTimeMillis();
				Pagination<HouseEs> pagination = new Pagination<>();
				if (searchValueFalge) {
					pagination = IndexManager.HouseSearch(houseSearchCondition);
				}

				List<HouseAppSearchVo> list = new ArrayList<HouseAppSearchVo>();
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
				return null;
			}

			// 当查询结果为空时，返回热卖单品
//

		} catch (Exception e) {
			LOGGER.error("ES查询，出现异常,--Exception--:{}",e);
			// 当用ES查询时出错时查询数据库的数据
			Map<String, Object> returnMap2 = new HashMap<>();
			try {
				returnMap2 = searchMysqlDate(paramMap);
				return Response.successResponse(returnMap2);
			} catch (Exception e1) {
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
			String areaCode = CommonUtils.getValue("areaCode");
			String subCode = CommonUtils.getValue("subCode");

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
				boolQueryBuilder.must(QueryBuilders.wildcardQuery("apartmentName", "*" + apartmentName + "*").boost(1.5f));
			}
			if(StringUtils.isNotEmpty(priceFlag)){
				boolQueryBuilder.must(QueryBuilders.termQuery("priceFlag",priceFlag).boost(1.5f));
			}
			//如果户型选不限，则不加此条件
			if(StringUtils.isNotEmpty(rentType) && (StringUtils.equals("1",rentType)|| StringUtils.equals("2",rentType)) ){
				boolQueryBuilder.must(QueryBuilders.termQuery("rentType",rentType).boost(1.5f));
			}
			if(StringUtils.isNotEmpty(room)){
				boolQueryBuilder.must(QueryBuilders.termQuery("room",room).boost(1.5f));
			}
			if(StringUtils.isNotEmpty(configName)){
				boolQueryBuilder.must(QueryBuilders.termQuery("configName",configName).boost(1.5f));
			}

			SearchRequestBuilder serachBuilder = ESClientManager.getClient().prepareSearch()
					.setTypes(HOUSE.getDataName())
					.setQuery(boolQueryBuilder);
			serachBuilder.setFrom(offset).setSize(row);
			SearchResponse response = serachBuilder.execute().actionGet();
			SearchHit[] hits = response.getHits().getHits();

			List<HouseEs> houseList = Lists.newArrayList();
			for(SearchHit hit: hits){
				//如果位置筛选不为空，计算所查结果与目标经纬度的距离
				HouseEs houseEs  = (HouseEs)ESDataUtil.readValue(hit.source(), HOUSE.getTypeClass());
				if(StringUtils.isNotEmpty(subCode)){
					//根据code查询经纬度，计算距离 TODO

				}
				if(StringUtils.isNotEmpty(areaCode)){
					//根据code查询经纬度，计算距离 TODO
				}
				houseList.add(houseEs);
				System.out.println(GsonUtils.toJson(houseEs));
			}
			return Response.success("ES查询成功",houseList);
		}catch (Exception e){
			LOGGER.error("ES查询失败！",e);
			return Response.fail("ES查询失败");
		}

	}

	private HouseAppSearchVo houseEsToHouseAppSearchVo(HouseEs houseEs) {
		HouseAppSearchVo vo = new HouseAppSearchVo();
		vo.setUrl(houseEs.getUrl());
		vo.setHouseTitle(houseEs.getHouseTitle());
		vo.setDescription(houseEs.getDescription());
		vo.setDetailAddr(houseEs.getDetailAddr());
		vo.setRoom(houseEs.getRoom());
		vo.setHall(houseEs.getHall());
		vo.setWei(houseEs.getWei());
		vo.setFloor(houseEs.getFloor());
		vo.setAcreage(houseEs.getAcreage());
		vo.setRentAmt(houseEs.getRentAmt());

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
		String sort = CommonUtils.getValue(paramMap, "sort");// 排序字段(default:默认;amount:销量;new:新品;price：价格)
		String order = CommonUtils.getValue(paramMap, "order");// 顺序(desc（降序），asc（升序）)
		String page = CommonUtils.getValue(paramMap, "page");
		String rows = CommonUtils.getValue(paramMap, "rows");

		if (StringUtils.isEmpty(order)) {
			order = "DESC";// 降序
		}
		List<HouseAppSearchVo> list = Lists.newArrayList();//存储返回数据
		HouseQueryParams houseQueryParams = new HouseQueryParams();
//		houseQueryParams.setApartmentName(searchValue);
		houseQueryParams.setHouseTitle(searchValue);
		houseQueryParams.setCommunityName(searchValue);
		houseQueryParams.setDetailAddr(searchValue);
		houseQueryParams.setPageParams(Integer.valueOf(rows),Integer.valueOf(page));

		list = houseService.queryHouseBasicEntityByEntity(houseQueryParams);
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("houseDataList", list);

		return returnMap;
	}
}
