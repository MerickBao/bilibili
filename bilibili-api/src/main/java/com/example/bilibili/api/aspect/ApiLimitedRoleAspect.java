package com.example.bilibili.api.aspect;

import com.example.bilibili.api.support.UserSupport;
import com.example.bilibili.domain.annotation.ApiLimitedRole;
import com.example.bilibili.domain.auth.UserRole;
import com.example.bilibili.service.UserRoleService;
import com.example.bilibili.service.exception.ConditionException;
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
 * @Description: 用户权限控制切面 --> 接口控制
 */

@Order(1)
@Component
@Aspect
public class ApiLimitedRoleAspect {

	@Autowired
	private UserSupport userSupport;

	@Autowired
	private UserRoleService userRoleService;

	// 切入点：触发的时机？
	@Pointcut("@annotation(com.example.bilibili.domain.annotation.ApiLimitedRole)")
	public void check() {

	}

	// AOP流程
	@Before("check() && @annotation(apiLimitedRole)")
	public void doBefore(JoinPoint joinPoint, ApiLimitedRole apiLimitedRole) {
		Long userId = userSupport.getCurrentUserId();
		List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);
		String[] limitedRoleCodeList = apiLimitedRole.limitedRoleCodeList();
		// 需要的权限
		Set<String> limitedRoleCodeSet = Arrays.stream(limitedRoleCodeList).collect(Collectors.toSet());
		// 用户现有的权限
		Set<String> roleCodeSet = userRoleList.stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
		// 取交集：用户现有等级要高于限制的等级，即没有交集
		roleCodeSet.retainAll(limitedRoleCodeSet);
		if (roleCodeSet.size() > 0) {
			throw new ConditionException("权限不足！");
		}
	}
}
