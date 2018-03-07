package com.apass.zufang.search.manager;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.apass.zufang.search.condition.HouseSearchCondition;
import com.apass.zufang.search.entity.HouseEs;
import com.apass.zufang.search.utils.Pinyin4jUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apass.zufang.search.entity.IdAble;
import com.apass.zufang.search.enums.IndexType;
import com.apass.zufang.search.utils.ESDataUtil;
import com.apass.zufang.search.utils.Esprop;
import com.apass.zufang.search.utils.PropertiesUtils;
import com.apass.gfb.framework.mybatis.page.Pagination;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Created by xianzhi.wang on 2017/5/22.
 */

public class IndexManager<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexManager.class);

    private static Esprop esprop = new Esprop();

    static {
        InputStream esIn = ESClientManager.class.getClassLoader().getResourceAsStream("esmapper/es.properties");
        try {
            Properties properties = PropertiesUtils.readFromText(IOUtils.toString(esIn).trim());
            esprop.setIndice(properties.getProperty("esIndice"));
        } catch (IOException e) {
            LOGGER.error("get es config error ...");
        } finally {
            IOUtils.closeQuietly(esIn);
        }
    }

    /**
     * 组合条件查询
     * @param sortField 排序字段
     * @param desc 是否降序
     * @param from 从哪里开始
     * @param size 一共搜索多少条
     * @param value 查询内容
     * @param <T>
     * @return
     */
    private static <T> Pagination<T> boolSearch(String sortField, boolean desc, int from, int size, String value) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder
                .should(QueryBuilders.wildcardQuery("apartmentName", "*" + value + "*").boost(1.5f))
                .should(QueryBuilders.wildcardQuery("apartmentNamePinyin", "*" + value + "*").boost(1.5f))
                .should(QueryBuilders.wildcardQuery("communityName", "*" + value + "*").boost(1.5f))
                .should(QueryBuilders.wildcardQuery("communityNamePinyin", "*" + value + "*").boost(1.5f))
                .should(QueryBuilders.wildcardQuery("houseTitle", "*" + value + "*").boost(2f))
                .should(QueryBuilders.wildcardQuery("houseTitlePinyin", "*" + value + "*").boost(2f))
                .should(QueryBuilders.wildcardQuery("detailAddr", "*" + value + "*").boost(1f))
                .should(QueryBuilders.wildcardQuery("detailAddrPinyin", "*" + value + "*").boost(1f))
                .should(QueryBuilders.queryStringQuery(value).field("communityName", 1.5f)
                        .field("communityNamePinyin", 1.5f)
                        .field("apartmentName", 1.5f)
                        .field("apartmentNamePinyin", 1.5f)
                        .field("houseTitle", 2f)
                        .field("houseTitlePinyin", 2f)
                        .field("detailAddr", 1f)
                        .field("detailAddrPinyin", 1f))
                .should(QueryBuilders.termQuery("apartmentName", value).boost(1.5f))
                .should(QueryBuilders.termQuery("apartmentNamePinyin", value).boost(1.5f))
                .should(QueryBuilders.termQuery("communityName", value).boost(1.5f))
                .should(QueryBuilders.termQuery("communityNamePinyin", value).boost(1.5f))
                .should(QueryBuilders.termQuery("houseTitle", value).boost(2f))
                .should(QueryBuilders.termQuery("houseTitlePinyin", value).boost(2f))
                .should(QueryBuilders.termQuery("detailAddr", value).boost(1f))
                .should(QueryBuilders.termQuery("detailAddrPinyin", value).boost(1f));
        return search(boolQueryBuilder, IndexType.HOUSE, desc, from, size, sortField);
    }

    /**
     * 获取ES house索引下document总数
     * @return
     */
    public static long getTotalCount(){
        return ESClientManager.getClient().prepareSearch(esprop.getIndice())
                .setTypes(IndexType.HOUSE.getDataName())
                .setSize(0)
                .setQuery(QueryBuilders.matchAllQuery()).get().getHits().getTotalHits();
    }

    /**
     * 获取包含某个搜索关键字的所有document总数
     * @param value
     * @return
     */
    public static long getTotalCount(String value){
        return ESClientManager.getClient().prepareSearch(esprop.getIndice())
                .setTypes(IndexType.HOUSE.getDataName())
                .setSize(0)
                .setQuery(QueryBuilders.queryStringQuery(value)).get().getHits().getTotalHits();
    }



    /**
     * 更新索引，如果新增的时候index存在，就是更新操作
     *
     * @param index
     * @param type
     * @param data
     * @throws UnknownHostException
     * @throws JsonProcessingException
     */
    public static <T extends IdAble> void updateDocument(String index, IndexType type, T data) {
        addDocument(index, type, data);
    }

    /**
     * 批量创建索引：先delete再create
     *
     * @param datas
     * @param indexType
     */
    public static <T extends IdAble> void createIndex(List<T> datas, IndexType indexType) {
        // 批量处理request
        BulkRequestBuilder bulkRequest = ESClientManager.getClient().prepareBulk();
        byte[] json;
        for (T t : datas) {
            json = ESDataUtil.toBytes(t);
            bulkRequest.add(new DeleteRequest(esprop.getIndice(),indexType.getDataName(),t.getId() + ""));
            bulkRequest.add(new IndexRequest(esprop.getIndice(), indexType.getDataName(), t.getId() + "").source(json));
        }


        /**
         * 执行批量处理request
         */
        BulkResponse bulkResponse = bulkRequest.get();
        // 处理错误信息
        if (bulkResponse.hasFailures()) {
            LOGGER.warn("====================批量创建索引过程中出现错误 下面是错误信息==========================");
            long count = 0L;
            for (BulkItemResponse bulkItemResponse : bulkResponse) {
                LOGGER.warn("类型 " + indexType.getDataName() + " 发生错误的 索引id为 : " + bulkItemResponse.getId() + " ，错误信息为：" + bulkItemResponse.getFailureMessage());
                count++;
            }
            LOGGER.warn("====================批量创建索引过程中出现错误 上面是错误信息 共有: " + count + " 条记录==========================");
        }
    }

    /**
     * 添加索引:单个添加索引包括document数据
     *
     * @param index
     * @param type
     * @param data
     */
    public static <T extends IdAble> void addDocument(String index, IndexType type, T data) {
        byte[] json = ESDataUtil.toBytes(data);
        ESClientManager.getClient().prepareIndex(index, type.getDataName(), String.valueOf(data.getId())).setSource(json).get();
    }

    /**
     * 删除索引
     *
     * @param index
     * @param type
     * @param id
     */
    public static void deleteDocument(String index, IndexType type, Integer id) {
        ESClientManager.getClient().prepareDelete(index, type.getDataName(), String.valueOf(id)).get();
    }

    /**
     * 根据index,type,id获取document
     * @param index:索引
     * @param type:类型
     * @param id:id
     * @return
     */
    public static <T> T getDocument(String index, IndexType type, Integer id) {
        GetResponse getResponse = ESClientManager.getClient().prepareGet(index, type.getDataName(), String.valueOf(id)).get();
        if (getResponse != null && getResponse.isExists()) {
            Object value = ESDataUtil.readValue(getResponse.getSourceAsBytes(), type.getTypeClass());
            return (T) value;
        }
        return null;
    }


    /**
     * 根据条件分页查询，且按照指定字段排序
     *
     * @param queryBuilder 查询字段
     * @param type 类型
     * @param desc 排序方式
     * @param from 分页起始偏移量
     * @param size 页面大小
     * @return
     */
    private static <T> Pagination<T> search(QueryBuilder queryBuilder, IndexType type, boolean desc, int from, int size, String ...sortFields) {
        List<T> results = new ArrayList<>();
        /**
         * 不同的索引、变量 代码通用
         */
        SearchRequestBuilder serachBuilder = ESClientManager.getClient().prepareSearch(esprop.getIndice())
                .setTypes(type.getDataName())
                .setQuery(queryBuilder);
        if (sortFields != null) {
            for (int i = 0; i <sortFields.length ; i++) {
                serachBuilder.addSort(sortFields[i], desc ? SortOrder.DESC : SortOrder.ASC);
            }
        }
        serachBuilder.addSort("_score", SortOrder.DESC);
        if (0 != size) {
            serachBuilder.setFrom(from).setSize(size);
        }

        SearchResponse response = serachBuilder.execute().actionGet();
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
			results.add((T) ESDataUtil.readValue(hit.source(), type.getTypeClass()));
		}
        int total = (int) searchHits.getTotalHits();
        Pagination pagination = new Pagination();
        pagination.setDataList(results);
        pagination.setTotalCount(total);
        return pagination;
    }

    /**
     * 根据条件查询ES中的消息
     */
	public static <HouseEs> List<HouseEs> goodSearchFromES(QueryBuilder queryBuilder) {
		List<HouseEs> results = new ArrayList<>();
        /**
         * 不同的索引  变量代   码通用
         */
		SearchRequestBuilder serachBuilder = ESClientManager.getClient().prepareSearch(esprop.getIndice())
							.setTypes(IndexType.HOUSE.getDataName())
							.setQuery(queryBuilder);
		serachBuilder.addSort("_score", SortOrder.DESC);
		SearchResponse response = serachBuilder.execute().actionGet();
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();
		for (SearchHit hit : hits) {
			results.add((HouseEs) ESDataUtil.readValue(hit.source(), IndexType.HOUSE.getTypeClass()));
		}
        return results;
	}

    public static Pagination<HouseEs> HouseSearch(HouseSearchCondition condition) {
        String sortField = condition.getSortMode().getSortField();
        boolean desc = condition.getSortMode().isDesc();
        int from = condition.getOffset();
        int size = condition.getPageSize();

        String value = condition.getHouseTitle();

        //如果是汉字走此流程
        if (Pinyin4jUtil.isContainChinese(value)||Pinyin4jUtil.isContainSpecial(value)) {
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(value,
                    "communityName","houseTitle", "detailAddr","apartmentName").operator(Operator.AND);
            multiMatchQueryBuilder.field("communityName", 1.5f);
            multiMatchQueryBuilder.field("houseTitle", 2f);
            multiMatchQueryBuilder.field("detailAddr", 1f);
            multiMatchQueryBuilder.field("apartmentName", 1f);

            //TODO
            Pagination<HouseEs> housePagination =
                    search(multiMatchQueryBuilder, IndexType.HOUSE, desc, from, size, sortField);
            if (!CollectionUtils.isEmpty(housePagination.getDataList())) {
                return housePagination;
            }
        }
        return boolSearch(sortField, desc, from, size, StringUtils.lowerCase(value));
    }


}
