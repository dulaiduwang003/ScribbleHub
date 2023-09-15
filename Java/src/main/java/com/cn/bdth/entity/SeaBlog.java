package com.cn.bdth.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 文章实体类
 *
 * @author 时间海 @github dulaiduwang003
 */
@Data
@TableName(value = "sea_blog")
@Accessors(chain = true)
public class SeaBlog {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long seaBlogId;

    /**
     * 所属人用户ID
     */
    private Long seaUserId;

    /**
     * 所属专题
     */
    private Long seaClassifyId;


    /**
     * 文章封面图片 ID
     */
    private Long seaResourceId;

    /**
     * 文章标题
     */
    private String title;


    /**
     * 是否为推荐博客
     */
    private Long isRecommend;

    /**
     * 文章摘要
     */
    private String summary;


    /**
     * 文章内容
     */
    private String content;


    /**
     * 文章标签
     */
    private String label;


    /**
     * 文章
     */
    private String fileResourceIds;


    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;


    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
