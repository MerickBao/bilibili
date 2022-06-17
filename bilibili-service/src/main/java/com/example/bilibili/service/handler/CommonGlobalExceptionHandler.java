package com.example.bilibili.service.handler;

import com.example.bilibili.domain.JsonResponse;
import com.example.bilibili.service.exception.ConditionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-15 17:28
 * @Description:
 */

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CommonGlobalExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public JsonResponse<String> commonExceptionHandler(HttpServletRequest request, Exception e) {
		String errorMsg = e.getMessage();
		if (e instanceof ConditionException) {
			// 可以获取客制化错误状态码
			String errorCode = ((ConditionException) e).getCode();
			return new JsonResponse<>(errorCode, errorMsg);
		} else {
			// 返回通用异常信息
			return new JsonResponse<>("500", errorMsg);
		}
	}
}
