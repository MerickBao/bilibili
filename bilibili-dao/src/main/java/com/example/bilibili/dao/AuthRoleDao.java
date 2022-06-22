package com.example.bilibili.dao;

import com.example.bilibili.domain.auth.AuthRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-22 19:36
 * @Description: 权限控制--角色表
 */

@Mapper
public interface AuthRoleDao {
	AuthRole getRoleByCode(String code);
}
