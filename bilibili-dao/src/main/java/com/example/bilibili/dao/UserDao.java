package com.example.bilibili.dao;

import com.example.bilibili.domain.User;
import com.example.bilibili.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;

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
}
