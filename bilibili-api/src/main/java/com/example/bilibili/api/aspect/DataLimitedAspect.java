package com.example.bilibili.api.aspect;

import com.example.bilibili.api.support.UserSupport;
import com.example.bilibili.domain.UserMoment;
import com.example.bilibili.domain.annotation.ApiLimitedRole;
import com.example.bilibili.domain.auth.UserRole;
import com.example.bilibili.domain.constant.AuthRoleConstant;
import com.example.bilibili.service.UserRoleService;
import com.example.bilibili.service.exception.ConditionException;
import org.apache.ibatis.ognl.security.UserMethod;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-22 16:06
 * @Description: 用户权限控制切面 --> 数据操纵
 */

@Order(1)
@Component
@Aspect
public class DataLimitedAspect {

	@Autowired
	private UserSupport userSupport;

	@Autowired
	private UserRoleService userRoleService;

	// 切入点：触发的时机？
	@Pointcut("@annotation(com.example.bilibili.domain.annotation.DataLimited)")
	public void check() {

	}

	// AOP流程
	@Before("check()")
	public void doBefore(JoinPoint joinPoint) {
		Long userId = userSupport.getCurrentUserId();
		List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);
		// 用户现有的权限
		Set<String> roleCodeSet = userRoleList.stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
		Object[] args = joinPoint.getArgs();
		for (Object arg : args) {
			if (arg instanceof UserMoment) {
				UserMoment userMoment = (UserMoment) arg;
				String type = userMoment.getType();
				if (roleCodeSet.contains(AuthRoleConstant.ROLE_LV0) && !"0".equals(type)) {
					throw new ConditionException("参数异常！");
				}
			}
		}
	}
}
