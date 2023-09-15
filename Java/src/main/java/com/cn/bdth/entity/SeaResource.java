package com.cn.bdth.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 资源实体类
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@TableName(value = "sea_resource")
@Accessors(chain = true)
public class SeaResource {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long seaResourceId;
    /**
     * 物理路径
     */
    private String target;

    /**
     * 类型
     */
    private String type;

    /**
     * 访问链接
     */
    private String uri;
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
