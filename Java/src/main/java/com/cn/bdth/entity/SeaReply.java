package com.cn.bdth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 回复实体类
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@TableName(value = "sea_reply")
@Accessors(chain = true)
public class SeaReply {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long seaReplyId;

    /**
     * 所属评论ID
     */
    private Long seaCommentId;

    /**
     * 回复人ID
     */
    private Long reciprocityId;

    /**
     * 所属人ID
     */
    private Long seaUserId;

    /**
     * 回复内容
     */
    private String replyContent;

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
