package com.example.bilibili.dao;

import com.example.bilibili.domain.Video;
import com.example.bilibili.domain.VideoTag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-25 10:44
 * @Description: 视频操作DAO
 */

@Mapper
public interface VideoDao {

	Integer addVideos(Video video);

	Integer batchAddVideoTags(List<VideoTag> videoTagList);

	Integer pageCountVideos(Map<String, Object> params);

	List<Video> pageListVideos(Map<String, Object> params);
}
