package com.example.bilibili.dao;

import com.example.bilibili.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

	Video getVideoById(Long videoId);

	VideoLike getVideoLikeByVideoIdAndUserId(@Param("videoId") Long videoId, @Param("userId") Long userId);

	Integer addVideoLike(VideoLike videoLike);

	Integer deleteVideoLike(@Param("videoId") Long videoId,
	                        @Param("userId") Long userId);

	Long getVideoLikes(Long videoId);

	Integer deleteVideoCollection(@Param("videoId") Long videoId,
	                              @Param("userId") Long userId);

	Integer addVideoCollection(VideoCollection videoCollection);

	Long getVideoCollections(Long videoId);

	VideoCollection getVideoCollectionByVideoIdAndUserId(@Param("videoId") Long videoId,
	                                                     @Param("userId") Long userId);

	VideoCoin getVideoCoinByVideoIdAndUserId(@Param("videoId") Long videoId,
	                                         @Param("userId") Long userId);

	Integer addVideoCoin(VideoCoin videoCoin);

	Integer updateVideoCoin(VideoCoin videoCoin);

	Long getVideoCoinsAmount(Long videoId);

	Integer addVideoComment(VideoComment videoComment);

	Integer pageCountVideoComments(Map<String, Object> params);

	List<VideoComment> pageListVideoComments(Map<String, Object> params);

	List<VideoComment> batchGetVideoCommentsByRootIds(@Param("rootIdList") List<Long> rootIdList);

	Video getVideoDetails(Long videoId);
}
