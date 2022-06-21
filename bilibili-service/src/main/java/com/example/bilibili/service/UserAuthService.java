package com.example.bilibili.service;

import com.example.bilibili.domain.auth.AuthRoleElementOperation;
import com.example.bilibili.domain.auth.AuthRoleMenu;
import com.example.bilibili.domain.auth.UserAuthorities;
import com.example.bilibili.domain.auth.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-21 21:48
 * @Description: 用户权限服务层
 */

@Service
public class UserAuthService {

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private AuthRoleService authRoleService;

	public UserAuthorities getUserAuthorities(Long userId) {
		// 查询用户对应的角色
		List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);
		// 获取角色id
		Set<Long> roleIdSet = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
		// 获取角色可以操作的元素列表
		List<AuthRoleElementOperation> authRoleElementOperationList = authRoleService.getRoleElementOperationsByRoleIds(roleIdSet);
		// 获取角色可以操作的所有菜单
		List<AuthRoleMenu> authRoleMenuList = authRoleService.getAuthRoleMenusByRoleIds(roleIdSet);

		// 构造用户所有权限返回结果
		UserAuthorities userAuthorities = new UserAuthorities();
		userAuthorities.setRoleElementOperationList(authRoleElementOperationList);
		userAuthorities.setRoleMenuList(authRoleMenuList);
		return userAuthorities;
	}
}
