package com.example.bilibili.service;

import com.example.bilibili.dao.FollowingGroupDao;
import com.example.bilibili.domain.FollowingGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-18 11:05
 * @Description:
 */

@Service
public class FollowingGroupService {

    @Autowired
	private FollowingGroupDao followingGroupDao;

	public FollowingGroup getByType(String type) {
		return followingGroupDao.getByType(type);
	}

	public FollowingGroup getById(Long id) {
		return followingGroupDao.getById(id);
	}

	public List<FollowingGroup> getByUserId(Long userId) {
		return followingGroupDao.getByUserId(userId);
	}
}
