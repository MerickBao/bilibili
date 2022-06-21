package com.example.bilibili.service;

import com.example.bilibili.dao.AuthRoleElementOperationDao;
import com.example.bilibili.domain.auth.AuthRoleElementOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-21 22:23
 * @Description: 角色 ---> 页面元素
 */

@Service
public class AuthRoleElementOperationService {

	@Autowired
	private AuthRoleElementOperationDao authRoleElementOperationDao;

	public List<AuthRoleElementOperation> getRoleElementOperationsByRoleIds(Set<Long> roleIdSet) {
		return authRoleElementOperationDao.getRoleElementOperationsByRoleIds(roleIdSet);
	}
}
