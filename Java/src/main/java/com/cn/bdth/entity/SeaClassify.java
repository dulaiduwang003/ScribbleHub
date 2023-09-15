package com.cn.bdth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 专题实体类
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@TableName(value = "sea_classify")
@Accessors(chain = true)
public class SeaClassify {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long seaClassifyId;
    /**
     * 专题名称
     */
    private String classifyName;
    /**
     * 专题封面
     */
    private Long seaResourceId;

    /**
     * 专题类型
     */
    private Long isType;

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
