package com.apass.esp.repository.order;

import com.apass.esp.domain.dto.aftersale.IdNum;
import com.apass.esp.domain.entity.bill.SalesOrderPassOrRefund;
import com.apass.esp.domain.entity.order.OrderInfoEntity;
import com.apass.esp.domain.enums.OrderStatus;
import com.apass.esp.domain.vo.CheckAccountOrderDetail;
import com.apass.gfb.framework.annotation.MyBatisRepository;
import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.mybatis.support.BaseMybatisRepository;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MyBatisRepository
public class OrderInfoRepository extends BaseMybatisRepository<OrderInfoEntity, Long> {
    /**
     * 根据订单列表查询订单概要信息列表
     * 
     * @throws BusinessException
     */
    public List<OrderInfoEntity> selectByOrderIdList(Map<String, Object> paramMap) throws BusinessException {
        try {
            List<OrderInfoEntity> oiList = getSqlSession().selectList(getSQL("selectByOrderIdList"), paramMap);
            return oiList;
        } catch (Exception e) {
            throw new BusinessException("根据订单列表查询订单概要信息列表失败", e);
        }
    }
    
    /**
     * 查询订单概要信息列表
     * 
     * @throws BusinessException
     */
    public List<OrderInfoEntity> queryOrderInfoList(Map<String, Object> paramMap) throws BusinessException {
        try {
            List<OrderInfoEntity> oiList = getSqlSession().selectList(getSQL("queryOrderInfoList"), paramMap);
            return oiList;
        } catch (Exception e) {
            throw new BusinessException("查询用户的消息列表失败", e);
        }
    }

    /**
     * 根据mainOrderId查询订单
     * 
     * @param mainOrderId
     * @return
     * @throws BusinessException
     */
    public List<OrderInfoEntity> selectByMainOrderId(String mainOrderId) throws BusinessException {
        return getSqlSession().selectList(getSQL("selectByMainOrderId"), mainOrderId);
    }
    
    public OrderInfoEntity selectByOrderIdAndUserId(String orderId, Long userId) {
        HashMap<Object, Object> param = new HashMap<>();
        param.put("orderId", orderId);
        param.put("userId", userId);
        return getSqlSession().selectOne("selectByOrderIdAndUserId", param);
    }
    
    public OrderInfoEntity selectByOrderId(String orderId) {
        HashMap<Object, Object> param = new HashMap<>();
        param.put("orderId", orderId);
        return getSqlSession().selectOne("selectByOrderIdAndUserId", param);
    }

    /**
     * 更新订单状态
     * 
     * @param orderId
     */
    public void updateStatusByOrderId(String orderId, String status) {
        OrderInfoEntity param = new OrderInfoEntity();
        param.setOrderId(orderId);
        param.setStatus(status);
        this.getSqlSession().update("updateStatusByOrderId", param);
    }
    
    /**
     * 更新订单状态
     * 
     * @param orderId
     */
    public void updateIsDeleteByOrderId(String orderId) {
        OrderInfoEntity param = new OrderInfoEntity();
        param.setOrderId(orderId);
        param.setUpdateDate(new Date());
        this.getSqlSession().update("updateIsDeleteByOrderId", param);
    }
    /**
     * 根据mainOrderId更新订单信息
     * 
     * @param param
     */
    public void updateByMainOrderId(OrderInfoEntity param) {
        this.getSqlSession().update("updateByMainOrderId", param);
    }
    
    /**
    * 获取用户 待付款、待发货、待收货 子订单数量
    * 
    * @param userId
    * @return
    */
    public List<IdNum> getOrderNum(Long userId) {
        return this.getSqlSession().selectList("getOrderNum", userId);
    }

    /**
     * 查询未支付订单
     * 
     * @return
     */
    public List<OrderInfoEntity> loadNoPayList() {
        return this.getSqlSession().selectList("loadNoPayList");
    }

    /**
     * 查询所有待签收订单
     * 
     * @return
     */
    public List<OrderInfoEntity> loadNoSignOrders() {
        return this.getSqlSession().selectList("loadNoSignOrders");
    }

    /**
     * 根据物流Id标识查询
     * 
     * @param logisticsId
     * @return
     */
    public OrderInfoEntity loadBylogisticsId(String logisticsId) {
        return getSqlSession().selectOne("loadBylogisticsId", logisticsId);
    }

    /**
     * 查询售后完成且超过3天的订单
     * @return
     */
    public List<OrderInfoEntity> loadCompleteOrderGtThreeDay() {
        return this.getSqlSession().selectList("loadCompleteOrderGtThreeDay");
    }

