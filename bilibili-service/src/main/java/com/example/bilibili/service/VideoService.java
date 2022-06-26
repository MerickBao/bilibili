package com.example.bilibili.service;

import com.example.bilibili.dao.VideoDao;
import com.example.bilibili.domain.PageResult;
import com.example.bilibili.domain.Video;
import com.example.bilibili.domain.VideoTag;
import com.example.bilibili.service.exception.ConditionException;
import com.example.bilibili.service.util.FastDFSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-25 10:43
 * @Description: 视频操作服务层
 */

@Service
public class VideoService {

	@Autowired
	private VideoDao videoDao;

	@Autowired
	private FastDFSUtil fastDFSUtil;

	@Transactional
	public void addVideos(Video video) {
		Date now = new Date();
		video.setCreateTime(now);
		// 将视频信息存储到数据库
		videoDao.addVideos(video);
		Long videoId = video.getId();
		// 获取视频的标签
		List<VideoTag> tagList = video.getVideoTagList();
		// 将tag和视频绑定
		if (tagList != null) {
			tagList.forEach(item -> {
				item.setCreateTime(now);
				item.setVideoId(videoId);
			});
			// 为视频添加tag
			videoDao.batchAddVideoTags(tagList);
		}
	}

	public PageResult<Video> pageListVideos(Integer size, Integer no, String area) {
		if (size == null || no == null) {
			throw new ConditionException("参数异常！");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("start", (no - 1) * size);
		params.put("limit", size);
		params.put("area", area);
		List<Video> list = new ArrayList<>();
		// 计算满足查询条件的视频总共有多少条
		Integer total = videoDao.pageCountVideos(params);
		if (total > 0) {
			list = videoDao.pageListVideos(params);
		}
		return new PageResult<>(total, list);
	}

	public void viewVideoOnlineBySlices(HttpServletRequest request,
	                                    HttpServletResponse response,
	                                    String url) throws Exception {
		fastDFSUtil.viewVideoOnlineBySlices(request, response, url);
	}
}
