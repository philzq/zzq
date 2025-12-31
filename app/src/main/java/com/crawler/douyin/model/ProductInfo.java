package com.crawler.douyin.model;

import lombok.Data;

/**
 * 商品信息模型类
 * 
 * @author crawler
 */
@Data
public class ProductInfo {
    /**
     * 商品ID
     */
    private String productId;

    /**
     * 商品名称/标题
     */
    private String title;

    /**
     * 商品价格
     */
    private String price;

    /**
     * 商品原价（如果有）
     */
    private String originalPrice;

    /**
     * 商品图片URL
     */
    private String imageUrl;

    /**
     * 商品链接
     */
    private String productUrl;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 商品评价数
     */
    private Long commentCount;

    /**
     * 商品好评率
     */
    private String goodRate;

    /**
     * 商品销量
     */
    private String salesVolume;

    /**
     * 商品评分
     */
    private String rating;

    /**
     * 商品分类
     */
    private String category;

    /**
     * 是否自营
     */
    private Boolean isSelfOperated;
}

