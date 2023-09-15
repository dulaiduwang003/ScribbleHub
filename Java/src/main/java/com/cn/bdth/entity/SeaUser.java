package com.cn.bdth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户实体类
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@TableName(value = "sea_user")
@Accessors(chain = true)
public class SeaUser {

    /**
     * 主键
     */
    @TableId(type = IdType.INPUT)
    private Long seaUserId;
    /**
     * 微信用户身份标识
     */
    private String openId;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private Long seaResourceId;


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