    /**
     * 最新赊购记录
     *
     * @param userId
     * @return
     */
    public OrderInfoEntity queryLatestSuccessOrderInfo(Long userId) {
        return getSqlSession().selectOne(getSQL("queryLatestSuccessOrderInfo"), userId);
    }
    
    /**
     * 查询待发货订单的信息，切订单的预发货状态为''
     */
    public List<OrderInfoEntity> toBeDeliver() {
    	return this.getSqlSession().selectList("toBeDeliver");
    }
    
    public void updateOrderStatusAndPreDelivery(OrderInfoEntity entity){
    	this.getSqlSession().update("updateOrderStatusAndPreDelivery",entity);
    }

    public void updateOrderStatus(OrderInfoEntity entity){
        this.getSqlSession().update("updateOrderStatus",entity);
    }

    /**
     * 根据日期和状态查询订单条数
     * @param orderStatus
     * @param dateBegin
     * @param dateEnd
     * @return
     */
    public  Integer selectOrderCountByStatus(String orderStatus,String dateBegin,String dateEnd){
        HashMap<Object, Object> param = new HashMap<>();
        param.put("orderStatus", orderStatus);
        param.put("dateBegin", dateBegin);
        param.put("dateEnd", dateEnd);
        return getSqlSession().selectOne("selectOrderCountByStatus", param);
    }

    public  Double selectOrderAmtByStatus(String orderStatus,String dateBegin,String dateEnd){
        HashMap<Object, Object> param = new HashMap<>();
        param.put("orderStatus", orderStatus);
        param.put("dateBegin", dateBegin);
        param.put("dateEnd", dateEnd);
        return getSqlSession().selectOne("selectOrderAmtByStatus", param);
    }

    public  List<OrderInfoEntity> selectOrderByStatus(String orderStatus,String dateBegin,String dateEnd){
        HashMap<Object, Object> param = new HashMap<>();
        param.put("orderStatus", orderStatus);
        param.put("dateBegin", dateBegin);
        param.put("dateEnd", dateEnd);
        return getSqlSession().selectList("selectOrderByStatus", param);
    }


    public  List<OrderInfoEntity> selectByStatusList(List<String> statusArray,String dateBegin,String dateEnd){
        HashMap<Object, Object> param = new HashMap<>();
        param.put("statusArray", statusArray);
        param.put("dateBegin", dateBegin);
        param.put("dateEnd", dateEnd);
        return getSqlSession().selectList("selectByStatusList", param);
    }

    public  Integer  selectSumAmt(String dateBegin,String dateEnd){
        HashMap<Object, Object> param = new HashMap<>();
        param.put("dateBegin", dateBegin);
        param.put("dateEnd", dateEnd);
        return getSqlSession().selectOne("selectSumAmt", param);
    }
    public  Integer  selectAliAmt(String dateBegin,String dateEnd){
        HashMap<Object, Object> param = new HashMap<>();
        param.put("dateBegin", dateBegin);
        param.put("dateEnd", dateEnd);
        return getSqlSession().selectOne("selectAliAmt", param);
    }


    public  Integer selectCreAmt(String dateBegin,String dateEnd){
        HashMap<Object, Object> param = new HashMap<>();
        param.put("dateBegin", dateBegin);
        param.put("dateEnd", dateEnd);
        return getSqlSession().selectOne("selectCreAmt", param);
    }

    public List<String> initGoodsSaleVolume(){
        return this.getSqlSession().selectList("initGoodsSaleVolume");
    }
    
    /**
     * 获取失效和删除的预占库存的京东订单
     * @return
     */
    public List<OrderInfoEntity> getInvalidAndDeleteJdOrder() {
    	return this.getSqlSession().selectList("getInvalidAndDeleteJdOrder");
    }
    
    /**
     * 更新订单的是否预占库存状态
     * 
     * @throws BusinessException
     */
    public void updatePreStockStatusByOrderId(Map<String, Object> map) throws BusinessException {
        updateBymap(map, getSQL("updatePreStockStatusByOrderId"));
    }

    /**
     * 查询预占库存 代发货的订单
     * @return
     */
    public List<OrderInfoEntity> getOrderByOrderStatusAndPreStatus(String souceType){
        return getSqlSession().selectList("getOrderByOrderStatusAndPreStatus",souceType);
    }

    /**
     * 根据京东订单号得到订单
     * @param extOrderId
     * @return
     */
    public OrderInfoEntity getOrderInfoByExtOrderId(String extOrderId){
        return getSqlSession().selectOne("getOrderInfoByExtOrderId",extOrderId);
    }


