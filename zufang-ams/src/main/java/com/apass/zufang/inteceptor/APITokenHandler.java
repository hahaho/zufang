package com.apass.zufang.inteceptor;

import com.apass.gfb.framework.jwt.core.JsonTokenHelper;
import com.apass.gfb.framework.jwt.domains.TokenInfo;
import com.apass.gfb.framework.utils.GsonUtils;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 提供第三方接口调用拦截
 */

@Aspect
@Component
@Order(value=Ordered.HIGHEST_PRECEDENCE + 100)
public class APITokenHandler {
	/**
	 * 日志
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(APITokenHandler.class);

	@Autowired
	private JsonTokenHelper jsonTokenHelper;
	//token失效
	private static final String EXPIRE_CODE = "-1";

	/**
	 * 拦截方法 - token校验
	 *
	 * @param point
	 * @return Object
	 * @throws Throwable
	 */
    @Around("execution(* com.apass.zufang.common.ZufangButtonJoinColler.createHouse(..))")
	private Object handleTokenInteceptor(ProceedingJoinPoint point)
			throws Throwable {
		// //取得class类名的方式
		Signature signature = point.getSignature();
		Class<?> returnType = ((MethodSignature) signature).getReturnType(); 
		Object[] arr = point.getArgs();
		boolean isJsonToken = false;
		if (arr == null || arr.length == 0) {
			Map<String, Object> resultMap = Maps.newHashMap();
			resultMap.put("msg", "无效的token");
			resultMap.put("status", EXPIRE_CODE);
			return GsonUtils.convertObj(resultMap, returnType);
		}

		Object[] newPara = new Object[arr.length];
		for (int i = 0; i < arr.length; i++) {
			if (!(arr[i] instanceof Map)) {
				newPara[i] = arr[i];
				continue;
			}
			Map<String, Object> paraMap = (Map<String, Object>) arr[i];
			// 参数token
			String token = (String) paraMap.get("token");
			if (StringUtils.isBlank(token)) {
				continue;
			}
			try {
				// 解密token，转化成具体数据
				TokenInfo tokenInfo = jsonTokenHelper.verifyToken(token);
				if (!tokenInfo.isExpire()) {
					isJsonToken = true;
				} else {
					Map<String, Object> resultMap = Maps.newHashMap();
					resultMap.put("msg", "无效的token");
					resultMap.put("status", EXPIRE_CODE);
					return GsonUtils.convertObj(resultMap, returnType);
				}
				paraMap.put("userId",tokenInfo.getUserId());
			} catch (Exception e) {
				LOGGER.error("token验证异常-------->", e);
				Map<String, Object> resultMap = Maps.newHashMap();
				resultMap.put("msg", "无效的token");
				resultMap.put("status", EXPIRE_CODE);
				return GsonUtils.convertObj(resultMap, returnType);
			}
		}
		
		if (!isJsonToken) {
			// 无效jsontoken
			Map<String, Object> resultMap = Maps.newHashMap();
			resultMap.put("msg", "无效的token");
			resultMap.put("status", EXPIRE_CODE);
			return GsonUtils.convertObj(resultMap, returnType);
		}

		return point.proceed(point.getArgs());

	}

}
