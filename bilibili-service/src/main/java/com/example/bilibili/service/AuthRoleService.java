package com.example.bilibili.service;

import com.example.bilibili.domain.auth.AuthRoleElementOperation;
import com.example.bilibili.domain.auth.AuthRoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-21 21:54
 * @Description: 查询权限角色和对应的级别
 */

@Service
public class AuthRoleService {

	@Autowired
	private AuthRoleElementOperationService authRoleElementOperationService;

	@Autowired
	private AuthRoleMenuService authRoleMenuService;

	public List<AuthRoleElementOperation> getRoleElementOperationsByRoleIds(Set<Long> roleIdSet) {
		return authRoleElementOperationService.getRoleElementOperationsByRoleIds(roleIdSet);
	}

	public List<AuthRoleMenu> getAuthRoleMenusByRoleIds(Set<Long> roleIdSet) {
		return authRoleMenuService.getAuthRoleMenusByRoleIds(roleIdSet);
	}
}
