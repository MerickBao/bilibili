package com.example.bilibili.api;

import com.example.bilibili.api.support.UserSupport;
import com.example.bilibili.domain.JsonResponse;
import com.example.bilibili.domain.PageResult;
import com.example.bilibili.domain.Video;
import com.example.bilibili.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-25 10:39
 * @Description: 视频相关API
 */

@RestController
public class VideoApi {

	@Autowired
	private VideoService videoService;

	@Autowired
	private UserSupport userSupport;
	// 8f16ff1b9443b1950cf42374877c4309
	// M00/00/00/UUdVxWK2y0OEKQA5AAAAAPVOpsU887.mp4

	// 上传视频
	@PostMapping("/videos")
	public JsonResponse<String> addVideos(@RequestBody Video video) {
		Long userId = userSupport.getCurrentUserId();
		video.setUserId(userId);
		videoService.addVideos(video);
		return JsonResponse.success();
	}

	// 分页查询视频
	@GetMapping("/videos")
	public JsonResponse<PageResult<Video>> pageListVideos(Integer size, Integer no, String area) {
		PageResult<Video> result = videoService.pageListVideos(size, no, area);
		return new JsonResponse<>(result);
	}

	// 视频在线观看
	@GetMapping("/video-slices")
	public void viewVideoOnlineBySlices(HttpServletRequest request,
	                                    HttpServletResponse response,
	                                    String url) throws Exception {
		videoService.viewVideoOnlineBySlices(request, response, url);
	}

}
