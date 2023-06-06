package com.cn.app.superbot.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type User order page vo.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Data
@Accessors(chain = true)
public class AdminOrderPageVo implements Serializable {

    /**
     * The Id.
     */
    private String id;

    /**
     * The Product name.
     */
    private String productName;

    /**
     * The Created time.
     */
    private LocalDateTime createdTime;

    /**
     * The Created time.
     */
    private LocalDateTime payTime;

    /**
     * The Created time.
     */
    private String reasonFailure;

    /**
     * The Price.
     */
    private Double productPrice;

    /**
     * The State.
     */
    private Long state;


    /**
     * The Total amount.
     */
    private Double totalAmount;


}
