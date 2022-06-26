package com.example.bilibili.domain;

import java.util.Date;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-25 10:50
 * @Description: 视频标签实体类
 */

public class VideoTag {

    private Long id;

    private Long videoId;

    private Long tagId;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
