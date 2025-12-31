package com.crawler.douyin.model;

import lombok.Data;

/**
 * 视频信息模型类
 * 
 * @author crawler
 */
@Data
public class VideoInfo {
    /**
     * 视频ID
     */
    private String videoId;

    /**
     * 视频标题/描述
     */
    private String title;

    /**
     * 作者昵称
     */
    private String author;

    /**
     * 视频URL
     */
    private String videoUrl;

    /**
     * 点赞数
     */
    private Long likeCount;

    /**
     * 评论数
     */
    private Long commentCount;

    /**
     * 分享数
     */
    private Long shareCount;

    /**
     * 播放量
     */
    private Long playCount;

    /**
     * 创建时间戳
     */
    private Long createTime;
}

