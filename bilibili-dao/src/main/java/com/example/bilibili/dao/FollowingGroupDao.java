package com.example.bilibili.dao;

import com.example.bilibili.domain.FollowingGroup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-18 11:02
 * @Description: 关注列表
 */

@Mapper
public interface FollowingGroupDao {
	FollowingGroup getByType(String type);

	FollowingGroup getById(Long id);

	List<FollowingGroup> getByUserId(Long userId);

	Long addUserFollowingGroups(FollowingGroup followingGroup);

	List<FollowingGroup> getUserFollowingGroups(Long userId);
}
