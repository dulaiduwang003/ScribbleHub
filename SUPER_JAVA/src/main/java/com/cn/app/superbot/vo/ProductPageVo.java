package com.cn.app.superbot.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * The type Product page vo.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Data
public class ProductPageVo {


    /**
     * The Id.
     */
    private Long id;
    /**
     * The Frequency.
     */
    private Long frequency;

    /**
     * The Name.
     */
    private String name;


    /**
     * The Created time.
     */
    private LocalDateTime createdTime;

    /**
     * The Price.
     */
    private BigDecimal price;


}
