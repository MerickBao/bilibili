package com.example.bilibili.api;

import com.example.bilibili.api.support.UserSupport;
import com.example.bilibili.domain.*;
import com.example.bilibili.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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

	/**
	 * 点赞视频
	 */
	@PostMapping("/video-likes")
	public JsonResponse<String> addVideoLike(@RequestParam Long videoId) {
		Long userId = userSupport.getCurrentUserId();
		videoService.addVideoLike(videoId, userId);
		return JsonResponse.success();
	}

	/**
	 * 取消点赞视频
	 */
	@DeleteMapping("/video-likes")
	public JsonResponse<String> deleteVideoLike(@RequestParam Long videoId) {
		Long userId = userSupport.getCurrentUserId();
		videoService.deleteVideoLike(videoId, userId);
		return JsonResponse.success();
	}

	/**
	 * 查询视频点赞数量
	 */
	@GetMapping("/video-likes")
	public JsonResponse<Map<String, Object>> getVideoLikes(@RequestParam Long videoId) {
		Long userId = null;
		try {
			userId = userSupport.getCurrentUserId();
		} catch (Exception ignored) {
		}
		Map<String, Object> result = videoService.getVideoLikes(videoId, userId);
		return new JsonResponse<>(result);
	}

	/**
	 * 收藏视频
	 */
	@PostMapping("/video-collections")
	public JsonResponse<String> addVideoCollection(@RequestBody VideoCollection videoCollection) {
		Long userId = userSupport.getCurrentUserId();
		videoService.addVideoCollection(videoCollection, userId);
		return JsonResponse.success();
	}

	/**
	 * 取消收藏视频
	 */
	@DeleteMapping("/video-collections")
	public JsonResponse<String> deleteVideoCollection(@RequestParam Long videoId) {
		Long userId = userSupport.getCurrentUserId();
		videoService.deleteVideoCollection(videoId, userId);
		return JsonResponse.success();
	}

	/**
	 * 查询视频收藏数量
	 */
	@GetMapping("/video-collections")
	public JsonResponse<Map<String, Object>> getVideoCollections(@RequestParam Long videoId) {
		Long userId = null;
		try {
			userId = userSupport.getCurrentUserId();
		} catch (Exception ignored) {
		}
		Map<String, Object> result = videoService.getVideoCollections(videoId, userId);
		return new JsonResponse<>(result);
	}

	/**
	 * 视频投币
	 */
	@PostMapping("/video-coins")
	public JsonResponse<String> addVideoCoins(@RequestBody VideoCoin videoCoin) {
		Long userId = userSupport.getCurrentUserId();
		videoService.addVideoCoins(videoCoin, userId);
		return JsonResponse.success();
	}

	/**
	 * 查询视频投币数量
	 */
	@GetMapping("/video-coins")
	public JsonResponse<Map<String, Object>> getVideoCoins(@RequestParam Long videoId) {
		Long userId = null;
		try {
			userId = userSupport.getCurrentUserId();
		} catch (Exception ignored) {
		}
		Map<String, Object> result = videoService.getVideoCoins(videoId, userId);
		return new JsonResponse<>(result);
	}

	/**
	 * 添加视频评论
	 */
	@PostMapping("/video-comments")
	public JsonResponse<String> addVideoComment(@RequestBody VideoComment videoComment) {
		Long userId = userSupport.getCurrentUserId();
		videoService.addVideoComment(videoComment, userId);
		return JsonResponse.success();
	}

	/**
	 * 分页查询视频评论
	 */
	@GetMapping("/video-comments")
	public JsonResponse<PageResult<VideoComment>> pageListVideoComments(@RequestParam Integer size,
	                                                                    @RequestParam Integer no,
	                                                                    @RequestParam Long videoId) {
		PageResult<VideoComment> result = videoService.pageListVideoComments(size, no, videoId);
		return new JsonResponse<>(result);
	}

	/**
	 * 获取视频详情
	 */
	@GetMapping("/video-details")
	public JsonResponse<Map<String, Object>> getVideoDetails(@RequestParam Long videoId){
		Map<String, Object> result = videoService.getVideoDetails(videoId);
		return new JsonResponse<>(result);
	}

}
