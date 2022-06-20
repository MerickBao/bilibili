package com.example.bilibili.api;

import com.example.bilibili.api.support.UserSupport;
import com.example.bilibili.domain.FollowingGroup;
import com.example.bilibili.domain.JsonResponse;
import com.example.bilibili.domain.UserFollowing;
import com.example.bilibili.service.UserFollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-18 17:26
 * @Description: 用户关注相关接口
 */

@RestController
public class UserFollowingApi {

	@Autowired
	private UserFollowingService userFollowingService;

	@Autowired
	private UserSupport userSupport;

	// 添加一个新的关注
	@PostMapping("/user-followings")
	public JsonResponse<String> addUserFollowings(@RequestBody UserFollowing userFollowing) {
		Long userId = userSupport.getCurrentUserId();
		userFollowing.setUserId(userId);
		userFollowingService.addUserFollowings(userFollowing);
		return JsonResponse.success();
	}

	// 获取用户关注列表
	@GetMapping("/user-followings")
	public JsonResponse<List<FollowingGroup>> getUserFollowings() {
		Long userId = userSupport.getCurrentUserId();
		List<FollowingGroup> result = userFollowingService.getUserFollowings(userId);
		return new JsonResponse<>(result);
	}

	// 获取用户粉丝
	@GetMapping("/user-fans")
	public JsonResponse<List<UserFollowing>> getUserFans() {
		Long userId = userSupport.getCurrentUserId();
		List<UserFollowing> result = userFollowingService.getUserFans(userId);
		return new JsonResponse<>(result);
	}

	// 新建用户关注分组
	@PostMapping("/user-following-groups")
	public JsonResponse<Long> addUserFollowingGroups(@RequestBody FollowingGroup followingGroup) {
		Long userId = userSupport.getCurrentUserId();
		followingGroup.setUserId(userId);
		Long groupId = userFollowingService.addUserFollowingGroups(followingGroup);
		return new JsonResponse<>(groupId);
	}

	// 获取用户关注分组
	@GetMapping("/user-following-groups")
	public  JsonResponse<List<FollowingGroup>> getUserFollowingGroups() {
		Long userId = userSupport.getCurrentUserId();
		List<FollowingGroup> groupList = userFollowingService.getUserFollowingGroups(userId);
		return new JsonResponse<>(groupList);
	}
}
