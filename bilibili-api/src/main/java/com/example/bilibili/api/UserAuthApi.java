package com.example.bilibili.api;

import com.example.bilibili.api.support.UserSupport;
import com.example.bilibili.domain.JsonResponse;
import com.example.bilibili.domain.auth.UserAuthorities;
import com.example.bilibili.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-21 21:47
 * @Description: 用户权限控制相关接口
 */

@RestController
public class UserAuthApi {

	@Autowired
	private UserSupport userSupport;

	@Autowired
	private UserAuthService userAuthService;

	@GetMapping("/user-authorities")
	public JsonResponse<UserAuthorities> getUserAuthorities() {
		Long userId = userSupport.getCurrentUserId();
		UserAuthorities userAuthorities = userAuthService.getUserAuthorities(userId);
		return new JsonResponse<>(userAuthorities);
	}
}
