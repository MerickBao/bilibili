package com.example.bilibili.dao;

import com.example.bilibili.domain.auth.AuthRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-21 22:46
 * @Description:
 */

@Mapper
public interface AuthRoleMenuDao {

    List<AuthRoleMenu> getAuthRoleMenusByRoleIds(@Param("roleIdSet") Set<Long> roleIdSet);
}
