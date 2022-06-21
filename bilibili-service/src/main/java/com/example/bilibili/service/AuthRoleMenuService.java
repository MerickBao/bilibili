package com.example.bilibili.service;

import com.example.bilibili.dao.AuthRoleMenuDao;
import com.example.bilibili.domain.auth.AuthMenu;
import com.example.bilibili.domain.auth.AuthRoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-21 22:24
 * @Description: 角色 ---> 可以操作的菜单
 */

@Service
public class AuthRoleMenuService {

	@Autowired
	private AuthRoleMenuDao authRoleMenuDao;

	public List<AuthRoleMenu> getAuthRoleMenusByRoleIds(Set<Long> roleIdSet) {
		return authRoleMenuDao.getAuthRoleMenusByRoleIds(roleIdSet);
	}
}
