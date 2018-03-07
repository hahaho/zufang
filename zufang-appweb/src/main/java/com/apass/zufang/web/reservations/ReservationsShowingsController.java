/*package com.apass.zufang.web.reservations;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.apass.gfb.framework.mybatis.page.Page;
import com.apass.gfb.framework.mybatis.page.Pagination;
import com.apass.gfb.framework.utils.BaseConstants;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.zufang.domain.entity.ReserveHouse;
import com.apass.zufang.service.reservations.ReservationsShowingsService;
import com.apass.zufang.utils.PaginationManage;
import com.apass.zufang.utils.ResponsePageBody;
import com.apass.zufang.web.onlinebooking.OnlineBookingController;

@Path("/reservations")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ReservationsShowingsController {
	
	private static final Logger logger = LoggerFactory.getLogger(OnlineBookingController.class);
	
	@Autowired
	private ReservationsShowingsService reservationsShowingsService;

			*//**
			 * 预约看房Reservations
			 * @param paramMap
			 * @return
			 *//*
			@POST
			@Path("/reservationsshowings")
			public ResponsePageBody<ReserveHouse> reservationsShowings(Map<String, Object> paramMap) {
				ResponsePageBody<ReserveHouse> respBody = new ResponsePageBody<ReserveHouse>();
				try {
					// 页码
					String page = CommonUtils.getValue(paramMap, "page");
					// 每页显示条数
					String rows = CommonUtils.getValue(paramMap, "rows");
					rows = StringUtils.isNotBlank(rows) ? rows: "20";
		        	page = StringUtils.isNotBlank(page) ? page: "1";
					
		            String userid = CommonUtils.getValue(paramMap, "userid");//用户id
		            String telphone = CommonUtils.getValue(paramMap, "telphone");//电话
		            
		            ReserveHouse crmety = new ReserveHouse();
		            if (null != telphone && !telphone.trim().isEmpty()) {
		            	crmety.setTelphone(telphone);
		            }
		            if (null != userid && !userid.trim().isEmpty()) {
		            	crmety.setUserId(userid);
		            }
		            crmety.setRows(Integer.parseInt(rows));
		            crmety.setPage(Integer.parseInt(page));
		            
		
		            ResponsePageBody<ReserveHouse> resultPage = reservationsShowingsService.queryReservations(crmety);
					
		            if (resultPage == null) {
		                respBody.setTotal(0);
		                respBody.setStatus(BaseConstants.CommonCode.SUCCESS_CODE);
		                return respBody;
		            }
		            respBody.setTotal(resultPage.getTotal());
		            respBody.setRows(resultPage.getRows());
		            respBody.setStatus(BaseConstants.CommonCode.SUCCESS_CODE);
				} catch (Exception e) {
					logger.error("预约看房查询失败", e);
			            respBody.setStatus(BaseConstants.CommonCode.FAILED_CODE);
			            respBody.setMsg("预约看房查询失败");
				}
				return respBody;
			}
}
*/