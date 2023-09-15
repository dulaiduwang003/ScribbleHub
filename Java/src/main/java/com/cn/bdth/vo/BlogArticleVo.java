package com.cn.bdth.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type Popular blogs' vo.
 * 推荐博客视图(首页轮播图)
 */
@Data
@Accessors(chain = true)
public class BlogArticleVo implements Serializable {

    /**
     * The Title.
     * 文章标题
     */
    private String title;

    /**
     * The Content.
     * 文章内容
     */
    private String content;

    /**
     * The Seat label.
     * 文章标签
     */
    private String label;


    /**
     * The Classify name.
     */
    private String classifyName;

    /**
     * The Articles.
     */
    private Long articles;


    /**
     * The Classify id.
     */
    private Long seaClassifyId;

    /**
     * The Created time.
     * 创建时间
     */
    private LocalDateTime createdTime;

}
