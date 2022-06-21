package com.example.bilibili.dao;

import com.example.bilibili.domain.auth.AuthRoleElementOperation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-21 22:28
 * @Description:
 */

@Mapper
public interface AuthRoleElementOperationDao {
    List<AuthRoleElementOperation> getRoleElementOperationsByRoleIds( @Param("roleIdSet") Set<Long> roleIdSet);
}
