package com.cn.bdth.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 评论实体类
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@TableName(value = "sea_comment")
@Accessors(chain = true)
public class SeaComment {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long seaCommentId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 所属用户
     */
    private Long seaUserId;


    /**
     * 所属文章id
     */
    private Long seaBlogId;

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
