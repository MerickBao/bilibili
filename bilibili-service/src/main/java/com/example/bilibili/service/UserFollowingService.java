package com.example.bilibili.service;

import com.example.bilibili.dao.UserFollowingDao;
import com.example.bilibili.domain.FollowingGroup;
import com.example.bilibili.domain.User;
import com.example.bilibili.domain.UserFollowing;
import com.example.bilibili.domain.UserInfo;
import com.example.bilibili.domain.constant.UserConstant;
import com.example.bilibili.service.exception.ConditionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-18 11:04
 * @Description: 用户关注服务层逻辑实现
 */

@Service
public class UserFollowingService {

	@Autowired
	private UserFollowingDao userFollowingDao;

	@Autowired
	private FollowingGroupService followingGroupService;

	@Autowired
	private UserService userService;

	// 添加新的关注
	@Transactional
	public void addUserFollowings(UserFollowing userFollowing) {
		Long groupId = userFollowing.getGroupId();
		if (groupId == null) {
			FollowingGroup followingGroup = followingGroupService.getByType(UserConstant.USER_FOLLOWING_GROUP_TYPE_DEFAULT);
			userFollowing.setGroupId(followingGroup.getId());
		} else {
			FollowingGroup followingGroup = followingGroupService.getById(groupId);
			if (followingGroup == null) {
				throw new ConditionException("关注分组不存在！");
			}
		}
		Long followingId = userFollowing.getFollowingId();
		User user = userService.getUserById(followingId);
		if (user == null) {
			throw new ConditionException("关注的用户不存在！");
		}
		userFollowingDao.deleteUserFollowing(userFollowing.getUserId(), followingId);
		userFollowing.setCreateTime(new Date());
		userFollowingDao.addUserFollowing(userFollowing);
	}

	// 获取用户的关注列表
	public List<FollowingGroup> getUserFollowings(Long userId) {
		// 获取该用户关注的所有用户
		List<UserFollowing> list = userFollowingDao.getUserFollowings(userId);
		// 得到上述被关注用户的所有的id
		Set<Long> followingIdSet = list.stream().map(UserFollowing::getFollowingId).collect(Collectors.toSet());
		List<UserInfo> userInfoList = new ArrayList<>();
		if (!followingIdSet.isEmpty()) {
			userInfoList = userService.getUserInfoByUserIds(followingIdSet);
		}
		// 加入被关注者的用户基本信息
		for (UserFollowing userFollowing : list) {
			for (UserInfo userInfo : userInfoList) {
				if (userFollowing.getFollowingId().equals(userInfo.getUserId())) {
					userFollowing.setUserInfo(userInfo);
				}
			}
		}

		// 查询该用户的关注分组信息
		List<FollowingGroup> groupList = followingGroupService.getByUserId(userId);

		//  全部关注列表中的信息
		FollowingGroup allGroup = new FollowingGroup();
		allGroup.setName(UserConstant.USER_FOLLOWING_GROUP_ALL_NAME);
		allGroup.setFollowingUserInfoList(userInfoList);

		List<FollowingGroup> result = new ArrayList<>();
		result.add(allGroup);

		// 将被关注者按照分组分类
		for (FollowingGroup group : groupList) {
			List<UserInfo> infoList = new ArrayList<>();
			for (UserFollowing userFollowing : list) {
				if (group.getId().equals(userFollowing.getGroupId())) {
					infoList.add(userFollowing.getUserInfo());
				}
			}
			group.setFollowingUserInfoList(infoList);
			result.add(group);
		}

		return result;
	}

	// 获取用户的粉丝
	public List<UserFollowing> getUserFans(Long userId) {
		// 获取粉丝列表
		List<UserFollowing> fanList = userFollowingDao.getUserFans(userId);
		Set<Long> fanIdSet = fanList.stream().map(UserFollowing::getUserId).collect(Collectors.toSet());
		// 粉丝的用户信息
		List<UserInfo> userInfoList = new ArrayList<>();
		if (fanIdSet.size() > 0) {
			userInfoList = userService.getUserInfoByUserIds(fanIdSet);
		}
		// 该用户的关注列表
		List<UserFollowing> followingList = userFollowingDao.getUserFollowings(userId);

		for (UserFollowing fan : fanList) {
			// 添加粉丝的用户信息
			for (UserInfo userInfo : userInfoList) {
				if (fan.getUserId().equals(userInfo.getUserId())) {
					userInfo.setFollowed(false);
					fan.setUserInfo(userInfo);
				}
			}
			// 判断是否为互粉状态
			for (UserFollowing following : followingList) {
				if (fan.getUserId().equals(following.getFollowingId())) {
					fan.getUserInfo().setFollowed(true);
				}
			}
		}

		return fanList;
	}

	public Long addUserFollowingGroups(FollowingGroup followingGroup) {
		followingGroup.setCreateTime(new Date());
		followingGroup.setType(UserConstant.USER_FOLLOWING_GROUP_TYPE_USER);
		followingGroupService.addUserFollowingGroups(followingGroup);
		return followingGroup.getId();
	}

	public List<FollowingGroup> getUserFollowingGroups(Long userId) {
		return followingGroupService.getUserFollowingGroups(userId);
	}

	public List<UserInfo> checkFollowingStatus(List<UserInfo> userInfoList, Long userId) {
		List<UserFollowing> userFollowingList = userFollowingDao.getUserFollowings(userId);
		for (UserInfo userInfo : userInfoList) {
			userInfo.setFollowed(false);
			for (UserFollowing userFollowing : userFollowingList) {
				if (userFollowing.getFollowingId().equals(userInfo.getUserId())) {
					userInfo.setFollowed(true);
				}
			}
		}
		return userInfoList;
	}
}
