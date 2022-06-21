package com.example.bilibili.dao;

import com.example.bilibili.domain.UserMoment;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-21 10:45
 * @Description: 用户动态DAO层
 */

@Mapper
public interface UserMomentsDao {

	void addUserMoments(UserMoment userMoment);
}
