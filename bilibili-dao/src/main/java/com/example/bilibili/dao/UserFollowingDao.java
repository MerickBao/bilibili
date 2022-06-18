package com.example.bilibili.dao;

import com.example.bilibili.domain.FollowingGroup;
import com.example.bilibili.domain.UserFollowing;
import com.sun.org.glassfish.gmbal.ParameterNames;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-18 11:02
 * @Description: 用户关注
 */

@Mapper
public interface UserFollowingDao {

	Integer deleteUserFollowing(@Param("userId") Long userId, @Param("followingId") Long followingId);

	Integer addUserFollowing(UserFollowing userFollowing);

	List<UserFollowing> getUserFollowings(Long userId);

	List<UserFollowing> getUserFans(Long userId);
}
