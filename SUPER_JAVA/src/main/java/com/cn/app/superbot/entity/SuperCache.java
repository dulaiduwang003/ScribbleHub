package com.cn.app.superbot.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@TableName(value = "super_cache")
@Accessors(chain = true)
public class SuperCache {

    /**
     * The'd.
     */
    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * The Mach.
     */
    private String mach;

    /**
     * The second word.
     */
    private String secondWord;

    /**
     * The SuperUser id.
     */
    private Long userId;
    /**
     * The Content.
     */
    private String content;
    /**
     * The Created time.
     */
    private LocalDateTime createdTime;
    /**
     * The Update time.
     */
    private LocalDateTime updateTime;
    /**
     * The Del.
     */
    private Long del;


}
