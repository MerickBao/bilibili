package com.example.bilibili.service;

import com.example.bilibili.dao.UserRoleDao;
import com.example.bilibili.domain.auth.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-21 21:54
 * @Description: 查询用户角色关联
 */

@Service
public class UserRoleService {

	@Autowired
	private UserRoleDao userRoleDao;

	public List<UserRole> getUserRoleByUserId(Long userId) {
		return userRoleDao.getUserRoleByUserId(userId);
	}
}
