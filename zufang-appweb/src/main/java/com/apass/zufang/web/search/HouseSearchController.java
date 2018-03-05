package com.apass.zufang.web.search;

import com.apass.esp.common.code.BusinessErrorCode;
import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.mybatis.page.Pagination;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.dto.HouseQueryParams;
import com.apass.zufang.domain.vo.HouseAppSearchVo;
import com.apass.zufang.search.condition.HouseSearchCondition;
import com.apass.zufang.search.entity.HouseEs;
import com.apass.zufang.search.enums.SortMode;
import com.apass.zufang.search.manager.IndexManager;
import com.apass.zufang.service.house.ApartHouseService;
import com.apass.zufang.service.house.HouseService;
import com.apass.zufang.service.search.SearchKeyService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	private ApartHouseService apartHouseService;

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
     * 查询
     * @param paramMap
     * @return
     */
	@POST
	@Path(value = "/search")
	public Response search2(@RequestBody Map<String, Object> paramMap) {
		try {
			HouseSearchCondition houseSearchCondition = new HouseSearchCondition();

			String deviceId = CommonUtils.getValue(paramMap, "deviceId");// 设备号
			String userId = CommonUtils.getValue(paramMap, "userId");// 用户号
			String searchValue = CommonUtils.getValue(paramMap, "searchValue");
			String sort = CommonUtils.getValue(paramMap, "sort");// 排序字段(default:默认;amount:销量;new:新品;price：价格)
			String order = CommonUtils.getValue(paramMap, "order");// 顺序(desc（降序），asc（升序）)
			String page = CommonUtils.getValue(paramMap, "page");
			String rows = CommonUtils.getValue(paramMap, "rows");

			String regex = "^[a-zA-Z0-9\\u4e00-\\u9fa5\\ ()（）.\\[\\]+=/\\-_\\【\\】]+$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(searchValue);
			String searchValue2 = "";
			Boolean searchValueFalge = false;
			if (matcher.matches()) {
				searchValueFalge = true;
				searchValue2 = searchValue;
				// 插入数据:搜索记录
				searchKeyService.addCommonSearchKeys(searchValue2, userId, deviceId);
			}

			if (!StringUtils.equalsIgnoreCase("ASC", order) && !StringUtils.equalsIgnoreCase("DESC", order)) {
				order = "DESC";// 降序
			}
			Integer pages = null;
			Integer row = null;
			if (StringUtils.isNotEmpty(rows)) {
				row = Integer.valueOf(rows);
			} else {
				row = 20;
			}
			pages = StringUtils.isEmpty(page) ? 1 : Integer.valueOf(page);

			houseSearchCondition.setSortMode(SortMode.PAGEVIEW_DESC);

			houseSearchCondition.setCommunityName(searchValue);
			houseSearchCondition.setDetailAddr(searchValue);
			houseSearchCondition.setHouseTitle(searchValue);

			Map<String, Object> returnMap = new HashMap<String, Object>();

			long before = System.currentTimeMillis();
			Pagination<HouseEs> pagination = new Pagination<>();
			if (searchValueFalge) {
				pagination = IndexManager.HouseSearch(houseSearchCondition,
						houseSearchCondition.getSortMode().getSortField(), houseSearchCondition.getSortMode().isDesc(),
						(pages - 1) * row, row);
			}

			List<HouseAppSearchVo> list = new ArrayList<HouseAppSearchVo>();
			for (HouseEs houseEs : pagination.getDataList()) {
				list.add(houseEsToHouseAppSearchVo(houseEs));
			}
			long after = System.currentTimeMillis();

			LOGGER.info("Es查询用时：" + (after - before)/1000+"s");
			Integer totalCount = pagination.getTotalCount();
			returnMap.put("totalCount", totalCount);

			// 当查询结果为空时，返回热卖单品
//			List<String> listActity = goodsservice.popularGoods(0, 20);
//			List<GoodsInfoEntity> goodsList = new ArrayList<>();
//			List<String> goodsIdList = new ArrayList<>();
//			if (list.size() == 0) {
//				if (CollectionUtils.isEmpty(listActity) || listActity.size() < 20) {
//					if (CollectionUtils.isEmpty(listActity)) {
//						goodsIdList = goodsservice.getRemainderGoodsNew(0, 20);
//					} else {
//						goodsIdList = goodsservice.getRemainderGoodsNew(0, 20 - listActity.size());
//					}
//					if (CollectionUtils.isNotEmpty(goodsIdList)) {
//						// list.removeAll(goodsIdList);
//						listActity.addAll(goodsIdList);
//					}
//				}
//				goodsList = getSaleVolumeGoods(listActity);
//			}
//			returnMap.put("goodsList", goodsList);
			returnMap.put("houseDataList", list);
			return Response.successResponse(returnMap);
		} catch (Exception e) {
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
