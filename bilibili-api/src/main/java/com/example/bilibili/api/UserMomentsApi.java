package com.example.bilibili.api;

import com.example.bilibili.api.support.UserSupport;
import com.example.bilibili.domain.JsonResponse;
import com.example.bilibili.domain.UserMoment;
import com.example.bilibili.service.UserMomentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-21 10:42
 * @Description: 用户动态接口
 */

@RestController
public class UserMomentsApi {

	@Autowired
    private UserMomentsService userMomentsService;

	@Autowired
	private UserSupport userSupport;

	// 新增用户动态
	@PostMapping("/user-moments")
	public JsonResponse<String> addUserMoments(@RequestBody UserMoment userMoment) throws Exception {
		Long userId = userSupport.getCurrentUserId();
		userMoment.setUserId(userId);
		userMomentsService.addUserMoments(userMoment);
		return JsonResponse.success();
	}

	// 获取关注者的用户动态
	@GetMapping("/user-subscribed-moments")
	public JsonResponse<List<UserMoment>> getUserSubscribedMoments() {
		Long userId = userSupport.getCurrentUserId();
		List<UserMoment> list = userMomentsService.getUserSubscribedMoments(userId);
		return new JsonResponse<>(list);
	}
}
