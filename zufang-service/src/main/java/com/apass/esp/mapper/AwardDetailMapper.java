package com.apass.esp.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.apass.esp.domain.entity.AwardDetail;
import com.apass.esp.domain.query.ActivityBindRelStatisticQuery;
import com.apass.gfb.framework.mybatis.GenericMapper;

public interface AwardDetailMapper extends GenericMapper<AwardDetail, Long> {


        List<AwardDetail> queryAwardDetail(@Param("userId") Long userId);

        List<AwardDetail> queryAwardDetailWithDate(@Param("userId")Long userId,
            @Param("beginMonthDay")String beginMonthDay, @Param("now")String now);


	List<AwardDetail> queryAwardDetailByStatusAndType(@Param("status") byte status, @Param("type") byte type);
	
	/**
	 * 统计在某一时间段内，已放款和预计放款总金额
	 * @param query
	 * @return
	 */
	List<AwardDetail> querySumAmountGroupByTypeStatus(ActivityBindRelStatisticQuery query);

	/**
	 * 查询有提现纪录的所有用户
	 * @param paramMap
	 * @return
	 */
	List<AwardDetail> queryAwardIntroList(Map<String, Object> paramMap);
	/**
	 * 查询当前活动下该用户本月共获得多少奖励金额
	 * @param paramMap
	 * @return
	 */
	BigDecimal queryAmountAward(Map<String, Object> paramMap);
	/**
	 * 查询判断在当前活动下邀请人是否已经获得了被邀请人的奖励
	 * @param paramMap
	 * @return
	 */
	Integer isAwardSameUserId(Map<String, Object> paramMap);
	/**
	 * 查询有提现纪录的所有用户总数
	 * @param paramMap
	 * @return
	 */
	Integer countAwardIntroList(Map<String, Object> paramMap);

	BigDecimal getAllAwardByUserId(Long userId);
	/**
	 *  统计某段时间内某个活动总奖励金额（或者提取金额）
	 */
	BigDecimal getAllAwardByActivityIdAndTime(ActivityBindRelStatisticQuery query);

	AwardDetail getAllAwardByUserIdAndInviteUserId(ActivityBindRelStatisticQuery query);
}
