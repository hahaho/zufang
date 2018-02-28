package com.apass.zufang.web.personal;

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

import com.apass.zufang.service.common.MobileSmsService;
import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.zufang.domain.Response;
/**
 * 验证码
 */
@Path("/mobile")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class MobileRandomController {
	private static final Logger logger = LoggerFactory.getLogger(MobileRandomController.class);

	/**
	 * 验证码工具
	 */
	@Autowired
	private MobileSmsService mobileRandomService;

	/**
	 * <pre>
	 * 根据用户传递的手机号码, 调用消息接口向该手机号码发送验证码
	 * 
	 * &#64;param mobile
	 * &#64;return Responses
	 * </pre>
	 */
	@POST
	@Path("/send")
	public Response sendRandomCode(Map<String, Object> paramMap) {


		String mobile = CommonUtils.getValue(paramMap, "mobile");// 微信号
		String smsType = CommonUtils.getValue(paramMap, "smsType");// 微信号
		logger.info("入参 mobile"+mobile+"smsType"+smsType);
		
		if (StringUtils.isAnyBlank(mobile, smsType)) {
			return Response.fail("验证码接收手机号不能为空");
		}
		try {
			mobileRandomService.sendMobileVerificationCode(smsType, mobile);
			return Response.success("验证码发送成功,请注意查收");
		} catch (BusinessException e) {
			logger.error("mobile verification code send fail", e);
			return Response.fail("网络异常,发送验证码失败,请稍后再试");
		}
	}

	/**
	 * <pre>
	 * 根据输入的手机号码&验证码进行校验用户填写的验证码是否正确
	 * &#64;param mobile
	 * &#64;param randomCode
	 * </pre>
	 */
	@POST
	@Path("/validate")
	public Response validateRandomCode(Map<String, Object> paramMap) {
		
		String smsType = CommonUtils.getValue(paramMap, "smsType");// 微信号
		String mobile = CommonUtils.getValue(paramMap, "mobile");// 微信号
		String code = CommonUtils.getValue(paramMap, "code");// 微信号
		logger.info("入参 mobile"+mobile+"smsType"+smsType);
		
		if (StringUtils.isAnyBlank(code, smsType, mobile)) {
			return Response.fail("手机号或验证码输入不能为空");
		}
		try {
			boolean result = mobileRandomService.mobileCodeValidate(smsType, mobile, code);
			String message = result ? "验证码验证成功" : "验证码验证失败";
			return Response.success(message, result);
		} catch (Exception e) {
			logger.error("mobile verification code send fail", e);
			return Response.fail("验证码验证失败");
		}
	}
}
