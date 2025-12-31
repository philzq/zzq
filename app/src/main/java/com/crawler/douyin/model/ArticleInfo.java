package com.crawler.douyin.model;

import lombok.Data;

/**
 * 文章信息模型类
 * 
 * @author crawler
 */
@Data
public class ArticleInfo {
    /**
     * 文章ID
     */
    private String articleId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 作者名称
     */
    private String author;

    /**
     * 文章摘要/内容预览
     */
    private String summary;

    /**
     * 文章URL
     */
    private String articleUrl;

    /**
     * 阅读数
     */
    private Long readCount;

    /**
     * 评论数
     */
    private Long commentCount;

    /**
     * 点赞数
     */
    private Long likeCount;

    /**
     * 分享数
     */
    private Long shareCount;

    /**
     * 发布时间
     */
    private String publishTime;

    /**
     * 文章分类/标签
     */
    private String category;
}

