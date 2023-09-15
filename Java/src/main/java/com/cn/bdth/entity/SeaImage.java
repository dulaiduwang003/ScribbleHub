package com.cn.bdth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 绘图实体类
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@TableName(value = "sea_image")
@Accessors(chain = true)
public class SeaImage {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long seaImageId;
    /**
     * 所属用户ID
     */
    private Long seaUserId;
    /**
     * 关键词
     */
    private String prompt;
    /**
     * 原图ID
     */
    private Long originalId;
    /**
     * 生成图ID
     */
    private Long generateId;

    /**
     * 是否公开
     */
    private Long isPublic;


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
