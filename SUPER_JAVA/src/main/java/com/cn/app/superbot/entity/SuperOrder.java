package com.cn.app.superbot.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * The type Super order.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Data
@TableName(value = "super_order")
@Accessors(chain = true)
public class SuperOrder {

    /**
     * The Id.
     */
    @TableId(type = IdType.INPUT)
    private String id;

    /**
     * The User id.
     */
    private Long userId;

    /**
     * The Product id.
     */
    private Long productId;

    /**
     * The Product name.
     */
    private String productName;

    /**
     * The Product price.
     */
    private Double productPrice;

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
     * The Frequency.
     */
    private Long frequency;

    /**
     * The State.
     */
    private Long state;

    /**
     * The Reason failure.
     */
    private String reasonFailure;

    /**
     * The Pay time.
     */
    private LocalDateTime payTime;
    /**
     * The Del.
     */
    private Long del;


}
