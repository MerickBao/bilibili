package com.example.bilibili.dao;

import com.alibaba.fastjson.JSONObject;
import com.example.bilibili.domain.RefreshTokenDetail;
import com.example.bilibili.domain.User;
import com.example.bilibili.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-15 21:30
 * @Description: 用户表 方法接口
 */

@Mapper
public interface UserDao {

	User getUserByPhone(String phone);

	Integer addUser(User user);

	Integer addUserInfo(UserInfo userInfo);

	User getUserById(Long userId);

	UserInfo getUserInfoByUserId(Long userId);

	void updateUserInfos(UserInfo userInfo);

	void updateUsers(User user);

	List<UserInfo> getUserInfoByUserIds(Set<Long> userIdList);

	Integer pageCountUserInfos(Map<String, Object> params);

	List<UserInfo> pageListUserInfos(Map<String, Object> params);

	Integer deleteRefreshToken(@Param("refreshToken") String refreshToken,
	                           @Param("userId") Long userId);

	Integer addRefreshToken(@Param("refreshToken") String refreshToken,
	                        @Param("userId") Long userId,
	                        @Param("createTime") Date date);

	RefreshTokenDetail getRefreshTokenDetail(String refreshToken);

	Integer deleteRefreshTokenByUserId(Long userId);

	List<UserInfo> batchGetUserInfoByUserIds(Set<Long> userIdList);
}
