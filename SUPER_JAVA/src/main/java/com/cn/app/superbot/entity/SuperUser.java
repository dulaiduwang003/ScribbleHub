package com.cn.app.superbot.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * The type SuperUser.
 */
@Data
@TableName(value = "super_user")
@Accessors(chain = true)
public class SuperUser {

    /**
     * The's.
     */
    @TableId(type = IdType.INPUT)
    private Long id;
    /**
     * The Email.
     */
    private String email;
    /**
     * The Password.
     */
    private String password;
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
