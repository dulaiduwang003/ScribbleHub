package com.cn.app.superbot.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * The type Super creation.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Data
@TableName(value = "super_creation")
@Accessors(chain = true)
public class SuperCreation {

    /**
     * The Id.
     */
    @TableId(type = IdType.INPUT)
    private Long id;
    /**
     * The User id.
     */
    private Long userId;
    /**
     * The Prompt.
     */
    private String prompt;
    /**
     * The Url.
     */
    private String url;

    /**
     * The Recommend.
     */
    private Long recommend;
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