    public void updateOrderStatusByExtOrderId(OrderInfoEntity entity){
        this.getSqlSession().update("updateOrderStatusByExtOrderId",entity);
    }

    /**
     * 根据状态获取所有京东的订单
     * @param orderStatus
     * @return
     */
    public List<OrderInfoEntity>  getJdOrderByOrderStatus(String orderStatus){
        return getSqlSession().selectList("getJdOrderByOrderStatus",orderStatus);
    }

    /**
     * 下单买家数
     * @param beginDate
     * @param endDate 
     * @return
     */
    public Integer getConfirmOrderCount(Date beginDate, Date endDate) {
        HashMap<Object, Object> param = new HashMap<>();
        param.put("beginDate", beginDate);
        param.put("endDate", endDate);
        return getSqlSession().selectOne("getConfirmOrderCount",param);
    }

    /**
     * 支付买家数
     * @param beginDate
     * @param endDate
     * @return
     */
    public Integer getConfirmPayCount(Date beginDate, Date endDate) {
        HashMap<Object, Object> param = new HashMap<>();
        param.put("beginDate", beginDate);
        param.put("endDate", endDate);
        return getSqlSession().selectOne("getConfirmPayCount",param);
    }

    /**
     * 获取所有下单金额
     * @param beginDate
     * @param endDate
     * @return
     */
    public BigDecimal getSumOrderamt(Date beginDate, Date endDate) {
        HashMap<Object, Object> param = new HashMap<>();
        param.put("beginDate", beginDate);
        param.put("endDate", endDate);
        return getSqlSession().selectOne("getSumOrderamt",param);
    }

    /**
     * 获取所有支付成功金额
     * @param beginDate
     * @param endDate
     * @return
     */
    public BigDecimal getSumOrderamtForPaySuccess(Date beginDate, Date endDate) {
        HashMap<Object, Object> param = new HashMap<>();
        param.put("beginDate", beginDate);
        param.put("endDate", endDate);
        return getSqlSession().selectOne("getSumOrderamtForPaySuccess",param);
    }
    
    public List<OrderInfoEntity> getSectionOrderList(String beginDate, String endDate) {
        HashMap<Object, Object> param = new HashMap<>();
        param.put("beginDate", beginDate);
        param.put("endDate", endDate);
        return getSqlSession().selectList("getSectionOrderList",param);
    }
    public List<SalesOrderPassOrRefund> selectSalesOrderStatusList(List<String> statusArray, String dateBegin, String dateEnd) {
        Map<String,Object> paramMap = Maps.newHashMap();
        paramMap.put("statusArray",statusArray);
        paramMap.put("dateBegin",dateBegin);
        paramMap.put("dateEnd",dateEnd);
        return getSqlSession().selectList(getSQL("salesOrderPassOrRefund"),paramMap);
    }
    public List<CheckAccountOrderDetail> getCheckOrderDetail(String beginDate) {
        HashMap<Object, Object> param = new HashMap<>();
        param.put("beginDate", beginDate);
        return getSqlSession().selectList("getCheckOrderDetail",param);
    }

    public List<OrderInfoEntity> selectByCouponId(Long couponId){
        HashMap<Object, Object> param = new HashMap<>();
        param.put("couponId", couponId);
        return getSqlSession().selectList("selectByCouponId",param);
    }
    
    public List<OrderInfoEntity> selectByParentOrderId(String parentOrderId){
    	 return getSqlSession().selectList(getSQL("selectByParentOrderId"), parentOrderId);
    }
    /**
     * findHandSignOrder 查询订单交易完成 7 天后的订单
     * @return
     */
    public List<OrderInfoEntity> findCompelateOrder() {
        return getSqlSession().selectList("findCompelateOrder");
    }
    /**
     * 查询该订单的子订单  根据mainorderId查询 不包括本订单自身
     * @param mainOrderId
     * @return
     */
    public List<OrderInfoEntity> findOrderByMainOrderId(String mainOrderId) {
        return getSqlSession().selectList(getSQL("findOrderByMainOrderId"), mainOrderId);
    }

    /**
     * 根据userId查询状态为订单完成的订单
     * @param inviteUserId
     * @return
     */
    public List<OrderInfoEntity> selectByUserId(Long inviteUserId) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("userId",String.valueOf(inviteUserId));
        param.put("orderstatus", OrderStatus.ORDER_COMPLETED.getCode());

        return getSqlSession().selectList(getSQL("selectByUserId"), param);
    }

    public List<CheckAccountOrderDetail> selectOrderDetailsByOrderIds(Map<String, Object> paramMap) {
        return getSqlSession().selectList(getSQL("selectOrderDetailsByOrderIds"), paramMap);
    }
}