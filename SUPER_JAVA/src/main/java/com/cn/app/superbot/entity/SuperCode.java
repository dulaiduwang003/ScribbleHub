package com.cn.app.superbot.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * The type SuperCode.
 */
@Data
@TableName(value = "super_code")
@Accessors(chain = true)
public class SuperCode {


    /**
     * The Id.
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * The SuperCode.
     */
    private String code;
    /**
     * The Frequency.
     */
    private Long frequency;
    /**
     * The Created time.
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * The Update time.
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * The Del.
     */
    private Long del;


}
