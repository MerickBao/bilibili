package com.example.bilibili.dao;

import com.example.bilibili.domain.auth.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-21 22:06
 * @Description: 用户角色DAO
 */

@Mapper
public interface UserRoleDao {

    public List<UserRole> getUserRoleByUserId(Long userId);

	void addUserRole(UserRole userRole);
}
