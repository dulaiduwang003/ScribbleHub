package com.cn.app.superbot.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * The type Super pricing.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Data
@TableName(value = "super_product")
@Accessors(chain = true)
public class SuperProduct {

    /**
     * The Id.
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * The Name.
     */
    private String name;
    /**
     * The Frequency.
     */
    private Long frequency;
    /**
     * The Price.
     */
    private Double price;

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
