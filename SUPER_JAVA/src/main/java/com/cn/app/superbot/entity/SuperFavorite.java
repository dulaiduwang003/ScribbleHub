package com.cn.app.superbot.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * The type Super favorite.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Data
@TableName(value = "super_favorite")
@Accessors(chain = true)
public class SuperFavorite {
    /**
     * The's.
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * The SuperUser id.
     */
    private Long userId;
 ;
    /**
     * The SuperUser dialogue.
     */
    private String userDialogue;
    /**
     * The Ai dialogue.
     */
    private String aiDialogue;
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
    private long del;


}
