package com.apass.esp.service;
import com.apass.esp.domain.entity.bill.PurchaseReturnOrder;
import com.apass.esp.domain.entity.ApassTxnAttr;
import com.apass.esp.domain.entity.CashRefund;
import com.apass.esp.domain.entity.bill.PurchaseOrderDetail;
import com.apass.esp.domain.entity.bill.TxnInfoEntity;
import com.apass.esp.domain.entity.bill.TxnOrderInfo;
import com.apass.esp.domain.enums.CashRefundStatus;
import com.apass.esp.domain.enums.TxnTypeCode;
import com.apass.esp.mapper.ApassTxnAttrMapper;
import com.apass.esp.mapper.TxnInfoMapper;
import com.apass.esp.service.refund.CashRefundService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by jie.xu on 17/7/13.
 */
@Service
public class TxnInfoService {

  @Autowired
  private TxnInfoMapper txnInfoMapper;

  @Autowired
  private CashRefundService cashRefundService;

  @Autowired
  private ApassTxnAttrMapper apassTxnAttrMapper;

  /**
   * 判断用户某笔订单是否进行了未出账主动还款
   * true:是
   */
  public boolean isActiveRepayForConsumableCredit(Long userId, String mainOrderId) {
      
    TxnInfoEntity txn = txnInfoMapper.selectLatestTxnByUserId(userId, TxnTypeCode.REPAY_CODE.getCode());
    if (txn == null) {
      return false;
    } else {
      List<CashRefund> cashRefunds = cashRefundService.getCashRefundByMainOrderId(mainOrderId, CashRefundStatus.CASHREFUND_STATUS3);
      //如果有撤销退款的申请，按照撤销退款的创建时间比较
      if (CollectionUtils.isNotEmpty(cashRefunds)) {
        Date createD = cashRefunds.get(0).getCreateDate();
        if (createD != null && txn.getTxnDate() != null && txn.getTxnDate().getTime() >= createD.getTime()) {
          return true;
        } else {
          return false;
        }
      } else {
        TxnInfoEntity creditTxn = txnInfoMapper.queryOrigTxnIdByOrderidAndstatus(mainOrderId, TxnTypeCode.XYZF_CODE.getCode());
        if (creditTxn == null) {
          return false;
        }
        //还款时间大于交易时间 则表示主动还款了
        if (txn.getTxnDate() != null && creditTxn.getTxnDate() != null && txn.getTxnDate().getTime() >= creditTxn.getTxnDate().getTime()) {
          return true;
        } else {
          return false;
        }
      }
    }
  }

  public List<TxnInfoEntity> getByMainOrderId(String mainOrderId){
    List<TxnInfoEntity> txnlinfoList=txnInfoMapper.selectByOrderId(mainOrderId);
    return txnlinfoList;
  }

  public List<TxnOrderInfo> selectByOrderStatusList(List<String> orderStatusArray,
                                             String dateBegin,
                                              String dateEnd){
    return txnInfoMapper.selectByOrderStatusList(orderStatusArray,dateBegin,dateEnd);
  }

  public ApassTxnAttr getApassTxnAttrByTxnId(Long txnId) {
    return apassTxnAttrMapper.getApassTxnAttrByTxnId(txnId);
  }
    /**
     * 采购订单明细
     * @param orderStatusList
     * @param dateBegin
     * @param dateEnd
     * @return
     */
    public List<PurchaseOrderDetail> selectPurchaseOrderList(List<String> orderStatusList, String dateBegin, String dateEnd) {
    	return txnInfoMapper.selectPurchaseOrderList(orderStatusList,dateBegin,dateEnd);
    }
    /**
     * 采购订单（采购和退货）
     * @param orderStatus
     * @param returnStatus
     * @param returnType
     * @param dateBegin
     * @param dateEnd
     * @return
     */
    public List<PurchaseReturnOrder> selectPurchaseReturnSalesList(List<String> orderStatus, List<String> returnStatus, List<String> returnType, String dateBegin, String dateEnd) {
        return txnInfoMapper.selectPurchaseReturnSalesList(orderStatus,returnStatus,returnType,dateBegin,dateEnd);
    }
    /**
     * VBS业务号对应 
     * @param orderStatusList
     * @param dateBegin
     * @param dateEnd
     * @return
     */
    public List<TxnOrderInfo> selectVBSBusinessNumList( String dateBegin, String dateEnd) {
        return txnInfoMapper.selectVBSBusinessNumList(dateBegin,dateEnd);
    }

    public List<TxnInfoEntity> selectRepayTxnByUserId(Long userId,String startDate,String endDate){
      return txnInfoMapper.queryRepayTxnByUserId(userId,startDate,endDate);
    }

    public Integer getTotalCreditPayNum(Long userId){
      return txnInfoMapper.getTotalCreditPayNum(userId);
    }

  public List<TxnOrderInfo> selectByTxnTypeCodeList(List<String> typeCodeList, String dateBegin, String dateEnd) {
    return txnInfoMapper.selectByTxnTypeCodeList(typeCodeList, dateBegin, dateEnd);
  }
}
